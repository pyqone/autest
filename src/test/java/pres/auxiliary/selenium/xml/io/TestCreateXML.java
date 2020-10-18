package pres.auxiliary.selenium.xml.io;

import java.io.File;

import org.junit.Test;

/**
 * FileName: TestCreateXML.java
 * 
 * 用于测试创建xml文件的程序
 * 
 * @author 彭宇琦
 * @Deta 2019年1月10日 上午11:35:25
 * @version ver1.0
 */
public class TestCreateXML {
	@Test
	public void test_01() throws XmlFileNameIsNullException {
		CreateXml.setMode(CreateXml.XPATH, CreateXml.ID, CreateXml.NAME);
		CreateXml.setXmlPath("src/test/java/pres/auxiliary/selenium/xml/io/resource/");
		CreateXml.create(new File("src/test/java/pres/auxiliary/selenium/xml/io/resource").listFiles());
	}
}
