package pres.auxiliary.testcase.writecase;

import java.io.File;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import pres.auxiliary.work.testcase.templet.ZentaoTemplet;
import pres.auxiliary.work.testcase.writecase.PresetCase;
import pres.auxiliary.work.testcase.writecase.Username;

public class TestUsername {
	static PresetCase pc;
	static Username m;
	
	@BeforeClass
	public static void createTemplet() {
		ZentaoTemplet.setCoverFile(true);
		ZentaoTemplet.create();
		
		pc = new PresetCase();
		m = new Username();
		
		pc.setModule("测试");
		
	}
	
	@AfterClass
	public static void openFolder() throws IOException {
		java.awt.Desktop.getDesktop().open(new File(ZentaoTemplet.getSavePath() + "/" + ZentaoTemplet.getFileName() + ".xlsx"));
	}
	
	@Test
	public void testAddRightLoginCase() throws IOException {
		pc.getUsername().addRightLoginCase();
		m.addRightLoginCase();
	}
	
	@Test
	public void testAddErrorLoginCase() throws IOException {
		pc.getUsername().addErrorLoginCase();
		m.addErrorLoginCase();
	}
	
	@Test
	public void testAddCaptchaCase() throws IOException {
		pc.getUsername().addCaptchaCase();
		m.addCaptchaCase();
	}
	
	@Test
	public void testAddLoginAuthorityCase() throws IOException {
		pc.getUsername().addLoginAuthorityCase(true);
		m.addLoginAuthorityCase(false);
	}
	
	@Test
	public void testAddUsernameRegisterCase() throws IOException {
		pc.getUsername().addUsernameRegisterCase(true);
		m.addUsernameRegisterCase(false);
	}
	
	@Test
	public void testAddUsernameForgetCase() throws IOException {
		pc.getUsername().addUsernameForgetCase();
		m.addUsernameForgetCase();
	}
	
	@Test
	public void testAddPasswordRegisterOrForgetCase() throws IOException {
		pc.getUsername().addPasswordRegisterOrForgetCase("注册");
		m.addPasswordRegisterOrForgetCase("忘记密码");
	}
	
	@Test
	public void testAddCodeRegisterOrForgetCase() throws IOException {
		pc.getUsername().addCodeRegisterOrForgetCase("注册", true);
		m.addCodeRegisterOrForgetCase("忘记密码", false);
	}
	
	@Test
	public void testAddAlterPasswordCase() throws IOException {
		pc.getUsername().addAlterPasswordCase();
		m.addAlterPasswordCase();
	}
}
