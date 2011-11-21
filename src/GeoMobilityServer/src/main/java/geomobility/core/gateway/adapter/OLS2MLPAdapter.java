package geomobility.core.gateway.adapter;

import geomobility.core.gateway.mlp.entity.svc_result.Eqop;
import geomobility.core.gateway.mlp.entity.svc_result.HorAcc;
import geomobility.core.gateway.mlp.entity.svc_result.LlAcc;
import geomobility.core.gateway.mlp.entity.svc_result.LocType;
import geomobility.core.gateway.mlp.entity.svc_result.Msid;
import geomobility.core.gateway.mlp.entity.svc_result.Msids;
import geomobility.core.gateway.mlp.entity.svc_result.Prio;
import geomobility.core.gateway.mlp.entity.svc_result.RespReq;
import geomobility.core.gateway.mlp.entity.svc_result.RespTimer;
import geomobility.core.gateway.mlp.entity.svc_result.Sessionid;
import geomobility.core.gateway.mlp.entity.svc_result.Slir;
import net.opengis.xls.v_1_1_0.HorAccType;
import net.opengis.xls.v_1_1_0.InputGatewayParametersType;
import net.opengis.xls.v_1_1_0.InputMSIDsType;
import net.opengis.xls.v_1_1_0.InputMSInformationType;
import net.opengis.xls.v_1_1_0.QualityOfPositionType;
import net.opengis.xls.v_1_1_0.SLIRType;
import net.opengis.xls.v_1_1_0.VerAccType;

/**
 * @author VoMinhTam
 */
public class OLS2MLPAdapter {
	public static Slir convertSLIRMessage(SLIRType slirType) {
		// Each SLIR corresponds to 1 MSID
		InputGatewayParametersType inputGatewayParameters = slirType
				.getInputGatewayParameters();
		InputMSIDsType inputMSIDs = inputGatewayParameters.getInputMSIDs();

		String locationType = inputGatewayParameters.getLocationType();
		String priority = inputGatewayParameters.getPriority();
		QualityOfPositionType requestedQoP = inputGatewayParameters
				.getRequestedQoP();

		if (requestedQoP != null) {
			Eqop eqop = new Eqop();
			String responnseReq = requestedQoP.getResponseReq();
			eqop.setRespReq(new RespReq(responnseReq));

			String responseTimer = requestedQoP.getResponseTimer();
			eqop.setRespTimer(new RespTimer(responseTimer));

			HorAccType horAccType = requestedQoP.getHorizontalAcc();
			String horAcc = horAccType.getDistance().getValue() + "";
			eqop.setHorAcc(new HorAcc(horAcc));

			VerAccType verAccType = requestedQoP.getVerticalAcc();
			String verAcc = verAccType.getDistance().getValue() + "";
			eqop.setLlAcc(new LlAcc(verAcc));
		}

		String requestedsrsName = inputGatewayParameters.getRequestedsrsName();

		InputMSInformationType inputMSInfomation = inputMSIDs
				.getInputMSInformation();
		String session = inputMSIDs.getSession();

		String encryption = inputMSInfomation.getEncryption();
		String msIDType = inputMSInfomation.getMsIDType();
		String msIDValue = inputMSInfomation.getMsIDValue();

		// Represent for SLIR Message
		Slir slir = new Slir();
		Msids msids = new Msids();

		Msid msid = new Msid();
		msid.setType(msIDType);
		msid.setContent(msIDValue);
		msid.setEnc(encryption);

		Sessionid sessionID = new Sessionid();
		msids.getMsidAndCodewordAndSession().add(sessionID);
		msids.getMsidAndCodewordAndSession().add(msid);
		slir.setMsids(msids);

		// Donot have information about Coordinate in OpenLS
		// CoordinateReferenceSystem coordinateReferenceSystem = new
		// GeoInfo geoInfo = new GeoInfo();

		slir.setLocType(new LocType(locationType));
		slir.setPrio(new Prio(priority));

		// Other properties allow dev to config with LP server
		// eqop.setMaxLocAge(new MaxLocAge("1000"));F
		return slir;
	}
}
