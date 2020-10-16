package pres.auxiliary.work.selenium.element;

import java.io.File;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pres.auxiliary.work.selenium.location.AbstractRead;
import pres.auxiliary.work.selenium.location.ReadXml;

/**
 * <p><b>文件名：</b>ElementDataTest.java</p>
 * <p><b>用途：</b>
 * 对{@link ElementData}类进行单元测试
 * </p>
 * <p><b>编码时间：</b>2020年10月12日上午8:47:14</p>
 * <p><b>修改时间：</b>2020年10月12日上午8:47:14</p>
 * @author 彭宇琦
 * @version Ver1.0
 *
 */
public class ElementDataTest {
	AbstractRead ar;
	ElementData test;
	
	final File XML_FILE = new File("src/test/java/pres/auxiliary/work/selenium/element/测试用xml文件.xml");
	
	@BeforeClass
	public void init() {
		ar = new ReadXml(XML_FILE);
	}
	
	@BeforeMethod
	public void addElement() {
		test = new ElementData("XX控件6", ar);
	}
	
	/**
	 * 用于测试{@link ElementData#getName()}方法<br>
	 * 预期：<br>
	 * XX控件6
	 */
	@Test
	public void getNameTest() {
		System.out.println(test.getName());
	}
	
	/**
	 * 用于测试{@link ElementData#getByTypeList()}方法<br>
	 * 预期：<br>
	 * XPATH<br>
	 * CSS<br>
	 * XPATH
	 */
	@Test 
	public void getByTypeListTest() {
		test.getByTypeList().forEach(System.out :: println);
	}
	
	/**
	 * 用于测试{@link ElementData#getValueList()}方法<br>
	 * 预期：<br>
	 * //XXX控件6[@X='XXXX']<br>
	 * http body ${tagName}<br>
	 * //XXX模板控件1[@X='${src}']/div[@name='XXX控件6']/div[@is='test' and text()='${src}']/span[text()='${str2}']/span[id='${aaaa}']
	 */
	@Test 
	public void getValueListTest() {
		test.getValueList().forEach(System.out :: println);
	}
	
	/**
	 * 用于测试{@link ElementData#getElementType()}方法<br>
	 * 预期：<br>
	 * COMMON_ELEMENT
	 */
	@Test 
	public void getElementTypeTest() {
		System.out.println(test.getElementType());
	}
	
	/**
	 * 用于测试{@link ElementData#getIframeNameList()}方法<br>
	 * 预期：<br>
	 * 窗体1<br>
	 * 窗体1.1<br>
	 * 窗体1.1.1
	 */
	@Test 
	public void getIframeNameListTest() {
		test.getIframeNameList().forEach(System.out :: println);
	}
	
	/**
	 * 用于测试{@link ElementData#getWaitTime()}方法<br>
	 * 预期：<br>
	 * -1
	 */
	@Test 
	public void getWaitTimeTest() {
		System.out.println(test.getWaitTime());
	}
	
	/**
	 * 用于测试{@link ElementData#addLinkWord(String...)}方法<br>
	 * 预期：<br>
	 * //XXX控件6[@X='XXXX']<br>
	 * http body 外链1<br>
	 * //XXX模板控件1[@X='外链1']/div[@name='XXX控件6']/div[@is='test' and text()='外链2']/span[text()='外链3']/span[id='${aaaa}']
	 */
	@Test 
	public void addLinkWordTest() {
		test.addLinkWord("外链1", "外链2", "外链3");
		test.getValueList().forEach(System.out :: println);
	}
}
