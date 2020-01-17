package pres.auxiliary.selenium.browers;

import org.junit.Ignore;
import org.junit.Test;

public class TestChromeBrower {
	
	public String s = "22";
	public String setS(String text) {
		return s = text;
	}
	
	/**
	 * 测试ChromeBrower(String driverPath, int port)
	 */
	@Ignore
	@Test
	public void test_01_ChromeBrower() {
		ChromeBrower cb = new ChromeBrower("src/test/java/pres/auxiliary/selenium/browers/chromedriver.exe", 9222);
		System.out.println(cb.getDriver().getCurrentUrl());
	}
	
	/**
	 * 测试通过已打开的浏览器改变URL
	 */
	@Test
	public void test_02_setURL() {
		ChromeBrower cb = new ChromeBrower("src/test/java/pres/auxiliary/selenium/browers/chromedriver.exe", 9222);
		cb.setURL("http://www.baidu.com");
		cb.getDriver();
	}
	
	/**
	 * 用于测试普通浏览器转换成已打开的浏览器的Driver
	 */
	@Test
	public void test_03_setPort() {
		ChromeBrower cb = new ChromeBrower("src/test/java/pres/auxiliary/selenium/browers/chromedriver.exe", "http://www.baidu.com");
		System.out.println(cb.getDriver().getCurrentUrl());
		cb.setPort(9222);
		System.out.println(cb.getDriver().getCurrentUrl());
	}
	
	@Test
	public void test_04_removeControl() {
		ChromeBrower cb = new ChromeBrower("src/test/java/pres/auxiliary/selenium/browers/chromedriver.exe", 9222);
		System.out.println(cb.getDriver().getCurrentUrl());
		cb.removeControl();
		System.out.println(cb);
		cb.setURL("http://www.baidu.com");
		cb.getDriver();
		cb.close();
	}
	
	@Test
	public void test_05_closeOpenBrower() {
		ChromeBrower cb = new ChromeBrower("src/test/java/pres/auxiliary/selenium/browers/chromedriver.exe", 9222);
		cb.getDriver().close();
		cb.close();
	}
}
