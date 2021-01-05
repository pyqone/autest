package com.auxiliary.tool.file.excel;

/**
 * <p><b>文件名：</b>NoSuchTypeException.java</p>
 * <p><b>用途：</b>
 * 当枚举类型不存在时抛出的异常
 * </p>
 * <p><b>编码时间：</b>2020年8月22日下午4:40:44</p>
 * <p><b>修改时间：</b>2020年8月22日下午4:40:44</p>
 * @author 彭宇琦
 * @version Ver1.0
 *
 */
public class NoSuchTypeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NoSuchTypeException() {
		super();
	}

	public NoSuchTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NoSuchTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoSuchTypeException(String message) {
		super(message);
	}

	public NoSuchTypeException(Throwable cause) {
		super(cause);
	}

}
