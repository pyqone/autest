package com.auxiliary.tool.file.io;

import java.io.File;
import java.util.Arrays;

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
	 * 存储表格中的数据
	 */
	TableData<String> table = new TableData<String>();
	/**
	 * 存储当前列表是否存在标题行
	 */
	boolean isFirstTitle = true;
	
	/**
	 * 空占位符，在表格数据拼接时使用，亦可用于字符串的切割
	 */
	public static final String SPLIT_SIGN = "\u200b";
	
	public AbstractTableFileRead(File readFile) {
		super(readFile);
		// 设置返回数据时不进行严格检查
		table.setExamine(false);
	}
	
	/**
	 * 用于设置当前表格首行是否为标题行
	 * @param isFirstTitle 是否为标题行
	 */
	public void isFirstTitle(boolean isFirstTitle) {
		this.isFirstTitle = isFirstTitle;
	}

	/**
	 * 用于对表格所有的数据进行返回
	 * 
	 * @return 表格数据类对象{@link TableData}
	 */
	public TableData<String> getTable() {
		// 判断当前表格数据是否为空，为空，则将内容切分后，对数据进行存储
		if (table.isEmpty()) {
			textList.stream().map(text -> text.split(SPLIT_SIGN)).map(texts -> Arrays.asList(texts))
					.forEach(table::addRow);
		}

		// 表格数据不为空时，则对表格数据进行返回
		return table;
	}
}
