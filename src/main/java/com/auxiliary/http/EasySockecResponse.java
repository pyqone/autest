package com.auxiliary.http;

import java.util.List;

/**
 * <p>
 * <b>用途：</b>用于对Socket请求方式的响应报文进行解析，并提供相应的报文处理方法
 * </p>
 * <p>
 * <b>编码时间：2023年12月08日 08:31:20
 * </p>
 * <p>
 * <b>修改时间：2023年12月08日 08:31:20
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 4.5.0
 */
public class EasySockecResponse extends EasyResponse {
    protected EasySockecResponse(byte[] responseBodyLengthBytes, List<Byte> responseBodyByteList, InterfaceInfo interInfo) {
    }
}
