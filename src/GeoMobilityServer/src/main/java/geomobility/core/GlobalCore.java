package geomobility.core;

import java.io.IOException;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import net.opengis.gml.v_3_1_1.ObjectFactory;

import com.gisgraphy.service.IStatsUsageService;
import com.gisgraphy.service.impl.StatsUsageServiceImpl;

/**
 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
 */
public class GlobalCore {
	//:net.opengis.gml.v_3_1_1:net.opengis.sld.v_1_1_0:net.opengis.wms.v_1_3_0:net.opengis.se.v_1_1_0:net.opengis.filter.v_1_1_0:net.opengis.sld.v_1_0_0
	public static final String OLSContextPath = "net.opengis.xls.v_1_1_0";
	public static final String WMSContextPath = "net.opengis.gml.v_3_1_1:net.opengis.sld.v_1_1_0:net.opengis.wms.v_1_3_0";
//	public static final String OLSContextPath = "net.opengis.xls.v_1_1_0:net.opengis.gml.v_3_1_1:net.opengis.sld.v_1_1_0:net.opengis.wms.v_1_3_0:net.opengis.se.v_1_1_0:net.opengis.filter.v_1_1_0:net.opengis.ows.v_1_1_0";
	public static final String MLPContextPath = "geomobility.core.gateway.mlp.entity.svc_result";

	public static JAXBContext jaxbContext = null;
	public static Marshaller marshaller = null;
	public static Unmarshaller unmarshaller = null;
	
	public static JAXBContext jaxbContextWMS = null;
	public static Marshaller marshallerWMS = null;
	public static Unmarshaller unmarshallerWMS = null;
	
	public static final net.opengis.xls.v_1_1_0.ObjectFactory objectFactoryXLS = new net.opengis.xls.v_1_1_0.ObjectFactory();
	public static final net.opengis.gml.v_3_1_1.ObjectFactory objectFactoryGML = new net.opengis.gml.v_3_1_1.ObjectFactory();
	public static final net.opengis.wms.v_1_3_0.ObjectFactory objectFactoryWMS = new net.opengis.wms.v_1_3_0.ObjectFactory();
	public static final net.opengis.sld.v_1_1_0.ObjectFactory objectFactorySLD = new net.opengis.sld.v_1_1_0.ObjectFactory();
	//public static final net.opengis.sld.v_1_0_0.ObjectFactory objectFactorySLD100 = new net.opengis.sld.v_1_0_0.ObjectFactory();
	public static final net.opengis.filter.v_1_1_0.ObjectFactory objectFactoryFilter = new net.opengis.filter.v_1_1_0.ObjectFactory();
	public static final net.opengis.se.v_1_1_0.ObjectFactory objectFactorySE = new net.opengis.se.v_1_1_0.ObjectFactory();
	static {
		try {
			jaxbContext = JAXBContext.newInstance(OLSContextPath);
			marshaller = jaxbContext.createMarshaller();
			unmarshaller = jaxbContext.createUnmarshaller();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	static {
		try {
			jaxbContextWMS = JAXBContext.newInstance(WMSContextPath);
			marshallerWMS = jaxbContextWMS.createMarshaller();
			unmarshallerWMS = jaxbContextWMS.createUnmarshaller();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public static class MyOutStream extends OutputStream {

		private StringBuilder string = new StringBuilder();

		@Override
		public void write(int b) throws IOException {
			this.string.append((char) b);
		}

		public String toString() {
			return this.string.toString();
		}

		public void clear() {
			string.setLength(0);
		}

	}

	public static MyOutStream output = new MyOutStream();

	public static final IStatsUsageService statsUsageService = new StatsUsageServiceImpl();
}
