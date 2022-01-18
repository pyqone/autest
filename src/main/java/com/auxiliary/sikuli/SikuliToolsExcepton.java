package com.auxiliary.sikuli;

/**
 * <p>
 * <b>文件名：</b>SikuliToolsExcepton.java
 * </p>
 * <p>
 * <b>用途：</b>
 * 当sikuli工具使用出错时，抛出的异常。所有与sikuli相关的异常类都应继承该异常
 * </p>
 * <p>
 * <b>编码时间：</b>2022年1月13日 上午8:04:39
 * </p>
 * <p>
 * <b>修改时间：</b>2022年1月13日 上午8:04:39
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since autest 3.0.0
 */
public class SikuliToolsExcepton extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public SikuliToolsExcepton() {
    }

    public SikuliToolsExcepton(String arg0) {
        super(arg0);
    }

    public SikuliToolsExcepton(Throwable arg0) {
        super(arg0);
    }

    public SikuliToolsExcepton(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public SikuliToolsExcepton(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3);
     }

}
