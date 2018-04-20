package org.opengeoportal.utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * A set of methods for parsing and dealing with the Location field in an OGP SolrRecord
 * 
 * The Location field is stored as a String, but should be a JSON object with key-value pairs
 * that describe how to access the layer over the web
 * 
 * @author cbarne02
 *
 */
public final class LocationFieldUtils {
	final static Logger logger = LoggerFactory.getLogger(LocationFieldUtils.class.getName());

	/**
	 * Get the first value of type "type" from the Location field
	 * 
	 * @param type	The field key
	 * @param locationField		The Solr record Location field as a String
	 * @return	the url for the layer, if the record has been populated correctly
	 * @throws JsonParseException
	 */
	public static String getUrl(String type, String locationField) throws JsonParseException{
		return parseLocationFromKey(locationField, type).get(0);

	}
	
	/**
	 * Get the first value in the "wms" array from the Location field
	 * 
	 * @param locationField		The Solr record Location field as a String
	 * @return	the url for the wms server for the layer, if the record has been populated correctly
	 * @throws JsonParseException
	 */
	public static String getWmsUrl(String locationField) throws JsonParseException{
		return parseLocationFromKey(locationField, "http://www.opengis.net/def/serviceType/ogc/wms").get(0);

	}
	
	/**
	 * determines if the SolrRecord Location field contains a value for the key "wms"
	 * 
	 * @param locationField		The Solr record Location field as a String
	 * @return true if the SolrRecord Location field contains a key for "wms"
	 */
	public static Boolean hasWmsUrl(String locationField){
		try {
			return hasKey(locationField, "http://www.opengis.net/def/serviceType/ogc/wms");
		} catch (JsonParseException e) {

		}
		return false;
	}
	
	/**
	 * determines if the SolrRecord Location field contains a value for the key "serviceStart"
	 * 
	 * the service start url refers to a custom servlet at Harvard that configures a layer in GeoServer so
	 * that it can be accessed via OGC web protocols
	 * 
	 * @param locationField		The Solr record Location field as a String
	 * @return true if the SolrRecord Location field contains a key for "serviceStart"
	 */
	public static Boolean hasServiceStart(String locationField){
		try {
			return hasKey(locationField, "serviceStart");
		} catch (JsonParseException e) {

		}
		return false;
	}
	
	/**
	 * Get the value for the "tilecache" key from the Location field
	 * 
	 * some layers have a tile cache url that differs from the wms url.  if the tile cache acts as
	 * a full wms server, the value should be in "wms" rather than "tilecache"
	 * 
	 * @param locationField		The Solr record Location field as a String
	 * @return	the url for the tilecache service point for the layer, if the record has been populated correctly
	 * @throws JsonParseException
	 */
	public static String getTilecacheUrl(String locationField) throws JsonParseException{
		return parseLocationFromKey(locationField, "tilecache").get(0);

	}
	
	/**
	 * Get the value for the "wfs" key from the Location field. 
	 * 
	 * Only vector layers with a wfs service should have
	 * a value here.
	 * 
	 * @param locationField		The Solr record Location field as a String
	 * @return	the url for the wfs server for the layer, if the record has been populated correctly
	 * @throws JsonParseException
	 */
	public static String getWfsUrl(String locationField) throws JsonParseException{
		return parseLocationFromKey(locationField, "http://www.opengis.net/def/serviceType/ogc/wfs").get(0);

	}

 	/**
	 * determines if the SolrRecord Location field contains a value for the key "wfs"
	 * 
	 * @param locationField		The Solr record Location field as a String
	 * @return true if the SolrRecord Location field contains a key for "wfs"
	 */

	// Added by Allen Lin on Jan, 24, 2014

	public static Boolean hasWfsUrl(String locationField){

		try {
			return hasKey(locationField, "http://www.opengis.net/def/serviceType/ogc/wfs");

		} catch (JsonParseException e) {

		}

		return false;

	}

	/**
	 * * Get the value (ISO 19139 location metadata) for the gmd key from the references field.
	 * *
	 * * @param locationField         The Solr record references field as a String
	 * * @return      the url for the iso metadata location for this layer
	 * * @throws JsonParseException
	 * */
	public static String getISO19139Url(String locationField) throws JsonParseException{
		return parseLocationFromKey(locationField, "http://www.isotc211.org/schemas/2005/gmd/").get(0);

	}

	/**
	 * * determines if the SolrRecord references field contains a value for the gmd key
	 * *
	 * * @param locationField         The Solr record references field as a String
	 * * @return true if the SolrRecord references field contains a key for gmd
	 * */

	public static Boolean hasISO19139Url(String locationField){
		try {
			return hasKey(locationField, "http://www.isotc211.org/schemas/2005/gmd/");
		} catch (JsonParseException e) {
		}
		return false;
	}

	 /**
         * * Get the value (FGDC location metadata) for the gmd key from the references field.
         * *
         * * @param locationField         The Solr record references field as a String
         * * @return      the url for the iso metadata location for this layer
         * * @throws JsonParseException
         * */
        public static String getFGDCUrl(String locationField) throws JsonParseException{
                return parseLocationFromKey(locationField, "http://www.opengis.net/cat/csw/csdgm/").get(0);

        }

        /**
         * * determines if the SolrRecord references field contains a value for the gmd key
         * *
         * * @param locationField         The Solr record references field as a String
         * * @return true if the SolrRecord references field contains a key for gmd
         * */

        public static Boolean hasFGDCUrl(String locationField){
                try {
                        return hasKey(locationField, "http://www.opengis.net/cat/csw/csdgm/");
                } catch (JsonParseException e) {
                }
                return false;
        }

	/**
	 * * Get the value (MODs location metadata)for the MODS key from the references field.
	 * *
	 * * @param locationField         The Solr record references field as a String
	 * * @return      the url for the MODs metadata location for this layer
	 * * @throws JsonParseException
	 * */
	public static String getMODSUrl(String locationField) throws JsonParseException{
		return parseLocationFromKey(locationField, "http://www.loc.gov/mods/v3").get(0);
	}

	/**
	 * * determines if the SolrRecord references field contains a value for the MODS key
	 * *
	 * * @param locationField         The Solr record references field as a String
	 * * @return true if the SolrRecord references field contains a key for MODS
	 * */
	public static Boolean hasMODSUrl(String locationField){
		try {
			return hasKey(locationField, "http://www.loc.gov/mods/v3");
		} catch (JsonParseException e) {
		}
		return false;
	}


	/**
	 * * Get the value (HTML formatted metadata location) for the HTML key from the references field.
	 * *
	 * * @param locationField         The Solr record references field as a String
	 * * @return      the url for the html metadata location for this layer
	 * * @throws JsonParseException
	 * */
	public static String getHTMLMetadataUrl(String locationField) throws JsonParseException{
		return parseLocationFromKey(locationField, "http://www.w3.org/1999/xhtml").get(0);
	}

	/**
	 * * determines if the SolrRecord references field contains a value for the HTML key
	 * *
	 * * @param locationField         The Solr record references field as a String
	 * * @return true if the SolrRecord references field contains a key for HTML
	 * */
	public static Boolean hasHTMLMetadataUrl(String locationField){
		try {
			return hasKey(locationField, "http://www.w3.org/1999/xhtml");
		} catch (JsonParseException e) {
		}
		return false;
	}

	
	/**
	 * determines if the SolrRecord Location field contains a value for the passed key
	 * 
	 * @param locationField		The Solr record Location field as a String
	 * @param key				The key in the Location Json object
	 * @return true if the key exists at the root of the Location Json object
	 * @throws JsonParseException
	 */
	private static Boolean hasKey(String locationField, String key) throws JsonParseException{
		JsonNode rootNode = parseLocationField(locationField);
		JsonNode pathNode = rootNode.path(key);
		if (pathNode.isMissingNode()){
			return false;
			
		} else {
			return true;
		}
	}
	
	/**
	 * @param locationField		The Solr record Location field as a String
	 * @param key				The key in the Location Json object
	 * @return a list of values with the given key
	 * @throws JsonParseException
	 */
	private static List<String> parseLocationFromKey(String locationField, String key) throws JsonParseException{
		JsonNode rootNode = parseLocationField(locationField);
		JsonNode pathNode = rootNode.path(key);
		Set<String> url = new HashSet<String>();
		if (pathNode.isMissingNode()){
			
			throw new JsonParseException("The Object '" + key + "' could not be found.", null);
			
		} else if (pathNode.isArray()){
			
			ArrayNode urls = (ArrayNode) rootNode.path(key);
			for(JsonNode currentUrl: urls){
				if (currentUrl.isTextual()){
					url.add(currentUrl.asText());
				} else {
					throw new JsonParseException("Invalid url value in Location field", null);
				}
			}
			
		} else if (pathNode.isTextual()){
			url.add(pathNode.asText());
		}

		if (url == null || url.isEmpty()){
			
			throw new JsonParseException("The Object '" + key + "' is empty.", null);

		}
		List<String> urlList = new ArrayList<String>();
		urlList.addAll(url);
		return urlList;
	}
	
	/**
	 * Get the value for the "wcs" key from the Location field
	 * 
	 * @param locationField		The Solr record Location field as a String
	 * @return	the url for the wcs server for the layer, if the record has been populated correctly
	 * @throws JsonParseException
	 */
	public static String getWcsUrl(String locationField) throws JsonParseException{

		return parseLocationFromKey(locationField, "http://www.opengis.net/def/serviceType/ogc/wcs").get(0);
	}
	
	/**
	 * Get the value for the "serviceStart" key from the Location field
	 * 
	 * @param locationField		The Solr record Location field as a String
	 * @return	the url for the "serviceStart" service point for the layer, if the record has been populated correctly
	 * @throws JsonParseException
	 */
	public static String getServiceStartUrl(String locationField) throws JsonParseException{
		return parseLocationFromKey(locationField, "serviceStart").get(0);

	}
	
	/**
	 * Get the values for the direct download key from the Location field
	 * 
	 * @param locationField		The Solr record Location field as a String
	 * @return	a List of urls for download for the layer, if the record has been populated correctly
	 * @throws JsonParseException
	 */
	public static List<String> getDownloadUrl(String locationField) throws JsonParseException{
			return parseLocationFromKey(locationField, "http://schema.org/downloadUrl");

	}
	
	/**
	 * determines if the SolrRecord Location field contains a value for the key "urn:x-esri:serviceType:ArcGIS#FeatureLayer"
	 * 
	 * @param locationField		The Solr record Location field as a String
	 * @return true if the SolrRecord Location field contains a key for ArcGIS FeatureLayers
	 */

	public static Boolean hasArcGISFeatureLayerUrl(String locationField){ // hasArcGISRestUrl
		try {
			return hasKey(locationField, "urn:x-esri:serviceType:ArcGIS#FeatureLayer");
		} catch (JsonParseException e) {
		}
		return false;
	}
		
 	/**
	 * Get the value in the urn:x-esri:serviceType:ArcGIS#FeatureLayer field from the Location field
	 * 
	 * @param locationField		The Solr record Location field as a String
	 * @return	the url for the ArcGIS Feature Layer server for the layer, if the record has been populated correctly
	 * @throws JsonParseException
	 */

	public static String getArcGISFeatureLayerUrl(String locationField) throws JsonParseException{  //getArcGISRestUrl
		return parseLocationFromKey(locationField, "urn:x-esri:serviceType:ArcGIS#FeatureLayer").get(0);
	}

	/**
	 * determines if the SolrRecord Location field contains a value for the key "urn:x-esri:serviceType:ArcGIS#TiledMapLayer"
	 * 
	 * @param locationField         The Solr record Location field as a String
	 * @return true if the SolrRecord Location field contains a key for ArcGIS Tiled Map Layer
	 */

	public static Boolean hasArcGISTiledMapLayerUrl(String locationField){
		try {
			return hasKey(locationField, "urn:x-esri:serviceType:ArcGIS#TiledMapLayer");
		} catch (JsonParseException e) {
		}
		return false;
	}

	/**
	 * Get the value in the urn:x-esri:serviceType:ArcGIS#TiledMapLayer field from the Location field
	 * 
	 * @param locationField         The Solr record Location field as a String
	 * @return      the url for the ArcGIS ArcGIS Tiled Map Layer server for the layer, if the record has been populated correctly
	 * @throws JsonParseException
	 */

	public static String getArcGISTiledMapLayerUrl(String locationField) throws JsonParseException{
		return parseLocationFromKey(locationField, "urn:x-esri:serviceType:ArcGIS#TiledMapLayer").get(0);
	}


	/**
	 * determines if the SolrRecord Location field contains a value for the key "urn:x-esri:serviceType:ArcGIS#DynamicMapLayer"
	 * 
	 * @param locationField         The Solr record Location field as a String
	 * @return true if the SolrRecord Location field contains a key for ArcGIS Dynamic Map Layer
	 */

	public static Boolean hasArcGISDynamicMapLayerUrl(String locationField){
		try {
			return hasKey(locationField, "urn:x-esri:serviceType:ArcGIS#DynamicMapLayer");
		} catch (JsonParseException e) {
		}
		return false;
	}

	/**
	 * Get the value in the urn:x-esri:serviceType:ArcGIS#DynamicMapLayer field from the Location field
	 * 
	 * @param locationField         The Solr record Location field as a String
	 * @return      the url for the ArcGIS ArcGIS Dynamic Map Layer server for the layer, if the record has been populated correctly
	 * @throws JsonParseException
	 */

	public static String getArcGISDynamicMapLayerUrl(String locationField) throws JsonParseException{
		return parseLocationFromKey(locationField, "urn:x-esri:serviceType:ArcGIS#DynamicMapLayer").get(0);
	}


	/**
	 * determines if the SolrRecord Location field contains a value for the key "urn:x-esri:serviceType:ArcGIS#ImageMapLayer"
	 * 
	 * @param locationField         The Solr record Location field as a String
	 * @return true if the SolrRecord Location field contains a key for ArcGIS Image Map Layer
	 */

	public static Boolean hasArcGISImageMapLayerUrl(String locationField){
		try {
			return hasKey(locationField, "urn:x-esri:serviceType:ArcGIS#ImageMapLayer");
		} catch (JsonParseException e) {
		}
		return false;
	}

	/**
	 * Get the value in the urn:x-esri:serviceType:ArcGIS#ImageMapLayer field from the Location field
	 * 
	 * @param locationField         The Solr record Location field as a String
	 * @return      the url for the ArcGIS ArcGIS Image Map Layer server for the layer, if the record has been populated correctly
	 * @throws JsonParseException
	 */

	public static String getArcGISImageMapLayerUrl(String locationField) throws JsonParseException{
		return parseLocationFromKey(locationField, "urn:x-esri:serviceType:ArcGIS#ImageMapLayer").get(0);
	}


	/**
	 * parses the SolrRecord Location Field into a JsonNode object for further processing.
	 * 
	 * Additionally, attempts to normalize key names before parsing
	 * 
	 * @param locationField		The Solr record Location field as a String
	 * @return a JsonNode parsed from the locationField String
	 */
	private static JsonNode parseLocationField(String locationField){

		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = null;
		try {
			rootNode = mapper.readTree(locationField);
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rootNode;
		
	}
}
