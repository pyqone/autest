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
    private final String DEFAULT_PROTOCOL = "http://";
    /**
     * 定义默认主机
     */
    private final String DEFAULT_HOST = "127.0.0.1";
    /**
     * 定义默认主机端口
     */
    private final int DEFAULT_PORT = 80;
    /**
     * 定义接口默认请求方式
     */
    private final RequestType DEFAULT_REQUESTTYPE = RequestType.GET;
    /**
     * 定义响应内容默认字符集
     */
    private final String DEFAULT_CHARSETNAME = "UTF-8";

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
    private Set<ResponseContentType> responseContentTypeSet = new HashSet<>(ConstType.DEFAULT_MAP_SIZE);

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
        this.protocol = Optional.ofNullable(protocol).filter(p -> !p.isEmpty()).map(p -> {
            // 判断协议内容末尾是否包含相应的符号，若不包含，则添加相应的符号
            String symbol = "://";
            if (p.lastIndexOf(symbol) != p.length() - symbol.length()) {
                p += symbol;
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
            if (h.lastIndexOf("/") == h.length() - 1) {
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
            if (p.indexOf("/") != 0) {
                p = "/" + p;
            }
            // 判断接口路径的最后一位是否为/，若是，则进行去除
            if (p.lastIndexOf("/") == p.length() - 1) {
                p = p.substring(0, p.length() - 1);
            }

            return p;
        }).orElseGet(() -> "");
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
        Optional.ofNullable(expression).filter(exp -> exp.matches("\\w+=\\w*(&\\w+=\\w*)*")).map(exp -> exp.split("&"))
                .map(Arrays::stream).ifPresent(expStream -> expStream.filter(pramExp -> pramExp.contains("="))
                        .map(pramExp -> pramExp.split("=")).forEach(params -> {
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
     * 该方法用于返回接口响应内容的格式枚举
     *
     * @return 接口响应内容的格式枚举
     * @since autest 3.3.0
     */
    public Set<ResponseContentType> getResponseContentTypeSet() {
        return responseContentTypeSet;
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
    public void addResponseContentTypeSet(ResponseContentType... responseContentTypes) {
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
    public String toInterfaceString() {
        // 拼接接口参数信息
        StringJoiner paramInfo = new StringJoiner("&");
        paramMap.forEach((k, v) -> paramInfo.add(String.format("%s=%s", k, v)));

        return String.format("%s%s:%d%s%s", protocol, host, port, path,
                (paramInfo.length() == 0 ? "?" + paramInfo.toString() : ""));
    }

    @Override
    public String toString() {
        // 添加请求头参数
        JSONObject headerJson = new JSONObject();
        requestHeaderMap.forEach(headerJson::put);

        // 添加接口整体参数
        JSONObject interInfoJson = new JSONObject();
        interInfoJson.put("url", toInterfaceString());
        interInfoJson.put("requestType", requestType);
        interInfoJson.put("body", body);
        interInfoJson.put("requestHeader", headerJson);

        return interInfoJson.toJSONString();
    }
}
