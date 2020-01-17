package pres.auxiliary.selenium.tool;

import java.io.IOException;
import java.lang.reflect.Method;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pres.auxiliary.selenium.browers.ChromeBrower;

public class RecordToolTest {
	/**
	 * 文件名
	 */
	private final String FILE_NAME = "test";
	
	private ChromeBrower chrome = new ChromeBrower("Resource/BrowersDriver/chromedriver.exe", 9222);
	
	/**
	 * 初始化数据，并用于测试setSaveFolder、setFileName和setModule方法
	 */
	@BeforeClass
	public void testSetFunction() {
		RecordTool.getLog().setFileName(FILE_NAME);
		
		RecordTool.getRecord().setFileName(FILE_NAME);
		
		RecordTool.getScreenshot().setDriver(chrome.getDriver());
		
		RecordTool.setModule("第一模块", true);
	}
	
	/**
	 * 关闭谷歌driver
	 * @throws IOException 
	 */
	@AfterClass
	public void closeDriver() throws IOException {
		chrome.getDriver().quit();
		RecordTool.createXmindReport("测试报告");
	}
	
	/**
	 * 输出测试方法及分隔符
	 */
	@BeforeMethod
	public void testSetFile(Method method) {
		System.out.println("-".repeat(20));
		System.out.println("当前执行方法：" + method.getName());
		System.out.println("=".repeat(10));
	}
	
	@AfterMethod
	public void endRecord(Method method) {
		System.out.println(RecordTool.getModuleInformation());
		System.out.println(RecordTool.getModuleJsonInformation());
	}
	
	/**
	 * 测试RecordStep.getRecordType()方法
	 */
	@Test
	public void testSetRecordType() {
		RecordTool.setModule("SetRecordType", true);
		RecordTool.startRecord(this.getClass().getName(), "testSetRecordType");
		RecordTool.setRecordType(RecordType.LOG);
		RecordTool.recordStep("这是testIsRecordStep()方法添加的第1步骤");
		RecordTool.setRecordType(RecordType.RECORD);
		RecordTool.recordStep("这是testIsRecordStep()方法添加的第2步骤");
		RecordTool.setRecordType(RecordType.SCREENSHOT);
		RecordTool.recordStep("这是testIsRecordStep()方法添加的第3步骤");
		RecordTool.setRecordType(RecordType.SYSTEM);
		RecordTool.recordStep("这是testIsRecordStep()方法添加的第4步骤");
		RecordTool.endRecord();
	}
	
	/**
	 * 测试RecordStep.setModule(方法)
	 */
	@Test
	public void testSetAndGetModule() {
		RecordTool.setModule("第二模块", false);
		RecordTool.startRecord(this.getClass().getName(), "testSetAndGetModule");
		RecordTool.endRecord();
		RecordTool.setModule("第二模块", false);
		RecordTool.startRecord(this.getClass().getName(), "testSetAndGetModule");
		RecordTool.endRecord();
		RecordTool.setModule("第三模块", false);
		RecordTool.startRecord(this.getClass().getName(), "testSetAndGetModule");
		RecordTool.endRecord();
		
		System.out.println(RecordTool.getModuleInformation());
		System.out.println(RecordTool.getModuleJsonInformation());
		System.out.println(RecordTool.getModuleInformation("第一模块"));
		System.out.println(RecordTool.getModuleJsonInformation("第一模块"));
		System.out.println(RecordTool.getModuleInformation("第四模块"));
		System.out.println(RecordTool.getModuleJsonInformation("第四模块"));
	}
	
	/**
	 * 测试RecordTool.recordStep()方法
	 */
	@Test
	public void testRecordStep() {
		RecordTool.setModule("RecordStep", true);
		RecordTool.startRecord(this.getClass().getName(), "testRecordStep");
		RecordTool.setRecordType(RecordType.values());
		RecordTool.recordStep("这是testRecordStep()方法添加的第1步骤");
		RecordTool.recordStep("这是testRecordStep()方法添加的第2步骤");
		RecordTool.recordStep("这是testRecordStep()方法添加的第3步骤");
		RecordTool.endRecord();
	}
	
	/**
	 * 测试isRecordStep()方法
	 */
	@Test
	public void testIsRecordStep() {
		RecordTool.setModule("IsRecordStep", true);
		RecordTool.startRecord(this.getClass().getName(), "testIsRecordStep");
		RecordTool.setRecordType(RecordType.values());
		RecordTool.recordStep("这是testIsRecordStep()方法添加的第1步骤");
		RecordTool.setRecordStep(false);
		RecordTool.recordStep("这是testIsRecordStep()方法添加的第2步骤");
		RecordTool.setRecordStep(true);
		RecordTool.recordStep("这是testIsRecordStep()方法添加的第3步骤");
		RecordTool.endRecord();
	}
	
	/**
	 * 测试recordResult()方法
	 */
	@Test
	public void testRecordResult() {
		RecordTool.setModule("RecordResult", true);
		RecordTool.startRecord(this.getClass().getName(), "testRecordResult");
		RecordTool.setRecordType(RecordType.values());
		RecordTool.recordResult(true, "成功", "失败");
		RecordTool.recordResult(false, "成功", "失败");
		RecordTool.recordResult(false, "成功");
		RecordTool.recordResult(true, "失败");
		RecordTool.endRecord();
	}
	
	/**
	 * 测试recordMark()方法
	 */
	@Test
	public void testRecordMark() {
		RecordTool.setModule("RecordMark", true);
		RecordTool.startRecord(this.getClass().getName(), "testRecordMark");
		RecordTool.setRecordType(RecordType.values());
		RecordTool.recordMark("第1个备注");
		RecordTool.recordMark("第2个备注");
		RecordTool.recordMark("第3个备注");
		RecordTool.endRecord();
	}
	
	/**
	 * 测试recordException()方法
	 */
	@Test
	public void testRecordException() {
		RecordTool.setModule("RecordException", true);
		RecordTool.startRecord(this.getClass().getName(), "testRecordException");
		RecordTool.setRecordType(RecordType.values());
		RecordTool.recordException(new NullPointerException("这是空指针异常"));
		RecordTool.endRecord();
	}
	
	/**
	 * 记录综合测试
	 */
	@Test
	public void testRecordAll() {
		RecordTool.setModule("RecordException", true);
		RecordTool.startRecord(this.getClass().getName(), "testRecordAll");
		RecordTool.setRecordType(RecordType.values());
		RecordTool.recordStep("第一步");
		RecordTool.recordStep("第二步");
		RecordTool.recordResult(true, "这是bug");
		RecordTool.recordMark("记录一下备注");
		RecordTool.recordException(new NullPointerException("这是空指针异常"));
		RecordTool.endRecord();
	}
	
	@Test(priority = 1)
	public void testGetAllModuleInformation() {
		RecordTool.startRecord(this.getClass().getName(), "testGetAllModuleInformation");
		RecordTool.endRecord();
		System.out.println(RecordTool.getAllModuleInformation());
	}
}
