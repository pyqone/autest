package pres.auxiliary.work.testcase;

import java.io.File;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pres.auxiliary.work.n.tcase.CaseContentException;
import pres.auxiliary.work.n.tcase.InformationCase;
import pres.auxiliary.work.n.tcase.InformationCase.InputRuleType;

public class InformationCaseTest {
InformationCase ic = new InformationCase(new File("ConfigurationFiles/CaseConfigurationFile/CaseTemplet/AddInformation.xml"));
	
	@BeforeClass
	public void start() {
		ic.setReplaceWord(InformationCase.BUTTON_NAME, "保存");
		ic.setReplaceWord(InformationCase.ADD_INFORMATION, "用户列表");
		ic.setReplaceWord(InformationCase.FAIL_EXCEPT_TEXT_START, "测试*失败*预期前文");
		ic.setReplaceWord(InformationCase.FAIL_EXCEPT_TEXT_END, "测试*失败*预期后文");
		ic.setReplaceWord(InformationCase.SUCCESS_EXCEPT_TEXT_START, "测试*成功*预期前文");
		ic.setReplaceWord(InformationCase.SUCCESS_EXCEPT_TEXT_END, "测试*成功*预期后文");
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
	 * 测试{@link InformationCase#addBasicTextboxCase(String, boolean, boolean, boolean, pres.auxiliary.tool.web.InputRuleType...)}方法
	 */
	@Test
	public void addBasicTextboxCaseTest_NotRule() {
		ic.addBasicTextboxCase("测试控件1", true, true, true);
	}
	
	/**
	 * 测试{@link InformationCase#addBasicTextboxCase(String, boolean, boolean, boolean, pres.auxiliary.tool.web.InputRuleType...)}方法
	 */
	@Test
	public void addBasicTextboxCaseTest_HaveRule() {
		ic.addBasicTextboxCase("测试控件2", false, false, false, InformationCase.InputRuleType.EN, InformationCase.InputRuleType.NUM);
	}
	
	/**
	 * 测试{@link InformationCase#addLengthRuleTextboxCase(String, boolean, boolean, boolean, int, int, InputRuleType...)}方法
	 */
	@Test
	public void aaddLengthRuleTextboxCaseTest_1() {
		ic.addLengthRuleTextboxCase("测试控件3", true, true, true, 2, 5);
	}
	
	/**
	 * 测试{@link InformationCase#addLengthRuleTextboxCase(String, boolean, boolean, boolean, int, int, InputRuleType...)}方法
	 */
	@Test
	public void addLengthRuleTextboxCaseTest_2() {
		ic.addLengthRuleTextboxCase("测试控件4", false, false, true, 5, 5, InformationCase.InputRuleType.EN, InformationCase.InputRuleType.NUM);
	}
	/**
	 * 测试{@link InformationCase#addLengthRuleTextboxCase(String, boolean, boolean, boolean, int, int, InputRuleType...)}方法
	 */
	@Test
	public void aaddLengthRuleTextboxCaseTest_3() {
		ic.addLengthRuleTextboxCase("测试控件5", false, false, false, 0, 5);
	}
	/**
	 * 测试{@link InformationCase#addLengthRuleTextboxCase(String, boolean, boolean, boolean, int, int, InputRuleType...)}方法
	 */
	@Test
	public void aaddLengthRuleTextboxCaseTest_4() {
		ic.addLengthRuleTextboxCase("测试控件6", false, false, false, 2, -5);
	}
	
	/**
	 * 测试{@link InformationCase#addLengthRuleTextboxCase(String, boolean, boolean, boolean, int, int, InputRuleType...)}方法
	 */
	@Test
	public void aaddLengthRuleTextboxCaseTest_5() {
		ic.addLengthRuleTextboxCase("测试控件7", false, false, false, 5, 2);
	}
	
	/**
	 * 测试{@link InformationCase#addLengthRuleTextboxCase(String, boolean, boolean, boolean, int, int, InputRuleType...)}方法
	 */
	@Test(expectedExceptions = {CaseContentException.class})
	public void aaddLengthRuleTextboxCaseTest_6() {
		ic.addLengthRuleTextboxCase("测试控件8", false, false, false, -54, 0);
	}
	
	/**
	 * 测试{@link InformationCase#addNumberRuleTextboxCase(String, boolean, boolean, boolean, int, int, int)}方法
	 */
	@Test
	public void addNumberRuleTextboxCaseTest_1() {
		ic.addNumberRuleTextboxCase("测试控件9", true, false, false, 2, 0, 100);
	}
	
	/**
	 * 测试{@link InformationCase#addNumberRuleTextboxCase(String, boolean, boolean, boolean, int, int, int)}方法
	 */
	@Test
	public void addNumberRuleTextboxCaseTest_2() {
		ic.addNumberRuleTextboxCase("测试控件10", false, true, true, -1, InformationCase.MIN_NUMBER, 100);
	}
	
	/**
	 * 测试{@link InformationCase#addNumberRuleTextboxCase(String, boolean, boolean, boolean, int, int, int)}方法
	 */
	@Test
	public void addNumberRuleTextboxCaseTest_3() {
		ic.addNumberRuleTextboxCase("测试控件11", true, false, false, 2, 20, InformationCase.MAX_NUMBER);
	}
	
	/**
	 * 测试{@link InformationCase#addNumberRuleTextboxCase(String, boolean, boolean, boolean, int, int, int)}方法
	 */
	@Test(expectedExceptions = {CaseContentException.class})
	public void addNumberRuleTextboxCaseTest_4() {
		ic.addNumberRuleTextboxCase("测试控件12", true, false, false, 0, 100, 100);
	}
	
	/**
	 * 测试{@link InformationCase#addNumberRuleTextboxCase(String, boolean, boolean, boolean, int, int, int)}方法
	 */
	@Test(expectedExceptions = {CaseContentException.class})
	public void addNumberRuleTextboxCaseTest_5() {
		ic.addNumberRuleTextboxCase("测试控件13", true, false, false, 0, InformationCase.MIN_NUMBER, InformationCase.MAX_NUMBER);
	}
	
	/**
	 * 测试{@link InformationCase#addNumberRuleTextboxCase(String, boolean, boolean, boolean, int, int, int)}方法
	 */
	@Test
	public void addNumberRuleTextboxCaseTest_6() {
		ic.addNumberRuleTextboxCase("测试控件14", true, false, false, 2, InformationCase.MAX_NUMBER, 20);
	}
}
