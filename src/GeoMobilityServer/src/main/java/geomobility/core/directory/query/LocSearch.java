package geomobility.core.directory.query;

import geomobility.core.GlobalCore;
import geomobility.core.directory.DirectorySearchEngine;
import geomobility.core.directory.GlobalDirectory;
import geomobility.core.directory.dto.DResultsDto;
import geomobility.core.directory.dto.LocSearchResultDto;
import geomobility.core.exception.DirectoryException;
import geomobility.core.exception.GeoException;
import geomobility.core.utils.StaticFunc;
import geomobility.gisgraphy.builder.GeolocQueryBuilder;
import geomobility.localization.I18N;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import net.opengis.gml.v_3_1_1.AbstractRingPropertyType;
import net.opengis.gml.v_3_1_1.AbstractRingType;
import net.opengis.gml.v_3_1_1.CircleByCenterPointType;
import net.opengis.gml.v_3_1_1.DirectPositionType;
import net.opengis.gml.v_3_1_1.EnvelopeType;
import net.opengis.gml.v_3_1_1.LinearRingType;
import net.opengis.gml.v_3_1_1.PointPropertyType;
import net.opengis.gml.v_3_1_1.PointType;
import net.opengis.gml.v_3_1_1.PolygonType;
import net.opengis.xls.v_1_1_0.AbstractLocationType;
import net.opengis.xls.v_1_1_0.AddressType;
import net.opengis.xls.v_1_1_0.DirectoryRequestType;
import net.opengis.xls.v_1_1_0.DistanceType;
import net.opengis.xls.v_1_1_0.DistanceUnitType;
import net.opengis.xls.v_1_1_0.NearestCriterionType;
import net.opengis.xls.v_1_1_0.NearestType;
import net.opengis.xls.v_1_1_0.POILocationType;
import net.opengis.xls.v_1_1_0.POIPropertiesType;
import net.opengis.xls.v_1_1_0.POIPropertyNameType;
import net.opengis.xls.v_1_1_0.POIPropertyType;
import net.opengis.xls.v_1_1_0.PointOfInterestType;
import net.opengis.xls.v_1_1_0.PositionType;
import net.opengis.xls.v_1_1_0.SortDirectionType;
import net.opengis.xls.v_1_1_0.WithinBoundaryType;
import net.opengis.xls.v_1_1_0.WithinDistanceType;

import com.gisgraphy.domain.geoloc.service.ServiceException;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.FulltextQuery;
import com.gisgraphy.domain.geoloc.service.geoloc.GeolocQuery;
import com.gisgraphy.helper.GeolocHelper;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * @author VoMinhTam
 */
public class LocSearch extends DirectorySearch {
	
	private static final String TAG = LocSearch.class.getSimpleName();

	public static final int MAX_RESULTS = 10;
	public static final int DEFAULT_DISTANCE = 1000;

	public LocSearch(DirectoryRequestType requestType) {
		this.directoryRequestType = requestType;
	}

	public DResultsDto searchPOI() throws GeoException, ServiceException {
		JAXBElement<? extends POIPropertiesType> jaxbPOIPropertiesType = 
			(JAXBElement<? extends POIPropertiesType>) directoryRequestType.getPOISelectionCriteria();
		DistanceUnitType distanceUnitType = directoryRequestType.getDistanceUnit();
		String sortCriteria = directoryRequestType.getSortCriteria();
		SortDirectionType sortDirectionType = directoryRequestType.getSortDirection();
		POILocationType poiLocationType = directoryRequestType.getPOILocation();

		POIPropertiesType poiPropertiesType = jaxbPOIPropertiesType.getValue();

		// TODO directoryType
		// String directoryType = poiPropertiesType.getDirectoryType();
		List<JAXBElement<?>> jaxbPOIPropertyType = poiPropertiesType.getPOIProperty();

		AddressType addressType = poiLocationType.getAddress();
		NearestType nearestType = poiLocationType.getNearest();
		WithinDistanceType withinDistanceType = poiLocationType.getWithinDistance();
		WithinBoundaryType withinBoundaryType = poiLocationType.getWithinBoundary();

		POIPropertyType poiProperttyType = (POIPropertyType) jaxbPOIPropertyType.get(0).getValue();
		POIPropertyNameType name = poiProperttyType.getName();
		String value = poiProperttyType.getValue();
		FulltextQuery query = null;
		String type = "";
		String value2 = "";
		if (name == POIPropertyNameType.POI_NAME || name == POIPropertyNameType.KEYWORD) {
			type = GeolocHelper.getListPOIType(value)[0];
		} else if (name == POIPropertyNameType.NAICS_TYPE || name == POIPropertyNameType.SIC_TYPE) {
			POIPropertyType poiProperttyType2 = (POIPropertyType) jaxbPOIPropertyType.get(1).getValue();
			POIPropertyNameType name2 = poiProperttyType2.getName();

			type = value;
			if (name2 == POIPropertyNameType.NAICS_SUB_TYPE || name2 == POIPropertyNameType.SIC_SUB_TYPE) {
				value2 = poiProperttyType2.getValue();
			} else {
				throw new DirectoryException(I18N.m.getString("have_not_supported_yet"));
			}
		} else {
			throw new DirectoryException(I18N.m.getString("have_not_supported_yet"));
		}

		// 4 types of Location based Search
		LocSearchResultDto result = new LocSearchResultDto();
		DirectorySearchEngine locSearchEngine = new DirectorySearchEngine(GlobalCore.statsUsageService);

		if (nearestType != null) {
			// Type 1: NearestType
			NearestCriterionType nearestCriterionType = nearestType.getNearestCriterion();
			List<JAXBElement<? extends AbstractLocationType>> jaxbListLocationType = nearestType.getLocation();

			for (int i = 0; i < jaxbListLocationType.size(); i++) {
				JAXBElement<? extends AbstractLocationType> jaxbLocationType = jaxbListLocationType.get(i);
				AbstractLocationType locationType = jaxbLocationType.getValue();

				float lng = 0;
				float lat = 0;

				if (locationType instanceof PositionType) {
					PositionType positionType = (PositionType) locationType;
					PointType pointType = positionType.getPoint();
					lng = pointType.getPos().getValue().get(0).floatValue();
					lat = pointType.getPos().getValue().get(1).floatValue();
				} else if (locationType instanceof PointOfInterestType) {
					PointOfInterestType pointOfInterestType = (PointOfInterestType) locationType;
					PointType pointType = pointOfInterestType.getPoint();
					lng = pointType.getPos().getValue().get(0).floatValue();
					lat = pointType.getPos().getValue().get(1).floatValue();
				} else if (locationType instanceof AddressType) {
					// TODO
					AddressType addressType2 = (AddressType) locationType;
					throw new DirectoryException("have_not_supported_yet");
				} else {
					throw new DirectoryException(
							"not_specify_position_in_location_based_search");
				}

				// Build query
				GeolocQueryBuilder geolocQueryBuilder = GeolocQueryBuilder
						.getInstance();
				GeolocQuery geolocQuery = geolocQueryBuilder.buildGeolocQuery(
						lat, lng, 100000, GlobalDirectory.FROM, GlobalDirectory.MAX_RESULTS, "", type, "no", "true", null,value2);

				LocSearchResultDto resultTmp = locSearchEngine.executeNearest(geolocQuery);
				result.addResult(resultTmp);
			}
		} else if (withinDistanceType != null) {
			JAXBElement<? extends AbstractLocationType> jaxbLocationType = withinDistanceType.getLocation();
			AbstractLocationType locationType = jaxbLocationType.getValue();

			float lng = 0;
			float lat = 0;

			if (locationType instanceof PositionType) {
				PositionType positionType = (PositionType) locationType;
				PointType pointType = positionType.getPoint();
				lng = pointType.getPos().getValue().get(0).floatValue();
				lat = pointType.getPos().getValue().get(1).floatValue();
			} else if (locationType instanceof PointOfInterestType) {
				PointOfInterestType pointOfInterestType = (PointOfInterestType) locationType;
				PointType pointType = pointOfInterestType.getPoint();
				lng = pointType.getPos().getValue().get(0).floatValue();
				lat = pointType.getPos().getValue().get(1).floatValue();
			} else if (locationType instanceof AddressType) {
				// TODO
				AddressType addressType2 = (AddressType) locationType;
				throw new DirectoryException("have_not_supported_yet");
			} else {
				throw new DirectoryException("not_specify_position_in_location_based_search");
			}

			// Build query
			GeolocQueryBuilder geolocQueryBuilder = GeolocQueryBuilder.getInstance();

			DistanceType distanceTypeMax = withinDistanceType.getMaximumDistance();
			DistanceType distanceTypeMin = withinDistanceType.getMinimumDistance();
			GeolocQuery geolocQuery = null;
			
			double max = distanceTypeMax==null ? 0 : StaticFunc.parseDouble(distanceTypeMax.getValue() + "");
			geolocQuery = geolocQueryBuilder.buildGeolocQuery(lat, lng, max, 
					GlobalDirectory.FROM, GlobalDirectory.MAX_RESULTS, "", type, "no", "true", null, value2);
			
			double min = distanceTypeMin==null ? 0 : StaticFunc.parseDouble(distanceTypeMin.getValue() + "");
			geolocQuery.setMinRadius(min);

			LocSearchResultDto resultTmp = locSearchEngine.executeNearest(geolocQuery);
			result.addResult(resultTmp);
		} else if (withinBoundaryType != null) {
			CircleByCenterPointType circleByCenterPointType = withinBoundaryType.getAOI().getCircleByCenterPoint();
			PolygonType polygonType = withinBoundaryType.getAOI().getPolygon();
			EnvelopeType envelopeType = withinBoundaryType.getAOI().getEnvelope()==null ? null : withinBoundaryType.getAOI().getEnvelope().getValue();

			if (circleByCenterPointType != null) {
				DirectPositionType directPositionType = circleByCenterPointType.getPos();
				if (directPositionType.getSrsDimension() != null && directPositionType.getSrsDimension().intValue() < 2)
					throw new DirectoryException("coordinate_not_exact");

				float lng = directPositionType.getValue().get(0).floatValue();
				float lat = directPositionType.getValue().get(1).floatValue();
				double radius = circleByCenterPointType.getRadius().getValue();

				// Build query
				GeolocQueryBuilder geolocQueryBuilder = GeolocQueryBuilder.getInstance();
				GeolocQuery geolocQuery = geolocQueryBuilder.buildGeolocQuery(lat, lng, radius, 
						GlobalDirectory.FROM, GlobalDirectory.MAX_RESULTS, "", type, "no", "true", null, value2);

				LocSearchResultDto resultTmp = locSearchEngine.executeNearest(geolocQuery);
				result.addResult(resultTmp);
			} else if (polygonType != null) {
				//List<List<Point>> listListPoints = new ArrayList<List<Point>>();
				List<List<Point>> listInterior = extractListPointInteriorFromRingProperty(polygonType.getInterior()); 
				List<Point> exterior = extractListPointExteriorFromRingProperty(polygonType.getExterior());
				
				for (List<Point> listPoints : listInterior){
					validatePolygon(listPoints);
				}
				validatePolygon(exterior);
				
				// Build query
				GeolocQueryBuilder geolocQueryBuilder = GeolocQueryBuilder.getInstance();
				GeolocQuery geolocQuery = geolocQueryBuilder.buildGeolocQuery(0, 0, 0, 
						GlobalDirectory.FROM, GlobalDirectory.MAX_RESULTS, "", type, "no", "true", null, value2);

				Polygon polygonInterior = null;
				if (listInterior.size()>0){
					polygonInterior = GeolocHelper.createPolygon(listInterior);
					geolocQuery.setPolygon(polygonInterior);
				}
		
				Polygon polygonExterior = null;
				if (exterior.size()>0){
					polygonExterior = GeolocHelper.createPolygonFromListPoint(exterior);
					geolocQuery.setPolygon(polygonExterior);
				}

				LocSearchResultDto resultTmp = locSearchEngine.searchInPolygon(geolocQuery);
				result.addResult(resultTmp);
			} else if (envelopeType != null) {
				if (envelopeType.getPos().size() < 2)
					throw new DirectoryException("number_position_in_envelop_type_not_correct");

				Point[] ps = new Point[2];
				for (int i = 0; i < 2; i++) {
					DirectPositionType pos = envelopeType.getPos().get(i);
					if (pos.getValue() != null && pos.getValue().size() < 2)
						throw new DirectoryException("coordinate_not_exact");

					ps[i] = GeolocHelper.createPoint(pos.getValue().get(0).floatValue(), pos.getValue().get(1).floatValue());
				}

				// Build query
				GeolocQueryBuilder geolocQueryBuilder = GeolocQueryBuilder.getInstance();
				GeolocQuery geolocQuery = geolocQueryBuilder.buildGeolocQuery(0, 0, 0, 
						GlobalDirectory.FROM, GlobalDirectory.MAX_RESULTS, "", type, "no", "true", null, value2);
				Polygon polygon = GeolocHelper.createPolygon(ps[0], ps[1]);
				geolocQuery.setPolygon(polygon);

				LocSearchResultDto resultTmp = locSearchEngine.searchInPolygon(geolocQuery);
				result.addResult(resultTmp);
			} else {
				throw new DirectoryException("have_not_supported_yet");
			}
		} else {
			throw new DirectoryException("have_not_supported_yet");
		}
		return result;
	}

	private static List<List<Point>> extractListPointInteriorFromRingProperty(List<JAXBElement<AbstractRingPropertyType>> listJAXB) {
		List<List<Point>> listListInterior = new ArrayList<List<Point>>();
		if (listJAXB!=null && listJAXB.size()>0){
			for (JAXBElement<AbstractRingPropertyType> jaxbARingProperty : listJAXB) {
				AbstractRingPropertyType abType = jaxbARingProperty.getValue();
				JAXBElement<? extends AbstractRingType> jaxbLinearRingType = abType.getRing();
				AbstractRingType abstractRingType = jaxbLinearRingType.getValue();
				if (abstractRingType instanceof LinearRingType) {
					LinearRingType linearRingType = (LinearRingType) abstractRingType;
					List<JAXBElement<?>> listJaxb = linearRingType.getPosOrPointPropertyOrPointRep();
					
					List<Point> listPoints = new ArrayList<Point>();
					for (JAXBElement<?> jaxbElement : listJaxb){
						Object element = jaxbElement.getValue();
						
						if (element instanceof DirectPositionType || element instanceof PointPropertyType){
							DirectPositionType directPositionType = (element instanceof DirectPositionType) ? 
									(DirectPositionType) element : ((PointPropertyType)element).getPoint().getPos();
							if (directPositionType.getValue().size()>=2){
								Point p = GeolocHelper.createXYPoint(directPositionType.getValue().get(0), directPositionType.getValue().get(1));
								listPoints.add(p);
							}
						}
					}
					listListInterior.add(listPoints);
				} 
			}
		}
		
		return listListInterior;
	}
	
	private static List<Point> extractListPointExteriorFromRingProperty(JAXBElement<AbstractRingPropertyType> jAXB) {
		List<Point> listPoints = new ArrayList<Point>();
		if (jAXB!=null){
			AbstractRingPropertyType abType = jAXB.getValue();
			JAXBElement<? extends AbstractRingType> jaxbLinearRingType = abType.getRing();
			AbstractRingType abstractRingType = jaxbLinearRingType.getValue();
			if (abstractRingType instanceof LinearRingType) {
				LinearRingType linearRingType = (LinearRingType) abstractRingType;
				List<JAXBElement<?>> listJaxb = linearRingType.getPosOrPointPropertyOrPointRep();
				
				for (JAXBElement<?> jaxbElement : listJaxb){
					Object element = jaxbElement.getValue();
					
					if (element instanceof DirectPositionType || element instanceof PointPropertyType){
						DirectPositionType directPositionType = (element instanceof DirectPositionType) ? 
								(DirectPositionType) element : ((PointPropertyType)element).getPoint().getPos();
						if (directPositionType.getValue().size()>=2){
							Point p = GeolocHelper.createXYPoint(directPositionType.getValue().get(0), directPositionType.getValue().get(1));
							listPoints.add(p);
						}
					}
				}
			}
		}
		
		return listPoints;
	}
	
	private static void validatePolygon(List<Point> listPoints) throws DirectoryException{
		if (listPoints.size()>1 && listPoints.get(0).equals(listPoints.get(listPoints.size() - 1)))
			listPoints.remove(listPoints.size()-1);
		
		if (listPoints.size()>0 && listPoints.size() < 3)
			throw new DirectoryException("polygon_not_valid");
	}
}
