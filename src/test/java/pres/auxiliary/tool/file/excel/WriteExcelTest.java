package pres.auxiliary.tool.file.excel;

import java.io.File;
import java.io.IOException;

import org.dom4j.DocumentException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class WriteExcelTest {
	final File configXml = new File("src/test/java/pres/auxiliary/tool/file/excel/WriteExcelTest.xml");
	final File excelFile = new File("src/test/java/pres/auxiliary/tool/file/excel/WriteExcelTest.xlsx");
	
	WriteExcel we;
	
	@BeforeClass 
	public void init() throws DocumentException, IOException {
		CreateExcelFile cef = new CreateExcelFile(configXml, excelFile);
		cef.setCoverFile(true);
		cef.create();
		
		we = new WriteExcel(configXml, excelFile);
	}
	
	@AfterClass
	public void writeFile() throws IOException {
		we.writeFile();
		java.awt.Desktop.getDesktop().open(excelFile.getParentFile());
	}
	
	@Test
	public void mark_fieldLink() {
		we.switchSheet("测试Sheet1")
			.addContent("标题", "测试标题")
			.addContent("状态", "1")
			.switchSheet("测试Sheet2")
			.addContent("关键用例", "2")
			.addContent("设计者", "测试")
			.switchSheet("测试Sheet1")
			.addContent("步骤", "步骤1", "步骤2", "步骤3")
			.end()
			.fieldLink("标题", "'测试Sheet2'!A1")
			.fieldLink("测试Sheet2", "设计者", "'测试Sheet1'!B2")
			;
	}
}
