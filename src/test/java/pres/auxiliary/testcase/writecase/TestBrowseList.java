package pres.auxiliary.testcase.writecase;

import java.io.File;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import pres.auxiliary.work.testcase.templet.ZentaoTemplet;
import pres.auxiliary.work.testcase.writecase.BrowseList;
import pres.auxiliary.work.testcase.writecase.PresetCase;

public class TestBrowseList {
	static PresetCase pc;
	static BrowseList b;
	
	@BeforeClass
	public static void createTemplet() {
		ZentaoTemplet.setCoverFile(true);
		ZentaoTemplet.create();
		
		pc = new PresetCase();
		b = new BrowseList();
		
		pc.setModule("测试");
		
	}
	
	@AfterClass
	public static void openFolder() throws IOException {
		java.awt.Desktop.getDesktop().open(new File(ZentaoTemplet.getSavePath() + "/" + ZentaoTemplet.getFileName() + ".xlsx"));
	}
	
	@Test
	public void testAddAppBrowseListCase() throws IOException {
		pc.getBrowseList().addAppBrowseListCase("苹果");
		b.addAppBrowseListCase("安卓");
	}
	
	@Test
	public void testAddWebBrowseListCase() throws IOException {
		pc.getBrowseList().addWebBrowseListCase("苹果");
		b.addWebBrowseListCase("安卓");
	}
	
	@Test
	public void testAddInputSearchCase() throws IOException {
		pc.getBrowseList().addInputSearchCase("型号", "苹果");
		b.addInputSearchCase("型号", "安卓");
	}
	
	@Test
	public void testAddSelectSearchCase() throws IOException {
		pc.getBrowseList().addSelectSearchCase("型号", "苹果");
		b.addSelectSearchCase("型号", "安卓");
	}
	
	@Test
	public void testAddDateSearchCase() throws IOException {
		pc.getBrowseList().addDateSearchCase("开始时间", true, "苹果");
		b.addDateSearchCase("结束时间", false, "安卓");
	}
	
	@Test
	public void testAddListSortCase() throws IOException {
		pc.getBrowseList().addListSortCase("生产日期", "苹果");
		b.addListSortCase("生产日期", "安卓");
	}
	
	@Test
	public void testAddExportListCase() throws IOException {
		pc.getBrowseList().addExportListCase("苹果", true);
		b.addExportListCase("安卓", false);
	}
	
	@Test
	public void testAddImportListCase() throws IOException {
		pc.getBrowseList().addImportListCase("苹果");
		b.addImportListCase("安卓");
	}
	
	@Test
	public void testAddResetSearchCase() throws IOException {
		pc.getBrowseList().addResetSearchCase();
		b.addResetSearchCase();
	}
	
	@Test
	public void testAddSwitchListShowDataCase() throws IOException {
		pc.getBrowseList().addSwitchListShowDataCase();
		b.addSwitchListShowDataCase();
	}
}
