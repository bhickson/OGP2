/**
 * This javascript module maintains state for how the table displays layers
 * 
 * @author Chris Barnett
 * 
 */

if (typeof OpenGeoportal == 'undefined') {
	OpenGeoportal = {};
} else if (typeof OpenGeoportal != "object") {
	throw new Error("OpenGeoportal already exists and is not an object");
}

if (typeof OpenGeoportal.Models == 'undefined') {
	OpenGeoportal.Models = {};
} else if (typeof OpenGeoportal.Models != "object") {
	throw new Error("OpenGeoportal.Models already exists and is not an object");
}
/**
 * TableRowSettings this object maintains state for rows, dependent on table
 * instance
 * 
 */

OpenGeoportal.Models.RowSetting = Backbone.Model.extend({
	// idAttribute: "layer_slug_s",
	defaults : {
		expanded : false
	},
	validate : function(model) {
		if (!model.changed.has("layer_slug_s") || !model.changed.has("expanded")) {
			return "Both 'layerSlug' and 'expanded' parameters required";
		}
		if (typeof model.changed.expanded != "boolean") {
			return "'expanded' must be a boolean value";
		}
	}

});

// if the value is equal to the default value, maybe we should just remove the
// model from the collection
OpenGeoportal.TableRowSettings = Backbone.Collection.extend({
	model : OpenGeoportal.Models.RowSetting,

	setExpandState : function(currentlayer_slug_s, state) {

		var layerModel = this.findWhere({
			layer_slug_s : currentlayer_slug_s
		});

		if (typeof layerModel === "undefined") {
			if (state) {
				this.add({
					layer_slug_s : currentlayer_slug_s,
					expanded : true
				});
			}
		} else {
			if (state) {
				layerModel.set({
					"expanded" : true
				});
			} else {
				this.remove(layerModel);
			}
		}
	},
	isExpanded : function(layerSlug) {
		var layerModel = this.findWhere({
			layer_slug_s : layerSlug
		});

		if (typeof layerModel === "undefined") {
			return false;
		}
		return true;
	}
});
