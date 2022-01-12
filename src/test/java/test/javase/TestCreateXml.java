package test.javase;

import java.io.File;

import pres.auxiliary.work.selenium.xml.XmlFileNameIsNullException;


public class TestCreateXml {
	public static void main(String[] args) throws XmlFileNameIsNullException {
		CreateXml.setMode(CreateXml.XPATH, CreateXml.CSS);
		CreateXml.create(new File("ConfigurationFiles/ReportConfigurationFile/txt").listFiles());
		System.out.println("The End");
	}
}