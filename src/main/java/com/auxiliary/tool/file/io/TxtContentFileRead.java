package com.auxiliary.tool.file.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
	/**
	 * 存放文件读取类
	 */
	BufferedReader read;
	/**
	 * 存储当前指向的文件
	 */
	File readFile;

	/**
	 * 根据文件类对象，打开相应的文件
	 * @param readFile 文件对象
	 * @throws FileException 当文件打开出错时抛出的异常
	 */
	public TxtContentFileRead(File readFile) {
		this.readFile = openStream(readFile);
	}

	@Override
	public String nextLine() {
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
	
	/**
	 * 用于打开文件读取流
	 * @param readFile 文件对象
	 */
	private File openStream(File readFile) {
		try {
			read = new BufferedReader(new FileReader(readFile));
		} catch (FileNotFoundException e) {
			throw new FileException("文件打开异常", readFile);
		}
		
		return readFile;
	}
}
