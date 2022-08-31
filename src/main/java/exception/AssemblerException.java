package exception;

public class AssemblerException extends Exception {

	private static final long serialVersionUID = 1L;
	private String message;
	
	public AssemblerException(String message) {
		super(message);
		this.message = message;
	}

	public AssemblerException(String message, Throwable t) {
		super(message, t);
		this.message = message;
	}

	
	@Override
	public String getMessage() {
		return message;
	}
	
}
