package pres.auxiliary.tool.randomstring;

public class IllegalStringLengthException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public IllegalStringLengthException() {
	}

	public IllegalStringLengthException(String arg0) {
		super(arg0);
	}

	public IllegalStringLengthException(Throwable arg0) {
		super(arg0);
	}

	public IllegalStringLengthException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public IllegalStringLengthException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
