package pres.auxiliary.work.selenium.element;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

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
 */
public class DataListBy extends ListBy {
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
	 * 用于判断列表的第一行元素是否为标题元素
	 */
	boolean isFristRowTitle = false;
	
	/**
	 * 用于存储元素列累计的个数
	 */
//	private HashMap<String, Integer> elementSizeMap = new HashMap<String, Integer>(16);
	
	/**
	 * 用于存储是否开始累加列表元素的个数
	 */
//	private boolean isStartAddSize = false;
	
	/**
	 * 通过浏览器对象{@link AbstractBrower}进行构造
	 * @param brower {@link AbstractBrower}对象
	 */
	public DataListBy(AbstractBrower brower) {
		super(brower);
	}

	/**
	 * 构造对象并存储浏览器的{@link WebDriver}对象
	 * 
	 * @param driver 浏览器的{@link WebDriver}对象
	 */
	public DataListBy(WebDriver driver) {
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
	
	@Override
	public void add(String name, ByType byType) {
		add(new ElementInformation(name, byType, ElementType.DATA_LIST_ELEMENT));
		
	}
	
	@Override
	void add(ElementInformation elementInformation) {
		//重写父类的add方法，使元素能进行极值的统计
		super.add(elementInformation);
		
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
	 * 用于返回列表多个指定的元素，传入的下标可参见{@link #getWebElement(String, int)}
	 * 
	 * @param name   列名称
	 * @param indexs 一组元素下标（即列表中对应的某一个元素）
	 * @return 对应列多个指定下标的元素
	 * @throws NoSuchElementException 当未对name列进行获取数据或index的绝对值大于列表最大值时抛出的异常
	 * @see #getWebElement(String, int)
	 */
	public List<Element> getElements(String name, int... indexs) {
		// 存储所有获取到的事件
		ArrayList<Element> events = new ArrayList<>();

		// 循环，解析所有的下标，并调用getEvent()方法，存储至events
		for (int index : indexs) {
			events.add(getElement(name, index));
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
	public List<Element> getRandomElements(String name, int length) {
		//获取元素信息，并判断元素是否存在，不存在则抛出异常
		ElementInformation element = nameToElementInformation(name);
		if (element == null) {
			throw new NoSuchElementException("不存在的定位方式：" + name);
		}
				
		// 判断传入的长度是否大于等于当前
		if (length >= elementMap.get(element).size()) {
			return getAllElement(name);
		}

		// 存储通过随机得到的数字
		ArrayList<Integer> indexsList = new ArrayList<Integer>();
		int randomLength = elementMap.get(element).size() + 1;
		// 循环，随机获取下标数字
		for (int i = 0; i < length; i++) {
			int randomIndex = 0;
			// 循环，直到生成的随机数不在indexs中为止
			while (indexsList.contains(randomIndex = new Random().nextInt(randomLength))) {
			}
			indexsList.add(randomIndex);
		}

		// 将indexsList转换成int[]
		int[] indexs = new int[indexsList.size()];
		for (int i = 0; i < indexsList.size(); i++) {
			indexs[i] = indexsList.get(i);
		}

		return getElements(name, indexs);
	}
	
	/*
	public void getLineWebElement(String name, int startIndex, int length) {
		if (length <= 0) {
			
		}
	}
	*/
	
	/**
	 * 用于根据参数，求取elementMap中最多或最少列表的元素个数以及列表的名称
	 * @param key 需要计算的最小值
	 * @return 极值以及极值所在的列
	 */
	private void findLimitColumn(ElementInformation key) {
		//获取指向的元素列表的元素个数
		int size = elementMap.get(key).size();
		
		//根据参数，判断获取的列是否为最大值所在的列，并对极值做进一步判断
		if (maxColumnSize < size) {
			maxColumnNameList.clear();
			maxColumnSize = size;
			maxColumnNameList.add(key.name);
		} else if (maxColumnSize == size) {
			maxColumnNameList.add(key.name);
		}
		
		if (minColumnSize > size) {
			minColumnNameList.clear();
			minColumnSize = size;
			minColumnNameList.add(key.name);
		} else if (minColumnSize == size) {
			minColumnNameList.add(key.name);
		}
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
