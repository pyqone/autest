package pres.auxiliary.work.old.testcase.writecase;

public class UndefinedDirectoryException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UndefinedDirectoryException() {
		super();
	}

	public UndefinedDirectoryException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UndefinedDirectoryException(String message, Throwable cause) {
		super(message, cause);
	}

	public UndefinedDirectoryException(String message) {
		super(message);
	}

	public UndefinedDirectoryException(Throwable cause) {
		super(cause);
	}

}
