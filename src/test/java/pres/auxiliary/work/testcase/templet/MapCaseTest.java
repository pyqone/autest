package pres.auxiliary.work.testcase.templet;

import java.io.File;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pres.auxiliary.tool.file.excel.CreateExcelFile;
import pres.auxiliary.work.testcase.file.JiraTestCaseWrite;
import pres.auxiliary.work.testcase.templet.MapCase.GraphType;

/**
 * <p><b>文件名：</b>MapCasetcw.addCase(test.java</p>
 * <p><b>用途：</b>
 * 测试{@link MapCase}类
 * </p>
 * <p><b>编码时间：</b>2020年11月13日下午7:11:46</p>
 * <p><b>修改时间：</b>2020年11月13日下午7:11:46</p>
 * @author 彭宇琦
 * @version Ver1.0
 *
 */
public class MapCaseTest {
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
	File browseListCase = new File("ConfigurationFiles/CaseConfigurationFile/CaseTemplet/Map.xml");
	
	MapCase test = new MapCase(browseListCase);
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
	 * 用于测试{@link MapCase#rangeFindingCase()}方法<br>
	 */
	@Test 
	public void rangeFindingCaseTest() {
		tcw.addCase(test.rangeFindingCase());
	}
	
	/**
	 * 用于测试{@link MapCase#mapPointCase(String)}方法<br>
	 * 预期：<br>
	 * 
	 */
	@Test 
	public void mapPointCaseTest() {
		tcw.addCase(test.mapPointCase("大楼"));
	}
	
	/**
	 * 用于测试{@link MapCase#mapSearchInformationCase(String, String)}方法<br>
	 * 预期：<br>
	 * 
	 */
	@Test 
	public void mapSearchInformationCaseTest() {
		tcw.addCase(test.mapSearchInformationCase("名称", "地址"));
	}
	
	/**
	 * 用于测试{@link MapCase#carLocusPlaybackCase()}方法<br>
	 * 预期：<br>
	 * 
	 */
	@Test 
	public void carLocusPlaybackCaseTest() {
		tcw.addCase(test.carLocusPlaybackCase());
	}
	
	/**
	 * 用于测试{@link MapCase#mapGraphSignCase(String, boolean, pres.auxiliary.work.testcase.templet.MapCase.GraphType...)}方法<br>
	 * 预期：<br>
	 * 
	 */
	@Test 
	public void mapGraphSignCaseTest() {
		tcw.addCase(test.mapGraphSignCase("围栏", true, GraphType.CIRCLE, GraphType.RECTANGLE));
	}
}
