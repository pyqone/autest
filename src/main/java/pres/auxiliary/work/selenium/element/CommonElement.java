package pres.auxiliary.work.selenium.element;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

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
	 * 用于根据xml文件中元素的名称，返回对应的WebElement对象。该方法亦可传入元素
	 * 的xpath或css定位内容，来获取元素的WebElement对象。
	 * @param name 元素的名称或元素定位内容
	 * @return WebElement对象
	 */
	public WebElement getWebElement(String name) {
		//判断传入的元素是否在xml文件中，若存在再判断是否自动切换窗体，若需要，则获取元素的所有父窗体并进行切换
		if (xml.isElement(name)) {
			//判断元素是否需要自动切换窗体
			if (isAutoSwitchIframe) {
				switchFrame(getParentFrameName(name));
			}
		}
		
		return null;
	}

}
