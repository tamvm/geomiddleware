package geomobility.core.reqbuilder;

import geomobility.core.GlobalCore;
import geomobility.servlet.service.exception.MissingParameterException;

import java.math.BigDecimal;
import java.util.Map;

import javax.xml.bind.JAXBElement;

import net.opengis.xls.v_1_1_0.RequestHeaderType;
import net.opengis.xls.v_1_1_0.XLSType;

/**
 * 
 * @author VoMinhTam
 *
 */
public class RequestBuilder {
	protected String getParameter(String key, Map<String, String[]> params, String defaultValue) {
		try {
			if (params != null && params.containsKey(key)) {
				String[] arrParam = params.get(key);
				return arrParam == null ? null
						: ((arrParam.length > 0) ? arrParam[0] : null);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return defaultValue;
	}

	protected String getParameterWithThrow(String parameterName,
			Map<String, String[]> params) throws MissingParameterException {
		String result = getParameter(parameterName, params, null);
		if (result == null) {
			throw new MissingParameterException(parameterName);
		}
		return result;
	}
	
	public XLSType buildRequestHeader(Map<String, String[]> params){
		XLSType xlsType = GlobalCore.objectFactoryXLS.createXLSType();
		
		String version = getParameter(ParamsKey.VERSION_KEY, params, "1.1");
		try {
			xlsType.setVersion(new BigDecimal(version));
		} catch (Exception e) {
		}
		
		RequestHeaderType requestHeaderType = new RequestHeaderType();
		requestHeaderType.setClientName(ParamsKey.USERNAME_KEY);
		requestHeaderType.setClientPassword(ParamsKey.PASSWORD_KEY);
		requestHeaderType.setSessionID(ParamsKey.SESSION_KEY);
		
		JAXBElement<RequestHeaderType> jaxbRequestHeader = GlobalCore.objectFactoryXLS.createRequestHeader(requestHeaderType);
		xlsType.setHeader(jaxbRequestHeader);
		
		return xlsType;
	}
}
