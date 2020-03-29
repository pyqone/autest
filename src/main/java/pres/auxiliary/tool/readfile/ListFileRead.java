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
import org.apache.poi.ss.usermodel.Cell;
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
	 * 存储文件的后缀名
	 */
	private String suffix = "";
	
	/**
	 * 存储被切分的词语<br>
	 * 外层ArrayList：存在多列的情况，存储每一列<br>
	 * 内层ArrayList：存储每一列的所有词语
	 */
	private ArrayList<List<String>> wordList = new ArrayList<List<String>>();
	
	/**
	 * 存储对文本分词处理的分隔符
	 */
	private String splitWord = "";
	
	/**
	 * 存储当前获取到的内容组成的表格最大行数
	 */
	private int maxRowNum = -1;
	/**
	 * 存储当前获取到的内容组成的表格最大列数
	 */
	private int maxColumnNum = -1;
	
	/**
	 * 根据传入文件格式，将文件的内容进行转换
	 * @param file
	 * @throws IOException
	 */
	public ListFileRead(File file) throws IOException {
		// 存储文件的名称
		String fileName = file.getName();
		//存储文件名的后缀，并根据后缀来判断采用哪一种读取方式
		switch ((suffix = fileName.substring(fileName.lastIndexOf(".") + 1))) {
		case "doc":
		case "docx":
			readWord(file);
			break;

		case "xls":
		case "xlsx":
			readExcel(file);
			break;

		case "txt":
			readTxt(file);
			break;

		case "csv":
			readCsv(file);
			break;
		default:
			throw new UnsupportedFileException("无法解析“" + fileName + "”文件格式");
		}
	}
	
	/**
	 * 用于返回一列数据
	 * @param columnIndex 列下标
	 * @return 相应一列的数据
	 */
	public List<String> getColumn(int columnIndex) {
		if (columnIndex > maxColumnNum) {
			return null;
		}
		
		return wordList.get(columnIndex);
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
		ArrayList<List<String>> newWordList = new ArrayList<List<String>>();
		
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
	 * 该方法用于读取并处理csv文件
	 * 
	 * @param f 待读取的文件
	 * @return 读取的文本
	 * @throws IOException
	 */
	private void readCsv(File f) throws IOException {
		// 定义CSV文件对象
		CSVReader csv = new CSVReader(new FileReader(f));
		//存储获取到的内容中一行最大的长度
		AtomicInteger maxSizeNum = new AtomicInteger(-1);
		
		//读取文件内容，由于该方法的读取是按照行进行读取，故需要对读取的行列进行一个转换
		csv.forEach(texts -> {
			//获取整行内容
			List<String> text = Arrays.asList(texts);
			//存储该行内容
			wordList.add(text);
			
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
	 * 该方法用于读取并处理txt文件
	 * 
	 * @param f 待读取的文件
	 * @return 读取的文本
	 * @throws IOException
	 */
	private void readTxt(File f) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(f));
		/*
		// 用于存储读取到的
		String text = "";
		// 定义临时读取文件时得到的字符串
		String temp = "";
		// 循环，读取文件中所有的文本
		// 循环，按行读取文本
		while ((temp = br.readLine()) != null) {
			// 拼接字符串
			text += (temp + LINE);
		}

		br.close();
		return text;
		*/
	}

	/**
	 * 该方法用于读取并处理excel文件，根据后缀名选择不同的读取方式
	 * 
	 * @param f 待读取的文件
	 * @return 读取的文本
	 * @throws IOException
	 */
	private void readExcel(File f) throws IOException {
		String xlsx = "xlsx";
		
		// 读取文件流
		FileInputStream fip = new FileInputStream(f);

		// 用于读取excel
		Workbook excel = null;
		// 根据文件名的后缀，对其判断文件的格式，并按照相应的格式构造对象
		if (f.getName().indexOf(xlsx) > -1) {
			// 通过XSSFWorkbook对表格文件进行操作
			excel = new XSSFWorkbook(fip);
		} else {
			// 通过XSSFWorkbook对表格文件进行操作
			excel = new HSSFWorkbook(fip);
		}

		// 关闭流
		fip.close();

		// 用于存储文本
		String text = "";

		// 读取excel中的内容
		// 读取方式为，将列与行的内容一同写在一列文本中，不考虑分列
		Sheet sheet = excel.getSheetAt(0);
		for (int i = 0; i < sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);
			for (int j = 0; j < row.getLastCellNum(); j++) {
				try {
					Cell cell = row.getCell(j);
//					text += (cell.toString() + LINE);
				} catch(NullPointerException e) {
					//当读取到的列表为空时，则会抛出空指针的异常，此时不对该行进行存储
				}
			}
		}

		excel.close();
	}

	/**
	 * 该方法用于读取并处理word文件，根据后缀名选择不同的读取方式
	 * 
	 * @param f 待读取的文件
	 * @return 读取的文本
	 * @throws IOException
	 */
	private void readWord(File f) throws IOException {
		String docx = "docx";
		
		// 用于存储读取到的文本
		String text = "";
		// 读取文件流
		FileInputStream fip = new FileInputStream(f);
		// 由于读取doc与docx的类不继承自同一个类，所以无法直接写在一起，只能通过判断语句隔开
		if (f.getName().indexOf(docx) > -1) {
			XWPFDocument word = new XWPFDocument(fip);
			// 07版本相较麻烦，需要分段读取
			for (XWPFParagraph pa : word.getParagraphs()) {
//				text += (pa.getText() + LINE);
			}
			word.close();
		} else {
			HWPFDocument word = new HWPFDocument(fip);
			// 由于03版的word可通过getText()方法直接返回文本内容，故可直接写
			StringBuilder temp = word.getText();
			while (true) {
				int i = -1;
				// 判断是否存在/r，若存在/r则替换成/n
				if ((i = temp.indexOf("\r")) > -1) {
					temp.replace(i, i + 1, "\n");
					continue;
				}
				// 若未搜索到/r则结束循环
				break;
			}

			// 存储替换后的文本
			text = temp.toString();
			word.close();
		}

		fip.close();
	}
}
