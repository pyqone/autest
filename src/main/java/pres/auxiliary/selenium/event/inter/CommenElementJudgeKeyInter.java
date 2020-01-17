package pres.auxiliary.selenium.event.inter;

import pres.auxiliary.selenium.event.Event;
import pres.auxiliary.selenium.event.EventResultEnum;

/**
 * <p>
 * <b>文件名：</b>CommenElementJudgeKeyInter.java
 * </p>
 * <p>
 * <b>用途：</b>该接口定义了普通元素对多个关键词判断事件的实现标准
 * </p>
 * <p>
 * <b>编码时间：</b>2019年9月2日下午2:28:59
 * </p>
 * <p>
 * <b>修改时间：</b>2019年9月2日下午2:28:59
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
@FunctionalInterface
public interface CommenElementJudgeKeyInter extends JudgeKeyInter {
	/**
	 * <p>
	 * 通过传入在xml文件中的控件名称，到类中指向的xml文件中查找控件名称对应的定位方式，或直接传入xpath与css定位方式，
	 * 根据定位方式在页面查找元素并获取控件中的文本，判断文本中是否包含传入的关键词，根据keyFull来判断是否需要所有的
	 * 关键词都包含在文本中，例如有如下标签：<br>
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
	 * @param name    控件的名称或xpath与css定位方式
	 * @param keyFull 是否需要所有关键词都包含
	 * @param keys    关键词组
	 * @return {@link Event}类对象，可通过{@link Event#getStringValve()}方法获取操作的返回值，
	 * 亦可通过{@link Event#getBooleanValve()}方法将枚举值转换为boolean进行返回
	 */
	Event judgeKey(String name, boolean keyFull, String... keys);
}
