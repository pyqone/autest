package pres.auxiliary.work.selenium.element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Random;

import org.openqa.selenium.NoSuchElementException;

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
public class ListBy extends AbstractBy {
	/**
	 * 用于存储获取到的列表一列元素，key为列表名称，value为列表元素
	 */
	LinkedHashMap<ElementInformation, Element> elementMap = new LinkedHashMap<>(16);
	
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
	
	public void add(String name) {
		add(new ElementInformation(name, null, ElementType.DATA_LIST_ELEMENT, null));
	}
	
	public void add(String name, ByType byType) {
		add(new ElementInformation(name, byType, ElementType.DATA_LIST_ELEMENT, null));
	}
	
	public void add(String name, ByType byType, String... links) {
		add(new ElementInformation(name, byType, ElementType.DATA_LIST_ELEMENT, Arrays.asList(links)));
		
	}

	public void add(String name, String... links) {
		add(new ElementInformation(name, null, ElementType.DATA_LIST_ELEMENT, Arrays.asList(links)));
		
	}
	
	void add(ElementInformation elementInformation) {
		Element element = new Element(brower, elementInformation.name, elementInformation.elementType);
		element.setWaitTime(getWaitTime(elementInformation.name));
		element.setByList(recognitionElement(elementInformation));
		
		//构造元素的父层元素，若元素不存在窗体结构，则不进行构造
		if (elementInformation.iframeList != null && elementInformation.iframeList.size() != 0) {
			setIframeElement(element, elementInformation);
		}
		
		//添加元素
		elementMap.put(elementInformation, element);
	}
	
	/**
	 * 根据元素名称反推元素信息类对象，用于根据列名称查找数据以及判断列是否存在，若列名不存在，则返回null
	 * @return ElementInformation对象
	 */
	ElementInformation nameToElementInformation(String name, String...linkKey) {
		//遍历elementMap，若查找与name一致的名称，则结束循环并返回相应的ElementInformation对象
		for (ElementInformation elementInfo : elementMap.keySet()) {
			if (elementInfo.name.equals(name) && elementInfo.linkKeyEquals(linkKey)) {
				return elementInfo;
			}
		}
		
		return null;
	}
	
	/**
	 * 该方法用于根据列名称，查找到相应的列，并返回与传入下标对应的元素。下标支持从后向前获取，传入的下标
	 * 与元素实际所在位置一致，当传入0时，则表示随机获取一个元素，如：<br>
	 * {@code getWebElement("姓名", 1)}表示获取名称为“姓名”的列中的第1个元素<br>
	 * {@code getWebElement("姓名", 0)}表示获取名称为“姓名”的列中在长度范围内随机一个元素<br>
	 * {@code getWebElement("//*[@id='name']", -1)}表示获取“//*[@id='name']”对应列中的倒数第1个元素<br>
	 * 注意，若使用了外链xml词语，则需要将词语写入到传参中，否则无法获取到相应的元素
	 * 
	 * @param name 列名称
	 * @param index 元素下标（即列表中对应的某一个元素）
	 * @param linkKey 外链的词语，若不存在，则不传入
	 * @return 对应列指定的元素
	 * @throws NoSuchElementException 当未对name列进行获取数据或index的绝对值大于列表最大值时抛出的异常
	 */
	public Element getElement(String name, int index, String...linkKey) {
		//获取元素信息，并判断元素是否存在，不存在则抛出异常
		ElementInformation elementInfo = nameToElementInformation(name, linkKey);
		if (elementInfo == null) {
			throw new NoSuchElementException("不存在的定位方式：" + name);
		}
		
		// 转义下标
//		index = getIndex(elementMap.get(elementInfo).getSize(), index, true);
		Element element = elementMap.get(elementInfo);
		element.setElementIndex(index);
		// 转义下标后，返回对应的元素
		return element;
	}
	
	/**
	 * 由于方法允许传入负数和特殊数字0为下标，并且下标的序号由1开始，
	 * 故可通过该方法对下标的含义进行转义，得到java能识别的下标
	 * @param length 元素的个数
	 * @param index 传入的下标
	 * @param randomZero 标记是否可以随机出数字0
	 * @return 可识别的下标
	 * @throws NoSuchElementException 当元素无法查找到时抛出的异常
	 */
	int getIndex(int length, int index, boolean randomZero) {
		//判断元素下标是否超出范围，由于可以传入负数，故需要使用绝对值
		if (Math.abs(index) > length) {
			throw new NoSuchElementException("指定的选项值大于选项的最大值。选项总个数：" + length + "，指定项：" + index);
		}
		
		//判断index的值，若大于0，则从前向后遍历，若小于0，则从后往前遍历，若等于0，则随机输入
		if (index > 0) {
			//选择元素，正数的选项值从1开始，故需要减小1
			return index - 1;
		} else if (index < 0) {
			//选择元素，由于index为负数，则长度加上选项值即可得到需要选择的选项
			return length + index;
		} else {
			//为0，则进行随机选择，但需要判断是否允许随机出0（第一个元素）
			int newindex = 0;
			do {
				newindex = new Random().nextInt(length);
			} while(newindex == 0 && !randomZero);
			
			return newindex;
		}
	}
}
