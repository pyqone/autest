package com.auxiliary.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.auxiliary.tool.regex.ConstType;

import okhttp3.Headers;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class EasyHttpResponse extends EasyResponse {
    /**
     * 请求报文类对象
     * 
     * @since autest 3.5.0
     */
    protected Response response;

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
        ResponseBody body = this.response.body();
        try {
            responseBody = body.bytes();
        } catch (IOException e) {
        }

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
    }
}
