package com.auxiliary.testcase.file;

import com.auxiliary.AuxiliaryToolsException;

/**
 * <p>
 * <b>文件名：</b>IncorrectFileException.java
 * </p>
 * <p>
 * <b>用途：</b>当文件格式或文件内容不正确时抛出的异常
 * </p>
 * <p>
 * <b>编码时间：</b>2020年2月11日下午11:18:22
 * </p>
 * <p>
 * <b>修改时间：</b>2023年2月7日 上午8:17:10
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 2.1.0
 */
public class IncorrectFileException extends AuxiliaryToolsException {

    private static final long serialVersionUID = 1L;

    public IncorrectFileException() {
        super();

    }

    public IncorrectFileException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);

    }

    public IncorrectFileException(String message, Throwable cause) {
        super(message, cause);

    }

    public IncorrectFileException(String message) {
        super(message);

    }

    public IncorrectFileException(Throwable cause) {
        super(cause);

    }

}
