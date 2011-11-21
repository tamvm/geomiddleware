/*******************************************************************************
 *   Gisgraphy Project 
 * 
 *   This library is free software; you can redistribute it and/or
 *   modify it under the terms of the GNU Lesser General Public
 *   License as published by the Free Software Foundation; either
 *   version 2.1 of the License, or (at your option) any later version.
 * 
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *   Lesser General Public License for more details.
 * 
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA
 * 
 *  Copyright 2008  Gisgraphy project 
 *  David Masclet <davidmasclet@gisgraphy.com>
 *  
 *  
 *******************************************************************************/
package com.gisgraphy.helper;

import geomobility.core.exception.GeoException;
import geomobility.core.utils.Config;
import geomobility.core.utils.GeoPoint;
import geomobility.core.utils.Log;
import geomobility.core.utils.StaticFunc;
import geomobility.data.osm.entity.SpatialRefSys;
import geomobility.localization.I18N;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import net.opengis.xls.v_1_1_0.DistanceUnitType;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import tam.HibernateUtil;

import com.gisgraphy.domain.geoloc.entity.GisFeature;
import com.gisgraphy.domain.valueobject.Constants;
import com.gisgraphy.domain.valueobject.FeatureCode;
import com.gisgraphy.domain.valueobject.SRID;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.WKBReader;
import com.vividsolutions.jts.io.WKTReader;

/**
 * Provides useful methods for geolocalisation
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class GeolocHelper {
	
	private static final String TAG = GeolocHelper.class.getSimpleName();

	private static final double COS0 = Math.cos(0);
	private static final double SIN90 = Math.sin(Math.PI/2);
	public static final int R = 6371000;

	private static final String INTERSECTION = "&&";
	private static final String BBOX = "BOX3D";

	/**
	 * The logger
	 */
	protected static final Logger logger = LoggerFactory
			.getLogger(GeolocHelper.class);

	/**
	 * Create a JTS point from the specified longitude and latitude for the SRID
	 * (aka : Spatial Reference IDentifier) 4326 (WGS84)<br>
	 * 
	 * @see <a href="http://en.wikipedia.org/wiki/SRID">SRID</a>
	 * @see SRID
	 * @param longitude
	 *            The longitude for the point
	 * @param latitude
	 *            The latitude for the point
	 * @return A jts point from the specified longitude and latitude
	 * @throws IllegalArgumentException
	 *             if latitude is not between -90 and 90, or longitude is not
	 *             between -180 and 180
	 */
	public static Point createPoint(Float longitude, Float latitude) {
		if (longitude < -180 || longitude > 180) {
			throw new IllegalArgumentException(
					"Longitude should be between -180 and 180");
		}
		if (latitude < -90 || latitude > 90) {
			throw new IllegalArgumentException(
					"latitude should be between -90 and 90");
		}
		GeometryFactory factory = new GeometryFactory(new PrecisionModel(
				PrecisionModel.FLOATING), SRID.WGS84_SRID.getSRID());
		Point point = (Point) factory.createPoint(new Coordinate(longitude,
				latitude));
		return point;
	}

	public static Point createXYPoint(double x, double y) {
		GeometryFactory factory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), SRID.WGS84_SRID.getSRID());
		Point point = (Point) factory.createPoint(new Coordinate(x, y));
		return point;
	}

	/**
	 * Create a JTS MultiLineString from the specified array of linestring for
	 * the SRID (aka : Spatial Reference IDentifier) 4326 (WGS84)<br>
	 * 
	 * example : {"LINESTRING (0 0, 10 10, 20 20)","LINESTRING (30 30, 40 40, 50
	 * 50)"}
	 * 
	 * @see <a href="http://en.wikipedia.org/wiki/SRID">SRID</a>
	 * @see SRID
	 * @param wktLineStrings
	 *            The array that contains all the linestrings
	 * @return A MultilineStringObject from the specified array of linestring
	 * @throws IllegalArgumentException
	 *             if the string are not correct
	 */
	public static MultiLineString createMultiLineString(String[] wktLineStrings) {
		LineString[] lineStrings = new LineString[wktLineStrings.length];
		for (int i = 0; i < wktLineStrings.length; i++) {
			LineString ls;
			try {
				ls = (LineString) new WKTReader().read(wktLineStrings[i]);
			} catch (com.vividsolutions.jts.io.ParseException pe) {
				throw new IllegalArgumentException(wktLineStrings[i]
						+ " is not valid " + pe);
			} catch (ClassCastException cce) {
				throw new IllegalArgumentException(wktLineStrings[i]
						+ " is not a LINESTRING");
			}
			lineStrings[i] = ls;
		}

		GeometryFactory factory = new GeometryFactory(new PrecisionModel(
				PrecisionModel.FLOATING), SRID.WGS84_SRID.getSRID());
		MultiLineString multiLineString = (MultiLineString) factory
				.createMultiLineString(lineStrings);
		return multiLineString;
	}

	/**
	 * Create a JTS LineString from the specified String for the SRID (aka :
	 * Spatial Reference IDentifier) 4326 (WGS84)<br>
	 * 
	 * example : {"LINESTRING (0 0, 10 10, 20 20)"}
	 * 
	 * @see <a href="http://en.wikipedia.org/wiki/SRID">SRID</a>
	 * @see SRID
	 * @param wktLineString
	 *            a String that represent the lineString
	 * @return A LineStringObject from the specified linestring
	 * @throws IllegalArgumentException
	 *             if the string are not correct
	 */
	public static LineString createLineString(String wktLineString) {
		try {
			LineString lineString = (LineString) new WKTReader()
					.read(wktLineString);
			lineString.setSRID(SRID.WGS84_SRID.getSRID());
			return lineString;
		} catch (com.vividsolutions.jts.io.ParseException pe) {
			throw new IllegalArgumentException(wktLineString + " is not valid "
					+ pe);
		} catch (ClassCastException cce) {
			throw new IllegalArgumentException(wktLineString
					+ " is not a LINESTRING");
		}
	}

	/**
	 * Calculate the distance between the specified point.
	 * 
	 * @param point1
	 *            The first JTS point
	 * @param point2
	 *            The second JTS point
	 * @return The calculated distance
	 */
	public static double distance(Point point1, Point point2) {
		Assert.isTrue(point1 != null && point2 != null,
				"Can not calculate distance for null point");

		// Unit<Length> targetUnit = (unit != null) ? unit : SI.METER;
		try {
			double distance = JTS.orthodromicDistance(point1.getCoordinate(),
					point2.getCoordinate(), DefaultGeographicCRS.WGS84);
			return distance;
			// return SI.METER.getConverterTo(targetUnit).convert(distance);
		} catch (TransformException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Return the class corresponding to the specified String or null if not
	 * found. The Class will be searched in the 'entity' package. The search is
	 * not case sensitive. This method is mainly used to determine an entity
	 * Class from a web parameter
	 * 
	 * @param classNameWithoutPackage
	 *            the simple name of the Class we want to retrieve
	 * @return The class corresponding to the specified String or null if not
	 *         found.
	 */
	@SuppressWarnings("unchecked")
	public static Class<? extends GisFeature> getClassEntityFromString(
			String classNameWithoutPackage) {
		if (classNameWithoutPackage != null) {
			return FeatureCode.entityClass.get(classNameWithoutPackage
					.toLowerCase());
		}
		return null;
	}

	/**
	 * parse a string and return the corresponding double value, it accepts
	 * comma or point as decimal separator
	 * 
	 * @param number
	 *            the number with a point or a comma as decimal separator
	 * @return the float value corresponding to the parsed string
	 * @throws ParseException
	 *             in case of errors
	 */
	public static float parseInternationalDouble(String number)
			throws ParseException {
		NumberFormat nffrench = NumberFormat.getInstance(Locale.FRENCH);
		NumberFormat nfus = NumberFormat.getInstance(Locale.US);

		Number numberToReturn = number.indexOf(',') != -1 ? nffrench
				.parse(number) : nfus.parse(number);
		return numberToReturn.floatValue();
	}

	/**
	 * @param lat
	 *            the central latitude for the Polygon
	 * @param lng
	 *            the central longitude for the polygon
	 * @param distance
	 *            the distance in meters from the point to create the polygon
	 * @return a polygon / square with a side of distance , with the point
	 *         (long,lat) as centroid throws {@link RuntimeException} if an
	 *         error occured thros {@link IllegalArgumentException} if lat, long
	 *         or distance is not correct
	 */
	public static Polygon createPolygonBox(double lng, double lat,
			double distance) {
		if (distance <= 0) {
			throw new IllegalArgumentException("distance is incorect : "
					+ distance);
		}

		double latrad = Math.toRadians(lat);
		double angulardistance = distance / Constants.RADIUS_OF_EARTH_IN_METERS;
		double latRadSinus = Math.sin(latrad);
		double latRadCosinus = Math.cos(latrad);
		double angularDistanceCosinus = Math.cos(angulardistance);
		double deltaYLatInRadian = Math.abs(Math.asin(latRadSinus
				* angularDistanceCosinus + latRadCosinus
				* Math.sin(angulardistance) * COS0)
				- latrad);

		double deltaXlngInRadian = Math.abs(Math.atan2(SIN90
				* Math.sin(angulardistance) * latRadCosinus,
				angularDistanceCosinus - latRadSinus * latRadSinus));

		double deltaYLatInDegree = Math.toDegrees(deltaYLatInRadian);
		double deltaXlngInDegree = Math.toDegrees(deltaXlngInRadian);

		double minX = lng - deltaXlngInDegree;
		double maxX = lng + deltaXlngInDegree;
		double minY = lat - deltaYLatInDegree;
		double maxY = lat + deltaYLatInDegree;

		WKTReader reader = new WKTReader();
		StringBuffer sb = new StringBuffer("POLYGON((");
		String polygonString = sb.append(minX).append(" ").append(minY).append(
				",").append(maxX).append(" ").append(minY).append(",").append(
				maxX).append(" ").append(maxY).append(",").append(minX).append(
				" ").append(maxY).append(",").append(minX).append(" ").append(
				minY).append("))").toString();
		try {
			Polygon polygon = (Polygon) reader.read(polygonString);
			polygon.setSRID(SRID.WGS84_SRID.getSRID());
			return polygon;
		} catch (com.vividsolutions.jts.io.ParseException e) {
			throw new RuntimeException("can not create Polygon for lat=" + lat
					+ " long=" + lng + " and distance=" + distance + " : " + e);
		}
	}
	public static Polygon createCirclePolygonBox(double lng, double lat,double distance,int n ) {
		if (distance <= 0) {
			throw new IllegalArgumentException("distance is incorect : "
					+ distance);
		}

		double latrad = Math.toRadians(lat);
		double lngrad = Math.toRadians(lng);
		double angulardistance = distance / Constants.RADIUS_OF_EARTH_IN_METERS;
		double latRadSinus = Math.sin(latrad);
		double latRadCosinus = Math.cos(latrad);
		double angularDistanceCosinus = Math.cos(angulardistance);
		WKTReader reader = new WKTReader();
		StringBuffer sb = new StringBuffer("POLYGON((");
		String polygonString = "";
		Log.d(GeolocHelper.class.toString() + " - Latitude and Longitude", "" + lat + ", " + lng);
		Log.d("GeolocHelper - 2*i*Math.Pi/n", ""+ 2*3*Math.PI/n);
		for(int i = 0;i<=n;i++)
		{
			double latRadians = Math.asin(Math.sin(latrad) * Math.cos(angulardistance) + Math.cos(latrad) * Math.sin(angulardistance) * Math.cos(2*i*Math.PI/n));
			double lngRadians = lngrad + Math.atan2(Math.sin(2*i*Math.PI/n) * Math.sin(angulardistance) * Math.cos(latrad), Math.cos(angulardistance) - Math.sin(latrad) * Math.sin(latRadians));
			sb = sb.append(Math.toDegrees(lngRadians)).append(" ").append(Math.toDegrees(latRadians)).append(",");
		}
		polygonString = sb.deleteCharAt(sb.length()-1).append("))").toString();
		
		Log.d(GeolocHelper.class.toString() + " - PolygonString", polygonString);

		try {
			Polygon polygon = (Polygon) reader.read(polygonString);
			polygon.setSRID(SRID.WGS84_SRID.getSRID());
			return polygon;
		} catch (com.vividsolutions.jts.io.ParseException e) {
			throw new RuntimeException("can not create Polygon for lat=" + lat
					+ " long=" + lng + " and distance=" + distance + " : " + e);
		}
	}

	/**
	 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
	 */
	public static Polygon createPolygon(Point p1, Point p2) {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("POLYGON((");

		strBuilder.append(p1.getX() + " " + p1.getY() + ",");
		strBuilder.append(p2.getX() + " " + p1.getY() + ",");
		strBuilder.append(p2.getX() + " " + p2.getY() + ",");
		strBuilder.append(p1.getX() + " " + p2.getY() + ",");
		strBuilder.append(p1.getX() + " " + p1.getY());

		strBuilder.append("))");

		System.out.println(strBuilder);

		WKTReader reader = new WKTReader();
		try {
			Polygon polygon = (Polygon) reader.read(strBuilder.toString());
			polygon.setSRID(SRID.WGS84_SRID.getSRID());
			return polygon;
		} catch (com.vividsolutions.jts.io.ParseException e) {
			throw new RuntimeException("can not create Polygon from: " + p1
					+ " and " + p2);
		}
	}

	/**
	 * Create Polygon Type of PostGIS from list of Point
	 * 
	 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
	 * @param points
	 * @return
	 * @throws GeoException 
	 */
	public static Polygon createPolygon(List<List<Point>> listListPoints) throws GeoException {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("POLYGON(");

		for (int i = 0; i < listListPoints.size(); i++) {
			List<Point> listPoints = listListPoints.get(i);
			strBuilder.append("(");
			for (int j = 0; j < listPoints.size(); j++) {
				Point p = listPoints.get(j);
				strBuilder.append(p.getX() + " " + p.getY() +", ");
			}
			strBuilder.append(listPoints.get(0).getX() + " " + listPoints.get(0).getY());
			strBuilder.append(")");
			if (i != listListPoints.size() - 1)
				strBuilder.append(",");
		}
		strBuilder.append(")");

		WKTReader reader = new WKTReader();
		try {
			Polygon polygon = (Polygon) reader.read(strBuilder.toString());
			polygon.setSRID(SRID.WGS84_SRID.getSRID());
			return polygon;
		} catch (com.vividsolutions.jts.io.ParseException e) {
			throw new GeoException("can not create Polygon from arr Points");
		}
	}
	
	public static Polygon createPolygonFromListPoint(List<Point> listPoints) throws GeoException {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("POLYGON((");

		for (int i = 0; i < listPoints.size(); i++) {
			Point p = listPoints.get(i);
			strBuilder.append(p.getX() + " " + p.getY() + ", ");
		}
		strBuilder.append(listPoints.get(0).getX() + " " + listPoints.get(0).getY() + ", ");
		strBuilder.append("))");

		WKTReader reader = new WKTReader();
		try {
			Polygon polygon = (Polygon) reader.read(strBuilder.toString());
			polygon.setSRID(SRID.WGS84_SRID.getSRID());
			return polygon;
		} catch (com.vividsolutions.jts.io.ParseException e) {
			throw new GeoException("can not create Polygon from arr Points: " + listPoints);
		}
	}

	// public static void main(String[] args) {
	// System.out.println(distance(createPoint(52F, 6F), createPoint(
	// 52.2165107727051F, 6.00290012359619F)));
	// System.out.println(distance(6, 52, 6.00290012359619,
	// 52.2165107727051));

	// ///////////////////
	// List<List<Point>> list = new ArrayList<List<Point>>();
	//		
	// List<Point> listPoints = new ArrayList<Point>();
	// listPoints.add(GeolocHelper.createPoint(10F, 10F));
	// listPoints.add(GeolocHelper.createPoint(20F, 20F));
	// listPoints.add(GeolocHelper.createPoint(30F, 30F));
	// listPoints.add(GeolocHelper.createPoint(10F, 10F));
	//	
	// list.add(listPoints);
	//		
	// listPoints = new ArrayList<Point>();
	// listPoints.add(GeolocHelper.createPoint(20F, 10F));
	// listPoints.add(GeolocHelper.createPoint(20F, 20F));
	// listPoints.add(GeolocHelper.createPoint(30F, 30F));
	// listPoints.add(GeolocHelper.createPoint(20F, 10F));
	//		
	// list.add(listPoints);
	//		
	// GeolocHelper.createPolygon(list);

	// GeolocHelper.createPolygonBox(10F, 20D, 10000);

	// GeolocHelper.createPolygon(createPoint(10F,30F), createPoint(20F, 40F));
	// }

	/**
	 * @param alias
	 *            the sql alias
	 * @param latInDegree
	 *            the latitude in degree
	 * @param longInDegree
	 *            the longitude in degree
	 * @param distance
	 *            the boundingbox distance
	 * @return a sql String that represents the bounding box
	 */
	public static String getBoundingBox(String alias, double latInDegree,
			double longInDegree, double distance) {

		double lat = Math.toRadians(latInDegree);
		double lon = Math.toRadians(longInDegree);

		double deltaXInDegrees = Math.abs(Math.asin(Math.sin(distance / Constants.RADIUS_OF_EARTH_IN_METERS) / Math.cos(lat)));
		double deltaYInDegrees = Math.abs(distance / Constants.RADIUS_OF_EARTH_IN_METERS);

		double minX = Math.toDegrees(lon - deltaXInDegrees);
		double maxX = Math.toDegrees(lon + deltaXInDegrees);
		double minY = Math.toDegrees(lat - deltaYInDegrees);
		double maxY = Math.toDegrees(lat + deltaYInDegrees);

		StringBuffer sb = new StringBuffer();
		// {alias}.location && setSRID(BOX3D(...), 4326)
		sb.append(alias);
		sb.append(".").append(GisFeature.LOCATION_COLUMN_NAME);
		sb.append(" ");
		sb.append(INTERSECTION);
		sb.append(" setSRID(");

		// Construct the BBOX : 'BOX3D(-119.2705528794688
		// 33.15289952334886,-117.2150071205312 34.95154047665114)'::box3d
		sb.append("cast (");
		sb.append("'");
		sb.append(BBOX);
		sb.append("(");
		sb.append(minX); // minX
		sb.append(" ");
		sb.append(minY); // minY
		sb.append(",");
		sb.append(maxX); // maxX
		sb.append(" ");
		sb.append(maxY); // maxY
		sb.append(")'as box3d)"); // cannot use the ::box3d notation, since
		// nativeSQL interprets :param as a named
		// parameter

		// end of the BBOX, finish the setSRID
		sb.append(", ");
		sb.append(SRID.WGS84_SRID.getSRID());
		sb.append(") ");

		return sb.toString();

	}

	/**
	 * @param hewewkbt
	 *            the string in hexa well know text
	 * @return the geometry type, or throw an exception if the string can not be
	 *         convert.the SRID will be {@link SRID#WGS84_SRID}
	 */
	public static Geometry convertFromHEXEWKBToGeometry(String hewewkbt) {
		try {
			WKBReader wkReader = new WKBReader();
			Geometry geometry = wkReader.read(hexToBytes(hewewkbt.trim()));
			geometry.setSRID(SRID.WGS84_SRID.getSRID());
			return geometry;
		} catch (com.vividsolutions.jts.io.ParseException e) {
			logger.error(e.toString());
			throw new RuntimeException(
					"error when convert HEXEWKB to Geometry", e);
		}

	}

	private static byte[] hexToBytes(String wkb) {
		// convert the String of hex values to a byte[]
		byte[] wkbBytes = new byte[wkb.length() / 2];

		for (int i = 0; i < wkbBytes.length; i++) {
			byte b1 = getFromChar(wkb.charAt(i * 2));
			byte b2 = getFromChar(wkb.charAt((i * 2) + 1));
			wkbBytes[i] = (byte) ((b1 << 4) | b2);
		}

		return wkbBytes;
	}

	/**
	 * Turns a char that encodes four bits in hexadecimal notation into a byte
	 * 
	 * @param c
	 *            the char to convert
	 * 
	 */
	public static byte getFromChar(char c) {
		if (c <= '9') {
			return (byte) (c - '0');
		} else if (c <= 'F') {
			return (byte) (c - 'A' + 10);
		} else {
			return (byte) (c - 'a' + 10);
		}
	}

	/**
	 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
	 */
	private static String[] tables = { "restaurant" };

	public static String[] getListPOIType(String s) {
		String[] result = new String[2];
		String firstWord;
		int spaceIndex = s.indexOf(' ');
		if (spaceIndex == -1)
			firstWord = s.toLowerCase();
		else
			firstWord = s.substring(0, spaceIndex).toLowerCase();

		String lastWord;
		spaceIndex = s.lastIndexOf(' ');
		if (spaceIndex == -1)
			lastWord = s.toLowerCase();
		else
			lastWord = s.substring(spaceIndex + 1).toLowerCase();

		for (String str : tables) {
			if (str.equals(firstWord))
				return new String[] { firstWord, s.replaceFirst(firstWord + " ?", "") };
			else if (str.equals(lastWord))
				return new String[] { lastWord, s.replaceFirst(" ?" + lastWord, "") };
		}
		return new String[] { "adm", s }; //return type the remaining text
	}

	private static double deg2rad(double deg) {
		return (Math.PI / 180) * deg;
	}

	private static double rad2deg(double radians) {
		return (180 / Math.PI) * radians;
	}

	/**
	 * Refer <a href="http://www.freevbcode.com/ShowCode.asp?ID=5532">Calculate
	 * Distance Between 2 Points Given Longitude/Latitude (ASP)</a>
	 * 
	 * @return kilometer
	 */
	public static double distance(double lat1, double lng1, double lat2,
			double lng2) {
		double theta = lng1 - lng2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
				* Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		double result = dist * 60 * 1.1515;
		return result * 1.609344 * 1000;
	}

	/**
	 * Calculate the initial bearing (angle between 2 points with equator). This
	 * formula is for the initial bearing (sometimes referred to as forward
	 * azimuth) which if followed in a straight line along a great-circle arc
	 * will take you from the start point to the end point.<br >
	 * Refer: <a
	 * href="http://www.movable-type.co.uk/scripts/latlong.html#bearing"
	 * >Calculate bearing</a>
	 * 
	 * @return
	 */
	public static double bearing(double lat1, double lon1, double lat2,
			double lon2) {
		// double dLat = deg2rad(lat2-lat1);
		double dLon = deg2rad(lon2 - lon1);
		double y = Math.sin(dLon) * Math.cos(lat2);
		double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
				* Math.cos(lat2) * Math.cos(dLon);
		return rad2deg(Math.atan2(y, x));
	}

	/**
	 * Calculate the midpoint between 2 points<br>
	 * Refer <a
	 * href="http://www.movable-type.co.uk/scripts/latlong.html#midpoint"
	 * >Calculate Midpoint</a>
	 * 
	 * @return
	 */
	public GeoPoint midpoint(double lon1, double lat1, double lon2, double lat2) {
		double dLon = deg2rad(lon2 - lon1);
		double Bx = Math.cos(lat2) * Math.cos(dLon);
		double By = Math.cos(lat2) * Math.sin(dLon);
		double lat3 = Math.atan2(Math.sin(lat1) + Math.sin(lat2), Math
				.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By));
		double lon3 = lon1 + Math.atan2(By, Math.cos(lat1) + Bx);

		return new GeoPoint(lon3, lat3);
	}

	public GeoPoint destFromDistanceBearingStartPoint(double lon1, double lat1, 
			double bearing, double d) {
		double lat2 = Math.asin(Math.sin(lat1) * Math.cos(d / R) + Math.cos(lat1) * Math.sin(d / R) * Math.cos(bearing));
		double lon2 = lon1 + 
			Math.atan2(Math.sin(bearing) * Math.sin(d / R) * Math.cos(lat1), Math.cos(d / R) - Math.sin(lat1) * Math.sin(lat2));
		return new GeoPoint(lon2, lat2);
	}

	public static boolean isInsideCircle(double latCenter1, double lonCenter1,
			double r, double lat2, double lon2) {
		double d = distance(latCenter1, lonCenter1, lat2, lon2);

		return (d > r) ? false : true;
	}

	public static void asd(double latitude, double longitude) {
		double x = R * Math.cos(longitude) * Math.sin(90 - latitude);
		double y = R * Math.sin(longitude) * Math.sin(90 - latitude);
		double z = R * Math.cos(90 - latitude);

		System.out.println(x + " " + y + " " + z);
	}

	public static boolean transform(int srsFrom, int srsTo, double[] fromCoors,
			double[] toCoors) throws NoSuchAuthorityCodeException,
			FactoryException, TransformException, GeoException {
		if (fromCoors.length != toCoors.length)
			return false;

		CoordinateReferenceSystem epsg4326 = createCRS(srsTo);
		CoordinateReferenceSystem epsgFrom = createCRS(srsFrom);

		MathTransform tx = CRS.findMathTransform(epsgFrom, epsg4326);

		tx.transform(fromCoors, 0, toCoors, 0, fromCoors.length / 2);

		return true;
	}

	public static boolean transform(String srsFrom, String srsTo,
			double[] fromCoors, double[] toCoors)
			throws NoSuchAuthorityCodeException, FactoryException,
			TransformException, GeoException {
		if (fromCoors.length != toCoors.length)
			return false;

		CoordinateReferenceSystem epsg4326 = createCRS(getSRIDFrom(srsTo));
		CoordinateReferenceSystem epsgFrom = createCRS(getSRIDFrom(srsFrom));

		MathTransform tx = CRS.findMathTransform(epsgFrom, epsg4326);

		tx.transform(fromCoors, 0, toCoors, 0, fromCoors.length / 2);

		return true;
	}

	/**
	 * {@linkplain http
	 * ://stackoverflow.com/questions/1689096/calculating-bounding
	 * -box-a-certain-distance-away-from-a-lat-long-coordinate-in-ja}
	 * 
	 * @param lat
	 * @param lng
	 * @param distance
	 *            in meter
	 * @return
	 */
	public static Envelope getBounding(double lat, double lng, double radius) {
		double x1, x2, y1, y2;
		x1 = (lng - Math.toDegrees(radius / R / Math.cos(Math.toRadians(lat))));
		x2 = (lng + Math.toDegrees(radius / R / Math.cos(Math.toRadians(lat))));
		y1 = (lat + Math.toDegrees(radius / R));
		y2 = (lat - Math.toDegrees(radius / R));

		return new Envelope(x1, x2, y1, y2);
	}

	/**
	 * <b>Test: </b>http://www.unitconversion.org/typography/pixels-x-to-meters-
	 * conversion.html {@linkplain http
	 * ://stackoverflow.com/questions/1341930/pixel-to-centimeter}
	 * 
	 * @param pixel
	 * @return
	 */
	public static double convertPixel2Meter(int pixels) {
		return (pixels * 2.54 / 96) / 100;
	}

	/**
	 * Distance converter into meter
	 * 
	 * @param distanceUnit
	 *            : meter, feet, inches, kilometer
	 * @param length
	 * @return
	 */
	public static double getLengthInMeter(String distanceUnit, double length) {
		if (StaticFunc.isNOE(distanceUnit))
			return length;

		if (distanceUnit.toLowerCase().equals("kilometer"))
			return getLengthInMeter(DistanceUnitType.KM, length);
		else if (distanceUnit.toLowerCase().equals("feet"))
			return getLengthInMeter(DistanceUnitType.FT, length);
		else if (distanceUnit.toLowerCase().equals("inches"))
			return length / 39.37;
		else
			return length;
	}

	public static double getLengthInMeter(DistanceUnitType unit, double length) {
		if (unit == null)
			return length;

		if (unit == DistanceUnitType.KM)
			return length * 1000;
		else if (unit == DistanceUnitType.FT)
			return length / 3.281;
		else if (unit == DistanceUnitType.DM)
			return length / 10;
		else
			return length;
	}

	/**
	 * Angle converter into Degrees
	 * 
	 * @param angularUnit
	 *            : degrees, radians
	 * @param angle
	 * @return
	 */
	public static double getAngleInDegree(String angularUnit, double angle) {
		if (StaticFunc.isNOE(angularUnit))
			return angle;

		if (angularUnit.equals("radians"))
			return angle * 180 / Math.PI;
		else
			return angle;
	}

	/**
	 * Get SRID from String
	 */
	public static int getSRIDFrom(String srs) {
		if (StaticFunc.isNOE(srs))
			return 0;

		int idx = srs.length() - 1;
		while (idx > -1 && Character.isDigit(srs.charAt(idx))) {
			idx--;
		}

		if (idx != srs.length() - 1)
			return Integer.parseInt(srs.substring(idx + 1).toString());
		return 0;
	}

	public static CoordinateReferenceSystem createCRS(int srid)
			throws GeoException {
		// Using support from authority "European Petroleum Survey Group"
		CoordinateReferenceSystem crs = null;
		try {
			crs = CRS.decode(Config.DEFAULT_SRS_PREFIX + srid);
		} catch (NoSuchAuthorityCodeException e) {
			//e.printStackTrace();
		} catch (FactoryException e) {
			//e.printStackTrace();
		}
		if (crs != null)
			return crs;

		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(SpatialRefSys.class);
		criteria.add(Restrictions.eq(SpatialRefSys.SRID, new Integer(srid)));
		criteria.setMaxResults(1);

		SpatialRefSys spatialRefSys = (SpatialRefSys) criteria.uniqueResult();

		if (spatialRefSys != null) {
			try {
				crs = CRS.parseWKT(spatialRefSys.getSrtext());
			} catch (FactoryException e) {
				e.printStackTrace();
			}
		}

		if (crs == null)
			throw new GeoException(I18N.m.getString("cannot_transform_srs"));

		return crs;
	}

}
