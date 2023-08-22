package com.auxiliary.http;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.auxiliary.testcase.TestCaseException;
import com.auxiliary.tool.common.Entry;
import com.auxiliary.tool.date.TimeUnit;
import com.auxiliary.tool.regex.ConstType;

/**
 * <p>
 * <b>文件名：InterfaceInfo.java</b>
 * </p>
 * <p>
 * <b>用途：</b> 存储接口的基本信息
 * </p>
 * <p>
 * <b>编码时间：2022年4月12日 上午8:37:18
 * </p>
 * <p>
 * <b>修改时间：2022年4月12日 上午8:37:18
 * </p>
 *
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 3.3.0
 */
public class InterfaceInfo implements Cloneable {
    /**
     * 定义默认接口协议
     *
     * @since autest 3.3.0
     */
    public static final String DEFAULT_PROTOCOL = "http://";
    /**
     * 定义默认主机
     *
     * @since autest 3.3.0
     */
    public static final String DEFAULT_HOST = "127.0.0.1";
    /**
     * 定义默认主机端口
     *
     * @since autest 3.3.0
     */
    public static final int DEFAULT_PORT = 80;
    /**
     * 定义接口默认请求方式
     *
     * @since autest 3.3.0
     */
    public static final RequestType DEFAULT_REQUESTTYPE = RequestType.GET;
    /**
     * 定义响应内容默认字符集
     *
     * @since autest 3.3.0
     */
    public static final String DEFAULT_CHARSETNAME = "UTF-8";
    /**
     * 定义默认接口请求超时时间
     *
     * @since autest 4.4.0
     */
    public static final Entry<Long, TimeUnit> DEFAULT_CONNECT_TIME = new Entry<>(15L, TimeUnit.SECOND);

    /**
     * 定义接口主机与协议间分隔的符号
     *
     * @since autest 3.3.0
     */
    protected final String SYMBOL_SPLIT_PROTOCOL = "://";
    /**
     * 定义接口主机与主机间的分隔符号
     *
     * @since autest 3.3.0
     */
    protected final String SYMBOL_SPLIT_PORT = ":";
    /**
     * 定义接口路径与接口主机间的分隔符号
     *
     * @since autest 3.3.0
     */
    protected final String SYMBOL_SPLIT_PATH = "/";
    /**
     * 定义接口起始参数与接口路径间的分隔符号
     *
     * @since autest 3.3.0
     */
    protected final String SYMBOL_SPLIT_START_PARAM = "?";
    /**
     * 定义接口参数键值之间的分隔符号
     *
     * @since autest 3.3.0
     */
    protected final String SYMBOL_SPLIT_PARAM_VALUE = "=";
    /**
     * 定义cookies键值之间的分隔符号
     *
     * @since autest 3.3.0
     */
    protected final String SYMBOL_SPLIT_COOKIES_VALUE = "=";
    /**
     * 定义接口参数与参数间的分隔符号
     *
     * @since autest 3.3.0
     */
    protected final String SYMBOL_SPLIT_PARAM = "&";
    /**
     * 定义cookies间的分隔符号
     *
     * @since autest 3.3.0
     */
    protected final String SYMBOL_SPLIT_COOKIES = ";";

    /**
     * 接口协议
     *
     * @since autest 3.3.0
     */
    protected String protocol = "";
    /**
     * 主机
     *
     * @since autest 3.3.0
     */
    protected String host = "";
    /**
     * 主机端口号
     *
     * @since autest 3.3.0
     */
    protected int port = -1;
    /**
     * 接口地址
     */
    protected String path = "";
    /**
     * 请求类型
     *
     * @since autest 3.3.0
     */
    protected RequestType requestType = null;
    /**
     * 接口请求参数
     *
     * @since autest 3.3.0
     */
    protected HashMap<String, String> paramMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
    /**
     * 接口请求体
     *
     * @since autest 3.3.0
     */
    protected Entry<MessageType, Object> body = null;
    /**
     * 接口请求头
     *
     * @since autest 3.3.0
     */
    protected HashMap<String, String> requestHeaderMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
    /**
     * 接口相应字符集
     *
     * @since autest 3.3.0
     */
    protected String charsetname = "";
    /**
     * 接口响应内容格式集合
     *
     * @since autest 3.3.0
     */
    protected HashMap<Integer, HashSet<MessageType>> responseContentTypeMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
    /**
     * 接口响应报文断言规则集合
     *
     * @since autest 3.3.0
     */
    protected HashSet<JSONObject> assertRuleSet = new HashSet<>(ConstType.DEFAULT_MAP_SIZE);
    /**
     * 接口响应报文提词规则
     *
     * @since autest 3.3.0
     */
    protected HashSet<JSONObject> extractRuleSet = new HashSet<>(ConstType.DEFAULT_MAP_SIZE);
    /**
     * 存储接口前置操作内容
     *
     * @since autest 3.3.0
     */
    protected ArrayList<BeforeInterfaceOperation> beforeOperationList = new ArrayList<>();
    /**
     * 存储cookie
     *
     * @since autest 3.3.0
     */
    protected HashMap<String, String> cookieMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
    /**
     * 存储接口的请求时间
     *
     * @since autest 3.3.0
     */
    protected Entry<Long, TimeUnit> connectTime = null;

    /**
     * 该方法用于返回接口的url协议
     *
     * @return url协议
     * @since autest 3.3.0
     */
    public String getProtocol() {
        // 若未存储协议信息，则返回默认协议
        if (protocol.isEmpty()) {
            return DEFAULT_PROTOCOL;
        }

        return protocol;
    }

    /**
     * 该方法用于接口的url协议
     *
     * @param protocol url协议
     * @since autest 3.3.0
     */
    public void setProtocol(String protocol) {
        // 过滤协议为空以及只包含协议符号的情况
        this.protocol = Optional.ofNullable(protocol).filter(p -> !p.isEmpty()).filter(p -> p.matches(".+:\\/\\/"))
                .orElseGet(() -> "");
    }

    /**
     * 该方法用于返回接口的主机地址或域名
     *
     * @return 主机地址或域名
     * @since autest 3.3.0
     */
    public String getHost() {
        if (host.isEmpty()) {
            return DEFAULT_HOST;
        }

        return host;
    }

    /**
     * 该方法用于设置接口的主机地址或域名
     *
     * @param host 接口的主机地址或域名
     * @since autest 3.3.0
     */
    public void setHost(String host) {
        this.host = Optional.ofNullable(host).filter(h -> !h.isEmpty()).map(h -> {
            // 判断主机或域名最后一位是否为/，若是，则进行去除
            if (h.lastIndexOf(SYMBOL_SPLIT_PATH) == h.length() - 1) {
                h = h.substring(0, h.length() - 1);
            }

            // 若设置的主机包含协议分隔符，则将协议设置到接口后，再在主机中内容进行去除
            if (h.contains(SYMBOL_SPLIT_PROTOCOL)) {
                setProtocol(h.substring(0, h.indexOf(SYMBOL_SPLIT_PROTOCOL) + SYMBOL_SPLIT_PROTOCOL.length() + 1));
                h = h.substring(h.indexOf(SYMBOL_SPLIT_PROTOCOL) + SYMBOL_SPLIT_PROTOCOL.length());
            }
            return h;
        }).orElse("");
    }

    /**
     * 该方法用于返回主机端口
     *
     * @return 主机端口
     * @since autest 3.3.0
     */
    public int getPort() {
        if (port == -1) {
            return DEFAULT_PORT;
        }

        return port;
    }

    /**
     * 该方法用于设置主机端口
     *
     * @param port 主机端口
     * @since autest 3.3.0
     */
    public void setPort(int port) {
        if (port <= 0 || port >= 65535) {
            return;
        }

        this.port = port;
    }

    /**
     * 该方法用于返回接口的路径
     *
     * @return 接口的路径
     * @since autest 3.3.0
     */
    public String getPath() {
        return path;
    }

    /**
     * 该方法用于设置接口的路径，可传入空串，表示无接口路径
     *
     * @param path 接口的路径
     * @since autest 3.3.0
     */
    public void setPath(String path) {
        this.path = Optional.ofNullable(path).map(p -> {
            // 判断接口第一位是否为/，若不是，则添加
            if (p.indexOf(SYMBOL_SPLIT_PATH) != 0) {
                p = SYMBOL_SPLIT_PATH + p;
            }
            // 判断接口路径的最后一位是否为/，若是，则进行去除
            if (p.lastIndexOf(SYMBOL_SPLIT_PATH) == p.length() - 1) {
                p = p.substring(0, p.length() - 1);
            }
            return p;
        }).orElse("");
    }

    /**
     * 该方法用于解析传入的url，并根据url的分隔规则，将接口信息进行识别，并存入到相应的参数中
     * <p>
     * 方法将传入的url按如下的顺序进行识别：
     * <ol>
     * <li>当识别到“?”符号时，则将其后的内容识别为参数表达式存储至参数列表中</li>
     * <li>当识别到“://”符号时，则将其前的数据识别为协议</li>
     * <li>当识别到“/”符号时，则将其后的内容识别为接口地址</li>
     * <li>当识别到“:”符号，则将其后的内容识别为端口号</li>
     * <li>识别完上述内容后，则将剩余的字符串识别为主机IP或域名</li>
     * </ol>
     * </p>
     * <p>
     * <b>注意：</b>该方法亦可仅传入url的一部分，用于设置已存储的接口信息，但传入非主机内容时，需要带该参数的分隔符号，
     * 否则传入的内容将会被识别为主机或域名（例如，仅传入ftp协议时，则传参必须是“ftp://”）
     * </p>
     *
     * @param url 接口请求url
     * @since autest 3.3.0
     */
    public void analysisUrl(String url) {
        if (url == null || url.isEmpty()) {
            return;
        }

        // 将传入接口的所有中文符号替换成英文符号，并存储
        StringBuilder urlText = new StringBuilder(
                url.replaceAll("：", SYMBOL_SPLIT_PORT).replaceAll("？", SYMBOL_SPLIT_START_PARAM).replaceAll("。·", "."));
        int index = -1;

        // 解析url中的参数，若存在参数分隔符，则在urlText去除，并将表达式传入相应的内容中
        if ((index = urlText.indexOf(SYMBOL_SPLIT_START_PARAM)) > -1) {
            addParam(urlText.substring(index));
            urlText.delete(index, urlText.length());
        }

        // 解析协议参数，若存在协议分隔符，则在urlText去除，并将协议传入相应的内容中
        if ((index = urlText.indexOf(SYMBOL_SPLIT_PROTOCOL)) > -1) {
            // 从第一位起，分离协议，并存储分离后的内容
            setProtocol(urlText.substring(0, index + SYMBOL_SPLIT_PROTOCOL.length()));
            urlText.delete(0, index + SYMBOL_SPLIT_PROTOCOL.length());
        }

        // 解析接口路径参数，若存在接口路径分隔符，则在urlText去除，并将路径传入相应的内容中
        if ((index = urlText.indexOf(SYMBOL_SPLIT_PATH)) > -1) {
            setPath(urlText.substring(index));
            urlText.delete(index, urlText.length());
        }

        // 判断剩余部分是否符合IP + 端口的正则，若不符合，则全部将其设置为接口路径
        // 由于部分接口路径存在编写时不带“/”的情况，为避免截取错误，故加上个判断进行弥补
//        if (!urlText.toString().matches("(\\d{1,3}\\.){3}\\d{1,3}(:\\d{1,5})?")) {
//            setPath(urlText.toString() + getPath());
//            urlText.delete(0, urlText.length());
//        }

        // 解析主机端口，若存在主机端口分隔符，则在urlText去除，并将端口转换后传入相应的内容中
        if ((index = urlText.indexOf(SYMBOL_SPLIT_PORT)) > -1) {
            try {
                setPort(Integer.valueOf(urlText.substring(index).replaceAll(SYMBOL_SPLIT_PORT, "")));
                urlText.delete(index, urlText.length());
            } catch (NumberFormatException e) {
            }
        }

        if (!urlText.toString().isEmpty()) {
            setHost(urlText.toString());
        }
    }

    /**
     * 该方法用于返回接口的请求方式枚举
     *
     * @return 请求方式枚举
     * @since autest 3.3.0
     */
    public RequestType getRequestType() {
        if (requestType == null) {
            return DEFAULT_REQUESTTYPE;
        }

        return requestType;
    }

    /**
     * 该方法用于设置接口的请求方式枚举
     *
     * @param requestType 接口的请求方式枚举
     * @since autest 3.3.0
     */
    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    /**
     * 该方法用于返回接口的请求参数（传参）
     *
     * @return 接口的请求参数
     * @since autest 3.3.0
     */
    @SuppressWarnings("unchecked")
    public Map<String, String> getParamMap() {
        return (Map<String, String>) paramMap.clone();
    }

    /**
     * 该方法用于返回是否存在参数数据
     *
     * @return 是否存在参数数据
     * @since autest 3.6.0
     */
    public boolean isEmptyParam() {
        return paramMap.isEmpty();
    }

    /**
     * 该方法用于添加一组请求参数（传参）
     *
     * @param paramMap 请求参数集合
     * @since autest 3.3.0
     */
    public void addParams(Map<String, String> paramMap) {
        // 过滤掉map为空的情况
        Optional.ofNullable(paramMap).filter(map -> !map.isEmpty()).ifPresent(map -> {
            map.forEach(this::addParam);
        });
    }

    /**
     * 该方法用于添加单个请求参数（传参）
     *
     * @param paramName  参数名称
     * @param paramValue 参数值
     * @since autest 3.3.0
     */
    public void addParam(String paramName, String paramValue) {
        // 过滤参数名称为空的情况，并且当参数值为null时，则处理为空串
        Optional.ofNullable(paramName).filter(key -> !key.isEmpty()).ifPresent(key -> {
            paramMap.put(key, Optional.ofNullable(paramValue).orElseGet(() -> ""));
        });
    }

    /**
     * 该方法用于根据url传入参数的表达式格式，向接口中添加参数
     * <p>
     * <b>注意：</b>表达式的判断的正则格式为“[\?？]?\w+=\w*(&\w+=\w*)*”，若未按规则编写，则无法添加参数
     * </p>
     *
     * @param expression 参数表达式
     * @since autest 3.3.0
     */
    public void addParam(String expression) {
        // 过滤不符合正则的表达式，之后按照分隔符号切分，得到每个参数的表达式后，再按照参数与值的符号切分
        Optional.ofNullable(expression).filter(exp -> exp.matches("[\\?？]?\\w+=\\w*(&\\w+=\\w*)*"))
                .map(exp -> (exp.indexOf("?") == 0 || exp.indexOf("？") == 0) ? exp.substring(1) : exp)
                .map(exp -> exp.split(SYMBOL_SPLIT_PARAM)).map(Arrays::stream)
                .ifPresent(expStream -> expStream.filter(pramExp -> pramExp.contains(SYMBOL_SPLIT_PARAM_VALUE))
                        .map(pramExp -> pramExp.split(SYMBOL_SPLIT_PARAM_VALUE)).forEach(params -> {
                            if (params.length == 1) {
                                addParam(params[0], "");
                            } else {
                                addParam(params[0], params[1]);
                            }
                        }));
    }

    /**
     * 该方法用于清空存储的所有参数内容
     *
     * @return 原有的参数内容
     * @since autest 3.4.0
     */
    public Map<String, String> clearParam() {
        Map<String, String> paramMap = getParamMap();
        this.paramMap.clear();
        return paramMap;
    }

    /**
     * 该方法用于返回接口的请求报文与请求报文类型封装类
     * <p>
     * 封装类类似于于键值对，键为报文类型，值为报文内容
     * </p>
     *
     * @return 接口的请求报文与请求报文类型封装类
     * @since autest 3.4.0
     */
    public Entry<MessageType, Object> getBodyContent() {
        return Optional.ofNullable(body).orElseGet(() -> new Entry<>(MessageType.NONE, ""));
    }

    /**
     * 该方法用于设置接口的请求体，根据报文的内容，自动设置相应的报文类型
     * <p>
     * <b>注意：</b>报文格式自动识别可能存在错误，若不符合预期，可调用{@link #setBodyContent(MessageType, Object)}方法对请求报文类型进行设置
     * </p>
     *
     * @param bodyText 接口的请求体
     * @since autest 3.3.0
     */
    public void setBody(String bodyText) {
        // 若body不为空，则根据body格式，自动设置请求头
        if (bodyText != null && !bodyText.isEmpty()) {
            try {
                // 判断文本是否能被识别为json格式
                JSONObject.parseObject(bodyText);
                setBodyContent(MessageType.JSON, bodyText);
            } catch (JSONException jsonException) {
                try {
                    DocumentHelper.parseText(bodyText);
                    // 若相应参数开头的文本为<html>，则表示其相应参数为html形式，则以html形式进行转换
                    if (bodyText.indexOf("<html>") == 0) {
                        setBodyContent(MessageType.HTML, bodyText);
                    } else {
                        setBodyContent(MessageType.XML, bodyText);
                    }

                } catch (DocumentException domException) {
                    setBodyContent(MessageType.RAW, bodyText);
                    ;
                }
            }
        } else {
            setBodyContent(MessageType.NONE, "");
        }
    }

    /**
     * 该方法用于设置请求体以及请求体的类型
     *
     * @param messageType 请求体类型枚举
     * @param bodyObject  请求体内容
     * @since autest 3.4.0
     */
    @SuppressWarnings({ "unused", "unchecked" })
    public void setBodyContent(MessageType messageType, Object bodyObject) {
        // 判断请求体类型是否为表单格式
        if (messageType == MessageType.X_WWW_FORM_URLENCODED || messageType == MessageType.FORM_DATA) {
            try {
                // 表单格式必须为List<Entry<String, Object>>类对象，故对其强转，若不成功，则抛出异常
                List<Entry<String, Object>> dataList = (List<Entry<String, Object>>) bodyObject;
            } catch (ClassCastException e) {
                throw new InterfaceReadToolsException(
                        "表单类型，其请求体必须是“List<Entry<String, Object>>”类型，错误的接口信息为：" + toUrlString(), e);
            }
        }
        body = new Entry<>(messageType, bodyObject);
    }

    /**
     * 该方法用于设置表单格式类型的请求体
     * <p>
     * <b>注意：</b>表单每一个元素为一个键值对类型
     * </p>
     *
     * @param messageType 请求体类型枚举
     * @param dataList    请求体内容
     * @since autest 3.6.0
     * @throws InterfaceReadToolsException 当消息类型枚举不为表单格式或传入的表单值为错误的类型时，抛出的异常
     */
    public void setFormBody(MessageType messageType, List<Entry<String, Object>> dataList) {
        // 判断请求体类型是否为表单格式
        if (messageType != MessageType.X_WWW_FORM_URLENCODED && messageType != MessageType.FORM_DATA) {
            throw new InterfaceReadToolsException(String.format("添加表单类型请求体，其类型必须为“%s”或“%s”，错误的接口信息为：%s",
                    MessageType.X_WWW_FORM_URLENCODED.getMediaValue(), MessageType.FORM_DATA.getMediaValue(),
                    toUrlString()));
        }

        // 判断是否传入表单内容
        if (dataList == null || dataList.isEmpty()) {
            throw new InterfaceReadToolsException("表单参数不能为空");
        }

        // 将表单数据重新存储，并对每个元素的类型进行判断
        List<Entry<String, Object>> list = new ArrayList<>();
        for (Entry<String, Object> data : dataList) {
            Object value = data.getValue();
            // 判断表单的值类型，若非指定的表单类型，则抛出异常
            if (messageType == MessageType.X_WWW_FORM_URLENCODED
                    && !(value instanceof String || value instanceof Number)) {
                throw new InterfaceReadToolsException(String.format("错误的表单值类型“%s”，“%s”类型的表单值只支持字符串或数字类型，错误的接口信息为：%s",
                        value.getClass().getSimpleName(), MessageType.X_WWW_FORM_URLENCODED.getMediaValue(),
                        toUrlString()));
            }
            if (messageType == MessageType.FORM_DATA
                    && !(value instanceof String || value instanceof Number || value instanceof File)) {
                throw new InterfaceReadToolsException(String.format(
                        "错误的表单值类型“%s”，“%s”类型的表单值只支持字符串、或数字或文件类型，错误的接口信息为：%s", value.getClass().getSimpleName(),
                        MessageType.FORM_DATA.getMediaValue(), toUrlString()));
            }

            list.add(data);
        }

        setBodyContent(messageType, list);
//        body = new Entry<>(messageType, list);
    }

    /**
     * 该方法用于返回接口的请求头
     *
     * @return 接口的请求头
     * @since autest 3.3.0
     */
    @SuppressWarnings("unchecked")
    public Map<String, String> getRequestHeaderMap() {
        return (Map<String, String>) requestHeaderMap.clone();
    }

    /**
     * 该方法用于返回是否存在请求头数据
     *
     * @return 是否存在请求头
     * @since autest 3.6.0
     */
    public boolean isEmptyHeader() {
        return requestHeaderMap.isEmpty();
    }

    /**
     * 该方法用于添加一组请求头
     *
     * @param requestHeaderMap 请求头集合
     * @since autest 3.3.0
     */
    public void addRequestHeaderMap(Map<String, String> requestHeaderMap) {
        // 过滤掉map为空的情况
        Optional.ofNullable(requestHeaderMap).filter(map -> !map.isEmpty()).ifPresent(map -> {
            map.forEach(this::addRequestHeader);
        });
    }

    /**
     * 该方法用于添加单个请求头
     *
     * @param headerName  请求头名称
     * @param headerValue 请求头值
     * @since autest 3.3.0
     */
    public void addRequestHeader(String headerName, String headerValue) {
        Optional.ofNullable(headerName).filter(header -> !header.isEmpty()).ifPresent(name -> {
            requestHeaderMap.put(name, Optional.ofNullable(headerValue).orElseGet(() -> ""));
        });
    }

    /**
     * 该方法用于清空存储的所有请求头内容
     *
     * @return 原有的请求头内容
     * @since autest 3.4.0
     */
    public Map<String, String> clearRequestHeaderMap() {
        Map<String, String> requestHeaderMap = getRequestHeaderMap();
        this.requestHeaderMap.clear();
        return requestHeaderMap;
    }

    /**
     * 该方法用于返回接口响应内容的字符集编码名称
     *
     * @return 接口响应内容的字符集编码名称
     * @since autest 3.3.0
     */
    public String getCharsetname() {
        if (charsetname.isEmpty()) {
            return DEFAULT_CHARSETNAME;
        }
        return charsetname;
    }

    /**
     * 该方法用于设置接口响应内容的字符集编码名称
     *
     * @param charsetname 接口响应内容的字符集编码名称
     * @since autest 3.3.0
     */
    public void setCharsetname(String charsetname) {
        this.charsetname = Optional.ofNullable(charsetname).orElseGet(() -> "");
    }

    /**
     * 该方法用于返回接口响应内容的格式枚举，当状态码不存在时，则返回空集合
     *
     * @param status 状态码
     * @return 接口响应内容的格式枚举
     * @since autest 3.3.0
     */
    public Set<MessageType> getResponseContentType(int status) {
        return Optional.ofNullable(responseContentTypeMap.get(status)).map(HashSet::new)
                .orElseGet(() -> new HashSet<>());
    }

    /**
     * 该方法用于返回已存储响应报文格式的响应状态码集合
     *
     * @return 响应状态码集合
     * @since autest 3.3.0
     */
    public Set<Integer> getAllSaveState() {
        return responseContentTypeMap.keySet();
    }

    /**
     * 该方法用于添加接口响应体内容的格式
     * <p>
     * 格式允许添加多个，以应对在不同参数或不同响应状态下的情况
     * </p>
     *
     * @param messageTypes 响应内容格式数组
     * @since autest 3.3.0
     */
    public void addResponseContentTypeSet(int state, MessageType... messageTypes) {
        // 过滤掉数组为null或为空的情况
        Optional.ofNullable(messageTypes).filter(types -> types.length != 0).ifPresent(types -> {
            // 判断当前状态码是否存在，若不存在，则添加空集合
            if (!responseContentTypeMap.containsKey(state)) {
                responseContentTypeMap.put(state, new HashSet<>());
            }

            // 获取当前状态码中存储的状态
            HashSet<MessageType> responseContentTypeSet = responseContentTypeMap.get(state);
            // 遍历传入的内容，将其逐个添加至内容格式集合中
            Arrays.stream(types).filter(type -> type != null).forEach(responseContentTypeSet::add);
        });
    }

    /**
     * 该方法用于移除指定状态码的响应体内容
     *
     * @param status 状态码
     * @return 状态码对应的原内容
     * @since autest 3.4.0
     */
    public Set<MessageType> clearResponseContentType(int status) {
        return Optional.ofNullable(responseContentTypeMap.remove(status)).orElseGet(() -> new HashSet<>());
    }

    /**
     * 该方法用于添加断言规则json
     * <p>
     * <b>注意：</b>
     * <ol>
     * <li>传入的json为空或不符合json格式时，将不进行存储</li>
     * <li>json必须包含{@link AssertResponse#JSON_ASSERT_ASSERT_REGEX}字段，否则将不进行存储</li>
     * </ol>
     * </p>
     *
     * @param assertRuleJsonText 断言规则json
     * @since autest 3.3.0
     */
    public void addAssertRule(String assertRuleJsonText) {
        // 若传入的json格式有误，则将不进行存储
        try {
            // 若json不包含断言内容字段，则亦不进行存储
            Optional.ofNullable(assertRuleJsonText).filter(text -> !text.isEmpty()).map(JSONObject::parseObject)
                    .filter(json -> json.containsKey(AssertResponse.JSON_ASSERT_ASSERT_REGEX))
                    .ifPresent(assertRuleSet::add);
        } catch (Exception e) {
        }
    }

    /**
     * 该方法用于添加断言规则集合
     * <p>
     * <b>注意：</b>集合必须包含{@link AssertResponse#JSON_ASSERT_ASSERT_REGEX}字段，否则将不进行存储
     * </p>
     *
     * @param assertRuleMap 断言规则集合
     * @since autest 3.3.0
     */
    public void addAssertRule(Map<String, String> assertRuleMap) {
        // 判断集合是否存在断言内容字段
        if (assertRuleMap == null || !assertRuleMap.containsKey(AssertResponse.JSON_ASSERT_ASSERT_REGEX)) {
            return;
        }

        JSONObject json = new JSONObject();
        assertRuleMap.forEach(json::put);
        assertRuleSet.add(json);
    }

    /**
     * 该方法用于添加多组断言规则json
     * <p>
     * <b>注意：</b>集合必须包含{@link AssertResponse#JSON_ASSERT_ASSERT_REGEX}字段，否则将不进行存储
     * </p>
     *
     * @param rules 断言规则json集合
     * @since autest 3.3.0
     */
    public void addAllAssertRule(Collection<String> rules) {
        rules.stream().filter(rule -> rule != null).map(rule -> {
            try {
                return JSONObject.parseObject(rule);
            } catch (Exception e) {
                return new JSONObject();
            }
        }).filter(json -> json.containsKey(AssertResponse.JSON_ASSERT_ASSERT_REGEX)).forEach(assertRuleSet::add);
    }

    /**
     * 该方法用于以字符串集合的形式，返回断言规则json
     * <p>
     * json字段可参考{@link AssertResponse}中“JSON_ASSERT_XXX”常量
     * </p>
     *
     * @return 断言规则json集合
     * @since autest 3.3.0
     */
    public Set<String> getAssertRuleJson() {
        Set<String> assertRuleJsonText = new HashSet<>(ConstType.DEFAULT_MAP_SIZE);
        assertRuleSet.stream().map(json -> json.toJSONString()).forEach(assertRuleJsonText::add);

        return assertRuleJsonText;
    }

    /**
     * 该方法用于清空并返回存储的断言规则集合
     *
     * @return 断言规则集合
     * @since autest 3.4.0
     */
    public Set<String> clearAssertRuleJson() {
        Set<String> assertRuleJsonText = getAssertRuleJson();
        assertRuleSet.clear();
        return assertRuleJsonText;
    }

    /**
     * 该方法用于存储提词规则
     * <p>
     * <b>注意：</b>
     * <ol>
     * <li>传入的json为空或不符合json格式时，将不进行存储</li>
     * <li>json必须包含{@link ExtractResponse#JSON_EXTRACT_SAVE_NAME}字段，否则将不进行存储</li>
     * </ol>
     * </p>
     *
     * @param extractRuleJsonText 提词规则json文本
     * @since autest 3.3.0
     */
    public void addExtractRule(String extractRuleJsonText) {
        // 若传入的json格式有误，则将不进行存储
        try {
            // 若json不包含断言内容字段，则亦不进行存储
            Optional.ofNullable(extractRuleJsonText).filter(text -> !text.isEmpty()).map(JSONObject::parseObject)
                    .filter(json -> json.containsKey(ExtractResponse.JSON_EXTRACT_SAVE_NAME)).ifPresent(json -> {
                        // 存储规则，并在提词内容集合中，构造空值
                        extractRuleSet.add(json);
                    });
        } catch (Exception e) {
        }
    }

    /**
     * 该方法用于添加提词规则
     * <p>
     * <b>注意：</b>集合必须包含{@link ExtractResponse#JSON_EXTRACT_SAVE_NAME}字段，否则将不进行存储
     * </p>
     *
     * @param extractRuleMap 提词规则集合
     * @since autest 3.3.0
     */
    public void addExtractRule(Map<String, String> extractRuleMap) {
        // 判断集合是否存在断言内容字段
        if (extractRuleMap == null || !extractRuleMap.containsKey(ExtractResponse.JSON_EXTRACT_SAVE_NAME)) {
            return;
        }

        JSONObject json = new JSONObject();
        extractRuleMap.forEach(json::put);
        extractRuleSet.add(json);
    }

    /**
     * 该方法用于添加多组提词规则json
     * <p>
     * <b>注意：</b>集合必须包含{@link ExtractResponse#JSON_EXTRACT_SAVE_NAME}字段，否则将不进行存储
     * </p>
     *
     * @param rules 提词规则json集合
     * @since autest 3.3.0
     */
    public void addAllExtractRule(Collection<String> rules) {
        rules.stream().filter(rule -> rule != null && !rule.isEmpty()).map(rule -> {
            try {
                return JSONObject.parseObject(rule);
            } catch (Exception e) {
                return new JSONObject();
            }
        }).filter(json -> json.containsKey(ExtractResponse.JSON_EXTRACT_SAVE_NAME)).forEach(extractRuleSet::add);
    }

    /**
     * 该方法用于以字符串集合的形式，返回提词规则json
     * <p>
     * json字段可参考{@link ExtractResponse}中“JSON_EXTRACT_XXX”常量
     * </p>
     *
     * @return 提词规则json集合
     * @since autest 3.3.0
     */
    public Set<String> getExtractRuleJson() {
        Set<String> extractRuleJsonText = new HashSet<>(ConstType.DEFAULT_MAP_SIZE);
        extractRuleSet.stream().map(json -> json.toJSONString()).forEach(extractRuleJsonText::add);

        return extractRuleJsonText;
    }

    /**
     * 该方法用于清空并返回存储的提词规则集合
     *
     * @return 提词规则集合
     * @since autest 3.4.0
     */
    public Set<String> clearExtractRuleJson() {
        Set<String> extractRuleJsonText = getExtractRuleJson();
        extractRuleSet.clear();
        return extractRuleJsonText;
    }

    /**
     * 该方法用于添加前置操作方法
     *
     * @param operation 前置操作封装类对象
     * @since autest 3.6.0
     */
    public void addBeforeOperation(BeforeInterfaceOperation operation) {
        beforeOperationList.add(operation);
    }

    /**
     * 该方法用于添加一组前置操作方法
     *
     * @param operations 前置操作封装类集合
     * @since autest 3.6.0
     */
    public void addAllBeforeOperation(Collection<BeforeInterfaceOperation> operations) {
        beforeOperationList.addAll(operations);
    }

    /**
     * 该方法用于返回添加的所有前置执行操作
     *
     * @return 前置操作封装类集合
     * @since autest 3.6.0
     */
    public List<BeforeInterfaceOperation> getBeforeOperationList() {
        List<BeforeInterfaceOperation> beforeOperationList = new ArrayList<>();
        beforeOperationList.addAll(this.beforeOperationList);
        return beforeOperationList;
    }

    /**
     * 该方法用于清除添加的前置操作
     *
     * @return 前置操作封装类集合
     * @since autest 3.6.0
     */
    public List<BeforeInterfaceOperation> clearBeforeOperation() {
        List<BeforeInterfaceOperation> beforeOperationList = getBeforeOperationList();
        this.beforeOperationList.clear();
        return beforeOperationList;
    }

    /**
     * 该方法用于返回接口的需要设置的cookies
     *
     * @return cookies
     * @since autest 3.6.0
     */
    @SuppressWarnings("unchecked")
    public Map<String, String> getCookieMap() {
        return (Map<String, String>) cookieMap.clone();
    }

    /**
     * 该方法用于返回已存储的Cookie的表达式，即以“xxx=xxx;xxx=xxx”的格式返回Cookie字符串
     *
     * @return Cookie的表达式
     * @since autest 3.6.0
     */
    public String getCookieExpression() {
        StringJoiner cookieExpression = new StringJoiner(";");
        if (!cookieMap.isEmpty()) {
            cookieMap.forEach((key, value) -> cookieExpression.add(String.format("%s=%s", key, value)));
        }

        return cookieExpression.toString();
    }

    /**
     * 该方法用于返回是否存在cookie数据
     *
     * @return 是否存在cookie数据
     * @since autest 3.6.0
     */
    public boolean isEmptyCookie() {
        return cookieMap.isEmpty();
    }

    /**
     * 该方法用于添加一组需要设置的cookies
     *
     * @param cookiesMap cookies集合
     * @since autest 3.6.0
     */
    public void addCookies(Map<String, String> cookiesMap) {
        // 过滤掉map为空的情况
        Optional.ofNullable(cookiesMap).filter(map -> !map.isEmpty()).ifPresent(map -> {
            map.forEach(this::addCookie);
        });
    }

    /**
     * 该方法用于添加单个需要设置的cookie
     *
     * @param cookieName  cookie参数名称
     * @param cookieValue cookie参数值
     * @since autest 3.6.0
     */
    public void addCookie(String cookieName, String cookieValue) {
        // 过滤参数名称为空的情况，并且当参数值为null时，则处理为空串
        Optional.ofNullable(cookieName).filter(key -> !key.isEmpty()).ifPresent(key -> {
            cookieMap.put(key, Optional.ofNullable(cookieValue).orElseGet(() -> ""));
        });
    }

    /**
     * 该方法用于根据url传入参数的表达式格式，向接口中添加参数
     * <p>
     * <b>注意：</b>表达式的判断的正则格式为“\w+=\w*(;\w+=\w*)*”，若未按规则编写，则无法添加参数
     * </p>
     *
     * @param expression 参数表达式
     * @since autest 3.6.0
     */
    public void addCookie(String expression) {
        // 过滤不符合正则的表达式，之后按照分隔符号切分，得到每个参数的表达式后，再按照参数与值的符号切分
        Optional.ofNullable(expression)
                .filter(exp -> exp.matches(String.format("\\w+%s\\w*(%s;\\w+%s\\w*)*", SYMBOL_SPLIT_COOKIES_VALUE,
                        SYMBOL_SPLIT_COOKIES, SYMBOL_SPLIT_COOKIES_VALUE)))
                .map(exp -> exp.split(SYMBOL_SPLIT_COOKIES)).map(Arrays::stream)
                .ifPresent(expStream -> expStream.filter(pramExp -> pramExp.contains(SYMBOL_SPLIT_COOKIES_VALUE))
                        .map(pramExp -> pramExp.split(SYMBOL_SPLIT_COOKIES_VALUE)).forEach(cookies -> {
                            if (cookies.length == 1) {
                                addCookie(cookies[0], "");
                            } else {
                                addCookie(cookies[0], cookies[1]);
                            }
                        }));
    }

    /**
     * 该方法用于清空存储的所有cookie内容
     *
     * @return 原有的cookie内容
     * @since autest 3.6.0
     */
    public Map<String, String> clearCookies() {
        Map<String, String> cookiesMap = getCookieMap();
        this.cookieMap.clear();
        return cookiesMap;
    }

    /**
     * 该方法用于设置接口的连接超时时间
     *
     * @param connectTime 接口超时时间
     * @param timeUnit    时间单位枚举
     * @since autest 3.6.0
     */
    public void setConnectTime(long connectTime, TimeUnit timeUnit) {
        this.connectTime = new Entry<>((connectTime <= 0L ? 0L : connectTime),
                Optional.ofNullable(timeUnit).orElse(TimeUnit.SECOND));
    }

    /**
     * 该方法用于通过传入时间块表达式，设置接口的超时时间
     * <p>
     * 传入的时间块单位可参考{@link TimeUnit}中每个枚举对应的正则判断，其传入的时间块表达式案例如下：
     * <ul>
     * <li>传入“1min”表示设置接口超时时间为1分钟</li>
     * <li>传入“1.5H”表示设置接口超时时间为1小时30分钟</li>
     * <li>传入“1min30s”表示设置接口超时时间为1分30秒</li>
     * </ul>
     * </p>
     * <p>
     * <b>注意：</b>建议不要使用月和年为单位，其单位为取近似值，在计算时可能不准确；时间块表达式不允许传入负号，传入的每个时间块只做加法处理
     * </p>
     *
     * @param timeExpression 时间块表达式
     * @since autest 3.6.0
     */
    public void setConnectTime(String timeExpression) {
        // 存储计算的时间
        long time = 0L;

        // 计算思路参考Time类中的addTime(String)方法
        char[] chars = Optional.ofNullable(timeExpression).filter(text -> !text.isEmpty())
                .map(text -> text + ".").map(String::toCharArray)
                .orElseGet(() -> new char[] {});

        // 遍历所有的字符，区别存储单位与增减的数值，参考Time类中的时间计算方法
        StringBuilder numText = new StringBuilder();
        StringBuilder unitText = new StringBuilder();
        boolean isUnit = false;
        for (char ch : chars) {
            if (Character.isDigit(ch) || ch == '.') {
                if (isUnit) {
                    // 获取单位
                    TimeUnit timeUnit = Arrays.stream(TimeUnit.values()).filter(unit -> unit.isTimeUnit(unitText.toString()))
                            .findFirst().orElseThrow(
                                    () -> new InterfaceReadToolsException("无法识别的时间单位块：" + numText + unitText));
                    // 获取需要计算的数值
                    double timeNum = 0.0;
                    int index = numText.toString().indexOf(".");
                    if (index == numText.toString().length() - 1) {
                        timeNum = Double.valueOf(numText.toString() + "0");
                    } else if (index == 0) {
                        timeNum = Double.valueOf("0" + numText.toString());
                    } else {
                        timeNum = Double.valueOf(numText.toString());
                    }

                    // 将数值转换成毫秒值进行计算，并存储
                    time += (timeNum * timeUnit.getToMillisNum());

                    numText.delete(0, numText.length());
                    unitText.delete(0, unitText.length());
                }

                numText.append(ch);
                isUnit = false;
            } else {
                isUnit = true;
                unitText.append(ch);
            }
        }

        // 判断time是否被赋值
        if (time != 0L) {
            setConnectTime(time, TimeUnit.MILLISECOND);
        }
    }

    /**
     * 该方法用于返回当前接口设置的请求超时时间
     *
     * @return 接口请求超时时间
     * @since autest 3.6.0
     */
    public Entry<Long, TimeUnit> getConnectTime() {
        if (connectTime == null) {
            return DEFAULT_CONNECT_TIME;
        }
        return new Entry<>(connectTime.getKey(), connectTime.getValue());
    }

    @SuppressWarnings("unchecked")
    @Override
    public InterfaceInfo clone() {
        InterfaceInfo newInter = null;
        try {
            // 克隆接口静态信息
            newInter = (InterfaceInfo) super.clone();
            // 克隆接口参数信息
            newInter.paramMap = (HashMap<String, String>) paramMap.clone();
            // 克隆接口请求头信息
            newInter.requestHeaderMap = (HashMap<String, String>) requestHeaderMap.clone();
            // 克隆cookie信息
            newInter.cookieMap = (HashMap<String, String>) cookieMap.clone();

            // 克隆接口响应报文格式信息
            newInter.responseContentTypeMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
            for (int key : responseContentTypeMap.keySet()) {
                newInter.responseContentTypeMap.put(key,
                        (HashSet<MessageType>) responseContentTypeMap.get(key).clone());
            }
            // 克隆接口响应报文断言规则信息
            newInter.assertRuleSet = new HashSet<>();
            assertRuleSet.stream().map(json -> json.toJSONString()).map(JSONObject::parseObject)
                    .forEach(newInter.assertRuleSet::add);
            // 克隆接口响应报文提词规则信息
            newInter.extractRuleSet = new HashSet<>();
            extractRuleSet.stream().map(json -> json.toJSONString()).map(JSONObject::parseObject)
                    .forEach(newInter.extractRuleSet::add);

        } catch (CloneNotSupportedException e) {
            throw new TestCaseException("接口数据异常，无法被拷贝：" + toString());
        }

        return newInter;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((body == null) ? 0 : body.hashCode());
        result = prime * result + ((host == null) ? 0 : host.hashCode());
        result = prime * result + ((paramMap == null) ? 0 : paramMap.hashCode());
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        result = prime * result + port;
        result = prime * result + ((protocol == null) ? 0 : protocol.hashCode());
        result = prime * result + ((requestType == null) ? 0 : requestType.hashCode());
        return result;
    }

    /**
     * 判断接口信息是否一致，其依据协议、主机（域名）、端口、接口路径、参数与请求体参数进行判断，当这些参数一致时，则认为是同一个接口
     *
     * @param obj 需对比的类
     * @return 判断结果
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        InterfaceInfo other = (InterfaceInfo) obj;
        if (body == null) {
            if (other.body != null) {
                return false;
            }
        } else if (!body.equals(other.body)) {
            return false;
        }
        if (host == null) {
            if (other.host != null) {
                return false;
            }
        } else if (!host.equals(other.host)) {
            return false;
        }
        if (paramMap == null) {
            if (other.paramMap != null) {
                return false;
            }
        } else if (!paramMap.equals(other.paramMap)) {
            return false;
        }
        if (path == null) {
            if (other.path != null) {
                return false;
            }
        } else if (!path.equals(other.path)) {
            return false;
        }
        if (port != other.port) {
            return false;
        }
        if (protocol == null) {
            if (other.protocol != null) {
                return false;
            }
        } else if (!protocol.equals(other.protocol)) {
            return false;
        }
        if (requestType != other.requestType) {
            return false;
        }
        return true;
    }

    /**
     * 该方法用于返回请求接口的完整url内容
     *
     * @return 接口url内容
     * @since autest 3.3.0
     */
    public String toUrlString() {
        // 拼接接口参数信息
        StringJoiner paramInfo = new StringJoiner(SYMBOL_SPLIT_PARAM);
        paramMap.forEach((k, v) -> paramInfo.add(String.format("%s%s%s", k, SYMBOL_SPLIT_PARAM_VALUE, v)));

        return String.format("%s%s%s%s%s", getProtocol(), getHost(),
                (getPort() == 80 ? "" : (SYMBOL_SPLIT_PORT + getPort())), getPath(),
                (paramInfo.length() != 0 ? (SYMBOL_SPLIT_START_PARAM + paramInfo.toString()) : ""));
    }

    @SuppressWarnings("unchecked")
    @Override
    public String toString() {
        // 添加请求头参数
        JSONObject headerJson = new JSONObject();
        requestHeaderMap.forEach(headerJson::put);
        String cookieExperssion = getCookieExpression();
        if (!cookieExperssion.isEmpty()) {
            headerJson.put("Cookie", cookieExperssion);
        }

        // 添加接口整体参数
        JSONObject interInfoJson = new JSONObject();
        interInfoJson.put("url", toUrlString());
        interInfoJson.put("requestType", getRequestType());

        // 将请求体的内容转换为字符串输出
        if (body != null) {
            String bodyText = "";
            Object value = body.getValue();
            if (value instanceof File) {
                bodyText = ((File) value).getAbsolutePath();
            } else if (value instanceof List) {
                bodyText = HttpUtil.formUrlencoded2Extract((List<Entry<String, Object>>) value);
            } else {
                bodyText = value.toString();
            }
            interInfoJson.put("body", bodyText);
        }

        interInfoJson.put("requestHeader", headerJson);

        return interInfoJson.toJSONString();
    }
}
