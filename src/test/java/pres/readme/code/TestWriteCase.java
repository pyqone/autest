package pres.readme.code;

import java.io.File;
import java.io.IOException;

import org.dom4j.DocumentException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pres.auxiliary.tool.file.excel.CreateExcelFile;
import pres.auxiliary.work.testcase.file.BasicTestCaseWrite;
import pres.auxiliary.work.testcase.templet.InformationCase;
import pres.auxiliary.work.testcase.templet.LabelType;

public class TestWriteCase {
	/**
	 * 用例编写类
	 */
	BasicTestCaseWrite wtc;
	/**
	 * 添加信息用例模板类
	 */
	InformationCase ic;
	
	/**
	 * 配置文件类对象
	 */
	File conFile = new File(
			"ConfigurationFiles/CaseConfigurationFile/FileTemplet/JiraCaseFileTemplet/jira测试用例导入模板.xml");
	/**
	 * 模板文件类对象
	 */
	File tempFile = new File("Result/测试用例.xlsx");
	/**
	 * 添加信息类测试用例模板文件
	 */
	File caseTempFile = new File("ConfigurationFiles/CaseConfigurationFile/CaseTemplet/AddInformation.xml");

	@BeforeClass
	public void createTemplet() throws DocumentException, IOException {
		//创建测试用例模板文件
		CreateExcelFile temp = new CreateExcelFile(conFile, tempFile);
		temp.setCoverFile(true);
		temp.create();

		//构造用例编写类对象
		wtc = new BasicTestCaseWrite(conFile, tempFile);
		
		//添加常量词语
		wtc.setFieldValue("模块", "/测试项目/账号管理/创建账号");
		wtc.setFieldValue("目的", "验证创建账号界面各个控件输入是否有效");
		wtc.setFieldValue("状态", "1");
		wtc.setFieldValue("设计者", "test");
		wtc.setFieldValue("关联需求", "TEST-1");
		
		//添加与测试用例模板的字段关联
		wtc.relevanceCase("步骤", LabelType.STEP.getName());
		wtc.relevanceCase("预期", LabelType.EXCEPT.getName());
		wtc.relevanceCase("前置条件", LabelType.PRECONDITION.getName());
		wtc.relevanceCase("优先级", LabelType.RANK.getName());
		wtc.relevanceCase("标题", LabelType.TITLE.getName());
		
		//构造测试用例模板类对象
		ic = new InformationCase(caseTempFile);
		//添加需要替换的词语
		ic.setReplaceWord(InformationCase.ADD_INFORMATION, "账号");
		ic.setReplaceWord(InformationCase.BUTTON_NAME, "保存");
	}
	
	@AfterClass
	public void openFolder() throws IOException {
		java.awt.Desktop.getDesktop().open(wtc.getCaseXml());
		//将测试用例内容写入到文件中
		wtc.writeFile();
	}

	@Test
	public void addCase() {
		wtc.addCase(ic.addBasicTextboxCase("姓名", false, true, false)).end();
		wtc.addCase(ic.addIdCardCase("身份证", true, false, false)).end();
		
		wtc.setFieldValue("模块", "/测试项目/账号管理/创建账号2");
		wtc.addCase(ic.addIdCardCase("护照号", true, false, false)).end();
	}
	
	@Test
	public void myCaseTest() {
		MyCase mc = new MyCase(new File("src/test/java/pres/readme/code/MyCase.xml"));
		wtc.addCase(mc.myCase1()).end();
		wtc.addCase(mc.myCase2("测试")).end();
		wtc.addCase(mc.myCase3()).end();
	}
}
