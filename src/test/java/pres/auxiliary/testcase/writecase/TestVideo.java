package pres.auxiliary.testcase.writecase;

import java.io.File;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import pres.auxiliary.work.old.testcase.templet.ZentaoTemplet;
import pres.auxiliary.work.old.testcase.writecase.PresetCase;
import pres.auxiliary.work.old.testcase.writecase.Video;

public class TestVideo {
	static PresetCase pc;
	static Video m;
	
	@BeforeClass
	public static void createTemplet() throws IOException {
		ZentaoTemplet.setCoverFile(true);
		ZentaoTemplet.create();
		
		pc = new PresetCase();
		m = new Video();
		
		pc.setModule("测试");
		
		pc.getVideo().setVideoType("电影");
		m.setVideoType("电视剧");
		
	}
	
	@AfterClass
	public static void openFolder() throws IOException {
		java.awt.Desktop.getDesktop().open(new File(ZentaoTemplet.getSavePath() + "/" + ZentaoTemplet.getFileName() + ".xlsx"));
	}
	
	@Test
	public void testAddPlayVideoCase() throws IOException {
		pc.getVideo().addPlayVideoCase(true);
		m.addPlayVideoCase(false);
	}
	
	@Test
	public void testAddVideoScreenshotCase() throws IOException {
		pc.getVideo().addVideoScreenshotCase();
		m.addVideoScreenshotCase();
	}
	
	@Test
	public void testAddVideoAdvanceCase() throws IOException {
		pc.getVideo().addVideoAdvanceCase(true, true);
		m.addVideoAdvanceCase(false, false);
	}
	
	@Test
	public void testAddVideoSpeedCase() throws IOException {
		pc.getVideo().addVideoSpeedCase(true);
		m.addVideoSpeedCase(false);
	}
	
	@Test
	public void testAddVideoProgressBarCase() throws IOException {
		pc.getVideo().addVideoProgressBarCase();
		m.addVideoProgressBarCase();
	}
	
	@Test
	public void testAddFullScreenPlayCase() throws IOException {
		pc.getVideo().addFullScreenPlayCase();
		m.addFullScreenPlayCase();
	}
}
