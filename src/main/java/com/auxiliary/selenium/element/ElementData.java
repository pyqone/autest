package com.auxiliary.selenium.element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import com.auxiliary.selenium.location.AbstractLocation;
import com.auxiliary.selenium.location.AppElementLocation;
import com.auxiliary.selenium.location.ElementLocationInfo;
import com.auxiliary.selenium.location.ReadElementLimit;
import com.auxiliary.selenium.location.ReadLocation;

/**
 * <p>
 * <b>文件名：</b>ElementData.java
 * </p>
 * <p>
 * <b>用途：</b> 用于存储页面元素的基本信息，以便于在查找元素中进行使用
 * </p>
 * <p>
 * <b>编码时间：</b>2020年9月27日上午7:50:44
 * </p>
 * <p>
 * <b>修改时间：</b>2021年4月10日下午2:53:33
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.2
 */
public class ElementData {
	/**
	 * 存储元素的名称
	 */
	private String name;
	/**
	 * 存储元素读取的方式
	 */
	private ReadLocation read;

	/**
	 * 存储外链的词语
	 */
	private ArrayList<String> linkWordList = new ArrayList<>();
	
	/**
	 * 根据元素名称，在配置文件中查找元素，将元素的信息进行存储
	 * 
	 * @param name 元素名称
	 * @param read 配置文件类对象
	 */
	public ElementData(String name, ReadLocation read) {
		//若查找成功，则存储元素名称与元素信息读取类对象
		this.name = name;
		this.read = read;
	}

	/**
	 * 返回元素名称
	 * 
	 * @return 元素名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 返回元素定位信息集合
	 * 
	 * @return 元素定位信息集合
	 */
	public ArrayList<ElementLocationInfo> getLocationList() {
		//对元素进行查找
		read.find(name);
				
		//获取元素定位信息
		ArrayList<ElementLocationInfo> locationList = new ArrayList<> (read.getElementLocation());
		
		// 若存储的外链词语不为空，则对需要外链的定位内容进行处理
		if (!linkWordList.isEmpty()) {
			for (int i = 0; i < locationList.size(); i++) {
				// 判断字符串是否包含替换词语的开始标志，若不包含，则进行不进行替换操作
				if (!locationList.get(i).getLocationText().contains(AbstractLocation.START_SIGN)) {
					continue;
				}

				// 获取替换词语集合的迭代器
				Iterator<String> linkWordIter = linkWordList.iterator();
				// 存储当前定位内容文本
				StringBuilder value = new StringBuilder(locationList.get(i).getLocationText());
				// 循环，替换当前定位内容中所有需要替换的词语，直到无词语替换或定位内容不存在需要替换的词语为止
				while (linkWordIter.hasNext() && value.indexOf(AbstractLocation.START_SIGN) > -1) {
					// 存储替换符的开始和结束位置
					int replaceStartIndex = value.indexOf(AbstractLocation.START_SIGN);
					int replaceEndIndex = value.indexOf(AbstractLocation.END_SIGN);

					// 对当前位置的词语进行替换
					value.replace(replaceStartIndex, replaceEndIndex + 1, linkWordIter.next());
				} 

				// 存储当前替换后的定位内容
				locationList.get(i).setLocationText(value.toString());
			}
		}

		return locationList;
	}

	/**
	 * 返回元素类型
	 * 
	 * @return 元素
	 */
	public ElementType getElementType() {
		//对元素进行查找
		read.find(name);
		return read.getElementType();
	}

	/**
	 * 返回元素父层窗体名称列表
	 * 
	 * @return 父层窗体名称列表
	 */
	public ArrayList<String> getIframeNameList() {
		//对元素进行查找
		read.find(name);
		ArrayList<String> iframeNameList = new ArrayList<>(read.getIframeNameList());
		return iframeNameList;
	}

	/**
	 * 返回元素加载超时时间
	 * 
	 * @return 元素加载超时时间
	 */
	public long getWaitTime() {
		//对元素进行查找
		read.find(name);
		return read.getWaitTime();
	}
	
	/**
	 * 用于返回元素的默认值。若元素不存在默认值，则返回空串
	 * @return 元素的默认值
	 */
	public String getDefaultValue() {
		//对元素进行查找
		read.find(name);
		String defaultValue = "";
		//判断元素读取类是否
		if (read instanceof ReadElementLimit) {
			defaultValue = ((ReadElementLimit)read).getDefaultValue();
		}
		
		return defaultValue;
	}
	
	/**
	 * 用于返回当前元素是否为app原生元素
	 * <p>
	 * <b>注意：</b>若元素属于app元素，则返回false
	 * </p>
	 * @return 元素是否为app原生元素
	 */
	public boolean isNativeElement() {
		if (read instanceof AppElementLocation) {
			return ((AppElementLocation)read).isNative();
		} else {
			return false;
		}
	}
	
	/**
	 * 用于返回app元素所在WebView的上下文
	 * <p>
	 * <b>注意：</b>若元素非app元素或app的原生元素，则返回空串
	 * </p>
	 * @return app元素所在WebView的上下文
	 */
	public String getWebViewContext() {
		if (read instanceof AppElementLocation) {
			return ((AppElementLocation)read).getContext();
		} else {
			return "";
		}
	}

	/**
	 * 返回元素定位方式的个数
	 * <p>
	 * 若定位方式与定位内容不一致时，则返回两者中的最小长度
	 * </p>
	 * 
	 * @return 元素定位方式的个数
	 * @deprecated 通过{@link #getLocationList()}方法获取到集合后，调用{@link ArrayList#size()}可获得
	 */
	@Deprecated
	public int getLocationSize() {
		return getLocationList().size();
	}

	/**
	 * 用于添加元素定位外链词语
	 * 
	 * @param linkWords 外链词语
	 */
	public void addLinkWord(String... linkWords) {
		if (linkWords != null && linkWords.length != 0) {
			linkWordList.addAll(Arrays.asList(linkWords));
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
