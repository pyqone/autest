package com.auxiliary.tool.file.io;

import java.io.File;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

/**
 * <p>
 * <b>文件名：</b>DocxContentFileRead.java
 * </p>
 * <p>
 * <b>用途：</b> 定义新版Word文件的内容读取方法，用于读取以docx为后缀的新版word文档文件
 * </p>
 * <p>
 * <b>编码时间：</b>2021年9月6日下午7:01:12
 * </p>
 * <p>
 * <b>修改时间：</b>2021年9月6日下午7:01:12
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public class DocxContentFileRead extends AbstractContentFileRead {
	XWPFDocument read;

	public DocxContentFileRead(File readFile) {
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
	public XWPFDocument getReadClass() {
		return read;
	}
}
