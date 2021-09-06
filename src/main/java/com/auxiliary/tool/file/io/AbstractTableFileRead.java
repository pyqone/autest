package com.auxiliary.tool.file.io;

import com.auxiliary.tool.data.TableData;

/**
 * <p>
 * <b>文件名：</b>AbstractTableFileRead.java
 * </p>
 * <p>
 * <b>用途：</b> 定义表格型文件的读取基本方法
 * </p>
 * <p>
 * <b>编码时间：</b>2021年9月6日下午6:52:18
 * </p>
 * <p>
 * <b>修改时间：</b>2021年9月6日下午6:52:18
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public abstract class AbstractTableFileRead extends AbstractFileRead {
	/**
	 * 空占位符，在表格数据拼接时使用，亦可用于字符串的切割
	 */
	public static final String SPLIT_SIGN = "\u200b";

	/**
	 * 用于对表格所有的数据进行返回
	 * 
	 * @return 表格数据类对象{@link TableData}
	 */
	public abstract TableData<String> getTable();
}
