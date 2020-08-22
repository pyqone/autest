package pres.auxiliary.work.testcase.file;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import org.dom4j.DocumentException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pres.auxiliary.tool.file.excel.CreateExcelFile;
import pres.auxiliary.work.testcase.file.JiraTestCaseWrite.JiraFieldIdType;
import pres.auxiliary.work.testcase.templet.DataListCase;

/**
 * <p><b>文件名：</b>JiraTestCaseWriteTest.java</p>
 * <p><b>用途：</b>定义了上传至jira中tests的测试用例模板，类中定义了基本的字段枚举，在使用该类编写测试用例时，
 * 若需要使用预设的测试用例，则无需设置模板字段与预设用例字段间的关系</p>
 * <p><b>编码时间：</b>2020年4月6日 上午11:35:56</p>
 * <p><b>修改时间：</b>2020年4月6日 上午11:35:56</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 */
public class JiraTestCaseWriteTest {
	JiraTestCaseWrite wtc;

	/**
	 * 配置文件类对象
	 */
	File conFile = new File("ConfigurationFiles/CaseConfigurationFile/FileTemplet/JiraCaseFileTemplet/jira测试用例导入模板.xml");
	/**
	 * 模板文件类对象
	 */
	File tempFile = new File("Result/测试用例.xlsx");

	@BeforeClass
	public void createTemplet() throws DocumentException, IOException {
		CreateExcelFile temp = new CreateExcelFile(conFile, tempFile);
		temp.setCoverFile(true);
		temp.create();

		wtc = new JiraTestCaseWrite(conFile, tempFile);
	}

	/**
	 * 打开文件夹
	 * 
	 * @throws IOException
	 */
	@AfterClass
	public void openFolder() throws IOException {
		wtc.writeFile();
		System.out.println("----------------------------");
		java.awt.Desktop.getDesktop().open(tempFile.getParentFile());
//		java.awt.Desktop.getDesktop().open(tempFile);
	}

	@BeforeMethod
	public void addContent(Method method) {
		System.out.println("=======正在运行：" + method.getName() + "=======");
	}

	/**
	 * 结束一条用例的编写
	 */
	@AfterMethod
	public void endCase() {
		wtc.end();
	}
	
	/**
	 * 测试{@link JiraTestCaseWrite#addStep(String...)}方法
	 */
	@Test
	public void addStepTest() {
		wtc.addStep("步骤1", "步骤2");
	}
	
	/**
	 * 测试{@link JiraTestCaseWrite#addExcept(String...)}方法
	 */
	@Test
	public void addExceptTest() {
		wtc.addExcept("预期1", "预期2");
	}
	
	/**
	 * 测试{@link JiraTestCaseWrite#addStepAndExcept(String, String)}方法
	 */
	@Test
	public void addStepAndExceptTest() {
		wtc.addStepAndExcept("合并步骤1", "合并预期1").addStepAndExcept("合并步骤2", "合并预期2");
	}
	
	/**
	 * 测试{@link JiraTestCaseWrite#addTitle(String)}方法
	 */
	@Test
	public void addTitleTest() {
		wtc.addTitle("标题1").addTitle("标题2");
	}
	
	/**
	 * 测试{@link JiraTestCaseWrite#addModule(String...)}方法
	 */
	@Test
	public void addModuleTest_NotString() {
		wtc.addModule();
	}
	
	/**
	 * 测试{@link JiraTestCaseWrite#addModule(String...)}方法
	 */
	@Test
	public void addModuleTest_NumberString() {
		wtc.addModule("1");
	}
	
	/**
	 * 测试{@link JiraTestCaseWrite#addModule(String...)}方法
	 */
	@Test
	public void addModuleTest_TextString() {
		wtc.addModule("标段合并管理");
	}
	
	/**
	 * 测试{@link JiraTestCaseWrite#addModule(String...)}方法
	 */
	@Test
	public void addModuleTest_SearchString() {
		wtc.addModule("运营", "标段现场管理");
	}
	
	/**
	 * 测试{@link JiraTestCaseWrite#addModule(String...)}方法
	 */
	@Test
	public void addModuleTest_NoetSearchString() {
		wtc.addModule("企业", "合并");
	}
	
	/**
	 * 测试添加一条预设的测试用例
	 * @throws IOException 
	 */
	@Test
	public void addCaseTest() throws IOException {
		DataListCase dlc = new DataListCase(new File("ConfigurationFiles/CaseConfigurationFile/CaseTemplet/BrowseList.xml"));
		dlc.setReplaceWord(DataListCase.DATA_NAME, "用户");
		wtc.addCase(dlc.appBrowseListCase());
	}
	
	/**
	 * 综合测试
	 */
	@Test
	public void synthesizeTest() {
		wtc.addTitle("测试一个标题")
			.addModule("企业", "标段现场")
			.addStep("步骤1", "步骤2")
			.addExcept("预期1", "预期2")
			.addStepAndExcept("步骤3", "预期3")
			.addContent(JiraFieldIdType.CASE_KEY.getName(), "1")
			.addContent(JiraFieldIdType.COMPONENT.getName(), "4")
			.addContent(JiraFieldIdType.ISSUES.getName(), "4")
			.addContent(JiraFieldIdType.OBJECTIVE.getName(), "目的")
			.addContent(JiraFieldIdType.OWNER.getName(), "彭宇琦")
			.addContent(JiraFieldIdType.PRECONDITION.getName(), "前置1", "前置2")
			.addContent(JiraFieldIdType.PRIORITY.getName(), "1")
			.addContent(JiraFieldIdType.STATUS.getName(), "3")
			.end()
			.changeTextColor(JiraFieldIdType.STEP.getName(), 0, MarkColorsType.GREEN)
			.fieldComment(JiraFieldIdType.FOLDER.getName(), "模块标记");
	}
}
