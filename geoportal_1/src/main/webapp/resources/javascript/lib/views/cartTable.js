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


OpenGeoportal.Views.CartTable = OpenGeoportal.Views.LayerTable
		.extend({
			events : {
				"click #downloadHeaderCheck" : "setChecks"
			},
			
			emptyTableMessage: "No data layers have been added to the cart.",
			
			initSubClass: function(){
				this.listenTo(this.collection, "add", this.addedToCart);
				this.listenTo(this.collection, "remove", this.removedFromCart);
				this.listenTo(this.collection, "update", this.updateTable);
			},

			setChecks: function(e){
				var isChecked = jQuery(e.target).is(':checked');
				
					this.collection.each(function(model){
						model.set({isChecked: isChecked});
					});
			},
			
			addedToCart : function(model) {
				var layerSlug = model.get("layer_slug_s");
				model.set({
					isChecked : true
				});

				this.updateSavedLayersNumber();
			},
			
			removedFromCart : function(model) {
				var layerSlug = model.get("layer_slug_s");

				this.updateSavedLayersNumber();
			},

			updateTable: function() {
				this.render();
			},
			
			updateSavedLayersNumber : function() {
				var number$ = jQuery('.savedLayersNumber');

				number$.text('(' + this.collection.length + ')');
			},

			createNewRow: function(model){
				var row = new OpenGeoportal.Views.CartRow(
						{
							model : model,
							tableConfig: this.tableConfig
						});
				return row;
			},
			
			addSharedLayers: function() {
				var solr = new OpenGeoportal.Solr();
				var that = this;

				if (OpenGeoportal.Config.shareIds.length > 0) {
					var groupType = "layers";
					var value  = OpenGeoportal.Config.shareIds
				} else if ( OpenGeoportal.Config.collectionId.length > 0 ) {
					var groupType = "collection";
					var value = OpenGeoportal.Config.collectionId;
				}

				if (groupType != undefined) {
					solr.getLayerInfoFromSolr(groupType, value,
							function(){ that.getLayerInfoSuccess.apply(that, arguments); }, 
							function(){ that.getLayerInfoJsonpError.apply(that, arguments); });
					return true;
				} else {
					return false;
				}
			},

			getLayerInfoSuccess: function(data) {
				var southwest, northeast
				var arr = this.solrToCollection(data);
				this.collection.add(arr);

				if (data.response.numFound < 6) {
					this.previewed.add(arr);
					this.previewed.each(function(model){
						model.set({preview: "on"});
					});
				};

				jQuery(document).trigger("map.zoomToLayerExtent", {
					bbox : OpenGeoportal.Config.shareBbox
				});

				if (OpenGeoportal.Config.shareBbox !== "-180,-90,180,90") { 
					bounds = OpenGeoportal.Config.shareBbox;
					console.log("Bounds: ", bounds);
					southwest = [bounds.split(',')[1], bounds.split(',')[0]];
					northeast = [bounds.split(',')[3], bounds.split(',')[2]]
				} else {
					var minX = Infinity; maxX = -Infinity; minY = Infinity; maxY = -Infinity;
					this.collection.each( function (model) {
	                                	layerBbox = model.get("solr_geom").split("(")[1].split(")")[0].split(" ");
						lminY = parseFloat(layerBbox[3]);
						lmaxY = parseFloat(layerBbox[2]);
						lminX = parseFloat(layerBbox[0]);
						lmaxX = parseFloat(layerBbox[1]);

						minX = Math.min(lminX, minX);
						maxX = Math.max(lmaxX, maxX);
						minY = Math.min(lminY, minY);
						maxY = Math.max(lmaxY, maxY);
					});
					southwest = [minY,minX];
					northeast = [maxY,maxX];
				};

				var bbox = new L.latLngBounds(southwest,northeast);
				setTimeout( function() {
			                OpenGeoportal.ogp.map.fitBounds(bbox)
				}, 550);
			},

			getLayerInfoJsonpError:function() {
				throw new Error(
						"The attempt to retrieve layer information from layerSlugs failed.");
			},

			addColumns: function(tableConfigCollection) {
				var that = this;
				tableConfigCollection
						.add([
								{
									order : 0,
									columnName : "expandControls",
									resizable : false,
									organize : false,
									visible : true,
									hidable : false,
									header : "",
									columnClass : "colExpand",
									width : 10,
									modelRender : function(model) {
										var showControls = model.get("showControls");
										return that.tableControls
												.renderExpandControl(showControls);
									}

								},
								{
										order : 1,
										columnName : "checkBox",
										resizable : false,
										organize : false,
										visible : true,
										hidable : false,
										header : "<input type=\"checkbox\" id=\"downloadHeaderCheck\" checked />",
										columnClass : "colChkBoxes",
										width : 21,
										modelRender : function(model) {
											
											return that.tableControls.renderDownloadControl(model.get("isChecked"));
											
										}
									},
									{
										order : 2,
										columnName : "layer_geom_type_s",
										resizable : false,
										organize : "group",
										visible : true,
										hidable : true,
										displayName : "Data Type",
										header : "Type",
										columnClass : "colType",
										width : 30,
										modelRender : function(model) {
											var dataType = model.get("DataType");
											return that.tableControls.renderTypeIcon(dataType);
										}

									}, {
										order : 3,
										columnName : "score",
										resizable : true,
										minWidth : 27,
										width : 27,
										organize : "numeric",
										visible : false,
										hidable : false,
										displayName : "Relevancy",
										header : "Relev",
										columnClass : "colScore"
									}, {
										order : 4,
										columnName : "dc_title_s",
										resizable : true,
										minWidth : 35,
										width : 200,
										organize : "alpha",
										visible : true,
										hidable : false,
										displayName : "Name",
										header : "Name",
										columnClass : "colTitle"
									}, {
										order : 5,
										columnName : "dc_creator_sm",
										resizable : true,
										minWidth : 62,
										width : 86,
										organize : "group",
										visible : true,
										hidable : true,
										displayName : "Creator",
										header : "Creator",
										columnClass : "colCreator"

									}, {
										order : 6,
										columnName : "dc_publisher_s",
										resizable : true,
										minWidth : 58,
										width : 80,
										organize : "group",
										visible : false,
										hidable : true,
										displayName : "Publisher",
										header : "Publisher",
										columnClass : "colPublisher"

									}, {
										order : 7,
										columnName : "solr_year_i",
										organize : "numeric",
										visible : false,
										displayName : "Year",
										resizable : true,
										minWidth : 30,
										width : 30,
										hidable : true,
										header : "Year",
										columnClass : "colYear",
										modelRender : function(model) {
											var date = model.get("solr_year_i");
											return that.tableControls.renderDate(date);
										}

									}, {
										order : 8,
										columnName : "dct_provenance_s",
										organize : "alpha",
										visible : true,
										hidable : true,
										resizable : false,
										displayName : "Repository",
										header : "Rep",
										columnClass : "colSource",
										width : 24,
										modelRender : function(model) {
											var repository = model.get("dct_provenance_s");
											return that.tableControls.renderRepositoryIcon(repository);

										}

									}, {
										order : 9,
										columnName : "dc_rights_s",
										resizable : false,
										organize : false,
										visible : false,
										hidable : false,
										header : "Access"
									}, 
								{
									order : 10,
									columnName : "Metadata",
									resizable : false,
									organize : false,
									visible : true,
									hidable : false,
									header : "Meta",
									columnClass : "colMetadata",
									width : 30,
									modelRender : function(model) {
										return that.tableControls.renderMetadataControl();
									}
								},
								{
									order : 11,
									columnName : "View",
									resizable : false,
									organize : false,
									visible : true,
									hidable : false,
									header : "View",
									columnClass : "colPreview",
									width : 39,
									modelRender : function(model) {
										var layerSlug = model.get("layer_slug_s");
										var location = model.get("dct_references_s");
										var access = model.get("dc_rights_s").toLowerCase();
										var institution = model.get("dct_provenance_s").toLowerCase();

										var stateVal = false;
										var selModel =	that.previewed.findWhere({
											layer_slug_s : layerSlug
										});
										if (typeof selModel !== 'undefined') {
											if (selModel.get("preview") === "on"){
												stateVal = true;
											}
										}
										
										var canPreview = function(location){
											//where is a good place to centralize this?
											return OpenGeoportal.Utility.hasLocationValueIgnoreCase(
													location, ["http://www.opengis.net/def/serviceType/ogc/wms",
														   "urn:x-esri:serviceType:ArcGIS#FeatureLayer",
														   "urn:x-esri:serviceType:ArcGIS#TiledMapLayer",
														   "urn:x-esri:serviceType:ArcGIS#ImageMapLayer",
														   "imagecollection"]);
										};
										
										var hasAccess = true;
										var canLogin = true;
										
										var previewable = canPreview(location);
										if (previewable){
											var loginModel = OpenGeoportal.ogp.appState.get("login").model;
											hasAccess = loginModel.hasAccessLogic(access, institution);
											canLogin = loginModel.canLoginLogic(institution);
										} 

										return that.tableControls.renderPreviewControl(previewable, hasAccess, canLogin, stateVal);
									}
								} ]);
			}


		});
