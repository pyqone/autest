package pres.auxiliary.work.selenium.brower;

import java.io.File;
import java.util.Scanner;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import pres.auxiliary.work.selenium.page.Page;

public class FirefoxBrowerTest {
	/**
	 * 指向driver文件
	 */
	private final File driverFile = new File("Resource/BrowersDriver/Firefox/82.0.2/geckodriver.exe");
	
	/**
	 * 指向浏览器对象
	 */
	FirefoxBrower fb;
	
	/**
	 * 关闭浏览器
	 */
	@AfterClass(alwaysRun = true)
	public void quit() {
		System.out.println(fb.getAllInformation());
		System.out.println("输入任意字符继续：");
		Scanner sc = new Scanner(System.in);
		sc.next();
		fb.closeBrower();
		sc.close();
	}
	
	/**
	 * 测试打开浏览器后加载预设界面
	 */
	@Test
	public void firefoxBrowerTest_FilePage() {
		fb = new FirefoxBrower(driverFile, new Page("https://www.baidu.com", "百度"));
		fb.getDriver();
	}
}
