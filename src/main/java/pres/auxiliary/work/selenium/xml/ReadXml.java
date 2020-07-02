package pres.auxiliary.work.selenium.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.InvalidXPathException;
import org.dom4j.io.SAXReader;
import org.openqa.selenium.By;

/**
 * <p>
 * <b>文件名：</b>ReadXml.java
 * </p>
 * <p>
 * <b>用途：</b>该类用于从指定格式的xml中读取配制信息<br/>
 * 
 * <b>注意：</b>
 * <ol>
 * <li>标签的name必须唯一，否则读取会出现错误
 * <li>不同类型的定位模板可以使用一个id属性，但同一种类型的定位模板id属性是唯一的，如xpath模板可以使用id='1'，css模板可以使用id='1'，但另一xpath模板的id属性就不能再定为1，但建议模板的id也唯一存在
 * <li>元素定位标签只能写xpath、css、classname、id、linktext、name、tagname
 * <li>所有标签均为小写
 * <ol>
 * </p>
 * <p>
 * <b>编码时间：</b>2017年9月25日下午4:23:40
 * </p>
 * <p>
 * <b>修改时间：</b>2019年10月25日下午2:40:40
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public class ReadXml {
	/**
	 * 用于读取XML文件
	 */
	private Document dom;

	/**
	 * 切分公式标记
	 */
	private final String SPLIT_START = "\\$\\{";
	/**
	 * 定义xml文件中，模板使用的替换符开始标志
	 */
	private final String START_SIGN = "${";
	/**
	 * 定义xml文件中，模板使用的替换符结束标志
	 */
	private final String END_SIGN = "}";
	
	/**
	 * 存储xml文件类对象
	 */
	private File xmlFile;

	/**
	 * 用于构造对象，并设置xml文件
	 * 
	 * @param xmlFile 指向xml文件的文件对象
	 */
	public ReadXml(File xmlFile) {
		setXmlPath(xmlFile);
	}
	
	/**
	 * 保留无参构造，不做任何操作
	 */
	public ReadXml() {
	}

	/**
	 * 该方法用于重新设置xml文件存放的路径，并将Document对象指向新的XML文件
	 * 
	 * @param xmlPath xml的存放路径
	 */
	public void setXmlPath(File xmlFile) {
		// 重新构造Document对象，使其指向新的XML文件
		try {
			dom = new SAXReader().read(xmlFile);
			this.xmlFile = xmlFile;
		} catch (DocumentException e) {
			// 若抛出异常，则将异常转换为自定义的IncorrectXMLFileException异常，使其不需要在编译时处理
			throw new IncorrectXmlPathException("XML文件不正确或XML路径不正确");
		}
	}
	
	/**
	 * 用于返回相应的xml文件类对象
	 * @return xml文件类对象
	 */
	public File getXmlFile() {
		return xmlFile;
	}

	/**
	 * 返回当前有效的元素定位标签
	 * 
	 * @param name 元素名称
	 * @return 当前元素有效的定位名称，多个标签名称以空格隔开
	 */
	public List<ByType> getElementMode(String name) {
		// 用于存储元素定位方式的标签名称
		List<ByType> modes = new ArrayList<>();

		try {
			@SuppressWarnings("unchecked")
			// 由于窗体信息是以iframe标签进行存储，故此处需要用“*”符号表示element和iframe标签
			List<Element> elements = ((Element) dom.selectSingleNode("//*[@name='" + name + "']")).elements();
			//移除iframe与element标签
			elements = elements.stream().filter(element -> (!"iframe".equals(element.getName()) && !"element".equals(element.getName()))).collect(Collectors.toList());
			// 循环，查找控件名对应的标签
			for (Element element : elements) {
				// 若标签可用且标签内属性不为空，则将该标签的名称存储在s中
				if ((element.attribute("is_use") == null || element.attributeValue("is_use").equalsIgnoreCase("true"))) {
					modes.add(getMode(element.getName()));
				}
			}
		} catch (NullPointerException e) {
			throw new UndefinedElementException();
		}

		// 返回元素存在的定位方式
		return modes;
	}

	/**
	 * 根据元素名称，在指定的xml文件中查找到相应的元素，返回其元素的信息，以{@link By}类返回
	 * 
	 * @param name 元素名称
	 * @param mode 定位方式枚举类对象，参见{@link ByType}
	 * @return 元素对应的{@link By}类对象
	 * 
	 * @throws UndefinedElementException 未找到相应的模板元素时抛出的异常
	 * @throws NoSuchSignValueException  模板中存在为定义值的标志时抛出的异常
	 */
	public By getBy(String name, ByType mode) {
		return getBy(name, mode, null);
	}
	
	/**
	 * 根据元素名称，在指定的xml文件中查找到相应的元素，返回其元素的信息，以{@link By}类返回
	 * 
	 * @param name 元素名称
	 * @param mode 定位方式枚举类对象，参见{@link ByType}
	 * @param linkKeyList 外链关键词集合
	 * @return 元素对应的{@link By}类对象
	 * 
	 * @throws UndefinedElementException 未找到相应的模板元素时抛出的异常
	 * @throws NoSuchSignValueException  模板中存在为定义值的标志时抛出的异常
	 */
	public By getBy(String name, ByType mode, List<String> linkKeyList) {
		// 存储从xml文件中读取到的元素定位
		String elementPos = getValue(name, mode, linkKeyList); 

		// 判断传入的参数是否符合使用By类的定位方式，如果符合，则拼接读取XML文件的xpath，若不符，则抛出异常
		try {
			switch (mode) {
			case XPATH:
				return By.xpath(elementPos);
			case CSS:
				return By.cssSelector(elementPos);
			case ID:
				return By.id(elementPos);
			case LINKTEXT:
				return By.linkText(elementPos);
			case NAME:
				return By.name(elementPos);
			case TAGNAME:
				return By.tagName(elementPos);
			case CLASSNAME:
				return By.className(elementPos);
			default:
				throw new IllegalArgumentException("未定义的元素定位方式：" + mode);
			}
		} catch (NullPointerException e) {
			throw new UndefinedElementException("未找到元素定位方式");
		}
	}

	/**
	 * 用于根据元素名称及定位方式来返回其定位方式的值
	 * 
	 * @param name 元素名称
	 * @param mode 定位方式枚举类对象，参见{@link ByType}
	 * @return xml文件中的相应元素的定位值
	 * 
	 * @throws UndefinedElementException 未找到相应的模板元素时抛出的异常
	 * @throws NoSuchSignValueException  模板中存在为定义值的标志时抛出的异常
	 */
	public String getValue(String name, ByType mode) {
		// 用于拼接读取XML中元素的xpath，为了兼容iframe标签，故此处使用“*”符号来查找元素
		String xmlXpath = "//*[@name='" + name + "']/" + mode.getValue();
		// 获取元素节点，并将其转为Element对象，以便于获取该元素的属性值
		Element element = (Element) dom.selectSingleNode(xmlXpath);

		// 判断元素是否使用模板（是否存在模板ID），若使用模板，则根据传入的mode，获取模板的定位方式
		if (element.attribute("temp_id") != null) {
			return getTempletPath(element, name, mode);
		} else {
			return element.getText();
		}
	}
	
	/**
	 * 用于根据元素名称及定位方式查找其定位方式标签中的内容，并根据关键词组，对需要修改的关键词
	 * 逐个进行修改。注意，该方法将根据需要修改的关键词顺序进行修改，与关键词名称无关
	 * 
	 * @param name 元素名称
	 * @param mode 定位方式枚举类对象，参见{@link ByType}
	 * @return xml文件中的相应元素的定位值
	 * 
	 * @throws UndefinedElementException 未找到相应的模板元素时抛出的异常
	 * @throws NoSuchSignValueException  模板中存在为定义值的标志时抛出的异常
	 */
	public String getValue(String name, ByType mode, List<String> linkKeyList) {
		//获取到元素定位方式内容
		StringBuilder value = new StringBuilder(getValue(name, mode));
		
		//判断linkKeyList是否为空或null，若为空或null，则直接返回
		if (linkKeyList == null || linkKeyList.isEmpty()) {
			return value.toString();
		}
		
		//按照公式起始符号对内容进行切分，遍历切分后的每一个元素
		String[] texts = value.toString().split(SPLIT_START);
		
		//用于lambda中计算替换的词语位置
		AtomicInteger index = new AtomicInteger(0);
		//遍历texts
		Arrays.stream(texts)
			//过滤不包含END_SIGN的元素
			.filter(text -> text.indexOf(END_SIGN) > -1)
			//提取元素的有效替换词语（${XXX}）
			.map(text -> START_SIGN + text.substring(0, text.indexOf(END_SIGN) + 1))
			//对词语进行替换
			.forEach(text -> {
				//判断当前下标是否超出linkKeyList最大元素个数，若超出，则结束当前执行
				if (index.get() >= linkKeyList.size()) {
					return;
				}
				
				//获取该字段在value中第一次出现的位置
 				int signIndex = value.indexOf(text);
 				// 将所有与该标志替换为元素属性中存储的内容（使用拼接的方式进行替换）
				value.replace(signIndex, signIndex + text.length(), linkKeyList.get(index.getAndAdd(1)));
 				
			});
		return value.toString();
	}
	
	/**
	 * 返回元素的所在的窗体
	 * @param name 元素在xml文件中的名称
	 * @return 元素所在的窗体
	 */
	public String getIframeName(String name) {
		// 获取元素节点，并将其转为Element对象，以便于获取该元素的属性值
		Element element = (Element) dom.selectSingleNode("//*[@name='" + name + "']");
				
		// 存储元素所在的窗体的名称，若元素未在任何窗体下，则其为空
		String iframeName = "";
		// 获取元素所在的窗体层级，由于当前元素定位到元素定位标签上，父节点元素应是element或iframe，需要再获取一次父层，其结构才正确
		Element iframeElement = element.getParent();
		// 判断iframeElement是否为根元素，若不为根元素，则存储其元素的name属性
		if (!iframeElement.isRootElement()) {
			iframeName = iframeElement.attributeValue("name");
		}
		
		return iframeName;
	}
	
	/**
	 * 该方法用于判断当前查找的元素是否存在于xml文件中
	 * @return 元素是否存在于xml
	 */
	public boolean isElement(String name) {
		//获取元素的xpath，查找所有带name属性的与传入的name相同元素
		String xpath = "//*[@name='" + name + "']";
		//若不存在该节点，则返回false，反之，则返回true
		try {
			if (dom.getRootElement().selectSingleNode(xpath) == null) {
				return false;
			} else {
				return true;
			}
		} catch (InvalidXPathException e) {
			//当写入name为一个xpath时会抛出该异常，若存在该异常，则直接返回false
			return false;
		}
	}

	/**
	 * 用于模板的元素的定位查找，返回其相应的定位
	 * 
	 * @param element 通过name定位得到的元素对象
	 * @param name    元素的名称
	 * @param mode    元素的定位方式名称
	 * @return 定位方式对应的元素定位
	 * 
	 * @throws UndefinedElementException 未找到相应的模板元素时抛出的异常
	 * @throws NoSuchSignValueException  模板中存在为定义值的标志时抛出的异常
	 */
	@SuppressWarnings("unchecked")
	private String getTempletPath(Element element, String name, ByType mode) {
		// 通过元素的temp_id定位到模板的id上
		Element templetElement = (Element) dom.selectSingleNode(
				"//templet/" + mode.getValue() + "[@id='" + element.attribute("temp_id").getValue() + "']");
		// 如果templetElement元素为null（无法查找到），则抛出UndefinedElementException
		if (templetElement == null) {
			throw new UndefinedElementException("未定义的模板，元素定位方式为：" + mode.getValue() + "，模板id为："
					+ element.attribute("temp_id").getValue() + "；该模板未定义");
		}

		// 添加元素相应的属性
		HashMap<String, String> attElements = new HashMap<>(16);
		attElements.put("name", name);
		// 循环，遍历元素的所有的属性，并将属性一一存入attElements中
		for (Attribute att : (List<Attribute>) element.attributes()) {
			attElements.put(att.getName(), att.getValue());
		}

		// 获取模板的内容
		String path = templetElement.getText();
		//按照公式起始符号对内容进行切分，遍历切分后的每一个元素
		String[] texts = path.split(SPLIT_START);
		for (int i = 0; i < texts.length; i++) {
			//判断切分的元素是否包含公式结束符号
			int index;
			if ((index = texts[i].indexOf(END_SIGN)) > -1) {
				//截取有效字符串，对关键词进行替换
				String sign = texts[i].substring(0, index);
				// 判断该标志名称是否存储在attElements中，若不存在，则抛出异常
				if (attElements.containsKey(sign)) {
					// 将所有与该标志相关的全部替换为元素属性中存储的内容
					path = path.replaceAll("\\$\\{" + sign + "\\}", attElements.get(sign));
				}
			}
		}

		return path;
	}
	
	/**
	 * 根据xml内容标签名称获取相应的ElementLocationType枚举
	 * @param modeName 定位标签名称
	 * @return ElementLocationType枚举
	 */
	private static ByType getMode(String modeName) {
		switch (modeName) {
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
			throw new IllegalArgumentException("Unexpected value: " + modeName);
		}
	}
}
