package com.auxiliary.selenium.brower;

import org.openqa.selenium.WebDriver;

import com.alibaba.fastjson.JSONObject;
import com.auxiliary.selenium.tool.ActionLogRecord;

/**
 * <p>
 * <b>文件名：</b>AbstractBrower.java
 * </p>
 * <p>
 * <b>用途：</b> 定义浏览器的基本方法，以便于操作类获取到相应的WebDriver对象
 * </p>
 * <p>
 * <b>编码时间：</b>2021年4月2日上午8:19:27
 * </p>
 * <p>
 * <b>修改时间：</b>2021年4月2日上午8:19:27
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public abstract class AbstractBrower {
    /**
     * 用于记录日志
     */
    private ActionLogRecord log = new ActionLogRecord();

    /**
     * 用于存储获取到的浏览器对象
     */
    protected WebDriver driver = null;

    /**
     * 用于存储浏览器启动时的信息
     */
    protected JSONObject informationJson = new JSONObject();

    /**
     * 用于启动浏览器，并返回WebDriver对象
     *
     * @return 指向浏览器的WebDriver对象
     */
    public abstract WebDriver getDriver();

    /**
     * 用于以json字符串的形式返回浏览器的信息
     *
     * @return 浏览器的信息
     */
    public abstract String getAllInformation();

    /**
     * 用于关闭浏览器
     */
    public void closeBrower() {
        if (driver != null) {
            // 关闭浏览器
            driver.quit();
            // 将driver指定为null
            driver = null;
        }
    }

    /**
     * 用于启动浏览器
     */
    protected abstract void openBrower();

    /**
     * 用于返回日志记录类对象
     * @return 日志记录类对象
     */
    public ActionLogRecord getLogRecord() {
        return log;
    }
}
