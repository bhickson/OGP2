if (typeof OpenGeoportal === 'undefined') {
	OpenGeoportal = {};
} else if (typeof OpenGeoportal !== "object") {
	throw new Error("OpenGeoportal already exists and is not an object");
}

if (typeof OpenGeoportal.Models === 'undefined') {
	OpenGeoportal.Models = {};
} else if (typeof OpenGeoportal.Models !== "object") {
	throw new Error("OpenGeoportal.Models already exists and is not an object");
}
/*
 * OpenGeoportal.LayerSettings object to hold display setting info, where it
 * exists (opacity, etc.)
 */

/*
 * 
 * 
 * Attributes: - resourceName: this is the layer name value needed to preview
 * the layer. Unfortunately, this may be different from the solr "Name" value,
 * which should be the layer name used to access ogc web services (ex: Harvard's
 * name value to access their tilecache does not include the database prefixes,
 * while ogc layer names (GeoServer) do.)
 * 
 * 
 */

OpenGeoportal.Models.PreviewLayer = OpenGeoportal.Models.ProtocolAware.extend({
	// preview controls are available according to what attributes this
	// model has
	// previewType determines what function is used to preview the layer
	defaults : {
		preview : "off",
		resourceName : "",
		previewType : "",
		showControls : false
	// panel is hidden by default
	},

	// preview types:
	// wms, arcgis, tilecache w/wms, tilecache w/out wms (essentially wmts,
	// right?), imageCollection,
	// browseGraphic, previewUrl
	supportedAttributesByType : [ {
		type : "wms",
		discriminator : "layer_geom_type_s",
		attributes : {
			raster : {
				getFeature : false,
				opacity : 100,
				sld : ""
			},

			"paper map" : {
				opacity : 100
			},
			// it's understood that point, line, polygon, are vector types
			point : {
				getFeature : false,
				opacity : 100,
				colorPickerOn : false,
				sld : "",
				color : "#ff0000",
				graphicWidth : 2
			},
			line : {
				getFeature : false,
				opacity : 100,
				colorPickerOn : false,
				sld : "",
				color : "#0000ff",
				graphicWidth : 2
			},
			polygon : {
				getFeature : false,
				colorPickerOn : false,
				sld : "",
				opacity : 80,
				color : "#003300",
				graphicWidth : 2 
			},
			"undefined" : {
				getFeature : false,
				opacity : 100,
				colorPickerOn : false,
				sld : "",
				color : "#aaaaaa",
				graphicWidth : 1
			}
		}
	}, {
		type : "tilecache",
		attributes : {
			opacity : 100
		}
	}, {
		type : "arcgisrest",
		attributes : {
			opacity : 100
		}
	} ],

	setPreviewType : function() {
		if (!this.has("dct_references_s")){
			return "noPreview";
		}
		var referencesObj = this.get("dct_references_s");
		
		if (_.isEmpty(referencesObj)){
			return "noPreview";
		}
		var previewType = "default";

		if (OpenGeoportal.Utility.hasLocationValueIgnoreCase(
				referencesObj,
				[ "http://www.opengis.net/def/serviceType/ogc/wms" ])) {
			previewType = "wms";
		} else if (OpenGeoportal.Utility.hasLocationValueIgnoreCase(
				referencesObj, [ "urn:x-esri:serviceType:ArcGIS#TiledMapLayer" ])) {
			previewType = "arcgisrest";  // FIXME
		} else if (OpenGeoportal.Utility.hasLocationValueIgnoreCase(
				referencesObj, [ "urn:x-esri:serviceType:ArcGIS#ImageMapLayer" ])) {
			previewType = "arcgisrest";  // FIXME
		} else if (OpenGeoportal.Utility.hasLocationValueIgnoreCase(
				referencesObj, [ "tilecache" ])) {
			// if we're here, the location field has a tilecache value, but no
			// wms value or arcgisrest value
			previewType = "tilecache";
		} else if (OpenGeoportal.Utility.hasLocationValueIgnoreCase(
				referencesObj, [ "imagecollection" ])) {
			// {"imageCollection": {"path": "furtwangler/17076013_03_028a.tif",
			// "url": "http://gis.lib.berkeley.edu:8080/geoserver/wms",
			// "collectionurl":
			// "http://www.lib.berkeley.edu/EART/mapviewer/collections/histoposf"}}
			previewType = "imagecollection";
		} else if (OpenGeoportal.Utility.hasLocationValueIgnoreCase(
				referencesObj, [ "externalLink" ])) {
			previewType = "externalLink";
		}


		this.set({
			previewType : previewType
		});

		return previewType;
	},

	assignAttributes : function() {
		// do some categorization
		var previewType = this.setPreviewType();
		var attr = this.getAttributesByType(previewType);
		this.set(attr);
	}
});

OpenGeoportal.Models.Attribute = Backbone.Model.extend({});

OpenGeoportal.Attributes = Backbone.Collection.extend({
	model : OpenGeoportal.Models.Attribute
});


OpenGeoportal.PreviewedLayers = Backbone.Collection.extend({
	model : OpenGeoportal.Models.PreviewLayer,
	initialize : function() {
		this.listenTo(this, "change:preview add", this.changePreview);
		this.listenTo(this, "change:graphicWidth change:color",
				this.changeLayerStyle);
		this.listenTo(this, "change:opacity", this.changeLayerOpacity);
		this.listenTo(this, "change:zIndex", this.changeZIndex);
		this.listenTo(this, "change:getFeature", this.changeGetFeatureState);

	},
	
	comparator: function(model1, model2){
		var getComparison = function(model){
			var comp = 0;
			if (model.has("zIndex")){
				comp = model.get("zIndex");
			}
			return comp;
		};
		
		var val1 = getComparison(model1);
		var val2 = getComparison(model2);
		if (val1 > val2){
			return -1;
		} else if (val2 > val1){
			return 1;
		} else {
			return 0;
		}	
	},
	
	changeLayerStyle : function(model, val, options) {
		var layerSlug = model.get("layer_slug_s");
		// tell map to change the linewidth/pointsize/borderwidth for this layer
		// this event should be attached to the model, so it only fires once;
		// better yet, have a map view that listens for this change event
		jQuery(document).trigger("map.styleChange", {
			layer_slug_s : layerSlug
		});
	},
	
	changeLayerOpacity : function(model, val, options) {
		var value = model.get("opacity");
		var layerSlug = model.get("layer_slug_s");
		// tell map to change the opacity for this layer
		jQuery(document).trigger("map.opacityChange", {
			layer_slug_s : layerSlug,
			opacity : value
		});
	},
	
	changeZIndex : function(model, val, options) {
		this.sort();

		var value = model.get("zIndex");
		var layerSlug = model.get("layer_slug_s");
		// tell map to change the zIndex for this layer
		jQuery(document).trigger("map.zIndexChange", {
			layer_slug_s : layerSlug,
			zIndex : value
		});
	},
	
	changeGetFeatureState : function(model, val, options) {
		var value = model.get("getFeature");
		var layerSlug = model.get("layer_slug_s");
		// tell map to change the getFeature status for this layer
		var mapEvent = null;
		if (value) {
			mapEvent = "map.getFeatureInfoOn";
			this.clearGetFeature(model); // passing a model to
			// clearGetFeature clears all other
			// gf
		} else {
			mapEvent = "map.getFeatureInfoOff";
		}
		jQuery(document).trigger(mapEvent, {
			layer_slug_s : layerSlug
		});
		
		this.checkGetFeatureState();
	},
	
	/**
	 * check to see if getFeature is turned on for any layers and fire
	 * appropriate event
	 */
	checkGetFeatureState : function(){
		var gfEvent = "map.attributeInfoOff";
		this.each(function(model){
			if (model.get("getFeature")){
				gfEvent = "map.attributeInfoOn";
				return;
			}
		});

		jQuery(document).trigger(gfEvent);


	},


	changePreview : function(model, val, options) {
		// console.log(arguments);
		var preview = model.get("preview");
		var layerSlug = model.get("layer_slug_s");
		if (preview === "on") {
			jQuery(document).trigger("previewLayerOn", {
				layer_slug_s : layerSlug
			});// show layer on map
		} else {

			jQuery(document).trigger("previewLayerOff", {
				layer_slug_s : layerSlug
			});
			// also set getFeature state to off.
			if (model.has("getFeature")) {
				model.set({
					getFeature : false
				});
			}
		}
	},

	isPreviewed : function(layerSlug) {
		var currModel = this.findWhere({
			layer_slug_s : layerSlug
		});
		var stateVal = false;
		if (typeof currModel !== "undefined") {
			var previewVal = currModel.get("preview");
			if (previewVal === "on") {
				stateVal = true;
			}
		}
		return stateVal;
	},

	getLayerModel : function(resultModel) {
		var layerSlug = resultModel.get("layer_slug_s");
		var arrModel = this.where({layer_slug_s: layerSlug});
		var layerModel;
		if (arrModel.length > 1){
			throw new Error("There are " + arrModel.length + " layers in the previewed layers collection.  This should never happen.");
		}
		if (arrModel.length > 0){
			layerModel = arrModel[0];
		} else {
			this.add(resultModel.attributes);
			layerModel = this.findWhere({
				layer_slug_s : layerSlug
			});
		}
		return layerModel;
	},

	clearGetFeature : function(turnOnModel) {
		// console.log("clearGetFeature");
		var layerSlug = "dummy";
		if (typeof turnOnModel !== "undefined") {
			layerSlug = turnOnModel.get("layer_slug_s");
		}
		this.each(function(model) {
			if (model.get("layer_slug_s") === layerSlug) {
				return;
			}
			if (model.get("getFeature")) {
				model.set({
					getFeature : false
				});
			}
		});
	}

});
