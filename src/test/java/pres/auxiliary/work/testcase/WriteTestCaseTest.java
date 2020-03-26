package pres.auxiliary.work.testcase;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import org.dom4j.DocumentException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pres.auxiliary.work.n.testcase.FieldType;
import pres.auxiliary.work.n.testcase.InformationCase;
import pres.auxiliary.work.n.testcase.LabelType;
import pres.auxiliary.work.n.testcase.MarkColorsType;
import pres.auxiliary.work.n.testcase.TestCaseTemplet;
import pres.auxiliary.work.n.testcase.TestCaseWrite;
import pres.auxiliary.work.n.testcase.TestCaseWrite.CaseMark;

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
	TestCaseWrite wtc;

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

		wtc = new TestCaseWrite(conFile, tempFile);

		wtc.relevanceCase("步骤", LabelType.STEP.getName());
		wtc.relevanceCase("预期", LabelType.EXCEPT.getName());
		wtc.relevanceCase("优先级", LabelType.RANK.getName());
		wtc.relevanceCase("前置条件", LabelType.PRECONDITION.getName());
		wtc.relevanceCase("标题", LabelType.TITLE.getName());
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
//		System.out.println("优先级：");
//		Arrays.stream(wtc.getRank()).forEach(System.out::println);
//		wtc.getCaseXml();
//		java.awt.Desktop.getDesktop().open(wtc.getCaseXml());
		java.awt.Desktop.getDesktop().open(tempFile.getParentFile());
		java.awt.Desktop.getDesktop().open(tempFile);
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
	 * 测试{@link TestCaseWrite#addContent(String, String)}方法
	 */
	@Test
	public void addContentTest_NotDataValidetion() {
		wtc.addContent("标题", "这是标题").addContent("目的", "这是目的");
	}
	
	/**
	 * 测试{@link TestCaseWrite#addContent(String, String)}方法
	 */
	@Test
	public void addContentTest_hasDataValidetion() {
		wtc.addContent("优先级", "1").addContent("状态", "2", "3", "5", "-1", "哈哈哈");
	}

	/**
	 * 测试{@link TestCaseWrite#addCase(pres.auxiliary.work.n.testcase.Case)}方法
	 */
	@Test
	public void addCaseTest() {
		InformationCase ic = new InformationCase(new File("ConfigurationFiles/CaseConfigurationFile/CaseTemplet/AddInformation.xml"));
		
		wtc.addCase(ic.addRadioButtonCase("测试控件")).end();
	}
	

	/**
	 * 测试{@link TestCaseWrite#setReplactWord(String, String)}方法
	 */
	@Test
	public void setReplactWordTest() {
		wtc.setReplactWord("特别", "TB");
		wtc.setReplactWord("BT", "变态");

		wtc.addContent("步骤", "#特别#1", "步骤#BT#", "步骤3");
		wtc.addContent("标题", "这是#特别#标题#BT#");
		wtc.addContent("前置条件", "#特别#前置条#BT#件1", "前#特别#置条#BT#件2", "前置条#BT#件3#特别#");
		wtc.addContent("预期" ,"预#特别#1", "预#特别#期2", "预期3#BT#");
	}

	/**
	 * 测试{@link CaseMark#fieldComment(String, String)}方法
	 */
	@Test
	public void markFieldTest() {
		CaseMark cm = wtc.end().fieldComment("步骤", "步骤标记").fieldComment(FieldType.EXPECT.getValue(), "预期标记");
		cm.fieldComment("目的", "目的标记");
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
		wtc.end().markStepAndExcept(2, MarkColorsType.RED).fieldComment("目的", "目的标记").changeFieldBackground("设计者",
				MarkColorsType.YELLOW);
	}
	
	/**
	 * 测试{@link TestCaseWrite#removeContent(String, int...)}方法
	 */
	@Test
	public void removeContentTest_1() {
		wtc.addContent("步骤", "1", "2", "3", "4", "5").removeContent("步骤", 2).end();
		wtc.addContent("前置条件", "1", "2", "3", "4", "5").removeContent("前置条件", 2).end();
	}
	
	/**
	 * 测试{@link TestCaseWrite#removeContent(String, int...)}方法
	 */
	@Test
	public void removeContentTest_2() {
		wtc.addContent("步骤", "1", "2", "3", "4", "5").removeContent("前置条件", 2).end();
	}
	
	/**
	 * 测试{@link TestCaseWrite#removeContent(String, int...)}方法
	 */
	@Test
	public void removeContentTest_3() {
		wtc.addContent("步骤", "1", "2", "3", "4", "5").removeContent("步骤", 5).end();
		wtc.addContent("前置条件", "1", "2", "3", "4", "5").removeContent("前置条件", 5).end();
	}
	
	/**
	 * 测试{@link TestCaseWrite#insertContent(String, int, String...)}方法
	 */
	@Test
	public void insertContentTest_1() {
		wtc.addContent("步骤", "1", "2", "3", "4", "5").insertContent("步骤", 2, "2.1", "2.2").end();
		wtc.addContent("前置条件", "1", "2", "3", "4", "5").insertContent("前置条件", 2, "2.1", "2.2").end();
	}
	
	/**
	 * 测试{@link TestCaseWrite#insertContent(String, int, String...)}方法
	 */
	@Test
	public void insertContentTest_2() {
		wtc.addContent("步骤", "1", "2", "3", "4", "5").insertContent("前置条件", 2, "2.1", "2.2").end();
	}
	
	/**
	 * 测试{@link TestCaseWrite#insertContent(String, int, String...)}方法
	 */
	@Test
	public void insertContentTest_3() {
		wtc.addContent("步骤", "1", "2", "3", "4", "5").insertContent("步骤", 2).end();
		wtc.addContent("前置条件", "1", "2", "3", "4", "5").insertContent("前置条件", 2).end();
	}
	
	/**
	 * 测试{@link TestCaseWrite#insertContent(String, int, String...)}方法
	 */
	@Test
	public void insertContentTest_4() {
		wtc.addContent("步骤", "1", "2", "3", "4", "5").insertContent("步骤", 6, "2.1", "2.2").end();
		wtc.addContent("前置条件", "1", "2", "3", "4", "5").insertContent("前置条件", 6, "2.1", "2.2").end();
	}
	
	/**
	 * 测试{@link TestCaseWrite#insertContent(String, int, String...)}方法
	 */
	@Test
	public void insertContentTest_5() {
		wtc.addContent("步骤", "1", "2", "3", "4", "5").insertContent("步骤", 5, "2.1", "2.2").end();
		wtc.addContent("前置条件", "1", "2", "3", "4", "5").insertContent("前置条件", 5, "2.1", "2.2").end();
	}
	
	/**
	 * 测试{@link TestCaseWrite#replaceContent(String, int, String...)}方法
	 */
	@Test
	public void replaceContentTest_1() {
		wtc.addContent("步骤", "1", "2", "3", "4", "5").replaceContent("步骤", 2, "2.1", "2.2").end();
		wtc.addContent("前置条件", "1", "2", "3", "4", "5").replaceContent("前置条件", 2, "2.1", "2.2").end();
	}
	
	/**
	 * 测试{@link TestCaseWrite#replaceContent(String, int, String...)}方法
	 */
	@Test
	public void replaceContentTest_2() {
		wtc.addContent("步骤", "1", "2", "3", "4", "5").replaceContent("前置条件", 2, "2.1", "2.2").end();
	}
	
	/**
	 * 测试{@link TestCaseWrite#replaceContent(String, int, String...)}方法
	 */
	@Test
	public void replaceContentTest_3() {
		wtc.addContent("步骤", "1", "2", "3", "4", "5").replaceContent("步骤", 2).end();
		wtc.addContent("前置条件", "1", "2", "3", "4", "5").replaceContent("前置条件", 2).end();
	}
	
	/**
	 * 测试{@link TestCaseWrite#replaceContent(String, int, String...)}方法
	 */
	@Test
	public void replaceContentTest_4() {
		wtc.addContent("步骤", "1", "2", "3", "4", "5").replaceContent("步骤", 6, "2.1", "2.2").end();
		wtc.addContent("前置条件", "1", "2", "3", "4", "5").replaceContent("前置条件", 6, "2.1", "2.2").end();
	}
	
	/**
	 * 测试{@link TestCaseWrite#replaceContent(String, int, String...)}方法
	 */
	@Test
	public void replaceContentTest_5() {
		wtc.addContent("步骤", "1", "2", "3", "4", "5").replaceContent("步骤", 5, "2.1", "2.2").end();
		wtc.addContent("前置条件", "1", "2", "3", "4", "5").replaceContent("前置条件", 5, "2.1", "2.2").end();
	}
}
