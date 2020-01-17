package test.javase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class TestPOI {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		XSSFWorkbook xw = new XSSFWorkbook(new FileInputStream(new File("D:\\8.test\\TestReadDateFormat\\测试文件.xlsx")));
		XSSFSheet xs = xw.getSheetAt(0);
		for (int i = 0; i < xs.getLastRowNum() + 1; i++) {
			if (xs.getRow(i).getCell(0) == null) {
				
			}
			if (xs.getRow(i).getCell(0).getCellTypeEnum() == CellType.NUMERIC
					&& !xs.getRow(i).getCell(0).getCellStyle().getDataFormatString().equalsIgnoreCase("General")) {
				System.out.println(new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒")
						.format(xs.getRow(i).getCell(0).getDateCellValue()));
			} else {
				System.out.println(xs.getRow(i).getCell(0).toString().replaceFirst("\\.0", ""));
			}
			System.out.println("-".repeat(11));
		}
	}
}
