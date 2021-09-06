package com.auxiliary.tool.file.io;

import java.io.BufferedReader;
import java.io.File;
import java.util.List;

/**
 * <p>
 * <b>文件名：</b>TxtContentFileRead.java
 * </p>
 * <p>
 * <b>用途：</b> 定义纯文本型文件的读取方法，可读取以txt后缀结尾的纯文本文件
 * </p>
 * <p>
 * <b>编码时间：</b>2021年9月6日下午6:56:40
 * </p>
 * <p>
 * <b>修改时间：</b>2021年9月6日下午6:56:40
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public class TxtContentFileRead extends AbstractContentFileRead {
	BufferedReader read;

	public TxtContentFileRead(File contentFile) {
	}

	@Override
	public boolean hasNextLine() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String nextLine() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> nextLine(String splitSign) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> readAllContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the read
	 */
	public BufferedReader getReadClass() {
		return read;
	}
}
