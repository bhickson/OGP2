


package org.opengeoportal.download;

import java.util.*;
import java.io.*; 

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.opengeoportal.utilities.LocationFieldUtils;
import org.opengeoportal.utilities.http.OgpHttpClient;
import org.opengeoportal.solr.SolrRecord;


import org.xml.sax.*;
import org.apache.commons.io.IOUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.SolrDocumentList;
import org.opengeoportal.metadata.LayerInfoRetriever;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.w3c.dom.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;



/**
 * retrieves a layer's XML metadata from an OGP solr instance
 * @author chris
 *
 */

public class MetadataFromSolr implements MetadataRetriever {
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private LayerInfoRetriever layerInfoRetriever;

	@Autowired
	@Qualifier("httpClient.pooling")
	OgpHttpClient ogpHttpClient;

	private String layerSlug;
	private Document xmlDocument;
	private DocumentBuilder builder;
	private Resource fgdcStyleSheet;
	private Resource iso19139StyleSheet;
	
	public enum MetadataType {
		ISO_19139, FGDC;

	}
	
	MetadataFromSolr() {
		// Create a factory
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);  // dtd isn't always available; would be nice to attempt to validate
		try {
			factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// Use document builder factory
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * takes the XML metadata string from the Solr instance, does some filtering, parses it as XML
	 * as a form of simplistic validation
	 * 
	 * @param rawXMLString
	 * @return the processed XML String
	 * @throws TransformerException
	 */
	String filterXMLString(String layerSlug, String rawXMLString)
	throws TransformerException
	{
		Document document = null;
		try {
			document = buildXMLDocFromString(layerSlug, rawXMLString);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Source xmlSource = new DOMSource(document);
		StringWriter stringWriter = new StringWriter();
		StreamResult streamResult = new StreamResult(stringWriter);
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer();
        	transformer.setOutputProperty(OutputKeys.INDENT, "yes");

		transformer.transform(xmlSource, streamResult);
		String outputString = stringWriter.toString();

		return outputString;
	}
	
	/**
	 * takes the XML metadata string from the Solr instance, does some filtering, returns an xml document
	 * 
	 * @param rawXMLString
	 * @return the XML String as a Document
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 */
	Document buildXMLDocFromString(String layerSlug, String rawXMLString) throws ParserConfigurationException, SAXException, IOException{
		logger.debug("Building XML Document From String"); 
		if ((layerSlug.equalsIgnoreCase(this.layerSlug))&&(this.xmlDocument != null)){
			return this.xmlDocument;
		} else {
			InputStream xmlInputStream = null;

			try{
				//parse the returned XML to make sure it is well-formed & to format
				//filter extra spaces from xmlString
				rawXMLString = rawXMLString.replaceAll(">[ \t\n\r\f]+<", "><").replaceAll("[\t\n\r\f]+", "");
				xmlInputStream = new ByteArrayInputStream(rawXMLString.getBytes("UTF-8"));

				//Parse the document
				Document document = builder.parse(xmlInputStream);
				return document;
			} finally {
				IOUtils.closeQuietly(xmlInputStream);
			}
		}

	}
	
	/**
	 * given a layer name, retrieves the XML metadata string from the OGP Solr instance
	 * @param layerName
	 * @return the XML metadata as a string
	 * @throws Exception 
	 */
	private String getXMLStringFromSolr(String identifier, String descriptor) throws Exception{
		Map<String,String> conditionsMap = new HashMap<String, String>();
		if (descriptor.equalsIgnoreCase("name")){
			int i = identifier.indexOf(":");
			if (i > 0){
				identifier = identifier.substring(i + 1);
			}
			conditionsMap.put("layer_slug_s", identifier);
			this.layerSlug = null;
		} else if(descriptor.equalsIgnoreCase("layer_slug_s")){
			conditionsMap.put("layer_slug_s", identifier);
			this.layerSlug = identifier;
		} else {
			this.layerSlug = null;
			return null;
		}

		// _____________________________________
		
		HttpClient httpclient = ogpHttpClient.getCloseableHttpClient();
	        String metadataString = null;
		String metadataLocation = null;
		SolrRecord layerInfo = layerInfoRetriever.getAllLayerInfo(identifier);

		// TODO - Add ability to get mods metadata (including stylesheet)
		if (LocationFieldUtils.hasISO19139Url(layerInfo.getServiceLocations())){
			metadataLocation = LocationFieldUtils.getISO19139Url(layerInfo.getServiceLocations());
		} else if (LocationFieldUtils.hasFGDCUrl(layerInfo.getServiceLocations())){
			metadataLocation = LocationFieldUtils.getFGDCUrl(layerInfo.getServiceLocations());
		} else {
			throw new Exception("No XML metadata location found for this layer.");
		}

        	HttpGet httpget = new HttpGet(metadataLocation);
		

        	try {
	              	HttpResponse response = httpclient.execute(httpget);

			if (response.getStatusLine().getStatusCode() != 200){
	                        throw new Exception("Attempt to download " + layerSlug + " metadata failed.");
                	}

        	        HttpEntity entity = response.getEntity();
	                String contentType = entity.getContentType().getValue();

			InputStream inputStream = null;
	                try {
                        	inputStream = entity.getContent();

		                if (contentType.toLowerCase().contains("xml")){
					logger.debug("CONTENT CONTAINS XML");
					String responseContent = EntityUtils.toString(entity);
					metadataString = responseContent;
				} else if (contentType.toLowerCase().contains("text/plain")) {
					String responseContent = EntityUtils.toString(entity);
                                        metadataString = responseContent;
					 
				} else {
					// TODO need some other checker here for non xml metadata (MODS and HTML)
					throw new Exception("Metadata retrieved is not XML formatted.");
				}
				logger.debug("METADATA STRING: ", metadataString);

	                } catch (Exception e){

                	} finally {
	                        inputStream.close();
                	}

        	} catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
        	        e.printStackTrace();
	        } catch (IOException e) {
	                // TODO Auto-generated catch block
                	e.printStackTrace();
        	}
	        return metadataString;

		// _____________________________________
	
		/*SolrQuery query = new SolrQuery();
		query.setQuery( descriptor + ":" + identifier );
		query.addField("layer_slug_s");
		query.setRows(1);
		logger.info("QUERY:" + query);
		
		SolrDocumentList docs = this.layerInfoRetriever.getSolrServer().query(query).getResults();
		return (String) docs.get(0).getFieldValue("layer_slug_s");*/
	}
	
	/**
	 * Gets the XML metadata as a string, then outputs it to a file.
	 * 
	 * @param metadataLayerName  the name of the layer you want XML metadata for
	 * @param xmlFile  the name of the file the metadata will be written to
	 * return a File object pointing to the XML metadata file for the layer
	 * @throws Exception 
	 */
	public File getXMLFile(String metadataLayerName, File xmlFile) throws Exception{
		OutputStream xmlFileOutputStream = null;
		try{
			String xmlString = this.getXMLStringFromSolr(metadataLayerName, "layer_slug_s");
			xmlString = this.filterXMLString("", xmlString);
			//write this string to a file
			xmlFileOutputStream = new FileOutputStream (xmlFile);
			new PrintStream(xmlFileOutputStream).print(xmlString);

			return xmlFile;

		} finally {
			IOUtils.closeQuietly(xmlFileOutputStream);
		}
	}

	@Override
	public String getXMLStringFromLayerSlug(String layerSlug) throws Exception {
		String xmlString = this.getXMLStringFromSolr(layerSlug, "layer_slug_s");
		xmlString = this.filterXMLString(layerSlug, xmlString);

		return xmlString;
	}
	
	@Override
	public String getMetadataAsHtml(String layerSlug) throws Exception {
		logger.debug("Getting Metadata As Html");
		String xmlString = this.getXMLStringFromSolr(layerSlug, "layer_slug_s");
		
		Document document = null;
		try {
			document = buildXMLDocFromString(layerSlug, xmlString);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.debug("XML Document:", document);

		//get the metadata type and correct xslt document
		
		Source xmlSource = new DOMSource(document);
		
		StringWriter stringWriter = new StringWriter();
		StreamResult streamResult = new StreamResult(stringWriter);
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		
		Source xslt = new StreamSource(getStyleSheet(document));

               	Transformer transformer = transformerFactory.newTransformer(xslt);
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

		transformer.transform(xmlSource, streamResult);
		String outputString = stringWriter.toString();

		return outputString;

	}
	
	File getStyleSheet(Document document) throws Exception{
		MetadataType metadataType = getMetadataType(document);
		
		//getMetadataType throws an exception if not fgdc or iso19139
		if (metadataType.equals(MetadataType.FGDC)){
			return this.getFgdcStyleSheet().getFile();
		} else {
			return this.getIso19139StyleSheet().getFile();
		}
	}
	
    public static MetadataType getMetadataType(Document document) throws Exception {
        MetadataType metadataType = null;
        try {
            //<metstdn>FGDC Content Standards for Digital Geospatial Metadata
            //<metstdv>FGDC-STD-001-1998
        	NodeList rootNodes = document.getElementsByTagName("metadata");
        	if (rootNodes.getLength() > 0){ 
        		//if (document.getElementsByTagName("metstdn").item(0).getTextContent().toLowerCase().contains("fgdc")){
        			metadataType = MetadataType.FGDC;
        		//}
        	}
        } catch (Exception e){/*ignore*/
            //document.getElementsByTagName("metstdn").item(0).getTextContent().toLowerCase();
        }

        try {
		NodeList rootNodes = document.getElementsByTagName("MD_Metadata");
		NodeList altRootNodes = document.getElementsByTagName("MI_Metadata");
		int totalNodes = rootNodes.getLength() + altRootNodes.getLength();
		if (totalNodes > 0){
			metadataType = MetadataType.ISO_19139;
		}
        } catch (Exception e){/*ignore*/}

        if (metadataType == null){
            //throw an exception...metadata type is not supported
            throw new Exception("Metadata Type is not supported.");
        }
        return metadataType;
    }
/*
 * <ptcontac>
<cntinfo>
<cntorgp>
<cntorg>Harvard Map Collection, Harvard College Library</cntorg>
</cntorgp>
<cntpos>Harvard Geospatial Library</cntpos>
<cntaddr>
<addrtype>mailing and physical address</addrtype>
<address>Harvard Map Collection</address>
<address>Pusey Library</address>
<address>Harvard University</address>
<city>Cambridge</city>
<state>MA</state>
<postal>02138</postal>
<country>USA</country>
</cntaddr>
<cntvoice>617-495-2417</cntvoice>
<cntfax>617-496-0440</cntfax>
<cntemail>hgl_ref@hulmail.harvard.edu</cntemail>
<hours>Monday - Friday, 9:00 am - 4:00 pm EST-USA</hours>
</cntinfo>
</ptcontac>
 * 
 */
	public NodeList getContactNodeList(String layerSlug) throws Exception{
		String xmlString = this.getXMLStringFromSolr(layerSlug, "layer_slug_s");
		Document document = buildXMLDocFromString(layerSlug, xmlString);
		NodeList contactInfo = document.getElementsByTagName("ptcontac");
		for (int i = 0; i < contactInfo.getLength(); i++){
			Node currentNode = contactInfo.item(i);
			if (currentNode.getNodeName().equalsIgnoreCase("cntinfo")){
				return currentNode.getChildNodes();
			}
		}
		return null;
	}
	
	public Node getContactInfo(String layerSlug, String nodeName){
		NodeList contactInfo = null;
		try {
			contactInfo = this.getContactNodeList(layerSlug);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < contactInfo.getLength(); i++){
			Node currentNode = contactInfo.item(i);
			if (currentNode.getNodeName().equalsIgnoreCase(nodeName)){
				return currentNode;
			}
		}
		return null;
	}
	
	public String getContactPhoneNumber(String layerSlug){
		return this.getContactInfo(layerSlug, "cntvoice").getNodeValue().trim();
	}
	
	public String getContactName(String layerSlug){
		String contactName =  this.getContactInfo(layerSlug, "cntpos").getNodeValue().trim();
		return contactName;
	}
	
	public String getContactAddress(String layerSlug){
		Node addressNode = this.getContactInfo(layerSlug, "cntaddr");
		NodeList addressNodeList = addressNode.getChildNodes();
		
		String address = "";
		String city = "";
		String state = "";
		String postal = "";
		String country = "";
		for (int i = 0; i < addressNodeList.getLength(); i++){
			Node currentAddressLine = addressNodeList.item(i);
			if (currentAddressLine.getNodeName().equalsIgnoreCase("address")){
				address += currentAddressLine.getNodeValue().trim() + ", ";
			} else if (currentAddressLine.getNodeName().equalsIgnoreCase("state")){
				state = "";
				state += currentAddressLine.getNodeValue().trim();
			} else if (currentAddressLine.getNodeName().equalsIgnoreCase("postal")){
				postal = "";
				postal += currentAddressLine.getNodeValue().trim();
			} else if (currentAddressLine.getNodeName().equalsIgnoreCase("country")){
				country = "";
				country += currentAddressLine.getNodeValue().trim();
			} else if (currentAddressLine.getNodeName().equalsIgnoreCase("city")){
				city = "";
				city += currentAddressLine.getNodeValue().trim();
			}
		}
		String fullAddress = "";
		if (!address.isEmpty()){
			fullAddress += address;
		}
		if (!city.isEmpty()){
			fullAddress += city + ",";
		}
		if (!state.isEmpty()){
			fullAddress += state + " ";
		}
		if (!postal.isEmpty()){
			fullAddress += postal;
		}
		if (!country.isEmpty()){
			fullAddress += ", " + country;
		}
		return fullAddress;
	}

	public Resource getFgdcStyleSheet() {
		return fgdcStyleSheet;
	}

	public void setFgdcStyleSheet(Resource fgdcStyleSheet) {
		this.fgdcStyleSheet = fgdcStyleSheet;
	}

	public Resource getIso19139StyleSheet() {
		return iso19139StyleSheet;
	}

	public void setIso19139StyleSheet(Resource iso19139StyleSheet) {
		this.iso19139StyleSheet = iso19139StyleSheet;
	}
}
