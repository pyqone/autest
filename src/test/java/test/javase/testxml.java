package test.javase;

import org.dom4j.DocumentException;
import pres.auxiliary.work.selenium.xml.XmlFileNameIsNullException;

public class testxml {

	public static void main(String[] args) throws XmlFileNameIsNullException, DocumentException {
		/*
		List<String> l = new ArrayList<>();
		l.add("帐号文本框");
		l.add("密码文本框");
		l.add("登录按钮");
		CreateXML.setXmlPath("xml");
		CreateXML.setXmlName("登录界面");
		CreateXML.create("帐号文本框", "密码文本框", "登录按钮");
		CreateXML.create("登录界面2", l);
		*/
		/*
		File f = new File("C:\\AutoTestting\\Xml\\ElementName");
		CreateXML.setMode(CreateXML.XPATH, CreateXML.CSS, CreateXML.NAME);
		CreateXML.create(f.listFiles());
		*/
		
		/*
		String s = "测试模块1(#949)";
		System.out.println(s.substring(0, s.indexOf("(")));
		System.out.println(s.substring(s.indexOf("(") + 2, s.indexOf(")")));
		*/
		
		/*
		Document dom = new SAXReader().read(new File("C:\\AutoTest\\Case\\ZentaoData.xml"));
		Element e = (Element)dom.selectSingleNode("/data/module[@name='新能源共享汽车后台管理系统']");
		System.out.println(e.attribute("id").getValue());
		*/
		
		System.out.println(Boolean.valueOf(""));
	}

}
