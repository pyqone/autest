package com.auxiliary.tool.file.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

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
	 * 根据文件类对象，打开相应的文件
	 * @param readFile 文件对象
	 * @throws FileException 当文件打开出错时抛出的异常
	 */
	public TxtContentFileRead(File readFile) {
		super(readFile);
		// 读取文件中的内容，并存储至文本数据集合中
		try (BufferedReader br = new BufferedReader(
				new FileReader(Optional.ofNullable(readFile).orElseThrow(() -> new FileException("未传入文件对象", readFile))))) {
			// 按行遍历文件内容
			br.lines().forEach(textList::add);
		} catch (IOException e) {
			throw new FileException("文件读取异常", readFile);
		}
	}

	@Override
	public List<String> readAllContext() {
		return textList;
	}

	/**
	 * 返回文件读取类对象，其流为新的流，即未进行过文件读取的流
	 * @return 文件读取流
	 */
	public BufferedReader getReadClass() {
		try {
			return new BufferedReader(new FileReader(readFile));
		} catch (FileNotFoundException e) {
			throw new FileException("文件打开异常", readFile);
		}
	}
}
