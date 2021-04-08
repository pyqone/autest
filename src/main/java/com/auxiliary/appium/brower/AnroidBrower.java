package com.auxiliary.appium.brower;

import java.net.URL;
import java.util.Map;
import java.util.Optional;

import org.openqa.selenium.WebElement;

import io.appium.java_client.android.AndroidDriver;

/**
 * <p><b>文件名：</b>AnroidBrower.java</p>
 * <p><b>用途：</b>
 * 定义启动安卓手机app自动化相关的方法
 * </p>
 * <p><b>编码时间：</b>2021年4月7日下午6:52:38</p>
 * <p><b>修改时间：</b>2021年4月7日下午6:52:38</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public class AnroidBrower extends CellphoneBrower {
	/**
	 * 构造对象，并初始化设备名称以及待测试app包信息，默认使用“uiautomator2”执行器，并清除app信息后启动
	 * @param deviceName 设备名称
	 * @param linkUrl appium的连接地址
	 * @param appPackage app包信息
	 */
	public AnroidBrower(String deviceName, URL linkUrl, AppPackage appPackage) {
		super(deviceName, linkUrl, appPackage);
	}

	/**
	 * 构造对象，并初始化设备名称以及待测试app包信息，默认使用“uiautomator2”执行器，并清除app信息后启动
	 * @param deviceName 设备名称
	 * @param linkUrl appium的连接地址
	 * @param packageName app包名称
	 * @param activityName app启动类名称
	 */
	public AnroidBrower(String deviceName, URL linkUrl, String packageName, String activityName) {
		super(deviceName, linkUrl, packageName, activityName);
	}

	@Override
	protected void openBrower() {
		driver = new AndroidDriver<WebElement>(linkUrl, driverInfo);
	}

	@Override
	protected Map<String, Object> getCapabilities() {
		return Optional.ofNullable(((AndroidDriver<?>)driver)).map(d -> d.getCapabilities().asMap()).orElse(driverInfo.asMap());
	}
}
