//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.06.18 at 01:01:16 PM EDT 
//


package org.OpenGeoPortal.Ogc.Wmc.JaxB;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LayerType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LayerType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Server" type="{http://www.opengis.net/context}ServerType"/>
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Title" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Abstract" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DataURL" type="{http://www.opengis.net/context}URLType" minOccurs="0"/>
 *         &lt;element name="MetadataURL" type="{http://www.opengis.net/context}URLType" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/sld}MinScaleDenominator" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/sld}MaxScaleDenominator" minOccurs="0"/>
 *         &lt;element name="SRS" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="FormatList" type="{http://www.opengis.net/context}FormatListType" minOccurs="0"/>
 *         &lt;element name="StyleList" type="{http://www.opengis.net/context}StyleListType" minOccurs="0"/>
 *         &lt;element name="DimensionList" type="{http://www.opengis.net/context}DimensionListType" minOccurs="0"/>
 *         &lt;element name="Extension" type="{http://www.opengis.net/context}ExtensionType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="queryable" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="hidden" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LayerType", namespace = "http://www.opengis.net/context", propOrder = {
    "server",
    "name",
    "title",
    "_abstract",
    "dataURL",
    "metadataURL",
    "minScaleDenominator",
    "maxScaleDenominator",
    "srs",
    "formatList",
    "styleList",
    "dimensionList",
    "extension"
})
public class LayerType {

    @XmlElement(name = "Server", required = true)
    protected ServerType server;
    @XmlElement(name = "Name", required = true)
    protected String name;
    @XmlElement(name = "Title", required = true)
    protected String title;
    @XmlElement(name = "Abstract")
    protected String _abstract;
    @XmlElement(name = "DataURL")
    protected URLType dataURL;
    @XmlElement(name = "MetadataURL")
    protected URLType metadataURL;
    @XmlElement(name = "MinScaleDenominator", namespace = "http://www.opengis.net/sld")
    protected Double minScaleDenominator;
    @XmlElement(name = "MaxScaleDenominator", namespace = "http://www.opengis.net/sld")
    protected Double maxScaleDenominator;
    @XmlElement(name = "SRS")
    protected List<String> srs;
    @XmlElement(name = "FormatList")
    protected FormatListType formatList;
    @XmlElement(name = "StyleList")
    protected StyleListType styleList;
    @XmlElement(name = "DimensionList")
    protected DimensionListType dimensionList;
    @XmlElement(name = "Extension")
    protected ExtensionType extension;
    @XmlAttribute(required = true)
    protected boolean queryable;
    @XmlAttribute(required = true)
    protected boolean hidden;

    /**
     * Gets the value of the server property.
     * 
     * @return
     *     possible object is
     *     {@link ServerType }
     *     
     */
    public ServerType getServer() {
        return server;
    }

    /**
     * Sets the value of the server property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServerType }
     *     
     */
    public void setServer(ServerType value) {
        this.server = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the abstract property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAbstract() {
        return _abstract;
    }

    /**
     * Sets the value of the abstract property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAbstract(String value) {
        this._abstract = value;
    }

    /**
     * Gets the value of the dataURL property.
     * 
     * @return
     *     possible object is
     *     {@link URLType }
     *     
     */
    public URLType getDataURL() {
        return dataURL;
    }

    /**
     * Sets the value of the dataURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link URLType }
     *     
     */
    public void setDataURL(URLType value) {
        this.dataURL = value;
    }

    /**
     * Gets the value of the metadataURL property.
     * 
     * @return
     *     possible object is
     *     {@link URLType }
     *     
     */
    public URLType getMetadataURL() {
        return metadataURL;
    }

    /**
     * Sets the value of the metadataURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link URLType }
     *     
     */
    public void setMetadataURL(URLType value) {
        this.metadataURL = value;
    }

    /**
     * Gets the value of the minScaleDenominator property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getMinScaleDenominator() {
        return minScaleDenominator;
    }

    /**
     * Sets the value of the minScaleDenominator property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setMinScaleDenominator(Double value) {
        this.minScaleDenominator = value;
    }

    /**
     * Gets the value of the maxScaleDenominator property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getMaxScaleDenominator() {
        return maxScaleDenominator;
    }

    /**
     * Sets the value of the maxScaleDenominator property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setMaxScaleDenominator(Double value) {
        this.maxScaleDenominator = value;
    }

    /**
     * Gets the value of the srs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the srs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSRS().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getSRS() {
        if (srs == null) {
            srs = new ArrayList<String>();
        }
        return this.srs;
    }

    /**
     * Gets the value of the formatList property.
     * 
     * @return
     *     possible object is
     *     {@link FormatListType }
     *     
     */
    public FormatListType getFormatList() {
        return formatList;
    }

    /**
     * Sets the value of the formatList property.
     * 
     * @param value
     *     allowed object is
     *     {@link FormatListType }
     *     
     */
    public void setFormatList(FormatListType value) {
        this.formatList = value;
    }

    /**
     * Gets the value of the styleList property.
     * 
     * @return
     *     possible object is
     *     {@link StyleListType }
     *     
     */
    public StyleListType getStyleList() {
        return styleList;
    }

    /**
     * Sets the value of the styleList property.
     * 
     * @param value
     *     allowed object is
     *     {@link StyleListType }
     *     
     */
    public void setStyleList(StyleListType value) {
        this.styleList = value;
    }

    /**
     * Gets the value of the dimensionList property.
     * 
     * @return
     *     possible object is
     *     {@link DimensionListType }
     *     
     */
    public DimensionListType getDimensionList() {
        return dimensionList;
    }

    /**
     * Sets the value of the dimensionList property.
     * 
     * @param value
     *     allowed object is
     *     {@link DimensionListType }
     *     
     */
    public void setDimensionList(DimensionListType value) {
        this.dimensionList = value;
    }

    /**
     * Gets the value of the extension property.
     * 
     * @return
     *     possible object is
     *     {@link ExtensionType }
     *     
     */
    public ExtensionType getExtension() {
        return extension;
    }

    /**
     * Sets the value of the extension property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExtensionType }
     *     
     */
    public void setExtension(ExtensionType value) {
        this.extension = value;
    }

    /**
     * Gets the value of the queryable property.
     * 
     */
    public boolean isQueryable() {
        return queryable;
    }

    /**
     * Sets the value of the queryable property.
     * 
     */
    public void setQueryable(boolean value) {
        this.queryable = value;
    }

    /**
     * Gets the value of the hidden property.
     * 
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * Sets the value of the hidden property.
     * 
     */
    public void setHidden(boolean value) {
        this.hidden = value;
    }

}
