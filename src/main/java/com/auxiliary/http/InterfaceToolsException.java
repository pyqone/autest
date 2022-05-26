package com.auxiliary.http;

import com.auxiliary.AuxiliaryToolsException;

/**
 * <p>
 * <b>文件名：InterfaceToolsException.java</b>
 * </p>
 * <p>
 * <b>用途：</b> 定义接口工具异常
 * </p>
 * <p>
 * <b>编码时间：2022年4月27日 上午8:15:57
 * </p>
 * <p>
 * <b>修改时间：2022年4月27日 上午8:15:57
 * </p>
 *
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 3.3.0
 */
public class InterfaceToolsException extends AuxiliaryToolsException {
    private static final long serialVersionUID = 1L;

    public InterfaceToolsException() {
        super();
    }

    public InterfaceToolsException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public InterfaceToolsException(String message, Throwable cause) {
        super(message, cause);
    }

    public InterfaceToolsException(String message) {
        super(message);
    }

    public InterfaceToolsException(Throwable cause) {
        super(cause);
    }

}
