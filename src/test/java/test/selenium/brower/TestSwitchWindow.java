package test.selenium.brower;

import java.util.Set;

import org.openqa.selenium.chrome.ChromeDriver;

public class TestSwitchWindow {
	
	public static void main(String[] args) throws InterruptedException {
		TestDriver td = new TestDriver();
		ChromeDriver cd = td.getDriver();
		
		td.open();
		
		cd.get("http://www.hao123.com");//页面在第二个标签页中被打开
		Thread.sleep(5000);
		cd.quit();
	}
	
	public static class TestDriver {
		ChromeDriver cd;
		
		public TestDriver() {
			System.setProperty("webdriver.chrome.driver", "Resource/BrowersDriver/chromedriver.exe");
			cd = new ChromeDriver();
			cd.get("http://www.baidu.com");
		}
		
		public void open() {
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
		}
		
		public ChromeDriver getDriver() {
			return cd;
		}
	}
}
