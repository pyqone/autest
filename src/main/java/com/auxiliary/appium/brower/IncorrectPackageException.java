package com.auxiliary.appium.brower;

/**
 * <p>
 * <b>文件名：</b>IncorrectPackageException.java
 * </p>
 * <p>
 * <b>用途：</b> 当app包信息有误时抛出的异常
 * </p>
 * <p>
 * <b>编码时间：</b>2021年4月7日上午8:12:46
 * </p>
 * <p>
 * <b>修改时间：</b>2021年4月7日上午8:12:46
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public class IncorrectPackageException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public IncorrectPackageException() {
	}

	public IncorrectPackageException(String message) {
		super(message);
	}

	public IncorrectPackageException(Throwable cause) {
		super(cause);
	}

	public IncorrectPackageException(String message, Throwable cause) {
		super(message, cause);
	}

	public IncorrectPackageException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
