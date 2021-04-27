package com.auxiliary.selenium.event.extend;

import java.util.ArrayList;

import com.auxiliary.selenium.brower.AbstractBrower;
import com.auxiliary.selenium.element.FindCommonElement;
import com.auxiliary.selenium.event.AbstractEvent;
import com.auxiliary.selenium.event.AssertEvent;
import com.auxiliary.selenium.event.ClickEvent;
import com.auxiliary.selenium.event.CompareNumberType;
import com.auxiliary.selenium.event.TextEvent;
import com.auxiliary.selenium.event.WaitEvent;
import com.auxiliary.selenium.location.AbstractLocation;

/**
 * <p>
 * <b>文件名：</b>EventCommonCollection.java
 * </p>
 * <p>
 * <b>用途：</b> 整合所有的基础事件，通过默认的元素等待时间、事件执行时间来对指定的单一元素进行操作
 * </p>
 * <p>
 * <b>编码时间：</b>2021年4月25日上午10:59:29
 * </p>
 * <p>
 * <b>修改时间：</b>2021年4月25日上午10:59:29
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public class CommonEventCollection extends AbstractEvent {
	/**
	 * 指向单一元素查找类
	 */
	protected FindCommonElement findElement;

	/**
	 * 指向点击事件
	 */
	protected ClickEvent clickEvent;
	/**
	 * 指向文本事件
	 */
	protected TextEvent textEvent;
	/**
	 * 指向等待事件
	 */
	protected WaitEvent waitEvent;
	/**
	 * 指向断言事件
	 */
	protected AssertEvent assertEvent;

	/**
	 * 日志信息集合，收集每个操作步骤返回的日志
	 */
	protected ArrayList<String> logTextList = new ArrayList<>();
	/**
	 * 控制日志前的序号，若序号为-1，则不再为日志添加序号
	 */
	protected boolean isReportLogIndex = false;

	/**
	 * 构造对象并存储浏览器对象
	 * 
	 * @param brower 浏览器{@link AbstractBrower}对象
	 */
	public CommonEventCollection(AbstractBrower brower) {
		super(brower);

		// 初始化各个查找类与事件类
		findElement = new FindCommonElement(brower);

		clickEvent = new ClickEvent(brower);
		textEvent = new TextEvent(brower);
		waitEvent = new WaitEvent(brower);
		assertEvent = new AssertEvent(brower);
	}

	/**
	 * 用于设置元素定位方式文件读取类对象
	 * <p>
	 * isBreakRootFrame参数表示是否将所有的元素切回到顶层，对于web元素而言，则是将iframe切回到顶层；对于app元素
	 * 而言，则是将上下文切换至原生层
	 * </p>
	 * 
	 * @param read             元素定位方式文件读取类对象
	 * @param isBreakRootFrame 是否需要将窗体切回到顶层
	 */
	public void setReadMode(AbstractLocation read, boolean isBreakRootFrame) {
		findElement.setReadMode(read, isBreakRootFrame);
	}

	/**
	 * 设置是否需要为当前收集的日志添加序号
	 * 
	 * @param isReportLogIndex 是否添加日志序号
	 */
	public void setIsReportLogIndex(boolean isReportLogIndex) {
		this.isReportLogIndex = isReportLogIndex;
	}

	/**
	 * 鼠标左键单击元素，参见{@link ClickEvent#click(com.auxiliary.selenium.element.Element)}
	 * 
	 * @param elementName 元素名称
	 * @param linkKeys    外链词语
	 */
	public void click(String elementName, String... linkKeys) {
		clickEvent.click(findElement.getElement(elementName, linkKeys));
		logTextList.add(logText);
	}

	/**
	 * 鼠标左键双击元素，参见{@link ClickEvent#doubleClick(com.auxiliary.selenium.element.Element)}
	 * 
	 * @param elementName 元素名称
	 * @param linkKeys    外链词语
	 */
	public void doubleClick(String elementName, String... linkKeys) {
		clickEvent.doubleClick(findElement.getElement(elementName, linkKeys));
		logTextList.add(logText);
	}

	/**
	 * 鼠标右键单击元素，参见{@link ClickEvent#rightClick(com.auxiliary.selenium.element.Element)}
	 * 
	 * @param elementName 元素名称
	 * @param linkKeys    外链词语
	 */
	public void rightClick(String elementName, String... linkKeys) {
		clickEvent.rightClick(findElement.getElement(elementName, linkKeys));
		logTextList.add(logText);
	}

	/**
	 * 鼠标左键连续点击元素，参见{@link ClickEvent#continuousClick(com.auxiliary.selenium.element.Element, int, long)}
	 * 
	 * @param clickCount    点击次数
	 * @param sleepInMillis 操作时间间隔，单位为毫秒
	 * @param elementName   元素名称
	 * @param linkKeys      外链词语
	 */
	public void continuousClick(int clickCount, long sleepInMillis, String elementName, String... linkKeys) {
		clickEvent.continuousClick(findElement.getElement(elementName, linkKeys), clickCount, sleepInMillis);
		logTextList.add(logText);
	}

	/**
	 * 清除元素文本内容，参见{@link TextEvent#clear(com.auxiliary.selenium.element.Element)}
	 * 
	 * @param elementName 元素名称
	 * @param linkKeys    外链词语
	 * @return 被清理的内容
	 */
	public String clear(String elementName, String... linkKeys) {
		textEvent.clear(findElement.getElement(elementName, linkKeys));
		logTextList.add(logText);
		return resultText;
	}

	/**
	 * 获取元素属性值，参见{@link TextEvent#getAttributeValue(com.auxiliary.selenium.element.Element, String)}
	 * 
	 * @param attributeName 元素属性名称
	 * @param elementName   元素名称
	 * @param linkKeys      外链词语
	 * @return 属性值
	 */
	public String getAttributeValue(String attributeName, String elementName, String... linkKeys) {
		textEvent.getAttributeValue(findElement.getElement(elementName, linkKeys), attributeName);
		logTextList.add(logText);
		return resultText;
	}

	/**
	 * 获取元素文本内容，参见{@link TextEvent#getText(com.auxiliary.selenium.element.Element)}
	 * 
	 * @param elementName 元素名称
	 * @param linkKeys    外链词语
	 * @return 元素文本内容
	 */
	public String getText(String elementName, String... linkKeys) {
		textEvent.getText(findElement.getElement(elementName, linkKeys));
		logTextList.add(logText);
		return resultText;
	}

	/**
	 * 在元素中输入文本，参见{@link TextEvent#input(com.auxiliary.selenium.element.Element, String)}
	 * 
	 * @param text        需要输入到控件中的
	 * @param elementName 元素名称
	 * @param linkKeys    外链词语
	 * @return 在控件中输入的内容
	 */
	public String input(String text, String elementName, String... linkKeys) {
		textEvent.input(findElement.getElement(elementName, linkKeys), text);
		logTextList.add(logText);
		return resultText;
	}

	/**
	 * 识别图片中的内容，参见{@link TextEvent#getImageText(com.auxiliary.selenium.element.Element)}
	 * 
	 * @param elementName 元素名称
	 * @param linkKeys    外链词语
	 * @return 识别图片的结果
	 */
	public String getImageText(String elementName, String... linkKeys) {
		textEvent.getImageText(findElement.getElement(elementName, linkKeys));
		logTextList.add(logText);
		return resultText;
	}

	/**
	 * 等待元素消失，参见{@link WaitEvent#disappear(com.auxiliary.selenium.element.Element)}
	 * 
	 * @param elementName 元素名称
	 * @param linkKeys    外链词语
	 * @return 元素是否已消失
	 */
	public boolean disappear(String elementName, String... linkKeys) {
		boolean result = waitEvent.disappear(findElement.getElement(elementName, linkKeys));
		logTextList.add(logText);
		return result;
	}

	/**
	 * 等待元素在页面中出现，参见{@link WaitEvent#appear(com.auxiliary.selenium.element.Element)}
	 * 
	 * @param elementName 元素名称
	 * @param linkKeys    外链词语
	 * @return 元素是否出现
	 */
	public boolean appear(String elementName, String... linkKeys) {
		boolean result = waitEvent.appear(findElement.getElement(elementName, linkKeys));
		logTextList.add(logText);
		return result;
	}

	/**
	 * 等待指定元素中显示相应的文本，参见{@link WaitEvent#showText(com.auxiliary.selenium.element.Element, String...)}
	 * 
	 * @param keys        需要判断的文本
	 * @param elementName 元素名称
	 * @param linkKeys    外链词语
	 * @return
	 */
	public boolean showText(String[] keys, String elementName, String... linkKeys) {
		boolean result = waitEvent.showText(findElement.getElement(elementName, linkKeys), keys);
		logTextList.add(logText);
		return result;
	}

	/**
	 * 断言元素的文本内容中包含指定的关键词，参见{@link AssertEvent#assertTextContainKey(com.auxiliary.selenium.element.Element, boolean, String...)}
	 * 
	 * @param isJudgeAllKey 是否需要完全判断所有关键词
	 * @param keys          关键词组
	 * @param elementName   元素名称
	 * @param linkKeys      外链词语
	 * @return 断言结果
	 */
	public boolean assertTextContainKey(boolean isJudgeAllKey, String[] keys, String elementName, String... linkKeys) {
		boolean result = assertEvent.assertTextContainKey(findElement.getElement(elementName, linkKeys), isJudgeAllKey,
				keys);
		logTextList.add(logText);
		return result;
	}

	/**
	 * 断言元素的文本内容中不包含指定的关键词，参见{@link AssertEvent#assertTextNotContainKey(com.auxiliary.selenium.element.Element, boolean, String...)}
	 * 
	 * @param isJudgeAllKey 是否需要完全判断所有关键词
	 * @param keys          关键词组
	 * @param elementName   元素名称
	 * @param linkKeys      外链词语
	 * @return 断言结果
	 */
	public boolean assertTextNotContainKey(boolean isJudgeAllKey, String[] keys, String elementName,
			String... linkKeys) {
		boolean result = assertEvent.assertTextNotContainKey(findElement.getElement(elementName, linkKeys),
				isJudgeAllKey, keys);
		logTextList.add(logText);
		return result;
	}

	/**
	 * 断言元素的指定属性是否包含指定的关键词，参见{@link AssertEvent#assertAttributeContainKey(com.auxiliary.selenium.element.Element, String, boolean, String...)}
	 * 
	 * @param attributeName 属性名称
	 * @param isJudgeAllKey 是否需要完全判断所有关键词
	 * @param keys          关键词组
	 * @param elementName   元素名称
	 * @param linkKeys      外链词语
	 * @return 断言结果
	 */
	public boolean assertAttributeContainKey(String attributeName, boolean isJudgeAllKey, String[] keys,
			String elementName, String... linkKeys) {
		boolean result = assertEvent.assertAttributeContainKey(findElement.getElement(elementName, linkKeys),
				attributeName, isJudgeAllKey, keys);
		logTextList.add(logText);
		return result;
	}

	/**
	 * 断言元素的文本内容中不包含指定的关键词，参见{@link AssertEvent#}
	 * 
	 * @param elementName 元素名称
	 * @param linkKeys    外链词语
	 * @return 断言结果
	 */
	public boolean assertAttributeNotContainKey(String attributeName, boolean isJudgeAllKey, String[] keys,
			String elementName, String... linkKeys) {
		boolean result = assertEvent.assertAttributeNotContainKey(findElement.getElement(elementName, linkKeys),
				attributeName, isJudgeAllKey, keys);
		logTextList.add(logText);
		return result;
	}

	/**
	 * 断言元素的内容与预期的文本一致，参见{@link AssertEvent#assertEqualsText(com.auxiliary.selenium.element.Element, String)}
	 * 
	 * @param text        需要判断的文本内容
	 * @param elementName 元素名称
	 * @param linkKeys    外链词语
	 * @return 断言结果
	 */
	public boolean assertEqualsText(String text, String elementName, String... linkKeys) {
		boolean result = assertEvent.assertEqualsText(findElement.getElement(elementName, linkKeys), text);
		logTextList.add(logText);
		return result;
	}

	/**
	 * 断言元素的内容与传入的文本不一致，参见{@link AssertEvent#assertNotEqualsText(com.auxiliary.selenium.element.Element, String)}
	 * 
	 * @param text        需要判断的文本内容
	 * @param elementName 元素名称
	 * @param linkKeys    外链词语
	 * @return 断言结果
	 */
	public boolean assertNotEqualsText(String text, String elementName, String... linkKeys) {
		boolean result = assertEvent.assertNotEqualsText(findElement.getElement(elementName, linkKeys), text);
		logTextList.add(logText);
		return result;
	}

	/**
	 * 断言元素存在，参见{@link AssertEvent#assertExistElement(com.auxiliary.selenium.element.Element)}
	 * 
	 * @param elementName 元素名称
	 * @param linkKeys    外链词语
	 * @return 断言结果
	 */
	public boolean assertExistElement(String elementName, String... linkKeys) {
		boolean result = assertEvent.assertExistElement(findElement.getElement(elementName, linkKeys));
		logTextList.add(logText);
		return result;
	}

	/**
	 * 断言元素不存在，参见{@link AssertEvent#assertNotExistElement(com.auxiliary.selenium.element.Element)}
	 * 
	 * @param elementName 元素名称
	 * @param linkKeys    外链词语
	 * @return 断言结果
	 */
	public boolean assertNotExistElement(String elementName, String... linkKeys) {
		boolean result = assertEvent.assertNotExistElement(findElement.getElement(elementName, linkKeys));
		logTextList.add(logText);
		return result;
	}

	/**
	 * 断言数字大小，参见{@link AssertEvent#assertNumber(com.auxiliary.selenium.element.Element, CompareNumberType, double)}
	 * 
	 * @param compareNumberType 比较方式{@link CompareNumberType}枚举类
	 * @param compareNumber     预期数字
	 * @param elementName       元素名称
	 * @param linkKeys          外链词语
	 * @return 断言结果
	 */
	public boolean assertNumber(CompareNumberType compareNumberType, double compareNumber, String elementName,
			String... linkKeys) {
		boolean result = assertEvent.assertNumber(findElement.getElement(elementName, linkKeys), compareNumberType,
				compareNumber);
		logTextList.add(logText);
		return result;
	}

	/**
	 * 用于返回收集的日志信息，并根据参数决定是否清空当前收集的日志
	 * 
	 * @param isClear 是否清空日志集
	 * @return 日志集
	 */
	public ArrayList<String> getLogList(boolean isClear) {
		ArrayList<String> logList = new ArrayList<>();

		// 判断当前是否需要加上日志序号
		if (isReportLogIndex) {
			for (int index = 0; index < logTextList.size(); index++) {
				logList.add(String.format("%d.%s", (index + 1), logTextList.get(index)));
			}
		} else {
			logList.addAll(logTextList);
		}

		// 判断是否清空收集的日志
		if (isClear) {
			logTextList.clear();
		}
		return logList;
	}
}
