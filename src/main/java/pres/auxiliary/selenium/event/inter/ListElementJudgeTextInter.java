package pres.auxiliary.selenium.event.inter;

import pres.auxiliary.selenium.event.Event;
import pres.auxiliary.selenium.event.EventResultEnum;

/**
 * <p><b>文件名：</b>ListElementJudgeTextInter.java</p>
 * <p><b>用途：</b>该接口定义了列表元素对单个关键词判断事件的实现标准</p>
 * <p><b>编码时间：</b>2019年10月8日上午8:37:35</p>
 * <p><b>修改时间：</b>2019年10月8日上午8:37:35</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
@FunctionalInterface
public interface ListElementJudgeTextInter extends JudgeTextInter {
	/**
	 * 判断控件中的文本是否包含或者与关键词一致。本操作返回的枚举值是{@link EventResultEnum#BOOLEAN_FALSE}或，{@link EventResultEnum#BOOLEAN_TRUE}
	 * 其枚举的value值为判断的结果，以字符串的形式存储"false"及"true"，可参见{@link EventResultEnum}类。
	 * 
	 * @param textFull 是否需要与页面文本完全一致
	 * @param key      关键词
	 * @return {@link Event}类对象，可通过{@link Event#getStringValve()}方法获取操作的返回值，
	 * 亦可通过{@link Event#getBooleanValve()}方法将枚举值转换为boolean进行返回
	 */
	Event judgeText(boolean keyFull, String key);
}
