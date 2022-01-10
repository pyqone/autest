package com.auxiliary.sikuli.location;

import java.util.Objects;

import org.sikuli.script.Location;
import org.sikuli.script.Pattern;

/**
 * <p>
 * <b>文件名：</b>ElementLocation.java
 * </p>
 * <p>
 * <b>用途：</b> 用于存储截图元素的封装信息以及其在查找时的附加信息
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
     * 定义默认的相似度，-1.0
     */
    public static final double DEFAULT_SIMILAR = -1.0;

    /**
     * 存储截图文件存放路径
     */
    private String screenFilePath;
    /**
     * 存储需要对元素操作的位置
     */
    private Location operateLocation = Location.getDefaultInstance4py();
    /**
     * 存储文件识别相似度
     */
    private double similar = DEFAULT_SIMILAR;

    /**
     * 初始化元素信息，并指定识别元素截图文件路径
     *
     * @param screenFile 元素截图文件类对象
     * @since autest 3.0.0
     */
    public ElementLocationInfo(String screenFilePath) {
        this.screenFilePath = screenFilePath;
    }

    /**
     * 初始化元素信息，并指定元素截图文件路径以及识别度
     *
     * @param screenFilePath 元素截图文件类对象
     * @param similar    识别度
     * @since autest 3.0.0
     */
    public ElementLocationInfo(String screenFilePath, double similar) {
        this.screenFilePath = screenFilePath;
        this.similar = similar;
    }

    /**
     * 初始化元素信息，并指定元素截图文件路径以及操作坐标
     * @param screenFilePath 元素截图文件类对象
     * @param operateLocation 元素操作坐标
     * @since autest 3.0.0
     */
    public ElementLocationInfo(String screenFilePath, Location operateLocation) {
        super();
        this.screenFilePath = screenFilePath;
        this.operateLocation = operateLocation;
    }

    /**
     * 初始化元素信息，并指定元素截图文件路径以及操作坐标、识别度
     * @param screenFilePath 元素截图文件类对象
     * @param operateLocation 元素操作坐标
     * @param similar 识别度
     * @since autest 3.0.0
     */
    public ElementLocationInfo(String screenFilePath, Location operateLocation, double similar) {
        super();
        this.screenFilePath = screenFilePath;
        this.operateLocation = operateLocation;
        this.similar = similar;
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

    /**
     * 该方法用于返回元素截图所在绝对路径
     *
     * @return 截图元素所在绝对路径
     * @since autest 3.0.0
     */
    public String getScreenFilePath() {
        return screenFilePath;
    }

    /**
     * 该方法用于根据已有的信息，返回截图元素封装类对象
     *
     * @return 截图元素封装类对象
     * @since autest 3.0.0
     */
    public Pattern getPattern() {
        Pattern pattern = new Pattern(screenFilePath);
        pattern.targetOffset(operateLocation);

        // 判断是否指定相似度，若未指定(similar小于0)，则不设置相似度数据
        if (Double.compare(similar, 0.0) > 0) {
            pattern.similar(similar);
        }

        return pattern;
    }

    @Override
    public String toString() {
        return String.format("%s:%d", screenFilePath, similar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operateLocation, screenFilePath);
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
        return Objects.equals(operateLocation, other.operateLocation)
                && Objects.equals(screenFilePath, other.screenFilePath);
    }
}
