package pres.auxiliary.tool.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadFile {
	/**
	 * 用于对Excel文件的后缀判定
	 */
	private static String EXCEL = "xlsx";

	public static ArrayList<String> readExcel(File tagerFile, int sheetIndex) throws IOException {
		ArrayList<String> text = new ArrayList<String>();
		// 读取文件流
		FileInputStream fip = new FileInputStream(tagerFile);
		// 用于读取excel
		Workbook excel = null;
		// 根据文件名的后缀，对其判断文件的格式，并按照相应的格式构造对象
		if (tagerFile.getName().indexOf(EXCEL) > -1) {
			// 通过XSSFWorkbook对表格文件进行操作
			excel = new XSSFWorkbook(fip);
		} else {
			// 通过XSSFWorkbook对表格文件进行操作
			excel = new HSSFWorkbook(fip);
		}
		// 关闭流
		fip.close();

		// 读取下标对应的sheet
		Sheet sheet = excel.getSheetAt(sheetIndex);
		// 循环，读取所有的行
		for (int i = 0; i < sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);
			// 循环，根据列读取单元格
			for (int j = 0; j < row.getLastCellNum(); j++) {
				Cell cell = row.getCell(j);
				text.add(cellValueToString(cell));
			}
		}

		excel.close();
		return text;
	}

	/**
	 * 将单元格的内容转换为字符串
	 * @param cell 单元格对象
	 * @return 转换后的字符串
	 */
	private static String cellValueToString(Cell cell) {
		//如果单元格未被创建，则返回空串
		if (cell == null) {
			return "";
		}
		//判断单元的的样式为数字，则区分是否为日期，否则，直接返回其值
		if (cell.getCellTypeEnum() == CellType.NUMERIC) {
			//判断单元格的样式是否为日期格式，若是日期格式，则按照“yyyy年MM月dd日 HH:mm:ss”的格式进行转换，若为数字，则去除其整数的“.0”
			if (!cell.getCellStyle().getDataFormatString().equalsIgnoreCase("General")) {
				return new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(cell.getDateCellValue());
			} else {
				return cell.toString().replaceFirst("\\.0", "");
			}
		} else {
			return cell.toString();
		}
	}
}
