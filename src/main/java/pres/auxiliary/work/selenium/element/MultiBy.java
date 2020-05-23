package pres.auxiliary.work.selenium.element;

import java.util.Random;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

import pres.auxiliary.selenium.xml.ByType;
import pres.auxiliary.work.selenium.brower.AbstractBrower;

/**
 * <p><b>文件名：</b>MultiElement.java</p>
 * <p><b>用途：</b>
 * 提供获取多个元素时使用的基本方法
 * </p>
 * <p><b>编码时间：</b>2020年5月22日上午7:54:28</p>
 * <p><b>修改时间：</b>2020年5月22日上午7:54:28</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public abstract class MultiBy extends AbstractBy {
	/**
	 * 通过浏览器对象{@link AbstractBrower}进行构造
	 * @param brower {@link AbstractBrower}对象
	 */
	public MultiBy(AbstractBrower brower) {
		super(brower);
	}

	/**
	 * 构造对象并存储浏览器的{@link WebDriver}对象
	 * 
	 * @param driver 浏览器的{@link WebDriver}对象
	 */
	public MultiBy(WebDriver driver) {
		super(driver);
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
		add(name, null);
	}
	
	/**
	 * 用于根据传入的元素在xml文件中的名称或者元素的定位内容，以及元素的定位方式，添加元素。
	 * @param name 元素在xml文件或者元素的定位内容
	 * @param byType 元素定位方式枚举对象（{@link ByType}枚举）
	 * @see #add(String)
	 * @throws TimeoutException 元素在指定时间内无法找到时抛出的异常
	 */
	public abstract void add(String name, ByType byType);
	
	/**
	 * 添加元素的底层方法
	 * @param elementInformation 元素信息类对象
	 * @param isAddSize 是否需要统计
	 */
	abstract void add(ElementInformation elementInformation);
	
	/**
	 * 该方法用于根据存入的元素名称或定位方式，对元素进行重新获取的操作。主要用于当列表数据翻页后，
	 * 其原存入的数据将会失效，必须重新获取。注意，调用该方法后会清空原存储的数据。
	 */
	public abstract void againGetElement();
	
	/**
	 * 用于清除或移除指定的列及列中的元素，当参数传入false时，则只清理列表中存储的元素，不移除
	 * 整个列，若传入true时，则直接移除整个列。若列名称对应的列未被获取，则返回null
	 * @param name 已被获取的元素列名称
	 * @param isRemove 是否需要将该列移除
	 * @return 被移除列中存储的所有元素
	 */
//	public abstract List<Element> clearColumn(String name, boolean isRemove);
	
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
