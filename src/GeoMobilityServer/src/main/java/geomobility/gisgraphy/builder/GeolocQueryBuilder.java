package geomobility.gisgraphy.builder;

import javax.servlet.http.HttpServletRequest;

import com.gisgraphy.domain.geoloc.entity.GisFeature;
import com.gisgraphy.domain.geoloc.service.geoloc.GeolocQuery;
import com.gisgraphy.domain.geoloc.service.geoloc.GeolocQueryHttpBuilder;
import com.gisgraphy.domain.geoloc.service.geoloc.GeolocSearchException;
import com.gisgraphy.domain.valueobject.GisgraphyConfig;
import com.gisgraphy.domain.valueobject.GisgraphyServiceType;
import com.gisgraphy.domain.valueobject.Output;
import com.gisgraphy.domain.valueobject.OutputFormatHelper;
import com.gisgraphy.domain.valueobject.Pagination;
import com.gisgraphy.helper.GeolocHelper;
import com.gisgraphy.serializer.OutputFormat;
import com.gisgraphy.servlet.GeolocServlet;
import com.gisgraphy.servlet.GisgraphyServlet;
import com.vividsolutions.jts.geom.Point;

/**
 * @author VoMinhTam
 * @see GeolocQueryHttpBuilder
 */
public class GeolocQueryBuilder {
	private static GeolocQueryBuilder instance = new GeolocQueryBuilder();

	protected GeolocQueryBuilder() {
	}

	public static GeolocQueryBuilder getInstance() {
		return instance;
	}

	/**
	 * @throws GeolocSearchException 
	 * @throws IllegalArgumentException
	 *             - if latitude is not between -90 and 90, or longitude is not
	 *             between -180 and 180
	 */
	public GeolocQuery buildGeolocQuery(float latitude, float longitude, double radius, int from, int maxResult, String formatStr,
			String placeTypeStr, String indentStr, String distanceStr, String callbackStr, String query) throws GeolocSearchException {

		// point
		Point point;
		try {
			point = GeolocHelper.createPoint(longitude, latitude);
		} catch (RuntimeException e1) {
			throw new GeolocSearchException(e1.getMessage());
		}
		if (point == null) {
			throw new GeolocSearchException("can not determine Point");
		}
		// radius
		if (radius == 0)
			radius = GeolocQuery.DEFAULT_RADIUS;

		GeolocQuery geolocQuery = constructMinimalQuery(point, radius);

		// pagination
		Pagination pagination = new Pagination(from, maxResult);
		
		// output format
		OutputFormat format = OutputFormat.getFromString(formatStr);
		format = OutputFormatHelper.getDefaultForServiceIfNotSupported(format, GisgraphyServiceType.GEOLOC);
		Output output = Output.withFormat(format);

		// indent
		if ("true".equalsIgnoreCase(indentStr) || "on".equalsIgnoreCase(indentStr)) {
			output.withIndentation();
		}

		// placetype
		Class<? extends GisFeature> clazz = GeolocHelper.getClassEntityFromString(placeTypeStr);

		// distance field
		if ("false".equalsIgnoreCase(distanceStr) || "off".equalsIgnoreCase(distanceStr)) {
			geolocQuery.withDistanceField(false);
		}

		if (callbackStr != null) {
			geolocQuery.withCallback(callbackStr);
		}

		geolocQuery.withPagination(pagination);
		if (clazz == null) {
			geolocQuery.withPlaceType(GisgraphyConfig.defaultGeolocSearchPlaceTypeClass);
		} else {
			geolocQuery.withPlaceType(clazz);
			geolocQuery.withOutput(output);
		}
		
		geolocQuery.setQuery(query);
		return geolocQuery;
	}

	/**
	 * Create a basic GeolocQuery. this method must be overide if we need to
	 * create inheritance object
	 * 
	 * @param point
	 *            the JTS point to create the query
	 * @param radius
	 *            the radius to search around
	 * @return
	 */
	protected GeolocQuery constructMinimalQuery(Point point, double radius) {
		GeolocQuery geolocQuery = new GeolocQuery(point, radius);
		return geolocQuery;
	}
}
