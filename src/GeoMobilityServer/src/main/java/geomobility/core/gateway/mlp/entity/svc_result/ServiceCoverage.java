//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.12.28 at 09:45:38 AM ICT 
//


package geomobility.core.gateway.mlp.entity.svc_result;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for service_coverage complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="service_coverage">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;sequence maxOccurs="unbounded">
 *           &lt;element ref="{}cc"/>
 *           &lt;element ref="{}ndc" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;/sequence>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "service_coverage", propOrder = {
    "ccAndNdc"
})
public class ServiceCoverage {

    @XmlElements({
        @XmlElement(name = "cc", required = true, type = Cc.class),
        @XmlElement(name = "ndc", required = true, type = Ndc.class)
    })
    protected List<Object> ccAndNdc;

    /**
     * Gets the value of the ccAndNdc property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ccAndNdc property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCcAndNdc().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Cc }
     * {@link Ndc }
     * 
     * 
     */
    public List<Object> getCcAndNdc() {
        if (ccAndNdc == null) {
            ccAndNdc = new ArrayList<Object>();
        }
        return this.ccAndNdc;
    }

}
