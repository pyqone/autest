package pres.auxiliary.directory.exception;

/**
 * 该异常在未指定文件路径或者文件名时抛出的异常，继承RuntimeException
 * 
 * @author 彭宇琦
 * @version Ver1.0
 */
public class UndefinedDirectoryException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UndefinedDirectoryException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UndefinedDirectoryException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public UndefinedDirectoryException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public UndefinedDirectoryException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public UndefinedDirectoryException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
