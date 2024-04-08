package com.auxiliary.tool.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * <p><b>文件名：</b>RandomObject.java</p>
 * <p><b>用途：</b>
 * 提供根据对象池（随机对象获取的范围），随机返回池中对象的方法
 * </p>
 * <p><b>编码时间：</b>2021年1月13日下午12:49:02</p>
 * <p><b>修改时间：</b>2021年1月13日下午12:49:02</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 *
 * @param <T> 随机对象的数据类型
 */
public class RandomObject<T> {
	/**
	 * 定义默认对象池的名称
	 */
	public static final String DEFAULT_NAME = "defaultColumn";

	/**
	 * 存储随机返回的对象
	 */
	private TableData<T> wordTable = new TableData<>();
	/**
	 * 控制是否允许随机出重复的数据
	 */
	private boolean isRepeat = true;

	/**
	 * 构造对象
	 */
	public RandomObject() {
		//设置表数据返回时不做严格检查
		wordTable.setExamine(false);
	}

	/**
	 * 是否允许在随机返回的对象中出现重复的对象
	 * <p>
	 * <b>注意：</b>是否允许重复是相对于返回的对象而言，若添加到对象池时
	 * 已存在重复，则在返回对象，即使设置不允许重复返回，在返回对象时也存
	 * 在相同对象的可能
	 * </p>
	 * @param isRepeat 是否允许出现重复对象
	 */
	public void setRepeat(boolean isRepeat) {
		this.isRepeat = isRepeat;
	}

	/**
	 * 用于返回指定名称对象池中元素的个数
	 * @param name 对象池名称
	 * @return 对象池中对象个数
	 */
	public int size(String name) {
        return wordTable.getListSize(name);
	}

	/**
	 * 用于向默认对象池中添加对象数据
	 * @param objs 对象组
	 */
	@SuppressWarnings("unchecked")
	public void addObject(T...objs) {
		addObject(Arrays.asList(objs));
	}

	/**
	 * 用于向默认对象池中添加对象数据
	 * @param objList 对象集合
	 */
	public void addObject(List<T> objList) {
		//以默认的名称
		wordTable.addColumn(DEFAULT_NAME, Optional.of(objList).orElse(new ArrayList<T> ()));
	}

	/**
	 * 用于清除指定对象池的对象
	 * @param name 对象池名称
	 */
	public void clear(String name) {
        try {
            wordTable.clearColumn(name);
        } catch (Exception e) {
        }
	}

	/**
	 * 用于返回指定的对象池数据
	 * @param name 对象池名称
	 * @return 对应的对象池数据
	 */
	public List<T> getObjectSeed(String name) {
		return wordTable.getColumnList(name).stream().map(result -> result.get()).collect(Collectors.toList());
	}

	/**
	 * 用于返回默认对象池中的一个对象的封装类对象。若默认对象池中无数据，则返回{@link Optional#empty()}
	 * @return  默认池中对象的封装类对象
	 */
	public Optional<T> toObject() {
		try {
			return toObject(1).get(0);
		} catch (IndexOutOfBoundsException e) {
			//若抛出数组越界异常，则返回空封装类
			return Optional.empty();
		}
	}

	/**
	 * 用于随机返回默认对象池中的指定个数的对象的封装类对象。
	 * 若默认对象池中无数据，则返回{@link Optional#empty()}
	 * <p>
	 * 该方法会根据设置的对象是否允许重复，对对象进行返回。若设置允许重复，则返回的随机对象中
	 * 同一个对象可能返回多次；反之，不会存在一个对象被返回多次的情况。
	 * </p>
	 * <p>
	 * <b>注意：</b>
	 * <ol>
	 * <li>若设置不允许重复，且需要返回的对象个数超出当前存储的对象个数时，
	 * 则超出的部分用{@link Optional#empty()}代替</li>
	 * <li>当传入的元素个数小于1时，则返回空集合</li>
	 * </ol>
	 * </p>
	 * @param length 对象返回个数
	 * @return 指定个数的对象的封装类对象
	 */
	public List<Optional<T>> toObject(int length) {
		return generateRandomObject(DEFAULT_NAME, length);
	}

	/**
	 * 用于在限定的个数范围内，随机返回默认对象池中的随机个对象的封装类对象。
	 * 若默认对象池中无数据，则返回{@link Optional#empty()}，具体规则可参考{@link #toObject(int)}方法
	 * <p>
	 * <b>注意：</b>
	 * <ol>
	 * <li>若传入的最多、最少个数相等，则等同于调用{@link #toObject(int)}方法</li>
	 * <li>若最少个数大于最多个数，则只按最少个数进行随机返回</li>
	 * <li>若最少个数小于1，且最多个数不小于1，则最少个数按照1进行设置；反之，则返回空集合</li>
	 * </ol>
	 * </p>
	 * @param minLength 最少返回个数
	 * @param maxLength 最多返回个数
	 * @return 随机个数的对象的封装类对象
	 */
	public List<Optional<T>> toObject(int minLength, int maxLength) {
		//判断最少个数是否小于1
		if (minLength < 1) {
			//再判断最多个数是否大于1，大于1则将最少个数设为1；
			if (maxLength > 1) {
				minLength = 1;
			} else {
				//若最少最多的数字都小于0，则返回空集合
				return new ArrayList<>();
			}
		}

		//判断最少个数是否大于最大个数
		if (minLength > maxLength) {
			maxLength = minLength;
		}

		//随机生成一个长度
		return toObject(new Random().nextInt(maxLength - minLength + 1) + minLength);
	}

	/**
	 * 用于根据对象组名称，从对象组中抽取指定数量的对象
	 * @return 对象集合
	 */
	private List<Optional<T>> generateRandomObject(String name, int length) {
		List<Optional<T>> objList = new ArrayList<>();

		//判断当前传入的对象返回个数是否大于0
		if (length < 1) {
			return objList;
		}
		//判断当前表中是否添加数据且当前对象组名称是否存在
		if (wordTable.isEmpty() || !Optional.ofNullable(name).filter(wordTable::containsColumn).isPresent()) {
			return objList;
		}
		//判断当前指向的列是否包含元素
		List<Optional<T>> columnObj = new ArrayList<>(wordTable.getColumnList(name));
		if (columnObj.size() == 0) {
			return objList;
		}

		//循环添加数据，直到生成的随机对象集合的个数与传入的个数一致为止
		Random random = new Random();
		while(objList.size() != length) {
			//获取当前集合的长度
			int size = columnObj.size();
			//判断当前集合的长度是否为0，若已不存在元素，则在objList中存储空对象，并继续循环
			if (size == 0) {
				objList.add(Optional.empty());
				continue;
			}

			//将为随机数的范围
			int randomNum = random.nextInt(size);
			//存储随机数指向的元素
			objList.add(columnObj.get(randomNum));

			//判断当前生成的对象是否允许重复，若不允许，则移除当前指向的元素
			if (!isRepeat) {
				columnObj.remove(randomNum);
			}
		}

		return objList;
	}
}
