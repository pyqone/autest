package pres.auxiliary.selenium.event.inter;

import pres.auxiliary.selenium.event.Event;
import pres.auxiliary.selenium.event.EventResultEnum;

/**
 * <p>
 * <b>文件名：</b>CommenElementClearInter.java
 * </p>
 * <p>
 * <b>用途：</b>该接口定义了列表元素内容清空事件的实现标准
 * </p>
 * <p>
 * <b>编码时间：</b>2019年9月6日上午8:31:24
 * </p>
 * <p>
 * <b>修改时间：</b>2019年10月8日上午8:31:24
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
@FunctionalInterface
public interface ListElementClearInter extends ClearInter {
	/**
	 * 该方法通过页面元素对象对页面元素进行清空内容的操作，主要用于清空文本框中已有的数据。
	 * 本操作返回的枚举值是{@link EventResultEnum#STRING}，其枚举的value值为控件中原有的内容，可参见{@link EventResultEnum}类。
	 * 
	 * @return {@link Event}类对象，可通过{@link Event#getStringValve()}方法获取操作的返回值
	 * 
	 */
	Event clear();
}
