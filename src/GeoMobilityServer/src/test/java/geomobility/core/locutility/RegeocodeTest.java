package geomobility.core.locutility;

import geomobility.core.exception.GeoException;
import geomobility.core.locutility.regeo.ReGeoController;
import geomobility.core.utils.StaticFunc;

/**
 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
 */
public class RegeocodeTest {
	public static void main(String[] args) throws GeoException {
//		testFullAdr();
		testIntersectionAdr();
	}
	
	public static void testFullAdr() throws GeoException{
		String xml = StaticFunc.getContent("data/loc_util/regeocode/full_adr.xml");
		System.out.println(ReGeoController.doController(xml));
	}
	
	public static void testIntersectionAdr() throws GeoException{
		String xml = StaticFunc.getContent("data/loc_util/regeocode/intersection_adr.xml");
		System.out.println(ReGeoController.doController(xml));
	}
}
