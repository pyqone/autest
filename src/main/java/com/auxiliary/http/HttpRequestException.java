package com.auxiliary.http;

/**
 * <p>
 * <b>文件名：HttpRequestException.java</b>
 * </p>
 * <p>
 * <b>用途：</b>定义接口请求错误时，抛出的异常
 * </p>
 * <p>
 * <b>编码时间：2022年5月10日 上午9:12:40
 * </p>
 * <p>
 * <b>修改时间：2022年5月10日 上午9:12:40
 * </p>
 *
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 3.3.0
 */
public class HttpRequestException extends InterfaceToolsException {
    private static final long serialVersionUID = 1L;

    public HttpRequestException() {
        super();
    }

    public HttpRequestException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public HttpRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpRequestException(String message) {
        super(message);
    }

    public HttpRequestException(Throwable cause) {
        super(cause);
    }
}
