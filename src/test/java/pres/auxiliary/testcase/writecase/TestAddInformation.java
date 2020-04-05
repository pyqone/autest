package pres.auxiliary.testcase.writecase;

import java.io.File;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import pres.auxiliary.work.old.testcase.change.Tab;
import pres.auxiliary.work.old.testcase.templet.ZentaoTemplet;
import pres.auxiliary.work.old.testcase.writecase.AddInformation;
import pres.auxiliary.work.old.testcase.writecase.FileType;
import pres.auxiliary.work.old.testcase.writecase.InputType;
import pres.auxiliary.work.old.testcase.writecase.PhoneType;
import pres.auxiliary.work.old.testcase.writecase.PresetCase;

public class TestAddInformation {
	static PresetCase pc;
	static AddInformation a;
	
	@BeforeClass
	public static void createTemplet() throws Exception {
		ZentaoTemplet.setCoverFile(true);
		ZentaoTemplet.create();
		
		pc = new PresetCase();
		a = new AddInformation();
		
		a.setButtonName("提交");
		a.setFailExpectation("失败");
		a.setSuccessExpectation("成功");
		a.setInformationName("测试");
		
		pc.getAddInformation().setButtonName("提交");
		pc.getAddInformation().setFailExpectation("失败");
		pc.getAddInformation().setSuccessExpectation("成功");
		pc.getAddInformation().setInformationName("测试");
		
		pc.setModule("测试");
		a.setModule("测试");
		
	}
	
	@AfterClass
	public static void openFolder() throws IOException {
		java.awt.Desktop.getDesktop().open(new File(ZentaoTemplet.getSavePath() + "/" + ZentaoTemplet.getFileName() + ".xlsx"));
	}
	
	@Test
	public void testAddTextboxCase() throws IOException {
		pc.getAddInformation().addTextboxCase("苹果", true, true, new char[] {InputType.CH, InputType.EN}, new int[] {5, 10}, null);
		a.addTextboxCase("安卓", false, false, null, null, new int[] {5, 10});
		pc.getAddInformation().addTextboxCase("苹果", true, false, new char[] {InputType.EN}, new int[] {5}, null);
		a.addTextboxCase("安卓", false, true, null, null, new int[] {10});
		pc.getAddInformation().addTextboxCase("苹果", true, false, new char[] {InputType.EN}, new int[] {5, pc.getAddInformation().NUM_NAN}, null);
		a.addTextboxCase("安卓", false, true, null, null, new int[] {10, pc.getAddInformation().NUM_NAN});
		pc.getAddInformation().addTextboxCase("苹果", true, false, new char[] {InputType.EN}, new int[] {pc.getAddInformation().NUM_NAN, 5}, null);
		a.addTextboxCase("安卓", false, true, null, null, new int[] {pc.getAddInformation().NUM_NAN, 10});
	}
	
	@Test
	public void testAddSelectboxCase() throws IOException {
		pc.getAddInformation().addSelectboxCase("苹果", true);
		a.addSelectboxCase("安卓", false);
	}
	
	@Test
	public void testAddRadioButtonCase() throws IOException {
		pc.getAddInformation().addRadioButtonCase("苹果", true);
		a.addRadioButtonCase("安卓", false);
	}
	
	@Test
	public void testAddCheckboxCase() throws IOException {
		pc.getAddInformation().addCheckboxCase("苹果", true);
		a.addCheckboxCase("安卓", false);
	}
	
	@Test
	public void testAddDateCase() throws IOException {
		pc.getAddInformation().addDateCase("苹果", true, false);
		a.addDateCase("安卓", false, true);
	}
	
	@Test
	public void testAddStartDateCase() throws IOException {
		pc.getAddInformation().addStartDateCase("苹果", true, false, "结束时间");
		a.addStartDateCase("安卓", false, true, "活动结束时间");
	}
	
	@Test
	public void teseAddEndDateCase() throws IOException {
		pc.getAddInformation().addEndDateCase("苹果", true, false, "开始时间");
		a.addEndDateCase("安卓", false, true, "活动开始时间");
	}
	
	@Test
	public void teseAddPhoneCase() throws IOException {
		pc.getAddInformation().addPhoneCase("苹果", true, false, PhoneType.FIXED);
		a.addPhoneCase("卡车", false, true, PhoneType.MOBLE).setRowColorTab(Tab.BLUE);
	}
	
	@Test
	public void teseAddIDCardCase() throws IOException {
		pc.getAddInformation().addIDCardCase("飞机证件", true, false).setRowColorTab(Tab.BLUE);
		a.addIDCardCase("汽车", false, true).setRowColorTab(Tab.BLUE);
	}
	
	@Test
	public void teseAddUploadImageCase() throws IOException {
		pc.getAddInformation().addUploadImageCase("苹果", true, true, true, true, true, new char[] {FileType.BMP, FileType.JPG}, new int[] {10});
		a.addUploadImageCase("安卓", true, true, true, true, true, null, null);
		
		pc.getAddInformation().addUploadImageCase("苹果", true, true, true, true, true, new char[] {FileType.BMP, FileType.JPG}, new int[] {10, 12});
		a.addUploadImageCase("安卓", true, true, true, true, true, null, new int[] {10, a.NUM_NAN});
	}
	
	@Test
	public void teseAddUploadImageCase_2() throws IOException {
		pc.getAddInformation().addUploadImageCase("苹果", true, true, true, true, true, new char[] {FileType.BMP, FileType.JPG}, new int[] {10});
	}
	
	@Test
	public void teseAddUploadFileCase() throws IOException {
		pc.getAddInformation().addUploadFileCase("苹果", true, true, true, new char[] {FileType.DOC, FileType.XLS}, new int[] {10});
		a.addUploadFileCase("安卓", true, true, true, null, null);
		
		pc.getAddInformation().addUploadFileCase("苹果", true, true, true, new char[] {FileType.DOCX, FileType.TXT}, new int[] {10, 12});
		a.addUploadFileCase("安卓", true, true, true, null, new int[] {10, a.NUM_NAN});
	}
	
	@Test
	public void teseAdd_Un_WholeInformationCase() throws IOException {
		pc.getAddInformation().addUnWholeInformationCase();
		a.addUnWholeInformationCase();
		
		pc.getAddInformation().addWholeInformationCase();
		a.addWholeInformationCase();
	}
}
