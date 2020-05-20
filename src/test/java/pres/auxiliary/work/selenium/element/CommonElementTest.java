package pres.auxiliary.work.selenium.element;

import java.io.File;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pres.auxiliary.work.selenium.brower.ChromeBrower;
import pres.auxiliary.work.selenium.brower.ChromeBrower.ChromeOptionType;

/**
 * <p><b>文件名：</b>CommonElementTest.java</p>
 * <p><b>用途：</b>
 * 对{@link CommonElement}类方法进行单元测试
 * </p>
 * <p><b>测试对象：</b>桂建通工资管理的工资单管理模块，获取第一条数据的单位信息</p>
 * <p><b>编码时间：</b>2020年4月30日上午7:44:34</p>
 * <p><b>修改时间：</b>2020年4月30日上午7:44:34</p>
 * @author 
 * @version Ver1.0
 * @since JDK 12
 *
 */
public class CommonElementTest {
	ChromeBrower cb = new ChromeBrower(new File("Resource/BrowersDriver/Chrom/78.0394.70/chromedriver.exe"));
	CommonElement ce;
	
	@BeforeClass
	public void initDate() {
		cb.addConfig(ChromeOptionType.CONTRAL_OPEN_BROWER, "127.0.0.1:9222");
		ce = new CommonElement(cb);
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
	 * 用于测试非xml文件中的传参进行窗体切换与元素的获取
	 */
	@Test
	public void getCommonElementTest() {
		ce.switchFrame("//iframe[contains(@src, '/Regulatory/admin/index.jsp')]");
		ce.switchFrame("//iframe[contains(@src, '工资单管理')]");
		System.out.println(ce.getElement("//*[@id=\"listBox\"]/li[1]/div[1]/p/span[1]").getWebElement().getText());
	}
	
	/**
	 * 用于测试xml文件中的传参进行窗体切换与元素的获取
	 */
	@Test
	public void getXmlElementTest() {
		File xmlFile = new File("src/test/java/pres/auxiliary/work/selenium/element/测试文件.xml");
		ce.setXmlFile(xmlFile, false);
		ce.setAutoSwitchIframe(false);
		ce.switchFrame("主窗体");
		ce.switchFrame("工资发放详情");
		System.out.println(ce.getElement("单位名称").getWebElement().getText());
	}
	
	/**
	 * 用于测试xml文件的自动定位窗体
	 */
	@Test
	public void autoLocationElementTest() {
		File xmlFile = new File("src/test/java/pres/auxiliary/work/selenium/element/测试文件.xml");
		ce.setXmlFile(xmlFile, false);
		System.out.println(ce.getElement("单位名称").getWebElement().getText());
	}
	
	/**
	 * 用于测试xml文件的多次切换窗体后自动定位窗体
	 */
	@Test
	public void exceptAutoLocationElementTest() {
		File xmlFile = new File("src/test/java/pres/auxiliary/work/selenium/element/测试文件.xml");
		ce.setXmlFile(xmlFile, false);
		//先切主窗体
		ce.switchFrame("主窗体");
		//在获取元素前，会判断元素所在窗体，由于主窗体是爷爷辈窗体，获取元素前会切换工资发放详情窗体
		System.out.println(ce.getElement("单位名称").getWebElement().getText());
	}
}
