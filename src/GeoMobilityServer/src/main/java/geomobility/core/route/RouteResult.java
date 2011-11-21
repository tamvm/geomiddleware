package geomobility.core.route;

import geomobility.core.utils.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class RouteResult {
	public String distance;
	public String time;
	
	public GeoPoint leftTopBB;
	public GeoPoint rightBottomBB;
//	public List<GeoPoint> listPoints = new ArrayList<GeoPoint>();
	public List<RouteInstruction> listRouteIns = new ArrayList<RouteResult.RouteInstruction>();
	
	
	static class RouteInstruction{
		public String description;
		public String duration;
		public String instruction;
		public String distance;
		public List<GeoPoint> listPoints = new ArrayList<GeoPoint>();
	}
}
