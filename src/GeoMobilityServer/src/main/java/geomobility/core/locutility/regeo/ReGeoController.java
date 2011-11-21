package geomobility.core.locutility.regeo;

import geomobility.core.GlobalCore;
import geomobility.core.exception.GeoException;
import geomobility.core.locutility.QueryResultDto;
import geomobility.core.utils.StaticFunc;
import geomobility.data.osm.entity.result.PointsResult;

import java.io.OutputStream;
import java.util.List;

import net.opengis.xls.v_1_1_0.ResponseType;
import net.opengis.xls.v_1_1_0.ReverseGeocodePreferenceType;
import net.opengis.xls.v_1_1_0.ReverseGeocodeRequestType;
import net.opengis.xls.v_1_1_0.ReverseGeocodeResponseType;
import net.opengis.xls.v_1_1_0.XLSType;

/**
 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
 */
public class ReGeoController {

	public static String doController(String xml) throws GeoException {
		XLSType xlsType = StaticFunc.getXLSType(xml);

		List<ReverseGeocodeRequestType> listReGeocodeRequestTypes = StaticFunc.parse(xlsType);
		xlsType.getBody().clear();

		for (ReverseGeocodeRequestType reverseGeocodeRequestType : listReGeocodeRequestTypes) {
			// Build Query
			QueryResultDto<PointsResult> dto = ReGeoExe.execute(reverseGeocodeRequestType);

			// Builder the output
			// TODO StreetIntersection has not been supported
			ReverseGeocodeResponseType reverseGeocodeResponseType = ReGeoBuilder.buildResult(dto, 
				reverseGeocodeRequestType.getReverseGeocodePreference().isEmpty() ? 
				ReverseGeocodePreferenceType.STREET_ADDRESS : reverseGeocodeRequestType.getReverseGeocodePreference().get(0));

			ResponseType responseType = new ResponseType();
			responseType.setResponseParameters(
					GlobalCore.objectFactoryXLS.createReverseGeocodeResponse(reverseGeocodeResponseType));
			xlsType.getBody().add(GlobalCore.objectFactoryXLS.createResponse(responseType));
		}

		// Output to String
		OutputStream outputStream = StaticFunc.buildXLSResult(xlsType,GlobalCore.output);
		return outputStream.toString();
	}
}
