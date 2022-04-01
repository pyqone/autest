package com.auxiliary.tool.data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.auxiliary.tool.common.Entry;

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
 * <b>修改时间：</b>2022年4月1日 上午8:37:49
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 2.2.0
 */
public class ListUtil {
    /**
     * 私有构造，避免被构造出对象
     * 
     * @since autest 3.3.0
     */
    private ListUtil() {
    }

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

    /**
     * 用于对列表中的数据类型进行转换，返回新数据类型的列表
     *
     * @param <T>       原数据类型
     * @param <U>       新数据类型
     * @param tableData 原数据类型表
     * @param mapper    数据转换方式
     * @return 转换后的数据类型表
     */
    public static <T, U> TableData<U> changeTable(TableData<T> tableData, Function<T, U> mapper) {
        TableData<U> newTable = new TableData<>();
        tableData.columnForEach((key, value) -> newTable.addColumn(key, changeList(value, mapper).stream()
                .map(dataOptional -> dataOptional.orElse(null)).collect(Collectors.toList())));
        return newTable;

    }

    /**
     * 用于对集合中的元素进行去重
     * <p>
     * <b>注意：</b>被比较的泛型对象去重按照equals()方法进行比较，需要正确实现equals()方法，才能达到去重的目的
     * </p>
     *
     * @param <T>  需要去重的数据类型
     * @param list 需要去重的集合
     * @return 去重后的集合
     */
    public static <T> List<Optional<T>> removeRepetition(List<Optional<T>> list) {
        LinkedHashSet<Optional<T>> set = new LinkedHashSet<>();
        Optional.ofNullable(list).filter(l -> !l.isEmpty()).ifPresent(l -> l.forEach(set::add));

        return new ArrayList<>(set);
    }

    /**
     * 用于根据指定的条件行对表中的数据进行去重
     * <p>
     * 方法需要指定列名，表示以指定的列为基准，对数据进行去重。类似于数据库中指定字段为主键或联合主键。
     * 当指定的列的数据重复时，将舍弃该数据所在行的整行数据，达到去重的目的
     * </p>
     * <p>
     * <b>注意：</b>
     * <ol>
     * <li>被比较的泛型对象去重按照equals()方法进行比较，需要正确实现equals()方法</li>
     * <li>被舍弃的数据不区分其他内容，只保留第一次存储的数据</li>
     * </ol>
     * </p>
     *
     * @param <T>       需要去重的数据类型
     * @param tableData 需要去重的表
     * @param columns   作为条件的列名称
     * @return 去重后的表
     */
    public static <T> TableData<T> removeRepetition(TableData<T> tableData, String... columns) {
        // 若传入的条件为空，则直接返回传入的tableData
        if (!Optional.ofNullable(columns).filter(cs -> cs.length != 0).isPresent()) {
            return tableData;
        }

        // 定义新的表数据，并初始化其标题
        TableData<T> nowTable = new TableData<>();

        // 按行遍历tableData的所有数据，过滤掉与nowTable中指定行数据一致的行数据，之后添加未被过滤的数据
        Optional.ofNullable(tableData).filter(td -> !td.isEmpty()).ifPresent(td -> {
            nowTable.addTitle(tableData.getColumnName());
            td.rowStream().filter(list -> {
                boolean isRepeat = true;
                for (String column : columns) {
                    // 获取指定列数据，若新表中该列的数据包含了当前的数据，则返回false
                    if (nowTable.getColumnList(column).contains(list.get(tableData.getFieldIndex(column)))) {
                        isRepeat = true;
                        continue;
                    } else {
                        isRepeat = false;
                        break;
                    }
                }

                // 返回不重复的结果
                return !isRepeat;
                // 由于返回的元素为封装类对象，需要对元素脱壳处理。处理方式为直接返回即可
            }).map(list -> list.stream().map(e -> e.get()).collect(Collectors.toList())).forEach(nowTable::addRow);
        });

        return nowTable;
    }

    /**
     * 该方法用于对表数据的每一行根据指定的条件进行筛选，将通过筛选的行组成一张新表进行返回
     * <p>
     * 筛选条件集合由{@link Entry}键值对类组成，其键为列下标，值为对每行键所在的列的元素进行筛选的条件
     * </p>
     *
     * @param <T>        元素类型
     * @param table      需要筛选的表数据类对象
     * @param isFull     是否需要满足所有的筛选条件
     * @param filterList 筛选条件集合
     * @return 筛选通过行组成的新表
     * @since autest 3.3.0
     */
    public static <T> TableData<T> filterTable(TableData<T> table, boolean isFull,
            List<Entry<Integer, Predicate<T>>> filterList) {
        // 根据原表格对象的列名集合，定义新的表格对象
        TableData<T> newTable = new TableData<>(table.getColumnName());

        // 遍历每一行元素，并根据筛选条件对其进行判断
        for (int rowIndex = 0; rowIndex < table.getLongColumnSize(); rowIndex++) {
            // 获取当前行
            List<Optional<T>> rowList = table.getRowData(rowIndex);

            // 遍历条件筛选集合，根据列名称，对指定列元素内容进行比较
            boolean isRecord = false;
            for (Entry<Integer, Predicate<T>> entry : filterList) {
                // 获取当前需要判断的列下标
                int columnIndex = entry.getKey();

                // 若指定的行数小于0或者大于当前行的最大列数，则继续循环，不执行筛选
                if (columnIndex < 0 || columnIndex > rowList.size()) {
                    continue;
                }

                // 获取筛选列的内容，并对内容进行筛选，存储当前是否筛选通过（是否需要记录）
                isRecord = rowList.get(columnIndex).filter(entry.getValue()::test).isPresent();
                // 判断存在以下四种情况：
                // 1. 通过判断，且不需要判断所有筛选条件，则结束循环
                // 2. 未通过判断，且需要判断所有筛选条件，则结束循环
                // 3. 通过判断，且需要判断所有筛选条件，则继续循环
                // 4. 未通过判断，且不需要筛选所有条件，则继续循环
                if ((isRecord && !isFull) || (!isRecord && isFull)) {
                    break;
                }
            }

            // 如果当前行需要记录，则将其添加至newTable中
            if (isRecord) {
                newTable.addRow(rowList.stream().map(Optional::get).collect(Collectors.toList()));
            }
        }

        return newTable;
    }
}
