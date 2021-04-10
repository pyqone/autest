package com.auxiliary.selenium.location;

/**
 * <p>
 * <b>文件名：</b>AppElementLocation.java
 * </p>
 * <p>
 * <b>用途：</b> 定义与app相关的元素信息返回方法
 * </p>
 * <p>
 * <b>编码时间：</b>2021年4月10日下午2:53:33
 * </p>
 * <p>
 * <b>修改时间：</b>2021年4月10日下午2:53:33
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public interface AppElementLocation {
	/**
	 * 用于返回当前元素是否为app原生元素
	 * 
	 * @return 是否为app原生元素
	 */
	boolean isNative();

	/**
	 * 用于返回当前元素的所在的上下文内容
	 * 
	 * @return 上下文内容
	 */
	String getContext();
}
