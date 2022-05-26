package com.auxiliary.http;

/**
 * <p><b>文件名：</b>HeadType.java</p>
 * <p><b>用途：</b>
 * 枚举部分请求头配置
 * </p>
 * <p><b>编码时间：</b>2020年6月22日上午9:05:07</p>
 * <p><b>修改时间：</b>2020年6月22日上午9:05:07</p>
 * @author 彭宇琦
 * @version Ver1.0
 *
 */
public enum HeadType {
	/**
	 * json协议
	 */
	CONTENT_TYPE_JSON("Content-Type", "application/json"),
	/**
	 * 表单
	 */
	CONTENT_TYPE_URLENCODED("Content-Type", "application/x-www-form-urlencoded"),
	/**
	 * 表单文件
	 */
	CONTENT_TYPE_FROM_DATA("Content-Type", "multipart/form-data"),
	/**
	 * 纯文本
	 */
	CONTENT_TYPE_PLAIN("Content-Type", "text/plain"),
	/**
	 * html文本
	 */
	CONTENT_TYPE_HTML("Content-Type", "text/html"),
	/**
     * xml报文
     */
    CONTENT_TYPE_XML("Content-Type", "application/xml"),
	/**
	 * 文件
	 */
	CONTENT_TYPE_FILE("Content-Type", "application/file"),
	;

	/**
	 * 请求头名称
	 */
	String headName;
	/**
	 * 请求头值
	 */
	String headValue;
	/**
	 * 文字编码
	 */
	String encoding ="";

	/**
	 * 初始化枚举值
	 * @param headName 请求头名称
	 * @param headValue 请求头值
	 */
	private HeadType(String headName, String headValue) {
		this.headName = headName;
		this.headValue = headValue;
	}

	/**
	 * 返回请求头名称
	 * @return 请求头名称
	 */
	public String getHeadName() {
		return headName;
	}

	/**
	 * 返回请求头的值
	 * @return 请求头的值
	 */
	public String getHeadValue() {
		return encoding.isEmpty() ? headValue : (headValue + ";" + encoding);
	}

	/**
	 * 用于设置请求的编码格式
	 * @param encoding 编码格式
	 * @return 枚举本身
	 */
	public HeadType setEncoding(String encoding) {
		this.encoding = "charset=" + encoding;
		return this;
	}

	/**
	 * 用于清除当前设置的编码格式
	 * @return 枚举本身
	 */
	public HeadType clearEncoding() {
		encoding = "";
		return this;
	}
}
