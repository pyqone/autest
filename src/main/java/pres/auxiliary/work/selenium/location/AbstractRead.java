package pres.auxiliary.work.selenium.location;

import java.util.ArrayList;

import pres.auxiliary.work.selenium.element.ElementType;

/**
 * <p><b>文件名：</b>AbstractReadConfig.java</p>
 * <p><b>用途：</b>
 * 定义读取自动化测试中元素定位方式的基本方法
 * </p>
 * <p><b>编码时间：</b>2020年9月28日上午7:37:00</p>
 * <p><b>修改时间：</b>2020年9月28日上午7:37:00</p>
 * @author 彭宇琦
 * @version Ver1.0
 *
 */
public abstract class AbstractRead {
	/**
	 * 用于返回元素的所有定位方式集合
	 * @return 元素的所有定位方式（{@link ByType}枚举）集合
	 */
	public abstract ArrayList<ByType> findElementByTypeList(String name);
	
	/**
	 * 用于返回与定位方式对应的元素定位内容集合
	 * @return 元素定位内容集合
	 */
	public abstract ArrayList<String> findValueList(String name);
	
	/**
	 * 用于返回元素的类型
	 * @return 元素类型（{@link ElementType}枚举）
	 */
	public abstract ElementType findElementType(String name);
	
	/**
	 * 用于返回元素的所有父窗体名称集合
	 * @return 元素的所有父窗体名称集合
	 */
	public abstract ArrayList<String> findIframeNameList(String name);
	
	/**
	 * 用于返回元素的等待时间
	 * @return 元素的等待时间
	 */
	public abstract long findWaitTime(String name);
	
	/**
	 * 用于将读取到的元素类型的文本值转换为元素类型枚举类对象
	 * @param value 元素类型文本值
	 * @return 元素类型枚举类对象
	 */
	protected ElementType toElementType(String value) {
		//转换元素类型枚举，并返回
		switch (value) {
		case "0":
			return ElementType.COMMON_ELEMENT;
		case "1":
			return ElementType.DATA_LIST_ELEMENT;
		case "2":
			return ElementType.SELECT_DATAS_ELEMENT;
		case "3":
			return ElementType.SELECT_OPTION_ELEMENT;
		case "4":
			return ElementType.IFRAME_ELEMENT;
		default:
			return null;
		}
	}
}
