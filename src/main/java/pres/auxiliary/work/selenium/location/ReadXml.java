package pres.auxiliary.work.selenium.location;

import java.io.File;
import java.util.ArrayList;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import pres.auxiliary.work.selenium.element.ElementType;
import pres.auxiliary.work.selenium.xml.ByType;

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
	
	public ReadXml(File xmlFile) throws DocumentException {
		dom = new SAXReader().read(xmlFile);
	}
	
	@Override
	public ArrayList<ByType> getElementByTypeList(String name) {
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
	public ArrayList<String> getValueList(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ElementType getElementType(String name) {
		//查询元素
		Element element = getElementLabelElement(name);
		//获取元素的元素类型属性
		String elementTypeText = element.attributeValue("element_type");
		//若属性不存在，则使其指向普通元素
		elementTypeText = elementTypeText == null ? "0" : elementTypeText;
		
		//转换元素类型枚举，并返回
		switch (elementTypeText) {
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
			break;
		}
				
		return null;
	}

	@Override
	public ArrayList<String> getIframeNameList(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getWaitTime(String name) {
		// TODO Auto-generated method stub
		return 0;
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
			throw new IllegalArgumentException("Unexpected value: " + labelName);
		}
	}
	
	/**
	 * 用于返回元素标签对象
	 * @param name 元素名称
	 * @return 相应的元素标签类对象
	 */
	private Element getElementLabelElement(String name) {
		//定义获取元素的xpath
		String selectElementXpath = "//element[@name='" + name +"']";
		return (Element) dom.selectSingleNode(selectElementXpath);
	}
}
