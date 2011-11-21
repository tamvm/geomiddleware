package geomobility.core.route;

import java.io.IOException;
import java.net.ConnectException;

import org.codehaus.jettison.json.JSONException;

import com.gisgraphy.domain.geoloc.service.ServiceException;

import geomobility.core.exception.GeoException;
import geomobility.core.utils.StaticFunc;

public class RouteTest {
	public static void main(String[] args) throws Exception {
		testNormal();
//		testRouteWithGeometry();
//		testRouteWithoutGeometry();
	}
	
	public static void testNormal() throws GeoException, ServiceException, ConnectException, IOException, JSONException{
		String xml = StaticFunc.getContent("data/routing/req_route_in_Swedish.xml");
		System.out.println(RouteController.doController(xml));
	}
	
	public static void testRouteWithGeometry() throws GeoException, ServiceException, ConnectException, IOException, JSONException{
		String xml = StaticFunc.getContent("data/routing/req_route_with_geometry.xml");
		System.out.println(RouteController.doController(xml));
	}
	
	public static void testRouteWithoutGeometry() throws GeoException, ServiceException, ConnectException, IOException, JSONException{
		String xml = StaticFunc.getContent("data/routing/req_route_without_geometry.xml");
		System.out.println(RouteController.doController(xml));
	}
}
