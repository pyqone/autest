package pres.auxiliary.work.selenium.xml;

/**
 * 该异常在XML文件不正确或XML路径不存在时抛出
 * 
 * @author 彭宇琦
 * @version V1.0
 * 
 */
public class IncorrectXmlPathException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public IncorrectXmlPathException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public IncorrectXmlPathException(String arg0, Throwable arg1, boolean arg2,
			boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

	public IncorrectXmlPathException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public IncorrectXmlPathException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public IncorrectXmlPathException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

}
