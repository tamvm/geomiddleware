package geomobility.core.gateway.mlp.service;

import geomobility.core.exception.GeoException;
import geomobility.core.gateway.adapter.MLP2OLSAdapter;
import geomobility.core.gateway.mlp.entity.svc_result.Slirep;
import geomobility.core.gateway.mlp.slis.SLISReport;
import geomobility.core.utils.StaticFunc;

import java.util.List;

import net.opengis.xls.v_1_1_0.SLIAType;

import org.junit.Test;


/**
 * @author VoMinhTam
 */
public class SLIRepTest {
	@Test
	public void testSLIS() throws GeoException{
		SLISReport slisReport = new SLISReport();
		
		Slirep slirep = (Slirep) slisReport.parse(StaticFunc.getContent("data/gateway/slirep.xml"));
		
		List<SLIAType> sliaType = MLP2OLSAdapter.convertSLIRepMessage(slirep);

		System.out.println(StaticFunc.toOLSString(sliaType.get(0)));
	}
	
	public static void main(String[] args) throws GeoException {
		SLIRepTest sliRepTest = new SLIRepTest();
		sliRepTest.testSLIS();
	}
}
