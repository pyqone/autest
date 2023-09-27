package com.auxiliary.http;

import java.util.Map;

/**
 * <p>
 * <b>文件名：ActionEnvironmentGroup.java</b>
 * </p>
 * <p>
 * <b>用途：</b>定义执行环境组所包含的基础方法
 * </p>
 * <p>
 * <b>编码时间：2023年8月9日 上午7:57:39
 * </p>
 * <p>
 * <b>修改时间：2023年8月9日 上午7:57:39
 * </p>
 *
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 4.4.0
 */
public interface ActionEnvironmentGroup extends ActionEnvironment {
    /**
     * 该方法用于返回指定执行环境组中的环境名称对应的地址
     *
     * @param envGroupName 环境组名称
     * @param envName      环境名称
     * @return 对应的地址
     * @since autest 4.4.0
     */
    public String getEnvironmentGroupName(String envGroupName, String envName);

    /**
     * 该方法用于返回环境组下的所有环境名称
     *
     * @param envGroupName 环境组名称
     * @return 环境组下的所有环境名称集合
     * @since autest 4.4.0
     */
    public Map<String, String> getGroupAllEnvironmentName(String envGroupName);

    /**
     * 该方法用于切换指定的环境组
     * <p>
     * <b>注意：</b>若环境组名称不存在，则不进行切换；若环境组名称传入null或空串，则切换为默认环境组的状态，等同于调用{@link #switchDefaultGroup()}方法
     * </p>
     *
     * @param envGroupName 环境组名称
     * @since autest 4.4.0
     */
    public void switchGroup(String envGroupName);

    /**
     * 该方法用于切换到默认环境组的状态，即读取环境组外的环境
     *
     * @since autest 4.4.0
     */
    public void switchDefaultGroup();

    /**
     * 该方法用于根据接口名称及所在环境，返回接口的信息类对象
     *
     * @param interName       接口名称
     * @param envGroupName    环境组名称
     * @param environmentName 环境名称
     * @return 接口信息类对象
     * @since autest 4.4.0
     */
    public abstract InterfaceInfo getInterface(String interName, String envGroupName, String environmentName);
}
