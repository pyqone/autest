package pres.auxiliary.selenium.browers;

/**
 * 该异常为在页面无法加载出来时抛出的异常，继承于RuntimeException
 * 
 * @author 彭宇琦
 * @version Ver1.0
 */
public class PageNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PageNotFoundException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PageNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public PageNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public PageNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public PageNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
