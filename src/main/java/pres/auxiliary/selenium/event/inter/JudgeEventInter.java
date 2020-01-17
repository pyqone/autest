package pres.auxiliary.selenium.event.inter;

import pres.auxiliary.selenium.event.Event;
import pres.auxiliary.selenium.event.EventResultEnum;

/**
 * <p><b>文件名：</b>JudgeEventInter.java</p>
 * <p><b>用途：</b>该接口集合普通元素的所有判断相关的事件，可在此处扩展新的方法</p>
 * <p><b>编码时间：</b>2019年9月2日下午3:00:19</p>
 * <p><b>修改时间：</b>2019年9月2日下午3:00:19</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public interface JudgeEventInter
		extends CommenElementJudgeKeyInter, CommenElementJudgeTextInter {
	/**
	 * 判断页面上是否存在name对应的控件。.本操作返回的枚举值是{@link EventResultEnum#BOOLEAN_FALSE}或，{@link EventResultEnum#BOOLEAN_TRUE}
	 * 其枚举的value值为判断的结果，以字符串的形式存储"false"及"true"，可参见{@link EventResultEnum}类。
	 * @param name 控件名称控件定位的xpath或者css路径
	 * @return {@link Event}类对象，可通过{@link Event#getStringValve()}方法获取操作的返回值，
	 * 亦可通过{@link Event#getBooleanValve()}方法将枚举值转换为boolean进行返回
	 */
	Event judgeControl(String name);
}
