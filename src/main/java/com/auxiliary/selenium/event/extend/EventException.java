package com.auxiliary.selenium.event.extend;

import com.auxiliary.selenium.SeleniumToolsException;

/**
 * <p>
 * <b>文件名：</b>EventException.java
 * </p>
 * <p>
 * <b>用途：</b> 在事件执行存在错误时，抛出的异常
 * </p>
 * <p>
 * <b>编码时间：</b>2021年3月27日上午11:07:05
 * </p>
 * <p>
 * <b>修改时间：</b>2022年3月25日上午8:42:39
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public class EventException extends SeleniumToolsException {
	private static final long serialVersionUID = 1L;

	public EventException() {
		super();
	}

	public EventException(String message) {
		super(message);
	}

	public EventException(Throwable cause) {
		super(cause);
	}

	public EventException(String message, Throwable cause) {
		super(message, cause);
	}

	public EventException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
