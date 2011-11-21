package geomobility.servlet.service.exception;

import java.util.List;

public class InvalidParameterException extends RestfulException {
	private static final long serialVersionUID = 1122744948405987845L;
	private String mServiceName;
	
	public InvalidParameterException(){}
	
	public InvalidParameterException(String serviceName) {
		this.mServiceName = serviceName;
	}
	
	public InvalidParameterException(String parameter, String message){
		super(parameter + ": " + message);
	}

	public InvalidParameterException(List<String> listParams, List<String> listMessages){
//		StringBuilder strBuilder = new StringBuilder();
//		if (listParams.size()!=listMessages.size()){
//			
//		}
//		for (int i=0;i<listParams.size();i++){
//			
//		}
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
}
