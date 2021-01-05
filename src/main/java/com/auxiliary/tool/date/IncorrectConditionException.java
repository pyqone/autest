package com.auxiliary.tool.date;

/**
 * <p><b>文件名：</b>IncorrectConditionException.java</p>
 * <p><b>用途：</b>在数据转换错误时抛出的异常</p>
 * <p><b>编码时间：</b>2019年7月28日下午2:52:32</p>
 * <p><b>修改时间：</b>2019年7月28日下午2:52:32</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 *
 */
public class IncorrectConditionException extends RuntimeException {

	private static final long serialVersionUID = 1189323604536229031L;

	public IncorrectConditionException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public IncorrectConditionException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public IncorrectConditionException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public IncorrectConditionException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public IncorrectConditionException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
}
