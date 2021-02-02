package com.auxiliary.tool.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

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
	 * 用于以行元素单位，合并csv文件中所有的元素内容，并以字符串的形式返回
	 * @param file 待读取文件对象
	 * @param isLine 是否插入换行符
	 * @return 合并后的文本
	 */
	public static String mergeAllRowDataToCsv(File file, boolean isLine) {
		return megerText(mergeRowDataToCsv(file), isLine);
	}
	
	/**
	 * 用于以列元素单位，合并csv文件中所有的元素内容，并以字符串的形式返回
	 * @param file 待读取文件对象
	 * @param isLine 是否插入换行符
	 * @return 合并后的文本
	 */
	public static String mergeAllColumnDataToCsv(File file, boolean isLine) {
		return megerText(mergeColumnDataToCsv(file), isLine);
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
	 * 用于以行元素单位，合并excel文件中所有的元素内容，并以字符串的形式返回
	 * @param file 待读取文件对象
	 * @param sheetName 读取的sheet名称
	 * @param isLine 是否插入换行符
	 * @return 合并后的文本
	 */
	public static String mergeAllRowDataToExcel(File file, String sheetName, boolean isLine) {
		return megerText(mergeRowDataToExcel(file, sheetName), isLine);
	}
	
	/**
	 * 用于以列元素单位，合并excel文件中所有的元素内容，并以字符串的形式返回
	 * @param file 待读取文件对象
	 * @param sheetName 读取的sheet名称
	 * @param isLine 是否插入换行符
	 * @return 合并后的文本
	 */
	public static String mergeAllColumnDataToExcel(File file, String sheetName, boolean isLine) {
		return megerText(mergeColumnDataToExcel(file, sheetName), isLine);
	}
	
	/**
	 * 用于读取旧版word（2003版本word）文档中每段的内容，并将段落内容以集合的形式返回
	 * @param file 待读取文件对象
	 * @return 读取的段落集合
	 */
	public static List<String> oldWordToLineList(File file) {
		List<String> lineTextList = new ArrayList<>();
		
		// 读取word
		Optional<HWPFDocument> wordOptional = Optional.empty();
		try (FileInputStream fip = new FileInputStream(
				Optional.ofNullable(file).orElseThrow(() -> new UnsupportedFileException("未传入文件对象")))) {
			wordOptional = Optional.ofNullable(new HWPFDocument(fip));
		} catch (IOException e) {
			throw new UnsupportedFileException("文件异常，无法进行读取：" + file.getAbsolutePath(), e);
		}

		try (HWPFDocument word = wordOptional.orElseThrow(() -> new UnsupportedFileException("Word文件读取类未构造"))) {
			// 获取word中的所有内容
			Range wordFileRange = word.getRange();
			// 生成段落下标，遍历word文档中的所有段落
			IntStream.range(0, wordFileRange.numParagraphs())
					// 将下标转换为段落类对象
					.mapToObj(wordFileRange::getParagraph)
					.filter(para -> para != null)
					// 读取段落的内容，对内容进行封装
					.map(Paragraph::text)
					// 去除换行符，并过滤掉空行
					.map(text -> text.replaceAll("\\r", ""))
					// 按行存储至列表对象中
					.forEach(lineTextList::add);
		} catch (IOException e) {
			throw new UnsupportedFileException("文件异常，无法进行读取：" + file.getAbsolutePath(), e);
		}
		
		return lineTextList;
	}
	
	/**
	 * 用于合并旧版word（2003版本word）文档中每段的内容，并以字符串的形式返回
	 * @param file 待读取文件对象
	 * @param isLine 是否插入换行符
	 * @return 合并后的文本
	 */
	public static String megerTextToOldWord (File file, boolean isLine) {
		return megerText(oldWordToLineList(file), isLine);
	}
	
	/**
	 * 用于读取新版word（2007及以上版本word）文档中每段的内容，并将段落内容以集合的形式返回
	 * @param file 待读取文件对象
	 * @return 读取的段落集合
	 */
	public static List<String> newWordToLineList(File file) {
		List<String> lineTextList = new ArrayList<>();
		
		// 读取word
		Optional<XWPFDocument> wordOptional = Optional.empty();
		try (FileInputStream fip = new FileInputStream(
				Optional.ofNullable(file).orElseThrow(() -> new UnsupportedFileException("未传入文件对象")))) {
			wordOptional = Optional.ofNullable(new XWPFDocument(fip));
		} catch (IOException e) {
			throw new UnsupportedFileException("文件异常，无法进行读取：" + file.getAbsolutePath(), e);
		}

		try (XWPFDocument word = wordOptional.orElseThrow(() -> new UnsupportedFileException("Word文件读取类未构造"))) {
			// 获取文本中所有的段落，若文档中无内容，则抛出异常
			Optional.ofNullable(word.getParagraphs())
					//判定当前内容是否为空
					.filter(list -> list.size() != 0)
					//返回段落集合，若段落集合为空，则抛出异常
					.orElseThrow(() -> new UnsupportedFileException("Word文件中无内容"))
					.stream()
					//过滤掉为null的段落
					.filter(para -> para != null)
					//将段落转换为为本
					.map(XWPFParagraph::getText)
					//拼接文本
					.forEach(lineTextList::add);
		} catch (IOException e) {
			throw new UnsupportedFileException("文件异常，无法进行读取：" + file.getAbsolutePath(), e);
		}
		
		return lineTextList;
	}
	
	/**
	 * 用于合并新版word（2007及以上版本word）文档中每段的内容，并以字符串的形式返回
	 * @param file 待读取文件对象
	 * @param isLine 是否插入换行符
	 * @return 合并后的文本
	 */
	public static String megerTextToNewWord(File file, boolean isLine) {
		return megerText(newWordToLineList(file), isLine);
	}
	
	/**
	 * 用于读取txt格式纯文本文档中每段的内容，并将段落内容以集合的形式返回
	 * @param file 待读取文件对象
	 * @return 读取的段落集合
	 */
	public static List<String> txtToLineList(File file) {
		List<String> lineTextList = new ArrayList<>();
		
		try (BufferedReader br = new BufferedReader(
				new FileReader(Optional.ofNullable(file).orElseThrow(() -> new UnsupportedFileException("未传入文件对象"))))) {
			// 按行遍历文件内容
			br.lines().forEach(lineTextList::add);
		} catch (IOException e) {
			throw new UnsupportedFileException("文件异常，无法进行读取：" + file.getAbsolutePath(), e);
		}
		
		return lineTextList;
	}
	
	/**
	 * 用于合并txt格式纯文本文档中每段的内容，并以字符串的形式返回
	 * @param file 待读取文件对象
	 * @param isLine 是否插入换行符
	 * @return 合并后的文本
	 */
	public static String megerTextToTxt(File file, boolean isLine) {
		return megerText(txtToLineList(file), isLine);
	}
	
	/**
	 * 用于对集合型内容进行合并
	 * @param textList 内容集合
	 * @param isLine 是否插入换行符
	 * @return 拼接后的文本
	 */
	private static String megerText(List<String> textList, boolean isLine) {
		StringBuilder megerText = new StringBuilder();
		textList.stream().map(str -> str += (isLine ? "\n" : "")).forEach(megerText::append);
		return megerText.toString();
	}
}
