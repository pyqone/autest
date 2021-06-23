package com.auxiliary.testcase.file;

/**
 * @author 彭宇琦
 */
public class IncorrectContentException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public IncorrectContentException() {
		super();
	}

	public IncorrectContentException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public IncorrectContentException(String message, Throwable cause) {
		super(message, cause);
	}

	public IncorrectContentException(String message) {
		super(message);
	}

	public IncorrectContentException(Throwable cause) {
		super(cause);
	}
}
