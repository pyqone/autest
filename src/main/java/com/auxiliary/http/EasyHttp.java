package com.auxiliary.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.auxiliary.datadriven.DataFunction;
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
     * 该方法用于根据接口信息，对接口进行请求，并返回响应内容
     *
     * @param interInfo 接口信息类对象
     * @return 接口响应
     * @since autest 3.3.0
     */
    public EasyResponse requst(InterfaceInfo interInfo) {
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        Builder requestBuilder = new Request.Builder().url(interInfo.toUrlString());
        setRequstType(interInfo, requestBuilder);
        addHeader(interInfo, requestBuilder);

        Request request = requestBuilder.build();
        try {
            Response response = client.newCall(request).execute();
            return new EasyResponse(response.body().string());
        } catch (IOException e) {
            throw new HttpRequestException(String.format("接口请求异常，接口信息：%s", interInfo.toString()), e);
        }
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
            headMap.forEach(requestBuilder::addHeader);
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
        RequestBody requestBody = RequestBody.create(MediaType.parse(body.getKey()), body.getValue());

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
        if (content == null || content.isEmpty() || !content.matches(FUNCTION_SIGN)) {
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
                content.replaceAll(disposeKey(signKey), extractMap.get(key));
                continue;
            }

            // 若不是存储的词语，则判断关键词是否符合公式集合中的内容，符合公式，则使用公式对内容进行替换
            for (String funKey : functionMap.keySet()) {
                if (key.matches(funKey)) {
                    content.replaceAll(disposeKey(signKey), functionMap.get(key).apply(key));
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
