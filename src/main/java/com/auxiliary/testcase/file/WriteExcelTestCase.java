package com.auxiliary.testcase.file;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

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
	
	@Override
	public T addStepAndExcept(String step, String except) {
		addStep(step);
		return addExcept(except);
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

	@SuppressWarnings("unchecked")
	@Override
	public T addCase(Case testCase) {
		// 获取用例内容
		HashMap<String, ArrayList<String>> labelMap = testCase.getFieldTextMap();
		
		// 遍历当前测试用例模板字段中的内容，将内容写入到相应的文件模板中
		labelMap.forEach((field, content) -> {
			addContent(caseFieldMap.get(field), labelMap.get(field).toArray(new String[] {}));
		});
		 
		return (T) this;
	}
}
