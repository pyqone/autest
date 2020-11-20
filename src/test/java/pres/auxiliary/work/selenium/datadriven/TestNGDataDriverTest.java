package pres.auxiliary.work.selenium.datadriven;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pres.auxiliary.tool.string.RandomString;
import pres.auxiliary.tool.string.StringMode;
import pres.auxiliary.work.selenium.datadriven.TestNGDataDriver.Data;

public class TestNGDataDriverTest {
	TestNGDataDriver testNGData = new TestNGDataDriver();
	
	File docxFile = new File("src/test/java/pres/auxiliary/work/selenium/datadriven/Test.docx");
	File xlsxFile = new File("src/test/java/pres/auxiliary/work/selenium/datadriven/AddTest.xlsx");
	File functionFile = new File("src/test/java/pres/auxiliary/work/selenium/datadriven/Functions.xlsx");
	
	/**
	 * 测试{@link TestNGDataDriver#addExcelData(java.io.File, String, boolean)}方法
	 * @throws IOException 
	 */
	@BeforeClass
	public void initData() throws IOException {
		String patten = SplitType.SPLIT_TAB.getSplitSign();
		String sheetName = "Sheet1";
		
		testNGData.addDataDriver(docxFile, patten, true);
		testNGData.addDataDriver(xlsxFile, sheetName, true);
	}
	
	@AfterMethod
	public void showData() {
		Arrays.stream(testNGData.getDataDriver()).forEach(objs -> {
			Arrays.stream(objs).forEach(obj -> {
				Data data = (Data)obj;
				
				testNGData.getTitle().forEach(title -> {
					System.out.println(title + " = " + data.getString(title));
				});
				
				System.out.println("-------------------------------");
			});
		});
	}
	
	/**
	 * 测试{@link TestNGDataDriver#addFunction(String, DataFunction)}方法
	 * @throws IOException
	 */
	@Test
	public void addFunctiontest() throws IOException {
		testNGData.addFunction(new DataDriverFunction("rs\\(\\d+\\)", text -> {
			int length = Integer.valueOf(text.substring(text.indexOf("(") + 1, (text.indexOf(")"))));
			return new RandomString(StringMode.CH).toString(length);
		}));
	}
	
	/**
	 * 测试{@link TestNGDataDriver#addFunction(String, DataFunction)}方法
	 * @throws IOException
	 */
	@Test
	public void addFunctiontest_Functions_Rs() throws IOException {
//		testNGData.addFunction(Functions.randomString());
		testNGData.addDataDriver(functionFile, "Sheet1", true);
	}
	
	/**
	 * 测试{@link TestNGDataDriver#addFunction(String, DataFunction)}方法
	 * @throws IOException
	 */
	@Test
	public void addFunctiontest_Functions_Time() throws IOException {
		testNGData.addFunction(Functions.getNowTime());
		testNGData.addFunction(Functions.getTime());
		testNGData.addDataDriver(functionFile, "Sheet1", true);
	}
	
	/**
	 * 测试{@link TestNGDataDriver#addDataDriver(String, Object...)}方法
	 */
	@Test
	public void addDataDriverTest_String_Objects() {
		testNGData.addDataDriver("123", "1", "2", "3");
		testNGData.addDataDriver("docx3", "1", "2", "3");
	}
}
