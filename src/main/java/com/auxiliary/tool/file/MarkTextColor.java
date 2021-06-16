package com.auxiliary.tool.file;

/**
 * <p>
 * <b>文件名：</b>MarkTextColor.java
 * </p>
 * <p>
 * <b>用途：</b> 规定可对写入文本内容字体颜色进行更改的基本方法
 * </p>
 * <p>
 * <b>编码时间：</b>2021年5月18日上午8:30:35
 * </p>
 * <p>
 * <b>修改时间：</b>2021年5月18日上午8:30:35
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @param <T> 子类
 */
public interface MarkTextColor<T extends MarkTextColor<T>> {
	/**
	 * 标记对内容添加颜色
	 */
	String KEY_COLOR = "color";
	
	/**
	 * 用于对指定文本内容的字体颜色进行更改
	 * 
	 * @param markColorsType 颜色枚举
	 * @param field          字段
	 * @param textIndexs     字段中文本的下标
	 * @return 类本身
	 */
	T changeTextColor(MarkColorsType markColorsType, String field, int... textIndexs);
}
