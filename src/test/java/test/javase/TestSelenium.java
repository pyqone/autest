package test.javase;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;

public class TestSelenium {
	public static void main(String[] args) {
		//固定写法，第一个参数，指定的是你使用的浏览器，第二个参数是谷歌浏览器驱动路径
		System.setProperty("webdriver.chrome.driver", "Resource/BrowersDriver/chromedriver.exe");
		//new对象
		ChromeDriver driver = new ChromeDriver();
		//打开浏览器，并进入指定的站点
		//注意：“http://”不能省略
		driver.get("https://www.hao123.com/");
		//全屏浏览器
		driver.manage().window().maximize();
		//设置加载时间，一下代码的含义是若页面在30秒内加载不出，则抛出异常
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		//获取页面title
		System.out.println(driver.getTitle());
		//在百度搜索文本框中，输入“abc”，点击“百度一下”
		driver.findElement(By.xpath("//*[@id=\"search\"]/form/div[2]/input")).sendKeys("abc");
		driver.findElement(By.xpath("//*[@id=\"search\"]/form/div[3]/input")).click();
		
		//点击id属性为test的元素
		driver.findElement(By.id("test")).click();
		//点击有超链接的“中国网”的元素
		driver.findElement(By.linkText("中国网")).click();
		//点击name属性为"word"的元素
		driver.findElement(By.name("word")).click();
		//点击html标签名为input的元素
		driver.findElement(By.tagName("input")).click();
		//点击xpath为“//*[@id=\"search\"]/form/div[3]/input”的元素，常用
		driver.findElement(By.xpath("//*[@id=\"search\"]/form/div[3]/input")).click();
		//点击css选择器为“html body.sk_skin-color-green div.index-page-inner ewc s-sbg2”的元素，常用
		driver.findElement(By.cssSelector("html body.sk_skin-color-green div.index-page-inner ewc s-sbg2")).click();
		//点击class属性为“searchWrapper”的元素
		driver.findElement(By.className("searchWrapper")).click();
		
		//div[1]/div[1]/form
		
		//双击事件
		//Actions a = new Actions(driver);
		//a.doubleClick(driver.findElement(By.xpath("//*[@id=\"search\"]/form/div[3]/input"))).perform();
		//关闭浏览器
		//driver.close();
	}
}
