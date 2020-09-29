package pres.auxiliary.work.selenium.location;

/**
 * 该异常在查找到相同文件名时抛出的异常
 * @author 彭宇琦
 * @version Ver1.0
 */
public class RepeatedXmlFileNameException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RepeatedXmlFileNameException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RepeatedXmlFileNameException(String arg0, Throwable arg1,
			boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

	public RepeatedXmlFileNameException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public RepeatedXmlFileNameException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public RepeatedXmlFileNameException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
	
}
