package pres.auxiliary.work.selenium.event.extend;

import java.io.File;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import pres.auxiliary.work.selenium.brower.ChromeBrower;
import pres.auxiliary.work.selenium.brower.ChromeBrower.ChromeOptionType;
import pres.auxiliary.work.selenium.element.CommonBy;
import pres.auxiliary.work.selenium.element.DataListBy;
import pres.auxiliary.work.selenium.element.Element;
import pres.auxiliary.work.selenium.event.TextEvent;
import pres.auxiliary.work.selenium.location.ByType;
import pres.auxiliary.work.selenium.location.NoFileLocation;

/**
 * <p><b>文件名：</b>DataTableEventTest.java</p>
 * <p><b>用途：</b>
 * 对{@link DataTableEvent}类进行单元测试
 * </p>
 * <p><b>编码时间：</b>2020年11月25日上午8:15:29</p>
 * <p><b>修改时间：</b>2020年11月25日上午8:15:29</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 *
 */
public class DataTableEventTest {
	//---------------------常量区---------------------------
	/**
	 * 谷歌浏览器驱动文件
	 */
	final File driverFile = new File("Resource/BrowersDriver/Chrom/86.0.4240.22/chromedriver.exe");
	//---------------------常量区---------------------------
	
	//---------------------属性区---------------------------
	/**
	 * 浏览器对象
	 */
	ChromeBrower brower;
	/**
	 * 单一元素获取类对象
	 */
	CommonBy cb;
	/**
	 * 多元素获取类对象
	 */
	DataListBy dlb;
	/**
	 * 无文件存储元素信息
	 */
	NoFileLocation nl;
	
	TextEvent te;
	//---------------------属性区---------------------------
	
	//---------------------待测对象区---------------------------
	/**
	 * 待测类对象
	 */
	DataTableEvent test;
	//---------------------待测对象区---------------------------
	
	//---------------------数据初始化区---------------------------
	/**
	 * 初始化数据
	 */
	@BeforeTest
	public void initData() {
		brower = new ChromeBrower(driverFile);
		brower.addConfig(ChromeOptionType.CONTRAL_OPEN_BROWER, "127.0.0.1:9222");
		
		test = new DataTableEvent(brower);
		
		nl = new NoFileLocation();
		
		cb = new CommonBy(brower);
		cb.setReadMode(nl, true);
		
		dlb = new DataListBy(brower);
		dlb.setReadMode(nl, true);
		
		te = new TextEvent(brower);
	}
	
	/**
	 * 用于设置数据
	 */
	@BeforeClass
	public void setData() {
		nl.putElementLocation("上一页", ByType.XPATH, "//*[@title=\"上一页\"]");
		nl.putElementLocation("下一页", ByType.XPATH, "//*[@title=\"下一页\"]");
		
		nl.putElementLocation("序号列", ByType.XPATH, "//tbody/tr/td[1]");
		nl.putElementLocation("账号列", ByType.XPATH, "//tbody/tr/td[2]");
		nl.putElementLocation("姓名列", ByType.XPATH, "//tbody/tr/td[3]");
		nl.putElementLocation("绑定手机号列", ByType.XPATH, "//tbody/tr/td[4]");
		nl.putElementLocation("是否实人认证列", ByType.XPATH, "//tbody/tr/td[5]");
		nl.putElementLocation("所属企业列", ByType.XPATH, "//tbody/tr/td[6]");
		nl.putElementLocation("创建时间列", ByType.XPATH, "//tbody/tr/td[8]");
		
		test.add(dlb.find("序号列"));
		test.add(dlb.find("账号列"));
		test.add(dlb.find("姓名列"));
		test.add(dlb.find("绑定手机号列"));
		test.add(dlb.find("是否实人认证列"));
		test.add(dlb.find("所属企业列"));
		test.add(dlb.find("创建时间列"));
	}
	
	/**
	 * 用于关闭驱动连接
	 */
	@AfterClass
	public void quit() {
		brower.closeBrower();
	}
	//---------------------数据初始化区---------------------------
	
	//---------------------单元测试区---------------------------
	/**
	 * 用于测试{@link DataTableEvent#pageTurning(Element)}方法<br>
	 * 预期：<br>
	 * 
	 */
	@Test 
	public void pageTurningTest() {
		System.out.println(test.pageTurning(cb.getElement("上一页")));
	}
	
	/**
	 * 用于测试{@link DataTableEvent#getRowElement(int)}方法<br>
	 * 预期：<br>
	 * 
	 */
	@Test 
	public void getRowElementTest() {
		test.getRowElement(1).stream()
			.map(element -> te.getText(element))
			.forEach(System.out :: println);
	}
	//---------------------单元测试区---------------------------
}
