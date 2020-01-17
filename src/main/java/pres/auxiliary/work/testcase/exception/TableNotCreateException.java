package pres.auxiliary.work.testcase.exception;

/**
 * 该异常在指定的单元格对象无效抛出的异常
 * @author 彭宇琦
 * @version Ver1.0
 */
public class TableNotCreateException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public TableNotCreateException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TableNotCreateException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public TableNotCreateException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public TableNotCreateException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public TableNotCreateException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
