package pres.auxiliary.work.selenium.tool;

public class IncorrectDirectoryException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public IncorrectDirectoryException() {
		super();
	}

	public IncorrectDirectoryException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public IncorrectDirectoryException(String message, Throwable cause) {
		super(message, cause);
	}

	public IncorrectDirectoryException(String message) {
		super(message);
	}

	public IncorrectDirectoryException(Throwable cause) {
		super(cause);
	}

}
