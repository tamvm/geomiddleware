package geomobility.core.gateway.mlp.service;

import geomobility.core.gateway.mlp.entity.svc_result.Slia;
import geomobility.core.gateway.mlp.entity.svc_result.Slir;
import geomobility.core.gateway.mlp.slis.SLISAnswer;
import geomobility.core.gateway.mlp.slis.SLISRequest;
import geomobility.core.gateway.mlp.util.GlobalMLP;
import geomobility.core.net.HttpData;
import geomobility.core.net.HttpRequest;
import geomobility.core.utils.StaticFunc;

/**
 * @author VoMinhTam
 */
public class MLPInputServiceWrapper {
	public static Slia sendSLRS(Slir slir) {
		SLISRequest sRequest = new SLISRequest();
		String postData = sRequest.parse(slir);

		HttpData httpData = HttpRequest.post(GlobalMLP.URL_LP_SLIR, postData);
		String content = httpData.content; // Content is SLIA Message
		if (StaticFunc.isNOE(content))
			return null;

		// TODO Just for testing
		// String content = StaticFunc.getContent("data/slia.xml");

		SLISAnswer sAnswer = new SLISAnswer();
		return (Slia) sAnswer.parse(content);
	}
}
