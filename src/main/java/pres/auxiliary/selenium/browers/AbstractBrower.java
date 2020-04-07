package pres.auxiliary.selenium.browers;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;

/**
 * <p><b>文件名：</b>AbstractBrower.java</p>
 * <p><b>用途：</b>该类定义启动浏览器时必要的方法以及打开浏览器，创建WebDriver对象的操作，由新添加的各个浏览器子类可进行继承</p>
 * <p><b>编码时间：</b>2020年4月6日 下午3:01:06</p>
 * <p><b>修改时间：</b>2020年4月6日 下午3:01:06</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 * @since selenium 3.0.0
 */
public abstract class AbstractBrower {
	/**
	 * 用于接收用户需要进入的网站
	 */
	private String url = "";
	/**
	 * 用于接收浏览器启动所需的文件路径
	 */
	private String browserRunPath = "";
	/**
	 * 存储页面加载等待时间
	 */
	private int loadTime = 120;
	/**
	 * 用于存储目标站点的title
	 */
	private StringBuilder pageTitle = new StringBuilder("");
	/**
	 * 用于存储页面自动刷新的次数，默认为0次
	 */
	private int rafreshCount = 0;
	/**
	 * 用于存储获取到的浏览器对象
	 */
	WebDriver driver = null;

	/**
	 * 该方法用于返回待链接的站点
	 * 
	 * @return 返回待链接的站点
	 */
	public String getURL() {
		return url.toString();
	}

	/**
	 * 该方法用于设置待链接的站点
	 * 
	 * @param uRL
	 *            待链接的站点
	 */
	public void setURL(String uRL) {
		//判断待测站点是否有发生改变，若未发生改变，则不重新设置
		if (!url.equalsIgnoreCase(uRL)) {
			// 由于改变了浏览器的条件，故初始化driver变量，以便于重新打开浏览器
			driver = null;
			url = uRL;
		}
	}

	/**
	 * 用于返回浏览器的启动路径。对于火狐则为浏览器可执行文件所在路径，对于谷歌浏览器即浏览器驱动所在路径。
	 * 
	 * @return 浏览器的启动路径
	 */
	public String getBrowserRunPath() {
		return browserRunPath;
	}

	/**
	 * 用于设置浏览器的启动路径。对于火狐则为浏览器可执行文件所在路径，对于谷歌浏览器即浏览器驱动所在路径。
	 * 
	 * @param startBrowserPath
	 *            浏览器的启动路径
	 */
	public void setBrowserRunPath(String browserRunPath) {
		//判断待测站点是否有发生改变，若未发生改变，则不重新设置
		if (!this.browserRunPath.equalsIgnoreCase(browserRunPath)) {
			// 由于改变了浏览器的条件，故初始化driver变量，以便于重新打开浏览器
			driver = null;
			this.browserRunPath = browserRunPath;
		}
		
	}

	/**
	 * 该方法用于返回设置的目标站点的title
	 * 
	 * @return 返回设置的目标站点的title
	 */
	public String getPageTitle() {
		return pageTitle.toString();
	}

	/**
	 * 该方法用于设置目标站点的title，以便进行链接是否正确的判断
	 * 
	 * @param pageTitle
	 *            设置的目标站点title
	 */
	public void setPageTitle(String pageTitle) {
		this.pageTitle.delete(0, this.pageTitle.length());
		this.pageTitle.append(pageTitle);
	}

	/**
	 * 该方法用于返回设置的页面等待时间，默认时间为120秒
	 * 
	 * @return 返回页面等待时间
	 */
	public int getLoadTime() {
		return loadTime;
	}

	/**
	 * 该方法用于设置页面等待时间，整形，单位为秒
	 * 
	 * @param LoadTime
	 *            传入的时间
	 */
	public void setLoadTime(int loadTime) {
		this.loadTime = loadTime;
	}

	/**
	 * 该方法用于返回自动刷新的次数，默认为0次
	 * 
	 * @return 返回自动刷新的次数
	 */
	public int getRafreshCount() {
		return rafreshCount;
	}

	/**
	 * 该方法用于设置页面的自动刷新次数，当设置该值后则开启自动刷新
	 * 
	 * @param rafreshCount
	 *            自动刷新的次数
	 */
	public void setRafreshCount(int rafreshCount) {
		this.rafreshCount = rafreshCount;
	}

	/**
	 * 该方法用于启动浏览器，并返回WebDriver对象
	 * 
	 * @return 指向浏览器的WebDriver对象
	 */
	public abstract WebDriver getDriver();

	/**
	 * 获取浏览器的名称
	 * 
	 * @return 浏览器名称
	 */
	public abstract String getBrowerName();

	/**
	 * 获取浏览器版本
	 * 
	 * @return 浏览器版本
	 */
	public abstract String getBrowerVersion();

	/**
	 * 获取操作系统信息
	 * 
	 * @return 操作系统信息
	 */
	public abstract String getSystemInformation();

	/**
	 * 用于初始化浏览器
	 */
	public WebDriver initialization() {
		driver.close();
		driver = null;
		return getDriver();
	}

	/**
	 * 用于关闭浏览器
	 */
	public void close() {
		driver.quit();
		driver = null;
	}

	/**
	 * 该方法用于对浏览器的一系列操作，包括全屏浏览器，跳转相应的站点，等待页面加载以及自动刷新，
	 * 使用该方法时会抛出两个运行时异常，分别为IncorrectPageTitleException和PageNotFoundException
	 * 
	 * @param driver
	 *            通过start()方法创建的WebDriver对象
	 * @return 返回对浏览器进行操作后的得到的WebDriver对象
	 * @throws IncorrectPageTitleException
	 *             页面title与设置的title不一致时抛出的异常
	 * @throws PageNotFoundException
	 *             页面加载失败时抛出的异常
	 */
	protected WebDriver oprateBrower(WebDriver driver) {
		// 将设置的自动刷新次数存储在临时变量中，并加上1
		// 加1的目的是自动刷新判断是用do...while循环实现，若不事先加上1在会导致循环少1次
		int rafresh = rafreshCount + 1;

		String errorTitle = "页面载入出错";
		
		// 进入站点
		driver.get(url);
		// 全屏浏览器
		driver.manage().window().maximize();

		// 循环执行页面加载判断，判断其是否加载出目标站点，若加载出来页面，则结束循环，若用户设置的自动刷新次数循环完还没加载出页面，则
		// 抛出PageNotFoundException异常。页面加载判断必须读取一次，所以使用do...while循环
		do {
			// 读取并设置浏览器等待时间
			driver.manage().timeouts().pageLoadTimeout(loadTime, TimeUnit.SECONDS);

			// 判断进入站点后页面的title是否为“页面载入出错”，若为该title，则刷新页面，重新加载
			// 注意，在火狐浏览器中凡是加载不出的页面，其页面title都是“页面载入出错”，所以该方法只适用于火狐浏览器
			if (errorTitle.equals(driver.getTitle())) {
				// 将刷新次数减1
				rafresh--;
				// 刷新网页
				driver.navigate().refresh();
				// 不执行后续代码，继续循环
				continue;
			}

			// 若页面加载成功，则判断加载的页面是否与用户设置的一致，若用户未设置目标站点的title，则跳过判断
			// 判断pageTitle中存储的信息是否为空，若不为空则再判断页面的title是否与设置的title一致，若不一致，则抛出IncorrectPageTileException
			if (!pageTitle.toString().equals("") && !driver.getTitle().equals(pageTitle.toString())) {
				continue;
			}

			// 若页面能正常加载，置空pageTitle并返回WebDriver对象
			pageTitle.delete(0, pageTitle.length());
			return driver;

		// 判断的条件刷新次数rafresh为0时则结束循环
		} while (rafresh != 0);

		// 若循环结束，则说明页面无法正常加载，则置空pageTitle，抛出PageNotFountException
		pageTitle.delete(0, pageTitle.length());
		throw new IncorrectPageTitleException("页面载入出错");
	}
}
