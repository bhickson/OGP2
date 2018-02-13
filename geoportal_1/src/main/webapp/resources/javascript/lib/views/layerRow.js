if (typeof OpenGeoportal === 'undefined') {
	OpenGeoportal = {};
} else if (typeof OpenGeoportal !== "object") {
	throw new Error("OpenGeoportal already exists and is not an object");
}

if (typeof OpenGeoportal.Views === 'undefined') {
	OpenGeoportal.Views = {};
} else if (typeof OpenGeoportal.Views !== "object") {
	throw new Error("OpenGeoportal.Views already exists and is not an object");
}

OpenGeoportal.Views.LayerRow = Backbone.View.extend({
	tagName : "div",
	className : "tableRow",
	attributes : function() { return {"layerid": this.model.get("layer_slug_s") } },
	events : {
		"click .viewMetadataControl" : "viewMetadata",
		"click .previewControl" : "togglePreview",
		"click .previewLink"	: "handlePreviewLink",
		"click .colExpand" : "toggleExpand",
		"click .colTitle" : "toggleExpand",
		"mouseover" : "doMouseoverOn",
		"mouseout" : "doMouseoverOff"
	},
	
	constructor: function (options) {
		//allow options to be passed in the constructor argument as in previous Backbone versions.
		    this.options = options;
		    Backbone.View.apply(this, arguments);
		  },
		  
	initialize : function() {
		this.template = OpenGeoportal.ogp.template;
		this.metadataViewer = new OpenGeoportal.MetadataViewer();
		this.previewed = OpenGeoportal.ogp.appState.get("previewed");

		this.login = OpenGeoportal.ogp.appState.get("login").model;

		var that = this;
		this.login.listenTo(this.login, "change:authenticated", function() {
			that.removeOnLogout.apply(that, arguments);
			that.render.apply(that, arguments);
		});
		this.listenTo(this.model, "change:showControls change:hidden", this.render);
		
		this.subClassInit();
		this.addSubClassEvents();
		//console.log("init render");
		this.subviewStorage();
		this.render();
	},
	
	subClassInit: function(){
		//nop
	},
	subClassEvents: {},
	
	addSubClassEvents: function(){
		jQuery.extend(this.events, this.subClassEvents);
	},
	
	subviewStorage: function(){
		this.subviews = [];
	},
	
	removeOnLogout : function(loginView) {
		if (!this.login.hasAccess(this.model)) {
			this.model.set({
				preview : "off"
			});
		}
	},

	viewMetadata : function() {
		// console.log(arguments);
		this.metadataViewer.viewMetadata(this.model);
	},
	
	handlePreviewLink: function(){
		//if there is a previewLink, prefer it
		var url = this.getPreviewLink();
		
		if (url === null){
			//otherwise, try to form a link from the repository config info
			url = this.getRepositoryLink();
		}
		
			
		//open the url in a new tab/window
		var target = "_blank";
		window.open(url, target);

		
	},
	
	getPreviewLink: function(){
		var url = null;
		if (this.model.has("dct_references_s")){
			if (typeof this.model.get("dct_references_s").previewLink !== "undefined"){
				//go to the previewLink url
				url = this.model.get("dct_references_s").previewLink;
			}
		}
		return url;
	},
	
	getRepositoryLink: function(){
		var url = null;
		var model = this.model;
		//try to assemble a link from the config
		var rep = OpenGeoportal.Config.Repositories.findWhere({id: model.get("dct_provenance_s").toLowerCase()});
				
		if (typeof rep == "undefined"){
				throw new Error("Repository could not be found");
		}
				
		
		if (rep.has("nodeType")){
			var nodeType = rep.get("nodeType");
			var url = null;
			if (rep.has("url")){
				url = rep.get("url");
			} else {
				throw new Error("Repository definition must have the parameter 'url'");
			}
			
			if (nodeType == "ogp1"){
//http://calvert.hul.harvard.edu:8080/opengeoportal/openGeoPortalHome.jsp?layer=HARVARD.SDE.GLB_INWTRA&minX=-557.578125&minY=-85.051128779807&maxX=557.578125&maxY=85.051128779807
				var params = {
						layer: model.get("layer_slug_s"),
						minX: model.get("MinX"),
						minY: model.get("MinY"),
						maxX: model.get("MaxX"),
						maxY: model.get("MaxY")
						};
				url += "/openGeoPortalHome.jsp?" + jQuery.param(params);
				
			} else if (nodeType == "ogp2"){
//http://localhost:8080/ogp/?ogpids=HARVARD.SDE.GLB_BAILEY&bbox=-180%2C-85.051129%2C180%2C85.051129
				var bbox = [ model.get("MinX"), model.get("MaxX"),model.get("MaxY"), model.get("MinY")].join();
				var params = {
						ogpids: model.get("layer_slug_s"),
						bbox: bbox
						};
				url += "/?" + jQuery.param(params);

			} else if (nodeType == "geoweb"){

					url += "/layer/" + model.get("layer_slug_s");
	
			}
			
	
		} else {
			throw new Error("Repository type is not defined.");
		}
				
		return url;
			
	},

	updateView: function(e, data){

		if (this.model.get("layer_slug_s") === data.layer_slug_s){
			if (e.type === "previewLayerOff"){
				if (this.model.has("hidden")){
					this.model.set({hidden: false});
					return;
				}
			}
			//console.log("update view");
			this.render();
		}
	},
	
	togglePreview : function(e) {
		var layerSlug = this.model.get("layer_slug_s");
		var model = this.previewed.findWhere({
			layer_slug_s : layerSlug
		});
		if (typeof model === "undefined") {
			var layerAttr = null;
			try {

				layerAttr = this.model.attributes;
				layerAttr.preview = "on";
			} catch (err) {
				console.log(err);
			}
			// add them to the previewed collection. Add them as attributes
			// since we
			// are using different models in the previewed collection, and we
			// want
			// "model" to be called
			this.previewed.add(_.clone(layerAttr));

		} else {
			var update = "";
			if (model.get("preview") === "on") {
				update = "off";		

			} else {
				update = "on";
			}
			
			model.set({preview: update});	

		}
	
	},

	getModelFromPreviewed: function(){
		var layerSlug = this.model.get("layer_slug_s");
		var model = this.previewed.findWhere({
			layer_slug_s : layerSlug
		});
		if (typeof model === "undefined") {
			// get the attributes for the layer retrieved from solr
			var layerAttr = null;
			try {

				layerAttr = this.model.attributes;
			} catch (e) {
				console.log(e);
			}
			// add them to the previewed collection. Add them as attributes
			// since we
			// are using different models in the previewed collection, and we
			// want
			// "model" to be called
			this.previewed.add(_.clone(layerAttr));
			var newmodel = this.previewed.findWhere({
				layer_slug_s : layerSlug
			});
			return newmodel;
		} else {
			return model;
		}
	},
	
	toggleExpand : function() {
		// console.log("toggleExpand");
		var controls = this.model.get("showControls");
		this.model.set({
			showControls : !controls
		});
	},
	
	skipLayer: function(){
		return false;
	},
	
	close: function(){
		if (this.closeSubviews){
			this.closeSubviews();
		}
		this.remove();		
		this.stopListening();
		if (this.onClose){
			this.onClose();
		}
		
	},
	
	closeSubviews: function(){
		_.each(this.subviews, function(view){
			if (view.close){
				view.close();
			} else {
				view.remove();
				view.stopListening();
			}
		});
		this.subviews = [];
	},

	render : function() {

		if (this.skipLayer()){
			this.$el.html("");
			this.$el.addClass("hiddenRow");
			return this;
		} else {
			this.$el.removeClass("hiddenRow");
		}
		var html = "";
		var that = this;
		var model = this.model;
		// determine which td elements are visible from the the
		// tableConfig model
		var visibleColumns = this.options.tableConfig.where({
			visible : true
		});

		_.each(visibleColumns, function(currCol){
			
			var contents = "";
			if (currCol.has("modelRender")) {
				contents = currCol.get("modelRender")(model);
				html += that.template.cartCell({
					colClass : currCol.get("columnClass"),
					contents : contents
				});
			} else {
				contents = model.get(currCol.get("columnName"));
				html += that.template.textCartCell({
					colClass : currCol.get("columnClass"),
					contents : contents
				});
			}
		});
		
		var container$ = this.renderExpand();
		
		if (container$ !== ""){
			this.$el.html(html).append(container$);

		} else {
			this.$el.html(html);
		}
		
		this.options.tableConfig.each(
				function(model){
					var width = model.get("width");
					var cclass = model.get("columnClass"); 
					that.$el.find("." + cclass).width(width);
					});
		
		return this;

	},
	
	renderExpand: function(){
		var expand$ = "";
		this.closeSubviews();
		if (this.model.get("showControls")) {

				expand$ = jQuery(this.template.cartPreviewToolsContainer());
				// a view that watches expand state
				// Open this row
				var tools$ = expand$.find(".previewTools").first();
				var previewModel = this.getModelFromPreviewed();
				var view = new OpenGeoportal.Views.PreviewTools({
					model : previewModel,
					el : tools$
				});// render to the container
				this.subviews.push(view);
		
		} 
		return expand$;
	},
	
	doMouseoverOn : function(){
		this.highlightRow();
		this.showBounds();
	},
	
	doMouseoverOff: function(){
		this.unHighlightRow();
		this.hideBounds();
	},
	
	highlightRow : function(){
		jQuery(".tableRow").removeClass("rowHover");
		this.$el.addClass("rowHover");

	},
	
	unHighlightRow: function(){
		this.$el.removeClass("rowHover");
	},
	
	showBounds : function() {
		var currModel = this.model;
		// The solr_geom bbox is in the format 'Envelope(xmin xmax ymax ymin)'.  Need to parse
		bb_list = currModel.get("solr_geom").split("(")[1].split(")")[0].split(" ");
		var bbox = {};
		bbox.south = parseFloat(bb_list[3]);
		bbox.north = parseFloat(bb_list[2]);
		bbox.west =  parseFloat(bb_list[0]);
		bbox.east =  parseFloat(bb_list[1]);
		jQuery(document).trigger("map.showBBox", bbox);
	},
	
	hideBounds : function() {
		jQuery(document).trigger("map.hideBBox");
	}
});
