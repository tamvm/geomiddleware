package geomobility.core.gateway.mlp.slis;

import geomobility.core.gateway.mlp.MLPEntity;
import geomobility.core.gateway.mlp.MLPInput;
import geomobility.core.gateway.mlp.entity.svc_result.Slirep;

import java.io.File;
import java.io.StringReader;
import java.net.URL;

import javax.xml.bind.JAXBElement;

public class SLISReport extends MLPInput {

	@Override
	public MLPEntity parse(String xml) {
		try {
			JAXBElement<Slirep> element = (JAXBElement<Slirep>) u
					.unmarshal(new StringReader(xml));
			Slirep item = element.getValue();
			return item;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public MLPEntity parse(File file) {
		try {
			JAXBElement<Slirep> element = (JAXBElement<Slirep>) u
					.unmarshal(file);
			Slirep item = element.getValue();
			return item;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public MLPEntity parse(URL url) {
		try {
			JAXBElement<Slirep> element = (JAXBElement<Slirep>) u
					.unmarshal(url);
			Slirep item = element.getValue();
			return item;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
