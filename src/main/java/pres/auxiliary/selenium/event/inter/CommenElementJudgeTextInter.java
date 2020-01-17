package pres.auxiliary.selenium.event.inter;

import pres.auxiliary.selenium.event.Event;
import pres.auxiliary.selenium.event.EventResultEnum;

/**
 * <p>
 * <b>文件名：</b>CommenElementJudgeTextInter.java
 * </p>
 * <p>
 * <b>用途：</b>该接口定义了普通元素对单个关键词判断事件的实现标准
 * </p>
 * <p>
 * <b>编码时间：</b>2019年9月2日下午2:39:26
 * </p>
 * <p>
 * <b>修改时间：</b>2019年9月2日下午2:39:26
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
@FunctionalInterface
public interface CommenElementJudgeTextInter extends JudgeTextInter {
	/**
	 * 通过传入在xml文件中的控件名称，到类中指向的xml文件中查找控件名称对应的定位方式，或直接传入xpath与css定位方式，
	 * 根据定位方式在页面查找元素并判断控件中的文本是否包含或者与关键词一致。本操作返回的枚举值是{@link EventResultEnum#BOOLEAN_FALSE}或，{@link EventResultEnum#BOOLEAN_TRUE}
	 * 其枚举的value值为判断的结果，以字符串的形式存储"false"及"true"，可参见{@link EventResultEnum}类。
	 * 
	 * @param name     控件名称控件定位的xpath或者css路径
	 * @param textFull 是否需要与页面文本完全一致
	 * @param key      关键词
	 * @return {@link Event}类对象，可通过{@link Event#getStringValve()}方法获取操作的返回值，
	 * 亦可通过{@link Event#getBooleanValve()}方法将枚举值转换为boolean进行返回
	 */
	Event judgeText(String name, boolean keyFull, String key);
}
