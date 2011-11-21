package geomobility.core.locutility.regeo;

import geomobility.core.locutility.QueryResultDto;
import geomobility.core.utils.StaticFunc;
import geomobility.data.osm.entity.Roads;
import geomobility.data.osm.entity.result.PointsResult;

import java.math.BigDecimal;
import java.math.BigInteger;

import net.opengis.gml.v_3_1_1.DirectPositionType;
import net.opengis.gml.v_3_1_1.PointType;
import net.opengis.xls.v_1_1_0.AddressType;
import net.opengis.xls.v_1_1_0.DistanceType;
import net.opengis.xls.v_1_1_0.ReverseGeocodePreferenceType;
import net.opengis.xls.v_1_1_0.ReverseGeocodeResponseType;
import net.opengis.xls.v_1_1_0.ReverseGeocodedLocationType;
import net.opengis.xls.v_1_1_0.StreetAddressType;
import net.opengis.xls.v_1_1_0.StreetNameType;

import com.gisgraphy.domain.valueobject.SRID;
import com.vividsolutions.jts.geom.Point;

/**
 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
 */
public class ReGeoBuilder {

	public static ReverseGeocodeResponseType buildResult(QueryResultDto<PointsResult> dto,
			ReverseGeocodePreferenceType preferenceType) {
		ReverseGeocodeResponseType responseType = new ReverseGeocodeResponseType();
		for (int i = 0; i < dto.getResult().size(); i++) {
			PointsResult p = dto.getResult().get(i);

			ReverseGeocodedLocationType locationType = new ReverseGeocodedLocationType();
			responseType.getReverseGeocodedLocation().add(locationType);

			// PointType
			PointType pointType = new PointType();
			locationType.setPoint(pointType);

			pointType.setGid(p.getOsm_id() + "");
			pointType.setId(p.getOsm_id() + "");
			pointType.setSrsName(SRID.WGS84_SRID.getSRID() + "");

			DirectPositionType directPositionType = new DirectPositionType();
			pointType.setPos(directPositionType);
			directPositionType.setSrsName(SRID.WGS84_SRID.getSRID() + "");
			directPositionType.setSrsDimension(new BigInteger(2 + ""));

			Point point84 = p.getPoint84();
			if (point84 != null) {
				directPositionType.getValue().add(point84.getY());
				directPositionType.getValue().add(point84.getX());
			}

			// AddressType
			AddressType addressType = new AddressType();
			locationType.setAddress(addressType);

			addressType.setCountryCode(p.getCountrycode());
			addressType.setAddressee(p.getName());
			addressType.setFreeFormAddress(p.getInterpolation());

			StreetAddressType streetAddressType = new StreetAddressType();
			addressType.setStreetAddress(streetAddressType);
			for (int j = 0; j < p.getRoads().size(); j++) {
				Roads roads = p.getRoads().get(j);
				if (roads!=null){
					StreetNameType streetNameType = new StreetNameType();
					streetNameType.setValue(StaticFunc.isNOE(roads.getInterpolation())? roads.getName() : roads.getInterpolation());
					streetAddressType.getStreet().add(streetNameType);
				}
			}

			DistanceType distanceType = new DistanceType();
			distanceType.setValue(new BigDecimal(p.getDistance() + ""));
			locationType.setSearchCentreDistance(distanceType);
		}

		return responseType;
	}
}
