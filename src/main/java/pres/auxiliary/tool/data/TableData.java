package pres.auxiliary.tool.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import pres.auxiliary.work.selenium.element.DataListBy;

/**
 * <p>
 * <b>文件名：</b>TableData.java
 * </p>
 * <p>
 * <b>用途：</b> 提供对列表型数据进行处理的方法。添加列元素时，可设置是否检查数据量的一致性，若打开该设置，则
 * 在返回元素时，当存在列元素数据个数与其他列不一致时，则会抛出一个异常。
 * </p>
 * <p>
 * <b>编码时间：</b>2020年12月17日上午8:24:47
 * </p>
 * <p>
 * <b>修改时间：</b>2020年12月17日上午8:24:47
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @param <T> 列元素的元素类型
 *
 */
public class TableData<T> {
	/**
	 * 存储列表元素数据
	 */
	protected LinkedHashMap<String, ArrayList<Optional<T>>> tableMap = new LinkedHashMap<>(16);
	/**
	 * 指向列表中最短列数据个数
	 */
	protected int shortColumnSize = Integer.MAX_VALUE;
	/**
	 * 指向列表中最长列数据个数
	 */
	protected int longColumnSize = -1;

	/**
	 * 定义添加列时是否严格检查的开关
	 */
	private boolean isExamine = true;

	/**
	 * 构造对象
	 */
	public TableData() {
	}

	/**
	 * 构造对象，并初始化列表的数据
	 * 
	 * @param tableMap 列表数据
	 */
	public TableData(Map<String, ArrayList<T>> tableMap) {
		tableMap.forEach(this::addColumn);
	}

	/**
	 * 用于设置是否对传入的元素列表的个数进行严格校验，即在调用{@link #addList(DataListBy)}方法时，
	 * 若元素个数与初次传入的个数不符且需要严格校验，则抛出异常；反之，则直接进行存储
	 * 
	 * @param isExamine 是否严格校验元素个数
	 */
	public void setExamine(boolean isExamine) {
		this.isExamine = isExamine;
	}

	/**
	 * 用于根据列表名称，存储一列元素数据，多次调用该方法时，将在相应列后继续添加数据
	 * 
	 * @param columnName     列表名称
	 * @param columnDataList 数据集合
	 * @return 类本身
	 * @throws IllegalDataException 传入元素集合为null时抛出的异常
	 */
	public TableData<T> addColumn(String columnName, List<T> columnDataList) {
		// 判断列表名称是否正确传入
		columnName = Optional.ofNullable(columnName).filter(text -> !text.isEmpty())
				.orElseThrow(() -> new IllegalDataException("必须指定列名称"));
		// 判断列表名称是否未存储，未存储，则添加相应的集合
		if (!tableMap.containsKey(columnName)) {
			tableMap.put(columnName, new ArrayList<Optional<T>>());
		}

		// 扫描数据，将数据添加至tableMap中
		tableMap.get(columnName)
				.addAll(Optional.ofNullable(columnDataList).orElseThrow(() -> new IllegalDataException("元素集合不能为null"))
						.stream().map(Optional::ofNullable).collect(Collectors.toList()));

		// 计算最长/最短列元素个数
		refreshLength();

		return this;
	}

	/**
	 * 用于向表中添加一列列名，若列名重复，则不进行添加
	 * 
	 * @param columnNameList 列名集合
	 * @return 类本身
	 * @throws IllegalDataException 未添加类名称时抛出的异常
	 */
	public TableData<T> addTitle(List<String> columnNameList) {
		Optional.ofNullable(columnNameList).filter(list -> !list.isEmpty())
				.orElseThrow(() -> new IllegalDataException("必须指定列名称"))
				// 筛选名称不为空，且不在tableMap中的内容进行添加
				.stream().filter(title -> !title.isEmpty())
				.filter(title -> !tableMap.containsKey(title))
				.forEach(title -> {
					tableMap.put(title, new ArrayList<Optional<T>>());
					// 刷新列长度
					refreshLength();
				});

		return this;
	}

	/**
	 * 用于向表中添加一个列名，若列名重复，则不进行添加
	 * 
	 * @param columnName 列名名称
	 * @return 类本身
	 * @throws IllegalDataException 未添加类名称时抛出的异常
	 */
	public TableData<T> addTitle(String columnName) {
		return addTitle(Arrays.asList(
				Optional.ofNullable(columnName).filter(titleText -> !tableMap.containsKey(titleText)).orElse("")));
	}

	/**
	 * 按照行存储元素，若元素个数少于当前表中存储的元素个数时，则使用{@link Optional#empty()}代替；若元素
	 * 个数超出当前表中存储的元素个数时，则抛出异常
	 * 
	 * @param rowDataList 行数据集合
	 * @return 类本身
	 * @throws IllegalDataException 未存储列名时或添加的元素超出列数量时抛出的异常
	 */
	public TableData<T> addRow(List<T> rowDataList) {
		// 判断是否存储数据
		if (tableMap.isEmpty()) {
			throw new IllegalDataException("未存储列名，无法按行添加元素");
		}

		
		List<T> rowList = Optional.ofNullable(rowDataList).filter(list -> !list.isEmpty()).orElse(new ArrayList<T>());
		//判断需要存储的元素个数是否为空，为空则结束方法
		if (rowList.size() != 0) {
			// 判断传入的行元素个数是否超出当前表中存储的列数，超出，则抛出异常
			if (rowList.size() > tableMap.size()) {
				throw new IllegalDataException(
						String.format("指定的行元素超出标题个数。当前传入数据个数：%d，表中列个数：%d", rowDataList.size(), tableMap.size()));
			}
	
			// 按照列添加数据，补全的列，按照空元素进行存储
			AtomicInteger index = new AtomicInteger(0);
			tableMap.forEach((key, value) -> {
				try {
					value.add(Optional.ofNullable(rowList.get(index.getAndAdd(1))));
				} catch (IndexOutOfBoundsException e) {
					value.add(Optional.empty());
				}
			});
			
			//由于每列数据均会增加，故最短与最长列的数据量均加上1
			shortColumnSize++;
			longColumnSize++;
		}

		return this;
	}

	/**
	 * 返回指定列的数据个数
	 * 
	 * @param columnName 列名称
	 * @return 数据个数
	 */
	public int getListSize(String columnName) {
		return getColumnList(columnName).size();
	}

	/**
	 * 返回列表中最长列的数据个数
	 * 
	 * @return 数据个数
	 */
	public int getLongColumnSize() {
		return longColumnSize;
	}

	/**
	 * 返回列表中最短列的数据个数
	 * 
	 * @return 数据个数
	 */
	public int getShortColumnSize() {
		return shortColumnSize;
	}

	/**
	 * 用于返回列名称
	 * 
	 * @return 列名称
	 */
	public ArrayList<String> getColumnName() {
		ArrayList<String> columnNameList = new ArrayList<>();
		tableMap.forEach((key, value) -> columnNameList.add(key));

		return columnNameList;
	}

	/**
	 * 用于清空指定列的数据集合，但不删除该列
	 * 
	 * @param columnName 列名称
	 * @return 被清空的列数据集合
	 * @throws IllegalDataException 列名称不存在或未传入时抛出的异常
	 */
	public ArrayList<Optional<T>> clearColumn(String columnName) {
		// 存储待清空的集合
		ArrayList<Optional<T>> columnDataList = getColumnList(columnName);

		// 清空列指向的集合
		tableMap.get(columnName).clear();
		// 刷新列长度
		refreshLength();

		return columnDataList;
	}

	/**
	 * 用于移除指定列的数据集合
	 * 
	 * @param columnName 列名称
	 * @return 被移除的列数据集合
	 * @throws IllegalDataException 列名称不存在或未传入时抛出的异常
	 */
	public ArrayList<Optional<T>> removeColumn(String columnName) {
		// 存储待清空的集合
		ArrayList<Optional<T>> columnDataList = getColumnList(columnName);

		// 清空列指向的集合
		tableMap.remove(columnName);
		// 刷新列表最长/最短列数据个数
		refreshLength();

		return columnDataList;
	}

	/**
	 * 用于根据列下标返回列字段的名称，下标从0开始计算，即0表示第1列。 若下标小于1或大于当前表中列的个数时，则返回空串
	 * 
	 * @param index 列下标
	 * @return 字段名称
	 */
	public String getFieldName(int index) {
		if (index < 0) {
			return "";
		}

		ArrayList<String> fieldNameList = getColumnName();

		if (index > fieldNameList.size() - 1) {
			return "";
		}

		return fieldNameList.get(index);
	}
	
	/**
	 * 用于返回指定列的所有数据
	 * 
	 * @param columnName 列名称
	 * @return 指定列的所有数据
	 */
	public ArrayList<Optional<T>> getColumnList(String columnName) {
		columnName = Optional.ofNullable(columnName).filter(tableMap::containsKey)
				.orElseThrow(() -> new IllegalDataException("不存在的元素列"));

		ArrayList<Optional<T>> columnDataList = new ArrayList<>();
		columnDataList.addAll(tableMap.get(columnName));

		return columnDataList;
	}
	
	/**
	 * 用于返回第一列的所有数据
	 * @return 第一列的所有数据
	 */
	public ArrayList<Optional<T>> getFirstColumn() {
		return getColumnList(getFieldName(0));
	}
	
	/**
	 * 用于获取第一列第一条数据
	 * @return 第一列第一条数据
	 */
	public Optional<T> getFirstData() {
		return getFirstColumn().get(0);
	}

	/**
	 * 用于获取指定列与指定列的数据，并以传入的字段顺序，将获取到的每列数据进行存储
	 * <p>
	 * 方法接收数据的起始下标与结束下标，并根据该组下标，获取结果，将其转换为
	 * 字符串的形式进行返回，另外，行下标从1开始遍历，下标传入0或者1都表示获取第1行元素，且下标允许传入负数， 表示反序遍历。其可能会出现以下情况：
	 * <ol>
	 * <li>起始下标大于数据总数：返回空集合</li>
	 * <li>起始下标大于结束下标：只获取起始行的数据</li>
	 * <li>起始下标等于结束下标：获取当前行的数据</li>
	 * <li>起始下标小于结束下标：获取相应多行的数据</li>
	 * </ol>
	 * </p>
	 * <p>
	 * <b>注意：</b>调用方法后，下标传入的负数实则会根据数据的个数，转换为一个正数
	 * 进行传参，即假设数据中有100个元素，下标传入-2时，会将其转换为99（倒数第二行）进行处理。
	 * 若负数下标的绝对值超过数据的总数，则表示获取第一行元素。另外，获取数据的数据采用包含两边的形式
	 * 存储数据，及传入起始下标为2，结束下标为4时，表示获取指定列2-4行的数据
	 * </p>
	 * 
	 * @param startRowIndex 起始下标
	 * @param endRowIndex   结束下标
	 * @param columnNames   列名称组，可传入多个值
	 * @return 按照字段顺序获取的列数据
	 * @throws IllegalDataException 需要严格检查且存在列数据不同时抛出的异常
	 */
	public LinkedHashMap<String, ArrayList<Optional<T>>> getData(int startRowIndex, int endRowIndex,
			String... columnNames) {
		return getData(startRowIndex, endRowIndex,
				Arrays.stream(Optional.ofNullable(columnNames).orElseThrow(() -> new IllegalDataException("未传入数据列")))
						.filter(tableMap::containsKey).collect(Collectors.toList()));
	}

	/**
	 * 用于获取指定列与指定列的数据，并以传入的字段顺序，将获取到的每列数据进行存储
	 * <p>
	 * 方法接收数据的起始下标与结束下标，并根据该组下标，获取结果，将其转换为
	 * 字符串的形式进行返回，另外，行下标从1开始遍历，下标传入0或者1都表示获取第1行元素，且下标允许传入负数， 表示反序遍历。其可能会出现以下情况：
	 * <ol>
	 * <li>起始下标大于数据总数：返回空集合</li>
	 * <li>起始下标大于结束下标：只获取起始行的数据</li>
	 * <li>起始下标等于结束下标：获取当前行的数据</li>
	 * <li>起始下标小于结束下标：获取相应多行的数据</li>
	 * </ol>
	 * </p>
	 * <p>
	 * <b>注意：</b>调用方法后，下标传入的负数实则会根据数据的个数，转换为一个正数
	 * 进行传参，即假设数据中有100个元素，下标传入-2时，会将其转换为99（倒数第二行）进行处理。
	 * 若负数下标的绝对值超过数据的总数，则表示获取第一行元素。另外，获取数据的数据采用包含两边的形式
	 * 存储数据，及传入起始下标为2，结束下标为4时，表示获取指定列2-4行的数据
	 * </p>
	 * 
	 * @param startRowIndex 起始下标
	 * @param endRowIndex   结束下标
	 * @param columnNames   列名称组，可传入多个值
	 * @return 按照字段顺序获取的列数据
	 * @throws IllegalDataException 需要严格检查且存在列数据不同时抛出的异常
	 */
	public LinkedHashMap<String, ArrayList<Optional<T>>> getData(int startRowIndex, int endRowIndex,
			List<String> columnNameList) {
		AtomicInteger startRowIndexA = new AtomicInteger(changeIndex(startRowIndex));
		AtomicInteger endRowIndexA = new AtomicInteger(changeIndex(endRowIndex));

		// 若最长列数据与最短列数据不一致，且需要严格判断，则抛出异常
		if (shortColumnSize != longColumnSize && isExamine) {
			throw new IllegalDataException(String.format("数据列长度不一致，最长数据列长度：%d；最短数据列长度：%d", longColumnSize, shortColumnSize));
		}

		// 存储获取的集合
		LinkedHashMap<String, ArrayList<Optional<T>>> columnDataMap = new LinkedHashMap<>(16);
		Optional.ofNullable(columnNameList).orElseThrow(() -> new IllegalDataException("未传入数据列")).stream()
				.filter(tableMap::containsKey).forEach(columnName -> {
					ArrayList<Optional<T>> columnDataList = new ArrayList<>();
					// 遍历列表，存储数据，若当前列无此行元素（即抛出数组越界异常），则存储空值
					IntStream.range(startRowIndexA.get(), endRowIndexA.get() + 1).forEach(index -> {
						try {
							columnDataList.add(tableMap.get(columnName).get(index - 1));
						} catch (IndexOutOfBoundsException e) {
							columnDataList.add(Optional.empty());
						}

					});
					columnDataMap.put(columnName, columnDataList);
				});

		return columnDataMap;
	}
	
	/**
	 * 用于获取表中第一行的所有数据
	 * @return 第一行的所有数据
	 */
	public LinkedHashMap<String, ArrayList<Optional<T>>> getFirstRowData() {
		return getData(1, 1, getColumnName());
	}

	/**
	 * 刷新表中最长与最短列的元素个数，用于清空或删除表时的计算
	 */
	private void refreshLength() {
		shortColumnSize = Integer.MAX_VALUE;
		longColumnSize = -1;

		// 遍历所有列，查询每一列数据个数
		for (String name : tableMap.keySet()) {
			int length = tableMap.get(name).size();
			shortColumnSize = Math.min(length, shortColumnSize);
			longColumnSize = Math.max(length, longColumnSize);
		}
	}

	/**
	 * 用于将下标转换为正序遍历的下标
	 * 
	 * @param index  传入的下标
	 * @param length 数据总数
	 * @return 转换后的下标
	 */
	private int changeIndex(int index) {
		// 若下标为0，则直接返回1，若下标小于0，则对下标进行处理，若下标大于0，则直接返回下标
		if (index < 0) {
			// 若下标的绝对值大于数据总数，则返回1
			if (Math.abs(index) > longColumnSize) {
				return 1;
			} else {
				return (longColumnSize + index) + 1;
			}
		} else if (index == 0) {
			return 1;
		} else {
			return index;
		}
	}
}
