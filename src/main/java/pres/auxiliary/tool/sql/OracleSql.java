package pres.auxiliary.tool.sql;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Properties;

import oracle.jdbc.driver.OracleDriver;

/**
 * <p><b>文件名：</b>OracleSql.java</p>
 * <p><b>用途：</b>
 * 提供对Oracle数据进行基本的操作
 * </p>
 * <p><b>编码时间：</b>2020年12月7日上午8:13:20</p>
 * <p><b>修改时间：</b>2020年12月7日上午8:13:20</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 *
 */
public class OracleSql extends AbstractSql {
	/**
	 * 构造方法，用于指定数据库基本信息
	 * @param username 用户名
	 * @param password 密码
	 * @param host 主机（包括端口，默认为1521）
	 * @param dataBase 数据源
	 */
	public OracleSql(String username, String password, String host, String dataBase) {
		//定义数据库对象，若驱动不存在时，则抛出异常
		Driver driver = new OracleDriver();
		try {
			DriverManager.deregisterDriver(driver);
		} catch (SQLException e) {
			throw new DatabaseException("ojdbc驱动不存在或驱动有误，无法连接Oracle数据库", e);
		}

		// 添加用户名与密码，连接数据库，若数据连接有误，则抛出异常
		Properties pro = new Properties();
		pro.put("user", username);
		pro.put("password", password);
		String url = "jdbc:oracle:thin:@" + host + ":" + dataBase;
		try {
			connect = driver.connect(url, pro);
		} catch (SQLException e) {
			throw new DatabaseException(String.format("Oracle数据库连接异常，连接信息：\n用户名：%s\n密码：%s\n连接url：%s", username, password, url), e);
		}
	}
	
	public OracleSql type() {
		
		return this;
	}
	
	/**
	 * 用于查找指定表名的数据，可指定输出的字段
	 * @param tableName 表名
	 * @param fieldNames 字段名
	 */
	public OracleSql find(String tableName, String...fieldNames) {
		sqlText.append("SELECT ");
		
		//添加字段名
		sqlText.append("");
		
		sqlText.append(" ");
		sqlText.append("FROM ");
		sqlText.append(tableName);
		
		return this;
	}
}
