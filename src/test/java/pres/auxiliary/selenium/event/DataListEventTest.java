package pres.auxiliary.selenium.event;

import java.io.File;
import java.lang.reflect.Method;
import java.util.stream.Collectors;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import pres.auxiliary.work.selenium.tool.RecordTool;

/**
 * <p>
 * <b>文件名：</b>DataListEventTest.java
 * </p>
 * <p>
 * <b>用途：</b>用于对类DataListEvent进行单元测试，测试站点：http://jira.caih.local/issues/?filter=11480
 * </p>
 * <p>
 * <b>编码时间：</b>2019年10月9日上午11:40:17
 * </p>
 * <p>
 * <b>修改时间：</b>2019年11月29日上午9:53:37
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public class DataListEventTest {
	DataListEvent dle;
	ChromeBrower cb = new ChromeBrower("Resource/BrowersDriver/chromedriver.exe", 9222);

	String[] names = { "关键字", "概要", "经办人", "报告人", "解决结果" };

	/**
	 * 初始化数据，兼测试{@link DataListEvent#add(String...)}方法
	 */
	@BeforeTest
	public void openBrowers() {
		dle = new DataListEvent(cb.getDriver());
		dle.setXmlFile(new File("src/test/java/pres/auxiliary/selenium/event/DataListEventTest.xml"));
		RecordTool.getScreenshot().setDriver(dle.getDriver());
		RecordTool.getRecord().setBrower(cb);
		RecordTool.getRecord().setActionName("彭宇琦");

		RecordTool.setModule("测试模块", true);

		dle.add(names);
	}

	/**
	 * 分隔单元测试
	 */
	@BeforeMethod
	public void before(Method method) {
		RecordTool.startRecord(this.getClass().getName(), method.getName());
		System.out.println("正在运行" + method.getName() + "方法");
		System.out.println("-".repeat(20));
	}

	/**
	 * 分隔单元测试，并测试{@link DataListEvent#size(String)}方法
	 */
	@AfterMethod
	public void after(Method method) {
		RecordTool.endRecord();
		System.out.println("当前各列元素个数情况：");
		for (String name : names) {
			System.out.println(name + "=" + dle.size(name));
		}
		System.out.println("错误测试=" + dle.size("错误测试"));
		System.out.println("-".repeat(20));
		System.out.println();
	}

	@AfterTest
	public void closeBrowers() {
		cb.getDriver().quit();
	}

	/**
	 * 测试{@link DataListEvent#clear(String)}方法
	 */
	@Test(priority = 0)
	public void clearTest() {
		dle.clear("经办人");
	}

	/**
	 * 测试{@link DataListEvent#againGetElement()}方法
	 */
	@Test(priority = 1)
	public void againGetElementTest() {
		dle.againGetElement();
	}

	/**
	 * 测试{@link DataListEvent#getEvent(String, int)}方法及{@link ListEvent#getAttributeValue(String)}方法
	 */
	@Test(priority = 2)
	public void getAttributeValueTest() {
		dle.getEvent("关键字", 0).getAttributeValue("href");
		System.out.println(Event.getStringValve());
	}

	/**
	 * 测试{@link DataListEvent#getEvents(String)}方法及{@link ListEvent#getText()}方法
	 */
	@Test(priority = 3)
	public void getTextTest() {
		dle.getEvents("概要").forEach(event -> {
			event.getText();
			System.out.println(Event.getStringValve());
		});
	}

	/**
	 * 测试{@link DataListEvent#getEvents(String)}方法及{@link ListEvent#judgeKey(boolean, String...)}方法
	 */
	@Test(priority = 4)
	public void judgeKeyTest() {
		dle.getEvents("概要").stream().filter(event -> event.judgeKey(false, "企业", "工资").getBooleanValue())
				.map(event -> event.getText().getStringValve()).forEach(System.out::println);
	}

	/**
	 * 测试{@link DataListEvent#getEvents(String)}方法
	 * 及{@link ListEvent#judgeText(boolean, String)}方法
	 * 及{@link ListEvent#getIndex()}方法
	 */
	@Test(priority = 5)
	public void judgeTextTest() {
//		dle.getEvents("经办人").stream().filter(event -> event.judgeKey(true, "余威").getBooleanValue())
//				.map(event -> event.getIndex()).forEach(System.out::println);
		int count = dle.getEvents("经办人").stream().filter(event -> event.judgeKey(true, "呵呵").getBooleanValue())
				.collect(Collectors.toList()).size();
		System.out.println(count);
	}

	/**
	 * 测试{@link DataListEvent#getEvent(String, int)}方法及{@link ListEvent#rightClick()}方法
	 */
	@Test(priority = 6)
	public void rightClickTest() {
		dle.getEvent("创建日期", 3).rightClick();
	}

	/**
	 * 测试{@link DataListEvent#getEvent(String, int)}方法及{@link ListEvent#doubleClick()}方法
	 */
	@Test(priority = 7)
	public void doubleClickTest() {
		dle.getEvent("创建日期", 5).doubleClick();
	}

	/**
	 * 测试{@link DataListEvent#getEvent(String, int)}方法及{@link ListEvent#doubleClick()}方法
	 */
	@Test(priority = 8)
	public void clickTest() {
		dle.getEvent("创建日期", 10).click();
	}

	/**
	 * 测试{@link DataListEvent#getEvent(String, int)}方法及{@link ListEvent#getText()}方法，传入正数
	 */
	@Test(priority = 9)
	public void getEventTest_Positive() {
		dle.getEvent("概要", 1).getText();
		System.out.println(Event.getStringValve());
	}

	/**
	 * 测试{@link DataListEvent#getEvent(String, int)}方法及{@link ListEvent#getText()}方法，传入负数
	 */
	@Test(priority = 9)
	public void getEventTest_Minus() {
		dle.getEvent("概要", -1).getText();
		System.out.println(Event.getStringValve());
	}

	/**
	 * 测试{@link DataListEvent#getEvent(String, int)}方法及{@link ListEvent#getText()}方法，传入0
	 */
	@Test(priority = 9)
	public void getEventTest_Zero() {
		dle.getEvent("概要", 0).getText();
		System.out.println(Event.getStringValve());
	}

	/**
	 * 测试{@link DataListEvent#getEvents(String, int...)}方法
	 */
	@Test
	public void getEvents_indexs() {
		dle.getEvents("概要", 1, 2, 3, 5, 9).forEach(elemnt -> System.out.println(elemnt.getText().getStringValve()));
	}

	/**
	 * 测试{@link DataListEvent#getRandomCountEvent(String, int)}方法
	 */
	@Test
	public void getRandomCountEventTest() {
		dle.getRandomCountEvent("概要", 500).forEach(elemnt -> System.out.println(elemnt.getText().getStringValve()));
	}

	/**
	 * 测试{@link DataListEvent#shortListName()}方法
	 */
	@Test
	public void shortListNameTest() {
		System.out.println("最短列为：" + dle.shortListName() + "=" + dle.size(dle.shortListName()));
	}

	/**
	 * 测试{@link DataListEvent#removeElement(String, int)}方法
	 */
	@Test
	public void removeElementTest() {
		RecordTool.setRecordStep(false);
		dle.removeElement("关键字", 1);
		dle.getEvents("关键字").forEach(e -> System.out.println(e.getText().getStringValve()));
		System.out.println("-".repeat(20));
		dle.removeElement("关键字", -1);
		dle.getEvents("关键字").forEach(e -> System.out.println(e.getText().getStringValve()));
		System.out.println("-".repeat(20));
		dle.removeElement("关键字", 2);
		dle.getEvents("关键字").forEach(e -> System.out.println(e.getText().getStringValve()));
		System.out.println("-".repeat(20));
		dle.removeElement("关键字", -2);
		dle.getEvents("关键字").forEach(e -> System.out.println(e.getText().getStringValve()));
		System.out.println("-".repeat(20));
		dle.removeElement("关键字", 0);
		dle.getEvents("关键字").forEach(e -> System.out.println(e.getText().getStringValve()));
		System.out.println("-".repeat(20));
	}
	
	/**
	 * 测试{@link DataListEvent#removeLineElement(int)}方法
	 */
	@Test
	public void removeLineElement() {
		RecordTool.setRecordStep(false);
		dle.removeLineElement(1);
		dle.getEvents("概要").forEach(e -> System.out.println(e.getText().getStringValve()));
		System.out.println("-".repeat(20));
		dle.removeLineElement(-1);
		dle.getEvents("概要").forEach(e -> System.out.println(e.getText().getStringValve()));
		System.out.println("-".repeat(20));
		dle.removeLineElement(2);
		dle.getEvents("概要").forEach(e -> System.out.println(e.getText().getStringValve()));
		System.out.println("-".repeat(20));
		dle.removeLineElement(-2);
		dle.getEvents("概要").forEach(e -> System.out.println(e.getText().getStringValve()));
		System.out.println("-".repeat(20));
		dle.removeLineElement(0);
		dle.getEvents("概要").forEach(e -> System.out.println(e.getText().getStringValve()));
		System.out.println("-".repeat(20));
	}

	/**
	 * 测试{@link DataListEvent#getLineEvent(int)}方法
	 */
	@Test
	public void getLineEventTest() {
		RecordTool.setRecordStep(false);
		dle.getLineEvent(1).forEach((key, value) -> {
			System.out.println(key + "：" + value.getText().getStringValve());
		});
		System.out.println("-".repeat(20));
		dle.getLineEvent(-1).forEach((key, value) -> {
			System.out.println(key + "：" + value.getText().getStringValve());
		});
		System.out.println("-".repeat(20));
		dle.getLineEvent(2).forEach((key, value) -> {
			System.out.println(key + "：" + value.getText().getStringValve());
		});
		System.out.println("-".repeat(20));
		dle.getLineEvent(-2).forEach((key, value) -> {
			System.out.println(key + "：" + value.getText().getStringValve());
		});
		System.out.println("-".repeat(20));
		dle.getLineEvent(0).forEach((key, value) -> {
			System.out.println(key + "：" + value.getText().getStringValve());
		});
	}

	/**
	 * 测试{@link DataListEvent#filterText(String, String, boolean)}方法
	 */
	@Test
	public void filterTextTest() {
		dle.getEvents("概要", dle.filterText("概要", "考勤", false))
				.forEach(e -> System.out.println(e.getText().getStringValve()));
	}
	
	/**
	 * 测试{@link DataListEvent#pageTurning(int, String)}方法
	 */
	@Test
	public void pageTurningTest() {
		dle.pageTurning(5, "/html/body/div[1]/section/div[1]/div[3]/div/div/div/div/div/div/div[4]/div[2]/div/a[3]", 1000);
	}
	
	/**
	 * 测试首行为标题行与非标题行的区别
	 */
	@Test
	public void firstRowTitleTest() {
		RecordTool.setRecordStep(false);
		dle.setFristRowTitle(false);
		dle.againGetElement();
		dle.getEvents("概要").forEach(e -> System.out.println(e.getText().getStringValve()));
		System.out.println("-".repeat(20));
		dle.setFristRowTitle(true);
		dle.againGetElement();
		dle.getEvents("概要").forEach(e -> System.out.println(e.getText().getStringValve()));
	}
}
