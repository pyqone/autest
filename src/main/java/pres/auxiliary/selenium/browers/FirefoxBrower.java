package pres.auxiliary.selenium.browers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * 该类用于启动火狐浏览器，并通过getDriver()方法获取当前浏览器的WebDriver对象。调用本类时可通过
 * set方法设置火狐浏览器的启动路径、待测试的站点和配置文件的位置，也可通过构造方法进行设置，亦可通过在
 * getDriver()方法直接设置参数以达到启动浏览器的目的。
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since Selenium 2.53.0
 * @since JDK 1.7
 */
public class FirefoxBrower extends AbstractBrower {
	// 存储配制文件名称
	private String settingName = "";

	/**
	 * 单参构造，用于设置待测试的站点，并创建对象
	 * 
	 * @param url
	 *            待测试的站点
	 */
	public FirefoxBrower(String url) {
		setURL(url);
	}

	/**
	 * 双参构造，用于设置火狐浏览器安装路径及待测试的站点，并创建对象
	 * 
	 * @param firefoxPath
	 *            火狐浏览器的安装路径
	 * @param url
	 *            待测试的站点
	 */
	public FirefoxBrower(String firefoxPath, String url) {
		setBrowserRunPath(firefoxPath);
		setURL(url);
	}

	/**
	 * 叁参构造，用于设置火狐浏览器安装路径及待测试的站点，并创建对象
	 * 
	 * @param firefoxPath
	 *            火狐浏览器的安装路径
	 * @param settingName
	 *            配置文件名称
	 * @param uRL
	 *            待测试的站点
	 */
	public FirefoxBrower(String firefoxPath, String settingName, String url) {
		setBrowserRunPath(firefoxPath);
		setSettingName(settingName);
		setURL(url);
	}

	/**
	 * 该方法用于返回火狐浏览器配置文件的文件名
	 * 
	 * @return 返回火狐浏览器配置文件的文件名
	 */
	public String getSettingName() {
		return settingName;
	}

	/**
	 * 该方法用于设置火狐浏览器配置文件的文件名
	 * 
	 * @param settingName
	 *            配置文件的文件名
	 */
	public void setSettingName(String settingName) {
		//若配置文件名未改变，则不重新赋值
		if ( !this.settingName.equalsIgnoreCase(settingName) ) {
			// 由于改变了浏览器的条件，故初始化driver变量，以便于重新打开浏览器
			driver = null;
			this.settingName = settingName;
		}
	}

	@Override
	public String getBrowerName() {
		return ((FirefoxDriver) driver).getCapabilities().getBrowserName();
	}

	@Override
	public String getBrowerVersion() {
		return ((FirefoxDriver) driver).getCapabilities().getVersion();
	}

	@Override
	public String getSystemInformation() {
		String s = ((FirefoxDriver) driver).getCapabilities().getPlatform().name();
		s += ((FirefoxDriver) driver).getCapabilities().getPlatform().getMajorVersion();
		
		return s;
	}

	/**
	 * 该方法通过类中的属性打开浏览器，跳转相应的站点，并创建WebDriver对象。使用前需要通过类中的set()方法为属性进行赋值。
	 * 在运行过程中可能会抛出两个运行时异常，分别为IncorrectPageTitleException和PageNotFoundException <br/>
	 * <b><i>注意，若未传入目标站点或者目标站点为空，则返回null</i></b>
	 * 
	 * @return 返回浏览器的WebDriver对象
	 * @throws IncorrectPageTitleException
	 *             页面title与设置的title不一致时抛出的异常
	 * @throws PageNotFoundException
	 *             页面加载失败时抛出的异常
	 */
	@Override
	public WebDriver getDriver() {
		// 判断类中的driver是否为null，不为null，则直接返回
		if (driver != null) {
			return driver;
		}

		// 判断传入的站点信息是否为空，若为空，则返回null
		if ("".equals(getURL())) {
			return null;
		}

		// 判断是否存在火狐的安装路径，若存在，则添加火狐启动路径
		if (!"".equals(getBrowserRunPath())) {
			System.setProperty("webdriver.firefox.bin", getBrowserRunPath());
		}

		// 判断火狐的配置文件名是否为空，若不为空，则通过配置文件创建webdriver对象，若为空，则不使用配置文件创建对象
		if (!"".equals(settingName)) {
			// 通过配置文件，创建webdriver对象，并启动浏览器
			//driver = new FirefoxDriver(new ProfilesIni().getProfile(settingName.toString()));
		} else {
			// 通过默认方式创建webdriver对象，并启动浏览器
			// 在创建对象时若火狐的路径不正确，则会抛出WebDriverException异常，捕捉该异常后再抛出FirefoxPathNotFountException的异常
			try {
				driver = new FirefoxDriver();
			} catch (WebDriverException e) {
				throw new FirefoxPathNotFoundException("指定的火狐浏览器安装路径不正确");
			}
		}

		// 调用OprateBrower()方法并返回其操作后的WebDriver对象
		return oprateBrower(driver);
	}
}
