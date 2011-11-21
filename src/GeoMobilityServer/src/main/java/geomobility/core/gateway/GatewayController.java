package geomobility.core.gateway;

import geomobility.core.GlobalCore;
import geomobility.core.exception.GeoException;
import geomobility.core.gateway.adapter.MLP2OLSAdapter;
import geomobility.core.gateway.adapter.OLS2MLPAdapter;
import geomobility.core.gateway.mlp.entity.svc_result.Slia;
import geomobility.core.gateway.mlp.entity.svc_result.Slir;
import geomobility.core.gateway.mlp.service.MLPOutputServiceWrapper;
import geomobility.core.utils.StaticFunc;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.opengis.xls.v_1_1_0.ResponseType;
import net.opengis.xls.v_1_1_0.SLIAType;
import net.opengis.xls.v_1_1_0.SLIRType;
import net.opengis.xls.v_1_1_0.XLSType;

/**
 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
 */
public class GatewayController {
	@SuppressWarnings("restriction")
	public static String doController(String xml) throws GeoException {
		XLSType xlsType = StaticFunc.getXLSType(xml);

		List<SLIRType> slirTypes = StaticFunc.parse(xlsType);
		xlsType.getBody().clear();

		for (SLIRType slirType : slirTypes) {
			Slir slir = OLS2MLPAdapter.convertSLIRMessage(slirTypes.get(0));

			Slia slia = MLPOutputServiceWrapper.sendSLRS(slir);

			List<SLIAType> listSliaType = MLP2OLSAdapter
					.convertSLIAMessage(slia);

			for (SLIAType sliaType : listSliaType) {
				ResponseType responseType = new ResponseType();
				responseType.setResponseParameters(GlobalCore.objectFactoryXLS
						.createSLIA(sliaType));
				xlsType.getBody().add(
						GlobalCore.objectFactoryXLS.createResponse(responseType));
			}

		}

		OutputStream outputStream = StaticFunc.buildXLSResult(xlsType,
				GlobalCore.output);

		return outputStream.toString();
	}
	
	public static List<SLIAType> doController(SLIRType slirType) throws GeoException {
		Slir slir = OLS2MLPAdapter.convertSLIRMessage(slirType);

		Slia slia = MLPOutputServiceWrapper.sendSLRS(slir);

		List<SLIAType> listSliaType = MLP2OLSAdapter
				.convertSLIAMessage(slia);

		return listSliaType;
	}
}
