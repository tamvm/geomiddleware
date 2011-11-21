package geomobility.core.gateway.openls;

import geomobility.core.gateway.mlp.entity.svc_result.ObjectFactory;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 * @author VoMinhTam
 */
public abstract class OLSOutput extends OLSMessage {
	protected static ObjectFactory objectFactory = new ObjectFactory();
	protected static Marshaller m = null;
	{
		try {
			m = jcOLS.createMarshaller();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

}
