package geomobility.servlet.service.exception;


/**
 * @author VoMinhTam
 * 
 */
public class SessionExpiredException extends RestfulException {
	private static final long serialVersionUID = 1122744948405987845L;
	private String mServiceName;

	public SessionExpiredException(String serviceName) {
		this.mServiceName = serviceName;
	}

}
