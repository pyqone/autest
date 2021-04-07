package com.auxiliary.appium.brower;

import java.net.URL;
import java.util.Optional;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.auxiliary.selenium.brower.AbstractBrower;
import com.auxiliary.selenium.brower.BrowerException;

/**
 * <p><b>文件名：</b>CellphoneBrower.java</p>
 * <p><b>用途：</b>
 * 定义手机浏览器自动化操作相关的基本方法
 * </p>
 * <p><b>编码时间：</b>2021年4月7日上午7:20:56</p>
 * <p><b>修改时间：</b>2021年4月7日上午7:20:56</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public abstract class CellphoneBrower extends AbstractBrower {
	/**
	 * 存储appium的连接地址
	 */
	protected URL linkUrl;
	/**
	 * 存储app信息
	 */
	protected DesiredCapabilities driverInfo = new DesiredCapabilities();
	
	/**
	 * 指向是否需要重启Driver对象
	 */
	protected boolean isRestartDriver = false;
	
	/**
	 * 构造对象，并初始化设备名称
	 * @param deviceName 设备名称
	 * @param linkUrl appium的连接地址
	 */
	public CellphoneBrower(String deviceName, URL linkUrl) {
		driverInfo.setCapability(CapabilityType.DEVICE_NAME.getName(), Optional.ofNullable(deviceName).orElseThrow(() -> new BrowerException("未指定设备名称")));
		this.linkUrl = Optional.ofNullable(linkUrl).orElseThrow(() -> new BrowerException("未指定appium的连接地址"));
	}
	
	/**
	 * 构造对象，并初始化设备名称以及待测试app包信息
	 * @param deviceName 设备名称
	 * @param linkUrl appium的连接地址
	 * @param appPackage app包信息
	 */
	public CellphoneBrower(String deviceName, URL linkUrl, AppPackage appPackage) {
		this(deviceName, linkUrl);
		driverInfo.setCapability(CapabilityType.APP_PACKAGE.getName(), appPackage.getAppPackage());
		driverInfo.setCapability(CapabilityType.APP_ACTIVITY.getName(), appPackage.getAppActivity());
	}
	
	/**
	 * 构造对象，并初始化设备名称以及待测试app包信息
	 * @param deviceName 设备名称
	 * @param linkUrl appium的连接地址
	 * @param packageName app包名称
	 * @param activityName app启动类名称
	 */
	public CellphoneBrower(String deviceName, URL linkUrl, String packageName, String activityName) {
		this(deviceName, linkUrl, new AppPackage(packageName, activityName));
	}
	
	/**
	 * 用于设置浏览器的启动数据
	 * <p>
	 * <b>注意：</b>当设置浏览器启动参数后，再次调用{@link #getDriver()}方法时，会关闭当前执行的浏览器，
	 * 重新调起新的浏览器进行操作
	 * </p>
	 * @param capabilityName 关键词名称
	 * @param value 值
	 */
	public void setCapability(String capabilityName, String value) {
		driverInfo.setCapability(capabilityName, value);
		isRestartDriver = true;
	}
	
	/**
	 * 用于根据常用参数枚举设置浏览器的启动数据
	 * <p>
	 * <b>注意：</b>当设置浏览器启动参数后，再次调用{@link #getDriver()}方法时，会关闭当前执行的浏览器，
	 * 重新调起新的浏览器进行操作
	 * </p>
	 * @param capabilityType 浏览器的启动常用参数枚举{@link CapabilityType}
	 * @param value 值
	 */
	public void setCapability(CapabilityType capabilityType, String value) {
		driverInfo.setCapability(capabilityType.getName(), value);
		isRestartDriver = true;
	}
	
	@Override
	public WebDriver getDriver() {
		//若浏览器设置的状态发生改变，则关闭原有的连接，重新启动浏览器
		if (isRestartDriver) {
			driver.quit();
		}
		
		// 若driver对象未生成，则进行开启浏览器的操作
		if (driver == null) {
			// 打开浏览器
			openBrower();
			isRestartDriver = false;
		}

		return driver;
	}
	
	/**
	 * <p><b>文件名：</b>CellphoneBrower.java</p>
	 * <p><b>用途：</b>
	 * 定义{@link DesiredCapabilities}类常用的Capability设置枚举
	 * </p>
	 * <p><b>编码时间：</b>2021年4月7日上午11:10:56</p>
	 * <p><b>修改时间：</b>2021年4月7日上午11:10:56</p>
	 * @author 彭宇琦
	 * @version Ver1.0
	 * @since JDK 1.8
	 */
	public enum CapabilityType {
		/**
		 * 设备名称
		 */
		DEVICE_NAME("deviceName"), 
		/**
		 * 平台名称
		 */
		PLATFORM_NAME("platformName"), 
		/**
		 * app包名
		 */
		APP_PACKAGE("appPackage"), 
		/**
		 * app启动入口
		 */
		APP_ACTIVITY("appActivity"), 
		/**
		 * 自动化执行器名称
		 */
		AUTOMATION_NAME("automationName"), 
		/**
		 * 是否无缓存
		 */
		NO_RESET("noReset")
		;
		/**
		 * 存储枚举的名称
		 */
		private String name;

		private CapabilityType(String name) {
			this.name = name;
		}
		
		/**
		 * 返回Capability设置的名称
		 * @return Capability设置的名称
		 */
		public String getName() {
			return name;
		}
	}
}
