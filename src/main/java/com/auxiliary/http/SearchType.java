package com.auxiliary.http;

import java.util.function.Function;

import com.auxiliary.tool.common.DisposeCodeUtils;

/**
 * <p>
 * <b>文件名：SearchType.java</b>
 * </p>
 * <p>
 * <b>用途：</b>定义提词或断言的搜索范围枚举
 * </p>
 * <p>
 * <b>编码时间：2022年5月5日 上午8:10:08
 * </p>
 * <p>
 * <b>修改时间：2022年5月5日 上午8:10:08
 * </p>
 *
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 3.3.0
 */
public enum SearchType {
    /**
     * 搜索响应头信息
     */
    HEADER,
    /**
     * 搜索响应体信息
     */
    BODY,
    /**
     * 搜索响应状态码
     */
    STATUS,
    /**
     * 搜索响应消息
     */
    MESSAGE;

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
    public static SearchType typeText2Type(String type) {
        return typeText2Type(type, text -> text);
    }

    /**
     * 该方法用于将枚举文本转换为消息枚举
     * <p>
     * 该方法与{@link #typeText2Type(String)}方法类似，其可对传入的文本进行自定义的转换，例如欲将传入的"bo"转换为"body"，则可写为
     * <code><pre>
     * SearchType.typeText2MessageType("bo", type -&gt; {
     *      if ("bo".equals(text)) {
     *          text = "body";
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
    public static SearchType typeText2Type(String type, Function<String, String> mapper) {
        return DisposeCodeUtils.disposeEnumTypeText(SearchType.class, type, mapper, true);
    }
}
