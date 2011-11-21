package geomobility.core.directory;

import geomobility.core.directory.dto.AttSearchResultDto;
import geomobility.core.directory.dto.LocSearchResultDto;

import java.math.BigDecimal;
import java.math.BigInteger;

import net.opengis.gml.v_3_1_1.DirectPositionType;
import net.opengis.gml.v_3_1_1.PointType;
import net.opengis.xls.v_1_1_0.AddressType;
import net.opengis.xls.v_1_1_0.DirectoryResponseType;
import net.opengis.xls.v_1_1_0.DistanceType;
import net.opengis.xls.v_1_1_0.POIAttributeListType;
import net.opengis.xls.v_1_1_0.POIInfoListType;
import net.opengis.xls.v_1_1_0.POIInfoType;
import net.opengis.xls.v_1_1_0.POIWithDistanceType;
import net.opengis.xls.v_1_1_0.PointOfInterestType;

import com.gisgraphy.domain.geoloc.entity.GisFeature;
import com.gisgraphy.domain.valueobject.GisFeatureDistance;

/**
 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
 */
public class DBuilder {

	public static DirectoryResponseType buildLoc(LocSearchResultDto dto) {
		DirectoryResponseType directoryResponseType = new DirectoryResponseType();

		for (int i = 0; i < dto.getNumFound(); i++) {
			GisFeatureDistance gFD = dto.getResult().get(i);

			POIWithDistanceType poiWithDistanceType = new POIWithDistanceType();
			directoryResponseType.getPOIContext().add(poiWithDistanceType);
			PointOfInterestType pointOfInterestType = new PointOfInterestType();
			poiWithDistanceType.setPOI(pointOfInterestType);

			DistanceType distanceType = new DistanceType();
			poiWithDistanceType.setDistance(distanceType);

			if (gFD.getDistance() != null)
				distanceType.setValue(new BigDecimal(gFD.getDistance().doubleValue()+ ""));

			pointOfInterestType.setID(gFD.getId() + "");
			pointOfInterestType.setPOIName(gFD.getName());
			pointOfInterestType.setPhoneNumber("");
			pointOfInterestType.setDescription("");

			pointOfInterestType.setPOIAttributeList(getPOIAttributeListType(gFD));
			pointOfInterestType.setAddress(getAddressType(gFD));
			pointOfInterestType.setPoint(getPointType(gFD));
		}

		return directoryResponseType;
	}

	public static DirectoryResponseType buildAtt(AttSearchResultDto dto) {
		DirectoryResponseType directoryResponseType = new DirectoryResponseType();

		for (int i = 0; i < dto.getNumFound(); i++) {
			GisFeature gFD = dto.getResult().get(i);

			POIWithDistanceType poiWithDistanceType = new POIWithDistanceType();
			directoryResponseType.getPOIContext().add(poiWithDistanceType);
			PointOfInterestType pointOfInterestType = new PointOfInterestType();
			poiWithDistanceType.setPOI(pointOfInterestType);

			pointOfInterestType.setID(gFD.getId() + "");
			pointOfInterestType.setPOIName(gFD.getName());
			pointOfInterestType.setPhoneNumber("");
			pointOfInterestType.setDescription("");

			pointOfInterestType.setPOIAttributeList(getPOIAttributeListType(gFD));
			pointOfInterestType.setAddress(getAddressType(gFD));
			pointOfInterestType.setPoint(getPointType(gFD));
		}

		return directoryResponseType;
	}

	private static POIAttributeListType getPOIAttributeListType(
			GisFeatureDistance gFD) {
		POIAttributeListType poiAttributeListType = new POIAttributeListType();
		POIInfoListType poiInfoListType = new POIInfoListType();
		poiAttributeListType.setPOIInfoList(poiInfoListType);

		// Build attribute list
		POIInfoType poiInfoType = createPIT("adm1Code", gFD.getAdm1Code());
		poiInfoListType.getPOIInfo().add(poiInfoType);
		poiInfoType = createPIT("adm2Code", gFD.getAdm2Code());
		poiInfoListType.getPOIInfo().add(poiInfoType);
		poiInfoType = createPIT("adm3Code", gFD.getAdm3Code());
		poiInfoListType.getPOIInfo().add(poiInfoType);
		poiInfoType = createPIT("adm4Code", gFD.getAdm4Code());
		poiInfoListType.getPOIInfo().add(poiInfoType);
		poiInfoType = createPIT("adm1Name", gFD.getAdm1Name());
		poiInfoListType.getPOIInfo().add(poiInfoType);
		poiInfoType = createPIT("adm2Name", gFD.getAdm2Name());
		poiInfoListType.getPOIInfo().add(poiInfoType);
		poiInfoType = createPIT("adm3Name", gFD.getAdm3Name());
		poiInfoListType.getPOIInfo().add(poiInfoType);
		poiInfoType = createPIT("adm4Name", gFD.getAdm4Name());
		poiInfoListType.getPOIInfo().add(poiInfoType);
		poiInfoType = createPIT("asciiName", gFD.getAsciiName());
		poiInfoListType.getPOIInfo().add(poiInfoType);
		poiInfoType = createPIT("countryCode", gFD.getCountryCode());
		poiInfoListType.getPOIInfo().add(poiInfoType);
		poiInfoType = createPIT("elevation", gFD.getAsciiName());
		poiInfoListType.getPOIInfo().add(poiInfoType);
		poiInfoType = createPIT("featureClass", gFD.getFeatureClass());
		poiInfoListType.getPOIInfo().add(poiInfoType);
		poiInfoType = createPIT("featureCode", gFD.getFeatureCode());
		poiInfoListType.getPOIInfo().add(poiInfoType);
		poiInfoType = createPIT("featureId", gFD.getFeatureId() + "");
		poiInfoListType.getPOIInfo().add(poiInfoType);
		poiInfoType = createPIT("gtopo30", gFD.getGtopo30() + "");
		poiInfoListType.getPOIInfo().add(poiInfoType);
		poiInfoType = createPIT("populution", gFD.getPopulation() + "");
		poiInfoListType.getPOIInfo().add(poiInfoType);
		poiInfoType = createPIT("timezone", gFD.getTimezone());
		poiInfoListType.getPOIInfo().add(poiInfoType);
		poiInfoType = createPIT("placeType", gFD.getPlaceType());
		poiInfoListType.getPOIInfo().add(poiInfoType);
		poiInfoType = createPIT("zipCodes", gFD.getZipCodes() + "");
		poiInfoListType.getPOIInfo().add(poiInfoType);
		poiInfoType = createPIT("level", gFD.getLevel() + "");
		poiInfoListType.getPOIInfo().add(poiInfoType);
		poiInfoType = createPIT("area", gFD.getArea() + "");
		poiInfoListType.getPOIInfo().add(poiInfoType);
		poiInfoType = createPIT("tld", gFD.getTld());
		poiInfoListType.getPOIInfo().add(poiInfoType);
		poiInfoType = createPIT("capitalName", gFD.getCapitalName());
		poiInfoListType.getPOIInfo().add(poiInfoType);
		poiInfoType = createPIT("continent", gFD.getContinent());
		poiInfoListType.getPOIInfo().add(poiInfoType);
		poiInfoType = createPIT("postalCodeRegex", gFD.getPostalCodeRegex());
		poiInfoListType.getPOIInfo().add(poiInfoType);
		poiInfoType = createPIT("equivalentFlipsCode", gFD.getEquivalentFipsCode());
		poiInfoListType.getPOIInfo().add(poiInfoType);
		poiInfoType = createPIT("flipsCode", gFD.getFipsCode());
		poiInfoListType.getPOIInfo().add(poiInfoType);
		poiInfoType = createPIT("phonePrefix", gFD.getPhonePrefix());
		poiInfoListType.getPOIInfo().add(poiInfoType);
		poiInfoType = createPIT("postalCodeMask", gFD.getPostalCodeMask());
		poiInfoListType.getPOIInfo().add(poiInfoType);

		return poiAttributeListType;
	}
	
	private static POIInfoType createPIT(String name, String value){
		POIInfoType poiInfoType = new POIInfoType();
		poiInfoType.setName(name);
		poiInfoType.setValue(value);
		return poiInfoType;
	}

	private static AddressType getAddressType(GisFeatureDistance gFD) {
		// TODO Add later
		return null;
	}

	private static PointType getPointType(GisFeatureDistance gFD) {
		PointType pointType = new PointType();
		DirectPositionType pos = new DirectPositionType();
		pointType.setPos(pos);

		pos.getValue().add(gFD.getLat());
		pos.getValue().add(gFD.getLng());
		pos.setSrsDimension(new BigInteger(pos.getValue().size() + ""));

		return pointType;
	}

	private static POIAttributeListType getPOIAttributeListType(GisFeature gFD) {
		POIAttributeListType poiAttributeListType = new POIAttributeListType();
		POIInfoListType poiInfoListType = new POIInfoListType();
		poiAttributeListType.setPOIInfoList(poiInfoListType);

		// Build attribute list
		POIInfoType poiInfoType = createPIT("adm1Code", gFD.getAdm1Code());
		poiInfoListType.getPOIInfo().add(poiInfoType);
		poiInfoType = createPIT("adm2Code", gFD.getAdm2Code());
		poiInfoListType.getPOIInfo().add(poiInfoType);
		poiInfoType = createPIT("adm3Code", gFD.getAdm3Code());
		poiInfoListType.getPOIInfo().add(poiInfoType);
		poiInfoType = createPIT("adm4Code", gFD.getAdm4Code());
		poiInfoListType.getPOIInfo().add(poiInfoType);
		poiInfoType = createPIT("adm1Name", gFD.getAdm1Name());
		poiInfoListType.getPOIInfo().add(poiInfoType);
		poiInfoType = createPIT("adm2Name", gFD.getAdm2Name());
		poiInfoListType.getPOIInfo().add(poiInfoType);
		poiInfoType = createPIT("adm3Name", gFD.getAdm3Name());
		poiInfoListType.getPOIInfo().add(poiInfoType);
		poiInfoType = createPIT("adm4Name", gFD.getAdm4Name());
		poiInfoListType.getPOIInfo().add(poiInfoType);
		poiInfoType = createPIT("asciiName", gFD.getAsciiName());
		poiInfoListType.getPOIInfo().add(poiInfoType);
		poiInfoType = createPIT("countryCode", gFD.getCountryCode());
		poiInfoListType.getPOIInfo().add(poiInfoType);
		poiInfoType = createPIT("elevation", gFD.getAsciiName());
		poiInfoListType.getPOIInfo().add(poiInfoType);
		poiInfoType = createPIT("featureClass", gFD.getFeatureClass());
		poiInfoListType.getPOIInfo().add(poiInfoType);
		poiInfoType = createPIT("featureCode", gFD.getFeatureCode());
		poiInfoListType.getPOIInfo().add(poiInfoType);
		poiInfoType = createPIT("featureId", gFD.getFeatureId() + "");
		poiInfoListType.getPOIInfo().add(poiInfoType);
		poiInfoType = createPIT("gtopo30", gFD.getGtopo30() + "");
		poiInfoListType.getPOIInfo().add(poiInfoType);
		poiInfoType = createPIT("populution", gFD.getPopulation() + "");
		poiInfoListType.getPOIInfo().add(poiInfoType);
		poiInfoType = createPIT("timezone", gFD.getTimezone());
		poiInfoListType.getPOIInfo().add(poiInfoType);
		// poiInfoType = createPIT("zipCodes", gFD.getZipCodes() + "");
		// poiInfoListType.getPOIInfo().add(poiInfoType);

		return poiAttributeListType;
	}

	private static AddressType getAddressType(GisFeature gFD) {
		// TODO Add later
		return null;
	}

	private static PointType getPointType(GisFeature gFD) {
		PointType pointType = new PointType();
		DirectPositionType pos = new DirectPositionType();
		pointType.setPos(pos);

		pos.setSrsName(gFD.getLocation().getSRID() + "");
		pos.getValue().add(gFD.getLatitude());
		pos.getValue().add(gFD.getLongitude());
		pos.setSrsDimension(new BigInteger(pos.getValue().size() + ""));

		return pointType;
	}
}
