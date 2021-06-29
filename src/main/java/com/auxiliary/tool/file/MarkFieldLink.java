package com.auxiliary.tool.file;

import java.io.File;
import java.net.URL;

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
public interface MarkFieldLink<T extends MarkFieldLink<T>> {
	/**
	 * 标记对内容添加超链接
	 */
	String KEY_LINK = "link";

	/**
	 * 用于在字段上添加文本内超链
	 * 
	 * @param field       字段
	 * @param linkContent 需要链接的字段
	 * @param index       字段指定的下标
	 * @return 类本身
	 */
	T linkField(String field, String linkContent, int index);

	/**
	 * 用于在字段上添加url超链接
	 * 
	 * @param field 字段
	 * @param url   站点url
	 * @return 类本身
	 */
	T linkUrl(String field, URL url);

	/**
	 * 用于在字段上添加Email超链接
	 * 
	 * @param field 字段
	 * @param email Email地址
	 * @return 类本身
	 */
	T linkEmail(String field, String email);

	/**
	 * 用于在字段上添加外部文件超链接
	 * 
	 * @param field 字段
	 * @param file  外部文件类对象
	 * @return 类本身
	 */
	T linkFile(String field, File file);
}
