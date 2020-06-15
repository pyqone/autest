package pres.auxiliary.work.selenium.xml;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import pres.auxiliary.directory.exception.IncorrectDirectoryException;
import pres.auxiliary.directory.operate.MakeDirectory;

/**
 * 该类用于向xml文件中写入页面元素的定位方式
 * @author 彭宇琦
 * @version Ver0.7
 */
public class WriteXml {
	// 用于设置到xml文件存放的路径
	private StringBuilder xmlPath;
	// 用于读取XML文件
	private Document dom;
	
	/**
	 * 用于构造对象，并设置xml文件的路径
	 * 
	 * @param xmlPath
	 *            代读取的xml文件路径
	 */
	public WriteXml(String xmlPath) {
		setXmlPath(xmlPath);
	}

	/**
	 * 该方法用于返回xml文件存放的位置
	 * 
	 * @return xml文件存放的位置
	 */
	public String getXmlPath() {
		return xmlPath.toString();
	}

	/**
	 * 该方法用于重新设置xml文件存放的路径，并将Document对象指向新的XML文件
	 * 
	 * @param xmlPath
	 *            xml的存放路径
	 */
	public void setXmlPath(String xmlPath) {
		// 将传入的参数进行格式化
		StringBuilder sb = new StringBuilder(xmlPath);
		sb = MakeDirectory.formatPath(sb);

		// 判断格式化后的文件路径格式是否符合windonws下文件路径的命名规则,不符合则抛出异常
		if (!MakeDirectory.isPath(sb.toString())) {
			throw new IncorrectDirectoryException("不合理的文件夹路径，文件路径："
					+ sb.toString());
		}

		// 重新构造Document对象，使其指向新的XML文件
		try {
			dom = new SAXReader().read(sb.toString());
		} catch (DocumentException e) {
			// 若抛出异常，则将异常转换为自定义的IncorrectXMLFileException异常，使其不需要在编译时处理
			throw new IncorrectXmlPathException("XML文件不正确或XML路径不正确");
		}
		
		// 将文件路径设置入属性中
		this.xmlPath = sb;
	}
	
	/**
	 * 该方法用于向xml文件中添加页面元素的定位方式
	 * @param name 页面元素的名称
	 * @param mode 定位的方式
	 * @param value 定位路径
	 * @return
	 */
	public boolean writeElement(String name, String mode, String value) {
		//获取根元素
		Element e = dom.getRootElement();
		//在根元素下方添加新元素
		e.addElement("element").addAttribute("name", name).addText(value);
		
		return true;
	}
	
}
