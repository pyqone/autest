package com.auxiliary.tool.file.io;

import com.auxiliary.tool.data.TableData;

/**
 * <p>
 * <b>文件名：</b>AbstractTableFileWrite.java
 * </p>
 * <p>
 * <b>用途：</b> 定义写入表格型文件的方法
 * </p>
 * <p>
 * <b>编码时间：</b>2021年9月6日下午7:42:28
 * </p>
 * <p>
 * <b>修改时间：</b>2021年9月6日下午7:42:28
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public abstract class AbstractTableFileWrite extends AbstractFileWrite {
	/**
	 * 指向当前列表是否包含标题行
	 */
	protected boolean isExistTitle = true;

	/**
	 * 用于添加整个列表数据
	 * <p>
	 * <b>注意：</b>由于列表数据类对象包含标题，故根据是否需要判断标题数据的参数不同，其写入文件的结果也不同
	 * <ul>
	 * <li>当不需要判断标题数据时，则将每列数据，按照当前已有的顺序，从第一列开始添加数据；若TableData对象中的列比当前存在的数据列多，则新增列添加数据</li>
	 * <li>当需要判断标题时，则将TableData对象中的标题与文件缓存中的标题行（第一行）进行对比，存在的标题列，则向该列下方添加数据；不存在时，则新增列添加数据</li>
	 * </ul>
	 * </p>
	 * 
	 * @param tableData    列表数据类对象
	 * @param isJudgeTitle 是否需要判断标题数据
	 */
	public abstract void addTable(TableData<String> tableData, boolean isJudgeTitle);

	/**
	 * 根据标题添加一列数据，若标题不存在时，则新增一列添加数据
	 * 
	 * @param columnName 列名称（标题）
	 * @param datas      数据组
	 */
	public abstract void addColumn(String columnName, String... datas);

	/**
	 * 根据指定的列下标添加一列数据
	 * 
	 * @param columnIndex 列下标
	 * @param datas       数据组
	 */
	public abstract void addColumn(int columnIndex, String... datas);

	/**
	 * 指定添加数据的单元格
	 * 
	 * @param rowIndex    行下标
	 * @param columnIndex 列下标
	 * @param data        数据
	 */
	public abstract void addCell(int rowIndex, int columnIndex, String data);
}
