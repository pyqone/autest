package pres.auxiliary.selenium.event.inter;

import org.openqa.selenium.WebElement;

import pres.auxiliary.selenium.event.Event;
import pres.auxiliary.selenium.event.EventResultEnum;

/**
 * <p><b>文件名：</b>GetTextInter.java</p>
 * <p><b>用途：</b>该接口定义获取元素内容事件的实现方法，可直接使用</p>
 * <p><b>编码时间：</b>2019年9月6日上午8:33:47</p>
 * <p><b>修改时间：</b>2019年9月26日下午7:02:57</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public interface GetTextInter {
	/**
	 * 用于判断标签名是否为“input”
	 */
	static final String TAGNAME_INPUT = "input";
	
	/**
	 * 该方法通过传入页面元素对象进行定位来获取其中的内容
	 * @param element 通过查找页面得到的控件元素对象
	 */
	static void getText(WebElement element) {
		// 判断元素是否为input元素，如果是，则获取其value值，若不是则调用getText()方法
		if (TAGNAME_INPUT.equalsIgnoreCase(element.getTagName())) {
			Event.setValue(EventResultEnum.STRING.setValue(element.getAttribute("value")));
		} else {
			Event.setValue(EventResultEnum.STRING.setValue(element.getText()));
		}
	}
}
