package com.auxiliary.tool.file;

/**
 * <p><b>文件名：</b>WriteFileException.java</p>
 * <p><b>用途：</b>
 * 当写入文件存在错误时，抛出的异常
 * </p>
 * <p><b>编码时间：</b>2021年5月17日上午8:18:22</p>
 * <p><b>修改时间：</b>2021年5月17日上午8:18:22</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public class WriteFileException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public WriteFileException() {
	}

	public WriteFileException(String message) {
		super(message);
	}

	public WriteFileException(Throwable cause) {
		super(cause);
	}

	public WriteFileException(String message, Throwable cause) {
		super(message, cause);
	}

	public WriteFileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
