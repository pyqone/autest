package com.auxiliary.sikuli.location;

import java.util.List;

/**
 * <p>
 * <b>文件名：</b>AbstractSikuliLocation.java
 * </p>
 * <p>
 * <b>用途：</b> 定义读取并返回元素信息相关的基本方法
 * </p>
 * <p>
 * <b>编码时间：</b>2021年12月27日 上午8:29:45
 * </p>
 * <p>
 * <b>修改时间：</b>2021年12月27日 上午8:29:45
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since autest 3.0.0
 */
public abstract class AbstractSikuliLocation {
    /**
     * 定义用于正则的默认替换符开始标记
     */
    public static final String MATCH_START_SIGN = "\\$\\{";
    /**
     * 定义用于正则的默认替换符结束标记
     */
    public static final String MATCH_END_SIGN = "\\}";

    /**
     * 定义默认等待时间
     */
    protected final int DEFAULT_WAIT_TIME = 3;

    /**
     * 存储当前查找的元素名称
     */
    protected String name = "";

    /**
     * 该方法用于查找并缓存指定的元素
     *
     * @param name 元素名称
     * @return 类本身
     * @since autest 3.0.0
     */
    protected abstract AbstractSikuliLocation find(String name);

    /**
     * 该方法用于返回元素的信息
     *
     * @param name 元素名称
     * @return 元素信息
     * @since autest 3.0.0
     */
    public abstract List<ElementLocationInfo> getElementLocationList(String name);

    /**
     * 该方法用于返回元素的查找等待时间
     * <p><b>注意：</b>当没有等待时间或等待时间小于0时，则返回默认等待时间“{@link #DEFAULT_WAIT_TIME}”</p>
     *
     * @param name 元素名称
     * @return 查找等待时间
     * @since autest 3.0.0
     */
    protected abstract int getWaitTime(String name);
}
