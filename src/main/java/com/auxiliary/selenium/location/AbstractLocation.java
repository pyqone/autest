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
 * <b>修改时间：</b>2021年3月8日上午8:08:45
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 *
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
     * 元素占位符起始标识，默认{@link ReadLocation#MATCH_START_SIGN}
     * 
     * @deprecated 该属性已无意义，占位符相关的内容由占位符类对象代替，将在4.3.0或后续版本中删除
     */
    @Deprecated
    protected String startRegex = ReadLocation.MATCH_START_SIGN;
    /**
     * 元素占位符结束标识，默认{@link ReadLocation#MATCH_END_SIGN}
     * 
     * @deprecated 该属性已无意义，占位符相关的内容由占位符类对象代替，将在4.3.0或后续版本中删除
     */
    @Deprecated
    protected String endRegex = ReadLocation.MATCH_END_SIGN;

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
     * 该方法用于根据标签的名称，返回相应的定位方式枚举
     *
     * @param labelName 标签名称
     * @return {@link ByType}枚举
     * @deprecated 该方法已过期，已被{@link ByType#typeText2Type(String)}方法代替，将在4.0.0版本后删除
     */
    @Deprecated
    protected ByType toByType(String labelName) {
        switch (labelName) {
        case "xpath":
            return ByType.XPATH;
        case "css":
            return ByType.CSS;
        case "classname":
            return ByType.CLASSNAME;
        case "id":
            return ByType.ID;
        case "linktext":
            return ByType.LINKTEXT;
        case "name":
            return ByType.NAME;
        case "tagname":
            return ByType.TAGNAME;
            // 定义需要忽略的标签
        case "element":
        case "iframe":
        case "limits":
            return null;
        default:
            throw new IllegalArgumentException("不存在的定位方式: " + labelName);
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
        if(!Optional.ofNullable(text).filter(t -> !t.isEmpty()).isPresent()) {
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
