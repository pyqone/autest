package com.auxiliary.http;

import java.util.function.Function;

import com.auxiliary.tool.common.DisposeCodeUtils;

/**
 * <p>
 * <b>文件名：</b>RequestType.java
 * </p>
 * <p>
 * <b>用途：</b> 定义接口请求的方式
 * </p>
 * <p>
 * <b>编码时间：</b>2020年6月17日下午7:50:54
 * </p>
 * <p>
 * <b>修改时间：</b>2022年4月19日 下午4:10:48
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.1
 * @since JDK 1.8
 * @since autest 3.3.0
 */
public enum RequestType {
    /**
     * GET请求
     */
    GET,
    /**
     * POST请求
     */
    POST,
    /**
     * PUT请求
     */
    PUT,
    /**
     * DELETE请求
     */
    DELETE,
    /**
     * OPTIONS请求
     */
    OPTIONS,
    /**
     * HEAD请求
     */
    HEAD,
    /**
     * 请求
     */
    PATCH,
    /**
     * TRACE请求
     */
    TRACE,
    /**
     * CONNECT请求
     */
    CONNECT,
    /**
     * COPY请求
     */
    COPY,
    /**
     * LINK请求
     */
    LINK,
    /**
     * UNLINK请求
     */
    UNLINK,
    /**
     * PURGE请求
     */
    PURGE,
    /**
     * LOCK请求
     */
    LOCK,
    /**
     * UNLOCK请求
     */
    UNLOCK,
    /**
     * MKCOL请求
     */
    MKCOL,
    /**
     * MOVE请求
     */
    MOVE,
    /**
     * PROPFIND请求
     */
    PROPFIND,
    /**
     * VIEW请求
     */
    VIEW;

    /**
     * 该方法用于将枚举文本转换为消息枚举
     * <p>
     * 该方法本质上与{@link #valueOf(String)}方法的作用一致，但其中加上了对文本的内容的判空、对文本内容大写转换、
     * 对特殊文本进行处理以及对异常文本进行汉化的操作。
     * </p>
     * 
     * @param type 枚举文本内容
     * @return 转换后的枚举
     * @since autest 3.7.0
     * @throws IllegalArgumentException 当枚举文本为空或不能转换成枚举时抛出的异常
     */
    public static RequestType typeText2Type(String type) {
        return typeText2Type(type, text -> text);
    }

    /**
     * 该方法用于将枚举文本转换为消息枚举
     * <p>
     * 该方法与{@link #typeText2Type(String)}方法类似，其可对传入的文本进行自定义的转换，例如欲将传入的"po"转换为"post"，则可写为
     * <code><pre>
     * RequestType.typeText2MessageType("po", type -> {
     *      if ("po".equals(text)) {
     *          text = "post";
     *      }
     *      
     *      return text;
     * });
     * </pre></code>
     * </p>
     * 
     * @param type   枚举文本内容
     * @param mapper 枚举文本的处理方法
     * @return 转换后的枚举
     * @since autest 3.7.0
     * @throws IllegalArgumentException 当枚举文本为空或不能转换成枚举时抛出的异常
     */
    public static RequestType typeText2Type(String type, Function<String, String> mapper) {
        return DisposeCodeUtils.disposeEnumTypeText(RequestType.class, type, mapper, true);
    }
}
