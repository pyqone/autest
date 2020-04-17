package pres.auxiliary.work.selenium.brower;

import java.io.File;
import java.util.Scanner;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import pres.auxiliary.work.selenium.brower.ChromeBrower.ChromeOptionType;

/**
 * <p><b>文件名：</b>TestChromeBrower.java</p>
 * <p><b>用途：</b>用于对{@link ChromeBrower}类进行测试</p>
 * <p><b>编码时间：</b>2020年4月17日下午3:40:47</p>
 * <p><b>修改时间：</b>2020年4月17日下午3:40:47</p>
 * @author 
 * @version Ver1.0
 * @since JDK 12
 *
 */
public class TestChromeBrower {
	/**
	 * 指向driver文件
	 */
	private final File driverFile = new File("Resource/BrowersDriver/Chrom/78.0394.70/chromedriver.exe");
	
	/**
	 * 指向浏览器对象
	 */
	ChromeBrower cb;
	
	/**
	 * 关闭浏览器
	 */
	@AfterClass
	public void quit() {
		cb.getAllInformation().forEach((k, v) -> {
			System.out.println(k + "：" + v);
		});
		Scanner sc = new Scanner(System.in);
		sc.next();
		cb.closeBrower();
		sc.close();
	}
	
	/**
	 * 测试只打开浏览器
	 */
	@Test
	public void chromeBrowerTest_File() {
		cb = new ChromeBrower(driverFile);
		cb.getDriver();
	}
	
	/**
	 * 测试打开浏览器后加载预设界面
	 */
	@Test
	public void chromeBrowerTest_FilePage() {
		cb = new ChromeBrower(driverFile, new Page("https://www.baidu.com", "百度"));
		cb.getDriver();
	}
	
	/**
	 * 测试打开浏览器后加载预设界面
	 */
	@Test
	public void chromeBrowerTest_FileStringString() {
		cb = new ChromeBrower(driverFile, "https://www.hao123.com", "好123");
		cb.getDriver();
	}
	
	@Test
	public void addConfigTest_ChromeOptionType() {
		cb = new ChromeBrower(driverFile, "https://www.hao123.com", "好123");
		
		//不加载图片
		cb.addConfig(ChromeOptionType.DONOT_LOAD_IMAGE);
		//不加载js
		cb.addConfig(ChromeOptionType.DONOT_LOAD_JS);
		//不允许弹窗
		cb.addConfig(ChromeOptionType.DONOT_POPUPS);
		//全屏启动
		cb.addConfig(ChromeOptionType.HEADLESS);
		
		cb.getDriver();
	}
	
	@Test
	public void addConfigTest_ChromeOptionTypeObject() {
		cb = new ChromeBrower(driverFile, "https://www.hao123.com", "好123");
		
		//不加载图片
		cb.addConfig(ChromeOptionType.DONOT_LOAD_IMAGE);
		//不加载js
		cb.addConfig(ChromeOptionType.DONOT_LOAD_JS);
		//1027*768的分辨率启动
		cb.addConfig(ChromeOptionType.SET_WINDOW_SIZE, "1224, 768");
		
		cb.getDriver();
	}
	
	/**
	 * 测试控制已打开的浏览器
	 */
	@Test
	public void addConfigTest_ContrlBrower() {
		cb = new ChromeBrower(driverFile);
		
		//1027*768的分辨率启动
		cb.addConfig(ChromeOptionType.CONTRAL_OPEN_BROWER, "127.0.0.1:9222");
		
		cb.getDriver().get("http://www.baidu.com");
	}
	
	/**
	 * 测试在新浏览器中以新标签形式打开新页面，并在基础上再打开一个新标签，加载另一个页面
	 * @throws InterruptedException 
	 */
	@Test
	public void openUrlTest_PageBoolean() throws InterruptedException {
		cb = new ChromeBrower(driverFile);
		
		//不加载图片
		cb.addConfig(ChromeOptionType.DONOT_LOAD_IMAGE);
		//不加载js
		cb.addConfig(ChromeOptionType.DONOT_LOAD_JS);
		
		cb.getDriver();
		
		cb.openUrl(new Page("http://www.baidu.com", "百度"), true);
		Thread.sleep(5000);
		cb.openUrl(new Page("http://www.hao123.com", "hao123"), true);
	}
	
	/**
	 * 测试在新浏览器中以原形式打开新页面，并在基础上在当前标签中，加载另一个页面
	 * @throws InterruptedException 
	 */
	@Test
	public void openUrlTest_StringStringBoolean() throws InterruptedException {
		cb = new ChromeBrower(driverFile);
		
		//不加载图片
		cb.addConfig(ChromeOptionType.DONOT_LOAD_IMAGE);
		//不加载js
		cb.addConfig(ChromeOptionType.DONOT_LOAD_JS);
		
		cb.getDriver();
		
		cb.openUrl("http://www.baidu.com", "百度", false);
		Thread.sleep(5000);
		cb.openUrl("http://www.hao123.com", "hao123", false);
	}
	
	/**
	 * 测试关闭标签，并测试关闭至最后一个标签
	 * @throws InterruptedException 
	 */
	@Test
	public void closeLabelTest() throws InterruptedException {
		cb = new ChromeBrower(driverFile, "https://www.hao123.com", "好123");
		
		//不加载图片
		cb.addConfig(ChromeOptionType.DONOT_LOAD_IMAGE);
		//不加载js
		cb.addConfig(ChromeOptionType.DONOT_LOAD_JS);
		
		cb.getDriver();
		
		cb.openUrl("http://www.baidu.com", "百度", true);
		Thread.sleep(2500);
		cb.closeLabel();
		Thread.sleep(5000);
		cb.closeLabel();
		Thread.sleep(2500);
		cb.openUrl("http://www.hao123.com", "hao123", false);
	}
	
	/**
	 * 测试重新唤起浏览器
	 * @throws InterruptedException 
	 */
	@Test
	public void closeBrowerTest() throws InterruptedException {
		cb = new ChromeBrower(driverFile, "https://www.hao123.com", "好123");
		
		//不加载图片
		cb.addConfig(ChromeOptionType.DONOT_LOAD_IMAGE);
		//不加载js
		cb.addConfig(ChromeOptionType.DONOT_LOAD_JS);
		
		cb.getDriver();
		Thread.sleep(2500);
		cb.closeBrower();
		
		Thread.sleep(2500);
		cb.getDriver();
		cb.openUrl("http://www.baidu.com", "百度", true);
		
		Thread.sleep(2500);
		cb.initialization();
	}
	
	/**
	 * 测试切换页面
	 * @throws InterruptedException 
	 */
	@Test
	public void switchWindowTest() throws InterruptedException {
		Page page = new Page("https://www.hao123.com", "好123");
		cb = new ChromeBrower(driverFile, page);
		
		//不加载图片
		cb.addConfig(ChromeOptionType.DONOT_LOAD_IMAGE);
		//不加载js
		cb.addConfig(ChromeOptionType.DONOT_LOAD_JS);
		
		cb.getDriver();
		cb.openUrl("http://www.baidu.com", "百度", true);
		cb.openUrl("http://www.qq.com", "腾讯", true);
		
		Thread.sleep(2500);
		cb.switchWindow("百度");
		cb.openUrl("http://www.163.com", "网易", false);
		
		Thread.sleep(2500);
		cb.switchWindow(page);
		cb.closeLabel();
	}
}
