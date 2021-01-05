package com.auxiliary.tool.file;

public class UnsupportedFileException extends RuntimeException {

	private static final long serialVersionUID = -5923843751831631669L;

	public UnsupportedFileException() {
		super();
	}

	public UnsupportedFileException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UnsupportedFileException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnsupportedFileException(String message) {
		super(message);
	}

	public UnsupportedFileException(Throwable cause) {
		super(cause);
	}

}
