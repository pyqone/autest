package com.auxiliary.testcase.templet;

import org.dom4j.Document;

/**
 * <p>
 * <b>文件名：XmlCaseTemplet.java</b>
 * </p>
 * <p>
 * <b>用途：</b>定义xml形式的用例模板读取类的基本方法
 * </p>
 * <p>
 * <b>编码时间：2023年1月16日 上午8:14:16
 * </p>
 * <p>
 * <b>修改时间：2023年1月16日 上午8:14:16
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 4.0.0
 */
public abstract class XmlCaseTemplet<T extends XmlCaseTemplet<T>> extends CaseTemplet<T> {
    /**
     * 用于指向测试用例xml文件的Document对象
     */
    protected Document configXml;
}
