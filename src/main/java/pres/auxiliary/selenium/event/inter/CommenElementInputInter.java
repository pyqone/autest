package pres.auxiliary.selenium.event.inter;

import pres.auxiliary.selenium.event.Event;
import pres.auxiliary.selenium.event.EventResultEnum;

/**
 * <p><b>文件名：</b>CommenElementInputInter.java</p>
 * <p><b>用途：</b>该接口定义了普通元素输入事件的实现标准</p>
 * <p><b>编码时间：</b>2019年9月6日上午8:17:21</p>
 * <p><b>修改时间：</b>2019年9月6日上午8:17:21</p>
 * @author 
 * @version Ver1.0
 * @since JDK 12
 *
 */
@FunctionalInterface
public interface CommenElementInputInter extends InputInter {
	/**
	 * 通过传入在xml文件中的控件名称，到类中指向的xml文件中查找控件名称对应的定位方式，或直接传入xpath与css定位方式，
	 * 根据定位方式在页面查找元素并在控件中输入指定的内容。
	 * 本操作返回的枚举值是{@link EventResultEnum#STRING}，其枚举的value值为输入到控件中的内容，可参见{@link EventResultEnum}类。
	 * @param name 控件的名称或xpath与css定位方式
	 * @param text 待输入的内容
	 * @return {@link Event}类对象，可通过{@link Event#getStringValve()}方法获取操作的返回值
	 */
	Event input(String name, String text);
}
