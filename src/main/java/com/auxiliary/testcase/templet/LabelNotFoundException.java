package com.auxiliary.testcase.templet;

public class LabelNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public LabelNotFoundException() {
		super();
	}

	public LabelNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		
	}

	public LabelNotFoundException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public LabelNotFoundException(String message) {
		super(message);
		
	}

	public LabelNotFoundException(Throwable cause) {
		super(cause);
		
	}

}
