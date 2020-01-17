package pres.auxiliary.selenium.event.inter;

import pres.auxiliary.selenium.event.Event;
import pres.auxiliary.selenium.event.EventResultEnum;

/**
 * <p><b>文件名：</b>CommenElementDoubleClickInter.java</p>
 * <p><b>用途：</b>该接口定义了普通元素鼠标左键双击事件的实现标准</p>
 * <p><b>编码时间：</b>2019年8月29日下午3:21:31</p>
 * <p><b>修改时间：</b>2019年8月29日下午3:21:31</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
@FunctionalInterface
public interface CommenElementDoubleClickInter extends DoubleClickInter {
	/**
	 * 通过传入传入的控件名称，到类中指向的xml文件中查找控件名称对应的定位方式，进行鼠标左键双击操作。
	 * 本操作返回的枚举值是{@link EventResultEnum#VOID}，其枚举的value值为空串，可参见{@link EventResultEnum}类。
	 * 
	 * @param name 控件的名称或xpath与css定位方式
	 * @return {@link Event}类对象，可通过{@link Event#getStringValve()}方法获取操作的返回值
	 */
	Event doubleClick(String name);
}
