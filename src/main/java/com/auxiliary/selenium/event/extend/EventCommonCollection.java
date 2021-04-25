package com.auxiliary.selenium.event.extend;

import java.util.ArrayList;

import com.auxiliary.selenium.brower.AbstractBrower;
import com.auxiliary.selenium.element.FindCommonElement;
import com.auxiliary.selenium.event.AbstractEvent;
import com.auxiliary.selenium.event.AssertEvent;
import com.auxiliary.selenium.event.ClickEvent;
import com.auxiliary.selenium.event.JsEvent;
import com.auxiliary.selenium.event.TextEvent;
import com.auxiliary.selenium.event.WaitEvent;
import com.auxiliary.selenium.location.AbstractLocation;

/**
 * <p><b>文件名：</b>EventCommonCollection.java</p>
 * <p><b>用途：</b>
 * 整合所有的基础事件，通过默认的元素等待时间、事件执行时间来对指定的单一元素进行操作
 * </p>
 * <p><b>编码时间：</b>2021年4月25日上午10:59:29</p>
 * <p><b>修改时间：</b>2021年4月25日上午10:59:29</p>
 * @author 
 * @version Ver1.0
 * @since JDK 1.8
 */
public class EventCommonCollection extends AbstractEvent {
	/**
	 * 指向单一元素查找类
	 */
	protected FindCommonElement findElement;
	
	/**
	 * 指向点击事件
	 */
	protected ClickEvent clickEvent;
	/**
	 * 指向文本事件
	 */
	protected TextEvent textEvent;
	/**
	 * 指向等待事件
	 */
	protected WaitEvent waitEvent;
	/**
	 * 指向断言事件
	 */
	protected AssertEvent assertEvent;
	/**
	 * 指向js事件
	 */
	protected JsEvent jsEvent;
	
	/**
	 * 日志信息集合，收集每个操作步骤返回的日志
	 */
	protected ArrayList<String> logTextList = new ArrayList<>();
	
	/**
	 * 构造对象并存储浏览器对象
	 * 
	 * @param brower 浏览器{@link AbstractBrower}对象
	 */
	public EventCommonCollection(AbstractBrower brower) {
		super(brower);
		
		//初始化各个查找类与事件类
		findElement = new FindCommonElement(brower);
		
		clickEvent = new ClickEvent(brower);
		textEvent = new TextEvent(brower);
		waitEvent = new WaitEvent(brower);
		assertEvent = new AssertEvent(brower);
		jsEvent = new JsEvent(brower);
	}
	
	/**
	 * 用于设置元素定位方式文件读取类对象
	 * <p>
	 * isBreakRootFrame参数表示是否将所有的元素切回到顶层，对于web元素而言，则是将iframe切回到顶层；对于app元素
	 * 而言，则是将上下文切换至原生层
	 * </p>
	 * 
	 * @param read             元素定位方式文件读取类对象
	 * @param isBreakRootFrame 是否需要将窗体切回到顶层
	 */
	public void setReadMode(AbstractLocation read, boolean isBreakRootFrame) {
		findElement.setReadMode(read, isBreakRootFrame);
	}
	
	public void click(String elementName, String... linkKeys) {
		clickEvent.click(findElement.getElement(elementName, linkKeys));
	}
	
	public void doubleClick(String elementName, String... linkKeys) {
		clickEvent.doubleClick(findElement.getElement(elementName, linkKeys));
	}
	
	public void rightClick(String elementName, String... linkKeys) {
		clickEvent.rightClick(findElement.getElement(elementName, linkKeys));
	}
	
	public void continuousClick(int clickCount, long sleepInMillis, String elementName, String... linkKeys) {
		clickEvent.continuousClick(findElement.getElement(elementName, linkKeys), clickCount, sleepInMillis);
	}
	
	public String clear(String elementName, String... linkKeys) {
		return textEvent.clear(findElement.getElement(elementName, linkKeys));
	}
	
	public String getAttributeValue(String attributeName, String elementName, String... linkKeys) {
		return textEvent.getAttributeValue(findElement.getElement(elementName, linkKeys), attributeName);
	}
	
	public String getText(String elementName, String... linkKeys) {
		return textEvent.getText(findElement.getElement(elementName, linkKeys));
	}
	
	public String input(String text, String elementName, String... linkKeys) {
		return textEvent.input(findElement.getElement(elementName, linkKeys), text);
	}
	
	public String getImageText(String elementName, String... linkKeys) {
		return textEvent.getImageText(findElement.getElement(elementName, linkKeys));
	}
}
