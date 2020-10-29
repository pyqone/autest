package pres.auxiliary.work.selenium.location;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;

/**
 * <p><b>文件名：</b>JsonLocationTest.java</p>
 * <p><b>用途：</b>
 * 对{@link JsonLocation}类进行单元测试
 * </p>
 * <p><b>编码时间：</b>2020年10月28日上午7:55:07</p>
 * <p><b>修改时间：</b>2020年10月28日上午7:55:07</p>
 * @author 彭宇琦
 * @version Ver1.0
 *
 */
public class JsonLocationTest {
	private final File JSON_FILE = new File("src/test/java/pres/auxiliary/work/selenium/location/测试用json文件.txt");
	/**
	 * 定义类
	 */
	JsonLocation test;
	
	/**
	 * 初始化数据
	 */
	@BeforeClass
	public void init() {
		test = new JsonLocation(JSON_FILE);
	}
	
	/**
	 * 测试读取文件中的json，并测试其是否能被转换为json
	 */
	@Test
	public void readJson() {
		StringBuilder jsonText = new StringBuilder();
		try(BufferedReader br = new BufferedReader(new FileReader(JSON_FILE))) {
			String text = "";
			while((text = br.readLine()) != null) {
				jsonText.append(text);
			}
		} catch (Exception e) {
		}
		
//		System.out.println(jsonText.toString());
		JSONObject json = JSONObject.parseObject(jsonText.toString());
		System.out.println(json.toJSONString());
		
//		System.out.println(new JSONObject().isEmpty());
		System.out.println(json.getJSONArray("test"));
	}
	
	/**
	 * 用于测试{@link JsonLocation#findElementByTypeList(String)}方法<br>
	 * 预期：<br>
	 * xpath
	 * css
	 * xpath
	 * id
	 */
	@Test 
	public void findElementByTypeListTest() {
		test.findElementByTypeList("XX控件5").forEach(System.out :: println);
	}
	
	/**
	 * 用于测试{@link JsonLocation#findValueList(String)}方法，不抛出异常<br>
	 * 预期：<br>
	 * //XXX模板控件1[@X='${name}']/div/div[@src='Test']/input
	 * http body div
	 * //XXX模板控件1[@X='${src}']/div[@name='${name}']
	 * 测试控件55555
	 */
	@Test 
	public void findValueListTest_NotException() {
		test.findValueList("XX控件5").forEach(System.out :: println);
	}
	
	/**
	 * 用于测试{@link JsonLocation#findValueList(String)}方法，抛出异常<br>
	 * 预期：<br>
	 * 通过
	 */
	@Test (expectedExceptions = UndefinedElementException.class)
	public void findValueListTest_Exception() {
		test.findValueList("XX控件20").forEach(System.out :: println);
	}
	
	/**
	 * 用于测试{@link JsonLocation#findElementType(String)}方法<br>
	 * 预期：<br>
	 * COMMON_ELEMENT
	 * DATA_LIST_ELEMENT
	 * SELECT_DATAS_ELEMENT
	 * COMMON_ELEMENT
	 * COMMON_ELEMENT
	 * SELECT_OPTION_ELEMENT
	 */
	@Test 
	public void findElementTypeTest() {
		System.out.println(test.findElementType("XX控件16"));
		System.out.println(test.findElementType("XX控件17"));
		System.out.println(test.findElementType("XX控件18"));
		System.out.println(test.findElementType("XX控件19"));
		System.out.println(test.findElementType("XX控件21"));
		System.out.println(test.findElementType("XX控件20"));
	}
	
	/**
	 * 用于测试{@link JsonLocation#findWaitTime(String)}方法<br>
	 * 预期：<br>
	 * 1
	 * 0
	 * -1
	 * -1
	 * -1
	 * -1
	 */
	@Test 
	public void findWaitTimeTest() {
		System.out.println(test.findWaitTime("XX控件16"));
		System.out.println(test.findWaitTime("XX控件17"));
		System.out.println(test.findWaitTime("XX控件18"));
		System.out.println(test.findWaitTime("XX控件19"));
		System.out.println(test.findWaitTime("XX控件21"));
		System.out.println(test.findWaitTime("XX控件20"));
	}
	
	/**
	 * 用于测试{@link JsonLocation#findIframeNameList(String)}方法<br>
	 * 预期：<br>
	 * ["窗体1", "窗体1.1"]
	 * ["窗体1", "窗体1.1", "窗体1.1.1"]
	 * 
	 */
	@Test 
	public void findIframeNameListTest_NotException() {
		System.out.println(test.findIframeNameList("XX控件3"));
		System.out.println(test.findIframeNameList("XX控件3.1"));
	}
	
	/**
	 * 用于测试{@link JsonLocation#findIframeNameList(String)}方法<br>
	 * 预期：<br>
	 * 显示无元素信息异常
	 */
	@Test 
	public void findIframeNameListTest_Exception() {
		try {
			System.out.println(test.findIframeNameList("XX控件3.2"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
