package pres.auxiliary.work.selenium.event;

import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pres.auxiliary.work.selenium.element.Element;

/**
 * <p><b>文件名：</b>ClickEvent.java</p>
 * <p><b>用途：</b>
 * 定义了对控件进行点击操作相关的方法，可通过该类，对页面进行基本的点击操作
 * </p>
 * <p><b>编码时间：</b>2019年8月29日下午3:24:34</p>
 * <p><b>修改时间：</b>2020年7月10日上午16:49:37</p>
 * 
 * @author 彭宇琦
 * @version Ver2.0
 * @since JDK 12
 *
 */
public class ClickEvent extends AbstractEvent {
	/**
	 * 构造ClickEvent类对象
	 * 
	 * @param driver WebDriver类对象
	 */
	public ClickEvent(WebDriver driver) {
		super(driver);
	}

	/**
	 * 鼠标左键单击事件
	 * 
	 * @param element 通过查找页面得到的控件元素对象
	 */
	public void click(Element element) {
		//在等待时间内判断元素是否可以点击
		try {
			wait.until(ExpectedConditions.elementToBeClickable(element.getWebElement()));
		} catch (Exception e) {
		}
		
		//进行操作，若仍抛出ElementClickInterceptedException异常，则再等待，直到不再抛出异常
		wait.until((driver) -> {
				try {
					element.getWebElement().click();
					return true;
				} catch (ElementClickInterceptedException e) {
					return false;
				} catch (StaleElementReferenceException e) {
					element.findElement();
					return false;
				}
			});
	}

	/**
	 * 鼠标左键双击事件
	 * 
	 * @param element 通过查找页面得到的控件元素对象
	 */
	public void doubleClick(Element element) {
		//在等待时间内判断元素是否可以点击
		try {
			wait.until(ExpectedConditions.elementToBeClickable(element.getWebElement()));
		} catch (Exception e) {
		}
 		//进行操作，若仍抛出ElementClickInterceptedException异常，则再等待，直到不再抛出异常
		wait.until((driver) -> {
				try {
					new Actions(driver).doubleClick(element.getWebElement()).perform();
					return true;
				} catch (ElementClickInterceptedException e) {
					return false;
				} catch (StaleElementReferenceException e) {
					element.findElement();
					return false;
				}
			});
	}

	/**
	 * 鼠标右键点击事件
	 * @param element 通过查找页面得到的控件元素对象
	 */
	public void rightClick(Element element) {
		//在等待时间内判断元素是否可以点击
		try {
			wait.until(ExpectedConditions.elementToBeClickable(element.getWebElement()));
		} catch (Exception e) {
		}
		//进行操作，若仍抛出ElementClickInterceptedException异常，则再等待，直到不再抛出异常
		wait.until((driver) -> {
				try {
					new Actions(driver).contextClick(element.getWebElement()).perform();
					return true;
				} catch (ElementClickInterceptedException e) {
					return false;
				} catch (StaleElementReferenceException e) {
					element.findElement();
					return false;
				}
			});
	}
	
	/**
	 * 连续进行指定次数的鼠标左键点击事件
	 * @param element 通过查找页面得到的控件元素对象
	 * @param clickCount 点击次数
	 * @param sleepInMillis 操作时间间隔，单位为毫秒
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
	}
}
