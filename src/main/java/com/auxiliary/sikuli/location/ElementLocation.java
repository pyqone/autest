package com.auxiliary.sikuli.location;

import java.util.Objects;

import org.sikuli.script.Pattern;

/**
 * <p>
 * <b>文件名：</b>ElementLocation.java
 * </p>
 * <p>
 * <b>用途：</b>
 * 用于存储截图元素的封装信息以及其在查找时的附加信息
 * </p>
 * <p>
 * <b>编码时间：</b>2021年12月27日 上午8:16:30
 * </p>
 * <p>
 * <b>修改时间：</b>2021年12月27日 上午8:16:30
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since autest 3.0.0
 */
public class ElementLocation {
    /**
     * 存储截图元素类对象
     */
    private Pattern element;

    /**
     * 存储元素的查找等待时间
     */
    private long waitTime;
    /**
     * 存储元素的名称
     */
    private String elementName;

    /**
     * 构造对象，并指定元素的名称
     * @param elementName 元素名称
     * @param element 截图元素
     */
    public ElementLocation(String elementName, Pattern element) {
        this.elementName = elementName;
        this.element = element;
    }

    /**
     * 该方法用于返回截图元素类对象
     *
     * @return 截图元素类对象
     * @since autest 3.0.0
     */
    public Pattern getElement() {
        return element;
    }

    /**
     * 该方法用于返回元素查找等待时间
     *
     * @return 元素查找等待时间
     * @since autest 3.0.0
     */
    public long getWaitTime() {
        return waitTime;
    }

    /**
     * 该方法用于设置元素查找等待时间
     *
     * @param waitTime 元素查找等待时间
     * @since autest 3.0.0
     */
    public void setWaitTime(long waitTime) {
        this.waitTime = waitTime;
    }

    /**
     * 该方法用于返回元素的名称
     *
     * @return 元素名称
     * @since autest 3.0.0
     */
    public String getElementName() {
        return elementName;
    }

    @Override
    public String toString() {
        return String.format("%s:%d:%s", elementName, waitTime, element.getFilename());
    }

    @Override
    public int hashCode() {
        return Objects.hash(element, elementName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ElementLocation other = (ElementLocation) obj;
        return Objects.equals(elementName, other.elementName) && Objects.equals(element.getFilename(), other.element.getFilename());
    }
}
