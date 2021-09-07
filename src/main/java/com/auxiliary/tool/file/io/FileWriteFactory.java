package com.auxiliary.tool.file.io;

import java.io.File;

/**
 * <p>
 * <b>文件名：</b>FileReadFactory.java
 * </p>
 * <p>
 * <b>用途：</b> 定义构造文件写入类的工厂
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

public class FileWriteFactory {
	private FileWriteFactory() {
	}

	/**
	 * 根据文件自动判断相应的文件读取方式，并进行返回文件写入类对象
	 * 
	 * @param file 文件类对象
	 * @return 文件写入类对象
	 * @see 支持的文件后缀可参考{@link FileType}
	 * @throws FileException 文件后缀无法识别时，抛出的异常
	 */
	public static AbstractFileWrite createWrite(File file) {
		// 获取文件后缀
		FileType fileTye = FileType.getFileType(file.getName());
		// 判断文件后缀对应的后缀是否存在
		if (fileTye != null) {
			return createWrite(file, fileTye);
		} else {
			throw new FileException("不支持的文件后缀");
		}
	}

	/**
	 * 根据文件类型枚举，以及相应的文件类对象，返回相应的文件写入类对象
	 * 
	 * @param file    文件类对象
	 * @param fileTye 文件类型枚举
	 * @return 文件写入类对象
	 */
	public static AbstractFileWrite createWrite(File file, FileType fileTye) {
		// 根据枚举，选择相应的文件读取子类，并进行返回
		switch (fileTye) {
		case CSV:
			return new CsvTableFileWrite(file);
		case DOC:
			return new DocContentFileWrite(file);
		case DOCX:
			return new DocxContentFileWrite(file);
		case TXT:
			return new TxtContentFileWrite(file);
		case XLS:
			return new XlsTableFileWrite(file);
		case XLSX:
			return new XlsxTableFileWrite(file);
		default:
			throw new FileException("不支持的文件类型");
		}
	}
}
