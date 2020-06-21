package pres.auxiliary.tool.http;

import java.util.Arrays;
import java.util.HashMap;

import com.alibaba.fastjson.JSONObject;

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
	 * 存储指定的请求方式
	 */
	private RequestType requestType;
	
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
	private String host = "";
	/**
	 * 存储端口
	 */
	private String port = "";
	/**
	 * 存储接口路径
	 */
	private String address = "";
	
	/**
	 * 存储接口请求的参数
	 */
	private HashMap<String, String> parameterMap = new HashMap<>(16);
	
	/**
	 * 用于存储请求体
	 */
	private String body = "";
	
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
	 * @param host 请求url
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
			port(inter.substring(index + 1));
			host(inter.substring(0, index));
		} else {
			host(inter);
			//若其中未包含冒号，则表示端口为80
			port("80");
		}
		
		//裁剪参数
		Arrays.stream(param.split("&")).forEach(parameterText -> {
			putParameter(parameterText);
		});
		
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
	 * @param host 接口路径
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
	public EasyHttp port(String port) {
		this.port = port;
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
		String[] parameter = parameterText.split("=");
		//去空格后传入putParameter方法中
		return putParameter(parameter[0].trim(), parameter[1].trim());
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
		if (requestType == RequestType.GET) {
			//根据parameterMap是否为空判断，应拼接何种连接符，若parameterMap存在参数，则拼接"&"符号
			url.append(parameterMap.isEmpty() ? "?" : "&");
		}
		
		//返回参数，并将空格转换为20%
		return url.toString().replaceAll(" ", "20%");
	}
}