package com.auxiliary.sikuli.location;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.sikuli.script.Location;

import com.auxiliary.selenium.location.IncorrectFileException;
import com.auxiliary.selenium.location.UndefinedElementException;
import com.auxiliary.selenium.location.UndefinedElementException.ExceptionElementType;

/**
 * <p>
 * <b>文件名：</b>XmlSikuliLocation.java
 * </p>
 * <p>
 * <b>用途：</b>
 * 用于读取通过xml文件的形式，存储的sikuli截图元素信息，并返回相应元素的信息。
 * </p>
 * <p>
 * <b>编码时间：</b>2022年1月10日 下午3:41:25
 * </p>
 * <p>
 * <b>修改时间：</b>2022年1月10日 下午3:41:25
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since autest 3.0.0
 */
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

    /**
     * 存储所有的模板元素
     */
    private HashMap<String, Element> tempElementMap = new HashMap<>();

    /**
     * 构造对象，读取xml文件，并初始化相应内容
     *
     * @param xmlFile xml文件类对象
     */
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

        // 读取模板元素，并缓存至类中
        rootElement.element(XmlFileField.ELE_TEMPLET.getName()).elements().forEach(element -> {
            // 获取模板的id属性，若发现不存在该属性的内容，则抛出异常
            String id = Optional.ofNullable(element.attributeValue(XmlFileField.ATT_ID.getName())).orElseThrow(
                    () -> new UndefinedElementException("文件中存在无id属性的模板。文件位置：" + xmlFile.getAbsolutePath()));
            // 缓存元素类对象
            tempElementMap.put(id, element);
        });
    }

    @Override
    protected AbstractSikuliLocation find(String name) {
        // 判断传入的名称是否正确
        String newName = Optional.ofNullable(name).filter(n -> !n.isEmpty())
                .orElseThrow(() -> new UndefinedElementException(name, ExceptionElementType.ELEMENT));

        // 查找元素
        this.element = getElementLabelElement(newName);
        this.name = newName;

        return this;
    }

    @Override
    public List<ElementLocationInfo> getElementLocationList(String name) {
        // 若当前查找的元素名称非上次查找的元素名称，则重新进行查找
        if (!Objects.equals(name, this.name)) {
            find(name);
        } else {
            if (Optional.ofNullable(elementInfoList).filter(li -> li.size() != 0).isPresent()) {
                return elementInfoList;
            }
        }

        // 获取当前元素下的所有元素，并将其转换为元素信息类对象
        return element.elements().stream().map(element -> {
            HashMap<ElementInfoDataType, String> elementDataMap = new HashMap<>();
            elementDataMap.put(ElementInfoDataType.X, "0.0");
            elementDataMap.put(ElementInfoDataType.Y, "0.0");
            elementDataMap.put(ElementInfoDataType.SIMILAR, "");
            elementDataMap.put(ElementInfoDataType.FILE_NAME, "");
            elementDataMap.put(ElementInfoDataType.PATH, defaultPath);
            elementDataMap.put(ElementInfoDataType.SUFFIX, defaultSuffix);

            // 判断元素是否调用模板
            String tempId = Optional.ofNullable(element.attributeValue(XmlFileField.ATT_TEMP_ID.getName())).orElse("");
            if (!tempId.isEmpty()) {
                // 判断当前模板是否存在，不存在，则抛出异常
                if (!tempElementMap.containsKey(tempId)) {
                    throw new UndefinedElementException(String.format("“%s”元素存在未定义的模板名称“%s”", name, tempId));
                }

                // 根据模板元素的标签属性更新元素信息
                elementDataMap = updataElementData(elementDataMap, tempElementMap.get(tempId));
            }

            // 根据当前元素标签的属性更新元素信息
            elementDataMap = updataElementData(elementDataMap, element);
            // 替换占位符
            elementDataMap.put(ElementInfoDataType.FILE_NAME,
                    replaceValue(element, elementDataMap.get(ElementInfoDataType.FILE_NAME)));

            // 封装元素文件类对象
            String fileName = elementDataMap.get(ElementInfoDataType.FILE_NAME);
            String suffix = elementDataMap.get(ElementInfoDataType.SUFFIX);

            // 若无法获取到文件名，则将元素的名称作为文件名
            if (fileName.isEmpty()) {
                fileName = element.getParent().attributeValue(XmlFileField.ATT_NAME.getName());
            }
            // 若后缀名为空，则抛出异常
            if (suffix.isEmpty()) {
                throw new IncorrectFileException(
                        String.format("元素“%s”截图文件路径异常，无法定义截图文件对象。文件名：%s.%s", name, fileName, suffix));
            }
            File screenFile = new File(
                    String.format("%s\\%s.%s", elementDataMap.get(ElementInfoDataType.PATH), fileName, suffix));

            // 封装元素操作坐标信息
            Location point = null;
            try {
                double x = Double.valueOf(elementDataMap.get(ElementInfoDataType.X));
                double y = Double.valueOf(elementDataMap.get(ElementInfoDataType.Y));

                point = new Location(x, y);
            } catch (NumberFormatException e) {
                throw new UndefinedElementException(String.format("“%s”元素指定的坐标存在异常，无法转换为数字：(%s, %s)",
                        Double.valueOf(elementDataMap.get(ElementInfoDataType.X)),
                        Double.valueOf(elementDataMap.get(ElementInfoDataType.Y))));
            }

            // 转换相似度数据
            double similar = ElementLocationInfo.DEFAULT_SIMILAR;
            try {
                // 若相似度数据不为空，则对相似度数据进行转换
                if (!elementDataMap.get(ElementInfoDataType.SIMILAR).isEmpty()) {
                    similar = Double.valueOf(elementDataMap.get(ElementInfoDataType.SIMILAR));
                }
            } catch (NumberFormatException e) {
                throw new UndefinedElementException(String.format("“%s”元素指定的相似度数据存在异常，无法转换为数字：%s", name,
                        elementDataMap.get(ElementInfoDataType.SIMILAR)));
            }

            return new ElementLocationInfo(screenFile.getAbsolutePath(), point, similar);
        }).collect(Collectors.toList());
    }

    @Override
    public int getWaitTime(String name) {
        // 若当前查找的元素名称非上次查找的元素名称，则重新进行查找
        if (!Objects.equals(name, this.name)) {
            find(name);
        }

        try {
            return Optional.ofNullable(element.attributeValue(XmlFileField.ATT_WAIT.getName())).map(Integer::valueOf)
                    .filter(time -> time > 0).orElse(DEFAULT_WAIT_TIME);
        } catch (NumberFormatException e) {
            throw new UndefinedElementException(String.format("“%s”元素指定的等待时间存在异常，无法转换为数字：%s", name,
                    element.attributeValue(XmlFileField.ATT_WAIT.getName())));
        }
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
        String xpath = String.format("//%s[@name='%s']", XmlFileField.ELE_ELEMENT.getName(), name);

        // 根据xpath查找元素
        return Optional.ofNullable(readDom.selectSingleNode(xpath)).map(node -> (Element) node)
                .orElseThrow(() -> new UndefinedElementException(name, ExceptionElementType.ELEMENT));
    }

    /**
     * 该方法用于对元素相应的属性进行更新
     *
     * @param elementDataMap 元素信息组
     * @param dataElement    元素类对象
     * @return 更新后的元素信息
     * @since autest 3.0.0
     */
    private HashMap<ElementInfoDataType, String> updataElementData(HashMap<ElementInfoDataType, String> elementDataMap,
            Element dataElement) {
        // 获取文件路径
        Optional.ofNullable(dataElement.attributeValue(XmlFileField.ATT_PATH.getName())).ifPresent(path -> {
            elementDataMap.put(ElementInfoDataType.PATH, path);
        });

        // 获取文件后缀
        Optional.ofNullable(dataElement.attributeValue(XmlFileField.ATT_SUFFIX.getName()))
                .filter(text -> !text.isEmpty()).ifPresent(suffix -> {
                    elementDataMap.put(ElementInfoDataType.SUFFIX, suffix);
                });

        // 获取文件名称
        Optional.ofNullable(dataElement.getText()).filter(text -> !text.isEmpty()).ifPresent(text -> {
            elementDataMap.put(ElementInfoDataType.FILE_NAME, text);
        });

        // 获取x坐标，当转换失败时，则不进行存储
        Optional.ofNullable(dataElement.attributeValue(XmlFileField.ATT_X.getName())).ifPresent(x -> {
            elementDataMap.put(ElementInfoDataType.X, x);
        });

        // 获取y坐标，当转换失败时，则不进行存储
        Optional.ofNullable(dataElement.attributeValue(XmlFileField.ATT_Y.getName())).ifPresent(y -> {
            elementDataMap.put(ElementInfoDataType.Y, y);
        });

        // 获取相似度，当转换失败时，则不进行存储
        Optional.ofNullable(dataElement.attributeValue(XmlFileField.ATT_SIMILAR.getName())).ifPresent(similar -> {
            try {
                elementDataMap.put(ElementInfoDataType.SIMILAR, similar);
            } catch (NumberFormatException e) {
            }
        });

        return elementDataMap;
    }

    /**
     * 用于对获取的元素内容进行转换，得到完整的定位内容
     *
     * @param element 元素类对象
     * @param value   定位内容
     * @return 完整的定位内容
     * @since autest 3.0.0
     */
    private String replaceValue(Element element, String value) {
        // 判断元素是否存在需要替换的内容，若不存在，则不进行替换
        if (!value.matches(String.format(".*%s.*%s.*", startRegex, endRegex))) {
            return value;
        }

        String repalceText = "";

        // 遍历元素的所有属性，并一一进行替换
        for (Attribute att : element.attributes()) {
            // 定义属性替换符
            repalceText = startRegex + att.getName() + endRegex;
            // 替换value中所有与repalceText匹配的字符
            value = value.replaceAll(repalceText, att.getValue());
        }

        // 替换父层节点的name属性
        repalceText = startRegex + "name" + endRegex;
        // 替换value中所有与repalceText匹配的字符
        value = value.replaceAll(repalceText, element.getParent().attributeValue("name"));

        return value;
    }

    /**
     * <p>
     * <b>文件名：</b>XmlSikuliLocation.java
     * </p>
     * <p>
     * <b>用途：</b> 定义xml文件中使用到的字段与属性，其中：
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
         * 定义相似度属性
         */
        ATT_SIMILAR("similar"),
        /**
         * 定义操作坐标x属性
         */
        ATT_X("x"),
        /**
         * 定义操作坐标y属性
         */
        ATT_Y("y"),
        /**
         * 定义读取模板id属性
         */
        ATT_TEMP_ID("temp_id"),
        /**
         * 定义操作等待时间
         */
        ATT_WAIT("wait"),
        /**
         * 定义模板id属性
         */
        ATT_ID("id"),
        /**
         * 定义元素名称属性
         */
        ATT_NAME("name"),
        /**
         * 定义元素标签
         */
        ELE_ELEMENT("element"),
        /**
         * 定义模板标签
         */
        ELE_TEMPLET("templet");
        ;

        /**
         * 存储字段枚举的名称
         */
        private String name = "";

        /**
         * 初始化字段名称
         *
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

    /**
     * <p>
     * <b>文件名：</b>XmlSikuliLocation.java
     * </p>
     * <p>
     * <b>用途：</b> 定义元素信息需要包含的内容枚举
     * </p>
     * <p>
     * <b>编码时间：</b>2022年1月6日 上午11:17:59
     * </p>
     * <p>
     * <b>修改时间：</b>2022年1月6日 上午11:17:59
     * </p>
     *
     * @author 彭宇琦
     * @version Ver1.0
     * @since autest 3.0.0
     */
    private enum ElementInfoDataType {
        X, Y, SIMILAR, FILE_NAME, PATH, SUFFIX;
    }
}
