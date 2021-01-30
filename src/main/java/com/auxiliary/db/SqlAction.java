package com.auxiliary.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.auxiliary.tool.data.TableData;

/**
 * <p>
 * <b>文件名：</b>SqlAction.java
 * </p>
 * <p>
 * <b>用途：</b> 数据库工具基类，定义工具需要实现的基本方法以及通用方法。
 * </p>
 * <p>
 * 类中的所有方法均为重头遍历结果集，与当前在结果集中光标的位置无关，并且遍历后不影响当前光标的位置。
 * 以调用{@link #getFirstResult()}方法为例：
 * </p>
 * <p>
 * 查询的结果集中存在ID字段，并且字段下有如下数据[1, 2, 3, 4, 5, 6, 7]，对该字段循环获取5次： <code><pre>
 * // run为SqlAction类对象
 * ResultSet result = run.{@link #getResult()};
 * for (int index = 0; index < 5 && result.next(), index++) {
 * 	System.out.print(result.getString("ID"));
 * }
 * 
 * System.out.print(run.{@link #getFirstResult()});
 * 
 * if (result.next()) {
 * 	System.out.print(result.getString("ID"));
 * }
 * </pre></code> 其结果输出为：<br>
 * 12345<br>
 * 1<br>
 * 6<br>
 * </p>
 * <p>
 * <b>编码时间：</b>2020年12月7日上午8:12:04
 * </p>
 * <p>
 * <b>修改时间：</b>2020年12月7日上午8:12:04
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 *
 */
public class SqlAction {
	/**
	 * SQL语句的分隔符号
	 */
	protected final String SQL_SPLIT_SIGN = ";";

	/**
	 * 数据库连接类对象
	 */
	protected Connection connect;
	/**
	 * 指向连接的数据库类型
	 */
	protected DataBaseType dataBaseType;
	/**
	 * 指向连接的数据库用户名
	 */
	protected String username;
	/**
	 * 指向连接的数据库密码
	 */
	protected String password;
	/**
	 * 指向连接的数据主机
	 */
	protected String host;
	/**
	 * 指向连接的数据名称
	 */
	protected String dataBaseName;
	
	/**
	 * 存储SQL执行结果集
	 */
	protected Optional<ResultSet> result = Optional.empty();

	/**
	 * 存储执行结果表的字段
	 */
	ArrayList<String> fieldNameList = new ArrayList<>();

	/**
	 * 构造方法，用于指定数据库基本信息
	 * 
	 * @param username     用户名
	 * @param password     密码
	 * @param host         主机（包括端口）
	 * @param dataBaseName 数据源
	 */
	public SqlAction(DataBaseType dataBaseType, String username, String password, String host, String dataBaseName) {
		this.dataBaseType = Optional.ofNullable(dataBaseType).orElseThrow(() -> new DatabaseException("未指定数据库类型"));
		this.username = Optional.ofNullable(username).orElseThrow(() -> new DatabaseException("未指定数据库登录用户名"));
		this.password = Optional.ofNullable(password).orElseThrow(() -> new DatabaseException("未指定数据库登录密码"));
		this.host = Optional.ofNullable(host).orElseThrow(() -> new DatabaseException("未指定数据库登录主机"));
		this.dataBaseName = Optional.ofNullable(dataBaseName).orElseThrow(() -> new DatabaseException("未指定数据库名称"));
		
		try {
			Class.forName(dataBaseType.getClassName());
			connect = Optional.ofNullable(DriverManager
					.getConnection(String.format(dataBaseType.getUrl(), host, dataBaseName), username, password))
					.orElseThrow(() -> new DatabaseException(String.format(dataBaseType + "数据库连接异常，连接信息：\n用户名：%s\n密码：%s\n连接url：%s", username,
					password, String.format(dataBaseType.getUrl(), host, dataBaseName))));
		} catch (SQLException e) {
			throw new DatabaseException(String.format(dataBaseType + "数据库连接异常，连接信息：\n用户名：%s\n密码：%s\n连接url：%s", username,
					password, String.format(dataBaseType.getUrl(), host, dataBaseName)), e);
		} catch (ClassNotFoundException e) {
			throw new DatabaseException("数据库驱动不存在：" + dataBaseType.getClassName());
		}
	}

	/**
	 * 运行指定的SQL语句。
	 * 
	 * @param sqlText SQL语句
	 * @return 类本身
	 * @throws DatabaseException 当SQL语句为空或执行有误时，抛出的异常
	 */
	public SqlAction run(String sqlText) {
		fieldNameList.clear();

		// 若需要执行的SQL语句字符串为空，则抛出异常
		if (sqlText.isEmpty()) {
			throw new DatabaseException("SQL语句为空");
		}

		// 添加数据库驱动信息
		try {
			Class.forName(dataBaseType.getClassName());
		} catch (ClassNotFoundException e) {
			throw new DatabaseException("数据库驱动不存在：" + dataBaseType.getClassName(), e);
		}
		// 连接数据库
		try {
			// 执行SQL，设置结果集可以滚动
			result = Optional
					.ofNullable(connect.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)
							.executeQuery(sqlText.toString()));

			// 存储执行结果的表字段名，若执行后不存在表（如执行插入等SQL）时，则不做任何处理
			try {
				ResultSetMetaData metaData = result.get().getMetaData();
				int columnSize = metaData.getColumnCount();
				// 注意：JDBC下标都是从1开始计算
				for (int index = 1; index <= columnSize; index++) {
					fieldNameList.add(metaData.getColumnName(index));
				}
			} catch (SQLException noFieldException) {
			}
		} catch (SQLException e) {
			// 若执行异常，则抛出异常
			throw new DatabaseException(String.format("SQL无法执行。\nSQL:%s", sqlText.toString()), e);
		}

		return this;
	}

	/**
	 * 用于返回结果集
	 * 
	 * @return 结果集
	 */
	public ResultSet getResultSet() {
		return result.orElseThrow(DatabaseException::new);
	}

	/**
	 * 用于返回当前结果集中的元素个数
	 * 
	 * @return 当前结果集中元素个数
	 */
	public int getResultSize() {
		int size = -1;
		ResultSet result = getResultSet();
		try {
			// 获取当前行
			int nowIndex = result.getRow();

			// 将光标移至最后一行，并存储当前行的行号，其行号即为当前元素的个数
			result.last();
			size = result.getRow();

			// 将光标移回原行
			result.absolute(nowIndex);
		} catch (SQLException e) {
			// 若执行异常，则抛出异常
			throw new DatabaseException("数据库异常，无法获取元素个数", e);
		}

		return size;
	}

	/**
	 * 获取结果集中所有字段的名称
	 * 
	 * @return 字段名称集合
	 */
	public ArrayList<String> getColumnNames() {
		return fieldNameList;
	}

	/**
	 * 用于根据表中的字段名，获取结果集指定行数的内容，并以字符串的形式进行存储。
	 * <p>
	 * <b>注意：</b>其行下标参数从1开始计算，即传入1则表示为获取第一行数据，具体参数说明可参考{@link #getResult(int, int, List)}
	 * </p>
	 * 
	 * @param startIndex 开始行
	 * @param endIndex   结束行
	 * @param fieldNames 字段组，可传入多个值
	 * @return 字段组对应的结果文本集合
	 * @throws DatabaseException 若字段有误或未执行SQL或无结果集时抛出的异常
	 * @see #getResult(int, int, List)
	 */
	public TableData<String> getResult(int startIndex, int endIndex, String... fieldNames) {
		return getResult(startIndex, endIndex, Arrays.asList(Optional.ofNullable(fieldNames).orElse(new String[] {})));
	}

	/**
	 * 用于根据表中的字段名，获取结果集指定行数的内容，并以字符串的形式进行存储。
	 * <p>
	 * 方法接收结果集的起始下标与结束下标，并根据该组下标，获取结果，将其转换为
	 * 字符串的形式进行返回，另外，行下标从1开始遍历，下标传入0或者1都表示获取第1行元素，且下标允许 传入负数，表示反序遍历。其可能会出现以下情况：
	 * <ol>
	 * <li>起始下标大于结果集总数：返回空集合</li>
	 * <li>起始下标大于结束下标：只获取起始行的数据</li>
	 * <li>起始下标等于结束下标：获取当前行的数据</li>
	 * <li>起始下标小于结束下标：获取相应多行的数据</li>
	 * </ol>
	 * </p>
	 * <p>
	 * <b>注意：</b>调用方法后，下标传入的负数实则会根据结果集的个数，转换为一个正数
	 * 进行传参，即假设结果集中有100个元素，下标传入-2时，会将其转换为99（倒数第二行）进行处理。 若负数下标的绝对值超过结果集的总数，则表示获取第一行元素
	 * </p>
	 * 
	 * @param startIndex 开始行
	 * @param endIndex   结束行
	 * @param fieldList  字段名集合
	 * @return 字段组对应的结果文本集合
	 * @throws DatabaseException 若字段有误或未执行SQL或无结果集时抛出的异常
	 * @see #getResult(int, int, String...)
	 */
	public TableData<String> getResult(int startIndex, int endIndex, List<String> fieldList) {
		// 转换下标
		int length = getResultSize();
		startIndex = changeIndex(startIndex, length);
		endIndex = changeIndex(endIndex, length);

		// 添加标题，若未传入标题，则设置为空数组
		TableData<String> fieldResultTable = new TableData<>();
		fieldResultTable.addTitle(Optional.ofNullable(fieldList).orElse(new ArrayList<String>()));

		// 判断是否存在结果集，不存在则抛出异常
		ResultSet result = this.result.orElseThrow(DatabaseException::new);
		
		try {
			// 记录当前结果集光标位置，用于最后设置回当前位置
			int rowIndex = result.getRow();

			// 设置结果集光标初始位置
			result.absolute(startIndex);
			// 判断当前光标是否在最后一行之后，若不为最后一行之后，则进行结果的存储
			if (!result.isAfterLast()) {
				// 无论endIndex为何值，至少需要执行获取命令一次
				int index = startIndex;
				do {
					// 获取相应字段的内容，若字段为null，则存储为空串
					ArrayList<String> columnDataList = new ArrayList<>();
					fieldList.forEach(fieldName -> {
						try {
							columnDataList.add(Optional.ofNullable(result.getString(fieldName)).orElse(""));
						} catch (SQLException e) {
							// 若执行异常，则抛出异常
							throw new DatabaseException(String.format("“%s”字段内容无法获取", fieldName), e);
						}
					});
					
					fieldResultTable.addRow(columnDataList);
				} while ((index++) < endIndex && result.next());
			}

			// 设置结果集回到原位
			result.absolute(rowIndex);
		} catch (SQLException e) {
			// 若执行异常，则抛出异常
			throw new DatabaseException("数据库异常，结果无法返回", e);
		}

		return fieldResultTable;
	}

	/**
	 * 用于获取结果集指定行数的所有列内容，并以字符串的形式进行存储。
	 * <p>
	 * <b>注意：</b>其行下标参数从1开始计算，即传入1则表示为获取第一行数据，具体参数说明可参考{@link #getResult(int, int, List)}
	 * </p>
	 * 
	 * @param startIndex 开始行
	 * @param endIndex   结束行
	 * @return 字段组对应的结果文本集合
	 * @throws DatabaseException 若字段有误或未执行SQL或无结果集时抛出的异常
	 * @see #getResult(int, int, List)
	 */
	public TableData<String> getAllColumnResult(int startIndex, int endIndex) {
		return getResult(startIndex, endIndex, getColumnNames());
	}
	
	/**
	 * 用于获取结果集中所有的内容，并以字符串的形式进行存储。
	 * <p>
	 * <b>注意：</b>调用该方法会将数据库中所有的数据全部获取并存储至类中，
	 * 若返回数据较多时，建议使用分行获取方法{@link #getAllColumnResult(int, int)}
	 * </p>
	 * 
	 * @return 字段组对应的结果文本集合
	 * @throws DatabaseException 若字段有误或未执行SQL或无结果集时抛出的异常
	 * @see #getAllColumnResult(int, int)
	 */
	public TableData<String> getAllResult() {
		return getAllColumnResult(1, -1);
	}
	
	/**
	 * 用于关闭数据库的连接
	 */
	public void close() {
		try {
			connect.close();
		} catch (SQLException e) {
			throw new DatabaseException("数据库连接无法关闭", e);
		}
	}

	/**
	 * 用于将下标转换为正序遍历的下标
	 * 
	 * @param index  传入的下标
	 * @param length 结果集总数
	 * @return 转换后的下标
	 */
	private int changeIndex(int index, int length) {
		// 若下标为0，则直接返回1，若下标小于0，则对下标进行处理，若下标大于0，则直接返回下标
		if (index < 0) {
			// 若下标的绝对值大于结果集总数，则返回1
			if (Math.abs(index) > length) {
				return 1;
			} else {
				return (length + index) + 1;
			}
		} else if (index == 0) {
			return 1;
		} else {
			return index;
		}
	}
}
