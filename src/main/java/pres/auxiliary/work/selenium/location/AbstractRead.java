package pres.auxiliary.work.selenium.location;

import java.util.ArrayList;

import pres.auxiliary.work.selenium.element.ElementType;
import pres.auxiliary.work.selenium.xml.ByType;

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
	 * 指向元素名称
	 */
	protected String name;
	
	/**
	 * 用于设置元素名称
	 * @param name 元素名称
	 */
	public void setElementName(String name) {
		this.name = name;
	}
	
	/**
	 * 用于返回元素名称
	 * @return 元素名称
	 */
	public String getElementName() {
		return name;
	}
	
	/**
	 * 用于返回元素的所有定位方式集合
	 * @return 元素的所有定位方式（{@link ByType}枚举）集合
	 */
	public abstract ArrayList<ByType> getElementByTypeList();
	
	/**
	 * 用于返回与定位方式对应的元素定位内容集合
	 * @return 元素定位内容集合
	 */
	public abstract ArrayList<String> getValueList();
	
	/**
	 * 用于返回元素的类型
	 * @return 元素类型（{@link ElementType}枚举）
	 */
	public abstract ElementType getElementType();
	
	/**
	 * 用于返回元素的所有父窗体名称集合
	 * @return 元素的所有父窗体名称集合
	 */
	public abstract ArrayList<String> getIframeNameList();
	
	/**
	 * 用于返回元素的等待时间
	 * @return 元素的等待时间
	 */
	public abstract long getWaitTime();
}
