package com.auxiliary.tool.file.io;

import java.io.File;

/**
 * <p>
 * <b>文件名：</b>AbstractContentFileRead.java
 * </p>
 * <p>
 * <b>用途：</b> 定义文本型文件的读取基本方法
 * </p>
 * <p>
 * <b>编码时间：</b>2021年9月6日上午8:26:39
 * </p>
 * <p>
 * <b>修改时间：</b>2021年9月6日上午8:26:39
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public abstract class AbstractContentFileRead extends AbstractFileRead {
	public AbstractContentFileRead(File readFile) {
		super(readFile);
	}
}
