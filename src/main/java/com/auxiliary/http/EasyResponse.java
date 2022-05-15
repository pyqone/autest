package com.auxiliary.http;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Optional;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.jsoup.Jsoup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.auxiliary.tool.file.UnsupportedFileException;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * <p>
 * <b>文件名：</b>EasyResponse.java
 * </p>
 * <p>
 * <b>用途：</b> 对接口响应参数进行处理，可根据不同的返回，对响应结果进行输出，或以格式化的形式输出响应内容
 * </p>
 * <p>
 * <b>编码时间：</b>2020年6月26日下午7:09:07
 * </p>
 * <p>
 * <b>修改时间：</b>2020年6月26日下午7:09:07
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 *
 */
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
     * 接口响应报文内容
     */
    private byte[] responseBody;
    /**
     * 接口响应报文格式
     */
    private MessageType bodyType;
    /**
     * 响应报文字符集编码名称
     */
    private String charsetName = InterfaceInfo.DEFAULT_CHARSETNAME;

    /**
     * 构造对象，指定OKHttp响应类
     * 
     * @param response OKHttp响应类
     */
    protected EasyResponse(Response response) {
        ResponseBody body = response.body();
        try {
            responseBody = body.bytes();
        } catch (IOException e) {
        }
        // TODO 将消息类型名称转换为消息类型枚举，存储返回的字符集编码
        body.contentType();

        response.headers();
    }

	/**
	 * 构造类，传入接口的响应数据，并根据响应参数的类型对响应参数进行转换，指定响应参数的类型
	 * 
	 * @param responseText 接口响应数据
	 */
	public EasyResponse(String responseText) {
		// 判断响应数据是否为空，若为空，则无需做任何处理
		if (responseText.isEmpty()) {
			responseType = ResponseType.EMPTY;
			return;
		}

		// 字符串形式存储
		this.responseText = responseText;

		// 转换为JSONObject格式，若不能转换，则responseJson为null
		try {
			responseJson = JSON.parseObject(responseText);
			responseType = ResponseType.JSON;
		} catch (JSONException jsonException) {
			// 设置responseJson为null
			responseJson = null;

			// 先通过dom4j的格式对数据进行转换，若不能转换，则表示其是文本形式
			try {
				responseXmlDom = DocumentHelper.parseText(responseText);
				// 若相应参数开头的文本为<html>，则表示其相应参数为html形式，则以html形式进行转换
				if (responseText.indexOf("<html>") == 0) {
					// 存储html形式
					responseType = ResponseType.HTML;
					// 设置responseXmlDom为null，保证返回的数据正确
					responseXmlDom = null;
					// 将文本转换为HTML的形式
					responseHtmlDom = Jsoup.parse(responseText);
				} else {
					responseType = ResponseType.XML;
				}
			} catch (DocumentException domExcepttion) {
				responseXmlDom = null;
				// 若响应数据无法转换成json或dom，则存储为纯文本形式
				responseType = ResponseType.TEXT;
			}
		}
	}

	/**
	 * 用于以文本形式返回响应数据
	 * 
	 * @return 响应数据
	 */

	public String getResponseText() {
		return responseText;
	}

	/**
	 * 以格式化的形式输出响应数据。
	 * 
	 * @return 格式化后的响应数据
	 */
	public String getFormatResponseText() {
		// 根据responseType存储的形式对格式进行转换
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
	 * 
	 * @param outputFile 需要输出的文件
	 * @param isFormat   是否格式化输出
	 * @return 写入的文件
	 * @throws IOException 写入文件有误时抛出的异常
	 */
	public File responseToFile(File outputFile, boolean isFormat) {
		outputFile = Optional.ofNullable(outputFile).filter(File::isFile)
				.orElseThrow(() -> new UnsupportedFileException("未指定结果存储文件"));
		
		// 创建文件夹
		outputFile.getParentFile().mkdirs();
		// 获取响应数据
		String responseText = isFormat ? getFormatResponseText() : this.responseText;

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
			bw.write(responseText);
		} catch (IOException e) {
			throw new UnsupportedFileException("文件异常，无法写入结果", e);
		}

		return outputFile;
	}

	/**
	 * 用于以{@link JSONObject}类的形式返回响应数据，若响应数据不是json格式时，则返回null
	 * 
	 * @return {@link JSONObject}类形式的响应数据
	 */
	public JSONObject getResponseJson() {
		return responseJson;
	}

	/**
	 * 用于以{@link org.jsoup.nodes.Document}类的形式返回响应数据，若响应数据不是html格式时，则返回null
	 * 
	 * @return {@link org.jsoup.nodes.Document}类形式的响应数据
	 */
	public org.jsoup.nodes.Document getHtmlDocument() {
		return responseHtmlDom;
	}

	/**
	 * 用于以{@link org.dom4j.Document}类的形式返回响应数据，若响应数据不是html或xml格式时，则返回null
	 * 
	 * @return {@link org.dom4j.Document}类形式的响应数据
	 */
	public org.dom4j.Document getXmlDocument() {
		return responseXmlDom;
	}

	/**
	 * 用于返回响应数据的类型
	 * 
	 * @return 响应数据类型
	 */
	public ResponseType getResponseType() {
		return responseType;
	}
}
