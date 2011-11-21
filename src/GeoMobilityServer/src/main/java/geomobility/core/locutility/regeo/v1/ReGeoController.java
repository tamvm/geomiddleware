package geomobility.core.locutility.regeo.v1;

import geomobility.core.exception.GeoException;
import geomobility.core.exception.LocUtilityException;
import geomobility.core.utils.StaticFunc;
import net.opengis.xls.v_1_1_0.ReverseGeocodeRequestType;

import com.gisgraphy.domain.geoloc.service.ServiceException;
import com.gisgraphy.domain.valueobject.StreetSearchResultsDto;

/**
 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
 */
public class ReGeoController {
	public static void main(String[] args) throws LocUtilityException, ServiceException {
		String xml = StaticFunc
				.getContent("data/loc_util/regeocode/full_adr.xml");

		try {
			ReGeoController.doController(xml);
		} catch (GeoException e) {
			e.printStackTrace();
		}
	}

	public static void doController(String xml) throws  GeoException, ServiceException {
		ReverseGeocodeRequestType reverseGeocodeRequestType = StaticFunc.parse(xml);

		// Build Query
		StreetSearchResultsDto dto = ReGeoOSMExe.execute(reverseGeocodeRequestType);

		// Builder the output
		System.out.println(ReGeoBuilder.buildResult(dto));
	}
}
