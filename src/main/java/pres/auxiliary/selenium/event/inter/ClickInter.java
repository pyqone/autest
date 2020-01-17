package pres.auxiliary.selenium.event.inter;

import org.openqa.selenium.WebElement;

import pres.auxiliary.selenium.event.Event;
import pres.auxiliary.selenium.event.EventResultEnum;

/**
 * <p><b>文件名：</b>ClickInter.java</p>
 * <p><b>用途：</b>该接口定义鼠标左键单击事件的实现方法，可直接使用</p>
 * <p><b>编码时间：</b>2019年8月29日下午3:16:45</p>
 * <p><b>修改时间：</b>2019年12月29日下午1:41:45</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public interface ClickInter {
	/**
	 * 鼠标左键单击事件的处理方法。本操作的的返回值是{@link EventResultEnum#VOID}，
	 * 其枚举的值为空串；参见{@link EventResultEnum}类。该方法将返回值可通过类{@link Event}中
	 * 的{@link Event#getStringValve()}方法进行返回。
	 * 
	 * @param element 通过查找页面得到的控件元素对象
	 */
	static void click(WebElement element) {
		//进行元素的点击事件
		element.click();
		//设置返回值为空
		Event.setValue(EventResultEnum.VOID);
	}
}
