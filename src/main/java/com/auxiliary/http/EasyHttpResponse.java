package com.auxiliary.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.auxiliary.tool.regex.ConstType;

import okhttp3.Headers;
import okhttp3.Response;

/**
 * <p>
 * <b>用途：</b>用于对HTTP请求方式的响应报文进行解析，并提供相应的报文处理方法
 * </p>
 * <p>
 * <b>编码时间：2023年12月08日 08:44:05
 * </p>
 * <p>
 * <b>修改时间：2023年12月08日 08:44:05
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 4.5.0
 */
public class EasyHttpResponse extends EasyResponse {
    /**
     * 请求报文类对象
     *
     * @since autest 3.5.0
     */
    protected Response response;
    /**
     * 接口响应体内容
     *
     * @since autest 3.5.0
     * @deprecated 该属性以无意义，类中不再存储响应体对象返回的字节数组，该属性将在5.0.0或后续版本中删除
     */
    @Deprecated
    protected byte[] responseBodyBytes;
    /**
     * 响应头集合
     *
     * @since autest 3.5.0
     */
    protected Map<String, String> responseHeaderMap = new HashMap<>();
    /**
     * 响应状态码
     *
     * @since autest 3.5.0
     */
    protected int status = 200;
    /**
     * 响应消息
     *
     * @since autest 3.5.0
     */
    protected String message = "";

    /**
     * 存储响应体的格式
     *
     * @since autest 3.5.0
     */
    protected Map<Integer, Set<MessageType>> bodyTypeMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);

    /**
     * 记录客户端发送请求时的时间戳
     *
     * @since autest 3.5.0
     */
    protected long sentRequestAtMillis = 0L;
    /**
     * 记录客户端收到请求头时的时间戳
     *
     * @since autest 3.5.0
     */
    protected long receivedResponseAtMillis = 0L;

    /**
     * 构造对象，指定OKHttp响应类
     *
     * @param response  OKHttp响应类
     * @param interInfo 接口信息类对象
     */
    protected EasyHttpResponse(Response response, InterfaceInfo interInfo) {
        // 记录接口实际请求信息
        requestInterInfo = interInfo;
        // 存储响应内容
        this.response = Optional.ofNullable(response).orElseThrow(() -> new HttpResponseException("接口响应结果为空"));

        // 存储响应头
        Headers heads = response.headers();
        for (String head : heads.names()) {
            List<String> valueList = heads.values(head);
            if (valueList.size() < 2) {
                responseHeaderMap.put(head, valueList.get(0));
            } else {
                responseHeaderMap.put(head, valueList.toString());
            }
        }

        // 存储响应状态及消息
        status = response.code();
        message = response.message();

        // 存储请求时间
        sentRequestAtMillis = response.sentRequestAtMillis();
        receivedResponseAtMillis = response.receivedResponseAtMillis();

        // 设置默认的响应字符集编码
        charsetName = interInfo.getResponseCharsetname();
    }

    /**
     * 该方法用于添加响应体的内容格式
     *
     * @param status     状态
     * @param messageSet 内容格式集合
     * @since autest 3.3.0
     */
    public void setMessageType(int status, Set<MessageType> messageSet) {
        bodyTypeMap.put(status, new HashSet<>(messageSet));
    }

    /**
     * 该方法用于返回接口返回的响应头
     *
     * @return 响应头
     * @since autest 3.3.0
     */
    public Map<String, String> getResponseHeaderMap() {
        return responseHeaderMap;
    }

    /**
     * 该方法用于返回接口响应状态码
     *
     * @return 响应状态码
     * @since autest 3.3.0
     */
    public int getStatus() {
        return response.code();
    }

    /**
     * 该方法用于返回接口响应消息
     *
     * @return 响应消息
     * @since autest 3.3.0
     */
    public String getMessage() {
        return message;
    }

    /**
     * 该方法用于返回服务端收到客户端发出请求时的时间戳
     *
     * @return 从客户端发出请求的时刻记录的时间戳
     * @since autest 3.4.0
     */
    public long getSentRequestAtMillis() {
        return sentRequestAtMillis;
    }

    /**
     * 该方法用于返回客户端接收到服务端返回的请求头时的时间戳
     *
     * @return 从服务器接收到请求头时记录的时间戳
     * @since autest 3.4.0
     */
    public long getReceivedResponseAtMillis() {
        return receivedResponseAtMillis;
    }

    /**
     * 该方法用于对客户端发送请求的时间戳与客户端接收到返回的时间戳做差，返回其差值，即接口从请求到响应的时间，单位为毫秒
     *
     * @return 客户端发送请求到客户端收到请求的时间
     * @since autest 4.0.0
     */
    public long getResponseTimeDifferenceAtMillis() {
        return getReceivedResponseAtMillis() - getSentRequestAtMillis();
    }

    /**
     * 该方法用于返回响应体字符数组
     *
     * @return 响应体字符数组
     * @since autest 3.3.0
     */
    public byte[] getResponseBody() {
        return Optional.ofNullable(response.body()).map(t -> {
            try {
                return t.bytes();
            } catch (IOException e) {
                throw new HttpResponseException("响应体类对象异常，无法返回响应体字节数组", e);
            }
        }).orElseThrow(() -> new HttpResponseException("响应体为空，无法生成响应体字节数组"));
    }

    @Override
    public String getResponseBodyText() {
        try {
            return new String(getResponseBody(), charsetName);
        } catch (UnsupportedEncodingException e) {
            throw new HttpResponseException("报文无法转义为字符串", e);
        }
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
        return assertResponse(assertRegex, searchType, leftBoundary, rightBoundary, index);
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
        return assertResponse(assertRegex, searchType, paramName, xpath, "", "", 0);
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
        return assertResponse(assertRegex, searchType, paramName, "");
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
        return assertResponse(assertRegex, SearchType.BODY, paramName);
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
     * </ol>
     * 通过以上方式提取后，若传入了左右边界内容，则继续按照左右边界的方式，再次对已提取的内容进行提取：
     * <ol>
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
        // 需要提词的内容
        String value = "";
        // 提词内容的格式集合，由于在父类中已对null进行处理，故此处无需进行初始化
        Set<MessageType> bodyTypeSet = null;
        // 根据响应体搜索范围，获取响应的文本内容及其格式
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
                    value = responseHeaderMap.toString();
                } else {
                    value = Optional.ofNullable(responseHeaderMap.get(paramName)).orElse("");
                }
                break;
            case BODY:
                // 获取响应体的字符串形式
                value = getResponseBodyText();
                // 判断paramName或xpath参数是否为空串
                if (!paramName.isEmpty() || !xpath.isEmpty()) {
                    // 根据响应状态，获取请求体类型
                    bodyTypeSet = bodyTypeMap.get(status);
                    value = analysisBody(value, paramName, xpath, bodyTypeSet);
                }
                break;
            default:
                throw new HttpResponseException("不支持的断言的接口响应内容：" + searchType);
        }

        return extractKey(value, bodyTypeSet, paramName, xpath, leftBoundary, rightBoundary, index);
    }

    /**
     * 该方法用于通过指定的边界条件对接口响应报文指定内容进行提取，返回提取到的内容
     * <p>
     * 提取规则如下：
     * <ol>
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
}
