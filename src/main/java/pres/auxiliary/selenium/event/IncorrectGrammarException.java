package pres.auxiliary.selenium.event;

/**
 * <p><b>文件名：</b>IncorrectGrammarException.java</p>
 * <p><b>用途：</b>文本语法有误时抛出的异常</p>
 * <p><b>编码时间：</b>2019年10月9日下午5:34:34</p>
 * <p><b>修改时间：</b>2019年10月9日下午5:34:34</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public class IncorrectGrammarException extends RuntimeException {

	public IncorrectGrammarException() {
		super();
	}

	public IncorrectGrammarException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public IncorrectGrammarException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public IncorrectGrammarException(String arg0) {
		super(arg0);
	}

	public IncorrectGrammarException(Throwable arg0) {
		super(arg0);
	}

	private static final long serialVersionUID = 1L;

}
