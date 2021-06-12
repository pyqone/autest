package com.auxiliary.tool.file.excel;

import org.apache.poi.common.usermodel.HyperlinkType;

/**
 * <p><b>文件名：</b>LinkType.java</p>
 * <p><b>用途：</b>
 * 用于枚举excel中可用的超链接类型
 * </p>
 * <p><b>编码时间：</b>2020年8月22日下午4:43:35</p>
 * <p><b>修改时间：</b>2021年6月12日下午2:27:23</p>
 * @author 彭宇琦
 * @version Ver1.1
 *
 */
public enum LinkType {
	/**
	 * 表示文件超链接
	 */
	FILE("file", (short) 0, HyperlinkType.FILE), 
	/**
	 * 表示网页超链接
	 */
	URL("url", (short) 1, HyperlinkType.URL), 
	/**
	 * 表示邮箱超链接
	 */
	EMAIL("email", (short) 2, HyperlinkType.EMAIL), 
	/**
	 * 表示文档内超链接
	 */
	DOMCUMENT("dom", (short) 3, HyperlinkType.DOCUMENT);
	
	/**
	 * 用于标记超链接名称
	 */
	private String name;
	/**
	 * 标记枚举的值
	 */
	private short code;
	/**
	 * 标记枚举指向的{@link HyperlinkType}枚举类对象
	 */
	private HyperlinkType hyperlinkType;

	/**
	 * 初始化枚举的名称
	 * @param name 枚举名称
	 */
	private LinkType(String name, short code, HyperlinkType hyperlinkType) {
		this.name = name;
		this.code=code;
		this.hyperlinkType=hyperlinkType;
	}
	
	/**
	 * 用于返回枚举的名称
	 * @return 枚举名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 用于返回枚举的编码
	 * @return 枚举编码
	 */
	public short getCode() {
		return code;
	}

	/**
	 * 与枚举相关的关联的{@link HyperlinkType}枚举类对象
	 * @return {@link HyperlinkType}枚举类对象
	 */
	public HyperlinkType getHyperlinkType() {
		return hyperlinkType;
	}
	
	/**
	 * 用于根据枚举名称，识别枚举，并进行返回
	 * <p>
	 * 若无法查到与之匹配的编码，则返回null
	 * </p>
	 * 
	 * @param code 枚举值
	 * @return 连接类型枚举
	 */
	public static LinkType getMarkColorsType(short code) {
		for (LinkType type : values()) {
			if (type.code == code) {
				return type;
			}
		}

		return null;
	}
}
