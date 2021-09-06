package com.auxiliary.tool.file.io;

import java.io.BufferedWriter;
import java.io.File;

/**
 * <p>
 * <b>文件名：</b>TxtContentFileWrite.java
 * </p>
 * <p>
 * <b>用途：</b> 定义纯文本型文件的写入方法，可写入以txt后缀结尾的纯文本文件
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
public class TxtContentFileWrite extends AbstractContentFileWrite {
	BufferedWriter write;
	
	public TxtContentFileWrite(File writeFile) {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the read
	 */
	public BufferedWriter getWriteClass() {
		return write;
	}

	@Override
	public void addLine(String... datas) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void newLine() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
}
