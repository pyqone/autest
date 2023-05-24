package com.auxiliary.selenium.location;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.yaml.snakeyaml.Yaml;

import com.auxiliary.selenium.element.ElementType;
import com.auxiliary.selenium.location.UndefinedElementException.ExceptionElementType;
import com.auxiliary.tool.common.Placeholder;

/**
 * <p>
 * <b>文件名：</b>YamlLocation.java
 * </p>
 * <p>
 * <b>用途：</b> 用于读取以yaml文件形式存储的元素定位信息
 * </p>
 * <p>
 * <b>编码时间：</b>2021年11月24日 上午7:58:43
 * </p>
 * <p>
 * <b>修改时间：</b>2021年11月24日 上午7:58:43
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 */
public class YamlLocation extends AbstractLocation implements ReadElementLimit, AppElementLocation {
	/**
	 * 指向yaml中的模板key值
	 */
	public static final String KEY_TEMPLETE = "templet";
	/**
	 * 指向yaml中的元素key值
	 */
	public static final String KEY_ELEMENT = "element";
	/**
	 * 指向yaml中的元素定位方式key值
	 */
	public static final String KEY_LOCATION = "location";
	/**
	 * 指向yaml中的元素定位模板key值
	 */
	public static final String KEY_TEMP = "temp";
	/**
	 * 指向yaml中的元素定位模板key值
	 */
	public static final String KEY_VALUE = "value";
	/**
	 * 指向yaml中的元素定位类型key值
	 */
	public static final String KEY_TYPE = "type";
	/**
	 * 指向yaml中的元素等待时间key值
	 */
	public static final String KEY_WAIT = "wait";
	/**
	 * 指向yaml中的元素默认值的key值
	 */
	public static final String KEY_DEFAULT_VALUE = "value";
	/**
	 * 指向yaml中的元素所在窗体key值
	 */
	public static final String KEY_IFRAME = "iframe";
	/**
	 * 指向yaml中的元素与app相关的上下文key值
	 */
	public static final String KEY_CONTEXT_VALUE = "context";
	/**
	 * 指向yaml中的元素前置等待时间key值
	 */
	public static final String KEY_BEFORE_TIME_VALUE = "before_time";

	/**
	 * 存储元素模板内容
	 */
	private Map<String, String> tempLetMap;
	/**
	 * 存储元素信息
	 */
	private Map<String, Object> elementsMap;

	/**
	 * 存储单个元素内容
	 */
	private Map<String, Object> elementMap;

	/**
	 * 通过写在文件中的yaml文本类对象对类进行构造
	 *
	 * @param yamlFile 存储yaml的文件类对象
	 */
	public YamlLocation(File yamlFile) {
		try (BufferedReader br = new BufferedReader(new FileReader(yamlFile))) {
			Map<String, Object> locationFileMap = new Yaml().load(br);
			analysisYaml(locationFileMap);
		} catch (IOException e) {
			throw new IncorrectFileException("yaml文件异常，文件位置：" + yamlFile.getAbsolutePath());
		}
	}

	@Override
	public ArrayList<ElementLocationInfo> getElementLocation() {
		// 判断是否进行元素查找
		if (!Optional.ofNullable(elementMap).filter(map -> !map.isEmpty()).isPresent()) {
			throw new UndefinedElementException("元素未进行查找，无法返回元素信息");
		}

		ArrayList<ElementLocationInfo> locationList = new ArrayList<>();
		// 获取当前元素的定位数组
		@SuppressWarnings("unchecked")
		List<Map<String, String>> fileLocationList = (List<Map<String, String>>) elementMap.get(KEY_LOCATION);
		// 若获取到的数组不为空，则读取其中的内容
		if (fileLocationList != null) {
			// 遍历locationList组，读取元素的定位内容，存储相应的
			for (int i = 0; i < fileLocationList.size(); i++) {
				// 获取当前读取的yaml
				Map<String, String> locationInfoMap = fileLocationList.get(i);
				// 读取其中的"type"值的内容
				String typeText = locationInfoMap.get(KEY_TYPE);
				// 若当前值不存在内容，则抛出异常
				if (typeText == null || typeText.isEmpty()) {
					throw new UndefinedElementException("元素“" + name + "”不存在" + KEY_TYPE + "值");
				}

				// 转换并存储定位方式
                ByType byType = Optional.ofNullable(ByType.typeText2Type(typeText))
                        .orElseThrow(() -> new UndefinedElementException("不存在的元素定位方式：" + typeText));

				String locationText = "";
				// 读取其中的"temp"值的内容
				String tempText = locationInfoMap.get(KEY_TEMP);
				// 若当前值不存在内容，则抛出异常
				if (tempText == null || tempText.isEmpty()) {
					String valueText = locationInfoMap.get(KEY_VALUE);
					// 若不存在模板key，则判断是否存在定位值key，若均不存在，则抛出异常
					if (valueText == null || valueText.isEmpty()) {
						throw new UndefinedElementException("元素“" + name + "”不存在定位方式键值对");
					}

					// 若存在value值，则读取相应的值，并进行存储
					locationText = valueText;
				} else {
					// 若存在模板，则按照模板读取方式进行处理
					locationText = analysisTemplet(locationInfoMap);
				}

				locationList.add(new ElementLocationInfo(byType, locationText));
			}
		}

		return locationList;
	}

	@Override
	public ElementType getElementType() {
		// 判断是否进行元素查找
		if (!Optional.ofNullable(elementMap).filter(map -> !map.isEmpty()).isPresent()) {
			throw new UndefinedElementException("元素未进行查找，无法返回元素信息");
		}

		// 若elementTypeText为空，则赋为0
		String elementTypeText = Optional.ofNullable(elementMap.get(KEY_TYPE)).map(Object::toString).orElse("0");
		return toElementType(elementTypeText);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<String> getIframeNameList() {
		// 判断是否进行元素查找
		if (!Optional.ofNullable(elementMap).filter(map -> !map.isEmpty()).isPresent()) {
			throw new UndefinedElementException("元素未进行查找，无法返回元素信息");
		}

		ArrayList<String> iframeNameList = new ArrayList<>();

		// 存储当前元素的父窗体的名称
		String iframeName = "";
		Map<String, Object> tempElementMap = elementMap;
		// 循环，根据父层级向上遍历元素，直到元素无父层窗体为止
		while (tempElementMap.get(KEY_IFRAME) != null
				&& !(iframeName = tempElementMap.get(KEY_IFRAME).toString()).isEmpty()) {
			tempElementMap = (Map<String, Object>) elementsMap.get(iframeName);
			iframeNameList.add(iframeName);
		}

		// 反序集合，使最高层窗体放在最前
		Collections.reverse(iframeNameList);

		return iframeNameList;
	}

	@Override
	public long getWaitTime() {
		// 判断是否进行元素查找
		if (!Optional.ofNullable(elementMap).filter(map -> !map.isEmpty()).isPresent()) {
			throw new UndefinedElementException("元素未进行查找，无法返回元素信息");
		}
		return toWaitTime(Optional.ofNullable(elementMap.get(KEY_WAIT)).map(Object::toString).orElse("-1"));
	}

	@SuppressWarnings("unchecked")
	@Override
	public ReadLocation find(String name) {
		// 判断传入的名称是否正确
		String newName = Optional.ofNullable(name).filter(n -> !n.isEmpty())
				.orElseThrow(() -> new UndefinedElementException(name, ExceptionElementType.ELEMENT));

		// 判断当前查找的元素是否与原元素名称一致，不一致，则进行元素查找
		if (!newName.equals(this.name)) {
			elementMap = (Map<String, Object>) elementsMap.get(newName);
			this.name = newName;
		}

		return this;
	}

	@Override
	public long getBeforeTime() {
		// 判断是否进行元素查找
		if (!Optional.ofNullable(elementMap).filter(map -> !map.isEmpty()).isPresent()) {
			throw new UndefinedElementException("元素未进行查找，无法返回元素信息");
		}

		long time = toWaitTime(
				Optional.ofNullable(elementMap.get(KEY_BEFORE_TIME_VALUE)).map(Object::toString).orElse("-1"));
		return time < 0 ? 0 : time;
	}

	@Override
	public boolean isNative() {
		// 判断是否进行元素查找
		if (!Optional.ofNullable(elementMap).filter(map -> !map.isEmpty()).isPresent()) {
			throw new UndefinedElementException("元素未进行查找，无法返回元素信息");
		}

		return elementMap.get(KEY_CONTEXT_VALUE) == null;
	}

	@Override
	public String getContext() {
		// 判断是否进行元素查找
		if (!Optional.ofNullable(elementMap).filter(map -> !map.isEmpty()).isPresent()) {
			throw new UndefinedElementException("元素未进行查找，无法返回元素信息");
		}

		return Optional.ofNullable(elementMap.get(KEY_CONTEXT_VALUE)).map(Object::toString).orElse("");
	}

	@Override
	public String getDefaultValue() {
		// 判断是否进行元素查找
		if (!Optional.ofNullable(elementMap).filter(map -> !map.isEmpty()).isPresent()) {
			throw new UndefinedElementException("元素未进行查找，无法返回元素信息");
		}

		return Optional.ofNullable(elementMap.get(KEY_DEFAULT_VALUE)).map(Object::toString).orElse("");
	}

	/**
	 * 用于解析读取到的yaml文件中的内容，将其转换为相应的map集合
	 *
	 * @param locationMap 模板文件内容map集合
	 */
	@SuppressWarnings("unchecked")
	protected void analysisYaml(Map<String, Object> locationMap) {
		// 解析模板信息
		tempLetMap = (Map<String, String>) locationMap.get(KEY_TEMPLETE);
		// 解析元素信息
		elementsMap = (Map<String, Object>) locationMap.get(KEY_ELEMENT);
	}

	/**
	 * 用于返回读取到的模板内容
	 *
	 * @param locationInfoMap 定位方式map
	 * @return 经过转换的定位方式
	 */
	private String analysisTemplet(Map<String, String> locationInfoMap) {
		// 获取模板名称
		String tempText = locationInfoMap.get(KEY_TEMP);
		// 判断名称是否存在，若不存在，则抛出异常
		if (!tempLetMap.containsKey(tempText)) {
			throw new UndefinedElementException(tempText, ExceptionElementType.TEMPLET);
		}

		// 读取模板内容
		String tempValueText = tempLetMap.get(tempText);

		// 判断元素是否存在需要替换的内容，若不存在，则不进行替换
        Placeholder placeholder = new Placeholder(this.placeholder);
        if (!placeholder.isContainsPlaceholder(tempValueText)) {
			return tempValueText;
		}

        // 添加特殊的占位符
        placeholder.addReplaceWord("name", name);
        // 添加已有的词语
        placeholder.addReplaceWord(locationInfoMap, false);

        return placeholder.replaceText(tempValueText);
	}
}
