package pres.auxiliary.work.selenium.browers;

import java.io.File;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.alibaba.fastjson.JSONObject;

import pres.auxiliary.selenium.browers.PageNotFoundException;

/**
 * 该类定义启动浏览器时必要的方法以及打开浏览器，创建WebDriver对象的操作，由新添加的各个浏览器子类可进行继承
 * 
 * @author 彭宇琦
 * @version Ver1.1
 * 
 */
public abstract class AbstractBrower {
	/**
	 * 用于接收用户需要进入的网站
	 */
	String url = "";
	/**
	 * 用于接收浏览器启动所需的文件路径
	 */
	File driverFile;
	/**
	 * 存储页面加载等待时间
	 */
	int loadTime = 120;
	/**
	 * 用于存储目标站点的title
	 */
	String assertTitle = "";
	/**
	 * 用于存储页面自动刷新的次数，默认为0次
	 */
	int rafreshCount = 0;
	/**
	 * 用于存储获取到的浏览器对象
	 */
	WebDriver driver = null;
	/**
	 * 存储浏览器的设置内容，key表示浏览器设置的属性；value表示设置的内容
	 */
	HashMap<String, BrowerSetField> browerSetMap = new HashMap<String, BrowerSetField>();

	/**
	 * 构造浏览器对象，并指定驱动文件及待测站点
	 * 
	 * @param driberFile 驱动文件对象
	 * @param url        待测站点
	 */
	public AbstractBrower(File driberFile, String url) {
		this.driverFile = driberFile;
		this.url = url;
		
		//TODO 初始化浏览器对应的基本信息，并在其他的方法中添加
	}

	/**
	 * 该方法用于返回当前站点的url，若当前未调用{@link #getDriver()}方法或浏览器启动失败时返回当前设置的待测站点
	 * 
	 * @return 返回当前站点的url或设置的待测站点
	 */
	public String getUrl() {
		if (driver == null) {
			return url.toString();
		} else {
			return driver.getCurrentUrl();
		}
	}

	/**
	 * 该方法用于打开新的站点，当待测站点与原站点不同时，则根据传入的openNewLabel参数，则以以下方式进入站点：
	 * <ul>
	 * <li>openNewLabel为true，则重新打开一个标签页，在新的标签页中打开站点</li>
	 * <li>openNewLabel为false，则在当前的标签页中打开站点</li>
	 * </ul>
	 * 注意：若未生成WebDriver对象时（即未调用{@link #getDriver()}方法时），该方法仅更改待测站点。执行该方法后，其
	 * WebDriver对象将切换至新的标签页上
	 * 
	 * @param url          站点域名
	 * @param openNewLabel 是否打开新的标签页
	 */
	public void openUrl(String url, boolean openNewLabel) {
		// 若未生成WebDriver对象，则只做设置
		if (driver == null) {
			this.url = url;
			return;
		}

		if (openNewLabel) {
			openNewLabelPage(url);
		} else {
			overridePage(url);
		}
	}

	/**
	 * 该方法用于设置目标站点的title，以便进行链接是否正确的判断
	 * 
	 * @param pageTitle 设置的目标站点title
	 */
	public void setAssertTitle(String assertTitle) {
		this.assertTitle = assertTitle;
	}

	/**
	 * 该方法用于设置页面等待时间，单位为秒
	 * 
	 * @param LoadTime 传入的时间
	 */
	public void setLoadTime(int loadTime) {
		this.loadTime = loadTime;
	}

	/**
	 * 该方法用于设置页面的自动刷新次数，当设置该值后则开启自动刷新
	 * 
	 * @param rafreshCount 自动刷新的次数
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
//	public abstract String getBrowerName();

	/**
	 * 获取浏览器版本
	 * 
	 * @return 浏览器版本
	 */
//	public abstract String getBrowerVersion();

	/**
	 * 获取操作系统信息
	 * 
	 * @return 操作系统信息
	 */
//	public abstract String getSystemInformation();
	
	public HashMap<String, String> getAllInformation() {
		return 
	}

	/**
	 * 用于返回浏览器的各项信息
	 * @param key
	 * @return
	 */
	public abstract String getInformation(String key);

	/**
	 * 用于重启浏览器，若未生成WebDriver对象时（即未调用{@link #getDriver()}方法时），则方法调用无效
	 */
	public void initialization() {
		if (driver != null) {
			driver.quit();
			driver = null;
			getDriver();
		}
	}

	/**
	 * 用于刷新当前页面，若未生成WebDriver对象时（即未调用{@link #getDriver()}方法时），则方法调用无效
	 */
	public void refreshPage() {
		if (driver != null) {
			driver.navigate().refresh();
		}
	}

	/**
	 * 用于关闭当前标签，若只存在一个标签时，则关闭浏览器，以保证进程能被完全浏览器驱动能被释放
	 */
	public void closeLabel() {
		if (driver.getWindowHandles().size() == 1) {
			driver.quit();
		} else {
			driver.close();
		}
	}

	/**
	 * 用于关闭浏览器
	 */
	public void closeBrower() {
		driver.quit();
	}

	/**
	 * 该方法用于对浏览器的一系列操作，包括全屏浏览器，跳转相应的站点，等待页面加载以及自动刷新，
	 * 使用该方法时会抛出两个运行时异常，分别为IncorrectPageTitleException和PageNotFoundException
	 * 
	 * @param driver 通过start()方法创建的WebDriver对象
	 * @return 返回对浏览器进行操作后的得到的WebDriver对象
	 * @throws IncorrectPageTitleException 页面title与设置的title不一致时抛出的异常
	 * @throws PageNotFoundException       页面加载失败时抛出的异常
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
			if (!assertTitle.toString().equals("") && !driver.getTitle().equals(assertTitle.toString())) {
				continue;
			}

			// 若页面能正常加载，置空pageTitle并返回WebDriver对象
			assertTitle = "";
			return driver;

			// 判断的条件刷新次数rafresh为0时则结束循环
		} while (rafresh != 0);

		// 若循环结束，则说明页面无法正常加载，则置空pageTitle，抛出PageNotFountException
		assertTitle = "";
		throw new IncorrectPageTitleException("页面载入出错");
	}

	/**
	 * 用于新增一个标签页，并打开指定站点
	 * 
	 * @param url 指定的站点
	 */
	void openNewLabelPage(String url) {
		// 获取当前所有的handle
		Set<String> handleSet = driver.getWindowHandles();
		// 编写js脚本，执行js，以开启一个新的标签页
		String js = "window.open(\"" + url + "\");";
		((JavascriptExecutor) driver).executeScript(js);
		// 移除原有的windows的Handle，保留新打开的windows的Handle
		String newHandle = "";
		for (String handle : driver.getWindowHandles()) {
			if (!handleSet.contains(handle)) {
				newHandle = handle;
				break;
			}
		}
		// 切换WebDriver
		driver.switchTo().window(newHandle);
	}

	/**
	 * 用于在原标签页中打开站点
	 * 
	 * @param url 指定的站点
	 */
	void overridePage(String url) {
		driver.get(url);
	}
	
	/**
	 * <p><b>文件名：</b>AbstractBrower.java</p>
	 * <p><b>用途：</b>用于对浏览器特殊设置的字段说明</p>
	 * <p><b>编码时间：</b>2020年4月7日 下午9:38:46</p>
	 * <p><b>修改时间：</b>2020年4月7日 下午9:38:46</p>
	 * @author 彭宇琦
	 * @version Ver1.0
	 * @since JDK 12
	 */
	class BrowerSetField {
		/**
		 * 存储key的名称
		 */
		public String keyName;
		/**
		 * 存储key值
		 */
		public String key;
		/**
		 * 存储相应的设置值
		 */
		public String value;
		/**
		 * 初始化字段值
		 * @param keyName key的名称
		 * @param key key值
		 * @param value 存储相应的设置值
		 */
		public BrowerSetField(String keyName, String key, String value) {
			this.keyName = keyName;
			this.key = key;
			this.value = value;
		}
	}
}
