package com.auxiliary.selenium.location;

import java.util.ArrayList;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auxiliary.selenium.element.ElementType;
import com.auxiliary.selenium.location.UndefinedElementException.ExceptionElementType;

/**
 * <p>
 * <b>文件名：</b>NoFileLocation.java
 * </p>
 * <p>
 * <b>用途：</b> 提供在不编写元素定位文件的情况下读取元素定位方法，通过该类，可存储一些临时添加的元素定位 方式，并在编写脚本过程中进行调用
 * </p>
 * <p>
 * <b>编码时间：</b>2020年10月29日上午8:38:24
 * </p>
 * <p>
 * <b>修改时间：</b>2021年3月9日上午8:08:45
 * </p>
 * 
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
		// 初始化json参数
		nowLocationJson = new JSONObject();
		nowLocationJson.put(JsonLocation.KEY_TEMPLETE, new JSONObject());
		nowLocationJson.put(JsonLocation.KEY_ELEMENT, new JSONObject());
		// 复制当前元素json信息
		newLocationJson = JSON.parseObject(nowLocationJson.toJSONString());

		jsonLocation = new JsonLocation(nowLocationJson.toJSONString());
	}

	@Override
	public void putElementLocation(String name, ByType byType, String value) {
		// 获取元素对象
		JSONObject elementJson = getElementJson(name);
		// 获取对象的location键值，若键值不存在，则添加键值
		if (!elementJson.containsKey(JsonLocation.KEY_LOCATION)) {
			elementJson.put(JsonLocation.KEY_LOCATION, new JSONArray());
		}

		// 封装定位方式
		JSONObject locationJson = new JSONObject();
		locationJson.put(JsonLocation.KEY_TYPE, byType.getValue());
		locationJson.put(JsonLocation.KEY_VALUE, value);

		// 向定位方式组中添加定位方式
		elementJson.getJSONArray(JsonLocation.KEY_LOCATION).add(locationJson);
	}

	@Override
	public void putElementTempletLocation(String name, ByType byType, String templetId) {
		// 获取元素对象
		JSONObject elementJson = getElementJson(name);
		// 获取对象的location键值，若键值不存在，则添加键值
		if (!elementJson.containsKey(JsonLocation.KEY_LOCATION)) {
			elementJson.put(JsonLocation.KEY_LOCATION, new JSONArray());
		}

		// 封装定位方式
		JSONObject locationJson = new JSONObject();
		locationJson.put(JsonLocation.KEY_TEMP, templetId);
		locationJson.put(JsonLocation.KEY_TYPE, byType.getValue());

		// 向定位方式组中添加定位方式
		elementJson.getJSONArray(JsonLocation.KEY_LOCATION).add(locationJson);
	}

	@Override
	public void putElementType(String name, ElementType elementType) {
		// 获取元素对象，并存储键值
		getElementJson(name).put(JsonLocation.KEY_TYPE, elementType.getValue());
	}

	@Override
	public void putIframeName(String name, String iframeName) {
		// 获取元素对象，并存储键值
		getElementJson(name).put(JsonLocation.KEY_IFRAME, iframeName);
	}

	@Override
	public void putWaitTime(String name, long waitTime) {
		// 获取元素对象，并存储键值
		getElementJson(name).put(JsonLocation.KEY_WAIT, waitTime);
	}

	@Override
	public void putTemplet(String templetId, String templetValue) {
		newLocationJson.getJSONObject((JsonLocation.KEY_TEMPLETE)).put(templetId, templetValue);
	}

	@Override
	public void putTempletReplaceKey(String name, String templetId, String key, String value) {
		// 获取元素定位方式列表
		JSONObject elementJson = getElementJson(name);
		// 获取元素定位方式集合
		JSONArray locationList = elementJson.getJSONArray(JsonLocation.KEY_LOCATION);
		// 若当前元素不存在定位方式集合，则抛出异常
		if (locationList == null) {
			throw new UndefinedElementException(templetId, ExceptionElementType.ELEMENT);
		}

		// 遍历集合，获取与当前指定的模板id一致的模板，在其下记录相应的key与value
		for (int i = 0; i < locationList.size(); i++) {
			JSONObject locationJson = locationList.getJSONObject(i);
			// 判断当前json是否包含模板Id的key
			if (locationJson.containsKey(JsonLocation.KEY_TEMP)) {
				String tempId = locationJson.getString(JsonLocation.KEY_TEMP);
				// 判断当前id是否与所传的id一致，若一致，则在json下记录相应的属性值，并结束当前方法
				if (templetId.equals(tempId)) {
					locationJson.put(key, value);
					return;
				}
			}
		}

		// 若循环完毕后仍未找到模板，则抛出异常
		throw new UndefinedElementException(templetId, ExceptionElementType.TEMPLET);
	}

	@Override
	@Deprecated
	public ArrayList<ByType> findElementByTypeList(String name) {
		// 对json定位方式读取类进行重构判断，并调用相应的方法
		return jsonLocation.findElementByTypeList(name);
	}

	@Override
	@Deprecated
	public ArrayList<String> findValueList(String name) {
		// 对json定位方式读取类进行重构判断，并调用相应的方法
		return jsonLocation.findValueList(name);
	}

	@Override
	@Deprecated
	public ElementType findElementType(String name) {
		// 对json定位方式读取类进行重构判断，并调用相应的方法
		return find(name).getElementType();
	}

	@Override
	@Deprecated
	public ArrayList<String> findIframeNameList(String name) {
		// 对json定位方式读取类进行重构判断，并调用相应的方法
		return find(name).getIframeNameList();
	}

	@Override
	@Deprecated
	public long findWaitTime(String name) {
		// 对json定位方式读取类进行重构判断，并调用相应的方法
		return find(name).getWaitTime();
	}
	
	@Override
	public ArrayList<ElementLocation> getElementLocation() {
		return jsonLocation.getElementLocation();
	}

	@Override
	public ElementType getElementType() {
		return jsonLocation.getElementType();
	}

	@Override
	public ArrayList<String> getIframeNameList() {
		return jsonLocation.getIframeNameList();
	}

	@Override
	public long getWaitTime() {
		return jsonLocation.getWaitTime();
	}

	/**
	 * 用于返回元素名称对应的元素json对象
	 * 
	 * @param name 元素名称
	 * @return 元素名称对应的json对象
	 */
	private JSONObject getElementJson(String name) {
		// 获取元素对象，若元素对象不存在，则创建相应的元素json，并返回
		if (!newLocationJson.getJSONObject(JsonLocation.KEY_ELEMENT).containsKey(name)) {
			newLocationJson.getJSONObject(JsonLocation.KEY_ELEMENT).put(name, new JSONObject());
		}

		return newLocationJson.getJSONObject(JsonLocation.KEY_ELEMENT).getJSONObject(name);
	}

	/**
	 * 用于构建JsonLocation类，若当前json未改变，则不重新构造；反之，则重新构造元素定位方式
	 * 
	 * @return 返回JsonLocation类
	 */
	/*
	 * private JsonLocation getJsonLocation() { if (!isJsonChange()) {
	 * jsonLocation.analysisJson(newLocationJson.toJSONString()); nowLocationJson =
	 * JSON.parseObject(newLocationJson.toJSONString()); }
	 * 
	 * return jsonLocation; }
	 */

	/**
	 * 用于判断当前json是否有变化，即是否对当前的json进行过变更
	 * 
	 * @return json是否存在变化
	 */
	private boolean isJsonChange() {
		return nowLocationJson.equals(newLocationJson);
	}

	@Override
	public ReadLocation find(String name) {
		if (!isJsonChange()) {
			jsonLocation.analysisJson(newLocationJson.toJSONString());
			nowLocationJson = JSON.parseObject(newLocationJson.toJSONString());
		}

		return jsonLocation.find(name);
	}
}
