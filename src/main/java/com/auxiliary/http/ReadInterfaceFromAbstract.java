package com.auxiliary.http;

import java.util.HashMap;

import com.auxiliary.tool.regex.ConstType;

/**
 * <p>
 * <b>文件名：ReadInterfaceFromAbstract.java</b>
 * </p>
 * <p>
 * <b>用途：</b> 定义接口信息读取类的基本方法
 * </p>
 * <p>
 * <b>编码时间：2022年4月12日 上午9:00:12
 * </p>
 * <p>
 * <b>修改时间：2022年4月12日 上午9:00:12
 * </p>
 *
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 3.3.0
 */
public abstract class ReadInterfaceFromAbstract {
    /**
     * 定义接口默认请求环境
     */
    protected final String DEFAULT_HOST = InterfaceInfo.DEFAULT_PROTOCOL + InterfaceInfo.DEFAULT_HOST;
    /**
     * 定义接口断言默认搜索范围
     */
    protected final String DEFAULT_SEARCH = "BODY";
    /**
     * 定义接口断言默认搜索结果下标
     */
    protected final String DEFAULT_ORD = "1";

    /**
     * 存储已读取的接口信息
     */
    protected HashMap<String, InterfaceInfo> interfaceCacheMap = new HashMap<>(
            ConstType.DEFAULT_MAP_SIZE);

    /**
     * 该方法用于根据接口名称，返回接口的信息类对象
     *
     * @param interName 接口名称
     * @return 接口信息类对象
     * @since autest 3.3.0
     */
    public abstract InterfaceInfo getInterface(String interName);

    /**
     * 该方法用于清除已缓存的接口信息
     * <p>
     * 调用该方法后，若再次读取之前已读取的接口信息时，则将重新查找接口信息（只针对启用缓存的情况）
     * </p>
     *
     * @since autest 3.3.0
     */
    public void clearInterfaceInfoCache() {
        interfaceCacheMap.clear();
    }
}