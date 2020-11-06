package pres.auxiliary.work.selenium.location;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TextLocationTest {
	TextLocation test;
	
	@BeforeClass
	public void init() {
		String text = "测试控件1==xpath==//*[@text()='测试1']==10==0\n" + 
				"窗体1==css==html body div==2\n" + 
				"	测试控件2==id==test==11\n" + 
				"	窗体1.1==name==tiframe==dfsg==dsfg\n" + 
				"		测试控件3==classname==textclass==sdf==1\n" + 
				"		窗体1.1.1==name==窗体1.1.1\n" + 
				"			测试控件9==xpath==//span[@name='${name}']//div[@id='9']\n" + 
				"			测试控件10==xpath==//span[@name='${name}']//div[@id='10']\n" + 
				"	测试控件4==id==4 sfda\n" + 
				"	窗体1.2==linktext==窗体==fsa\n" + 
				"		测试控件5==tagname==span\n" + 
				"	测试控件6==xpath==//span[@name='${name}']\n" + 
				"测试控件7==xpath==//span[@name='${name}']//div[@id='${id}']\n" + 
				"窗体2==id==2\n" + 
				"	测试控件8==xpath==//span[@name='${name}']//div[@id='${id}']";
		test = new TextLocation(text);
	}
	
	/**
	 * 用于测试{@link TextLocation#findElementByTypeList(String)}方法<br>
	 * 预期：<br>
	 * XPATH
	 * CSS
	 * ID
	 * NAME
	 * CLASSNAME
	 */
	@Test 
	public void findElementByTypeListTest() {
		System.out.println(test.findElementByTypeList("测试控件1"));
		System.out.println(test.findElementByTypeList("窗体1"));
		System.out.println(test.findElementByTypeList("测试控件2"));
		System.out.println(test.findElementByTypeList("窗体1.1"));
		System.out.println(test.findElementByTypeList("测试控件3"));
	}
	
	/**
	 * 用于测试{@link TextLocation#findValueList(String)}方法<br>
	 * 预期：<br>
	 * //*[@text()='测试1']
	 * css html body div
	 * test
	 * tiframe
	 * textclass
	 */
	@Test 
	public void findValueListTest() {
		System.out.println(test.findValueList("测试控件1"));
		System.out.println(test.findValueList("窗体1"));
		System.out.println(test.findValueList("测试控件2"));
		System.out.println(test.findValueList("窗体1.1"));
		System.out.println(test.findValueList("测试控件3"));
	}
	
	/**
	 * 用于测试{@link TextLocation#findElementType(String)}方法<br>
	 * 预期：<br>
	 * COMMON_ELEMENT
	 * COMMON_ELEMENT
	 * COMMON_ELEMENT
	 * COMMON_ELEMENT
	 * DATA_LIST_ELEMENT
	 */
	@Test 
	public void findElementTypeTest() {
		System.out.println(test.findElementType("测试控件1"));
		System.out.println(test.findElementType("窗体1"));
		System.out.println(test.findElementType("测试控件2"));
		System.out.println(test.findElementType("窗体1.1"));
		System.out.println(test.findElementType("测试控件3"));
	}
	
	/**
	 * 用于测试{@link TextLocation#findWaitTime(String)}方法<br>
	 * 预期：<br>
	 * 10
	 * 2
	 * 11
	 * -1
	 * -1
	 */
	@Test 
	public void findWaitTimeTest() {
		System.out.println(test.findWaitTime("测试控件1"));
		System.out.println(test.findWaitTime("窗体1"));
		System.out.println(test.findWaitTime("测试控件2"));
		System.out.println(test.findWaitTime("窗体1.1"));
		System.out.println(test.findWaitTime("测试控件3"));
	}
	
	/**
	 * 用于测试{@link TextLocation#findIframeNameList(String)}方法<br>
	 * 预期：<br>
	 * []
	 * []
	 * [窗体1]
	 * [窗体1]
	 * [窗体1, 窗体1.1]
	 * [窗体1, 窗体1.1, 窗体1.1.1]
	 * [窗体1, 窗体1.1, 窗体1.1.1]
	 * [窗体1]
	 * [窗体1, 窗体1.2]
	 * [窗体1]
	 * []
	 * [窗体2]
	 */
	@Test 
	public void findIframeNameListTest() {
		System.out.println(test.findIframeNameList("测试控件1"));
		System.out.println(test.findIframeNameList("窗体1"));
		System.out.println(test.findIframeNameList("测试控件2"));
		System.out.println(test.findIframeNameList("窗体1.1"));
		System.out.println(test.findIframeNameList("测试控件3"));
		System.out.println(test.findIframeNameList("测试控件9"));
		System.out.println(test.findIframeNameList("测试控件10"));
		System.out.println(test.findIframeNameList("测试控件4"));
		System.out.println(test.findIframeNameList("测试控件5"));
		System.out.println(test.findIframeNameList("测试控件6"));
		System.out.println(test.findIframeNameList("测试控件7"));
		System.out.println(test.findIframeNameList("测试控件8"));
	}
	
	/**
	 * 测试修改分隔符后是否能成功切分元素信息及层级信息
	 */
	@Test
	public void changeSplitSign() {
		String text = "窗体1qqxpathqq//*[@text()='测试1']qq10qq0\n" + 
				"QQ窗体2qqxpathqq//*[@text()='测试1']qq10qq0\n" + 
				"QQQQ测试控件1qqcssqqhtml body divqq2\n";
		
		TextLocation textL = new TextLocation(text, "qq", "QQ");
		System.out.println("窗体1等待时间：" + textL.findWaitTime("窗体1"));
		System.out.println("窗体1定位方式：" + textL.findElementByTypeList("窗体1"));
		System.out.println("窗体1元素类型：" + textL.findElementType("窗体1"));
		System.out.println("窗体1父层窗体：" + textL.findIframeNameList("窗体1"));
		System.out.println("窗体1定位内容" + textL.findValueList("窗体1"));
		
		System.out.println("测试控件1等待时间：" + textL.findWaitTime("测试控件1"));
		System.out.println("测试控件1定位方式：" + textL.findElementByTypeList("测试控件1"));
		System.out.println("测试控件1元素类型：" + textL.findElementType("测试控件1"));
		System.out.println("测试控件1父层窗体：" + textL.findIframeNameList("测试控件1"));
		System.out.println("测试控件1定位内容" + textL.findValueList("测试控件1"));
		
	}
}
