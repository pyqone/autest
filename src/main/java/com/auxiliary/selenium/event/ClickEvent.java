package com.auxiliary.selenium.event;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.interactions.Actions;

import com.auxiliary.selenium.brower.AbstractBrower;
import com.auxiliary.selenium.element.Element;

/**
 * <p><b>文件名：</b>ClickEvent.java</p>
 * <p><b>用途：</b>
 * 定义了对控件进行点击操作相关的方法，可通过该类，对页面进行基本的点击操作
 * </p>
 * <p><b>编码时间：</b>2019年8月29日下午3:24:34</p>
 * <p><b>修改时间：</b>2020年10月17日下午16:34:37</p>
 * 
 * @author 彭宇琦
 * @version Ver2.0
 * @since JDK 8
 *
 */
public class ClickEvent extends AbstractEvent {
	/**
	 * 构造对象
	 * 
	 * @param brower 浏览器{@link AbstractBrower}类对象
	 */
	public ClickEvent(AbstractBrower brower) {
		super(brower);
	}

	/**
	 * 鼠标左键单击事件
	 * 
	 * @param element {@link Element}对象
	 * @throws TimeoutException 元素无法操作时抛出的异常
	 * @throws NoSuchElementException 元素不存在或下标不正确时抛出的异常 
	 */
	public void click(Element element) {
		actionOperate(element, (e) -> {
			e.getWebElement().click();
			return "";
		});
		
		logText = "左键点击“" + element.getElementData().getName() + "”元素";
	}

	/**
	 * 鼠标左键双击事件
	 * 
	 * @param element {@link Element}对象
	 * @throws TimeoutException 元素无法操作时抛出的异常 
	 * @throws NoSuchElementException 元素不存在或下标不正确时抛出的异常 
	 */
	public void doubleClick(Element element) {
		actionOperate(element, (e) -> {
			new Actions(brower.getDriver()).doubleClick(e.getWebElement()).perform();
			return "";
		});
		
		logText = "左键双击“" + element.getElementData().getName() + "”元素";
	}

	/**
	 * 鼠标右键点击事件
	 * @param element {@link Element}对象
	 * @throws TimeoutException 元素无法操作时抛出的异常 
	 * @throws NoSuchElementException 元素不存在或下标不正确时抛出的异常 
	 */
	public void rightClick(Element element) {
		actionOperate(element, (e) -> {
			new Actions(brower.getDriver()).contextClick(e.getWebElement()).perform();
			return "";
		});
		
		logText = "右键点击“" + element.getElementData().getName() + "”元素";
	}
	
	/**
	 * 连续进行指定次数的鼠标左键点击事件
	 * @param element {@link Element}对象
	 * @param clickCount 点击次数
	 * @param sleepInMillis 操作时间间隔，单位为毫秒
	 * @throws TimeoutException 元素无法操作时抛出的异常 
	 * @throws NoSuchElementException 元素不存在或下标不正确时抛出的异常 
	 */
	public void continuousClick(Element element, int clickCount, long sleepInMillis) {
		for(int i = 0; i < clickCount; i++) {
			click(element);
			
			try {
				Thread.sleep(sleepInMillis);
			} catch (InterruptedException e) {
				continue;
			}
		}
		
		logText = "左键连续点击“" + element.getElementData().getName() + "”元素" + clickCount + "次";
	}
}
