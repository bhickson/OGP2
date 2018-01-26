package org.opengeoportal.download;

import java.io.File;

public interface MetadataRetriever {

	File getXMLFile(String metadataFileName, File xmlFile) throws Exception;
	String getXMLStringFromId(String layerSLUG, String xmlFormat) throws Exception;
	String getContactName(String layerSLUG);
	String getContactPhoneNumber(String layerSlug);
	String getContactAddress(String layerSLUG);
	String getMetadataAsHtml(String layerSLUG) throws Exception;
}
