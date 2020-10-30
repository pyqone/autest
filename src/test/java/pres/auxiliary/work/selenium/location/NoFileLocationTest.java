package pres.auxiliary.work.selenium.location;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pres.auxiliary.work.selenium.element.ElementType;

public class NoFileLocationTest {
	/**
	 * 定义类
	 */
	NoFileLocation test;
	
	/**
	 * 初始化数据
	 */
	@BeforeClass
	public void init() {
		test = new NoFileLocation();
	}
	
	@AfterClass
	public void showData() {
		System.out.println(test.getJson());
	}
	
	/**
	 * 用于测试{@link NoFileLocation#putElementLocation(String, ByType, String)}方法<br>
	 */
	@Test 
	public void putElementLocationTest() {
		test.putElementLocation("测试控件1", ByType.NAME, "测试控件11111");
		test.putElementLocation("测试控件2", ByType.CLASSNAME, "test_class");
		test.putElementLocation("测试控件3", ByType.CSS, "html body div");
		test.putElementLocation("测试控件4", ByType.ID, "test");
		test.putElementLocation("测试控件5", ByType.LINKTEXT, "测试控件");
		test.putElementLocation("测试控件6", ByType.TAGNAME, "span");
		test.putElementLocation("测试控件7", ByType.XPATH, "//*[text()='测试控件7']");
		test.putElementLocation("测试控件7", ByType.ID, "test7");
		test.putElementLocation("测试控件8", ByType.LINKTEXT, "测试控件");
	}
	
	/**
	 * 用于测试{@link NoFileLocation#putElementTempletLocation(String, ByType, String)}方法<br>
	 */
	@Test 
	public void putElementTempletLocationTest() {
		test.putElementTempletLocation("测试控件8", ByType.XPATH, "模板1");
		test.putElementTempletLocation("测试控件8", ByType.CLASSNAME, "模板2");
	}
	
	/**
	 * 用于测试{@link NoFileLocation#putElementType(String, pres.auxiliary.work.selenium.element.ElementType)}方法<br>
	 * 预期：<br>
	 * 
	 */
	@Test 
	public void putElementTypeTest() {
		test.putElementType("测试控件8", ElementType.COMMON_ELEMENT);
	}
	
	/**
	 * 用于测试{@link NoFileLocation#putIframeNameList(String, String)}方法<br>
	 * 预期：<br>
	 * 
	 */
	@Test 
	public void putIframeNameListTest() {
		test.putIframeNameList("测试控件8", "窗体1");
	}
	
	/**
	 * 用于测试{@link NoFileLocation#putWaitTime(String, long)}方法<br>
	 * 预期：<br>
	 * 
	 */
	@Test 
	public void putWaitTimeTest() {
		test.putWaitTime("测试控件8", 10);
	}
	
	/**
	 * 用于测试{@link NoFileLocation#putTemplet(String, String)}方法<br>
	 * 预期：<br>
	 * 
	 */
	@Test 
	public void putTempletTest() {
		test.putTemplet("模板1", "//*[text()='${name}']");
	}
}
