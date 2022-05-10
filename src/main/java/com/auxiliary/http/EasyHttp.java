package com.auxiliary.http;

import java.io.IOException;
import java.util.Map;

import com.auxiliary.tool.common.Entry;

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

}
