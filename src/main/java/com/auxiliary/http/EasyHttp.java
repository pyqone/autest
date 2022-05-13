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
import com.auxiliary.tool.common.Entry;
import com.auxiliary.tool.regex.ConstType;
import com.auxiliary.tool.regex.RegexType;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;

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
        // 定义请求客户端
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        // 构造请求体，并添加接口信息中的请求参数、请求头和请求体信息
        Builder requestBuilder = new Request.Builder().url(disposeContent(interInfo.toUrlString()));
        setRequstType(interInfo, requestBuilder);
        addHeader(interInfo, requestBuilder);

        // 对接口进行请求
        Request request = requestBuilder.build();
        try {
            // TODO 修改响应工具后，再来修改此处代码
            Response response = client.newCall(request).execute();
            // TODO 修改响应工具后，添加自动断言和提词代码
            return new EasyResponse(response.body().string());
        } catch (IOException e) {
            throw new HttpRequestException(String.format("接口请求异常，接口信息：%s", interInfo.toString()), e);
        }
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
            Response response = client.newCall(request).execute();
            return new EasyResponse(response.body().string());
        } catch (IOException e) {
            throw new HttpRequestException(String.format("接口请求异常，接口信息：%s", url), e);
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
     * 该方法用于读取接口的请求头信息，并将其添加至请求内容中
     *
     * @param interInfo      接口信息封装类
     * @param requestBuilder 接口请求构造类
     * @return 接口请求构造类
     * @since autest 3.3.0
     */
    private void addHeader(InterfaceInfo interInfo, Builder requestBuilder) {
        // 获取接口信息中存储的请求头
        Map<String, String> headMap = interInfo.getRequestHeaderMap();
        if (!headMap.isEmpty()) {
            headMap.forEach((key, value) -> requestBuilder.addHeader(disposeContent(key), disposeContent(value)));
        }
    }

    /**
     * 该方法用于读取接口信息中的接口请求方式，并构造接口的请求方式
     *
     * @param interInfo      接口信息封装类
     * @param requestBuilder 接口请求构造类
     * @return 接口请求构造类
     * @since autest 3.3.0
     */
    private void setRequstType(InterfaceInfo interInfo, Builder requestBuilder) {
        // 获取接口的请求报文
        Entry<String, String> body = interInfo.getBody();
        RequestBody requestBody = RequestBody.create(MediaType.parse(body.getKey()), disposeContent(body.getValue()));

        // 根据请求方式，对接口进行请求
        switch (interInfo.getRequestType()) {
        case GET:
            requestBuilder.get();
            break;
        case POST:
            requestBuilder.post(requestBody);
            break;
        case HEAD:
            requestBuilder.head();
            break;
        case DELETE:
            requestBuilder.delete();
            break;
        case PUT:
            requestBuilder.put(requestBody);
            break;
        case PATCH:
            requestBuilder.patch(requestBody);
            break;
        default:
            throw new HttpRequestException(
                    String.format("不支持的请求方式：%s；接口信息：%s", interInfo.getRequestType(), interInfo.toString()));
        }
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
        if (content == null || content.isEmpty() || !content.matches(".*" + FUNCTION_SIGN + ".*")) {
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
                content = content.replaceAll(disposeKey(signKey), extractMap.get(key));
                continue;
            }

            // 若不是存储的词语，则判断关键词是否符合公式集合中的内容，符合公式，则使用公式对内容进行替换
            for (String funKey : functionMap.keySet()) {
                if (key.matches(funKey)) {
                    content = content.replaceAll(disposeKey(signKey), functionMap.get(funKey).apply(key));
                    break;
                }
            }

        }

        return content;
    }

    /**
     * 该方法用于处理需要替换的关键词内容，在需要转义的字符前添加反斜杠，避免在替换词语时无法被正则识别
     *
     * @param key 关键词
     * @return 添加转义符后的关键词
     * @since autest 3.3.0
     */
    private String disposeKey(String key) {
        StringBuilder newKey = new StringBuilder();
        // 对当前字符串逐字遍历
        for (int i = 0; i < key.length(); i++) {
            String charStr = key.substring(i, i + 1);
            // 若遍历的字符为正则中需要转义的特殊字符，则对该内容进行转义
            if (charStr.matches(RegexType.REGEX_ESC.getRegex())) {
                newKey.append("\\" + charStr);
            } else {
                newKey.append(charStr);
            }
        }

        return newKey.toString();
    }
}
