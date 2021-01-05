package com.auxiliary.selenium.tool;

/**
 * <p><b>文件名：</b>RecordType.java</p>
 * <p><b>用途：</b>用于限定使用的自动记录工具</p>
 * <p><b>编码时间：</b>2019年9月7日下午8:09:35</p>
 * <p><b>修改时间：</b>2019年9月7日下午8:09:35</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public enum RecordType {
	/**
	 * 指向使用Log类记录
	 */
	LOG("Log"),
	/**
	 * 指向使用Record类记录
	 */
	RECORD("Record"), 
	/**
	 * 指向使用分步截图方法
	 */
	SCREENSHOT("Screenshot"), 
	/**
	 * 指向使用控制台记录
	 */
	SYSTEM("System");
	
	/**
	 * 记录枚举的名称
	 */
	private String name;
	
	/**
	 * 初始化枚举的名称 
	 * @param name 枚举的名称 
	 */
	private RecordType(String name) {
		this.name = name;
	}
	
	/**
	 * 用于返回枚举值的名称
	 * @return 枚举值的名称
	 */
	public String getName() {
		return this.name;
	}
}
