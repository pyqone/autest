package com.auxiliary.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
    private HashMap<Integer, Set<MessageType>> bodyTypeMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);

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
    public void setMessageType(int status, Set<MessageType> messageSet) {
        bodyTypeMap.put(status, new HashSet<>(messageSet));
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
    public boolean assertResponse(String assertRegex, SearchType searchType, String paramName, String xpath,
            String leftBoundary, String rightBoundary, int index) {
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
            value = analysisBody(paramName, xpath);
            break;
        default:
            throw new HttpResponseException("不支持的断言的接口响应内容：" + searchType);
        }

        return assertTextType(assertRegex, value, leftBoundary, rightBoundary, index);
    }

    /**
     * 该方法用于根据指定的响应体内容格式，转义响应体，并根据查找参数或xpath对响应元素内容进行查找，返回找到的元素内容
     *
     * @param paramName 查找元素名称
     * @param xpath     查找xml的xpath
     * @return 查找到元素的内容
     * @since autest 3.3.0
     */
    private String analysisBody(String paramName, String xpath) {
        // 根据响应状态，获取请求体类型
        Set<MessageType> bodyTypeSet = Optional.ofNullable(bodyTypeMap.get(status))
                .orElseGet(() -> new HashSet<>());

        // 获取响应体的字符串形式
        String bodyText = getResponseBodyText();

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
            String value = Optional.ofNullable(xml.selectSingleNode("xpath")).map(ele -> ((Element) ele).getText()).orElse("");
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
                if (isEndElement) {
                    return arrJson.getString(arrData.getValue());
                } else {
                    return arrJson.getJSONObject(arrData.getValue());
                }
            } else if (elementType == 1) {
                if (isEndElement) {
                    return ((Element) parentElement).elements(arrData.getKey()).get(arrData.getValue()).getText();
                } else {
                    return ((Element) parentElement).elements(arrData.getKey()).get(arrData.getValue());
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
