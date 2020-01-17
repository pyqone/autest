package pres.auxiliary.selenium.event;

import java.lang.reflect.Method;

import org.openqa.selenium.By;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import pres.auxiliary.selenium.browers.ChromeBrower;
import pres.auxiliary.selenium.event.ClickEvent;
import pres.auxiliary.selenium.event.Event;
import pres.auxiliary.selenium.tool.RecordTool;

/**
 * <p><b>文件名：</b>ClickEventTest.java</p>
 * <p><b>用途：</b>用于对ClickEvent类的单元测试</p>
 * <p><b>测试地址：</b>http://10.19.27.16/gjtManager/#/eduTrainManager/questionManager</p>
 * <p><b>编码时间：</b>2019年8月30日下午7:21:49</p>
 * <p><b>修改时间：</b>2019年11月29日上午9:53:37</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public class ClickEventTest {
	ClickEvent ce;
	ChromeBrower cb = new ChromeBrower("Resource/BrowersDriver/chromedriver.exe", 9222);
	
	@BeforeTest
	public void openBrowers() {
		ce = new ClickEvent(cb.getDriver()); 
		RecordTool.getScreenshot().setDriver(ce.getDriver());
		RecordTool.getRecord().setBrower(cb);
		RecordTool.getRecord().setActionName("彭宇琦");
	}
	
	@BeforeMethod
	public void before(Method method) {
		RecordTool.getRecord().startRecord(this.getClass().getName(), method.getName());
		System.out.println("正在运行" + method.getName() + "方法");
		System.out.println("-".repeat(20));
	}
	
	@AfterMethod
	public void after(Method method) throws InterruptedException {
		RecordTool.getRecord().endRecord();
		System.out.println("-".repeat(20));
		System.out.println();
		Thread.sleep(1500);
	}
	
	@AfterTest
	public void closeBrowers() {
		cb.getDriver().quit();
	}
	
	/**
	 * 测试{@link ClickEvent#doubleClick(String)}方法
	 */
	@Test(priority = 0)
	public void doubleclickTest() {
		ce.doubleClick("//*[@id='app']/div/div/section/div/div[2]/div[3]/table/tbody/tr[1]/td[1]/div/span");
		System.out.println(Event.getStringValve());
	}
	
	/**
	 * 测试{@link ClickEvent#click(String)}方法
	 */
	@Test(priority = 2)
	public void clickTest() {
//		ce.click("//*[@id='app']/div/div/section/div/div[1]/button[2]/span");
//		boolean a = ce.getDriver().findElement(By.xpath("//*[@id=\"app\"]/div/div/section/div/div[3]/div/button[2]")).isDisplayed();
//		boolean b = ce.getDriver().findElement(By.xpath("//*[@id=\"app\"]/div/div/section/div/div[3]/div/button[2]")).isEnabled();
//		System.out.println(a + ", " + b);
		ce.click("//*[@id=\"app\"]/div/div/section/div/div[3]/div/button[2]");
		System.out.println(Event.getStringValve());
	}
	
	/**
	 * 测试{@link ClickEvent#rightClick(String)}方法
	 */
	@Test(priority = 1)
	public void rightClickTest() {
		ce.rightClick("//*[@id=\"app\"]/div/div/section/div/div[2]/div[3]/table/tbody/tr[1]/td[2]/div/span");
		System.out.println(Event.getStringValve());
	}
}
