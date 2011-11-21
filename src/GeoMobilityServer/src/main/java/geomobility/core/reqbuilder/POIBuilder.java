package geomobility.core.reqbuilder;

import geomobility.core.GlobalCore;
import geomobility.core.utils.StaticFunc;
import geomobility.servlet.service.exception.InvalidParameterException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;

import net.opengis.gml.v_3_1_1.PointType;
import net.opengis.xls.v_1_1_0.AbstractLocationType;
import net.opengis.xls.v_1_1_0.AbstractPOISelectionCriteriaType;
import net.opengis.xls.v_1_1_0.AddressType;
import net.opengis.xls.v_1_1_0.AreaOfInterestType;
import net.opengis.xls.v_1_1_0.DirectoryRequestType;
import net.opengis.xls.v_1_1_0.DirectoryResponseType;
import net.opengis.xls.v_1_1_0.DistanceType;
import net.opengis.xls.v_1_1_0.GeocodeRequestType;
import net.opengis.xls.v_1_1_0.GeocodeResponseType;
import net.opengis.xls.v_1_1_0.NearestCriterionType;
import net.opengis.xls.v_1_1_0.NearestType;
import net.opengis.xls.v_1_1_0.POILocationType;
import net.opengis.xls.v_1_1_0.POIProperties;
import net.opengis.xls.v_1_1_0.POIPropertiesType;
import net.opengis.xls.v_1_1_0.POIPropertyNameType;
import net.opengis.xls.v_1_1_0.POIPropertyType;
import net.opengis.xls.v_1_1_0.PositionType;
import net.opengis.xls.v_1_1_0.SLIAType;
import net.opengis.xls.v_1_1_0.WithinBoundaryType;
import net.opengis.xls.v_1_1_0.WithinDistanceType;

/**
 * 
 * @author VoMinhTam
 *
 */
public class POIBuilder extends RequestBuilder{
	// Singleton
	private static POIBuilder i = null;
	public static POIBuilder instance(){
		if (i==null)
			i = new POIBuilder();
		return i;
	}
	
	public  DirectoryRequestType build(Map<String, String[]> params){
		DirectoryRequestType directoryRequestType = new DirectoryRequestType();
		addPOIProperties(directoryRequestType, params);
		
		return directoryRequestType;
	}
	
	/**
	 * Search by Nearest
	 * @param listPointTypes
	 * @param params
	 * @return
	 * @throws InvalidParameterException 
	 */
	public  DirectoryRequestType build(List<PointType> listPointTypes, Map<String, String[]> params) throws InvalidParameterException{
		DirectoryRequestType directoryRequestType = new DirectoryRequestType();
		POILocationType poiLocationType = new POILocationType();
		directoryRequestType.setPOILocation(poiLocationType);

		String searchType = getParameter(ParamsKey.D_SEARCHTYPE_KEY.toString(),params, ParamsKey.SearchType.NEAREST.toString());
		if (ParamsKey.SearchType.WITHIN_DISTANCE.equals(searchType)){
			return build(listPointTypes.get(0), params);
		}else {
			//Search by NearestType
			String nearestTypeParam = getParameter(ParamsKey.D_NEARESTTYPE_KEY.toString(), params, null);
			NearestType nearestType = new NearestType();
			poiLocationType.setNearest(nearestType);
			
			if ("easiest".equals(nearestTypeParam)){
				nearestType.setNearestCriterion(NearestCriterionType.EASIEST);
			}else if ("fastest".equals(nearestTypeParam)){
				nearestType.setNearestCriterion(NearestCriterionType.FASTEST);
			}else if ("proximity".equals(nearestTypeParam)){
				nearestType.setNearestCriterion(NearestCriterionType.PROXIMITY);
			}else {
				nearestType.setNearestCriterion(NearestCriterionType.SHORTEST);
			}
			
			List<JAXBElement<? extends AbstractLocationType>> listJaxbLocations = nearestType.getLocation();
			nearestType.setLocation(listJaxbLocations);
			for(PointType pointType : listPointTypes){
				PositionType positionType = new PositionType();
				positionType.setPoint(pointType);
				
				JAXBElement<AbstractLocationType> jaxbLocationType = GlobalCore.objectFactoryXLS.createLocation(positionType);
				listJaxbLocations.add(jaxbLocationType);
			}
			
			addPOIProperties(directoryRequestType, params);
			return directoryRequestType;
		}
	}
	
	/**
	 * Search by WithinDistanceType
	 * @param pointType
	 * @param params
	 * @return
	 * @throws InvalidParameterException 
	 */
	public DirectoryRequestType build(PointType pointType, Map<String, String[]> params) throws InvalidParameterException{
		PositionType positionType = new PositionType();
		positionType.setPoint(pointType);
		return build(positionType, params);
	}
	
	public  DirectoryRequestType build(SLIAType sliaType, Map<String, String[]> params) throws InvalidParameterException{
		PositionType positionType = sliaType.getOutputGatewayParameters().getOutputMSIDs().getOutputMSInformation().getPosition();
		return build(positionType, params);
	}

	/**
	 * @deprecated
	 */
	public  List<DirectoryRequestType> build(GeocodeRequestType geocodeReqType, Map<String, String[]> params){
		List<DirectoryRequestType> listDirectoryRequestTypes = new ArrayList<DirectoryRequestType>();

		List<AddressType> listAddress = geocodeReqType.getAddress();
		for (AddressType addressType:listAddress){
			DirectoryRequestType directoryRequestType = new DirectoryRequestType();
			directoryRequestType.getPOILocation().setAddress(addressType);
			
			addPOIProperties(directoryRequestType, params);
		}
		return listDirectoryRequestTypes;
	}
	
	public DirectoryRequestType build(GeocodeResponseType geocodeResType, Map<String, String[]> params) throws InvalidParameterException{
		PointType pointType = geocodeResType.getGeocodeResponseList().get(0).getGeocodedAddress().get(0).getPoint();
		return build(pointType, params);
	}
	
	private DirectoryRequestType build(PositionType positionType, Map<String, String[]> params) throws InvalidParameterException{
		String searchType = getParameter(ParamsKey.D_SEARCHTYPE_KEY.toString(),params, ParamsKey.SearchType.NEAREST.toString());
		
		DirectoryRequestType directoryRequestType = new DirectoryRequestType();
		
		// Part1: Basic information is converted from Gateway Services
		POILocationType poiLocationType = new POILocationType();
		directoryRequestType.setPOILocation(poiLocationType);
		
		if (ParamsKey.SearchType.NEAREST.equals(searchType)){
			NearestType nearestType = new NearestType();
			
			String nearestTypeParam = getParameter(ParamsKey.D_NEARESTTYPE_KEY.toString(), params, "shortest");
			if ("easiest".equals(nearestTypeParam)){
				nearestType.setNearestCriterion(NearestCriterionType.EASIEST);
			}else if ("fastest".equals(nearestTypeParam)){
				nearestType.setNearestCriterion(NearestCriterionType.FASTEST);
			}else if ("proximity".equals(nearestTypeParam)){
				nearestType.setNearestCriterion(NearestCriterionType.PROXIMITY);
			}else {
				nearestType.setNearestCriterion(NearestCriterionType.SHORTEST);
			}
			
			poiLocationType.setNearest(nearestType);
			
			List<JAXBElement<? extends AbstractLocationType>> listJaxbLocations = nearestType.getLocation();
			JAXBElement<AbstractLocationType> jaxbLocationType = GlobalCore.objectFactoryXLS.createLocation(positionType);
			listJaxbLocations.add(jaxbLocationType);
			nearestType.setLocation(listJaxbLocations);
			
			nearestType.setNearestCriterion(NearestCriterionType.SHORTEST);
			
			// TODO: User can specify more nearest point, besides location from Gateway Service 
			// (in that case, this point is obtained from params
		}else if (ParamsKey.SearchType.WITHIN_DISTANCE.equals(searchType)){
			//Note: Default distance in meter
			String maxDistance = getParameter(ParamsKey.D_MAXDISTANCE_KEY, params, null);
			String minDistance = getParameter(ParamsKey.D_MINDISTANCE_KEY, params, null);
			
			WithinDistanceType withinDistanceType = new WithinDistanceType();
			JAXBElement<AbstractLocationType> jaxbLocationType = GlobalCore.objectFactoryXLS.createLocation(positionType);
			withinDistanceType.setLocation(jaxbLocationType);
			
			if (maxDistance!=null) {
				try {
					DistanceType maxDistanceType = new DistanceType();
					maxDistanceType.setValue(new BigDecimal(maxDistance));
					withinDistanceType.setMaximumDistance(maxDistanceType);
				} catch (Exception e) {
					throw new InvalidParameterException(ParamsKey.D_MAXDISTANCE_KEY.toString(), "invalid");
				}
			}
			
			if (minDistance!=null) {
				try {
					DistanceType minDistanceType = new DistanceType();
					minDistanceType.setValue(new BigDecimal(minDistance));
					withinDistanceType.setMaximumDistance(minDistanceType);
				} catch (Exception e) {
					throw new InvalidParameterException(ParamsKey.D_MINDISTANCE_KEY.toString(), "invalid");
				}
			}
		}else{//WithinBoundaryType
			WithinBoundaryType withinBoundaryType = new WithinBoundaryType();
			AreaOfInterestType areaOfInterestType = new AreaOfInterestType();
			withinBoundaryType.setAOI(areaOfInterestType);
			
			areaOfInterestType.setCircleByCenterPoint(positionType.getCircleByCenterPoint());
			areaOfInterestType.setPolygon(positionType.getPolygon());
			//areaOfInterestType.setEnvelope(value)
		}
		
		//Part 2: Other information is obtained from request
		addPOIProperties(directoryRequestType, params);
		
		return directoryRequestType;
	}
	
	private void addPOIProperties(
			DirectoryRequestType directoryRequestType,
			Map<String, String[]> params) {
		String directoryType = getParameter(ParamsKey.D_DIRECTORYTYPE_KEY, params, ParamsKey.D_DEFAULT_DIRECTORYTYPE_KEY);
		String poiType = getParameter(ParamsKey.D_POITYPE_KEY, params, "All");
		String poiSubtype = getParameter(ParamsKey.D_POISUBTYPE_KEY, params, "");
		System.out.println("poiType: " + poiType);
		System.out.println("poiSubType: " + poiSubtype);
		
		POIProperties poiPropertiesType =  new POIProperties();

		JAXBElement<? extends AbstractPOISelectionCriteriaType> jaxbPOIPropertiesType = (JAXBElement<? extends AbstractPOISelectionCriteriaType>) GlobalCore.objectFactoryXLS.createPOIProperties(poiPropertiesType);
		directoryRequestType.setPOISelectionCriteria(jaxbPOIPropertiesType);
		
		poiPropertiesType.setDirectoryType(directoryType);
		
		List<JAXBElement<?>> listJaxbPOIPropertyType = poiPropertiesType.getPOIProperty();
		poiPropertiesType.setPOIProperty(listJaxbPOIPropertyType);
		
		POIPropertyType poiPropertyType = new POIPropertyType();
		poiPropertyType.setName(POIPropertyNameType.NAICS_TYPE);
		poiPropertyType.setValue(poiType);
		JAXBElement<POIPropertyType> jaxbPOIProperty = GlobalCore.objectFactoryXLS.createPOIProperty(poiPropertyType);
		listJaxbPOIPropertyType.add(jaxbPOIProperty);
		
		poiPropertyType = new POIPropertyType();
		poiPropertyType.setName(POIPropertyNameType.NAICS_SUB_TYPE);
		poiPropertyType.setValue(poiSubtype);
		jaxbPOIProperty = GlobalCore.objectFactoryXLS.createPOIProperty(poiPropertyType);
		listJaxbPOIPropertyType.add(jaxbPOIProperty);
	}
	
}
