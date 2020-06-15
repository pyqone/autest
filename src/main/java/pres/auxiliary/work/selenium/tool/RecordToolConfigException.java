package pres.auxiliary.work.selenium.tool;

/**
 * <p><b>文件名：</b>RecordToolConfigException.java</p>
 * <p><b>用途：</b>使用RecordTool类时，其类中使用的工具未配置正确时抛出该异常</p>
 * <p><b>编码时间：</b>2019年9月22日上午11:48:16</p>
 * <p><b>修改时间：</b>2019年9月22日上午11:48:16</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public class RecordToolConfigException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RecordToolConfigException() {
		super();
	}

	public RecordToolConfigException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RecordToolConfigException(String message, Throwable cause) {
		super(message, cause);
	}

	public RecordToolConfigException(String message) {
		super(message);
	}

	public RecordToolConfigException(Throwable cause) {
		super(cause);
	}

}
