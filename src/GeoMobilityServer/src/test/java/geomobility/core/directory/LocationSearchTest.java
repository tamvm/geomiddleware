package geomobility.core.directory;

import junit.framework.TestCase;
import geomobility.core.exception.DirectoryException;
import geomobility.core.exception.GeoException;
import geomobility.core.utils.StaticFunc;

import org.junit.Test;

import com.gisgraphy.domain.geoloc.service.ServiceException;

/**
 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
 */
public class LocationSearchTest extends TestCase {
	public static void main(String[] args) {
		LocationSearchTest locationSearchTest = new LocationSearchTest();
//		locationSearchTest.testwithin_boudary_circle_by_center_point();
//		locationSearchTest.testwithin_boudary_polygon();
//		locationSearchTest.testwithin_boudary_envelop();
//		locationSearchTest.testwithin_distance();
		locationSearchTest.testNearest();
	}

	public void testwithin_distance() {
		System.out.println("testwithin_distance");
		String xml = StaticFunc.getContent("data/directory/location/within_distance.xml");
		try {
			String result = DController.doController(xml);
			System.out.println(result);
		} catch (DirectoryException e) {
			e.printStackTrace();
		} catch (GeoException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testNearest() {
		String xml = StaticFunc.getContent("data/directory/location/nearest_point.xml");
		try {
			String result = DController.doController(xml);
			System.out.println(result);
		} catch (DirectoryException e) {
			e.printStackTrace();
		} catch (GeoException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testwithin_boudary_circle_by_center_point() {
		String xml = StaticFunc.getContent("data/directory/location/within_boudary_circle_by_center_point.xml");
		try {
			String result = DController.doController(xml);
			System.out.println(result);
		} catch (DirectoryException e) {
			e.printStackTrace();
		} catch (GeoException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testwithin_boudary_polygon() {
		System.out.println("testwithin_boudary_polygon");
		String xml = StaticFunc.getContent("data/directory/location/within_boudary_polygon.xml");
		try {
			String result = DController.doController(xml);
			System.out.println(result);
		} catch (DirectoryException e) {
			e.printStackTrace();
		} catch (GeoException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testwithin_boudary_envelop() {
		System.out.println("within_boudary_envelop");
		String xml = StaticFunc.getContent("data/directory/location/within_boudary_envelop.xml");
		try {
			String result = DController.doController(xml);
			System.out.println(result);
		} catch (DirectoryException e) {
			e.printStackTrace();
		} catch (GeoException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

}
