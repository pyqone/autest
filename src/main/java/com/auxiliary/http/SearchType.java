package com.auxiliary.http;

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
     * 搜索响应报文信息
     */
    BODY;
}
