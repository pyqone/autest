package com.auxiliary.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONObject;
import com.auxiliary.datadriven.DataDriverFunction;
import com.auxiliary.datadriven.DataFunction;
import com.auxiliary.datadriven.Functions;
import com.auxiliary.tool.common.DisposeCodeUtils;
import com.auxiliary.tool.regex.ConstType;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;

/**
 * <p>
 * <b>文件名：AuHttp.java</b>
 * </p>
 * <p>
 * <b>用途：</b>根据接口信息，对接口进行请求的工具，亦可通过类中的静态方法，对接口进行快速请求。
 * 工具类允许对参数设置公式和提取词语等方法，并可以设置自动断言，方便接口自动化运行
 * </p>
 * <p>
 * <b>编码时间：2020年6月18日上午7:02:54
 * </p>
 * <p>
 * <b>修改时间：2022年5月9日 上午8:30:33
 * </p>
 *
 *
 * @author 彭宇琦
 * @version Ver2.0
 * @since JDK 1.8
 * @since autest 3.3.0
 */
public class EasyHttp {
    /**
     * 占位符标记
     */
    private final String FUNCTION_SIGN = "@\\{.+?\\}";
    /**
     * 占位符起始符号
     */
    private final String FUNCTION_START_SIGN = "@\\{";
    /**
     * 占位符结束符号
     */
    private final String FUNCTION_END_SIGN = "\\}";
    
    /**
     * 定义断言结果json的接口信息字段名
     */
    public static final String ASSERT_RESULT_JSON_INTER_INFO = "interInfo";
    /**
     * 定义断言结果json的结果字段名
     */
    public static final String ASSERT_RESULT_JSON_RESULT = "result";

    /**
     * 存储提词内容
     */
    private HashMap<String, String> extractMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
    /**
     * 存储断言结果
     */
    private Set<String> assertResultSet = new HashSet<>(ConstType.DEFAULT_MAP_SIZE);
    /**
     * 存储公式内容
     */
    private HashMap<String, DataFunction> functionMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);

    /**
     * 断言失败是否抛出异常
     */
    private boolean isAssertFailThrowException = false;

    /**
     * 该方法用于添加数据处理函数
     * <p>
     * 可通过lambda添加公式对数据处理的方式，例如，将文本中的存在的"a()"全部替换为文本“test”，则可按如下写法： <code><pre>
     * addFunction(new DataDriverFunction("a\\(\\)", text -> "test"));
     * </pre></code>
     * </p>
     * <p>
     * 可添加{@link Functions}类中预设的函数
     * </p>
     *
     * @param function 数据处理函数
     * @since autest 3.3.0
     */
    public void addFunction(DataDriverFunction function) {
        if (function != null) {
            functionMap.put(function.getRegex(), function.getFunction());
        }
    }

    /**
     * 该方法用于添加待替换的关键词及相应的替换内容
     *
     * @param key   待替换关键词
     * @param value 替换内容
     * @since autest 3.3.0
     */
    public void addReplaceKey(String key, String value) {
        Optional.ofNullable(key).filter(k -> !k.isEmpty()).ifPresent(k -> {
            extractMap.put(k, Optional.ofNullable(value).orElse(""));
        });
    }

    /**
     * 该方法用于返回指定待替换关键词的内容
     *
     * @param key 待替换关键词
     * @return 替换内容
     * @since autest 3.3.0
     */
    public String getReplaceKey(String key) {
        return extractMap.get(key);
    }

    /**
     * 该方法用于设置自动断言失败时，是否需要抛出异常
     * 
     * @param isAssertFailThrowException 断言失败是否抛出异常
     * @since autest 3.3.0
     */
    public void isAssertFailThrowException(boolean isAssertFailThrowException) {
        this.isAssertFailThrowException = isAssertFailThrowException;
    }

    /**
     * 该方法用于返回请求接口后自动断言的结果集合
     * 
     * @return 断言结果集合
     * @since autest 3.3.0
     */
    public Set<String> getAssertResult() {
        return assertResultSet;
    }

    /**
     * 该方法用于根据接口信息，对接口进行请求，并返回响应内容
     *
     * @param interInfo 接口信息类对象
     * @return 接口响应类
     * @since autest 3.3.0
     */
    public EasyResponse requst(InterfaceInfo interInfo) {
        // 处理请求头信息
        Map<String, String> newHeadMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
        interInfo.getRequestHeaderMap()
                .forEach((key, value) -> newHeadMap.put(disposeContent(key), disposeContent(value)));
        // 对接口进行请求，获取响应类
        EasyResponse response = requst(interInfo.getRequestType(), disposeContent(interInfo.toUrlString()), newHeadMap,
                interInfo.getBody().getKey(), disposeContent(interInfo.getBody().getValue()));
        // 设置响应体解析字符集
        response.setCharsetName(interInfo.getCharsetname());
        // 设置响应体内容格式
        interInfo.getAllSaveState()
                .forEach(status -> response.setMessageType(status, interInfo.getResponseContentType(status)));

        // 对响应报文提词
        interInfo.getExtractRuleJson().stream().map(JSONObject::parseObject).forEach(json -> {
            // 获取传参
            String saveName = json.getString(ExtractResponse.JSON_EXTRACT_SAVE_NAME);
            SearchType searchType = SearchType.valueOf(json.getString(ExtractResponse.JSON_EXTRACT_SEARCH));
            String paramName = json.getString(ExtractResponse.JSON_EXTRACT_PARAM_NAME);
            String xpath = json.getString(ExtractResponse.JSON_EXTRACT_XPATH);
            String lb = json.getString(ExtractResponse.JSON_EXTRACT_LB);
            String rb = json.getString(ExtractResponse.JSON_EXTRACT_RB);
            int index = Integer.valueOf(json.getString(ExtractResponse.JSON_EXTRACT_ORD));

            // 存储提词结果
            addReplaceKey(saveName, response.extractKey(searchType, paramName, xpath, lb, rb, index));
        });

        // 自动断言
        assertResultSet.clear();
        interInfo.getAssertRuleJson().stream().map(JSONObject::parseObject).forEach(json -> {
            // 获取传参
            String assertRegex = json.getString(AssertResponse.JSON_ASSERT_ASSERT_REGEX);
            SearchType searchType = SearchType.valueOf(json.getString(AssertResponse.JSON_ASSERT_SEARCH));
            String paramName = json.getString(AssertResponse.JSON_ASSERT_PARAM_NAME);
            String xpath = json.getString(AssertResponse.JSON_ASSERT_XPATH);
            String lb = json.getString(AssertResponse.JSON_ASSERT_LB);
            String rb = json.getString(AssertResponse.JSON_ASSERT_RB);
            int index = Integer.valueOf(json.getString(AssertResponse.JSON_ASSERT_ORD));

            // 断言
            boolean result = response.assertResponse(assertRegex, searchType, paramName, xpath, lb, rb, index);
            // 判断是否需要抛出异常
            if (!result && isAssertFailThrowException) {
                String responseText = response.extractKey(searchType, paramName, xpath, lb, rb, index);
                throw new HttpResponseException(
                        String.format("接口响应报文断言失败，断言规则为“%s”，接口响应报文实际检索内容为“%s”，接口信息：“%s”", assertRegex, responseText,
                                interInfo.toString()));
            }

            // 将断言结果和接口信息写入json中，并将其文本形式存储至assertResultSet中
            json.put(ASSERT_RESULT_JSON_RESULT, result);
            json.put(ASSERT_RESULT_JSON_INTER_INFO, JSONObject.parseObject(interInfo.toString()));
            assertResultSet.add(json.toJSONString());
        });


        return response;
    }

    /**
     * 该方法用于对接口进行快速请求
     *
     * @param requestType 请求类型
     * @param url         接口url地址
     * @param requestHead 请求头集合
     * @param messageType 请求报文类型
     * @param body        请求体
     * @return 接口响应类
     * @since autest 3.3.0
     */
    public static EasyResponse requst(RequestType requestType, String url, Map<String, String> requestHead,
            MessageType messageType, String body) {
        // 定义请求客户端
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        // 构造请求体，并添加接口信息中的请求参数、请求头和请求体信息
        RequestBody requestBody = null;
        // 当请求方式为get或head时，则不需要添加请求体
        if (requestType != RequestType.GET && requestType != RequestType.HEAD) {
            requestBody = RequestBody.create(
                    MediaType.parse(Optional.ofNullable(messageType).map(MessageType::getMediaValue).orElse("none")),
                    Optional.ofNullable(body).orElse(""));
        }
        
        Builder requestBuilder = new Request.Builder().url(url).method(requestType.toString(), requestBody);
        if (requestHead != null) {
            requestHead.forEach(requestBuilder::addHeader);
        }

        // 对接口进行请求
        Request request = requestBuilder.build();
        try {
            return new EasyResponse(client.newCall(request).execute());
        } catch (IOException e) {
            throw new HttpRequestException(String.format("接口请求异常，接口信息为：%s", url), e);
        } catch (HttpResponseException e) {
            throw new HttpResponseException(String.format("%s，接口信息为：%s", e.getMessage(), url), e);
        }
    }

    /**
     * 该方法用于对接口进行快速请求
     *
     * @param requestType 请求类型
     * @param url         接口url地址
     * @param messageType 请求报文类型
     * @param body        请求体
     * @return 接口响应类
     * @since autest 3.4.0
     */
    public static EasyResponse requst(RequestType requestType, String url, MessageType messageType, String body) {
        return requst(requestType, url, null, messageType, body);
    }

    /**
     * 该方法用于对接口进行快速请求
     *
     * @param requestType 请求类型
     * @param url         接口url地址
     * @return 接口响应类
     * @since autest 3.4.0
     */
    public static EasyResponse requst(RequestType requestType, String url) {
        return requst(requestType, url, null, null, null);
    }

    /**
     * 该方法用于以get请求的方式对接口进行快速请求
     *
     * @param url 接口url地址
     * @return 接口响应类
     * @since autest 3.3.0
     */
    public static EasyResponse requst(String url) {
        return requst(RequestType.GET, url, null, null, null);
    }

    /**
     * 该方法用于对所有可以使用公式的文本进行处理，返回处理后的文本
     *
     * @param content 待替换文本
     * @return 处理后的文本
     * @since autest 3.3.0
     */
    private String disposeContent(String content) {
        // 判断需要才处理的内容是否为空或是否包含公式标志，若存在，则返回content
        String matchPrefix = ".*";
        if (content == null || content.isEmpty() || !content.matches(matchPrefix + FUNCTION_SIGN + matchPrefix)) {
            return content;
        }

        // 通过函数标志对文本中的函数或方法进行提取
        Matcher m = Pattern.compile(FUNCTION_SIGN).matcher(content);
        while (m.find()) {
            // 去除标记符号，获取关键词
            String signKey = m.group();
            String key = signKey.replaceAll(FUNCTION_START_SIGN, "").replaceAll(FUNCTION_END_SIGN, "");

            // 判断关键词是否为已存储的词语
            if (extractMap.containsKey(key)) {
                content = content.replaceAll(DisposeCodeUtils.disposeRegexSpecialSymbol(signKey), extractMap.get(key));
                continue;
            }

            // 若不是存储的词语，则判断关键词是否符合公式集合中的内容，符合公式，则使用公式对内容进行替换
            for (String funKey : functionMap.keySet()) {
                if (key.matches(funKey)) {
                    content = content.replaceAll(DisposeCodeUtils.disposeRegexSpecialSymbol(signKey),
                            functionMap.get(funKey).apply(key));
                    break;
                }
            }

        }

        return content;
    }
}
