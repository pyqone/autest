package pres.auxiliary.work.selenium.brower;

import java.io.File;
import java.util.Scanner;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import pres.auxiliary.work.selenium.page.Page;

/**
 * <p><b>文件名：</b>IeBrowerTest.java</p>
 * <p><b>用途：</b>用于对{@link IeBrower}类进行测试</p>
 * <p><b>编码时间：</b>2020年11月8日 下午4:54:29</p>
 * <p><b>修改时间：</b>2020年11月8日 下午4:54:29</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public class IeBrowerTest {
	/**
	 * 指向driver文件
	 */
	private final File driverFile = new File("Resource/BrowersDriver/Ie/IEDriverServer.exe");
	
	/**
	 * 指向浏览器对象
	 */
	IeBrower ib;
	
	/**
	 * 关闭浏览器
	 */
	@AfterClass(alwaysRun = true)
	public void quit() {
		System.out.println(ib.getAllInformation());
		Scanner sc = new Scanner(System.in);
		sc.next();
		ib.closeBrower();
		sc.close();
	}
	
	/**
	 * 测试打开浏览器后加载预设界面
	 */
	@Test
	public void chromeBrowerTest_FilePage() {
		ib = new IeBrower(driverFile, new Page("https://www.baidu.com", "百度"));
		ib.getDriver();
	}
}
