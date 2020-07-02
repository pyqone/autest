package pres.auxiliary.work.selenium.xml;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ReadXmlTest {
ReadXml r;
	
	/**
	 * 初始化数据
	 */
	@BeforeClass
	public void newReadXML() {
		r = new ReadXml(new File("src/test/java/pres/auxiliary/work/selenium/xml/测试用xml文件.xml"));
	}
	
	@AfterMethod
	public void over(Method method) {
		System.out.println("*****" + method.getName() + "运行完毕" + "*****");
	}
	
	/**
	 * 用于测试{@link ReadXml#getBy(String, ByType)}方法，获取普通元素
	 */
	@Test
	public void getByTest_Element() {
		System.out.println(r.getBy("XX控件7", ByType.XPATH));
	}
	
	/**
	 * 用于测试{@link ReadXml#getBy(String, ByType)}方法，获取窗体元素
	 */
	@Test
	public void getByTest_Iframe() {
		System.out.println(r.getBy("窗体1.1", ByType.XPATH));
	}
	
	/**
	 * 用于测试{@link ReadXml#getBy(String, ByType)}方法，获取模板元素
	 */
	@Test
	public void getByTest_Templet() {
		System.out.println(r.getBy("XX控件11", ByType.XPATH));
		System.out.println(r.getBy("窗体1", ByType.CSS));
	}
	
	/**
	 * 用于测试{@link ReadXml#getBy(String, ByType)}方法，获取顶层元素
	 */
	@Test
	public void getByTest_RootElement() {
		System.out.println(r.getBy("XX控件1", ByType.XPATH));
	}
	
	/**
	 * 用于测试{@link ReadXml#getValue(String, ByType)}方法，获取普通元素
	 */
	@Test
	public void getElementValueTest_Element() {
		System.out.println(r.getValue("XX控件7", ByType.XPATH));
	}
	
	/**
	 * 用于测试{@link ReadXml#getValue(String, ByType)}方法，获取窗体元素
	 */
	@Test
	public void getElementValueTest_Iframe() {
		System.out.println(r.getValue("窗体1.1", ByType.XPATH));
	}
	
	/**
	 * 用于测试{@link ReadXml#getValue(String, ByType)}方法，获取模板元素
	 */
	@Test
	public void getElementValueTest_Templet() {
		System.out.println(r.getValue("XX控件11", ByType.XPATH));
		System.out.println(r.getValue("窗体1", ByType.CSS));
	}
	
	/**
	 * 用于测试{@link ReadXml#getValue(String, ByType)}方法，获取顶层元素
	 */
	@Test
	public void getElementValueTest_RootElement() {
		System.out.println(r.getValue("XX控件1", ByType.XPATH));
	}
	
	/**
	 * 用于测试{@link ReadXml#getIframeName(String, ByType)}方法，获取普通元素
	 */
	@Test
	public void getIframeNameTest_Element() {
		System.out.println(r.getIframeName("XX控件7"));
	}
	
	/**
	 * 用于测试{@link ReadXml#getIframeName(String, ByType)}方法，获取窗体元素
	 */
	@Test
	public void getIframeNameTest_Iframe() {
		System.out.println(r.getIframeName("窗体1.1"));
	}
	
	/**
	 * 用于测试{@link ReadXml#getIframeName(String, ByType)}方法，获取模板元素
	 */
	@Test
	public void getIframeNameTest_Templet() {
		System.out.println(r.getIframeName("XX控件11"));
		System.out.println(r.getIframeName("窗体1"));
	}
	
	/**
	 * 用于测试{@link ReadXml#getIframeName(String)}方法，获取顶层元素
	 */
	@Test
	public void getIframeNameTest_RootElement() {
		System.out.println(r.getIframeName("XX控件1"));
	}
	
	/**
	 * 用于测试{@link ReadXml#getIframeName(String)}方法，获取模板元素
	 */
	@Test
	public void getIframeNameTest_NoPram() {
		System.out.println(r.getIframeName("XX控件12"));
	}
	
	/**
	 * 用于测试{@link ReadXml#getValue(String, ByType)}方法，未查找到替换的属性
	 */
	@Test
	public void getValueTest_NoPram() {
		System.out.println(r.getValue("XX控件12", ByType.XPATH));
		System.out.println(r.getValue("窗体3", ByType.XPATH));
	}
	
	/**
	 * 用于测试{@link ReadXml#getValue(String, ByType)}方法，外链关键词
	 */
	@Test
	public void getValueTest_Link() {
		ArrayList<String> link = new ArrayList<>();
		link.add("测试1");
		link.add("测试2");
		link.add("测试3");
		
		//XXX模板控件1[@X='${src}']/div[@name='${name}']
		System.out.println(r.getValue("XX控件13", ByType.XPATH, link));
		System.out.println(r.getValue("窗体3", ByType.XPATH, link));
		//XXX模板控件1[@X='${src}']/div[@name='${name}']/div[@is='${str1}' and text()='${str1}']
		System.out.println(r.getValue("XX控件14", ByType.XPATH, link));
		//XXX模板控件1[@X='${src}']/div[@name='${name}']/div[@is='${str1}' and text()='${src}']/span[text()='${str2}']/span[id='${aaaa}']
		System.out.println(r.getValue("XX控件15", ByType.XPATH, link));
	}
}
