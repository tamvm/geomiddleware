package geomobility.core.gateway.mlp;


import geomobility.core.gateway.mlp.entity.svc_result.Slia;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class MainReceiveSLIA {
	public static void main(String[] args) {
		///Example 1: Receive SILA from Location Provider, convert into MLP Entity
		try {
			JAXBContext jc = JAXBContext.newInstance ("geomobility.core.gateway.mlp.entity.svc_result");
            Unmarshaller u = jc.createUnmarshaller ();
            File f = new File ("data/slia.xml");
			JAXBElement element = (JAXBElement) u.unmarshal(f);

			Slia item = (Slia) element.getValue();
			System.out.println(item.getPos().get(0).getMsid().getContent());
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

}
