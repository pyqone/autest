package com.auxiliary.selenium.location;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.auxiliary.selenium.element.ElementType;
import com.auxiliary.selenium.location.UndefinedElementException.ExceptionElementType;
import com.auxiliary.tool.common.Placeholder;

/**
 * <p>
 * <b>文件名：</b>XmlLocation.java
 * </p>
 * <p>
 * <b>用途：</b>该类用于从指定格式的xml中读取元素信息<br/>
 *
 * <b>注意：</b>
 * <ol>
 * <li>标签的name属性必须唯一，否则读取会出现错误
 * <li>不同类型的定位模板可以使用一个id属性，但同一种类型的定位模板id属性是唯一的，
 * 如xpath模板可以使用id='1'，css模板可以使用id='1'，但另一xpath模板的id属性就不能 再定为1，但建议模板的id也唯一存在
 * <li>元素定位标签只能写xpath、css、classname、id、linktext、name、tagname
 * <li>所有标签均为小写
 * </ol>
 * </p>
 * <p>
 * <b>编码时间：</b>2017年9月25日下午4:23:40
 * </p>
 * <p>
 * <b>修改时间：</b>2021年4月17日 上午11:21:25
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.6
 * @since JDK 8
 */
public class XmlLocation extends AbstractLocation implements ReadElementLimit, AppElementLocation {
    /**
     * 缓存元素信息
     */
    protected Element element = null;

    /**
     * 存储构造后的Document类对象，以读取xml文件中的内容
     */
    private Document readDom;

    /**
     * 用于存储指向的父层文件对象
     */
    private ArrayList<XmlLocation> parentLocationList = new ArrayList<>();

    /**
     * 根据xml文件对象进行构造
     *
     * @param xmlFile xml文件对象
     * @throws IncorrectFileException xml文件有误时抛出的异常
     */
    public XmlLocation(File xmlFile) {
        // 将编译时异常转换为运行时异常
        try {
            readDom = new SAXReader().read(xmlFile);
        } catch (DocumentException e) {
            throw new IncorrectFileException("xml文件异常，文件位置：" + xmlFile.getAbsolutePath(), e);
        }

        // 若存在父层元素，则对父层文件进行存储
        readParentFile();
    }

    /**
     * 根据xml文件的{@link Document}对象进行构造
     *
     * @param dom {@link Document}对象
     */
    public XmlLocation(Document dom) {
        this.readDom = dom;

        // 若存在父层元素，则对父层文件进行存储
        readParentFile();
    }

    @Override
    public ArrayList<ElementLocationInfo> getElementLocation() {
        ArrayList<ElementLocationInfo> locationList = new ArrayList<>();
        // 判断是否进行元素查找
        if (element == null) {
            throw new UndefinedElementException("元素未进行查找，无法返回元素信息");
        }

        // 遍历元素下的所有子标签
        for (Element e : element.elements()) {
            ByType byType = ByType.typeText2Type(e.getName());
            // 忽略不能转换成ByType类型的其他标签
            if (byType == null) {
                continue;
            }

            // 判断标签是否启用，若不启用，则继续循环
            boolean isUse = Optional.ofNullable(e.attributeValue("is_user")).filter(t -> !t.isEmpty()).map(t -> {
                try {
                    return Boolean.valueOf(t).booleanValue();
                } catch (Exception ex) {
                    return true;
                }
            }).orElse(true);
            if (!isUse) {
                continue;
            }

            String locationText = "";
            String tempId = Optional.ofNullable(e.attributeValue("temp_id")).orElse("");
            // 判断是否存在模板，并返回相应的定位方式
            if (tempId.isEmpty()) {
                locationText = e.getText();
            } else {
                locationText = getTemplateValue(tempId, Optional.ofNullable(ByType.typeText2Type(e.getName()))
                        .orElseThrow(() -> new UndefinedElementException("不存在的元素定位方式：" + e.getName())));
            }
            // 替换占位符，得到最终的定位方式
            locationText = replaceValue(e, locationText);

            // 构造并存储该定位方式
            locationList.add(new ElementLocationInfo(byType, locationText));
        }

        return locationList;
    }

    @Override
    public ElementType getElementType() {
        // 判断是否进行元素查找
        if (element == null) {
            throw new UndefinedElementException("元素未进行查找，无法返回元素信息");
        }

        // 若元素标签为iframe，则无法获取属性，直接赋予窗体类型
        if ("iframe".equals(element.getName())) {
            return ElementType.IFRAME_ELEMENT;
        } else {
            // 非窗体元素，则获取元素的元素类型属性
            String elementTypeText = element.attributeValue("element_type");
            // 若属性不存在，则使其指向普通元素
            return toElementType(elementTypeText == null ? "0" : elementTypeText);
        }
    }

    @Override
    public ArrayList<String> getIframeNameList() {
        // 判断是否进行元素查找
        if (element == null) {
            throw new UndefinedElementException("元素未进行查找，无法返回元素信息");
        }

        ArrayList<String> iframeNameList = new ArrayList<>();
        // 循环，获取元素的父层级，直到获取到顶层为止
        Element element = this.element;
        while (!element.isRootElement()) {
            // 定位到元素的父层
            element = element.getParent();
            // 判断当前元素的标签名称，若为iframe标签，则获取其名称，并存储至集合中
            if (element.getName().equals("iframe")) {
                iframeNameList.add(element.attributeValue("name"));
            }
        }

        // 反序集合，使最上层窗体排在最前面
        Collections.reverse(iframeNameList);

        return iframeNameList;
    }

    @Override
    public long getWaitTime() {
        // 判断是否进行元素查找
        if (element == null) {
            throw new UndefinedElementException("元素未进行查找，无法返回元素信息");
        }

        return toWaitTime(element.attributeValue("wait"));
    }

    @Override
    public String getDefaultValue() {
        // 判断是否进行元素查找
        if (element == null) {
            throw new UndefinedElementException("元素未进行查找，无法返回元素信息");
        }

        return Optional.ofNullable(element.attributeValue("value")).orElse("");
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
        String xpath = "//*[@name='" + name + "']";

        // 根据xpath查找元素
        return Optional.ofNullable(xpath2Element(xpath))
                .orElseThrow(() -> new UndefinedElementException(name, ExceptionElementType.ELEMENT));
    }

    /**
     * 获取模板中的定位内容
     *
     * @param tempId 模板id
     * @param byType 定位方式枚举
     * @return 相应的定位内容
     * @throws UndefinedElementException 模板不存在时抛出的异常
     */
    private String getTemplateValue(String tempId, ByType byType) {
        String selectTempXpath = "//templet/" + byType.getValue() + "[@id='" + tempId + "']";

        return Optional.ofNullable(xpath2Element(selectTempXpath))
                .orElseThrow(() -> new UndefinedElementException(tempId, ExceptionElementType.TEMPLET)).getText();
    }

    /**
     * 根据xpath在指定的XmlLocation对象中查找元素
     *
     * @param xml   XmlLocation对象
     * @param xpath 查询元素的xpath
     * @return 被查询的元素，无元素时返回null
     */
    private Element xpath2Element(String xpath) {
        // 在当前层级查找节点
        Element element = (Element) (readDom.selectSingleNode(xpath));
        // 判断节点是否存在，若存在，则返回元素
        if (element != null) {
            return element;
        } else {
            // 若当前不存在节点，则循环查找所有的父层元素，并调用父层的该方法，直到找到元素为止
            for (XmlLocation x : parentLocationList) {
                element = x.xpath2Element(xpath);
                if (element != null) {
                    return element;
                }
            }
        }

        // 若遍历完父层级仍无法找到元素，则返回null
        return null;
    }

    /**
     * 用于对获取的元素内容进行转换，得到完整的定位内容
     *
     * @param element 元素类对象
     * @param value   定位内容
     * @return 完整的定位内容
     */
    private String replaceValue(Element element, String value) {
        Placeholder placeholder = new Placeholder(this.placeholder);
        // 判断元素是否存在需要替换的内容，若不存在，则不进行替换
        if (!placeholder.isContainsPlaceholder(value)) {
            return value;
        }

        // 添加特殊的占位符
        placeholder.addReplaceWord("name", element.getParent().attributeValue("name"));
        // 添加已有的词语
        for (Attribute att : element.attributes()) {
            placeholder.addReplaceWord(att.getName(), att.getValue());
        }

        return placeholder.replaceText(value);
    }

    /**
     * 用于读取父层文件路径，并封装成File对象后，进行存储
     */
    private void readParentFile() {
        Optional.ofNullable(readDom.getRootElement().element("parents")).map(e -> e.elements("parent"))
                .filter(list -> !list.isEmpty()).ifPresent(list -> {
                    list.stream().map(Element::getText).map(File::new).map(XmlLocation::new)
                            .forEach(parentLocationList::add);
                });
    }

    @Override
    public ReadLocation find(String name) {
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
    public boolean isNative() {
        // 判断是否进行元素查找
        if (element == null) {
            throw new UndefinedElementException("元素未进行查找，无法返回元素信息");
        }

        return element.attribute("context") == null;
    }

    @Override
    public String getContext() {
        // 判断是否进行元素查找
        if (element == null) {
            throw new UndefinedElementException("元素未进行查找，无法返回元素信息");
        }

        return Optional.ofNullable(element.attributeValue("context")).orElse("");
    }

    @Override
    public long getBeforeTime() {
        // 判断是否进行元素查找
        if (element == null) {
            throw new UndefinedElementException("元素未进行查找，无法返回元素信息");
        }

        // 转换等待时间，若等待时间小于0，则返回为0
        long time = toWaitTime(element.attributeValue("before_time"));
        return time < 0 ? 0 : time;
    }
}
