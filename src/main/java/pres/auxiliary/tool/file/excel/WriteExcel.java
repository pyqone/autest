package pres.auxiliary.tool.file.excel;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.DocumentException;

import pres.auxiliary.testcase.file.IncorrectFileException;

/**
 * <p><b>文件名：</b>WriteExcel.java</p>
 * <p><b>用途：</b>
 * 用于根据xml配置文件，将所需内容写入到excel文件中。在写入内容时，
 * 可通过调用<br>
 * {@code setReplactWord(String word, String replactWord) }<br>
 * 方法，设置需要替换词语，在文本中使用“#替换词语#”进行标记。需要注意的是，若被标记的词语
 * 未进行设置，则会保留原始内容（包含标记符号）。
 * <p>
 * 在添加内容时，亦可通过调用<br>
 * {@code setFieldValue(String field, String content) }<br>
 * 方法来设置常值，即被标记的字段每行文本都使用相同的内容。
 * </p>
 * </p>
 * <p><b>编码时间：</b>2020年8月12日上午8:53:55</p>
 * <p><b>修改时间：</b>2020年8月12日上午8:53:55</p>
 * @author 彭宇琦
 * @version Ver1.0
 *
 */
public class WriteExcel extends AbstractWriteExcel<WriteExcel> {
	/**
	 * 通过测试文件模板xml配置文件和测试用例文件进行构造。当配置文件中
	 * 只存在一个sheet标签时，则直接获取其对应sheet下所有column标签的id属性；若存在
	 * 多个sheet标签时，则读取第一个sheet标签，如需切换sheet标签，则可调用{@link #switchSheet(String)} 方法。
	 * 
	 * @param configFile 测试文件模板xml配置文件类对象
	 * @param caseFile   测试用例文件类对象
	 * @throws DocumentException 当xml配置文件错误时抛出
	 * @throws IncorrectFileException 文件格式或路径不正确时抛出的异常
	 */
	public WriteExcel(File configFile, File caseFile) throws DocumentException {
		super(configFile, caseFile);
	}

	/**
	 * 通过测试文件模板xml配置文件的{@link Document}类对象和测试用例文件进行构造。当配置文件中
	 * 只存在一个sheet标签时，则直接获取其对应sheet下所有column标签的id属性；若存在
	 * 多个sheet标签时，则读取第一个sheet标签，如需切换sheet标签，则可调用{@link #switchSheet(String)} 方法。
	 * 
	 * @param configDocument 测试文件模板xml配置文件{@link Document}类对象
	 * @param caseFile   测试用例文件类对象
	 * @throws IncorrectFileException 文件格式或路径不正确时抛出的异常
	 */
	public WriteExcel(Document configDocument, File caseFile) {
		super(configDocument, caseFile);
	}

}
