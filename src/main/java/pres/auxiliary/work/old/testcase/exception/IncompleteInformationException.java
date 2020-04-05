package pres.auxiliary.work.old.testcase.exception;

/**
 * 该异常在新增用例信息不全时抛出
 * @author 彭宇琦
 * @version Ver1.0
 */
public class IncompleteInformationException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public IncompleteInformationException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public IncompleteInformationException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public IncompleteInformationException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public IncompleteInformationException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public IncompleteInformationException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
