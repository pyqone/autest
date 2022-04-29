package com.auxiliary.http;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * <p>
 * <b>文件名：ReadInterfaceFromXml.java</b>
 * </p>
 * <p>
 * <b>用途：</b>用于读取xml文件中的接口信息
 * </p>
 * <p>
 * <b>编码时间：2022年4月27日 上午8:10:25
 * </p>
 * <p>
 * <b>修改时间：2022年4月27日 上午8:10:25
 * </p>
 *
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 3.3.0
 */
public class ReadInterfaceFromXml extends ReadInterfaceFromAbstract
        implements ActionEnvironment, AssertResponse, ExtractResponse {
    /**
     * 定义接口默认请求环境
     */
    private final String DEFAULT_HOST = InterfaceInfo.DEFAULT_PROTOCOL + InterfaceInfo.DEFAULT_HOST;

    /**
     * 存储xml元素文件类对象
     */
    private File xmlFile;
    /**
     * xml文件根节点
     */
    private Element rootElement;
    private Document dom;

    /**
     * 环境集合
     */
    private HashMap<String, String> environmentMap = new HashMap<>();
    /**
     * 当前执行接口的环境
     */
    private String actionEnvironment = DEFAULT_HOST;

    /**
     * 根据xml文件对象，解析接口信息xml文件，并设置接口执行环境及接口默认执行环境
     *
     * @param xmlFile xml文件类对象
     */
    public ReadInterfaceFromXml(File xmlFile) {
        try {
            dom = new SAXReader().read(xmlFile);
            rootElement = dom.getRootElement();
            this.xmlFile = xmlFile;
        } catch (DocumentException e) {
            throw new InterfaceReadToolsException(String.format("xml文件存在异常，无法解析。文件路径为：%s", xmlFile.getAbsolutePath()),
                    e);
        } catch (NullPointerException e) {
            throw new InterfaceReadToolsException("文件类对象为null");
        }

        // 读取环境集合
        Element environmentsElement = rootElement.element(XmlParamName.XML_LABEL_ENVIRONMENTS);
        List<Element> environmentElementList = environmentsElement.elements();
        // 判断集合是否为空，若为不为空，则存储所有的环境，并设置默认环境
        if (!environmentElementList.isEmpty()) {
            // 获取所有得到环境，并存储所有的环境
            for (Element environmentElement : environmentElementList) {
                String name = Optional.ofNullable(environmentElement.attributeValue(XmlParamName.XML_ATTRI_NAME))
                        .orElseGet(() -> "");
                // 判断当前环境名称是否为空，为空，则不存储
                if (!name.isEmpty()) {
                    // 若当前标签不存在文本节点，则存储默认内容
                    environmentMap.put(name,
                            Optional.ofNullable(environmentElement.getText()).orElseGet(() -> DEFAULT_HOST));
                }
            }

            // 判断环境集合标签中是否指定默认环境，若存在，则将执行环境指向为默认环境；若不存在，则取环境集合的第一个元素
            if (environmentMap.containsKey(environmentsElement.attributeValue(XmlParamName.XML_ATTRI_DEFAULT))) {
                actionEnvironment = environmentMap
                        .get(environmentsElement.attributeValue(XmlParamName.XML_ATTRI_DEFAULT));
            } else {
                actionEnvironment = environmentElementList.get(0).getText();
            }
        }
    }

    @Override
    public InterfaceInfo getInterface(String interName) {
        if (interName == null || interName.isEmpty()) {
            throw new InterfaceReadToolsException("指定的接口名称为空或未指定接口名称：" + interName);
        }

        // 判断该接口是否已缓存，若存在缓存，则直接返回缓存信息
        if (interfaceMap.containsKey(interName)) {
            return interfaceMap.get(interName).clone();
        }

        // 若未缓存信息，则构造接口信息对象，添加接口信息
        InterfaceInfo inter = new InterfaceInfo();
        // 解析环境信息
        inter.analysisUrl(actionEnvironment);

        // 定义查找接口的xpath，并获取接口元素
        String elementXpath = String.format("//%s/%s[@%s='%s']", XmlParamName.XML_LABEL_INTERFACES,
                XmlParamName.XML_LABEL_INTERFACE, XmlParamName.XML_ATTRI_NAME, interName);
        Element interElement = (Element) rootElement.selectSingleNode(elementXpath);
        // 若未查找接口元素，则抛出异常
        if (interElement == null) {
            throw new InterfaceReadToolsException(
                    String.format("接口元素“%s”在文件中不存在：%s", interName, xmlFile.getAbsolutePath()));
        }

        // 获取接口的url
        inter.analysisUrl(readInterURL(interElement));
        // 获取接口的路径
        inter.setPath(readInterPath(interElement));
        // 获取接口的请求方式
        inter.setRequestType(readInterRequestType(interElement));
        // 获取接口参数信息
        readParams(inter, interElement);
        // 设置请求头信息
        readHeader(inter, interElement);
        // 读取请求体信息
        inter.setBody(readInterBody(interElement));

        // TODO 读取接口响应信息

        return inter;
    }

    /**
     * 该方法用于返回接口的路径信息
     *
     * @param interElement 接口元素
     * @return 接口的路径信息
     * @since autest 3.3.0
     */
    private String readInterPath(Element interElement) {
        return interElement.attributeValue(XmlParamName.XML_ATTRI_PATH);
    }

    /**
     * 该方法用于读取接口的请求体信息
     *
     * @param interElement 接口元素
     * @return 请求体信息
     * @since autest 3.3.0
     */
    private String readInterBody(Element interElement) {
        return interElement.elementText(XmlParamName.XML_LABEL_BODY);
    }

    /**
     * 该方法用于读取接口的请求头信息，并存储至接口信息类对象中
     *
     * @param inter        接口信息类对象
     * @param interElement 接口元素
     * @since autest 3.3.0
     */
    private void readHeader(InterfaceInfo inter, Element interElement) {
        // 获取所有的请求头元素
        List<Element> headerElementList = interElement.element(XmlParamName.XML_LABEL_HEADERS)
                .elements(XmlParamName.XML_LABEL_HEADER);
        for (Element headerElement : headerElementList) {
            // 判断当前标签是否存储参数名称，若为空，则不进行存储
            Optional.ofNullable(headerElement.attributeValue(XmlParamName.XML_ATTRI_NAME))
                    .filter(name -> !name.isEmpty()).ifPresent(name -> {
                        inter.addRequestHeader(name,
                                Optional.ofNullable(headerElement.attributeValue(XmlParamName.XML_ATTRI_VALUE))
                                        .orElseGet(() -> ""));
                    });
        }
    }

    /**
     * 该方法用于读取接口的参数信息，并存储至接口信息类对象中
     *
     * @param inter        接口信息类对象
     * @param interElement 接口元素
     * @since autest 3.3.0
     */
    private void readParams(InterfaceInfo inter, Element interElement) {
        // 获取所有的参数标签元素
        List<Element> paramElementList = interElement.element(XmlParamName.XML_LABEL_PARAMS)
                .elements(XmlParamName.XML_LABEL_PARAM);
        for (Element paramElement : paramElementList) {
            // 判断当前标签是否存储参数名称，若为空，则不进行存储
            Optional.ofNullable(paramElement.attributeValue(XmlParamName.XML_ATTRI_NAME))
                    .filter(name -> !name.isEmpty()).ifPresent(name -> {
                        inter.addParam(name,
                                Optional.ofNullable(paramElement.attributeValue(XmlParamName.XML_ATTRI_VALUE))
                                        .orElseGet(() -> ""));
                    });
        }
    }

    /**
     * 该方法用于读取接口请求方式文本，并返回接口的请求方式
     *
     * @param interElement 接口元素
     * @return 接口请求方式
     * @since autest 3.3.0
     */
    private RequestType readInterRequestType(Element interElement) {
        return Optional.ofNullable(interElement.attributeValue(XmlParamName.XML_ATTRI_TYPE)).map(String::toUpperCase)
                .map(RequestType::valueOf).orElseGet(() -> InterfaceInfo.DEFAULT_REQUESTTYPE);
    }

    /**
     * 该方法用于读取接口标签中的url属性，以添加完整接口信息
     *
     * @param interElement 接口元素
     * @return 接口标签中的url属性
     * @since autest 3.3.0
     */
    private String readInterURL(Element interElement) {
        return interElement.attributeValue(XmlParamName.XML_ATTRI_URL);
    }

    @Override
    public String getExtractContent(String interName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getAssertContent(String interName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setActionEnvironment(String environmentName) {
        if (environmentMap.containsKey(environmentName)) {
            actionEnvironment = environmentMap.get(environmentName);
        }
    }

    @Override
    public HashMap<String, String> getActionEnvironment() {
        return environmentMap;
    }

    @Override
    public InterfaceInfo getInterface(String interName, String environmentName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<String> getInterfaceParent(String interName) {
        // TODO Auto-generated method stub
        return null;
    }

    // TODO 考虑参数间关联

    /**
     * <p>
     * <b>文件名：ReadInterfaceFromXml.java</b>
     * </p>
     * <p>
     * <b>用途：</b>定义接口xml文件中包含的所有标签名称以及属性名称
     * </p>
     * <p>
     * <b>编码时间：2022年4月28日 下午5:14:16
     * </p>
     * <p>
     * <b>修改时间：2022年4月28日 下午5:14:16
     * </p>
     *
     *
     * @author 彭宇琦
     * @version Ver1.0
     * @since JDK 1.8
     * @since autest 3.3.0
     */
    private class XmlParamName {
        /**
         * 定义environments标签名称
         */
        public static final String XML_LABEL_ENVIRONMENTS = "environments";
        /**
         * 定义default属性名称
         */
        public static final String XML_ATTRI_DEFAULT = "default";
        /**
         * 定义interfaces标签名称
         */
        public static final String XML_LABEL_INTERFACES = "interfaces";
        /**
         * 定义interface标签名称
         */
        public static final String XML_LABEL_INTERFACE = "interface";
        /**
         * 定义params标签名称
         */
        public static final String XML_LABEL_PARAMS = "params";
        /**
         * 定义param标签名称
         */
        public static final String XML_LABEL_PARAM = "param";
        /**
         * 定义headers标签名称
         */
        public static final String XML_LABEL_HEADERS = "headers";
        /**
         * 定义header标签名称
         */
        public static final String XML_LABEL_HEADER = "header";
        /**
         * 定义body标签名称
         */
        public static final String XML_LABEL_BODY = "body";
        /**
         * 定义name属性名称
         */
        public static final String XML_ATTRI_NAME = "name";
        /**
         * 定义url属性名称
         */
        public static final String XML_ATTRI_URL = "url";
        /**
         * 定义path属性名称
         */
        public static final String XML_ATTRI_PATH = "path";
        /**
         * 定义type属性名称
         */
        public static final String XML_ATTRI_TYPE = "type";
        /**
         * 定义value属性名称
         */
        public static final String XML_ATTRI_VALUE = "value";
    }
}
