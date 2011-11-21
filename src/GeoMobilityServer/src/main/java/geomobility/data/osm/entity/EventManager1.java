package geomobility.data.osm.entity;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernatespatial.criterion.SpatialRestrictions;

import tam.HibernateUtil;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 * @author VoMinhTam
 */
public class EventManager1 {
	public static void main(String[] args) {
		EventManager1 mgr = new EventManager1();

		System.out.println(mgr.getAllEvent1());
		HibernateUtil.getSessionFactory().close();
	}

	private List find(String wktFilter) {
		WKTReader fromText = new WKTReader();
		Geometry filter = null;
		try {
			filter = fromText.read(wktFilter);
		} catch (ParseException e) {
			throw new RuntimeException("Not a WKT String:" + wktFilter);
		}
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		System.out.println("Filter is : " + filter);
		Criteria testCriteria = session.createCriteria(Roads.class);
		testCriteria.add(SpatialRestrictions.within("location", filter));
		List results = testCriteria.list();
		session.getTransaction().commit();
		return results;
	}

	private List<Roads> getAllEvent1() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List result = session.createQuery("from geomobility.data.osm.entity.Roads").list();
		Criteria criteria = session.createCriteria(Roads.class);
		criteria.setMaxResults(2);
//		session.close();

		return criteria.list();
	}

}
