package pres.auxiliary.work.selenium.location;

import java.util.ArrayList;

import pres.auxiliary.work.selenium.element.ElementType;

/**
 * <p><b>文件名：</b>ReadLocation.java</p>
 * <p><b>用途：</b>
 * 定义读取元素定位内容必须实现的方法
 * </p>
 * <p><b>编码时间：</b>2020年10月29日下午12:52:41</p>
 * <p><b>修改时间：</b>2020年10月29日下午12:52:41</p>
 * @author 彭宇琦
 * @version Ver1.0
 *
 */
public interface ReadLocation {
	/**
	 * 用于返回元素的所有定位方式集合
	 * @param name 元素名称
	 * @return 元素的所有定位方式（{@link ByType}枚举）集合
	 * @throws UndefinedElementException 元素不存在时抛出的异常
	 */
	public abstract ArrayList<ByType> findElementByTypeList(String name);
	
	/**
	 * 用于返回与定位方式对应的元素定位内容集合
	 * @param name 元素名称
	 * @return 元素定位内容集合
	 * @throws UndefinedElementException 元素不存在时抛出的异常
	 */
	public abstract ArrayList<String> findValueList(String name);
	
	/**
	 * 用于返回元素的类型
	 * @param name 元素名称
	 * @return 元素类型（{@link ElementType}枚举）
	 * @throws UndefinedElementException 元素不存在时抛出的异常
	 */
	public abstract ElementType findElementType(String name);
	
	/**
	 * 用于返回元素的所有父窗体名称集合
	 * @param name 元素名称
	 * @return 元素的所有父窗体名称集合
	 * @throws UndefinedElementException 元素不存在时抛出的异常
	 */
	public abstract ArrayList<String> findIframeNameList(String name);
	
	/**
	 * 用于返回元素的等待时间
	 * @param name 元素名称
	 * @return 元素的等待时间
	 * @throws UndefinedElementException 元素不存在时抛出的异常
	 */
	public abstract long findWaitTime(String name);
}
