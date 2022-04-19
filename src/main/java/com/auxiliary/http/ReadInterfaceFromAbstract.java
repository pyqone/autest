package com.auxiliary.http;

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
    // TODO 自动识别请求体格式，并设置相应的请求头
    /**
     * 当前执行接口的环境
     */
    protected String environment = "";

    /**
     * 该方法用于根据接口名称，返回接口的信息类对象
     *
     * @param interName 接口名称
     * @return 接口信息类对象
     * @since autest 3.3.0
     */
    public abstract InterfaceInfo getInterface(String interName);
}