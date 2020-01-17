package pres.auxiliary.selenium.event.inter;

import pres.auxiliary.selenium.event.Event;
import pres.auxiliary.selenium.event.EventResultEnum;

/**
 * <p><b>文件名：</b>ListElementInputInter.java</p>
 * <p><b>用途：</b>该接口定义了普通元素输入事件的实现标准</p>
 * <p><b>编码时间：</b>2019年10月8日上午8:25:39</p>
 * <p><b>修改时间：</b>2019年10月8日上午8:25:39</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public interface ListElementInputInter extends InputInter {
	/**
	 * 根据元素对象，在其控件中进行输入指定的内容。
	 * 本操作返回的枚举值是{@link EventResultEnum#STRING}，其枚举的value值为输入到控件中的内容，可参见{@link EventResultEnum}类。
	 * @param text 待输入的内容
	 * @return {@link Event}类对象，可通过{@link Event#getStringValve()}方法获取操作的返回值
	 */
	Event input(String text);
}
