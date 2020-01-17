package pres.auxiliary.selenium.xml.io;

import java.io.File;
import java.lang.reflect.Method;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pres.auxiliary.selenium.xml.PosMode;
import pres.auxiliary.selenium.xml.ReadXml;

/**
 * <p><b>文件名：</b>TestReadXml.java</p>
 * <p><b>用途：</b>用于测试ReadXml类</p>
 * <p><b>编码时间：</b>2019年10月25日下午3:14:25</p>
 * <p><b>修改时间：</b>2019年10月25日下午3:14:25</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public class TestReadXml {
	ReadXml r;
	
	/**
	 * 初始化数据
	 */
	@BeforeClass
	public void newReadXML() {
		r = new ReadXml(new File("src/test/java/pres/auxiliary/selenium/xml/io/测试用xml文件.xml"));
	}
	
	@AfterMethod
	public void over(Method method) {
		System.out.println("*".repeat(5) + method.getName() + "运行完毕" + "*".repeat(5));
	}
	
	/**
	 * 用于测试{@link ReadXml#getBy(String, PosMode)}方法，获取普通元素
	 */
	@Test
	public void getByTest_Element() {
		System.out.println(r.getBy("XX控件7", PosMode.XPATH));
	}
	
	/**
	 * 用于测试{@link ReadXml#getBy(String, PosMode)}方法，获取窗体元素
	 */
	@Test
	public void getByTest_Iframe() {
		System.out.println(r.getBy("窗体1.1", PosMode.XPATH));
	}
	
	/**
	 * 用于测试{@link ReadXml#getBy(String, PosMode)}方法，获取模板元素
	 */
	@Test
	public void getByTest_Templet() {
		System.out.println(r.getBy("XX控件11", PosMode.XPATH));
		System.out.println(r.getBy("窗体1", PosMode.CSS));
	}
	
	/**
	 * 用于测试{@link ReadXml#getBy(String, PosMode)}方法，获取顶层元素
	 */
	@Test
	public void getByTest_RootElement() {
		System.out.println(r.getBy("XX控件1", PosMode.XPATH));
	}
	
	/**
	 * 用于测试{@link ReadXml#getValue(String, PosMode)}方法，获取普通元素
	 */
	@Test
	public void getElementValueTest_Element() {
		System.out.println(r.getValue("XX控件7", PosMode.XPATH));
	}
	
	/**
	 * 用于测试{@link ReadXml#getValue(String, PosMode)}方法，获取窗体元素
	 */
	@Test
	public void getElementValueTest_Iframe() {
		System.out.println(r.getValue("窗体1.1", PosMode.XPATH));
	}
	
	/**
	 * 用于测试{@link ReadXml#getValue(String, PosMode)}方法，获取模板元素
	 */
	@Test
	public void getElementValueTest_Templet() {
		System.out.println(r.getValue("XX控件11", PosMode.XPATH));
		System.out.println(r.getValue("窗体1", PosMode.CSS));
	}
	
	/**
	 * 用于测试{@link ReadXml#getValue(String, PosMode)}方法，获取顶层元素
	 */
	@Test
	public void getElementValueTest_RootElement() {
		System.out.println(r.getValue("XX控件1", PosMode.XPATH));
	}
	
	/**
	 * 用于测试{@link ReadXml#getIframeName(String, PosMode)}方法，获取普通元素
	 */
	@Test
	public void getIframeNameTest_Element() {
		System.out.println(r.getIframeName("XX控件7"));
	}
	
	/**
	 * 用于测试{@link ReadXml#getIframeName(String, PosMode)}方法，获取窗体元素
	 */
	@Test
	public void getIframeNameTest_Iframe() {
		System.out.println(r.getIframeName("窗体1.1"));
	}
	
	/**
	 * 用于测试{@link ReadXml#getIframeName(String, PosMode)}方法，获取模板元素
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
}
