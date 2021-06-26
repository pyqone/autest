package com.auxiliary.tool.asserts;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;

import com.auxiliary.selenium.element.Element;
import com.auxiliary.selenium.event.CompareNumberType;

/**
 * <p><b>文件名：</b>ContentAssertUtil.java</p>
 * <p><b>用途：</b>
 * 提供对文本内容进行判断的工具
 * </p>
 * <p><b>编码时间：</b>2021年6月24日上午8:05:14</p>
 * <p><b>修改时间：</b>2021年6月24日上午8:05:14</p>
 * @author 
 * @version Ver1.0
 * @since JDK 1.8
 */
public class AssertStringUtil {
	/**
	 * <p>
	 * 	断言元素的文本内容中包含指定的关键词。可设置元素的文本内容是否需要判断传入的所有关键词，即
	 * 	<ul>
	 * 		<li>当需要完全判断时，则当且仅当文本包含所有关键词时，断言方法返回true</li>
	 * 		<li>当不需要完全判断时，则当前仅当文本不包含所有关键词时，断言方法返回false</li>
	 * 	</ul>
	 * </p>
	 * <p>
	 * 	例如,假设元素中存在文本“你好，世界！”，则
	 * 	<ul>
	 * 		<li>需要判断的关键词为["你好", "Java"]时：
	 * 			<ol>
	 * 				<li>需要判断所有文本，此时断言方法返回false</li>
	 * 				<li>不需要判断所有文本，此时断言方法返回true</li>
	 * 			</ol>
	 * 		</li>
	 * 		<li>需要判断的关键词为["你好", "世界"]时：
	 * 			<ol>
	 * 				<li>需要判断所有文本，此时断言方法返回true</li>
	 * 				<li>不需要判断所有文本，此时断言方法返回true</li>
	 * 			</ol>
	 * 		</li>
	 * 		<li>需要判断的关键词为["Hello", "Java"]时：
	 * 			<ol>
	 * 				<li>需要判断所有文本，此时断言方法返回false</li>
	 * 				<li>不需要判断所有文本，此时断言方法返回false</li>
	 * 			</ol>
	 * 		</li>
	 * 	</ul>
	 * </p>
	 * <p>
	 * 	<b>注意：</b>若不传入关键词，或关键词为null时，则返回true
	 * </p>
	 * 
	 * @param element       {@link Element}对象
	 * @param isJudgeAllKey 是否需要完全判断所有关键词
	 * @param keys          关键词组
	 * @return 断言结果
	 * @throws TimeoutException 元素无法操作时抛出的异常 
	 * @throws NoSuchElementException 元素不存在或下标不正确时抛出的异常 
	 */
	public static boolean assertTextContainKey(String text, boolean isJudgeAllKey, String... keys) {
		return judgetText(text, isJudgeAllKey, false, keys);
	}
	
	/**
	 * <p>
	 * 	断言元素的文本内容中不包含指定的关键词。可设置元素的文本内容是否需要判断传入的所有关键词，即
	 * 	<ul>
	 * 		<li>当需要完全判断时，则当且仅当文本不包含所有关键词时，断言方法返回true</li>
	 * 		<li>当不需要完全判断时，则当前仅当文本包含所有关键词时，断言方法返回false</li>
	 * 	</ul>
	 * </p>
	 * <p>
	 * 	例如,假设元素中存在文本“你好，世界！”，则
	 * 	<ul>
	 * 		<li>需要判断的关键词为["你好", "Java"]时：
	 * 			<ol>
	 * 				<li>需要判断所有文本，此时断言方法返回false</li>
	 * 				<li>不需要判断所有文本，此时断言方法返回true</li>
	 * 			</ol>
	 * 		</li>
	 * 		<li>需要判断的关键词为["你好", "世界"]时：
	 * 			<ol>
	 * 				<li>需要判断所有文本，此时断言方法返回false</li>
	 * 				<li>不需要判断所有文本，此时断言方法返回false</li>
	 * 			</ol>
	 * 		</li>
	 * 		<li>需要判断的关键词为["Hello", "Java"]时：
	 * 			<ol>
	 * 				<li>需要判断所有文本，此时断言方法返回true</li>
	 * 				<li>不需要判断所有文本，此时断言方法返回true</li>
	 * 			</ol>
	 * 		</li>
	 * 	</ul>
	 * </p>
	 * <p>
	 * 	<b>注意：</b>若不传入关键词，或关键词为null时，则返回true
	 * </p>
	 * 
	 * @param element       {@link Element}对象
	 * @param isJudgeAllKey 是否需要完全判断所有关键词
	 * @param keys          关键词组
	 * @return 断言结果
	 * @throws TimeoutException 元素无法操作时抛出的异常 
	 * @throws NoSuchElementException 元素不存在或下标不正确时抛出的异常 
	 */
	public static boolean assertTextNotContainKey(String text, boolean isJudgeAllKey, String... keys) {
		return judgetText(text, isJudgeAllKey, true, keys);
	}
	
	/**
	 * 用于对文本信息与关键词进行对比
	 * @param text 需要判断的文本
	 * @param isJudgeAllKey 是否需要完全判断关键词
	 * @param isNot 是否为不包含关键词
	 * @param keys 关键词组
	 * @return 断言结果
	 */
	private static boolean judgetText(String text, boolean isJudgeAllKey, boolean isNot, String... keys) {
		boolean result = false;
        // 循环，判断keys中所有的内容
		for (String key : keys) {
			// 判断当前关键词是否包含于text中
            if (text.contains(key)) {
            	// 若包含文本且不需要关键词完全包含，且判断条件为需要包含文本，则记录当前结果为True，并结束循环
                if (!isJudgeAllKey && !isNot) {
                	result = true;
                	break;
                } else if (isJudgeAllKey && isNot) {
                	// 若包含文本且需要关键词完全包含，且判断条件为不需要包含文本，则记录当前结果为False，并结束循环
                	result = false;
                	break;
                } else if (!isJudgeAllKey && isNot) {
                	result = false;
                	continue;
                } else {
                	result = true;
                	continue;
                }
            } else {
            	// 若不包含且需要关键词完全包含，且判断条件为需要包含文本，则记录当前结果为False，并结束循环
                if (isJudgeAllKey && !isNot) {
                	result = false;
                	break;
                } else if (!isJudgeAllKey && isNot) {
                	// 若包含文本且需要关键词完全包含，且判断条件为不需要包含文本，则记录当前结果为False，并结束循环
                	result = true;
                	break;
                } else if (isJudgeAllKey && isNot) {
                	result = true;
                	continue;
                } else {
                	result = false;
                	continue;
                }
            }
		}
		
	    return result;
	}
	
	/**
	 * 断言元素中的数字内容与指定的数字按照{@link CompareNumberType}指定的比较类型进行比较。若断言的
	 * 元素文本内容为非数字字符时，则返回false
	 * 
	 * @param text 文本内容
	 * @param compareNumberType 比较方式{@link CompareNumberType}枚举类
	 * @param compareNumber 预期数字
	 * @return 断言结果
	 */
	public static boolean assertNumber(String text, CompareNumberType compareNumberType, double compareNumber) {
		try {
			double elementNumber = Double.valueOf(text);
			//根据枚举内容，返回对比结果
			switch (compareNumberType) {
			case EQUAL:
				return elementNumber == compareNumber;
			case GREATER:
				return elementNumber > compareNumber;
			case LESS:
				return elementNumber < compareNumber;
			case GREATER_OR_EQUAL:
				return elementNumber >= compareNumber;
			case LESS_OR_EQUAL:
				return elementNumber <= compareNumber;
			default:
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
