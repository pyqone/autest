package pres.auxiliary.work.selenium.element.delect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.NoSuchElementException;

import pres.auxiliary.work.selenium.brower.AbstractBrower;

/**
 * <p><b>文件名：</b>DataListElement.java</p>
 * <p><b>用途：</b>
 * 提供在辅助化测试中，对页面列表元素获取的方法，并对列表元素的获取做了优化。
 * 类中获取元素的方法兼容传入定位方式对元素进行查找，建议使用xml对页面元素的
 * 定位方式进行存储，以简化编码时的代码量，也便于对代码的维护。
 * </p>
 * <p><b>编码时间：</b>2020年4月29日下午6:18:46</p>
 * <p><b>修改时间：</b>2020年4月29日下午6:18:46</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 8
 */
public class DataListBy extends ListBy {
	//TODO 添加获取最大列方法
	
	/**
	 * 通过浏览器对象{@link AbstractBrower}进行构造
	 * @param brower {@link AbstractBrower}对象
	 */
	public DataListBy(AbstractBrower brower) {
		super(brower);
	}

	/**
	 * 返回元素最多列的元素个数
	 * @return 元素最多列的元素个数
	 */
//	public int getMaxColumnSize() {
//		return maxColumnSize;
//	}

	/**
	 * 返回元素最多列的列名称
	 * @return 元素最多列的列名称
	 */
//	public List<String> getMaxColumnName() {
//		return maxColumnNameList;
//	}

	/**
	 * 返回元素最少列的元素个数
	 * @return 元素最少列的元素个数
	 */
//	public int getMinColumnSize() {
//		return minColumnSize;
//	}

	/**
	 * 返回元素最少列的列名称
	 * @return 元素最少列的列名称
	 */
//	public List<String> getMinColumnName() {
//		return minColumnNameList;
//	}
	
	/**
	 * 用于返回列表多个指定的元素，传入的下标可参见{@link #getWebElement(String, int)}
	 * 
	 * @param name   列名称
	 * @param indexs 一组元素下标（即列表中对应的某一个元素）
	 * @return 对应列多个指定下标的元素
	 * @throws NoSuchElementException 当未对name列进行获取数据或index的绝对值大于列表最大值时抛出的异常
	 * @see #getWebElement(String, int)
	 */
//	public List<Element> getElements(String name, int... indexs) {
//		// 存储所有获取到的事件
//		ArrayList<Element> events = new ArrayList<>();
//
//		// 循环，解析所有的下标，并调用getEvent()方法，存储至events
//		for (int index : indexs) {
//			events.add(getElement(name, index));
//		}
//
//		return events;
//	}
	
	/**
	 * 根据元素列名称与外链词语，查找并返回该列所有的元素对象，。注意，调用该方法时，若未对元素
	 * 进行查找，则会先查找一次元素，此时，若元素无法找到，则可能抛出异常
	 * @param name 元素名称
	 * @param linkKey 外链的词语，若不存在，则不传入
	 * @return 指定列中所有的元素对象
	 * @throws NoSuchElementException 当未对name列进行获取数据或index的绝对值大于列表最大值时抛出的异常
	 */
	public List<Element> getAllElement(String name, String...linkKey) {
		//根据元素名称和外链词语，获取元素信息，用于获取元素
		ElementInformation elementInfo = nameToElementInformation(name, Arrays.asList(linkKey));
		
		//若元素不存在，则抛出异常
		if (elementInfo == null) {
			throw new NoSuchElementException("不存在的元素或外链词语不正确：" + name + "=" + Arrays.asList(linkKey));
		}
		
		//获取名称对应的元素
		Element element = elementMap.get(elementInfo);
		//获取元素能查找到的个数
		int size = element.getSize();
		//判断获取到的元素其长度是否为-1，若是-1，则表示未对元素进行获取，需要先对元素进行一次获取，再重新获取元素个数
		if (element.getSize() == -1) {
			element.getWebElement();
			size = element.getSize();
		}
		
		// 存储克隆的元素
		ArrayList<Element> elementList = new ArrayList<>();
		//循环，克隆并存储元素
		for (int index = 0; index < size; index++) {
			Element cloneElement = element.clone();
			//下标从1开始，0为随机
			cloneElement.setElementIndex(index + 1);
			elementList.add(cloneElement);
		}
		
		return elementList;
	}
	
	/**
	 * 用于根据列名称，查找并返回相应列的第一个元素
	 * @param name 元素名称
	 * @param linkKey 外链的词语，若不存在，则不传入
	 * @return 对应列指定的元素
	 */
	public Element getElement(String name, String...linkKey) {
		return getElement(name, 0, linkKey);
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
	 */
	public Element getElement(String name, int index, String...linkKey) {
		//获取元素信息，并判断元素是否存在，不存在则抛出异常
		ElementInformation elementInfo = nameToElementInformation(name, Arrays.asList(linkKey));
		if (elementInfo == null) {
			throw new NoSuchElementException("不存在的元素：" + name);
		}
		
		Element element = elementMap.get(elementInfo);
		element.setElementIndex(index);
		return element;
	}
	
	@Override
	ElementType setElementType() {
		return ElementType.DATA_LIST_ELEMENT;
	}
}
