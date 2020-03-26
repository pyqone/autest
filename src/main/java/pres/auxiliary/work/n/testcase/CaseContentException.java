package pres.auxiliary.work.n.testcase;

/**
 * <p><b>文件名：</b>CaseContentException.java</p>
 * <p><b>用途：</b>用于当用例内容生成有误时抛出的异常</p>
 * <p><b>编码时间：</b>2020年3月15日下午5:36:02</p>
 * <p><b>修改时间：</b>2020年3月15日下午5:36:02</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public class CaseContentException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CaseContentException() {
		super();
	}

	public CaseContentException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CaseContentException(String message, Throwable cause) {
		super(message, cause);
	}

	public CaseContentException(String message) {
		super(message);
	}

	public CaseContentException(Throwable cause) {
		super(cause);
	}

}
