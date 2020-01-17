package pres.auxiliary.report;

/**
 * 该异常在XML文件语法出错时抛出的异常
 * @author 彭宇琦
 */
public class IncorrectGrammarException extends RuntimeException {
	
	public IncorrectGrammarException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public IncorrectGrammarException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public IncorrectGrammarException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public IncorrectGrammarException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public IncorrectGrammarException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 1L;
}
