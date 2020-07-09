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
	 * 用于进行单一元素获取
	 */
	private CommonBy commonBy;
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
	 * 用于执行当前需要切换的xml文件对象
	 */
	private File xmlFile;
	/**
	 * 用于指向是否需要切换到顶层
	 */
	private boolean isBreakRootFrame;
	/**
	 * 存储WebDriver对象
	 */
	private WebDriver driver;
	
	/**
	 * 初始化{@link WebDriver}对象
	 * @param driver {@link WebDriver}对象
	 */
	public Event(WebDriver driver) {
		//初始化元素获取类
		commonBy = new CommonBy(driver);
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
		selectBy.setXmlFile(xmlFile, isBreakRootFrame);
		
		this.xmlFile = xmlFile;
		this.isBreakRootFrame = isBreakRootFrame;
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
		DataListBy dataListBy = new DataListBy(driver);
		dataListBy.setXmlFile(xmlFile, isBreakRootFrame);
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
	 * @param index 元素所在下标
	 * @param keys 需要替换的定位方式内容的关键词
	 */
	public void doubleClick(String name, int index, String...keys) {
		DataListBy dataListBy = new DataListBy(driver);
		dataListBy.setXmlFile(xmlFile, isBreakRootFrame);
		dataListBy.add(name, keys);
		clickEvent.doubleClick(dataListBy.getElement(name, index));
	}
	
	/**
	 * 用于根据元素名称对单一元素进行右击事件
	 * @param name 元素名称
	 * @param keys 需要替换的定位方式内容的关键词
	 */
	public void rightClick(String name, String...keys) {
		clickEvent.rightClick(commonBy.getElement(name));
	}
	
	/**
	 * 用于根据元素名称对列表元素进行右击事件
	 * @param name 元素名称
	 * @param index 元素所在下标
	 * @param keys 需要替换的定位方式内容的关键词
	 */
	public void rightClick(String name, int index, String...keys) {
		DataListBy dataListBy = new DataListBy(driver);
		dataListBy.setXmlFile(xmlFile, isBreakRootFrame);
		dataListBy.add(name, keys);
		clickEvent.rightClick(dataListBy.getElement(name, index));
	}
	
	/**
	 * 用于根据元素名称对单一元素进行连续点击事件
	 * @param name 元素名称
	 * @param clickCount 点击次数
	 * @param sleepInMillis 操作时间间隔，单位为毫秒
	 * @param keys 需要替换的定位方式内容的关键词
	 */
	public void continuousClick(String name, int clickCount, long sleepInMillis, String...keys) {
		clickEvent.continuousClick(commonBy.getElement(name), clickCount, sleepInMillis);
	}
	
	/**
	 * 用于根据元素名称对列表元素进行连续点击事件
	 * @param name 元素名称
	 * @param clickCount 点击次数
	 * @param sleepInMillis 操作时间间隔，单位为毫秒
	 * @param index 元素所在下标
	 * @param keys 需要替换的定位方式内容的关键词
	 */
	public void continuousClick(String name, int index, int clickCount, long sleepInMillis, String...keys) {
		DataListBy dataListBy = new DataListBy(driver);
		dataListBy.setXmlFile(xmlFile, isBreakRootFrame);
		dataListBy.add(name, keys);
		clickEvent.continuousClick(dataListBy.getElement(name, index), clickCount, sleepInMillis);
	}
	
	/**
	 * 用于根据控件名称清除单一控件的内容
	 * @param name 控件名称
	 * @param keys 需要替换的定位方式内容的关键词
	 * @return 被清除的内容
	 */
	public String clear(String name, String...keys) {
		return textEvent.clear(commonBy.getElement(name));
	}
	
	/**
	 * 用于根据控件名称清除列表控件的内容
	 * @param name 控件名称
	 * @param index 元素所在下标
	 * @param keys 需要替换的定位方式内容的关键词
	 * @return 被清除的内容
	 */
	public String clear(String name, int index, String...keys) {
		DataListBy dataListBy = new DataListBy(driver);
		dataListBy.setXmlFile(xmlFile, isBreakRootFrame);
		dataListBy.add(name, keys);
		return textEvent.clear(dataListBy.getElement(name, index));
	}
	
	/**
	 * 用于根据控件名称获取单一控件指定属性的内容
	 * @param name 控件名称
	 * @param attributeName 属性名称
	 * @param keys 需要替换的定位方式内容的关键词
	 * @return 控件属性的内容
	 */
	public String getAttributeValue(String name, String attributeName, String...keys) {
		return textEvent.getAttributeValue(commonBy.getElement(name), attributeName);
	}
	
	/**
	 * 用于根据控件名称获取列表控件指定属性的内容
	 * @param name 控件名称
	 * @param index 元素所在下标
	 * @param attributeName 属性名称
	 * @param keys 需要替换的定位方式内容的关键词
	 * @return 控件属性的内容
	 */
	public String getAttributeValue(String name, int index, String attributeName, String...keys) {
		DataListBy dataListBy = new DataListBy(driver);
		dataListBy.setXmlFile(xmlFile, isBreakRootFrame);
		dataListBy.add(name, keys);
		return textEvent.getAttributeValue(dataListBy.getElement(name, index), attributeName);
	}
	
	/**
	 * 用于根据控件名称获取单一控件的内容
	 * @param name 控件名称
	 * @param keys 需要替换的定位方式内容的关键词
	 * @return 控件的内容
	 */
	public String getText(String name, String...keys) {
		return textEvent.getText(commonBy.getElement(name));
	}
	
	/**
	 * 用于根据控件名称获取列表控件的内容
	 * @param name 控件名称
	 * @param index 元素所在下标
	 * @param keys 需要替换的定位方式内容的关键词
	 * @return 控件的内容
	 */
	public String getText(String name, int index, String...keys) {
		DataListBy dataListBy = new DataListBy(driver);
		dataListBy.setXmlFile(xmlFile, isBreakRootFrame);
		dataListBy.add(name, keys);
		return textEvent.getText(dataListBy.getElement(name, index));
	}
	
	/**
	 * 用于根据控件名称对单一控件进行输入
	 * @param name 控件名称
	 * @param text 输入的内容
	 * @param keys 需要替换的定位方式内容的关键词
	 * @return 输入到控件的内容
	 */
	public String input(String name, String text, String...keys) {
		return textEvent.input(commonBy.getElement(name), text);
	}
	
	/**
	 * 用于根据控件名称获取列表控件的内容
	 * @param name 控件名称
	 * @param index 元素所在下标
	 * @param text 输入的内容
	 * @param keys 需要替换的定位方式内容的关键词
	 * @return 输入到控件的内容
	 */
	public String input(String name, int index, String text, String...keys) {
		DataListBy dataListBy = new DataListBy(driver);
		dataListBy.setXmlFile(xmlFile, isBreakRootFrame);
		dataListBy.add(name, keys);
		return textEvent.input(dataListBy.getElement(name, index), text);
	}
}
