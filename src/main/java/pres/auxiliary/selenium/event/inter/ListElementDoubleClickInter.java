package pres.auxiliary.selenium.event.inter;

import pres.auxiliary.selenium.event.Event;
import pres.auxiliary.selenium.event.EventResultEnum;

/**
 * <p><b>文件名：</b>ListElementDoubleClickInter.java</p>
 * <p><b>用途：</b>该接口定义了列表元素鼠标左键双击事件的实现标准</p>
 * <p><b>编码时间：</b>2019年8月29日下午3:22:49</p>
 * <p><b>修改时间：</b>2019年8月29日下午3:22:49</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
@FunctionalInterface
public interface ListElementDoubleClickInter extends DoubleClickInter {
	/**
	 * 根据列表类返回的对象，进行鼠标左键双击操作。
	 * 
	 * @return Event类对象，可通过该类继续写操作，以及获取返回值，参见{@link Event}类。
	 * 本操作的的返回值是EventResultEnum.VOID；参见{@link EventResultEnum}类。
	 */
	Event doubleClick();
}
