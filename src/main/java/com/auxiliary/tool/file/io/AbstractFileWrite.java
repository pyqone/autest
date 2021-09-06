package com.auxiliary.tool.file.io;

/**
 * <p>
 * <b>文件名：</b>FileWrite.java
 * </p>
 * <p>
 * <b>用途：</b> 定义写入文件的基本方法
 * </p>
 * <p>
 * <b>编码时间：</b>2021年9月6日下午7:13:26
 * </p>
 * <p>
 * <b>修改时间：</b>2021年9月6日下午7:13:26
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public abstract class AbstractFileWrite {
	/**
	 * 指向是否需要覆盖源文件
	 */
	protected boolean isCover = false;

	/**
	 * 用于向行中添加内容
	 * <p>
	 * <b>注意：</b>当调用添加方法未调用{@link #newLine()}方法时，其不会换行
	 * </p>
	 * 
	 * @param datas 内容数组
	 */
	public abstract void addLine(String... datas);

	/**
	 * 从新的一行开始添加数据
	 */
	public abstract void newLine();

	/**
	 * 设置是否覆盖源文件
	 * 
	 * @param isCover 是否覆盖源文件
	 */
	public void isCover(boolean isCover) {
		this.isCover = isCover;	
	}

	/**
	 * 将缓存的数据写入到文件中
	 */
	public abstract void write();

	/**
	 * 关闭流
	 */
	public abstract void close();
}
