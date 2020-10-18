package pres.auxiliary.work.selenium.event;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import pres.auxiliary.work.selenium.brower.AbstractBrower;

public class WaitEvent extends AbstractEvent{
	/**
	 * 控制等待
	 */
	private WebDriverWait wait;
	
	/**
	 * 构造对象
	 * 
	 * @param brower 浏览器{@link AbstractBrower}类对象
	 */
	public WaitEvent(AbstractBrower brower) {
		this(brower, 60);
	}
	
	/**
	 * 构造对象
	 * 
	 * @param brower 浏览器{@link AbstractBrower}类对象
	 */
	public WaitEvent(AbstractBrower brower, long waitTime) {
		super(brower);
		wait = new WebDriverWait(brower.getDriver(), waitTime, 200);
		wait.withMessage("等待超时，事件等待失败：" + waitTime + "秒");
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
	public void disappear(Element_Old waitElement) {
		wait.until(driver -> {
			return !waitElement.getWebElement().isDisplayed();
		});
	}
	
	/**
	 * 用于等待元素元素内出现文本
	 * @param waitElement 需要等待的元素
	 */ 
	public void showText(Element_Old waitElement) {
		wait.until(driver -> {
			return !new TextEvent(driver).getText(waitElement).isEmpty();
		});
	}
}
