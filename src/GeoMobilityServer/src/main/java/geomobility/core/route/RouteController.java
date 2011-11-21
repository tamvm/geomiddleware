package geomobility.core.route;

import geomobility.core.GlobalCore;
import geomobility.core.exception.GeoException;
import geomobility.core.net.HttpData;
import geomobility.core.net.HttpRequest;
import geomobility.core.route.RouteResult.RouteInstruction;
import geomobility.core.utils.GeoPoint;
import geomobility.core.utils.StaticFunc;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import net.opengis.gml.v_3_1_1.CoordType;
import net.opengis.gml.v_3_1_1.DirectPositionType;
import net.opengis.gml.v_3_1_1.EnvelopeType;
import net.opengis.gml.v_3_1_1.LineStringType;
import net.opengis.gml.v_3_1_1.ObjectFactory;
import net.opengis.gml.v_3_1_1.PointType;
import net.opengis.xls.v_1_1_0.DetermineRouteRequestType;
import net.opengis.xls.v_1_1_0.DetermineRouteResponseType;
import net.opengis.xls.v_1_1_0.DistanceType;
import net.opengis.xls.v_1_1_0.DistanceUnitType;
import net.opengis.xls.v_1_1_0.PositionType;
import net.opengis.xls.v_1_1_0.ResponseType;
import net.opengis.xls.v_1_1_0.RouteGeometryRequestType;
import net.opengis.xls.v_1_1_0.RouteGeometryType;
import net.opengis.xls.v_1_1_0.RouteHandleType;
import net.opengis.xls.v_1_1_0.RouteInstructionType;
import net.opengis.xls.v_1_1_0.RouteInstructionsListType;
import net.opengis.xls.v_1_1_0.RouteInstructionsRequestType;
import net.opengis.xls.v_1_1_0.RouteMapRequestType;
import net.opengis.xls.v_1_1_0.RoutePlanType;
import net.opengis.xls.v_1_1_0.RouteSummaryType;
import net.opengis.xls.v_1_1_0.XLSType;

import org.apache.xerces.jaxp.datatype.DatatypeFactoryImpl;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.gisgraphy.domain.geoloc.service.ServiceException;

/**
 * @author VoMinhTam
 */
public class RouteController {
	public static String doController(String xml) throws GeoException, ServiceException, ConnectException, IOException, JSONException {
		XLSType xlsType = StaticFunc.getXLSType(xml);
		List<DetermineRouteRequestType> listDetermineRouteReq = StaticFunc.parse(xlsType);
		xlsType.getBody().clear();

		for (DetermineRouteRequestType determineRoute : listDetermineRouteReq) {
			String url = buildURL(determineRoute);
			RouteResult routeResult = sendRequest2GM(url);
			DetermineRouteResponseType determineRouteResponse = outputBuilder(routeResult);
			
			ResponseType responseType = new ResponseType();
			responseType.setResponseParameters(GlobalCore.objectFactoryXLS.createDetermineRouteResponse(determineRouteResponse));
			xlsType.getBody().add(GlobalCore.objectFactoryXLS.createResponse(responseType));
		}

		// Output to String
		OutputStream outputStream = StaticFunc.buildXLSResult(xlsType,GlobalCore.output);
		return outputStream.toString();
	}
	
	public static String buildURL(DetermineRouteRequestType determineRouteRequest) throws RouteException{
		RouteHandleType routeHandle = determineRouteRequest.getRouteHandle();
		RoutePlanType routePlan = determineRouteRequest.getRoutePlan();
		RouteInstructionsRequestType routeInstructions = determineRouteRequest.getRouteInstructionsRequest();
		RouteGeometryRequestType routeGeometry = determineRouteRequest.getRouteGeometryRequest();
		RouteMapRequestType routeMap = determineRouteRequest.getRouteMapRequest();
		DistanceUnitType distanceUnit = determineRouteRequest.getDistanceUnit();
		if (distanceUnit==null)
			distanceUnit = DistanceUnitType.M;
		
		PointType startPoint = null;
		PointType endPoint = null;
		double lng1;
		double lat1;
		double lng2;
		double lat2;
		try {
			startPoint = ((PositionType) (routePlan.getWayPointList().getStartPoint().getLocation().getValue())).getPoint();
			lng1 = startPoint.getPos().getValue().get(0);
			lat1 = startPoint.getPos().getValue().get(1);
		} catch (Exception e) {
			throw new RouteException("Start Point is null or invalid");
		}
		
		String distance = "metric";
		if (distanceUnit==DistanceUnitType.M || distanceUnit==DistanceUnitType.FT)
			distance = "imperial";
		
		try {
			endPoint = ((PositionType) (routePlan.getWayPointList().getEndPoint().getLocation().getValue())).getPoint();
			lng2 = endPoint.getPos().getValue().get(0);
			lat2 = endPoint.getPos().getValue().get(1);
		} catch (Exception e) {
			throw new RouteException("End Point is null or invalid");
		}
		
		
		String url = "http://maps.googleapis.com/maps/api/directions/json?origin=%s&destination=%s&sensor=false&distance=%s";
		url = String.format(url, lat1+","+lng1, lat2+","+lng2, distance);
		
		System.out.println(url);
		return url;
	}
	
	public static RouteResult sendRequest2GM(String url) throws ConnectException, IOException, JSONException{
		HttpData data = HttpRequest.get(url, true);
		String content = data.content;
		
		RouteResult routeResult = new RouteResult();
		JSONObject jsonResult = new JSONObject(content);
		JSONArray jsonARoutes = jsonResult.getJSONArray("routes");
		if (jsonARoutes.length()>0){
			JSONObject jsonRoute = jsonARoutes.getJSONObject(0);
			
			JSONObject jsonBounds = jsonRoute.getJSONObject("bounds");
			JSONObject jsonNortheast = jsonBounds.getJSONObject("northeast");
			routeResult.leftTopBB = new GeoPoint(Double.parseDouble(jsonNortheast.getString("lng")), 
					Double.parseDouble(jsonNortheast.getString("lat")));
			JSONObject jsonSouthwest = jsonBounds.getJSONObject("southwest");
			routeResult.rightBottomBB = new GeoPoint(Double.parseDouble(jsonSouthwest.getString("lng")), 
					Double.parseDouble(jsonSouthwest.getString("lat")));
			
			JSONArray jsonALegs = jsonRoute.getJSONArray("legs");
			if (jsonALegs.length()>0){
				JSONObject jsonLeg = jsonALegs.getJSONObject(0);
				JSONObject jsonDistance = jsonLeg.getJSONObject("distance");
				JSONObject jsonDuration = jsonLeg.getJSONObject("duration");
				routeResult.distance = jsonDistance.getString("value");
				routeResult.time = jsonDuration.getString("value");
				JSONArray jsonASteps = jsonLeg.getJSONArray("steps");
				
				for (int i=0;i<jsonASteps.length();i++){
					JSONObject jsonStep = jsonASteps.getJSONObject(i);
					
					JSONObject jsonDurationStep = jsonStep.getJSONObject("duration");
					JSONObject jsonDistanceStep = jsonStep.getJSONObject("distance");
					JSONObject jsonPolyline = jsonStep.getJSONObject("polyline");
					
					RouteInstruction routeIns = new RouteInstruction();
					routeIns.duration = jsonDurationStep.getString("value");
					routeIns.distance = jsonDistanceStep.getString("value");
					routeIns.instruction = jsonStep.getString("html_instructions");
					routeResult.listRouteIns.add(routeIns);
					
					List<GeoPoint> listGeoPoints = decodePoly(jsonPolyline.getString("points"));
					routeIns.listPoints.addAll(listGeoPoints);
				}
			}
		}
		return routeResult;
	}
	
	public static DetermineRouteResponseType outputBuilder(RouteResult routeResult){
		DetermineRouteResponseType determineRouteResponse = new DetermineRouteResponseType();
		RouteSummaryType routeSummary = new RouteSummaryType();
		ObjectFactory gmlObjectFactory = new ObjectFactory(); 
		DatatypeFactoryImpl datatypeFactory = new DatatypeFactoryImpl();
		
		determineRouteResponse.setRouteSummary(routeSummary);
		
		try {
			DistanceType distanceType = new DistanceType();
			routeSummary.setTotalDistance(distanceType);
			distanceType.setValue(new BigDecimal(routeResult.distance));
		} catch (Exception e) {
		}

		try {
			routeSummary.setTotalTime(datatypeFactory.newDuration(Long.parseLong(routeResult.time)));
		} catch (Exception e) {
		}
		
		List<DirectPositionType> listDirectPositionTypes = new ArrayList<DirectPositionType>();
		DirectPositionType directPositionTypeTL = new DirectPositionType();
		ArrayList<Double> arrayListTL = new ArrayList<Double>();
		arrayListTL.add(routeResult.leftTopBB.lng);
		arrayListTL.add(routeResult.leftTopBB.lat);
		directPositionTypeTL.setValue(arrayListTL);
		listDirectPositionTypes.add(directPositionTypeTL);
		
		DirectPositionType directPositionTypeRB = new DirectPositionType();
		ArrayList<Double> arrayListRB = new ArrayList<Double>();
		arrayListRB.add(routeResult.rightBottomBB.lng);
		arrayListRB.add(routeResult.rightBottomBB.lat);
		directPositionTypeRB.setValue(arrayListRB);
		listDirectPositionTypes.add(directPositionTypeRB);

		EnvelopeType envelopeType = new EnvelopeType();
		envelopeType.setPos(listDirectPositionTypes);
		routeSummary.setBoundingBox(envelopeType);
		
		RouteGeometryType routeGeometry = new RouteGeometryType();
		determineRouteResponse.setRouteGeometry(routeGeometry);
//		LineStringType lineStringType = new LineStringType();
//		routeGeometry.setLineString(lineStringType);
//		ArrayList<JAXBElement<?>> listJAXBDirectPosition = new ArrayList<JAXBElement<?>>();
//		lineStringType.setPosOrPointPropertyOrPointRep(listJAXBDirectPosition);
//		
//		for (GeoPoint p : routeResult.listPoints){
//			DirectPositionType directPositionType = new DirectPositionType();
//			JAXBElement<DirectPositionType> jAXBDirecPosition = gmlObjectFactory.createPos(directPositionType);
//			listJAXBDirectPosition.add(jAXBDirecPosition);
//			
//			List<Double> listCoord = new ArrayList<Double>();
//			directPositionType.setValue(listCoord);
//			listCoord.add(p.lng);
//			listCoord.add(p.lat);
//		}
		
		RouteInstructionsListType routeInstructionsList = new RouteInstructionsListType();
		determineRouteResponse.setRouteInstructionsList(routeInstructionsList);
		for (RouteInstruction routeIns : routeResult.listRouteIns){
			RouteInstructionType routeInstruction = new RouteInstructionType();
			routeInstructionsList.getRouteInstruction().add(routeInstruction);
			
			try {
				DistanceType distanceType = new DistanceType();
				distanceType.setValue(new BigDecimal(routeIns.distance));
				routeInstruction.setDistance(distanceType);
			} catch (Exception e) {
			}
			
			try {
				routeInstruction.setDuration(datatypeFactory.newDuration(routeIns.duration));
			} catch (Exception e) {
			}
			
			routeInstruction.setInstruction(routeIns.instruction);
			routeInstruction.setDescription(routeIns.description);
			RouteGeometryType routeGeometryType = new RouteGeometryType();
			routeInstruction.setRouteInstructionGeometry(routeGeometryType);
			
			LineStringType lineStringType = new LineStringType();
			routeGeometryType.setLineString(lineStringType);
			ArrayList<JAXBElement<?>> listJAXBDirectPosition = new ArrayList<JAXBElement<?>>();
			for (GeoPoint p : routeIns.listPoints){
				DirectPositionType directPositionType = new DirectPositionType();
				JAXBElement<DirectPositionType> jAXBDirecPosition = gmlObjectFactory.createPos(directPositionType);
				
				List<Double> listCoord = new ArrayList<Double>();
				listCoord.add(p.lng);
				listCoord.add(p.lat);
				directPositionType.setValue(listCoord);
				listJAXBDirectPosition.add(jAXBDirecPosition);
			}
			lineStringType.setPosOrPointPropertyOrPointRep(listJAXBDirectPosition);
		}
		
		return determineRouteResponse;
	}
	
	public static List<GeoPoint> decodePoly(String encoded) {

	    List<GeoPoint> poly = new ArrayList<GeoPoint>();
	    int index = 0, len = encoded.length();
	    int lat = 0, lng = 0;

	    while (index < len) {
	        int b, shift = 0, result = 0;
	        do {
	            b = encoded.charAt(index++) - 63;
	            result |= (b & 0x1f) << shift;
	            shift += 5;
	        } while (b >= 0x20);
	        int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
	        lat += dlat;

	        shift = 0;
	        result = 0;
	        do {
	            b = encoded.charAt(index++) - 63;
	            result |= (b & 0x1f) << shift;
	            shift += 5;
	        } while (b >= 0x20);
	        int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
	        lng += dlng;

	        GeoPoint p = new GeoPoint((int) (((double) lng / 1E5) * 1E6), (int) (((double) lat / 1E5) * 1E6));
	        poly.add(p);
	    }

	    return poly;
	}
}
