package pres.auxiliary.work.selenium.event;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * <p><b>文件名：</b>AbstractEvent.java</p>
 * <p><b>用途：</b>所有事件类的基类，包含了事件类所用到的基础方法以及弹窗处理方法和窗体、窗口的切换方法</p>
 * <p><b>编码时间：</b>2019年9月24日下午4:24:15</p>
 * <p><b>修改时间：</b>2020年5月10日 下午3:42:15</p>
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
	 * 设置显示等待的超时时间（默认3秒）
	 */
	long waitTime = 3;
	
	/**
	 * 用于在记录步骤时需要替换的元素名称文本
	 */
	public static final String ELEMENT_NAME = "&{元素名称}";
	
	/**
	 * 存储事件的返回结果
	 */
	String result = "";
	/**
	 * 存储事件的操作说明
	 */
	String step = "";

	/**
	 * 构造对象并存储浏览器的WebDriver对象
	 * 
	 * @param driver 浏览器的WebDriver对象
	 */
	public AbstractEvent(WebDriver driver) {
		this.driver = driver;
	}

	/**
	 * 用于设置等待时间
	 * 
	 * @param waitTime 等待时间
	 */
	public void setWaitTime(long waitTime) {
		this.waitTime = waitTime;
	}
	
	/**
	 * 用于获取事件的返回结果
	 * @return 事件的返回结果
	 */
	public String getResult() {
		return result;
	}
	
	/**
	 * 用于获取事件的文字说明，其文字存在一个可以替换元素名称的预留字段，可根据参数确定返回时是否需要保留该字段
	 * @param clearElementName 是否清除预留字段
	 * @return 操作的文本说明
	 */
	public String getStep(boolean clearElementName) {
		return clearElementName ? step.replaceAll(ELEMENT_NAME, "") : step;
	}
	
	/**
	 * 用于将页面控件元素高亮显示
	 * @param element 当前指向的元素
	 */
	void elementHight(WebElement element) {
		//获取当前指向的元素的style属性
		String elementStyle = element.getAttribute("style");
		// 控件高亮
		((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('style',arguments[1])", element,
				elementStyle + "background:yellow;solid:red;");
		
		//解除高亮
		((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('style',arguments[1])",
				element, elementStyle + "background:yellow;solid:red;");
	}
}
