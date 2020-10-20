package pres.auxiliary.work.selenium.event;

import java.time.Duration;
import java.util.Arrays;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import pres.auxiliary.work.selenium.brower.AbstractBrower;
import pres.auxiliary.work.selenium.element.AbstractBy.Element;

/**
 * <p><b>文件名：</b>AbstractEvent.java</p>
 * <p><b>用途：</b>所有事件类的基类，包含了事件类所用到的基础方法以及弹窗处理方法和窗体、窗口的切换方法</p>
 * <p><b>编码时间：</b>2019年9月24日下午4:24:15</p>
 * <p><b>修改时间：</b>2020年7月10日上午16:49:37</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 8
 *
 */
public abstract class AbstractEvent {
	/**
	 * 定义定位到元素相应位置的js代码
	 */
	protected final String LOCATION_ELEMENT_JS = "arguments[0].scrollIntoView(false);";
	
	/**
	 * 用于存储浏览器对象
	 */
	protected AbstractBrower brower;
	/**
	 * 用于存储事件等待事件
	 */
	protected WebDriverWait wait;
	/**
	 * 设置显示等待的超时时间（默认3秒）
	 */
	protected long waitTime = 5;
	
	/**
	 * 存储操作的日志文本
	 */
	protected String logText = "";
	/**
	 * 存储操作的返回值文本
	 */
	protected String resultText = "";
	
	/**
	 * 构造对象并存储浏览器对象
	 * 
	 * @param brower 浏览器{@link AbstractBrower}对象
	 */
	public AbstractEvent(AbstractBrower brower) {
		this.brower = brower;
		wait = new WebDriverWait(brower.getDriver(), waitTime, 200);
		wait.withMessage("操作超时，当前元素无法进行该操作");
	}

	/**
	 * 用于设置事件等待时间，默认时间为5秒
	 * 
	 * @param waitTime 事件等待时间
	 */
	public void setWaitTime(long waitTime) {
		wait.withTimeout(Duration.ofSeconds(waitTime));
	}
	
	/**
	 * 返回操作的日志
	 * @return 操作日志
	 */
	public String getLogText() {
		return logText;
	}

	/**
	 * 返回操作的返回值，若操作无返回值时，则返回空串
	 * @return 操作返回值
	 */
	public String getResultText() {
		return resultText;
	}
	
	/**
	 * 用于返回当前指向的{@link AbstractBrower}类对象
	 * @return {@link AbstractBrower}类对象
	 */
	protected AbstractBrower getBrower() {
		return brower;
	}

	/**
	 * 用于通过js脚本，将页面定位元素所在位置
	 * @param element {@link Element}对象
	 */
	protected void locationElement(WebElement  element) {
		//若抛出NoSuchElementException异常，则将异常抛出，其他异常将不做处理
		try {
			((JavascriptExecutor) brower.getDriver()).executeScript(LOCATION_ELEMENT_JS, element);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 用于判断元素是否存在
	 * @param element {@link Element}对象
	 * @return 当前指定的元素是否存在
	 */
	protected boolean isExistElement(Element element) {
		//判断元素是否存在，若返回元素时抛出异常，则返回false
		try {
			element.getWebElement();
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}
	
	/**
	 * 用于将字符串数组内容转成字符串形式返回
	 * @param keys 字符串数组
	 * @return 数组文本
	 */
	protected String arrayToString(String...keys) {
		if (keys == null || keys.length == 0) {
			return "[]";
		}
		
		StringBuilder text = new StringBuilder("[");
		Arrays.asList(keys).forEach(key -> {
			text.append(key);
			text.append(", ");
		});
		
		return text.substring(0, text.lastIndexOf(", "));
	}
}
