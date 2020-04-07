package test.selenium.brower;

import java.util.Set;

import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestOpenNewHandle {
	ChromeDriver cd;
	
	@BeforeClass
	public void openBrower() {
		System.setProperty("webdriver.chrome.driver", "Resource/BrowersDriver/chromedriver.exe");
		cd = new ChromeDriver();
	}
	
	@AfterClass
	public void quitBrower() throws InterruptedException {
		Thread.sleep(5000);
		cd.quit();
	}
	
	/**
	 * 覆盖原标签页
	 * @throws InterruptedException
	 */
	@Test
	public void overridePage() throws InterruptedException {
		cd.get("http://www.baidu.com");
		Thread.sleep(2000);
		cd.get("http://www.qq.com");
	}
	
	/**
	 * 打开新的标签页
	 * @throws InterruptedException
	 */
	@Test
	public void openNewLabel() throws InterruptedException {
		//获取当前所有的handle
		Set<String> handleSet = cd.getWindowHandles();
		//编写js脚本，执行js，以开启一个新的标签页
		String js = "window.open(\"https://www.sogou.com\");";
		cd.executeScript(js);
		//移除原有的windows的Handle，保留新打开的windows的Handle
		String newHandle = "";
		for (String handle : cd.getWindowHandles()) {
			if (!handleSet.contains(handle)) {
				newHandle = handle;
				break;
			}
		}
		//切换WebDriver
		cd.switchTo().window(newHandle);
		Thread.sleep(2000);
		cd.get("http://www.hao123.com");
	}
	
	/**
	 * 打开新的浏览器
	 */
	@Test
	public void openNewBrower() {
		//关闭原有的浏览器
		cd.quit();
		//重新构造，并进入待测站点
		System.setProperty("webdriver.chrome.driver", "Resource/BrowersDriver/chromedriver.exe");
		cd = new ChromeDriver();
		cd.get("http://www.163.com");
		
	}
}
