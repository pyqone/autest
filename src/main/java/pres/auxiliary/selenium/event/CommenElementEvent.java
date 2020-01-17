package pres.auxiliary.selenium.event;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * <p><b>文件名：</b>AbstractEvent.java</p>
 * <p><b>用途：</b>用于定义所有普通元素的事件类的基础方法，所有普通元素的事件类必须继承该类</p>
 * <p><b>编码时间：</b>2019年8月29日下午3:23:30</p>
 * <p><b>修改时间：</b>2019年9月25日下午3:04:30</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public abstract class CommenElementEvent extends AbstractEvent {
	/**
	 * 存储元素的原style属性，用以控件高亮
	 */
	protected String style = "";
	/**
	 * 存储获取到的元素
	 */
	protected WebElement element = null;
	
	/**
	 * 构造对象并存储浏览器的WebDriver对象
	 * 
	 * @param driver 浏览器的WebDriver对象
	 */
	public CommenElementEvent(WebDriver driver) {
		super(driver);
	}
}
