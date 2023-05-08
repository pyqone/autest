package com.auxiliary.tool.date;

import com.auxiliary.AuxiliaryToolsException;

/**
 * <p>
 * <b>文件名：</b>IncorrectConditionException.java
 * </p>
 * <p>
 * <b>用途：</b>在数据转换错误时抛出的异常
 * </p>
 * <p>
 * <b>编码时间：</b>2019年7月28日下午2:52:32
 * </p>
 * <p>
 * <b>修改时间：</b>2019年7月28日下午2:52:32
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 2.0.0
 */
public class IncorrectConditionException extends AuxiliaryToolsException {

    private static final long serialVersionUID = 1189323604536229031L;

    public IncorrectConditionException() {
        super();
    }

    public IncorrectConditionException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public IncorrectConditionException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectConditionException(String message) {
        super(message);
    }

    public IncorrectConditionException(Throwable cause) {
        super(cause);
    }

}
