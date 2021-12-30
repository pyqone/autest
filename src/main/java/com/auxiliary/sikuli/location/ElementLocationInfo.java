package com.auxiliary.sikuli.location;

import java.util.Objects;

import org.sikuli.script.Location;

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
public class ElementLocationInfo {
    /**
     * 存储元素的名称
     */
    private String elementName;

    /**
     * 存储截图文件存放路径
     */
    private String screenFilePath;
    /**
     * 存储需要对元素操作的位置
     */
    private Location operateLocation = null;
    /**
     * 存储文件识别相似度
     */
    private double similar = -1.0;

    /**
     * 存储元素的查找等待时间
     */
    private long waitTime;

    /**
     * 初始化元素信息，并指定识别元素截图文件路径
     * @param elementName 元素名称
     * @param screenFile 元素截图文件类对象
     * @since autest 3.0.0
     */
    public ElementLocationInfo(String elementName, String screenFilePath) {
        super();
        this.elementName = elementName;
        this.screenFilePath = screenFilePath;
    }

    /**
     * 初始化元素信息，并指定元素截图文件路径以及识别度
     * @param elementName 元素名称
     * @param screenFile 元素截图文件类对象
     * @param similar 识别度
     * @since autest 3.0.0
     */
    public ElementLocationInfo(String elementName, String screenFilePath, double similar) {
        super();
        this.elementName = elementName;
        this.screenFilePath = screenFilePath;
        this.similar = similar;
    }

    /**
     * 初始化元素信息，并指定元素截图文件路径以及元素操作坐标
     * @param elementName 元素名称
     * @param screenFile 元素截图文件类对象
     * @param operateLocation 元素操作坐标
     * @since autest 3.0.0
     */
    public ElementLocationInfo(String elementName, String screenFilePath, Location operateLocation) {
        super();
        this.elementName = elementName;
        this.screenFilePath = screenFilePath;
        this.operateLocation = operateLocation;
    }

    /**
     * 初始化元素信息，并指定元素截图文件路径以及元素操作坐标和识别度
     * @param elementName 元素名称
     * @param screenFile 元素截图文件类对象
     * @param operateLocation 元素操作坐标
     * @param similar 识别度
     * @since autest 3.0.0
     */
    public ElementLocationInfo(String elementName, String screenFilePath, Location operateLocation, double similar) {
        super();
        this.elementName = elementName;
        this.screenFilePath = screenFilePath;
        this.operateLocation = operateLocation;
        this.similar = similar;
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

    /**
     * 该方法用于元素操作的坐标类对象
     *
     * @return 坐标类对象
     * @since autest 3.0.0
     */
    public Location getOperateLocation() {
        return operateLocation;
    }

    /**
     * 该方法用于设置元素操作的坐标类对象
     *
     * @param operateLocation 坐标类对象
     * @since autest3.0.0
     */
    public void setOperateLocation(Location operateLocation) {
        this.operateLocation = operateLocation;
    }

    /**
     * 该方法用于返回元素识别的相似度
     *
     * @return 相似度
     * @since autest 3.0.0
     */
    public double getSimilar() {
        return similar;
    }

    /**
     * 该方法用于设置元素识别的相似度
     *
     * @param similar 相似度
     * @since autest 3.0.0
     */
    public void setSimilar(double similar) {
        this.similar = similar;
    }

    @Override
    public String toString() {
        return String.format("%s:%d:%s", elementName, waitTime, screenFilePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(elementName, operateLocation, screenFilePath);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ElementLocationInfo other = (ElementLocationInfo) obj;
        return Objects.equals(elementName, other.elementName) && Objects.equals(operateLocation, other.operateLocation)
                && Objects.equals(screenFilePath, other.screenFilePath);
    }
}
