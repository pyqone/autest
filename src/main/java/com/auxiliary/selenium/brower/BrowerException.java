package com.auxiliary.selenium.brower;

import com.auxiliary.selenium.SeleniumToolsException;

/**
 * <p>
 * <b>文件名：</b>BrowerException.java
 * </p>
 * <p>
 * <b>用途：</b> 当浏览器初始化错误时抛出的异常
 * </p>
 * <p>
 * <b>编码时间：</b>2021年4月7日上午8:30:39
 * </p>
 * <p>
 * <b>修改时间：</b>2022年3月25日上午8:42:39
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public class BrowerException extends SeleniumToolsException {

	private static final long serialVersionUID = 1L;

	public BrowerException() {
	}

	public BrowerException(String message) {
		super(message);
	}

	public BrowerException(Throwable cause) {
		super(cause);
	}

	public BrowerException(String message, Throwable cause) {
		super(message, cause);
	}

	public BrowerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
