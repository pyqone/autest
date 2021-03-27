package com.auxiliary.selenium.location;

import java.util.ArrayList;

import com.auxiliary.selenium.element.ElementType;

/**
 * <p>
 * <b>文件名：</b>ReadLocation.java
 * </p>
 * <p>
 * <b>用途：</b> 定义读取元素定位内容必须实现的方法
 * </p>
 * <p>
 * <b>编码时间：</b>2020年10月29日下午12:52:41
 * </p>
 * <p>
 * <b>修改时间：</b>2021年3月8日上午8:08:45
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 */
public interface ReadLocation {
	/**
	 * 用于返回元素的所有定位方式集合
	 * 
	 * @param name 元素名称
	 * @return 元素的所有定位方式（{@link ByType}枚举）集合
	 * @throws UndefinedElementException 元素不存在时抛出的异常
	 * @deprecated 已改进元素查找的机制，需要先调用{@link #find(String)}查找元素后，
	 *             再调用{@link #getElementLocation()}方法对元素信息进行返回。此方法将在2.3.0版本中删除
	 */
	@Deprecated
	public abstract ArrayList<ByType> findElementByTypeList(String name);

	/**
	 * 用于返回与定位方式对应的元素定位内容集合
	 * 
	 * @param name 元素名称
	 * @return 元素定位内容集合
	 * @throws UndefinedElementException 元素不存在时抛出的异常
	 * @deprecated 已改进元素查找的机制，需要先调用{@link #find(String)}查找元素后，
	 *             再调用{@link #getElementLocation()}方法对元素信息进行返回。此方法将在2.3.0版本中删除
	 */
	@Deprecated
	public abstract ArrayList<String> findValueList(String name);

	/**
	 * 用于返回元素的定位信息
	 * @return 元素定位信息
	 * @throws UndefinedElementException 元素不存在时抛出的异常
	 * @since autest 2.2.0
	 */
	public abstract ArrayList<ElementLocationInfo> getElementLocation();

	/**
	 * 用于返回元素的类型
	 * 
	 * @param name 元素名称
	 * @return 元素类型（{@link ElementType}枚举）
	 * @throws UndefinedElementException 元素不存在时抛出的异常
	 * @deprecated 已改进元素查找的机制，需要先调用{@link #find(String)}查找元素后，
	 *             再调用{@link #getElementType()}方法对元素信息进行返回。此方法将在2.3.0版本中删除
	 */
	@Deprecated
	public abstract ElementType findElementType(String name);

	/**
	 * 用于返回元素的类型
	 * 
	 * @return 元素类型（{@link ElementType}枚举）
	 * @throws UndefinedElementException 元素不存在时抛出的异常
	 * @since autest 2.2.0
	 */
	public abstract ElementType getElementType();

	/**
	 * 用于返回元素的所有父窗体名称集合
	 * 
	 * @param name 元素名称
	 * @return 元素的所有父窗体名称集合
	 * @throws UndefinedElementException 元素不存在时抛出的异常
	 * @deprecated 已改进元素查找的机制，需要先调用{@link #find(String)}查找元素后，
	 *             再调用{@link #getIframeNameList()}方法对元素信息进行返回。此方法将在2.3.0版本中删除
	 */
	@Deprecated
	public abstract ArrayList<String> findIframeNameList(String name);

	/**
	 * 用于返回元素的所有父窗体名称集合
	 * 
	 * @return 元素的所有父窗体名称集合
	 * @throws UndefinedElementException 元素不存在时抛出的异常
	 * @since autest 2.2.0
	 */
	public abstract ArrayList<String> getIframeNameList();

	/**
	 * 用于返回元素的等待时间
	 * 
	 * @param name 元素名称
	 * @return 元素的等待时间
	 * @throws UndefinedElementException 元素不存在时抛出的异常
	 * @deprecated 已改进元素查找的机制，需要先调用{@link #find(String)}查找元素后，
	 *             再调用{@link #getWaitTime()}方法对元素信息进行返回。此方法将在2.3.0版本中删除
	 */
	@Deprecated
	public abstract long findWaitTime(String name);

	/**
	 * 用于返回元素的等待时间
	 * 
	 * @return 元素的等待时间
	 * @throws UndefinedElementException 元素不存在时抛出的异常
	 * @since autest 2.2.0
	 */
	public abstract long getWaitTime();

	/**
	 * 用于预读元素信息，并将元素信息进行缓存，以便于快速查找元素信息
	 * 
	 * @param name 元素名称
	 * @since autest 2.2.0
	 */
	public abstract ReadLocation find(String name);
}
