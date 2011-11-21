package geomobility.core.gateway.mlp.slis;

import geomobility.core.gateway.mlp.MLPEntity;
import geomobility.core.gateway.mlp.MLPInput;
import geomobility.core.gateway.mlp.entity.svc_result.Slia;

import java.io.File;
import java.io.StringReader;
import java.net.URL;

import javax.xml.bind.JAXBElement;

public class SLISAnswer extends MLPInput {
	/******** SLIA Message *******/
	public MLPEntity parse(String xml) {
		try {
			JAXBElement<Slia> element = (JAXBElement<Slia>) u
					.unmarshal(new StringReader(xml));
			Slia item = element.getValue();
			return item;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public MLPEntity parse(File file) {
		try {
			JAXBElement<Slia> element = (JAXBElement<Slia>) u.unmarshal(file);
			Slia item = element.getValue();
			return item;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public MLPEntity parse(URL url) {
		try {
			JAXBElement<Slia> element = (JAXBElement<Slia>) u.unmarshal(url);
			Slia item = element.getValue();
			return item;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
