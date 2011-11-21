package geomobility.core.locutility;

import geomobility.core.utils.Config;
import geomobility.core.utils.StaticFunc;
import geomobility.data.osm.dao.RoadsDao;
import geomobility.data.osm.entity.Points;
import geomobility.data.osm.entity.Roads;
import geomobility.data.osm.entity.result.PointsResult;
import geomobility.data.osm.entity.result.RoadsResult;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.NotImplementedException;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tam.HibernateUtil;

import com.gisgraphy.domain.geoloc.service.fulltextsearch.StreetSearchMode;
import com.gisgraphy.domain.repository.IDatabaseHelper;
import com.gisgraphy.helper.GeolocHelper;
import com.gisgraphy.helper.IntrospectionHelper;
import com.gisgraphy.hibernate.criterion.FulltextRestriction;
import com.gisgraphy.hibernate.criterion.IntersectsRestriction;
import com.gisgraphy.hibernate.criterion.MySimpleProjection;
import com.gisgraphy.hibernate.criterion.ProjectionOrder;
import com.gisgraphy.hibernate.criterion.ResultTransformerUtil;
import com.gisgraphy.hibernate.projection.ProjectionBean;
import com.gisgraphy.hibernate.projection.SpatialProjection;
import com.vividsolutions.jts.geom.Polygon;

/**
 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
 */
public class LocUtilEngine {
	private static LocUtilEngine instance = new LocUtilEngine();
	private RoadsDao roadsDao = new RoadsDao();

	public static LocUtilEngine getInstance() {
		return instance;
	}

	IDatabaseHelper databaseHelper;

	/**
	 * The logger
	 */
	protected static final Logger logger = LoggerFactory
			.getLogger(LocUtilEngine.class);

	/**
	 * Default constructor
	 */
	private LocUtilEngine() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gisgraphy.domain.repository.IOpenStreetMapDao#getNearestAndDistanceFrom
	 * (com.vividsolutions.jts.geom.Point, double, int, int, java.lang.String,
	 * java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<RoadsResult> searchRoads(final String name, final int firstResult, final int maxResults, 
			final StreetSearchMode streetSearchMode, final String countryCode) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(Roads.class);

		List<String> fieldList = IntrospectionHelper.getFieldsAsList(Roads.class);
		ProjectionList projections = ProjectionBean.fieldList(fieldList, false);

		MySimpleProjection interpolateProjection = SpatialProjection.generateInterpolateProjection(Points.WAY, 0.5);
		projections.add(interpolateProjection);

		criteria.setProjection(projections);
		if (maxResults > 0) {
			criteria = criteria.setMaxResults(maxResults);
		} 
		criteria = criteria.setFirstResult(firstResult);

		if (!StaticFunc.isNOE(name)) {
			if (streetSearchMode == StreetSearchMode.CONTAINS) {
				//criteria = criteria.add(Restrictions.isNotNull("name"));// optimisation!
				criteria = criteria.add(Restrictions.or(
					Restrictions.sqlRestriction("no_sign("+Roads.NAME+")" + " ilike " + "'%"+StaticFunc.removeSign(name)+"%'"), 
					Restrictions.sqlRestriction("no_sign("+Roads.INTERPOLATION+")" + " ilike " + "'%"+StaticFunc.removeSign(name)+"%'")));
			} else if (streetSearchMode == StreetSearchMode.FULLTEXT) {
				criteria = criteria.add(new FulltextRestriction(Roads.NAME, name));
			}
		}

		if (!StaticFunc.isNOE(countryCode)) {
			criteria = criteria.add(Restrictions.eq("countrycode",countryCode));
		}

		criteria.setCacheable(true);
		List<?> queryResults = criteria.list();

		if (queryResults != null && queryResults.size() != 0) {
			String[] propertiesNameArray = IntrospectionHelper.getFieldsAsArray(Roads.class);

			List<RoadsResult> listRoads = ResultTransformerUtil.transformToListRoadsResult(
					propertiesNameArray,queryResults, interpolateProjection.getPosition());

			return listRoads;
		} else {
			return new ArrayList<RoadsResult>();
		}
	}
	
	public String getWay(String name, String countryCode) throws HibernateException, SQLException{
		Session session = HibernateUtil.getSessionFactory().openSession();
		Statement stm=session.connection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		String sql="select astext(way) from planet_osm_roads where (no_sign(name) ilike '%"+StaticFunc.removeSign(name)+"%'" +
				"or no_sign(interpolation) ilike '%"+StaticFunc.removeSign(name)+"%') ";
		if (!StaticFunc.isNOE(countryCode))
			sql += " and countrycode='"+countryCode+"'";
		ResultSet resultSet = stm.executeQuery(sql);
		resultSet.last();
		if (resultSet.getRow()>0){
			resultSet.first();
			return resultSet.getString(1);
		}
		return null;
	}
	
	public String intersectionAdr(String lineString1, String lineString2) throws HibernateException, SQLException{
		Session session = HibernateUtil.getSessionFactory().openSession();
		Statement stm=session.connection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		String sql="select astext(st_intersection(GeomFromText('"+lineString1+"',4326), GeomFromText('"+lineString2+"', 4326)))";
		ResultSet resultSet = stm.executeQuery(sql);
		resultSet.last();
		if (resultSet.getRow()>0){
			resultSet.first();
			String result =resultSet.getString(1);
			if (StaticFunc.isNOE(result) || result.equals("GEOMETRYCOLLECTION EMPTY"))
				return null;
			return result;
		}
		return null;
	}

	public List<PointsResult> getNearestAndDistanceFrom(final float lng, final float lat, final double distance, 
			final int firstResult, final int maxResults, final String name, final StreetSearchMode streetSearchMode,
			final boolean includeDistanceField, final String countryCode, boolean isFindRoads) {
		if (name != null && streetSearchMode == null) {
			throw new IllegalArgumentException("streetSearchmode can not be null if name is provided");
		}
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(Points.class);
		List<PointsResult> results = null;
		
		List<String> fieldList = IntrospectionHelper.getFieldsAsList(Points.class);
		ProjectionList projections = ProjectionBean.fieldList(fieldList, false);

		if (includeDistanceField) {
			projections.add(SpatialProjection.distance(GeolocHelper.createPoint(lng, lat),Roads.WAY, true).as(PointsResult.DISTANCE));
		}
		projections.add(SpatialProjection.generateTransformProjection(Points.WAY).as(PointsResult.WAY84));

		criteria.setProjection(projections);
		if (includeDistanceField) {
			criteria.addOrder(new ProjectionOrder(PointsResult.DISTANCE));
		}
		if (maxResults > 0) {
			criteria = criteria.setMaxResults(maxResults);
		}
		criteria = criteria.setFirstResult(firstResult);

		Polygon polygonBox = GeolocHelper.createPolygonBox(lng, lat, distance);
		criteria = criteria.add(new IntersectsRestriction(Roads.WAY, polygonBox, true));

		if (!StaticFunc.isNOE(name)) {
			if (streetSearchMode == StreetSearchMode.CONTAINS) {
				criteria = criteria.add(Restrictions.isNotNull("name"));// optimisation!
				criteria = criteria.add(Restrictions.or(
					Restrictions.sqlRestriction("no_sign("+Roads.NAME+")" + " ilike " + "'%"+StaticFunc.removeSign(name)+"%'"), 
					Restrictions.sqlRestriction("no_sign("+Roads.INTERPOLATION+")" + " ilike " + "'%"+StaticFunc.removeSign(name)+"%'")));
			} else if (streetSearchMode == StreetSearchMode.FULLTEXT) {
				criteria = criteria.add(new FulltextRestriction(Roads.NAME, name));
			} else {
				throw new NotImplementedException(streetSearchMode + " is not implemented for street search");
			}
		}

		if (!StaticFunc.isNOE(countryCode)) {
			criteria = criteria.add(Restrictions.eq("countrycode", countryCode));
		}

		criteria.setCacheable(true);
		// System.out.println(HibernateUtil.getSQL(criteria));
		List<Object[]> queryResults = criteria.list();

		// Calculate distance
		if (queryResults != null && queryResults.size() != 0) {
			String[] propertiesNameArray;
			if (includeDistanceField) {
				propertiesNameArray = (String[]) ArrayUtils.add(IntrospectionHelper.getFieldsAsArray(Points.class),PointsResult.DISTANCE);
			} else {
				propertiesNameArray = IntrospectionHelper.getFieldsAsArray(Points.class);
			}
			propertiesNameArray = (String[]) ArrayUtils.add(propertiesNameArray, PointsResult.WAY84);
			results = ResultTransformerUtil.transformToPoints(propertiesNameArray, queryResults);
			// return queryResults;
		} else {
			results = new ArrayList<PointsResult>();
		}

		if (isFindRoads){
			// Find the roads
			for (int i = 0; i < results.size(); i++) {
				PointsResult p = (PointsResult) results.get(i);
				p.setRoads(roadsDao.getRoadContain(GeolocHelper.createXYPoint(p.getWay().getX(), p.getWay().getY()),
						Config.REGEO_MAX_ROADS_IN_POINTS, false));
			}
		}

		return results;
	}

	public static void main(String[] args) {
		LocUtilEngine roadDao = new LocUtilEngine();
		List<RoadsResult> list = roadDao.searchRoads("Thạch Sơn", 0, 3,StreetSearchMode.CONTAINS, "VN");
		System.out.println(list.get(0).getInterpolateStr());
		System.out.println(list.size());

//		List<PointsResult> list = roadDao.getNearestAndDistanceFrom(105.7292366F, 10.0899023F, 10000, 0, 5, "", StreetSearchMode.CONTAINS, true, "VN");
//		for (PointsResult o : list) {
//			System.out.println(o);
//		}
//		System.out.println("Size: " + list.size());
	}
}
