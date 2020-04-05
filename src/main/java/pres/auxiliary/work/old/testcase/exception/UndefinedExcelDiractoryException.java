package pres.auxiliary.work.old.testcase.exception;

/**
 * 该异常在未指定excel文件时抛出
 * @author 彭宇琦
 * @version Ver1.0
 *
 */
public class UndefinedExcelDiractoryException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UndefinedExcelDiractoryException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UndefinedExcelDiractoryException(String arg0, Throwable arg1,
			boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

	public UndefinedExcelDiractoryException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public UndefinedExcelDiractoryException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public UndefinedExcelDiractoryException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
	
}
