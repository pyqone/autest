package com.auxiliary.selenium.location;

import com.auxiliary.selenium.SeleniumToolsException;

/**
 * <p>
 * <b>文件名：</b>UndefinedElementException.java
 * </p>
 * <p>
 * <b>用途：</b>用于查找文件中有误时抛出的异常
 * </p>
 * <p>
 * <b>编码时间：</b>2019年10月25日上午8:30:20
 * </p>
 * <p>
 * <b>修改时间：</b>2022年3月25日上午8:42:39
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 *
 */
public class UndefinedElementException extends SeleniumToolsException {

	public UndefinedElementException() {
		super();
	}

	public UndefinedElementException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UndefinedElementException(String message, Throwable cause) {
		super(message, cause);
	}

	public UndefinedElementException(String message) {
		super(message);
	}

	public UndefinedElementException(Throwable cause) {
		super(cause);
	}

	public UndefinedElementException(String elemenetName, ExceptionElementType exceptionElementType) {
		super("不存在的" + exceptionElementType.name+ "名称：" + elemenetName);
	}


	private static final long serialVersionUID = 1L;

	public enum ExceptionElementType {
		ELEMENT("元素"),
		TEMPLET("模板")
		;
		String name;

		private ExceptionElementType(String name) {
			this.name = name;
		}
	}
}
