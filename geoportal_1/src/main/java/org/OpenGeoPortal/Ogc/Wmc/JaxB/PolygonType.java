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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *         A Polygon is defined by an outer boundary and zero or more inner 
 *         boundaries which are in turn defined by LinearRings.
 *       
 * 
 * <p>Java class for PolygonType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PolygonType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.opengis.net/gml}AbstractGeometryType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.opengis.net/gml}outerBoundaryIs"/>
 *         &lt;element ref="{http://www.opengis.net/gml}innerBoundaryIs" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PolygonType", namespace = "http://www.opengis.net/gml", propOrder = {
    "outerBoundaryIs",
    "innerBoundaryIs"
})
public class PolygonType
    extends AbstractGeometryType
{

    @XmlElement(required = true)
    protected LinearRingMemberType outerBoundaryIs;
    protected List<LinearRingMemberType> innerBoundaryIs;

    /**
     * Gets the value of the outerBoundaryIs property.
     * 
     * @return
     *     possible object is
     *     {@link LinearRingMemberType }
     *     
     */
    public LinearRingMemberType getOuterBoundaryIs() {
        return outerBoundaryIs;
    }

    /**
     * Sets the value of the outerBoundaryIs property.
     * 
     * @param value
     *     allowed object is
     *     {@link LinearRingMemberType }
     *     
     */
    public void setOuterBoundaryIs(LinearRingMemberType value) {
        this.outerBoundaryIs = value;
    }

    /**
     * Gets the value of the innerBoundaryIs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the innerBoundaryIs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInnerBoundaryIs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LinearRingMemberType }
     * 
     * 
     */
    public List<LinearRingMemberType> getInnerBoundaryIs() {
        if (innerBoundaryIs == null) {
            innerBoundaryIs = new ArrayList<LinearRingMemberType>();
        }
        return this.innerBoundaryIs;
    }

}
