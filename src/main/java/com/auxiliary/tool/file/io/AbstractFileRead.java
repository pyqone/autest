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
	 * 返回当前文本的下一行内容
	 * 
	 * @return 文本下一行内容
	 */
	public abstract String nextLine();

	/**
	 * 读取当前文本的下一行内容，并对内容进行切分，返回切分后的内容集合
	 * 
	 * @param splitSign 切分符号
	 * @return 切分后的内容集合
	 */
	public abstract List<String> nextLine(String splitSign);

	/**
	 * 读取当前文本的所有内容，并按行的形式存储，返回相应的内容集合
	 * 
	 * @return
	 */
	public abstract List<String> readAllContext();

	/**
	 * 关闭文件读取流
	 */
	public abstract void close();
}
