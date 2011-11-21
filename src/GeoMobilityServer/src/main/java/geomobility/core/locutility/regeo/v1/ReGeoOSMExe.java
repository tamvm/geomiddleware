package geomobility.core.locutility.regeo.v1;

import geomobility.core.GlobalCore;
import geomobility.gisgraphy.builder.StreetSearchQueryBuilder;

import java.util.List;

import net.opengis.xls.v_1_1_0.ReverseGeocodeRequestType;

import com.gisgraphy.domain.geoloc.service.ServiceException;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.StreetSearchMode;
import com.gisgraphy.domain.geoloc.service.geoloc.StreetSearchEngine;
import com.gisgraphy.domain.geoloc.service.geoloc.StreetSearchQuery;
import com.gisgraphy.domain.repository.OpenStreetMapDao;
import com.gisgraphy.domain.valueobject.StreetSearchResultsDto;

/**
 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
 */
public class ReGeoOSMExe {
	private static final int NUM_RESULTS = 5;

	public static StreetSearchResultsDto execute(
			ReverseGeocodeRequestType requestType) throws ServiceException {
		List<Double> listDim = requestType.getPosition().getPoint().getPos()
				.getValue();

		StreetSearchQuery streetSearchQuery = StreetSearchQueryBuilder
				.getInstance().buildStreetSearchQuery(
						listDim.get(0).floatValue(),
						listDim.get(1).floatValue(), 100000, 1, NUM_RESULTS,
						null, null, false, true, null, null, null, "",
						StreetSearchMode.CONTAINS);

		StreetSearchEngine streetSearchEngine = new StreetSearchEngine(
				new OpenStreetMapDao(), null, GlobalCore.statsUsageService);

		return streetSearchEngine.executeQuery(streetSearchQuery);
	}
}
