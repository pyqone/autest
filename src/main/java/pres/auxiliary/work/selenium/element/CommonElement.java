package pres.auxiliary.work.selenium.element;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import pres.auxiliary.selenium.xml.ByType;
import pres.auxiliary.work.selenium.brower.AbstractBrower;

/**
 * <p><b>文件名：</b>CommonElement.java</p>
 * <p><b>用途：</b>
 *   提供在辅助化测试中，对页面单一元素获取的方法。类中获取元素的方法兼容传入定位方式对
 * 元素进行查找，建议使用xml对页面元素的定位方式进行存储，以简化编码时的代码量，也便于
 * 对代码的维护
 * </p>
 * <p><b>编码时间：</b>2020年4月26日下午10:34:55</p>
 * <p><b>修改时间：</b>2020年4月26日下午10:34:55</p>
 * @author 
 * @version Ver1.0
 * @since JDK 12
 *
 */
public class CommonElement extends AbstractElement {
	/**
	 * 构造对象并存储浏览器的WebDriver对象
	 * 
	 * @param driver 浏览器的WebDriver对象
	 */
	public CommonElement(WebDriver driver) {
		super(driver);
	}
	
	/**
	 * 通过浏览器对象{@link AbstractBrower}进行构造
	 * @param brower {@link AbstractBrower}对象
	 */
	public CommonElement(AbstractBrower brower) {
		super(brower);
	}

	/**
	 * 用于根据xml文件中元素的名称，返回对应的WebElement对象。该方法亦可传入元素
	 * 定位内容，通过遍历所有的定位方式，在页面上查找元素，来获取元素的WebElement对象。
	 * 建议传入的定位内容为xpath路径或绝对的css路径，若非这两路径，则在识别元素时会很慢，降低
	 * 程序运行速度。若非xml文件中的元素，且不是xpath路径或绝对的css路径，建议使用{@link #getWebElement(String, ByType)}方法
	 * @param name 元素的名称或元素定位内容
	 * @return WebElement对象
	 */
	public WebElement getWebElement(String name) {
		return getWebElement(name, null);
	}
	
	/**
	 * 用于根据xml文件中元素的名称，与定位方式，返回对应的WebElement对象。该方法亦可传入元素
	 * 定位内容，并根据定位方式，对页面数据进行查找
	 * @param name 元素的名称或元素定位内容
	 * @return WebElement对象
	 */
	public WebElement getWebElement(String name, ByType byType) {
		//判断传入的元素是否在xml文件中，若存在再判断是否自动切换窗体，若需要，则获取元素的所有父窗体并进行切换
		if (xml != null && xml.isElement(name) && isAutoSwitchIframe) {
			switchFrame(getParentFrameName(name));
		}
		
		return recognitionElement(name, byType).get(0);
	}

	@Override
	boolean isExistElement(By by, long waitTime) {
		//当查找到元素时，则返回true，若查不到元素，则会抛出异常，故返回false
		return new WebDriverWait(driver, waitTime, 200).
				until((driver) -> {
					WebElement element = driver.findElement(by);
					return element != null;
				});
	}
}
