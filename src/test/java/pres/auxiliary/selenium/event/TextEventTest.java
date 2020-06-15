package pres.auxiliary.selenium.event;

import java.lang.reflect.Method;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import pres.auxiliary.tool.randomstring.StringMode;
import pres.auxiliary.work.selenium.tool.RecordTool;

public class TextEventTest {
	TextEvent te;
	ChromeBrower cb = new ChromeBrower("Resource/BrowersDriver/chromedriver.exe", 9222);
	
	@BeforeTest
	public void openBrowers() {
		te = new TextEvent(cb.getDriver()); 
		RecordTool.getScreenshot().setDriver(te.getDriver());
		RecordTool.getRecord().setBrower(cb);
		RecordTool.getRecord().setActionName("彭宇琦");
	}
	
	/**
	 * 控制浏览器
	 */
	@BeforeMethod
	public void before(Method method) {
		RecordTool.getRecord().startRecord(this.getClass().getName(), method.getName());
		System.out.println("正在运行" + method.getName() + "方法");
		System.out.println("-".repeat(20));
	}
	
	/**
	 * 关闭浏览器
	 */
	@AfterMethod
	public void after(Method method) {
		RecordTool.getRecord().endRecord();
		System.out.println("-".repeat(20));
		System.out.println();
	}
	
	@AfterTest
	public void closeBrowers() {
		cb.getDriver().quit();
	}
	
	/**
	 * 测试{@link TextEvent#clear(String)}方法
	 */
	@Test(priority = 3)
	public void testClear() {
		te.clear("//*[@id='loginname']");
		System.out.println(Event.getStringValve());
	}
	
	/**
	 * 测试{@link TextEvent#input(String, String)}方法
	 */
	@Test(priority = 0)
	public void testInput() {
		te.input("//*[@id='loginname']", "sysadmin");
		System.out.println(Event.getStringValve());
	}
	
	/**
	 * 测试{@link TextEvent#avgIntergeInput(int, String...)}方法
	 */
	@Test(priority = 4)
	public void testAvgIntergeInput() {
		te.avgIntergeInput(100, "//*[@id='loginname']", "//*[@id='loginpass']");
		System.out.println(Event.getStringValve());
	}
	
	/**
	 * 测试{@link TextEvent#codeInput(String, String)}方法
	 */
	@Test(priority = 5)
	public void testCodeInput() {
		te.codeInput("//*[@id='eaf_captcha_text']", "//*[@id='verifyCode']");
		System.out.println(Event.getStringValve());
	}
	
	/**
	 * 测试{@link TextEvent#getAttributeValue(String, String)}方法
	 */
	@Test(priority = 1)
	public void testGetAttributeValue() {
		te.getAttributeValue("//*[@id='loginname']", "type");
		System.out.println(Event.getStringValve());
	}
	
	/**
	 * 测试{@link TextEvent#getText(String)}方法
	 */
	@Test(priority = 2)
	public void testGetText() {
		te.getText("//*[@id='loginname']");
		System.out.println(Event.getStringValve());
	}
	
	/**
	 * 测试{@link TextEvent#randomInput(String, int, int, StringModes...)}方法
	 */
	@Test(priority = 6)
	public void testRandomInput_modes() {
		te.clear("//*[@id='loginname']");
		te.randomInput("//*[@id='loginname']", 3, 3, StringMode.NUM);
		System.out.println(Event.getStringValve());
	}
	
	/**
	 * 测试{@link TextEvent#randomInput(String, int, int, String)}方法
	 */
	@Test(priority = 7)
	public void testRandomInput_mode() {
		te.clear("//*[@id='loginname']");
		te.randomInput("//*[@id='loginname']", 3, 10, "ABCDS");
		System.out.println(Event.getStringValve());
	}
}
