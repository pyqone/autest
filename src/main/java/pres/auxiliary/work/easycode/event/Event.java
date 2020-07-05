package pres.auxiliary.work.easycode.event;

import java.io.File;

import org.openqa.selenium.WebDriver;

import pres.auxiliary.work.selenium.brower.AbstractBrower;
import pres.auxiliary.work.selenium.element.CommonBy;
import pres.auxiliary.work.selenium.element.DataListBy;
import pres.auxiliary.work.selenium.element.SelectBy;
import pres.auxiliary.work.selenium.event.ClickEvent;
import pres.auxiliary.work.selenium.event.TextEvent;

public class Event {
	/**
	 * 存储WebDriver对象
	 */
	private WebDriver driver;
	
	/**
	 * 用于进行单一元素获取
	 */
	private CommonBy commonBy;
	/**
	 * 用于进行列表元素获取
	 */
	private DataListBy dataListBy;
	/**
	 * 用于进行选项元素获取
	 */
	private SelectBy selectBy;
	
	/**
	 * 用于进行点击事件
	 */
	private ClickEvent clickEvent;
	/**
	 * 用于进行文本事件
	 */
	private TextEvent textEvent;
	
	/**
	 * 初始化{@link WebDriver}对象
	 * @param driver {@link WebDriver}对象
	 */
	public Event(WebDriver driver) {
		this.driver = driver;
		
		//初始化元素获取类
		commonBy = new CommonBy(driver);
		dataListBy = new DataListBy(driver);
		selectBy = new SelectBy(driver);
		
		//初始化元素操作类
		clickEvent = new ClickEvent(driver);
		textEvent = new TextEvent(driver);
	}
	
	/**
	 * 通过{@link AbstractBrower}对象初始化{@link WebDriver}对象
	 * @param brower {@link AbstractBrower}对象
	 */
	public Event(AbstractBrower brower) {
		this(brower.getDriver());
	}
	
	/**
	 * 用于切换相应的xml文件
	 * @param xmlFile xml文件对象
	 * @param isBreakRootFrame 是否切换回到顶层
	 */
	public void switchXmlFile(File xmlFile, boolean isBreakRootFrame) {
		commonBy.setXmlFile(xmlFile, isBreakRootFrame);
		dataListBy.setXmlFile(xmlFile, isBreakRootFrame);
		selectBy.setXmlFile(xmlFile, isBreakRootFrame);
	}
	
	/**
	 * 用于根据元素名称对单一元素进行点击事件
	 * @param name 元素名称
	 * @param keys 需要替换的定位方式内容的关键词
	 */
	public void click(String name, String...keys) {
		clickEvent.click(commonBy.getElement(name, keys));
	}
	
	/**
	 * 用于根据元素名称对列表元素进行点击事件
	 * @param name 元素名称
	 * @param index 元素所在下标
	 * @param keys 需要替换的定位方式内容的关键词
	 */
	public void click(String name, int index, String...keys) {
		dataListBy.clearColumn(name, true);
		dataListBy.add(name, keys);
		clickEvent.click(dataListBy.getElement(name, index));
	}
	
	/**
	 * 用于根据元素名称对单一元素进行双击事件
	 * @param name 元素名称
	 * @param keys 需要替换的定位方式内容的关键词
	 */
	public void doubleClick(String name, String...keys) {
		clickEvent.doubleClick(commonBy.getElement(name));
	}
	
	/**
	 * 用于根据元素名称对列表元素进行双击事件
	 * @param name 元素名称
	 * @param keys 需要替换的定位方式内容的关键词
	 */
	public void doubleClick(String name, int index, String...keys) {
		dataListBy.clearColumn(name, true);
		dataListBy.add(name, keys);
		clickEvent.doubleClick(dataListBy.getElement(name, index));
	}
	
	/**
	 * 用于根据元素名称对单一元素进行双击事件
	 * @param name 元素名称
	 * @param keys 需要替换的定位方式内容的关键词
	 */
	public void rightClick(String name, String...keys) {
		clickEvent.rightClick(commonBy.getElement(name));
	}
	
	/**
	 * 用于根据元素名称对列表元素进行右击事件
	 * @param name 元素名称
	 * @param keys 需要替换的定位方式内容的关键词
	 */
	public void rightClick(String name, int index, String...keys) {
		dataListBy.clearColumn(name, true);
		dataListBy.add(name, keys);
		clickEvent.rightClick(dataListBy.getElement(name, index));
	}
}
