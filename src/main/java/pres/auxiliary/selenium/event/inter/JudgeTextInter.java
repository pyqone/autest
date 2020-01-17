package pres.auxiliary.selenium.event.inter;

import org.openqa.selenium.WebElement;

import pres.auxiliary.selenium.event.Event;
import pres.auxiliary.selenium.event.EventResultEnum;

/**
 * <p>
 * <b>文件名：</b>JudgeKeyInter.java
 * </p>
 * <p>
 * <b>用途：</b>该接口定义页面文本与指定文本的判断方法，该方法用传入的关键词与页面对比，可精准对比，亦可模糊对比
 * </p>
 * <p>
 * <b>编码时间：</b>2019年8月30日下午7:52:43
 * </p>
 * <p>
 * <b>修改时间：</b>2019年9月29日下午9:23:43
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public interface JudgeTextInter {
	/**
	 * 判断控件中的文本是否包含或者与关键词一致
	 * <p>本操作的的返回值是{@link EventResultEnum#BOOLEAN_FALSE}或，{@link EventResultEnum#BOOLEAN_TRUE}，
	 * 其枚举的值为输入到控件的内容；参见{@link EventResultEnum}类。
	 * 该方法将返回值可通过类{@link Event}中的{@link Event#getStringValve()}方法进行返回，
	 * 亦可通过{@link Event#getBooleanValue()}方法进行返回。
	 * </p>
	 * 
	 * @param element  通过查找页面得到的控件元素的WebElement对象
	 * @param keyFull 是否需要与页面文本完全一致
	 * @param text     需要判断的字符号串
	 */
	static void judgeText(WebElement element, boolean keyFull, String key) {
		// 获取元素指向的信息的内容文本
		GetTextInter.getText(element);
		String text = Event.getStringValve();

		// 判断控件文本是否需要完整包含
		if (keyFull) {
			// 判断获取的文字一致
			if (text.equals(key)) {
				Event.setValue(EventResultEnum.BOOLEAN_TRUE);
			} else {
				Event.setValue(EventResultEnum.BOOLEAN_FALSE);
			}
		} else {
			// 判断文字是否包含
			if (text.indexOf(key) > -1) {
				Event.setValue(EventResultEnum.BOOLEAN_TRUE);
			} else {
				Event.setValue(EventResultEnum.BOOLEAN_FALSE);
			}
		}
	}
}
