package pres.auxiliary.work.selenium.event;

import java.io.File;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pres.auxiliary.work.selenium.brower.ChromeBrower;
import pres.auxiliary.work.selenium.brower.ChromeBrower.ChromeOptionType;
import pres.auxiliary.work.selenium.element.delect.old.CommonBy;
import pres.auxiliary.work.selenium.element.delect.old.DataListBy;
import pres.auxiliary.work.selenium.element.delect.old.Element;

/**
 * <p><b>文件名：</b>CommonElementTest.java</p>
 * <p><b>用途：</b>
 * 对{@link TextEvent}类方法进行单元测试
 * </p>
 * <p><b>测试对象：</b>桂建通工资管理的工资单管理模块，获取第一条数据的单位信息</p>
 * <p><b>编码时间：</b>2020年4月30日上午7:44:34</p>
 * <p><b>修改时间：</b>2020年4月30日上午7:44:34</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public class TextEventTest {
	/**
	 * 列表上编号列第一个元素
	 */
	final String FRIST_ID_XPATH = "//*[@class=\"el-table__body-wrapper\"]/table/tbody/tr[1]/td[1]/div/span";
	/**
	 * 列表上编号列所有元素
	 */
	final String ID_LIST_XPATH = "//*[@class=\"el-table__body-wrapper\"]/table/tbody/tr/td[1]/div/span";
	
	ChromeBrower cb = new ChromeBrower(new File("Resource/BrowersDriver/Chrom/78.0394.70/chromedriver.exe"));
	CommonBy_Old ce;
	DataListBy_Old dle;
	TextEvent t;
	Element_Old turningButton;
	
	@BeforeClass
	public void init() {
		cb.addConfig(ChromeOptionType.CONTRAL_OPEN_BROWER, "127.0.0.1:9222");
		ce = new CommonBy_Old(cb.getDriver());
		dle = new DataListBy_Old(cb.getDriver());
		
		t = new TextEvent(cb.getDriver());
		
		turningButton = ce.getElement("//i[@class=\"el-icon el-icon-arrow-right\"]");
	}
	
	@AfterClass
	public void qiut() {
		cb.getDriver().quit();
	}
	
	@BeforeMethod
	public void switchRootFrame() {
		ce.switchRootFrame();
	}
	
	/**
	 * 测试普通元素调用{@link TextEvent#getText(pres.auxiliary.work.selenium.element.Element_Old)}方法
	 * @throws InterruptedException
	 */
	@Test
	public void getTextTest_CommonElement() throws InterruptedException {
		System.out.println("第一页：");
		System.out.println(t.getText(ce.getElement(FRIST_ID_XPATH)));
		//测试元素过期问题
		System.out.println("==========================================");
		System.out.println("第二页：");
		//测试元素过期问题
		turningButton.getWebElement().click();
		Thread.sleep(5000);
		//翻页后再获取
		System.out.println(t.getText(ce.getElement(FRIST_ID_XPATH)));
	}
	
	/**
	 * 测试列表元素调用{@link TextEvent#getText(pres.auxiliary.work.selenium.element.Element_Old)}方法
	 * @throws InterruptedException
	 */
	@Test
	public void getTextTest_DataListElement() throws InterruptedException {
		dle.add(ID_LIST_XPATH);
		System.out.println("第一页：");
		dle.getAllElement(ID_LIST_XPATH).forEach(element -> {
			System.out.println(t.getText(element));
		});
		//测试元素过期问题
		System.out.println("==========================================");
		turningButton.getWebElement().click();
		Thread.sleep(5000);
		System.out.println("第二页：");
		//翻页后再次获取
		dle.getAllElement(ID_LIST_XPATH).forEach(element -> {
			System.out.println(t.getText(element));
		});
	}
}
