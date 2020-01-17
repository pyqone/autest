package pres.auxiliary.selenium.event.inter;

import pres.auxiliary.selenium.event.Event;
import pres.auxiliary.selenium.event.EventResultEnum;

/**
 * <p><b>文件名：</b>CommenElementGetAttributeValueInter.java</p>
 * <p><b>用途：</b>该接口定义了获取列表元素在html标签下属性值事件的实现标准</p>
 * <p><b>编码时间：</b>2019年9月6日上午8:45:54</p>
 * <p><b>修改时间：</b>2019年9月6日上午8:45:54</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
@FunctionalInterface
public interface ListElementGetAttributeValueInter extends GetAttributeValueInter {
	/**
	 * 用于获取页面元素的某一属性值的内容。本操作返回的枚举值
	 * 是{@link EventResultEnum#STRING}，其枚举的value值为获取到的控件指定属性的内容，可参见{@link EventResultEnum}类。
	 * 
	 * @param attributeName 属性名称
	 * @return {@link Event}类对象，可通过{@link Event#getStringValve()}方法获取操作的返回值
	 */
	Event getAttributeValue(String attributeName);
}
