package pres.auxiliary.work.selenium.location;

import java.io.File;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * <p><b>文件名：</b>ReadXmlTest.java</p>
 * <p><b>用途：</b>用于对{@link ReadXml}类进行测试</p>
 * <p><b>编码时间：</b>2020年10月8日 下午11:19:40</p>
 * <p><b>修改时间：</b>2020年10月8日 下午11:19:40</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 */
public class ReadXmlTest {
	private final File XML_FILE = new File("src/test/java/pres/auxiliary/work/selenium/location/测试用xml文件.xml");
	/**
	 * 定义类
	 */
	ReadXml rx;
	
	/**
	 * 初始化数据
	 */
	@BeforeClass
	public void init() {
		rx = new ReadXml(XML_FILE);
	}
	
	/**
	 * 测试{@link ReadXml#getElementByTypeList(String)}方法
	 * 预期：
	 * XPATH
	 * CSS
	 * XPATH
	 */
	@Test
	public void getElementByTypeListTest() {
		rx.getElementByTypeList("XX控件11").forEach(System.out :: println);
	}
	
	/**
	 * 测试{@link ReadXml#getElementType(String)}方法
	 * 预期：
	 * COMMON_ELEMENT
	 * COMMON_ELEMENT
	 * DATA_LIST_ELEMENT
	 * SELECT_DATAS_ELEMENT
	 * SELECT_OPTION_ELEMENT
	 * IFRAME_ELEMENT
	 */
	@Test
	public void getElementTypeTest() {
		System.out.println(rx.getElementType("XX控件11"));
		System.out.println(rx.getElementType("XX控件16"));
		System.out.println(rx.getElementType("XX控件17"));
		System.out.println(rx.getElementType("XX控件18"));
		System.out.println(rx.getElementType("XX控件19"));
		System.out.println(rx.getElementType("窗体3"));
	}
	
	/**
	 * 测试{@link ReadXml#getIframeNameList(String)}方法
	 * 预期：
	 * 窗体1
	 * 窗体1.1
	 * 窗体1.1.1
	 */
	@Test
	public void getIframeNameListTest() {
		rx.getIframeNameList("XX控件6").forEach(System.out :: println);
	}
	
	/**
	 * 测试{@link ReadXml#getWaitTime(String)}方法
	 * 预期：
	 * -1
	 * 100
	 * 1
	 * 0
	 * -1
	 * -1
	 */
	@Test
	public void getWaitTimeTest() {
		System.out.println(rx.getWaitTime("XX控件14"));
		System.out.println(rx.getWaitTime("XX控件15"));
		System.out.println(rx.getWaitTime("XX控件16"));
		System.out.println(rx.getWaitTime("XX控件17"));
		System.out.println(rx.getWaitTime("XX控件18"));
		System.out.println(rx.getWaitTime("XX控件19"));
	}
}
