package pres.auxiliary.tool.http;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class EasyHttpTest {
	EasyHttp eh = new EasyHttp();
	
	/**
	 * 显示url
	 */
	@AfterMethod
	public void showUrl() {
		System.out.println(eh.getUrlString());
	}
	
	/**
	 * 测试{@link EasyHttp#url(String)}方法，地址全面
	 */
	@Test
	public void urlTest_All() {
		eh.url("http://127.0.0.1:8080/a/b?a=1&w=2 5");
	}
	
	/**
	 * 测试{@link EasyHttp#url(String)}方法，无协议
	 */
	@Test
	public void urlTest_NotAgreement() {
		eh.url("127.0.0.1:8080/a/b");
	}
	
	/**
	 * 测试{@link EasyHttp#url(String)}方法，无端口
	 */
	@Test
	public void urlTest_NotPort() {
		eh.url("http://127.0.0.1/a/b");
	}
	
	/**
	 * 测试{@link EasyHttp#url(String)}方法，无请求路径
	 */
	@Test
	public void urlTest_NotAddress() {
		eh.url("http://127.0.0.1:8080");
	}
	
	/**
	 * 测试{@link EasyHttp#url(String)}方法，域名
	 */
	@Test
	public void urlTest_Host() {
		eh.url("http://www.hao123.com/a/b");
	}
	
	/**
	 * 测试{@link EasyHttp#agreement(String)}方法
	 */
	@Test
	public void agreementTest() {
		eh.agreement("ftp://"); 
	}
	
	/**
	 * 测试不做任何参数处理时的返回
	 */
	@Test
	public void notSetTest() {
	}
	
	/**
	 * 测试{@link EasyHttp#host(String)}方法
	 */
	@Test
	public void hostTest() {
		eh.host("10.125.163.144");
	}
	
	/**
	 * 测试{@link EasyHttp#port(String)}方法
	 */
	@Test
	public void portTest() {
		eh.port("55663");
	}
}
