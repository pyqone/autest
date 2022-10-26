package com.auxiliary.tool.file;

import org.apache.poi.ss.usermodel.IndexedColors;

/**
 * <p>
 * <b>文件名：</b>MarkFieldBackground.java
 * </p>
 * <p>
 * <b>用途：</b> 规定对字段所在位置的背景进行颜色标记的基本方法
 * </p>
 * <p>
 * <b>编码时间：</b>2021年5月19日上午7:58:01
 * </p>
 * <p>
 * <b>修改时间：</b>2021年5月19日上午7:58:01
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @param <T> 子类
 */
public interface MarkFieldBackground<T extends MarkFieldBackground<T>> {
	/**
     * 用于标记当前内容的所有字段背景颜色
     * 
     * @param markColorsType 颜色枚举
     * @return 类本身
     * @deprecated 该方法由{@link #changeCaseBackground(IndexedColors)}方法代替，将在后续版本中删除
     */
	@Deprecated
    T changeCaseBackground(MarkColorsType markColorsType);

	/**
     * 用于标记当前内容指定字段的背景颜色
     * 
     * @param field          字段名称
     * @param markColorsType 颜色枚举
     * @return 类本身
     * @deprecated 该方法由{@link #changeFieldBackground(String, IndexedColors)}方法代替，将在后续版本中删除
     */
	@Deprecated
    T changeFieldBackground(String field, MarkColorsType markColorsType);

    /**
     * 用于标记当前内容的所有字段背景颜色
     * 
     * @param indexedColors 颜色枚举
     * @return 类本身
     * @since autest 3.7.0
     */
    T changeCaseBackground(IndexedColors indexedColors);

    /**
     * 用于标记当前内容指定字段的背景颜色
     * 
     * @param field         字段名称
     * @param indexedColors 颜色枚举
     * @return 类本身
     * @since autest 3.7.0
     */
    T changeFieldBackground(String field, IndexedColors indexedColors);
}
