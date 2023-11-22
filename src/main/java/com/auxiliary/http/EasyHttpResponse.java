package com.auxiliary.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.auxiliary.tool.regex.ConstType;

import okhttp3.Headers;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class EasyHttpResponse extends EasyResponse {
    /**
     * 存储接口响应信息
     */
    ResponseInfo info = new ResponseInfo();

    /**
     * 构造对象，指定OKHttp响应类
     *
     * @param response  OKHttp响应类
     * @param interInfo
     */
    protected EasyHttpResponse(Response response, InterfaceInfo interInfo) {
        // 记录接口实际请求信息
        info.requestInterInfo = interInfo;
        // 记录请求结果
        info.response = response;
        // 存储响应内容
        ResponseBody body = response.body();
        try {
            info.responseBody = body.bytes();
        } catch (IOException e) {
        }

        // 存储响应头
        Headers heads = response.headers();
        for (String head : heads.names()) {
            List<String> valueList = heads.values(head);
            if (valueList.size() < 2) {
                info.responseHeaderMap.put(head, valueList.get(0));
            } else {
                info.responseHeaderMap.put(head, valueList.toString());
            }
        }

        // 存储响应状态及消息
        info.status = response.code();
        info.message = response.message();

        // 存储请求时间
        info.sentRequestAtMillis = response.sentRequestAtMillis();
        info.receivedResponseAtMillis = response.receivedResponseAtMillis();
    }

    /**
     * <p>
     * <b>文件名：EasyResponse.java</b>
     * </p>
     * <p>
     * <b>用途：</b> 定义接口响应的信息类，用于对接口响应工具的数据返回
     * </p>
     * <p>
     * <b>编码时间：2022年7月15日 上午8:16:23
     * </p>
     * <p>
     * <b>修改时间：2022年7月15日 上午8:16:23
     * </p>
     *
     * @author 彭宇琦
     * @version Ver1.0
     * @since JDK 1.8
     * @since autest 3.5.0
     */
    private class ResponseInfo {
        /**
         * 请求报文类对象
         */
        public Response response;

        /**
         * 接口响应体内容
         */
        public byte[] responseBody;
        /**
         * 响应头集合
         */
        public HashMap<String, String> responseHeaderMap = new HashMap<>();
        /**
         * 响应状态码
         */
        public int status = 200;
        /**
         * 响应消息
         */
        public String message = "";
        /**
         * 响应体转义字符集
         */
        public String charsetName = InterfaceInfo.DEFAULT_CHARSETNAME;
        /**
         * 存储响应体的格式
         */
        public HashMap<Integer, Set<MessageType>> bodyTypeMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
        /**
         * 记录客户端发送请求时的时间戳
         */
        public long sentRequestAtMillis = 0L;
        /**
         * 记录客户端收到请求头时的时间戳
         */
        public long receivedResponseAtMillis = 0L;

        /**
         * 记录接口的实际请求
         */
        public InterfaceInfo requestInterInfo = new InterfaceInfo();
    }
}
