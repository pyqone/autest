package com.auxiliary.tool.file.io;

import java.util.Objects;
import java.util.Optional;

/**
 * <p>
 * <b>文件名：</b>FileType.java
 * </p>
 * <p>
 * <b>用途：</b> 定义可读取/写入的文件类型枚举
 * </p>
 * <p>
 * <b>编码时间：</b>2021年9月6日下午8:01:19
 * </p>
 * <p>
 * <b>修改时间：</b>2021年9月6日下午8:01:19
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public enum FileType {
	/**
	 * 纯文本型文件
	 */
	TXT("txt"),
	/**
	 * 07版word文件
	 */
	DOC("doc"),
	/**
	 * 新版word文件
	 */
	DOCX("docx"),
	/**
	 * 逗号分隔型数据文件（以csv结尾等）
	 */
	CSV("csv"),
	/**
	 * 07版excel文件
	 */
	XLS("xls"),
	/**
	 * 新版excel文件
	 */
	XLSX("xlsx");

	/**
	 * 存储文件后缀的判定规则
	 */
	private String regex;

	private FileType(String regex) {
		this.regex = regex;
	}

	/**
	 * 用于根据文件名称，获取其后缀后，与当前的枚举进行判断，若存在与枚举名称相同的后缀，则返回相应的枚举；反之，返回null
	 * 
	 * @param fileName 文件名称（请勿直接传文件后缀）
	 * @return 与文件名称后缀对应的枚举
	 */
	public static FileType getFileType(String fileName) {
		// 获取当前文件名称的后缀
		String suffix = Optional.ofNullable(fileName).filter(na -> na.indexOf('.') > -1)
				.map(na -> na.substring(na.lastIndexOf('.') + 1)).orElse("");

		// 判断获取的后缀是否为空，不为空，则循环与当前的所有枚举进行判断，若与枚举的名称匹配，则返回相应的枚举
		if (!suffix.isEmpty()) {
			for (FileType type : values()) {
				if (Objects.equals(type.regex, suffix)) {
					return type;
				}
			}
		}

		return null;
	}
}
