package geomobility.core.gateway.mlp;

import java.io.File;
import java.net.URL;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * This class parsers all MLP message from Location Provider to GeoMobility
 * 
 * @author VoMinhTam
 */
public abstract class MLPInput extends MLPMessage {
	public static Unmarshaller u = null;
	{
		try {
			u = jcMLP.createUnmarshaller();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public abstract MLPEntity parse(String xml);

	public abstract MLPEntity parse(File file);

	public abstract MLPEntity parse(URL url);
}
