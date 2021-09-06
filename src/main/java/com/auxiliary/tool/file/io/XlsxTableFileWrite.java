package com.auxiliary.tool.file.io;

import java.io.File;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.auxiliary.tool.data.TableData;

/**
 * <p><b>文件名：</b>XlsxTableFileWrite.java</p>
 * <p><b>用途：</b>
 * 定义新版Excel文件的内容读取方法，用于读取以xlsx为后缀的新版Excel文件。
 * </p>
 * <p><b>编码时间：</b>2021年9月6日下午7:03:13</p>
 * <p><b>修改时间：</b>2021年9月6日下午7:03:13</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public class XlsxTableFileWrite extends AbstractTableFileWrite {
	XSSFWorkbook write;
	
	public XlsxTableFileWrite(File writeFile) {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @return the read
	 */
	public XSSFWorkbook getWriteClass() {
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
