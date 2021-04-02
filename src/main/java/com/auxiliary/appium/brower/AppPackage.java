package com.auxiliary.appium.brower;

/**
 * <p><b>文件名：</b>AppPackage.java</p>
 * <p><b>用途：</b>
 * 存储app包信息
 * </p>
 * <p><b>编码时间：</b>2021年4月2日下午5:27:22</p>
 * <p><b>修改时间：</b>2021年4月2日下午5:27:22</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public class AppPackage {
	/**
	 * 存储app包名
	 */
	private String appPackage;
	/**
	 * 存储app的启动路径
	 */
	private String appActivity;
	
	/**
	 * 初始化app包的信息
	 * @param appPackage app包名
	 * @param appActivity app启动路径
	 */
	public AppPackage(String appPackage, String appActivity) {
		super();
		this.appPackage = appPackage;
		this.appActivity = appActivity;
	}

	/**
	 * 用于返回app的包名
	 * @return app的包名
	 */
	public String getAppPackage() {
		return appPackage;
	}

	/**
	 * 用于返回app的启动路径
	 * @return 启动路径
	 */
	public String getAppActivity() {
		return appActivity;
	}
}
