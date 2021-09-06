package com.auxiliary.tool.file.io;

import java.io.File;

import com.auxiliary.tool.data.TableData;
import com.opencsv.CSVWriter;

/**
 * <p><b>文件名：</b>CsvTableFileWrite.java</p>
 * <p><b>用途：</b>
 * 定义csv文件的内容写入方法，用于写入以csv为后缀的表格文件。
 * </p>
 * <p><b>编码时间：</b>2021年9月6日下午7:03:13</p>
 * <p><b>修改时间：</b>2021年9月6日下午7:03:13</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public class CsvTableFileWrite extends AbstractTableFileWrite {
	CSVWriter write;
	
	public CsvTableFileWrite(File writeFile) {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @return the read
	 */
	public CSVWriter getWriteClass() {
		return write;
	}

	@Override
	public void addTable(TableData<String> tableData, boolean isJudgeTitle) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addColumn(String columnName, String... datas) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addColumn(int columnIndex, String... datas) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addCell(int rowIndex, int columnIndex, String data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addLine(String... datas) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void newLine() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
}
