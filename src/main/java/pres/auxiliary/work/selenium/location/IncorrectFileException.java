package pres.auxiliary.work.selenium.location;

/**
 * 路径文件有误时抛出的异常
 * @author 彭宇琦
 * @version Ver1.0
 */
public class IncorrectFileException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public IncorrectFileException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public IncorrectFileException(String arg0, Throwable arg1,
			boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

	public IncorrectFileException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public IncorrectFileException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public IncorrectFileException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
	
}
