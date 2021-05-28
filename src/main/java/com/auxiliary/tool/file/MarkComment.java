package com.auxiliary.tool.file;

/**
 * <p>
 * <b>文件名：</b>MarkComment.java
 * </p>
 * <p>
 * <b>用途：</b> 规定对字段进行注释标记的基本方法
 * </p>
 * <p>
 * <b>编码时间：</b>2021年5月19日上午7:51:08
 * </p>
 * <p>
 * <b>修改时间：</b>2021年5月19日上午7:51:08
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @param <T> 子类
 */
public interface MarkComment<T extends MarkComment<T>> {
	/**
	 * 标记对内容添加注解
	 */
	String KEY_COMMENT = "comment";
	
	/**
	 * 用于在字段上添加注解
	 * 
	 * @param field       字段
	 * @param commentText 注解文本
	 * @return 类本身
	 */
	T fieldComment(String field, String commentText);
}
