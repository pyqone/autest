package pres.auxiliary.work.testcase;

import java.io.File;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pres.auxiliary.work.n.tcase.InformationCase;

public class InformationCaseTest {
	InformationCase ic = new InformationCase(new File("ConfigurationFiles/CaseConfigurationFile/CaseTemplet/AddInformation.xml"));
	
	@BeforeMethod
	public void start() {
		ic.setReplaceWord(InformationCase.BUTTON_NAME, "普通");
	}
	
	@Test
	public void getTextTest() {
	}
}
