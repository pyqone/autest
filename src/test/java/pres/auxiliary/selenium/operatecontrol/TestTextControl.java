package pres.auxiliary.selenium.operatecontrol;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import pres.auxiliary.selenium.browers.FirefoxBrower;
import pres.auxiliary.selenium.event.TextEvent;

public class TestTextControl {
	TextEvent tc;
	WebDriver d;
	
	@Before
	public void openBrowers() {
		d = new FirefoxBrower("", "pyqone", "http://www.hao123.com").getDriver();
		tc = new TextEvent(d);
		tc.setXmlFile(new File("src/test/java/pres/auxiliary/selenium/event/测试.xml"));
	} 
	
	@After
	public void closeBrowers() {
		//d.close();
	}
	
	@Test
	public void test1() {
		tc.input("呵呵呵", "哈哈哈");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test2() {
		tc.input("//*[@id=\"search-input\"]", "哈哈哈");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test3() {
		tc.input("/html/body/div[2]/div[2]/div/div[1]/div[2]/div/div/div[1]/div[3]/form/div[1]/div/input", "哈哈哈");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test4() {
		tc.input("html body div#skinroot.sk_skin-color-green div.layout-container.s-sbg1 div.layout-container-inner.s-sbg2 div div.hao123-search-panel-box div#hao123-search-panel.g-wd.page-width.hao123-search-panel div.hao123-search.hao123-indexsearchlist1 div#search.search div.right.form-wrapper form#search-form.form-hook div.input-wrapper.wrapper-hook.g-ib div.input-shadow.shadow-hook.g-ib input#search-input.input.input-hook", "哈哈哈");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test5() {
		tc.input("嘻嘻嘻", "哈哈哈");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test6() {
		tc.input("嘎嘎嘎", "哈哈哈");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
