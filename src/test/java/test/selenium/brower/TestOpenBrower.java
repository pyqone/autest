package test.selenium.brower;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

/**
 * <p><b>文件名：</b>TestOpenBrower.java</p>
 * <p><b>用途：</b>用于实验打开各类浏览器</p>
 * <p><b>编码时间：</b>2020年11月8日 下午2:49:54</p>
 * <p><b>修改时间：</b>2020年11月8日 下午2:49:54</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 */
public class TestOpenBrower {
	WebDriver driver;
	
	@AfterClass
	public void quit() {
		driver.quit();
	}
	
	@Test
	public void openIeBrower() {
//		File ieDriverFile = new File("Resource/BrowersDriver/Ie/IEDriverServer.exe");
		// 指定IE driver的存放路径
		System.setProperty("webdriver.ie.driver", "Resource/BrowersDriver/Ie/IEDriverServer.exe");
		InternetExplorerOptions ieo = new InternetExplorerOptions();
		
		//实例化webdriver对象，启动IE浏览器
		driver = new InternetExplorerDriver();		
		//通过对象driver调用具体的get方法来打开网页
        driver.get("http://www.baidu.com/"); 
	}
}
