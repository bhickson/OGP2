package org.opengeoportal.download.controllers;

import org.opengeoportal.download.types.LayerRequest.Status;
import org.opengeoportal.layer.BoundingBox;

public class RequestedLayerStatus {
	private Status status;
	private String id;
	private String bounds;
	private String slug;
	private String responseType;
	
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBounds() {
		return bounds;
	}
	public void setBounds(BoundingBox bounds) {
		this.bounds = bounds.toStringLatLon();
	}	
	public void setBounds(String bounds) {
		this.bounds = bounds;
	}
	public String getLayerSlug() {
		return slug;
	}
	public void setLayerSlug(String slug) {
		this.slug = slug;
	}
	public String getResponseType() {
		return responseType;
	}
	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}
	


}
