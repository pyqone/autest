package pres.auxiliary.selenium.event;

import java.lang.reflect.Method;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import pres.auxiliary.selenium.browers.ChromeBrower;
import pres.auxiliary.selenium.tool.RecordTool;

public class SelectEventTest {
	SelectEvent se;
	ChromeBrower cb = new ChromeBrower("Resource/BrowersDriver/chromedriver.exe", 9222);
	
	Event event;
	
	private final String SELECT_XPATH = "//*[@id='customfield_10508']";
	private final String DIV_XPATH = "/html/body/div[4]/div/div";
	
	@BeforeTest
	public void openBrowers() {
		se = new SelectEvent(cb.getDriver()); 
		event = Event.newInstance(cb.getDriver());
		RecordTool.getScreenshot().setDriver(se.getDriver());
		RecordTool.getRecord().setBrower(cb);
		RecordTool.getRecord().setActionName("彭宇琦");
	}
	
	/**
	 * 控制浏览器
	 */
	@BeforeMethod
	public void before(Method method) {
		RecordTool.startRecord(this.getClass().getName(), method.getName());
		System.out.println("正在运行" + method.getName() + "方法");
		System.out.println("-".repeat(20));
//		event.click("//*[@id=\"app\"]/div/div/section/div/div[1]/div[2]/div[1]/input");
	}
	
	/**
	 * 关闭浏览器
	 */
	@AfterMethod
	public void after(Method method) {
		RecordTool.endRecord();
		System.out.println("-".repeat(20));
		System.out.println();
	}
	
	@AfterTest
	public void closeBrowers() {
		cb.getDriver().quit();
	}
	
	@Test(enabled = false)
	public void test() {
		Select select = new Select(cb.getDriver().findElement(By.xpath(DIV_XPATH)));
		//获取当前选择的选项
		select.getAllSelectedOptions().forEach(element -> System.out.println(element.getText()));
		//获取所有的选项
		select.getOptions().forEach(element -> System.out.println(element.getText()));
	}
	
	/**
	 * 测试{@link SelectEvent#selectFirst(String)}方法
	 */
	@Test(priority = 2)
	public void testSelectFirst() {
		se.selectFirst(DIV_XPATH);
	}
	
	/**
	 * 测试{@link SelectEvent#select(String, int)}方法，指定正数选项值
	 */
	@Test(priority = 0)
	public void testSelect_Positive() {
		//表示第三个选项
		se.select(DIV_XPATH, 3);
		System.out.println(Event.getStringValve());
	}
	
	/**
	 * 测试{@link SelectEvent#select(String, int)}方法，指定负数选项值
	 */
	@Test(priority = 0)
	public void testSelect_Minus() {
		//表示倒数第三个选项
		se.select(DIV_XPATH, -3);
		System.out.println(Event.getStringValve());
	}
	
	/**
	 * 测试{@link SelectEvent#select(String, int)}方法，指定正数选项值
	 */
	@Test(priority = 0)
	public void testSelect_Zero() {
		//表示随机选择一个
		se.select(DIV_XPATH, 0);
		System.out.println(Event.getStringValve());
	}
	
	/**
	 * 测试{@link SelectEvent#selectLast(String)}方法
	 */
	@Test(priority = 1)
	public void testSelectLast() {
		se.selectLast(DIV_XPATH);
		System.out.println(Event.getStringValve());
	}
	
	/**
	 * 测试{@link SelectEvent#select(String, int)}方法，指定正数选项值
	 */
	@Test(priority = 3)
	public void testSelect_Exception() {
		se.select(DIV_XPATH, -10);
		System.out.println(Event.getStringValve());
	}
	
	/**
	 * 测试{@link SelectEvent#select(String, String)}方法，指定正数选项值
	 */
	@Test(priority = 4)
	public void testSelect_String() {
//		se.select(SELECT_XPATH, "运营支撑");
		se.select(DIV_XPATH, "南宁市精品线路、快速路挡墙护坡绿化工程2标");
		System.out.println(Event.getStringValve());
	}
}
