package pres.auxiliary.selenium.xml;

/**
 * <p><b>文件名：</b>NoSuchSignValueException.java</p>
 * <p><b>用途：</b>用于当模板内容中标志的值未定义为元素的属性中时，抛出的异常</p>
 * <p><b>编码时间：</b>2019年10月25日上午11:12:20</p>
 * <p><b>修改时间：</b>2019年10月25日上午11:12:20</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public class NoSuchSignValueException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NoSuchSignValueException() {
		super();
	}

	public NoSuchSignValueException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NoSuchSignValueException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoSuchSignValueException(String message) {
		super(message);
	}

	public NoSuchSignValueException(Throwable cause) {
		super(cause);
	}
}
