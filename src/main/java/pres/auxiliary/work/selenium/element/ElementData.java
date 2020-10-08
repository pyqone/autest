package pres.auxiliary.work.selenium.element;

import java.util.ArrayList;

import pres.auxiliary.work.selenium.location.AbstractRead;
import pres.auxiliary.work.selenium.xml.ByType;

/**
 * <p><b>文件名：</b>ElementData.java</p>
 * <p><b>用途：</b>
 * 用于存储页面元素的基本信息，以便于在查找元素中进行使用
 * </p>
 * <p><b>编码时间：</b>2020年9月27日上午7:50:44</p>
 * <p><b>修改时间：</b>2020年9月27日上午7:50:44</p>
 * @author 彭宇琦
 * @version Ver1.0
 */
public class ElementData {
	/**
	 * 存储元素的名称或定位内容
	 */
	private String name;
	/**
	 * 存储元素的定位方式，需要与valueList一一对应
	 */
	private ArrayList<ByType> byTypeList = new ArrayList<>();
	/**
	 * 存储元素的定位内容，需要与byTypeList一一对应
	 */
	private ArrayList<String> valueList = new ArrayList<>();
	/**
	 * 用于标记元素的类型
	 */
	private ElementType elementType;
	/**
	 * 用于存储元素的父层窗体（所有的父层窗体）
	 */
	private ArrayList<String> iframeNameList = new ArrayList<>();
	/**
	 * 存储元素
	 */
	private long waitTime;
	
	/**
	 * 根据元素名称，在配置文件中查找元素，将元素的信息进行存储
	 * @param name 元素名称
	 * @AbstractReadConfig 配置文件类对象
	 */
	public ElementData(String name, AbstractRead arc) {
		//存储元素名称
		this.name = name;
		
		//根据传入的读取配置文件类对象，使用其中的返回方法，初始化元素信息
		byTypeList = arc.getElementByTypeList(name);
		valueList = arc.getValueList(name);
		elementType = arc.getElementType(name);
		iframeNameList = arc.getIframeNameList(name);
		waitTime = arc.getWaitTime(name);
	}

	/**
	 * 返回元素名称
	 * @return 元素名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 返回元素定位类型集合
	 * @return 元素定位类型集合
	 */
	public ArrayList<ByType> getByTypeList() {
		return byTypeList;
	}

	/**
	 * 返回元素定位内容集合
	 * @return 元素定位内容集合
	 */
	public ArrayList<String> getValueList() {
		return valueList;
	}

	/**
	 * 返回元素类型
	 * @return 元素
	 */
	public ElementType getElementType() {
		return elementType;
	}

	/**
	 * 返回元素父层窗体名称列表
	 * @return
	 */
	public ArrayList<String> getIframeNameList() {
		return iframeNameList;
	}

	/**
	 * 返回元素加载超时时间
	 * @return 元素加载超时时间
	 */
	public long getWaitTime() {
		return waitTime;
	}
	
	
}
