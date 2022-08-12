package com.auxiliary.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auxiliary.tool.common.DisposeCodeUtils;
import com.auxiliary.tool.common.Entry;
import com.auxiliary.tool.regex.ConstType;

import okhttp3.Headers;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * <p>
 * <b>文件名：</b>EasyResponse.java
 * </p>
 * <p>
 * <b>用途：</b> 对接口响应参数进行处理，可根据不同的返回，对响应结果进行输出，或以格式化的形式输出响应内容
 * </p>
 * <p>
 * <b>编码时间：</b>2020年6月26日下午7:09:07
 * </p>
 * <p>
 * <b>修改时间：</b>2020年6月26日下午7:09:07
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since autest 3.3.0
 */
public class EasyResponse {
    /**
     * 存储接口响应信息
     */
    ResponseInfo info = new ResponseInfo();

    /**
     * 构造对象，指定OKHttp响应类
     *
     * @param response  OKHttp响应类
     * @param interInfo
     */
    protected EasyResponse(Response response, InterfaceInfo interInfo) {
        // 记录当前时间的时间戳
        info.timeAfterRequestAtMillis = System.currentTimeMillis();

        // 记录接口实际请求信息
        info.requestInterInfo = interInfo;
        // 记录请求结果
        info.response = response;
        // 存储响应内容
        ResponseBody body = response.body();
        try {
            info.responseBody = body.bytes();
        } catch (IOException e) {
        }

        // 存储响应头
        Headers heads = response.headers();
        for (String head : heads.names()) {
            List<String> valueList = heads.values(head);
            if (valueList.size() < 2) {
                info.responseHeaderMap.put(head, valueList.get(0));
            } else {
                info.responseHeaderMap.put(head, valueList.toString());
            }
        }

        // 存储响应状态及消息
        info.status = response.code();
        info.message = response.message();
        
        // 存储请求时间
        info.sentRequestAtMillis = response.sentRequestAtMillis();
        info.receivedResponseAtMillis = response.receivedResponseAtMillis();
    }

    /**
     * 该方法用于设置字符集名称
     *
     * @param charsetName 字符集名称
     * @since autest 3.3.0
     */
    public void setCharsetName(String charsetName) {
        info.charsetName = charsetName;
    }

    /**
     * 该方法用于添加响应体的内容格式
     * 
     * @param status     状态
     * @param messageSet 内容格式集合
     * @since autest 3.3.0
     */
    public void setMessageType(int status, Set<MessageType> messageSet) {
        info.bodyTypeMap.put(status, new HashSet<>(messageSet));
    }

    /**
     * 该方法用于以{@link #setCharsetName(String)}方法设定的编码格式，返回接口的响应体字符串
     *
     * @since autest 3.3.0
     */
    public String getResponseBodyText() {
        try {
            return new String(info.responseBody, info.charsetName);
        } catch (UnsupportedEncodingException e) {
            throw new HttpResponseException("报文无法转义为字符串", e);
        }
    }

    /**
     * 该方法用于返回响应体
     *
     * @return 响应体
     * @since autest 3.3.0
     */
    public byte[] getResponseBody() {
        return info.responseBody;
    }

    /**
     * 该方法用于返回接口返回的响应头
     *
     * @return 响应头
     * @since autest 3.3.0
     */
    public Map<String, String> getResponseHeaderMap() {
        return info.responseHeaderMap;
    }

    /**
     * 该方法用于返回接口响应状态码
     *
     * @return 响应状态码
     * @since autest 3.3.0
     */
    public int getStatus() {
        return info.response.code();
    }

    /**
     * 该方法用于返回接口响应消息
     *
     * @return 响应消息
     * @since autest 3.3.0
     */
    public String getMessage() {
        return info.message;
    }

    /**
     * 该方法用于返回从客户端发出请求时记录的时间戳
     * 
     * @return 从客户端发出请求的时刻记录的时间戳
     * @since autest 3.4.0
     */
    public long getSentRequestAtMillis() {
        return info.sentRequestAtMillis;
    }

    /**
     * 该方法用于返回从服务器接收到请求头时记录的时间戳
     * 
     * @return 从服务器接收到请求头时记录的时间戳
     * @since autest 3.4.0
     */
    public long getReceivedResponseAtMillis() {
        return info.receivedResponseAtMillis;
    }

    /**
     * 该方法用于返回接口请求成功后记录的时间戳
     * 
     * @return 接口请求成功后记录的时间戳
     * @since autest 3.4.0
     */
    public long getTimeAfterRequestAtMillis() {
        return info.timeAfterRequestAtMillis;
    }

    /**
     * 该方法用于根据指定的接口开始请求时间与接口成功请求时间做差，返回其差值，单位为毫秒
     * <p>
     * 参数指定根据何种类型的时间计算，传入true表示以{@link #getSentRequestAtMillis()}方法返回的时间进行计算；传入false表示以
     * {@link #getReceivedResponseAtMillis()}方法返回的时间进行计算
     * </p>
     * 
     * @param isSentRequestTime 是否以从客户端发出请求时记录的时间戳进行计算
     * @return 接口开始请求与结束请求之间的时间差
     * @since autest 3.4.0
     */
    public long getResponseTimeDifferenceAtMillis(boolean isSentRequestTime) {
        return getTimeAfterRequestAtMillis()
                - (isSentRequestTime ? getSentRequestAtMillis() : getReceivedResponseAtMillis());
    }

    /**
     * 该方法用于返回接口的实际请求信息
     * <p>
     * <b>注意：</b>返回的信息类仅存储请求接口的地址、请求类型、请求头和请求体信息，若通过{@link EasyHttp#requst(InterfaceInfo)}请求接口时，其除前面提到的信息外，
     * 其他在{@link InterfaceInfo}参数中的信息将不会被存储
     * </p>
     * 
     * @return 接口实际请求信息
     * @since autest 3.5.0
     */
    public InterfaceInfo getRequestInterfaceInfo() {
        return info.requestInterInfo.clone();
    }

    /**
     * 该方法用于对接口响应报文的指定部分内容进行断言
     * <p>
     * <b>注意：</b>断言规则为正则表达式，若断言非正则表达式的内容，需要自行进行转义，例如断言“(”符号，则需要传入“\\(”。方法中各个参数的解释，可参考{@link #extractKey(SearchType, String, String, String, String, int)}方法
     * </p>
     *
     * @param assertRegex   断言规则
     * @param searchType    搜索范围枚举
     * @param paramName     搜索变量
     * @param xpath         提取内容xpath
     * @param leftBoundary  断言内容左边界
     * @param rightBoundary 断言内容右边界
     * @param index         指定下标内容
     * @return 断言结果
     * @since autest 3.3.0
     */
    public boolean assertResponse(String assertRegex, SearchType searchType, String paramName, String xpath,
            String leftBoundary, String rightBoundary, int index) {
        return extractKey(searchType, paramName, xpath, leftBoundary, rightBoundary, index).matches(assertRegex);
    }

    /**
     * 该方法用于对接口响应报文的指定部分内容进行断言
     * <p>
     * <b>注意：</b>断言规则为正则表达式，若断言非正则表达式的内容，需要自行进行转义，例如断言“(”符号，则需要传入“\\(”。
     * 方法中各个参数的解释，可参考{@link #extractKey(SearchType, String, String, int)}方法
     * </p>
     *
     * @param assertRegex   断言规则
     * @param searchType    搜索范围枚举
     * @param leftBoundary  断言内容左边界
     * @param rightBoundary 断言内容右边界
     * @param index         指定下标内容
     * @return 断言结果
     * @since autest 3.3.0
     */
    public boolean assertResponse(String assertRegex, SearchType searchType, String leftBoundary, String rightBoundary,
            int index) {
        return extractKey(searchType, leftBoundary, rightBoundary, index).matches(assertRegex);
    }

    /**
     * 该方法用于对接口响应报文的指定部分内容进行断言
     * <p>
     * <b>注意：</b>断言规则为正则表达式，若断言非正则表达式的内容，需要自行进行转义，例如断言“(”符号，则需要传入“\\(”。
     * 方法中各个参数的解释，可参考{@link #extractKey(SearchType, String, String)}方法
     * </p>
     *
     * @param assertRegex 断言规则
     * @param searchType  搜索范围枚举
     * @param paramName   搜索变量
     * @param xpath       提取内容xpath
     * @return 断言结果
     * @since autest 3.3.0
     */
    public boolean assertResponse(String assertRegex, SearchType searchType, String paramName, String xpath) {
        return extractKey(searchType, paramName, xpath).matches(assertRegex);
    }

    /**
     * 该方法用于对接口响应报文的指定部分内容进行断言
     * <p>
     * <b>注意：</b>断言规则为正则表达式，若断言非正则表达式的内容，需要自行进行转义，例如断言“(”符号，则需要传入“\\(”。
     * 方法中各个参数的解释，可参考{@link #extractKey(SearchType, String)}方法
     * </p>
     *
     * @param assertRegex 断言规则
     * @param searchType  搜索范围枚举
     * @param paramName   搜索变量
     * @return 断言结果
     * @since autest 3.3.0
     */
    public boolean assertResponse(String assertRegex, SearchType searchType, String paramName) {
        return extractKey(searchType, paramName).matches(assertRegex);
    }

    /**
     * 该方法用于对接口响应报文的指定部分内容进行断言
     * <p>
     * <b>注意：</b>断言规则为正则表达式，若断言非正则表达式的内容，需要自行进行转义，例如断言“(”符号，则需要传入“\\(”。
     * 方法中各个参数的解释，可参考{@link #extractKey(String)}方法
     * </p>
     *
     * @param assertRegex 断言规则
     * @param paramName   搜索变量
     * @return 断言结果
     * @since autest 3.3.0
     */
    public boolean assertResponse(String assertRegex, String paramName) {
        return extractKey(paramName).matches(assertRegex);
    }

    /**
     * 该方法用于通过指定的条件对接口响应报文指定内容进行提取，返回提取到的内容
     * <p>
     * 提取规则如下：
     * <ol>
     * <li>必须指定搜索范围{@link SearchType}枚举，否则抛出{@link HttpResponseException}异常</li>
     * <li>当搜索范围为{@link SearchType#MESSAGE}或{@link SearchType#STATUS}时，则paramName不生效</li>
     * <li>当搜索范围为{@link SearchType#HEADER}时，若指定了paramName内容，则获取响应头对应键的内容（没有该键值则返回空串）；若未指定paramName参数，则将响应头以{@link HashMap#toString()}方法的形式返回</li>
     * <li>当搜索范围为{@link SearchType#BODY}时，存在以下情况：
     * <ol>
     * <li>当响应体为{@link MessageType#JSON}类型时，其xpath参数不生效，判断paramName参数的方式与{@link SearchType#HEADER}类似</li>
     * <li>当响应体为{@link MessageType#XML}或{@link MessageType#HTML}类型时，则优先判断xpath参数，若其为空时，则再判断paramName参数，其判断方式与{@link SearchType#HEADER}类似</li>
     * <li>当响应体为其他类型时，则xpath与paramName参数均不生效</li>
     * </ol>
     * </li>
     * <li>通过paramName或xpath提取后，仍会对内容进行指定边界的提取，其两种提取方式不独立</li>
     * <li>若同时未指定左右边界，则不进行边界内容提取</li>
     * <li>当边界提取到多条数据时，则根据指定的index进行提取，其下标从1开始计算（1为第一条元素），若值小于1，则获取第一条数据；若值大于提取到的数据集合数量，则返回最后一条数据</li>
     * <li>左右边界允许为正则表达式</li>
     * </ol>
     * </p>
     * 
     * @param searchType    提词搜索范围枚举
     * @param paramName     提取内容参数名
     * @param xpath         提取内容xpath
     * @param leftBoundary  提取内容左边界
     * @param rightBoundary 提取内容右边界
     * @param index         边界提取到多条内容时指定的获取下标
     * @return 对响应体提取到的内容
     * @since autest 3.3.0
     */
    public String extractKey(SearchType searchType, String paramName, String xpath, String leftBoundary,
            String rightBoundary, int index) {
        // 判断searchType参数是否为null
        if (searchType == null) {
            throw new HttpResponseException("未指定搜索范围，无法查找响应内容");
        }

        // 处理paramName参数，若其为null，则使其变为空串
        paramName = Optional.ofNullable(paramName).orElse("");
        xpath = Optional.ofNullable(xpath).orElse("");

        String value = "";
        switch (searchType) {
        case STATUS:
            value = String.valueOf(getStatus());
            break;
        case MESSAGE:
            value = getMessage();
            break;
        case HEADER:
            // 判断paramName参数是否为空串
            if (paramName.isEmpty()) {
                value = info.responseHeaderMap.toString();
            } else {
                value = Optional.ofNullable(info.responseHeaderMap.get(paramName)).orElse("");
            }
            break;
        case BODY:
            // 获取响应体的字符串形式
            value = getResponseBodyText();
            // 判断paramName或xpath参数是否为空串
            if (!paramName.isEmpty() || !xpath.isEmpty()) {
                value = analysisBody(value, paramName, xpath);
            }
            break;
        default:
            throw new HttpResponseException("不支持的断言的接口响应内容：" + searchType);
        }

        // 根据存在不同边界条件的情况，选择不同的处理方法
        leftBoundary = Optional.ofNullable(leftBoundary).orElse("");
        rightBoundary = Optional.ofNullable(rightBoundary).orElse("");
        if (leftBoundary.isEmpty() && rightBoundary.isEmpty()) {
            return value;
        } else if (!leftBoundary.isEmpty() && !rightBoundary.isEmpty()) {
            return disposeCompleteBoundaryText(value, leftBoundary, rightBoundary, index);
        } else {
            return disposeSingleBoundaryText(value, leftBoundary, rightBoundary, index);
        }
    }

    /**
     * 该方法用于通过指定的边界条件对接口响应报文指定内容进行提取，返回提取到的内容
     * <p>
     * 提取规则如下：
     * <ol>
     * <li>通过paramName或xpath提取后，仍会对内容进行指定边界的提取，其两种提取方式不独立</li>
     * <li>若同时未指定左右边界，则不进行边界内容提取</li>
     * <li>当边界提取到多条数据时，则根据指定的index进行提取，其下标从1开始计算（1为第一条元素），若值小于1，则获取第一条数据；若值大于提取到的数据集合数量，则返回最后一条数据</li>
     * <li>左右边界允许为正则表达式</li>
     * </ol>
     * </p>
     * 
     * @param searchType    提词搜索范围枚举
     * @param leftBoundary  提取内容左边界
     * @param rightBoundary 提取内容右边界
     * @param index         边界提取到多条内容时指定的获取下标
     * @return 对响应体提取到的内容
     * @since autest 3.3.0
     */
    public String extractKey(SearchType searchType, String leftBoundary, String rightBoundary, int index) {
        return extractKey(searchType, "", "", leftBoundary, rightBoundary, index);
    }

    /**
     * 该方法用于通过指定的搜索参数对接口响应报文指定内容进行提取，返回提取到的内容
     * <p>
     * 提取规则如下：
     * <ol>
     * <li>必须指定搜索范围{@link SearchType}枚举，否则抛出{@link HttpResponseException}异常</li>
     * <li>当搜索范围为{@link SearchType#MESSAGE}或{@link SearchType#STATUS}时，则paramName不生效</li>
     * <li>当搜索范围为{@link SearchType#HEADER}时，若指定了paramName内容，则获取响应头对应键的内容（没有该键值则返回空串）；若未指定paramName参数，则将响应头以{@link HashMap#toString()}方法的形式返回</li>
     * <li>当搜索范围为{@link SearchType#BODY}时，存在以下情况：
     * <ol>
     * <li>当响应体为{@link MessageType#JSON}类型时，其xpath参数不生效，判断paramName参数的方式与{@link SearchType#HEADER}类似</li>
     * <li>当响应体为{@link MessageType#XML}或{@link MessageType#HTML}类型时，则优先判断xpath参数，若其为空时，则再判断paramName参数，其判断方式与{@link SearchType#HEADER}类似</li>
     * <li>当响应体为其他类型时，则xpath与paramName参数均不生效</li>
     * </ol>
     * </li>
     * </ol>
     * </p>
     * 
     * @param searchType 提词搜索范围枚举
     * @param paramName  提取内容参数名
     * @param xpath      提取内容xpath
     * @return 对响应体提取到的内容
     * @since autest 3.3.0
     */
    public String extractKey(SearchType searchType, String paramName, String xpath) {
        return extractKey(searchType, paramName, xpath, "", "", 0);
    }

    /**
     * 该方法用于通过指定的搜索参数对接口响应报文指定内容进行提取，返回提取到的内容
     * <p>
     * 提取规则如下：
     * <ol>
     * <li>必须指定搜索范围{@link SearchType}枚举，否则抛出{@link HttpResponseException}异常</li>
     * <li>当搜索范围为{@link SearchType#MESSAGE}或{@link SearchType#STATUS}时，则paramName不生效</li>
     * <li>当搜索范围为{@link SearchType#HEADER}时，若指定了paramName内容，则获取响应头对应键的内容（没有该键值则返回空串）；若未指定paramName参数，则将响应头以{@link HashMap#toString()}方法的形式返回</li>
     * <li>当搜索范围为{@link SearchType#BODY}时，存在以下情况：
     * <ol>
     * <li>当响应体为{@link MessageType#JSON}或{@link MessageType#XML}或{@link MessageType#HTML}类型时，其判断paramName参数的方式与{@link SearchType#HEADER}类似</li>
     * <li>当响应体为其他类型时，则paramName参数均不生效</li>
     * </ol>
     * </li>
     * </ol>
     * </p>
     * 
     * @param searchType 提词搜索范围枚举
     * @param paramName  提取内容参数名
     * @return 对响应体提取到的内容
     * @since autest 3.3.0
     */
    public String extractKey(SearchType searchType, String paramName) {
        return extractKey(searchType, paramName, "", "", "", 0);
    }

    /**
     * 该方法用于通过指定的搜索参数对响应体进行提取，返回提取到的内容
     * <p>
     * 提取规则如下：
     * <ol>
     * <li>当响应体为{@link MessageType#JSON}或{@link MessageType#XML}或{@link MessageType#HTML}类型时，其判断paramName参数的方式与{@link SearchType#HEADER}类似</li>
     * <li>当响应体为其他类型时，则paramName参数均不生效</li>
     * </ol>
     * </p>
     * 
     * @param paramName 提取内容参数名
     * @return 对响应体提取到的内容
     * @since autest 3.3.0
     */
    public String extractKey(String paramName) {
        return extractKey(SearchType.BODY, paramName, "", "", "", 0);
    }

    /**
     * 该方法用于根据指定的响应体内容格式，转义响应体，并根据查找参数或xpath对响应元素内容进行查找，返回找到的元素内容
     *
     * @param paramName 查找元素名称
     * @param xpath     查找xml的xpath
     * @return 查找到元素的内容
     * @since autest 3.3.0
     */
    private String analysisBody(String bodyText, String paramName, String xpath) {
        // 根据响应状态，获取请求体类型
        Set<MessageType> bodyTypeSet = Optional.ofNullable(info.bodyTypeMap.get(info.status))
                .orElseGet(() -> new HashSet<>());

        // 判断获取到的类型是否为空，若为空，则直接返回响应体文本
        if (bodyTypeSet.isEmpty()) {
            return bodyText;
        }

        // 若搜索变量名与path均为空，则直接返回响应体文本
        if (paramName.isEmpty() && xpath.isEmpty()) {
            return bodyText;
        }

        // 定义特殊符号随机替换符
        String replaceSymbol = "#" + UUID.randomUUID().toString().replaceAll("-", "") + "#";
        // 对paramName中需要转义的符号进行替换
        paramName = paramName.replaceAll(AssertResponse.SEPARATE_TRANSFERRED_MEANING_REGEX, replaceSymbol);
        // 对paramName按照切分符号进行切分
        String[] paramNames = paramName.split(AssertResponse.SEPARATE_SPLIT_REGEX);

        // 根据指定的响应体格式，对内容进行转换
        if (bodyTypeSet.contains(MessageType.JSON) && paramNames.length != 0) {
            try {
                return disposeJsonParam(JSONObject.parseObject(bodyText), replaceSymbol, paramNames);
            } catch (Exception e) {
            }
        }

        if (bodyTypeSet.contains(MessageType.XML) || bodyTypeSet.contains(MessageType.HTML)) {
            try {
                return disposeXmlParam(DocumentHelper.parseText(bodyText), replaceSymbol, paramNames, xpath);
            } catch (Exception e) {
            }
        }

        return bodyText;
    }

    /**
     * 该方法用于处理响应体为xml或html串时变量指向内容的获取
     * <p>
     * <b>注意：</b>当xml或html存在多个参数时，其根元素名称不进行判断
     * </p>
     *
     * @param xml           响应体xml类对象
     * @param replaceSymbol 替换符号
     * @param paramNames    参数名称数组
     * @return 搜索到的内容
     * @since autest 3.3.0
     */
    private String disposeXmlParam(Document xml, String replaceSymbol, String[] paramNames, String xpath) {
        // 对转换过程中的异常进行处理，若抛出异常，则直接返回空串
        try {
            // 先按照xpath方式对元素进行查找并进行转换，若未找到元素，则赋予空串
            String value = Optional.ofNullable(xpath).filter(x -> !x.isEmpty()).map(x -> {
                // 若查找xpath报错，则直接返回null
                try {
                    return xml.selectSingleNode(x);
                } catch (Exception e) {
                    return null;
                }
            }).map(ele -> ((Element) ele).getText()).orElse("");

            // 判断value是否为空，若不为空，则返回value的内容
            if (!value.isEmpty() || paramNames.length == 0) {
                return value;
            }

            // 若value为空，则进一步获取参数名中的内容
            Element root = xml.getRootElement();
            // 判断paramNames是否只包含一位数据，若只包含一位数据，则返回根元素的文本内容
            if (paramNames.length == 1) {
                if (paramNames[0].equals(root.getName())) {
                    return root.getText();
                } else {
                    return "";
                }
            }

            // 若paramNames存在多位数据，则循环获取到倒数第二位的数据，并逐层向下获取
            int index = 1;
            Element paramElement = root;
            for (; index < paramNames.length - 1; index++) {
                paramElement = (Element) getElement(paramNames[index], replaceSymbol, paramElement, (short) 1, false);
                // 若当前未查找到元素，则返回空串
                if (paramElement == null) {
                    return "";
                }
            }

            return (String) getElement(paramNames[index], replaceSymbol, paramElement, (short) 1, true);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 该方法用于处理响应体为json串时变量指向内容的获取
     *
     * @param json          响应体json类对象
     * @param replaceSymbol 替换符号
     * @param paramNames    参数名称数组
     * @return 搜索到的内容
     * @since autest 3.3.0
     */
    private String disposeJsonParam(JSONObject json, String replaceSymbol, String[] paramNames) {
        // 对转换过程中的异常进行处理，若抛出异常，则直接返回空串
        try {
            // 按照参数名，向下获取json串，直至达到目标前一个json串
            JSONObject paramJson = json;
            int index = 0;
            for (; index < paramNames.length - 1; index++) {
                paramJson = (JSONObject) getElement(paramNames[index], replaceSymbol, paramJson, (short) 0, false);
                // 若当前未查找到元素，则返回空串
                if (paramJson == null) {
                    return "";
                }
            }

            // 处理末尾的变量名
            return (String) getElement(paramNames[index], replaceSymbol, paramJson, (short) 0, true);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 该方法用于对元素内容进行解析，返回相应的下级元素或文本
     * 
     * @param paramArrayName 当前获取的元素名称
     * @param replaceSymbol  替换符号
     * @param parentElement  上级元素类对象
     * @param elementType    查找下级元素对象的类型；0为json，1为xml或html
     * @param isEndElement   是否为尾元素
     * @return 相应的内容
     * @since autest 3.3.0
     */
    private Object getElement(String paramArrayName, String replaceSymbol, Object parentElement, short elementType,
            boolean isEndElement) {
        // 定义数组判断正则
        String arrRegex = String.format(".*\\%s\\d+\\%s", AssertResponse.ARRAY_START_SYMBOL,
                AssertResponse.ARRAY_END_SYMBOL);
        // 转换元素名称，将被替换的符号还原
        String name = paramArrayName.replaceAll(replaceSymbol, AssertResponse.SEPARATE_SYMBOL);

        // 判断当前元素是否为数组元素，若为数组元素，则按照数组方式对元素进行切分
        if (name.matches(arrRegex)) {
            // 获取需要切分数组内容
            Entry<String, Integer> arrData = valueOfArrIndex(name);
            // 判断元素类型，根据不同的类型，对应不同的获取方式
            if (elementType == 0) {
                // 获取元素集合，并根据是否为尾元素，返回相应的内容
                JSONArray arrJson = ((JSONObject) parentElement).getJSONArray(arrData.getKey());
                int index = DisposeCodeUtils.disposeArrayIndex(arrData.getValue(), 0, arrJson.size() - 1, false, false,
                        false);
                if (isEndElement) {
                    return arrJson.getString(index);
                } else {
                    return arrJson.getJSONObject(index);
                }
            } else if (elementType == 1) {
                // 处理下标，并返回相应下标的内容
                List<Element> elementList = ((Element) parentElement).elements(arrData.getKey());
                int index = DisposeCodeUtils.disposeArrayIndex(arrData.getValue(), 0, elementList.size() - 1, false,
                        false, false);
                if (isEndElement) {
                    return elementList.get(index).getText();
                } else {
                    return elementList.get(index);
                }
            } else {
                throw new HttpResponseException("暂不支持的响应体解析类型：" + elementType);
            }
        } else {
            // 判断元素类型，根据不同的类型，对应不同的获取方式
            if (elementType == 0) {
                if (isEndElement) {
                    return Optional.ofNullable(((JSONObject) parentElement).getString(name)).orElse("");
                } else {
                    return ((JSONObject) parentElement).getJSONObject(name);
                }
            } else if (elementType == 1) {
                if (isEndElement) {
                    // 判断最后一位元素是否为属性，若能获取到属性，则返回属性值内容，若不为属性，则获取返回标签中存储的文本
                    Attribute att = ((Element) parentElement).attribute(name);
                    if (att != null) {
                        return att.getText();
                    }
                    return ((Element) parentElement).elementText(name);
                } else {
                    return ((Element) parentElement).element(name);
                }
            } else {
                throw new HttpResponseException("暂不支持的响应体解析类型：" + elementType);
            }
        }
    }

    /**
     * 该方法用于对变量名中的数组下标进行分离，并返回变量名与转换为整形的下标
     * 
     * @param name 待分离的表达式
     * @return 变量名与下标键值对
     * @since autest 3.3.0
     */
    private Entry<String, Integer> valueOfArrIndex(String name) {
        Integer index = Integer.valueOf(name.substring(name.indexOf(AssertResponse.ARRAY_START_SYMBOL) + 1,
                name.indexOf(AssertResponse.ARRAY_END_SYMBOL)));
        String paramName = name.substring(0, name.indexOf(AssertResponse.ARRAY_START_SYMBOL));

        return new Entry<>(paramName, index);
    }

    /**
     * 该方法用于在存在完整边界的情况下，对文本的处理方法
     *
     * @param value         待提取内容
     * @param leftBoundary  提取左边界
     * @param rightBoundary 提取右边界
     * @param index         多词情况下提取下标
     * @return 提取的内容
     * @since autest 3.3.0
     */
    private String disposeCompleteBoundaryText(String value, String leftBoundary, String rightBoundary, int index) {
        // 若value为空串，则直接返回，不做后续处理
        if (value.isEmpty()) {
            return value;
        }

        // 若左右边界不为空，则将其拼接为边界正则
        String boundaryRegex = String.format("%s.+?%s", DisposeCodeUtils.disposeRegexSpecialSymbol(leftBoundary),
                DisposeCodeUtils.disposeRegexSpecialSymbol(rightBoundary));
        // 将断言内容在边界正则中进行提取
        ArrayList<String> valueExtractList = new ArrayList<>();
        Matcher match = Pattern.compile(boundaryRegex).matcher(value);
        while (match.find()) {
            valueExtractList.add(match.group());
        }

        // 判断是否提到内容，若不存在内容，则直接返回false
        int size = valueExtractList.size();
        if (size == 0) {
            return "";
        }
        // 若存在提词内容，则对查找下标进行判断，获取到对应的词语
        // 由于index下标从1开始，且可能传入其他有问题的数字，故需要对下标进行处理
        index = DisposeCodeUtils.customizedIndex2ArrayIndex(index, 1, size, 1, true, false, false, false);

        // 获取相应下标的文本
        String key = valueExtractList.get(index);
        // 判断左右边界在文本中的位置，并对其进行去除
        // 当不存在左边界时，则将左边界位置赋予字符串开头下标，即0
        // 当不存在右边界时，则将右边界赋予字符串最后一个位下标，即key.length()
        int leftIndex = leftBoundary.isEmpty() ? 0 : (key.indexOf(leftBoundary) + leftBoundary.length());
        int rightIndex = rightBoundary.isEmpty() ? key.length() : key.lastIndexOf(rightBoundary);
        return key.substring(leftIndex, rightIndex);
    }

    /**
     * 该方法用于在仅存在单个边界的情况下，对文本的处理方法
     * 
     * @param value         待提取内容
     * @param leftBoundary  提取左边界
     * @param rightBoundary 提取右边界
     * @param index         多词情况下提取下标
     * @return 提取的内容
     * @since autest 3.3.0
     */
    private String disposeSingleBoundaryText(String value, String leftBoundary, String rightBoundary, int index) {
        // 判断非空边界，并获取非空边界内容
        boolean isLeftBoundary = !leftBoundary.isEmpty();
        String boundary = isLeftBoundary ? leftBoundary : rightBoundary;
        // 判断待判断的内容是否包含边界内容，若不包含，则直接返回空
        if (!value.contains(boundary)) {
            return "";
        }

        // 将内容按照边界内容进行切分
        String[] keys = value.split(DisposeCodeUtils.disposeRegexSpecialSymbol(boundary));
        // 得到字符串数组后，若边界为左边界，则数组第一个元素不能作为值返回；若边界为右边界，则数组最后一个元素不能最为值返回
        // 例如“123#456#789”，当用“#”作为左边界时，则“123”左边不存在“#”符号，故不能作为第一个元素，右边界类似
        // 由于index下标从1开始，且可能传入其他有问题的数字，故需要对下标进行处理；不论边界为何种边界，其数组总数均需要减1
        int size = keys.length - 1;
        int minIndex = isLeftBoundary ? 1 : 0;
        if (index < 1) {
            index = minIndex;
        } else if (index >= 1 && index <= size) {
            index -= (1 - minIndex);
        } else {
            index = size - 1 + minIndex;
        }

        return keys[index];
    }

    /**
     * <p>
     * <b>文件名：EasyResponse.java</b>
     * </p>
     * <p>
     * <b>用途：</b> 定义接口响应的信息类，用于对接口响应工具的数据返回
     * </p>
     * <p>
     * <b>编码时间：2022年7月15日 上午8:16:23
     * </p>
     * <p>
     * <b>修改时间：2022年7月15日 上午8:16:23
     * </p>
     *
     * @author 彭宇琦
     * @version Ver1.0
     * @since JDK 1.8
     * @since autest 3.5.0
     */
    private class ResponseInfo {
        /**
         * 请求报文类对象
         */
        public Response response;

        /**
         * 接口响应体内容
         */
        public byte[] responseBody;
        /**
         * 响应头集合
         */
        public HashMap<String, String> responseHeaderMap = new HashMap<>();
        /**
         * 响应状态码
         */
        public int status = 200;
        /**
         * 响应消息
         */
        public String message = "";
        /**
         * 响应体转义字符集
         */
        public String charsetName = InterfaceInfo.DEFAULT_CHARSETNAME;
        /**
         * 存储响应体的格式
         */
        public HashMap<Integer, Set<MessageType>> bodyTypeMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
        /**
         * 记录接口发送请求时的时间戳
         */
        public long sentRequestAtMillis = 0L;
        /**
         * 记录接口收到请求头时的时间戳
         */
        public long receivedResponseAtMillis = 0L;
        /**
         * 记录请求成功后的时间戳
         */
        public long timeAfterRequestAtMillis = 0L;

        /**
         * 记录接口的实际请求
         */
        public InterfaceInfo requestInterInfo = new InterfaceInfo();
    }
}
