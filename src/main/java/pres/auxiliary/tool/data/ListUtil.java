package pres.auxiliary.tool.data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * <b>文件名：</b>TableUtil.java
 * </p>
 * <p>
 * <b>用途：</b> 提供对列表型数据进行转换的工具方法
 * </p>
 * <p>
 * <b>编码时间：</b>2020年12月22日上午8:27:07
 * </p>
 * <p>
 * <b>修改时间：</b>2020年12月22日上午8:27:07
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 *
 */
public class ListUtil {
	/**
	 * 用于将带标题型的列表数据（Map型表数据）转换为无标题型的列表数据（List型表数据）
	 * 
	 * @param tableMap 带标题的表数据
	 * @return 无标题的表数据
	 */
	public static <T> List<List<Optional<T>>> toNoTitleTable(Map<String, ? extends List<Optional<T>>> tableMap) {
		List<List<Optional<T>>> tableList = new ArrayList<>();
		// 遍历tableMap，在tableList中添加数据
		Optional.ofNullable(tableMap).filter(table -> !table.isEmpty())
				.orElseThrow(() -> new IllegalDataException("未指定列表"))
				.forEach((key, value) -> tableList.add(new ArrayList<>(value)));

		return tableList;
	}

	/**
	 * 用于对列表数据进行转置
	 * 
	 * @param tableList 原列表数据
	 * @return 转置后的列表数据
	 * @throws IllegalDataException 数据列为空时抛出的异常
	 */
	public static <T> List<List<Optional<T>>> transposition(List<? extends List<Optional<T>>> tableList) {
		tableList = Optional.ofNullable(tableList).filter(table -> !table.isEmpty())
				.orElseThrow(() -> new IllegalDataException("数据列为空"));

		// 获取当前列表的最长列的数据个数
		AtomicInteger maxRowA = new AtomicInteger(-1);
		tableList.stream()
				// 返回列表长度
				.map(list -> list.size())
				// 与当前数值比较，取最大值并存储
				.map(size -> Math.max(size, maxRowA.get())).forEach(maxRowA::set);

		// 按行遍历列表，并存储相应的数据，若当前列对应的行数据为空，则存储空的Optional对象
		List<List<Optional<T>>> newTableList = new ArrayList<>();
		for (int rowIndex = 0; rowIndex < maxRowA.get(); rowIndex++) {
			ArrayList<Optional<T>> newColumnList = new ArrayList<>();
			AtomicInteger rowIndexA = new AtomicInteger(rowIndex);
			// 将列表数据进行转换，若抛出数组越界异常，则用空Optional对象代替
			tableList.forEach(columnList -> {
				try {
					newColumnList.add(columnList.get(rowIndexA.get()));
				} catch (IndexOutOfBoundsException e) {
					newColumnList.add(Optional.empty());
				}
			});

			// 存储转换后的列数据
			newTableList.add(newColumnList);
		}

		return newTableList;
	}

	/**
	 * 用于以列的形式返回表中的指定行数据
	 * 
	 * @param <T>       数据列的数据类型
	 * @param tableList 数据表
	 * @param rowIndex  指定的行
	 * @return 以列形式返回该行数据
	 * @throws IllegalDataException 数据列为空时抛出的异常
	 */
	public static <T> List<Optional<T>> rowDataToList(List<? extends List<Optional<T>>> tableList, int rowIndex) {
		return transposition(tableList).get(rowIndex);
	}

	/**
	 * 用于对单列数据集合的数据类型进行转换，返回新数据类型的集合
	 * 
	 * @param <T>    原数据类型
	 * @param <U>    新数据类型
	 * @param list   原数据类型集合
	 * @param mapper 数据转换方式
	 * @return 转换后的数据类型列表
	 */
	public static <T, U> List<Optional<U>> changeList(List<Optional<T>> list, Function<T, U> mapper) {
		return list.stream().map(data -> data.map(mapper)).collect(Collectors.toList());
	}

	/**
	 * 用于对列表中的数据类型进行转换，返回新数据类型的列表
	 * 
	 * @param <T>       原数据类型
	 * @param <U>       新数据类型
	 * @param tableList 原数据类型表
	 * @param mapper    数据转换方式
	 * @return 转换后的数据类型表
	 */
	public static <T, U> List<? extends List<Optional<U>>> changeTable(List<? extends List<Optional<T>>> tableList,
			Function<T, U> mapper) {
		return tableList.stream().map(list -> ListUtil.changeList(list, mapper)).collect(Collectors.toList());
	}

	/**
	 * 用于对列表中的数据类型进行转换，返回新数据类型的列表
	 * 
	 * @param <T>      原数据类型
	 * @param <U>      新数据类型
	 * @param tableMap 原数据类型表
	 * @param mapper   数据转换方式
	 * @return 转换后的数据类型表
	 */
	public static <T, U> Map<String, List<Optional<U>>> changeTable(Map<String, ? extends List<Optional<T>>> tableMap,
			Function<T, U> mapper) {
		Map<String, List<Optional<U>>> newTableMap = new LinkedHashMap<>();
		tableMap.forEach((key, value) -> newTableMap.put(key, changeList(value, mapper)));

		return newTableMap;
	}

	public static <T, U> TableData<U> changeTable(TableData<T> tableData, Function<T, U> mapper) {
		TableData<U> newTable = new TableData<>();
		tableData.columnForEach((key, value) -> newTable.addColumn(key, changeList(value, mapper).stream()
				.map(dataOptional -> dataOptional.orElse(null)).collect(Collectors.toList())));
		return newTable;
		
	}
}
