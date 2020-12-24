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
import java.util.concurrent.atomic.AtomicInteger;

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
 * <p><b>文件名：</b>FileRead.java</p>
 * <p><b>用途：</b>用于读取文本内容，并将其转换为行列的形式对内容进行返回，
 * 支持表格文件（xls/xlsx/csv格式）和文本文件（doc/docx/txt格式）。默认在读取文件时，
 * 以列为单位读取并输出，若特殊需要，可调用表格转置方法，将内容的行列转置后再输出
 * </p>
 * <p><b>编码时间：</b>2020年3月29日 下午2:36:46</p>
 * <p><b>修改时间：</b>2020年3月29日 下午2:36:46</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public class ListFileRead {
	/**
	 * 存储被切分的词语<br>
	 * 外层ArrayList：存在多列的情况，存储每一列<br>
	 * 内层ArrayList：存储每一列的所有词语
	 */
//	private ArrayList<ArrayList<String>> wordList = new ArrayList<ArrayList<String>>();
	private TableData<String> wordTable = new TableData<>();
	
	/**
	 * 标记首行是否为标题行
	 */
	private boolean isFirstTitle = true;
	
	/**
	 * 根据传入文件格式，将文件的内容进行转换。若读取的是Excel文件，则通过该方法构造时
	 * 默认读取第一个sheet的内容，若需要读取其他的sheet，参见构造方法{@link #ListFileRead(File, String)}
	 * @param file 待读取的文件对象
	 * @throws IOException 文件状态或路径不正确时抛出的异常
	 */
	public ListFileRead(File file, boolean isFirstTitle) throws IOException {
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
	 * @param pattern sheet名称或切分文本的规则
	 * @throws IOException 文件状态或路径不正确时抛出的异常
	 */
	public ListFileRead(File file, String pattern) throws IOException {
		//若pattern为null，则赋为空
		if (pattern == null) {
			pattern = "";
		}
		
		// 存储文件的名称
		String fileName = file.getName();
		//存储文件名的后缀，并根据后缀来判断采用哪一种读取方式
		switch ( fileName.substring(fileName.lastIndexOf(".") + 1)) {
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
	 * 用于返回一列数据，若传入的下标大于最大列数，则返回最后一列数据；若传值小于0，则返回第一列数据
	 * @param columnIndex 列下标
	 * @return 相应一列的数据
	 */
	public List<Optional<String>> getColumn(int columnIndex) {
		return wordTable.getColumnList(wordTable.getFieldName(columnIndex));
	}
	
	/**
	 * 用于返回一列数据中某几行元素。
	 * <p>
	 * 其行下标从1开始计算，传入0与1时，均表示获取第一行。允许传入负数，表示从后向前计算行号。
	 * 其获取的行元素包括传入的结束行当起始行下标与结束行下标一致时，则表示获取当前的行元素；
	 * 列下标从0开始计算，不允许传入负数，例如：
	 * <ul>
	 * 	<li>调用{@code getColumn(0, 0, 3)}表示获取第1列的第1行到第3行元素</li>
	 * 	<li>调用{@code getColumn(1, 1, 1)}表示获取第2列的第1行元素</li>
	 * 	<li>调用{@code getColumn(2, 1, -2)}表示获取第3列的第1行到倒数第2行元素</li>
	 * </ul>
	 * </p>
	 * @param columnIndex 列下标
	 * @param startRowIndex 开始行下标
	 * @param endRowIndex 结束行下标
	 * @return 相应的数据
	 */
	public List<Optional<String>> getColumn(int columnIndex, int startRowIndex, int endRowIndex) {
		String fieldName = wordTable.getFieldName(columnIndex);
		return wordTable.getData(startRowIndex, endRowIndex, fieldName).get(fieldName);
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
		
		//读取文件内容，由于该方法的读取是按照行进行读取，故需要对读取的行列进行一个转换
		csv.forEach(texts -> {
			//获取整行内容
			List<String> text = Arrays.asList(texts);
			//存储该行内容
			wordList.add(new ArrayList<>(text));
		});
		
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
					texts.add(getCellContent(row.getCell(cellNum)));
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
	
	/**
	 * 根据单元格的类型，对其内容进行输出
	 * @param cell 单元格类对象
	 * @return 单元格中的文本
	 */
	private String getCellContent(Cell cell) {
		//判断单元格中的内容的格式
		if (CellType.NUMERIC == cell.getCellTypeEnum()) {
			//数值类型
			//判断单元格内的数据是否为日期
			if (DateUtil.isCellDateFormatted(cell)) {
				//将单元格内容转换为Date后，在time中进行存储
				Time time = new Time(cell.getDateCellValue());
				//根据存储的时间格式，对时间进行转换，输出格式化后的时间
				return time.getFormatTime();
			} else {
				//若非日期格式，则强转为字符串格式，之后输出
				cell.setCellType(CellType.STRING);
				return cell.getStringCellValue();
			}
		} else if (CellType.FORMULA == cell.getCellTypeEnum()) {
			//公式类型
			//公式得到的值可能是一个字符串，也可能是一个数字，此时则需要进行区分（转换错误会抛出异常）
			try {
				return cell.getRichStringCellValue().getString();
			} catch (IllegalStateException e) {
				return String.valueOf(cell.getNumericCellValue());
			}
		} else {
			//其他类型，按照字符串进行读取
			return cell.getStringCellValue();
		}
	}
}
