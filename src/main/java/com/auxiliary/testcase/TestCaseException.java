package com.auxiliary.testcase;

import com.auxiliary.AuxiliaryToolsException;

/**
 * <p>
 * <b>文件名：</b>TestCaseException.java
 * </p>
 * <p>
 * <b>用途：</b>
 * 定义在测试用例使用存在问题时抛出的异常
 * </p>
 * <p>
 * <b>编码时间：</b>2022年3月2日 上午8:15:11
 * </p>
 * <p>
 * <b>修改时间：</b>2022年3月2日 上午8:15:11
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 3.2.0
 */
public class TestCaseException extends AuxiliaryToolsException {
    private static final long serialVersionUID = 1L;

    public TestCaseException() {
        super();
    }

    public TestCaseException(String arg0) {
        super(arg0);
    }

    public TestCaseException(Throwable arg0) {
        super(arg0);
    }

    public TestCaseException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public TestCaseException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3);
    }

}
