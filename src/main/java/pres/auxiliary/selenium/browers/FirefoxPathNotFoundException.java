package pres.auxiliary.selenium.browers;

/**
 * 该异常在没有找到火狐路径时抛出的异常，继承于RuntimeException
 * @author 彭宇琦
 * @version Ver1.0
 */
public class FirefoxPathNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public FirefoxPathNotFoundException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FirefoxPathNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public FirefoxPathNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public FirefoxPathNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public FirefoxPathNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
	

}
