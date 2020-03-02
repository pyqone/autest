package pres.auxiliary.work.testcase;

import java.io.File;
import java.io.IOException;

import org.dom4j.DocumentException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pres.auxiliary.work.n.tcase.FieldType;
import pres.auxiliary.work.n.tcase.MarkColorsType;
import pres.auxiliary.work.n.tcase.TestCaseTemplet;
import pres.auxiliary.work.n.tcase.WriteTestCase;

public class WriteTestCaseTest_WriteCase {
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
		wtc.getCaseXml();
		java.awt.Desktop.getDesktop().open(tempFile.getParentFile());
	}

	@Test
	public void step_01() {
		wtc.addStep("第一步", "第二部", "第三部", "第三部").addExcept("预期1", "预期2", "预期3").addTitle("用例1").end().changeFieldBackground("步骤", MarkColorsType.BLUE);

		wtc.addStep("第一步", "第二部", "第三部").addExcept("预期1", "预期2", "预期3").addTitle("用例2").end()
				.changeTextColor(FieldType.STEP.getValue(), 0, MarkColorsType.RED);

		wtc.addStep("第一步", "第二部").addExcept("预期1", "预期2").addTitle("用例3").end()
				.changeTextColor(FieldType.STEP.getValue(), 1, MarkColorsType.BLUE)
				.changeTextColor(FieldType.STEP.getValue(), 0, MarkColorsType.YELLOW);

		wtc.addStep("第一步", "第二部", "第三部").addExcept("预期1", "预期2", "预期3").addTitle("用例4").end();

		wtc.addStep("第一步", "第二").addExcept("预期1", "预期2").addTitle("用例5").end()
				.changeTextColor(FieldType.STEP.getValue(), 0, MarkColorsType.GREEN);

		wtc.addStep("第一步", "第二部").addExcept("预期1", "预期2").addTitle("用例6").end().changeRowTextColor(MarkColorsType.RED);
	}
}
