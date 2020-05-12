package pres.auxiliary.work.selenium.event;

import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * <p><b>文件名：</b>ClickEvent.java</p>
 * <p><b>用途：</b>
 * 定义了对控件进行点击操作相关的方法，可通过该类，对页面进行基本的点击操作
 * </p>
 * <p><b>编码时间：</b>2019年8月29日下午3:24:34</p>
 * <p><b>修改时间：</b>2020年5月10日 下午3:42:36</p>
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
	public void click(WebElement element) {
		//元素高亮
		elementHight(element);
		//在等待时间内判断元素是否可以点击，若可以点击元素，则进行点击事件
		new WebDriverWait(driver, waitTime, 200).
			until(ExpectedConditions.elementToBeClickable(element));
		
		new WebDriverWait(driver, waitTime, 200).
			until((driver) -> {
				try {
					element.click();
				} catch (ElementClickInterceptedException e) {
					return false;
				}
				return true;
			});
		
		//记录操作内容
		result = "";
		step = "鼠标左键点击“" + ELEMENT_NAME + "”元素";
	}

	/**
	 * 鼠标左键双击事件
	 * 
	 * @param element 通过查找页面得到的控件元素对象
	 */
	public void doubleClick(WebElement element) {
		//元素高亮
		elementHight(element);
		//在等待时间内判断元素是否可以点击，若可以点击元素，则进行双击事件
		new Actions(driver).
			doubleClick(new WebDriverWait(driver, waitTime, 200).until(ExpectedConditions.elementToBeClickable(element))).
			perform();
		
		//记录操作内容
		result = "";
		step = "鼠标左键双击“" + ELEMENT_NAME + "”元素";
	}

	/**
	 * 鼠标右键点击事件
	 * @param element 通过查找页面得到的控件元素对象
	 */
	public void rightClick(WebElement element) {
		//元素高亮
		elementHight(element);
		//在等待时间内判断元素是否可以点击，若可以点击元素，则进行右击事件
		new Actions(driver).
			contextClick(new WebDriverWait(driver, waitTime, 200).until(ExpectedConditions.elementToBeClickable(element))).
			perform();
		
		//记录操作内容
		result = "";
		step = "鼠标右键点击“" + ELEMENT_NAME + "”元素";
	}
	
	/**
	 * 连续进行指定次数的鼠标左键点击事件
	 * @param element 通过查找页面得到的控件元素对象
	 * @param clickCount 点击次数
	 * @param sleepInMillis 操作时间间隔，单位为毫秒
	 */
	public void continuousClick(WebElement element, int clickCount, long sleepInMillis) {
		for(int i = 0; i < clickCount; i++) {
			click(element);
			
			try {
				Thread.sleep(sleepInMillis);
			} catch (InterruptedException e) {
				continue;
			}
		}
		
		//记录操作内容
		result = "";
		step = "鼠标左键连续" + clickCount + "次点击“" + ELEMENT_NAME + "”元素";
	}
}
