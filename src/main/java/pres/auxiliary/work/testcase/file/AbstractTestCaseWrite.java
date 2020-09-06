package pres.auxiliary.work.testcase.file;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.dom4j.DocumentException;

import pres.auxiliary.tool.file.excel.AbstractWriteExcel;
import pres.auxiliary.work.testcase.templet.Case;
import pres.auxiliary.work.testcase.templet.LabelNotFoundException;

/**
 * <p><b>文件名：</b>AbstractTestCaseWrite.java</p>
 * <p><b>用途：</b>
 * 根据配置文件中的字段，向生成的Excel文件中添加内容
 * </p>
 * <p><b>编码时间：</b>2020年8月12日上午8:54:10</p>
 * <p><b>修改时间：</b>2020年8月12日上午8:54:10</p>
 * @author 彭宇琦
 * @version Ver1.0
 *
 * @param <T> 继承自AbstractTestCaseWrite的类
 */
public abstract class AbstractTestCaseWrite<T extends AbstractTestCaseWrite<T>> extends AbstractWriteExcel<AbstractTestCaseWrite<T>> {
	/**
	 * 用于存储与测试用例生成类关联的字段，参数1为用例文件中的字段，参数2为测试用例生成方法中的字段
	 */
	protected HashMap<String, String> relevanceMap = new HashMap<>(16);
	
	/**
	 * 通过测试文件模板xml配置文件和测试用例文件来构造JiraTestCaseWrite类。当配置文件中
	 * 只存在一个sheet标签时，则直接获取其对应sheet下所有column标签的id属性；若存在
	 * 多个sheet标签时，则读取第一个sheet标签，如需切换sheet标签，则可调用{@link #switchSheet(String)} 方法。
	 * 
	 * @param configFile 测试文件模板xml配置文件类对象
	 * @param caseFile   测试用例文件类对象
	 * @throws DocumentException 当xml配置文件错误时抛出
	 * @throws IncorrectFileException 文件格式或路径不正确时抛出的异常
	 */
	public AbstractTestCaseWrite(File configFile, File caseFile) throws DocumentException {
		super(configFile, caseFile);
	}

	/**
	 * 用于将测试用例模板（继承自{@link Case}类的方法）所成的测试用例添加到测试用例文件中
	 * 
	 * @param testCase 测试用例生成方法
	 * @return 类本身
	 */
	@SuppressWarnings("unchecked")
	public T addCase(Case testCase) {
		// 获取用例内容
		HashMap<String, ArrayList<String>> labelMap = testCase.getFieldTextMap();

		// 遍历relevanceMap，将用例字段内容写入到xml中
		relevanceMap.forEach((field, label) -> {
			addContent(field, labelMap.get(label).toArray(new String[] {}));
		});

		return (T) this;
	}
	
	/**
	 * 用于将测试用例文件模板中的字段名与测试用例生成类（继承自{@link Case}的测试用例生成类）中
	 * 的字段进行关联，通过该方法设置关联字段后，可将生成的测试用例写入到测试用例文件中
	 * 
	 * @param field     测试用例文件字段
	 * @param caseLabel 测试用例生成方法的字段
	 * @throws LabelNotFoundException 当在sheet标签中查不到相应的单元格id不存在时抛出的异常
	 */
	public void relevanceCase(String field, String labelType) {
		// 判断字段是否存在，若不存在，则抛出异常
		if (!fieldMap.get(nowSheetName).containsKey(field)) {
			throw new LabelNotFoundException("当前sheet不存在的标签id：" + field);
		}

		// 添加字段
		relevanceMap.put(field, labelType);
	}
}
