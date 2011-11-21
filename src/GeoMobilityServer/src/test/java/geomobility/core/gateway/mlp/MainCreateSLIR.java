package geomobility.core.gateway.mlp;


import geomobility.core.gateway.mlp.entity.svc_result.Msid;
import geomobility.core.gateway.mlp.entity.svc_result.MsidRange;
import geomobility.core.gateway.mlp.entity.svc_result.Msids;
import geomobility.core.gateway.mlp.entity.svc_result.ObjectFactory;
import geomobility.core.gateway.mlp.entity.svc_result.Slir;
import geomobility.core.gateway.mlp.entity.svc_result.StartMsid;
import geomobility.core.gateway.mlp.entity.svc_result.StopMsid;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

public class MainCreateSLIR {
	public static void main(String[] args) {
		///Example 2: Generate SLIR request to send to Location Provider
		ObjectFactory objectFactory = new ObjectFactory();
		Slir slir = new Slir();

		Msids msids = new Msids();

		Msid msid = new Msid();
		msid.setType("IPV4");
		msid.setContent("93.10.0.250");

		MsidRange msidRange = new MsidRange();

		StartMsid startMsid = new StartMsid();
		msid = new Msid();
		msid.setContent("461018765710");
		startMsid.setMsid(msid);

		StopMsid stopMsid = new StopMsid();
		msid = new Msid();
		msid.setContent("461018765712");
		stopMsid.setMsid(msid);

		msidRange.setStartMsid(startMsid);
		msidRange.setStopMsid(stopMsid);

		msids.getMsidAndCodewordAndSession().add(msid);
		msids.getMsidAndCodewordAndSession().add(msidRange);

		slir.setMsids(msids);

		try {
			JAXBElement<Slir> jaxbElement = objectFactory.createSlir(slir);
			JAXBContext jc = JAXBContext
					.newInstance("geomobility.core.gateway.mlp.entity.svc_result");
			javax.xml.bind.Marshaller marshaller = jc.createMarshaller();
			marshaller.marshal(jaxbElement, System.out);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
}
