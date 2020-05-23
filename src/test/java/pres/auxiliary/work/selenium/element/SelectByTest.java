package pres.auxiliary.work.selenium.element;

import java.io.File;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pres.auxiliary.work.selenium.brower.ChromeBrower;
import pres.auxiliary.work.selenium.brower.ChromeBrower.ChromeOptionType;
import pres.auxiliary.work.selenium.event.ClickEvent;

/**
 * <p><b>文件名：</b>SelectByText.java</p>
 * <p><b>用途：</b>
 * 对{@link SelectBy}类进行测试
 * </p>
 * <p><b>页面：</b>
 * 对标准型下拉选项测试页面为jira提BUG弹窗，对非标准型下拉为运营系统测试环境消息推送管理页面
 * </p>
 * <p><b>编码时间：</b>2020年5月23日下午4:29:10</p>
 * <p><b>修改时间：</b>2020年5月23日下午4:29:10</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public class SelectByTest {
	ChromeBrower cb = new ChromeBrower(new File("Resource/BrowersDriver/Chrom/78.0394.70/chromedriver.exe"));
	SelectBy s;
	CommonBy cby;
	ClickEvent ce;
	
	@BeforeClass(alwaysRun = true)
	public void init() {
		cb.addConfig(ChromeOptionType.CONTRAL_OPEN_BROWER, "127.0.0.1:9222");
		
		s = new SelectBy(cb, false);
		cby = new CommonBy(cb);
		ce = new ClickEvent(cb.getDriver());
	}
	
	@AfterClass(alwaysRun = true)
	public void quit() {
		cb.getDriver().quit();
	}
	
	/**
	 * 用于测试选择非标准型下拉选项
	 * @throws InterruptedException 
	 */
	@Test(groups = "list")
	public void changeElement_List() throws InterruptedException {
		Element e = cby.getElement("/html/body/div[1]/div/div/section/div/div[1]/div[4]/div[1]/input");
		ce.click(e);
		s.add("/html/body/div/div/div[1]/ul/li/span");
		
		//按照下标进行选择(第三个选项：待审核)
		ce.click(s.getElement(3));
		Thread.sleep(2000);
		ce.click(e);
		//按照文本进行选择
		ce.click(s.getElement("审核通过"));
	}
	
	/**
	 * 测试标准型拉下选项的选择
	 * @throws InterruptedException 
	 */
	@Test(groups = "commom")
	public void addTest_Common() throws InterruptedException {
		s.add("//label[text()='严重等级']/../select");
		
		//按照下标进行选择(第三个选项：轻微)
		ce.click(s.getElement(3));
		Thread.sleep(2000);
		//按照文本进行选择
		ce.click(s.getElement("致命"));
		
		s.add("//label[text()='缺陷来源']/../select");
		//按照下标进行选择(第三个选项：与需求不一致)
		ce.click(s.getElement(3));
	}
}
