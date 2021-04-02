package com.auxiliary.selenium.brower;

import org.openqa.selenium.WebDriver;

/**
 * <p><b>文件名：</b>AbstractBrower.java</p>
 * <p><b>用途：</b>
 * 定义浏览器的基本方法，以便于操作类获取到相应的WebDriver对象
 * </p>
 * <p><b>编码时间：</b>2021年4月2日上午8:19:27</p>
 * <p><b>修改时间：</b>2021年4月2日上午8:19:27</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public abstract class AbstractBrower {
	/**
	 * 用于启动浏览器，并返回WebDriver对象
	 * 
	 * @return 指向浏览器的WebDriver对象
	 */
	public abstract WebDriver getDriver();
	
	/**
	 * 用于以json字符串的形式返回浏览器的信息
	 * @return 浏览器的信息
	 */
	public abstract String getAllInformation();
}
