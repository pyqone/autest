package com.auxiliary.tool.file;

/**
 * <p>
 * <b>文件名：IncorrectFieldException.java</b>
 * </p>
 * <p>
 * <b>用途：</b>用于标记模板字段错误的异常
 * </p>
 * <p>
 * <b>编码时间：2025年10月22日 上午8:30:18
 * </p>
 * <p>
 * <b>修改时间：2025年10月22日 上午8:30:18
 * </p>
 *
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 5.1.0
 */
public class IncorrectFieldException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public IncorrectFieldException(String message) {
        super("字段异常：" + message);
    }
}
