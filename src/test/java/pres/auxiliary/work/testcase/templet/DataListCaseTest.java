package pres.auxiliary.work.testcase.templet;

import java.io.File;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pres.auxiliary.tool.file.excel.CreateExcelFile;
import pres.auxiliary.work.testcase.file.BasicTestCaseWrite;

public class DataListCaseTest {
	/**
	 * 指向生成的测试用例文件
	 */
	File testCaseFile  = new File("src/test/java/pres/auxiliary/work/testcase/测试用例.xlsx");
	/**
	 * 指向测试用例文件字段配置文件
	 */
	File templetXml = new File("ConfigurationFiles/CaseConfigurationFile/FileTemplet/JiraCaseFileTemplet/jira测试用例导入模板.xml");
	/**
	 * 指向与InformationCase使用到的预设测试用例配置文件
	 */
	File browseListCase = new File("ConfigurationFiles/CaseConfigurationFile/CaseTemplet/BrowseList.xml");
	
	DataListCase dc = new DataListCase(browseListCase);
	/**
	 * 用于写入用例到文件中
	 */
	BasicTestCaseWrite tcw;

	@BeforeClass
	public void start() throws Exception {
		CreateExcelFile tct = new CreateExcelFile(templetXml, testCaseFile);
		//为方便演示，则允许覆盖用例文件
		tct.setCoverFile(true);
		//生成用例文件
		tct.create();
		
		//初始化
		tcw = new BasicTestCaseWrite(templetXml, testCaseFile);
		
		dc.setReplaceWord(DataListCase.DATA_NAME, "用户");
		
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
	
	@AfterClass
	public void outputInformation() throws Exception {
		//写入用例
		tcw.writeFile();
		//打开用例文件夹
		java.awt.Desktop.getDesktop().open(testCaseFile.getParentFile());
		java.awt.Desktop.getDesktop().open(testCaseFile);
	}
	
	@AfterMethod
	public void endCase() {
		tcw.end();
	}

	/**
	 * 测试{@link DataListCase#appBrowseListCase()}
	 */
	@Test
	public void appBrowseListCaseTest() {
		tcw.addCase(dc.appBrowseListCase());
	}
	
	/**
	 * 测试{@link DataListCase#dateSearchCase(String)}
	 */
	@Test
	public void dateSearchCaseTest() {
		tcw.addCase(dc.dateSearchCase("考勤时间"));
	}
	
	/**
	 * 测试{@link DataListCase#exportListCase(String, boolean)}
	 */
	@Test
	public void exportListCaseTest_True() {
		tcw.addCase(dc.exportListCase("导出数据", true));
	}
	
	/**
	 * 测试{@link DataListCase#exportListCase(String, boolean)}
	 */
	@Test
	public void exportListCaseTest_false() {
		tcw.addCase(dc.exportListCase("导出数据", false));
	}
	
	/**
	 * 测试{@link DataListCase#importListCase()}
	 */
	@Test
	public void importListCaseTest() {
		tcw.addCase(dc.importListCase());
	}
	
	/**
	 * 测试{@link DataListCase#listSortCase(String)}
	 */
	@Test
	public void listSortCaseTest() {
		tcw.addCase(dc.listSortCase("创建时间"));
	}
	
	/**
	 * 测试{@link DataListCase#resetSearchCase()}
	 */
	@Test
	public void resetSearchCaseTest() {
		tcw.addCase(dc.resetSearchCase());
	}
	
	/**
	 * 测试{@link DataListCase#selectSearchCase(String)}
	 */
	@Test
	public void selectSearchCaseTest_String() {
		tcw.addCase(dc.selectSearchCase("班组"));
	}
	
	/**
	 * 测试{@link DataListCase#selectSearchCase(String, String)}
	 */
	@Test
	public void selectSearchCaseTest_StringString() {
		tcw.addCase(dc.selectSearchCase("企业单位", "班组"));
	}
	
	/**
	 * 测试{@link DataListCase#switchListShowDataCase()}
	 */
	@Test
	public void switchListShowDataCaseTest() {
		tcw.addCase(dc.switchListShowDataCase());
	}
	
	/**
	 * 测试{@link DataListCase#textboxSearchCase(String)}
	 */
	@Test
	public void textboxSearchCaseTest() {
		tcw.addCase(dc.textboxSearchCase("统一社会信用代码"));
	}
	
	/**
	 * 测试{@link DataListCase#timeQuantumSearchCase(String, String)}
	 */
	@Test
	public void timeQuantumSearchCaseTest() {
		tcw.addCase(dc.timeQuantumSearchCase("开工时间", "竣工时间"));
	}
	
	/**
	 * 测试{@link DataListCase#webBrowseListCase()}
	 */
	@Test
	public void webBrowseListCaseTest() {
		tcw.addCase(dc.webBrowseListCase());
	}
	
	/**
	 * 测试{@link DataListCase#delectDataCase(String)}
	 */
	@Test
	public void delectDataCaseTest() {
		tcw.addCase(dc.delectDataCase("删除"));
	}
}
