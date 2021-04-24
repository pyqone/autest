package com.auxiliary.appium.brower;

import java.net.URL;
import java.util.Map;
import java.util.Optional;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.alibaba.fastjson.JSONObject;
import com.auxiliary.selenium.brower.AbstractBrower;
import com.auxiliary.selenium.brower.BrowerException;

/**
 * <p>
 * <b>文件名：</b>CellphoneBrower.java
 * </p>
 * <p>
 * <b>用途：</b> 定义手机浏览器自动化操作相关的基本方法
 * </p>
 * <p>
 * <b>编码时间：</b>2021年4月7日上午7:20:56
 * </p>
 * <p>
 * <b>修改时间：</b>2021年4月7日上午7:20:56
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public abstract class AbstractCellphoneBrower extends AbstractBrower {
	/**
	 * 存储appium的连接地址
	 */
	protected URL linkUrl;
	/**
	 * 存储app信息
	 */
	protected DesiredCapabilities driverInfo = new DesiredCapabilities();
	/**
	 * 用于存储app包信息
	 */
	protected AppPackage appPackage;

	/**
	 * 指向是否需要重启Driver对象
	 */
	protected boolean isRestartDriver = false;

	/**
	 * 构造对象，并初始化设备名称以及待测试app包信息，并清除app信息后启动
	 * 
	 * @param deviceName 设备名称
	 * @param linkUrl    appium的连接地址
	 * @param appPackage app包信息
	 */
	public AbstractCellphoneBrower(String deviceName, URL linkUrl, AppPackage appPackage) {
		if (appPackage == null) {
			throw new BrowerException("未指定app包信息");
		}

		// 设置自动化连接的设备信息
		driverInfo.setCapability(CapabilityType.DEVICE_NAME.getName(), Optional.ofNullable(deviceName)
				.filter(t -> !t.isEmpty()).orElseThrow(() -> new BrowerException("未指定设备名称")));
		driverInfo.setCapability(CapabilityType.NO_RESET.getName(), "false");
		this.linkUrl = Optional.ofNullable(linkUrl).orElseThrow(() -> new BrowerException("未指定appium的连接地址"));

		// 设置app信息
		driverInfo.setCapability(CapabilityType.APP_PACKAGE.getName(), appPackage.getAppPackage());
		driverInfo.setCapability(CapabilityType.APP_ACTIVITY.getName(), appPackage.getAppActivity());
		
		this.appPackage = appPackage;
	}

	/**
	 * 构造对象，并初始化设备名称以及待测试app包信息，并清除app信息后启动
	 * 
	 * @param deviceName   设备名称
	 * @param linkUrl      appium的连接地址
	 * @param packageName  app包名称
	 * @param activityName app启动类名称
	 */
	public AbstractCellphoneBrower(String deviceName, URL linkUrl, String packageName, String activityName) {
		this(deviceName, linkUrl, new AppPackage(packageName, activityName));
	}

	/**
	 * 用于设置浏览器的启动数据
	 * <p>
	 * <b>注意：</b>当设置浏览器启动参数后，再次调用{@link #getDriver()}方法时，会关闭当前执行的浏览器， 重新调起新的浏览器进行操作
	 * </p>
	 * 
	 * @param capabilityName 关键词名称
	 * @param value          值
	 */
	public void setCapability(String capabilityName, String value) {
		Optional.ofNullable(capabilityName).filter(t -> !t.isEmpty()).ifPresent(cn -> {
			driverInfo.setCapability(cn, value);
			isRestartDriver = true;
		});
	}

	/**
	 * 用于根据常用参数枚举设置浏览器的启动数据
	 * <p>
	 * <b>注意：</b>当设置浏览器启动参数后，再次调用{@link #getDriver()}方法时，会关闭当前执行的浏览器， 重新调起新的浏览器进行操作
	 * </p>
	 * 
	 * @param capabilityType 浏览器的启动常用参数枚举{@link CapabilityType}
	 * @param value          值
	 */
	public void setCapability(CapabilityType capabilityType, String value) {
		Optional.ofNullable(capabilityType).ifPresent(cat -> {
			driverInfo.setCapability(cat.getName(), value);
			isRestartDriver = true;
		});
	}

	@Override
	public String getAllInformation() {
		JSONObject json = new JSONObject();

		Map<String, Object> infoMap = getCapabilities();
		json.put("app包名", infoMap.get("appPackage"));
		json.put("app启动类名", infoMap.get("appActivity"));
		json.put("操作系统", infoMap.get("platformName"));
		json.put("版本", infoMap.get("platformVersion"));
		json.put("设备名称", infoMap.get("deviceName"));
		json.put("执行器名称", infoMap.get("automationName"));
		json.put("分辨率", infoMap.get("deviceScreenSize"));

		return json.toJSONString();
	}

	/**
	 * 用于返回浏览器相关的参数信息
	 * @return 参数信息
	 */
	protected abstract Map<String, Object> getCapabilities();
	
	/**
	 * 用于返回app原生元素所在上下文名称
	 * @return 上下文名称
	 */
	public abstract String getNativeName();

	@Override
	public WebDriver getDriver() {
		// 若浏览器设置的状态发生改变，则关闭原有的连接，重新启动浏览器
		if (isRestartDriver) {
			closeBrower();
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
	 * 用于返回app包相关的信息
	 * @return app包信息类对象
	 */
	public AppPackage getAppPackage() {
		return appPackage;
	}

	/**
	 * <p>
	 * <b>文件名：</b>CellphoneBrower.java
	 * </p>
	 * <p>
	 * <b>用途：</b> 定义{@link DesiredCapabilities}类常用的Capability设置枚举
	 * </p>
	 * <p>
	 * <b>编码时间：</b>2021年4月7日上午11:10:56
	 * </p>
	 * <p>
	 * <b>修改时间：</b>2021年4月7日上午11:10:56
	 * </p>
	 * 
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
		NO_RESET("noReset");
		/**
		 * 存储枚举的名称
		 */
		private String name;

		private CapabilityType(String name) {
			this.name = name;
		}

		/**
		 * 返回Capability设置的名称
		 * 
		 * @return Capability设置的名称
		 */
		public String getName() {
			return name;
		}
	}
}
