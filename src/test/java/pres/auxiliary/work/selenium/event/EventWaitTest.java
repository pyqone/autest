package pres.auxiliary.work.selenium.event;

import java.io.File;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pres.auxiliary.work.selenium.brower.ChromeBrower;
import pres.auxiliary.work.selenium.brower.ChromeBrower.ChromeOptionType;
import pres.auxiliary.work.selenium.element.old.CommonBy;
import pres.auxiliary.work.selenium.element.old.DataListBy;

/**
 * <p><b>文件名：</b>EventWaitTest.java</p>
 * <p><b>用途：</b>
 * 用于对{@link EventWait}类进行测试
 * </p>
 * <p><b>页面：</b>
 * 运维管理系统，岗前答题库模块
 * </p>
 * <p><b>编码时间：</b>2020年5月24日下午4:37:25</p>
 * <p><b>修改时间：</b>2020年5月24日下午4:37:25</p>
 * @author 
 * @version Ver1.0
 * @since JDK 12
 *
 */
public class EventWaitTest {
	EventWait wait;
	ClickEvent c;
	TextEvent t;
	CommonBy cby;
	ChromeBrower cb = new ChromeBrower(new File("Resource/BrowersDriver/Chrom/78.0394.70/chromedriver.exe"));
	
	@BeforeClass
	public void init() {
		cb.addConfig(ChromeOptionType.CONTRAL_OPEN_BROWER, "127.0.0.1:9222");
		cby = new CommonBy(cb);
		c = new ClickEvent(cb.getDriver());
		t = new TextEvent(cb.getDriver());
		wait = new EventWait(cb.getDriver());
		
	}
	
	@AfterClass
	public void quit() {
		cb.getDriver().quit();
	}
	
	/**
	 * 测试{@link EventWait#disappear(pres.auxiliary.work.selenium.element.old.Element)}方法
	 */
	@Test
	public void disappearTest() {
		//获取编号列
		DataListBy element = new DataListBy(cb);
		element.add("/html/body/div[1]/div/div/section/div/div[2]/div[3]/table//td[contains(@class,'el-table_1_column_1 ')]/div/span");
		
		System.out.println(element.getMinColumnSize());
		System.out.println("开始页：");
		//获取并打印编号列
		for (int i = 1; i <= element.getMinColumnSize(); i++) {
			System.out.println("====================================");
			for (String  name : element.getNames()) {
				System.out.println("编号" + t.getText(element.getElement(name, i)));
			}
			System.out.println("====================================");
		}
		
		//点击搜索按钮，使其弹出等待加载的图标
		c.click(cby.getElement("//button[@class='btn-next']"));
		//等待图标消失
		wait.disappear(cby.getElement("//*[@class='circular']"));
		
		System.out.println("下一页：");
		//重新再获取并打印编号列
		for (int i = 1; i <= element.getMinColumnSize(); i++) {
			System.out.println("====================================");
			for (String  name : element.getNames()) {
				System.out.println("编号" + "=" + t.getText(element.getElement(name, i)));
			}
			System.out.println("====================================");
		}
	}
}
