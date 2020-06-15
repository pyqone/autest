package pres.auxiliary.selenium.tool;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.openqa.selenium.TimeoutException;

import pres.auxiliary.work.selenium.tool.ExcelRecord;
import pres.auxiliary.work.selenium.tool.RecordTool;
import pres.auxiliary.work.selenium.tool.Screenshot;

public class TestRecord {
	@Rule
	public TestName name = new TestName();
	static ExcelRecord r;
	static FirefoxBrower fb;
	static Event event;
	static Screenshot sc;

	@BeforeClass
	public static void open() {
		fb = new FirefoxBrower("", "", "http://116.10.187.227:88/zentao/user-login.html");
		fb.getDriver();
		r.setBrower(fb);
		r.setActionName("彭宇琦");
		sc = new Screenshot(fb.getDriver());
		sc.setSavePath("C:\\AutoTest\\Screenshot\\");
		sc.setTime(1000);
		event = Event.newInstance(fb.getDriver());
		event.setXmlFile(new File("src/test/java/pres/auxiliary/selenium/operation/登录界面.xml"));
		r = RecordTool.getRecord();
	}

	@AfterClass
	public static void close() {
		fb.close();
	}

	@Before
	public void startRecord() {
		r.startRecord(this.getClass().getName(), name.getMethodName());
		
		r.caseID("Test_01_01");
		r.caseModule("登录");
		r.caseTitle("测试非正常登录系统");
		r.caseCondition("进入登录页面");
	}

	@After
	public void endRecord() {
		r.endRecord();
	}

//	@Ignore
	@Test
	public void test1() throws Exception {
		try {
			r.caseStep("通过正确的用户名和密码登录");
			r.caseExpect("能正常登录系统");
			r.caseFlow("主线流");
			
			event.getTextEvent().input("禅道用户名", "pengyuqi");
			event.getTextEvent().input("禅道密码", "123456");
			event.getClickEvent().click("禅道登录");

			File f = sc.creatImage("正常登录");

			try {
				event.getClickEvent().click("禅道退出");
				r.result("输入正确的用户名和密码后能正常登录，测试通过", true);
				r.runScreenshot(f);
			} catch (TimeoutException e) {
				r.result("输入正确的用户名和密码后不能正常登录，测试不通过，详见截图：" + f.getAbsolutePath());
				r.runScreenshot(f);
			}
		} catch (Exception e) {
			r.setException(e);
			r.errorScreenshot(sc.creatImage("抛出异常"));
		}
	}

//	@Ignore
	@Test
	public void test2() throws Exception {
		try {
			r.caseStep("通过不正确的用户名和密码登录");
			r.caseExpect("不能正常登录系统");
			r.caseFlow("备选流1");
			
			event.getTextEvent().input("禅道用户名", "pengyqi");
			event.getTextEvent().input("禅道密码", "123456");
			event.getClickEvent().click("禅道登录");
			fb.getDriver().switchTo().alert().accept();

			File f = sc.creatImage("不正常登录");

			try {
				event.getClickEvent().click("禅道退出");
				r.result("输入不正确的用户名和密码后能正常登录，测试不通过", true);
				r.runScreenshot(f);
			} catch (TimeoutException e) {
				r.result("输入不正确的用户名和密码后不能正常登录，测试通过");
				r.runScreenshot(f);
			}
		} catch (Exception e) {
			r.setException(e);
			r.errorScreenshot(sc.creatImage("抛出异常"));
		}
	}

//	@Ignore
	@Test
	public void test3() throws Exception {
		try {
			event.getTextEvent().input("禅道用户名", "pengyqi");
			event.getTextEvent().input("禅道密码2", "123456");
			event.getClickEvent().click("禅道登录");

			File f = sc.creatImage("乱登录");

			try {
				event.getClickEvent().click("禅道退出");
				r.result("输入不正确的用户名和密码后能正常登录，测试不通过，详见截图：" + f.getAbsolutePath());
				r.runScreenshot(f);
			} catch (TimeoutException e) {
				r.result("输入不正确的用户名和密码后不能正常登录，测试通过，详见截图：" + f.getAbsolutePath());
				r.runScreenshot(f);
			}
		} catch (Exception e) {
			r.setException(e);
			r.errorScreenshot(sc.creatImage("抛出异常"));
		}
	}
	
	@Ignore
	@Test
	public void test4() {
		r.result("呵呵呵",false);
		r.result("哈哈哈", true);
		r.result("嘻嘻嘻");
		r.result("嘎嘎嘎",true);
		r.result("唧唧唧");
	}
}
