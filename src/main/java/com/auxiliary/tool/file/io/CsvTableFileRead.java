package com.auxiliary.tool.file.io;

import java.io.File;
import java.util.List;

import com.auxiliary.tool.data.TableData;
import com.opencsv.CSVReader;

/**
 * <p><b>文件名：</b>CsvTableFileRead.java</p>
 * <p><b>用途：</b>
 * 定义csv文件的内容读取方法，用于读取以csv为后缀的表格文件。在按行读取文件内容时，会将一行的每一个元素
 * 拼接在一起，每个元素中间会拼接一个{@link AbstractTableFileRead#SPLIT_SIGN}字符，形成一串字符串进行返回。
 * </p>
 * <p><b>编码时间：</b>2021年9月6日下午7:03:13</p>
 * <p><b>修改时间：</b>2021年9月6日下午7:03:13</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public class CsvTableFileRead extends AbstractTableFileRead {
	CSVReader read;
	
	public CsvTableFileRead(File readFile) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String nextLine() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> nextLine(String splitSign) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> readAllContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public TableData<String> getTable() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * @return the read
	 */
	public CSVReader getReadClass() {
		return read;
	}

	@Override
	public void againRead() {
		// TODO Auto-generated method stub
		
	}
}
