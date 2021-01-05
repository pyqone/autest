package com.auxiliary.selenium.event;

/**
 * <p><b>文件名：</b>CompareNumberType.java</p>
 * <p><b>用途：</b>
 * 用于枚举对数字比较的方式
 * </p>
 * <p><b>编码时间：</b>2020年10月20日下午7:00:14</p>
 * <p><b>修改时间：</b>2020年10月20日下午7:00:14</p>
 * @author 彭宇琦
 * @version Ver1.0
 *
 */
public enum CompareNumberType {
	/**
	 * 等于
	 */
	EQUAL("等于"), 
	/**
	 * 大于
	 */
	GREATER("大于"), 
	/**
	 * 小于
	 */
	LESS("小于"), 
	/**
	 * 大于等于
	 */
	GREATER_OR_EQUAL("大于等于"), 
	/**
	 * 小于等于
	 */
	LESS_OR_EQUAL("小于等于");
	
	/**
	 * 日志文本
	 */
	String logText;

	/**
	 * 初始化枚举值
	 * @param logText 枚举值
	 */
	private CompareNumberType(String logText) {
		this.logText = logText;
	}

	/**
	 * 返回枚举值
	 * @return 枚举值
	 */
	public String getLogText() {
		return logText;
	}
}
