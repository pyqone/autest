package pres.auxiliary.work.selenium.brower;

import java.io.File;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import pres.auxiliary.work.selenium.page.Page;

/**
 * <p><b>文件名：</b>FirefoxBrower.java</p>
 * <p><b>用途：</b>用于启动火狐浏览器，并加载相应的待测页面</p>
 * <p>启动浏览器需要调用{@link #getDriver()}方法启动浏览器，若在构造方法中定义了{@link Page}类，则
 * 启动浏览器时会自动对页面进行加载，若未定义，则只打开浏览器，如:</p>
 * <p>
 * 若调用方法：
 * <pre><code>
 * FirefoxBrower brower = new {@link #FirefoxBrower(File)}
 * brower.{@link #getDriver()}
 * </code></pre>
 * 后将只全屏打开浏览器，不会加载页面；若调用方法：
 * <pre><code>
 * FirefoxBrower brower = new {@link #FirefoxBrower(File, Page)}
 * //或FirefoxBrower brower = new {@link #FirefoxBrower(File, String, String)}
 * brower.{@link #getDriver()}
 * </code></pre>
 * 
 * 后将全屏打开浏览器，并加载相应的页面
 * </p>
 * <p><b>编码时间：</b>2020年12月13日 下午4:33:25</p>
 * <p><b>修改时间：</b>2020年12月13日 下午4:33:25</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
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
	 * @param driverFile 驱动文件对象
	 * @param url        待测站点
	 * @param pageName   待测站点名称，用于切换页面
	 */
	public FirefoxBrower(File driverFile, String url, String pageName) {
		super(driverFile, url, pageName);
	}

	/**
	 * 指定驱动文件所在路径
	 * 
	 * @param driverFile 驱动文件对象
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
