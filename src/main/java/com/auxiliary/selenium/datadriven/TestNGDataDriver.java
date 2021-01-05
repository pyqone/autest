package com.auxiliary.selenium.datadriven;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import com.auxiliary.tool.data.ListUtil;
import com.auxiliary.tool.data.TableData;
import com.auxiliary.tool.date.Time;
import com.auxiliary.tool.file.TableFileReadUtil;

/**
 * <p>
 * <b>文件名：</b>TestNGDataDriver.java
 * </p>
 * <p>
 * <b>用途：</b> 根据文件中的数据内容返回适配TestNG的数据驱动，以便于在编写自动化脚本中使用。源数据文件支持
 * xls/xlsx/csv/txt/doc/docx，支持自定义特殊关键词，其自定义的关键词用“${关键词}”
 * 进行标记，系统根据在{@link #addFunction(DataDriverFunction)}中定义的处理方式对数据进行处理。 具体的用法可以参考：
 * </p>
 * <p>
 * 在类中可按照以下方法定义：
 * 
 * <pre>
 * <code>
 * dataDriver.addFunction("test", text -&gt; "HelloWorld");
 * dataDriver.addFunction("select(\\d+)", text -&gt; {
 *	switch(Integer.value(text)) {
 * 		case 1:
 * 			return "function1"
 * 			break;
 * 		case 2:
 * 			return "function2"
 * 			break;
 * 	}
 * });
 * </code>
 * </pre>
 * 
 * 从数据驱动中读取到的数据有${test}、${select(1)}，则按照字符串的形式输出后分别得到“HelloWorld”、“function1”。
 * </p>
 * <p>
 * 除自定义函数外，构造类时将默认加载部分函数，可直接调用，详见{@link Functions}
 * </p>
 * <p>
 * <b>编码时间：</b>2020年6月3日上午7:04:51
 * </p>
 * <p>
 * <b>修改时间：</b>2020年6月3日上午7:04:51
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 8
 *
 */
public class TestNGDataDriver {
	/**
	 * 用于存储数据列的名称
	 */
//	private ArrayList<String> titleList = new ArrayList<>();
	/**
	 * 用于存储读取到的数据
	 */
//	private ArrayList<Data> dataList = new ArrayList<>();
//	private HashMap<String, List<Object>> dataMap = new HashMap<>(16);
	private TableData<Object> dataTable;

	/**
	 * 用于存储自定义的公式
	 */
	private HashMap<String, DataFunction> dataFunctionMap = new HashMap<>(16);

	/**
	 * 用于对日期格式或特殊字段输入的日期进行转换
	 */
	private Time time = new Time();

	/**
	 * 用于存储最大列的元素个数
	 */
//	private int maxColumnSize = -1;

	/**
	 * 构造类，并将默认的数据驱动函数（{@link Functions}）加载至类中
	 */
	public TestNGDataDriver() {
		// 构造Functions，通过反射，将所有Functions中的所有方法执行并添加至dataFunctionMap中
		Functions functions = new Functions();
		for (Method method : functions.getClass().getDeclaredMethods()) {
			try {
				addFunction((DataDriverFunction) method.invoke(functions));
			} catch (Exception e) {
			}
		}

		dataTable = new TableData<>();
	}

	/**
	 * 用于返回数据驱动中列表的元素个数，即当前数据驱动中最大行元素个数
	 * 
	 * @return 列元素个数
	 */
	public int getColumnSize() {
		return dataTable.getColumnName().size();
	}

	/**
	 * 用于设置日期的输出格式，该方法仅对由excel读取的日期格式或由特殊字段输出的日期生效
	 */
	public void setDateFromat(String pattern) {
		time.setTimeFormat(pattern);
	}

	/**
	 * 用于添加自定义的公式，支持传入正则表达式，当数据驱动中的数据满足传入的正则时，则会
	 * 将其按照在公式中定义的处理方式对数据进行处理，生成对应的数据。注意，公式的返回值和 传参均为{@link String}类型
	 * 
	 * @param function 数据处理方法
	 */
	public void addFunction(DataDriverFunction function) {
		dataFunctionMap.put(function.getRegex(), function.getFunction());
	}

	/**
	 * 根据传入文件格式，读取文件的内容添加至数据驱动中。该方法可用于读取xls/xlsx/csv/doc/docx/txt格式的文件，
	 * 根据不同的文件格式，其pattern参数具有不同的意义：
	 * <ol>
	 * <li>当读取的文件为Excel文件（xls/xlsx格式）时，其传入的pattern表示需要读取的sheet名称</li>
	 * <li>当读取的文件为文本文件（doc/docx/txt格式）时，其传入的pattern表示对每一行文本的切分规则，以得到一个列表</li>
	 * <li>当读取的文件为csv文件（csv格式）时，其传入的pattern不生效（无意义）</li>
	 * </ol>
	 * 
	 * @param dataFile     源数据文件类对象
	 * @param pattern      读取规则
	 * @param isFirstTitle 首行是否为标题
	 * @throws IOException 当文件读取有误时抛出的异常
	 */
	public void addDataDriver(File dataFile, String pattern, boolean isFirstTitle) {
		// 读取文件内的数据
		dataTable.addTable(ListUtil.changeTable(TableFileReadUtil.readFile(dataFile, pattern, isFirstTitle),
				data -> (Object) data));
	}

	/**
	 * 用于在非文件中添加数据驱动
	 * 
	 * @param title   数据列标题
	 * @param objects 相应的元素
	 */
	public void addDataDriver(String title, Object... objects) {
		dataTable.addColumn(Optional.ofNullable(title).orElse("数据列" + getColumnSize()),
				Arrays.asList(Optional.ofNullable(objects).orElse(new Object[] {})));
	}

	/**
	 * 返回可以使用的TestNG数据驱动。注意，数组第一维代表获取到的数据驱动的行，第二维只包含
	 * 一个{@link Data}类对象，通过该对象，对获取到的所有数据进行返回，以简化在编写脚本时 的传参
	 * 
	 * @return TestNG形式的数据驱动
	 */
	public Object[][] getDataDriver() {
		// 构造Object[][]
		Object[][] objs = new Object[dataTable.getLongColumnSize()][1];

		// 用于根据下标将数据添加至objs
		AtomicInteger index = new AtomicInteger(0);
		// 按行添加数据
		dataTable.rowStream().map(Data::new).forEach(data -> objs[index.getAndAdd(1)][0] = data);

		return objs;
	}

	/**
	 * 用于以{@link TableData}表类对象的形式返回当前读取的数据驱动
	 * 
	 * @return {@link TableData}表类对象
	 */
	public TableData<Object> getTable() {
		return dataTable;
	}

	/**
	 * <p>
	 * <b>文件名：</b>TestNGDataDriver.java
	 * </p>
	 * <p>
	 * <b>用途：</b> 存储从文件中读取到的数据，根据列名或列下标，通过不同的get方法，获取不同类型的数据。当传入的 列名或下标不存在时，则返回空串
	 * </p>
	 * <p>
	 * <b>编码时间：</b>2020年6月3日上午7:12:20
	 * </p>
	 * <p>
	 * <b>修改时间：</b>2020年6月3日上午7:12:20
	 * </p>
	 * 
	 * @author 彭宇琦
	 * @version Ver1.0
	 *
	 */
	public class Data {
		/**
		 * 切分公式标记
		 */
		private final String SPLIT_START = "\\$\\{";

		/**
		 * 公式起始标记
		 */
		private final String FUNCTION_SIGN_START = "${";
		/**
		 * 公式结束标记
		 */
		private final String FUNCTION_SIGN_END = "}";

		/**
		 * 用于存储数据
		 */
		private List<Optional<Object>> dataList;

		/**
		 * 构造方法，初始化一组数据
		 * 
		 * @param dataList 数据集合
		 */
		public Data(List<Optional<Object>> dataList) {
			this.dataList = dataList;
		}

		/**
		 * 用于根据列名称，以字符串的形式返回数据
		 * 
		 * @param listName 列名称
		 * @return 字符串形式的数据
		 */
		public String getString(String listName) {
			// 判断列表是否存在，若列名不存在，则返回一个空串
			return dataList.get(dataTable.getFieldIndex(listName)).map(data -> data.toString())
					.map(this::disposeContent).orElse("");
		}

		/**
		 * 用于根据列编号，以字符串的形式返回数据
		 * 
		 * @param index 列下标
		 * @return 字符串形式的数据
		 */
		public String getString(int index) {
			return getString(indexToFieldName(index));
		}

		/**
		 * 用于根据列名称，以double的形式对数据进行返回
		 * 
		 * @param listName 列表名称
		 * @return double类型的数据
		 */
		public double getDouble(String listName) {
			return Double.valueOf(getString(listName));
		}

		/**
		 * 用于根据列编号，以double的形式对数据进行返回
		 * 
		 * @param index 列下标
		 * @return double类型的数据
		 */
		public double getDouble(int index) {
			return Double.valueOf(getString(index));
		}

		/**
		 * 用于根据列名称，以int的形式对数据进行返回
		 * 
		 * @param listName 列表名称
		 * @return int类型的数据
		 */
		public int getInt(String listName) {
			return Integer.valueOf(getString(listName));
		}

		/**
		 * 用于根据列编号，以int的形式对数据进行返回
		 * 
		 * @param index 列下标
		 * @return int类型的数据
		 */
		public int getInt(int index) {
			return Integer.valueOf(getString(index));
		}

		/**
		 * 用于根据列名称，以long的形式对数据进行返回
		 * 
		 * @param listName 列表名称
		 * @return long类型的数据
		 */
		public long getLong(String listName) {
			return Long.valueOf(getString(listName));
		}

		/**
		 * 用于根据列编号，以long的形式对数据进行返回
		 * 
		 * @param index 列下标
		 * @return long类型的数据
		 */
		public long getLong(int index) {
			return Long.valueOf(getString(index));
		}

		/**
		 * 用于根据列名称，以boolean的形式对数据进行返回
		 * 
		 * @param listName 列表名称
		 * @return boolean类型的数据
		 */
		public boolean getBoolean(String listName) {
			return Boolean.valueOf(getString(listName));
		}

		/**
		 * 用于根据列编号，以boolean的形式对数据进行返回
		 * 
		 * @param index 列下标
		 * @return boolean类型的数据
		 */
		public boolean getBoolean(int index) {
			return Boolean.valueOf(getString(index));
		}

		/**
		 * 用于根据列编号，以{@link File}的形式对数据进行返回
		 * 
		 * @param index 列下标
		 * @return {@link File}类型的数据
		 */
		public File getFile(int index) {
			return new File(getString(index));
		}

		/**
		 * 用于根据列名称，以{@link File}的形式对数据进行返回
		 * 
		 * @param listName 列表名称
		 * @return {@link File}类型的数据
		 */
		public File getFile(String listName) {
			return new File(getString(listName));
		}

		/**
		 * 用于根据列编号，以{@link Time}的形式对数据进行返回
		 * 
		 * @param index 列下标
		 * @return {@link Time}类型的数据
		 */
		public Time getTime(int index) {
			return new Time(getString(index));
		}

		/**
		 * 用于根据列名称，以{@link Time}的形式对数据进行返回
		 * 
		 * @param listName 列表名称
		 * @return {@link Time}类型的数据
		 */
		public Time getTime(String listName) {
			return new Time(getString(listName));
		}

		/**
		 * 用于根据列名称，以{@link Object}的形式对数据进行返回
		 * 
		 * @param listName 列表名称
		 * @return {@link Object}类型的数据
		 */
		public Object getObject(String listName) {
			return dataList.get(dataTable.getFieldIndex(listName))
					.orElseThrow(() -> new DataNotFoundException(String.format("“%s”列数据为null，无法返回", listName)));
		}

		/**
		 * 用于根据列名称，以{@link Object}的形式对数据进行返回
		 * 
		 * @param index 列表下标
		 * @return {@link Object}类型的数据
		 */
		public Object getObject(int index) {
			return getObject(indexToFieldName(index));
		}

		/**
		 * 用于处理读取得到的内容，并将处理后的内容进行返回
		 * 
		 * @param content 读取的内容
		 * @return 处理后的内容
		 */
		private String disposeContent(String content) {
			// TODO 想办法在处理时输出Object类
			// 判断传入的内容是否包含公式起始符号，若不包含，则返回原串
			if (content.indexOf(FUNCTION_SIGN_START) < 0) {
				return content;
			}

			// 存储处理后的内容
			StringBuilder newContent = new StringBuilder();

			// 按照公式起始符号对内容进行切分，遍历切分后的每一个元素
			String[] texts = content.split(SPLIT_START);
			for (int i = 0; i < texts.length; i++) {
				// 判断切分的元素是否包含公式结束符号
				int index;
				if ((index = texts[i].indexOf(FUNCTION_SIGN_END)) > -1) {
					// 截取公式串，处理公式
					String disposeText = functionToData(texts[i].substring(0, index));
					// 判断处理后得到字符串是否为null，不为null，则拼接至newContent中
					if (disposeText != null) {
						newContent.append(disposeText + texts[i].substring(index + 1));
					} else {
						newContent.append(i == 0 ? texts[i] : (FUNCTION_SIGN_START + texts[i]));
					}
				} else {
					// 若不包含公式结束符号，则直接拼接原串
					newContent.append(i == 0 ? texts[i] : (FUNCTION_SIGN_START + texts[i]));
				}
			}

			// 若原串中最后一个元素是公式结束标记，则将该标记拼接至新串的后面
			if (content.lastIndexOf(FUNCTION_SIGN_START) + FUNCTION_SIGN_START.length() == content.length()) {
				newContent.append(FUNCTION_SIGN_START);
			}

			return newContent.toString();
		}

		/**
		 * 用于对数据驱动中的公式进行处理，返回处理得到的数据，若数据不予存储的任何正则匹配，则 返回空串
		 * 
		 * @param content 内容
		 * @return 处理后的内容
		 */
		private String functionToData(String content) {
			// 遍历所有的转译公式
			for (String regex : dataFunctionMap.keySet()) {
				// 若传入的内容与正则匹配，则将数据进行处理，并返回处理结果
				if (Pattern.compile(regex).matcher(content).matches()) {
					return dataFunctionMap.get(regex).apply(content);
				}
			}

			// 若内容不与任何正则匹配，则返回原始内容
			return null;
		}
		
		/**
		 * 用于根据列下标返回列名称，若列下标对应的列不存在，则抛出异常
		 * @param index 列下标
		 * @return 下标对应的列名
		 * @throws DataNotFoundException 列不存在时抛出的异常
		 */
		private String indexToFieldName(int index) {
			return Optional.ofNullable(dataTable.getFieldName(index)).filter(name -> !name.isEmpty())
					.orElseThrow(() -> new DataNotFoundException("列下标不存在，无法返回数据"));
		}
	}
}
