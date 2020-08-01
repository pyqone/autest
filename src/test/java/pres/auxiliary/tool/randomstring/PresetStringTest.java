package pres.auxiliary.tool.randomstring;

import org.testng.annotations.Test;

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
