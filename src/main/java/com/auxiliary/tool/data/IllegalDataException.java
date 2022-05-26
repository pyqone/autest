package com.auxiliary.tool.data;

import com.auxiliary.AuxiliaryToolsException;

/**
 * <p>
 * <b>文件名：IllegalDataException.java</b>
 * </p>
 * <p>
 * <b>用途：</b> 当元素数据存在问题时抛出的异常
 * </p>
 * <p>
 * <b>编码时间：2022年3月29日 上午8:13:23
 * </p>
 * <p>
 * <b>修改时间：2022年3月29日 上午8:13:23
 * </p>
 *
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 3.0.0
 */
public class IllegalDataException extends AuxiliaryToolsException {

	private static final long serialVersionUID = 1L;

	public IllegalDataException() {
	}

	public IllegalDataException(String arg0) {
		super(arg0);
	}

	public IllegalDataException(Throwable arg0) {
		super(arg0);
	}

	public IllegalDataException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public IllegalDataException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
