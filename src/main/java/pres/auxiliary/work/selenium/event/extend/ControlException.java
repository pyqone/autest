package pres.auxiliary.work.selenium.event.extend;

/**
 * <p><b>文件名：</b>ControlException.java</p>
 * <p><b>用途：</b>
 * 获取控件有误时抛出的异常
 * </p>
 * <p><b>编码时间：</b>2020年11月30日上午8:37:49</p>
 * <p><b>修改时间：</b>2020年11月30日上午8:37:49</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 *
 */
public class ControlException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ControlException() {
	}

	public ControlException(String message) {
		super(message);
	}

	public ControlException(Throwable cause) {
		super(cause);
	}

	public ControlException(String message, Throwable cause) {
		super(message, cause);
	}

	public ControlException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
	/**
	 * 用于对控件枚举不存在抛出异常时编写的基础信息
	 * @param controlName 控件名称
	 * @param enumName 枚举名称
	 */
	public ControlException(String controlName, String enumName) {
		super("“" + controlName + "”映射不存在，无法操作，需要指定枚举“" + enumName + "”的映射");
	}

}
