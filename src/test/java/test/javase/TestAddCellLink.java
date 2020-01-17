package test.javase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class TestAddCellLink {
	public static void main(String[] args) throws Exception {
		// 构造ecxel对象
		XSSFWorkbook xw = null;
		File f = new File("C:\\AutoTest\\Record\\AutoTestRecord - 副本.xlsx");
		try {
			FileInputStream fip = new FileInputStream(f);
			xw = new XSSFWorkbook(fip);
			fip.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		XSSFCell xc = xw.getSheet("运行记录").getRow(2).getCell(3);

		// 使用creationHelpper来创建XSSFHyperlink对象
		CreationHelper createHelper = xw.getCreationHelper();
		XSSFHyperlink link = (XSSFHyperlink) createHelper.createHyperlink(HyperlinkType.DOCUMENT);
		link.setAddress("#错误记录!A2");
		xc.setHyperlink(link);
		
		// 将内容写入文件
		try {
			FileOutputStream fop = new FileOutputStream(new File("C:\\AutoTest\\Record\\AutoTestRecord - 副本.xlsx"));
			xw.write(fop);
			fop.close();
			xw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		java.awt.Desktop.getDesktop().open(new File("C:\\AutoTest\\Record\\AutoTestRecord - 副本.xlsx"));
	}
}
