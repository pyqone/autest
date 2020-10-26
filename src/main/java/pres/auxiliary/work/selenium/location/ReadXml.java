package pres.auxiliary.work.selenium.location;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import pres.auxiliary.work.selenium.element.ElementType;

/**
 * <p>
 * <b>文件名：</b>ReadXml.java
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
 * <p>
 * <b>修改时间：</b>2020年9月29日上午9:40:40
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 8
 *
 */
public class ReadXml extends AbstractRead {
	/**
	 * 存储构造后的Document类对象，以读取xml文件中的内容
	 */
	private Document dom;
	
	/**
	 * 根据xml文件对象进行构造
	 * @param xmlFile xml文件对象
	 * @throws IncorrectFileException xml文件有误时抛出的异常
	 */
	public ReadXml(File xmlFile) {
		//将编译时异常转换为运行时异常
		try {
			dom = new SAXReader().read(xmlFile);
		} catch (DocumentException e) {
			throw new IncorrectFileException("xml文件异常，文件位置：" + xmlFile.getAbsolutePath());
		}
		
	}
	
	/**
	 * 根据xml文件的{@link Document}对象进行构造
	 * @param dom {@link Document}对象
	 */
	public ReadXml(Document dom) {
		this.dom = dom;
	}
	
	@Override
	public ArrayList<ByType> findElementByTypeList(String name) {
		ArrayList<ByType> byTypeList = new ArrayList<ByType>();
		//查询元素
		Element element = getElementLabelElement(name);
		
		//遍历元素下所有的定位标签，并将其转换为相应的ByType枚举，存储至byTypeList中
		for (Object byElement : element.elements()) {
			byTypeList.add(getByType(((Element)byElement).getName()));
		}
		
		return byTypeList;
	}

	@Override
	public ArrayList<String> findValueList(String name) {
		ArrayList<String> valueList = new ArrayList<>();
		//查询元素
		Element element = getElementLabelElement(name);
		
		//遍历元素下所有的定位标签，并将其转换为相应的ByType枚举，存储至byTypeList中
		for (Object byElement : element.elements()) {
			//判断元素是否启用，若元素未启用，则下一个循环
			String isUserText = ((Element) byElement).attributeValue("is_user");
			if (isUserText != null && !Boolean.valueOf(isUserText)) {
				continue;
			}
			
			//判断元素是否启用模板，若启用模板，则获取模板内容，并将定位内容进行转换
			String tempId = ((Element) byElement).attributeValue("temp_id");
			String value = tempId != null ? 
					getTemplateValue(tempId, getByType(((Element) byElement).getName())) :
					((Element)byElement).getText();
			
			valueList.add(replaceValue(((Element) byElement), value));
		}
		
		return valueList;
	}

	@Override
	public ElementType findElementType(String name) {
		//查询元素
		Element element = getElementLabelElement(name);
		//若元素标签为iframe，则无法获取属性，直接赋予窗体类型
		if (element.getName().equals("iframe")) {
			return toElementType("4");
		} else {
			//非窗体元素，则获取元素的元素类型属性
			String elementTypeText = element.attributeValue("element_type");
			//若属性不存在，则使其指向普通元素
			return toElementType(elementTypeText == null ? "0" : elementTypeText);
		}
	}

	@Override
	public ArrayList<String> findIframeNameList(String name) {
		//查询元素
		Element element = getElementLabelElement(name);
		
		//存储父层iframe元素名称
		ArrayList<String> iframeName = new ArrayList<>();
		
		//循环，获取元素的父层级，直到获取到顶层为止
		while (!element.isRootElement()) {
			//定位到元素的父层
			element = element.getParent();
			//判断当前元素的标签名称，若为iframe标签，则获取其名称，并存储至集合中
			if (element.getName().equals("iframe")) {
				iframeName.add(element.attributeValue("name"));
			}
		}
		
		//反序集合，使最上层窗体排在最前面
		Collections.reverse(iframeName);
		return iframeName;
	}

	@Override
	public long findWaitTime(String name) {
		//查询元素
		Element element = getElementLabelElement(name);
		//获取元素存储等待时间属性值，并转换为long类型
		try {
			String text = element.attributeValue("wait");
			//将属性值进行转换，若属性值不存在，则赋为-1
			long time = Long.valueOf(text == null ? "-1" : text);
			//若转换的时间小于0，则返回-1
			return time < 0 ? -1L : time;
		} catch (NumberFormatException e) {
			return -1L;
		}
	}
	
	/**
	 * 该方法用于根据标签的名称，返回相应的定位方式枚举
	 * @param labelName 标签名称
	 * @return {@link ByType}枚举
	 */
	private ByType getByType(String labelName) {
		switch (labelName) {
		case "xpath":
			return ByType.XPATH;
		case "css":
			return ByType.CSS;
		case "classname":
			return ByType.CLASSNAME;
		case "id":
			return ByType.ID;
		case "linktext":
			return ByType.LINKTEXT;
		case "name":
			return ByType.NAME;
		case "tagname":
			return ByType.TAGNAME;
		default:
			throw new IllegalArgumentException("不存在的定位方式: " + labelName);
		}
	}
	
	/**
	 * 用于返回元素标签对象
	 * @param name 元素名称
	 * @return 相应的元素标签类对象
	 * @throws UndefinedElementException 元素不存在时抛出的异常
	 */
	private Element getElementLabelElement(String name) {
		//定义获取元素的xpath
		String selectElementXpath = "//*[@name='" + name +"']";
		//根据xpath获取元素，若无法获取到元素，则抛出异常
		Element element = (Element) dom.selectSingleNode(selectElementXpath);
		if (element != null) {
			return element;
		} else {
			throw new UndefinedElementException("不存在的元素名称：" + name);
		}
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
		//根据xpath获取元素，若无法获取到元素，则抛出异常
		Element element = (Element) dom.selectSingleNode(selectTempXpath);
		if (element != null) {
			return element.getText();
		} else {
			throw new UndefinedElementException("不存在的模板：" + selectTempXpath);
		}
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
}
