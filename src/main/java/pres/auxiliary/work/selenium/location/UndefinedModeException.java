package pres.auxiliary.work.selenium.location;

/**
 * 若传入的模型参数有误时抛出的异常
 * @author 彭宇琦
 * @version V1.0
 *
 */
public class UndefinedModeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UndefinedModeException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UndefinedModeException(String arg0, Throwable arg1, boolean arg2,
			boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

	public UndefinedModeException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public UndefinedModeException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public UndefinedModeException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	
}
