/* This javascript module creates all the global objects, namespaces them
 * It is the main function of the OGP application
 * 
 * author: Chris Barnett
 * 
 */

if (typeof OpenGeoportal == 'undefined'){
	OpenGeoportal = {};
} else if (typeof OpenGeoportal != "object"){
    throw new Error("OpenGeoportal already exists and is not an object");
}

if ( !Object.create ) {
    Object.create = function ( o ) {
        function F() {}
        F.prototype = o;
        return new F();
        };
}
	
jQuery(document).ready(function (){
	
	//ogp will hold instances
	OpenGeoportal.ogp = {};
	var ogp = OpenGeoportal.ogp;	//an alias
	
	//holds and initializes app-wide settings
	ogp.appState = new OpenGeoportal.Models.OgpSettings();
	ogp.appState.set({controls: new OpenGeoportal.CommonControls()});

	//handles behavior of "frame elements", like expansion of advanced search area, left panel
	ogp.ui = new OpenGeoportal.UserInterface();
	ogp.ui.init();
	
	//create the map and handle map related functions
	ogp.map = new OpenGeoportal.MapController();
	ogp.map.createMap("map");	
	
	//creating the cart
	ogp.cartView = new OpenGeoportal.Views.Cart({collection: ogp.appState.get("cart"), el: $("#cart")});	


	
	jQuery(document).on("mapReady",	function(){
		//console.log("mapready");
			//wait to do this until the google map is ready
			//(createMap could instead return a promise that is fullfilled when the map is loaded)
				//these loads could/should be deferred	
		//needs to wait for ogpConfig fetch
		
		//The collection that holds search results
		ogp.results = new OpenGeoportal.ResultCollection();
		
		//The search results table
		ogp.resultsTableObj = new OpenGeoportal.SearchResultsTable();
		ogp.resultsTableObj.initTable("searchResults");
		
		//if the url is a share link, add the shared layers to the cart
		var hasSharedLayers = ogp.cartView.cartTableObj.addSharedLayersToCart();
		
		//introFlow dictates behavior of info bubbles, first search opens Search Results, etc.
		ogp.ui.introFlow(hasSharedLayers);
		});
	
	/*downtime notice --does this still work? */
	//ogp.ui.showDowntimeNotice();
});


  