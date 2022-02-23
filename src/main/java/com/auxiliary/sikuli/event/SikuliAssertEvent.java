package com.auxiliary.sikuli.event;

import java.util.Arrays;
import java.util.Optional;
import java.util.StringJoiner;

import com.auxiliary.selenium.element.Element;
import com.auxiliary.selenium.event.CompareNumberType;
import com.auxiliary.sikuli.SikuliToolsExcepton;
import com.auxiliary.sikuli.element.FindSikuliElement;
import com.auxiliary.sikuli.element.SikuliElement;
import com.auxiliary.sikuli.element.TimeoutException;
import com.auxiliary.tool.asserts.AssertStringUtil;

/**
 * <p>
 * <b>文件名：</b>SikuliAssertEvent.java
 * </p>
 * <p>
 * <p>
 * <b>用途：</b> 封装sikuli工具的断言事件，可设置{@link FindSikuliElement}类对象，用以元素名称来进行操作
 * <p>
 * <b>注意：</b>通过元素名称对元素进行查找的方式不支持外链词语
 * </p>
 * </p>
 * <p>
 * <b>编码时间：</b>2022年2月16日 上午8:40:52
 * </p>
 * <p>
 * <b>修改时间：</b>2022年2月16日 上午8:40:52
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 3.1.0
 */
public class SikuliAssertEvent extends SikuliAbstractEvent {
    /**
     * 整个事件名称都为断言，故提取出相应的事件名称
     */
    private String eventName = "断言";
    /**
     * 控制当断言失败时，是否抛出一个异常
     */
    boolean isThrowException = false;

    /**
     * 存储OCR事件
     */
    SikuliOcrEvent ocr;

    /**
     * 构造对象
     *
     * @since autest 3.1.0
     */
    public SikuliAssertEvent() {
        super();
        // 构造默认的OCR事件
        ocr = new SikuliOcrEvent();
    }

    /**
     * 该方法用于设置当断言失败时，是否抛出{@link SikuliAssertFailException}异常
     *
     * @param isThrowException 断言是否是否抛出异常
     * @since autest 3.1.0
     */
    public void setThrowException(boolean isThrowException) {
        this.isThrowException = isThrowException;
    }

    /**
     * 该方法用于设置使用自定义配置的ocr事件类对象
     *
     * @param ocr ocr事件类对象
     * @since autest 3.1.0
     */
    public void setOcrEvent(SikuliOcrEvent ocr) {
        if (ocr != null) {
            this.ocr = ocr;
        }
    }

    /**
     * 断言元素的文本内容中包含指定的关键词。可设置元素的文本内容是否需要判断传入的所有关键词，其用法可参考{@link AssertStringUtil#assertTextContainKey(String, boolean, String...)}
     *
     * @param element       元素类对象
     * @param isJudgeAllKey 是否需要完全判断所有关键词
     * @param keys          关键词组
     * @return 断言结果
     * @throws OperateTimeoutException 操作超时时，抛出的异常
     */
    public boolean assertTextContainKey(SikuliElement element, boolean isJudgeAllKey, String... keys) {
        Boolean result = (Boolean) actionOperate(eventName, element, match -> {
            String text = ocr.readText(element);
            return AssertStringUtil.assertTextContainKey(text, isJudgeAllKey, keys);
        });

        recordLog(String.format("%s“%s”元素的文本内容包含%s关键词：%s", eventName, element.getName(), (isJudgeAllKey ? "所有" : "部分"),
                arrayToString(keys)), 1);

        return result;
    }

    /**
     * 断言元素的文本内容中包含指定的关键词。可设置元素的文本内容是否需要判断传入的所有关键词，其用法可参考{@link AssertStringUtil#assertTextContainKey(String, boolean, String...)}
     *
     * @param elementName   元素名称
     * @param index         多元素时的下标，允许传入负数，参考{@link FindSikuliElement#findElement(String, int, String...)}方法
     * @param isJudgeAllKey 是否需要完全判断所有关键词
     * @param keys          关键词组
     * @return 断言结果
     * @throws ElementOperateException 未指定元素查找类时，抛出的异常
     * @throws TimeoutException        元素查找超时时，抛出的异常
     * @throws OperateTimeoutException 操作超时时，抛出的异常
     */
    public boolean assertTextContainKey(String elementName, int index, boolean isJudgeAllKey, String... keys) {
        if (Optional.ofNullable(find).isPresent()) {
            return assertTextContainKey(find.findElement(elementName, index), isJudgeAllKey, keys);
        } else {
            throw new ElementOperateException("未指定元素查找类对象，无法通过元素名称查找指定的元素");
        }
    }

    /**
     * 断言元素的文本内容中包含指定的关键词。可设置元素的文本内容是否需要判断传入的所有关键词，其用法可参考{@link AssertStringUtil#assertTextContainKey(String, boolean, String...)}
     *
     * @param elementName   元素名称
     * @param isJudgeAllKey 是否需要完全判断所有关键词
     * @param keys          关键词组
     * @return 断言结果
     * @throws ElementOperateException 未指定元素查找类时，抛出的异常
     * @throws TimeoutException        元素查找超时时，抛出的异常
     * @throws OperateTimeoutException 操作超时时，抛出的异常
     */
    public boolean assertTextContainKey(String elementName, boolean isJudgeAllKey, String... keys) {
        return assertTextContainKey(elementName, 1, isJudgeAllKey, keys);
    }

    /**
     * 断言元素的文本内容中不包含指定的关键词。可设置元素的文本内容是否需要判断传入的所有关键词，其用法可参考{@link AssertStringUtil#assertTextNotContainKey(String, boolean, String...)}
     *
     * @param element       元素类对象
     * @param isJudgeAllKey 是否需要完全判断所有关键词
     * @param keys          关键词组
     * @return 断言结果
     * @throws OperateTimeoutException 操作超时时，抛出的异常
     */
    public boolean assertTextNotContainKey(SikuliElement element, boolean isJudgeAllKey, String... keys) {
        Boolean result = (Boolean) actionOperate(eventName, element, match -> {
            String text = ocr.readText(element);
            return AssertStringUtil.assertTextNotContainKey(text, isJudgeAllKey, keys);
        });
        recordLog(String.format("%s“%s”元素的文本内容不包含%s关键词：%s", eventName, element.getName(), (isJudgeAllKey ? "所有" : "部分"),
                arrayToString(keys)), 1);

        return result;
    }

    /**
     * 断言元素的文本内容中不包含指定的关键词。可设置元素的文本内容是否需要判断传入的所有关键词，其用法可参考{@link AssertStringUtil#assertTextNotContainKey(String, boolean, String...)}
     *
     * @param elementName   元素名称
     * @param index         多元素时的下标，允许传入负数，参考{@link FindSikuliElement#findElement(String, int, String...)}方法
     * @param isJudgeAllKey 是否需要完全判断所有关键词
     * @param keys          关键词组
     * @return 断言结果
     * @throws ElementOperateException 未指定元素查找类时，抛出的异常
     * @throws TimeoutException        元素查找超时时，抛出的异常
     * @throws OperateTimeoutException 操作超时时，抛出的异常
     */
    public boolean assertTextNotContainKey(String elementName, int index, boolean isJudgeAllKey, String... keys) {
        if (Optional.ofNullable(find).isPresent()) {
            return assertTextNotContainKey(find.findElement(elementName, index), isJudgeAllKey, keys);
        } else {
            throw new ElementOperateException("未指定元素查找类对象，无法通过元素名称查找指定的元素");
        }
    }

    /**
     * 断言元素的文本内容中不包含指定的关键词。可设置元素的文本内容是否需要判断传入的所有关键词，其用法可参考{@link AssertStringUtil#assertTextNotContainKey(String, boolean, String...)}
     *
     * @param elementName   元素名称
     * @param isJudgeAllKey 是否需要完全判断所有关键词
     * @param keys          关键词组
     * @return 断言结果
     * @throws ElementOperateException 未指定元素查找类时，抛出的异常
     * @throws TimeoutException        元素查找超时时，抛出的异常
     * @throws OperateTimeoutException 操作超时时，抛出的异常
     */
    public boolean assertTextNotContainKey(String elementName, boolean isJudgeAllKey, String... keys) {
        return assertTextNotContainKey(elementName, 1, isJudgeAllKey, keys);
    }

    /**
     * 断言两元素中的数字内容，按照{@link CompareNumberType}指定的比较类型进行比较。若断言的
     * 元素文本内容存在非数字字符时，则直接断言失败
     *
     * @param referencedElement  参考元素
     * @param compareNumberType  比较方式{@link CompareNumberType}枚举类
     * @param comparativeElement 比较元素
     * @return 断言结果
     * @throws OperateTimeoutException 操作超时时，抛出的异常
     */
    public boolean assertElementsNumber(SikuliElement referencedElement, CompareNumberType compareNumberType,
            SikuliElement comparativeElement) {
        Boolean result = (Boolean) actionOperate(eventName, referencedElement, match -> {
            String text = ocr.readText(referencedElement);
            Double comparaText;
            try {
                comparaText = Double.valueOf(ocr.readText(comparativeElement));
            } catch (Exception e) {
                return null;
            }

            return AssertStringUtil.assertNumber(text, compareNumberType, comparaText);
        });
        recordLog(String.format("%s“%s”元素数字内容%s“%s”元素数字内容", eventName, referencedElement.getName(),
                compareNumberType.getLogText(), comparativeElement.getName()), 2);

        return result;
    }

    /**
     * 断言两元素中的数字内容，按照{@link CompareNumberType}指定的比较类型进行比较。若断言的元素文本内容存在非数字字符时，则直接断言失败
     *
     * @param referencedElementName  参考元素名称
     * @param referencedIndex        多元素时的下标，允许传入负数，参考{@link FindSikuliElement#findElement(String, int, String...)}方法
     * @param compareNumberType      比较方式{@link CompareNumberType}枚举类
     * @param comparativeElementName 比较元素名称
     * @param comparativeIndex       多元素时的下标，允许传入负数，参考{@link FindSikuliElement#findElement(String, int, String...)}方法
     * @return 断言结果
     * @throws ElementOperateException 未指定元素查找类时，抛出的异常
     * @throws TimeoutException        元素查找超时时，抛出的异常
     * @throws OperateTimeoutException 操作超时时，抛出的异常
     */
    public boolean assertElementsNumber(String referencedElementName, int referencedIndex,
            CompareNumberType compareNumberType, String comparativeElementName, int comparativeIndex) {
        if (Optional.ofNullable(find).isPresent()) {
            return assertElementsNumber(find.findElement(referencedElementName, referencedIndex), compareNumberType,
                    find.findElement(comparativeElementName, comparativeIndex));
        } else {
            throw new ElementOperateException("未指定元素查找类对象，无法通过元素名称查找指定的元素");
        }
    }

    /**
     * 断言两元素中的数字内容，按照{@link CompareNumberType}指定的比较类型进行比较。若断言的元素文本内容存在非数字字符时，则直接断言失败
     *
     * @param referencedElementName  参考元素名称
     * @param compareNumberType      比较方式{@link CompareNumberType}枚举类
     * @param comparativeElementName 比较元素名称
     * @return 断言结果
     * @throws ElementOperateException 未指定元素查找类时，抛出的异常
     * @throws TimeoutException        元素查找超时时，抛出的异常
     * @throws OperateTimeoutException 操作超时时，抛出的异常
     */
    public boolean assertElementsNumber(String referencedElementName, CompareNumberType compareNumberType,
            String comparativeElementName) {
        return assertElementsNumber(referencedElementName, 1, compareNumberType, comparativeElementName, 1);
    }

    /**
     * 断言元素中的数字内容与指定的数字按照{@link CompareNumberType}指定的比较类型进行比较。若断言的元素文本内容为非数字字符时，则直接断言失败
     *
     * @param element           {@link Element}对象
     * @param compareNumberType 比较方式{@link CompareNumberType}枚举类
     * @param compareNumber     预期数字
     * @return 断言结果
     * @throws OperateTimeoutException 操作超时时，抛出的异常
     */
    public boolean assertNumber(SikuliElement element, CompareNumberType compareNumberType, double compareNumber) {
        Boolean result = (Boolean) actionOperate(eventName, element, match -> {
            String text = ocr.readText(element);
            return AssertStringUtil.assertNumber(text, compareNumberType, compareNumber);
        });
        recordLog(String.format("%s断言“%s”元素数字内容%s%f", eventName, element.getName(), compareNumberType.getLogText(),
                compareNumber), 1);

        return result;
    }

    /**
     * 断言两元素中的数字内容，按照{@link CompareNumberType}指定的比较类型进行比较。若断言的元素文本内容存在非数字字符时，则直接断言失败
     *
     * @param elementName       参考元素名称
     * @param index             多元素时的下标，允许传入负数，参考{@link FindSikuliElement#findElement(String, int, String...)}方法
     * @param compareNumberType 比较方式{@link CompareNumberType}枚举类
     * @param compareNumber     预期数字
     * @return 断言结果
     * @throws ElementOperateException 未指定元素查找类时，抛出的异常
     * @throws TimeoutException        元素查找超时时，抛出的异常
     * @throws OperateTimeoutException 操作超时时，抛出的异常
     */
    public boolean assertNumber(String elementName, int index, CompareNumberType compareNumberType,
            double compareNumber) {
        if (Optional.ofNullable(find).isPresent()) {
            return assertNumber(find.findElement(elementName, index), compareNumberType, compareNumber);
        } else {
            throw new ElementOperateException("未指定元素查找类对象，无法通过元素名称查找指定的元素");
        }
    }

    /**
     * 断言两元素中的数字内容，按照{@link CompareNumberType}指定的比较类型进行比较。若断言的元素文本内容存在非数字字符时，则直接断言失败
     *
     * @param elementName       参考元素名称
     * @param compareNumberType 比较方式{@link CompareNumberType}枚举类
     * @param compareNumber     预期数字
     * @return 断言结果
     * @throws ElementOperateException 未指定元素查找类时，抛出的异常
     * @throws TimeoutException        元素查找超时时，抛出的异常
     * @throws OperateTimeoutException 操作超时时，抛出的异常
     */
    public boolean assertNumber(String elementName, CompareNumberType compareNumberType, double compareNumber) {
        return assertNumber(elementName, 1, compareNumberType, compareNumber);
    }

    /**
     * 用于将字符串数组内容转成字符串形式返回
     *
     * @param keys 字符串数组
     * @return 数组文本
     */
    private String arrayToString(String... keys) {
        if (keys == null || keys.length == 0) {
            return "[]";
        }

        StringJoiner text = new StringJoiner(", ", "[", "]");
        Arrays.asList(keys).forEach(text::add);
        return text.toString();
    }

    /**
     * <p>
     * <b>文件名：</b>SikuliAssertEvent.java
     * </p>
     * <p>
     * <b>用途：</b> 用于当断言失败且打开抛出异常的开关时，抛出的异常
     * </p>
     * <p>
     * <b>编码时间：</b>2022年2月16日 下午2:09:46
     * </p>
     * <p>
     * <b>修改时间：</b>2022年2月16日 下午2:09:46
     * </p>
     *
     * @author 彭宇琦
     * @version Ver1.0
     * @since JDK 1.8
     * @since autest 3.1.0
     */
    public class SikuliAssertFailException extends SikuliToolsExcepton {
        private static final long serialVersionUID = 1L;

        public SikuliAssertFailException() {
        }

        public SikuliAssertFailException(String arg0) {
            super(arg0);
        }

        public SikuliAssertFailException(Throwable arg0) {
            super(arg0);
        }

        public SikuliAssertFailException(String arg0, Throwable arg1) {
            super(arg0, arg1);
        }

        public SikuliAssertFailException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
            super(arg0, arg1, arg2, arg3);
        }

    }
}
