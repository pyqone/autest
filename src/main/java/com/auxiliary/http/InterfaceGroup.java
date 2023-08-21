package com.auxiliary.http;

import java.util.Map;

/**
 * <p>
 * <b>文件名：InterfaceGroup.java</b>
 * </p>
 * <p>
 * <b>用途：</b>定义能实现接口组的模板读取类应包含的方法
 * </p>
 * <p>
 * <b>编码时间：2023年8月18日 上午10:19:33
 * </p>
 * <p>
 * <b>修改时间：2023年8月18日 上午10:19:33
 * </p>
 *
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 4.4.0
 */
public interface InterfaceGroup {
    /**
     * 该方法用于返回接口组中的所有接口集合
     *
     * @param groupName 接口组名称
     * @return 接口组中的接口集合
     * @since autest 4.4.0
     */
    public Map<String, InterfaceInfo> getInterfaceGroup(String groupName);
}
