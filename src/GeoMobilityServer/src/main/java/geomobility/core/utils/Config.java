package geomobility.core.utils;

import com.gisgraphy.domain.valueobject.SRID;


/**
 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
 */
public class Config {
	
	public static final int REGEO_DEFAULT_DISTANCE = 1000;
	public static final int REGEO_MAX_ROADS_IN_POINTS = 5;

	//public static final int GEO_MAX_RESULT = 5;

	// Presentation Service
	public static final String GEOSERVER_PATH = "http://localhost:8080/geoserver/wms";
	public static final String WMS_PATH = GEOSERVER_PATH + "wms";
	public static final String DEFAULT_FORMAT_OUTPUT = "image/png";
	public static final String DEFAULT_WORKSPACE = "geomobility";
	public static final String DEFAULT_LAYER_NAME = "planet_osm_point";
	public static final String DEFAULT_LAYER = DEFAULT_WORKSPACE + ":"
			+ DEFAULT_LAYER_NAME;
	public static final int DEFAULT_WIDTH = 240;
	public static final int DEFAULT_HEIGHT = 512;
	public static final String DEFAULT_SRS_PREFIX = "EPSG:";
	public static final String DEFAULT_SRD = DEFAULT_SRS_PREFIX
			+ SRID.WGS84_SRID.getSRID();
	public static final String WMS_VERSION = "1.1.0";
	
	public static final String DEFAULT_IMG_FORMAT = "image/svg";
	public static final String DEFAULT_BGCOLOR = "#FFFFFF";
}
