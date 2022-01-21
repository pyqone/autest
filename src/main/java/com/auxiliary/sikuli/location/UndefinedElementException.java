package com.auxiliary.sikuli.location;

import com.auxiliary.sikuli.SikuliToolsExcepton;

/**
 * <p>
 * <b>文件名：</b>UndefinedElementException.java
 * </p>
 * <p>
 * <b>用途：</b>
 * 在指定的元素文件中无法查到相应元素时抛出的异常
 * </p>
 * <p>
 * <b>编码时间：</b>2022年1月21日 上午8:37:16
 * </p>
 * <p>
 * <b>修改时间：</b>2022年1月21日 上午8:37:16
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since autest 3.0.0
 */
public class UndefinedElementException extends SikuliToolsExcepton {
    private static final long serialVersionUID = 1L;

    public UndefinedElementException() {
        super();
    }

    public UndefinedElementException(String elemenetName, ExceptionElementType exceptionElementType) {
        super("不存在的" + exceptionElementType.name+ "名称：" + elemenetName);
    }

    public UndefinedElementException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3);
    }

    public UndefinedElementException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public UndefinedElementException(String arg0) {
        super(arg0);
    }

    public UndefinedElementException(Throwable arg0) {
        super(arg0);
    }

    public enum ExceptionElementType {
        ELEMENT("元素"),
        TEMPLET("模板")
        ;
        String name;

        private ExceptionElementType(String name) {
            this.name = name;
        }
    }
}
