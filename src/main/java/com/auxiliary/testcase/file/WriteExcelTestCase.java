package com.auxiliary.testcase.file;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.dom4j.Document;

import com.auxiliary.testcase.templet.Case;
import com.auxiliary.tool.file.FileTemplet;
import com.auxiliary.tool.file.excel.WriteExcelTempletFile;

/**
 * <p>
 * <b>文件名：</b>WriteExcelTestCase.java
 * </p>
 * <p>
 * <b>用途：</b> 定义写入excel类型的测试用例基本方法
 * </p>
 * <p>
 * <b>编码时间：</b>2021年6月17日上午8:15:56
 * </p>
 * <p>
 * <b>修改时间：</b>2021年8月27日下午7:42:16
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @param <T> 子类
 */
public abstract class WriteExcelTestCase<T extends WriteExcelTestCase<T>> extends WriteExcelTempletFile<T>
		implements RelevanceTestCaseTemplet<T>, BasicTsetCase<T> {
	/**
	 * 用于存储测试用例与测试用例模板字段之间的关联
	 */
	protected HashMap<String, String> caseFieldMap = new HashMap<>();

	/**
	 * 通过模板配置xml文件对文件写入类进行构造
	 * <p>
	 * 通过该方法构造的写入类为包含模板的写入类，可直接按照字段编写文件内容
	 * </p>
	 *
	 * @param templetXml 模板配置文件
	 * @param saveFile   文件保存路径
	 */
	public WriteExcelTestCase(Document templetXml, File saveFile) {
		super(templetXml, saveFile);
		initField();
	}

	/**
	 * 构造用例写入类，并设置一个Sheet页的模板及相应的名称
	 *
	 * @param templetName 模板名称
	 * @param templet     模板类
	 */
	public WriteExcelTestCase(String templetName, FileTemplet templet) {
		super(templetName, templet);
		initField();
	}

	/**
	 * 用于初始化字段的链接
	 */
	protected void initField() {
		getField().forEach(field -> caseFieldMap.put(field, field));
	}

	@SuppressWarnings("unchecked")
	@Override
	public T addStep(String... stepTexts) {
		addContent(caseFieldMap.get(CASE_STEP), stepTexts);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T addTitle(String titleText) {
		addContent(caseFieldMap.get(CASE_TITLE), titleText);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T addExcept(String... exceptTexts) {
		addContent(caseFieldMap.get(CASE_EXCEPT), exceptTexts);
		return (T) this;
	}

	/**
     * 用于添加测试用例的一条步骤及预期
     * <p>
     * <b>注意：</b>多次调用该方法时，会在内容下方继续添加写入的内容。当步骤或预期字段为自动换行字段时，则会在插入内容前，进行自动换行，之后再插入相应内容
     * </p>
     *
     * @param step   步骤
     * @param except 预期
     * @return 类本身
     */
	@Override
	public T addStepAndExcept(String step, String except) {
		return disposeWriteFieldsContent(Arrays.asList(caseFieldMap.get(CASE_STEP), caseFieldMap.get(CASE_EXCEPT)), () -> {
		    addStep(step);
	        addExcept(except);
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public T addModule(String module) {
		addContent(caseFieldMap.get(CASE_MODULE), module);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T addPrecondition(String... preconditions) {
		addContent(caseFieldMap.get(CASE_PRECONDITION), preconditions);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T addPriority(String priority) {
		addContent(caseFieldMap.get(CASE_RANK), priority);
		return (T) this;
	}

	@Override
	public void relevanceCase(String templetField, String caseField) {
		caseFieldMap.put(caseField, templetField);
	}

	/**
     * 用于将测试用例模板（继承自{@link Case}类的方法）所成的测试用例添加到测试用例文件中
     * <p>
     * <b>注意：</b>若写入的用例中包含有自动换行字段时，则会在插入内容前，进行自动换行，之后再插入相应内容
     * </p>
     *
     * @param testCase 测试用例生成方法
     * @return 类本身
     */
	@SuppressWarnings("unchecked")
	@Override
	public T addCase(Case testCase) {
		// 获取用例内容
		HashMap<String, ArrayList<String>> labelMap = testCase.getFieldTextMap();
		// 获取需要写入的字段集合
		List<String> fieldList = labelMap.keySet().stream().map(caseFieldMap::get).filter(f -> f != null).collect(Collectors.toList());

		// 遍历当前测试用例模板字段中的内容，将内容写入到相应的文件模板中
		disposeWriteFieldsContent(fieldList, () -> {
		    labelMap.forEach((field, content) -> {
	            addContent(caseFieldMap.get(field), labelMap.get(field).toArray(new String[] {}));
	        });
		});

		return (T) this;
	}
}