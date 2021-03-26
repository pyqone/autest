package com.auxiliary.selenium.brower;

import java.net.URL;

import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.auxiliary.selenium.page.Page;

/**
 * <p>
 * <b>文件名：</b>RemoteBrower.java
 * </p>
 * <p>
 * <b>用途：</b> 定义连接远程服务器上浏览器的方法，指定远程浏览器地址后，便可调起远程浏览器
 * </p>
 * <p>
 * <b>编码时间：</b>2021年3月26日下午7:23:54
 * </p>
 * <p>
 * <b>修改时间：</b>2021年3月26日下午7:23:54
 * </p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public class RemoteBrower extends AbstractBrower {
	/**
	 * 指向浏览器名称
	 */
	protected String browerName = "";
	/**
	 * 指向浏览器版本
	 */
	protected String browerVersion = "";
	/**
	 * 指向服务地址
	 */
	protected URL serverUrl;
	/**
	 * 指向当前操作系统类型，默认为自动类型{@link Platform#ANY}
	 */
	protected Platform platform = Platform.ANY;

	/**
	 * 构造对象
	 * 
	 * @param serverUrl   服务地址{@link URL}对象
	 * @param browserType 浏览器名称，可通过{@link BrowserType}类，调用其中的静态属性获取
	 * @param url         待测站点
	 * @param pageName    页面名称
	 */
	public RemoteBrower(URL serverUrl, String browserType, String url, String pageName) {
		this(serverUrl, browserType, new Page(url, pageName));
	}

	/**
	 * 构造对象
	 * 
	 * @param serverUrl   服务地址{@link URL}对象
	 * @param browserType 浏览器名称，可通过{@link BrowserType}类，调用其中的静态属性获取
	 * @param page        页面{@link Page}类对象
	 */
	public RemoteBrower(URL serverUrl, String browserType, Page page) {
		super(page);
		this.browerName = browserType;
		this.serverUrl = serverUrl;
	}

	/**
	 * 用于设置远程浏览器所在操作系统类型
	 * <p>
	 * <b>注意：</b>浏览器启动后，该方法调用后将不会生效
	 * </p>
	 * 
	 * @param platform 操作系统类型{@link Platform}枚举
	 */
	public void setSystemType(Platform platform) {
		this.platform = platform;
	}

	/**
	 * 用于设置远程浏览器的版本
	 * <p>
	 * <b>注意：</b>浏览器启动后，该方法调用后将不会生效
	 * </p>
	 * 
	 * @param browerVersion 浏览器的版本
	 */
	public void setBrowerVersion(String browerVersion) {
		this.browerVersion = browerVersion;
	}

	@Override
	protected void openBrower() {
		try {
			DesiredCapabilities dc = new DesiredCapabilities(browerName,
					browerVersion.isEmpty() ? browerName : browerVersion, platform);
			driver = new RemoteWebDriver(serverUrl, dc);
		} catch (Exception e) {
			throw new IncorrectPageException("远程浏览器异常，无法打开");
		}
	}

	@Override
	protected String getBrowerDriverSetName() {
		return "webdriver.remote.driver";
	}

}
