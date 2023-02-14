package com.auxiliary.testcase.file.extend;

import java.io.File;
import java.util.Arrays;

import org.dom4j.Document;

import com.auxiliary.testcase.file.WriteExcelTestCase;
import com.auxiliary.tool.file.FileTemplet;
import com.auxiliary.tool.file.excel.ExcelFileTemplet;

/**
 * <p>
 * <b>文件名：</b>WriteJiraExcelTestCase.java
 * </p>
 * <p>
 * <b>用途：</b> 用于对上传jira测试用例模板，通过该类构造的用例文件，在使用测试用例
 * 模板类写入用例时可以不用指定相应的字段关系。该类中包含部分个性的方法，以方便编写 测试用例
 * </p>
 * <p>
 * <b>编码时间：</b>2021年6月19日下午3:30:43
 * </p>
 * <p>
 * <b>修改时间：</b>2021年6月19日下午3:30:43
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public class WriteJiraExcelTestCase extends WriteExcelTestCase<WriteJiraExcelTestCase> {
	public static final String DEFAULT_CASE_NAME = "测试用例";

	/**
	 * 用例标题目的
	 */
	public static final String CASE_OBJECTIVE = "objective";
	/**
	 * 用例标题状态
	 */
	public static final String CASE_STATUS = "status";
	/**
	 * 用例标题项目
	 */
	public static final String CASE_COMPONENT = "component";
	/**
	 * 用例标题设计者
	 */
	public static final String CASE_OWNER = "owner";
	/**
	 * 用例标题关联需求
	 */
	public static final String CASE_ISSUES = "issues";

	/**
	 * 通过模板配置xml文件对文件写入类进行构造
	 * <p>
	 * 通过该方法构造的写入类为包含模板的写入类，可直接按照字段编写文件内容
	 * </p>
	 * 
	 * @param templetXml 模板配置文件
	 * @param saveFile   文件保存路径
	 */
	public WriteJiraExcelTestCase(Document templetXml, File saveFile) {
		super(templetXml, saveFile);
	}

	/**
	 * 构造用例写入类，使用默认模板
	 * <p>
	 * 默认的模板可通过{@link WriteJiraExcelTestCase#getJiraCaseExcelTemplet()}方法进行获取
	 * </p>
	 */
	public WriteJiraExcelTestCase() {
		super(DEFAULT_CASE_NAME, WriteJiraExcelTestCase.getJiraCaseExcelTemplet());
	}

	/**
	 * 构造用例写入类，使用默认模板并重新设置文件保存路径
	 * <p>
	 * 默认的模板可通过{@link WriteJiraExcelTestCase#getJiraCaseExcelTemplet()}方法进行获取
	 * </p>
	 * 
	 * @param saveFile 保存路径文件类对象
	 */
	public WriteJiraExcelTestCase(File saveFile) {
		this();
		// 重新设置保存路径
		dataMap.get(DEFAULT_CASE_NAME).getTemplet().setSaveFile(saveFile);
	}

	/**
	 * 构造用例写入类，并重新设置模板
	 * 
	 * @param templet 模板类对象
	 */
	public WriteJiraExcelTestCase(FileTemplet templet) {
		this();
		// 重新设置模板
		addTemplet(DEFAULT_CASE_NAME, templet);
	}

	@Override
	protected void initField() {
//		super.initField();
		caseFieldMap.put(CASE_OBJECTIVE, CASE_OBJECTIVE);
		caseFieldMap.put(CASE_STATUS, CASE_STATUS);
		caseFieldMap.put(CASE_COMPONENT, CASE_COMPONENT);
		caseFieldMap.put(CASE_OWNER, CASE_OWNER);
		caseFieldMap.put(CASE_ISSUES, CASE_ISSUES);
	}

	/**
	 * 用于添加测试用例目的
	 * <p>
	 * <b>注意：</b>多次调用该方法时，会覆盖前一次写入的内容
	 * </p>
	 * 
	 * @param objective 用例目的
	 * @return 类本身
	 */
	public WriteJiraExcelTestCase addObjective(String objective) {
		addContent(caseFieldMap.get(CASE_OBJECTIVE), objective);
		return this;
	}

	/**
	 * 用于添加测试用例状态
	 * <p>
	 * <b>注意：</b>多次调用该方法时，会覆盖前一次写入的内容
	 * </p>
	 * 
	 * @param status 用例状态文本
	 * @return 类本身
	 */
	public WriteJiraExcelTestCase addStatus(String status) {
		addContent(caseFieldMap.get(CASE_STATUS), status);
		return this;
	}

	/**
	 * 用于添加测试用例模块
	 * <p>
	 * <b>注意：</b>多次调用该方法时，会覆盖前一次写入的内容
	 * </p>
	 * 
	 * @param component 用例模块文本
	 * @return 类本身
	 */
	public WriteJiraExcelTestCase addComponent(String component) {
		addContent(caseFieldMap.get(CASE_COMPONENT), component);
		return this;
	}

	/**
	 * 用于添加测试用例关联需求
	 * <p>
	 * <b>注意：</b>多次调用该方法时，会覆盖前一次写入的内容
	 * </p>
	 * 
	 * @param issues 用例关联需求文本
	 * @return 类本身
	 */
	public WriteJiraExcelTestCase addIssues(String issues) {
		addContent(caseFieldMap.get(CASE_ISSUES), issues);
		return this;
	}

	/**
	 * 用于添加测试用例设计者
	 * <p>
	 * <b>注意：</b>多次调用该方法时，会覆盖前一次写入的内容
	 * </p>
	 * 
	 * @param owner 用例设计者
	 * @return 类本身
	 */
	public WriteJiraExcelTestCase addOwner(String owner) {
		addContent(caseFieldMap.get(CASE_OWNER), owner);
		return this;
	}

	/**
	 * 用于生成默认的jira测试用例模板类对象
	 * 
	 * @return jira测试用例模板类对象
	 */
	public static ExcelFileTemplet getJiraCaseExcelTemplet() {
		ExcelFileTemplet jiraTemplet = new ExcelFileTemplet(new File("result/测试用例.xlsx"));

		// 设置模板全局内容
		jiraTemplet.setFreeze(1, 3).setFiltrate(true);

		// 设置模板字段内容
        jiraTemplet.addField(CASE_TITLE);
        jiraTemplet.addTitle(CASE_TITLE, "Name").setWide(30.88, CASE_TITLE);

		jiraTemplet.addField(CASE_OBJECTIVE);
        jiraTemplet.addTitle(CASE_OBJECTIVE, "Objective");
        

		jiraTemplet.addField(CASE_PRECONDITION);
        jiraTemplet.addTitle(CASE_PRECONDITION, "Precondition");

		jiraTemplet.addField(CASE_STEP);
        jiraTemplet.addTitle(CASE_STEP, "Test Script (Step-by-Step) - Step");
        
		jiraTemplet.addField(CASE_EXCEPT);
        jiraTemplet.addTitle(CASE_EXCEPT, "Test Script (Step-by-Step) - Expected Result");

		jiraTemplet.addField(CASE_MODULE);
        jiraTemplet.addTitle(CASE_MODULE, "Folder");


		jiraTemplet.addField(CASE_STATUS);
        jiraTemplet.addTitle(CASE_STATUS, "Status");

		jiraTemplet.addField(CASE_RANK);
        jiraTemplet.addTitle(CASE_RANK, "Priority");

		jiraTemplet.addField(CASE_COMPONENT);
        jiraTemplet.addTitle(CASE_COMPONENT, "Component");

		jiraTemplet.addField(CASE_OWNER);
        jiraTemplet.addTitle(CASE_OWNER, "Owner");

		jiraTemplet.addField(CASE_ISSUES);
        jiraTemplet.addTitle(CASE_ISSUES, "Coverage (Issues)");

        // 设置字段垂直居中对齐
        jiraTemplet.setAlignment(AlignmentType.VERTICAL_CENTER);
        // 设置字段左对齐
        jiraTemplet.setAlignment(AlignmentType.HORIZONTAL_LEFT, CASE_OBJECTIVE, CASE_PRECONDITION, CASE_STEP,
                CASE_EXCEPT);
        // 设置字段居中水平对齐
        jiraTemplet.setAlignment(AlignmentType.HORIZONTAL_MIDDLE, CASE_TITLE, CASE_MODULE, CASE_STATUS, CASE_RANK,
                CASE_COMPONENT, CASE_OWNER, CASE_ISSUES);
        
        // 设置字段宽度/
        jiraTemplet.setWide(18.25, CASE_OBJECTIVE, CASE_PRECONDITION);
        jiraTemplet.setWide(45.75, CASE_STEP, CASE_EXCEPT);
        jiraTemplet.setWide(20, CASE_MODULE, CASE_ISSUES);
        jiraTemplet.setWide(10, CASE_STATUS, CASE_RANK, CASE_COMPONENT, CASE_OWNER);

        // 设置需要编号的字段
        jiraTemplet.setAutoSerialNumber(true, CASE_OBJECTIVE, CASE_PRECONDITION, CASE_STEP, CASE_EXCEPT);
        // 设置需要换行的字段
        jiraTemplet.setContentBranch(1, CASE_STEP, CASE_EXCEPT);

		// 添加数据有效性
		jiraTemplet.addDataOption(CASE_RANK, Arrays.asList("HIGH", "NORMAL", "LOW"));
		jiraTemplet.addDataOption(CASE_STATUS, Arrays.asList("APPROVED", "DRAFT", "DEPRECATED"));

		return jiraTemplet;
	}
}
