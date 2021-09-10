package com.auxiliary.tool.file.io;

import java.util.List;

/**
 * <p>
 * <b>文件名：</b>AbstractFileRead.java
 * </p>
 * <p>
 * <b>用途：</b> 定义文件读取类中基本的方法
 * </p>
 * <p>
 * <b>编码时间：</b>2021年9月6日上午8:19:14
 * </p>
 * <p>
 * <b>修改时间：</b>2021年9月6日上午8:19:14
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public abstract class AbstractFileRead {
	/**
	 * 读取当前文本的所有内容，并按行的形式存储，返回相应的内容集合
	 * 
	 * @return
	 */
	public abstract List<String> readAllContext();
}
