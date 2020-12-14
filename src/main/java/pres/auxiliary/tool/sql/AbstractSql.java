package pres.auxiliary.tool.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

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
 * @param <T> 子类本身
 */
public class AbstractSql<T extends AbstractSql<T>> {
	/**
	 * SQL语句的分隔符号
	 */
	protected final String SQL_SPLIT_SIGN = ";";
	
	/**
	 * 数据库连接类对象
	 */
	protected Connection connect;
	/**
	 * 存储SQL执行结果集
	 */
	ArrayList<ResultSet> resultList;
//	protected Statement statement;
//	protected PreparedStatement preState;
	
	/**
	 * 运行指定的SQL语句，支持多条SQL执行，每条SQL语句间需要使用分号进行分隔。注意，
	 * 执行多条语句时，若其中一条语句无法执行，则抛出异常，并结束所有语句的执行，且不会保存结果集。
	 * @param sqlText SQL语句
	 * @param isClear 是否清空原结果集
	 * @return 类本身
	 * @throws DatabaseException 当SQL语句为空或执行有误时，抛出的异常
	 */
	@SuppressWarnings("unchecked")
	public T run(String sqlText, boolean isClear) {
		//若需要执行的SQL语句字符串为空，则抛出异常
		if (sqlText.isEmpty()) {
			throw new DatabaseException("SQL语句为空");
		}
		
		ArrayList<ResultSet> resultList = new ArrayList<>();
		//按照分号拆分语句，并遍历执行所有的语句
		Arrays.stream(sqlText.split(SQL_SPLIT_SIGN)).
		filter(sql -> !sql.isEmpty()).
		map(sql -> {
			try {
				//执行SQL
				return connect.prepareStatement(sqlText.toString()).executeQuery();
			} catch (SQLException e) {
				//若执行异常，则抛出异常
				throw new DatabaseException(String.format("SQL无法执行。\nSQL:%s", sqlText.toString()), e);
			}
		}).forEach(resultList :: add);
		
		//判断是否需要清空原结果集
		if (isClear) {
			clear();
		}
		//存储当前执行的结果集
		this.resultList.addAll(resultList);
		
		return (T) this;
	}
	
	/**
	 * 用于清空当前存储的SQL执行结果集
	 */
	public void clear() {
		resultList.clear();
	}
	
	/**
	 * 用于返回指定的结果集
	 * @param index 结果集下标
	 * @return 相应的结果集
	 */
	public ResultSet getResult(int index) {
		return resultList.get(index);
	}
}
