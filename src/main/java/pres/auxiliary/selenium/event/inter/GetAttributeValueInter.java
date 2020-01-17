package pres.auxiliary.selenium.event.inter;

import org.openqa.selenium.WebElement;

import pres.auxiliary.selenium.event.Event;
import pres.auxiliary.selenium.event.EventResultEnum;

/**
 * <p><b>文件名：</b>GetAttributeValueInter.java</p>
 * <p><b>用途：</b>该接口定义获取元素在html标签下属性值事件的实现方法，可直接使用</p>
 * <p><b>编码时间：</b>2019年9月6日上午8:39:57</p>
 * <p><b>修改时间：</b>2019年9月26日下午7:02:57</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public interface GetAttributeValueInter {
	/**
	 * 用于获取页面元素的某一属性值的内容
	 * @param element 控件的名称或xpath与css定位方式
	 * @param attributeName 属性名称
	 */
	static void getAttributeValue(WebElement element, String attributeName) {
		//获取元素的属性值，将其存储至枚举中
		Event.setValue(EventResultEnum.STRING.setValue(element.getAttribute(attributeName)));
	}
}
