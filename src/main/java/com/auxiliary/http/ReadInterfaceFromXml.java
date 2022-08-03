package com.auxiliary.http;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.alibaba.fastjson.JSONObject;
import com.auxiliary.AuxiliaryToolsException;
import com.auxiliary.tool.file.TextFileReadUtil;
import com.auxiliary.tool.regex.ConstType;

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
        implements ActionEnvironment, AssertResponse, ExtractResponse, BeforeOperation {
    /**
     * 存储xml元素文件类对象
     */
    private File xmlFile;
    /**
     * xml文件根节点
     */
    private Element rootElement;

    /**
     * 当前查找元素类对象
     */
    private Element nowElement;
    /**
     * 当前查找元素名称
     */
    private String nowElementName = "";

    /**
     * 环境集合
     */
    private HashMap<String, String> environmentMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
    /**
     * 当前执行接口的环境
     */
    private String actionEnvironment = "";

    /**
     * 根据xml文件对象，解析接口信息xml文件，并设置接口执行环境及接口默认执行环境
     *
     * @param xmlFile xml文件类对象
     */
    public ReadInterfaceFromXml(File xmlFile) {
        try {
            rootElement = new SAXReader().read(xmlFile).getRootElement();
            this.xmlFile = xmlFile;
        } catch (DocumentException e) {
            throw new InterfaceReadToolsException(String.format("xml文件存在异常，无法解析。文件路径为：%s", xmlFile.getAbsolutePath()),
                    e);
        } catch (NullPointerException e) {
            throw new InterfaceReadToolsException("文件类对象为null");
        }

        // 读取环境集合
        Element environmentsElement = rootElement.element(XmlParamName.XML_LABEL_ENVIRONMENTS);
        List<Element> environmentElementList = Optional.ofNullable(environmentsElement).map(Element::elements)
                .orElseGet(ArrayList::new);
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
            String defaultenvironmentName = environmentsElement.attributeValue(XmlParamName.XML_ATTRI_DEFAULT);
            setActionEnvironment(environmentMap.containsKey(defaultenvironmentName) ? defaultenvironmentName
                    : environmentElementList.get(0).attributeValue(XmlParamName.XML_ATTRI_NAME));
        }
    }

    @Override
    public InterfaceInfo getInterface(String interName) {
        // 查找元素，并判断其是否指定环境属性
        Element interElement = findElement(interName);
        String environment = Optional.ofNullable(interElement.attributeValue(XmlParamName.XML_ATTRI_ENVIRONMENT))
                .filter(environmentMap::containsKey).map(environmentMap::get).orElse(actionEnvironment);
        return raedInterInfo(interName, environment);
    }

    /**
     * 该方法用于读取接口的响应报文格式信息，并存储至接口信息类对象中
     *
     * @param inter        接口信息类对象
     * @param interElement 接口元素
     * @since autest 3.3.0
     */
    private void readResponseTypes(InterfaceInfo inter, Element interElement) {
        // 获取响应标签
        List<Element> responeTypeElementList = Optional
                .ofNullable(interElement.element(XmlParamName.XML_LABEL_RESPONSE))
                .map(ele -> ele.element(XmlParamName.XML_LABEL_RESPONSE_TYPES))
                .map(ele -> ele.elements(XmlParamName.XML_LABEL_RESPONSE_TYPE)).orElseGet(() -> new ArrayList<>());
        // 遍历所有的响应格式标签
        for (Element responeTypeElement : responeTypeElementList) {
            // 解析状态码，若状态码无法解析，则不进行存储
            try {
                int state = Integer.valueOf(responeTypeElement.attributeValue(XmlParamName.XML_ATTRI_STATUS));
                String responeType = Optional.ofNullable(responeTypeElement.attributeValue(XmlParamName.XML_ATTRI_TYPE))
                        .filter(type -> !type.isEmpty()).map(String::toUpperCase).orElse("");
                if (responeType.isEmpty()) {
                    continue;
                }
                // 将响应报文格式进行转换，之后存储相应的内容
                inter.addResponseContentTypeSet(state, MessageType.valueOf(responeType));
            } catch (IllegalArgumentException e) {
                continue;
            }
        }
    }

    /**
     * 该方法用于返回响应报文的字符集格式
     *
     * @param interElement 接口元素类对象
     * @return 响应报文的字符集格式
     * @since autest 3.3.0
     */
    private String readCharsetname(Element interElement) {
        return Optional.ofNullable(interElement.element(XmlParamName.XML_LABEL_RESPONSE))
                .map(ele -> ele.attributeValue(XmlParamName.XML_ATTRI_CHARSET)).orElseGet(() -> "");
    }

    /**
     * 该方法用于返回接口的路径信息
     *
     * @param interElement 接口元素
     * @return 接口的路径信息
     * @since autest 3.3.0
     */
    private String readInterPath(Element interElement) {
        return Optional.ofNullable(interElement.attributeValue(XmlParamName.XML_ATTRI_PATH)).orElse("");
    }

    /**
     * 该方法用于读取接口的请求体信息
     *
     * @param interElement 接口元素
     * @return 请求体信息
     * @since autest 3.3.0
     */
    private void readInterBody(InterfaceInfo inter, Element interElement) {
        // 获取模板中普通body元素
        Element bodyElement = interElement.element(XmlParamName.XML_LABEL_BODY);
        // 若元素存在，则按照普通body进行读取
        if (bodyElement != null) {
            // 获取元素的文本节点
            String bodyText = Optional.ofNullable(bodyElement.getText()).orElse("");
            // 若获取到的文本内容为空，则判断其是否包含file属性，若存在，则读取其内容
            if (bodyText.isEmpty()) {
                // 获取文件路径属性，并读取文件中的内容，若读取的内容存在问题，则设置bodyText为空
                String filePath = Optional.ofNullable(bodyElement.attributeValue(XmlParamName.XML_ATTRI_FILE))
                        .orElse("");
                if (!filePath.isEmpty()) {
                    try {
                        bodyText = TextFileReadUtil.megerTextToTxt(new File(filePath), true);
                    } catch (AuxiliaryToolsException e) {
                        bodyText = "";
                    }
                }
            }

            // 获取消息类型属性
            String messageTypeText = Optional.ofNullable(bodyElement.attributeValue(XmlParamName.XML_ATTRI_TYPE))
                    .map(String::toUpperCase).orElse("");
            // 消息类型转换为消息类型枚举，若转换失败，则按照文本格式进行识别
            try {
                inter.setBodyContent(MessageType.valueOf(messageTypeText), bodyText);
            } catch (Exception e) {
                inter.setBody(bodyText);
            }

            return;
        }

        // 若不存在普通body，则读取文本body类型
        if ((bodyElement = interElement.element(XmlParamName.XML_LABEL_FILE_BODY)) != null) {
            String filePath = Optional.ofNullable(bodyElement.attributeValue(XmlParamName.XML_ATTRI_FILE)).orElse("");
            // 若文件路径不为空，则定义文件类对象
            if (!filePath.isEmpty()) {
                File file = new File(filePath);
                // 若文件存在，且文件非文件夹路径，则设置在接口信息中设置相应的内容
                if (file.exists() && file.isFile()) {
                    inter.setBodyContent(MessageType.FILE, file);
                    return;
                }
            }
        }

        // 若不存在标签，或内容未通过判断，则添加请求体为none类型
        inter.setBodyContent(MessageType.NONE, "");
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
        List<Element> headerElementList = Optional.ofNullable(interElement.element(XmlParamName.XML_LABEL_HEADERS))
                .map(ele -> ele.elements(XmlParamName.XML_LABEL_HEADER)).orElseGet(() -> new ArrayList<>());
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
        List<Element> paramElementList = Optional.ofNullable(interElement.element(XmlParamName.XML_LABEL_PARAMS))
                .map(ele -> ele.elements(XmlParamName.XML_LABEL_PARAM)).orElseGet(() -> new ArrayList<>());
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
    private String readInterUrl(Element interElement) {
        return interElement.attributeValue(XmlParamName.XML_ATTRI_URL);
    }

    @Override
    public Set<String> getExtractContent(String interName) {
        // 查找元素
        Element element = findElement(interName);
        // 获取断言标签集合
        List<Element> extractElementList = Optional.ofNullable(element.element(XmlParamName.XML_LABEL_RESPONSE))
                .map(ele -> ele.element(XmlParamName.XML_LABEL_EXTRACTS))
                .map(ele -> ele.elements(XmlParamName.XML_LABEL_EXTRACT)).orElseGet(() -> new ArrayList<>());

        // 根据标签属性，生成断言json
        Set<String> extractSet = new HashSet<>();
        for (Element extractElement : extractElementList) {
            String saveName = extractElement.attributeValue(XmlParamName.XML_ATTRI_SAVE_NAME);
            // 判断当前是否存在断言内容属性，若不存在，则不进行存储
            if (saveName == null || saveName.isEmpty()) {
                continue;
            }

            // 存储断言信息，生成断言json
            JSONObject extractJson = new JSONObject();
            extractJson.put(ExtractResponse.JSON_EXTRACT_SAVE_NAME, saveName);
            extractJson.put(ExtractResponse.JSON_EXTRACT_SEARCH,
                    Optional.ofNullable(extractElement.attributeValue(XmlParamName.XML_ATTRI_SEARCH))
                            .map(String::toUpperCase).orElseGet(() -> DEFAULT_SEARCH));
            extractJson.put(ExtractResponse.JSON_EXTRACT_LB,
                    Optional.ofNullable(extractElement.attributeValue(XmlParamName.XML_ATTRI_LB)).orElseGet(() -> ""));
            extractJson.put(ExtractResponse.JSON_EXTRACT_RB,
                    Optional.ofNullable(extractElement.attributeValue(XmlParamName.XML_ATTRI_RB)).orElseGet(() -> ""));
            extractJson.put(ExtractResponse.JSON_EXTRACT_PARAM_NAME, Optional
                    .ofNullable(extractElement.attributeValue(XmlParamName.XML_ATTRI_PARAMNAME)).orElseGet(() -> ""));
            extractJson.put(ExtractResponse.JSON_EXTRACT_XPATH, Optional
                    .ofNullable(extractElement.attributeValue(XmlParamName.XML_ATTRI_XPATH)).orElseGet(() -> ""));
            extractJson.put(ExtractResponse.JSON_EXTRACT_ORD,
                    Optional.ofNullable(extractElement.attributeValue(XmlParamName.XML_ATTRI_ORD))
                            .orElseGet(() -> DEFAULT_ORD));

            extractSet.add(extractJson.toJSONString());
        }

        return extractSet;
    }

    @Override
    public Set<String> getAssertContent(String interName) {
        // 查找元素
        Element element = findElement(interName);
        // 获取断言标签集合
        List<Element> assertElementList = Optional.ofNullable(element.element(XmlParamName.XML_LABEL_RESPONSE))
                .map(ele -> ele.element(XmlParamName.XML_LABEL_ASSERTS))
                .map(ele -> ele.elements(XmlParamName.XML_LABEL_ASSERT)).orElseGet(() -> new ArrayList<>());

        // 根据标签属性，生成断言json，并存储至集合中
        Set<String> assertSet = new HashSet<>();
        for (Element assertElement : assertElementList) {
            String assertValue = assertElement.attributeValue(XmlParamName.XML_ATTRI_ASSERT_REGEX);
            // 判断当前是否存在断言内容属性，若不存在，则不进行存储
            if (assertValue == null) {
                continue;
            }

            // 存储断言信息，生成断言json
            JSONObject assertJson = new JSONObject();
            assertJson.put(AssertResponse.JSON_ASSERT_ASSERT_REGEX, assertValue);
            assertJson.put(AssertResponse.JSON_ASSERT_SEARCH,
                    Optional.ofNullable(assertElement.attributeValue(XmlParamName.XML_ATTRI_SEARCH))
                            .map(String::toUpperCase).orElseGet(() -> DEFAULT_SEARCH));
            assertJson.put(AssertResponse.JSON_ASSERT_LB, Optional
                    .ofNullable(assertElement.attributeValue(XmlParamName.XML_ATTRI_LB)).orElseGet(() -> ""));
            assertJson.put(AssertResponse.JSON_ASSERT_RB,
                    Optional.ofNullable(assertElement.attributeValue(XmlParamName.XML_ATTRI_RB)).orElseGet(() -> ""));
            assertJson.put(AssertResponse.JSON_ASSERT_PARAM_NAME, Optional
                    .ofNullable(assertElement.attributeValue(XmlParamName.XML_ATTRI_PARAMNAME)).orElseGet(() -> ""));
            assertJson.put(AssertResponse.JSON_ASSERT_XPATH, Optional
                    .ofNullable(assertElement.attributeValue(XmlParamName.XML_ATTRI_XPATH)).orElseGet(() -> ""));
            assertJson.put(AssertResponse.JSON_ASSERT_ORD, Optional
                    .ofNullable(assertElement.attributeValue(XmlParamName.XML_ATTRI_ORD)).orElseGet(() -> DEFAULT_ORD));

            assertSet.add(assertJson.toJSONString());
        }

        return assertSet;
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
        return raedInterInfo(interName,
                Optional.ofNullable(environmentMap.get(environmentName)).orElse(actionEnvironment));
    }

    /**
     * 该方法用于根据指定的接口名称与执行的环境，构造相应的接口信息读取类对象
     * 
     * @param interName   接口名称
     * @param environment 接口执行环境
     * @return 接口信息类对象
     * @since autest 3.4.0
     */
    private InterfaceInfo raedInterInfo(String interName, String environment) {
        if (interName == null || interName.isEmpty()) {
            throw new InterfaceReadToolsException("指定的接口名称为空或未指定接口名称：" + interName);
        }

        // 判断该接口是否已缓存，若存在缓存，则直接返回缓存信息
        if (interfaceMap.containsKey(interName)) {
            InterfaceInfo inter = interfaceMap.get(interName).clone();
            inter.analysisUrl(environment);
            return inter;
        }

        // 若未缓存信息，则构造接口信息对象，添加接口信息
        InterfaceInfo inter = new InterfaceInfo();
        // 解析环境，获取环境主机等信息
        inter.analysisUrl(environment);

        // 查找元素
        Element interElement = findElement(interName);
        // 获取接口的url
        inter.analysisUrl(readInterUrl(interElement));
        // 获取接口的路径
        String path = readInterPath(interElement);
        if (!path.isEmpty()) {
            inter.setPath(path);
        }
        // 获取接口的请求方式
        inter.setRequestType(readInterRequestType(interElement));
        // 获取接口参数信息
        readParams(inter, interElement);
        // 设置请求头信息
        readHeader(inter, interElement);
        // 读取请求体信息
        readInterBody(inter, interElement);
        // 读取响应体字符集
        inter.setCharsetname(readCharsetname(interElement));
        // 读取接口不同状态的响应报文格式
        readResponseTypes(inter, interElement);
        // 读取接口断言规则信息
        inter.addAllAssertRule(getAssertContent(interName));
        // 读取接口提词规则信息
        inter.addAllExtractRule(getExtractContent(interName));
        // 添加前置操作集合
        inter.addAllBeforeOperation(getBeforeOperation(interName));

        // 缓存读取的接口
        interfaceMap.put(interName, inter);
        return inter;
    }

    @Override
    public List<String> getParentInterfaceName(String interName) {
        // 查找元素
        Element element = findElement(interName);
        // 获取所有的前置接口名称
        List<Element> beforeElementList = Optional.ofNullable(element.element(XmlParamName.XML_LABEL_BEFORE))
                .map(ele -> ele.elements(XmlParamName.XML_LABEL_INTERFACE)).orElseGet(() -> new ArrayList<>());

        // 遍历标签，并存储相应不为空的接口名称
        List<String> interNameList = new ArrayList<>();
        for (Element beforeElement : beforeElementList) {
            String name = beforeElement.attributeValue(XmlParamName.XML_ATTRI_NAME);
            if (name != null && !name.isEmpty()) {
                interNameList.add(name);
            }
        }

        return interNameList;
    }

    @Override
    public List<BeforeInterfaceOperation> getBeforeOperation(String interName) {
        List<BeforeInterfaceOperation> operationList = new ArrayList<>();

        // 查找元素
        Element element = findElement(interName);
        // 获取所有前置操作标签并进行遍历
        List<Element> beforeElementList = Optional.ofNullable(element.element(XmlParamName.XML_LABEL_BEFORE))
                .map(Element::elements).orElseGet(ArrayList::new);
        for (Element beforeElement : beforeElementList) {
            // 根据标签名称，定义相应的前置操作封装类
            switch (beforeElement.getName()) {
            case XmlParamName.XML_LABEL_INTERFACE:
                operationList.add(getBeforeInterface(interName, beforeElement));
                break;
            default:
                continue; // 若非指定的封装类，则不进行解析
            }
        }

        return operationList;
    }

    /**
     * 该方法用于返回包含接口信息的前置操作封装类
     * 
     * @param interName 接口名称
     * @return 前置操作封装类
     * @since autest 3.6.0
     */
    private BeforeInterfaceOperation getBeforeInterface(String interName, Element beforeElement) {
        String name = beforeElement.attributeValue(XmlParamName.XML_ATTRI_NAME);
        try {
            return new BeforeInterfaceOperation(getInterface(name));
        } catch (InterfaceReadToolsException e) {
            throw new InterfaceReadToolsException(String.format("接口“%s”不存在相应的父层接口“%s”", interName, name), e);
        }
    }

    /**
     * 该方法用于通过元素名称查找相应的接口元素
     *
     * @param interName 元素名称
     * @return 元素名称对应的接口元素
     * @since autest 3.3.0
     */
    private Element findElement(String interName) {
        // 若指定的接口名称为空，则抛出异常
        if (interName == null || interName.isEmpty()) {
            throw new InterfaceReadToolsException("未指定元素名称，无法查找元素");
        }

        // 若指定的接口名称与当前缓存的接口名称相同，则直接返回缓存的元素类对象
        if (interName.equals(nowElementName)) {
            return nowElement;
        }

        // 定义查找接口的xpath，并获取接口元素
        String elementXpath = String.format("//%s/%s[@%s='%s']", XmlParamName.XML_LABEL_INTERFACES,
                XmlParamName.XML_LABEL_INTERFACE, XmlParamName.XML_ATTRI_NAME, interName);
        Element interElement = (Element) rootElement.selectSingleNode(elementXpath);
        // 若未查找接口元素，则抛出异常
        if (interElement == null) {
            throw new InterfaceReadToolsException(
                    String.format("接口元素“%s”在文件中不存在：%s", interName, xmlFile.getAbsolutePath()));
        }

        // 将缓存元素指向当前元素
        nowElementName = interName;
        nowElement = interElement;

        return interElement;
    }

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
         * 定义response标签名称
         */
        public static final String XML_LABEL_RESPONSE = "response";
        /**
         * 定义responseTypes标签名称
         */
        public static final String XML_LABEL_RESPONSE_TYPES = "responseTypes";
        /**
         * 定义responseType标签名称
         */
        public static final String XML_LABEL_RESPONSE_TYPE = "responseType";
        /**
         * 定义asserts标签名称
         */
        public static final String XML_LABEL_ASSERTS = "asserts";
        /**
         * 定义assert标签名称
         */
        public static final String XML_LABEL_ASSERT = "assert";
        /**
         * 定义extract标签名称
         */
        public static final String XML_LABEL_EXTRACT = "extract";
        /**
         * 定义extracts标签名称
         */
        public static final String XML_LABEL_EXTRACTS = "extracts";
        /**
         * 定义before标签名称
         */
        public static final String XML_LABEL_BEFORE = "before";
        /**
         * 定义file_body标签名称
         */
        public static final String XML_LABEL_FILE_BODY = "file_body";

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
        /**
         * 定义charset属性名称
         */
        public static final String XML_ATTRI_CHARSET = "charset";
        /**
         * 定义status属性名称
         */
        public static final String XML_ATTRI_STATUS = "status";
        /**
         * 定义assertValue属性名称
         */
        public static final String XML_ATTRI_ASSERT_REGEX = "assertRegex";
        /**
         * 定义saveName属性名称
         */
        public static final String XML_ATTRI_SAVE_NAME = "saveName";
        /**
         * 定义search属性名称
         */
        public static final String XML_ATTRI_SEARCH = "search";
        /**
         * 定义lb属性名称
         */
        public static final String XML_ATTRI_RB = "rb";
        /**
         * 定义rb属性名称
         */
        public static final String XML_ATTRI_LB = "lb";
        /**
         * 定义paramName属性名称
         */
        public static final String XML_ATTRI_PARAMNAME = "paramName";
        /**
         * 定义xpath属性名称
         */
        public static final String XML_ATTRI_XPATH = "xpath";
        /**
         * 定义ord属性名称
         */
        public static final String XML_ATTRI_ORD = "ord";
        /**
         * 定义environment属性名称
         */
        public static final String XML_ATTRI_ENVIRONMENT = "environment";
        /**
         * 定义file属性名称
         */
        public static final String XML_ATTRI_FILE = "file";
    }
}
