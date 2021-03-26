package com.auxiliary.selenium.location;

/**
 * <p><b>文件名：</b>ElementLocation.java</p>
 * <p><b>用途：</b>
 * 封装元素定位内容，对元素定位类型以及元素定位方式进行统一管理
 * </p>
 * <p><b>编码时间：</b>2021年3月22日上午8:31:20</p>
 * <p><b>修改时间：</b>2021年3月22日上午8:31:20</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 2.2.0
 */
public class ElementLocationInfo {
	/**
	 * 存储元素定位类型
	 */
	private ByType byType;
	/**
	 * 存储元素定位方式
	 */
	private String locationText;
	
	/**
	 * 设定元素定位信息
	 * @param byType 元素定位类型
	 * @param locationText 元素定位方式
	 */
	public ElementLocationInfo(ByType byType, String locationText) {
		this.byType = byType;
		this.locationText = locationText;
	}
	
	/**
	 * 用于返回元素定位类型
	 * @return 元素定位类型
	 */
	public ByType getByType() {
		return byType;
	}
	
	/**
	 * 用于返回元素定位内容
	 * @return 元素定位内容
	 */
	public String getLocationText() {
		return locationText;
	}

	@Override
	public String toString() {
		return byType + "=" + locationText;
	}

	/**
	 * 用于设置元素定位内容
	 * @param locationText 元素定位内容
	 */
	public void setLocationText(String locationText) {
		this.locationText = locationText;
	}
	
	
}
