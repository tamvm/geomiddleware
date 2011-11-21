package geomobility.core.gateway.mlp.slis;

import geomobility.core.GlobalCore;
import geomobility.core.gateway.mlp.MLPEntity;
import geomobility.core.gateway.mlp.MLPOutput;
import geomobility.core.gateway.mlp.entity.svc_result.Slir;

import java.io.IOException;
import java.io.OutputStream;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;


public class SLISRequest extends MLPOutput{

	@Override
	public String parse(MLPEntity entity) {
		if (!(entity instanceof Slir))
			return null;
		
		Slir slir = (Slir) entity;
		try {
			JAXBElement<Slir> jaxbElement = objectFactory.createSlir(slir);
			javax.xml.bind.Marshaller marshaller = jcMLP.createMarshaller();
			
			marshaller.marshal(jaxbElement, GlobalCore.output);
			return GlobalCore.output.toString();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
