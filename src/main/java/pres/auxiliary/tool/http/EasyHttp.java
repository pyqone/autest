package pres.auxiliary.tool.http;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * <p><b>文件名：</b>EasyHttp.java</p>
 * <p><b>用途：</b>
 * 可用于进行简单的接口请求
 * </p>
 * <p><b>编码时间：</b>2020年6月18日上午7:02:54</p>
 * <p><b>修改时间：</b>2020年6月18日上午7:02:54</p>
 * @author 彭宇琦
 * @version Ver1.0
 *
 */
public class EasyHttp implements Cloneable {
	/**
	 * 存储指定的请求方式，默认get请求
	 */
	private RequestType requestType = RequestType.GET;
	
	/**
	 * 存储接口请求地址，及相应拼接的参数，用于最终请求
	 */
	private StringBuilder url = new StringBuilder();
	
	/**
	 * 存储接口请求协议
	 */
	private String agreement = "http://";
	/**
	 * 存储主机，IP或者域名
	 */
	private String host = "127.0.0.1";
	/**
	 * 存储端口
	 */
	private String port = "80";
	/**
	 * 存储接口路径
	 */
	private String address = "";
	
	/**
	 * 存储接口请求的参数
	 */
	private HashMap<String, String> parameterMap = new HashMap<>(16);
	
	/**
	 * 存储请求体
	 */
	private String body = "";
	
	/**
	 * 存储字体编码
	 */
	private String encoding = "UTF-8";
	
	/**
	 * 用于存储请求头
	 */
	private HashMap<String, String> headMap = new HashMap<>();
	
	/**
	 * 用于设置请求方式，传入请求方式枚举类{@link RequestType}
	 * @param requestType {@link RequestType}枚举类
	 * @return 类本身
	 */
	public EasyHttp requestType(RequestType requestType) {
		this.requestType = requestType;
		return this;
	}
	
	/**
	 * 指定接口请求url，当未指定协议时，将默认以http为请求协议
	 * @param url 请求url
	 * @return 类本身
	 */
	public EasyHttp url(String url) {
		String inter = "";
		String param = "";
		
		//替换中文冒号为英文冒号
		inter.replaceAll("：", ":");
		//判断传入的url是否存在参数
		if (url.indexOf("?") > -1) {
			inter = url.substring(0, url.indexOf("?"));
			param = url.substring(url.indexOf("?") + 1);
		} else {
			inter = url;
		}
		
		//判断传参是否包含协议，若未包含，则默认拼接http
		int index = inter.indexOf("://");
		if (index > -1) {
			agreement(inter.substring(0, index + "://".length()));
			//裁剪协议
			inter = inter.substring(index + "://".length());
		} 
		
		//按照“/”符号，拆分IP及端口
		if((index = inter.indexOf("/")) > -1) {
			//获取接口地址
			address(inter.substring(index));
			//裁剪接口地址
			inter = inter.substring(0, index);
		}
		
		//判断获取的ip是否包含冒号，若包含冒号，则按照冒号切分ip及端口
		if ((index = inter.indexOf(":")) > -1) {
			port(Integer.valueOf(inter.substring(index + 1)));
			host(inter.substring(0, index));
		} else {
			host(inter);
		}
		
		//裁剪参数
		if (!param.isEmpty()) {
			Arrays.stream(param.split("&")).forEach(parameterText -> {
				putParameter(parameterText);
			});
		}
		
		return this;
	}
	
	/**
	 * 用于设置接口请求协议，默认为“http://”
	 * @param agreement 协议
	 * @return 类本身
	 */
	public EasyHttp agreement(String agreement) {
		this.agreement = agreement;
		return this;
	}
	
	/**
	 * 用于设置接口请求的IP或域名
	 * @param host IP或域名
	 * @return 类本身
	 */
	public EasyHttp host(String host) {
		this.host = host;
		return this;
	}
	
	/**
	 * 用于设置接口路径
	 * @param address 接口路径
	 * @return 类本身
	 */
	public EasyHttp address(String address) {
		this.address = address;
		return this;
	}
	
	/**
	 * 用于设置端口号
	 * @param port 端口号
	 * @return 类本身
	 */
	public EasyHttp port(int port) {
		this.port = String.valueOf(port);
		return this;
	}
	
	/**
	 * 用于设置请求参数，若参数名存在时，则覆盖上一次设置的值
	 * @param key 参数名
	 * @param value 参数值
	 * @return 类本身
	 */
	public EasyHttp putParameter(String key, String value) {
		parameterMap.put(key, value);
		return this;
	}
	
	/**
	 * 用于设置“key=value”格式的参数，若参数名存在时，则覆盖上一次设置的值
	 * @param parameterText 参数文本
	 * @return 类本身
	 * @see #putParameter(String, String)
	 */
	public EasyHttp putParameter(String parameterText) {
		//判断parameterText是否包含等号，若不包含，则直接返回
		if (parameterText.indexOf("=") < 0) {
			return this;
		}
		
		String[] parameter = parameterText.split("=");
		//去空格后传入putParameter方法中
		return putParameter(parameter[0].trim(), parameter[1].trim());
	}
	
	/**
	 * 用于清空设置的请求参数
	 * @return 类本身
	 */
	public EasyHttp clearParameter() {
		parameterMap.clear();
		return this;
	}
	
	/**
	 * 用于设置接口的请求体
	 * @param body 请求体
	 * @return 类本身
	 */
	public EasyHttp body(String body) {
		this.body = body;
		return this;
	}
	
	/**
	 * 用于设置请求头，若请求头名存在时，则覆盖上一次设置的值
	 * @param key 请求头名
	 * @param value 请求头值
	 * @return 类本身
	 */
	public EasyHttp putHead(String key, String value) {
		headMap.put(key, value);
		return this;
	}
	
	/**
	 * 用于根据请求头枚举类{@link HeadType}设置请求头，若请求头名存在时，则覆盖上一次设置的值
	 * @param headType {@link HeadType}枚举类
	 * @return 类本身
	 */
	public EasyHttp putHead(HeadType headType) {
		putHead(headType.getKey(), headType.getValue());
		return this;
	}
	
	/**
	 * 用于清空设置的请求头
	 * @return 类本身
	 */
	public EasyHttp clearHead() {
		headMap.clear();
		return this;
	}
	
	/**
	 * 用于设置字体编码，默认为UTF-8
	 * @param encoding 字体编码
	 * @return 类本身
	 */
	public EasyHttp encoding(String encoding) {
		this.encoding = encoding;
		return this;
	}
	
	/**
	 * 返回设置的url
	 * @return url
	 */
	public String getUrlString() {
		//删除已存储的url值
		url.delete(0, url.length());
		//拼接接口请求地址
		url.append(agreement).append(host).append(":" + port).append(address).toString();
		
		//拼接接口请求参数，若无请求参数，则不拼接参数
		if (!parameterMap.isEmpty()) {
			url.append("?");
			
			//遍历parameterMap，获取参数值
			parameterMap.forEach((key, value) -> {
				url.append(key).append("=").append(value).append("&");
			});
			
			//删除多余的&符号
			url.delete(url.lastIndexOf("&"), url.length());
		}
		
		//判断请求方式是否为get请求，若为get请求时，若设置了body，则将body拼接至末尾
		if (requestType == RequestType.GET && !body.isEmpty()) {
			//根据parameterMap是否为空判断，应拼接何种连接符，若parameterMap存在参数，则拼接"&"符号
			url.append(parameterMap.isEmpty() ? "?" : "&");
		}
		
		//返回参数，并将空格转换为%20
		return url.toString().replaceAll(" ", "%20");
	}
	
	/**
	 * 根据设置的内容，对接口进行请求，返回{@link EasyResponse}类对象，可通过该类对响应结果进行返回
	 * @return {@link EasyResponse}
	 * @throws URISyntaxException 当url地址有误时抛出的异常
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public EasyResponse response() throws URISyntaxException, ClientProtocolException, IOException {
		//根据请求方式，构造相应的请求类对象
		HttpRequestBase request = null;
		switch (requestType) {
		case POST:
			request = new HttpPost(new URI(getUrlString()));
			//由于在HttpRequestBase类中不存在设置请求体的参数，故若请求为post请求时，则先添加请求体
			if (!body.isEmpty()) {
				((HttpPost) request).setEntity(new StringEntity(body, encoding));
			}
			break;
		case GET:
			request = new HttpGet(new URI(getUrlString()));
			break;
		default:
			break;
		}
		
		//设置请求头
		request = setHead(request);
		//对接口进行请求，并存储响应结果
		String responseText = EntityUtils.toString(HttpClients.createDefault().execute(request).getEntity(), encoding);
		//通过响应结果构造EasyResponse类
		return new EasyResponse(responseText);
	}
	
	/**
	 * 用于设置请求头
	 * @param request 请求类对象
	 * @return 设置请求头的类对象
	 */
	public HttpRequestBase setHead(HttpRequestBase request) {
		//遍历headMap中的内容，将请求头逐个设置
		headMap.forEach((key, value) -> {
			request.setHeader(key, value);
		});
		
		return request;
	}
}