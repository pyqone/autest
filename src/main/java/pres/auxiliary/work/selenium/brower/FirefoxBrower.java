package pres.auxiliary.work.selenium.brower;

import java.io.File;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class FirefoxBrower extends AbstractBrower {
	/**
	 * 用于存储与火狐浏览器相关的配置
	 */
	FirefoxOptions firefoxOption = new FirefoxOptions();

	/**
	 * 指定驱动文件路径并添加一个待测站点
	 * 
	 * @param driverFile 驱动文件对象
	 * @param page       {@link Page}类对象
	 */
	public FirefoxBrower(File driverFile, Page page) {
		super(driverFile, page);
	}

	/**
	 * 指定驱动文件路径并添加一个待测站点
	 * 
	 * @param driberFile 驱动文件对象
	 * @param url        待测站点
	 * @param pageName   待测站点名称，用于切换页面
	 */
	public FirefoxBrower(File driverFile, String url, String pageName) {
		super(driverFile, url, pageName);
	}

	/**
	 * 指定驱动文件所在路径
	 * 
	 * @param driberFile 驱动文件对象
	 */
	public FirefoxBrower(File driverFile) {
		super(driverFile);
	}
	
	/**
	 * 用于设置火狐浏览器的启动路径，在启动浏览器时，将打开该路径下的浏览器。
	 * 通过该方法可用于打开为谷歌浏览器内核的浏览器
	 * </p>
	 * <p>
	 * 	<b>注意：</b>若浏览器启动后再设置浏览器启动路径时，会重新打开一个新的浏览器，且
	 * 	{@link WebDriver}对象将重新构造，指向新打开的浏览器
	 * </p>
	 * @param binaryPath
	 */
	public void setBinary(File binaryPath) {
		firefoxOption.setBinary(binaryPath.getAbsolutePath());
		driver = null;
	}

	@Override
	void openBrower() {
		driver = new FirefoxDriver(firefoxOption);
	}

	@Override
	String getBrowerDriverSetName() {
		return "webdriver.gecko.driver";
	}

}
