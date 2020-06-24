package pres.auxiliary.tool.http;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

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
	 * 以HTML的形式或XML的形式存储响应数据，通过DocumentHelper.parseText(String)方法可以转换
	 */
	private Document responseDom = null;
	
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
			responseJson = null;
			
			//转换为Document格式，若不能转换，则dom为null
			try {
				responseDom = DocumentHelper.parseText(responseText);
				//根据xml格式的特点进行判断，若响应数据的第一位为"<?xml"，则表示该文本是xml格式
				responseType = responseText.indexOf("<?xml") == 0 ? ResponseType.XML : ResponseType.HTML;
			} catch (DocumentException domExcepttion) {
				responseDom = null;
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
	 * 用于以{@link JSONObject}类的形式返回响应数据，若响应数据不是json格式时，则返回null
	 * @return {@link JSONObject}类形式的响应数据
	 */
	public JSONObject getResponseJson() {
		return responseJson;
	}
	
	/**
	 * 用于以{@link Document}类的形式返回响应数据，若响应数据不是html或xml格式时，则返回null
	 * @return {@link Document}类形式的响应数据
	 */
	public Document getDocument() {
		return responseDom;
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
