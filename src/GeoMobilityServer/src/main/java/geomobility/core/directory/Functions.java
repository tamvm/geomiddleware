package geomobility.core.directory;

import geomobility.core.utils.StaticFunc;

import java.io.StringReader;
import java.math.BigInteger;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import net.opengis.xls.v_1_1_0.AbstractBodyType;
import net.opengis.xls.v_1_1_0.AbstractRequestParametersType;
import net.opengis.xls.v_1_1_0.DirectoryRequestType;
import net.opengis.xls.v_1_1_0.DistanceUnitType;
import net.opengis.xls.v_1_1_0.NearestType;
import net.opengis.xls.v_1_1_0.POILocationType;
import net.opengis.xls.v_1_1_0.POIPropertiesType;
import net.opengis.xls.v_1_1_0.RequestType;
import net.opengis.xls.v_1_1_0.SortDirectionType;
import net.opengis.xls.v_1_1_0.WithinDistanceType;
import net.opengis.xls.v_1_1_0.XLSType;

public class Functions {

	public static void main(String[] args) {
		DirectoryRequestType directoryRequestType = parseDirectoryType("data/DistanceReq.xml");
		searchPOI(directoryRequestType);
	}

	private static DirectoryRequestType parseDirectoryType(String string) {
		// TODO Auto-generated method stub
		JAXBElement<XLSType> element;
		String xml = StaticFunc.getContent(string);
		try {
			JAXBContext context = JAXBContext
					.newInstance("geomobility.core.directory.openls.entity");
			Unmarshaller un = context.createUnmarshaller();

			element = (JAXBElement<XLSType>) un
					.unmarshal(new StringReader(xml));
			XLSType item = element.getValue();
			List<JAXBElement<? extends AbstractBodyType>> listRT = item.getBody();
			int sizeBody = listRT.size();

			JAXBElement<RequestType> reqType = (JAXBElement<RequestType>) listRT.get(0);

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
			DirectoryRequestType directoryRequestType = (DirectoryRequestType) requestParameters.getValue();
			
			return directoryRequestType;
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void searchPOI(DirectoryRequestType direct) {

		POILocationType poiLocationType = direct.getPOILocation();
		JAXBElement<? extends POIPropertiesType> poiPropertiesType = (JAXBElement<? extends POIPropertiesType>) direct
				.getPOISelectionCriteria();

		DistanceUnitType distanceUnitType = direct.getDistanceUnit();
		String sortCri = direct.getSortCriteria();
		SortDirectionType sortDirectionType = direct.getSortDirection();

		if (poiLocationType == null) {
			requestAttributeSearch(poiPropertiesType);
		} else {
			requestLocationSearch(poiLocationType, poiPropertiesType);
		}
	}

	private static void requestLocationSearch(POILocationType poiLocationType,
			JAXBElement<? extends POIPropertiesType> poiPropertiesType) {
		// TODO Auto-generated method stub
		NearestType nearestType = poiLocationType.getNearest();
		WithinDistanceType withinDistanceType = poiLocationType
				.getWithinDistance();
		// poiLocationType.getAddress();
		// poiLocationType.getWithinBoundary();
	}

	private static void requestAttributeSearch(
			JAXBElement<? extends POIPropertiesType> poiPropertiesType) {
		// TODO Auto-generated method stub

	}

}
