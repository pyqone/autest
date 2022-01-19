package com.auxiliary.sikuli.element;

import org.sikuli.script.Match;
import org.sikuli.script.Pattern;

/**
 * <p>
 * <b>文件名：</b>SikuliElement.java
 * </p>
 * <p>
 * <b>用途：</b>
 * 定义在界面上查找到的目标元素类对象，可根据中的
 * </p>
 * <p>
 * <b>编码时间：</b>2022年1月19日 下午9:39:02
 * </p>
 * <p>
 * <b>修改时间：</b>2022年1月19日 下午9:39:02
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since autest 3.0.0
 */
public class SikuliElement {
    /**
     * 元素名称
     */
    private String name = "";
    /**
     * 需要操作的元素
     */
    private Match element;

    /**
     * 构造对象
     * @param name 元素名称
     * @param element {@link Pattern}类对象
     * @since autest 3.0.0
     */
    public SikuliElement(String name, Match element) {
        this.name = name;
        this.element = element;
    }

    /**
     * 该方法用于返回元素的名称
     *
     * @return 元素名称
     * @since autest 3.0.0
     */
    public String getName() {
        return name;
    }

    /**
     * 该方法用于返回元素的{@link Match}类对象
     *
     * @return {@link Match}类对象
     * @since autest 3.0.0
     */
    public Match getElement() {
        return element;
    }
}
