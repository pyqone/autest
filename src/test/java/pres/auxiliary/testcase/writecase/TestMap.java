package pres.auxiliary.testcase.writecase;

import java.io.File;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import pres.auxiliary.work.old.testcase.templet.ZentaoTemplet;
import pres.auxiliary.work.old.testcase.writecase.Map;
import pres.auxiliary.work.old.testcase.writecase.PresetCase;

public class TestMap {
	static PresetCase pc;
	static Map m;
	
	@BeforeClass
	public static void createTemplet() {
		ZentaoTemplet.setCoverFile(true);
		ZentaoTemplet.create();
		
		pc = new PresetCase();
		m = new Map();
		
		pc.setModule("测试");
		
	}
	
	@AfterClass
	public static void openFolder() throws IOException {
		java.awt.Desktop.getDesktop().open(new File(ZentaoTemplet.getSavePath() + "/" + ZentaoTemplet.getFileName() + ".xlsx"));
	}
	
	@Test
	public void testAddRangeFindingCase() throws IOException {
		pc.getMap().addRangeFindingCase();
		m.addRangeFindingCase();
	}
	
	@Test
	public void testAddMapPointCase() throws IOException {
		pc.getMap().addMapPointCase("小车");
		m.addMapPointCase("卡车");
	}
	
	@Test
	public void testAddMapSearchInformationCase() throws IOException {
		pc.getMap().addMapSearchInformationCase("车牌", "小车");
		m.addMapSearchInformationCase("车牌", "卡车");
	}
	
	@Test
	public void testAddCarLocusPlaybackCase() throws IOException {
		pc.getMap().addCarLocusPlaybackCase();
		m.addCarLocusPlaybackCase();
	}
	
	@Test
	public void testAddShowLocusCase() throws IOException {
		pc.getMap().addShowLocusCase("小车", "打车");
		m.addShowLocusCase("公交");
	}
}
