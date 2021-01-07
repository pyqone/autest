package com.auxiliary.datadriven;

public enum SplitType {
	/**
	 * 切分标记：tap
	 */
	SPLIT_TAB("\\t"),
	/**
	 * 切分标记：空格
	 */
	SPLIT_SPACE(" "),
	/**
	 * 切分标记：中文逗号
	 */
	SPLIT_COMMA_CH("，"),
	/**
	 * 切分标记：英文逗号
	 */
	SPLIT_COMMA_EN(","),
	/**
	 * 切分标记：中文分号
	 */
	SPLIT_SEMICOLON_CH("；"),
	/**
	 * 切分标记：英文分号
	 */
	SPLIT_SEMICOLON_EN(";"),
	/**
	 * 切分标记：中文顿号
	 */
	SPLIT_STOP_CH("、"),
	/**
	 * 切分标记：斜杠
	 */
	SPLIT_SLASH("/"),
	/**
	 * 切分标记：反斜杠
	 */
	SPLIT_BACKSLASH("\\\\"),
	;
	/**
	 * 分隔符号
	 */
	String splitSign = "";

	/**
	 * 初始化数据
	 * @param splitSign 分隔符号
	 */
	private SplitType(String splitSign) {
		this.splitSign = splitSign;
	}

	/**
	 * 用于返回分隔文本的符号
	 * @return 分隔文本的符号
	 */
	public String getSplitSign() {
		return splitSign;
	}
	
	
}
