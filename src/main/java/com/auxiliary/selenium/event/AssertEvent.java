package com.auxiliary.selenium.event;

import java.util.function.Function;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;

import com.auxiliary.selenium.SeleniumToolsException;
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
 * <b>修改时间：</b>2021年10月18日 上午10:25:01
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
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
     * 断言元素的文本内容中包含指定的关键词。可设置元素的文本内容是否需要判断传入的所有关键词，即
     * <ul>
     * <li>当需要完全判断时，则当且仅当文本包含所有关键词时，断言方法返回true</li>
     * <li>当不需要完全判断时，则当前仅当文本不包含所有关键词时，断言方法返回false</li>
     * </ul>
     * </p>
     * <p>
     * 例如,假设元素中存在文本“你好，世界！”，则
     * <ul>
     * <li>需要判断的关键词为["你好", "Java"]时：
     * <ol>
     * <li>需要判断所有文本，此时断言方法返回false</li>
     * <li>不需要判断所有文本，此时断言方法返回true</li>
     * </ol>
     * </li>
     * <li>需要判断的关键词为["你好", "世界"]时：
     * <ol>
     * <li>需要判断所有文本，此时断言方法返回true</li>
     * <li>不需要判断所有文本，此时断言方法返回true</li>
     * </ol>
     * </li>
     * <li>需要判断的关键词为["Hello", "Java"]时：
     * <ol>
     * <li>需要判断所有文本，此时断言方法返回false</li>
     * <li>不需要判断所有文本，此时断言方法返回false</li>
     * </ol>
     * </li>
     * </ul>
     * </p>
     * <p>
     * <b>注意：</b>若不传入关键词，或关键词为null时，则返回true
     * </p>
     *
     * @param element       {@link Element}对象
     * @param isJudgeAllKey 是否需要完全判断所有关键词
     * @param keys          关键词组
     * @return 断言结果
     * @throws TimeoutException       元素无法操作时抛出的异常
     * @throws NoSuchElementException 元素不存在或下标不正确时抛出的异常
     */
    public boolean assertTextContainKey(Element element, boolean isJudgeAllKey, String... keys) {
        boolean result = assertTextContainKey(element, (e) -> textEvent.getText(e), isJudgeAllKey, keys);

        // 由于调用获取文本方法，故需要删除前两条日志
        brower.getLogRecord().removeLog(2);
        String logText = "断言“%s”元素的文本内容包含%s关键词：%s";
        brower.getLogRecord().recordLog(String.format(logText, element.getElementData().getName(),
                (isJudgeAllKey ? "所有" : "部分"), arrayToString(keys)));

        return disposeException(result, logText);
    }

    /**
     * <p>
     * 断言元素的文本内容中不包含指定的关键词。可设置元素的文本内容是否需要判断传入的所有关键词，即
     * <ul>
     * <li>当需要完全判断时，则当且仅当文本不包含所有关键词时，断言方法返回true</li>
     * <li>当不需要完全判断时，则当前仅当文本包含所有关键词时，断言方法返回false</li>
     * </ul>
     * </p>
     * <p>
     * 例如,假设元素中存在文本“你好，世界！”，则
     * <ul>
     * <li>需要判断的关键词为["你好", "Java"]时：
     * <ol>
     * <li>需要判断所有文本，此时断言方法返回false</li>
     * <li>不需要判断所有文本，此时断言方法返回true</li>
     * </ol>
     * </li>
     * <li>需要判断的关键词为["你好", "世界"]时：
     * <ol>
     * <li>需要判断所有文本，此时断言方法返回false</li>
     * <li>不需要判断所有文本，此时断言方法返回false</li>
     * </ol>
     * </li>
     * <li>需要判断的关键词为["Hello", "Java"]时：
     * <ol>
     * <li>需要判断所有文本，此时断言方法返回true</li>
     * <li>不需要判断所有文本，此时断言方法返回true</li>
     * </ol>
     * </li>
     * </ul>
     * </p>
     * <p>
     * <b>注意：</b>若不传入关键词，或关键词为null时，则返回true
     * </p>
     *
     * @param element       {@link Element}对象
     * @param isJudgeAllKey 是否需要完全判断所有关键词
     * @param keys          关键词组
     * @return 断言结果
     * @throws TimeoutException       元素无法操作时抛出的异常
     * @throws NoSuchElementException 元素不存在或下标不正确时抛出的异常
     */
    public boolean assertTextNotContainKey(Element element, boolean isJudgeAllKey, String... keys) {
        boolean result = assertTextNotContainKey(element, (e) -> textEvent.getText(e), isJudgeAllKey, keys);

        // 由于调用获取文本方法，故需要删除前两条日志
        brower.getLogRecord().removeLog(2);
        String logText = "断言“%s”元素的文本内容不包含%s关键词：%s";
        brower.getLogRecord().recordLog(String.format(logText, element.getElementData().getName(),
                (isJudgeAllKey ? "所有" : "部分"), arrayToString(keys)));

        return disposeException(result, logText);
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
     * @throws TimeoutException       元素无法操作时抛出的异常
     * @throws NoSuchElementException 元素不存在或下标不正确时抛出的异常
     */
    public boolean assertAttributeContainKey(Element element, String attributeName, boolean isJudgeAllKey,
            String... keys) {
        // 判断是否传入关键词
        boolean result = assertTextContainKey(element, (e) -> textEvent.getAttributeValue(e, attributeName),
                isJudgeAllKey, keys);

        // 由于调用获取文本方法，故需要删除前两条日志
        brower.getLogRecord().removeLog(2);
        String logText = "断言“%s”元素的“%s”属性值包含%s关键词:%s";
        brower.getLogRecord().recordLog(String.format(logText, element.getElementData().getName(), attributeName,
                (isJudgeAllKey ? "所有" : "部分"), arrayToString(keys)));

        return disposeException(result, logText);
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
     * @throws TimeoutException       元素无法操作时抛出的异常
     * @throws NoSuchElementException 元素不存在或下标不正确时抛出的异常
     */
    public boolean assertAttributeNotContainKey(Element element, String attributeName, boolean isJudgeAllKey,
            String... keys) {
        boolean result = assertTextNotContainKey(element, (e) -> textEvent.getAttributeValue(e, attributeName),
                isJudgeAllKey, keys);

        // 由于调用获取文本方法，故需要删除前两条日志
        brower.getLogRecord().removeLog(2);
        String logText = "断言“%s”元素的“%s”属性值不包含%s关键词：%s";
        brower.getLogRecord().recordLog(String.format(logText, element.getElementData().getName(), attributeName,
                (isJudgeAllKey ? "所有" : "部分"), arrayToString(keys)));

        return disposeException(result, logText);
    }

    /**
     * 对元素内容进行处理，并断言处理后的文本内容中包含指定的关键词。 可设置元素的文本内容是否需要判断传入的所有关键词，具体参数介绍
     * 可参考{@link #assertTextContainKey(Element, boolean, String...)}方法
     *
     * @param element       {@link Element}对象
     * @param dispose       对元素内容的处理方法，传入的参数为{@link Element}对象，返回的参数为字符串
     * @param isJudgeAllKey 是否需要完全判断所有关键词
     * @param keys          关键词组
     * @return 断言结果
     * @throws TimeoutException       元素无法操作时抛出的异常
     * @throws NoSuchElementException 元素不存在或下标不正确时抛出的异常
     */
    public boolean assertTextContainKey(Element element, Function<Element, String> dispose, boolean isJudgeAllKey,
            String... keys) {
        // 判断是否传入关键词
        boolean result = true;
        String text = "";
        if (keys != null && keys.length != 0) {
            text = dispose.apply(element);
            result = AssertStringUtil.assertTextContainKey(text, isJudgeAllKey, keys);
        }

        String logText = "断言“%s”元素处理后的文本内容（%s）包含%s关键词:%s";
        brower.getLogRecord().recordLog(String.format(logText, element.getElementData().getName(), text,
                (isJudgeAllKey ? "所有" : "部分"), arrayToString(keys)));

        return disposeException(result, logText);
    }

    /**
     * 对元素内容进行处理，并断言处理后的文本内容中不包含指定的关键词。 可设置元素的文本内容是否需要判断传入的所有关键词，
     * 具体参数介绍可参考{@link #assertTextContainKey(Element, boolean, String...)}方法
     *
     * @param element       {@link Element}对象
     * @param dispose       对元素内容的处理方法，传入的参数为{@link Element}对象，返回的参数为字符串
     * @param isJudgeAllKey 是否需要完全判断所有关键词
     * @param keys          关键词组
     * @return 断言结果
     * @throws TimeoutException       元素无法操作时抛出的异常
     * @throws NoSuchElementException 元素不存在或下标不正确时抛出的异常
     */
    public boolean assertTextNotContainKey(Element element, Function<Element, String> dispose, boolean isJudgeAllKey,
            String... keys) {
        // 判断是否传入关键词
        boolean result = true;
        String text = "";
        if (keys != null && keys.length != 0) {
            text = dispose.apply(element);
            result = AssertStringUtil.assertTextNotContainKey(text, isJudgeAllKey, keys);
        }

        String logText = "断言“%s”元素处理后的文本内容（%s）包含%s关键词：%s";
        brower.getLogRecord().recordLog(String.format(logText, element.getElementData().getName(), text,
                (isJudgeAllKey ? "所有" : "部分"), arrayToString(keys)));

        return disposeException(result, logText);
    }

    /**
     * 用于断言元素的内容与预期的文本一致
     *
     * @param element {@link Element}对象
     * @param text    需要判断的文本内容
     * @return 断言结果
     * @throws TimeoutException       元素无法操作时抛出的异常
     * @throws NoSuchElementException 元素不存在或下标不正确时抛出的异常
     */
    public boolean assertEqualsText(Element element, String text) {
        // 判断是否传入关键词
        text = text == null ? "" : text;

        boolean result = assertTextContainKey(element, true, text);

        // 由于调用其他断言方法，故需要删除前一条日志
        brower.getLogRecord().removeLog(1);

        String logText = "断言“%s”元素的文本内容为“%s”";
        brower.getLogRecord().recordLog(String.format(logText, element.getElementData().getName(), text));

        return disposeException(result, logText);
    }

    /**
     * 用于断言元素的内容与传入的文本不一致
     *
     * @param element {@link Element}对象
     * @param text    需要判断的文本内容
     * @return 断言结果
     * @throws TimeoutException       元素无法操作时抛出的异常
     * @throws NoSuchElementException 元素不存在或下标不正确时抛出的异常
     */
    public boolean assertNotEqualsText(Element element, String text) {
        // 判断是否传入关键词
        text = text == null ? "" : text;

        boolean result = assertTextNotContainKey(element, true, text);

        // 由于调用其他断言方法，故需要删除前一条日志
        brower.getLogRecord().removeLog(1);

        String logText = "断言“%s”元素的文本内容不为“%s”";
        brower.getLogRecord().recordLog(String.format(logText, element.getElementData().getName(), text));

        return disposeException(result, logText);
    }

    /**
     * 断言两个元素内文本一致
     *
     * @param referencedElement  参考元素
     * @param comparativeElement 比较元素
     * @return 断言结果
     *
     * @throws TimeoutException       元素无法操作时抛出的异常
     * @throws NoSuchElementException 元素不存在或下标不正确时抛出的异常
     */
    public boolean assertEqualsElementText(Element referencedElement, Element comparativeElement) {
        boolean result = assertTextContainKey(referencedElement, true, textEvent.getText(comparativeElement));

        // 由于调用其他断言方法，故需要删除前一条日志
        brower.getLogRecord().removeLog(3);

        String logText = "断言“%s”元素的文本内容与“%s”元素的文本内容一致";
        brower.getLogRecord().recordLog(String.format(logText, referencedElement.getElementData().getName(),
                comparativeElement.getElementData().getName()));

        return disposeException(result, logText);
    }

    /**
     * 断言两个元素内文本不一致
     *
     * @param referencedElement  参考元素
     * @param comparativeElement 比较元素
     * @return 断言结果
     *
     * @throws TimeoutException       元素无法操作时抛出的异常
     * @throws NoSuchElementException 元素不存在或下标不正确时抛出的异常
     */
    public boolean assertNotEqualsElementText(Element referencedElement, Element comparativeElement) {
        boolean result = assertTextNotContainKey(referencedElement, true, textEvent.getText(comparativeElement));

        // 由于调用其他断言方法，故需要删除前一条日志
        brower.getLogRecord().removeLog(2);

        String logText = "断言“%s”元素的文本内容与“%s”元素的文本内容不一致";
        brower.getLogRecord().recordLog(String.format(logText, referencedElement.getElementData().getName(),
                comparativeElement.getElementData().getName()));

        return disposeException(result, logText);
    }

    /**
     * 断言元素存在
     *
     * @param element {@link Element}对象
     * @return 断言结果
     */
    public boolean assertExistElement(Element element) {
        boolean result = isExistElement(element);

        String logText = "断言“%s”元素存在";
        brower.getLogRecord().recordLog(String.format(logText, element.getElementData().getName()));

        return disposeException(result, logText);
    }

    /**
     * 断言元素不存在
     *
     * @param element {@link Element}对象
     * @return 断言结果
     */
    public boolean assertNotExistElement(Element element) {
        boolean result = !isExistElement(element);

        String logText = "断言“%s”元素不存在";
        brower.getLogRecord().recordLog(String.format(logText, element.getElementData().getName()));

        return disposeException(result, logText);
    }

    /**
     * 断言两元素中的数字内容，按照{@link CompareNumberType}指定的比较类型进行比较。若断言的
     * 元素文本内容存在非数字字符时，则直接断言失败
     *
     * @param referencedElement  参考元素
     * @param compareNumberType  比较方式{@link CompareNumberType}枚举类
     * @param comparativeElement 比较元素
     * @return 断言结果
     */
    public boolean assertElementsNumber(Element referencedElement, CompareNumberType compareNumberType,
            Element comparativeElement) {
        String logText = "断言“%s”元素数字内容%s“%s”元素数字内容";
        brower.getLogRecord().recordLog(String.format(logText, referencedElement.getElementData().getName(),
                compareNumberType.getLogText(), comparativeElement.getElementData().getName()));

        // 当判定元素为非数字文本时，则直接返回false
        boolean result = false;
        try {
            result = assertNumber(referencedElement, compareNumberType,
                    Double.valueOf(textEvent.getText(comparativeElement)));

            // 删除获取文本和调用assertNumber方法的日志
            brower.getLogRecord().removeLog(2);
        } catch (NumberFormatException e) {
            return false;
        }

        return disposeException(result, logText);
    }

    /**
     * 断言元素中的数字内容与指定的数字按照{@link CompareNumberType}指定的比较类型进行比较。若断言的
     * 元素文本内容为非数字字符时，则直接断言失败
     *
     * @param element           {@link Element}对象
     * @param compareNumberType 比较方式{@link CompareNumberType}枚举类
     * @param compareNumber     预期数字
     * @return 断言结果
     */
    public boolean assertNumber(Element element, CompareNumberType compareNumberType, double compareNumber) {
        String logText = "断言“%s”元素数字内容%s%f";
        brower.getLogRecord().recordLog(String.format(logText, element.getElementData().getName(),
                compareNumberType.getLogText(), compareNumber));

        boolean result = AssertStringUtil.assertNumber(textEvent.getText(element), compareNumberType, compareNumber);
        // 删除获取文本的日志
        brower.getLogRecord().removeLog(1);

        return disposeException(result, logText);
    }

    /**
     * 该方法用于判断断言失败时是否需要抛出异常
     *
     * @param result  断言结果
     * @param logText 断言日志
     * @return 断言结果
     * @since autest 3.2.0
     */
    private boolean disposeException(boolean result, String logText) {
        // 当断言失败且需要抛出异常时，则抛出指定的异常
        if (!result && isThrowException) {
            throw new SeleniumAssertException(logText);
        }

        return result;
    }

    /**
     * <p>
     * <b>文件名：AssertEvent.java</b>
     * </p>
     * <p>
     * <b>用途：</b>定义当Selenium工具断言失败时抛出的异常
     * </p>
     * <p>
     * <b>编码时间：2022年3月25日 上午8:10:28
     * </p>
     * <p>
     * <b>修改时间：2022年3月25日 上午8:10:28
     * </p>
     *
     *
     * @author 彭宇琦
     * @version Ver1.0
     * @since JDK 1.8
     * @since autest 3.2.0
     */
    public class SeleniumAssertException extends SeleniumToolsException {
        private static final long serialVersionUID = 1L;

        public SeleniumAssertException(String message) {
            super("selenium事件断言失败，日志为：" + message);
        }
    }
}
