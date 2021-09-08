package com.auxiliary.tool.file.io;

import java.io.File;
import java.util.Optional;

/**
 * <p>
 * <b>文件名：</b>FileException.java
 * </p>
 * <p>
 * <b>用途：</b> 在文件读取/写入错误时，抛出的异常
 * </p>
 * <p>
 * <b>编码时间：</b>2021年9月7日上午8:24:21
 * </p>
 * <p>
 * <b>修改时间：</b>2021年9月7日上午8:24:21
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public class FileException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public FileException() {
	}

	public FileException(String message) {
		super(message);
	}

	public FileException(Throwable cause) {
		super(cause);
	}

	public FileException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * 文件异常时，则根据文件是否为空，给出文件内容的提示语
	 * 
	 * @param message       主要消息
	 * @param exceptionFile 异常文件类对象
	 */
	public FileException(String message, File exceptionFile) {
		this(message + "：" + Optional.ofNullable(exceptionFile).map(f -> {
			if (!f.exists()) {
				return String.format("不存在的文件路径“%s”", f.getAbsolutePath());
			}

			if (!f.isFile()) {
				return String.format("文件类对象指向非文件路径“%s”", f.getAbsolutePath());
			}

			return String.format("文件路径为“%s”", f.getAbsolutePath());
		}).orElse("文件类对象为null"));
	}
}
