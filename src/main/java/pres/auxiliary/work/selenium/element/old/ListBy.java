package pres.auxiliary.work.selenium.element.old;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import pres.auxiliary.work.selenium.brower.AbstractBrower;

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
public abstract class ListBy extends MultiBy {
	/**
	 * 用于存储获取到的列表一列元素，key为列表名称，value为列表元素
	 */
	LinkedHashMap<ElementInformation, List<Element>> elementMap = new LinkedHashMap<>(16);
	
	/**
	 * 通过浏览器对象{@link AbstractBrower}进行构造
	 * @param brower {@link AbstractBrower}对象
	 */
	public ListBy(AbstractBrower brower) {
		super(brower);
	}

	/**
	 * 构造对象并存储浏览器的{@link WebDriver}对象
	 * 
	 * @param driver 浏览器的{@link WebDriver}对象
	 */
	public ListBy(WebDriver driver) {
		super(driver);
	}
	
	/**
	 * 通过{@link AbstractBy}对象对类进行构造，将传入的AbstractBy类中的关键参数设置到当前类对象中
	 * @param brower {@link AbstractBy}对象
	 */
	public ListBy(AbstractBy by) {
		super(by);
	}
	
	/**
	 * 返回列表名称对应的元素个数，若该列未被获取，则返回-1
	 * @param name 被获取的列名称
	 * @return 列名对应列的元素个数
	 */
	public int getSize(String name) {
		ElementInformation element = nameToElementInformation(name);
		if (element != null) {
			return elementMap.get(element).size();
		} else {
			return -1;
		}
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
	
	@Override
	void add(ElementInformation elementInformation) {
		//判断传入的元素是否在xml文件中，若存在再判断是否自动切换窗体，若需要，则获取元素的所有父窗体并进行切换
		if (xml != null && xml.isElement(elementInformation.name) && isAutoSwitchIframe) {
			switchFrame(getParentFrameName(elementInformation.name));
		}
				
		List<Element> elementList = new ArrayList<Element>();
		//获取元素
		By by = recognitionElement(elementInformation);
		int size = driver.findElements(by).size();
		//构造Element对象
		for (int i = 0; i < size; i++) {
			elementList.add(new Element(driver, ElementType.DATA_LIST_ELEMENT, by, elementInformation.name, i));
		}
		//elementList = driver.findElements(recognitionElement(elementInformation));
		//添加元素
		elementMap.put(elementInformation, elementList);
	}
	
	/**
	 * 用于清除或移除指定的列及列中的元素，当参数传入false时，则只清理列表中存储的元素，不移除
	 * 整个列，若传入true时，则直接移除整个列。若列名称对应的列未被获取，则返回null
	 * @param name 已被获取的元素列名称
	 * @param isRemove 是否需要将该列移除
	 * @return 被移除列中存储的所有元素
	 */
	public List<Element> clearColumn(String name, boolean isRemove) {
		ElementInformation element = nameToElementInformation(name);
		//若元素不存在，则直接返回null
		if (element == null) {
			return null;
		}
		
		//用于存储被移除的元素
		List<Element> elementList = elementMap.get(element);
		//判断元素是否需要被完全移除
		if (isRemove) {
			//若元素需要被完全移除，则直接移除元素
			elementMap.remove(element);
			//由于元素被移除，若该列存在元素个数统计，则同样将该元素移除
			/*
			if (elementSizeMap.containsKey(name)) {
				elementSizeMap.remove(name);
			}
			*/
		} else {
			//若元素无需移除，则将元素存储的列表内容清空
			elementMap.get(element).clear();
		}
		
		return elementList;
	}
	
	@Override
	public void againGetElement() {
		// 读取elements中的元素
		elementMap.forEach((key, value) -> {
			// 清空元素中的内容
			clearColumn(key.name, false);
			// 对页面内容重新进行获取
			add(key);
		});
	}
	
	/**
	 * 根据元素名称反推元素信息类对象，用于根据列名称查找数据以及判断列是否存在，若列名不存在，则返回null
	 * @return ElementInformation对象
	 */
	ElementInformation nameToElementInformation(String name) {
		//遍历elementMap，若查找与name一致的名称，则结束循环并返回相应的ElementInformation对象
		for (ElementInformation element : elementMap.keySet()) {
			if (element.name.equals(name)) {
				return element;
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
	 * 
	 * @param name 列名称
	 * @param index 元素下标（即列表中对应的某一个元素）
	 * @return 对应列指定的元素
	 * @throws NoSuchElementException 当未对name列进行获取数据或index的绝对值大于列表最大值时抛出的异常
	 */
	public Element getElement(String name, int index) {
		//获取元素信息，并判断元素是否存在，不存在则抛出异常
		ElementInformation element = nameToElementInformation(name);
		if (element == null) {
			throw new NoSuchElementException("不存在的定位方式：" + name);
		}
		
		// 转义下标
		index = getIndex(elementMap.get(element).size(), index, true);

		// 转义下标后，返回对应的元素
		return elementMap.get(element).get(index);
	}
	
	/**
	 * 该方法用于根据列名称，获取该列下所有的元素
	 * 
	 * @param name 列名称
	 * @return 对应列元素
	 * @throws NoSuchElementException 当未对name列进行获取数据时抛出的异常
	 */
	public List<Element> getAllElement(String name) {
		//获取元素信息，并判断元素是否存在，不存在则抛出异常
		ElementInformation element = nameToElementInformation(name);
		if (element == null) {
			throw new NoSuchElementException("不存在的定位方式：" + name);
		}
		
		return elementMap.get(element);
	}
}
