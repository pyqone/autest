package com.auxiliary.tool.file;

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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
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

import com.auxiliary.tool.data.IllegalDataException;
import com.auxiliary.tool.data.TableData;
import com.auxiliary.tool.date.Time;
import com.opencsv.CSVReader;

/**
 * <p>
 * <b>文件名：</b>TableFileReadUtil.java
 * </p>
 * <p>
 * <b>用途：</b> 将存储在文件中的词语，以字符串的形式读取，并将值存储至{@link TableData}类中，可调用类中的方法，对
 * 读取的词语进行返回。文件支持表格文件（xls/xlsx/csv格式）和文本文件（doc/docx/txt格式）。
 * </p>
 * <p>
 * <b>注意：</b>文件内，首行（首段）的内容为定制列表的标准，若其后的内容获取到的词语个数超出第一行获取到的
 * 词语个数，则将抛出{@link IllegalDataException}异常。
 * </p>
 * <p>
 * <b>编码时间：</b>2020年3月29日 下午2:36:46
 * </p>
 * <p>
 * <b>修改时间：</b>2020年12月25日下午6:40:07
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since POI 3.17
 *
 */
public class TableFileReadUtil {
	/**
	 * 默认的列名称
	 */
	private static final String DEFAULT_COLUMN_NAME = "列表";
	/**
	 * 指向xlsx文件后缀名
	 */
	private static final String FILE_SUFFIX_XLSX = "xlsx";

	private TableFileReadUtil() {
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
				columnNameList.addAll(createDefaultColumnName(wordDataList.size()));
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
	 * @param file         文件
	 * @param regex        切分规则
	 * @param isFirstTitle 首行是否为标题行
	 * @return 数据表类对象
	 * @throws UnsupportedFileException 文件未传入或读取异常时抛出的异常
	 */
	public static TableData<String> readTxt(File file, String regex, boolean isFirstTitle) {
		TableData<String> wordTable = new TableData<>();

		try (BufferedReader br = new BufferedReader(
				new FileReader(Optional.ofNullable(file).orElseThrow(() -> new UnsupportedFileException("未传入文件对象"))))) {
			// 指向当前表中是否存在标题
			AtomicBoolean hasTitle = new AtomicBoolean(false);
			// 按行遍历文件内容
			br.lines()
					// 将文本按照内容进行切分，转换为字符串数组
					.map(text -> text.split(regex))
					// 将数组转换为字符串集合
					.map(Arrays::asList).forEach(dataList -> {
						// 需要对第一行数据进行处理，若该行数据为标题行，则将其存储为标题，若为普通数据行，则添加基础标题
						if (!hasTitle.get()) {
							if (isFirstTitle) {
								wordTable.addTitle(dataList);
							} else {
								wordTable.addTitle(createDefaultColumnName(dataList.size()));
								wordTable.addRow(dataList);
							}
							// 设置标题存在
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
	 * @param file         待读取的文件
	 * @param sheetName    需要读取的sheet名称
	 * @param isFirstTitle 首行是否为标题行
	 * @return 数据表类对象
	 * @throws UnsupportedFileException 文件未传入或读取异常时抛出的异常
	 */
	public static TableData<String> readExcel(File file, String sheetName, boolean isFirstTitle) {
		// 用于读取excel
		Optional<Workbook> excelOptional = Optional.empty();
		try (FileInputStream fip = new FileInputStream(
				Optional.ofNullable(file).orElseThrow(() -> new UnsupportedFileException("未传入文件对象")))) {
			// 根据文件名的后缀，对其判断文件的格式，并按照相应的格式构造对象
			if (file.getName().indexOf(FILE_SUFFIX_XLSX) > -1) {
				// 通过XSSFWorkbook对表格文件进行操作
				excelOptional = Optional.ofNullable(new XSSFWorkbook(fip));
			} else {
				// 通过XSSFWorkbook对表格文件进行操作
				excelOptional = Optional.ofNullable(new HSSFWorkbook(fip));
			}
		} catch (IOException e) {
			throw new UnsupportedFileException("文件异常，无法进行读取：" + file.getAbsolutePath(), e);
		}

		try (Workbook excel = excelOptional.orElseThrow(() -> new UnsupportedFileException("Excel文件读取类未构造"))) {
			return readExcel(excel, sheetName, isFirstTitle);
		} catch (UnsupportedFileException | IOException e) {
			throw new UnsupportedFileException("文件异常，无法进行读取：" + file.getAbsolutePath(), e);
		}
	}

	/**
	 * 该方法用于读取并处理excel文件，根据传入的sheet名称来读取不同的sheet
	 *
	 * @param excel         excel类{@link Workbook}对象
	 * @param sheetName    需要读取的sheet名称
	 * @param isFirstTitle 首行是否为标题行
	 * @return 数据表类对象
	 * @throws UnsupportedFileException 文件未传入或读取异常时抛出的异常
	 */
	public static TableData<String> readExcel(Workbook excel, String sheetName, boolean isFirstTitle) {
		TableData<String> wordTable = new TableData<>();

		// 读取excel中的内容，若未存储读取的sheet名称，则读取第一个sheet
		Sheet sheet = Optional
				.ofNullable(Optional.ofNullable(sheetName).orElse("").isEmpty() ? excel.getSheetAt(0)
						: excel.getSheet(sheetName))
				.orElseThrow(() -> new UnsupportedFileException(String.format("“%s”工作表不存在", sheetName)));

		// 获取第一行内容
		Optional<Row> row = Optional.ofNullable(sheet.getRow(0));
		// 存储第一行指向的列个数，以此为总列数的参考标准
		int cellLength = row
				.orElseThrow(
						() -> new UnsupportedFileException(String.format("“%s”中无首行内容，无法读取", sheet.getSheetName())))
				.getLastCellNum();

		// 判断首行是否为标题行，若为标题行，则读取标题并进行存储；若不是，则存储默认的标题，并存储初始读取行的下标
		int startIndex = 0;
		if (isFirstTitle) {
			// 存储第一行数据
			wordTable.addTitle(readExcelLineData(row));
			// 设置行起始读取下标为第2行
			startIndex = 1;
		} else {
			// 存储第一行数据
			wordTable.addTitle(createDefaultColumnName(cellLength));
			// 设置行起始读取下标为第1行
			startIndex = 0;
		}

		IntStream.range(startIndex, sheet.getLastRowNum() + 1)
				// 转换下标为行对象
				.mapToObj(index -> Optional.ofNullable(sheet.getRow(index)))
				// 读取行中的单元格内容，将其转换为字符串集合
				.map(TableFileReadUtil::readExcelLineData).forEach(wordTable::addRow);

		return wordTable;
	}

	/**
	 * 用于读取Excel中的一行数据，并以集合形式进行返回
	 *
	 * @param rowOption 行封装类对象
	 * @return 数据集合
	 */
	private static List<String> readExcelLineData(Optional<Row> rowOption) {
		List<String> dataList = new ArrayList<>();
		rowOption.ifPresent(row -> {
			IntStream.range(0, row.getLastCellNum())
				.mapToObj(row::getCell)
				.map(TableFileReadUtil::getCellContent)
				.forEach(dataList::add);
		});

		return dataList;
	}

	/**
	 * 根据单元格的类型，对其内容进行输出
	 *
	 * @param cell 单元格类对象
	 * @return 单元格中的文本
	 */
	private static String getCellContent(Cell cellObj) {
		return Optional.ofNullable(cellObj).map(cell -> {
			// 判断单元格中的内容的格式
			if (CellType.NUMERIC == cell.getCellTypeEnum()) {
				// 数值类型
				// 判断单元格内的数据是否为日期
				if (DateUtil.isCellDateFormatted(cell)) {
					// 将单元格内容转换为Date后，在time中进行存储
					Time time = Time.parse(cell.getDateCellValue());
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
		//若单元格对象为空，则返回空串
		}).orElse("");
	}

	/**
	 * 该方法用于读取并处理旧版(后缀为“.doc”)word文件，可通过切分规则对文本每一行的内容进行切分
	 *
	 * @param file         文件
	 * @param regex        切分规则
	 * @param isFirstTitle 首行是否为标题行
	 * @return 数据表类对象
	 * @throws UnsupportedFileException 文件未传入或读取异常时抛出的异常
	 */
	public static TableData<String> readOldWord(File file, String regex, boolean isFirstTitle) {
		TableData<String> wordTable = new TableData<>();

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

			// 判断首行是否为标题行，若为标题行，则读取标题并进行存储；若不是，则存储默认的标题，并存储初始读取行的下标
			int startIndex = 0;
			// 获取段落
			List<String> firstRowTextList = Arrays
					.asList(Optional.ofNullable(wordFileRange.getParagraph(0).text()).filter(text -> !text.isEmpty())
							.orElseThrow(() -> new UnsupportedFileException("首段文本为空，无法进行拉取")).split(regex));
			if (isFirstTitle) {
				// 存储第一行数据
				wordTable.addTitle(firstRowTextList);
				// 设置行起始读取下标为第2行
				startIndex = 1;
			} else {
				// 存储第一行数据
				wordTable.addTitle(createDefaultColumnName(firstRowTextList.size()));
				// 设置行起始读取下标为第1行
				startIndex = 0;
			}

			// 生成段落下标，遍历word文档中的所有段落
			IntStream.range(startIndex, wordFileRange.numParagraphs())
					// 将下标转换为段落类对象
					.mapToObj(wordFileRange::getParagraph)
					// 读取段落的内容，对内容进行封装
					.map(pa -> Optional.ofNullable(pa.text()))
					// 去除换行符，并过滤掉空行
					.map(textOptional -> textOptional.map(text -> text.replaceAll("\\r", ""))
							.filter(text -> !text.isEmpty()))
					// 过滤掉无内容的段落
					.filter(text -> text.isPresent())
					// 切分字符串并转换为词语集合
					.map(textOptional -> Arrays
							.asList(textOptional.orElse("").split(Optional.ofNullable(regex).orElse(""))))
					// 按行存储至列表对象中
					.forEach(wordTable::addRow);

		} catch (IOException e) {
			throw new UnsupportedFileException("文件异常，无法进行读取：" + file.getAbsolutePath(), e);
		}

		return wordTable;
	}

	/**
	 * 该方法用于读取并处理新版(后缀为“.docx”)word文件，可通过切分规则对文本每一行的内容进行切分
	 *
	 * @param file         文件
	 * @param regex        切分规则
	 * @param isFirstTitle 首行是否为标题行
	 * @return 数据表类对象
	 * @throws UnsupportedFileException 文件未传入或读取异常时抛出的异常
	 */
	public static TableData<String> readNewWord(File file, String regex, boolean isFirstTitle) {
		TableData<String> wordTable = new TableData<>();

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
			List<XWPFParagraph> paragraphList = Optional.ofNullable(word.getParagraphs())
					.filter(list -> list.size() != 0).orElseThrow(() -> new UnsupportedFileException("Word文件中无内容"));

			// 获取段落
			List<String> firstRowTextList = Arrays
					.asList(Optional.ofNullable(paragraphList.get(0).getText()).filter(text -> !text.isEmpty())
							.orElseThrow(() -> new UnsupportedFileException("首段文本为空，无法进行拉取")).split(regex));
			// 判断首行是否为标题行，若为标题行，则读取标题并进行存储；若不是，则存储默认的标题，并存储初始读取行的下标
			int startIndex = 0;
			if (isFirstTitle) {
				// 存储第一行数据
				wordTable.addTitle(firstRowTextList);
				// paragraphList.remove(0);//直接移除会抛出异常
				// 设置行起始读取下标为第2行
				startIndex = 1;
			} else {
				// 存储生成的标题数据
				wordTable.addTitle(createDefaultColumnName(firstRowTextList.size()));
				// 设置行起始读取下标为第1行
				startIndex = 0;
			}

			// 生成段落下标，遍历word文档中的所有段落
			IntStream.range(startIndex, paragraphList.size()).mapToObj(index -> paragraphList.get(index))
					.map(pa -> Optional.ofNullable(pa.getText()))
					// 过滤掉无内容的段落
					.filter(text -> text.isPresent())
					// 切分字符串并转换为词语集合
					.map(textOptional -> Arrays.asList(textOptional.orElse("").split(regex)))
					// 按行存储至列表对象中
					.forEach(wordTable::addRow);
			;
		} catch (IOException e) {
			throw new UnsupportedFileException("文件异常，无法进行读取：" + file.getAbsolutePath(), e);
		}

		return wordTable;
	}

	/**
	 * 用于生成默认的列名
	 *
	 * @param length 数据列个数
	 * @return 相应的默认列名
	 */
	private static List<String> createDefaultColumnName(int length) {
		return IntStream.range(0, length)
				// 转换下标数字为列名称
				.mapToObj(index -> DEFAULT_COLUMN_NAME + (index + 1)).collect(Collectors.toList());
	}
}
