package pres.auxiliary.tool.http;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class EasyHttpTest {
	EasyHttp eh = new EasyHttp();
	
	@AfterMethod
	public void showUrl() {
		System.out.println(eh.getUrlString());
	}
	
	@Test
	public void urlTest_All() {
		eh.url("http://127.0.0.1:8080/a/b");
	}
	
	@Test
	public void urlTest_NotAgreement() {
		eh.url("127.0.0.1:8080/a/b");
	}
	
	@Test
	public void urlTest_NotPort() {
		eh.url("http://127.0.0.1/a/b");
	}
	
	@Test
	public void urlTest_NotAddress() {
		eh.url("http://127.0.0.1:8080");
	}
	
	@Test
	public void urlTest_Host() {
		eh.url("http://www.hao123.com/a/b");
	}
}
