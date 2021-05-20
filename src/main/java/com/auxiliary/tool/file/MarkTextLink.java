package com.auxiliary.tool.file;

/**
 * <p>
 * <b>文件名：</b>MarkTextLink.java
 * </p>
 * <p>
 * <b>用途：</b> 规定向文本或字段添加超链接的基本方法
 * </p>
 * <p>
 * <b>编码时间：</b>2021年5月19日上午8:08:08
 * </p>
 * <p>
 * <b>修改时间：</b>2021年5月19日上午8:08:08
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @param <T> 子类
 */
public interface MarkTextLink<T extends MarkTextLink<T>> {
	/**
	 * 用于对字段指定段落文本内容添加超链接
	 * 
	 * @param field       字段
	 * @param textIndex   内容下标
	 * @param likeContent 超链接内容
	 * @return 类本身
	 */
	T textLink(String field, int textIndex, String likeContent);

	/**
	 * 用于对在字段文本块上添加超链接
	 * 
	 * @param field       字段
	 * @param likeContent 超链接内容
	 * @return 类本身
	 */
	T fieldLink(String field, String likeContent);
}
