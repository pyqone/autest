package test.javase;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TestSe {
	public static void main(String[] args) throws InterruptedException {
		//TestPrintBrowserInfomation();
		Test_1();
	}
	
	public static void TestPrintBrowserInfomation() {
		//若火狐路径在默认位置，且不需要使用配置文件时，可直接使用以下代码，以下代码作用为打开火狐浏览器
		FirefoxDriver driver = new FirefoxDriver();
		//巨大化浏览器
		driver.manage().window().maximize();
		//打开待测试的网址，注意，网址一定要加上http://，否则无法进入
		driver.get("http://www.hao123.com");
		
		System.out.println(driver.getPageSource());//输出HTML代码
		System.out.println("-------------------------------------------------------");
		System.out.println(driver.getCurrentUrl());//输出网址
		System.out.println("-------------------------------------------------------");
		System.out.println(driver.getCapabilities().getVersion());//输出浏览器版本
		System.out.println("-------------------------------------------------------");
		System.out.println(driver.getCapabilities().getBrowserName());//输出浏览器名称
		System.out.println("-------------------------------------------------------");
		System.out.println(driver.getCapabilities().getPlatform().name());//输出操作系统名称
		System.out.println("-------------------------------------------------------");
		System.out.println(driver.getCapabilities().getPlatform().getMajorVersion());//输出操作系统版本
		System.out.println("-------------------------------------------------------");
		driver.close();
	}
	
	public static void Test_1() throws InterruptedException {
		//若火狐路径不在默认位置时，需要使用该代码来找到火狐的位置
		//System.setProperty("webdriver.firefox.bin", "D:\\firefox\\firefox.exe");
		
		//若需要使用配置时（即在火狐中创建的配置文件），需要用到该行代码
		//ProfilesIni pi = new ProfilesIni();
		//FirefoxProfile profile = pi.getProfile("pyqone");
		//WebDriver driver = new FirefoxDriver(profile);
		
		//若火狐路径在默认位置，且不需要使用配置文件时，可直接使用以下代码，以下代码作用为打开火狐浏览器
		WebDriver driver = new FirefoxDriver();
		
		//巨大化浏览器
		driver.manage().window().maximize();

		//打开待测试的网址，注意，网址一定要加上http://，否则无法进入
		driver.get("http://www.hao123.com");
		
		//获取待测页面上的文本框定位
		//一般使用xpath定位和css定位，获取方式为使用火狐进行获取
		//findElement()方法表示查找定位方式所对应的元素
		//By.xpath()表示使用xpath方式来定位，此时复制的最简xpath
		//sendKeys()方法表示在控件中输入信息
		//以下代码的作用为在hao123的百度搜索文本框中输入QQ
		WebElement element = driver.findElement(By.xpath("//*[@id=\"search-input\"]"));
		((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('style',arguments[1])", element,"background:yellow;solid:red;");
		 element.sendKeys("QQ");
		 Thread.sleep(5000);
		//System.out.println(driver.findElement(By.xpath("//*[@id=\"search-input\"]")).getAttribute("value"));
		System.out.println(driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div[1]/div[2]/div/div/div[1]/a")).getTagName());
		System.out.println(driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div[1]/div[2]/div/div/div[1]/a")).getAttribute("value"));
		/*
		String js = "var s = document.getElementById(\"search-input\");";
		js += "return s.nodeValue";
		System.out.println(((JavascriptExecutor) driver).executeScript(js));
		*/
		
		//以上代码等价于，此时用的完整xpath来定位
		//driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div[1]/div[2]/div/div/div[1]/div[3]/form/div[1]/div/input")).sendKeys("QQ");
		//也等价于，此时用的是CSS方式定位
		//driver.findElement(By.cssSelector("html body div#skinroot.sk_skin-color-green div.layout-container.s-sbg1 div.layout-container-inner.s-sbg2 div div.hao123-search-panel-box div#hao123-search-panel.g-wd.page-width.hao123-search-panel div.hao123-search.hao123-indexsearchlist1 div#search.search div.right.form-wrapper form#search-form.form-hook div.input-wrapper.wrapper-hook.g-ib div.input-shadow.shadow-hook.g-ib input#search-input.input.input-hook")).sendKeys("QQ");
		/*
		//点击hao123页面上的百度一下按钮
		driver.findElement(By.xpath("//*[@value=\"百度一下\"]")).click();
		//等价于
		WebElement w = driver.findElement(By.xpath("//*[@value=\"百度一下\"]"));
		w.click();
		
		//关闭浏览器
		driver.close();
		*/
	}
}
