package com.auxiliary.selenium.event;

import java.time.Duration;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.auxiliary.selenium.brower.AbstractBrower;
import com.auxiliary.selenium.element.Element;

/**
 * <p>
 * <b>文件名：</b>WaitEvent.java
 * </p>
 * <p>
 * <b>用途：</b> 定义了对元素进行特殊事件等待的方法，包括等待控件消失、等待控件出现文字等， 可通过该类，等待元素进行一定的变化的操作
 * </p>
 * <p>
 * <b>编码时间：</b>2020年10月19日上午7:21:05
 * </p>
 * <p>
 * <b>修改时间：</b>2020年10月19日上午7:21:05
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 *
 */
public class WaitEvent extends AbstractEvent {
	/**
	 * 控制等待
	 */
	private WebDriverWait eventWait;

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
		eventWait = new WebDriverWait(brower.getDriver(), Duration.ofSeconds(waitTime), Duration.ofMillis(200));
		eventWait.withMessage("等待超时，事件等待失败，超时时间：" + waitTime + "秒");
	}

	/**
	 * 用于设置元素查询超时时间，单位为秒
	 * 
	 * @param overtime 超时时间
	 */
	public void setOvertime(long overtime) {
		eventWait.pollingEvery(Duration.ofSeconds(overtime < 0 ? 60L : overtime));
	}

	/**
	 * 用于等待元素消失，若元素无法获取到，则也判定为元素不存在
	 * 
	 * @param element {@link Element}对象
	 * @return 元素是否已消失
	 * @throws TimeoutException 等待超时时抛出的异常
	 */
	public boolean disappear(Element element) {
		eventWait.withMessage("等待超时，元素“" + element.getElementData().getName() + "”仍然存在，超时时间：" + waitTime + "秒");
		boolean result = eventWait.until(driver -> {
			// 调用isDisplayed()方法，判断元素是否存在
			try {
				return !element.getWebElement().isDisplayed();
			} catch (NoSuchElementException | TimeoutException e) {
				// 若在调用获取页面元素时抛出NoSuchElementException异常，则说明元素本身不存在，则直接返回true
				return true;
			} catch (StaleElementReferenceException e) {
				// 若抛出StaleElementReferenceException异常，则说明页面过期，重新获取元素后再进行判断
				element.againFindElement();
				return false;
			}
		});

		logText = "等待“" + element.getElementData().getName() + "”元素从页面消失";
		resultText = String.valueOf(result);

		return result;
	}

	/**
	 * 该方法用于根据元素信息，在元素被加载后，等待元素在页面中出现，并返回元素出现的结果，
	 * 注意，等待元素出现的超时时间与元素中设置的超时时间一致，与类中超时时间无关
	 * 
	 * @param element {@link Element}对象
	 * @return 元素是否出现
	 * @throws TimeoutException 等待超时时抛出的异常
	 */
	public boolean appear(Element element) {
		eventWait.withMessage("等待超时，元素“" + element.getElementData().getName() + "”仍然不存在，超时时间：" + waitTime + "秒");
		boolean result = eventWait.until(driver -> {
			// 调用isDisplayed()方法，判断元素是否存在
			try {
				return element.getWebElement().isDisplayed();
			} catch (StaleElementReferenceException | NoSuchElementException e) {
				// 若抛出StaleElementReferenceException异常，则说明页面过期，重新获取元素后再进行判断
				// 若抛出NoSuchElementException异常，则说明当前元素不存在，则再重新进行一次获取
				element.againFindElement();
				return false;
			}
		});

		logText = "等待“" + element.getElementData().getName() + "”元素出现在页面";
		resultText = String.valueOf(result);

		return result;
	}

	/**
	 * 该方法用于等待指定元素中显示相应的文本，可指定显示文本的关键词，直到显示相应的关键词为止，
	 * 若不传入关键词，则只判断元素加载出文本。若元素未出现，则返回false
	 * 
	 * @param element {@link Element}对象
	 * @param keys    需要判断的文本
	 * @return 元素是否存在文本或包含指定文本
	 * @throws TimeoutException 等待超时时抛出的异常
	 */
	public boolean showText(Element element, String... keys) {
		// 判断是否传入关键词数组，根据是否传入数组，调用不同的判断方式
		if (keys == null || keys.length == 0) {
			logText = "等待“" + element.getElementData().getName() + "”元素加载出文本内容";

			// 判断元素是否存在，若不存在，则直接返回false
			if (!isExistElement(element)) {
				resultText = Boolean.toString(false);
				return false;
			} else {
				eventWait.withMessage(
						"等待超时，元素“" + element.getElementData().getName() + "”仍未出现文本，超时时间：" + waitTime + "秒");
				TextEvent textEvent = new TextEvent(brower);
				boolean result = eventWait.until(driver -> {
					// 调用isDisplayed()方法，判断元素是否存在
					try {
						return !textEvent.getText(element).isEmpty();
					} catch (StaleElementReferenceException e) {
						// 若抛出StaleElementReferenceException异常，则说明页面过期，重新获取元素后再进行判断
						element.againFindElement();
						return false;
					}
				});

				resultText = String.valueOf(result);
				return result;
			}
		} else {
			String keyText = arrayToString(keys);
			logText = "等待“" + element.getElementData().getName() + "”元素出现文本关键词：" + keyText;

			// 判断元素是否存在，若不存在，则直接返回false
			if (isExistElement(element)) {
				resultText = Boolean.toString(false);
				return false;
			} else {
				eventWait.withMessage("等待超时，元素“" + element.getElementData().getName() + "”仍未出现关键词“" + keyText
						+ "”，超时时间：" + waitTime + "秒");
				AssertEvent assertEvent = new AssertEvent(brower);
				boolean result = eventWait.until(driver -> {
					// 断言方法，直到方法返回true为止
					try {
						return assertEvent.assertTextContainKey(element, true, keys);
					} catch (StaleElementReferenceException e) {
						// 若抛出StaleElementReferenceException异常，则说明页面过期，重新获取元素后再进行判断
						element.againFindElement();
						return false;
					}
				});

				resultText = String.valueOf(result);
				return result;
			}
		}
	}
}
