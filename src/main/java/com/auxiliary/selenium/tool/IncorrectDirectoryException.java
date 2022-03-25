package com.auxiliary.selenium.tool;

import com.auxiliary.selenium.SeleniumToolsException;

/**
 * <p>
 * <b>文件名：IncorrectDirectoryException.java</b>
 * </p>
 * <p>
 * <b>用途：</b> 当selenium截图存在问题时，抛出的异常
 * </p>
 * <p>
 * <b>编码时间：2022年3月25日 上午8:53:04
 * </p>
 * <p>
 * <b>修改时间：2022年3月25日 上午8:53:04
 * </p>
 *
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 2.0.0
 */
public class IncorrectDirectoryException extends SeleniumToolsException {

	private static final long serialVersionUID = 1L;

	public IncorrectDirectoryException() {
		super();
	}

	public IncorrectDirectoryException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public IncorrectDirectoryException(String message, Throwable cause) {
		super(message, cause);
	}

	public IncorrectDirectoryException(String message) {
		super(message);
	}

	public IncorrectDirectoryException(Throwable cause) {
		super(cause);
	}

}
