package pres.auxiliary.work.selenium.element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
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
 *
 */
public class DataListElement extends AbstractElement {
	/**
	 * 用于存储获取到的列表一列元素，key为列表名称，value为列表元素
	 */
	private HashMap<String, List<WebElement>> elementMap = new HashMap<>(16);
	
	/**
	 * 用于存储元素列累计的个数
	 */
	private HashMap<String, Integer> elementSizeMap = new HashMap<String, Integer>(16);
	
	/**
	 * 用于存储是否开始累加列表元素的个数
	 */
	private boolean isStartAddSize = false;
	
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
	private String maxColumnName = "";
	/**
	 * 存储获取到的元素列表中最少元素列的元素个数
	 */
	private int minColumnSize = Integer.MAX_VALUE;
	/**
	 * 存储获取到的元素列表中最少元素列的名称
	 */
	private String minColumnName = "";
	
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
	public void setStartAddSize(boolean isStartAddSize) {
		this.isStartAddSize = isStartAddSize;
	}
	
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
	public String getMaxColumnName() {
		return maxColumnName;
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
	public String getMinColumnName() {
		return minColumnName;
	}
	
	/**
	 * 返回列表名称对应的元素个数，若该列未被获取，则返回-1
	 * @param name 被获取的列名称
	 * @return 列名对应列的元素个数
	 */
	public int getColumnSize(String name) {
		if (elementMap.containsKey(name)) {
			return elementMap.get(name).size();
		} else {
			return -1;
		}
	}
	
	/**
	 * 用于根据传入的元素在xml文件中的名称或者元素的定位内容，添加元素列。由于该方法不指定元素的定位
	 * 方式，若传入的参数不是xml元素且非xpath路径或绝对css路径时，其识别效率较慢，建议在该情况下
	 * 调用{@link #add(String, ByType)}方法，指定元素定位方法
	 * @param name 元素在xml文件或者元素的定位内容
	 * @param isAddSize 是否对元素个数进行统计
	 * @see #add(String, ByType)
	 * @throws TimeoutException 元素在指定时间内无法找到时抛出的异常
	 */
	public void add(String name, boolean isAddSize) {
		add(name, null, isAddSize);
	}
	
	/**
	 * 用于根据传入的元素在xml文件中的名称或者元素的定位内容，以及元素的定位方式，添加元素列。
	 * @param name 元素在xml文件或者元素的定位内容
	 * @param byType 元素定位方式枚举对象（{@link ByType}枚举）
	 * @param isAddSize 是否对元素个数进行统计
	 * @see #add(String)
	 * @throws TimeoutException 元素在指定时间内无法找到时抛出的异常
	 */
	public void add(String name, ByType byType, boolean isAddSize) {
		List<WebElement> elementList = new ArrayList<WebElement>();
		//获取元素
		elementList = recognitionElement(name, byType);
		//添加元素
		elementMap.put(name, elementList);
		
		//判断当前获取到的元素是否大于当前存储的元素的最大值或小于当前存储的最小值，若是，则将最大值或最小值进行替换
		int elementSize = elementList.size();
		if (elementSize > maxColumnSize) {
			maxColumnSize = elementSize;
			maxColumnName = name;
		} else if (elementSize < minColumnSize) {
			minColumnSize = elementSize;
			minColumnName = name;
		} else {
		}
		
		//判断元素是否需统计，若元素需要统计，则对元素列表的个数加上当前获取到的元素个数；若不需要统计，则移除该元素
		if (isAddSize) {
			if (elementSizeMap.containsKey(name)) {
				elementSizeMap.put(name, 0);
			}
			
			if (isStartAddSize) {
				elementSizeMap.put(name, elementSizeMap.get(name) + elementSize);
			}
		} else {
			if (elementSizeMap.containsKey(name)) {
				elementSizeMap.remove(name);
			}
		}
	}
	
	/**
	 * 用于清除或移除指定的列及列中的元素，当参数传入false时，则只清理列表中存储的元素，不移除
	 * 整个列，若传入true时，则直接移除整个列。若列名称对应的列未被获取，则返回null
	 * @param name 已被获取的元素列名称
	 * @param isRemove 是否需要将该列移除
	 * @return 被移除列中存储的所有元素
	 */
	public List<WebElement> clearColumn(String name, boolean isRemove) {
		//若元素不存在，则直接返回null
		if (elementMap.containsKey(name)) {
			return null;
		}
		
		//用于存储被移除的元素
		List<WebElement> elementList = elementMap.get(name);
		//判断元素是否需要被完全移除
		if (isRemove) {
			//若元素需要被完全移除，则直接移除元素
			elementMap.remove(name);
			//由于元素被移除，若该列存在元素个数统计，则同样将该元素移除
			if (elementSizeMap.containsKey(name)) {
				elementSizeMap.remove(name);
			}
		} else {
			//若元素无需移除，则将元素存储的列表内容清空
			elementMap.get(name).clear();
		}
		
		return elementList;
	}
	
	private int findColumnLimitSize(boolean isMax) {
		//根据传入的参数判断是获取最大值还是获取最小值，根据不同的类型初始化不同的参数
		int limit = isMax ? -1 : Integer.MAX_VALUE;
		
		//TODO 获取极值
		elementMap.forEach((key, value) -> {
			
		});
		
		return limit;
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
