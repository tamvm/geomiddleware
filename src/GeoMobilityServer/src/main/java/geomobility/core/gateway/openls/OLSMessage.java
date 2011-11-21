package geomobility.core.gateway.openls;

import geomobility.core.GlobalCore;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

/**
 * @author VoMinhTam
 */
public abstract class OLSMessage {
	public static JAXBContext jcOLS = null;
	static {
		try {
			jcOLS = JAXBContext
					.newInstance(GlobalCore.OLSContextPath);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
}
