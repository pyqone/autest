package pres.auxiliary.tool.file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import pres.auxiliary.selenium.tool.ExcelRecord;

/**
 * <p><b>文件名：</b>ConfigXmlTool.java</p>
 * <p><b>用途：</b>
 * 用于创建特定类中使用到的xml文件，方便在未保存xml配置文件时，对配置文件进行创建。
 * </p>
 * <p><b>编码时间：</b>2020年8月12日下午4:14:32</p>
 * <p><b>修改时间：</b>2020年8月12日下午4:14:32</p>
 * @author 彭宇琦
 * @version Ver1.0
 *
 */
public class ExcelConfigXmlTool {
	/**
	 * 存储生成的Document对象
	 */
	private static Document document;
	
	/**
	 * 用于生成{@link ExcelRecord}类所需的xml结构
	 * @return {@link ExcelRecord}类所需的xml结构
	 */
	public static Document createExcelRecordXml() {
		
		
		return document;
	}
	
	/**
	 * 用于将生成的{@link Document}对象写入到指定的文件中
	 * @param saveFile 生成的xml文件对象
	 * @throws IOException xml文件生成有误时抛出的异常
	 * @throws NullPointerException 未生成{@link Document}对象时抛出的异常
	 */
	public static void createFile(File saveFile) throws IOException {
		if (document == null) {
			throw new NullPointerException("未生成xml配置文件Document对象");
		}
		
		//创建文件存储路径
		saveFile.getParentFile().mkdirs();
		
		//格式化document
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding(document.getXMLEncoding());
		
		//将document中的内容写入到相应的文件中
		XMLWriter writer = new XMLWriter(new FileWriter(saveFile), format);
		writer.write(document);
		writer.close();
	}
}
