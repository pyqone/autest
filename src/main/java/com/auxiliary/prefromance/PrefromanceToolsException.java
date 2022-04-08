package com.auxiliary.prefromance;

import com.auxiliary.AuxiliaryToolsException;

/**
 * <p>
 * <b>文件名：PrefromanceToolsException.java</b>
 * </p>
 * <p>
 * <b>用途：</b>定义性能测试辅助工具大类异常
 * </p>
 * <p>
 * <b>编码时间：2022年4月8日 上午8:55:17
 * </p>
 * <p>
 * <b>修改时间：2022年4月8日 上午8:55:17
 * </p>
 *
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 3.3.0
 */
public class PrefromanceToolsException extends AuxiliaryToolsException {
    private static final long serialVersionUID = 1L;

    public PrefromanceToolsException() {
        super();
    }

    public PrefromanceToolsException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public PrefromanceToolsException(String message, Throwable cause) {
        super(message, cause);
    }

    public PrefromanceToolsException(String message) {
        super(message);
    }

    public PrefromanceToolsException(Throwable cause) {
        super(cause);
    }
}
