package com.auxiliary.tool.file.io;

/**
 * <p><b>文件名：</b>FileType.java</p>
 * <p><b>用途：</b>
 * 定义可读取/写入的文件类型枚举
 * </p>
 * <p><b>编码时间：</b>2021年9月6日下午8:01:19</p>
 * <p><b>修改时间：</b>2021年9月6日下午8:01:19</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public enum FileType {
	/**
	 * 纯文本型文件
	 */
	TXT, 
	/**
	 * 07版word文件
	 */
	DOC, 
	/**
	 * 新版word文件
	 */
	DOCX, 
	/**
	 * 逗号分隔型数据文件（以csv结尾等）
	 */
	CSV, 
	/**
	 * 07版excel文件
	 */
	XLS, 
	/**
	 * 新版excel文件
	 */
	XLSX;
}
