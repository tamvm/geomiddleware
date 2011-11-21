package geomobility.core.gateway.openls;

import java.io.StringReader;
import java.math.BigInteger;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import net.opengis.xls.v_1_1_0.AbstractBodyType;
import net.opengis.xls.v_1_1_0.AbstractRequestParametersType;
import net.opengis.xls.v_1_1_0.RequestType;
import net.opengis.xls.v_1_1_0.SLIRType;
import net.opengis.xls.v_1_1_0.XLSType;

/**
 * @author VoMinhTam
 */
public class OLSInput extends OLSMessage {
	public static Unmarshaller u = null;
	{
		try {
			u = jcOLS.createUnmarshaller();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @deprecated
	 * @param xml
	 * @return
	 */
	public static SLIRType parseSLIRType(String xml) {
		try {
			JAXBElement<XLSType> element;
			element = (JAXBElement<XLSType>) u.unmarshal(new StringReader(xml));
			XLSType item = element.getValue();
			List<JAXBElement<? extends AbstractBodyType>> listRT = item
					.getBody();
			int sizeBody = listRT.size();

			JAXBElement<RequestType> reqType = (JAXBElement<RequestType>) listRT
					.get(0);

			Class<RequestType> declaredType = reqType.getDeclaredType();
			QName name = reqType.getName();
			// QName nameQ = new QName(namespaceURI, localPart, prefix)
			boolean nil = reqType.isNil();
			Class scope = reqType.getScope();
			RequestType value = reqType.getValue();
			BigInteger maxinumResponses = value.getMaximumResponses();
			String methodName = value.getMethodName();
			String requestID = value.getRequestID();
			JAXBElement<? extends AbstractRequestParametersType> requestParameters = value
					.getRequestParameters();
			String version = value.getVersion();

			Class<? extends AbstractRequestParametersType> declaredTypeOfValueRequestParameters = requestParameters
					.getDeclaredType();
			SLIRType slirType = (SLIRType) requestParameters.getValue();
			return slirType;
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
