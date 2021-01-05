package com.auxiliary.tool.file.excel;

/**
 * <p><b>文件名：</b>LinkType.java</p>
 * <p><b>用途：</b>
 * 用于枚举excel中可用的超链接类型
 * </p>
 * <p><b>编码时间：</b>2020年8月22日下午4:43:35</p>
 * <p><b>修改时间：</b>2020年8月22日下午4:43:35</p>
 * @author 彭宇琦
 * @version Ver1.0
 *
 */
public enum LinkType {
	/**
	 * 表示文件超链接
	 */
	FILE("file"), 
	/**
	 * 表示网页超链接
	 */
	URL("url"), 
	/**
	 * 表示邮箱超链接
	 */
	EMAIL("email"), 
	/**
	 * 表示文档内超链接
	 */
	DOMCUMENT("dom");
	
	/**
	 * 用于标记超链接名称
	 */
	private String name;

	/**
	 * 初始化枚举的名称
	 * @param name 枚举名称
	 */
	private LinkType(String name) {
		this.name = name;
	}
	
	/**
	 * 用于返回枚举的名称
	 * @return 枚举名称
	 */
	public String getName() {
		return name;
	}
}
