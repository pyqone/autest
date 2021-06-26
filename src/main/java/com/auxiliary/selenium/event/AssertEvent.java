package com.auxiliary.selenium.event;

import java.util.function.Function;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;

import com.auxiliary.selenium.brower.AbstractBrower;
import com.auxiliary.selenium.element.Element;
import com.auxiliary.tool.asserts.AssertStringUtil;

/**
 * <p>
 * <b>文件名：</b>AssertEvent.java
 * </p>
 * <p>
 * <b>用途：</b> 定义了对控件进行断言的方法，可通过该类，对页面元素进行断言操作。类中的所有断言均以
 * boolean类型进行返回，可通过类中提供的方法指定当断言失败时，是否抛出一个异常，默认 不抛出异常。
 * </p>
 * <p>
 * <b>编码时间：</b>2020年10月20日上午7:48:05
 * </p>
 * <p>
 * <b>修改时间：</b>2020年10月20日上午7:48:05
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 *
 */
public class AssertEvent extends AbstractEvent {
	/**
	 * 控制当断言失败时，是否抛出一个异常
	 */
	boolean isThrowException = false;
	/**
	 * 定义文本事件，用于对元素的文本进行获取
	 */
	TextEvent textEvent;

	/**
	 * 构造对象
	 * 
	 * @param brower 浏览器{@link AbstractBrower}类对象
	 */
	public AssertEvent(AbstractBrower brower) {
		super(brower);
		// 初始化文本事件
		textEvent = new TextEvent(brower);
	}

	/**
	 * 设置当前断言失败时，是否抛出一个异常，默认为false，即不抛出异常
	 * 
	 * @param isThrowException 是否抛出异常
	 */
	public void setThrowException(boolean isThrowException) {
		this.isThrowException = isThrowException;
	}

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
	public boolean assertTextContainKey(Element element, boolean isJudgeAllKey, String... keys) {
		boolean result = assertTextContainKey(element, (e) -> textEvent.getText(e), 
				isJudgeAllKey, keys);
		
		logText = "断言“" + element.getElementData().getName() + "”元素的文本内容包含"
				+ (isJudgeAllKey ? "所有" : "部分") + "关键词" + arrayToString(keys);
		resultText = String.valueOf(result);
		
		return result;
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
	public boolean assertTextNotContainKey(Element element, boolean isJudgeAllKey, String... keys) {
		boolean result = assertTextNotContainKey(element, (e) -> textEvent.getText(e), 
				isJudgeAllKey, keys);
		
		logText = "断言“" + element.getElementData().getName() + "”元素的文本内容不包含"
				+ (isJudgeAllKey ? "所有" : "部分") + "关键词" + arrayToString(keys);
		resultText = String.valueOf(result);
		
		return result;
	}

	/**
	 * 断言元素的指定属性是否包含指定的关键词。可设置元素的文本内容是否需要判断传入的所有关键词，
	 * 具体参数介绍可参考{@link #assertTextContainKey(Element, boolean, String...)}方法
	 * 
	 * @param element       {@link Element}对象
	 * @param attributeName 属性名称
	 * @param isJudgeAllKey 是否需要完全判断所有关键词
	 * @param keys          关键词组
	 * @return 断言结果
	 * @throws TimeoutException 元素无法操作时抛出的异常 
	 * @throws NoSuchElementException 元素不存在或下标不正确时抛出的异常 
	 */
	public boolean assertAttributeContainKey(Element element, String attributeName, boolean isJudgeAllKey,
			String... keys) {
		//判断是否传入关键词
//		boolean result = true;
//		if (keys != null && keys.length != 0) {
//			result = judgetText(textEvent.getAttributeValue(element, attributeName), isJudgeAllKey, false, keys);	
//		}
		boolean result = assertTextContainKey(element, (e) -> textEvent.getAttributeValue(e, attributeName), 
				isJudgeAllKey, keys);
		
		logText = "断言“" + element.getElementData().getName() + "”元素的“" + attributeName
				+ "”属性值包含"
				+ (isJudgeAllKey ? "所有" : "部分") + "关键词" + arrayToString(keys);
		resultText = String.valueOf(result);
		
		return result;
	}
	
	/**
	 * 断言元素的文本内容中不包含指定的关键词。可设置元素的文本内容是否需要判断传入的所有关键词，
	 * 具体参数介绍可参考{@link #assertTextNotContainKey(Element, boolean, String...)}方法
	 * 
	 * @param element       {@link Element}对象
	 * @param attributeName 属性名称
	 * @param isJudgeAllKey 是否需要完全判断所有关键词
	 * @param keys          关键词组
	 * @return 断言结果
	 * @throws TimeoutException 元素无法操作时抛出的异常 
	 * @throws NoSuchElementException 元素不存在或下标不正确时抛出的异常 
	 */
	public boolean assertAttributeNotContainKey(Element element, String attributeName, boolean isJudgeAllKey,
			String... keys) {
		boolean result = assertTextNotContainKey(element, (e) -> textEvent.getAttributeValue(e, attributeName), 
				isJudgeAllKey, keys);
		
		logText = "断言“" + element.getElementData().getName() + "”元素的“" + attributeName
				+ "”属性值不包含"
				+ (isJudgeAllKey ? "所有" : "部分") + "关键词" + arrayToString(keys);
		resultText = String.valueOf(result);
		
		return result;
	}
	
	/**
	 * 对元素内容进行处理，并断言处理后的文本内容中包含指定的关键词。
	 * 可设置元素的文本内容是否需要判断传入的所有关键词，具体参数介绍
	 * 可参考{@link #assertTextContainKey(Element, boolean, String...)}方法
	 * 
	 * @param element {@link Element}对象
	 * @param dispose 对元素内容的处理方法，传入的参数为{@link Element}对象，返回的参数为字符串
	 * @param isJudgeAllKey 是否需要完全判断所有关键词
	 * @param keys 关键词组
	 * @return 断言结果
	 * @throws TimeoutException 元素无法操作时抛出的异常 
	 * @throws NoSuchElementException 元素不存在或下标不正确时抛出的异常 
	 */
	public boolean assertTextContainKey(Element element, Function<Element, String> dispose, boolean isJudgeAllKey, String... keys) {
		//判断是否传入关键词
		boolean result = true;
		String text = "";
		if (keys != null && keys.length != 0) {
			text = dispose.apply(element);
//			result = judgetText(text, isJudgeAllKey, false, keys);	
			result = AssertStringUtil.assertTextContainKey(text, isJudgeAllKey, keys);	
		}
		
		logText = "断言“" + element.getElementData().getName() + "”元素处理后的文本内容（" + text +"）包含"
				+ (isJudgeAllKey ? "所有" : "部分") + "关键词" + arrayToString(keys);
		resultText = String.valueOf(result);
		
		return result;
	}
	
	/**
	 * 对元素内容进行处理，并断言处理后的文本内容中不包含指定的关键词。
	 * 可设置元素的文本内容是否需要判断传入的所有关键词，
	 * 具体参数介绍可参考{@link #assertTextContainKey(Element, boolean, String...)}方法
	 * 
	 * @param element {@link Element}对象
	 * @param dispose 对元素内容的处理方法，传入的参数为{@link Element}对象，返回的参数为字符串
	 * @param isJudgeAllKey 是否需要完全判断所有关键词
	 * @param keys 关键词组
	 * @return 断言结果
	 * @throws TimeoutException 元素无法操作时抛出的异常 
	 * @throws NoSuchElementException 元素不存在或下标不正确时抛出的异常 
	 */
	public boolean assertTextNotContainKey(Element element, Function<Element, String> dispose, boolean isJudgeAllKey, String... keys) {
		//判断是否传入关键词
		boolean result = true;
		String text = "";
		if (keys != null && keys.length != 0) {
			text = dispose.apply(element);
//			result = judgetText(text, isJudgeAllKey, true, keys);	
			result = AssertStringUtil.assertTextNotContainKey(text, isJudgeAllKey, keys);
		}
		
		logText = "断言“" + element.getElementData().getName() + "”元素处理后的文本内容（" + text +"）包含"
				+ (isJudgeAllKey ? "所有" : "部分") + "关键词" + arrayToString(keys);
		resultText = String.valueOf(result);
		
		return result;
	}
	
	/**
	 * 用于断言元素的内容与预期的文本一致
	 * @param element {@link Element}对象
	 * @param text 需要判断的文本内容
	 * @return 断言结果
	 * @throws TimeoutException 元素无法操作时抛出的异常 
	 * @throws NoSuchElementException 元素不存在或下标不正确时抛出的异常 
	 */
	public boolean assertEqualsText(Element element, String text) {
		//判断是否传入关键词
		text = text == null ? "" : text;
		
//		boolean result = judgetText(textEvent.getText(element), true, false, text);	
		boolean result = assertTextContainKey(element, true, text);
		
		logText = "断言“" + element.getElementData().getName() + "”元素的文本内容为“" + text + "”";
		resultText = String.valueOf(result);
		
		return result;
	}
	
	/**
	 * 用于断言元素的内容与传入的文本不一致
	 * @param element {@link Element}对象
	 * @param text 需要判断的文本内容
	 * @return 断言结果
	 * @throws TimeoutException 元素无法操作时抛出的异常 
	 * @throws NoSuchElementException 元素不存在或下标不正确时抛出的异常 
	 */
	public boolean assertNotEqualsText(Element element, String text) {
		//判断是否传入关键词
		text = text == null ? "" : text;
		
		boolean result = assertTextNotContainKey(element, true, text);
		
		logText = "断言“" + element.getElementData().getName() + "”元素的文本内容不为“" + text + "”";
		resultText = String.valueOf(result);
		
		return result;
	}
	
	/**
	 * 断言两个元素内文本一致
	 * @param referencedElement 参考元素
	 * @param comparativeElement 比较元素
	 * @return 断言结果
	 * 
	 * @throws TimeoutException 元素无法操作时抛出的异常 
	 * @throws NoSuchElementException 元素不存在或下标不正确时抛出的异常 
	 */
	public boolean assertEqualsElementText(Element referencedElement, Element comparativeElement) {
		boolean result = assertTextContainKey(referencedElement, true, textEvent.getText(comparativeElement));
	
		logText = "断言“" + referencedElement.getElementData().getName() + "”元素的文本内容与“" 
					+ comparativeElement + "”元素的文本内容一致";
		resultText = String.valueOf(result);
		
		return result;
	}
	
	/**
	 * 断言两个元素内文本不一致
	 * @param referencedElement 参考元素
	 * @param comparativeElement 比较元素
	 * @return 断言结果
	 * 
	 * @throws TimeoutException 元素无法操作时抛出的异常 
	 * @throws NoSuchElementException 元素不存在或下标不正确时抛出的异常 
	 */
	public boolean assertNotEqualsElementText(Element referencedElement, Element comparativeElement) {
		boolean result = assertTextNotContainKey(referencedElement, true, textEvent.getText(comparativeElement));
	
		logText = "断言“" + referencedElement.getElementData().getName() + "”元素的文本内容与“" 
					+ comparativeElement + "”元素的文本内容不一致";
		resultText = String.valueOf(result);
		
		return result;
	}
	
	/**
	 * 断言元素存在
	 * @param element {@link Element}对象
	 * @return 断言结果
	 */
	public boolean assertExistElement(Element element) {
		boolean result = isExistElement(element);
		
		logText = "断言“" + element.getElementData().getName() + "”元素存在";
		resultText = String.valueOf(result);
		
		return result;
	}
	
	/**
	 * 断言元素不存在
	 * @param element {@link Element}对象
	 * @return 断言结果
	 */
	public boolean assertNotExistElement(Element element) {
		boolean result = !isExistElement(element);
		
		logText = "断言“" + element.getElementData().getName() + "”元素不存在";
		resultText = String.valueOf(result);
		
		return result;
	}
	
	/**
	 * 断言两元素中的数字内容，按照{@link CompareNumberType}指定的比较类型进行比较。若断言的
	 * 元素文本内容存在非数字字符时，则直接断言失败
	 * @param referencedElement 参考元素
	 * @param compareNumberType 比较方式{@link CompareNumberType}枚举类
	 * @param comparativeElement 比较元素
	 * @return 断言结果
	 */
	public boolean assertElementsNumber(Element referencedElement, CompareNumberType compareNumberType, Element comparativeElement) {
		logText = "断言“" + referencedElement.getElementData().getName() + "”元素数字内容" 
					+ compareNumberType.getLogText() 
					+ "“" + comparativeElement.getElementData().getName() + "”元素数字内容";
		boolean result = assertNumber(referencedElement, compareNumberType, Double.valueOf(textEvent.getText(comparativeElement)));
		
		resultText = String.valueOf(result);
		return result;
	}
	
	/**
	 * 断言元素中的数字内容与指定的数字按照{@link CompareNumberType}指定的比较类型进行比较。若断言的
	 * 元素文本内容为非数字字符时，则直接断言失败
	 * 
	 * @param element {@link Element}对象
	 * @param compareNumberType 比较方式{@link CompareNumberType}枚举类
	 * @param compareNumber 预期数字
	 * @return 断言结果
	 */
	public boolean assertNumber(Element element, CompareNumberType compareNumberType, double compareNumber) {
		logText = "断言“" + element.getElementData().getName() + "”元素数字内容" + compareNumberType.getLogText() + compareNumber;
		boolean result = AssertStringUtil.assertNumber(textEvent.getText(element), compareNumberType, compareNumber);
 		resultText = String.valueOf(result);
		return result;
	}
}
