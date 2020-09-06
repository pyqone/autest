package pres.auxiliary.work.testcase.file;

import java.io.File;

import org.dom4j.DocumentException;

/**
 * <p><b>文件名：</b>BasicTestCaseWrite.java</p>
 * <p><b>用途：</b>在无相应的测试用例文件类时，可使用本类，对自定义的一个测试用例模板进行编辑</p>
 * <p><b>编码时间：</b>2020年4月5日 下午6:51:54</p>
 * <p><b>修改时间：</b>2020年4月5日 下午6:51:54</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 */
public class BasicTestCaseWrite extends AbstractTestCaseWrite<BasicTestCaseWrite> {
	/**
	 * 通过测试文件模板xml配置文件和测试用例文件来构造AbstractTestCaseWrite类。当配置文件中
	 * 只存在一个sheet标签时，则直接获取其对应sheet下所有column标签的id属性；若存在
	 * 多个sheet标签时，则读取第一个sheet标签，如需切换sheet标签，则可调用{@link #switchSheet(String)} 方法。
	 * 
	 * @param configFile 测试文件模板xml配置文件类对象
	 * @param caseFile   测试用例文件类对象
	 * @throws DocumentException 
	 * @throws IncorrectFileException 文件格式或路径不正确时抛出的异常
	 */
	public BasicTestCaseWrite(File configFile, File caseFile) throws DocumentException {
		super(configFile, caseFile);
	}
}
