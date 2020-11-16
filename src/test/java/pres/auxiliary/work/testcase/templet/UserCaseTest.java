package pres.auxiliary.work.testcase.templet;

import java.io.File;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pres.auxiliary.tool.file.excel.CreateExcelFile;
import pres.auxiliary.work.testcase.file.JiraTestCaseWrite;
import pres.auxiliary.work.testcase.templet.UserCase.OprateType;

public class UserCaseTest {
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
	File browseListCase = new File("ConfigurationFiles/CaseConfigurationFile/CaseTemplet/Username.xml");
	
	UserCase test = new UserCase(browseListCase);
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
	 * 用于测试{@link UserCase#rightLoginCase()}方法<br>
	 * 预期：<br>
	 * 
	 */
	@Test 
	public void rightLoginCaseTest() {
		tcw.addCase(test.rightLoginCase());
	}
	
	/**
	 * 用于测试{@link UserCase#errorLoginCase()}方法<br>
	 * 预期：<br>
	 * 
	 */
	@Test 
	public void errorLoginCaseTest() {
		tcw.addCase(test.errorLoginCase());
	}
	
	/**
	 * 用于测试{@link UserCase#captchaLoginCase()}方法<br>
	 * 预期：<br>
	 * 
	 */
	@Test 
	public void captchaLoginCaseTest() {
		tcw.addCase(test.captchaLoginCase());
	}
	
	/**
	 * 用于测试{@link UserCase#loginAuthorityCase(boolean)}方法<br>
	 * 预期：<br>
	 * 
	 */
	@Test 
	public void loginAuthorityCaseTest() {
		tcw.addCase(test.loginAuthorityCase(true));
	}
	
	/**
	 * 用于测试{@link UserCase#usernameRegisterCase}方法<br>
	 * 预期：<br>
	 * 
	 */
	@Test 
	public void usernameRegisterCaseTest() {
		tcw.addCase(test.usernameRegisterCase(true));
	}
	
	/**
	 * 用于测试{@link UserCase#usernameForgetCase}方法<br>
	 * 预期：<br>
	 * 
	 */
	@Test 
	public void usernameForgetCaseTest() {
		tcw.addCase(test.usernameForgetCase());
	}
	
	/**
	 * 用于测试{@link UserCase#passwordRegisterOrForgetCase(pres.auxiliary.work.testcase.templet.UserCase.OprateType)}方法<br>
	 * 预期：<br>
	 * 
	 */
	@Test 
	public void passwordRegisterOrForgetCaseTest() {
		tcw.addCase(test.passwordRegisterOrForgetCase(OprateType.FORGET_PASSWORD));
	}
	
	/**
	 * 用于测试{@link UserCase#captchaOprateCase}方法<br>
	 * 预期：<br>
	 * 
	 */
	@Test 
	public void captchaOprateCaseTest() {
		tcw.addCase(test.captchaOprateCase(OprateType.FORGET_PASSWORD, true));
	}
	
	/**
	 * 用于测试{@link UserCase#amendPasswordCase()}方法<br>
	 * 预期：<br>
	 * 
	 */
	@Test 
	public void amendPasswordCaseTest() {
		tcw.addCase(test.amendPasswordCase());
	}
}
