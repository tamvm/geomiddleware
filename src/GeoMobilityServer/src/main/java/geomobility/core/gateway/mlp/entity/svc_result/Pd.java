//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.12.28 at 09:45:38 AM ICT 
//


package geomobility.core.gateway.mlp.entity.svc_result;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for pd complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="pd">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}time"/>
 *         &lt;element ref="{}shape"/>
 *         &lt;sequence minOccurs="0">
 *           &lt;element ref="{}alt"/>
 *           &lt;element ref="{}alt_unc" minOccurs="0"/>
 *         &lt;/sequence>
 *         &lt;element ref="{}speed" minOccurs="0"/>
 *         &lt;element ref="{}direction" minOccurs="0"/>
 *         &lt;element ref="{}lev_conf" minOccurs="0"/>
 *         &lt;element ref="{}qos_not_met" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pd", propOrder = {
    "time",
    "shape",
    "alt",
    "altUnc",
    "speed",
    "direction",
    "levConf",
    "qosNotMet"
})
public class Pd {

    @XmlElement(required = true)
    protected Time time;
    @XmlElement(required = true)
    protected Shape shape;
    protected Alt alt;
    @XmlElement(name = "alt_unc")
    protected AltUnc altUnc;
    protected Speed speed;
    protected Direction direction;
    @XmlElement(name = "lev_conf")
    protected LevConf levConf;
    @XmlElement(name = "qos_not_met")
    protected QosNotMet qosNotMet;

    /**
     * Gets the value of the time property.
     * 
     * @return
     *     possible object is
     *     {@link Time }
     *     
     */
    public Time getTime() {
        return time;
    }

    /**
     * Sets the value of the time property.
     * 
     * @param value
     *     allowed object is
     *     {@link Time }
     *     
     */
    public void setTime(Time value) {
        this.time = value;
    }

    /**
     * Gets the value of the shape property.
     * 
     * @return
     *     possible object is
     *     {@link Shape }
     *     
     */
    public Shape getShape() {
        return shape;
    }

    /**
     * Sets the value of the shape property.
     * 
     * @param value
     *     allowed object is
     *     {@link Shape }
     *     
     */
    public void setShape(Shape value) {
        this.shape = value;
    }

    /**
     * Gets the value of the alt property.
     * 
     * @return
     *     possible object is
     *     {@link Alt }
     *     
     */
    public Alt getAlt() {
        return alt;
    }

    /**
     * Sets the value of the alt property.
     * 
     * @param value
     *     allowed object is
     *     {@link Alt }
     *     
     */
    public void setAlt(Alt value) {
        this.alt = value;
    }

    /**
     * Gets the value of the altUnc property.
     * 
     * @return
     *     possible object is
     *     {@link AltUnc }
     *     
     */
    public AltUnc getAltUnc() {
        return altUnc;
    }

    /**
     * Sets the value of the altUnc property.
     * 
     * @param value
     *     allowed object is
     *     {@link AltUnc }
     *     
     */
    public void setAltUnc(AltUnc value) {
        this.altUnc = value;
    }

    /**
     * Gets the value of the speed property.
     * 
     * @return
     *     possible object is
     *     {@link Speed }
     *     
     */
    public Speed getSpeed() {
        return speed;
    }

    /**
     * Sets the value of the speed property.
     * 
     * @param value
     *     allowed object is
     *     {@link Speed }
     *     
     */
    public void setSpeed(Speed value) {
        this.speed = value;
    }

    /**
     * Gets the value of the direction property.
     * 
     * @return
     *     possible object is
     *     {@link Direction }
     *     
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Sets the value of the direction property.
     * 
     * @param value
     *     allowed object is
     *     {@link Direction }
     *     
     */
    public void setDirection(Direction value) {
        this.direction = value;
    }

    /**
     * Gets the value of the levConf property.
     * 
     * @return
     *     possible object is
     *     {@link LevConf }
     *     
     */
    public LevConf getLevConf() {
        return levConf;
    }

    /**
     * Sets the value of the levConf property.
     * 
     * @param value
     *     allowed object is
     *     {@link LevConf }
     *     
     */
    public void setLevConf(LevConf value) {
        this.levConf = value;
    }

    /**
     * Gets the value of the qosNotMet property.
     * 
     * @return
     *     possible object is
     *     {@link QosNotMet }
     *     
     */
    public QosNotMet getQosNotMet() {
        return qosNotMet;
    }

    /**
     * Sets the value of the qosNotMet property.
     * 
     * @param value
     *     allowed object is
     *     {@link QosNotMet }
     *     
     */
    public void setQosNotMet(QosNotMet value) {
        this.qosNotMet = value;
    }

}
