package com.auxiliary.datadriven;

/**
 * 用于定义可向数据驱动中添加的预设模型
 * @author 彭宇琦
 *
 */
public enum DataMode {
	/**
	 * 生成一组身份证号码
	 */
	IDCARD, 
	/**
	 * 生成一组手机号码
	 */
	MOBLE_PHONE, 
	/**
	 * 生成一组座机号码
	 */
	FIXED_PHONE, 
	/**
	 * 生成一组民用车牌
	 */
	CIVIL_CAR_LICECEN, 
	/**
	 * 生成一组警用车牌
	 */
	POLICE_CAR_LICECEN, 
	/**
	 * 生成一组大使馆车牌
	 */
	ELCHEE_CAR_LICECEN, 
	/**
	 * 生成一组新能源汽车车牌
	 */
	ENERGY_CAR_LICECEN, 
	/**
	 * 生成一组姓名
	 */
	NAME;
}
