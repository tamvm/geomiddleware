package geomobility.core.presentation;

import org.geotools.styling.UserLayerImpl;
import org.opengis.referencing.ObjectFactory;

/**
 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
 */
public class TestWMS {
	public static void main(String[] args) {
		UserLayerImpl userLayerImpl = new UserLayerImpl();
		userLayerImpl.setName("sdfsdf");
		
		System.out.println(userLayerImpl);
		
	}
}
