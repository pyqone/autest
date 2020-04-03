package pres.auxiliary.tool.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

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

/**
 * <p>
 * <b>文件名：</b>DisposeText.java
 * </p>
 * <p>
 * <b>用途：</b>用于对文件中的文本进行处理，以简化日常工作中对文本的内容的测试
 * </p>
 * <p>
 * <b>编码时间：</b>2019年7月4日 07:08
 * </p>
 * <p>
 * <b>修改时间：</b>2019年7月11日 09:12
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public class DisposeText {
	/**
	 * 段落或者表格列标记
	 */
	private static final String LINE = "\n";

	/**
	 * 该方法用于对两文本中文本的单词进行比较
	 * 
	 * @param testFile   待测试文件
	 * @param targetFile 目标文件
	 * @return 返回待测文件中不在目标文件中的单词
	 * @throws IOException
	 */
	public static ArrayList<String> compareFileWord(File testFile, File targetFile) throws IOException {
		// 存储不在目标文件中的词语
		ArrayList<String> words = new ArrayList<>();

		// 定义存储切分词语的容器，并存储其不重复的单词
		LinkedHashMap<String, Integer> testTextMap = saveWord(split(readFile(testFile), LINE));
		LinkedHashMap<String, Integer> targeTextMap = saveWord(split(readFile(targetFile), LINE));

		// 对比结果
		// 定义存储不在目标文件的词语
		for (String targeWord : targeTextMap.keySet()) {
			// 用于判断待测单词是否存在于目标单词表中，存在则为true
			boolean b = true;
			for (String testWord : testTextMap.keySet()) {
				// 判断待测单词是否与遍历到的目标单词一致，一致则将b设置为true，并结束遍历
				if (testWord.trim().equals(targeWord.trim())) {
					b = false;
					break;
				}

				b = true;
			}
			// 判断b的值，为false时，则存储至words中
			if (b) {
				words.add(targeWord);
			}
		}

		return words;
	}

	/**
	 * 该方法用于对文本进行去重，保留不重复的字符
	 * 
	 * @param textFile 存储文本的文件
	 * @return 去重后的文本
	 * @throws IOException
	 */
	public static String textDelDuplication(File textFile) throws IOException {
		// 存储文件中的文本
		StringBuilder text = new StringBuilder(readFile(textFile));
		// 用于存储去重后的字符
		String newText = "";
		// 存储当前字符在本文本中出现的次数，使用map存储
		LinkedHashMap<String, Integer> saveChar = new LinkedHashMap<String, Integer>(16);

		// 循环，读取字符串中的每一个字符
		for (int i = 0; i < text.length(); i++) {
			// 获取当前读取的字符
			String temp = text.substring(i, i + 1);
			// 判断该字符是否存在于map中，不存在则将该字符存储至map，并拼接至newText中；若存在，则将map中的元素+1
			if (!saveChar.containsKey(temp)) {
				saveChar.put(temp, 1);
				newText += temp;
			} else {
				saveChar.put(temp, saveChar.get(temp) + 1);
			}
		}

		return newText;
	}

	/**
	 * 该方法用于对两文件中的文本，按照指定的方式进行切分后，输出待测文本与目标文本不同的地方
	 * 
	 * @param testFile  待测文件
	 * @param targeFile 目标文件
	 * @return 待测文件与目标文件不相同的地方
	 * @throws IOException
	 */
	public static List<String[]> compareFileText(File testFile, File targeFile, String... regexs) throws IOException {
		// 读取并按照规则切割文本，并存储被切割的文本
		// 由于返回为Collection，故需要强转为ArrayList
		ArrayList<String> testTexts = (ArrayList<String>) split(readFile(testFile), regexs);
		ArrayList<String> targeTexts = (ArrayList<String>) split(readFile(targeFile), regexs);

		// 用于存储对比后的文本，容器元素的字符串数组存储待测文本和目标文本
		ArrayList<String[]> result = new ArrayList<>();

		// 注意，此处用到一个对比，由于在切分后后，可能会由于目标或者待测文件多出一部分信息，多两一个文本中按规则切分不出那么多的信息后，则读取长的可能会出现空指针，故需要按照短的那部分进行读取
		// 存储最短的文本，true表示targeTexts文本最短，false表示testTexts最短
		boolean compareText = testTexts.size() > targeTexts.size();
		// 存储切分后容器元素最少的容器长度，作为循环终点
		int minLength = compareText ? targeTexts.size() : testTexts.size();
		// 循环，对两个容器中的元素进行逐个对比
		for (int i = 0; i < minLength; i++) {
			// 判断两个字符串是否相同，相同则存储至容器中
			// 由于切分的文档可能有空格的干扰，故进行去空格处理后再对比
			if (!testTexts.get(i).trim().equals(targeTexts.get(i).trim())) {
				result.add(new String[] { testTexts.get(i), targeTexts.get(i) });
			}
		}

		// 存储剩余
		// 若两容器里的元素量相同，则条件语句里的循环将不执行，故亦可使用该方法
		// 循环，按照最短的元素继续存储至result容器中
		for (int i = minLength; i < (compareText ? testTexts.size() : targeTexts.size()); i++) {
			// 判断容器元素最少的容器，存储多元素容器中的元素至result容器，并将另外一个设为空串
			if (compareText) {
				result.add(new String[] { testTexts.get(i), "" });
			} else {
				result.add(new String[] { "", targeTexts.get(i) });
			}
		}

		// 返回结果
		return result;
	}
	
	/**
	 * 该方法用于对文本中单词进行去重，输出不重复单词
	 * @param testFile 待测文件
	 * @return 去重后的单词数组
	 * @throws IOException
	 */
	public static String[] wordDelDuplication(File testFile) throws IOException {
		//读取、切分并存储文本不重复的单词
		LinkedHashMap<String, Integer> result = saveWord(split(readFile(testFile), LINE));
		
		//将不重复的单词转换成字符串数组，并返回该数组
		return result.keySet().toArray(new String[] {});
	}

	/**
	 * 该方法用于读取文件，并自动判断文件的格式，以选择相应的读取方式<br>
	 * <ul>
	 * <li>doc、docx、txt格式以文本形式原文读取，段落使用段落标记进行拼接</li>
	 * <li>xls、xlsx、csv格式以表格形式读取数据后，按照标记拼接至字符串中读取</li>
	 * </ul>
	 * 
	 * @param f 封装的文件对象
	 * @return 文本中的内容
	 * @throws IOException
	 */
	private static String readFile(File f) throws IOException {
		// 用于存储读取文件中的内容
		String text = "";
		// 用于存储文件的后缀名，以判断文件的格式
		String[] fileName = f.getName().split("\\.");
		String suffix = fileName[fileName.length - 1];

		switch (suffix) {
		case "doc":
		case "docx":
			text = readWord(f);
			break;

		case "xls":
		case "xlsx":
			text = readExcel(f);
			break;

		case "txt":
			text = readTxt(f);
			break;

		case "csv":
			text = readCsv(f);
			break;
		default:
			throw new UnsupportedFileException("无法解析“" + suffix + "”文件格式");
		}

		return text;
	}

	/**
	 * 该方法用于读取并处理csv文件
	 * 
	 * @param f 待读取的文件
	 * @return 读取的文本
	 * @throws IOException
	 */
	private static String readCsv(File f) throws IOException {
		// 定义CSV文件对象
		CSVReader csv = new CSVReader(new FileReader(f));

		// 存储转换的字符串
		String text = "";
		// 用于临时转换的字符串数组
		String[] texts;
		// 循环，读取所有的信息
		while ((texts = csv.readNext()) != null) {
			// 循环，读取行内的信息
			for (String temp : texts) {
				text += (temp + LINE);
			}
		}

		csv.close();

		return text;
	}

	/**
	 * 该方法用于读取并处理txt文件
	 * 
	 * @param f 待读取的文件
	 * @return 读取的文本
	 * @throws IOException
	 */
	private static String readTxt(File f) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(f));

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
	}

	/**
	 * 该方法用于读取并处理excel文件，根据后缀名选择不同的读取方式
	 * 
	 * @param f 待读取的文件
	 * @return 读取的文本
	 * @throws IOException
	 */
	private static String readExcel(File f) throws IOException {
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
			//若获取不到行，则说明该行无数据，直接继续循环
			if (row == null) {
				continue;
			}
			for (int j = 0; j < row.getLastCellNum(); j++) {
				try {
					Cell cell = row.getCell(j);
					text += (cell.toString() + LINE);
				} catch(NullPointerException e) {
					//当读取到的列表为空时，则会抛出空指针的异常，此时不对该行进行存储
				}
			}
		}

		excel.close();
		return text;
	}

	/**
	 * 该方法用于读取并处理word文件，根据后缀名选择不同的读取方式
	 * 
	 * @param f 待读取的文件
	 * @return 读取的文本
	 * @throws IOException
	 */
	private static String readWord(File f) throws IOException {
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
				text += (pa.getText() + LINE);
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
		return text;
	}

	/**
	 * 该方法用于按照规则对字符串进行切分，规则可定义多个
	 * 
	 * @param regex 切分字符串的规则
	 * @return 切分的结果
	 */
	private static Collection<String> split(String text, String... regexs) {
		// 用于存储切分后的信息
		Collection<String> c = new ArrayList<String>();
		c.add(text);

		// 循环，逐个读取切分的规则信息
		for (String regex : regexs) {
			Collection<String> temp = new ArrayList<String>();
			c.forEach(element -> {
				temp.addAll(Arrays.asList(element.split(regex)));
			});
			c = temp;
		}
		return c;
	}
	
	/**
	 * 该方法用于存储关键词出现的次数
	 * @param words 需要写入word中的数据
	 * @return 返回map
	 */
	private static LinkedHashMap<String, Integer> saveWord(Collection<String> words) {
		LinkedHashMap<String, Integer> result = new LinkedHashMap<>(16);
		
		//存储不重复的单词，并存储其在文中出现的次数
		words.forEach(element -> {
			// 判断map是否已经存在该词语，若存在，则将key对应的value加上1
			if (result.containsKey(element)) {
				result.put(element, result.get(element) + 1);
			} else {
				result.put(element, 1);
			}
		});
		
		return result;
	}
}
