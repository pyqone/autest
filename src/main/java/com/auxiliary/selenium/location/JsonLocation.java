package com.auxiliary.selenium.location;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auxiliary.selenium.element.ElementType;
import com.auxiliary.selenium.location.UndefinedElementException.ExceptionElementType;

/**
 * <p>
 * <b>文件名：</b>JsonLocation.java
 * </p>
 * <p>
 * <b>用途：</b> 用于读取以json形式存储的元素定位信息
 * </p>
 * <p>
 * <b>编码时间：</b>2020年10月28日上午8:24:56
 * </p>
 * <p>
 * <b>修改时间：</b>2021年3月8日上午8:08:45
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 *
 */
public class JsonLocation extends AbstractLocation {
	/**
	 * 指向json中的模板key值
	 */
	public static final String KEY_TEMPLETE = "templet";
	/**
	 * 指向json中的元素key值
	 */
	public static final String KEY_ELEMENT = "element";
	/**
	 * 指向json中的元素定位方式key值
	 */
	public static final String KEY_LOCATION = "location";
	/**
	 * 指向json中的元素定位模板key值
	 */
	public static final String KEY_TEMP = "temp";
	/**
	 * 指向json中的元素定位模板key值
	 */
	public static final String KEY_VALUE = "value";
	/**
	 * 指向json中的元素定位类型key值
	 */
	public static final String KEY_TYPE = "type";
	/**
	 * 指向json中的元素等待时间key值
	 */
	public static final String KEY_WAIT = "wait";
	/**
	 * 指向json中的元素所在窗体key值
	 */
	public static final String KEY_IFRAME = "iframe";

	/**
	 * 存储转换后获得到的模板json对象
	 */
	JSONObject templateJson;
	/**
	 * 存储转换后获得的元素json对象
	 */
	JSONObject elementJson;

	/**
	 * 通过写在文件中的json文本类对象对类进行构造
	 * 
	 * @param jsonTextFile 存储json的文件类对象
	 */
	public JsonLocation(File jsonTextFile) {
		// 读取json文件中的内容
		StringBuilder jsonText = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(jsonTextFile))) {
			String text = "";
			while ((text = br.readLine()) != null) {
				jsonText.append(text);
			}
		} catch (IOException e) {
			throw new IncorrectFileException("json文件异常，文件位置：" + jsonTextFile.getAbsolutePath());
		}

		// 转换读取的json内容
		analysisJson(jsonText.toString());
	}

	/**
	 * 通过json文本对类进行构造
	 * 
	 * @param jsonText json文本
	 */
	public JsonLocation(String jsonText) {
		analysisJson(jsonText);
	}

	@Override
	@Deprecated
	public ArrayList<ByType> findElementByTypeList(String name) {
		return find(name).getElementByTypeList();
	}

	@Override
	@Deprecated
	public ArrayList<String> findValueList(String name) {
		return find(name).getValueList();
	}

	@Override
	@Deprecated
	public ElementType findElementType(String name) {
		return find(name).getElementType();
	}

	@Override
	@Deprecated
	public ArrayList<String> findIframeNameList(String name) {
		return find(name).getIframeNameList();
	}

	@Override
	@Deprecated
	public long findWaitTime(String name) {
		return find(name).getWaitTime();
	}

	/**
	 * 返回获取的模板json内容
	 * 
	 * @return 模板json
	 */
	public String getTempletJson() {
		return templateJson.toJSONString();
	}

	/**
	 * 返回获取的元素json内容
	 * 
	 * @return 元素json
	 */
	public String getElementJson() {
		return elementJson.toJSONString();
	}

	/**
	 * 用于将读取到的json进行转换，分离模板json与元素json
	 * 
	 * @param text json文本
	 * @throws UndefinedElementException json中无元素信息时抛出的异常
	 */
	protected void analysisJson(String text) {
		// 将文件解析成JSONObject类对象
		JSONObject json = JSON.parseObject(text);

		// 获取元素json，若不存在元素json，则抛出异常
		if (json.containsKey(KEY_ELEMENT)) {
			this.elementJson = json.getJSONObject(KEY_ELEMENT);
		} else {
			throw new UndefinedElementException("Json中不存在“" + KEY_ELEMENT + "”key值");
		}

		// 获取模板json，若不存在模板json，则不进行处理
		if (json.containsKey(KEY_TEMPLETE)) {
			this.templateJson = json.getJSONObject(KEY_TEMPLETE);
		} else {
			this.templateJson = new JSONObject();
		}
	}

	/**
	 * 用于获取元素json
	 * 
	 * @param name 元素名称
	 * @return 获取到的元素json类对象
	 */
	private JSONObject getElementJson(String name) {
		// 判断元素是否存在，若不存在，则抛出异常
		if (!elementJson.containsKey(name)) {
			throw new UndefinedElementException(name, ExceptionElementType.ELEMENT);
		}

		// 获取当前元素的定位数组
		return elementJson.getJSONObject(name);
	}

	/**
	 * 用于返回当前元素是否存在父层窗体，若存在，则返回相应的窗体名称；反之则返回空串
	 * 
	 * @param elementJson 元素json
	 * @return 元素对应的父层窗体名称
	 */
	private String getNextIframe(JSONObject elementJson) {
		// 获取元素父窗体的名称
		String iframeName = elementJson.getString(KEY_IFRAME);
		return iframeName == null ? "" : iframeName;
	}

	/**
	 * 用于返回读取到的模板内容
	 * 
	 * @param locationJson 定位方式json
	 * @return 经过转换的定位方式
	 */
	private String analysisTemplet(JSONObject locationJson) {
		// 获取模板名称
		String tempText = locationJson.getString(KEY_TEMP);
		// 判断名称是否存在，若不存在，则抛出异常
		if (!templateJson.containsKey(tempText)) {
			throw new UndefinedElementException(tempText, ExceptionElementType.TEMPLET);
		}

		// 读取模板内容
		String tempValueText = templateJson.getString(tempText);

		// 遍历所有键值对，将相应的内容进行存储
		for (String key : locationJson.keySet()) {
			String matchKey = MATCH_START_SIGN + key + MATCH_END_SIGN;
			tempValueText = tempValueText.replaceAll(matchKey, locationJson.getString(key));
		}

		return tempValueText;
	}

	@Override
	public ReadLocation find(String name) {
		// 判断传入的名称是否正确
		String newName = Optional.ofNullable(name).filter(n -> !n.isEmpty())
				.orElseThrow(() -> new UndefinedElementException(name, ExceptionElementType.ELEMENT));

		// 判断当前查找的元素是否与原元素名称一致，不一致，则进行元素查找
		if (!newName.equals(this.name)) {
			// 清除原始缓存
			clearCache();

			// 查找元素
			JSONObject element = getElementJson(newName);
			// 根据元素对象查找元素信息，并缓存
			saveElementByTypeList(element);
			saveElementType(element);
			saveIframeNameList(element);
			saveValueList(element);
			saveWaitTime(element);
		}

		return this;
	}

	/**
	 * 用于查找元素的等待时间，并进行缓存
	 * 
	 * @param element 元素对象
	 */
	private void saveWaitTime(JSONObject element) {
		waitTime = toWaitTime(element.getString(KEY_WAIT));
	}

	/**
	 * 用于查找元素定位内容集合，并进行缓存
	 * 
	 * @param element 元素对象
	 */
	private void saveValueList(JSONObject element) {
		// 获取当前元素的定位数组
		JSONArray locationList = element.getJSONArray(KEY_LOCATION);
		// 若获取到的数组不为空，则读取其中的内容
		if (locationList != null) {
			// 遍历locationList组，读取元素的定位内容，存储相应的
			for (int i = 0; i < locationList.size(); i++) {
				// 获取当前读取的json
				JSONObject locationJson = locationList.getJSONObject(i);
				// 读取其中的"type"值的内容
				String tempText = locationJson.getString(KEY_TEMP);
				// 若当前值不存在内容，则抛出异常
				if (tempText == null || tempText.isEmpty()) {
					String valueText = locationJson.getString(KEY_VALUE);
					// 若不存在模板key，则判断是否存在定位值key，若均不存在，则抛出异常
					if (valueText == null || valueText.isEmpty()) {
						throw new UndefinedElementException("元素“" + name + "”不存在定位方式键值对");
					}

					// 若存在value值，则读取相应的值，并进行存储
					valueList.add(valueText);
				} else {
					// 若存在模板，则按照模板读取方式进行处理
					valueList.add(analysisTemplet(locationJson));
				}
			}
		}
	}

	/**
	 * 用于查找元素的所有父窗体名称集合，并进行缓存
	 * 
	 * @param element 元素对象
	 */
	private void saveIframeNameList(JSONObject element) {
		// 存储当前元素的父窗体的名称
		String iframeName = "";

		JSONObject elementJson = element;
		// 循环，根据父层级向上遍历元素，直到元素无父层窗体为止
		while (!(iframeName = getNextIframe(elementJson)).isEmpty()) {
			elementJson = getElementJson(iframeName);
			iframeNameList.add(iframeName);
		}

		// 反序集合，使最高层窗体放在最前
		Collections.reverse(iframeNameList);
	}

	/**
	 * 用于查找元素的类型，并进行缓存
	 * 
	 * @param element 元素对象
	 */
	private void saveElementType(JSONObject element) {
		String elementTypeText = element.getString(KEY_TYPE);
		// 若elementTypeText为空，则赋为0
		elementType = toElementType(elementTypeText == null ? "0" : elementTypeText);
	}

	/**
	 * 用于查找元素的所有定位方式集合，并进行缓存
	 * 
	 * @param element 元素对象
	 */
	private void saveElementByTypeList(JSONObject element) {
		// 获取当前元素的定位数组
		JSONArray locationList = element.getJSONArray(KEY_LOCATION);
		// 若获取到的数组不为空，则读取其中的内容
		if (locationList != null) {
			// 遍历locationList组，读取元素的定位内容，存储相应的
			for (int i = 0; i < locationList.size(); i++) {
				// 获取当前读取的json
				JSONObject locationJson = locationList.getJSONObject(i);
				// 读取其中的"type"值的内容
				String typeText = locationJson.getString(KEY_TYPE);
				// 若当前值不存在内容，则抛出异常
				if (typeText == null || typeText.isEmpty()) {
					throw new UndefinedElementException("元素“" + name + "”不存在" + KEY_TYPE + "值");
				}

				// 转换并存储定位方式
				byTypeList.add(toByType(typeText));
			}
		}
	}
}
