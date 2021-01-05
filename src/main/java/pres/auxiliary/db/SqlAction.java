package pres.auxiliary.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import pres.auxiliary.tool.data.TableData;

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
 * <pre></code> 其结果输出为：<br>
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
	protected Optional<Connection> connect = Optional.empty();
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
	 * @param username 用户名
	 * @param password 密码
	 * @param host     主机（包括端口）
	 * @param dataBaseName 数据源
	 */
	public SqlAction(DataBaseType dataBaseType, String username, String password, String host, String dataBaseName) {
		try {
			Class.forName(dataBaseType.getClassName());
			connect = Optional.ofNullable(DriverManager
					.getConnection(String.format(dataBaseType.getUrl(), host, dataBaseName), username, password));
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

		// 若无法获取Connection类对象，则抛出异常
		Connection connect = this.connect.orElseThrow(() -> new DatabaseException("数据库连接异常"));

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
	public ResultSet getResult() {
		return result.orElseThrow(DatabaseException::new);
	}

	/**
	 * 用于返回当前结果集中的元素个数
	 * 
	 * @return 当前结果集中元素个数
	 */
	public int getResultSize() {
		int size = -1;
		ResultSet result = getResult();
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
	 * 用于返回结果集中第一条数据，即第一行第一列的数据。若结果集中无任何数据，则返回空串
	 * 
	 * @return 结果集第一条数据
	 * @throws DatabaseException 若字段有误或未执行SQL或无结果集时抛出的异常
	 */
	public String getFirstResult() {
		// 返回字符串，若搜索结果无对应行，则返回空串
		try {
			return getFirstRowResult(1, 1).get(0);
		} catch (IndexOutOfBoundsException e) {
			return "";
		}
	}

	/**
	 * 用于以字符串集合的形式返回结果集第一列指定行的结果
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
	 * @param startIndex 起始下标
	 * @param endIndex   结束下标
	 * @return 第一列指定行的元素集合
	 * @throws DatabaseException 若字段有误或未执行SQL或无结果集时抛出的异常
	 */
	public ArrayList<String> getFirstRowResult(int startIndex, int endIndex) {
		return getRowResult(fieldNameList.get(0), startIndex, endIndex);
	}

	/**
	 * 用于以字符串集合的形式返回结果集指定列及指定行的结果，其下标规则可 参考{@link #getFirstRowResult(int, int)}方法
	 * 
	 * @param fieldName  字段名称
	 * @param startIndex 起始下标
	 * @param endIndex   结束下标
	 * @return 第一列指定行的元素集合
	 * @throws DatabaseException 若字段有误或未执行SQL或无结果集时抛出的异常
	 * @see #getFirstRowResult(int, int)
	 */
	public ArrayList<String> getRowResult(String fieldName, int startIndex, int endIndex) {
		ArrayList<String> resultList = new ArrayList<>(getResult(startIndex, endIndex, fieldName)
				.getColumnList(fieldName).stream()
				.map(data -> data.orElse(""))
				.collect(Collectors.toList())
				);
		return resultList;
	}

	/**
	 * <p>
	 * 用于以字符串集合的形式返回结果集指定列及指定行的结果，其下标规则可 参考{@link #getFirstRowResult(int, int)}方法。
	 * </p>
	 * <p>
	 * <b>注意：</b>字段下标与结果集列下标的起始位置不同，字段下标的第1位，其下标为0，
	 * 即调用{@code getRowResult(1, 1, 2)}方法时，表示获取第1列，第1行到第2行的数据
	 * </p>
	 * 
	 * @param fieldIndex  列下标
	 * @param startIndex 起始下标
	 * @param endIndex   结束下标
	 * @return 第一列指定行的元素集合
	 * @throws DatabaseException 若字段有误或未执行SQL或无结果集时抛出的异常
	 * @see #getFirstRowResult(int, int)
	 */
	public ArrayList<String> getRowResult(int fieldIndex, int startIndex, int endIndex) {
		return getRowResult(fieldNameList.get(fieldIndex), startIndex, endIndex);
	}

	/**
	 * 用于根据表中的字段名，获取结果集指定行数的内容，并以字符串的形式进行存储。
	 * <p>
	 * <b>注意：</b>下标从1开始，可以传入负数，表示从后遍历
	 * </p>
	 * 
	 * @param startIndex 开始行
	 * @param endIndex   结束行
	 * @param fieldNames 字段组，可传入多个值
	 * @return 字段组对应的结果文本集合
	 * @throws DatabaseException 若字段有误或未执行SQL或无结果集时抛出的异常
	 */
	private TableData<String> getResult(int startIndex, int endIndex, String... fieldNames) {
		// 转换下标
		int length = getResultSize();
		startIndex = changeIndex(startIndex, length);
		endIndex = changeIndex(endIndex, length);

		// 添加标题，若未传入标题，则设置为空数组
		TableData<String> fieldResultTable = new TableData<>();
		fieldResultTable.addTitle(Arrays.asList(Optional.ofNullable(fieldNames).orElse(new String[] {})));

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
					Arrays.stream(fieldNames).forEach(fieldName -> {
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
