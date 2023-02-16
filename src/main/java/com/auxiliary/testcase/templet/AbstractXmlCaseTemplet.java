package com.auxiliary.testcase.templet;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import com.auxiliary.testcase.TestCaseException;

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
public abstract class AbstractXmlCaseTemplet extends AbstractCaseTemplet {
    /**
     * 用于指向测试用例xml文件的Document对象
     */
    protected Document configXml;

    /**
     * 构造对象，读取xml文件模板类
     * 
     * @param xmlTempletFile xml模板文件类对象
     */
    public AbstractXmlCaseTemplet(File xmlTempletFile) {
        // 判断文件是否存在
        if (xmlTempletFile != null && xmlTempletFile.isFile() && xmlTempletFile.exists()) {
            try {
                configXml = new SAXReader().read(xmlTempletFile);
            } catch (DocumentException e) {
                throw new TestCaseException("xml用例模板文件异常，无法读取：" + xmlTempletFile.getAbsolutePath(), e);
            }
        }
    }
}
