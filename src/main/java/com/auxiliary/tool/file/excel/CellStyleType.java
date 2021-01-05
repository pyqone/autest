package com.auxiliary.tool.file.excel;

import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFont;

/**
 * <p><b>文件名：</b>CellStyleType.java</p>
 * <p><b>用途：</b>
 * 枚举常用的单元格样式，方便后续调用
 * </p>
 * <p><b>编码时间：</b>2020年8月14日上午7:49:07</p>
 * <p><b>修改时间：</b>2020年8月14日上午7:49:07</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since POI 3.15
 */
public enum CellStyleType {
	/**
	 * 指向超链接文本样式
	 */
	LINK_TEXT;
	
	public void getXSSFCellStyle(XSSFCell cell) {
		switch (this) {
		case LINK_TEXT:
			// 获取字体信息
			XSSFFont xf = cell.getCellStyle().getFont();
			// 添加下划线
			xf.setUnderline((byte) 1);
			// 设置字体颜色为蓝色
			xf.setColor(IndexedColors.BLUE.getIndex());
			break;

		default:
			break;
		}
	}
}
