package pres.auxiliary.work.selenium.event;

import java.io.File;
import java.lang.reflect.Method;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;

import pres.auxiliary.work.selenium.brower.ChromeBrower;
import pres.auxiliary.work.selenium.brower.ChromeBrower.ChromeOptionType;

/**
 * <p><b>文件名：</b>JsEventTest.java</p>
 * <p><b>用途：</b>
 * 用于对{@link JsEvent}类进行单元测试，使用控制已打开的浏览器
 * </p>
 * <p><b>页面：</b>
 * https://www.baidu.com/（百度首页），针对搜索条件文本框
 * </p>
 * <p><b>编码时间：</b>2020年5月17日 下午1:40:28</p>
 * <p><b>修改时间：</b>2020年5月17日 下午1:40:28</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 */
public class JsEventTest {
	/**
	 * 输入文本框元素对象
	 */
	WebElement inputElemnt;
	JsEvent event;
	ChromeBrower cb;
	
	/**
	 * 初始化数据
	 */
	@BeforeClass
	public void init() {
		cb = new ChromeBrower(new File("Resource/BrowersDriver/Chrom/80.0.3987.163/chromedriver.exe"));
		cb.addConfig(ChromeOptionType.CONTRAL_OPEN_BROWER, "127.0.0.1:9222");
		
		//获取输入文本框
		inputElemnt = cb.getDriver().findElement(By.xpath("//*[@id='kw']"));
		//初始化js类
		event = new JsEvent(cb.getDriver());
	}
	
	@AfterClass
	public void quit() {
		cb.getDriver().quit();
	}
	
	@BeforeMethod
	public void show(Method method) {
		System.out.println("================================");
		System.out.println(method.getName() + "方法测试结果：");
	}
	
	/**
	 * 测试{@link JsEvent#getAttribute(WebElement, String)}方法
	 */
	@Test
	public void getAttributeTest() {
		System.out.println(event.getAttribute(inputElemnt, "class"));
	}
	
	/**
	 * 测试{@link JsEvent#putAttribute(WebElement, String, String)}方法
	 */
	@Test
	public void putAttributeTest() {
		System.out.println(event.putAttribute(inputElemnt, "lll", null));
	}
	
	/**
	 * 测试{@link JsEvent#addElement(WebElement, String)}方法
	 */
	@Test
	public void addElementTest_String() {
		System.out.println(event.addElement(inputElemnt, "bbb"));
	}
	
	/**
	 * 测试{@link JsEvent#deleteElement(WebElement)}方法
	 */
	@Test
	public void deleteElementTest() {
		System.out.println(event.deleteElement(inputElemnt));
	}
	
	/**
	 * 测试{@link JsEvent#addElement(WebElement, com.alibaba.fastjson.JSONObject)}方法
	 */
	@Test
	public void addElementTest_Json() {
		JSONObject json = event.deleteElement(cb.getDriver().findElement(By.xpath("//*[@value = '百度一下']")));
		WebElement e = cb.getDriver().findElement(By.xpath("//*[text() = '百度热榜']"));
		System.out.println(event.addElement(e, json));
	}
}
