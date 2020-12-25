package pres.auxiliary.work.selenium.datadriven;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import com.opencsv.CSVReader;

import pres.auxiliary.tool.data.TableData;
import pres.auxiliary.tool.date.Time;
import pres.auxiliary.tool.file.UnsupportedFileException;

/**
 * <p>
 * <b>文件名：</b>FileRead.java
 * </p>
 * <p>
 * <b>用途：</b>用于读取文本内容，并将其转换为行列的形式对内容进行返回，
 * 支持表格文件（xls/xlsx/csv格式）和文本文件（doc/docx/txt格式）。默认在读取文件时，
 * 以列为单位读取并输出，若特殊需要，可调用表格转置方法，将内容的行列转置后再输出
 * </p>
 * <p>
 * <b>编码时间：</b>2020年3月29日 下午2:36:46
 * </p>
 * <p>
 * <b>修改时间：</b>2020年3月29日 下午2:36:46
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public class TableFileReadUtil {
	/**
	 * 默认的列名称
	 */
	private static final String DEFAULT_COLUMN_NAME = "列表";

	/**
	 * 根据传入文件格式，将文件的内容进行转换。该构造方法主要在用于读取Excel文件和文本文件上：
	 * <ol>
	 * <li>当读取的文件为Excel文件（xls/xlsx格式）时，其传入的regex表示需要读取的sheet名称</li>
	 * <li>当读取的文件为文本文件（doc/docx/txt格式）时，其传入的regex表示对每一行文本的切分规则，以得到一个列表</li>
	 * </ol>
	 * 除上述两种文件类型外，其他的文件调用该构造方法时，则等同于调用{@link #ListFileRead(File, String)}
	 * 
	 * @param file    待读取的文件对象
	 * @param pattern sheet名称或切分文本的规则
	 * @throws IOException 文件状态或路径不正确时抛出的异常
	 */
	public static TableData<String> readFile(File file, String pattern) {
		// 若pattern为null，则赋为空
		if (pattern == null) {
			pattern = "";
		}

		// 存储文件的名称
		String fileName = file.getName();
		// 存储文件名的后缀，并根据后缀来判断采用哪一种读取方式
		switch (fileName.substring(fileName.lastIndexOf(".") + 1)) {
		case "doc":
		case "docx":
			readWord(file, pattern);
			break;

		case "xls":
		case "xlsx":
			readExcel(file, pattern);
			break;

		case "txt":
			readTxt(file, pattern);
			break;

		case "csv":
			readCsv(file);
			break;
		default:
			throw new UnsupportedFileException("无法解析“" + fileName + "”文件格式");
		}
	}

	/**
	 * 用于读取并处理csv格式文件，并将内容转换为字符串列表进行返回
	 * 
	 * @param file         文件
	 * @param isFirstTitle 首行是否为标题行
	 * @return 数据表类对象
	 * @throws UnsupportedFileException 文件未传入或读取异常时抛出的异常
	 */
	public static TableData<String> readCsv(File file, boolean isFirstTitle) {
		TableData<String> wordTable = new TableData<>();

		// 定义CSV文件对象
		try (CSVReader csv = new CSVReader(
				new FileReader(Optional.ofNullable(file).orElseThrow(() -> new UnsupportedFileException("未传入文件对象"))))) {
			// 获取文件中所有的内容
			List<String[]> wordDataList = csv.readAll();

			// 根据首行是否为标题行，对wordTable类的标题进行初始化
			List<String> columnNameList = new ArrayList<>();
			if (isFirstTitle) {
				columnNameList.addAll(Arrays.asList(wordDataList.get(0)));
			} else {
				columnNameList.addAll(createDefaultColumnName(wordDataList.get(0).length));
			}
			wordTable.addTitle(columnNameList);

			// 根据首行是否为标题行初始化起始遍历的行下标
			IntStream.range(isFirstTitle ? 1 : 0, wordDataList.size())
					// 根据下标获取元素集合
					.mapToObj(index -> Arrays.asList(wordDataList.get(index)))
					// 按行的形式存储数据
					.forEach(wordTable::addRow);
		} catch (IOException e) {
			throw new UnsupportedFileException("文件异常，无法进行读取：" + file.getAbsolutePath(), e);
		}

		return wordTable;
	}

	/**
	 * 该方法用于读取并处理txt格式的文件，可通过切分规则对文本每一行的内容进行切分
	 * 
	 * @param file  文件
	 * @param regex 切分规则
	 * @throws IOException 文件格式或路径不正确时抛出
	 */
	public static TableData<String> readTxt(File file, String regex, boolean isFirstTitle) {
		TableData<String> wordTable = new TableData<>();

		try (BufferedReader br = new BufferedReader(
				new FileReader(Optional.ofNullable(file).orElseThrow(() -> new UnsupportedFileException("未传入文件对象"))))) {
			//指向当前表中是否存在标题
			AtomicBoolean hasTitle = new AtomicBoolean(false);
			//按行遍历文件内容
			br.lines()
				//将文本按照内容进行切分，转换为字符串数组
				.map(text -> text.split(regex))
				//将数组转换为字符串集合
				.map(Arrays::asList)
				.forEach(dataList -> {
					//需要对第一行数据进行处理，若该行数据为标题行，则将其存储为标题，若为普通数据行，则添加基础标题
					if (!hasTitle.get()) {
						if (isFirstTitle) {
							wordTable.addTitle(dataList);
						} else {
							wordTable.addTitle(createDefaultColumnName(dataList.size()));
							wordTable.addRow(dataList);
						}
						//设置标题存在
						hasTitle.set(true);
					} else {
						wordTable.addRow(dataList);
					}
				});
		} catch (IOException e) {
			throw new UnsupportedFileException("文件异常，无法进行读取：" + file.getAbsolutePath(), e);
		}

		return wordTable;
	}

	/**
	 * 该方法用于读取并处理excel文件，根据传入的sheet名称来读取不同的sheet
	 * 
	 * @param file      待读取的文件
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
		// 判断获取的sheet是否为null，为Null则同样获取第一个sheet
		sheet = (sheet == null) ? excel.getSheetAt(0) : sheet;
		// 遍历所有单元格，将单元格内容存储至wordList中
		maxColumnNum = sheet.getLastRowNum() + 1;
		for (int rowNum = 0; rowNum < maxColumnNum; rowNum++) {
			// 获取指向的行
			Row row = sheet.getRow(rowNum);
			ArrayList<String> texts = new ArrayList<>();
			for (int cellNum = 0; cellNum < row.getLastCellNum(); cellNum++) {
				// 存储单元格的内容，若单元格为null，则存储空串
				try {
					texts.add(getCellContent(row.getCell(cellNum)));
				} catch (NullPointerException e) {
					texts.add("");
				}

				// 对比当前行单元格的最大个数，若大于当前存储的，则存储为当前的最大值
				maxRowNum = (maxRowNum > row.getLastCellNum()) ? maxRowNum : row.getLastCellNum();
			}
			wordList.add(texts);
		}

		// 转置，使列表返回符合规则
		tableTransposition();

		excel.close();
	}

	/**
	 * 该方法用于读取并处理word文件，可通过切分规则对文本每一行的内容进行切分
	 * 
	 * @param file  文件
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
				// 存储文本内容
				ArrayList<String> texts = new ArrayList<>();

				try {
					// 若传入的regex为空，则不进行切分
					if (!regex.isEmpty()) {
						// 对获取的文本按照传入的规则进行切分
						String[] words = pa.getText().split(regex);
						// 存储切分后的词语
						texts.addAll(Arrays.asList(words));
					} else {
						texts.add(pa.getText());
					}
				} catch (Exception e) {
					// 若抛出异常，则直接存储一整行
					texts.add(pa.getText());
				}

				// 查找每一个texts的长度，若该长度大于maxRowNum存储的长度，则替换maxRowNum
				maxRowNum = (maxRowNum > texts.size()) ? maxRowNum : texts.size();
				wordList.add(texts);
			}
			// 关闭文件
			wordFile.close();
		} else {
			HWPFDocument wordFile = new HWPFDocument(fip);
			Range wordFileRange = wordFile.getRange();

			// 根据段落读取相应的文本
			for (int paragraphIndex = 0; paragraphIndex < wordFileRange.numParagraphs(); paragraphIndex++) {
				// 获取段落
				Paragraph pa = wordFileRange.getParagraph(paragraphIndex);

				// 存储文本内容
				ArrayList<String> texts = new ArrayList<>();

				try {
					// 若传入的regex为空，则不进行切分
					if (!regex.isEmpty()) {
						// 对获取的文本按照传入的规则进行切分
						String[] words = pa.text().split(regex);
						// 存储切分后的词语
						texts.addAll(Arrays.asList(words));
					} else {
						texts.add(pa.text());
					}
				} catch (Exception e) {
					// 若抛出异常，则直接存储一整行
					texts.add(pa.text());
				}

				// 查找每一个texts的长度，若该长度大于maxRowNum存储的长度，则替换maxRowNum
				maxRowNum = (maxRowNum > texts.size()) ? maxRowNum : texts.size();
				wordList.add(texts);
			}

			// 存储替换后的文本
			wordFile.close();
		}

		// 记录最大列数
		maxColumnNum = wordList.size();

		// 为满足输出要求，则对文本进行转置
		tableTransposition();

		fip.close();
	}

	/**
	 * 根据单元格的类型，对其内容进行输出
	 * 
	 * @param cell 单元格类对象
	 * @return 单元格中的文本
	 */
	private String getCellContent(Cell cell) {
		// 判断单元格中的内容的格式
		if (CellType.NUMERIC == cell.getCellTypeEnum()) {
			// 数值类型
			// 判断单元格内的数据是否为日期
			if (DateUtil.isCellDateFormatted(cell)) {
				// 将单元格内容转换为Date后，在time中进行存储
				Time time = new Time(cell.getDateCellValue());
				// 根据存储的时间格式，对时间进行转换，输出格式化后的时间
				return time.getFormatTime();
			} else {
				// 若非日期格式，则强转为字符串格式，之后输出
				cell.setCellType(CellType.STRING);
				return cell.getStringCellValue();
			}
		} else if (CellType.FORMULA == cell.getCellTypeEnum()) {
			// 公式类型
			// 公式得到的值可能是一个字符串，也可能是一个数字，此时则需要进行区分（转换错误会抛出异常）
			try {
				return cell.getRichStringCellValue().getString();
			} catch (IllegalStateException e) {
				return String.valueOf(cell.getNumericCellValue());
			}
		} else {
			// 其他类型，按照字符串进行读取
			return cell.getStringCellValue();
		}
	}
	
	/**
	 * 用于生成默认的列名
	 * @param length 数据列个数
	 * @return 相应的默认列名
	 */
	private static List<String> createDefaultColumnName(int length) {
		return IntStream.range(1, length)
				// 转换下标数字为列名称
				.mapToObj(index -> DEFAULT_COLUMN_NAME + index).collect(Collectors.toList());
	}
}
