package geomobility.core.gateway.mlp;

import geomobility.core.GlobalCore;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;


public abstract class MLPMessage {
	public static JAXBContext jcMLP = null;
	{
		try {
			jcMLP = JAXBContext
					.newInstance(GlobalCore.MLPContextPath);//"geomobility.core.gateway.mlp.entity.svc_result"
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
}
