package geomobility.core.locutility;

import java.sql.SQLException;

import org.hibernate.HibernateException;

import geomobility.core.exception.GeoException;
import geomobility.core.locutility.geo.GeoController;
import geomobility.core.utils.StaticFunc;

/**
 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
 */
public class GeocodeTest {
	public static void main(String[] args) throws Exception {
//		testNearestFullAdr();
		testIntersectionAdr();
	}
	
	public static void testNearestFullAdr() throws GeoException, HibernateException, SQLException{
		String xml = StaticFunc.getContent("data/loc_util/geocode/geocode_nearest_full_adr.xml");
		System.out.println(GeoController.doController(xml));
	}
	
	public static void testIntersectionAdr() throws GeoException, HibernateException, SQLException{
		String xml = StaticFunc.getContent("data/loc_util/geocode/geocode_nearest_intersection_adr.xml");
		System.out.println(GeoController.doController(xml));
	}
}
