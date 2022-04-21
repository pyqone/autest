package com.auxiliary.http;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;

import com.alibaba.fastjson.JSONObject;
import com.auxiliary.testcase.TestCaseException;
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
     */
    protected final String DEFAULT_PROTOCOL = "http://";
    /**
     * 定义默认主机
     */
    protected final String DEFAULT_HOST = "127.0.0.1";
    /**
     * 定义默认主机端口
     */
    protected final int DEFAULT_PORT = 80;
    /**
     * 定义接口默认请求方式
     */
    protected final RequestType DEFAULT_REQUESTTYPE = RequestType.GET;
    /**
     * 定义响应内容默认字符集
     */
    protected final String DEFAULT_CHARSETNAME = "UTF-8";

    /**
     * 定义接口主机与协议间分隔的符号
     */
    protected final String SYMBOL_SPLIT_PROTOCOL = "://";
    /**
     * 定义接口主机与主机间的分隔符号
     */
    protected final String SYMBOL_SPLIT_PORT = ":";
    /**
     * 定义接口路径与接口主机间的分隔符号
     */
    protected final String SYMBOL_SPLIT_PATH = "/";
    /**
     * 定义接口起始参数与接口路径间的分隔符号
     */
    protected final String SYMBOL_SPLIT_START_PARAM = "?";
    /**
     * 定义接口参数键值之间的分隔符号
     */
    protected final String SYMBOL_SPLIT_PARAM_VALUE = "=";
    /**
     * 定义接口参数与参数间的分隔符号
     */
    protected final String SYMBOL_SPLIT_PARAM = "&";

    /**
     * 接口协议，默认http
     */
    private String protocol = DEFAULT_PROTOCOL;
    /**
     * 主机，默认127.0.0.1
     */
    private String host = DEFAULT_HOST;
    /**
     * 主机端口号，默认80
     */
    private int port = DEFAULT_PORT;
    /**
     * 接口地址
     */
    private String path = "";
    /**
     * 请求类型，默认Get
     */
    private RequestType requestType = DEFAULT_REQUESTTYPE;
    /**
     * 接口请求参数
     */
    private HashMap<String, String> paramMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
    /**
     * 接口请求体
     */
    private String body = "";
    /**
     * 接口请求头
     */
    private HashMap<String, String> requestHeaderMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
    /**
     * 接口相应字符集
     */
    private String charsetname = DEFAULT_CHARSETNAME;
    /**
     * 接口响应内容格式
     */
    private HashMap<Integer, HashSet<ResponseContentType>> responseContentTypeMap = new HashMap<>(
            ConstType.DEFAULT_MAP_SIZE);
    /**
     * 存储接口返回提词的内容
     */
    private HashMap<String, String> extractMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);

    /**
     * 该方法用于返回接口的url协议
     *
     * @return url协议
     * @since autest 3.3.0
     */
    public String getProtocol() {
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
                .map(p -> {
                    // 判断协议内容末尾是否包含相应的符号，若不包含，则添加相应的符号
                    if (p.lastIndexOf(SYMBOL_SPLIT_PROTOCOL) != p.length() - SYMBOL_SPLIT_PROTOCOL.length()) {
                        p += SYMBOL_SPLIT_PROTOCOL;
                    }
                    return p;
                }).orElseGet(() -> DEFAULT_PROTOCOL);
    }

    /**
     * 该方法用于返回接口的主机地址或域名
     *
     * @return 主机地址或域名
     * @since autest 3.3.0
     */
    public String getHost() {
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
            return h;
        }).orElseGet(() -> DEFAULT_HOST);
    }

    /**
     * 该方法用于返回主机端口
     *
     * @return 主机端口
     * @since autest 3.3.0
     */
    public int getPort() {
        return port;
    }

    /**
     * 该方法用于设置主机端口
     *
     * @param port 主机端口
     * @since autest 3.3.0
     */
    public void setPort(int port) {
        if (port < 0 || port > 65535) {
            port = DEFAULT_PORT;
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
     * 该方法用于设置接口的路径
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
        }).orElseGet(() -> "");
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
     * @return 类本身
     * @since autest 3.3.0
     */
    public InterfaceInfo analysisUrl(String url) {
        if (url == null || url.isEmpty()) {
            return this;
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

        // 解析主机端口，若存在主机端口分隔符，则在urlText去除，并将端口转换后传入相应的内容中
        if ((index = urlText.indexOf(SYMBOL_SPLIT_PORT)) > -1) {
            try {
                setPort(Integer.valueOf(urlText.substring(index).replaceAll(SYMBOL_SPLIT_PORT, "")));
                urlText.delete(index, urlText.length());
            } catch (NumberFormatException e) {
            }
        }

        setHost(urlText.toString());
        return this;
    }

    /**
     * 该方法用于返回接口的请求方式枚举
     *
     * @return 请求方式枚举
     * @since autest 3.3.0
     */
    public RequestType getRequestType() {
        return requestType;
    }

    /**
     * 该方法用于设置接口的请求方式枚举
     *
     * @param requestType 接口的请求方式枚举
     * @since autest 3.3.0
     */
    public void setRequestType(RequestType requestType) {
        this.requestType = Optional.ofNullable(requestType).orElseGet(() -> DEFAULT_REQUESTTYPE);
    }

    /**
     * 该方法用于返回接口的请求参数（传参）
     *
     * @return 接口的请求参数
     * @since autest 3.3.0
     */
    public Map<String, String> getParamMap() {
        return paramMap;
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
     * <b>注意：</b>表达式的判断的正则格式为“\w+=\w*(&\w+=\w*)*”，若未按规则编写，则无法添加参数
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
     * 该方法用于返回接口的请求体
     *
     * @return 接口的请求体
     * @since autest 3.3.0
     */
    public String getBody() {
        return body;
    }

    /**
     * 该方法用于设置接口的请求体
     *
     * @param body 接口的请求体
     * @since autest 3.3.0
     */
    public void setBody(String body) {
        this.body = Optional.ofNullable(body).orElseGet(() -> "");
    }

    /**
     * 该方法用于返回接口的请求头
     *
     * @return 接口的请求头
     * @since autest 3.3.0
     */
    public Map<String, String> getRequestHeaderMap() {
        return requestHeaderMap;
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
     * 该方法用于返回接口响应内容的字符集编码名称
     *
     * @return 接口响应内容的字符集编码名称
     * @since autest 3.3.0
     */
    public String getCharsetname() {
        return charsetname;
    }

    /**
     * 该方法用于设置接口响应内容的字符集编码名称
     *
     * @param charsetname 接口响应内容的字符集编码名称
     * @since autest 3.3.0
     */
    public void setCharsetname(String charsetname) {
        this.charsetname = Optional.ofNullable(charsetname).orElseGet(() -> DEFAULT_CHARSETNAME);
    }

    /**
     * 该方法用于返回接口响应内容的格式枚举，当状态码不存在时，则返回空集合
     *
     * @param state 状态码
     * @return 接口响应内容的格式枚举
     * @since autest 3.3.0
     */
    public Set<ResponseContentType> getResponseContentType(int state) {
        return Optional.ofNullable(responseContentTypeMap.get(state)).orElseGet(() -> new HashSet<>());
    }

    /**
     * 该方法用于添加接口相应内容的格式
     * <p>
     * 格式允许添加多个，以应对在不同参数或不同响应状态下的情况
     * </p>
     *
     * @param responseContentTypes 响应内容格式数组
     * @since autest 3.3.0
     */
    public void addResponseContentTypeSet(int state, ResponseContentType... responseContentTypes) {
        // 判断当前状态码是否存在，若不存在，则添加空集合
        if (!responseContentTypeMap.containsKey(state)) {
            responseContentTypeMap.put(state, new HashSet<>());
        }

        // 获取当前状态码中 存储的状态
        HashSet<ResponseContentType> responseContentTypeSet = responseContentTypeMap.get(state);

        // 过滤掉数组为null或为空的情况
        Optional.ofNullable(responseContentTypes).filter(types -> types.length != 0).ifPresent(types -> {
            Arrays.stream(types).filter(type -> type != null).forEach(responseContentTypeSet::add);
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public InterfaceInfo clone() throws CloneNotSupportedException {
        InterfaceInfo newInter = null;
        try {
            newInter = (InterfaceInfo) super.clone();
            newInter.paramMap = (HashMap<String, String>) paramMap.clone();
            newInter.requestHeaderMap = (HashMap<String, String>) requestHeaderMap.clone();
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

        return String.format("%s%s%s%s%s", protocol, host, (port == 80 ? "" : (SYMBOL_SPLIT_PORT + port)), path,
                (paramInfo.length() != 0 ? (SYMBOL_SPLIT_START_PARAM + paramInfo.toString()) : ""));
    }

    @Override
    public String toString() {
        // 添加请求头参数
        JSONObject headerJson = new JSONObject();
        requestHeaderMap.forEach(headerJson::put);

        // 添加接口整体参数
        JSONObject interInfoJson = new JSONObject();
        interInfoJson.put("url", toUrlString());
        interInfoJson.put("requestType", requestType);
        interInfoJson.put("body", body);
        interInfoJson.put("requestHeader", headerJson);

        return interInfoJson.toJSONString();
    }
}
