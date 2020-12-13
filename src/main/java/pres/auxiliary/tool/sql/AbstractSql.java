package pres.auxiliary.tool.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringJoiner;

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
	 * 运行指定的SQL语句。注意，使用该方法执行的语句会覆盖类中存储的SQL语句
	 * @param sqlText SQL语句
	 * @return 类本身
	 * @throws DatabaseException 当SQL语句为空或执行有误时，抛出的异常
	 */
	@SuppressWarnings("unchecked")
	public T run(String sqlText) {
		//若需要执行的SQL语句字符串为空，则抛出异常
		if (sqlText.isEmpty()) {
			throw new DatabaseException("SQL语句为空");
		}
		
		try {
			//执行SQL
			resultList.add(connect.prepareStatement(sqlText.toString()).executeQuery());
			//返回结果集
			return (T) this;
		} catch (SQLException e) {
			//若执行异常，则抛出异常
			throw new DatabaseException(String.format("SQL无法执行。\nSQL:%s", sqlText.toString()), e);
		}
	}
	
	public T run() {
		
	}
	
	/**
	 * <p><b>文件名：</b>AbstractSql.java</p>
	 * <p><b>用途：</b>
	 * 用于在SQL中添加条件
	 * </p>
	 * <p><b>编码时间：</b>2020年12月9日上午8:09:06</p>
	 * <p><b>修改时间：</b>2020年12月9日上午8:09:06</p>
	 * @author 
	 * @version Ver1.0
	 * @since JDK 1.8
	 *
	 */
	public static class Where {
		/**
		 * 添加字段与某个值相等或不相等的条件（“=”条件），即：
		 * <ul>
		 * 	<li>字段与值相等：Where.fieldEquals("name", "张三", false)</li>
		 * 	<li>字段与值不相等：Where.fieldEquals("a.name", "李四", true)</li>
		 * </ul>
		 * @param field 字段名
		 * @param value 值
		 * @param isNot 是否取反
		 * @return 相应的SQL语句
		 */
		public static String equalsValue(String field, String value, boolean isNot) {
			return field + (isNot ? " !" : " ") + "= '" + value + "'";
		}
		
		/**
		 * 添加字段的值与表其他字段的值相等或不相等的条件（“=”条件），即：
		 * <ul>
		 * 	<li>字段与值相等：Where.fieldEquals("name", "login_name", false)</li>
		 * 	<li>字段与值不相等：Where.fieldEquals("a.name", "b.name", true)</li>
		 * </ul>
		 * @param field 字段名
		 * @param equalsField 需判断的字段名
		 * @param isNot 是否取反
		 * @return 相应的SQL语句
		 */
		public static String equalsField(String field, String equalsField, boolean isNot) {
			return field + (isNot ? " !" : " ") + "= " + equalsField;
		}
		
		/**
		 * 添加字段包含或不包含某个值的条件（like条件），即：
		 * <ul>
		 * 	<li>字段包含值：Where.like("name", "%张三%", false)</li>
		 * 	<li>字段不包含值：Where.like("a.name", "%张三", true)</li>
		 * </ul>
		 * @param field 字段名
		 * @param value 值
		 * @param isNot 是否取反
		 * @return 相应的SQL语句
		 */
		public static String like(String field, String value, boolean isNot) {
			return field  + (isNot ? " NOT " : " ") + "LIKE " + value;
		}
		
		/**
		 * 添加字段值是否在一组值集合中的条件（in条件），即：
		 * <ul>
		 * 	<li>字段在某个值集合中：Where.in("name", false, "张三", "李四", "王五")</li>
		 * 	<li>字段不包含值：Where.like("a.name", true, "张三", "李四", "王五")</li>
		 * </ul>
		 * @param field 字段名
		 * @param isNot 是否取反
		 * @param values 值集合，可传入多个
		 * @return 相应的SQL语句
		 */
		public static String in(String field, boolean isNot, String...values) {
			//拼接值集合
			StringJoiner valueText = new StringJoiner(", ", "(", ")");
			Arrays.stream(values).map(value -> "'" + value + "'").forEach(valueText :: add);
			
			return field + (isNot ? " NOT " : " ") + "IN ";
		}
	}
}
