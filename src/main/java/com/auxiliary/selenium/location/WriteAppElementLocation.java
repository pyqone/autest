package com.auxiliary.selenium.location;

/**
 * <p>
 * <b>文件名：</b>WriteAppElementLocation.java
 * </p>
 * <p>
 * <b>用途：</b>定义编写app元素的内容
 * </p>
 * <p>
 * <b>编码时间：</b>2021年4月16日 下午9:33:38
 * </p>
 * <p>
 * <b>修改时间：</b>2021年4月16日 下午9:33:38
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public interface WriteAppElementLocation {
	/**
	 * 用于编写app元素的上下文
	 * <p>
	 * 当写入该属性后，则表示该app元素为webview的元素
	 * </p>
	 * 
	 * @param name    元素名称
	 * @param context 上下文
	 */
	void putContext(String name, String context);
}
