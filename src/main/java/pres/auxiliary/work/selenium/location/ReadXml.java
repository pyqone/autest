package pres.auxiliary.work.selenium.location;

import java.io.File;
import java.util.ArrayList;

import org.dom4j.Document;
import org.dom4j.DocumentException;
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
	public ArrayList<ByType> getElementByTypeList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> getValueList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ElementType getElementType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> getIframeNameList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getWaitTime() {
		// TODO Auto-generated method stub
		return 0;
	}
}
