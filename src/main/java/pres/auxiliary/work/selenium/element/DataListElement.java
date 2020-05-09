package pres.auxiliary.work.selenium.element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import pres.auxiliary.selenium.event.DataListEvent.ListEvent;
import pres.auxiliary.selenium.xml.ByType;
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
 * @since JDK 12
 *
 */
public class DataListElement extends AbstractElement {
	/**
	 * 用于存储获取到的列表一列元素，key为列表名称，value为列表元素
	 */
	private LinkedHashMap<ElementInformation, List<WebElement>> elementMap = new LinkedHashMap<>(16);
	
	/**
	 * 用于存储元素列累计的个数
	 */
	private HashMap<String, Integer> elementSizeMap = new HashMap<String, Integer>(16);
	
	/**
	 * 用于存储是否开始累加列表元素的个数
	 */
//	private boolean isStartAddSize = false;
	
	/**
	 * 用于判断列表的第一行元素是否为标题元素
	 */
	private boolean isFristRowTitle = false;
	
	/**
	 * 存储获取到的元素列表中最多元素列的元素个数
	 */
	private int maxColumnSize = -1;
	/**
	 * 存储获取到的元素列表中最多元素列的名称
	 */
	private ArrayList<String> maxColumnNameList = new ArrayList<>();
	/**
	 * 存储获取到的元素列表中最少元素列的元素个数
	 */
	private int minColumnSize = Integer.MAX_VALUE;
	/**
	 * 存储获取到的元素列表中最少元素列的名称
	 */
	private ArrayList<String> minColumnNameList = new ArrayList<>();
	
	/**
	 * 通过浏览器对象{@link AbstractBrower}进行构造
	 * @param brower {@link AbstractBrower}对象
	 */
	public DataListElement(AbstractBrower brower) {
		super(brower);
	}

	/**
	 * 构造对象并存储浏览器的WebDriver对象
	 * 
	 * @param driver 浏览器的WebDriver对象
	 */
	public DataListElement(WebDriver driver) {
		super(driver);
	}
	
	/**
	 * 用于设置首行元素是否为标题元素
	 * @param isFristRowTitle 首行是否为标题元素
	 */
	public void setFristRowTitle(boolean isFristRowTitle) {
		this.isFristRowTitle = isFristRowTitle;
	}

	/**
	 * 用于设置是否开始计算元素个数
	 * @param isStartAddSize 是否开始计算元素个数
	 */
	/*
	public void setStartAddSize(boolean isStartAddSize) {
		this.isStartAddSize = isStartAddSize;
	}
	*/
	
	/**
	 * 返回元素最多列的元素个数
	 * @return 元素最多列的元素个数
	 */
	public int getMaxColumnSize() {
		return maxColumnSize;
	}

	/**
	 * 返回元素最多列的列名称
	 * @return 元素最多列的列名称
	 */
	public List<String> getMaxColumnName() {
		return maxColumnNameList;
	}

	/**
	 * 返回元素最少列的元素个数
	 * @return 元素最少列的元素个数
	 */
	public int getMinColumnSize() {
		return minColumnSize;
	}

	/**
	 * 返回元素最少列的列名称
	 * @return 元素最少列的列名称
	 */
	public List<String> getMinColumnName() {
		return minColumnNameList;
	}
	
	/**
	 * 返回列表名称对应的元素个数，若该列未被获取，则返回-1
	 * @param name 被获取的列名称
	 * @return 列名对应列的元素个数
	 */
	public int getColumnSize(String name) {
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
	public ArrayList<String> getColumnName() {
		ArrayList<String> nameList = new ArrayList<>();
		elementMap.forEach((key, value) -> {
			nameList.add(key.name);
		});
		
		return nameList;
	}
	
	/**
	 * 用于根据传入的元素在xml文件中的名称或者元素的定位内容，添加元素列。由于该方法不指定元素的定位
	 * 方式，若传入的参数不是xml元素且非xpath路径或绝对css路径时，其识别效率较慢，建议在该情况下
	 * 调用{@link #add(String, ByType)}方法，指定元素定位方法
	 * @param name 元素在xml文件或者元素的定位内容
//	 * @param isAddSize 是否对元素个数进行统计
	 * @see #add(String, ByType)
	 * @throws TimeoutException 元素在指定时间内无法找到时抛出的异常
	 */
	public void add(String name) {
		add(name, null);
	}
	
	/**
	 * 用于根据传入的元素在xml文件中的名称或者元素的定位内容，以及元素的定位方式，添加元素列。
	 * @param name 元素在xml文件或者元素的定位内容
	 * @param byType 元素定位方式枚举对象（{@link ByType}枚举）
//	 * @param isAddSize 是否对元素个数进行统计
	 * @see #add(String)
	 * @throws TimeoutException 元素在指定时间内无法找到时抛出的异常
	 */
	public void add(String name, ByType byType) {
		
	}
	
	/**
	 * 添加元素的底层方法
	 * @param elementInformation 元素信息类对象
	 * @param isAddSize 是否需要统计
	 */
	private void add(ElementInformation elementInformation) {
		List<WebElement> elementList = new ArrayList<WebElement>();
		//获取元素
		elementList = recognitionElement(elementInformation);
		//添加元素
		elementMap.put(elementInformation, elementList);
		
		//判断当前获取到的元素是否大于当前存储的元素的最大值或小于当前存储的最小值，若是，则将最大值或最小值进行替换
		findLimitColumn(elementInformation);
		
		/*
		//判断元素是否需统计，若元素需要统计，则对元素列表的个数加上当前获取到的元素个数；若不需要统计，则移除该元素
		if (isAddSize) {
			if (elementSizeMap.containsKey(elementInformation.name)) {
				elementSizeMap.put(elementInformation.name, 0);
			}
			
			if (isStartAddSize) {
				elementSizeMap.put(elementInformation.name, elementSizeMap.get(elementInformation.name) + elementList.size());
			}
		} else {
			if (elementSizeMap.containsKey(elementInformation.name)) {
				elementSizeMap.remove(elementInformation.name);
			}
		}
		*/
	}
	
	/**
	 * 用于清除或移除指定的列及列中的元素，当参数传入false时，则只清理列表中存储的元素，不移除
	 * 整个列，若传入true时，则直接移除整个列。若列名称对应的列未被获取，则返回null
	 * @param name 已被获取的元素列名称
	 * @param isRemove 是否需要将该列移除
	 * @return 被移除列中存储的所有元素
	 */
	public List<WebElement> clearColumn(String name, boolean isRemove) {
		ElementInformation element = nameToElementInformation(name);
		//若元素不存在，则直接返回null
		if (element == null) {
			return null;
		}
		
		//用于存储被移除的元素
		List<WebElement> elementList = elementMap.get(element);
		//判断元素是否需要被完全移除
		if (isRemove) {
			//若元素需要被完全移除，则直接移除元素
			elementMap.remove(element);
			//由于元素被移除，若该列存在元素个数统计，则同样将该元素移除
			if (elementSizeMap.containsKey(name)) {
				elementSizeMap.remove(name);
			}
		} else {
			//若元素无需移除，则将元素存储的列表内容清空
			elementMap.get(element).clear();
		}
		
		return elementList;
	}
	
	/**
	 * 该方法用于根据存入的元素名称或定位方式，对元素进行重新获取的操作。主要用于当列表数据翻页后，
	 * 其原存入的数据将会失效，必须重新获取。注意，调用该方法后会清空原存储的数据。
	 */
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
	 * 该方法用于根据列名称，查找到相应的列，并返回与传入下标对应的元素。下标支持从后向前获取，传入的下标
	 * 与元素实际所在位置一致，当传入0时，则表示随机获取一个元素，如：<br>
	 * {@code getWebElement("姓名", 1)}表示获取名称为“姓名”的列中的第1个元素<br>
	 * {@code getWebElement("姓名", 0)}表示获取名称为“姓名”的列中在长度范围内随机一个元素<br>
	 * {@code getWebElement("//*[@id='name']", -1)}表示获“//*[@id='name']”对应列中的倒数第1个元素<br>
	 * 
	 * @param name 列名称
	 * @param index 元素下标（即列表中对应的某一个元素）
	 * @return 对应列指定的元素
	 * @throws NoSuchElementException 当未对name列进行获取数据或index的绝对值大于列表最大值时抛出的异常
	 */
	public WebElement getWebElement(String name, int index) {
		//获取元素信息，并判断元素是否存在，不存在则抛出异常
		ElementInformation element = nameToElementInformation(name);
		if (element == null) {
			throw new NoSuchElementException("不存在的定位方式：" + name);
		}
		
		// 转义下标
		index = getIndex(element, index);

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
	public List<WebElement> getAllWebElement(String name) {
		//获取元素信息，并判断元素是否存在，不存在则抛出异常
		ElementInformation element = nameToElementInformation(name);
		if (element == null) {
			throw new NoSuchElementException("不存在的定位方式：" + name);
		}
		
		return elementMap.get(element);
	}
	
	/**
	 * 用于返回列表多个指定的元素，传入的下标可参见{@link #getWebElement(String, int)}
	 * 
	 * @param name   列名称
	 * @param indexs 一组元素下标（即列表中对应的某一个元素）
	 * @return 对应列多个指定下标的元素
	 * @throws NoSuchElementException 当未对name列进行获取数据或index的绝对值大于列表最大值时抛出的异常
	 * @see #getWebElement(String, int)
	 */
	public List<WebElement> getWebElements(String name, int... indexs) {
		// 存储所有获取到的事件
		ArrayList<WebElement> events = new ArrayList<>();

		// 循环，解析所有的下标，并调用getEvent()方法，存储至events
		for (int index : indexs) {
			events.add(getWebElement(name, index));
		}

		return events;
	}
	
	/**
	 * 用于返回列表中指定随机个数的元素
	 * 
	 * @param name 列名称
	 * @param length 需要返回列表事件的个数
	 * @return 列表事件组
	 */
	public List<WebElement> getRandomWebElements(String name, int length) {
		// 判断传入的长度是否大于等于当前
		if (length >= elements.get(name).size()) {
			return getEvents(name);
		}

		// 存储通过随机得到的数字
		ArrayList<Integer> indexsList = new ArrayList<Integer>();
		// 循环，随机获取下标数字
		for (int i = 0; i < length; i++) {
			int randomIndex = 0;
			// 循环，直到生成的随机数不在indexs中为止
			while (indexsList.contains(randomIndex = new Random().nextInt(elements.get(name).size()) + 1)) {
			}
			indexsList.add(randomIndex);
		}

		// 将indexsList转换成int[]
		int[] indexs = new int[indexsList.size()];
		for (int i = 0; i < indexsList.size(); i++) {
			indexs[i] = indexsList.get(i);
		}

		return getEvents(name, indexs);
	}
	
	/**
	 * 用于根据参数，求取elementMap中最多或最少列表的元素个数以及列表的名称
	 * @param isMax 是否为求取最大值
	 * @return 极值以及极值所在的列
	 */
	private void findLimitColumn(ElementInformation key) {
		//获取指向的元素列表的元素个数
		int size = elementMap.get(key).size();
		
		//根据参数，判断获取的列是否为最大值所在的列，并对极值做进一步判断
		if (maxColumnSize < size) {
			
			if (maxColumnSize < size) {
				maxColumnNameList.clear();
				maxColumnSize = size;
			}
			maxColumnNameList.add(key.name);
		} else {
			//对比当前存储的最小值与列的元素个数，其操作与求取最大值相反
			if (minColumnSize < size) {
				return;
			}
			
			if (minColumnSize > size) {
				minColumnNameList.clear();
				minColumnSize = size;
			}
			minColumnNameList.add(key.name);
		}
	}
	
	/**
	 * 由于方法允许传入负数和特殊数字0为下标，并且下标的序号由1开始，
	 * 故可通过该方法对下标的含义进行转义，得到java能识别的下标
	 * @param element 元素信息类对象
	 * @param index 传入的下标
	 * @return 可识别的下标
	 * @throws NoSuchElementException 当元素无法查找到时抛出的异常
	 */
	int getIndex(ElementInformation element, int index) {
		int length = elementMap.get(element).size();
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
			//为0，则随机进行选择
			return new Random().nextInt(length);
		}
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
		
	@Override
	boolean isExistElement(By by, long waitTime) {
		//TODO 修改等待方法，改为获取到元素后才返回相应的状态
		//当查找到元素时，则返回true，若查不到元素，则会抛出异常，故返回false
		return new WebDriverWait(driver, waitTime, 200).
				until((driver) -> {
					List<WebElement> elementList = driver.findElements(by);
					
					//若获取的标题首行为标题行时，则判断为获取到大于1个元素时返回true，否则则大于0个元素返回true
					if (isFristRowTitle) {
						return elementList.size() > 1;
					} else {
						return elementList.size() > 0;
					}
				});
	}
}
