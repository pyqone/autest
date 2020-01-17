package pres.auxiliary.selenium.event.inter;

import pres.auxiliary.selenium.event.Event;
import pres.auxiliary.selenium.event.EventResultEnum;

/**
 * <p><b>文件名：</b>ListElementJudgeKeyInter.java</p>
 * <p><b>用途：</b>该接口定义了列表元素对多个关键词判断事件的实现标准</p>
 * <p><b>编码时间：</b>2019年10月8日上午8:35:42</p>
 * <p><b>修改时间：</b>2019年10月8日上午8:35:42</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
@FunctionalInterface
public interface ListElementJudgeKeyInter extends JudgeKeyInter {
	/**
	 * <p>
	 * 用于判断文本中是否包含传入的关键词，根据keyFull来判断是否需要所有的关键词都包含在文本中，例如有如下标签：<br>
	 * &lt;p id="h"&gt;请输入正确填写关键词&lt;/p&gt;
	 * </p>
	 * <p>
	 * 则如下调用时，结果为true：<br>
	 * judgeKey("//p[@id='h']", true, "请输入", "关键词");<br>
	 * judgeKey("//p[@id='h']", false, "标题", "关键词");
	 * </p>
	 * <p>
	 * 如下调用时，结果为false：<br>
	 * judgeKey("//p[@id='h']", true, "标题", "关键词");<br>
	 * judgeKey("//p[@id='h']", false, "标题", "内容");
	 * </p>
	 * <p>
	 * 本操作返回的枚举值是{@link EventResultEnum#BOOLEAN_FALSE}或，{@link EventResultEnum#BOOLEAN_TRUE}
	 * 其枚举的value值为判断的结果，以字符串的形式存储"false"及"true"，可参见{@link EventResultEnum}类。
	 * </p>
	 * 
	 * @param keyFull 是否需要所有关键词都包含
	 * @param keys    关键词组
	 * @return {@link Event}类对象，可通过{@link Event#getStringValve()}方法获取操作的返回值，
	 * 亦可通过{@link Event#getBooleanValve()}方法将枚举值转换为boolean进行返回
	 */
	Event judgeKey(boolean keyFull, String... keys);
}
