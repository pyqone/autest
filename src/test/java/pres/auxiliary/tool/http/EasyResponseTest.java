package pres.auxiliary.tool.http;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;

/**
 * <p><b>文件名：</b>EasyResponseTest.java</p>
 * <p><b>用途：</b>
 * 用于对{@link EasyResponse}类进行测试
 * </p>
 * <p><b>编码时间：</b>2020年6月26日下午8:47:16</p>
 * <p><b>修改时间：</b>2020年6月26日下午8:47:16</p>
 * @author 
 * @version Ver1.0
 *
 */
public class EasyResponseTest {
	EasyResponse re;
	
	@BeforeMethod
	public void showMethodName(Method m) {
		System.out.println("---------------------------");
		System.out.println("运行" + m.getName() + "方法：");
	}
	
	/**
	 * 以格式化的形式输出响应参数，测试{@link EasyResponse#getFormatResponseText()}方法
	 */
	@AfterMethod
	public void showFormatResponse() {
		System.out.println("***************************");
		System.out.println(re.getFormatResponseText());
		System.out.println("---------------------------");
	}
	
	/**
	 * 测试{@link EasyResponse#getResponseText()}方法
	 */
	@Test
	public void getResponseTextTest() {
		re = new EasyResponse("success");
		System.out.println(re.getResponseText());
	} 
	
	/**
	 * 测试{@link EasyResponse#getResponseText()}方法
	 */
	@Test
	public void getResponseJsonTest() {
		re = new EasyResponse("{\"result\":\"false\",\"code\":\"29\",\"status\":203,\"message\":\"Invalid Api Key\",\"detail_message\":\"由服务器端分配给手机端\",\"result_data\":{}}");
		JSONObject json = re.getResponseJson();
		System.out.println(json.get("result"));
	} 
	
	/**
	 * 测试{@link EasyResponse#getResponseText()}方法
	 */
	@Test
	public void getHtmlDocumentTest() {
		re = new EasyResponse("<html><head></head><body><div id='div'><label>请输入：</label><input id='input' /></div></body></html>");
		org.jsoup.nodes.Document dom = re.getHtmlDocument();
		System.out.println(dom.getElementById("div").tagName());
	} 
	
	/**
	 * 测试{@link EasyResponse#getResponseText()}方法
	 */
	@Test
	public void getXmlDocumentTest() {
		re = new EasyResponse("<?xml version=\"1.0\" encoding=\"UTF-8\"?><project name=\"\"><templet><xpath id='1'>//XXX模板控件1[@X='${name}']/div/div[@${att}='${id}']/input</xpath><css id='2'>http body ${tagName}</css></templet></project>");
		org.dom4j.Document dom = re.getXmlDocument();
		System.out.println(dom.getRootElement().getName());
	} 
	
	/**
	 * 测试{@link EasyResponse#responseToFile()}方法
	 * @throws IOException 
	 */
	@Test
	public void responseToFileTest() throws IOException {
		re = new EasyResponse("<?xml version=\"1.0\" encoding=\"UTF-8\"?><project name=\"\"><templet><xpath id='1'>//XXX模板控件1[@X='${name}']/div/div[@${att}='${id}']/input</xpath><css id='2'>http body ${tagName}</css></templet></project>");
		File file = new File("D:\\8.test\\EasyResponse\\EasyResponseTest.txt");
		re.responseToFile(file, true);
		
		java.awt.Desktop.getDesktop().open(file.getParentFile());
	}
}
