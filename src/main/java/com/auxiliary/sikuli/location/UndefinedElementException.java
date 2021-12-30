package com.auxiliary.sikuli.location;

/**
 * <p>
 * <b>文件名：</b>UndefinedElementException.java
 * </p>
 * <p>
 * <b>用途：</b>
 * 当查找到的元素存在错误时抛出的异常
 * </p>
 * <p>
 * <b>编码时间：</b>2021年12月28日 上午9:23:52
 * </p>
 * <p>
 * <b>修改时间：</b>2021年12月28日 上午9:23:52
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since autest 3.0.0
 */
public class UndefinedElementException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UndefinedElementException() {
    }

    public UndefinedElementException(String arg0) {
        super(arg0);
    }

    public UndefinedElementException(Throwable arg0) {
        super(arg0);
    }

    public UndefinedElementException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public UndefinedElementException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3);
    }

}
