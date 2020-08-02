package pres.auxiliary.work.selenium.element.old;

/**
 * <p><b>文件名：</b>UnrecognizableLocationModeException.java</p>
 * <p><b>用途：</b>在元素定位方式无法被识别的情况下，抛出的异常</p>
 * <p><b>编码时间：</b>2019年9月24日下午3:19:43</p>
 * <p><b>修改时间：</b>2019年9月24日下午3:19:43</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public class UnrecognizableLocationModeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UnrecognizableLocationModeException() {
		super();
	}

	public UnrecognizableLocationModeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UnrecognizableLocationModeException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnrecognizableLocationModeException(String message) {
		super(message);
	}

	public UnrecognizableLocationModeException(Throwable cause) {
		super(cause);
	}

}
