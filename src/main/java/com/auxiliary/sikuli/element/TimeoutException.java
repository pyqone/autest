package com.auxiliary.sikuli.element;

/**
 * <p>
 * <b>文件名：</b>TimeoutException.java
 * </p>
 * <p>
 * <b>用途：</b>
 * 当元素操作执行超时时，抛出的异常
 * </p>
 * <p>
 * <b>编码时间：</b>2022年1月17日 上午8:06:59
 * </p>
 * <p>
 * <b>修改时间：</b>2022年1月17日 上午8:06:59
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since autest 3.0.0
 */
public class TimeoutException extends FindElementException {
    private static final long serialVersionUID = 1L;

    public TimeoutException() {
    }

    public TimeoutException(String arg0) {
        super(arg0);
    }

    public TimeoutException(Throwable arg0) {
        super(arg0);
    }

    public TimeoutException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public TimeoutException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3);
    }

}
