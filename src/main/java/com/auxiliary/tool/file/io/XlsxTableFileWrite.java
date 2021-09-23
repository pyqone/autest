package com.auxiliary.tool.file.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.StringJoiner;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.auxiliary.tool.data.TableData;
import com.auxiliary.tool.file.WriteFileException;
import com.auxiliary.tool.file.excel.ExcelFileTemplet;

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
	/**
	 * 用于存储excel对象
	 */
	private XSSFWorkbook excel;
	/**
	 * 用于存储当前指向的sheet页
	 */
	private XSSFSheet sheet;
	
	/**
	 * 根据文件类对象，打开相应的文件
	 * 
	 * @param readFile 文件对象
	 * @throws FileException 当文件打开出错时抛出的异常
	 */
	public XlsxTableFileWrite(File writeFile) {
		if (writeFile.exists()) {
			try (FileInputStream fip = new FileInputStream(writeFile)) {
				// 通过输入流，使XSSFWorkbook对象指向模版文件
				excel = new XSSFWorkbook(fip);
			} catch (IOException e) {
				throw new FileException("文件读取异常", writeFile);
			}
		} else {
			// 创建文件存储的路径文件夹
			writeFile.getParentFile().mkdirs();
			excel = new XSSFWorkbook();
		}
	}
	
	/**
	 * @return the read
	 */
	public XSSFWorkbook getWriteClass() {
		return null;
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
