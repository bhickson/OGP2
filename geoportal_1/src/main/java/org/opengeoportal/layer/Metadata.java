package org.opengeoportal.layer;


public class Metadata {
	BoundingBox bounds;
	String id;
	String title;
	String description;
	String owsName;
	String serviceLocations;
	String[] originator;
	String[] subjectKeywords;
	String[] placeKeywords;
	String institution;
	String fullText;
	AccessLevel access;
	GeometryType geometryType;
	private String publisher;
	private Boolean georeferenced;
	private String contentYear;
		
	public Metadata(String layerSlug){
		setId(layerSlug);
	}
	public Metadata(){
	}
	
	public GeometryType getGeometryType() {
		return geometryType;
	}

	public void setGeometryType(GeometryType geometryType) {
		this.geometryType = geometryType;
	}
	
	public void setGeometryType(String geometryTypeString) {
		this.geometryType = GeometryType.parseGeometryType(geometryTypeString);
	}

	public AccessLevel getAccess() {
		return access;
	}
	public void setAccessLevel(AccessLevel access) {
		this.access = access;
	}
	
	public void setAccessLevel(String accessString){
		this.access = AccessLevel.parseString(accessString);
	}
	
	public BoundingBox getBounds() {
		return bounds;
	}
	public void setBounds(BoundingBox bounds) {
		this.bounds = bounds;
	}
	
	public void setBounds(String minX, String maxX, String maxY, String minY) {
		this.bounds = new BoundingBox(minX, maxX, maxY, minY);
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getOwsName() {
		return owsName;
	}
	public void setOwsName(String owsName) {
		this.owsName = owsName;
	}
	public String getServiceLocations() {
		return serviceLocations;
	}
	public void setServiceLocations(String serviceLocations) {
		this.serviceLocations = serviceLocations;
	}
	public String[] getOriginator() {
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
	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public void setAccess(AccessLevel access) {
		this.access = access;
	}

	public String getFullText() {
		return fullText;
	}
	public void setFullText(String fullText) {
		this.fullText = fullText;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher){
		this.publisher = publisher;
	}

	public Boolean getGeoreferenced() {
		return georeferenced;
	}
	
	public void setGeoreferenced(Boolean georeferenced){
		this.georeferenced = georeferenced;
	}

	public String getContentYear() {
		return contentYear;
	}
	
	public void setContentDate(String contentYear){
		this.contentYear = contentYear;
	}
}
