package pres.auxiliary.selenium.browers;

/**
 * 该异常在没有找到Chromedriver路径时抛出的异常，继承于RuntimeException
 * @author 彭宇琦
 * @version Ver1.0
 */
public class ChromedriverNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ChromedriverNotFoundException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ChromedriverNotFoundException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public ChromedriverNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ChromedriverNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ChromedriverNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
	

}
