package com.auxiliary.http;

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
}
