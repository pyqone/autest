package pres.auxiliary.work.selenium.event;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * <p><b>文件名：</b>AbstractEvent.java</p>
 * <p><b>用途：</b>所有事件类的基类，包含了事件类所用到的基础方法以及弹窗处理方法和窗体、窗口的切换方法</p>
 * <p><b>编码时间：</b>2019年9月24日下午4:24:15</p>
 * <p><b>修改时间：</b>2020年7月10日上午16:49:37</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public abstract class AbstractEvent {
	/**
	 * 用于存储浏览器的WebDriver对象，设为静态，保证所有的子类只使用一个WebDriver对象，以避免造成WebDriver不正确导致的Bug
	 */
	WebDriver driver;
	/**
	 * 用于存储事件等待事件
	 */
	WebDriverWait wait;
	/**
	 * 设置显示等待的超时时间（默认3秒）
	 */
	long waitTime = 5;
	
	/**
	 * 构造对象并存储浏览器的WebDriver对象
	 * 
	 * @param driver 浏览器的WebDriver对象
	 */
	public AbstractEvent(WebDriver driver) {
		this.driver = driver;
		wait = new WebDriverWait(driver, waitTime, 200);
	}

	/**
	 * 用于设置事件等待时间，默认时间为3秒
	 * 
	 * @param waitTime 事件等待时间
	 */
	public void setWaitTime(long waitTime) {
		wait.withTimeout(Duration.ofSeconds(waitTime));
	}
}
