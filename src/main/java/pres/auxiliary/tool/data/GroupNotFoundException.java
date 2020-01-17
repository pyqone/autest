package pres.auxiliary.tool.data;

/**
 * <p><b>文件名：</b>GroupNotFindException.java</p>
 * <p><b>用途：</b>当条件组名不存在时抛出的异常</p>
 * <p><b>编码时间：</b>2020年1月9日上午8:57:41</p>
 * <p><b>修改时间：</b>2020年1月9日上午8:57:41</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public class GroupNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public GroupNotFoundException() {
		super();
	}

	public GroupNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public GroupNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public GroupNotFoundException(String message) {
		super(message);
	}

	public GroupNotFoundException(Throwable cause) {
		super(cause);
	}
	
}
