package pres.auxiliary.work.selenium.event;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import pres.auxiliary.work.selenium.element.Element;

public class EventWait {
	/**
	 * 控制等待
	 */
	private WebDriverWait wait;
	
	/**
	 * 构造WaitEvent类对象，默认60秒的查询超时时间，可设置超时时间，见{@link #setOvertime(long)}
	 * @param driver WebDriver类对象
	 */
	public EventWait(WebDriver driver) {
		this(driver, 60);
	}
	
	/**
	 * 构造WaitEvent类对象，并设置等待超时时间，单位为秒
	 * @param driver WebDriver类对象
	 */
	public EventWait(WebDriver driver, long overtime) {
		wait = new WebDriverWait(driver, overtime, 200);
		wait.withMessage("元素仍然存在，操作超时：" + overtime + "秒");
	}

	/**
	 * 用于设置元素查询超时时间，单位为秒
	 * @param overtime 超时时间
	 */
	public void setOvertime(long overtime) {
		wait.pollingEvery(Duration.ofSeconds(overtime));
	}
	
	/**
	 * 用于等待元素消失
	 * @param waitElement 需要等待的元素
	 */ 
	public void disappear(Element waitElement) {
		wait.until(driver -> {
			return !waitElement.getWebElement().isDisplayed();
		});
	}
	
	/**
	 * 用于等待元素元素内出现文本
	 * @param waitElement 需要等待的元素
	 */ 
	public void showText(Element waitElement) {
		wait.until(driver -> {
			return !new TextEvent(driver).getText(waitElement).isEmpty();
		});
	}
}
