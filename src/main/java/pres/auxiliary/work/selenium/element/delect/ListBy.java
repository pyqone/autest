package pres.auxiliary.work.selenium.element.delect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.openqa.selenium.TimeoutException;

import pres.auxiliary.work.selenium.brower.AbstractBrower;
import pres.auxiliary.work.selenium.xml.ByType;

/**
 * <p><b>文件名：</b>ListElement.java</p>
 * <p><b>用途：</b>
 * 提供获取列表类型元素时的基本方法
 * </p>
 * <p><b>编码时间：</b>2020年5月22日上午7:54:55</p>
 * <p><b>修改时间：</b>2020年5月22日上午7:54:55</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public abstract class ListBy extends AbstractBy {
	/**
	 * 用于存储获取到的列表一列元素，key为列表名称，value为列表元素
	 */
	LinkedHashMap<ElementInformation, Element> elementMap = new LinkedHashMap<>(16);
	
	/**
	 * 控制元素首行是否为
	 */
	private boolean fristIsEmpty = false;
	
	/**
	 * 通过浏览器对象{@link AbstractBrower}进行构造
	 * @param brower {@link AbstractBrower}对象
	 */
	public ListBy(AbstractBrower brower) {
		super(brower);
	}
	
	/**
	 * 该方法用于返回所有列的名称
	 * @return 所有列的名称
	 */
	public ArrayList<String> getNames() {
		ArrayList<String> nameList = new ArrayList<>();
		elementMap.forEach((key, value) -> {
			nameList.add(key.name);
		});
		
		return nameList;
	}
	
	/**
	 * 设置首个选项是否为不可选择的选项
	 * @param fristIsEmpty 首个选项是否为不可选择
	 */
	public void setFristIsEmpty(boolean fristIsEmpty) {
		this.fristIsEmpty = fristIsEmpty;
	}
	
	/**
	 * 用于清空存储的内容
	 */
	public void clear() {
		elementMap.clear();
	}
	
	/**
	 * 用于根据传入的元素在xml文件中的名称或者元素的定位内容，添加元素。由于该方法不指定元素的定位
	 * 方式，若传入的参数不是xml元素且非xpath路径或绝对css路径时，其识别效率较慢，建议在该情况下
	 * 调用{@link #add(String, ByType)}方法，指定元素定位方法
	 * @param name 元素在xml文件或者元素的定位内容
	 * @see #add(String, ByType)
	 * @throws TimeoutException 元素在指定时间内无法找到时抛出的异常
	 */
	public void add(String name) {
		add(new ElementInformation(name, null, setElementType(), null));
	}
	
	/**
	 * 用于根据传入的元素在xml文件中的名称或者元素的定位内容，以及元素的定位方式，添加元素。
	 * @param name 元素在xml文件或者元素的定位内容
	 * @param byType 元素定位方式枚举对象（{@link ByType}枚举）
	 * @see #add(String)
	 * @throws TimeoutException 元素在指定时间内无法找到时抛出的异常
	 */
	public void add(String name, ByType byType) {
		add(new ElementInformation(name, byType, setElementType(), null));
	}
	
	/**
	 * 用于根据传入的元素在xml文件中的名称或者元素的定位内容，以及元素的定位方式，添加元素。
	 * 该方法可对由xml文件读取的内容进行词语替换，根据
	 * 传参中词语的顺序，对需要替换的词语进行替换
	 * @param name 元素在xml文件或者元素的定位内容
	 * @param byType 元素定位方式枚举对象（{@link ByType}枚举）
	 * @param links 替换词语
	 * @see #add(String, ByType)
	 * @throws TimeoutException 元素在指定时间内无法找到时抛出的异常
	 */
	public void add(String name, ByType byType, String... links) {
		add(new ElementInformation(name, byType, setElementType(), Arrays.asList(links)));
		
	}

	/**
	 * 用于根据传入的元素在xml文件中的名称或者元素的定位内容，添加元素。由于该方法不指定元素的定位
	 * 方式，若传入的参数不是xml元素且非xpath路径或绝对css路径时，其识别效率较慢，建议在该情况下
	 * 调用{@link #add(String, ByType)}方法，指定元素定位方法。该方法可对由xml文件读取的内容进
	 * 行词语替换，根据传参中词语的顺序，对需要替换的词语进行替换
	 * @param name 元素在xml文件或者元素的定位内容
	 * @param links 替换词语
	 * @see #add(String)
	 * @throws TimeoutException 元素在指定时间内无法找到时抛出的异常
	 */
	public void add(String name, String... links) {
		add(new ElementInformation(name, null, ElementType.DATA_LIST_ELEMENT, Arrays.asList(links)));
		
	}
	
	/**
	 * 添加元素的底层方法
	 * @param elementInformation 元素信息类对象
	 */
	void add(ElementInformation elementInformation) {
		Element element = new Element(brower, elementInformation.name, elementInformation.elementType);
		element.setWaitTime(getWaitTime(elementInformation.name));
		element.setByList(recognitionElement(elementInformation));
		//若首元素为空元素或不允许选择的选项，则表示随机时不允许出现第一个选项
		element.setRandomZero(!fristIsEmpty);
		
		//构造元素的父层元素，若元素不存在窗体结构，则不进行构造
		if (elementInformation.iframeList != null && elementInformation.iframeList.size() != 0) {
			setIframeElement(element, elementInformation);
		}
		
		//添加元素
		elementMap.put(elementInformation, element);
	}
	
	/**
	 * 用于设置元素的类型，根据该类型来存储元素信息
	 * @return
	 */
	abstract ElementType setElementType();
	
	/**
	 * 根据元素名称反推元素信息类对象，用于根据列名称查找数据以及判断列是否存在，若列名不存在，则返回null
	 * @return ElementInformation对象
	 */
	ElementInformation nameToElementInformation(String name, List<String> linkKey) {
		//遍历elementMap，若查找与name一致的名称，则结束循环并返回相应的ElementInformation对象
		for (ElementInformation elementInfo : elementMap.keySet()) {
			if (elementInfo.name.equals(name) && elementInfo.linkKeyEquals(linkKey)) {
				return elementInfo;
			}
		}
		
		return null;
	}
}
