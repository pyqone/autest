package pres.auxiliary.work.selenium.xml;

import java.io.File;
import java.io.IOException;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import pres.auxiliary.work.old.testcase.templet.ZentaoTemplet;

/**
 * 该类用于通过读取生成的case文件来自动编写测试用例及selenium脚本（未做）
 * 
 * @author 彭宇琦
 */
public class ReadCaseXml {
	// 用于读取XML文件
	private Document dom;

	/**
	 * 用于构造对象，并设置xml文件
	 * 
	 * @param caseFile
	 *            指向xml文件的文件对象
	 */
	public ReadCaseXml(File caseFile) {
		setCaseFilePath(caseFile);
	}

	/**
	 * 该方法用于重新设置xml文件存放的路径，并将Document对象指向新的XML文件
	 * 
	 * @param caseFile
	 *            xml的存放路径
	 */
	public void setCaseFilePath(File caseFile) {
		// 重新构造Document对象，使其指向新的XML文件
		try {
			dom = new SAXReader().read(caseFile);
		} catch (DocumentException e) {
			// 若抛出异常，则将异常转换为自定义的IncorrectXMLFileException异常，使其不需要在编译时处理
			throw new IncorrectXmlPathException("case文件不正确或case文件路径不正确");
		}
	}

	/**
	 * 该方法用于读取生成的case文件，并自动编写测试用例
	 * @throws IOException 
	 */
	public void writeCase() throws IOException {
		// 获取到根元素
		Element root = dom.getRootElement();
		// 读取根元素的属性，并通过该属性创建测试用例模版
		String caseTempletFileName = root.attribute("name").getText();
		String caseTempletFilePath = root.attribute("case_file_path").getText();
		createCaseTemplet(caseTempletFileName, caseTempletFilePath,
				root.attribute("module_file_path"),
				root.attribute("story_file_path"));
	}

	/**
	 * 该方法用于创建禅道模版，注意，后两个参数为Attribute类型
	 * @throws IOException 
	 */
	private void createCaseTemplet(String name, String path, Attribute module,
			Attribute story) throws IOException {
		//读取module和story属性中的内容，判断xml中是否存在该属性，若不存在则赋为空值
		String moduleStr = "";
		String storyStr = "";
		
		if ( module != null ) {
			moduleStr = module.getText();
		}
		
		if ( story != null ) {
			storyStr = story.getText();
		}
		
		//创建禅道模版
		ZentaoTemplet.setSavePath(path);
		ZentaoTemplet.setFileName(name);
		
		ZentaoTemplet.create();
		
		ZentaoTemplet.readModlueData(new File(moduleStr));
		ZentaoTemplet.readStoryData(new File(storyStr));
	}
}
