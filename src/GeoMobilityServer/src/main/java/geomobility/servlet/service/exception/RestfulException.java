package geomobility.servlet.service.exception;


public class RestfulException extends Exception {
	private static final long serialVersionUID = -2489066639297825465L;
	
	public RestfulException(){
	}
	
	public RestfulException(String message){
		super(message);
	}
}
