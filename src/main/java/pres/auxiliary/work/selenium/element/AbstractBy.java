package pres.auxiliary.work.selenium.element;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;

import pres.auxiliary.work.selenium.brower.AbstractBrower;
import pres.auxiliary.work.selenium.xml.ByType;
import pres.auxiliary.work.selenium.xml.ReadXml;

/**
 * <p><b>文件名：</b>AbstractElement.java</p>
 * <p><b>用途：</b></p>
 * <p><pre>
 *   对辅助化测试工具selenium的获取元素代码进行的二次封装，通过类中提供的方法以及配合相应存储元素的
 * xml文件，以更简便的方式对页面元素进行获取，减少编程时的代码量。
 * </pre></p>
 * <p><b>编码时间：</b>2020年4月25日 下午4:18:37</p>
 * <p><b>修改时间：</b>2020年4月25日 下午4:18:37</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 */
public abstract class AbstractBy {
	/**
	 * 用于指向存储控件定位方式的xml文件，设置属性为静态，用于在编写脚本时不需要频繁切换xml文件
	 */
	static ReadXml xml;
	
	/**
	 * 用于存储浏览器对象
	 */
	AbstractBrower brower;
	/**
	 * 存储单个控件的等待时间
	 */
	private HashMap<String, Long> controlWaitTime = new HashMap<String, Long>();
	
	/**
	 * 用于存储元素通用的等待时间，默认5秒
	 */
	private long waitTime = 5;
	
	/**
	 * 通过浏览器对象{@link AbstractBrower}进行构造
	 * @param brower {@link AbstractBrower}对象
	 */
	public AbstractBy(AbstractBrower brower) {
		this.brower = brower;
	}
	
	/**
	 * 用于设置元素查找等待时间，默认时间为5秒
	 * 
	 * @param waitTime 事件等待时间
	 */
	public void setWaitTime(long waitTime) {
		this.waitTime = waitTime;
	}
	
	/**
	 * 用于对符合正则表达式的控件名称设置等待时间
	 * 
	 * @param regex    正则表达式
	 * @param waitTime 等待时间
	 */
	public void setContorlWaitTime(String regex, long waitTime) {
		controlWaitTime.put(regex, waitTime);
	}
	
	/**
	 * 用于设置指向存储元素定位方式的xml文件对象，并根据传参，判断窗体是否需要回到顶层
	 * @param xmlFile 存储元素定位方式的xml文件对象
	 * @param isBreakRootFrame 是否需要将窗体切回到顶层
	 */
	public void setXmlFile(File xmlFile, boolean isBreakRootFrame) {
		if (xml == null) {
			xml = new ReadXml(xmlFile);
		} else {
			xml.setXmlPath(xmlFile);
		}
		
		//若需要切回顶层，则切换到顶层窗体
		if (isBreakRootFrame) {
			brower.getDriver().switchTo().defaultContent();
		}
	}
	
	/**
	 * <p>
	 * 用于根据传入的控件名称或定位方式，对控件在页面上定位，返回其WebElement对象。形参可以传入在xml文件中元素的名称，
	 * 亦可以传入页面元素的定位方式，但目前识别只支持xpath和css两种方式。
	 * 该方法获取的是一组元素，可用于对列表元素事件中。
	 * </p>
	 * <p>
	 * 元素识别判断方式按照以下步骤进行：<br>
	 *  1.先对xml文件进行扫描，若存在该元素对应的标签，则读取xml文件的定位方式，并识别有效的定位方式一一匹配，直到正确为止；<br>
	 *  2.若在xml文件中查找不到该元素，则按照xpath和css的规则进行匹配，直到判断出该元素的定位方式位置；<br>
	 *  3.若仍找不到元素，则抛出UnrecognizableLocationModeException
	 *  </p>
	 * 
	 * @param name   元素名称或元素的定位方式
	 * @return 返回页面一组元素的定位方式对象
	 * @throws TimeoutException 元素在指定时间内未查找到时，抛出的异常
	 * @throws UnrecognizableLocationModeException 元素无法识别时抛出的异常
	 */
	List<By> recognitionElement(ElementInformation elementInformation) {
		if (isXmlElement(elementInformation.name)) {
			//若指定了xml文件，且传入的元素名称存在与xml文件中，则判断元素相应的定位方式及定位内容
			return recognitionXmlElement(elementInformation);
		} else {
			//若未指定xml文件，或者在xml文件中无法查到相应的元素时，则将name的值赋给value，且根据value判断相应定位方式
			return recognitionCommonElement(elementInformation);
		}
	}

	/**
	 * 获取普通元素的By对象
	 * @param elementInformation 元素信息类对象
	 * @return 元素信息指向的By对象
	 */
	private List<By> recognitionCommonElement(ElementInformation elementInformation) {
		List<By> byList = new ArrayList<>();
		
		//判断传入的ByType对象是否为null
		if (elementInformation.byType == null) {
			byList.addAll(getCommonElementBy(elementInformation.name));
		} else {
			byList.add(valueToBy(elementInformation.name, elementInformation.byType));
		}
		
		return byList;
	}

	/**
	 * 获取xml文件内元素的By对象
	 * @param elementInformation 元素信息类对象
	 * @return 元素信息指向的By对象
	 */
	private List<By> recognitionXmlElement(ElementInformation elementInformation) {
		//查找元素的父层级结构
		elementInformation.iframeList = getParentFrameName(elementInformation.name);
		
		//存储元素的By对象集合
		List<By> byList = new ArrayList<>();
		
		//判断传入的ByType对象是否为null
		if (elementInformation.byType == null) {
			byList.addAll(getXmlElementBy(elementInformation.name, elementInformation.linkKeyList));
		} else {
			byList.add(xml.getBy(elementInformation.name, elementInformation.byType, elementInformation.linkKeyList));
		}
		
		return byList;
	}
	
	/**
	 * 用于返回控件的等待时间，若设置单个控件的等待时间（使用{@link #setContorlWaitTime(String, long)}方法设置），
	 * 则返回设置的控件等待时间；若未设置单个控件的等待时间，则返回设置的通用等待时间（使用{@link #setWaitTime(long)}方法）
	 * ；若未对通用时间进行设置，则返回默认时间（{@link #waitTime}）
	 * @param name 控件名称
	 * @return 相应控件的等待时间
	 * @see #setContorlWaitTime(String, long)
	 * @see #setWaitTime(long)
	 */
	long getWaitTime(String name) {
		for (String regex : controlWaitTime.keySet()) {
			if (Pattern.compile(regex).matcher(name).matches()) {
				return controlWaitTime.get(regex);
			}
		}
		
		return waitTime;
	}
	
	/**
	 * 用于获取元素在xml文件中所有的父窗体，并以集合的形式返回，存储的顺序为父窗体在前，子窗体在后，若当前元素没有窗体，
	 * 则集合的长度为0
	 * @param name 元素在xml文件中的名称
	 * @return 元素在xml文件中所有的父窗体集合
	 */
	List<String> getParentFrameName(String name) {
		//存储获取到的父层窗体名称
		List<String> nameList = new ArrayList<String>();
		
		//获取元素所在窗体的名称
		String iframeName = xml.getIframeName(name);
		//循环，判断窗体是否存在（方法返回不为空），若存在，则再将父窗体进行存储
		while(!iframeName.isEmpty()) {
			//存储窗体
			nameList.add(iframeName);
			//再以当前窗体的名称再次获取该窗体的父窗体
			iframeName = xml.getIframeName(iframeName);
		}
		
		return nameList;
	}

	/**
	 * 用于根据传入的参数，识别非xml文件内的元素定位方式。
	 * 该方法能快速识别xpath定位方式以及绝对css定位方式，若不是以上两种定位方式
	 * 则会遍历所有的定位方式，此时会降低运行速度，建议在不是以上两种定位方式的
	 * 情况下，直接指定元素的定位方式，以提高效率
	 */
	List<By> getCommonElementBy(String value) {
		List<By> byList = new ArrayList<>();
		// 如果抛出元素名称查找不到的的异常，则对应匹配xpath和绝对css路径两种定位方式
		// 匹配xpath定位，判定方法，判断text的第一个字符是否是“/”
		//由于是识别普通元素，非xml元素其value就是元素的名称name， 故获取等待时间时可直接将value传入
		if (value.indexOf("/") == 0) {
			byList.add(valueToBy(value, ByType.XPATH));
		} else if (value.indexOf("html") == 0) {
			//在页面中查找元素，若元素能找到，则结束查找
			byList.add(valueToBy(value, ByType.CSS));
		} else {
			//若元素无法识别，则将所有的定位类型（排除xpath类型）与之进行对比，直到在页面上找到元素为止
			for(ByType type : ByType.values()) {
				if (type == ByType.XPATH) {
					continue;
				}
				
				byList.add(valueToBy(value, type));
			}
		}
		
		return byList;
		
	}
	
	/**
	 * 用于设置xml文件内的元素的定位方式及定位内容
	 */
	List<By> getXmlElementBy(String name, List<String> linkList) {
		//用于存储从xml文件中读取到的所有内容，并封装成By对象
		List<By> byList = new ArrayList<>();
		// 循环，逐个在页面上配对有效的标签对应的定位方式
		for (ByType mode : xml.getElementMode(name)) {
			byList.add(valueToBy(xml.getValue(name, mode, linkList), mode));
		}
		
		return byList;
	}
	
	/**
	 * 根据元素的参数，返回元素的By对象
	 * @return 元素的By对象
	 */
	By valueToBy(String value, ByType byType) {
		//根据元素的定位方式，对定位内容进行选择，返回相应的By对象
		switch (byType) {
		case XPATH: 
			return By.xpath(value);
		case CLASSNAME:
			return By.className(value);
		case CSS:
			return By.cssSelector(value);
		case ID:
			return By.id(value);
		case LINKTEXT:
			return By.linkText(value);
		case NAME:
			return By.name(value);
		case TAGNAME:
			return By.tagName(value);
		default:
			throw new UnrecognizableLocationModeException("无法识别的定位类型：" + byType);
		}
	}
	
	/**
	 * 用于判断元素是否为xml文件内的元素
	 * @param name 元素名称
	 * @return 是否为xml文件内的元素
	 */
	boolean isXmlElement(String name) {
		return (xml != null && xml.isElement(name));
	}
	
	/**
	 * 用于向元素中添加完整的父窗体结构
	 * @param element 需要添加窗体的元素
	 * @param elementInformation 元素信息
	 */
	void setIframeElement(Element element, ElementInformation elementInformation) {
		//指向当前元素，用于设置当前元素的父层窗体元素
		Element childElement = element;
		//使用Iterator
		Iterator<String> it = elementInformation.iframeList.iterator();
		//循环，一一设置父层结构
		do {
			//获取父层的元素的名称
			String iframeName = it.next();
			//构造父层元素
			Element iframeElement = new Element(brower, iframeName, ElementType.COMMON_ELEMENT);
			//设置父层元素的获取方式及等待时间
			iframeElement.setWaitTime(getWaitTime(iframeName));
			iframeElement.setByList(getXmlElementBy(iframeName, null));
			iframeElement.setElementIndex(1);
			
			//设置父层
			childElement.setIframeElement(iframeElement);
			//将子元素指向父层元素，以获取下一层窗体元素
			childElement = iframeElement;
		} while(it.hasNext());
	}
	
	/**
	 * <p><b>文件名：</b>AbstractElement.java</p>
	 * <p><b>用途：</b>
	 * 存储获取元素时的信息
	 * </p>
	 * <p><b>编码时间：</b>2020年5月9日上午7:57:24</p>
	 * <p><b>修改时间：</b>2020年5月22日上午8:18:39</p>
	 * @author 彭宇琦
	 * @version Ver1.1
	 * @since JDK 1.8
	 *
	 */
	class ElementInformation {
		/**
		 * 存储元素的名称或定位内容
		 */
		public String name;
		/**
		 * 存储元素的定位方式
		 */
		public ByType byType;
		/**
		 * 用于标记元素的类型
		 */
		public ElementType elementType;
		/**
		 * 用于存储在xml文件中需要外链的词语
		 */
		public List<String> linkKeyList;
		/**
		 * 用于存储元素的父层结构
		 */
		public List<String> iframeList;
		/**
		 * 构造元素信息
		 * @param name 元素名称
		 * @param byType 元素定位的By类型，枚举
		 * @param elementType 元素类型，枚举
		 * @param linkKeyList xml文件中需要外部替换的词语
		 */
		public ElementInformation(String name, ByType byType, ElementType elementType, List<String> linkKeyList) {
			super();
			this.name = name;
			this.byType = byType;
			this.elementType = elementType;
			this.linkKeyList = linkKeyList;
		}
		
		/**
		 * 用于判断其元素的使用的外链xml文件词语是否与存储的内容一致
		 * @param linkKey 需要判断的外链xml文件词语
		 * @return 是否与类中存储的词语一致
		 */
		public boolean linkKeyEquals(List<String> linkKey) {
			//判断传入的linkKey是否为null，或未传入的linkKey为空，为空，则直接判断linkKeyList的状态
			if (linkKey == null || linkKey.size() == 0) {
				//若linkKeyList为null或为空，则可直接返回true；反之，则返回false
				if (linkKeyList == null || linkKeyList.size() == 0) {
					return true;
				} else {
					return false;
				}
				
			} 
			
			//判断linkKey中的元素是否完全存在于linkKeyList，且两个集合的长度一致，若存在一项判断不符合，则返回false
			if (linkKeyList.containsAll(linkKey) && linkKeyList.size() == linkKey.size()) {
				return true;
			} else {
				return false;
			}
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ElementInformation other = (ElementInformation) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			if (byType != other.byType)
				return false;
			if (elementType != other.elementType)
				return false;
			if (iframeList == null) {
				if (other.iframeList != null)
					return false;
			} else if (!iframeList.equals(other.iframeList))
				return false;
			if (linkKeyList == null) {
				if (other.linkKeyList != null)
					return false;
			} else if (!linkKeyList.equals(other.linkKeyList))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}
		
		private AbstractBy getEnclosingInstance() {
			return AbstractBy.this;
		}
		
	}
}
