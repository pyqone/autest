package test.javase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class TestPOIData {
	public static void main(String[] args) throws IOException {
		// 创建excel文件
		XSSFWorkbook xw = new XSSFWorkbook();
		XSSFSheet xs1 = xw.createSheet("测试");
		XSSFSheet xs2 = xw.createSheet("数据");

		xs1.createRow(0);
		// 加载下拉列表内容，由于Excel对序列有字数的限制，无法添加大量的数据进入序列，所以需要使用以下的方法实现
		XSSFDataValidationConstraint constraint1 = 
				new XSSFDataValidationConstraint(new String[] { "呵呵", "哈哈", "嘻嘻" });
		// 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
		CellRangeAddressList regions = new CellRangeAddressList(0, 0, 0, 0);
		// 数据有效性对象
		DataValidation d = new XSSFDataValidationHelper(xs1).createValidation(constraint1, regions);
		xs1.addValidationData(d);

		// 创建一列数据
		xs2.createRow(0).createCell(0).setCellValue("我");
		xs2.createRow(1).createCell(0).setCellValue("嘞");
		xs2.createRow(2).createCell(0).setCellValue("个");
		xs2.createRow(3).createCell(0).setCellValue("去");
		xs2.createRow(4).createCell(0).setCellValue("啊");
		xs2.createRow(5).createCell(0).setCellValue("!");

		XSSFRow xr2 = xs1.createRow(1);
		
		// 创建公式约束
		DataValidationConstraint constraint2 = new XSSFDataValidationHelper(xs1)
				.createFormulaListConstraint("=数据!$A$1:$A$" + String.valueOf(xs2.getLastRowNum() + 1));
		
		// 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
		CellRangeAddressList regions2 = new CellRangeAddressList(0, 0, 1, 1);
		// 数据有效性对象
		DataValidation d2 = new XSSFDataValidationHelper(xs1).createValidation(constraint2, regions2);
		xs1.addValidationData(d2);
		
		System.out.println(xs1.getLastRowNum());

		File f = new File("E:\\Test\\haha.xlsx");

		// 将预设内容写入Excel文件中，异常直接处理，不抛出
		try {
			// 定义输出流，用于向指定的Excel文件中写入内容
			FileOutputStream fop = new FileOutputStream(f);
			// 写入文件
			xw.write(fop);
			// 关闭流
			fop.close();
			xw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		java.awt.Desktop.getDesktop().open(f);
	}
}
