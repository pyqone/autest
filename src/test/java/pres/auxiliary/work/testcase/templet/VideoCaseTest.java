package pres.auxiliary.work.testcase.templet;

import java.io.File;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pres.auxiliary.tool.file.excel.CreateExcelFile;
import pres.auxiliary.work.testcase.file.JiraTestCaseWrite;

public class VideoCaseTest {
	/**
	 * 指向生成的测试用例文件
	 */
	File testCaseFile  = new File("src/test/java/pres/auxiliary/work/testcase/templet/测试用例.xlsx");
	/**
	 * 指向测试用例文件字段配置文件
	 */
	File templetXml = new File("ConfigurationFiles/CaseConfigurationFile/FileTemplet/JiraCaseFileTemplet/jira测试用例导入模板.xml");
	/**
	 * 指向与InformationCase使用到的预设测试用例配置文件
	 */
	File browseListCase = new File("ConfigurationFiles/CaseConfigurationFile/CaseTemplet/Video.xml");
	
	VideoCase test = new VideoCase(browseListCase);
	/**
	 * 用于写入用例到文件中
	 */
	JiraTestCaseWrite tcw;
	
	@BeforeClass
	public void start() throws Exception {
		CreateExcelFile tct = new CreateExcelFile(templetXml, testCaseFile);
		//为方便演示，则允许覆盖用例文件
		tct.setCoverFile(true);
		//生成用例文件
		tct.create();
		
		//初始化
		tcw = new JiraTestCaseWrite(templetXml, testCaseFile);
		
		//设置每条测试用例中值都一样的字段
		tcw.setFieldValue("设计者", "彭宇琦");
		tcw.setFieldValue("状态", "2");
		
		test.setReplaceWord(VideoCase.VIDEO_TYPE, "培训教育");
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
	 * 用于测试{@link VideoCase#playVideoCase(boolean)}方法<br>
	 * 预期：<br>
	 * 
	 */
	@Test 
	public void playVideoCaseTest() {
		tcw.addCase(test.playVideoCase(true));
	}
	
	/**
	 * 用于测试{@link VideoCase#videoScreenshotCase()}方法<br>
	 * 预期：<br>
	 * 
	 */
	@Test 
	public void videoScreenshotCaseTest() {
		tcw.addCase(test.videoScreenshotCase());
	}
	
	/**
	 * 用于测试{@link VideoCase#videoAdvanceCase(boolean, boolean)}方法<br>
	 * 预期：<br>
	 * 
	 */
	@Test 
	public void videoAdvanceCaseTest() {
		tcw.addCase(test.videoAdvanceCase(true, true));
	}
	
	/**
	 * 用于测试{@link VideoCase#videoSpeedCase(boolean)}方法<br>
	 * 预期：<br>
	 * 
	 */
	@Test 
	public void videoSpeedCaseTest() {
		tcw.addCase(test.videoSpeedCase(true));
	}
	
	/**
	 * 用于测试{@link VideoCase#videoProgressBarCase()}方法<br>
	 * 预期：<br>
	 * 
	 */
	@Test 
	public void videoProgressBarCaseTest() {
		tcw.addCase(test.videoProgressBarCase());
	}
	
	/**
	 * 用于测试{@link VideoCase#videoFullScreenPlayCase()}方法<br>
	 * 预期：<br>
	 * 
	 */
	@Test 
	public void videoFullScreenPlayCaseTest() {
		tcw.addCase(test.videoFullScreenPlayCase());
	}
}
