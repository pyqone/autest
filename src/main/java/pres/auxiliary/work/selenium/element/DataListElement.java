package pres.auxiliary.work.selenium.element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

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
	 * 用于判断列表的第一行元素是否为标题元素
	 */
	private boolean isFristRowTitle = false;
	
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
	 * 用于根据xml文件中元素的名称，返回对应的获取到的一组WebElement对象。该方法亦可传入元素
	 * 定位内容，通过遍历所有的定位方式，在页面上查找元素，来获取元素的WebElement对象。
	 * 建议传入的定位内容为xpath路径或绝对的css路径，若非这两路径，则在识别元素时会很慢，降低
	 * 程序运行速度。
	 * 
	 * @param names 一组控件的名称或xpath与css定位方式
	 */
	public void add(String name) {
		//判断当前列名是否存在，不存在，则先存储其类名
		if (!elementMap.containsKey(name)) {
			elementMap.put(name, new ArrayList<WebElement>());
		}
		
		
	}
}
