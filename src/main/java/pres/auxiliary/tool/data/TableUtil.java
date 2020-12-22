package pres.auxiliary.tool.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

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
public class TableUtil {
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
	 */
	public static <T> List<List<Optional<T>>> transposition(List<List<Optional<T>>> tableList) {
		tableList = Optional.ofNullable(tableList).filter(table -> !table.isEmpty())
				.orElseThrow(() -> new IllegalDataException("未指定列表"));

		// 获取当前列表的最长列的数据个数
		AtomicInteger maxRowA = new AtomicInteger(-1);
		tableList.stream()
				//返回列表长度
				.map(list -> list.size())
				//与当前数值比较，取最大值并存储
				.map(size -> Math.max(size, maxRowA.get())).forEach(maxRowA :: set);
		
		//按行遍历列表，并存储相应的数据，若当前列对应的行数据为空，则存储空的Optional对象
		List<List<Optional<T>>> newTableList = new ArrayList<>();
		for (int rowIndex = 0; rowIndex < maxRowA.get(); rowIndex++) {
			ArrayList<Optional<T>> newColumnList = new ArrayList<>();
			AtomicInteger rowIndexA = new AtomicInteger(rowIndex);
			//将列表数据进行转换，若抛出数组越界异常，则用空Optional对象代替
			tableList.forEach(columnList -> {
				try {
					newColumnList.add(columnList.get(rowIndexA.get()));
				} catch (IndexOutOfBoundsException e) {
					newColumnList.add(Optional.empty());
				}
			});
			
			//存储转换后的列数据
			newTableList.add(newColumnList);
		}

		return newTableList;
	}
	
	/**
	 * 用于对列表中的数据类型进行转换，返回新数据类型的列表
	 * @param <T> 原数据类型
	 * @param <U> 新数据类型
	 * @param tableList 原数据类型列表
	 * @param mapper 数据转换方式
	 * @return 转换后的数据类型列表
	 */
	public static <T, U> List<List<Optional<U>>> changeTable(List<List<Optional<T>>> tableList, Function<T, U> mapper) {
		List<List<Optional<U>>> newTableList = new ArrayList<>();
		
		tableList.forEach(columnList -> {
			List<Optional<U>> newColumnList = new ArrayList<>();
			columnList.forEach(data -> {
				newColumnList.add(data.map(mapper));
			});
			
			newTableList.add(newColumnList);
		});
		
		return newTableList;
	}
}
