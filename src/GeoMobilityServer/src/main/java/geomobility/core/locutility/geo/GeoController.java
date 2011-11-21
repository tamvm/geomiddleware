package geomobility.core.locutility.geo;

import geomobility.core.GlobalCore;
import geomobility.core.exception.GeoException;
import geomobility.core.locutility.QueryResultDto;
import geomobility.core.utils.StaticFunc;
import geomobility.data.osm.entity.result.RoadsResult;

import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;

import net.opengis.xls.v_1_1_0.GeocodeRequestType;
import net.opengis.xls.v_1_1_0.GeocodeResponseType;
import net.opengis.xls.v_1_1_0.ResponseType;
import net.opengis.xls.v_1_1_0.XLSType;

/**
 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
 */
public class GeoController {
	public static String doController(String xml) throws GeoException, HibernateException, SQLException {
		XLSType xlsType = StaticFunc.getXLSType(xml);

		List<GeocodeRequestType> listGeocodeRequestTypes = StaticFunc.parse(xlsType);
		xlsType.getBody().clear();

		for (GeocodeRequestType geocodeRequestType : listGeocodeRequestTypes) {
			// Build Query
			QueryResultDto<List<RoadsResult>> dto = GeoExe.execute(geocodeRequestType);

			// Builder the output
			GeocodeResponseType geocodeResponseType = GeoBuilder.buildResult(dto);

			ResponseType responseType = new ResponseType();
			responseType.setResponseParameters(GlobalCore.objectFactoryXLS.createGeocodeResponse(geocodeResponseType));
			xlsType.getBody().add(GlobalCore.objectFactoryXLS.createResponse(responseType));
		}

		// Output to String
		OutputStream outputStream = StaticFunc.buildXLSResult(xlsType, GlobalCore.output);
		return outputStream.toString();
	}
	
	public static List<GeocodeResponseType> doController2(String xml) throws GeoException, HibernateException, SQLException{
		List<GeocodeResponseType> listResult = new ArrayList<GeocodeResponseType>();
		XLSType xlsType = StaticFunc.getXLSType(xml);

		List<GeocodeRequestType> listGeocodeRequestTypes = StaticFunc.parse(xlsType);

		for (GeocodeRequestType geocodeRequestType : listGeocodeRequestTypes) {
			// Build Query
			QueryResultDto<List<RoadsResult>> dto = GeoExe.execute(geocodeRequestType);

			// Builder the output
			GeocodeResponseType geocodeResponseType = GeoBuilder.buildResult(dto);
			listResult.add(geocodeResponseType);
		}

		return listResult;
	}
	
	public static GeocodeResponseType doController(GeocodeRequestType geocodeRequestType) throws HibernateException, SQLException{
		// Build Query
		QueryResultDto<List<RoadsResult>> dto = GeoExe.execute(geocodeRequestType);

		// Builder the output
		GeocodeResponseType geocodeResponseType = GeoBuilder.buildResult(dto);
		return geocodeResponseType;
	}

}
