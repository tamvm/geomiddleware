package geomobility.core.locutility.regeo;

import geomobility.core.locutility.GlobalLocUtility;
import geomobility.core.locutility.LocUtilEngine;
import geomobility.core.locutility.QueryResultDto;
import geomobility.core.utils.Config;
import geomobility.data.osm.entity.result.PointsResult;

import java.util.List;

import net.opengis.xls.v_1_1_0.ReverseGeocodePreferenceType;
import net.opengis.xls.v_1_1_0.ReverseGeocodeRequestType;

import com.gisgraphy.domain.geoloc.service.fulltextsearch.StreetSearchMode;

/**
 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
 */
public class ReGeoExe {
	public static QueryResultDto<PointsResult> execute(ReverseGeocodeRequestType requestType) {
		List<Double> listDim = requestType.getPosition().getPoint().getPos().getValue();

		QueryResultDto<PointsResult> result = new QueryResultDto<PointsResult>();
		long start = System.currentTimeMillis();

		ReverseGeocodePreferenceType type = ReverseGeocodePreferenceType.STREET_ADDRESS;
		if (requestType.getReverseGeocodePreference().size()>0){
			type = requestType.getReverseGeocodePreference().get(0);
		}
		
		result.setResult(LocUtilEngine.getInstance().getNearestAndDistanceFrom(listDim.get(0).floatValue(), 
			listDim.get(1).floatValue(),Config.REGEO_DEFAULT_DISTANCE, GlobalLocUtility.FROM, 
			GlobalLocUtility.MAX_RESULTS, "", StreetSearchMode.CONTAINS, true, "", 
			type==ReverseGeocodePreferenceType.STREET_ADDRESS?false:true));
		result.setTime(System.currentTimeMillis() - start);
		return result;
	}
}
