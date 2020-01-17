package pres.auxiliary.selenium.event.inter;

import org.openqa.selenium.WebElement;

import pres.auxiliary.selenium.event.Event;
import pres.auxiliary.selenium.event.EventResultEnum;

/**
 * <p>
 * <b>文件名：</b>JudgeKey.java
 * </p>
 * <p>
 * <b>用途：</b>该接口定义页面文本与指定文本的判断方法，该方法可传入多个关键词，对页面的数据进行逐一
 * 比对，根据传参判断是否要完全满足。该关键词的对比为模糊对比 ，只需要关键词在页面文本中包含即可
 * </p>
 * <p>
 * <b>编码时间：</b>2019年9月1日上午9:52:04
 * </p>
 * <p>
 * <b>修改时间：</b>2019年9月1日上午9:52:04
 * </p>
 * 
 * @author
 * @version Ver1.0
 * @since JDK 12
 *
 */
public interface JudgeKeyInter {
	/**
	 * <p>
	 * 用于判断文本中是否包含传入的关键词，根据keyFull来判断是否需要所有的关键词都包含在文本中，例如有如下标签：<br>
	 * &lt;p id="h"&gt;请输入正确填写关键词&lt;/p&gt;<br>
	 * <br>
	 * 则如下调用时，结果为true：<br>
	 * judgeKey("//p[@id='h']", true, "请输入", "关键词");<br>
	 * judgeKey("//p[@id='h']", false, "标题", "关键词");<br>
	 * <br>
	 * 如下调用时，结果为false：<br>
	 * judgeKey("//p[@id='h']", true, "标题", "关键词");<br>
	 * judgeKey("//p[@id='h']", false, "标题", "内容");<br>
	 * </p>
	 * <p>本操作的的返回值是{@link EventResultEnum#BOOLEAN_FALSE}或，{@link EventResultEnum#BOOLEAN_TRUE}，
	 * 其枚举的值为输入到控件的内容；参见{@link EventResultEnum}类。
	 * 该方法将返回值可通过类{@link Event}中的{@link Event#getStringValve()}方法进行返回，
	 * 亦可通过{@link Event#getBooleanValue()}方法进行返回。
	 * </p>
	 * 
	 * @param element 通过查找页面得到的控件元素的WebElement对象
	 * @param keyFull 是否需要所有关键词都包含
	 * @param keys    关键词组
	 */
	static void judgeKey(WebElement element, boolean keyFull, String... keys) {
		//获取当前元素的文本
		GetTextInter.getText(element);
		// 将文本存储至text中
		String text = Event.getStringValve();

		// 循环，判断文本是否包含所有关键词
		for (String key : keys) {
			// 判断文字是否包含关键词，若未包含，则设置为false，若包含则设置为true
			if (text.indexOf(key) == -1) {
				//由于Event中存储的返回值是以最后一次存储的值为主，故此处可以直接使用此方法以达到存储返回值的目的
				Event.setValue(EventResultEnum.BOOLEAN_FALSE);
				// 若未包含且需要关键词全包含时（keyFull为true时），可直接返回false
				if (keyFull) {
					return;
				}
			} else { 
				Event.setValue(EventResultEnum.BOOLEAN_TRUE);
				// 若未包含且不需要关键词全包含时（keyFull为false时），可直接返回true
				if (!keyFull) {
					Event.setValue(EventResultEnum.BOOLEAN_TRUE);
					return;
				}
			}
		}
	}
}
