package com.auxiliary.tool.file.io;

import java.io.File;

import org.apache.poi.hwpf.HWPFDocument;

/**
 * <p>
 * <b>文件名：</b>DocContentFileWrite.java
 * </p>
 * <p>
 * <b>用途：</b> 定义旧版Word文件的内容写入方法，用于写入以doc为后缀的07版word文档文件
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
public class DocContentFileWrite extends AbstractContentFileWrite {
	HWPFDocument write;
	
	public DocContentFileWrite(File writeFile) {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the read
	 */
	public HWPFDocument getWriteClass() {
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
