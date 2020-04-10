package pres.auxiliary.work.selenium.browers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 该类定义启动浏览器时必要的方法以及打开浏览器，创建WebDriver对象的操作，由新添加的各个浏览器子类可进行继承
 * 
 * @author 彭宇琦
 * @version Ver1.1
 * 
 */
public abstract class AbstractBrower {
	/**
	 * 用于接收浏览器启动所需的文件路径
	 */
	File driverFile;
	/**
	 * 用于存储获取到的浏览器对象
	 */
	WebDriver driver = null;
	/**
	 * 存储浏览器的设置内容，key表示浏览器设置的属性；value表示设置的内容
	 */
	HashMap<String, String> browerSetMap = new HashMap<String, String>();
	/**
	 * 存储打开的页面
	 */
	ArrayList<Page> pageList = new ArrayList<>();

	/**
	 * 用于存储WebDriver当前指向的页面信息
	 */
	Page nowPage;

	/**
	 * 用于存储浏览器启动时的信息
	 */
	JSONObject informationJson = new JSONObject();

	/**
	 * 指定驱动文件所在路径
	 * 
	 * @param driberFile 驱动文件对象
	 */
	public AbstractBrower(File driverFile) {
		this.driverFile = driverFile;

		// 存储页面信息
		informationJson.put("驱动所在路径", driverFile.getAbsolutePath());
	}

	/**
	 * 指定驱动文件路径并添加一个待测站点
	 * 
	 * @param driberFile 驱动文件对象
	 * @param url        待测站点
	 * @param pageName   待测站点名称，用于切换页面
	 */
	public AbstractBrower(File driverFile, String url, String pageName) {
		this(driverFile);
		nowPage = new Page(url, pageName);
	}

	/**
	 * 指定驱动文件路径并添加一个待测站点
	 * 
	 * @param driverFile 驱动文件对象
	 * @param page       {@link Page}类对象
	 */
	public AbstractBrower(File driverFile, Page page) {
		this(driverFile);
		nowPage = page;
	}

	/**
	 * 该方法用于打开新的站点，通过该方法将生成一个Page对象，存在类中，并根据传入的openNewLabel参数，则以以下方式进入站点：
	 * <ul>
	 * <li>openNewLabel为true，则重新打开一个标签页，在新的标签页中打开站点</li>
	 * <li>openNewLabel为false，则在当前的标签页中打开站点</li>
	 * </ul>
	 * 注意：
	 * <ol>
	 * <li>若未生成WebDriver对象时（即未调用{@link #getDriver()}方法时），则抛出异常</li>
	 * <li>若生成WebDriver对象但未设置默认页面时（即调用了{@link #AbstractBrower(File)}单参构造来构造对象），
	 * 则在调用该方法时，无论openNewLabel传入是何值，均不影响在第一个标签中打开页面</li>
	 * <li>执行该方法后，其WebDriver对象将切换至新的标签页上</li>
	 * </ol>
	 * 
	 * @param url          站点域名
	 * @param pageName     页面名称
	 * @param openNewLabel 是否在新标签中打开页面
	 * @throws IncorrectPageException 当浏览器未打开或者页面加载错误时抛出的异常
	 */
	public void openUrl(String url, String pageName, boolean openNewLabel) {
		// 若未生成WebDriver对象，则不做任何
		if (driver == null) {
			throw new IncorrectPageException("未打开浏览器");
		}

		// 将传入的参数简单封装成Page类，传入重载的方法中
		openUrl(new Page(url, pageName), openNewLabel);
	}

	/**
	 * 该方法用于打开新的站点，根据传入的openNewLabel参数，则以以下方式进入站点：
	 * <ul>
	 * <li>openNewLabel为true，则重新打开一个标签页，在新的标签页中打开站点</li>
	 * <li>openNewLabel为false，则在当前的标签页中打开站点</li>
	 * </ul>
	 * 注意：
	 * <ol>
	 * <li>若未生成WebDriver对象时（即未调用{@link #getDriver()}方法时），则抛出异常</li>
	 * <li>若生成WebDriver对象但未设置默认页面时（即调用了{@link #AbstractBrower(File)}单参构造来构造对象），
	 * 则在调用该方法时，无论openNewLabel传入是何值，均不影响在第一个标签中打开页面</li>
	 * <li>执行该方法后，其WebDriver对象将切换至新的标签页上</li>
	 * </ol>
	 * 
	 * @param newPage      Page类对象
	 * @param openNewLabel 是否在新标签中打开页面
	 * @throws IncorrectPageException 当浏览器未打开或者页面加载错误时抛出的异常
	 */
	public void openUrl(Page newPage, boolean openNewLabel) {
		// 若未生成WebDriver对象，则抛出IncorrectPageException异常
		if (driver == null) {
			throw new IncorrectPageException("未打开浏览器");
		}

		// 若pageList无元素，则表明此为第一次打开浏览器，则无论openNewLabel是何值，均按照同一规则打开页面
		// 若pageList有元素，则根据openNewLabel参数来决定如何打开新的页面
		if (pageList.size() == 0) {
			// 存储页面对象
			pageList.add(newPage);
			// 将当前页面对象指向newPage
			nowPage = newPage;
			// 在当前标签上打开页面
			overridePage();
		} else {
			if (openNewLabel) {
				// 存储页面对象
				pageList.add(newPage);
				// 将当前页面对象指向newPage
				nowPage = newPage;
				// 在标签页上打开页面
				openNewLabelPage();
			} else {
				// 先在pageList移除nowPage
				pageList.set(pageList.indexOf(nowPage), newPage);
				// 将nowPage指向newPage
				nowPage = newPage;
				// 在当前标签上打开页面
				overridePage();
			}
		}

		// 遍历所有标签页，存储标签页信息
		JSONArray labelInformation = new JSONArray();
		pageList.forEach(page -> {
			labelInformation.add(page.getPageInformation());
		});
		// 存储标签页信息
		informationJson.put("标签信息", labelInformation);

		// 切换至相应的窗口
		driver.switchTo().window(nowPage.getHandle());
	}

	/**
	 * 该方法用于启动浏览器，并返回WebDriver对象
	 * 
	 * @return 指向浏览器的WebDriver对象
	 */
	public WebDriver getDriver() {
		// 若driver对象未生成，则进行开启浏览器的操作
		if (driver == null) {
			// 打开浏览器
			openBrower();

			// 若存在需要打开的页面，则打开第一个页面
			if (nowPage != null) {
				openUrl(nowPage, false);
			}
		}

		return driver;
	}

	/**
	 * 用于打开浏览器
	 * 
	 * @return {@link WebDriver}类对象
	 */
	abstract void openBrower();

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

	public JSONObject getAllInformation() {
		return informationJson;
	}

	/**
	 * 用于根据名称返回对浏览器的各项信息，若信息不存在，则返回空
	 * 
	 * @param key 需要搜索的信息名称
	 * @return 名称对应的浏览器信息
	 */
	public String getInformation(String key) {
		if (informationJson.containsKey(key)) {
			return informationJson.getString(key);
		} else {
			return "";
		}
	}

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
	 * 该方法用于对浏览器的一系列操作，包括全屏浏览器，跳转相应的站点，等待页面加载以及页面错误重载，
	 * 使用该方法时会抛出两个运行时异常，分别为IncorrectPageTitleException和PageNotFoundException
	 * 
	 * @param driver WebDriver对象
	 * @return 返回对浏览器进行操作后的得到的WebDriver对象
	 * @throws IncorrectPageException 页面title与设置的title不一致时抛出的异常
	 */
	WebDriver oprateBrower() {
		// 全屏浏览器
		driver.manage().window().maximize();

		// 若循环结束，则说明页面无法正常加载，则置空pageTitle，抛出PageNotFountException
		throw new IncorrectPageException("页面载入出错");
	}

	/**
	 * 用于新增一个标签页，并打开指定站点
	 * 
	 * @param url 指定的站点
	 * @throws IncorrectPageException 当页面无法加载时抛出
	 */
	private String openNewLabelPage() {
		// 获取当前所有的handle
		Set<String> handleSet = driver.getWindowHandles();
		// 编写js脚本，执行js，以开启一个新的标签页
		String js = "window.open(\"\");";
		((JavascriptExecutor) driver).executeScript(js);
		// 移除原有的windows的Handle，保留新打开的windows的Handle
		String newHandle = "";
		for (String handle : driver.getWindowHandles()) {
			if (!handleSet.contains(handle)) {
				newHandle = handle;
				break;
			}
		}

		// 设置页面的Handle
		nowPage.setHandle(newHandle);
		// 加载页面
		loadPage(nowPage);

		return nowPage.getHandle();
	}

	/**
	 * 用于在原标签页中打开站点
	 * 
	 * @param url 指定的站点
	 */
	private String overridePage() {
		// 设置页面的Handle
		nowPage.setHandle(driver.getWindowHandle());
		// 加载页面
		loadPage(nowPage);

		return nowPage.getHandle();
	}

	/**
	 * 用于加载页面
	 * 
	 * @param newPage 页面类对象
	 */
	private void loadPage(Page newPage) {
		// 切换窗口
		driver.switchTo().window(newPage.getHandle());
		// 加载页面，若页面无法加载，则抛出IncorrectPageException
		if (!newPage.loadPage(driver)) {
			throw new IncorrectPageException("页面无法加载");
		}
	}

	/**
	 * 该方法用于根据页面的名称查找窗口的Handle
	 * 
	 * @param pageName 页面名称
	 * @return 页面对应窗口的Handle
	 */
	private String findPageHandle(String pageName) {
		// 存储查找到的handle
		String handle = "";

		// 循环，查找与pageName对应的Page对象，并返回相应的Handle
		for (Page page : pageList) {
			if (page.getPageName().equals(pageName)) {
				handle = page.getHandle();
				break;
			}
		}

		return handle;
	}
}
