package pres.auxiliary.work.selenium.xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import pres.auxiliary.directory.exception.IncorrectDirectoryException;
import pres.auxiliary.directory.operate.MakeDirectory;

/**
 * <p><b>文件名：</b>CreateXML.java</p>
 * <p><b>用途：</b>用于通过指定样式的txt文件，来创建自动化测试所需要的xml文件</p>
 * <p><b>编码时间：</b>2019年6月1日上午11:41:02</p>
 * <p><b>修改时间：</b>2019年6月1日上午11:41:02</p>
 * @author 彭宇琦
 *
 */
public class CreateXml {
	// 用于设置到xml文件存放的路径
	private static StringBuilder xmlPath = new StringBuilder(
			"C:\\AutoTest\\Xml\\");
	private static StringBuilder xmlName = new StringBuilder("");
	// 用于存储用户选择的定位方式标签
	private static StringBuilder mode = new StringBuilder("");

	// 定义字段
	/** 添加xpath标签 */
	public static final short XPATH = 0;
	/** 添加css标签 */
	public static final short CSS = 1;
	/** 添加id标签 */
	public static final short ID = 2;
	/** 添加linktext标签 */
	public static final short LINKTEXT = 3;
	/** 添加name标签 */
	public static final short NAME = 4;
	/** 添加tagname标签 */
	public static final short TAGNAME = 5;
	/** 添加classname标签 */
	public static final short CLASSNAME = 6;
	/** 添加default（默认）标签 */
	public static final short DEFAULT = 7;
	

	/**
	 * 该方法用于返回xml文件的名称（不带后缀）
	 * 
	 * @return xml文件的名称
	 */
	public static String getXmlName() {
		return xmlName.toString();
	}

	/**
	 * 该方法用于设置xml文件的名称，传入的名称不需要添加后缀，不能为中文
	 * 
	 * @param xmlName
	 *            xml文件的名称
	 */
	public static void setXmlName(String xmlName) {
		// 判断传入的测试结果文件名称是否符合windows下的命名规则，若不符合，则抛出IncorrectDirectoryException异常
		if (!MakeDirectory.isFileName(xmlName)) {
			throw new IncorrectDirectoryException("不合理的文件名称，文件名称：" + xmlName);
		}

		// 通过判断后，则清空xmlName存储的信息并将新的文件名称放入xmlName种属性中
		CreateXml.xmlName.delete(0, CreateXml.xmlName.length());
		CreateXml.xmlName.append(xmlName);
	}

	/**
	 * 该方法用于返回xml文件存放的位置
	 * 
	 * @return xml文件存放的位置
	 */
	public static String getXmlPath() {
		return xmlPath.toString();
	}

	/**
	 * 该方法用于重新设置xml文件存放的路径，并将Document对象指向新的XML文件
	 * 
	 * @param xmlPath
	 *            xml的存放路径
	 */
	public static void setXmlPath(String xmlPath) {
		// 将传入的参数进行格式化
		StringBuilder sb = new StringBuilder(xmlPath);
		sb = MakeDirectory.formatPath(sb);

		// 判断格式化后的文件路径格式是否符合windonws下文件路径的命名规则,不符合则抛出异常
		if (!MakeDirectory.isPath(sb.toString())) {
			System.out.println(sb.toString());
			throw new IncorrectDirectoryException("不合理的文件夹路径，文件路径："
					+ sb.toString());
		}

		// 将文件路径设置入属性中
		CreateXml.xmlPath = sb;
	}

	/**
	 * 该方法用于返回使用的定位标签的情况，
	 * 
	 * @return 正在使用的定位标签
	 */
	public static String getMode() {
		StringBuilder sb = new StringBuilder();
		sb.append("mode [ ");

		// 判断mode包含的标签
		if (mode.indexOf("0") > -1) {
			sb.append("xpath, ");
		}
		if (mode.indexOf("1") > -1) {
			sb.append("css, ");
		}
		if (mode.indexOf("2") > -1) {
			sb.append("id, ");
		}
		if (mode.indexOf("3") > -1) {
			sb.append("linktext, ");
		}
		if (mode.indexOf("4") > -1) {
			sb.append("name, ");
		}
		if (mode.indexOf("5") > -1) {
			sb.append("tagname, ");
		}
		if (mode.indexOf("6") > -1) {
			sb.append("classname, ");
		}

		// 删除多余的逗号
		sb.delete(sb.lastIndexOf(","), sb.lastIndexOf(",") + 1);
		sb.append("]");

		return sb.toString();
	}

	/**
	 * 该方法用于设置需要使用的定位标签，可传入多个参数，若该标签的定位方式已存在，则不重复添加
	 * 
	 * @param modes
	 *            传入定位方式组
	 */
	public static void setMode(int... modes) {
		for (int mode : modes) {
			// 判断mode中是否已经存在定位模型，不存在则添加该模型
			if (CreateXml.mode.indexOf(String.valueOf(mode)) < 0) {
				CreateXml.mode.append(String.valueOf(mode));
			}
		}
	}

	/**
	 * 该方法通过存储的xml文件名创建一个空的xml文件，相当于导出一个模版
	 * 
	 * @throws XmlFileNameIsNullException
	 *             当xml文件名为空时抛出
	 * @throws RepeatedXmlFileNameException
	 *             当文件夹中存在与定义的xml文件名同名时抛出（运行时异常）
	 * 
	 * @see #create(String...)
	 * @see #create(List)
	 * @see #create(String, List)
	 */
	public static void create() throws XmlFileNameIsNullException {
		create("");
	}

	/**
	 * 该方法通过存储的xml文件名称创建一个已定义元素名称的xml文件， 传入的名称个数即为创建xml文件内元素的个数
	 * 
	 * @param names
	 *            传入的元素名称组
	 * @throws XmlFileNameIsNullException
	 *             当xml文件名为空时抛出
	 * @throws RepeatedXmlFileNameException
	 *             当文件夹中存在与定义的xml文件名同名时抛出（运行时异常）
	 * @see #create()
	 * @see #create(List)
	 * @see #create(String, List)
	 */
	public static void create(String... names)
			throws XmlFileNameIsNullException {
		create(Arrays.asList(names));
	}

	/**
	 * 该方法通过传入的xml文件名称创建一个已定义元素名称的xml文件， 传入的数组中存储的名称个数即为创建xml文件内元素的个数
	 * 
	 * @param xmlName
	 *            定义的xml文件名称
	 * @param names
	 *            传入的元素名称组
	 * @throws XmlFileNameIsNullException
	 *             当xml文件名为空时抛出
	 * @throws RepeatedXmlFileNameException
	 *             当文件夹中存在与定义的xml文件名同名时抛出（运行时异常）
	 * @see #create()
	 * @see #create(String...)
	 * @see #create(List)
	 */
	public static void create(String xmlName, List<String> names)
			throws XmlFileNameIsNullException {
		setXmlName(xmlName);
		create(names);
	}

	/**
	 * 该方法通过存储的xml文件名称创建一个已定义元素名称的xml文件， 传入的数组中存储的名称个数即为创建xml文件内元素的个数
	 * 
	 * @param names
	 *            传入的元素名称组
	 * @throws XmlFileNameIsNullException
	 *             当xml文件名为空时抛出
	 * @throws RepeatedXmlFileNameException
	 *             当文件夹中存在与定义的xml文件名同名时抛出（运行时异常）
	 * @see #create()
	 * @see #create(String...)
	 * @see #create(String, List)
	 */
	public static void create(List<String> texts)
			throws XmlFileNameIsNullException {
		// 判断xml文件的名称是否为空，若为空，则抛出异常
		if ("".equals(xmlName.toString())) {
			throw new XmlFileNameIsNullException("xml文件名为空");
		}
		
		// 创建存放xml的文件夹
		File xmlfile = new File(xmlPath.toString());
		xmlfile.mkdirs();
		// 将File对象定位到xml文件上
		xmlfile = new File(xmlfile, (xmlName.toString() + ".xml"));
		if (xmlfile.exists()) {
			throw new RepeatedXmlFileNameException("存在的文件名："
					+ (xmlName.toString() + ".xml"));
		}
		
		//创建空的xml文件
		Document dom = DocumentHelper.createDocument();
		
		//设置根元素，并设置根元素的属性
		dom.setRootElement(dom.addElement("project"));
		dom.getRootElement().addAttribute("name", xmlName.toString());
		
		//插入控件元素
		for (String text : texts) {
			//创建元素
			Element e = dom.getRootElement().addElement("element");
			
			String name = "";
			String value = "";
			short valueMode = -1;
			//判断传入的信息中是否包含分隔符（空格），包含则表示存在录入的数据，则直接写入到xml文件中
			if ( text.split(" ").length > 1 ) {
				name = text.split(" ")[0];
				//value = text.split(" ")[1]; 由于css路径是存在空格的，故不能按照切分的方法存入，应按照截取的方法，从第一个空格的下一位开始截取
				value  = text.substring(text.indexOf(" ") + 1);
				//判断传入结果的模型
				valueMode = judgeValue(value);
			} else {
				name = text;
			}
			
			//设置元素的name属性
			e.addAttribute("name", name);
			
			//创建定位方式元素
			// 定义空mode的开关，当mode为空时则开关打开，并在mode中添加所有的定位标签，最后，若该开关为开启状态，则清空mode中所有的内容
			boolean modeIsEmpty = false;
			// 判断mode中是否为空，为空则将开关打开，并想mode中添加所有标签的定位方式
			if ("".equals(mode.toString())) {
				modeIsEmpty = true;
				mode.append(String.valueOf(XPATH));
				mode.append(String.valueOf(CSS));
				mode.append(String.valueOf(ID));
				mode.append(String.valueOf(LINKTEXT));
				mode.append(String.valueOf(NAME));
				mode.append(String.valueOf(TAGNAME));
				mode.append(String.valueOf(CLASSNAME));
			}
			
			// 判断mode包含的标签
			if (mode.indexOf(String.valueOf(XPATH)) > -1 || valueMode == XPATH) {
				//添加节点
				Element childElement = e.addElement("xpath");
				childElement.addAttribute("is_use", "true");
				//添加value中的内容（若内容为空也不会将其添加到xml文件中，故此处可以直接这样写）
				if (valueMode == XPATH) {
					childElement.addText(value);
					//清空value中的内容，避免出现后面的标签也存入信息
					value = "";
				}
			}
			if (mode.indexOf(String.valueOf(CSS)) > -1 || valueMode == CSS) {
				//添加节点
				Element childElement = e.addElement("css");
				childElement.addAttribute("is_use", "true");
				//添加value中的内容（若内容为空也不会将其添加到xml文件中，故此处可以直接这样写）
				if (valueMode == CSS) {
					childElement.addText(value);
					//清空value中的内容，避免出现后面的标签也存入信息
					value = "";
				}
			}
			if (mode.indexOf(String.valueOf(ID)) > -1) {
				e.addElement("id").addAttribute("is_use", "true");
			}
			if (mode.indexOf(String.valueOf(LINKTEXT)) > -1) {
				e.addElement("linktext").addAttribute("is_use", "true");
			}
			if (mode.indexOf(String.valueOf(NAME)) > -1) {
				e.addElement("name").addAttribute("is_use", "true");
			}
			if (mode.indexOf(String.valueOf(TAGNAME)) > -1) {
				e.addElement("tagname").addAttribute("is_use", "true");
			}
			if (mode.indexOf(String.valueOf(CLASSNAME)) > -1) {
				e.addElement("classname").addAttribute("is_use", "true");
			}
			//如果存在模型值，但系统识别为非xpath和css时，则添加一个temp标签，对模型值进行存储
			if ( valueMode == DEFAULT ) {
				//添加注释
				e.addComment("当前节点作为临时存储节点，该节点在自动化测试中不被读取，请将节点的值放入到相应的标签中");
				//添加节点
				Element childElement = e.addElement("temp");
				childElement.addAttribute("is_use", "false");
				//存储模型值
				childElement.addText(value);
			}

			// 判断modeIsEmpty开关是否开启，若开启，则清空mode中的内容
			if (modeIsEmpty) {
				mode.delete(0, mode.length());
			}
		}

		/*
		// 用于存储xml文件的内容，并添加头文件
		StringBuilder sb = new StringBuilder();
		// 添加头标签
		sb.append("<?xml version=\"1.0\" encoding=\"GBK\"?>\r\n");
		// 添加dtd文件
//		sb.append("<!DOCTYPE project SYSTEM \"ElementDTD.dtd\">\r\n");
		// 添加根标签
		sb.append("<project name=\"" + xmlName.toString() + "\">\r\n");

		// 添加已有名称的标签
		for (String name : names) {
			sb.append(element(name));
			sb.append("\r\n");
		}

		sb.append("</project>\r\n");

		// 创建写入流
		try {
			FileWriter fw = new FileWriter(xmlfile);
			fw.write(sb.toString());
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		
		// 使用dom4j的漂亮格式
		OutputFormat format = OutputFormat.createPrettyPrint();
		// 设置xml文件的编码方式
		format.setEncoding("GBK");
		//关闭自闭和标签，若不设置此开关，则当元素无内容时其标签会被写为半闭合标签，但我希望标签成对出现
		format.setExpandEmptyElements(true);
		
		// 写入xml
		XMLWriter xmlWriter = null;
		try {
			xmlWriter = new XMLWriter(new FileOutputStream(xmlfile),
					format);
			xmlWriter.write(dom);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				xmlWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
//		createDTD();

	}

	/**
	 * 该方法用于向文件中读取控件名称并在指定的路径下创建带控件名称的xml文件，通过该方法创建的xml文件名称为读取的文件名称<br/>
	 * 注意，使用该方法时，除了后缀名前可以带“.”外 ，文件名称中不能再出现该符号，否则方法可能会报错
	 * 
	 * @param files
	 *            指定的文件，可传入多个
	 * @throws XmlFileNameIsNullException
	 *             当xml文件名为空时抛出
	 * @throws RepeatedXmlFileNameException
	 *             当文件夹中存在与定义的xml文件名同名时抛出（运行时异常）
	 */
	public static void create(File... files) throws XmlFileNameIsNullException {
		// 用于存储从文件中获取到的元素名称
		List<String> l = new ArrayList<>();

		// 循环，读取传入的文件
		for (File f : files) {
			// 判断该文件是否为文件夹，若为文件夹则继续循环
			if (f.isDirectory()) {
				continue;
			}

			// 根据符号“.”对获取的文件名称进行分割，使其在xml文件中不再带后缀
			setXmlName(f.getName().split("\\.")[0]);
			// 封装高效缓冲文件流
			BufferedReader br = null;
			try {
				br = new BufferedReader(new FileReader(f));
				// 循环，将读取到的元素名称存储l中，结束条件为，若l中最后一个元素为空，则表示上一次未读取到内容，测试跳出循环
				String s = br.readLine();
				while (s != null) {
					// 直接读取一行文本，并将其存入l中
					l.add(s);
					s = br.readLine();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// 调用类中的方法，将存储的控件名称转换成xml，若文件名重复，则跳过
			try {
				create(l);
			} catch (RepeatedXmlFileNameException e) {
				//TODO 控制台输出跳过创建的信息
				System.out.println(f.getName().split("\\.")[0] + ".xml文件已存在，跳过");
			}
			
			// 清空l中的所有内容，重新进行循环
			l.clear();
		}
	}
	
	/**
	 * 用于判断传入的值，但目前只能判断xpath和css，若非这两种，则返回default
	 */
	private static short judgeValue(String value) {
		// 如果抛出元素名称查找不到的的异常，则对应匹配xpath和css两种定位方式
		// 匹配xpath定位，判定方法，判断text的第一个字符是否是“/”
		if (value.indexOf("/") == 0) {
			return XPATH;
		} else if (value.indexOf("html") == 0) {
			// 匹配css，判断方法：判断text的前四个字符是否是“html”
			return CSS;
		} else {
			return DEFAULT;
		}
	}

	/**
	 * 该方法用于创建元素标签以及元素下的定位方式标签
	 * 
	 * @param name
	 *            element标签的name属性值
	 * @return 添加标签后的StringBuilder对象
	 */
	/*
	private static StringBuilder element(String name) {
		// 定义空mode的开关，当mode为空时则开关打开，并在mode中添加所有的定位标签，最后，若该开关为开启状态，则清空mode中所有的内容
		boolean modeIsEmpty = false;
		// 判断mode中是否为空，为空则将开关打开，并想mode中添加所有标签的定位方式
		if (mode.toString().equals("")) {
			modeIsEmpty = true;
			mode.append(String.valueOf(XPATH));
			mode.append(String.valueOf(CSS));
			mode.append(String.valueOf(ID));
			mode.append(String.valueOf(LINKTEXT));
			mode.append(String.valueOf(NAME));
			mode.append(String.valueOf(TAGNAME));
			mode.append(String.valueOf(CLASSNAME));
		}
		
		// 用于拼接xml文件内容
		StringBuilder sb = new StringBuilder();
		// 添加element标签
		sb.append("	<element name=\"" + name + "\">\r\n");
		// 添加所有的定位方式标签
		// 判断mode包含的标签
		if (mode.indexOf("0") > -1) {
			sb.append("		<xpath is_use=\"true\"></xpath>\r\n");
		}
		if (mode.indexOf("1") > -1) {
			sb.append("		<css is_use=\"true\"></css>\r\n");
		}
		if (mode.indexOf("2") > -1) {
			sb.append("		<id is_use=\"true\"></id>\r\n");
		}
		if (mode.indexOf("3") > -1) {
			sb.append("		<linktext is_use=\"true\"></linktext>\r\n");
		}
		if (mode.indexOf("4") > -1) {
			sb.append("		<name is_use=\"true\"></name>\r\n");
		}
		if (mode.indexOf("5") > -1) {
			sb.append("		<tagname is_use=\"true\"></tagname>\r\n");
		}
		if (mode.indexOf("6") > -1) {
			sb.append("		<classname is_use=\"true\"></classname>\r\n");
		}
		sb.append("	</element>\r\n");

		// 判断modeIsEmpty开关是否开启，若开启，则清空mode中的内容
		if (modeIsEmpty) {
			mode.delete(0, mode.length());
		}

		return sb;
	}
	*/

	/**
	 * 该方法用于配套xml生成的DTD文件，用于限制输入
	 */
	/*
	private static void createDTD() {
		// 在xml文件存放的目录下创建一个dtd文件
		File f = new File(xmlPath.toString() + "\\ElementDTD.dtd");
		// 判断ElementDTD.dtd文件是否存在，存在则不创建文件，返回
		if (f.exists()) {
			return;
		}

		// 若不存在，则存储dtd代码，并生成文件
		StringBuilder sb = new StringBuilder();
		// 限制project标签下可以有多个element元素
		sb.append("<!ELEMENT project (element+)>\r\n");
		// 限制element标签下的定位方式标签可出现一次或零次
		sb.append("<!ELEMENT element (xpath?,css?,id?,linktext?,name?,tagname?,classname?)>\r\n");
		// 对于定位方式标签限制没有子标签，数据为字符型数据
		sb.append("<!ELEMENT xpath (#PCDATA)>\r\n");
		sb.append("<!ELEMENT css (#PCDATA)>\r\n");
		sb.append("<!ELEMENT id (#PCDATA)>\r\n");
		sb.append("<!ELEMENT linktext (#PCDATA)>\r\n");
		sb.append("<!ELEMENT name (#PCDATA)>\r\n");
		sb.append("<!ELEMENT tagname (#PCDATA)>\r\n");
		sb.append("<!ELEMENT classname (#PCDATA)>\r\n");
		// 限制project中的name属性的值为非必要
		sb.append("<!ATTLIST project name CDATA #IMPLIED>\r\n");
		// 限制element中的name属性的值为唯一且必要
		sb.append("<!ATTLIST element name ID #REQUIRED>\r\n");

		try {
			FileWriter fw = new FileWriter(f);
			fw.write(sb.toString());
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	*/
}