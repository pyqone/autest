package com.auxiliary.appium.brower;

import java.util.Optional;

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
	 * 存储启动app时是否清空信息
	 */
	private boolean noReset = false;
	
	/**
	 * 初始化app包的信息
	 * @param appPackage app包名
	 * @param appActivity app启动路径
	 * @throws IncorrectPackageException 当包名或启动类名有误时抛出的异常
	 */
	public AppPackage(String appPackage, String appActivity) {
		super();
		this.appPackage = Optional.ofNullable(appPackage).filter(a -> !a.isEmpty()).orElseThrow(() -> new IncorrectPackageException("未指定app包名"));
		this.appActivity = Optional.ofNullable(appActivity).filter(a -> !a.isEmpty()).orElseThrow(() -> new IncorrectPackageException("未指定app启动类名称"));
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

	/**
	 * 以字符串的形式返回启动app时是否清空信息
	 * @return 启动app时是否清空信息
	 */
	public String isNoReset() {
		return String.valueOf(noReset);
	}

	/**
	 * 设置启动app时是否清空信息
	 * @param noReset 启动app时是否清空信息
	 */
	public void setNoReset(boolean noReset) {
		this.noReset = noReset;
	}
}
