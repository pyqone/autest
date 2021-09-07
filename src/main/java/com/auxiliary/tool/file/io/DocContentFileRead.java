package com.auxiliary.tool.file.io;

import java.io.File;
import java.util.List;

import org.apache.poi.hwpf.HWPFDocument;

/**
 * <p>
 * <b>文件名：</b>DocContentFileRead.java
 * </p>
 * <p>
 * <b>用途：</b> 定义旧版Word文件的内容读取方法，用于读取以doc为后缀的07版word文档文件
 * </p>
 * <p>
 * <b>编码时间：</b>2021年9月6日下午7:00:01
 * </p>
 * <p>
 * <b>修改时间：</b>2021年9月6日下午7:00:01
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public class DocContentFileRead extends AbstractContentFileRead {
	HWPFDocument read;

	public DocContentFileRead(File readFile) {
		// TODO Auto-generated constructor stub
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
	public HWPFDocument getReadClass() {
		return read;
	}
}
