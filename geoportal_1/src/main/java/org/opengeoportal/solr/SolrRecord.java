package org.opengeoportal.solr;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.solr.client.solrj.beans.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SolrRecord {
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Field("layer_slug_s")
	String layerSlug; //layerId
	@Field("layer_id_s")
	String serviceId; //name
	@Field("dct_isPartOf_sm")
	String[] collectionIds; //collectionId
	@Field("dct_provenance_s")
	String institution;
	@Field("dc_rights_s")
	String access;
	@Field("layer_geom_type_s")
	String dataType;
	@Field("dc_type_s")
	String layerType;  //availability
	@Field("dc_title_s")
	String layerTitle;  //layerDisplayName
	@Field("dc_publisher_s")
	String publisher;
	@Field("dc_creator_sm")
	String[] originator;
	@Field("dc_subject_sm")
	String[] subjectKeywords; //themeKeywords;
	@Field("dct_spatial_sm")
	String[] placeKeywords;
	@Field("dc_description_s")
	String description;
	@Field("dct_references_s")
	String serviceLocations; //location;
	@Field("solr_geom")
	String boundingBox;
	@Field("solr_year_i")
	int contentYear; //contentDate
	//@Field("FgdcText")
	//String fgdcText;
	Double maxY;
	Double minY;
	Double maxX;
	Double minX;

	

	public String getLayerSlug() {
		return layerSlug;
	}
	public void setLayerSlug(String layerSlug) {
		this.layerSlug = layerSlug;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String[] getCollectionIds() {
		return collectionIds;
	}
	public void setCollectionIds(String[] collectionIds) {
		this.collectionIds = collectionIds;
	}
	public String getInstitution() {
		return institution;
	}
	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public String getAccess() {
		return access;
	}
	public void setAccess(String access) {
		this.access = access;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getLayerType() {
		return layerType;
	}
	public void setLayerType(String layerType) {
		this.layerType = layerType;
	}
	public String getLayerTitle() {
		return layerTitle;
	}
	public void setLayerTitle(String layerTitle) {
		this.layerTitle = layerTitle;
	}

	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String[] getOriginator() {
		logger.info("ORIGINATOR: " + originator);
		return originator;
	}
	public void setOriginator(String[] originator) {
		this.originator = originator;
	}

	public String[] getSubjectKeywords() {
		return subjectKeywords;
	}
	public void setSubjectKeywords(String[] subjectKeywords) {
		this.subjectKeywords = subjectKeywords;
	}
	public String[] getPlaceKeywords() {
		return placeKeywords;
	}
	public void setPlaceKeywords(String[] placeKeywords) {
		this.placeKeywords = placeKeywords;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getServiceLocations() {
		return serviceLocations;
	}
	public void setServiceLocations(String serviceLocations) {
		this.serviceLocations = serviceLocations;
	}
	public Double getMaxY() {
		// boundingBox string is in the form of ENVELOPE(MINX MAXX MAXY MINY)
		return Double.parseDouble(this.boundingBox.split("\\(")[1].split("\\)")[0].split(", ")[2]);
	}
	public void setMaxY(Double maxY) {
		this.maxY = maxX;
	}
	public Double getMinY() {
		// boundingBox string is in the form of ENVELOPE(MINX MAXX MAXY MINY)
		return Double.parseDouble(this.boundingBox.split("\\(")[1].split("\\)")[0].split(", ")[3]);
	}
	public void setMinY(Double minY) {
		this.minY = minY;
	}
	public Double getMaxX() {
		// boundingBox string is in the form of ENVELOPE(MINX MAXX MAXY MINY)
		return Double.parseDouble(this.boundingBox.split("\\(")[1].split("\\)")[0].split(", ")[1]);
	}
	public void setMaxX(Double maxX) {
		this.maxX = maxX;
	}
	public Double getMinX() {
		// boundingBox string is in the form of ENVELOPE(MINX MAXX MAXY MINY)
		return Double.parseDouble(this.boundingBox.split("\\(")[1].split("\\)")[0].split(", ")[0]);
	}
	public void setMinX(Double minX) {
		this.minX = minX;
	}
	public int getContentYear() {
		return contentYear;
	}
	public void setContentYear(int contentYear) {
		this.contentYear = contentYear;
	}
	
	public Map<String,String> toMap(){
		Map<String,String> map = new HashMap<String,String>();
		map.put("LayerSlug", this.layerSlug);
		map.put("ServicesLayerName", this.serviceId);
		map.put("Title", this.layerTitle);
		map.put("DataType", this.dataType);
		map.put("Access", this.access);
		map.put("Bounds", this.minX + "," + this.boundingBox.split("\\(")[1].split("\\)")[0].split(", ")[1] + "," + this.maxY + "," + this.minY);
		String originators = String.join(",",this.originator);
		map.put("Originator", originators);
		map.put("Publisher", this.publisher);
		logger.info("SET BOUNDS:" + map);
		return map;
	}
	public String toString(){
		Map<String, String> map = this.toMap();
		String s = "";
		for (String key: map.keySet()){
			s += key;
			s += ": ";
			s += map.get(key);
			s += ",";
		}
		return s;
	}
}
