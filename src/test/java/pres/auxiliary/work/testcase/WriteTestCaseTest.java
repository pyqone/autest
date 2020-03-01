package pres.auxiliary.work.testcase;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.dom4j.DocumentException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pres.auxiliary.work.n.tcase.FieldType;
import pres.auxiliary.work.n.tcase.MarkColorsType;
import pres.auxiliary.work.n.tcase.TestCaseTemplet;
import pres.auxiliary.work.n.tcase.WriteTestCase;
import pres.auxiliary.work.n.tcase.WriteTestCase.CaseMark;

/**
 * <p>
 * <b>文件名：</b>WriteTestCaseTest.java
 * </p>
 * <p>
 * <b>用途：</b>用于对WriteTestCase类进行单元测试
 * </p>
 * <p>
 * <b>编码时间：</b>2020年2月19日下午10:31:40
 * </p>
 * <p>
 * <b>修改时间：</b>2020年2月19日下午10:31:40
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public class WriteTestCaseTest {
	WriteTestCase wtc;

	/**
	 * 配置文件类对象
	 */
	File conFile = new File("src/test/java/pres/auxiliary/work/testcase/jira测试用例导入模板.xml");
	/**
	 * 模板文件类对象
	 */
	File tempFile = new File("Result/测试用例.xlsx");

	@BeforeClass
	public void createTemplet() throws DocumentException, IOException {
		TestCaseTemplet temp = new TestCaseTemplet(conFile, tempFile);
		temp.setCoverFile(true);
		temp.create();

		wtc = new WriteTestCase(conFile, tempFile);

		wtc.setPresupposeField(FieldType.EXPECT, "预期");
		wtc.setPresupposeField(FieldType.PRECONDITION, "前置条件");
		wtc.setPresupposeField(FieldType.RANK, "优先级");
		wtc.setPresupposeField(FieldType.STEP, "步骤");
		wtc.setPresupposeField(FieldType.TITLE, "标题");
	}

	/**
	 * 打开文件夹
	 * 
	 * @throws IOException
	 */
	@AfterClass
	public void openFolder() throws IOException {
		wtc.writeFile();
		System.out.println("-".repeat(20));
		System.out.println("优先级：");
		Arrays.stream(wtc.getRank()).forEach(System.out::println);
		wtc.getCaseXml();
//		java.awt.Desktop.getDesktop().open(wtc.getCaseXml());
		java.awt.Desktop.getDesktop().open(tempFile.getParentFile());
	}

	@BeforeMethod
	public void addContext(Method method) {
		System.out.println("=======正在运行：" + method.getName() + "=======");
		wtc.addRank(1);
		wtc.addStep("步骤1", "步骤2", "步骤3");
		wtc.addTitle("这是标题");
		wtc.addPrecondition("前置条件1", "前置条件2", "前置条件3");
		wtc.addExcept("预期1", "预期2", "预期3");
		wtc.addContext("目的", "这是目的1", "这是目的2");
		wtc.addContext("模块", "这是模块");
		wtc.addContext("状态", "这是状态");
		wtc.addContext("项目", "这是项目");
		wtc.addContext("设计者", "这是设计者");
	}

	/**
	 * 结束一条用例的编写
	 */
	@AfterMethod
	public void endCase() {
		wtc.end();
	}

	/**
	 * 测试{@link WriteTestCase#addContext(String, String)}方法
	 */
	@Test
	public void addContextTest() {
		wtc.addContext("标题", "这是标题").addContext("目的", "这是目的");
	}

	/**
	 * 测试{@link WriteTestCase#setPresupposeField(FieldType, String)}方法
	 */
	@Test(enabled = false)
	public void setPresupposeFieldTest() {
		wtc.setPresupposeField(FieldType.EXPECT, "预期");
		wtc.setPresupposeField(FieldType.PRECONDITION, "前置条件");
		wtc.setPresupposeField(FieldType.RANK, "优先级");
		wtc.setPresupposeField(FieldType.STEP, "步骤");
		wtc.setPresupposeField(FieldType.TITLE, "标题");
	}

	/**
	 * 测试{@link WriteTestCase#setRank(String...)}方法
	 */
	@Test
	public void setRankTest() {
		wtc.setRank("hehe", "haha", "xixi");
		wtc.addRank(2);
	}

	/**
	 * 测试{@link WriteTestCase#addStep(String...)}方法
	 */
	@Test
	public void addStepTest() {
		wtc.addStep("步骤1", "步骤2", "步骤3");
	}

	/**
	 * 测试{@link WriteTestCase#addExcept(String...)}方法
	 */
	@Test
	public void addExceptTest() {
		wtc.addExcept("预期1", "预期2", "预期3");
	}

	/**
	 * 测试{@link WriteTestCase#addPrecondition(String...)}方法
	 */
	@Test
	public void addPreconditionTest() {
		wtc.addPrecondition("前置条件1", "前置条件2", "前置条件3");
	}

	/**
	 * 测试{@link WriteTestCase#addTitle(String)}方法
	 */
	@Test
	public void addTitleTest() {
		wtc.addTitle("前置条件标题");
	}

	/**
	 * 测试{@link WriteTestCase#addRank(int)}方法
	 */
	@Test
	public void addRankTest_NotData() {
		wtc.addRank(1);
		wtc.addRank(-1);
		wtc.addRank(100);
	}

	/**
	 * 测试{@link WriteTestCase#end()}方法
	 */
	@Test
	public void endTest() {
		wtc.addRank(1);
		wtc.addStep("步骤1", "步骤2", "步骤3");
		wtc.addTitle("这是标题");
		wtc.addPrecondition("前置条件1", "前置条件2", "前置条件3");
		wtc.addExcept("预期1", "预期2", "预期3");

		wtc.end();
	}

	/**
	 * 测试{@link WriteTestCase#setReplactWord(String, String)}方法
	 */
	@Test
	public void setReplactWordTest() {
		wtc.setReplactWord("特别", "TB");
		wtc.setReplactWord("BT", "变态");

		wtc.addRank(1);
		wtc.addStep("#特别#1", "步骤#BT#", "步骤3");
		wtc.addTitle("这是#特别#标题#BT#");
		wtc.addPrecondition("#特别#前置条#BT#件1", "前#特别#置条#BT#件2", "前置条#BT#件3#特别#");
		wtc.addExcept("预#特别#1", "预#特别#期2", "预期3#BT#");
	}

	/**
	 * 测试{@link CaseMark#markField(String, String)}方法
	 */
	@Test
	public void markFieldTest() {
		CaseMark cm = wtc.end().markField("步骤", "步骤标记").markField(FieldType.EXPECT.getValue(), "预期标记");
		cm.markField("目的", "目的标记");
	}

	/**
	 * 测试{@link CaseMark#fieldBackground(String, MarkColorsType)}方法
	 */
	@Test
	public void fieldBackgroundTest() {
		CaseMark cm = wtc.end().changeFieldBackground("步骤", MarkColorsType.BLUE).changeFieldBackground(FieldType.EXPECT.getValue(),
				MarkColorsType.RED);
		cm.changeFieldBackground("目的", MarkColorsType.GREEN);
	}

	/**
	 * 测试{@link CaseMark#rowBackground(MarkColorsType)}方法
	 */
	@Test
	public void rowBackgroundTest() {
		wtc.end().changeRowBackground(MarkColorsType.YELLOW);
	}

	/**
	 * 测试{@link CaseMark#markText(String, int, MarkColorsType)}方法
	 */
	@Test
	public void markTextTest() {
		wtc.end().changeTextColor("目的", 0, MarkColorsType.YELLOW);
	}

	/**
	 * 测试{@link CaseMark#markStepAndExcept(int, MarkColorsType)}方法
	 */
	@Test
	public void markStepAndExceptTest() {
		wtc.end().markStepAndExcept(2, MarkColorsType.RED);
	}

	/**
	 * 综合测试标记方法
	 */
	@Test
	public void markTest() {
		wtc.end().markStepAndExcept(2, MarkColorsType.RED).markField("目的", "目的标记").changeFieldBackground("设计者",
				MarkColorsType.YELLOW);
	}
}
