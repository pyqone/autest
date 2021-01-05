package com.auxiliary.selenium.element;

import java.util.HashSet;
import java.util.Set;

import com.auxiliary.selenium.brower.AbstractBrower;

/**
 * <p><b>文件名：</b>DataListBy.java</p>
 * <p><b>用途：</b>
 * 提供在辅助化测试中，通过一个元素定位方式，获取一列数据（多条数据）的方法，并提供各种方式的元素返回方式
 * </p>
 * <p><b>编码时间：</b>2020年10月14日下午6:45:51</p>
 * <p><b>修改时间：</b>2020年10月14日下午6:45:51</p>
 * @author 彭宇琦
 * @version Ver1.0
 *
 */
public class FindDataListElement extends FindMultiElement<FindDataListElement> {
	/**
	 * 构造方法，初始化浏览器对象
	 * @param brower {@link AbstractBrower}类对象
	 */
	public FindDataListElement(AbstractBrower brower) {
		super(brower);
	}
	
	/**
	 * 用于返回指定个数的随机元素，若未调用{@link #find(String, String...)}方法对元素进行查找，
	 * 或者查无元素时，则调用该方法时会抛出超时异常。
	 * @param length 随机元素个数
	 * @return 相应个数的随机不重复元素{@link Set}集合
	 */
	public Set<Element> getRandomElement(int length) {
		//存储随机得到的数字
		Set<Element> elementSet = new HashSet<>();
		//循环，获取随机数，直到其numberSet的长度与传入的个数一致为止
		while(elementSet.size() < length) {
			elementSet.add(getElement(0));
		}
		
		return elementSet;
	}
}
