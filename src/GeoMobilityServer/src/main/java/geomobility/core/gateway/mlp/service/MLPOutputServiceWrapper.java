package geomobility.core.gateway.mlp.service;

import geomobility.core.gateway.mlp.entity.svc_result.Slia;
import geomobility.core.gateway.mlp.entity.svc_result.Slir;
import geomobility.core.gateway.mlp.slis.SLISAnswer;
import geomobility.core.gateway.mlp.slis.SLISRequest;
import geomobility.core.gateway.mlp.util.GlobalMLP;
import geomobility.core.net.HttpRequest;
import geomobility.core.utils.Log;
import geomobility.core.utils.StaticFunc;

/**
 * @author VoMinhTam
 */
public class MLPOutputServiceWrapper {
	private static final String TAG = MLPInputServiceWrapper.class
			.getSimpleName();

	public static Slia sendSLRS(Slir slir) {
		SLISRequest sRequest = new SLISRequest();
		String postData = sRequest.parse(slir);

		// TODO Uncomment later
		// Content is SLIA Message
		Log.d(TAG, "");
		String content = HttpRequest
				.postString(GlobalMLP.URL_LP_SLIR, postData);
		if (StaticFunc.isNOE(content))
			return null;

		// TODO Just for testing
		// String content = StaticFunc.getContent("data/slia.xml");

		SLISAnswer sAnswer = new SLISAnswer();
		return (Slia) sAnswer.parse(content);
	}

	public static void main(String[] args) {
		String tmp = HttpRequest.postString(GlobalMLP.URL_LP_SLIR, "asdasd");

		System.out.println(tmp);
	}
}
