package pres.auxiliary.selenium.browers;

import org.junit.Test;

public class TestFirefoxBrower {
	@Test
	public void test1() {
		FirefoxBrower fb = new FirefoxBrower("", "pyqone", "http://baidu.com");
		fb.getDriver();
		
		System.out.println(fb.getBrowerName());
		System.out.println(fb.getBrowerVersion());
		System.out.println(fb.getSystemInformation());
		
		fb.close();
	}
}
