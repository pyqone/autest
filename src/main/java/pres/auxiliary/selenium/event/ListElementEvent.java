package pres.auxiliary.selenium.event;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import pres.auxiliary.selenium.tool.RecordTool;

public abstract class ListElementEvent extends AbstractEvent {
	/**
	 * 用于标记首行是否为标题行
	 */
	private boolean fristRowTitle = false;
	
	/**
	 * 用于存储一列列表的数据并返回其相应的列表事件
	 */
	protected LinkedHashMap<String, ArrayList<WebElement>> elements = new LinkedHashMap<>(16);

	/**
	 * 当列表元素获取为空时，设置的重新获取次数
	 */
	private int toDataCount = 3;
	
	/**
	 * 设置列表的首行是否为标题行
	 * 
	 * @param isTitleRow 列表首行是否为标题行
	 */
	public void setFristRowTitle(boolean fristRowTitle) {
		this.fristRowTitle = fristRowTitle;
	}
	
	/**
	 * 用于设置列表数据获取为空时重新获取的次数，在限制次数内，列表未获取到元素，则重新对列表进行一次获取，直到获取到列表元素或达到次数上限为止
	 * @param toDataCount 自动重新获取次数
	 */
	public void setToDataCount(int toDataCount) {
		this.toDataCount = toDataCount;
	}

	/**
	 * 构造对象
	 * @param driver 页面WebDriver对象
	 */
	public ListElementEvent(WebDriver driver) {
		super(driver);
	}
	
	/**
	 * 通过传入在xml文件中的控件名称，到类中指向的xml文件中查找控件名称对应的定位方式，或直接传入xpath与css定位方式，
	 * 根据定位方式在页面查找元素并将查到的一组元素进行存储。该方法可传入多个元素定位参数。
	 * 
	 * @param names 一组控件的名称或xpath与css定位方式
	 */
	public void add(String... names) {
		for (String name : names) {
			RecordTool.recordStep("获取“" + name + "”定位方式对应的列");
			
			//判断当前列名是否存在，不存在，则先存储其类名
			if (!elements.containsKey(name)) {
				elements.put(name, new ArrayList<WebElement>());
			}
			
			//循环，直到获取到列表元素或者达到重获次数限制为止
			for (int count = 0; count < toDataCount; count++) {
				try {
					//存储获取到的数据
					elements.get(name).addAll(judgeElementModes(name));
					
					//若首行为标题行，则删除当前存储列的第一个元素
					if (fristRowTitle) {
						elements.get(name).remove(0);
					}
				} catch (TimeoutException excetion) {
				}
					
				//判断获取到的数据的个数是否为0，若为0，则
				if (elements.get(name).size() == 0) {
					if (count == toDataCount - 1) {
						//若在页面未找到元素，则记录异常信息，但不结束获取
						RecordTool.recordMark("“" + name + "”对应的定位方式获取超时");
					} else {
						continue;
					}
				}
				
				//若获取到元素或次数达到限制上限，则结束循环
				break;
			}
		}
	}
	
	/**
	 * 清空指定的一列元素数据，若未对传入的控件名称或定位方式进行获取时，则该方法调用无效。
	 * @param name 控件的名称或xpath与css定位方式
	 */
	public void clear(String name) {
		if (elements.containsKey(name)) {
			elements.get(name).clear();
		}
	}
	
	/**
	 * 获取指定的一列元素的个数，若未对传入的控件名称或定位方式进行获取时，则该方法返回-1。
	 * @param name 控件的名称或xpath与css定位方式
	 * @return 指定列的元素个数
	 */
	public int size(String name) {
		if (elements.containsKey(name)) {
			return elements.get(name).size();
		} else {
			return -1;
		}
	}
	
	/**
	 * 用于根据元素下标移除当前列下标对应元素，下标从1开始，1表示第一个元素，0表示随机选择一个元素进行移除。
	 * 下标亦允许传入负数，意为由后向前遍历
	 * @param name 元素列名称
	 * @param index 元素下标
	 * 
	 * @throws NoSuchElementException 传入的元素列或元素不存在时抛出的异常
	 */
	public int removeElement(String name, int index) {
		//判断元素是否存在，若元素不存在抛出异常
		if (!elements.containsKey(name)) {
			throw new NoSuchElementException("不存在的定位方式：" + name);
		}
		
		//若传入的数值大于当前列的总长度，则跑出异常
		int size = size(name);
		if (size < Math.abs(index)) {
			throw new NoSuchElementException("元素不存在，当前列总长度：" + size + "，传入的参数：" + index);
		}
		
		//转义下标，删除相应的元素
		index = getIndex(name, index);
		elements.get(name).remove(index);
		
		return index;
	}
	
	/**
	 * 由于方法允许传入负数和特殊数字0为下标，并且下标的序号由1开始，故可通过该方法对下标的含义进行转义，得到java能识别的下标
	 * @param index 
	 * @return
	 */
	protected int getIndex(String name, int index) {
		//判断元素是否存在
		if (elements.containsKey(name)) {
			int length = elements.get(name).size();
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
		} else {
			throw new NoSuchElementException("不存在的定位方式：" + name);
		}
	}
}
