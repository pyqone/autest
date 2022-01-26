package com.auxiliary.sikuli.element;

import com.auxiliary.sikuli.SikuliToolsExcepton;

/**
 * <p>
 * <b>文件名：</b>FindElementException.java
 * </p>
 * <p>
 * <b>用途：</b>
 * 当查找元素存在问题时，抛出的异常
 * </p>
 * <p>
 * <b>编码时间：</b>2022年1月13日 上午8:07:42
 * </p>
 * <p>
 * <b>修改时间：</b>2022年1月13日 上午8:07:42
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since autest 3.0.0
 */
public class FindElementException extends SikuliToolsExcepton {
    private static final long serialVersionUID = 1L;

    public FindElementException() {
    }

    public FindElementException(String arg0) {
        super(arg0);
    }

    public FindElementException(Throwable arg0) {
        super(arg0);
    }

    public FindElementException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public FindElementException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3);
    }

}
