package com.auxiliary.selenium.event.extend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.auxiliary.datadriven.DataDriverFunction;
import com.auxiliary.datadriven.FunctionUtil;
import com.auxiliary.datadriven.Functions;
import com.auxiliary.selenium.brower.AbstractBrower;
import com.auxiliary.selenium.element.Element;
import com.auxiliary.selenium.element.ElementData;
import com.auxiliary.selenium.event.AbstractEvent;
import com.auxiliary.selenium.event.ClickEvent;
import com.auxiliary.selenium.event.TextEvent;

/**
 * <p>
 * <b>文件名：</b>DimEvent.java
 * </p>
 * <p>
 * <b>用途：</b> 简化事件写法的模糊事件，根据元素的类型和默认值，自动关联元素的点击和输入事件，以简化事件的编写。
 * </p>
 * <p>
 * 在类中，定义了获取元素文本内容的关键词“get”和清空文本的关键词“clear”，当元素的默认值为该字符串时，
 * 则将方法定义为获取元素的文本内容，故在编写元素默认值时，需要注意该关键词，避免造成不符合预期的结果。
 * </p>
 * <p>
 * <b>编码时间：</b>2021年3月23日下午6:29:22
 * </p>
 * <p>
 * <b>修改时间：</b>2021年3月23日下午6:29:22
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public class DimEvent extends AbstractEvent {
	/**
	 * 用于指定获取元素文本内容的字符串
	 */
	public static final String CONST_GET_TEXT_VALUE = "get";

	/**
	 * 用于指定获取元素文本内容的字符串
	 */
	public static final String CONST_CLEAR_VALUE = "clear";

	/**
	 * 用于存储自定义的公式
	 */
	private HashSet<DataDriverFunction> dataFunctionSet = new HashSet<>();

	/**
	 * 用于存储事件执行结果集合
	 */
	private ArrayList<String> resultList = new ArrayList<>();

	/**
	 * 指向点击事件
	 */
	private ClickEvent ce;
	/**
	 * 指向文本事件
	 */
	private TextEvent te;

	/**
	 * 构造对象
	 * 
	 * @param brower 浏览器{@link AbstractBrower}类对象
	 */
	public DimEvent(AbstractBrower brower) {
		super(brower);
		ce = new ClickEvent(brower);
		te = new TextEvent(brower);
	}

	/**
	 * 用于添加自定义的公式到类中，应用于对公式型的默认值进行转译
	 * <p>
	 * 方法支持传入正则表达式，当默认值满足传入的正则时，则会将其按照在公式中定义 的处理方式对数据进行处理，生成对应的数据。例如： <code><pre>
	 * addFunction(new DataDriverFunction("Wo.*", text -&gt; "Hello " + text + "!"));
	 * </pre></code>
	 * 当元素默认值为“${World}”时，其字符串“World”与正则表达式“Wo.*”匹配，故在事件的操作中，该字符串将被解析为“Hello World!”
	 * </p>
	 * 
	 * <p>
	 * <b>注意：</b>
	 * <ol>
	 * <li>公式的返回值和传参均为{@link String}类型</li>
	 * <li>在{@link Functions}类中已定义多个默认公式可供选择，其方法均为静态方法，返回值为{@link DataDriverFunction}</li>
	 * </ol>
	 * </p>
	 * 
	 * @param function 方法
	 */
	public void addFunction(DataDriverFunction function) {
		dataFunctionSet.add(Optional.ofNullable(function).orElseThrow(() -> new EventException("未指定公式")));
	}

	/**
	 * 用于根据传入的元素，自动选择相应的事件，并进行操作，返回操作结果
	 * <p>
	 * <b>注意：</b>
	 * <ol>
	 * <li>事件的返回值为每个元素进行操作后的事件返回值，若匹配到的事件为点击事件，则返回值存为空</li>
	 * <li>窗体元素等特殊的元素类型将无法被识别，传入该元素后，将抛出异常</li>
	 * </ol>
	 * </p>
	 * 
	 * @param elements 元素组
	 * @return 最后一个事件执行返回结果
	 */
	public String event(Element... elements) {
		Arrays.stream(Optional.ofNullable(elements).filter(es -> es.length > 0)
				.orElseThrow(() -> new EventException("未指定元素数组"))).forEach(element -> {
					element.againFindElement();
					// 获取元素信息
					ElementData ed = element.getElementData();

					// 获取元素的默认值
					String defaultValue = ed.getDefaultValue();
					// 提取默认值中公式的关键词，并对类中存储的公式进行匹配，得到转译后的替换词语map
					HashMap<String, String> repleatMap = repleat(FunctionUtil.getFunctionKey(defaultValue));

					// 对关键词进行替换
					for (String key : repleatMap.keySet()) {
						int index = -1;
						while ((index = defaultValue.indexOf(key)) > -1) {
							if (index == 0) {
								defaultValue = repleatMap.get(key) + defaultValue.substring(index + key.length());
							} else {
								defaultValue = defaultValue.substring(0, index) + repleatMap.get(key)
										+ defaultValue.substring(index + key.length());
							}
						}
					}

					// 根据元素的类型，对元素进行操作
					switch (ed.getElementType()) {
					case DATA_LIST_ELEMENT:
					case SELECT_DATAS_ELEMENT:
					case SELECT_OPTION_ELEMENT:
					case COMMON_ELEMENT:
						resultList.add(disposeCommonElement(element, defaultValue));
						break;
					case IFRAME_ELEMENT:
					default:
						throw new EventException(String.format("无法识别“%s”元素的类型：%s", ed.getName(), ed.getElementType()));
					}
				});

		return resultList.get(resultList.size() - 1);
	}

	/**
	 * 用于返回上一次调用模糊事件后的执行结果集合
	 * 
	 * @return 执行结果集合
	 */
	public ArrayList<String> getResultList() {
		return resultList;
	}

	/**
	 * 定义对普通型元素进行处理的方法
	 * 
	 * @param element      元素类对象
	 * @param defaultValue 元素的默认值
	 * @return 元素的操作结果
	 */
	private String disposeCommonElement(Element element, String defaultValue) {
		// 若默认值为空，则进行点击操作
		if (defaultValue.isEmpty()) {
			ce.click(element);
			return "";
		}

		// 若默认值为获取元素文本，则进行文本获取操作
		if (CONST_GET_TEXT_VALUE.equals(defaultValue)) {
			return te.getText(element);
		}

		// 若默认值为获取元素文本，则进行文本获取操作
		if (CONST_CLEAR_VALUE.equals(defaultValue)) {
			return te.clear(element);
		}

		// 若元素通过以上判断，则以输入事件，将默认值输入到元素中
		return te.input(element, defaultValue);
	}

	/**
	 * 根据公式关键词，与存储的公式进行匹配，返回替换的关键词以及替换结果
	 * 
	 * @param keySet 公式关键词
	 * @return 与公式正则匹配的结果
	 */
	private HashMap<String, String> repleat(Set<String> keySet) {
		HashMap<String, String> repleatMap = new HashMap<>(16);
		// 遍历提取的所有关键词
		keySet.forEach(key -> {
			// 将关键词与公式正则进行匹配，若匹配成功，则将相应的内容进行转译
			for (DataDriverFunction fun : dataFunctionSet) {
				if (fun.matchRegex(key)) {
					repleatMap.put(String.format("${%s}", key), fun.getFunction().apply(key));
				}
			}
		});

		return repleatMap;
	}
}
