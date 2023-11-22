package com.auxiliary.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.primitives.Bytes;

/**
 * <p>
 * <b>用途：</b> 定义使用Socket方式对接口进行请求的基本方法
 * </p>
 * <p>
 * <b>编码时间：2023年11月22日 09:07:46
 * </p>
 * <p>
 * <b>修改时间：2023年11月22日 09:07:46
 * </p>
 * 
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 4.5.0
 */
public class EasyTCPSocket extends EasyRequest {
    /**
     * 定义响应报文byte数组默认长度
     */
    protected static final int RESPONSE_BODY_BYTE_LENGTH = 1024;

    @Override
    public EasyResponse requst(InterfaceInfo interInfo) {
        // 读取接口信息中的接口超时时间
        // Entry<Long, TimeUnit> timeData = interInfo.getConnectTime();
        // long connectTime = (timeData.getKey() <= 0L ? 0L
        // : (timeData.getKey()
        // *
        // Optional.ofNullable(timeData.getValue()).orElse(TimeUnit.SECOND).getToMillisNum()));
        // if (Optional.ofNullable(requestBodyCharsetname).filter(name ->
        // !name.isEmpty()).filter(Charset::isSupported)
        // .isPresent()) {
        // }

        return null;
    }

/**
 * 该方法用于对接口进行Socket请求
 * 
 * @param ip 服务器IP地址
 * @param port 服务器端口号
 * @param connectTime 连接超时时间（单位：毫秒）
 * @param requstBody 请求体字节数组
 * @param responseBodyCharset 响应体字符集
 * @param responseStartBodyLength 响应体长度起始标识长度，即响应体标识响应体整体长度的标识，该标识占响应体起始位置的长度
 * @param responseEndSingle 响应体返回结束标志
 * @return 响应类对象
 */
public static EasyResponse requst(String ip, int port, int connectTime, byte[] requstBody,
        String responseBodyCharset, int responseStartBodyLength, String responseEndSingle) {
    // 定义byte集合，用于存储每次循环读取到的byte数据
    List<Byte> responseBodyByteList = new ArrayList<>();
    // 将响应报文结束标志转换为byte数组，若响应报文结束标志为null或为空或响应体字符集无法被转换为byte数组，则将其置为空数组
    List<Byte> responseEndSingleList = new ArrayList<>();
    if (responseEndSingle != null && !responseEndSingle.isEmpty()) {
        try {
            responseEndSingleList.addAll(Bytes.asList(responseEndSingle.getBytes(responseBodyCharset)));
        } catch (UnsupportedEncodingException e) {
        }
    }

    // 定义socket连接
    try (Socket socket = new Socket(ip, port)) {
        // 设置读取超时时间
        socket.setSoTimeout(connectTime);

        // 获取输出流，向服务器发送数据
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(requstBody);

        // 获取输入流，接收服务器返回的响应内容，并将其拼接至字符串中
        InputStream inputStream = socket.getInputStream();
        // 判断当前是否传入响应报文的起始长度，并根据长度读取相应的报文长度
        int responseBodyByteTotalLenth = -1;
        byte[] responseLengthBytes = null;
        if (responseStartBodyLength > 0) {
            // 存储接口返回的字符串长度
            responseLengthBytes = new byte[responseStartBodyLength];
            // 在流中读取响应报文长度
            inputStream.read(responseLengthBytes);
            // 对长度文本进行转译
            String lengthText = new String(responseLengthBytes);
            // 转译并存储响应报文长度，若无法进行转换，则将转译的报文内容存储只responseText中
            try {
                responseBodyByteTotalLenth = Integer.parseInt(lengthText);
            } catch (Exception e) {
                // 将responseLengthBytes数组中的内容存储至responseBodyByteList中
                // Bytes为guava jar包下的工具类，其下还提供其他基础数据类型数组转集合的方法
                responseBodyByteList.addAll(Bytes.asList(responseLengthBytes));
            }
        }

        // 定义响应报文byte数组
        byte[] responseBodyBytes = new byte[RESPONSE_BODY_BYTE_LENGTH];
        // 定义当前已接收到的报文总长度
        int receiveTotalLength = 0;
        // 定义当前已接收到的报文长度
        int receiveLength = 0;
        // 循环，接收流中的响应内容
        while ((receiveLength = inputStream.read(responseBodyBytes)) > 0) {
            // 计算当前已接收到的报文总长度
            receiveTotalLength += receiveLength;
            // 将当前已接收到的报文存储至responseBodyByteList中
            responseBodyByteList.addAll(Bytes.asList(responseBodyBytes).subList(0, receiveLength));

            // 判断当前报文的长度是否与指定的长度相等，或当前报文是否包含响应报文结束标志，若是，则跳出循环
            if (receiveTotalLength == responseBodyByteTotalLenth || (!responseEndSingleList.isEmpty()
                    && responseBodyByteList.containsAll(responseEndSingleList))) {
                break;
            }
        }

        // 定义接口实际请求信息
        InterfaceInfo info = new InterfaceInfo();
        info.analysisUrl(String.format("%s:%d", ip, port));
        info.setBodyContent(MessageType.NONE, Bytes.asList(requstBody));

        // 返回响应类对象
        return new EasySockecResponse(responseLengthBytes, responseBodyByteList, info);
    } catch (UnknownHostException unknownHostException) {
        throw new HttpRequestException(String.format("socket连接地址错误：%s:%s", ip, port), unknownHostException);
    } catch (IOException iOException) {
        throw new HttpRequestException(String.format("socket连接异常，连接地址：%s:%s", ip, port), iOException);
    }
}
}
