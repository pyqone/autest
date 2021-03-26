package com.auxiliary.selenium.event.extend;

/**
 * <p><b>文件名：</b>EventException.java</p>
 * <p><b>用途：</b>
 * </p>
 * <p><b>编码时间：</b>2021年3月24日上午7:35:25</p>
 * <p><b>修改时间：</b>2021年3月24日上午7:35:25</p>
 * @author 
 * @version Ver1.0
 * @since JDK 1.8
 */
public class EventException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public EventException() {
		// TODO Auto-generated constructor stub
	}

	public EventException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public EventException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public EventException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public EventException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
