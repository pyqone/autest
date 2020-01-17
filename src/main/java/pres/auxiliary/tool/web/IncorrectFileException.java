package pres.auxiliary.tool.web;

/**
 * <p><b>文件名：</b>IncorrectFileException.java</p>
 * <p><b>用途：</b>用于读取的文件与相应的代码不匹配时弹出的异常</p>
 * <p><b>编码时间：</b>2019年8月2日下午5:33:01</p>
 * <p><b>修改时间：</b>2019年8月2日下午5:33:01</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 *
 */
public class IncorrectFileException extends RuntimeException {

	private static final long serialVersionUID = -5513588664798256321L;

	public IncorrectFileException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public IncorrectFileException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public IncorrectFileException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public IncorrectFileException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public IncorrectFileException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
