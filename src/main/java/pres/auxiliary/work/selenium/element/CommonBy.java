package pres.auxiliary.work.selenium.element;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import pres.auxiliary.work.selenium.brower.AbstractBrower;
import pres.auxiliary.work.selenium.xml.ByType;

/**
 * <p><b>文件名：</b>CommonElement.java</p>
 * <p><b>用途：</b>
 *   提供在辅助化测试中，对页面单一元素获取的方法。类中获取元素的方法兼容传入定位方式对
 * 元素进行查找，建议使用xml对页面元素的定位方式进行存储，以简化编码时的代码量，也便于
 * 对代码的维护
 * </p>
 * <p><b>编码时间：</b>2020年4月26日下午10:34:55</p>
 * <p><b>修改时间：</b>2020年4月26日下午10:34:55</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public class CommonBy extends AbstractBy {
	/**
	 * 构造对象并存储浏览器的WebDriver对象
	 * 
	 * @param driver 浏览器的WebDriver对象
	 */
	public CommonBy(WebDriver driver) {
		super(driver);
	}
	
	/**
	 * 通过浏览器对象{@link AbstractBrower}进行构造
	 * @param brower {@link AbstractBrower}对象
	 */
	public CommonBy(AbstractBrower brower) {
		super(brower);
	}
	
	/**
	 * 通过{@link AbstractBy}对象对类进行构造，将传入的AbstractBy类中的关键参数设置到当前类对象中
	 * @param brower {@link AbstractBy}对象
	 */
	public CommonBy(AbstractBy by) {
		super(by);
	}

	/**
	 * 用于根据xml文件中元素的名称，返回对应的{@link Element}对象。该方法亦可传入元素
	 * 定位内容，通过遍历所有的定位方式，在页面上查找元素，来获取元素的WebElement对象。
	 * 建议传入的定位内容为xpath路径或绝对的css路径，若非这两路径，则在识别元素时会很慢，降低
	 * 程序运行速度。若非xml文件中的元素，且不是xpath路径或绝对的css路径，建议使用{@link #getElement(String, ByType)}方法
	 * @param name 元素的名称或元素定位内容
	 * @return {@link Element}对象
	 */
	public Element getElement(String name) {
		return getElement(new ElementInformation(name, null, ElementType.COMMON_ELEMENT));
	}
	
	/**
	 * 用于根据xml文件中元素的名称，与定位方式，返回对应的{@link Element}对象。该方法亦可传入元素
	 * 定位内容，并根据定位方式，对页面数据进行查找
	 * @param name 元素的名称或元素定位内容
	 * @return {@link Element}对象
	 */
	public Element getElement(String name, ByType byType) {
		return getElement(new ElementInformation(name, byType, ElementType.COMMON_ELEMENT));
	}
	
	/**
	 * 用于根据xml文件中元素的名称，与定位方式，返回对应的{@link Element}对象。该方法亦可传入元素
	 * 定位内容，并根据定位方式，对页面数据进行查找。该方法可对由xml文件读取的内容进行词语替换，根据
	 * 传参中词语的顺序，对需要替换的词语进行替换
	 * @param name 元素名称或定位方式内容
	 * @param byType 元素定位方式
	 * @param links 替换词语
	 * @return {@link Element}对象
	 */
	public Element getElement(String name, ByType byType, String...links) {
		return getElement(new ElementInformation(name, byType, ElementType.COMMON_ELEMENT, links));
	}
	
	/**
	 *  用于根据xml文件中元素的名称，返回对应的{@link Element}对象。该方法亦可传入元素
	 * 定位内容，通过遍历所有的定位方式，在页面上查找元素，来获取元素的WebElement对象。
	 * 建议传入的定位内容为xpath路径或绝对的css路径，若非这两路径，则在识别元素时会很慢，降低
	 * 程序运行速度。若非xml文件中的元素，且不是xpath路径或绝对的css路径，建议使用{@link #getElement(String, ByType, String...)}方法
	 * @param name 元素的名称或元素定位内容
	 * @param links 替换词语
	 * @return {@link Element}对象
	 */
	public Element getElement(String name, String...links) {
		return getElement(name, null, links);
	}

	/**
	 * 获取元素的底层方法
	 * @param elementInformation 元素信息类对象
	 * @return WebElement对象
	 */
	private Element getElement(ElementInformation elementInformation) {
		//判断传入的元素是否在xml文件中，若存在再判断是否自动切换窗体，若需要，则获取元素的所有父窗体并进行切换
		if (xml != null && xml.isElement(elementInformation.name) && isAutoSwitchIframe) {
			switchFrame(getParentFrameName(elementInformation.name));
		}
		
		return new Element(driver, ElementType.COMMON_ELEMENT, recognitionElement(elementInformation), elementInformation.name);
	}

	@Override
	boolean isExistElement(By by, long waitTime) {
		//当查找到元素时，则返回true，若查不到元素，则会抛出异常，故返回false
		return new WebDriverWait(driver, waitTime, 200).
				until((driver) -> {
					WebElement element = driver.findElement(by);
					return element != null;
				});
		/*
		return new WebDriverWait(driver, waitTime, 200).
				until(ExpectedConditions.and(ExpectedConditions.visibilityOfAllElementsLocatedBy(by), 
						ExpectedConditions.invisibilityOfElementLocated(by)));
						*/
	}
}
