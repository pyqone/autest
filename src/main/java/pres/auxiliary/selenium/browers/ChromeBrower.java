package pres.auxiliary.selenium.browers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * <p><b>文件名：</b>ChromeBrower.java</p>
 * <p><b>用途：</b>
 * 该类用于启动谷歌浏览器，并通过getDriver()方法获取当前浏览器的WebDriver对象。调用本类时可通过
 * set方法设置chromedriver的所在路径、待测试的站点，也可通过构造方法进行设置，亦可通过在
 * getDriver()方法直接设置参数以达到启动浏览器的目的。
 * </p>
 * <p><b>修改时间：</b>2019年6月5日下午4:24:23</p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since Selenium 2.53.0
 * @since JDK 1.7
 */
public class ChromeBrower extends AbstractBrower {
	/**
	 * 定义ChromeOptions类，当需要控制已打开的浏览器时，则定义该类
	 * */
	ChromeOptions cp = null;
	
	/**
	 * 双参构造，用于设置谷歌浏览器启动驱动路径及待测试的站点，并创建对象
	 * 
	 * @param driverPath
	 *            谷歌浏览器启动驱动路径
	 * @param url
	 *            待测试的站点
	 */
	public ChromeBrower(String driverPath, String url) {
		setBrowserRunPath(driverPath);
		setURL(url);
	}
	
	/**
	 * 该构造用于使用已打开的浏览器，对浏览器进行操作
	 * @param port 端口号
	 */
	public ChromeBrower(String driverPath, int port) {
		setBrowserRunPath(driverPath);
		//构造ChromeOptions类，设置传入的浏览器打开的端口号
		cp = new ChromeOptions();
		cp.setExperimentalOption("debuggerAddress", "127.0.0.1:" + port);
	}

	@Override
	public String getBrowerName() {
		return ((ChromeDriver) driver).getCapabilities().getBrowserName();
	}

	@Override
	public String getBrowerVersion() {
		return ((ChromeDriver) driver).getCapabilities().getVersion();
	}

	@Override
	public String getSystemInformation() {
		String s = ((ChromeDriver) driver).getCapabilities().getPlatform().name();
		s += ((ChromeDriver) driver).getCapabilities().getPlatform().getMajorVersion();

		return s;
	}
	
	@Override
	public void close() {
		//若是控制已打开的浏览器时，其quit()方法无法关闭浏览器，故需要用close()方法来关闭
		if (cp == null) {
			driver.quit();
			driver = null;
		} else {
			driver.close();
			driver = null;
		}
	}
	
	/**
	 * 该方法用于设置需要控制的已打开的浏览器端口<br>
	 * <b>注意，为了安全，调用该方法时，会将传入的url（待测站点）值清空</b>
	 * @param port 端口
	 */
	public void setPort(int port) {
		//构造ChromeOptions类，设置传入的浏览器打开的端口号
		cp = new ChromeOptions();
		cp.setExperimentalOption("debuggerAddress", "127.0.0.1:" + port);
		//将WebDriver对象置空，以便于重新制定WebDriver对象
		driver = null;
		//将待测站点的信息也置空
		setURL("");
	}
	
	/**
	 * 该方法用于解除对已打开的浏览器的控制，调用该方法后重新获取的WebDriver对象将重新打开浏览器
	 */
	public void removeControl() {
		cp = null;
		driver = null;
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
		if ("".equals(getURL()) && cp == null) {
			return null;
		}

		// 判断是否存在谷歌浏览器启动驱动的安装路径，若存在，则添加谷歌浏览器启动驱动启动路径，若不存在，则抛出ChromedriverNotFoundException异常
		if (!"".equals(getBrowserRunPath())) {
			System.setProperty("webdriver.chrome.driver", getBrowserRunPath());
		} else {
			throw new ChromedriverNotFoundException("chromedriver文件没有找到");
		}
		
		//判断当前是否通过已打开的浏览器来进行测试
		if (cp != null) {
			//通过已打开的浏览器参数来创建WebDiver对象
			driver = new ChromeDriver(cp);
			//判断当前是否有传入url，若url为空，则不改变页面的url，若url不为空，则进行更改
			if (!"".equals(getURL())) {
				//获取并判断浏览器当前站点是否与传入的站点一致，若不一致，则改变url
				if (!getURL().equalsIgnoreCase(driver.getCurrentUrl())) {
					driver.get(getURL());
				} 
			}
			return driver;
		} else {
			// 创造WebDriver对象
			driver = new ChromeDriver();
			// 调用OprateBrower()方法并返回其操作后的WebDriver对象
			return oprateBrower(driver);
		}
	}
}
