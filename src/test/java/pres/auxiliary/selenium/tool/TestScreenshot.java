package pres.auxiliary.selenium.tool;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriverException;

import pres.auxiliary.work.selenium.tool.Screenshot;

public class TestScreenshot {
	static ChromeBrower cb;
	static Screenshot sc;
	static Event event;
	
	@BeforeClass
	public static void openBrowers() throws InterruptedException {
		cb = new ChromeBrower("D:\\2.学习\\1.自动化测试\\1.自动化驱动\\chromedriver.exe", "http://116.10.187.227:88");
		sc = new Screenshot(cb.getDriver());
		event = Event.newInstance(cb.getDriver());
	}
	
	@AfterClass
	public static void closeBrowers() {
		cb.close();
	}
	
	@Test
	public void testScreenshotToTime() throws InterruptedException, WebDriverException, IOException {
		sc.screenshotToTime();
		Thread.sleep(6000);
		
	}
}
