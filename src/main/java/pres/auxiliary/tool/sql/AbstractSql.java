package pres.auxiliary.tool.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * <p><b>文件名：</b>AbstractSql.java</p>
 * <p><b>用途：</b>
 * 数据库工具基类，定义工具需要实现的基本方法以及通用方法
 * </p>
 * <p><b>编码时间：</b>2020年12月7日上午8:12:04</p>
 * <p><b>修改时间：</b>2020年12月7日上午8:12:04</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 *
 */
public class AbstractSql {
	/**
	 * 存储需要执行的SQL语句
	 */
	StringBuilder sqlText = new StringBuilder("");
	
	/**
	 * 数据库连接类对象
	 */
	protected Connection connect;
	protected Statement statement;
	protected PreparedStatement preState;
	
	/**
	 * 用于返回当前需要执行的Sql语句
	 * @return 需要执行的Sql语句
	 */
	public String getSql() {
		return sqlText.toString();
	}
	
	/**
	 * 用于执行存储SQL，并返回结果集({@link ResultSet})对象
	 * @return {@link ResultSet}对象
	 */
	public ResultSet run() {
		try {
			return connect.prepareStatement(sqlText.toString()).executeQuery();
		} catch (SQLException e) {
			throw new DatabaseException(String.format("SQL无法执行。\nSQL:%s", sqlText.toString()));
		}
	}

	@Override
	public String toString() {
		return getSql();
	}
}
