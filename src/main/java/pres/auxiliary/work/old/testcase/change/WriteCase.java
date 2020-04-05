package pres.auxiliary.work.old.testcase.change;

import java.io.File;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 该类定义了通过读取XML方式来生成用例的基本方法，通过该方法生成的用例有测试用例及自动化测试脚本用例
 * @author 彭宇琦
 * @version V1.0
 * @since JDK 1.7
 * @since DOM4J 1.6.1
 * @since POI 13.0
 */
public abstract class WriteCase implements Runnable {
	//用于读取XML文件
	protected Document dom;
	
	/**
	 * 该方法用于设置xml文件
	 * @param xmlFile xml文件
	 * @throws DocumentException
	 */
	public void setDocument(File xmlFile) throws DocumentException {
		dom = new SAXReader().read(xmlFile);
	}
	
	/**
	 * 该方法用于设置xml文件
	 * @param dom 指向xml文件的Document对象
	 * @throws DocumentException
	 */
	public void setDocument(Document dom) throws DocumentException {
		this.dom = dom;
	}
	
	/**
	 * 该方法用于通过XML来创建用例
	 */
	public abstract void write() throws IOException ;
	
	/**
	 * 该方法用于启动多线程来创建测试用例
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public abstract void run();
	
	/**
	 * 该方法用于对模块标签进行操作
	 * @param mocule 模块标签的Element对象
	 * @throws IOException 
	 */
	protected abstract void disposeModule(Element mocule) throws IOException;
	
	/**
	 * 该方法用于对用例类型进行操作
	 * @param type 类型标签的Element对象
	 * @throws IOException 
	 */
	protected abstract void disposeType(Element type) throws IOException;
	
	/**
	 * 该方法用于对需要编写用例的元素进行操作
	 * @param element 待编写用例的元素标签的Element对象
	 * @throws IOException 
	 */
	protected abstract void disposeElement(Element element) throws IOException;
}
