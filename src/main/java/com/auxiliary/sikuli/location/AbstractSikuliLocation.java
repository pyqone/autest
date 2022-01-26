package com.auxiliary.sikuli.location;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.auxiliary.selenium.location.ReadLocation;
import com.auxiliary.sikuli.element.ElementLocationInfo;

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
     * 元素占位符起始标识，默认{@link ReadLocation#MATCH_START_SIGN}
     */
    protected String startRegex = ReadLocation.MATCH_START_SIGN;
    /**
     * 元素占位符结束标识，默认{@link ReadLocation#MATCH_END_SIGN}
     */
    protected String endRegex = ReadLocation.MATCH_END_SIGN;

    /**
     * 存储元素信息集合，建议将该数据进行缓存，以减少不必要的麻烦处理
     */
    protected List<ElementLocationInfo> elementInfoList;

    /**
     * 定义默认等待时间
     */
    protected final int DEFAULT_WAIT_TIME = 3;

    /**
     * 存储当前查找的元素名称
     */
    protected String name;

    /**
     * 该方法用于查找并缓存指定的元素
     *
     * @param name 元素名称
     * @return 类本身
     * @since autest 3.0.0
     */
    protected abstract AbstractSikuliLocation find(String name);

    /**
     * 该方法用于设置自定义的元素占位符标识
     * <p>
     * <b>注意：</b>该方法接收的标识符是正则表达式，若传入的标识符为特殊符号（如：*），则需要使用双反斜杠来转义（如：\\*）
     * </p>
     *
     * @param startRegex 占位符起始标识
     * @param endRegex   占位符结束标识
     * @since autest 3.0.0
     */
    public void setElementPlaceholder(String startRegex, String endRegex) {
        this.startRegex = startRegex;
        this.endRegex = endRegex;
    }

    /**
     * 该方法用于返回元素占位符起始标识
     *
     * @return 占位符起始标识
     * @since autest 3.0.0
     */
    public String getStartElementPlaceholder() {
        return startRegex;
    }

    /**
     * 该方法用于返回元素占位符结束标识
     *
     * @return 占位符结束标识
     * @since autest 3.0.0
     */
    public String getEndElementPlaceholder() {
        return endRegex;
    }

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
     * <p>
     * <b>注意：</b>当没有等待时间或等待时间小于0时，则返回默认等待时间“{@link #DEFAULT_WAIT_TIME}”
     * </p>
     *
     * @param name 元素名称
     * @return 查找等待时间
     * @since autest 3.0.0
     */
    public abstract int getWaitTime(String name);

    /**
     * 该方法用于对当前的元素名称与传入的元素名称进行对比，判断是否一致，并返回是否判断结果
     * <p>
     * <b>注意：</b>该方法可对传入的元素名称是否为空进行判断，为空时将抛出{@link UndefinedElementException}异常
     * </p>
     *
     * @param name 元素名称
     * @return 与当前元素名称的对比结果
     * @since autest 3.0.0
     * @throws UndefinedElementException 当传入的元素名称为空时，抛出的异常
     */
    protected boolean compareElementName(String name) {
        return Objects.equals(Optional.ofNullable(name).filter(n -> !n.isEmpty())
                .orElseThrow(() -> new UndefinedElementException("未指定元素名称")), this.name);
    }
}
