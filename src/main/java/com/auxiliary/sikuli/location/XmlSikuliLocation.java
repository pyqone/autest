package com.auxiliary.sikuli.location;

import java.io.File;
import java.util.List;
import java.util.Optional;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.auxiliary.selenium.location.IncorrectFileException;
import com.auxiliary.selenium.location.UndefinedElementException;
import com.auxiliary.selenium.location.UndefinedElementException.ExceptionElementType;

public class XmlSikuliLocation extends AbstractSikuliLocation {
    /**
     * 存储xml文件读取类对象
     */
    private Document readDom;
    /**
     * 缓存查找到的元素
     */
    private Element element = null;

    /**
     * 存储截图文件默认存放路径
     */
    private String defaultPath = "";
    /**
     * 存储截图文件默认后缀名
     */
    private String defaultSuffix = "";

    public XmlSikuliLocation(File xmlFile) {
        // 将编译时异常转换为运行时异常
        try {
            readDom = new SAXReader().read(xmlFile);
        } catch (DocumentException e) {
            throw new IncorrectFileException("xml文件异常，无法读取。文件位置：" + xmlFile.getAbsolutePath(), e);
        }

        // 获取根节点
        Element rootElement = readDom.getRootElement();
        // 获取元素默认存放路径
        defaultPath = Optional.ofNullable(rootElement.attributeValue(XmlFileField.ATT_PATH.getName())).orElse("");
        // 获取元素默认后缀名
        defaultSuffix = Optional.ofNullable(rootElement.attributeValue(XmlFileField.ATT_SUFFIX.getName())).orElse("");
    }

    @Override
    protected AbstractSikuliLocation find(String name) {
        // 判断传入的名称是否正确
        String newName = Optional.ofNullable(name).filter(n -> !n.isEmpty())
                .orElseThrow(() -> new UndefinedElementException(name, ExceptionElementType.ELEMENT));

        // 判断当前查找的元素是否与原元素名称一致，不一致，则进行元素查找
        if (!newName.equals(this.name)) {
            // 查找元素
            this.element = getElementLabelElement(newName);
            this.name = newName;
        }

        return this;
    }

    @Override
    public List<ElementLocationInfo> getElementLocationList(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected int getWaitTime(String name) {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * 用于返回元素标签对象
     *
     * @param name 元素名称
     * @return 相应的元素标签类对象
     * @throws UndefinedElementException 元素不存在时抛出的异常
     */
    private Element getElementLabelElement(String name) {
        // 将元素名称转换为查找使用的xpath
        String xpath = String.format("//%selement[@name='%s']", XmlFileField.ELE_ELEMENT.getName(), name);

        // 根据xpath查找元素
        return Optional.ofNullable(readDom.selectSingleNode(xpath)).map(node -> (Element) node)
                .orElseThrow(() -> new UndefinedElementException(name, ExceptionElementType.ELEMENT));
    }

    /**
     * <p>
     * <b>文件名：</b>XmlSikuliLocation.java
     * </p>
     * <p>
     * <b>用途：</b>
     * 定义xml文件中使用到的字段与属性，其中：
     * <ul>
     * <li>ATT表示属性</li>
     * <li>ELE表示标签</li>
     * </ul>
     * </p>
     * <p>
     * <b>编码时间：</b>2022年1月5日 上午11:31:51
     * </p>
     * <p>
     * <b>修改时间：</b>2022年1月5日 上午11:31:51
     * </p>
     *
     * @author 彭宇琦
     * @version Ver1.0
     * @since autest 3.0.0
     */
    public enum XmlFileField {
        /**
         * 定义文件存放路径属性
         */
        ATT_PATH("path"),
        /**
         * 定义文件的后缀
         */
        ATT_SUFFIX("suffix"),
        /**
         * 定义元素标签
         */
        ELE_ELEMENT("element");
        ;

        /**
         * 存储字段枚举的名称
         */
        private String name = "";

        /**
         * 初始化字段名称
         * @param name 字段名称
         */
        private XmlFileField(String name) {
            this.name = name;
        }

        /**
         * 该方法用于返回枚举名称
         *
         * @return 枚举名称
         * @since autest 3.0.0
         */
        public String getName() {
            return name;
        }
    }
}
