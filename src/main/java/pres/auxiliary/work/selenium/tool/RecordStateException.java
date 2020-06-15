package pres.auxiliary.work.selenium.tool;

/**
 * <p><b>文件名：</b>RecordStateException.java</p>
 * <p><b>用途：</b>当记录状态不正确的情况下抛出的异常</p>
 * <p><b>编码时间：</b>2019年10月6日下午5:55:27</p>
 * <p><b>修改时间：</b>2019年10月6日下午5:55:27</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public class RecordStateException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RecordStateException() {
		super();
	}

	public RecordStateException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RecordStateException(String message, Throwable cause) {
		super(message, cause);
	}

	public RecordStateException(String message) {
		super(message);
	}

	public RecordStateException(Throwable cause) {
		super(cause);
	}

}
