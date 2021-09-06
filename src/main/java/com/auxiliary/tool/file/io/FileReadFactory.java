package com.auxiliary.tool.file.io;

import java.io.File;

/**
 * <p>
 * <b>文件名：</b>FileReadFactory.java
 * </p>
 * <p>
 * <b>用途：</b> 定义构造文件读取类的工厂
 * </p>
 * <p>
 * <b>编码时间：</b>2021年9月6日下午7:58:38
 * </p>
 * <p>
 * <b>修改时间：</b>2021年9月6日下午7:58:38
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */

public class FileReadFactory {
	private FileReadFactory() {
	}

	/**
	 * 根据文本文件自动判断相应的文件读取方式，并进行返回文件读取类对象
	 * 
	 * @param file 文件类对象
	 * @return 文件读取类对象
	 */
	public static AbstractFileRead createRead(File file) {
	}

	/**
	 * 根据文件类型枚举，以及相应的文件类对象，返回相应的文件读取类对象
	 * 
	 * @param file    文件类对象
	 * @param fileTye 文件类型枚举
	 * @return 文件读取类对象
	 */
	public static AbstractFileRead createRead(File file, FileType fileTye) {
	}
}
