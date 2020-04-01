package pres.auxiliary.tool.readfile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import com.opencsv.CSVReader;

import pres.auxiliary.tool.file.UnsupportedFileException;

/**
 * <p><b>文件名：</b>FileRead.java</p>
 * <p><b>用途：</b>用于读取文本内容，并将其转换为行列的形式对内容进行返回，
 * 支持表格文件（xls/xlsx/csv格式）和文本文件（doc/docx/txt格式）。默认在读取文件时，
 * 以列为单位读取并输出，若特殊需要，可调用表格转置方法，将内容的行列转置后再输出
 * </p>
 * <p><b>编码时间：</b>2020年3月29日 下午2:36:46</p>
 * <p><b>修改时间：</b>2020年3月29日 下午2:36:46</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 */
public class ListFileRead {
	/**
	 * 切分标记：tap
	 */
	public static final String SPLIT_TAB = "\\t";
	/**
	 * 切分标记：空格
	 */
	public static final String SPLIT_SPACE = " ";
	/**
	 * 切分标记：中文逗号
	 */
	public static final String SPLIT_COMMA_CH = "，";
	/**
	 * 切分标记：英文逗号
	 */
	public static final String SPLIT_COMMA_EN = ",";
	/**
	 * 切分标记：中文分号
	 */
	public static final String SPLIT_SEMICOLON_CH = "；";
	/**
	 * 切分标记：英文分号
	 */
	public static final String SPLIT_SEMICOLON_EN = ";";
	/**
	 * 切分标记：中文顿号
	 */
	public static final String SPLIT_STOP_CH = "、";
	/**
	 * 切分标记：斜杠
	 */
	public static final String SPLIT_SLASH = "/";
	/**
	 * 切分标记：反斜杠
	 */
	public static final String SPLIT_BACKSLASH = "\\\\";
	
	/**
	 * 存储被切分的词语<br>
	 * 外层ArrayList：存在多列的情况，存储每一列<br>
	 * 内层ArrayList：存储每一列的所有词语
	 */
	private ArrayList<ArrayList<String>> wordList = new ArrayList<ArrayList<String>>();
	
	/**
	 * 存储当前获取到的内容组成的表格最大行数
	 */
	private int maxRowNum = 0;
	/**
	 * 存储当前获取到的内容组成的表格最大列数
	 */
	private int maxColumnNum = 0;
	
	/**
	 * 根据传入文件格式，将文件的内容进行转换。若读取的是Excel文件，则通过该方法构造时
	 * 默认读取第一个sheet的内容，若需要读取其他的sheet，参见构造方法{@link #ListFileRead(File, String)}
	 * @param file 待读取的文件对象
	 * @throws IOException 文件状态或路径不正确时抛出的异常
	 */
	public ListFileRead(File file) throws IOException {
		this(file, "");
	}
	
	/**
	 * 根据传入文件格式，将文件的内容进行转换。该构造方法主要在用于读取Excel文件和文本文件上：
	 * <ol>
	 * 	<li>当读取的文件为Excel文件（xls/xlsx格式）时，其传入的regex表示需要读取的sheet名称</li>
	 * 	<li>当读取的文件为文本文件（doc/docx/txt格式）时，其传入的regex表示对每一行文本的切分规则，以得到一个列表</li>
	 * </ol>
	 * 除上述两种文件类型外，其他的文件调用该构造方法时，则等同于调用{@link #ListFileRead(File, String)}
	 * @param file 待读取的文件对象
	 * @param regex sheet名称或切分文本的规则
	 * @throws IOException 文件状态或路径不正确时抛出的异常
	 */
	public ListFileRead(File file, String regex) throws IOException {
		//若regex为null，则赋为空
		if (regex == null) {
			regex = "";
		}
		
		// 存储文件的名称
		String fileName = file.getName();
		//存储文件名的后缀，并根据后缀来判断采用哪一种读取方式
		switch ( fileName.substring(fileName.lastIndexOf(".") + 1)) {
		case "doc":
		case "docx":
			readWord(file, regex);
			break;
		
		case "xls":
		case "xlsx":
			readExcel(file, regex);
			break;
		
		case "txt":
			readTxt(file, regex);
			break;
		
		case "csv":
			readCsv(file);
			break;
		default:
			throw new UnsupportedFileException("无法解析“" + fileName + "”文件格式");
		}
	}
	
	/**
	 * 用于返回一列数据，若传入的下标大于最大列数，则返回最后一列数据；若传值小于0，则返回第一列数据
	 * @param columnIndex 列下标
	 * @return 相应一列的数据
	 */
	public List<String> getColumn(int columnIndex) {
		columnIndex = columnIndex >= maxColumnNum ? maxColumnNum : columnIndex;
		columnIndex = columnIndex < 0 ? 0 : columnIndex;
		
		return wordList.get(columnIndex);
	}
	
	/**
	 * 用于返回一列数据中某几行元素，若传入的下标大于最大列数，则返回最后一列数据；若传值小于0，则返回第一行数据。
	 * 同样，若传入的行标于列表最大行标时，返回最后一行数据；若行标小于0，则返回第一行数据。<br>
	 * 注意，返回的数据不包括endRowIndex行，但允许两个参数相同
	 * @param columnIndex 列下标
	 * @param startRowIndex 开始行下标
	 * @param endRowIndex 结束行下标
	 * @return 相应的数据
	 */
	public List<String> getColumn(int columnIndex, int startRowIndex, int endRowIndex) {
		columnIndex = columnIndex >= maxColumnNum ? maxColumnNum - 1 : columnIndex;
		columnIndex = columnIndex < 0 ? 0 : columnIndex;
		
		//若传值大于最大maxRowNum时，则直接赋予maxRowNum
		startRowIndex = (startRowIndex >= maxRowNum) ? maxRowNum - 1 : startRowIndex;
		startRowIndex = startRowIndex < 0 ? 0 : startRowIndex;
		endRowIndex = (endRowIndex >= maxRowNum) ? maxRowNum - 1 : endRowIndex;
		endRowIndex = endRowIndex < 0 ? 0 : endRowIndex;
		
		//若两个传值相同，则endRowIndex+1
		endRowIndex = (endRowIndex == startRowIndex) ? (endRowIndex + 1) : endRowIndex;
		
		//存储获取的内容
		ArrayList<String> texts = new ArrayList<>(); 
		
		int minRowIndex = startRowIndex < endRowIndex ? startRowIndex : endRowIndex;
		int maxRowIndex = startRowIndex < endRowIndex ? endRowIndex :  startRowIndex;
		//获取数据
		for (int rowIndex = minRowIndex; rowIndex < maxRowIndex; rowIndex++) {
			texts.add(wordList.get(columnIndex).get(rowIndex));
		}
		
		return texts;
	}
	
	/**
	 * 用于返回从文件中读取的整个列表
	 * @return 整个列表数据
	 */
	public ArrayList<ArrayList<String>> getTable() {
		return wordList;
	}
	
	/**
	 * 用于根据参数，返回从文件中读取的制定区域列表。当传入的参数大于最大行或列数时，则返回最后
	 * 一行列或行；若传入的参数小于0，则返回第一列或行
	 * @param startColumnIndex 开始列下标
	 * @param endColumnIndex 结束列下标
	 * @param startRowIndex 开始行下标
	 * @param endRowIndex 结束行下标
	 * @return 相应列表的数据
	 */
	public ArrayList<ArrayList<String>> getTable(int startColumnIndex, int endColumnIndex, int startRowIndex, int endRowIndex) {
		ArrayList<ArrayList<String>> newWordList = new ArrayList<>();
		
		//若传值大于最大maxColumnNum时，则直接赋予maxColumnNum，小于0，则直接赋予0
		startColumnIndex = startColumnIndex >= maxColumnNum ? maxColumnNum - 1 : startColumnIndex;
		startColumnIndex = startColumnIndex < 0 ? 0 : startColumnIndex;
		endColumnIndex = endColumnIndex >= maxColumnNum ? maxColumnNum - 1 : endColumnIndex;
		endColumnIndex = endColumnIndex < 0 ? 0 : endColumnIndex;
		
		//若传值大于最大maxRowNum时，则直接赋予maxRowNum，小于0，则直接赋予0
		startRowIndex = (startRowIndex >= maxRowNum) ? maxRowNum - 1 : startRowIndex;
		startRowIndex = startRowIndex < 0 ? 0 : startRowIndex;
		endRowIndex = (endRowIndex >= maxRowNum) ? maxRowNum - 1 : endRowIndex;
		endRowIndex = endRowIndex < 0 ? 0 : endRowIndex;
		
		//若两个传值相同，则endXXXIndex+1
		endColumnIndex = (endColumnIndex == startColumnIndex) ? (endColumnIndex + 1) : endColumnIndex;
		endRowIndex = (endRowIndex == startRowIndex) ? (endRowIndex + 1) : endRowIndex;
		
		//取值
		int minColumnIndex = startColumnIndex < endColumnIndex ? startColumnIndex : endColumnIndex;
		int maxColumnIndex = startColumnIndex < endColumnIndex ? endColumnIndex :  startColumnIndex;
		int minRowIndex = startRowIndex < endRowIndex ? startRowIndex : endRowIndex;
		int maxRowIndex = startRowIndex < endRowIndex ? endRowIndex :  startRowIndex;
		//获取数据
		for (int columnIndex = minColumnIndex; columnIndex < maxColumnIndex; columnIndex++) {
			ArrayList<String> columnTextList = new ArrayList<>();
			for (int rowIndex = minRowIndex; rowIndex < maxRowIndex; rowIndex++) {
				columnTextList.add(wordList.get(columnIndex).get(rowIndex));
			}
			newWordList.add(columnTextList);
		}
		
		return newWordList;
	}
	
	/**
	 * 用于将存储的行列内容转置（行作为列，列作为行），为保证转置后能再次转换回原始行列，若列表本身
	 * 数量不对应时，则转置中将无数据的位置添加为空串
	 */
	public void tableTransposition() {
		//若行列长度为0，则不进行存储
		if (maxColumnNum ==0 || maxRowNum == 0) {
			return;
		}
		
		//存储转置后的文本
		ArrayList<ArrayList<String>> newWordList = new ArrayList<ArrayList<String>>();
		
		//读取wordList的一列，将该列内容作为newWordList的一行
		for (int row = 0; row < maxRowNum; row++) {
			ArrayList<String> columnText = new ArrayList<String>();
			//根据行读取每一个元素
			for (int column = 0; column < maxColumnNum; column++) {
				try {
					columnText.add(wordList.get(column).get(row));
				} catch (IndexOutOfBoundsException e) {
					//若该列的该行并无元素，则存储空串
					columnText.add("");
				}
			}
			//存储该列内容
			newWordList.add(columnText);
		}
		
		//将wordList替换成newWordList
		wordList = newWordList;
		
		//交换最大行列
		int temp = maxColumnNum;
		maxColumnNum = maxRowNum;
		maxRowNum = temp;
	}
	
	/**
	 * 该方法用于返回最大行数
	 * @return 最大行数
	 */
	public int getMaxRowNumber() {
		return maxRowNum;
	}
	
	/**
	 * 该方法用于返回最大列数
	 * @return 最大列数
	 */
	public int getMaxColumnNumber() {
		return maxColumnNum;
	}

	/**
	 * 该方法用于读取并处理csv格式文件
	 * 
	 * @param file 文件
	 * @throws IOException 文件格式或路径不正确时抛出
	 */
	private void readCsv(File file) throws IOException {
		// 定义CSV文件对象
		CSVReader csv = new CSVReader(new FileReader(file));
		//存储获取到的内容中一行最大的长度
		AtomicInteger maxSizeNum = new AtomicInteger(-1);
		
		//读取文件内容，由于该方法的读取是按照行进行读取，故需要对读取的行列进行一个转换
		csv.forEach(texts -> {
			//获取整行内容
			List<String> text = Arrays.asList(texts);
			//存储该行内容
			wordList.add(new ArrayList<>(text));
			
			//判断当前内容的元素个数，若大于之前存储的内容，则将该行的元素个数最为最大值存储
			maxSizeNum.set(maxSizeNum.get() > text.size() ? maxSizeNum.get() : text.size());
		});
		
		//更新最大行列数
		maxRowNum = maxSizeNum.get();
		maxColumnNum = wordList.size();
		
		//由于文本以行作为单位，不符合当前的返回规则，故将其进行转置处理
		tableTransposition();
		
		csv.close();
	}

	/**
	 * 该方法用于读取并处理txt格式的文件，可通过切分规则对文本每一行的内容进行切分
	 * @param file 文件
	 * @param regex 切分规则
	 * @throws IOException 文件格式或路径不正确时抛出
	 */
	private void readTxt(File file, String regex) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		// 定义临时读取文件时得到的字符串
		String text = "";
		
		// 循环，按行读取文本
		while ((text = br.readLine()) != null) {
			//存储读取的文本
			ArrayList<String> texts = new ArrayList<>();
			
			try {
				//若传入的regex为空，则不进行切分
				if (!regex.isEmpty()) {
					// 对获取的文本按照传入的规则进行切分
					String[] words = text.split(regex);
					//存储切分后的词语
					texts.addAll(Arrays.asList(words));
				} else {
					texts.add(text);
				}
			} catch (Exception e) {
				//若抛出异常，则直接存储一整行
				texts.add(text);
			}
			//查找每一个texts的长度，若该长度大于maxRowNum存储的长度，则替换maxRowNum
			maxRowNum = (maxRowNum > texts.size()) ? maxRowNum : texts.size();
			
			//将存储的文本放入wordList中
			wordList.add(texts);
		}
		
		//记录最大行列数
		maxColumnNum = wordList.size();
		
		//为满足输出要求，则对文本进行转置
		tableTransposition();

		br.close();
	}

	/**
	 * 该方法用于读取并处理excel文件，根据传入的sheet名称来读取不同的sheet
	 * 
	 * @param file 待读取的文件
	 * @param sheetName 需要读取的sheet名称
	 * @throws IOException 文件格式或路径不正确时抛出
	 */
	private void readExcel(File file, String sheetName) throws IOException {
		String xlsx = "xlsx";
		
		// 读取文件流
		FileInputStream fip = new FileInputStream(file);

		// 用于读取excel
		Workbook excel = null;
		
		// 根据文件名的后缀，对其判断文件的格式，并按照相应的格式构造对象
		if (file.getName().indexOf(xlsx) > -1) {
			// 通过XSSFWorkbook对表格文件进行操作
			excel = new XSSFWorkbook(fip);
		} else {
			// 通过XSSFWorkbook对表格文件进行操作
			excel = new HSSFWorkbook(fip);
		}

		// 关闭流
		fip.close();

		// 读取excel中的内容，若未存储读取的sheet名称，则读取第一个sheet
		Sheet sheet = sheetName.isEmpty() ? excel.getSheetAt(0) : excel.getSheet(sheetName);
		//判断获取的sheet是否为null，为Null则同样获取第一个sheet
		sheet = (sheet == null) ? excel.getSheetAt(0) : sheet;
		//遍历所有单元格，将单元格内容存储至wordList中
		maxColumnNum = sheet.getLastRowNum() + 1;
		for (int rowNum = 0; rowNum < maxColumnNum; rowNum++) {
			//获取指向的行
			Row row = sheet.getRow(rowNum);
			ArrayList<String> texts = new ArrayList<>();
			for (int cellNum = 0; cellNum < row.getLastCellNum(); cellNum++) {
				//存储单元格的内容，若单元格为null，则存储空串
				try {
					texts.add(row.getCell(cellNum).toString());
				}catch (NullPointerException e) {
					texts.add("");
				}
				
				//对比当前行单元格的最大个数，若大于当前存储的，则存储为当前的最大值
				maxRowNum = (maxRowNum > row.getLastCellNum()) ? maxRowNum : row.getLastCellNum();
			}
			wordList.add(texts);
		}
		
		//转置，使列表返回符合规则
		tableTransposition();
		
		excel.close();
	}

	/**
	 * 该方法用于读取并处理word文件，可通过切分规则对文本每一行的内容进行切分
	 * 
	 * @param file 文件
	 * @param regex 切分规则
	 * @throws IOException 文件格式或路径不正确时抛出
	 */
	private void readWord(File file, String regex) throws IOException {
		String docx = "docx";
		
		// 读取文件流
		FileInputStream fip = new FileInputStream(file);
		// 由于读取doc与docx的类不继承自同一个类，所以无法直接写在一起，只能通过判断语句隔开
		if (file.getName().indexOf(docx) > -1) {
			XWPFDocument wordFile = new XWPFDocument(fip);
			// 07版本相较麻烦，需要分段读取
			for (XWPFParagraph pa : wordFile.getParagraphs()) {
				//存储文本内容
				ArrayList<String> texts = new ArrayList<>();
				
				try {
					//若传入的regex为空，则不进行切分
					if (!regex.isEmpty()) {
						// 对获取的文本按照传入的规则进行切分
						String[] words = pa.getText().split(regex);
						//存储切分后的词语
						texts.addAll(Arrays.asList(words));
					} else {
						texts.add(pa.getText());
					}
				} catch (Exception e) {
					//若抛出异常，则直接存储一整行
					texts.add(pa.getText());
				}
				
				//查找每一个texts的长度，若该长度大于maxRowNum存储的长度，则替换maxRowNum
				maxRowNum = (maxRowNum > texts.size()) ? maxRowNum : texts.size();
				wordList.add(texts);
			}
			//关闭文件
			wordFile.close();
		} else {
			HWPFDocument wordFile = new HWPFDocument(fip);
			Range wordFileRange = wordFile.getRange();
			
			//根据段落读取相应的文本
			for (int paragraphIndex = 0; paragraphIndex < wordFileRange.numParagraphs(); paragraphIndex++) {
				//获取段落
				Paragraph pa = wordFileRange.getParagraph(paragraphIndex);
				
				//存储文本内容
				ArrayList<String> texts = new ArrayList<>();
				
				try {
					//若传入的regex为空，则不进行切分
					if (!regex.isEmpty()) {
						// 对获取的文本按照传入的规则进行切分
						String[] words = pa.text().split(regex);
						//存储切分后的词语
						texts.addAll(Arrays.asList(words));
					} else {
						texts.add(pa.text());
					}
				} catch (Exception e) {
					//若抛出异常，则直接存储一整行
					texts.add(pa.text());
				}
				
				//查找每一个texts的长度，若该长度大于maxRowNum存储的长度，则替换maxRowNum
				maxRowNum = (maxRowNum > texts.size()) ? maxRowNum : texts.size();
				wordList.add(texts);
			}
			
			// 存储替换后的文本
			wordFile.close();
		}
		
		//记录最大列数
		maxColumnNum = wordList.size();
		
		//为满足输出要求，则对文本进行转置
		tableTransposition();

		fip.close();
	}
}
