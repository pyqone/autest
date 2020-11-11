package pres.auxiliary.work.selenium.brower;

import java.io.File;

import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;

import pres.auxiliary.work.selenium.page.Page;

/**
 * <p><b>文件名：</b>IeBrower.java</p>
 * <p><b>用途：</b>用于启动IE浏览器，并加载相应的待测页面。</p>
 * <p>启动浏览器需要调用{@link #getDriver()}方法启动浏览器，若在构造方法中定义了{@link Page}类，则
 * 启动浏览器时会自动对页面进行加载，若未定义，则只打开浏览器，如:</p>
 * <p>
 * 若调用方法：<br>
 * IeBrower brower = new {@link #IeBrower(File)}<br>
 * brower.{@link #getDriver()}<br>
 * 后将只全屏打开浏览器，不会加载页面
 * </p>
 * <p>
 * 若调用方法：<br>
 * IeBrower brower = new {@link #IeBrower(File, Page)}<br>
 * 或IeBrower brower = new {@link #IeBrower(File, String, String)}<br>
 * brower.{@link #getDriver()}<br>
 * 后将全屏打开浏览器，并加载相应的页面
 * </p>
 * <p><b>编码时间：</b>2020年11月8日 下午3:52:31</p>
 * <p><b>修改时间：</b>2020年11月8日 下午3:52:31</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public class IeBrower extends AbstractBrower {
	/**
	 * TODO 用于存储对IE的设置，目前未开发在类中设置IE的方法，此处保留
	 */
	private InternetExplorerOptions ieOption = new InternetExplorerOptions();

	/**
	 * 指定驱动文件路径并添加一个待测站点
	 * 
	 * @param driverFile 驱动文件对象
	 * @param page       {@link Page}类对象
	 */
	public IeBrower(File driverFile, Page page) {
		super(driverFile, page);
	}

	/**
	 * 指定驱动文件路径并添加一个待测站点
	 * 
	 * @param driberFile 驱动文件对象
	 * @param url        待测站点
	 * @param pageName   待测站点名称，用于切换页面
	 */
	public IeBrower(File driverFile, String url, String pageName) {
		super(driverFile, url, pageName);
	}

	/**
	 * 指定驱动文件所在路径
	 * 
	 * @param driberFile 驱动文件对象
	 */
	public IeBrower(File driverFile) {
		super(driverFile);
	}
	
	/**
	 * 用于对IE浏览器的个性化设置
	 * @param ieOption IE设置类对象（{@link InternetExplorerOptions}）
	 */
	public void setIeOption(InternetExplorerOptions ieOption) {
		this.ieOption = ieOption;
	}

	@Override
	void openBrower() {
		//构造浏览器
		driver = new InternetExplorerDriver(ieOption);
	}

	@Override
	String getBrowerDriverSetName() {
		return "webdriver.ie.driver";
	}
}
