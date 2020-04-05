package pres.auxiliary.work.testcase.file;

import org.apache.poi.ss.usermodel.IndexedColors;

/**
 * <p><b>文件名：</b>MarkColorsType.java</p>
 * <p><b>用途：</b>枚举可用的标记颜色</p>
 * <p><b>编码时间：</b>2020年2月25日上午8:31:14</p>
 * <p><b>修改时间：</b>2020年2月25日上午8:31:14</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
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
	
	
}
