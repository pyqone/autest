package com.auxiliary.tool.file.io;

import java.io.File;

/**
 * <p><b>文件名：</b>IOFactory.java</p>
 * <p><b>用途：</b>
 * 定义构造文件读取/写入类的超级工厂
 * </p>
 * <p><b>编码时间：</b>2021年9月6日下午8:09:26</p>
 * <p><b>修改时间：</b>2021年9月6日下午8:09:26</p>
 * @author 
 * @version Ver1.0
 * @since JDK 1.8
 */
public class IOFactory {
	private IOFactory() {
	}
	
	/**
	 * 根据文本文件自动判断相应的文件读取方式，并进行返回文件读取类对象
	 * 
	 * @param file 文件类对象
	 * @return 文件读取类对象
	 */
	public static AbstractFileRead createRead(File file) {
		return FileReadFactory.createRead(file);
	}

	/**
	 * 根据文件类型枚举，以及相应的文件类对象，返回相应的文件读取类对象
	 * 
	 * @param file    文件类对象
	 * @param fileTye 文件类型枚举
	 * @return 文件读取类对象
	 */
	public static AbstractFileRead createRead(File file, FileType fileTye) {
		return FileReadFactory.createRead(file, fileTye);
	}
	
	/**
	 * 根据文本文件自动判断相应的文件写入方式，并进行返回文件写入类对象
	 * 
	 * @param file 文件类对象
	 * @return 文件写入类对象
	 */
	public static AbstractFileWrite createWrite(File file) {
		return FileWriteFactory.createWrite(file);
	}

	/**
	 * 根据文件类型枚举，以及相应的文件类对象，返回相应的文件写入类对象
	 * 
	 * @param file    文件类对象
	 * @param fileTye 文件类型枚举
	 * @return 文件写入类对象
	 */
	public static AbstractFileWrite createWrite(File file, FileType fileTye) {
		return FileWriteFactory.createWrite(file, fileTye);
	}
}
