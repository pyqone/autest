package pres.auxiliary.selenium.event.inter;

import pres.auxiliary.selenium.event.Event;
import pres.auxiliary.selenium.event.EventResultEnum;

/**
 * <p><b>文件名：</b>CommenElementRightClickInter.java</p>
 * <p><b>用途：</b>该接口定义了普通元素鼠标右键单击事件的实现标准</p>
 * <p><b>编码时间：</b>2019年8月29日下午3:21:55</p>
 * <p><b>修改时间：</b>2019年8月29日下午3:21:55</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
@FunctionalInterface
public interface CommenElementRightClickInter extends RightClickInter {
	/**
	 * 通过传入在xml文件中的控件名称，到类中指向的xml文件中查找控件名称对应的定位方式，或直接传入xpath与css定位方式，
	 * 根据定位方式在页面查找元素并进行鼠标右键单击操作。
	 * 本操作返回的枚举值是{@link EventResultEnum#BOOLEAN_FALSE}或，{@link EventResultEnum#BOOLEAN_TRUE}
	 * 其枚举的value值为判断的结果，以字符串的形式存储"false"及"true"，可参见{@link EventResultEnum}类。
	 * 
	 * @param name 控件的名称或xpath与css定位方式
	 * @return {@link Event}类对象，可通过{@link Event#getStringValve()}方法获取操作的返回值，
	 * 亦可通过{@link Event#getBooleanValve()}方法将枚举值转换为boolean进行返回
	 */
	Event rightClick(String name);
}
