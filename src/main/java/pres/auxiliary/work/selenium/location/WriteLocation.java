package pres.auxiliary.work.selenium.location;

import pres.auxiliary.work.selenium.element.ElementType;

/**
 * <p><b>文件名：</b>WriteLocation.java</p>
 * <p><b>用途：</b>
 * 定义写入元素定位内容必须实现的基本方法
 * </p>
 * <p><b>编码时间：</b>2020年10月29日下午1:06:46</p>
 * <p><b>修改时间：</b>2020年10月29日下午1:06:46</p>
 * @author 彭宇琦
 * @version Ver1.0
 *
 */
public interface WriteLocation {
	/**
	 * 用于设置元素的定位方式，该内容允许多次设置，将存储多个元素定位方式
	 * @param name 元素名称
	 * @param byType 元素定位类型{@link ByType}枚举
	 * @param value 元素定位内容
	 */
	public abstract void putElementLocation(String name, ByType byType, String value);
	
	/**
	 * 用于设置元素的类型，多次设置内容时将覆盖上一次设置的内容
	 * @param name 元素名称
	 * @param elementType 元素类型（{@link ElementType}枚举）
	 */
	public abstract void putElementType(String name, ElementType elementType);
	
	/**
	 * 用于设置元素的所有父窗体名称集合，多次设置内容时将覆盖上一次设置的内容
	 * @param name 元素名称
	 * @param iframeName 元素父窗体名称
	 */
	public abstract void putIframeName(String name, String iframeName);
	
	/**
	 * 用于设置元素的等待时间，多次设置内容时将覆盖上一次设置的内容
	 * @param name 元素名称
	 * @param waitTime 元素等待时间
	 */
	public abstract void putWaitTime(String name, long waitTime);
}
