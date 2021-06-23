package com.auxiliary.testcase.file.extend;

import java.io.File;
import java.util.Arrays;

import org.dom4j.Document;

import com.auxiliary.testcase.file.WriteExcelTestCase;
import com.auxiliary.tool.file.FileTemplet;
import com.auxiliary.tool.file.WriteFileException;
import com.auxiliary.tool.file.WriteTempletFile;
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
	protected static final String DEFAULT_CASE_NAME = "测试用例";

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
		templetMap.get(DEFAULT_CASE_NAME).setSaveFile(saveFile);
	}

	/**
	 * 构造用例写入类，并重新设置模板
	 * 
	 * @param templet  模板类对象
	 */
	public WriteJiraExcelTestCase(FileTemplet templet) {
		this();
		// 重新设置模板
		addTemplet(DEFAULT_CASE_NAME, templet);
	}

	/**
	 * 根据已有的写入类对象，构造新的写入类对象，并保存原写入类对象中的模板、内容、字段默认内容以及词语替换内容
	 * <p>
	 * <b>注意：</b>在转换模板时，若模板的name字段为对象，则以默认名称“Sheet + 序号”来命名，并修改其中的name字段值
	 * </p>
	 * 
	 * @param writeTempletFile 文件写入类对象
	 * @throws WriteFileException 文件写入类对象为空时，抛出的异常
	 */
	public WriteJiraExcelTestCase(WriteTempletFile<?> writeTempletFile) {
		super(writeTempletFile);
	}

	@Override
	protected void initField() {
		super.initField();
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
	 * @return jira测试用例模板类对象
	 */
	public static ExcelFileTemplet getJiraCaseExcelTemplet() {
		ExcelFileTemplet jiraTemplet = new ExcelFileTemplet(new File("result/测试用例.xlsx"));

		// 设置模板全局内容
		jiraTemplet.setFreeze(1, 3).setFiltrate(true);

		// 设置模板字段内容
		jiraTemplet.addField(CASE_TITLE);
		jiraTemplet.addTitle(CASE_TITLE, "Name").setAlignment(CASE_TITLE, AlignmentType.HORIZONTAL_LEFT)
				.setAlignment(CASE_TITLE, AlignmentType.VERTICAL_CENTER).setWide(CASE_TITLE, 30.88);

		jiraTemplet.addField(CASE_OBJECTIVE);
		jiraTemplet.addTitle(CASE_OBJECTIVE, "Objective").setAlignment(CASE_OBJECTIVE, AlignmentType.HORIZONTAL_LEFT)
				.setAlignment(CASE_OBJECTIVE, AlignmentType.VERTICAL_CENTER).setAutoSerialNumber(CASE_OBJECTIVE, true)
				.setWide(CASE_OBJECTIVE, 18.25);

		jiraTemplet.addField(CASE_PRECONDITION);
		jiraTemplet.addTitle(CASE_PRECONDITION, "Precondition")
				.setAlignment(CASE_PRECONDITION, AlignmentType.HORIZONTAL_LEFT)
				.setAlignment(CASE_PRECONDITION, AlignmentType.VERTICAL_CENTER)
				.setAutoSerialNumber(CASE_PRECONDITION, true).setWide(CASE_PRECONDITION, 18.25);

		jiraTemplet.addField(CASE_STEP);
		jiraTemplet.addTitle(CASE_STEP, "Test Script (Step-by-Step) - Step")
				.setAlignment(CASE_STEP, AlignmentType.HORIZONTAL_LEFT)
				.setAlignment(CASE_STEP, AlignmentType.VERTICAL_CENTER).setContentBranch(CASE_STEP, 1)
				.setAutoSerialNumber(CASE_STEP, true).setWide(CASE_STEP, 45.75);

		jiraTemplet.addField(CASE_EXCEPT);
		jiraTemplet.addTitle(CASE_EXCEPT, "Test Script (Step-by-Step) - Expected Result")
				.setAlignment(CASE_EXCEPT, AlignmentType.HORIZONTAL_LEFT)
				.setAlignment(CASE_EXCEPT, AlignmentType.VERTICAL_CENTER).setContentBranch(CASE_EXCEPT, 1)
				.setAutoSerialNumber(CASE_EXCEPT, true).setWide(CASE_EXCEPT, 45.75);

		jiraTemplet.addField(CASE_MODULE);
		jiraTemplet.addTitle(CASE_MODULE, "Folder").setAlignment(CASE_MODULE, AlignmentType.HORIZONTAL_MIDDLE)
				.setAlignment(CASE_MODULE, AlignmentType.VERTICAL_CENTER).setWide(CASE_MODULE, 22);

		jiraTemplet.addField(CASE_STATUS);
		jiraTemplet.addTitle(CASE_STATUS, "Status").setAlignment(CASE_STATUS, AlignmentType.HORIZONTAL_MIDDLE)
				.setAlignment(CASE_STATUS, AlignmentType.VERTICAL_CENTER).setWide(CASE_STATUS, 10);

		jiraTemplet.addField(CASE_RANK);
		jiraTemplet.addTitle(CASE_RANK, "Priority").setAlignment(CASE_RANK, AlignmentType.HORIZONTAL_MIDDLE)
				.setAlignment(CASE_RANK, AlignmentType.VERTICAL_CENTER).setWide(CASE_RANK, 10);

		jiraTemplet.addField(CASE_COMPONENT);
		jiraTemplet.addTitle(CASE_COMPONENT, "Component").setAlignment(CASE_COMPONENT, AlignmentType.HORIZONTAL_MIDDLE)
				.setAlignment(CASE_COMPONENT, AlignmentType.VERTICAL_CENTER).setWide(CASE_COMPONENT, 10);

		jiraTemplet.addField(CASE_OWNER);
		jiraTemplet.addTitle(CASE_OWNER, "Owner").setAlignment(CASE_OWNER, AlignmentType.HORIZONTAL_MIDDLE)
				.setAlignment(CASE_OWNER, AlignmentType.VERTICAL_CENTER).setWide(CASE_OWNER, 10);

		jiraTemplet.addField(CASE_ISSUES);
		jiraTemplet.addTitle(CASE_ISSUES, "Coverage (Issues)")
				.setAlignment(CASE_ISSUES, AlignmentType.HORIZONTAL_MIDDLE)
				.setAlignment(CASE_ISSUES, AlignmentType.VERTICAL_CENTER).setWide(CASE_ISSUES, 20);

		// 添加数据有效性
		jiraTemplet.addDataOption(CASE_RANK, Arrays.asList("HIGH", "NORMAL", "LOW"));
		jiraTemplet.addDataOption(CASE_STATUS, Arrays.asList("APPROVED", "DRAFT", "DEPRECATED"));

		return jiraTemplet;
	}
}
