package geomobility.servlet;

import geomobility.servlet.service.exception.MissingParameterException;
import geomobility.servlet.service.exception.RestfulException;

import java.util.Map;

public abstract class BaseRestfulService implements RestfulService {
	protected String[] mRequiredParameters;
	protected String mServiceName;

	public BaseRestfulService(String serviceName) {
		super();
		mServiceName = serviceName;
	}

	public BaseRestfulService(String[] requiredMethods) {
		this.mRequiredParameters = requiredMethods;
	}

	protected MissingParameterException checkParams(Map params) {
		if (mRequiredParameters != null) {
			for (String param : mRequiredParameters) {
				if (params.containsKey(param))
					continue;
				return missingParameter(param);
			}
		}
		return null;
	}

	protected MissingParameterException missingParameter(String paramName) {
		return new MissingParameterException(mServiceName, paramName);
	}

	protected String getParameter(String key, Map<String, String[]> params) {
		try {
			if (params != null && params.containsKey(key)) {
				String[] arrParam = params.get(key);
				return arrParam == null ? null
						: ((arrParam.length > 0) ? arrParam[0] : null);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	protected String getParameterWithThrow(String parameterName,
			Map<String, String[]> params) throws MissingParameterException {
		String result = getParameter(parameterName, params);
		if (result == null) {
			throw missingParameter(parameterName);
		}
		return result;
	}

	public abstract String process(Map<String, String[]> params, String content)
			throws Exception;
}
