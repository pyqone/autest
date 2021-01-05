package pres.auxiliary.db;

/**
 * <p><b>文件名：</b>DatabaseException.java</p>
 * <p><b>用途：</b>
 * 数据库链接有误时抛出的异常
 * </p>
 * <p><b>编码时间：</b>2020年12月7日上午8:28:45</p>
 * <p><b>修改时间：</b>2020年12月7日上午8:28:45</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 *
 */
public class DatabaseException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public DatabaseException() {
		super("结果集为空，SQL执行异常");
	}

	public DatabaseException(String message) {
		super(message);
	}

	public DatabaseException(Throwable cause) {
		super(cause);
	}

	public DatabaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public DatabaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
