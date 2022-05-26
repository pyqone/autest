package com.auxiliary.http;

/**
 * <p>
 * <b>文件名：HttpResponseException.java</b>
 * </p>
 * <p>
 * <b>用途：</b>定义接口响应错误时，抛出的异常
 * </p>
 * <p>
 * <b>编码时间：2022年5月16日 下午5:17:17
 * </p>
 * <p>
 * <b>修改时间：2022年5月16日 下午5:17:17
 * </p>
 *
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 3.3.0
 */
public class HttpResponseException extends InterfaceToolsException {
    private static final long serialVersionUID = 1L;

    public HttpResponseException() {
        super();
    }

    public HttpResponseException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public HttpResponseException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpResponseException(String message) {
        super(message);
    }

    public HttpResponseException(Throwable cause) {
        super(cause);
    }
}
