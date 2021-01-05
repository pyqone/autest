package com.auxiliary.tool.file.excel;

/**
 * <p><b>文件名：</b>IncorrectIndexException.java</p>
 * <p><b>用途：</b>
 * 当下标不正确时抛出的异常
 * </p>
 * <p><b>编码时间：</b>2020年8月16日下午10:42:47</p>
 * <p><b>修改时间：</b>2020年8月16日下午10:42:47</p>
 * @author 彭宇琦
 * @version Ver1.0
 *
 */
public class IncorrectIndexException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public IncorrectIndexException() {
		super();
	}

	public IncorrectIndexException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public IncorrectIndexException(String message, Throwable cause) {
		super(message, cause);
	}

	public IncorrectIndexException(String message) {
		super(message);
	}

	public IncorrectIndexException(Throwable cause) {
		super(cause);
	}

}
