package pres.auxiliary.work.selenium.location;

import java.util.ArrayList;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import pres.auxiliary.work.selenium.element.ElementType;

/**
 * <p><b>文件名：</b>NoFileLocation.java</p>
 * <p><b>用途：</b>
 * 提供在不编写元素定位文件的情况下读取元素定位方法，通过该类，可存储一些临时添加的元素定位
 * 方式，并在编写脚本过程中进行调用
 * </p>
 * <p><b>编码时间：</b>2020年10月29日上午8:38:24</p>
 * <p><b>修改时间：</b>2020年10月29日上午8:38:24</p>
 * @author 彭宇琦
 * @version Ver1.0
 */
public class NoFileLocation extends AbstractLocation implements WriteLocation, WriteTempletLocation {
	/**
	 * 存储json的读取方式
	 */
	private JsonLocation jsonLocation;
	/**
	 * 用于存储当前通过方法添加的元素信息
	 */
	private JSONObject nowLocationJson;
	/**
	 * 用于存储通过新增方法添加的元素信息
	 */
	private JSONObject newLocationJson;
	
	/**
	 * 构造类对象
	 */
	public NoFileLocation() {
		//初始化json参数
		nowLocationJson = new JSONObject();
		nowLocationJson.put(JsonLocation.KEY_TEMPLETE, new JSONObject());
		nowLocationJson.put(JsonLocation.KEY_ELEMENT, new JSONObject());
		//克隆当前元素json信息
		newLocationJson = (JSONObject) nowLocationJson.clone();
		
		//初始化json定位类对象
		jsonLocation = new JsonLocation(nowLocationJson.toJSONString());
	}
	
	@Override
	public void putElementLocation(String name, ByType byType, String value) {
		//获取元素对象
		JSONObject elementJson = getElementJson(name);
		//获取对象的location键值，若键值不存在，则添加键值
		if (!elementJson.containsKey(JsonLocation.KEY_LOCATION)) {
			elementJson.put(JsonLocation.KEY_LOCATION, new JSONArray());
		}
		
		//封装定位方式
		JSONObject locationJson = new JSONObject();
		locationJson.put(JsonLocation.KEY_TYPE, byType.getValue());
		locationJson.put(JsonLocation.KEY_VALUE, value);
		
		//向定位方式组中添加定位方式
		elementJson.getJSONArray(JsonLocation.KEY_LOCATION).add(locationJson);
	}
	
	@Override
	public void putElementTempletLocation(String name, ByType byType, String templetId) {
		//获取元素对象
		JSONObject elementJson = getElementJson(name);
		//获取对象的location键值，若键值不存在，则添加键值
		if (!elementJson.containsKey(JsonLocation.KEY_LOCATION)) {
			elementJson.put(JsonLocation.KEY_LOCATION, new JSONArray());
		}
		
		//封装定位方式
		JSONObject locationJson = new JSONObject();
		locationJson.put(JsonLocation.KEY_TEMP, templetId);
		locationJson.put(JsonLocation.KEY_TYPE, byType.getValue());
		
		//向定位方式组中添加定位方式
		elementJson.getJSONArray(JsonLocation.KEY_LOCATION).add(locationJson);
	}

	@Override
	public void putElementType(String name, ElementType elementType) {
		//获取元素对象，并存储键值
		getElementJson(name).put(JsonLocation.KEY_TYPE, elementType.getValue());
	}

	@Override
	public void putIframeNameList(String name, String iframeName) {
		//获取元素对象，并存储键值
		getElementJson(name).put(JsonLocation.KEY_IFRAME, iframeName);
	}

	@Override
	public void putWaitTime(String name, long waitTime) {
		//获取元素对象，并存储键值
		getElementJson(name).put(JsonLocation.KEY_WAIT, waitTime);
	}
	
	@Override
	public void putTemplet(String templetId, String templetValue) {
		newLocationJson.getJSONObject((JsonLocation.KEY_TEMPLETE)).put(templetId, templetValue);
	}

	@Override
	public void putTempletReplaceKey(String name, String templetId, String key, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<ByType> findElementByTypeList(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> findValueList(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ElementType findElementType(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> findIframeNameList(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long findWaitTime(String name) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	/**
	 * TODO 测试使用，返回元素信息
	 */
	public String getJson() {
		return newLocationJson.toJSONString();
	}
	
	/**
	 * 用于返回元素名称对应的元素json对象
	 * @param name 元素名称
	 * @return 元素名称对应的json对象
	 */
	private JSONObject getElementJson(String name) {
		//获取元素对象，若元素对象不存在，则创建相应的元素json，并返回
		if (!newLocationJson.getJSONObject(JsonLocation.KEY_ELEMENT).containsKey(name)) {
			newLocationJson.getJSONObject(JsonLocation.KEY_ELEMENT).put(name, new JSONObject());
		}
		
		return newLocationJson.getJSONObject(JsonLocation.KEY_ELEMENT).getJSONObject(name);
	}
}
