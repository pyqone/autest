package pres.auxiliary.selenium.event.inter;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import pres.auxiliary.selenium.event.Event;
import pres.auxiliary.selenium.event.EventResultEnum;

/**
 * <p><b>文件名：</b>DoubleClickInter.java</p>
 * <p><b>用途：</b>该接口定义鼠标左键双击事件的实现方法，可直接使用</p>
 * <p><b>编码时间：</b>2019年8月29日下午3:18:21</p>
 * <p><b>修改时间：</b>2019年12月29日下午1:41:45</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public interface DoubleClickInter {
	/**
	 * 鼠标左键双击事件的处理方法。本操作的的返回值是{@link EventResultEnum#VOID}，其
	 * 枚举的值为空串；参见{@link EventResultEnum}类。该方法将返回值可通过类{@link Event}中
	 * 的{@link Event#getStringValve()}方法进行返回。
	 * 
	 * @param driver 页面WebDriver对象
	 * @param element 通过查找页面得到的控件元素的WebElement对象
	 */
	static void doubleClick(WebDriver driver, WebElement element) {
		//进行元素的点击事件
		new Actions(driver).doubleClick(element).perform();
		
		Event.setValue(EventResultEnum.VOID);
	}
}
