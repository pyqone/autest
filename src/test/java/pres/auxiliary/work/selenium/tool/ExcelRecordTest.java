package pres.auxiliary.work.selenium.tool;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import org.dom4j.DocumentException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import pres.auxiliary.tool.file.excel.CreateExcelFile;

public class ExcelRecordTest {
	CreateExcelFile cef;
	ExcelRecord er;
	
	final File configXml = new File("ConfigurationFiles/SeleniumConfigurationFile/LogConfiguration/ExcelRecordTemplet.xml");
	final File tempFile = new File("src/test/java/pres/auxiliary/work/selenium/tool/Test.xlsx");
	final File imageFile = new File("src/test/java/pres/auxiliary/work/selenium/tool/微信图片_20200828101657.png");
	
	@BeforeTest
	public void init() throws DocumentException, IOException {
		cef = new CreateExcelFile(configXml, tempFile);
		cef.setCoverFile(true);
		cef.create();
		
		er = new ExcelRecord(configXml, tempFile);
	}
	
	@AfterTest
	public void writeFile() throws IOException {
		er.writeFile();
		Desktop.getDesktop().open(tempFile.getParentFile());
		Desktop.getDesktop().open(er.getCaseXml());
	}
	
	@BeforeClass
	public void recordBaseInfo() {
		er.setActionName("yuqipeng_d");
	}
	
	@BeforeMethod
	public void startRecord(Method method) {
		er.runMethod(this.getClass().getName(), method.getName());
		er.reckonByTime();
	}
	
	@AfterMethod
	public void endRecord() {
		er.end();
	}
	
	@Test
	public void runStepTest() {
		er.runStep("第一步", "第二步", "第三步");
	}
	
	@Test
	public void runResultTest() {
		er.runResult("结果1，不是BUG", false);
		er.runResult("结果2，是BUG", true);
	}
	
	@Test
	public void runMarkTest() {
		er.runMark("这是备注信息");
	}
	
	@Test
	public void runScreenshotTest() throws IOException {
		er.runScreenshot(imageFile);
	}
	
	@Test
	public void exceptionTest_NotFile() {
		er.exception(new NullPointerException("抛出了NullPointerException异常"));
	}
	
	@Test
	public void exceptionTest_HasFile() {
		er.exception(new RecordStateException("抛出了RecordStateException异常"), imageFile);
	}
	
	@Test
	public void caseConditionTest() {
		er.caseCondition("测试用例前置条件1", "测试用例前置条件2");
	}
	
	@Test
	public void caseTitleTest() {
		er.caseTitle("测试用例标题");
	}
	
	@Test
	public void caseStepTest() {
		er.caseStep("测试用例步骤1", "测试用例步骤2");
	}
	
	@Test
	public void caseExpectTest() {
		er.caseExpect("测试用例预期1", "测试用例预期2");
	}
	
	@Test
	public void addAllContentTest() {
		er.caseTitle("测试成功运行标题")
			.caseStep("测试第1步")
			.caseExpect("测试第1预期")
			.caseCondition("前置条件1")
			.runStep("实际第1步")
			.runStep("实际第2步")
			.runStep("实际第3步", "实际第4步")
			.runResult("实际结果1", false)
			.runResult("实际结果2", true)
			.runMark("实际备注")
			.runScreenshot(imageFile)
			.end();
		
		er.reckonByTime()
			.caseTitle("测试失败运行标题")
			.caseStep("测试第1步")
			.caseExpect("测试第1预期")
			.caseCondition("前置条件1")
			.runStep("实际第1步")
			.runStep("实际第2步", "实际第3步")
			.exception(new NullPointerException("此时抛出了NullPointerException异常"), imageFile)
			.runStep("实际第4步")
			.runStep("实际第5步")
			.exception(new RecordStateException("这里出现了RecordStateException异常"))
			.runResult("实际结果1", false)
			.runResult("实际结果2", true)
			.runMark("实际备注")
			.runScreenshot(imageFile);
	}
}
