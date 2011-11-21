package geomobility.core.directory.query;

import geomobility.core.GlobalCore;
import geomobility.core.directory.DirectorySearchEngine;
import geomobility.core.directory.dto.DResultsDto;
import geomobility.core.directory.dto.LocSearchResultDto;
import geomobility.core.exception.DirectoryException;
import geomobility.gisgraphy.builder.GeolocQueryBuilder;
import geomobility.localization.I18N;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;

import net.opengis.gml.v_3_1_1.PointType;
import net.opengis.xls.v_1_1_0.AbstractLocationType;
import net.opengis.xls.v_1_1_0.AddressType;
import net.opengis.xls.v_1_1_0.DirectoryRequestType;
import net.opengis.xls.v_1_1_0.DistanceUnitType;
import net.opengis.xls.v_1_1_0.NearestCriterionType;
import net.opengis.xls.v_1_1_0.NearestType;
import net.opengis.xls.v_1_1_0.POILocationType;
import net.opengis.xls.v_1_1_0.POIPropertiesType;
import net.opengis.xls.v_1_1_0.POIPropertyNameType;
import net.opengis.xls.v_1_1_0.POIPropertyType;
import net.opengis.xls.v_1_1_0.PositionType;
import net.opengis.xls.v_1_1_0.SortDirectionType;
import net.opengis.xls.v_1_1_0.WithinBoundaryType;
import net.opengis.xls.v_1_1_0.WithinDistanceType;

import com.gisgraphy.domain.geoloc.service.ServiceException;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.FulltextQuery;
import com.gisgraphy.domain.geoloc.service.geoloc.GeolocQuery;
import com.gisgraphy.helper.GeolocHelper;

/**
 * @author Nam
 */
public class RegSearch extends DirectorySearch {

	public static final int MAX_RESULTS = 10;
	public static final int DEFAULT_DISTANCE = 1000;

	public RegSearch(DirectoryRequestType requestType) {
		this.directoryRequestType = requestType;
	}

	public DResultsDto searchPOI() throws DirectoryException, ServiceException {
		JAXBElement<? extends POIPropertiesType> jaxbPOIPropertiesType = (JAXBElement<? extends POIPropertiesType>) directoryRequestType
				.getPOISelectionCriteria();
		DistanceUnitType distanceUnitType = directoryRequestType
				.getDistanceUnit();
		String sortCriteria = directoryRequestType.getSortCriteria();
		SortDirectionType sortDirectionType = directoryRequestType
				.getSortDirection();
		POILocationType poiLocationType = directoryRequestType.getPOILocation();

		POIPropertiesType poiPropertiesType = jaxbPOIPropertiesType.getValue();

		// TODO directoryType
		// String directoryType = poiPropertiesType.getDirectoryType();
		List<JAXBElement<?>> jaxbPOIPropertyType = poiPropertiesType
				.getPOIProperty();

		AddressType addressType = poiLocationType.getAddress();
		NearestType nearestType = poiLocationType.getNearest();
		WithinDistanceType withinDistanceType = poiLocationType
				.getWithinDistance();
		WithinBoundaryType withinBoundaryType = poiLocationType
				.getWithinBoundary();

		POIPropertyType poiProperttyType = (POIPropertyType) jaxbPOIPropertyType
				.get(0).getValue();
		POIPropertyNameType name = poiProperttyType.getName();
		String value = poiProperttyType.getValue();
		FulltextQuery query = null;
		String type = "";
		String value2 = null;
		if (name == POIPropertyNameType.POI_NAME
				|| name == POIPropertyNameType.KEYWORD) {
			type = GeolocHelper.getListPOIType(value)[0];
		} else if (name == POIPropertyNameType.NAICS_TYPE
				|| name == POIPropertyNameType.SIC_TYPE) {
			POIPropertyType poiProperttyType2 = (POIPropertyType) jaxbPOIPropertyType
					.get(1).getValue();
			POIPropertyNameType name2 = poiProperttyType2.getName();

			type = value;
			if (name2 == POIPropertyNameType.NAICS_SUB_TYPE
					|| name2 == POIPropertyNameType.SIC_SUB_TYPE) {
				value2 = poiProperttyType2.getValue();
			} else {
				throw new DirectoryException(I18N.m
						.getString("have_not_supported_yet"));
			}
		} else {
			throw new DirectoryException(I18N.m
					.getString("have_not_supported_yet"));
		}

		// 4 types of Location based Search
		LocSearchResultDto result = new LocSearchResultDto();
		DirectorySearchEngine locSearchEngine = new DirectorySearchEngine(
				GlobalCore.statsUsageService);

		if (nearestType != null) {
			
			//NAM: only process this
			// Type 1: NearestType
			NearestCriterionType nearestCriterionType = nearestType
					.getNearestCriterion();
			
			// Declare CasperSegment class: 2 Point s form a Segment
			class CasperPoint {
				float lat, lng;

				public CasperPoint(float lat, float lng) {
					super();
					this.lat = lat;
					this.lng = lng;
				}

				@Override
				public boolean equals(Object arg0) {
					// TODO Auto-generated method stub
					if(arg0 instanceof CasperPoint) {
						CasperPoint anotherPoint = (CasperPoint) arg0;
						return lat == anotherPoint.lat && lng == anotherPoint.lng;
					}
					return super.equals(arg0);
				}
				
				
			};
			
			class CasperSegment {
				CasperPoint p1, p2;
				
				public CasperSegment(CasperPoint p1, CasperPoint p2) {
					super();
					this.p1 = p1;
					this.p2 = p2;
				}
				
				@Override
				public boolean equals(Object arg0) {
					// TODO Auto-generated method stub
					if(arg0 instanceof CasperSegment) {
						CasperSegment anotherSegment = (CasperSegment) arg0;
						return (p1.equals(anotherSegment.p1) && p2.equals(anotherSegment.p2)) 
								|| (p1.equals(anotherSegment.p2) && p2.equals(anotherSegment.p1));
					}
					return super.equals(arg0);
				}
				
				
			};
			
			List<CasperSegment> segmentQueue = new LinkedList<CasperSegment>();
			Map<CasperPoint, LocSearchResultDto> nearests = new Hashtable<CasperPoint, LocSearchResultDto>();
			
			
			// Build segment queue
			List<JAXBElement<? extends AbstractLocationType>> jaxbListLocationType = nearestType
					.getLocation();

			for (int i = 0; i < jaxbListLocationType.size(); i++) {
				
				JAXBElement<? extends AbstractLocationType> jaxbLocationType = jaxbListLocationType
						.get(i);
				AbstractLocationType locationType = jaxbLocationType.getValue();

				float lat = 0;
				float lng = 0;

				if (locationType instanceof PositionType) {
					PositionType positionType = (PositionType) locationType;
					PointType pointType = positionType.getPoint();
					lat = pointType.getPos().getValue().get(0).floatValue();
					lng = pointType.getPos().getValue().get(1).floatValue();
				} else {
					throw new DirectoryException(
							"not_specify_position_in_location_based_search");
				}
				
				CasperPoint p1 = new CasperPoint(lat, lng);
				
				jaxbLocationType = jaxbListLocationType.get(i + 1 >= jaxbListLocationType.size() ? 0 : i + 1);
				
				locationType = jaxbLocationType.getValue();

				if (locationType instanceof PositionType) {
					PositionType positionType = (PositionType) locationType;
					PointType pointType = positionType.getPoint();
					lat = pointType.getPos().getValue().get(0).floatValue();
					lng = pointType.getPos().getValue().get(1).floatValue();
				} else {
					throw new DirectoryException(
							"not_specify_position_in_location_based_search");
				}
				
				CasperPoint p2 = new CasperPoint(lat, lng);
				
				CasperSegment s = new CasperSegment(p1, p2);
				
				segmentQueue.add(s);
				
				// Build query
//				GeolocQueryBuilder geolocQueryBuilder = GeolocQueryBuilder
//						.getInstance();
//				GeolocQuery geolocQuery = geolocQueryBuilder.buildGeolocQuery(
//						lat, lng, 100000, 0, 0, "", type, "no", "true", null,
//						value2);
//
//				LocSearchResultDto resultTmp = locSearchEngine
//						.executeNearest(geolocQuery);
//
//				result.addResult(resultTmp);
			}
			
			//process the queue here
			int count = 0;
			int refine = 1;
			int limit = segmentQueue.size() * (int)(Math.pow(refine, 2.0) - 1); //limit is calculated based on refine
			int time = limit; //a new param that add runtime constraint
			int scale = 100000; //convert lat, lng distance to km
			//List<LocSearchResultDto> candidates = new LinkedList<LocSearchResultDto>();
			while(segmentQueue.size() > 0) {
				count++;
				
				CasperSegment segment = segmentQueue.get(0);
				
				//process segment, add 2 new segment if needed
				CasperPoint p1 = segment.p1;
				CasperPoint p2 = segment.p2;
				
				GeolocQueryBuilder geolocQueryBuilder = GeolocQueryBuilder.getInstance();
				
				GeolocQuery geolocQuery1 = geolocQueryBuilder.buildGeolocQuery(
						p1.lat, p1.lng, 100000, 0, 0, "", type, "no", "true", null,
						value2);
				
				LocSearchResultDto result1 = nearests.get(p1);
				if(result1 == null) {
					result1 = locSearchEngine.executeNearest(geolocQuery1);
					nearests.put(p1, result1);
				}
				
				GeolocQuery geolocQuery2 = geolocQueryBuilder.buildGeolocQuery(
						p2.lat, p2.lng, 100000, 0, 0, "", type, "no", "true", null,
						value2);
		
				LocSearchResultDto result2 = nearests.get(p2);
				if(result2 == null) {
					result2 = locSearchEngine.executeNearest(geolocQuery2);
					nearests.put(p2, result2);
				}
				
				// result1 == result2 - no need to add
				// result1 != result2 - add 2 new segment
				
				if(result1.equals(result2)) {
					//result should be 1
					//candidates.add(result1); //or add result2, the same
					result.addResult(result1);
				} else {
					// find the perpendicular
					float lat = 0;
					float lng = 0;
					CasperPoint p = new CasperPoint(lat, lng);
					
					if((refine != -1 && count > limit) || (count > time)) {
						
						// range search
						double distance = Math.sqrt(Math.pow((p.lat - p1.lat), 2) + Math.pow(p.lng - p1.lng, 2)) * scale;
						GeolocQuery geolocQuery = geolocQueryBuilder.buildGeolocQuery(
								p.lat, p.lng, distance, 0, 0, "", type, "no", "true", null,
								value2);
						LocSearchResultDto rangeResult = locSearchEngine.executeNearest(geolocQuery);
						result.addResult(rangeResult);
						break;
					}
					
					CasperSegment s1 = new CasperSegment(p1, p);
					CasperSegment s2 = new CasperSegment(p, p2);
					segmentQueue.add(s1);
					segmentQueue.add(s2);
				}
			}
			
		} else
			throw new DirectoryException("have_not_supported_yet");
		
		return result;
	}

	@Override
	public boolean equals(Object arg0) {
		// TODO Auto-generated method stub
		return super.equals(arg0);
	}
}
