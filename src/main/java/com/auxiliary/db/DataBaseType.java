package com.auxiliary.db;

/**
 * <p><b>文件名：</b>DataBaseType.java</p>
 * <p><b>用途：</b>
 * 枚举可操作的数据库类型
 * </p>
 * <p><b>编码时间：</b>2020年12月15日上午8:26:38</p>
 * <p><b>修改时间：</b>2020年12月15日上午8:26:38</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 *
 */
public enum DataBaseType {
	/**
	 * 连接Oracle数据库
	 */
	ORACLE("jdbc:oracle:thin:@%s:%s", "oracle.jdbc.driver.OracleDriver"),
	/**
	 * 连接MySQL数据库
	 */
	MYSQL("jdbc:mysql://%s/%s", "com.mysql.jdbc.Driver"),
	/**
	 * 连接SQL Server数据库
	 */
	SQL_SERVER("jdbc:sqlserver://%s;DataBaseName=%s", "com.microsoft.sqlserver.jdbc.SQLServerDriver"),
	;
	
	/**
	 * 存储数据库连接的URL
	 */
	String url;
	/**
	 * 存储数据库加载的类名
	 */
	String className;
	/**
	 * 初始化枚举
	 * @param url 数据库连接的URL
	 * @param className 数据库加载的类名
	 */
	private DataBaseType(String url, String className) {
		this.url = url;
		this.className = className;
	}
	
	/**
	 * 用于返回数据库连接的URL，URL为带格式化字符的字符串，可通过以下方法格式化当前数据库连接的URL：
	 * <code><pre>
	 * String.format(dataBaseType.getUrl(), host, dataBaseName)
	 * </pre></code>
	 * @return 数据库连接的URL
	 */
	public String getUrl() {
		return url;
	}
	
	/**
	 * 用于返回数据库加载的类名
	 * @return 数据库加载的类名
	 */
	public String getClassName() {
		return className;
	}
}
