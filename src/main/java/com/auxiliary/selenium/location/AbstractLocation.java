package com.auxiliary.selenium.location;

import java.util.ArrayList;

import com.auxiliary.selenium.element.ElementType;

/**
 * <p><b>文件名：</b>AbstractReadConfig.java</p>
 * <p><b>用途：</b>
 * 定义读取自动化测试中元素定位方式的基本方法
 * </p>
 * <p><b>编码时间：</b>2020年9月28日上午7:37:00</p>
 * <p><b>修改时间：</b>2020年9月28日上午7:37:00</p>
 * @author 彭宇琦
 * @version Ver1.0
 *
 */
public abstract class AbstractLocation implements ReadLocation {
	/**
	 * 定义用于正则的替换符开始标记
	 */
	public static final String MATCH_START_SIGN = "\\$\\{";
	/**
	 * 定义替换符开始标志
	 */
	public static final String START_SIGN = "${";
	/**
	 * 定义用于正则的替换符结束标记
	 */
	public static final String MATCH_END_SIGN = "\\}";
	/**
	 * 定义替换符结束标志
	 */
	public static final String END_SIGN = "}";
	
	/**
	 * 元素名称
	 */
	protected String name = "";
	/**
	 * 元素定位方式集合
	 */
	protected ArrayList<ByType> byTypeList = new ArrayList<>();
	/**
	 * 元素定位内容集合
	 */
	protected ArrayList<String> valueList = new ArrayList<>();
	/**
	 * 元素类型
	 */
	protected ElementType elementType;
	/**
	 * 元素父层窗体集合
	 */
	protected ArrayList<String> iframeNameList = new ArrayList<>();
	/**
	 * 元素等待时间
	 */
	protected long waitTime = -1;
	
	
	/**
	 * 用于将读取到的元素类型的文本值转换为元素类型枚举类对象
	 * @param value 元素类型文本值
	 * @return 元素类型枚举类对象
	 */
	protected ElementType toElementType(String value) {
		//转换元素类型枚举，并返回
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
	 * @param labelName 标签名称
	 * @return {@link ByType}枚举
	 */
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
		//定义需要忽略的标签
		case "element":
		case "iframe":
			return null;
		default:
			throw new IllegalArgumentException("不存在的定位方式: " + labelName);
		}
	}
	
	/**
	 * 用于对等待时间进行转换
	 * @param text 获取的文本
	 * @return 转换后的等待时间
	 */
	protected long toWaitTime(String text) {
		//获取元素存储等待时间属性值，并转换为long类型
		try {
			//将属性值进行转换，若属性值不存在，则赋为-1
			long time = Long.valueOf(text == null ? "-1" : text);
			//若转换的时间小于0，则返回-1
			return time < 0 ? -1L : time;
		} catch (NumberFormatException e) {
			return -1L;
		}
	}
	
	/**
	 * 用于清理集合类型的缓存
	 */
	protected void clearCache() {
		byTypeList.clear();
		valueList.clear();
		iframeNameList.clear();
	}
}
