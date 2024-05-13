package com.auxiliary.selenium.location;

import java.util.Optional;

import com.auxiliary.datadriven.DataFunction;
import com.auxiliary.selenium.element.ElementType;
import com.auxiliary.tool.common.AddPlaceholder;
import com.auxiliary.tool.common.Placeholder;

/**
 * <p>
 * <b>文件名：</b>AbstractReadConfig.java
 * </p>
 * <p>
 * <b>用途：</b> 定义读取自动化测试中元素定位方式的基本方法
 * </p>
 * <p>
 * <b>编码时间：</b>2020年9月28日上午7:37:00
 * </p>
 * <p>
 * <b>修改时间：</b>2023年5月24日 下午5:46:55
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.1
 * @since autest 2.0.0
 */
public abstract class AbstractLocation implements ReadLocation, AddPlaceholder {
    /**
     * 定义替换符开始标志
     */
    public static final String START_SIGN = "${";
    /**
     * 定义替换符结束标志
     */
    public static final String END_SIGN = "}";

    /**
     * 占位符类对象
     * 
     * @since autest 4.2.0
     */
    protected Placeholder placeholder = new Placeholder(START_SIGN, END_SIGN);

    /**
     * 元素名称
     */
    protected String name = "";

    @Override
    public void setElementPlaceholder(String startSign, String endSign) {
        placeholder.setPlaceholderSign(startSign, endSign);
    }

    @Override
    public String getStartElementPlaceholder() {
        return placeholder.getPlaceholderSign()[0];
    }

    @Override
    public String getEndElementPlaceholder() {
        return placeholder.getPlaceholderSign()[1];
    }

    @Override
    public void addReplaceWord(String word, String replaceWord) {
        placeholder.addReplaceWord(word, replaceWord);
    }

    @Override
    public void addReplaceFunction(String regex, DataFunction function) {
        placeholder.addReplaceFunction(regex, function);
    }

    @Override
    public Placeholder getPlaceholder() {
        return placeholder;
    }

    /**
     * 用于将读取到的元素类型的文本值转换为元素类型枚举类对象
     *
     * @param value 元素类型文本值
     * @return 元素类型枚举类对象
     */
    protected ElementType toElementType(String value) {
        // 转换元素类型枚举，并返回
        switch (value) {
        case "1":
            return ElementType.DATA_LIST_ELEMENT;
        case "2":
            return ElementType.SELECT_DATAS_ELEMENT;
        case "3":
            return ElementType.SELECT_OPTION_ELEMENT;
        case "4":
            return ElementType.IFRAME_ELEMENT;
        case "0":
        default:
            return ElementType.COMMON_ELEMENT;
        }
    }

    /**
     * 用于对等待时间进行转换
     *
     * @param text 获取的文本
     * @return 转换后的等待时间
     */
    protected long toWaitTime(String text) {
        // 若值为空值，则返回-1
        if (!Optional.ofNullable(text).filter(t -> !t.isEmpty()).isPresent()) {
            return -1L;
        }

        // 获取元素存储等待时间属性值，并转换为long类型
        try {
            // 将属性值进行转换，若属性值不存在，则赋为-1
            long time = Long.valueOf(text == null ? "-1" : text);
            // 若转换的时间小于0，则返回-1
            return time < 0 ? -1L : time;
        } catch (NumberFormatException e) {
            return -1L;
        }
    }
}
