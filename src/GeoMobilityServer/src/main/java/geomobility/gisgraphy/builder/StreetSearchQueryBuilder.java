package geomobility.gisgraphy.builder;

import com.gisgraphy.domain.geoloc.service.fulltextsearch.StreetSearchMode;
import com.gisgraphy.domain.geoloc.service.geoloc.GeolocQueryHttpBuilder;
import com.gisgraphy.domain.geoloc.service.geoloc.GeolocSearchException;
import com.gisgraphy.domain.geoloc.service.geoloc.StreetSearchException;
import com.gisgraphy.domain.geoloc.service.geoloc.StreetSearchQuery;
import com.gisgraphy.domain.geoloc.service.geoloc.street.StreetType;
import com.vividsolutions.jts.geom.Point;

/**
 * @author VoMinhTam
 * @see GeolocQueryHttpBuilder
 */
public class StreetSearchQueryBuilder extends GeolocQueryBuilder {
	private static StreetSearchQueryBuilder instance = new StreetSearchQueryBuilder();

	protected StreetSearchQueryBuilder() {
	}

	public static StreetSearchQueryBuilder getInstance() {
		return instance;
	}

	@Override
	protected StreetSearchQuery constructMinimalQuery(Point point, double radius) {
		StreetSearchQuery streetSearchQuery = new StreetSearchQuery(point,
				radius);
		return streetSearchQuery;
	}

	/**
	 * <a href="file:///D:/Location-based%20Services/GeoMobility/Stuff/gisgraphy-2.1-final/docs/user_guide/index.htm#streetserviceparameters"
	 * >Detail</a>
	 * 
	 * @param latitude
	 * @param longitude
	 * @param radius
	 * @param from
	 * @param to
	 * @param formatStr
	 * @param placeTypeStr
	 * @param indentStr
	 * @param distanceStr
	 *            # Distance field : Wether (or not) we want the distance field
	 *            to be filled. this option is usefull when we don't care about
	 *            the distance (e.g : we search for name) to improve the
	 *            performances. Off course, the resultq won't be sorted by
	 *            distance.
	 * @param callbackStr
	 * @param streettype
	 * @param oneway
	 * @param name
	 * @param mode
	 * @return
	 * @throws StreetSearchException 
	 * @throws GeolocSearchException 
	 */
	public StreetSearchQuery buildStreetSearchQuery(float latitude,
			float longitude, double radius, int from, int to, String formatStr,
			String placeTypeStr, boolean isIndentStr, boolean isInclueDistance,
			String callbackStr, StreetType streettype, Boolean isOneway,
			String name, StreetSearchMode mode) throws StreetSearchException, GeolocSearchException {
		StreetSearchQuery streetSearchQuery = 
				(StreetSearchQuery) super.buildGeolocQuery(latitude, longitude, radius, from, to, formatStr, placeTypeStr, 
				(isIndentStr) ? "yes" : "no", (isInclueDistance) ? "yes" : "no", callbackStr, null);

		// streettype
		streetSearchQuery.withStreetType(streettype);

		// OneWay
		streetSearchQuery.withOneWay(isOneway);

		// name
		streetSearchQuery.withName(name);

		streetSearchQuery.withStreetSearchMode(mode);
		return streetSearchQuery;
	}

}
