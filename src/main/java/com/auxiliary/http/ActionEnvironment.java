package com.auxiliary.http;

import java.util.HashMap;

/**
 * <p>
 * <b>文件名：ActionEnvironment.java</b>
 * </p>
 * <p>
 * <b>用途：</b>定义可写入执行环境信息的接口信息存储工具基本方法
 * </p>
 * <p>
 * <b>编码时间：2022年4月27日 上午7:58:17
 * </p>
 * <p>
 * <b>修改时间：2022年4月27日 上午7:58:17
 * </p>
 *
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 3.3.0
 */
public interface ActionEnvironment {
    /**
     * 该方法用于设置当前执行接口的环境
     *
     * @param environmentName 环境名称
     * @since autest 3.3.0
     */
    public abstract void setActionEnvironment(String environmentName);

    /**
     * 该方法用于返回所有的执行环境集合
     *
     * @return 所有执行环境集合
     * @since autest 3.3.0
     */
    public abstract HashMap<String, String> getActionEnvironment();

    /**
     * 该方法用于根据接口名称及所在环境，返回接口的信息类对象
     *
     * @param interName       接口名称
     * @param environmentName 环境名称
     * @return 接口信息类对象
     * @since autest 3.3.0
     */
    public abstract InterfaceInfo getInterface(String interName, String environmentName);
}
