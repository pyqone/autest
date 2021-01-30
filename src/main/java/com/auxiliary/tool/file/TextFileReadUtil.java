package com.auxiliary.tool.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <p><b>文件名：</b>TextFileReadUtil.java</p>
 * <p><b>用途：</b>将存储在文件中的文本内容，以字符串的形式拼接读取。文件支持表格文件（xls/xlsx/csv格式）和
 * 文本文件（doc/docx/txt格式）。若读取的文件为文本型文件，则按行对表格内容进行拼接。
 * </p>
 * <p><b>编码时间：</b>2021年1月30日下午5:46:21</p>
 * <p><b>修改时间：</b>2021年1月30日下午5:46:21</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 *
 */
public class TextFileReadUtil {
	private TextFileReadUtil() {
	}
	
	/**
	 * 用于以行元素单位，合并csv文件中的文本内容，并以集合的形式返回
	 * @param file 待读取文件对象
	 * @return 合并后的文本集合
	 */
	public static List<String> mergeRowDataToCsv(File file) {
		List<String> rowDataList = new ArrayList<>();
		
		//调用列表型文件读取方式读取文件，并将每行数据拼接成字符串返回
		TableFileReadUtil.readCsv(file, false).rowStream()
			.map(list -> {
				StringBuilder text = new StringBuilder();
				list.stream().map(str -> str.orElse("")).forEach(text::append);
				return text.toString();
			})
			.forEach(rowDataList::add);

		return rowDataList;
	}
	
	/**
	 * 用于以列元素单位，合并csv文件中的文本内容，并以集合的形式返回
	 * @param file 待读取文件对象
	 * @return 合并后的文本集合
	 */
	public static List<String> mergeColumnDataToCsv(File file) {
		List<String> columnDataList = new ArrayList<>();
		
		//调用列表型文件读取方式读取文件，并将每行数据拼接成字符串返回
		TableFileReadUtil.readCsv(file, false).columnForEach((title, value) -> {
			StringBuilder text = new StringBuilder();
			value.stream().map(str -> str.orElse("")).forEach(text::append);
			columnDataList.add(text.toString());
		});
		
		return columnDataList;
	}
	
	/**
	 * 用于以行元素单位，合并csv文件中所有的元素内容，并以字符串的形式返回。
	 * @param file 待读取文件对象
	 * @return 合并后的文本集合
	 */
	public static String mergeAllRowDataToCsv(File file) {
		StringBuilder text = new StringBuilder();
		mergeRowDataToCsv(file).forEach(text::append);
		
		return text.toString();
	}
	
	/**
	 * 用于以列元素单位，合并csv文件中所有的元素内容，并以字符串的形式返回。
	 * @param file 待读取文件对象
	 * @return 合并后的文本集合
	 */
	public static String mergeAllColumnDataToCsv(File file) {
		StringBuilder text = new StringBuilder();
		mergeColumnDataToCsv(file).forEach(text::append);
		
		return text.toString();
	}
	
	/**
	 * 用于以行元素单位，合并excel文件中的文本内容，并以集合的形式返回
	 * @param file 待读取文件对象
	 * @return 合并后的文本集合
	 */
	public static List<String> mergeRowDataToExcel(File file, String sheetName) {
		List<String> rowDataList = new ArrayList<>();
		
		//调用列表型文件读取方式读取文件，并将每行数据拼接成字符串返回
		TableFileReadUtil.readExcel(file, sheetName, false).rowStream()
			.map(list -> {
				StringBuilder text = new StringBuilder();
				list.stream().map(str -> str.orElse("")).forEach(text::append);
				return text.toString();
			})
			.forEach(rowDataList::add);

		return rowDataList;
	}
	
	/**
	 * 用于以列元素单位，合并excel文件中的文本内容，并以集合的形式返回
	 * @param file 待读取文件对象
	 * @return 合并后的文本集合
	 */
	public static List<String> mergeColumnDataToExcel(File file, String sheetName) {
		List<String> columnDataList = new ArrayList<>();
		
		//调用列表型文件读取方式读取文件，并将每行数据拼接成字符串返回
		TableFileReadUtil.readExcel(file, sheetName, false).columnForEach((title, value) -> {
			StringBuilder text = new StringBuilder();
			value.stream().map(str -> str.orElse("")).forEach(text::append);
			columnDataList.add(text.toString());
		});
		
		return columnDataList;
	}
	
	/**
	 * 用于以行元素单位，合并excel文件中所有的元素内容，并以字符串的形式返回。
	 * @param file 待读取文件对象
	 * @return 合并后的文本集合
	 */
	public static String mergeAllRowDataToExcel(File file, String sheetName) {
		StringBuilder text = new StringBuilder();
		mergeRowDataToExcel(file, sheetName).forEach(text::append);
		
		return text.toString();
	}
	
	/**
	 * 用于以列元素单位，合并excel文件中所有的元素内容，并以字符串的形式返回。
	 * @param file 待读取文件对象
	 * @return 合并后的文本集合
	 */
	public static String mergeAllColumnDataToExcel(File file, String sheetName) {
		StringBuilder text = new StringBuilder();
		mergeColumnDataToExcel(file, sheetName).forEach(text::append);
		
		return text.toString();
	}
}
