package com.auxiliary.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
 *
 */
public class EasyResponse {
    /**
     * 接口响应体内容
     */
    private byte[] responseBody;
    /**
     * 响应头集合
     */
    private HashMap<String, String> responseHeaderMap = new HashMap<>();
    /**
     * 响应状态码
     */
    private int status = 200;
    /**
     * 响应消息
     */
    private String message = "";
    /**
     * 响应报文转义字符集
     */
    private String charsetName = InterfaceInfo.DEFAULT_CHARSETNAME;
    /**
     * 存储响应报文的格式
     */
    private HashSet<MessageType> messageSet = new HashSet<>(ConstType.DEFAULT_MAP_SIZE);

    /**
     * 构造对象，指定OKHttp响应类
     *
     * @param response OKHttp响应类
     */
    protected EasyResponse(Response response) {
        // 存储响应内容
        ResponseBody body = response.body();
        try {
            responseBody = body.bytes();
        } catch (IOException e) {
        }

        // 存储响应头
        Headers heads = response.headers();
        for (String head : heads.names()) {
            responseHeaderMap.put(head, heads.get(head));
        }

        // 存储响应状态及消息
        status = response.code();
        message = response.message();
    }

    /**
     * 该方法用于设置字符集名称
     *
     * @param charsetName 字符集名称
     * @since autest 3.3.0
     */
    public void setCharsetName(String charsetName) {
        this.charsetName = charsetName;
    }

    /**
     * 该方法用于以{@link #setCharsetName(String)}方法设定的编码格式，返回接口的响应报文字符串
     *
     * @param charsetName 编码格式名称
     * @since autest 3.3.0
     */
    public String getResponseBodyText() {
        try {
            return new String(responseBody, charsetName);
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
        return responseBody;
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
        return status;
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
     * 该方法用于对接口响应的指定部分内容进行断言
     *
     * @param assertRegex   断言规则
     * @param searchType    搜索响应内容枚举
     * @param paramName     搜索变量
     * @param leftBoundary  断言内容左边界
     * @param rightBoundary 断言内容右边界
     * @param index         指定下标内容
     * @return 断言结果
     * @since autest 3.3.0
     */
    public boolean assertResponse(String assertRegex, SearchType searchType, String paramName, String leftBoundary,
            String rightBoundary, int index) {
        String value = "";
        switch (searchType) {
        case STATUS:
            value = String.valueOf(getStatus());
            break;
        case MESSAGE:
            value = getMessage();
            break;
        case HEADER:
            if (!responseHeaderMap.containsKey(paramName)) {
                throw new HttpResponseException(String.format("响应头中不包含参数“%s”，无法断言", paramName));
            }
            value = responseHeaderMap.get(paramName);
            break;
        case BODY:
            // TODO 对响应报文进行断言，若类型为图片或文件等，则不支持断言
            break;
        default:
            throw new HttpResponseException("不支持的断言的接口响应内容：" + searchType);
        }

        return assertTextType(assertRegex, value, leftBoundary, rightBoundary, index);
    }

    /**
     * 该方法用于添加响应体的内容格式
     *
     * @param messageSet 内容格式集合
     * @since autest 3.3.0
     */
    protected void setMessageType(HashSet<MessageType> messageSet) {
        this.messageSet.addAll(messageSet);
    }

    /**
     * 该方法用于对文本内容进行断言
     *
     * @param assertRegex   断言规则
     * @param value         待断言内容
     * @param leftBoundary  取词左边界
     * @param rightBoundary 取词右边界
     * @param index         取词下标
     * @return 断言结果
     * @since autest 3.3.0
     */
    private boolean assertTextType(String assertRegex, String value, String leftBoundary, String rightBoundary,
            int index) {
        // 判断左右边界是否都为空，若都为空，则只判内容是否符合断言规则
        if (leftBoundary.isEmpty() && rightBoundary.isEmpty()) {
            return value.matches(assertRegex);
        }

        // 去除获取文本的左右边界，并对剩余内容进行断言规则判断
        value = extractKey(value, leftBoundary, rightBoundary, index);
        return value.substring(value.indexOf(leftBoundary.length()), value.indexOf(rightBoundary)).matches(assertRegex);
    }

    /**
     * 该方法用于对指定内容按照限定规则进行提取，返回提取到的内容
     *
     * @param value         待提取内容
     * @param leftBoundary  提取左边界
     * @param rightBoundary 提取右边界
     * @param index         多词情况下提取下标
     * @return 提取的内容
     * @since autest 3.3.0
     */
    private String extractKey(String value, String leftBoundary, String rightBoundary, int index) {
        // 若左右边界不为空，则将其拼接为边界正则
        String boundaryRegex = rightBoundary + ".*?" + leftBoundary;
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
        if (index < 1) {
            index = 0;
        } else if (index >= 1 && index <= size) {
            index -= 1;
        } else {
            index = size - 1;
        }

        // 去除获取文本的左右边界，并对剩余内容进行断言规则判断
        return valueExtractList.get(index);
    }
}
