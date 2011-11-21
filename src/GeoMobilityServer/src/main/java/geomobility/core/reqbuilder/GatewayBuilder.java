package geomobility.core.reqbuilder;

import java.util.Map;

import net.opengis.xls.v_1_1_0.InputGatewayParametersType;
import net.opengis.xls.v_1_1_0.InputMSIDsType;
import net.opengis.xls.v_1_1_0.InputMSInformationType;
import net.opengis.xls.v_1_1_0.SLIRType;

public class GatewayBuilder extends RequestBuilder{
	// Singleton
	private static GatewayBuilder i = null;
	public static GatewayBuilder instance(){
		if (i==null)
			i = new GatewayBuilder();
		return i;
	}
	
	public SLIRType build(Map<String, String[]> params){
		SLIRType slirType = new SLIRType();
		InputGatewayParametersType inputGatewayParametersType = slirType.getInputGatewayParameters();
		
		InputMSIDsType inputMSIDs = inputGatewayParametersType.getInputMSIDs();
		InputMSInformationType inputMSInformation = inputMSIDs.getInputMSInformation();
		inputMSInformation.setMsIDType(getParameter(ParamsKey.GW_MSIDTYPE, params, null));
		inputMSInformation.setMsIDValue(getParameter(ParamsKey.GW_MSIDVALUE, params, null));

		return slirType;
	}
}
