package com.auxiliary.appium.brower;

import com.auxiliary.selenium.brower.AbstractBrower;

public abstract class CellphoneBrower extends AbstractBrower {
	/**
	 * 存储设备的名称
	 */
	protected String deviceName;
	/**
	 * 存储启动app时是否清空数据
	 */
	protected Boolean noReset;
	
	/**
	 * 构造对象，并初始化设备名称
	 * @param deviceName 设备名称
	 */
	public CellphoneBrower(String deviceName) {
		this.deviceName = deviceName;
	}
}
