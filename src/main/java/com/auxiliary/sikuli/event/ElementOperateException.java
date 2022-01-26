package com.auxiliary.sikuli.event;

import com.auxiliary.sikuli.SikuliToolsExcepton;

/**
 * <p>
 * <b>文件名：</b>ElementOperateException.java
 * </p>
 * <p>
 * <b>用途：</b>
 * 当sikuli元素无法进行相应操作时，抛出的异常
 * </p>
 * <p>
 * <b>编码时间：</b>2022年1月19日 上午8:11:11
 * </p>
 * <p>
 * <b>修改时间：</b>2022年1月19日 上午8:11:11
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since autest 3.0.0
 */
public class ElementOperateException extends SikuliToolsExcepton {
    private static final long serialVersionUID = 1L;

    public ElementOperateException() {
    }

    public ElementOperateException(String arg0) {
        super(arg0);
    }

    public ElementOperateException(Throwable arg0) {
        super(arg0);
    }

    public ElementOperateException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public ElementOperateException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3);
    }

}
