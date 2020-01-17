package pres.auxiliary.tool.data;

/**
 * <p><b>文件名：</b>RootConditionNotFindException.java</p>
 * <p><b>用途：</b>当条件组未添加根条件时抛出的异常</p>
 * <p><b>编码时间：</b>2020年1月8日下午8:45:15</p>
 * <p><b>修改时间：</b>2020年1月8日下午8:45:15</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public class RootConditionNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public RootConditionNotFoundException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RootConditionNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public RootConditionNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public RootConditionNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public RootConditionNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
