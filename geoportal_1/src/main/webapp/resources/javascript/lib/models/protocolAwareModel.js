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

OpenGeoportal.Models.ProtocolAware = OpenGeoportal.Models.ResultItem
		.extend({
			initialize : function() {
				this.assignAttributes();
			},

			// a stub
			assignAttributes : function() {
				console.log("subclass me!");
			},
			supportedAttributesByType : [],
			getAttributesByType : function(type) {
				var arrAttr = this.supportedAttributesByType;
				var objAttr = {};
				for ( var i in arrAttr) {
					if (arrAttr[i].type === type) {
						objAttr = arrAttr[i];
					}
				}

				if (_.isEmpty(objAttr)) {
					// if no match, then there are no attributes to add
					return objAttr;
				}

				var attributes = {};
				if (_.has(objAttr, "discriminator")
						&& objAttr["discriminator"] !== "none") {
					// parse the object further based on discriminator value
					if (!this.has(objAttr["discriminator"])) {
						throw new Error(
								"Model does not contain the attribute : "
										+ objAttr["discriminator"]);
					}
					var key = this.get(objAttr["discriminator"]).toLowerCase();
					if (_.has(objAttr.attributes, key)) {
						attributes = objAttr.attributes[key];
					} else {
						console
								.log("attributes Object does not contain the property : "
										+ key);
					}

				} else {
					attributes = objAttr.attributes;
				}

				return attributes;
			},
			missingAttribute : function(attributeName) {
				throw new Error("Model does not contain the attribute '"
						+ attributeName + "'");
			},
			isPublic : function() {
				var access = this.get("dc_rights_s").toLowerCase();
				if (access !== "public") {

					return false;
				}
				return true;
			},
			attributeIsOneOf : function(attr, attrVals) {
				if (this.has(attr)) {
					var val = this.get(attr);
					return OpenGeoportal.Utility.arrayContainsIgnoreCase(
							attrVals, val);
				}

				this.missingAttribute(att);
			},
			isVector : function() {
				var attr = "layer_geom_type_s";
				// we'll assume that unknown is a vector
				var attrVals = [ "point", "line", "polygon", "undefined" ];
				return this.attributeIsOneOf(attr, attrVals);
			},
			isRaster : function() {
				var attr = "layer_geom_type_s";
				var attrVals = [ "raster", "paper map", "scanned map" ];
				return this.attributeIsOneOf(attr, attrVals);
			},
			hasOGCEndpoint : function(ogcProtocol) {
				var attr = "dct_references_s";
				if (this.has(attr)) {
					
					var location = this.get(attr);

					return OpenGeoportal.Utility.hasLocationValueIgnoreCase(
							location, [ ogcProtocol ]);
				}

				this.missingAttribute(att);
			}

		});

