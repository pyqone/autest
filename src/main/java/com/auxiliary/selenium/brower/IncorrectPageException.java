package com.auxiliary.selenium.brower;

/**
 * <p><b>文件名：</b>IncorrectPageException.java</p>
 * <p><b>用途：</b>当页面配置错误或加载有误时抛出</p>
 * <p><b>编码时间：</b>2020年4月9日下午6:07:53</p>
 * <p><b>修改时间：</b>2020年4月9日下午6:07:53</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public class IncorrectPageException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public IncorrectPageException() {
		super();
	}

	public IncorrectPageException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public IncorrectPageException(String message, Throwable cause) {
		super(message, cause);
	}

	public IncorrectPageException(String message) {
		super(message);
	}

	public IncorrectPageException(Throwable cause) {
		super(cause);
	}

}
