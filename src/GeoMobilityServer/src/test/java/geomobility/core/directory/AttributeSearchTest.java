package geomobility.core.directory;

import org.junit.Test;

import com.gisgraphy.domain.geoloc.service.ServiceException;

import junit.framework.TestCase;
import geomobility.core.exception.DirectoryException;
import geomobility.core.exception.GeoException;
import geomobility.core.utils.StaticFunc;

/**
 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
 */
public class AttributeSearchTest extends TestCase {
	public static void main(String[] args) {
//		testPOIName();
		testType();
	}

	@Test
	public static void testPOIName() {
		String xml = StaticFunc.getContent("data/directory/attribute/poiname.xml");
		try {
			String result = DController.doController(xml);
			System.out.println(result);
			//assertNotNull(result);
			//assertTrue(result.contains("Wirtshaus zum Rostigen Anker"));
		} catch (DirectoryException e) {
			e.printStackTrace();
		} catch (GeoException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public static void testType() {
		String xml = StaticFunc.getContent("data/directory/attribute/type.xml");
		try {
			String result = DController.doController(xml);
			System.out.println(result);
			//assertNotNull(result);
			//assertTrue(result.contains("Chinese"));
		} catch (DirectoryException e) {
			e.printStackTrace();
		} catch (GeoException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
