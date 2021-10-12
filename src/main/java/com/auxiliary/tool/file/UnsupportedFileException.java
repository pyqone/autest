package com.auxiliary.tool.file;

/**
 * <p>
 * <b>文件名：</b>UnsupportedFileException.java
 * </p>
 * <p>
 * <b>用途：</b>当文件错误时，抛出的异常
 * </p>
 * <p>
 * <b>编码时间：</b>2021年10月5日 上午10:18:58
 * </p>
 * <p>
 * <b>修改时间：</b>2021年10月5日 上午10:18:58
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public class UnsupportedFileException extends RuntimeException {

	private static final long serialVersionUID = -5923843751831631669L;

	public UnsupportedFileException() {
		super();
	}

	public UnsupportedFileException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UnsupportedFileException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnsupportedFileException(String message) {
		super(message);
	}

	public UnsupportedFileException(Throwable cause) {
		super(cause);
	}

}
