package pres.auxiliary.selenium.event.inter;

import pres.auxiliary.selenium.event.Event;
import pres.auxiliary.selenium.event.EventResultEnum;

 /**
 * <p><b>文件名：</b>ListElementGetTextInter.java</p>
 * <p><b>用途：</b>该接口定义了获取普通元素内容事件事件的实现标准</p>
 * <p><b>编码时间：</b>2019年9月6日上午8:37:20</p>
 * <p><b>修改时间：</b>2019年10月8日上午8:34:04</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
@FunctionalInterface
public interface ListElementGetTextInter extends GetTextInter {
	/**
	 * 该方法通过传入控件名称或定位方式对页面元素进行定位来获取其中的内容。
	 * 本操作返回的枚举值是{@link EventResultEnum#STRING}，其枚举的value值为控件中的文本内容，可参见{@link EventResultEnum}类。
	 * @return {@link Event}类对象，可通过{@link Event#getStringValve()}方法获取操作的返回值
	 */
	Event getText();
}
