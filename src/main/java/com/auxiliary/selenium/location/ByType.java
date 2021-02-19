package com.auxiliary.selenium.location;

import org.openqa.selenium.By;

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
 * <b>修改时间：</b>2019年10月24日下午5:18:57
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 *
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
}
