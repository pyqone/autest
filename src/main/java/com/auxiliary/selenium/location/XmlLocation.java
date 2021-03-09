package com.auxiliary.selenium.location;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.auxiliary.selenium.element.ElementType;
import com.auxiliary.selenium.location.UndefinedElementException.ExceptionElementType;

/**
 * <p>
 * <b>文件名：</b>XmlLocation.java
 * </p>
 * <p>
 * <b>用途：</b>该类用于从指定格式的xml中读取元素信息<br/>
 * 
 * <b>注意：</b>
 * <ol>
 * <li>标签的name属性必须唯一，否则读取会出现错误
 * <li>不同类型的定位模板可以使用一个id属性，但同一种类型的定位模板id属性是唯一的，
 * 如xpath模板可以使用id='1'，css模板可以使用id='1'，但另一xpath模板的id属性就不能
 * 再定为1，但建议模板的id也唯一存在
 * <li>元素定位标签只能写xpath、css、classname、id、linktext、name、tagname
 * <li>所有标签均为小写
 * <ol>
 * </p>
 * <p>
 * <b>编码时间：</b>2017年9月25日下午4:23:40
 * </p>
 * <p><b>修改时间：</b>2021年3月8日上午8:08:45</p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 8
 *
 */
public class XmlLocation extends AbstractLocation {
	/**
	 * 存储构造后的Document类对象，以读取xml文件中的内容
	 */
	private Document readDom;
	
	/**
	 * 用于存储指向的父层文件对象
	 */
	private ArrayList<XmlLocation> parentLocationList = new ArrayList<>();
	
	/**
	 * 根据xml文件对象进行构造
	 * @param xmlFile xml文件对象
	 * @throws IncorrectFileException xml文件有误时抛出的异常
	 */
	public XmlLocation(File xmlFile) {
		//将编译时异常转换为运行时异常
		try {
			readDom = new SAXReader().read(xmlFile);
		} catch (DocumentException e) {
			throw new IncorrectFileException("xml文件异常，文件位置：" + xmlFile.getAbsolutePath(), e);
		}
		
		// 若存在父层元素，则对父层文件进行存储
		readParentFile();
	}
	
	/**
	 * 根据xml文件的{@link Document}对象进行构造
	 * @param dom {@link Document}对象
	 */
	public XmlLocation(Document dom) {
		this.readDom = dom;
		
		// 若存在父层元素，则对父层文件进行存储
		readParentFile();
	}
	
	@Override
	@Deprecated
	public ArrayList<ByType> findElementByTypeList(String name) {
		return find(name).getElementByTypeList();
	}

	@Override
	@Deprecated
	public ArrayList<String> findValueList(String name) {
		return find(name).getValueList();
	}

	@Override
	@Deprecated
	public ElementType findElementType(String name) {
		return find(name).getElementType();
	}

	@Override
	@Deprecated
	public ArrayList<String> findIframeNameList(String name) {
		return find(name).getIframeNameList();
	}

	@Override
	@Deprecated
	public long findWaitTime(String name) {
		return find(name).getWaitTime();
	}
	
	/**
	 * 用于查找元素的所有定位方式集合，并进行缓存
	 * @param element 元素对象
	 */
	private void saveElementByTypeList(Element element) {
		//获取标签名称
		element.elements().stream().map(lable -> lable.getName())
				//将名称转换为ByType枚举
				.map(this::toByType)
				//过滤掉返回为null的元素
				.filter(lable -> lable != null)
				//存储标签
				.forEach(byTypeList::add);
	}
	
	/**
	 * 用于查找元素定位内容集合，并进行缓存
	 * @param element 元素对象
	 */
	private void saveValueList(Element element) {
		//查询元素，遍历元素下所有的定位标签，并过滤掉元素标签
		element.elements().stream().filter(ele -> !"element".equals(ele.getName()))
				//过滤不启用的标签
				.filter(ele -> {
					return Optional.ofNullable(ele.attributeValue("is_user"))
							.filter(t -> !t.isEmpty())
							.map(t -> {
								try {
									return Boolean.valueOf(t).booleanValue();
								} catch (Exception e) {
									return true;
								}
							}).orElse(true);
				//根据值或模板，将定位内容转译，并存储至valueList
				}).forEach(ele -> {
					String value = "";
					String tempId = Optional.ofNullable(ele.attributeValue("temp_id")).orElse("");
					if (tempId.isEmpty()) {
						value = ele.getText();
					} else {
						value = getTemplateValue(tempId, toByType(ele.getName()));
					}
					
					valueList.add(replaceValue(ele, value));
				});
	}
	
	/**
	 * 用于查找元素的类型，并进行缓存
	 * @param element 元素对象
	 */
	private void saveElementType(Element element) {
		//若元素标签为iframe，则无法获取属性，直接赋予窗体类型
		if ("iframe".equals(element.getName())) {
			elementType =  ElementType.IFRAME_ELEMENT;
		} else {
			//非窗体元素，则获取元素的元素类型属性
			String elementTypeText = element.attributeValue("element_type");
			//若属性不存在，则使其指向普通元素
			elementType =   toElementType(elementTypeText == null ? "0" : elementTypeText);
		}
	}
	
	/**
	 * 用于查找元素的所有父窗体名称集合，并进行缓存
	 * @param element 元素对象
	 */
	private void saveIframeNameList(Element element) {
		//循环，获取元素的父层级，直到获取到顶层为止
		while (!element.isRootElement()) {
			//定位到元素的父层
			element = element.getParent();
			//判断当前元素的标签名称，若为iframe标签，则获取其名称，并存储至集合中
			if (element.getName().equals("iframe")) {
				iframeNameList.add(element.attributeValue("name"));
			}
		}
		
		//反序集合，使最上层窗体排在最前面
		Collections.reverse(iframeNameList);
	}
	
	/**
	 * 用于查找元素的等待时间，并进行缓存
	 * @param element 元素对象
	 */
	private void saveWaitTime(Element element) {
		String waitText = element.attributeValue("wait");
		try {
			waitTime = Long.valueOf(waitText);
			//若等待时间小于0，则以-1赋值
			if (waitTime < 0L) {
				waitTime = -1L;
			}
		} catch (NumberFormatException e) {
			waitTime = -1L;
		}
	}
	
	/**
	 * 用于返回元素标签对象
	 * @param name 元素名称
	 * @return 相应的元素标签类对象
	 * @throws UndefinedElementException 元素不存在时抛出的异常
	 */
	private Element getElementLabelElement(String name) {
		//将元素名称转换为查找使用的xpath
		String xpath = "//*[@name='" + name + "']";
		
		//根据xpath查找元素
		return Optional.ofNullable(xpath2Element(xpath))
				.orElseThrow(() -> new UndefinedElementException(name, ExceptionElementType.ELEMENT));
  	}
	
	/**
	 * 获取模板中的定位内容
	 * @param tempId 模板id
	 * @param byType 定位方式枚举
	 * @return 相应的定位内容
	 * @throws UndefinedElementException 模板不存在时抛出的异常
	 */
	private String getTemplateValue(String tempId, ByType byType) {
		String selectTempXpath = "//templet/" + byType.getValue() + "[@id='" + tempId + "']";
		
		/*
		//根据xpath获取元素，若无法获取到元素，则抛出异常
		Element element = (Element) readDom.selectSingleNode(selectTempXpath);
		if (element != null) {
			return element.getText();
		} else {
			throw new UndefinedElementException(tempId, ExceptionElementType.TEMPLET);
		}
		*/
		return Optional.ofNullable(xpath2Element(selectTempXpath))
				.orElseThrow(() -> new UndefinedElementException(tempId, ExceptionElementType.TEMPLET))
				.getText();
	}
	
	/**
	 * 根据xpath在指定的XmlLocation对象中查找元素
	 * @param xml XmlLocation对象
	 * @param xpath 查询元素的xpath
	 * @return 被查询的元素，无元素时返回null
	 */
	private Element xpath2Element(String xpath) {
		//在当前层级查找节点
		Element element = (Element) (readDom.selectSingleNode(xpath));
		//判断节点是否存在，若存在，则返回元素
		if (element != null) {
			return element;
		} else {
			//若当前不存在节点，则循环查找所有的父层元素，并调用父层的该方法，直到找到元素为止
			for (XmlLocation x : parentLocationList) {
				element = x.xpath2Element(xpath);
				if (element != null) {
					return element;
				}
			}
		}
		
		//若遍历完父层级仍无法找到元素，则返回null
		return null;
	}
	
	/**
	 * 用于对获取的元素内容进行转换，得到完整的定位内容
	 * @param element 元素类对象
	 * @param value 定位内容
	 * @return 完整的定位内容
	 */
	private String replaceValue(Element element, String value) {
		//判断元素是否存在需要替换的内容，若不存在，则不进行替换
		if (value.indexOf(START_SIGN) == -1) {
			return value;
		}
		
		String repalceText = "";
		
		//遍历元素的所有属性，并一一进行替换
		for (Object att : element.attributes()) {
			//定义属性替换符
			repalceText = MATCH_START_SIGN + ((Attribute) att).getName() + MATCH_END_SIGN;
			//替换value中所有与repalceText匹配的字符
			value = value.replaceAll(repalceText, ((Attribute) att).getValue());
		}
		
		//替换父层节点的name属性
		repalceText = MATCH_START_SIGN + "name" + MATCH_END_SIGN;
		//替换value中所有与repalceText匹配的字符
		value = value.replaceAll(repalceText, element.getParent().attributeValue("name"));
		
		return value;
	}
	
	/**
	 * 用于读取父层文件路径，并封装成File对象后，进行存储
	 */
	private void readParentFile() {
		Optional.ofNullable(readDom.getRootElement().element("parents")).map(e -> e.elements("parent"))
				.filter(list -> !list.isEmpty()).ifPresent(list -> {
					list.stream().map(Element::getText).map(File::new)
							.map(XmlLocation::new).forEach(parentLocationList::add);
				});
	}

	@Override
	public ReadLocation find(String name) {
		// 判断传入的名称是否正确
		String newName = Optional.ofNullable(name).filter(n -> !n.isEmpty())
				.orElseThrow(() -> new UndefinedElementException(name, ExceptionElementType.ELEMENT));
		
		// 判断当前查找的元素是否与原元素名称一致，不一致，则进行元素查找
		if (!newName.equals(this.name)) {
			//清除原始缓存
			clearCache();
			
			//查找元素
			Element element = getElementLabelElement(newName);
			//根据元素对象查找元素信息，并缓存
			saveElementByTypeList(element);
			saveElementType(element);
			saveIframeNameList(element);
			saveValueList(element);
			saveWaitTime(element);
		}
		
		return this;
	}
}
