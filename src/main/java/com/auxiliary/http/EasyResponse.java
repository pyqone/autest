package com.auxiliary.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
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
     * 响应体转义字符集
     */
    private String charsetName = InterfaceInfo.DEFAULT_CHARSETNAME;
    /**
     * 存储响应体的格式
     */
    private HashSet<MessageType> bodyTypeSet = new HashSet<>(ConstType.DEFAULT_MAP_SIZE);

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
     * 该方法用于添加响应体的内容格式
     *
     * @param bodyTypeSet 内容格式集合
     * @since autest 3.3.0
     */
    public void setMessageType(HashSet<MessageType> messageSet) {
        this.bodyTypeSet.clear();
        this.bodyTypeSet.addAll(messageSet);
    }

    /**
     * 该方法用于以{@link #setCharsetName(String)}方法设定的编码格式，返回接口的响应体字符串
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
     * @param searchType    搜索范围枚举
     * @param paramName     搜索变量
     * @param leftBoundary  断言内容左边界
     * @param rightBoundary 断言内容右边界
     * @param index         指定下标内容
     * @return 断言结果
     * @since autest 3.3.0
     */
    public boolean assertResponse(String assertRegex, SearchType searchType, String paramName, String leftBoundary,
            String rightBoundary, int index) {
        // 考虑searchType为null的情况
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
            // TODO 对响应体进行断言，若类型为图片或文件等，则不支持断言
            break;
        default:
            throw new HttpResponseException("不支持的断言的接口响应内容：" + searchType);
        }

        return assertTextType(assertRegex, value, leftBoundary, rightBoundary, index);
    }

    private String analysisBody(String paramName, String xpath) {
        // 获取响应体的字符串形式
        String bodyText = getResponseBodyText();

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
        if (bodyTypeSet.contains(MessageType.JSON)) {
            try {
                return disposeJsonParam(JSONObject.parseObject(bodyText), replaceSymbol, paramNames);
            } catch (JSONException e) {
            } catch (HttpResponseException e) {
                // 当抛出HttpResponseException异常时，说明转换变量存在问题，则抛出带说明的异常
                StringJoiner messageText = new StringJoiner("→");
                for (String str : paramNames) {
                    messageText.add(str.replaceAll(replaceSymbol, AssertResponse.SEPARATE_SYMBOL));
                }

                throw new HttpResponseException("无法查找json类型的响应体变量链路：" + messageText.toString());
            }
        }
    }

    /**
     * 该方法用于处理响应体为json串时变量的获取
     *
     * @param bodyText
     * @param replaceSymbol
     * @param paramNames
     * @return
     * @since autest
     */
    private String disposeJsonParam(JSONObject json, String replaceSymbol, String[] paramNames) {
        // 定义数组判断正则
        String arrRegex = String.format(".*%s\\d+%s", AssertResponse.ARRAY_START_SYMBOL,
                AssertResponse.ARRAY_END_SYMBOL);

        // 对转换过程中的异常进行处理，替换为HttpResponseException异常
        try {
            // 按照参数名，向下获取json串，直至达到目标前一个json串
            JSONObject paramJson = json;
            int index = 0;
            for (; index < paramNames.length - 1; index++) {
                // 将被替换的转义符号还原为原来的符号
                String name = paramNames[index].replaceAll(replaceSymbol, AssertResponse.SEPARATE_SYMBOL);
                // 判断当前是否需要查找数组（存在数组正则判定）
                if (name.matches(arrRegex)) {
                    // 获取需要切分数组下标
                    int arrIndex = Integer.valueOf(name.substring(name.indexOf(AssertResponse.ARRAY_START_SYMBOL) + 1,
                            name.indexOf(AssertResponse.ARRAY_END_SYMBOL)));
                    // 按照数组方式，对元素进行获取
                    JSONArray arr = paramJson
                            .getJSONArray(name.substring(0, name.indexOf(AssertResponse.ARRAY_START_SYMBOL)));
                    // 将paramJson指向获取的Json串
                    paramJson = arr.getJSONObject(arrIndex);
                } else {
                    paramJson = paramJson.getJSONObject(name);
                }
            }

            // 处理末尾的变量名
            // 判断当前是否需要查找数组（存在数组正则判定）
            String name = paramNames[index].replaceAll(replaceSymbol, AssertResponse.SEPARATE_SYMBOL);
            if (name.matches(arrRegex)) {
                // 获取需要切分数组下标
                int arrIndex = Integer.valueOf(name.substring(name.indexOf(AssertResponse.ARRAY_START_SYMBOL) + 1,
                        name.indexOf(AssertResponse.ARRAY_END_SYMBOL)));
                // 按照数组方式，对元素进行获取
                JSONArray arr = paramJson
                        .getJSONArray(name.substring(0, name.indexOf(AssertResponse.ARRAY_START_SYMBOL)));
                // 将paramJson指向获取的Json串
                return arr.getString(arrIndex);
            } else {
                return paramJson.getString(name);
            }
        } catch (Exception e) {
            throw new HttpResponseException();
        }
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
