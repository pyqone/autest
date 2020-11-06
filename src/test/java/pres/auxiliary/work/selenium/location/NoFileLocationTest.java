package pres.auxiliary.work.selenium.location;

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
	 * 用于测试{@link NoFileLocation#putIframeName(String, String)}方法<br>
	 * 预期：<br>
	 * 
	 */
	@Test 
	public void putIframeNameListTest() {
		test.putIframeName("测试控件8", "窗体1");
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
	
	/**
	 * 用于测试{@link NoFileLocation#putTempletReplaceKey(String, String, String, String)}方法<br>
	 * 预期：<br>
	 * 
	 */
	@Test 
	public void putTempletReplaceKeyTest_NoException() {
		test.putElementLocation("测试控件8", ByType.LINKTEXT, "测试控件");
		test.putElementTempletLocation("测试控件8", ByType.XPATH, "模板1");
		test.putElementTempletLocation("测试控件8", ByType.CLASSNAME, "模板2");
		
		test.putTempletReplaceKey("测试控件8", "模板1", "id", "test");
	}
	
	/**
	 * 用于测试{@link NoFileLocation#putTempletReplaceKey(String, String, String, String)}方法<br>
	 * 预期：<br>
	 * 
	 */
	@Test 
	public void putTempletReplaceKeyTest_Exception() {
		try {
			test.putTempletReplaceKey("测试控件8", "模板1", "id", "test");
		} catch (Exception e) {
			System.out.println("抛出异常,无元素定位方式");
		}
		
		test.putElementLocation("测试控件8", ByType.LINKTEXT, "测试控件");
		test.putElementTempletLocation("测试控件8", ByType.XPATH, "模板1");
		test.putElementTempletLocation("测试控件8", ByType.CLASSNAME, "模板2");
		
		try {
			test.putTempletReplaceKey("测试控件8", "模板3", "id", "test");
		} catch (Exception e) {
			System.out.println("抛出异常,不存在相应的元素定位模板");
		}
	}
	
	/**
	 * 由于返回方法调用{@link JsonLocation}类的返回方法，故统一进行测试
	 */
	@Test
	public void getElementLocationTest() {
		String name = "测试控件8";
		
		//添加定位方式
		test.putElementLocation(name, ByType.LINKTEXT, "测试控件");
		test.putElementTempletLocation(name, ByType.XPATH, "模板1");
		//添加模板
		test.putTemplet("模板1", "//*[text()='${name}']/span[@id=${id}]/span[text()='${key}']");
		//添加定位属性
		test.putTempletReplaceKey(name, "模板1", "id", "test");
		test.putTempletReplaceKey(name, "模板1", "name", "控件8");
		//添加元素
		test.putElementType(name, ElementType.COMMON_ELEMENT);
		//添加元素所在窗体
		test.putIframeName(name, "窗体1");
		//添加元素等待时间
		test.putWaitTime(name, 10);
		
		//添加窗体
		test.putElementLocation("窗体1", ByType.LINKTEXT, "窗体");
		
		//读取元素信息
		System.out.println("元素定位内容：" + test.findValueList(name));
		System.out.println("元素定位方式：" + test.findElementByTypeList(name));
		System.out.println("元素类型：" + test.findElementType(name));
		System.out.println("元素所在窗体：" + test.findIframeNameList(name));
		System.out.println("元素等待时间：" + test.findWaitTime(name));
	}
	
	/**
	 * 测试json改变与不改变时，是否有重新构建读取类对象（需要断点）
	 */
	@Test
	public void changeJsonGetElementTest() {
		test.putElementLocation("测试控件1", ByType.LINKTEXT, "测试控件1");
		test.putElementLocation("测试控件2", ByType.LINKTEXT, "测试控件2");
		
		System.out.println("元素定位内容：" + test.findValueList("测试控件1"));
		System.out.println("元素定位内容：" + test.findValueList("测试控件2"));
	}
}
