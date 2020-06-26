package pres.auxiliary.tool.http;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.jsoup.Jsoup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class EasyResponse {
	/**
	 * 存储接口响应数据
	 */
	private String responseText = "";
	
	/**
	 * 以Json类的形式存储响应数据
	 */
	private JSONObject responseJson = null;
	
	/**
	 * 以XML的形式存储响应数据，通过DocumentHelper.parseText(String)方法可以转换
	 */
	private org.dom4j.Document responseXmlDom = null;
	
	/**
	 * 以HTML的形式存储响应数据，通过Jsoup.parse(String)方法可以转换
	 */
	private org.jsoup.nodes.Document responseHtmlDom = null;
	
	/**
	 * 存储响应数据的类型
	 */
	private ResponseType responseType;
	
	/**
	 * 构造类，传入接口的响应数据，并根据响应参数的类型对响应参数进行转换，指定响应参数的类型
	 * @param responseText 接口响应数据
	 */
	public EasyResponse(String responseText) {
		//判断响应数据是否为空，若为空，则无需做任何处理
		if (responseText.isEmpty()) {
			responseType = ResponseType.EMPTY;
			return;
		}
		
		//字符串形式存储
		this.responseText = responseText;
		
		//转换为JSONObject格式，若不能转换，则responseJson为null
		try {
			responseJson = JSONObject.parseObject(responseText);
			responseType = ResponseType.JSON;
		} catch (JSONException jsonException) {
			//设置responseJson为null
			responseJson = null;
			
			//先通过dom4j的格式对数据进行转换，若不能转换，则表示其是文本形式
			try {
				responseXmlDom = DocumentHelper.parseText(responseText);
				//若相应参数开头的文本为<html>，则表示其相应参数为html形式，则以html形式进行转换
				if (responseText.indexOf("<html>") == 0) {
					//存储html形式
					responseType = ResponseType.HTML;
					//设置responseXmlDom为null，保证返回的数据正确
					responseXmlDom = null;
					//将文本转换为HTML的形式
					responseHtmlDom = Jsoup.parse(responseText);
				} else {
					responseType = ResponseType.XML;
				}
			} catch (DocumentException domExcepttion) {
				responseXmlDom = null;
				//若响应数据无法转换成json或dom，则存储为纯文本形式
				responseType = ResponseType.TEXT;
			}
		}
	}
	
	/**
	 * 用于以文本形式返回响应数据
	 * @return 响应数据
	 */
	
	public String getResponseText() {
		return responseText;
	}
	
	/**
	 * 以格式化的形式输出响应数据。
	 * @return 格式化后的响应数据
	 */
	public String getFormatResponseText() {
		//根据responseType存储的形式对格式进行转换
		switch (responseType) {
		case HTML:
			return responseHtmlDom.html();
		case JSON:
			return JSON.toJSONString(responseJson, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue);
		case XML:
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding(responseXmlDom.getXMLEncoding());
			StringWriter stringWriter = new StringWriter();
			XMLWriter writer = new XMLWriter(stringWriter, format);
			try {
				writer.write(responseXmlDom);
				writer.close();
			} catch (IOException e) {
			}
			return stringWriter.toString();
		case EMPTY:
		case TEXT:
			return responseText;
		default:
			return "";
		}
	}
	
	/**
	 * 用于将响应数据写入到文件中
	 * @param outputFile 需要输出的文件
	 * @param isFormat 是否格式化输出
	 * @return 写入的文件
	 * @throws IOException 写入文件有误时抛出的异常
	 */
	public File responseToFile(File outputFile, boolean isFormat) throws IOException {
		//创建文件夹
		outputFile.getParentFile().mkdirs();
		//获取响应数据
		String responseText = isFormat ? getFormatResponseText() : this.responseText;
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
		bw.write(responseText);
		bw.close();
		
		return outputFile;
	}
	
	/**
	 * 用于以{@link JSONObject}类的形式返回响应数据，若响应数据不是json格式时，则返回null
	 * @return {@link JSONObject}类形式的响应数据
	 */
	public JSONObject getResponseJson() {
		return responseJson;
	}
	
	/**
	 * 用于以{@link org.jsoup.nodes.Document}类的形式返回响应数据，若响应数据不是html格式时，则返回null
	 * @return {@link org.jsoup.nodes.Document}类形式的响应数据
	 */
	public org.jsoup.nodes.Document getHtmlDocument() {
		return responseHtmlDom;
	}
	
	/**
	 * 用于以{@link Document}类的形式返回响应数据，若响应数据不是html或xml格式时，则返回null
	 * @return {@link Document}类形式的响应数据
	 */
	public org.dom4j.Document getXmlDocument() {
		return responseXmlDom;
	}
	
	/**
	 * 用于返回响应数据的类型
	 * @return 响应数据类型 
	 */
	public ResponseType getResponseType() {
		return responseType;
	}
	
	/**
	 * <p><b>文件名：</b>EasyResponse.java</p>
	 * <p><b>用途：</b>
	 * 定义响应数据的所有格式
	 * </p>
	 * <p><b>编码时间：</b>2020年6月24日上午8:18:03</p>
	 * <p><b>修改时间：</b>2020年6月24日上午8:18:03</p>
	 * @author 彭宇琦
	 * @version Ver1.0
	 *
	 */
	public enum ResponseType {
		/**
		 * json格式响应数据
		 */
		JSON, 
		/**
		 * html格式响应数据
		 */
		HTML, 
		/**
		 * xml格式响应数据
		 */
		XML, 
		/**
		 * 纯文本格式响应数据
		 */
		TEXT, 
		/**
		 * 无响应数据
		 */
		EMPTY;
	} 
}
