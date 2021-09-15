package com.auxiliary.tool.file.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.StringJoiner;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * <p>
 * <b>文件名：</b>XlsTableFileRead.java
 * </p>
 * <p>
 * <b>用途：</b> 定义07版Excel文件的内容读取方法，用于读取以xls为后缀的07版Excel文件。在按行读取文件内容时，会将一行的每一个元素
 * 拼接在一起，每个元素中间会拼接一个{@link AbstractTableFileRead#SPLIT_SIGN}字符，形成一串字符串进行返回。
 * </p>
 * <p>
 * <b>编码时间：</b>2021年9月6日下午7:03:13
 * </p>
 * <p>
 * <b>修改时间：</b>2021年9月6日下午7:03:13
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public class XlsTableFileRead extends AbstractTableFileRead {
	/**
	 * 根据文件类对象，打开相应的文件
	 * 
	 * @param readFile 文件对象
	 * @throws FileException 当文件打开出错时抛出的异常
	 */
	public XlsTableFileRead(File readFile) {
		super(readFile);
		// 读取excel
		try (HSSFWorkbook excel = new HSSFWorkbook(new FileInputStream(
				Optional.ofNullable(readFile).orElseThrow(() -> new FileException("未传入文件对象"))))) {
			// 读取excel中第一个sheet
			HSSFSheet sheet = excel.getSheetAt(0);

			for (int index = 0; index < sheet.getLastRowNum() + 1; index++) {
				// 读取当前行，若该行为空，则不添加数据
				HSSFRow row = sheet.getRow(index);
				if (row == null) {
					continue;
				}
				
				// 添加该列中没行的数据
				StringJoiner text = new StringJoiner(SPLIT_SIGN);
				row.forEach(cell -> text.add(cell.getStringCellValue()));
				
				textList.add(text.toString());
			}
		} catch (IOException e) {
			throw new FileException("文件读取异常", readFile);
		}
	}
	
	/**
	 * 返回07版Excel文件读取类对象
	 * 
	 * @return 07版Excel文件读取类对象
	 */
	public HSSFWorkbook getReadClass() {
		try (HSSFWorkbook excel = new HSSFWorkbook(new FileInputStream(
				Optional.ofNullable(readFile).orElseThrow(() -> new FileException("未传入文件对象"))))) {
			return excel;
		} catch (IOException e) {
			throw new FileException("文件读取异常", readFile, e);
		}
	}
}
