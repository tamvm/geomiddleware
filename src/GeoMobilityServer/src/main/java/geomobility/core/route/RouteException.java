package geomobility.core.route;

import geomobility.core.exception.GeoException;

public class RouteException extends GeoException{

	private static final long serialVersionUID = 1L;

	public RouteException(){}
	
	public RouteException(String msg){
		super(msg);
	}
}
