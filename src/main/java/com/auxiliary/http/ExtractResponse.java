package com.auxiliary.http;

import java.util.Set;

/**
 * <p>
 * <b>文件名：ExtractResponse.java</b>
 * </p>
 * <p>
 * <b>用途：</b>定义可写入提词信息的接口信息存储工具基本方法
 * </p>
 * <p>
 * <b>编码时间：2022年4月27日 上午8:01:37
 * </p>
 * <p>
 * <b>修改时间：2022年4月27日 上午8:01:37
 * </p>
 *
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 3.3.0
 */
public interface ExtractResponse {
    /**
     * 定义元素名称中的“.”符号的转义符号，调用该符号后，其变量名将作为整体看待，不会被切割
     * <p>
     * 例如，“valus.s”在识别时会被当成两个元素名称，而“value\.s”则将作为单个变量名称使用
     * </p>
     */
    public static final String SEPARATE_TRANSFERRED_MEANING_REGEX = "\\\\.";
    /**
     * 定义元素名称分隔
     */
    public static final String SEPARATE_SYMBOL = ".";
    /**
     * 定义切分元素名称的正则符号
     */
    public static final String SEPARATE_SPLIT_REGEX = "\\.";
    /**
     * 定义查找数组元素起始标志符号
     */
    public static final String ARRAY_START_SYMBOL = "[";
    /**
     * 定义查找数组元素结束标志符号
     */
    public static final String ARRAY_END_SYMBOL = "]";

    /**
     * 该方法用于指定接口的报文提词内容json串集合
     * <p>
     * json串的键值可通过静态属性获取，当没有断言内容时，则json中的键都为空值
     * </p>
     *
     * @param interName 接口名称
     * @return 接口响应报文提词内容json串集合
     * @since autest 3.3.0
     */
    public abstract Set<String> getExtractContent(String interName);
}
