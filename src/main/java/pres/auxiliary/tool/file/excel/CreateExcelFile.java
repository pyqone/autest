package pres.auxiliary.tool.file.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import pres.auxiliary.work.selenium.datadriven.TableFileReadUtil;
import pres.auxiliary.work.testcase.file.IncorrectFileException;
import pres.auxiliary.work.testcase.templet.LabelNotFoundException;

/**
 * <p>
 * <b>文件名：</b>CreateCaseFile.java
 * </p>
 * <p>
 * <b>用途：</b>用于根据模板xml配置文件来生成Excel模板文件
 * </p>
 * <p>
 * <b>编码时间：</b>2020年2月11日下午11:12:56
 * </p>
 * <p>
 * <b>修改时间：</b>2020年4月1日下午7:11:18
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 8
 *
 */
public class CreateExcelFile {
	/**
	 * 用于对数据有效性在文件中标题的sheet与id之间的分隔符
	 */
	public static final String SIGN = "-";
	
	/**
	 * 定义数据有效性sheet页的名称
	 */
	public static final String DATA_SHEET_NAME = "数据有效性";

	/**
	 * 用于指向用例文件，并赋予其默认值
	 */
	private File templetFile = new File("");

	/**
	 * 用于控制是否覆盖原文件
	 */
	private boolean cover = false;

	/**
	 * 用于指向配置文件的Document对象
	 */
	private Document xml;

	/**
	 * 根据xml配置文件位置以及模板文件生成文件位置进行构造
	 * 
	 * @param configurationFile xml配置文件
	 * @param templetFile       测试用例模板文件生成位置
	 * @throws DocumentException      当xml配置文件错误时抛出
	 * @throws IncorrectFileException 文件格式或路径不正确时抛出的异常
	 */
	public CreateExcelFile(File configurationFile, File templetFile) throws DocumentException {
		this(new SAXReader().read(configurationFile), templetFile);
	}
	
	/**
	 * 根据文件模板xml的{@link Document}类对象以及模板文件生成文件位置进行构造
	 * 
	 * @param configDocument 文件模板xml的{@link Document}类对象
	 * @param templetFile       测试用例模板文件生成位置
	 * @throws IncorrectFileException 文件格式或路径不正确时抛出的异常
	 */
	public CreateExcelFile(Document configDocument, File templetFile) {
		// 判断路径是否包含“.xlsx”（使用的方法为XSSFWork）
		if (templetFile.getAbsolutePath().indexOf(".xlsx") > -1) {
			this.templetFile = templetFile;
		} else {
			throw new IncorrectFileException("不正确的文件格式：" + templetFile.getAbsolutePath());
		}
		
		xml = configDocument;
	}

	/**
	 * 返回生成的测试用例模板文件类对象
	 * 
	 * @return 测试用例文件的保存路径
	 */
	public File getTempletFile() {
		return templetFile;
	}

	/**
	 * 设置测试用例模板文件生成位置
	 * 
	 * @param templetFile 测试用例模板文件生成位置
	 */
	public void setTempletFile(File templetFile) {
		// 判断路径是否包含“.xlsx”（使用的方法为XSSFWork）
		if (templetFile.getAbsolutePath().indexOf(".xlsx") > -1) {
			this.templetFile = templetFile;
		} else {
			throw new IncorrectFileException("不正确的文件格式：" + templetFile.getAbsolutePath());
		}
	}

	/**
	 * 用于设置是否允许覆盖文件，当设置为false时，在创建测试用例模板时会判断文件是否存在（文件同名），若存在，则抛出异常；若设置为true，
	 * 则不做重复文件判断，直接覆盖。在类中，默认为false。
	 * 
	 * @param cover 是否覆盖文件
	 */
	public void setCoverFile(boolean cover) {
		this.cover = cover;
	}

	/**
	 * 根据xml配置文件中的内容，生成相应的测试用例模板文件
	 * 
	 * @return 生成的模板类文件对象
	 * @throws IOException       文件路径有误时抛出的异常
	 * @throws DocumentException xml配置文件有误时抛出的异常
	 */
	public File create() throws IOException {
		// 判断文件是否已经存在，若存在且设置为不可覆盖时，则抛出异常；若允不存在，则创建路径
		if (templetFile.exists()) {
			if (!cover) {
				throw new IncorrectFileException("文件存在：" + templetFile.getAbsolutePath());
			}
		} else {
			// 创建文件存储的路径文件夹
			templetFile.getParentFile().mkdirs();
		}

		// 创建excel文件
		XSSFWorkbook xw = new XSSFWorkbook();

		// 针对表格进行操作
		// 创建sheet
		createSheet(xw);
		// 创建标题行
		createTable(xw);
		// 创建数据有效性
		createDataValidation(xw);

		// 定义输出流，用于向指定的Excel文件中写入内容
		FileOutputStream fop = new FileOutputStream(templetFile);
		// 写入文件
		xw.write(fop);
		// 关闭流
		fop.close();
		xw.close();
		
		return templetFile;
	}

	/**
	 * 用于创建excel文件sheet
	 * 
	 * @param xw 指向excel文件对象
	 */
	private void createSheet(XSSFWorkbook xw) {
		// 读取所有sheet标签
		List<?> xmlSheetList = xml.getRootElement().elements("sheet");

		// 循环，创建sheet
		for (int i = 0; i < xmlSheetList.size(); i++) {
			// 获取标签的name属性，并将该属性值作为sheet的名称
			XSSFSheet xs = xw.createSheet(((Element) (xmlSheetList.get(i))).attributeValue("name"));

			// 冻结表格
			int z = Integer.valueOf(((Element) (xmlSheetList.get(i))).attributeValue("freeze"));
			xs.createFreezePane(z, 1, z, 1);
		}
	}

	/**
	 * 用于创建sheet中的表格
	 * 
	 * @param xw 指向excel文件对象
	 */
	private void createTable(XSSFWorkbook xw) {
		// 读取所有sheet标签
		List<?> xmlSheetList = xml.getRootElement().elements("sheet");
		// 循环，读取sheet
		for (int i = 0; i < xmlSheetList.size(); i++) {
			// 获取标签的name属性，通过该属性在excel文件中查找sheet
			XSSFSheet xs = xw.getSheet(((Element) (xmlSheetList.get(i))).attributeValue("name"));

			// 获取sheet标签下的所有column标签
			List<?> xmlColumnList = ((Element) (xmlSheetList.get(i))).elements("column");

			// 创建第一行
			XSSFRow xr = xs.createRow(0);
			// 循环，创建单元格，并将其赋值
			for (int j = 0; j < xmlColumnList.size(); j++) {
				// 创建单元格
				XSSFCell xc = xr.createCell(j);
				// 填写表头
				xc.setCellValue(((Element) (xmlColumnList.get(j))).attributeValue("name"));
				// 读取宽度信息并设置单元格的宽度
				xs.setColumnWidth(j,
						(short) (Double.valueOf(((Element) (xmlColumnList.get(j))).attributeValue("wide")) * 256));

				// 设置单元格的样式
				xc.setCellStyle(columnStyle(xw));
			}

			// 添加筛选按钮
			xs.setAutoFilter(CellRangeAddress.valueOf((String.valueOf((char) (65)) + "1:"
					+ String.valueOf((char) (65 + xmlColumnList.size() - 1)) + "1")));
		}
	}

	/**
	 * 创建数据有效性
	 * 
	 * @param xw 指向excel文件对象
	 */
	@SuppressWarnings("unchecked")
	private void createDataValidation(XSSFWorkbook xw) {
		// 读取所有sheet标签
		List<Element> xmlSheetList = xml.getRootElement().elements("sheet");

		// 定义行下标，初始值为-1用于做前自增操作，这样可无需在最后写列下标自增的代码
		int columnIndex = -1;

		// 循环，读取sheet
		for (int sheetIndex = 0; sheetIndex < xmlSheetList.size(); sheetIndex++) {
			// 读取所有datas标签
			List<Element> datasList = xmlSheetList.get(sheetIndex).elements("datas");

			// 若不存在数据有效性标签，则直接结束
			if (datasList.size() == 0) {
				continue;
			}

			// 判断数据有效性sheet页是否存在，若不存在，则对其进行创建
			XSSFSheet dataSheet;
			if ((dataSheet = xw.getSheet(DATA_SHEET_NAME)) == null) {
				dataSheet = xw.createSheet("数据有效性");
			}

			for (int datasIndex = 0; datasIndex < datasList.size(); datasIndex++) {
				// 创建查找数据有效性在表中相应位置的xpath（以//sheet[@name='']/column[@id='']的形式定位）
				// 其中,sheet的name属性通过datas标签的父标签(sheet标签)的name属性获取
				// column的id属性通过本身的id属性获取
				String sheetName = datasList.get(datasIndex).getParent().attributeValue("name");
				String xpath = "//sheet[@name='" + sheetName
						+ "']/column[@id='" + datasList.get(datasIndex).attributeValue("id") + "']";

				// 写入数据有效性到sheet中
				// 定义列下标
				int rowIndex = -1;
				// 添加标题，若该行已被创建，则直接获取来创建单元格
				Element node = (Element) (xml.selectSingleNode(xpath));
				// 若标签未找到，则抛出异常
				if (node == null) {
					throw new LabelNotFoundException("无法找到相应的标签：" + xpath);
				}
				
				//判断标题行是否被创建，若未被创建，则创建其行并创建单元格；反之，则直接创建单元格
				XSSFRow titleRow;
				XSSFCell titleCell;
				if ((titleRow = dataSheet.getRow(++rowIndex)) == null) {
					titleCell = dataSheet.createRow(rowIndex).createCell(++columnIndex);
				} else {
					titleCell = titleRow.createCell(++columnIndex);
				}
				
				//设置单元格的值与格式
				titleCell.setCellValue(sheetName + SIGN + node.attributeValue("id"));
				titleCell.setCellStyle(columnStyle(xw));

				// 添加数据，获取相应datas下的所有data标签
				writeDataValidity(datasList.get(datasIndex), dataSheet, rowIndex, columnIndex);
			}
		}
	}

	/**
	 * 用于创建标题行的格式
	 * 
	 * @param xw 指向excel文件对象
	 * @return 返回样式类对象
	 */
	private XSSFCellStyle columnStyle(XSSFWorkbook xw) {
		// 创建样式
		XSSFCellStyle xcs = xw.createCellStyle();
		// 设置单元格水平居中
		xcs.setAlignment(HorizontalAlignment.CENTER);
		// 设置单元格垂直居中
		xcs.setVerticalAlignment(VerticalAlignment.CENTER);
		// 创建字体样式
		XSSFFont xf = xw.createFont();
		// 设置字体名称
		xf.setFontName("宋体");
		// 设置字体大小，注意，字体大小单位为磅，小四字体对应12磅
		xf.setFontHeightInPoints((short) 12);
		// 设置字体加粗
		xf.setBold(true);
		// 设置字体的样式
		xcs.setFont(xf);
		// 设置单元格自动换行
		xcs.setWrapText(true);

		return xcs;
	}
	
	/**
	 * 用于将写入数据有效性至相应的sheet页
	 * @param datasElement
	 * @param dataSheet
	 * @param rowIndex
	 * @param columnIndex
	 */
	@SuppressWarnings("unchecked")
	private void writeDataValidity(Element datasElement, XSSFSheet dataSheet, int rowIndex, int columnIndex) {
		// 添加数据，获取相应datas下的所有data标签
		List<Element> dataElementList = datasElement.elements();
		ArrayList<String> dataTextList = new ArrayList<>();
		for (int j = 0; j < dataElementList.size(); j++) {
			//判断标签是否为文件标签，若为文件标签，则读取外部数据
			if ("file".equalsIgnoreCase(dataElementList.get(j).getName())) {
				try {
					dataTextList.addAll(getFileDataValidity(dataElementList.get(j)));
				} catch (Exception e) {
					//若抛出任何异常，则说明xml配置不正确，故不进行操作
				}
			} else {
				dataTextList.add(dataElementList.get(j).attributeValue("name"));
			}
		}
		
		//写入文件
		for (String dataText : dataTextList) {
			//判断当前行是否被创建，若未被创建，则创建其行并创建单元格；反之，则直接创建单元格
			XSSFRow row;
			// 添加数据，若该行已被创建，则直接获取来创建单元格
			if ((row = dataSheet.getRow(++rowIndex)) == null) {
				dataSheet.createRow(rowIndex).createCell(columnIndex)
						.setCellValue(dataText);
			} else {
				row.createCell(columnIndex).setCellValue(dataText);
			}
		}
	}
	
	/**
	 * 用于获取写在外部文件中的数据有效性
	 * @param textElement 数据标签
	 * @return 从外部文件中读取到的数据
	 */
	private ArrayList<String> getFileDataValidity(Element textElement) {
		ArrayList<String> dataList = new ArrayList<>();
		try {
			//读取数据有效性文件内容
			File dataFile = new File(textElement.attributeValue("path"));
			String sheet = textElement.attributeValue("regex");
			TableFileReadUtil lfr = new TableFileReadUtil(dataFile, sheet);
			
			//获取需要读取的列信息，若未指定列信息，则默认读取第一列
			int columnIndex = textElement.attributeValue("column") == null ? 0 : Integer.valueOf(textElement.attributeValue("column"));
			//读取起始行信息，若未指定起始行信息，则默认读取第一行
			int startRowIndex = textElement.attributeValue("start_row") == null ? 0 : Integer.valueOf(textElement.attributeValue("start_row"));
			//读取结束行信息，若未指定起始行信息，则默认读取最后一行
			int endRowIndex = textElement.attributeValue("end_row") == null ? lfr.getCoulumnSize(columnIndex) : Integer.valueOf(textElement.attributeValue("end_row"));
			//获取数据信息
			dataList.addAll(lfr.getColumn(columnIndex, startRowIndex, endRowIndex));
		} catch (Exception e) {
			//若抛出任何异常，则说明xml配置不正确，故不进行操作
		}
		
		//去除空行
		for (int i = 0; i < dataList.size(); i++) {
			if ("".equals(dataList.get(i))) {
				dataList.remove(i);
				i--;
			}
		}
		
		return dataList;
	}
}
