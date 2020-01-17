package pres.auxiliary.work.testcase.exception;

/**
 * 该异常在所填写的单元格中有信息存在且不允许被覆盖是抛出的异常
 * @author 彭宇琦
 * @version Ver1.0
 */
public class ExistentContentException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ExistentContentException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ExistentContentException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public ExistentContentException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ExistentContentException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ExistentContentException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
