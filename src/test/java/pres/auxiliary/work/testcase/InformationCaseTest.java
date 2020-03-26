package pres.auxiliary.work.testcase;

import java.io.File;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pres.auxiliary.work.n.testcase.CaseContentException;
import pres.auxiliary.work.n.testcase.InformationCase;
import pres.auxiliary.work.n.testcase.InformationCase.FileRuleType;
import pres.auxiliary.work.n.testcase.InformationCase.InputRuleType;
import pres.auxiliary.work.n.testcase.InformationCase.UploadFileType;

public class InformationCaseTest {
	InformationCase ic = new InformationCase(new File("ConfigurationFiles/CaseConfigurationFile/CaseTemplet/AddInformation.xml"));
	
	@BeforeClass
	public void start() {
		ic.setReplaceWord(InformationCase.BUTTON_NAME, "保存");
		ic.setReplaceWord(InformationCase.ADD_INFORMATION, "用户");
		ic.setReplaceWord(InformationCase.FAIL_EXCEPT_TEXT_START, "测试*失败*预期前文");
		ic.setReplaceWord(InformationCase.FAIL_EXCEPT_TEXT_END, "测试*失败*预期后文");
		ic.setReplaceWord(InformationCase.SUCCESS_EXCEPT_TEXT_START, "测试*成功*预期前文");
		ic.setReplaceWord(InformationCase.SUCCESS_EXCEPT_TEXT_END, "测试*成功*预期后文");
	}
	
	@AfterMethod
	public void outputInformation() {
		System.out.println("------------------------------");
		ic.getFieldTextMap().forEach((k, v) -> {
			//可生成序号，类封装了普通的自增等操作
			System.out.println(k + ":");
			v.forEach(s -> {
				System.out.println(s);
			});
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
	 * 测试{@link InformationCase#editBasicTextboxCase(String, boolean, boolean, boolean, pres.auxiliary.tool.web.InputRuleType...)}方法
	 */
	@Test
	public void editBasicTextboxCaseTest_NotRule() {
		ic.editBasicTextboxCase("测试控件1", true, true, true);
	}
	
	/**
	 * 测试{@link InformationCase#editBasicTextboxCase(String, boolean, boolean, boolean, pres.auxiliary.tool.web.InputRuleType...)}方法
	 */
	@Test
	public void editBasicTextboxCaseCaseTest_HaveRule() {
		ic.editBasicTextboxCase("测试控件2", false, false, false, InformationCase.InputRuleType.EN, InformationCase.InputRuleType.NUM);
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
	
	/**
	 * 测试{@link InformationCase#addPhoneCase(String, boolean, boolean, boolean, pres.auxiliary.work.n.testcase.InformationCase.PhoneType...)}方法
	 */
	@Test
	public void addPhoneCaseTest_Fixed() {
		ic.addPhoneCase("测试控件15", true, false, false, InformationCase.PhoneType.FIXED);
	}
	
	/**
	 * 测试{@link InformationCase#addPhoneCase(String, boolean, boolean, boolean, pres.auxiliary.work.n.testcase.InformationCase.PhoneType...)}方法
	 */
	@Test
	public void addPhoneCaseTest_Moble() {
		ic.addPhoneCase("测试控件16", true, false, false, InformationCase.PhoneType.MOBLE);
	}
	
	/**
	 * 测试{@link InformationCase#addPhoneCase(String, boolean, boolean, boolean, pres.auxiliary.work.n.testcase.InformationCase.PhoneType...)}方法
	 */
	@Test
	public void addPhoneCaseTest_All() {
		ic.addPhoneCase("测试控件17", true, false, false, InformationCase.PhoneType.values());
	}
	
	/**
	 * 测试{@link InformationCase#addIDCardCase(String, boolean, boolean, boolean)}方法
	 */
	@Test
	public void addIDCardCaseTest() {
		ic.addIdCardCase("测试控件18", true, false, false);
	}
	
	/**
	 * 测试{@link InformationCase#addSelectboxCase(String, boolean, boolean, boolean)}方法
	 */
	@Test
	public void addSelectboxCaseTest_HasEmpty() {
		ic.addSelectboxCase("测试控件19", true, true, true);
	}
	
	/**
	 * 测试{@link InformationCase#addSelectboxCase(String, boolean, boolean, boolean)}方法
	 */
	@Test
	public void addSelectboxCaseTest_NotEmpty() {
		ic.addSelectboxCase("测试控件20", true, false, false);
	}
	
	/**
	 * 测试{@link InformationCase#addRadioButtonCase(String)}方法
	 */
	@Test
	public void addRadioButtonCaseTest() {
		ic.addRadioButtonCase("测试控件22");
	}
	
	/**
	 * 测试{@link InformationCase#addCheckboxCase(String, boolean)}方法
	 */
	@Test
	public void addCheckboxCaseTest() {
		ic.addCheckboxCase("测试控件22", true);
	}
	
	/**
	 * 测试{@link InformationCase#addDateCase(String, boolean, boolean, boolean)}方法
	 */
	@Test
	public void addDateCaseTest_1() {
		ic.addDateCase("测试控件22", true, true, true);
	}
	
	/**
	 * 测试{@link InformationCase#addDateCase(String, boolean, boolean, boolean)}方法
	 */
	@Test
	public void addDateCaseTest_2() {
		ic.addDateCase("测试控件23", false, false, false);
	}
	
	/**
	 * 测试{@link InformationCase#editDateCase(String, boolean, boolean, boolean)}方法
	 */
	@Test
	public void editDateCaseTest_2() {
		ic.editDateCase("测试控件23", false, false, false);
	}
	
	/**
	 * 测试{@link InformationCase#addStartDateCase(String, String, boolean, boolean, boolean)}方法
	 */
	@Test
	public void addStartDateCaseTest() {
		ic.addStartDateCase("测试控件23", "测试控件24", false, false, false);
	}
	
	/**
	 * 测试{@link InformationCase#addEndDateCaseTest(String, String, boolean, boolean, boolean)}方法
	 */
	@Test
	public void addEndDateCaseTest() {
		ic.addEndDateCase("测试控件24", "测试控件23", true, true, true);
	}
	
	/**
	 * 测试{@link InformationCase#addUploadFileCase(String, boolean, boolean, boolean, int, int, int, pres.auxiliary.work.n.testcase.InformationCase.UploadFileType, pres.auxiliary.work.n.testcase.InformationCase.FileRuleType...)}方法
	 */
	@Test
	public void addUploadFileCaseTest_1() {
		ic.addUploadFileCase("测试控件25", true, true, true, 10, 2, 10, UploadFileType.FILE, FileRuleType.CSV, FileRuleType.XLS, FileRuleType.XLSX);
	}
	
	/**
	 * 测试{@link InformationCase#addUploadFileCase(String, boolean, boolean, boolean, int, int, int, pres.auxiliary.work.n.testcase.InformationCase.UploadFileType, pres.auxiliary.work.n.testcase.InformationCase.FileRuleType...)}方法
	 */
	@Test
	public void addUploadFileCaseTest_2() {
		ic.addUploadFileCase("测试控件26", false, false, false, 0, 0, 10, UploadFileType.IMAGE);
	}
	
	/**
	 * 测试{@link InformationCase#addUploadFileCase(String, boolean, boolean, boolean, int, int, int, pres.auxiliary.work.n.testcase.InformationCase.UploadFileType, pres.auxiliary.work.n.testcase.InformationCase.FileRuleType...)}方法
	 */
	@Test
	public void addUploadFileCaseTest_3() {
		ic.addUploadFileCase("测试控件27", true, true, true, -1, 2, 0, UploadFileType.IMAGE);
	}
	
	/**
	 * 测试{@link InformationCase#addUploadFileCase(String, boolean, boolean, boolean, int, int, int, pres.auxiliary.work.n.testcase.InformationCase.UploadFileType, pres.auxiliary.work.n.testcase.InformationCase.FileRuleType...)}方法
	 */
	@Test
	public void addUploadFileCaseTest_4() {
		ic.addUploadFileCase("测试控件28", false, false, false, -1, 2, 2, UploadFileType.IMAGE);
	}
	
	/**
	 * 测试{@link InformationCase#addUploadFileCase(String, boolean, boolean, boolean, int, int, int, pres.auxiliary.work.n.testcase.InformationCase.UploadFileType, pres.auxiliary.work.n.testcase.InformationCase.FileRuleType...)}方法
	 */
	@Test
	public void addUploadFileCaseTest_5() {
		ic.addUploadFileCase("测试控件29", false, false, false, -1, -1, -1, UploadFileType.IMAGE);
	}
	
	/**
	 * 测试{@link InformationCase#browseEditPageCase()}方法
	 */
	@Test
	public void browseEditPageCaseTest() {
		ic.browseEditPageCase();
	}
	
	/**
	 * 测试{@link InformationCase#cencelSaveAddDataCase(String)}方法
	 */
	@Test
	public void cencelSaveAddDataCaseTest() {
		ic.cencelSaveAddDataCase("取消");
	}
	
	/**
	 * 测试{@link InformationCase#cencelSaveEditDataCase(String)}方法
	 */
	@Test
	public void cencelSaveEditDataCaseTest() {
		ic.cencelSaveEditDataCase("取消");
	}
}
