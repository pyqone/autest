package pres.auxiliary.selenium.event.inter;

import org.openqa.selenium.WebElement;

import pres.auxiliary.selenium.event.Event;
import pres.auxiliary.selenium.event.EventResultEnum;

/**
 * <p><b>文件名：</b>ClearInter.java</p>
 * <p><b>用途：</b>该接口定义控件内容清空事件的实现方法，可直接使用</p>
 * <p><b>编码时间：</b>2019年9月6日上午8:19:34</p>
 * <p><b>修改时间：</b>2019年9月6日上午8:19:34</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public interface ClearInter {
	/**
	 * 用于判断标签名是否为“input”
	 */
	static final String TAGNAME_INPUT = "input";
	/**
	 * 该方法通过控件名称或定位方式对页面元素进行定位来清空控件中的内容，主要用于清空文本框中已有的数据。
	 * 本操作的的返回值是{@link EventResultEnum#STRING}，其枚举的值为输入到控件的内容；参见{@link EventResultEnum}类。
	 * 该方法将返回值可通过类{@link Event}中的{@link Event#getStringValve()}方法进行返回。
	 * @param element 通过查找页面得到的控件元素对象
	 */
	static void clear(WebElement element) {
		// 判断元素是否为input元素，如果是，则获取其value值，若不是则调用getText()方法
		if (TAGNAME_INPUT.equalsIgnoreCase(element.getTagName())) {
			Event.setValue(EventResultEnum.STRING.setValue(element.getAttribute("value")));
		} else {
			Event.setValue(EventResultEnum.STRING.setValue(element.getText()));
		}
		//清空元素中的内容
		element.clear();
	}
}
