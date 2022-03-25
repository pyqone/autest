package com.auxiliary.selenium;

import com.auxiliary.AuxiliaryToolsException;

/**
 * <p>
 * <b>文件名：SeleniumToolsException.java</b>
 * </p>
 * <p>
 * <b>用途：</b> 定义selenium工具所有异常类的父类
 * </p>
 * <p>
 * <b>编码时间：2022年3月25日 上午8:07:51
 * </p>
 * <p>
 * <b>修改时间：2022年3月25日 上午8:07:51
 * </p>
 *
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 3.2.0
 */
public class SeleniumToolsException extends AuxiliaryToolsException {
    private static final long serialVersionUID = 1L;

    public SeleniumToolsException() {
        super();
    }

    public SeleniumToolsException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public SeleniumToolsException(String message, Throwable cause) {
        super(message, cause);
    }

    public SeleniumToolsException(String message) {
        super(message);
    }

    public SeleniumToolsException(Throwable cause) {
        super(cause);
    }
}
