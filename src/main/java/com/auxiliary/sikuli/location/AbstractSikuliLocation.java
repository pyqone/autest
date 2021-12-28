package com.auxiliary.sikuli.location;

import java.io.File;
import java.util.Objects;

import org.sikuli.script.Location;
import org.sikuli.script.Pattern;

/**
 * <p>
 * <b>文件名：</b>AbstractSikuliLocation.java
 * </p>
 * <p>
 * <b>用途：</b>
 * 定义读取并返回元素信息相关的基本方法
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
     * 存储当前正在操作的元素信息
     */
    ElementLocation element;

    /**
     * 该方法用于返回根据元素名称查找元素，并返回元素的信息
     *
     * @param name 元素名称
     * @return 元素信息
     * @since autest 3.0.0
     */
    public ElementLocation getElement(String name) {
        // 若元素名称不相同，则重新查找元素信息；若不相同，则直接返回当前存储的元素信息
        if (!Objects.equals(name, element.getElementName())) {
            // 将元素基本信息进行封装
            element = new ElementLocation(name, new Pattern(getScreenFile().getAbsolutePath()).targetOffset(getLocation()).similar(getSimilar()));

            // TODO 设置元素的附加信息
            element.setWaitTime(getWaitTime());
        }

        return element;
    }

    /**
     * 该方法用于返回截图文件类对象
     *
     * @return 截图文件类对象
     * @since autest 3.0.0
     */
    protected abstract File getScreenFile();

    /**
     * 该方法用于返回截图中需要操作的坐标点
     *
     * @return 需要操作的坐标点
     * @since autest 3.0.0
     */
    protected abstract Location getLocation();

    /**
     * 该方法用于返回元素在操作范围内的识别度
     *
     * @return 识别度
     * @since autest 3.0.0
     */
    protected abstract double getSimilar();

    /**
     * 该方法用于返回元素的查找等待时间
     *
     * @return 查找的等待时间
     * @since autest 3.0.0
     */
    protected abstract long getWaitTime();
}
