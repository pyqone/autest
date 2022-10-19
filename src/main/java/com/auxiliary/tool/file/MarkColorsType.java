package com.auxiliary.tool.file;

import org.apache.poi.ss.usermodel.IndexedColors;

/**
 * <p>
 * <b>文件名：</b>MarkColorsType.java
 * </p>
 * <p>
 * <b>用途：</b>枚举可用的标记颜色
 * </p>
 * <p>
 * <b>编码时间：</b>2020年2月25日上午8:31:14
 * </p>
 * <p>
 * <b>修改时间：</b>2022年10月19日 上午10:11:33
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @deprecated 该枚举类已无意义，由{@link IndexedColors}枚举类进行代替
 */
public enum MarkColorsType {
	/**
	 * 红色
	 */
	RED(IndexedColors.RED.getIndex()),
	/**
	 * 黄色
	 */
	YELLOW(IndexedColors.YELLOW.getIndex()),
	/**
	 * 蓝色
	 */
	BLUE(IndexedColors.BLUE.getIndex()),
	/**
	 * 绿色
	 */
	GREEN(IndexedColors.GREEN.getIndex());
	
	/**
	 * 标记枚举的值
	 */
	private short colorsValue;
	
	/**
	 * 设置枚举值
	 * @param colorsValue 枚举值
	 */
	private MarkColorsType(short colorsValue) {
		this.colorsValue = colorsValue;
	}

	/**
	 * 用于返回颜色枚举的值
	 * @return 颜色枚举值
	 */
	public short getColorsValue() {
		return colorsValue;
	}
	
	/**
	 * 用于根据枚举名称，识别枚举，并进行返回
	 * <p>
	 * 若无法查到与之匹配的编码，则返回null
	 * </p>
	 * 
	 * @param colorsValue 枚举名称
	 * @return 颜色枚举
	 */
	public static MarkColorsType getMarkColorsType(short colorsValue) {
		for (MarkColorsType type : values()) {
			if (type.colorsValue == colorsValue) {
				return type;
			}
		}

		return null;
	}
}
