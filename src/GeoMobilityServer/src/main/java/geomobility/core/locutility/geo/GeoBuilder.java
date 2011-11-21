package geomobility.core.locutility.geo;

import geomobility.core.locutility.QueryResultDto;
import geomobility.data.osm.entity.result.RoadsResult;

import java.math.BigInteger;
import java.util.List;

import net.opengis.gml.v_3_1_1.DirectPositionType;
import net.opengis.gml.v_3_1_1.PointType;
import net.opengis.xls.v_1_1_0.AddressType;
import net.opengis.xls.v_1_1_0.GeocodeResponseListType;
import net.opengis.xls.v_1_1_0.GeocodeResponseType;
import net.opengis.xls.v_1_1_0.GeocodedAddressType;
import net.opengis.xls.v_1_1_0.StreetAddressType;
import net.opengis.xls.v_1_1_0.StreetNameType;

import com.vividsolutions.jts.geom.Point;

/**
 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
 */
public class GeoBuilder {
	public static GeocodeResponseType buildResult(QueryResultDto<List<RoadsResult>> dto) {
		GeocodeResponseType responseType = new GeocodeResponseType();
		for (int i = 0; i < dto.getResult().size(); i++) {
			List<RoadsResult> listRoads = dto.getResult().get(i);
			GeocodeResponseListType geocodeResponseListType = new GeocodeResponseListType();
			responseType.getGeocodeResponseList().add(geocodeResponseListType);
			geocodeResponseListType.setNumberOfGeocodedAddresses(new BigInteger(listRoads.size() + ""));

			for (RoadsResult roads : listRoads) {
				Point p = roads.getInterpolatePoint();
				GeocodedAddressType geocodedAddressType = new GeocodedAddressType();
				geocodeResponseListType.getGeocodedAddress().add(geocodedAddressType);

				PointType pointType = new PointType();
				geocodedAddressType.setPoint(pointType);
				DirectPositionType directPositionType = new DirectPositionType();
				pointType.setPos(directPositionType);
				directPositionType.getValue().add(p.getX());
				directPositionType.getValue().add(p.getY());

				pointType.setGid(roads.getOsm_id() + "");
				pointType.setId(roads.getOsm_id() + "");

				AddressType addressType = new AddressType();
				geocodedAddressType.setAddress(addressType);

				addressType.setCountryCode(roads.getCountrycode());
				StreetAddressType streetAddressType = new StreetAddressType();
				addressType.setStreetAddress(streetAddressType);

				StreetNameType streetNameType = new StreetNameType();
				streetNameType.setValue(roads.getName());
				streetAddressType.getStreet().add(streetNameType);
			}
		}

		return responseType;
	}
}
