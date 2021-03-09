package com.auxiliary.selenium.element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import com.auxiliary.selenium.location.AbstractLocation;
import com.auxiliary.selenium.location.ByType;
import com.auxiliary.selenium.location.ReadLocation;

/**
 * <p><b>文件名：</b>ElementData.java</p>
 * <p><b>用途：</b>
 * 用于存储页面元素的基本信息，以便于在查找元素中进行使用
 * </p>
 * <p><b>编码时间：</b>2020年9月27日上午7:50:44</p>
 * <p><b>修改时间：</b>2021年3月9日上午8:08:45</p>
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
	 * 用于存储外链的词语
	 */
	private ArrayList<String> linkWordList = new ArrayList<>();
	
	/**
	 * 根据元素名称，在配置文件中查找元素，将元素的信息进行存储
	 * @param name 元素名称
	 * @param read 配置文件类对象
	 */
	public ElementData(String name, ReadLocation read) {
		//存储元素名称
		this.name = name;
		
		//根据传入的读取配置文件类对象，使用其中的返回方法，初始化元素信息
		read.find(name);
		byTypeList = read.getElementByTypeList();
		valueList = read.getValueList();
		elementType = read.getElementType();
		iframeNameList = read.getIframeNameList();
		waitTime = read.getWaitTime();
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
		//若存储的外链词语不为空，则对需要外链的定位内容进行处理
		if (!linkWordList.isEmpty()) {
			for (int i = 0; i < valueList.size(); i++) {
				//判断字符串是否包含替换词语的开始标志，若不包含，则进行不进行替换操作
				if (!valueList.get(i).contains(AbstractLocation.START_SIGN)) {
					continue;
				}
				
				//获取替换词语集合的迭代器
				Iterator<String> linkWordIter = linkWordList.iterator();
				//存储当前定位内容文本
				StringBuilder value = new StringBuilder(valueList.get(i));
				//循环，替换当前定位内容中所有需要替换的词语，直到无词语替换或定位内容不存在需要替换的词语为止
				while(linkWordIter.hasNext() && value.indexOf(AbstractLocation.START_SIGN) > -1) {
					//存储替换符的开始和结束位置
					int replaceStartIndex = value.indexOf(AbstractLocation.START_SIGN);
					int replaceEndIndex = value.indexOf(AbstractLocation.END_SIGN);
					
					//对当前位置的词语进行替换
					value.replace(replaceStartIndex, replaceEndIndex + 1, linkWordIter.next());
				} 
				
				//存储当前替换后的定位内容
				valueList.set(i, value.toString());
			}
		}
		
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
	 * @return 父层窗体名称列表
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
	
	/**
	 * 返回元素定位方式的个数
	 * <p>
	 * 若定位方式与定位内容不一致时，则返回两者中的最小长度
	 * </p>
	 * @return 元素定位方式的个数
	 */
	public int getLocationSize() {
		return Math.min(byTypeList.size(), valueList.size());
	}

	/**
	 * 用于添加元素定位外链词语
	 * @param linkWords 外链词语
	 */
	public void addLinkWord(String...linkWords) {
		if (linkWords != null && linkWords.length != 0) {
			linkWordList.addAll(Arrays.asList(linkWords));
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((iframeNameList == null) ? 0 : iframeNameList.hashCode());
		result = prime * result + ((linkWordList == null) ? 0 : linkWordList.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ElementData other = (ElementData) obj;
		if (iframeNameList == null) {
			if (other.iframeNameList != null)
				return false;
		} else if (!iframeNameList.equals(other.iframeNameList))
			return false;
		if (linkWordList == null) {
			if (other.linkWordList != null)
				return false;
		} else if (!linkWordList.equals(other.linkWordList))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
