package pres.auxiliary.tool.http;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.ClientProtocolException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import pres.auxiliary.tool.date.Time;

/**
 * <p><b>文件名：</b>EasyHttpTest.java</p>
 * <p><b>用途：</b>
 * 对{@link EasyHttp}类进行测试
 * </p>
 * <p><b>编码时间：</b>2020年6月26日下午8:45:49</p>
 * <p><b>修改时间：</b>2020年6月26日下午8:45:49</p>
 * @author 
 * @version Ver1.0
 *
 */
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
		eh.port(55663);
	}
	
	/**
	 * 测试{@link EasyHttp#putParameter(String, String)}方法
	 */
	@Test 
	public void putParameterTest_StringString() {
		eh.putParameter("A", "a");
		eh.putParameter("B", "b");
	}
	
	/**
	 * 测试{@link EasyHttp#putParameter(String)}方法
	 */
	@Test 
	public void putParameterTest_String() {
		eh.putParameter("C=c");
		eh.putParameter("D=d");
	}
	
	/**
	 * 测试{@link EasyHttp#clearParameter()}方法
	 */
	@Test
	public void clearParameterTest() {
		eh.putParameter("C=c");
		eh.clearParameter();
	}
	
	/**
	 * 测试{@link EasyHttp#response()}方法
	 * @throws IOException 
	 * @throws URISyntaxException 
	 * @throws ClientProtocolException 
	 */
	@Test 
	public void responseTest() throws ClientProtocolException, URISyntaxException, IOException {
		String time = new Time().getFormatTime();
		String apiKey = "F1047305D50145159C13CB12C200E931";
		String clientSerial = "FT00000003";
		String apiSecret = "921DE23A2D434C8B9919EBC21E8DEFDC";
		String body = "{}";
		String md5string = apiKey+"api_key"+apiKey+"api_version1.0"+"body"+body+"client_serial"+clientSerial+"timestamp"+time+apiSecret;
		String signature = DigestUtils.md5Hex(md5string).toUpperCase();
		
		String responseText = eh.host("10.19.27.1").port(7013).address("/CWRService/KeepAlive")
			.putParameter("api_version", "1.0")
			.putParameter("timestamp", "time")
			.putParameter("client_serial", clientSerial)
			.putParameter("signature", signature)
			.putParameter("api_key", apiKey)
			.putHead(HeadType.CONTENT_TYPE_JSON.setEncoding("UTF-8")).response().getFormatResponseText();
			
		System.out.println("-----------------------------------");
		System.out.println(responseText);
		System.out.println("-----------------------------------");
	}
}
