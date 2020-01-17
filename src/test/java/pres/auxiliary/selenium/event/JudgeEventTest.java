package pres.auxiliary.selenium.event;

import java.lang.reflect.Method;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import pres.auxiliary.selenium.browers.ChromeBrower;
import pres.auxiliary.selenium.event.Event;
import pres.auxiliary.selenium.event.JudgeEvent;
import pres.auxiliary.selenium.tool.RecordTool;

public class JudgeEventTest {
	JudgeEvent je;
	ChromeBrower cb = new ChromeBrower("Resource/BrowersDriver/chromedriver.exe", 9222);
	
	@BeforeTest
	public void openBrowers() {
		je = new JudgeEvent(cb.getDriver()); 
		RecordTool.getScreenshot().setDriver(je.getDriver());
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
	 * 测试{@link JudgeEvent#judgeKey(String, boolean, String...)}方法，boolean参数为false
	 */
	@Test
	public void testJudgeKey_false() {
		je.judgeKey("//*[@id='create-issue-dialog']/div[1]/h2", false, "创建", "题目");
		System.out.println(Event.getBooleanValue());
		je.judgeKey("//*[@id='create-issue-dialog']/div[1]/h2", false, "创建", "问题");
		System.out.println(Event.getBooleanValue());
		je.judgeKey("//*[@id='create-issue-dialog']/div[1]/h2", false, "看见", "问题");
		System.out.println(Event.getBooleanValue());
		je.judgeKey("//*[@id='create-issue-dialog']/div[1]/h2", false, "看见", "题目");
		System.out.println(Event.getBooleanValue());
	}
	
	/**
	 * 测试{@link JudgeEvent#judgeKey(String, boolean, String...)}方法，boolean参数为true
	 */
	@Test
	public void testJudgeKey_true() {
		je.judgeKey("//*[@id='create-issue-dialog']/div[1]/h2", true, "创建", "题目");
		System.out.println(Event.getBooleanValue());
		je.judgeKey("//*[@id='create-issue-dialog']/div[1]/h2", true, "创建", "问题");
		System.out.println(Event.getBooleanValue());
		je.judgeKey("//*[@id='create-issue-dialog']/div[1]/h2", true, "看见", "问题");
		System.out.println(Event.getBooleanValue());
		je.judgeKey("//*[@id='create-issue-dialog']/div[1]/h2", true, "看见", "题目");
		System.out.println(Event.getBooleanValue());
	}
	
	/**
	 * 测试{@link JudgeEvent#judgeText(String, boolean, String)}方法，boolean参数为false
	 */
	@Test
	public void testJudgeText_false() {
		je.judgeText("//*[@id='create-issue-dialog']/div[1]/h2", false, "创建");
		System.out.println(Event.getBooleanValue());
		je.judgeText("//*[@id='create-issue-dialog']/div[1]/h2", false, "题目");
		System.out.println(Event.getBooleanValue());
		je.judgeText("//*[@id='create-issue-dialog']/div[1]/h2", false, "创建问题");
		System.out.println(Event.getBooleanValue());
	}
	
	/**
	 * 测试{@link JudgeEvent#judgeText(String, boolean, String)}方法，boolean参数为true
	 */
	@Test
	public void testJudgeText_true() {
		je.judgeText("//*[@id='create-issue-dialog']/div[1]/h2", true, "创建");
		System.out.println(Event.getBooleanValue());
		je.judgeText("//*[@id='create-issue-dialog']/div[1]/h2", true, "题目");
		System.out.println(Event.getBooleanValue());
		je.judgeText("//*[@id='create-issue-dialog']/div[1]/h2", true, "创建问题");
		System.out.println(Event.getBooleanValue());
	}
	
	/**
	 * 测试JudgeEvent类的judgeControl()方法
	 */
	@Test
	public void testJudgeControl() {
		je.judgeControl("//*[@id='create-issue-dialog']/div[1]/h9");
		System.out.println(Event.getBooleanValue());
	}
}
