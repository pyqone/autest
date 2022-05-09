package com.auxiliary.http;

/**
 * <p>
 * <b>文件名：InterfaceReadToolsException.java</b>
 * </p>
 * <p>
 * <b>用途：</b>定义接口读取工具出错时抛出的异常
 * </p>
 * <p>
 * <b>编码时间：2022年4月27日 上午8:19:55
 * </p>
 * <p>
 * <b>修改时间：2022年4月27日 上午8:19:55
 * </p>
 *
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 3.3.0
 */
public class InterfaceReadToolsException extends InterfaceToolsException {
    private static final long serialVersionUID = 1L;

    public InterfaceReadToolsException() {
        super();
    }

    public InterfaceReadToolsException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public InterfaceReadToolsException(String message, Throwable cause) {
        super(message, cause);
    }

    public InterfaceReadToolsException(String message) {
        super(message);
    }

    public InterfaceReadToolsException(Throwable cause) {
        super(cause);
    }
}
