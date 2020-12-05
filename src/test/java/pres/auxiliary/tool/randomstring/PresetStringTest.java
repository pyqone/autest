package pres.auxiliary.tool.randomstring;

import org.testng.annotations.Test;

import pres.auxiliary.tool.string.MobleNumberType;
import pres.auxiliary.tool.string.PresetString;

public class PresetStringTest {
	@Test
	public void IdentityCardTest() {
		System.out.println(PresetString.identityCard());
	}
	
	@Test
	public void getMobleNumberTest() {
		System.out.println(PresetString.mobleNumber(MobleNumberType.CHINA_MOBILE));
	}
}
