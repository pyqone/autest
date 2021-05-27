package com.auxiliary.tool.file;

/**
 * <p>
 * <b>文件名：</b>WriteFilePage.java
 * </p>
 * <p>
 * <b>用途：</b> 规定写入存在多页数据时所必须方法
 * </p>
 * <p>
 * <b>编码时间：</b>2021年5月18日上午8:07:59
 * </p>
 * <p>
 * <b>修改时间：</b>2021年5月18日上午8:07:59
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public interface WriteFilePage {
	/**
	 * 指向添加到模板属性中的“name”字段
	 */
	String KEY_NAME = "name";

	/**
	 * 用于添加模板，并设置模板的名称，方便模板之间的切换
	 * 
	 * @param name    模板名称
	 * @param templet 模板类对象
	 */
	default void addTemplet(String name, FileTemplet templet) {
		templet.addTempletAttribute("name", name);
	}

	/**
	 * 根据模板名称，对模板文件进行切换
	 * 
	 * @param name 模板名称
	 */
	void switchPage(String name);
	
	/**
	 * 用于返回指定名称的模板类
	 * @param name 模板名称
	 * @return 相应名称的模板类
	 */
	FileTemplet getTemplet(String name);
}
