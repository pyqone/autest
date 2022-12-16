package com.auxiliary.selenium.location;

import org.openqa.selenium.By;

import com.auxiliary.tool.common.DisposeCodeUtils;

/**
 * <p>
 * <b>文件名：</b>ByType.java
 * </p>
 * <p>
 * <b>用途：</b>用于枚举出能被识别的元素定位方式
 * </p>
 * <p>
 * <b>编码时间：</b>2019年10月24日下午5:18:57
 * </p>
 * <p>
 * <b>修改时间：</b>修改时间：2022年12月13日 上午8:03:18
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.1
 * @since JDK 1.8
 * @since autest 2.0.0
 */
public enum ByType {
	/** 通过xpath方式进行定位 */
	XPATH("xpath"),
	/** 通过css方式进行定位 */
	CSS("css"),
	/** 通过className方式进行定位 */
	CLASSNAME("classname"),
	/** 通过id方式进行定位 */
	ID("id"),
	/** 通过linkText方式进行定位 */
	LINKTEXT("linktext"),
	/** 通过name方式进行定位 */
	NAME("name"),
	/** 通过tagName方式进行定位 */
	TAGNAME("tagname"), 
	/**
	 * 通过jQuert的方式进行定位
	 */
//	JQ("jquert"), 
	;

	/**
	 * 定义枚举值
	 */
	private String value;

    /**
     * 初始化枚举值
     * 
     * @param value 枚举值
     */
	private ByType(String value) {
		this.value = value;
	}

	/**
	 * 返回元素定位枚举对应的定位方式名称
	 * 
	 * @return 元素定位方式名称
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * 根据枚举内容，返回相应的{@link By}对象
	 * @param value 元素的定位内容
	 * @return 包含定位内容的元素{@link By}对象
	 */
	public By getBy(String value) {
		switch (this) {
		case CLASSNAME:
			return By.className(value);
		case CSS:
			return By.cssSelector(value);
		case ID:
			return By.id(value);
		case LINKTEXT:
			return By.linkText(value);
		case NAME:
			return By.name(value);
		case TAGNAME:
			return By.tagName(value);
		case XPATH:
			return By.xpath(value);
		default:
			return null;
		}
	}

    /**
     * 该方法用于将枚举文本转换为消息枚举
     * 
     * @param typeText 枚举文本
     * @return 枚举类对象
     * @since autest 3.8.0
     */
    public static ByType typeText2Type(String typeText) {
        return DisposeCodeUtils.disposeEnumTypeText(ByType.class, typeText, text -> text, false, (short) 1);
    }
}
