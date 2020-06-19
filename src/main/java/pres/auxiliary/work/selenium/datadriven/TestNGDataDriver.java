package pres.auxiliary.work.selenium.datadriven;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import pres.auxiliary.tool.date.Time;

/**
 * <p><b>文件名：</b>TestNGDataDriver.java</p>
 * <p><b>用途：</b>
 * 根据文件中的数据内容返回适配TestNG的数据驱动，以便于在编写自动化脚本中使用。源数据文件支持
 * xls/xlsx/csv/txt/doc/docx，支持自定义特殊关键词，其自定义的关键词用“${关键词}”
 * 进行标记，系统根据在{@link #addFunction(String, DataFunction)}中定义的处理方式对数据进行处理。
 * 具体的用法可以参考：
 * </p>
 * <p>
 * 在类中可按照以下方法定义：
 * <pre>{@code
 * dataDriver.addFunction("test", text -> "HelloWorld");
 *dataDriver.addFunction("select(\\d+)", text -> {
 *	switch(Integer.value(text)) {
 * 		case 1:
 * 			return "function1"
 * 			break;
 * 		case 2:
 * 			return "function2"
 * 			break;
 * 	}
 *});
 * </pre>
 * 从数据驱动中读取到的数据有${test}、${select(1)}，则按照字符串的形式输出后分别得到“HelloWorld”、“function1”。
 * </p>
 * <p>
 * 除自定义函数外，构造类时将默认加载部分函数，可直接调用，详见{@link Functions}
 * </p>
 * <p><b>编码时间：</b>2020年6月3日上午7:04:51</p>
 * <p><b>修改时间：</b>2020年6月3日上午7:04:51</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 8
 *
 */
public class TestNGDataDriver {
	/**
	 * 用于存储数据列的名称
	 */
	private ArrayList<String> titleList = new ArrayList<>();
	/**
	 * 用于存储读取到的数据
	 */
	private ArrayList<Data> dataList = new ArrayList<>();
	
	/**
	 * 用于存储自定义的公式
	 */
	private HashMap<String, DataFunction> dataFunctionMap = new HashMap<>(16);

	/**
	 * 用于对日期格式或特殊字段输入的日期进行转换
	 */
	private Time time = new Time();
	
	/**
	 * 构造类，并将默认的数据驱动函数（{@link Functions}）加载至类中
	 */
	public TestNGDataDriver() {
		//构造Functions，通过反射，将所有Functions中的所有方法执行并添加至dataFunctionMap中
		Functions functions = new Functions();
		for (Method method : functions.getClass().getDeclaredMethods()) {
			try {
				addFunction((DataDriverFunction) method.invoke(functions));
			} catch (Exception e) {
			}
		}
	}
	
	/**
	 * 用于返回数据驱动中列表的元素个数，即当前数据驱动中最大行元素个数
	 * @return 列元素个数
	 */
	public int columnSize() {
		//当前添加的数据中，每一个元素都对应一个列名，故通过列名可判断最大行的元素个数
		return titleList.size();
	}
	
	/**
	 * 用于设置日期的输出格式，该方法仅对由excel读取的日期格式或由特殊字段输出的日期生效
	 */
	public void setDateFromat(String pattern) {
		time.setTimeFormat(pattern);
	}
	
	/**
	 * 用于添加自定义的公式，支持传入正则表达式，当数据驱动中的数据满足传入的正则时，则会
	 * 将其按照在公式中定义的处理方式对数据进行处理，生成对应的数据。注意，公式的返回值和
	 * 传参均为{@link String}类型
	 * @param regex 正则表达式
	 * @param function 数据处理方法
	 */
	public void addFunction(DataDriverFunction function) {
		dataFunctionMap.put(function.getRegex(), function.getFunction());
	}
	
	/**
	 * 根据传入文件格式，读取文件的内容添加至数据驱动中。该方法可用于读取xls/xlsx/csv/doc/docx/txt格式的文件，
	 * 根据不同的文件格式，其pattern参数具有不同的意义：
	 * <ol>
	 * 	<li>当读取的文件为Excel文件（xls/xlsx格式）时，其传入的pattern表示需要读取的sheet名称</li>
	 * 	<li>当读取的文件为文本文件（doc/docx/txt格式）时，其传入的pattern表示对每一行文本的切分规则，以得到一个列表</li>
	 * 	<li>当读取的文件为csv文件（csv格式）时，其传入的pattern不生效（无意义）</li>
	 * </ol>
	 * @param dataFile 源数据文件类对象
	 * @param pattern 读取规则
	 * @param isFirstTitle 首行是否为标题
	 * @throws IOException 当文件读取有误时抛出的异常
	 */
	public void addDataDriver(File dataFile, String pattern, boolean isFirstTitle) throws IOException {
		//读取文件内的数据
		ListFileRead data = new ListFileRead(dataFile, pattern);
		//由于数据按照列的形式读取，故此处将数据转置
		data.tableTransposition();
		
		//若设定首行为标题行，则根据首行元素读取标题
		if (isFirstTitle) {
			//定位到首行
			List<String> dataTitleList = data.getColumn(0);
			
			//记录当前标题的个数
			for (int i = 0; i < data.getMaxRowNumber(); i++) {
				//判断单元格是否为空或者为空串，若为空串，则存储为“数据 + 列下标”的形式
				if (dataTitleList.get(i).isEmpty()) {
					//rowSize() + 1表示在整个数据驱动中其实际下标加上1
					titleList.add("数据" + (1 + columnSize()));
				} else {
					//为避免添加多个文件时出现重复数据，此处做一个判断重复的工作，若列名重复，则添加序号
					String title = dataTitleList.get(i);
					int index = 1;
					while(titleList.contains(title)) {
						title += (++index);
					}
					titleList.add(title);
				}
			}
		}
		
		//遍历所有行，读取所有的数据
		for (int rowIndex = isFirstTitle ? 1 : 0; rowIndex < data.getMaxColumnNumber(); rowIndex++) {
			addRowData(isFirstTitle ? rowIndex - 1 : rowIndex, data.getColumn(rowIndex));
		}
	}
	
	/**
	 * 返回当前存储的所有标题
	 * @return 返回存储的标题
	 */
	public ArrayList<String> getTitle() {
		return titleList;
	}
	
	/**
	 * 返回可以使用的TestNG数据驱动。注意，数组第一维代表获取到的数据驱动的行，第二维只包含
	 * 一个{@link Data}类对象，通过该对象，对获取到的所有数据进行返回，以简化在编写脚本时
	 * 的传参
	 * 
	 * @return TestNG数据驱动
	 */
	public Object[][] getDataDriver() {
		Object[][] objs = new Object[dataList.size()][1];
		
		for (int i = 0; i < dataList.size(); i++) {
			objs[i][0] = dataList.get(i);
		}
		
		return objs;
	}
	
	/**
	 * 用于将一行的数据转为Data类对象
	 * @param rowDataList 一行数据
	 * @return 返回一行数据的Data类对象
	 */
	private void addRowData(int rowInde, List<String> rowDataList) {
		//当rowInde大于dataList的总列数时，则表示未对该行进行存储，此时存在两种情况：
		//1.第一次文件，此时dataList并无数据，调用方法时为初次添加数据，则按照正常方式添加
		//2.非第一次读取文件，此时dataList已存在上次读取的数据，当写入的数据行数大于存储的行数
		//时，直接添加数据会造成数据向左移，则需要读取原始列表的内容，将原来没有的数据补全后再
		//添加数据
		if (rowInde > dataList.size() - 1) {
			Data data = new Data();
			//判断rowDataList元素与上一行元素个数一致，若一致，则表示当前为第一次读取
			if (dataList.size() != 0 && dataList.get(rowInde - 1).dataList.size() != rowDataList.size()) {
				//若上一行的元素个数与rowDataList的元素个数不一致，则此时是非第一次读取文件，rowDataList存在数据，则
				//将rowDataList之前的列都置为空串
				int oldListSize = dataList.get(rowInde - 1).dataList.size();
				for (int index = 0; index < oldListSize - rowDataList.size(); index++) {
					data.addData("");
				}
			}
			
			//存储数据
			rowDataList.forEach(data :: addData);
			dataList.add(data);
		} else {
			rowDataList.forEach(dataList.get(rowInde) :: addData);
		}
	}

	/**
	 * <p><b>文件名：</b>TestNGDataDriver.java</p>
	 * <p><b>用途：</b>
	 * 存储从文件中读取到的数据，根据列名或列下标，通过不同的get方法，获取不同类型的数据。当传入的
	 * 列名或下标不存在时，则返回空串
	 * </p>
	 * <p><b>编码时间：</b>2020年6月3日上午7:12:20</p>
	 * <p><b>修改时间：</b>2020年6月3日上午7:12:20</p>
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
		private ArrayList<String> dataList = new ArrayList<>();
		
		/**
		 * 用于根据列名称，以字符串的形式返回数据
		 * @param listName 列名称
		 * @return 字符串形式的数据
		 */
		public String getString(String listName) {
			//判断列表是否存在，若列名不存在，则返回一个空串
			return titleList.indexOf(listName) > -1 ? getString(titleList.indexOf(listName)) : "";
		}
		
		/**
		 * 用于根据列编号，以字符串的形式返回数据
		 * @param index 列下标
		 * @return 字符串形式的数据
		 */
		public String getString(int index) {
			return index < dataList.size() ? disposeContent(dataList.get(index)) : "";
		}
		
		/**
		 * 用于根据列名称，以double的形式对数据进行返回
		 * @param listName 列表名称
		 * @return double类型的数据
		 * @throw NumberFormatException 数据无法转时抛出的异常
		 */
		public double getDouble(String listName) {
			return Double.valueOf(getString(listName));
		}
		
		/**
		 * 用于根据列编号，以double的形式对数据进行返回
		 * @param index 列下标
		 * @return double类型的数据
		 * @throw NumberFormatException 数据无法转时抛出的异常
		 */
		public double getDouble(int index) {
			return Double.valueOf(getString(index));
		}
		
		/**
		 * 用于根据列名称，以int的形式对数据进行返回
		 * @param listName 列表名称
		 * @return int类型的数据
		 * @throw NumberFormatException 数据无法转换时抛出的异常
		 */
		public int getInt(String listName) {
			return Integer.valueOf(getString(listName));
		}
		
		/**
		 * 用于根据列编号，以int的形式对数据进行返回
		 * @param index 列下标
		 * @return int类型的数据
		 * @throw NumberFormatException 数据无法转时抛出的异常
		 */
		public int getInt(int index) {
			return Integer.valueOf(getString(index));
		}
		
		/**
		 * 用于根据列名称，以long的形式对数据进行返回
		 * @param listName 列表名称
		 * @return long类型的数据
		 * @throw NumberFormatException 数据无法转换时抛出的异常
		 */
		public long getLong(String listName) {
			return Long.valueOf(getString(listName));
		}
		
		/**
		 * 用于根据列编号，以long的形式对数据进行返回
		 * @param index 列下标
		 * @return long类型的数据
		 * @throw NumberFormatException 数据无法转时抛出的异常
		 */
		public long getLong(int index) {
			return Long.valueOf(getString(index));
		}
		
		/**
		 * 用于根据列名称，以boolean的形式对数据进行返回
		 * @param listName 列表名称
		 * @return boolean类型的数据
		 */
		public boolean getBoolean(String listName) {
			return Boolean.valueOf(getString(listName));
		}
		
		/**
		 * 用于根据列编号，以boolean的形式对数据进行返回
		 * @param index 列下标
		 * @return boolean类型的数据
		 */
		public boolean getBoolean(int index) {
			return Boolean.valueOf(getString(index));
		}
		
		/**
		 * 用于根据列编号，以{@link File}的形式对数据进行返回
		 * @param index 列下标
		 * @return {@link File}类型的数据
		 */
		public File getFile(int index) {
			return new File(getString(index));
		}
		
		/**
		 * 用于根据列名称，以{@link File}的形式对数据进行返回
		 * @param listName 列表名称
		 * @return {@link File}类型的数据
		 */
		public File getFile(String listName) {
			return new File(getString(listName));
		}
		
		/**
		 * 用于根据列编号，以{@link Time}的形式对数据进行返回
		 * @param index 列下标
		 * @return {@link Time}类型的数据
		 */
		public Time getTime(int index) {
			return new Time(getString(index));
		}
		
		/**
		 * 用于根据列名称，以{@link Time}的形式对数据进行返回
		 * @param listName 列表名称
		 * @return {@link Time}类型的数据
		 */
		public Time getTime(String listName) {
			return new Time(getString(listName));
		}
		
		/**
		 * 用于添加元素
		 * @param content 元素内容
		 */
		private void addData(String content) {
			dataList.add(disposeContent(content));
		}
		
		/**
		 * 用于处理读取得到的内容，并将处理后的内容进行返回
		 * @param content 读取的内容
		 * @return 处理后的内容
		 */
		private String disposeContent(String content) {
			//判断传入的内容是否包含公式起始符号，若不包含，则返回原串
			if (content.indexOf(FUNCTION_SIGN_START) < 0) {
				return content;
			}
			
			//存储处理后的内容
			StringBuilder newContent = new StringBuilder();
			
			//按照公式起始符号对内容进行切分，遍历切分后的每一个元素
			String[] texts = content.split(SPLIT_START);
			for (int i = 0; i < texts.length; i++) {
				//判断切分的元素是否包含公式结束符号
				int index;
				if ((index = texts[i].indexOf(FUNCTION_SIGN_END)) > -1) {
					//截取公式串，处理公式
					String disposeText = functionToData(texts[i].substring(0, index));
					//判断处理后得到字符串是否为null，不为null，则拼接至newContent中
					if (disposeText != null) {
						newContent.append(disposeText + texts[i].substring(index + 1));
					} else {
						newContent.append(i == 0 ? texts[i] : (FUNCTION_SIGN_START + texts[i]));
					}
				} else {
					//若不包含公式结束符号，则直接拼接原串
					newContent.append(i == 0 ? texts[i] : (FUNCTION_SIGN_START + texts[i]));
				}
			}
			
			//若原串中最后一个元素是公式结束标记，则将该标记拼接至新串的后面
			if (content.lastIndexOf(FUNCTION_SIGN_START) + FUNCTION_SIGN_START.length() == content.length()) {
				newContent.append(FUNCTION_SIGN_START);
			}
			
			return newContent.toString();
		}
		
		/**
		 * 用于对数据驱动中的公式进行处理，返回处理得到的数据，若数据不予存储的任何正则匹配，则
		 * 返回空串
		 * @param content 内容
		 * @return 处理后的内容
		 */
		private String functionToData(String content) {
			//遍历所有的转译公式
			for (String regex : dataFunctionMap.keySet()) {
				//若传入的内容与正则匹配，则将数据进行处理，并返回处理结果
				if (Pattern.compile(regex).matcher(content).matches()) {
					return dataFunctionMap.get(regex).apply(content);
				}
			}
			
			//若内容不与任何正则匹配，则返回原始内容
			return null;
		}
	} 
}
