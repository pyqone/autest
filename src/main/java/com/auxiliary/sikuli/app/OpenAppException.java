package com.auxiliary.sikuli.app;

/**
 * <p>
 * <b>文件名：</b>OpenAppException.java
 * </p>
 * <p>
 * <b>用途：</b>
 * 当应用打开出错时，抛出的异常
 * </p>
 * <p>
 * <b>编码时间：</b>2021年12月20日 下午3:19:37
 * </p>
 * <p>
 * <b>修改时间：</b>2021年12月20日 下午3:19:37
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since autest 3.0.0
 */
public class OpenAppException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public OpenAppException() {
    }

    public OpenAppException(String arg0) {
        super(arg0);
    }

    public OpenAppException(Throwable arg0) {
        super(arg0);
    }

    public OpenAppException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public OpenAppException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3);
    }

}
