package pres.auxiliary.work.testcase;

import java.io.File;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pres.auxiliary.work.n.tcase.InformationCase;
import pres.auxiliary.work.n.tcase.InputRuleType;

public class InformationCaseTest {
InformationCase ic = new InformationCase(new File("ConfigurationFiles/CaseConfigurationFile/CaseTemplet/AddInformation.xml"));
	
	@BeforeClass
	public void start() {
		ic.setReplaceWord(InformationCase.BUTTON_NAME, "保存");
		ic.setReplaceWord(InformationCase.ADD_INFORMATION, "用户列表");
		ic.setReplaceWord(InformationCase.FAIL_EXCEPT_TEXT_START, "测试失败预期前文");
		ic.setReplaceWord(InformationCase.FAIL_EXCEPT_TEXT_END, "测试失败预期后文");
		ic.setReplaceWord(InformationCase.SUCCESS_EXCEPT_TEXT_START, "测试成功预期前文");
		ic.setReplaceWord(InformationCase.SUCCESS_EXCEPT_TEXT_END, "测试成功预期后文");
	}
	
	@AfterMethod
	public void outputInformation() {
		System.out.println("------------------------------");
		ic.getFieldTextMap().forEach((k, v) -> {
			System.out.println(k + ":");
			v.forEach(System.out :: println);
		});
	}
	
	/**
	 * 测试{@link InformationCase#addWholeInformationCase()}方法
	 */
	@Test
	public void addWholeInformationCaseTest() {
		ic.addWholeInformationCase();
	}
	
	/**
	 * 测试{@link InformationCase#addUnWholeInformationCase()}方法
	 */
	@Test
	public void addUnWholeInformationCaseTest() {
		ic.addUnWholeInformationCase();
	}
	
	/**
	 * 测试{@link InformationCase#addBasicTextboxCase(String, boolean, boolean, pres.auxiliary.work.n.tcase.InputRuleType...)}方法
	 */
	@Test
	public void addBasicTextboxCaseTest_NotRule() {
		ic.addBasicTextboxCase("测试控件1", true, false);
	}
	
	/**
	 * 测试{@link InformationCase#addBasicTextboxCase(String, boolean, boolean, pres.auxiliary.work.n.tcase.InputRuleType...)}方法
	 */
	@Test
	public void addBasicTextboxCaseTest_HaveRule() {
		ic.addBasicTextboxCase("测试控件2", false, true, InputRuleType.EN, InputRuleType.NUM);
	}
}
