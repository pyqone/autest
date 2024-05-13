package com.auxiliary.selenium.location;

import java.util.ArrayList;

import com.auxiliary.selenium.element.ElementType;
import com.auxiliary.tool.common.Placeholder;

/**
 * <p>
 * <b>文件名：</b>ReadLocation.java
 * </p>
 * <p>
 * <b>用途：</b> 定义读取元素定位内容必须实现的方法
 * </p>
 * <p>
 * <b>编码时间：</b>2020年10月29日下午12:52:41
 * </p>
 * <p>
 * <b>修改时间：</b>2021年4月17日 上午10:34:00
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.1
 */
public interface ReadLocation {
    /**
     * 用于返回元素的定位信息
     *
     * @return 元素定位信息
     * @throws UndefinedElementException 元素不存在时抛出的异常
     * @since autest 2.2.0
     */
    public abstract ArrayList<ElementLocationInfo> getElementLocation();

    /**
     * 用于返回元素的类型
     *
     * @return 元素类型（{@link ElementType}枚举）
     * @throws UndefinedElementException 元素不存在时抛出的异常
     * @since autest 2.2.0
     */
    public abstract ElementType getElementType();

    /**
     * 用于返回元素的所有父窗体名称集合
     *
     * @return 元素的所有父窗体名称集合
     * @throws UndefinedElementException 元素不存在时抛出的异常
     * @since autest 2.2.0
     */
    public abstract ArrayList<String> getIframeNameList();

    /**
     * 用于返回元素的等待时间
     *
     * @return 元素的等待时间
     * @throws UndefinedElementException 元素不存在时抛出的异常
     * @since autest 2.2.0
     */
    public abstract long getWaitTime();

    /**
     * 用于预读元素信息，并将元素信息进行缓存，以便于快速查找元素信息
     *
     * @param name 元素名称
     * @since autest 2.2.0
     */
    public abstract ReadLocation find(String name);

    /**
     * 用于返回元素的前置等待时间
     * <p>
     * <b>注意：</b>该方法读取到的时间单位为毫秒
     * </p>
     *
     * @return 元素等待时间
     * @throws UndefinedElementException 元素不存在时抛出的异常
     * @since autest 2.3.0
     */
    public abstract long getBeforeTime();

    /**
     * 该方法用于设置自定义的元素占位符标识
     * <p>
     * <b>注意：</b>该方法接收的标识符是正则表达式，若传入的标识符为特殊符号（如：*），则需要使用双反斜杠来转义（如：\\*）
     * </p>
     *
     * @param startRegex 占位符起始标识
     * @param endRegex   占位符结束标识
     * @since autest 2.7.0
     */
    public abstract void setElementPlaceholder(String startRegex, String endRegex);

    /**
     * 该方法用于返回元素占位符起始标识
     *
     * @return 占位符起始标识
     * @since autest 2.7.0
     */
    public abstract String getStartElementPlaceholder();

    /**
     * 该方法用于返回元素占位符结束标识
     *
     * @return 占位符结束标识
     * @since autest 2.7.0
     */
    public abstract String getEndElementPlaceholder();

    /**
     * 该方法用于返回占位符类对象
     * 
     * @return 占位符类对象
     * @since autest 4.2.0
     */
    public abstract Placeholder getPlaceholder();
}
