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
