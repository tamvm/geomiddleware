package geomobility.core.gateway.mlp.service;

import geomobility.core.exception.GeoException;
import geomobility.core.gateway.GatewayController;
import geomobility.core.utils.StaticFunc;

import org.junit.Test;

/**
 * @author VoMinhTam
 */
public class SLIRTest {
	private static final String TAG = SLIRTest.class.getSimpleName();

	@Test
	public void testSLIS() throws GeoException {
		// OLSInput olsInput = new OLSInput();
		// SLIRType slirType = StaticFunc.parse(StaticFunc
		// .getContent("data/gateway/slir-openls.xml"));
		//
		// Slir slir = OLS2MLPAdapter.convertSLIRMessage(slirType);
		//
		// Slia slia = MLPOutputServiceWrapper.sendSLRS(slir);
		//
		// List<SLIAType> sliaType = MLP2OLSAdapter.convertSLIAMessage(slia);
		//
		// System.out.println(sliaType.get(0).toOLSString());
		// Assert.assertNotNull(sliaType);

		String s = GatewayController.doController(StaticFunc
				.getContent("data/gateway/slir-openls.xml"));
		System.out.println(s);
	}

	public static void main(String[] args) throws GeoException {
		SLIRTest slirTest = new SLIRTest();
		slirTest.testSLIS();
	}
}
