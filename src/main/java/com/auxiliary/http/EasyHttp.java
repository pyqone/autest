package com.auxiliary.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
     * 存储提词内容
     */
    private HashMap<String, String> extractMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
    /**
     * 存储公式内容
     */
    private HashMap<String, DataFunction> functionMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);

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
        EasyResponse response = requst(interInfo.getRequestType(), interInfo.toUrlString(), newHeadMap,
                interInfo.getBody().getKey(), disposeContent(interInfo.getBody().getValue()));
        // 设置响应体解析字符集
        response.setCharsetName(interInfo.getCharsetname());
        // 设置响应体内容格式
        interInfo.getAllSaveState()
                .forEach(status -> response.setMessageType(status, interInfo.getResponseContentType(status)));

        // TODO 添加自动断言及提词逻辑

        return response;
    }

    /**
     * 该方法用于对接口进行快速请求
     * <p>
     * <b>注意：</b>该方法为静态方法，将不会使用类中（若已构造对象）存储替换词语和公式，若
     * </p>
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
        if (messageType != null && messageType != MessageType.NONE && body != null && requestType != RequestType.GET
                && requestType != RequestType.HEAD && requestType != RequestType.DELETE) {
            requestBody = RequestBody.create(MediaType.parse(messageType.getMediaValue()), body);
        }
        Builder requestBuilder = new Request.Builder().url(url).method(requestType.toString(), requestBody);
        if (requestHead != null) {
            requestHead.forEach(requestBuilder::addHeader);
        }

        // 对接口进行请求
        Request request = requestBuilder.build();
        try {
            // TODO 修改响应工具后，再来修改此处代码
            return new EasyResponse(client.newCall(request).execute());
        } catch (IOException e) {
            throw new HttpRequestException(String.format("接口请求异常，接口信息为：%s", url), e);
        } catch (HttpResponseException e) {
            throw new HttpResponseException(String.format("%s，接口信息为：%s", e.getMessage(), url), e);
        }
    }

    /**
     * 该方法用于以get请求的方式对接口发起请求
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
