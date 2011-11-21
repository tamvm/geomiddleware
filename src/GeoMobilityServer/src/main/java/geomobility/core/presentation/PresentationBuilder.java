package geomobility.core.presentation;

import geomobility.core.utils.StaticFunc;
import junit.framework.Assert;
import net.opengis.xls.v_1_1_0.ContentType;
import net.opengis.xls.v_1_1_0.MapType;
import net.opengis.xls.v_1_1_0.OutputType;
import net.opengis.xls.v_1_1_0.PortrayMapRequestType;
import net.opengis.xls.v_1_1_0.PortrayMapResponseType;

/**
 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
 */
public class PresentationBuilder {

	public static PortrayMapResponseType buildResult(PortrayMapRequestType portrayMap, String[] urlImages) {
		PortrayMapResponseType responseType = new PortrayMapResponseType();
		Assert.assertNotNull(urlImages);
		Assert.assertEquals(portrayMap.getOutput().size(), urlImages.length);
		
		for (int i=0;i<urlImages.length;i++){
			if (!StaticFunc.isNOE(urlImages[i])){
				MapType mapType = new MapType();
				responseType.getMap().add(mapType);
				
				OutputType outputType = portrayMap.getOutput().get(i);
				mapType.setBBoxContext(outputType.getBBoxContext());
				mapType.setCenterContext(outputType.getCenterContext());
				
				ContentType contentType = new ContentType();
				mapType.setContent(contentType);
				
				contentType.setFormat(outputType.getFormat());
				contentType.setWidth(outputType.getWidth());
				contentType.setHeight(outputType.getHeight());
				contentType.setURL(urlImages[i]);
			}
		}

		return responseType;
	}
}
