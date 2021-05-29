package com.auxiliary.tool.file;

/**
 * <p>
 * <b>文件名：</b>MarkTextFont.java
 * </p>
 * <p>
 * <b>用途：</b> 规定可对写入文本内容字体进行更改的基本方法
 * </p>
 * <p>
 * <b>编码时间：</b>2021年5月18日上午8:21:20
 * </p>
 * <p>
 * <b>修改时间：</b>2021年5月18日上午8:21:20
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @param <T> 子类
 */
public interface MarkTextFont<T extends MarkTextFont<T>> {
	/**
	 * 标记对文本加粗的字段名
	 */
	String KEY_BOLD = "bold";
	/**
	 * 标记对文本倾斜的字段名
	 */
	String KEY_ITALIC = "italic";
	/**
	 * 标记对文本添加下划线的字段名
	 */
	String KEY_UNDERLINE = "underline";
	String KEY_FONT_NAME = "fontName";
	String KEY_FONT_SIZE = "fontSize";

	/**
	 * 用于对指定文本内容的字体进行加粗
	 * 
	 * @param field      字段
	 * @param textIndexs 字段中文本的下标
	 * @return 类本身
	 */
	T bold(String field, int... textIndexs);

	/**
	 * 用于倾斜指定文本内容的字体
	 * 
	 * @param field      字段
	 * @param textIndexs 字段中文本的下标
	 * @return 类本身
	 */
	T italic(String field, int... textIndexs);

	/**
	 * 用于对指定文本内容的字体加上下划线
	 * 
	 * @param field      字段
	 * @param textIndexs 字段中文本的下标
	 * @return 类本身
	 */
	T underline(String field, int... textIndexs);
}
