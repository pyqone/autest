package com.auxiliary.datadriven;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class FunctionUtil {
	/**
	 * 公式切分起始标记（正则写法）
	 */
	public static final String SPLIT_START = "\\$\\{";
	/**
	 * 公式切分结束标记（正则写法）
	 */
	public static final String SPLIT_END = "\\$\\{";

	/**
	 * 公式起始标记
	 */
	public static final String FUNCTION_SIGN_START = "${";
	/**
	 * 公式结束标记
	 */
	public static final String FUNCTION_SIGN_END = "}";
	
	/**
	 * 工具类，私有构造
	 */
	private FunctionUtil() {
	}
	
	/**
	 * 用于提取内容中所有占位符中的内容
	 * 
	 * @param content 读取的内容
	 * @return 处理后的内容
	 */
	public static Set<String> getFunctionKey(String content) {
		Set<String> keyList = new HashSet<>();
		
		//判断是否传入内容
		if (!Optional.ofNullable(content).filter(t -> !t.isEmpty()).isPresent()) {
			return keyList;
		}
		
		// 判断传入的内容是否包含公式起始符号，若不包含，则返回原串
		if (content.indexOf(FUNCTION_SIGN_START) < 0) {
			return keyList;
		}

		// 按照公式起始符号对内容进行切分，遍历切分后的每一个元素
		String[] texts = content.split(SPLIT_START);
		for (int i = 0; i < texts.length; i++) {
			// 判断切分的元素是否包含公式结束符号
			int index = texts[i].indexOf(FUNCTION_SIGN_END);
			if (index > -1) {
				// 截取公式关键词
				keyList.add(texts[i].substring(0, index));
			}
		}

		return keyList;
	}
}
