package pres.auxiliary.work.testcase;

import java.io.File;
import java.io.IOException;

import org.dom4j.DocumentException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import pres.auxiliary.work.n.testcase.file.MarkColorsType;
import pres.auxiliary.work.n.testcase.file.TestCaseTemplet;
import pres.auxiliary.work.n.testcase.file.TestCaseWrite;
import pres.auxiliary.work.n.testcase.templet.InformationCase;
import pres.auxiliary.work.n.testcase.templet.LabelType;
import pres.auxiliary.work.n.testcase.templet.InformationCase.InputRuleType;

public class ProgramWriteTestCaseDemo {
	/**
	 * 指向生成的测试用例文件
	 */
	File testCaseFile  = new File("src/test/java/pres/auxiliary/work/testcase/测试用例.xlsx");
	/**
	 * 指向测试用例文件字段配置文件
	 */
	File templetXml = new File("ConfigurationFiles/CaseConfigurationFile/FileTemplet/jira测试用例导入模板.xml");
	/**
	 * 指向与InformationCase使用到的预设测试用例配置文件
	 */
	File informationCase = new File("ConfigurationFiles/CaseConfigurationFile/CaseTemplet/AddInformation.xml");
	
	/**
	 * 用于写入用例到文件中
	 */
	TestCaseWrite tcw;
	/**
	 * 用于使用信息类相关的测试用例模板
	 */
	InformationCase ic;
	
	/**
	 * 生成测试用例文件，并初始化参数
	 * @throws DocumentException 
	 * @throws IOException 
	 */
	@BeforeTest
	public void createCaseFile() throws IOException, DocumentException {
		TestCaseTemplet tct = new TestCaseTemplet(templetXml, testCaseFile);
		//为方便演示，则允许覆盖用例文件
		tct.setCoverFile(true);
		//生成用例文件
		tct.create();
		
		//初始化
		tcw = new TestCaseWrite(templetXml, testCaseFile);
		ic = new InformationCase(informationCase);
		
		
	}
	
	/**
	 * 用于打开生成的测试用例文件所在的文件夹
	 * @throws IOException 
	 */
	@AfterTest
	public void openCaseFolder() throws IOException {
		//写入用例
		tcw.writeFile();
		//打开用例文件夹
		java.awt.Desktop.getDesktop().open(new File("src/test/java/pres/auxiliary/work/testcase"));
	}
	
	/**
	 * 设置测试用例文件中的字段与测试用例模板类之间的字段关联
	 */
	@BeforeClass
	public void setFieldRelevance() {
		//设置测试用例文件中字段与测试用例模板中的字段对应关系
		tcw.relevanceCase("步骤", LabelType.STEP.getName());
		tcw.relevanceCase("预期", LabelType.EXCEPT.getName());
		tcw.relevanceCase("优先级", LabelType.RANK.getName());
		tcw.relevanceCase("前置条件", LabelType.PRECONDITION.getName());
		tcw.relevanceCase("标题", LabelType.TITLE.getName());
		
		//设置每条测试用例中值都一样的字段
		tcw.setFieldValue("设计者", "彭宇琦");
		tcw.setFieldValue("状态", "2");
	}
	
	/**
	 * 设置模块信息
	 */
	@BeforeGroups(groups="添加项目")
	public void setCaseModule() {
		//设置测试用例模板中需要使用的字段值
		ic.setReplaceWord(InformationCase.BUTTON_NAME, "保存");
		ic.setReplaceWord(InformationCase.ADD_INFORMATION, "项目");
			
		//设置模块信息
		tcw.setFieldValue("模块", "项目管理");
	}
	
	@Test(groups="添加项目")
	public void addTestCase() {
		tcw.addCase(ic.addWholeInformationCase()).end();
		tcw.addCase(ic.addUnWholeInformationCase()).end();
		
		tcw.addCase(ic.addBasicTextboxCase("项目名称", true, false, false)).end();
		tcw.addCase(ic.addBasicTextboxCase("项目代号", true, false, false, InputRuleType.CAP, InputRuleType.NUM)).end();
		ic.setReplaceWord(InformationCase.SUCCESS_EXCEPT_TEXT_START, "选择后，其下方的日期随选项而改变");
		tcw.addCase(ic.addRadioButtonCase("起始日期")).end().fieldComment("步骤", "添加下方的日期控件内容会改变的步骤");
		tcw.addCase(ic.addStartDateCase("起始日期开始时间", "起始日期结束时间", true, true, false)).end();
		tcw.addCase(ic.addEndDateCase("起始日期结束时间", "起始日期开始时间", true, true, false)).end();
		tcw.addCase(ic.addBasicTextboxCase("可用工作日", false, true, false, InputRuleType.NUM)).end();
		tcw.addCase(ic.addBasicTextboxCase("项目名称", false, true, false)).end();
		tcw.addCase(ic.addSelectboxCase("项目类型", false, false, false)).end();
		tcw.addCase(ic.addSelectboxCase("关联产品", false, false, false)).end().changeTextColor("步骤", 1, 2, MarkColorsType.BLUE).fieldComment("步骤", "添加与产品联动的步骤与预期");
		tcw.addCase(ic.addSelectboxCase("关联计划", false, false, false)).end().changeTextColor("步骤", 1, 2, MarkColorsType.BLUE).fieldComment("步骤", "添加与计划联动的步骤与预期");
		tcw.addCase(ic.addLengthRuleTextboxCase("项目简介", false, true, false, 0, 500)).end();
		tcw.addCase(ic.addRadioButtonCase("访问控制")).end();
	}
}
