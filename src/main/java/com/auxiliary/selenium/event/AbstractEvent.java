package com.auxiliary.selenium.event;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Function;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.auxiliary.selenium.brower.AbstractBrower;
import com.auxiliary.selenium.element.Element;

/**
 * <p>
 * <b>文件名：</b>AbstractEvent.java
 * </p>
 * <p>
 * <b>用途：</b>所有事件类的基类，包含了事件类所用到的基础方法以及弹窗处理方法和窗体、窗口的切换方法
 * </p>
 * <p>
 * <b>编码时间：</b>2019年9月24日下午4:24:15
 * </p>
 * <p>
 * <b>修改时间：</b>2021年4月17日 下午12:47:25
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.1
 * @since JDK 8
 *
 */
public abstract class AbstractEvent {
    /**
     * 定义定位到元素相应位置的js代码
     */
    protected final String LOCATION_ELEMENT_JS = "arguments[0].scrollIntoView(false);";

    /**
     * 用于存储浏览器对象
     */
    protected AbstractBrower brower;
    /**
     * 用于存储事件等待事件
     */
    protected WebDriverWait wait;
    /**
     * 设置显示等待的超时时间（默认3秒）
     */
    protected long waitTime = 5;

    /**
     * 存储操作的日志文本
     */
    //	protected static String logText = "";
    /**
     * 存储操作的返回值文本
     */
    //	protected static String resultText = "";

    /**
     * 存储当前操作的元素类
     */
    protected WebElement webElement;

    protected HashSet<String> exceptionSet = new HashSet<>();

    /**
     * 指向当前是否需要自动定位到元素所在位置
     */
    protected boolean isLocationElement = true;

    /**
     * 构造对象并存储浏览器对象
     * 
     * @param brower 浏览器{@link AbstractBrower}对象
     */
    public AbstractEvent(AbstractBrower brower) {
        this.brower = brower;
        wait = new WebDriverWait(brower.getDriver(), waitTime, 200);
    }

    /**
     * 用于设置事件等待时间，默认时间为5秒
     * 
     * @param waitTime 事件等待时间
     */
    public void setWaitTime(long waitTime) {
        wait.withTimeout(Duration.ofSeconds(waitTime));
    }

    /**
     * 用于设置是否将页面移至元素所在的位置
     * 
     * @param isLocationElement 是否将页面移至元素所在的位置
     */
    public void setLocationElement(boolean isLocationElement) {
        this.isLocationElement = isLocationElement;
    }

    /**
     * 用于返回当前在执行等待时被捕获的异常信息集合
     * 
     * @return 异常信息集合
     */
    public Set<String> getExceptionInfomation() {
        return exceptionSet;
    }

    /**
     * 用于返回当前指向的{@link AbstractBrower}类对象
     * 
     * @return {@link AbstractBrower}类对象
     */
    protected AbstractBrower getBrower() {
        return brower;
    }

    /**
     * 用于通过js脚本，将页面定位元素所在位置
     * 
     * @param element {@link Element}对象
     */
    protected void locationElement(WebElement element) {
        // 若抛出NoSuchElementException异常，则不做处理
        try {
            ((JavascriptExecutor) brower.getDriver()).executeScript(LOCATION_ELEMENT_JS, element);
        } catch (Exception e) {
        }
    }

    /**
     * 用于判断元素是否存在
     * 
     * @param element {@link Element}对象
     * @return 当前指定的元素是否存在
     */
    protected boolean isExistElement(Element element) {
        // 判断元素是否存在，若返回元素时抛出异常，则返回false
        try {
            element.getWebElement();
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * 用于将字符串数组内容转成字符串形式返回
     * 
     * @param keys 字符串数组
     * @return 数组文本
     */
    protected String arrayToString(String... keys) {
        if (keys == null || keys.length == 0) {
            return "[]";
        }

        StringJoiner text = new StringJoiner(", ", "[", "]");
        Arrays.asList(keys).forEach(text::add);
        return text.toString();
    }

    /**
     * 用于对元素进行指定操作，并统一处理异常情况。操作必须包含一个{@link String}类型的返回值，
     * 方法中将该返回值存储至{@link AbstractEvent#resultText}属性中。
     * 
     * @param element {@link Element}对象
     * @param action  需要执行的操作
     */
    protected String actionOperate(Element element, Function<Element, String> action) {
        // 清空异常信息
        exceptionSet.clear();
        String resultText = "";

        if (action != null) {
            // 在指定的时间内判断是否能进行操作
            wait.withMessage("操作超时，元素“" + element.getElementData().getName() + "”无法进行相应操作");
            resultText = wait.until((driver) -> {
                try {
                    // 定位到元素
                    if (isLocationElement) {
                        locationElement(element.getWebElement());
                    }

                    Thread.sleep(element.getElementData().getBeforeTime());
                    return action.apply(element);
                } catch (StaleElementReferenceException e) {
                    // 添加异常信息
                    exceptionSet.add(toExceptionString(e));
                    // 若抛出元素过期异常，则调用重新获取元素的方法，并返回失败结果，继续等待再次操作
                    element.againFindElement();
                    return null;
                } catch (NoSuchElementException e) {
                    // 添加异常信息
                    exceptionSet.add(toExceptionString(e));
                    // 若出现元素不存在异常，则直接抛出
                    throw e;
                } catch (Exception e) {
                    // 添加异常信息
                    exceptionSet.add(toExceptionString(e));
                    // 其他异常则返回一个失败的结果，使其继续等待
                    return null;
                }
            });
            webElement = element.getWebElement();
        } else {
            // 在指定的时间内判断是否能进行操作
            wait.withMessage("操作超时，元素“" + element.getElementData().getName() + "”无法获取");
            webElement = wait.until((driver) -> {
                try {
                    return element.getWebElement();
                } catch (StaleElementReferenceException e) {
                    // 若抛出元素过期异常，则调用重新获取元素的方法，并返回失败结果，继续等待再次操作
                    element.againFindElement();
                    return null;
                } catch (NoSuchElementException e) {
                    // 若出现元素不存在异常，则直接抛出
                    throw e;
                } catch (Exception e) {
                    // 添加异常信息
                    exceptionSet.add(toExceptionString(e));
                    // 其他异常则返回一个失败的结果，使其继续等待
                    return null;
                }
            });

            resultText = "";
        }

        return resultText;
    }

    /**
     * 用于将异常信息转换为字符串，转换的格式为“类名:异常信息”
     * 
     * @param exception 异常类对象
     * @return 转换后的异常信息
     */
    private String toExceptionString(Exception exception) {
        return exception.getClass().getName() + ":" + exception.getMessage();
    }
}
