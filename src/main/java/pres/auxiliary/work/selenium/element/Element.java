package pres.auxiliary.work.selenium.element;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Element {
	/**
	 * 存储元素的名称或定位内容
	 */
	private List<WebElement> elementList = null;
	
	private By by;
	
	/**
	 * 初始化信息
	 * @param name 元素名称或定位内容
	 * @param byType 元素定位
	 */
	public Element(By by) {
		super();
		this.by = by;
	}
	
	/**
	 * 用于返回元素对应的WebElement对象
	 * @return 返回元素对应的WebElement对象
	 */
	public WebElement getWebElement(WebDriver driver) {
		return driver.findElement(by);
	}
	
	public void 
}
