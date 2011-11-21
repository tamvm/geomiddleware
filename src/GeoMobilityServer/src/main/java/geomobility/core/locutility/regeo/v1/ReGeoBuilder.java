package geomobility.core.locutility.regeo.v1;

import geomobility.core.GlobalCore;
import geomobility.core.locutility.QueryResultDto;
import geomobility.data.osm.entity.Points;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import net.opengis.gml.v_3_1_1.DirectPositionType;
import net.opengis.gml.v_3_1_1.PointType;
import net.opengis.xls.v_1_1_0.AddressType;
import net.opengis.xls.v_1_1_0.DistanceType;
import net.opengis.xls.v_1_1_0.ReverseGeocodeResponseType;
import net.opengis.xls.v_1_1_0.ReverseGeocodedLocationType;
import net.opengis.xls.v_1_1_0.StreetAddressType;
import net.opengis.xls.v_1_1_0.StreetNameType;

import com.gisgraphy.domain.valueobject.StreetDistance;
import com.gisgraphy.domain.valueobject.StreetSearchResultsDto;

/**
 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
 */
public class ReGeoBuilder {
	public static String buildResult(StreetSearchResultsDto dto) {
		ReverseGeocodeResponseType responseType = new ReverseGeocodeResponseType();
		for (int i = 0; i < dto.getNumFound(); i++) {
			//
			StreetDistance sD = dto.getResult().get(i);

			//
			ReverseGeocodedLocationType locationType = new ReverseGeocodedLocationType();
			responseType.getReverseGeocodedLocation().add(locationType);

			// PointType
			PointType pointType = new PointType();
			locationType.setPoint(pointType);

			pointType.setGid(sD.getGid() + "");
			pointType.setId(sD.getGid() + "");
			pointType.setSrsName(sD.getLocation().getSRID() + "");

			DirectPositionType directPositionType = new DirectPositionType();
			pointType.setPos(directPositionType);
			directPositionType.setSrsName(sD.getLocation().getSRID() + "");
			directPositionType.setSrsDimension(new BigInteger(2 + ""));
			directPositionType.getValue().add(sD.getLat());
			directPositionType.getValue().add(sD.getLng());

			// AddressType
			AddressType addressType = new AddressType();
			locationType.setAddress(addressType);

			addressType.setCountryCode(sD.getCountryCode());
			addressType.setAddressee(sD.getName());
			addressType.setFreeFormAddress(sD.getName());

			// TODO Add later
			StreetAddressType streetAddressType = new StreetAddressType();
			addressType.setStreetAddress(streetAddressType);
			StreetNameType streetNameType = new StreetNameType();
			streetNameType.setValue(sD.getName());
			streetAddressType.getStreet().add(streetNameType);

			DistanceType distanceType = new DistanceType();
			distanceType.setValue(new BigDecimal(sD.getDistance() + ""));
			locationType.setSearchCentreDistance(distanceType);

		}

		try {
			JAXBElement<ReverseGeocodeResponseType> jaxbElement = GlobalCore.objectFactoryXLS
					.createReverseGeocodeResponse(responseType);
			GlobalCore.marshaller.marshal(jaxbElement, GlobalCore.output);

			return GlobalCore.output.toString();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String buildResult(QueryResultDto<Points> dto) {
		ReverseGeocodeResponseType responseType = new ReverseGeocodeResponseType();

		return null;
	}
}
