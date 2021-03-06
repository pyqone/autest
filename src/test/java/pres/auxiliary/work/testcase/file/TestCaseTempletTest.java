package pres.auxiliary.work.testcase.file;

import java.io.File;
import java.io.IOException;

import org.dom4j.DocumentException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pres.auxiliary.tool.file.excel.CreateExcelFile;
import pres.auxiliary.work.testcase.file.IncorrectFileException;

public class TestCaseTempletTest {
	/**
	 * 类对象
	 */
	CreateExcelFile temp;
	
	/**
	 * 模板文件类对象
	 */
	File tempFile = new File("Result/测试用例.xlsx");
	/**
	 * 配置文件类对象
	 */
	File conFile = new File("ConfigurationFiles/NewCaseConfigurationFile/jira测试用例导入模板.xml");

	/**
	 * 构造对象
	 * @throws DocumentException 
	 */
	@BeforeClass
	public void newTestCaseTemplet() throws DocumentException {
		temp = new CreateExcelFile(conFile, tempFile);
		temp.setCoverFile(true);
	}
	
	/**
	 * 打开文件夹
	 * @throws IOException
	 */
	@AfterClass
	public void openFolder() throws IOException {
		java.awt.Desktop.getDesktop().open(tempFile.getParentFile());
	}
	
	/**
	 * 设置生成的用例文件允许被覆盖
	 */
	@BeforeMethod
	public void canCover() {
		temp.setCoverFile(true);
	}
	
	/**
	 * 测试{@link CreateExcelFile#create()}以及{@link CreateExcelFile#setCoverFile(boolean)} <br>
	 * 断言将抛出IncorrectFileException异常
	 * @throws IOException 
	 */
	@Test(expectedExceptions = IncorrectFileException.class)
	public void createTest() throws IOException {
		//设置允许覆盖
		temp.setCoverFile(true);
		temp.create();
		//设置不允许覆盖
		temp.setCoverFile(false);
		temp.create();
	}
}
