package pres.auxiliary.selenium.browers;

/**
 * 该异常在目标站点Title与设置的目标站点Title不一致的情况下抛出的异常，继承于RuntimeException
 * 
 * @author 彭宇琦
 * @version Ver1.0
 */
public class IncorrectPageTitleException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public IncorrectPageTitleException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public IncorrectPageTitleException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public IncorrectPageTitleException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public IncorrectPageTitleException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public IncorrectPageTitleException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
