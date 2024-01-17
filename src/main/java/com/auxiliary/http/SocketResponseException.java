package com.auxiliary.http;

/**
 * <p>
 * <b>文件名：SocketResponseException.java</b>
 * </p>
 * <p>
 * <b>用途：</b>定义Socket响应错误时，抛出的异常
 * </p>
 * <p>
 * <b>编码时间：2023年12月12日 上午8:12:42
 * </p>
 * <p>
 * <b>修改时间：2023年12月12日 上午8:12:42
 * </p>
 *
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 4.5.0
 */
public class SocketResponseException extends InterfaceToolsException {
    private static final long serialVersionUID = 1L;

    public SocketResponseException() {
        super();
    }

    public SocketResponseException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public SocketResponseException(String message, Throwable cause) {
        super(message, cause);
    }

    public SocketResponseException(String message) {
        super(message);
    }

    public SocketResponseException(Throwable cause) {
        super(cause);
    }

}
