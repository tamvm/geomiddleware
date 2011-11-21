package geomobility.core.gateway.mlp;

import geomobility.core.gateway.mlp.entity.svc_result.ObjectFactory;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public abstract class MLPOutput extends MLPMessage {
	protected static ObjectFactory objectFactory = new ObjectFactory();
	protected static Marshaller m = null;
	{
		try {
			m = jcMLP.createMarshaller();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public abstract String parse(MLPEntity entity);
}
