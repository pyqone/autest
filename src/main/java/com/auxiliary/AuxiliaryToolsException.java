package com.auxiliary;

/**
 * <p>
 * <b>文件名：</b>AuxiliaryToolsException.java
 * </p>
 * <p>
 * <b>用途：</b>
 * 定义整个工具中自定义异常的基类，以便于统一管理和处理异常
 * </p>
 * <p>
 * <b>编码时间：</b>2022年3月2日 上午8:13:28
 * </p>
 * <p>
 * <b>修改时间：</b>2022年3月2日 上午8:13:28
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 3.2.0
 */
public class AuxiliaryToolsException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public AuxiliaryToolsException() {
    }

    public AuxiliaryToolsException(String message) {
        super(message);
    }

    public AuxiliaryToolsException(Throwable cause) {
        super(cause);
    }

    public AuxiliaryToolsException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuxiliaryToolsException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
