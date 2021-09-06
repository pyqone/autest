package com.auxiliary.tool.file.io;

import java.io.File;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

/**
 * <p>
 * <b>文件名：</b>DocxContentFileWrite.java
 * </p>
 * <p>
 * <b>用途：</b> 定义新版Word文件的内容写入方法，用于写入以docx为后缀的新版word文档文件
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
public class DocxContentFileWrite extends AbstractContentFileWrite {
	XWPFDocument write;

	public DocxContentFileWrite(File writeFile) {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @return the read
	 */
	public XWPFDocument getWriteClass() {
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
