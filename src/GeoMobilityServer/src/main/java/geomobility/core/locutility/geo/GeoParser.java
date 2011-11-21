package geomobility.core.locutility.geo;

import geomobility.core.GlobalCore;
import geomobility.core.exception.DirectoryException;
import geomobility.localization.I18N;

import java.io.StringReader;
import java.math.BigInteger;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;

import net.opengis.xls.v_1_1_0.AbstractBodyType;
import net.opengis.xls.v_1_1_0.AbstractRequestParametersType;
import net.opengis.xls.v_1_1_0.GeocodeRequestType;
import net.opengis.xls.v_1_1_0.RequestType;
import net.opengis.xls.v_1_1_0.XLSType;

/**
 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
 */
public class GeoParser {
	public static GeocodeRequestType parse(String xml)
			throws DirectoryException {
		JAXBElement<XLSType> element;
		try {
			element = (JAXBElement<XLSType>) GlobalCore.unmarshaller
					.unmarshal(new StringReader(xml));
			XLSType item = element.getValue();
			List<JAXBElement<? extends AbstractBodyType>> listRT = item
					.getBody();
			int sizeBody = listRT.size();

			JAXBElement<RequestType> reqType = (JAXBElement<RequestType>) listRT
					.get(0);

			Class<RequestType> declaredType = reqType.getDeclaredType();
			QName name = reqType.getName();
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

			GeocodeRequestType geocodeRequestType = (GeocodeRequestType) requestParameters
					.getValue();

			return geocodeRequestType;
		} catch (JAXBException e) {
			e.printStackTrace();
			throw new DirectoryException(I18N.m
					.getString("cannot_parse_geocode_request"));
		}
	}
}
