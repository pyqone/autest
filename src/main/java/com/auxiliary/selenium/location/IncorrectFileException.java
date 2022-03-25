package com.auxiliary.selenium.location;

import com.auxiliary.selenium.SeleniumToolsException;

/**
 * <p>
 * <b>文件名：IncorrectFileException.java</b>
 * </p>
 * <p>
 * <b>用途：</b>路径文件有误时抛出的异常
 * </p>
 * <p>
 * <b>编码时间：2022年3月25日 上午8:51:44
 * </p>
 * <p>
 * <b>修改时间：2022年3月25日 上午8:51:44
 * </p>
 *
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 2.0.0
 */
public class IncorrectFileException extends SeleniumToolsException {

	private static final long serialVersionUID = 1L;

	public IncorrectFileException() {
		super();
	}

	public IncorrectFileException(String arg0, Throwable arg1,
			boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public IncorrectFileException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public IncorrectFileException(String arg0) {
		super(arg0);
	}

	public IncorrectFileException(Throwable arg0) {
		super(arg0);
	}

}
