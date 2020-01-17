package pres.auxiliary.directory.exception;

/**
 * 该异常为传入的文件夹路径不正确时抛出的异常，继承RuntimeException
 * @author 彭宇琦
 * @version Ver1.0
 */
public class IncorrectDirectoryException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public IncorrectDirectoryException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public IncorrectDirectoryException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public IncorrectDirectoryException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public IncorrectDirectoryException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public IncorrectDirectoryException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	
}
