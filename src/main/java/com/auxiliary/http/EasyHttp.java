package com.auxiliary.http;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONObject;
import com.auxiliary.datadriven.DataDriverFunction;
import com.auxiliary.datadriven.DataFunction;
import com.auxiliary.datadriven.Functions;
import com.auxiliary.tool.common.AddPlaceholder;
import com.auxiliary.tool.common.DisposeCodeUtils;
import com.auxiliary.tool.common.Entry;
import com.auxiliary.tool.common.Placeholder;
import com.auxiliary.tool.date.TimeUnit;
import com.auxiliary.tool.regex.ConstType;
import com.auxiliary.tool.regex.RegexType;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;

/**
 * <p>
 * <b>文件名：EasyHttp.java</b>
 * </p>
 * <p>
 * <b>用途：</b>根据接口信息，对接口进行请求的工具，亦可通过类中的静态方法，对接口进行快速请求。
 * 工具类允许对参数设置公式和提取词语等方法，并可以设置自动断言，方便接口自动化运行
 * </p>
 * <p>
 * <b>编码时间：2020年6月18日上午7:02:54
 * </p>
 * <p>
 * <b>修改时间：2023年5月23日 上午11:28:54
 * </p>
 *
 *
 * @author 彭宇琦
 * @version Ver2.1
 * @since JDK 1.8
 * @since autest 3.3.0
 */
public class EasyHttp implements AddPlaceholder {
    /**
     * 占位符起始符号
     */
    private final String FUNCTION_START_SIGN = "@{";
    /**
     * 占位符结束符号
     */
    private final String FUNCTION_END_SIGN = "}";

    /**
     * 占位符标记
     */
    private final String FUNCTION_SIGN = FUNCTION_START_SIGN + ".+?" + FUNCTION_END_SIGN;

    /**
     * 定义消息类型为NONE的请求体
     */
    private static final RequestBody NONE_REQUEST_BODY = RequestBody
            .create(MediaType.parse(MessageType.NONE.getMediaValue()), "");

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
     *
     * @deprecated 占位符替换方法已由{@link #placeholder}代替，其属性及方法将在4.3.0或后续版本中删除
     */
    @Deprecated
    private HashMap<String, String> extractMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
    /**
     * 存储断言结果
     */
    private Set<String> assertResultSet = new HashSet<>(ConstType.DEFAULT_MAP_SIZE);
    /**
     * 存储公式内容
     *
     * @deprecated 占位符替换方法已由{@link #placeholder}代替，其属性及方法将在4.3.0或后续版本中删除
     */
    @Deprecated
    private HashMap<String, DataFunction> functionMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
    /**
     * 存储占位符类对象
     *
     * @since autest 4.2.0
     */
    private Placeholder placeholder = new Placeholder(FUNCTION_START_SIGN, FUNCTION_END_SIGN);

    /**
     * 断言失败是否抛出异常
     */
    private boolean isAssertFailThrowException = false;
    /**
     * 定义调用接口前是否自动调用前置操作
     *
     * @since autest 4.3.0
     */
    private boolean isAutoBeforeOperation = true;

    /**
     * 定义默认连接超时时间，仅对使用默认时间的静态方法生效
     */
    public static Entry<Long, TimeUnit> connectTime = InterfaceInfo.DEFAULT_CONNECT_TIME;

    /**
     * 该方法用于添加数据处理函数
     * <p>
     * 可通过lambda添加公式对数据处理的方式，例如，将文本中的存在的"a()"全部替换为文本“test”，则可按如下写法： <code><pre>
     * addFunction(new DataDriverFunction("a\\(\\)", text -&gt; "test"));
     * </pre></code>
     * </p>
     * <p>
     * 可添加{@link Functions}类中预设的函数
     * </p>
     *
     * @param functions 数据处理函数
     * @return 类本身
     * @since autest 3.3.0
     */
    public EasyHttp addFunction(DataDriverFunction functions) {
        if (functions != null) {
            placeholder.addReplaceFunction(functions.getRegex(), functions.getFunction());
        }

        return this;
    }

    @Override
    public void addReplaceFunction(String regex, DataFunction function) {
        placeholder.addReplaceFunction(regex, function);
    }

    /**
     * 该方法用于添加待替换的关键词及相应的替换内容
     *
     * @param key   待替换关键词
     * @param value 替换内容
     * @return 类本身
     * @since autest 3.3.0
     * @deprecated 该方法由{@link #addReplaceWord(String, String)}代替，将在4.3.0或后续版本中删除
     */
    @Deprecated
    public EasyHttp addReplaceKey(String key, String value) {
        placeholder.addReplaceWord(key, value);

        return this;
    }

    @Override
    public void addReplaceWord(String word, String replaceWord) {
        placeholder.addReplaceWord(word, replaceWord);
    }

    /**
     * 该方法用于返回指定待替换关键词的内容
     *
     * @param key 待替换关键词
     * @return 替换内容
     * @since autest 3.3.0
     */
    public String getReplaceKey(String key) {
        return placeholder.replaceWord(key);
    }

    /**
     * 该方法用于设置自动断言失败时，是否需要抛出异常
     *
     * @param isAssertFailThrowException 断言失败是否抛出异常
     * @since autest 3.3.0
     * @deprecated 命名不规范，已由{@link #setAssertFailThrowException(boolean)}方法代替，将在3.8.0或以上版本中删除
     */
    @Deprecated
    public void isAssertFailThrowException(boolean isAssertFailThrowException) {
        this.isAssertFailThrowException = isAssertFailThrowException;
    }

    /**
     * 该方法用于设置自动断言失败时，是否需要抛出异常
     *
     * @param isAssertFailThrowException 断言失败是否抛出异常
     * @return 类本身
     * @since autest 3.6.0
     */
    public EasyHttp setAssertFailThrowException(boolean isAssertFailThrowException) {
        this.isAssertFailThrowException = isAssertFailThrowException;
        return this;
    }

    /**
     * 该方法用于设置在请求接口时，是否自动调用接口的前置操作
     *
     * @param isAutoBeforeOperation 是否自动调用接口的前置操作
     * @return 类本身
     * @since autest 4.3.0
     */
    public EasyHttp setAutoBeforeOperation(boolean isAutoBeforeOperation) {
        this.isAutoBeforeOperation = isAutoBeforeOperation;
        return this;
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
    @SuppressWarnings("unchecked")
    public EasyResponse requst(InterfaceInfo interInfo) {
        // 判断是否自动调用前置操作，若需要，则获取接口的前置操作，并根据操作枚举，执行相应的前置操作
        if (isAutoBeforeOperation) {
            List<BeforeInterfaceOperation> beforeOperation = interInfo.getBeforeOperationList();
            for (BeforeInterfaceOperation before : beforeOperation) {
                // 判断当前前置操作是否存在剩余执行次数
                if (before.isExecutedOperation()) {
                    switch (before.getOperationType()) {
                    case INTERFACE:
                        before.actionInterface(this);
                        break;
                    default:
                        continue;
                    }
                }
            }
        }

        // 处理请求头信息
        Map<String, String> newHeadMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
        interInfo.getRequestHeaderMap()
                .forEach((key, value) -> newHeadMap.put(placeholder.replaceText(key), placeholder.replaceText(value)));

        // 添加cookies信息
        String cookiesExpression = interInfo.getCookieExpression();
        if (!cookiesExpression.isEmpty()) {
            newHeadMap.put("Cookie", placeholder.replaceText(cookiesExpression));
        }

        // 对接口进行请求，获取响应类
        Entry<MessageType, Object> body = interInfo.getBodyContent();
        // 获取请求体内容，若请求体为字符串，则对请求体进行处理
        Object bodyContent = body.getValue();
        if (bodyContent instanceof String) {
            bodyContent = placeholder.replaceText((String) bodyContent);
        } else if (bodyContent instanceof List) {
            for (Entry<String, Object> data : (List<Entry<String, Object>>) bodyContent) {
                Object dataValue = data.getValue();
                if (!(dataValue instanceof File)) {
                    data.setValue(placeholder.replaceText(dataValue.toString()));
                }
            }
        }

        // 获取接口的超时时间
        Entry<Long, TimeUnit> connectTime = interInfo.getConnectTime();
        EasyResponse response = requst(interInfo.getRequestType(), placeholder.replaceText(interInfo.toUrlString()),
                newHeadMap, body.getKey(), bodyContent, connectTime.getKey(), connectTime.getValue());
        // 设置响应体解析字符集
        response.setCharsetName(interInfo.getCharsetname());
        // 设置响应体内容格式
        interInfo.getAllSaveState()
                .forEach(status -> response.setMessageType(status, interInfo.getResponseContentType(status)));

        // 对响应报文提词
        interInfo.getExtractRuleJson().stream().map(JSONObject::parseObject).forEach(json -> {
            // 获取传参
            String saveName = json.getString(ExtractResponse.JSON_EXTRACT_SAVE_NAME);
            SearchType searchType = SearchType.typeText2Type(json.getString(ExtractResponse.JSON_EXTRACT_SEARCH));
            String paramName = placeholder.replaceText(json.getString(ExtractResponse.JSON_EXTRACT_PARAM_NAME));
            String xpath = placeholder.replaceText(json.getString(ExtractResponse.JSON_EXTRACT_XPATH));
            String lb = placeholder.replaceText(json.getString(ExtractResponse.JSON_EXTRACT_LB));
            String rb = placeholder.replaceText(json.getString(ExtractResponse.JSON_EXTRACT_RB));
            int index = Optional.ofNullable(json.getString(ExtractResponse.JSON_EXTRACT_ORD))
                    .filter(RegexType.INTEGER::judgeString).map(Integer::valueOf).orElse(1);

            // 存储提词结果
            addReplaceWord(saveName, response.extractKey(searchType, paramName, xpath, lb, rb, index));
        });

        // 自动断言
        assertResultSet.clear();
        interInfo.getAssertRuleJson().stream().map(JSONObject::parseObject).forEach(json -> {
            // 获取传参
            String assertRegex = placeholder.replaceText(json.getString(AssertResponse.JSON_ASSERT_ASSERT_REGEX));
            SearchType searchType = SearchType.typeText2Type(json.getString(AssertResponse.JSON_ASSERT_SEARCH));
            String paramName = placeholder.replaceText(json.getString(AssertResponse.JSON_ASSERT_PARAM_NAME));
            String xpath = placeholder.replaceText(json.getString(AssertResponse.JSON_ASSERT_XPATH));
            String lb = placeholder.replaceText(json.getString(AssertResponse.JSON_ASSERT_LB));
            String rb = placeholder.replaceText(json.getString(AssertResponse.JSON_ASSERT_RB));
            int index = Integer.valueOf(json.getString(AssertResponse.JSON_ASSERT_ORD));

            // 断言
            boolean result = response.assertResponse(assertRegex, searchType, paramName, xpath, lb, rb, index);
            // 判断是否需要抛出异常
            if (!result && isAssertFailThrowException) {
                String responseText = response.extractKey(searchType, paramName, xpath, lb, rb, index);
                throw new HttpResponseException(String.format("接口响应报文断言失败，断言规则为“%s”，接口响应报文实际检索内容为“%s”，接口信息：“%s”",
                        assertRegex, responseText, interInfo.toString()));
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
     * <p>
     * <b>注意：</b> 当请求体为表单类型时，其body必须是“List&lt;Entry&lt;String,
     * Object&gt;&gt;”类型；当请求体为文件类型时，则body必须是“File”类型
     * </p>
     *
     * @param requestType 请求类型
     * @param url         接口url地址
     * @param requestHead 请求头集合
     * @param messageType 请求体内容格式类型
     * @param body        请求体内容
     * @return 接口响应类
     * @since autest 3.3.0
     */
    public static EasyResponse requst(RequestType requestType, String url, Map<String, String> requestHead,
            MessageType messageType, Object body) {
        // 若类中存储的超时时间为Null，则将其处理为默认的超时时间
        connectTime = Optional.ofNullable(connectTime).orElse(InterfaceInfo.DEFAULT_CONNECT_TIME);
        return requst(requestType, url, requestHead, messageType, body, connectTime.getKey(), connectTime.getValue());
    }

    /**
     * 该方法用于对接口进行快速请求
     *
     * @param requestType 请求类型
     * @param url         接口url地址
     * @param messageType 请求体内容格式类型
     * @param body        请求体内容
     * @return 接口响应类
     * @since autest 3.4.0
     */
    public static EasyResponse requst(RequestType requestType, String url, MessageType messageType, Object body) {
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
     * @deprecated 占位符替换方法已由{@link #placeholder}代替，其属性及方法将在4.3.0或后续版本中删除
     */
    @SuppressWarnings("unused")
    @Deprecated
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

    /**
     * 该方法用于判断指定的请求类型是否需要请求体参数
     *
     * @param requestType 请求类型枚举
     * @return 对应的请求类型是否需要请求体
     * @since autest 3.4.0
     */
    private static boolean isNoBodyRequest(RequestType requestType) {
        return Optional.of(requestType).filter(type -> type != RequestType.GET).filter(type -> type != RequestType.HEAD)
                .isPresent();
    }

    /**
     * 该方法用于对接口进行快速请求
     * <p>
     * <b>注意：</b> 当请求体为表单类型时，其body必须是“List&lt;Entry&lt;String,
     * Object&gt;&gt;”类型；当请求体为文件类型时，则body必须是“File”类型
     * </p>
     *
     * @param requestType 请求类型
     * @param url         接口url地址
     * @param requestHead 请求头集合
     * @param messageType 请求体内容格式类型
     * @param body        请求体内容
     * @param connectTime 接口连接时间
     * @param timeUnit    时间单位枚举
     * @return 接口响应类
     * @since autest 3.6.0
     */
    @SuppressWarnings("unchecked")
    private static EasyResponse requst(RequestType requestType, String url, Map<String, String> requestHead,
            MessageType messageType, Object body, long connectTime, TimeUnit timeUnit) {
        // 处理传入的时间，并将传入的超时时间转换为毫秒值
        timeUnit = Optional.ofNullable(timeUnit).orElse(TimeUnit.SECOND);
        connectTime = (connectTime <= 0L ? 0L : (connectTime * timeUnit.getToMillisNum()));

        // 定义请求客户端
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(connectTime, java.util.concurrent.TimeUnit.MILLISECONDS)
                .readTimeout(connectTime, java.util.concurrent.TimeUnit.MILLISECONDS)
                .writeTimeout(connectTime, java.util.concurrent.TimeUnit.MILLISECONDS).build();

        // 构造请求体，并添加接口信息中的请求参数、请求头和请求体信息
        RequestBody requestBody = null;
        // 判断当前请求类型是否需要添加请求体
        if (isNoBodyRequest(requestType)) {
            // 根据消息格式，创建RequestBody类对象
            messageType = Optional.ofNullable(messageType).orElse(MessageType.NONE);
            MediaType mediaType = MediaType.parse(messageType.toMessageTypeString());
            switch (messageType) {
            case JSON:
            case XML:
            case HTML:
                requestBody = RequestBody.create(mediaType, Optional.ofNullable(body).map(Object::toString).orElse(""));
                break;
            case FILE:
            case BINARY:
                requestBody = RequestBody.create(mediaType, (File) body);
                break;
            case NONE:
                requestBody = NONE_REQUEST_BODY;
                break;
            case FORM_DATA:
                // 定义表单请求体构造类
                MultipartBody.Builder builder = new MultipartBody.Builder();
                // 获取表单数据
                List<Entry<String, Object>> fromDataList = (List<Entry<String, Object>>) body;

                // 遍历所有的数据，并将其添加到表单构造类中
                for (Entry<String, Object> data : fromDataList) {
                    Object value = data.getValue();
                    // 判断值类型是否为文件类型，若为文件类型，则需要进行特殊处理
                    if (value instanceof File) {
                        File file = (File) value;
                        builder.addFormDataPart(data.getKey(), file.getAbsolutePath(),
                                RequestBody.create(MediaType.parse("application/octet-stream"), file));
                    } else {
                        // 若不为文件类型，则直接存储相应的文本内容
                        builder.addFormDataPart(data.getKey(), value.toString());
                    }
                }
                // 遍历所有数据后，构造请求体对象
                requestBody = builder.build();
                break;
            case X_WWW_FORM_URLENCODED:
                requestBody = RequestBody.create(mediaType,
                        HttpUtil.formUrlencoded2Extract((List<Entry<String, Object>>) body));
                break;
            default:
                throw new HttpRequestException(String.format("暂时不支持“%s”类型的消息格式，请求接口：%s", messageType, url));
            }
        }

        // 构造请求报文
        Builder requestBuilder = new Request.Builder().url(url).method(Optional.ofNullable(requestType)
                .map(RequestType::toString).orElseThrow(() -> new HttpRequestException("当前接口未指定请求类型：" + url)),
                requestBody);
        if (requestHead != null) {
            requestHead.forEach(requestBuilder::addHeader);
        }

        // 对接口进行请求
        Request request = requestBuilder.build();
        try {
            InterfaceInfo info = new InterfaceInfo();
            info.analysisUrl(url);
            info.setRequestType(requestType);
            info.setBodyContent(messageType, body);
            info.addRequestHeaderMap(requestHead);
            return new EasyResponse(client.newCall(request).execute(), info);
        } catch (SocketTimeoutException e) {
            throw new HttpRequestException(String.format("接口请求超时，请求设置超时时间为%d毫秒，接口信息为：%s", connectTime, url), e);
        } catch (IOException e) {
            throw new HttpRequestException(String.format("接口请求异常，接口信息为：%s", url), e);
        } catch (HttpResponseException e) {
            throw new HttpResponseException(String.format("%s，接口信息为：%s", e.getMessage(), url), e);
        }
    }
}
