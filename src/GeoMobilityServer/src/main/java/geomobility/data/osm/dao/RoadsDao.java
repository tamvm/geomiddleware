package geomobility.data.osm.dao;

import geomobility.data.osm.entity.Roads;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.Assert;

import tam.HibernateUtil;

import com.gisgraphy.domain.repository.GenericDao;
import com.gisgraphy.domain.repository.IDatabaseHelper;
import com.gisgraphy.helper.GeolocHelper;
import com.gisgraphy.helper.IntrospectionHelper;
import com.gisgraphy.hibernate.criterion.IntersectsRestrictionWithPoint;
import com.gisgraphy.hibernate.criterion.ResultTransformerUtil;
import com.gisgraphy.hibernate.projection.ProjectionBean;
import com.vividsolutions.jts.geom.Point;

/**
 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
 */
public class RoadsDao extends GenericDao<Roads, Long> {
	IDatabaseHelper databaseHelper;

	/**
	 * The logger
	 */
	protected static final Logger logger = LoggerFactory
			.getLogger(RoadsDao.class);

	/**
	 * Default constructor
	 */
	public RoadsDao() {
		super(Roads.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gisgraphy.domain.repository.IOpenStreetMapDao#getByGid(java.lang.
	 * Long)
	 */
	public Roads getByOSMID(final Long gid) {
		Assert.notNull(gid);
		return (Roads) this.getHT().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws PersistenceException {
				String queryString = "from " + Roads.class.getSimpleName() + " as c where c.osm_id= ?";

				Query qry = session.createQuery(queryString);
				qry.setCacheable(true);

				qry.setParameter(0, gid);
				Roads result = (Roads) qry.uniqueResult();
				return result;
			}
		});
	}

	public List<Roads> getRoadContain(final Point p, final int numResult, final boolean isTransform) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(Roads.class);
		List<Roads> listRoads = new ArrayList<Roads>();
		
//		 String queryString = "from " + Roads.class.getSimpleName() + " as c where c." + Roads.WAY + " & GeometryFromText( '%s' ," + SRID.WGS84_SRID.getSRID() + ")";
//		 System.out.println("asdasd" + p.toString());
//		 Query qry = session.createQuery(String.format(queryString,
//		 p.toString()));
//		 qry.setCacheable(true);
//		 qry.setParameter(0, p);
//		 Roads result = (Roads) qry.uniqueResult();
//		 return result;

		List<String> fieldList = IntrospectionHelper.getFieldsAsList(Roads.class);
		ProjectionList projections = ProjectionBean.fieldList(fieldList, false);

		criteria.setProjection(projections);
		criteria.add(new IntersectsRestrictionWithPoint(Roads.WAY, p));
		criteria.setMaxResults(1);
		criteria.setCacheable(true);

		String[] propertiesNameArray = IntrospectionHelper.getFieldsAsArray(Roads.class);
		if (numResult > 1) {
			Object[] result = (Object[]) criteria.uniqueResult();

			// Tranforms into Roads object
			Roads road = ResultTransformerUtil.transformToRoads(propertiesNameArray, result);
			listRoads.add(road);
		} else {
			criteria.setMaxResults(numResult);
			List<Object[]> list = criteria.list();
			if (list == null)
				return null;

			listRoads = ResultTransformerUtil.transformToListRoads(propertiesNameArray, list);
		}
		return listRoads;
	}

	public static void main(String[] args) {
		RoadsDao roadsDao = new RoadsDao();
		roadsDao.getRoadContain(GeolocHelper.createXYPoint(11813445.4100951,
				1165061.6396931), 2, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gisgraphy.domain.repository.IOpenStreetMapDao#countEstimate()
	 */
	public long countEstimate() {
		return (Long) this.getHT().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws PersistenceException {
				String queryString = "select max(osm_id) from "
						+ persistentClass.getSimpleName();

				Query qry = session.createQuery(queryString);
				qry.setCacheable(true);
				Long count = (Long) qry.uniqueResult();
				return count;
			}
		});
	}

}
