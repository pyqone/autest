package pres.auxiliary.work.selenium.element;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CommonElement extends Element {
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
		//TODO 编写返回元素的方法
		//1.判断传入的参数在xml文件中是否能找到
		//1.1 若能找到
		//1.1.1 获取元素内容
		//1.1.2 获取元素所在的frame以及父层的frame
		//1.1.3 对元素的frame自动进行切换
		//1.1.4 调用判断元素是否存在的方法，对元素进行存在判断
		//1.1.4.1 存在，获取并返回
		//1.1.4.2 不存在，抛出异常
		//1.2 若不能找到，调用判断方法对传入的内容判断是xpath或css
		//1.2.1 若均不是，抛出异常
		//1.2.2 若是，则调用判断元素是否存在的方法，对元素进行存在判断
		
		return null;
	}

}
