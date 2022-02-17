package com.auxiliary.sikuli;

/**
 * <p>
 * <b>文件名：</b>IncorrectFileException.java
 * </p>
 * <p>
 * <b>用途：</b>
 * 当sikuli工具使用中文件或文件夹路径存在异常时，抛出的异常
 * </p>
 * <p>
 * <b>编码时间：</b>2022年2月17日 下午1:58:15
 * </p>
 * <p>
 * <b>修改时间：</b>2022年2月17日 下午1:58:15
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 3.1.0
 */
public class IncorrectFileException extends SikuliToolsExcepton {
    private static final long serialVersionUID = 1L;

    public IncorrectFileException() {
    }

    public IncorrectFileException(String arg0) {
        super(arg0);
    }

    public IncorrectFileException(Throwable arg0) {
        super(arg0);
    }

    public IncorrectFileException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public IncorrectFileException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3);
    }

}
