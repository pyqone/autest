package pres.auxiliary.work.selenium.brower;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * <p><b>文件名：</b>AbstractBrower.java</p>
 * <p><b>用途：</b>该类定义启动浏览器时必要的方法，开启浏览器的方法由子类继承编写</p>
 * <p><b>编码时间：</b>2020年4月17日下午8:51:53</p>
 * <p><b>修改时间：</b>2020年4月17日下午8:51:53</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
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
	 * 存储打开的页面
	 */
	HashMap<String, Page> pageList = new HashMap<String, Page>(16);

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
			pageList.put(newPage.getPageName(), newPage);
			// 将当前页面对象指向newPage
			nowPage = newPage;
			// 在当前标签上打开页面
			overridePage();
		} else {
			if (openNewLabel) {
				// 存储页面对象
				pageList.put(newPage.getPageName(), newPage);
				// 将当前页面对象指向newPage
				nowPage = newPage;
				// 在标签页上打开页面
				openNewLabelPage();
			} else {
				// 先在pageList移除nowPage
				pageList.remove(nowPage.getPageName());
				pageList.put(newPage.getPageName(), newPage);
				// 将nowPage指向newPage
				nowPage = newPage;
				// 在当前标签上打开页面
				overridePage();
			}
		}

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
	 */
	abstract void openBrower();

	public JSONObject getAllInformation() {
		// 遍历所有标签页，存储标签页信息
		JSONArray labelInformation = new JSONArray();
		pageList.forEach((name, page) -> {
			labelInformation.add(page.getPageInformation());
		});
		// 存储标签页信息
		informationJson.put("标签信息", labelInformation);
					
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
			//若需要查看标签信息，则对标签信息即时进行存储
			if ("标签信息".equals(key)) {
				// 遍历所有标签页，存储标签页信息
				JSONArray labelInformation = new JSONArray();
				pageList.forEach((name, page) -> {
					labelInformation.add(page.getPageInformation());
				});
				// 存储标签页信息
				informationJson.put("标签信息", labelInformation);
			}
			
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
	 * 用于关闭当前标签，若只存在一个标签时，关闭标签页后将生成一个空白页
	 */
	public void closeLabel() {
		// 若当前只存在一个标签时，关闭标签，并生成一个新的空标签；若当前不只一个标签时，则对标签进行关闭
		if (driver.getWindowHandles().size() == 1) {
			// 打开一个新标签
			String newHandle = openNewLabel();
			// 关闭当前的标签
			driver.close();
			// 将页面切换至新打开的标签上
			driver.switchTo().window(newHandle);

			// 将标签内容全部置空及当前页面内容置空
			pageList.clear();
			nowPage = null;
		} else {
			// 根据当前窗口handle查找相应的page对象
			Page page = findPage(driver.getWindowHandle());
			// 若窗口指向的页面存在，则将该页面从类中移除
			if (page != null) {
				pageList.remove(page.getPageName());
			}

			// 关闭当前标签
			driver.close();
			// 将driver随意指定一个窗口，保证能正常切换
			driver.getWindowHandles().stream().forEach(handle -> {
				driver.switchTo().window(handle);
				return;
			});
		}
	}

	/**
	 * 用于关闭浏览器
	 */
	public void closeBrower() {
		//关闭浏览器
		driver.quit();
		//将driver指定为null
		driver = null;
		// 清空页面存储的内容
		pageList.clear();
		nowPage = null;
	}

	/**
	 * 用于根据页面名称切换页面
	 * 
	 * @param pageName 页面名称
	 */
	public void switchWindow(String pageName) {
		try {
			driver.switchTo().window(findPageHandle(pageName));
		} catch (NoSuchWindowException e) {
			throw new IncorrectPageException("页面未在浏览器中打开");
		}
	}

	/**
	 * 用于根据页面切换页面
	 * 
	 * @param page 页面对象
	 */
	public void switchWindow(Page page) {
		// 查找pageList中是否存在传入的page，若存在，则获取相应的handle后对窗口进行切换
		if (pageList.containsKey(page.getPageName())) {
			try {
				driver.switchTo().window(page.getHandle());
			} catch (NoSuchWindowException e) {
				throw new IncorrectPageException("页面未在浏览器中打开");
			}
		}
	}

	/**
	 * 用于切换到当前页面
	 */
	public void switchNowPage() {
		// 若当前存储的页面不为null并且handle不为空时，则将页面切换至nowPage指向的页面
		if (nowPage != null && !nowPage.getHandle().isEmpty()) {
			switchWindow(nowPage);
		}
	}

	/**
	 * 用于新增一个标签页，并打开指定站点
	 * 
	 * @param url 指定的站点
	 * @throws IncorrectPageException 当页面无法加载时抛出
	 */
	private void openNewLabelPage() {
		// 设置页面的Handle
		nowPage.setHandle(openNewLabel());
		// 加载页面
		loadPage(nowPage);
	}

	/**
	 * 用于在原标签页中打开站点
	 * 
	 * @param url 指定的站点
	 */
	private void overridePage() {
		// 设置页面的Handle
		nowPage.setHandle(driver.getWindowHandle());
		// 加载页面
		loadPage(nowPage);
	}

	/**
	 * 用于新建一个标签（窗口），返回其在浏览器中的handle值
	 * 
	 * @return 新建标签（窗口）的handle
	 */
	private String openNewLabel() {
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

		return newHandle;
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
		if (pageList.containsKey(pageName)) {
			return pageList.get(pageName).getHandle();
		} else {
			return "";
		}
	}

	/**
	 * 用于根据窗口handle来获取Page对象
	 * 
	 * @param handle 窗口handle
	 * @return Page对象
	 */
	private Page findPage(String handle) {
		// 遍历所有的page，查询与传入相同的handle
		for (String pageName : pageList.keySet()) {
			// 若存储的handle与传入的handle相同，则对其进行
			if (handle.equals(pageList.get(pageName).getHandle())) {
				return pageList.get(pageName);
			}
		}
		
		// 若类中未存储相应page，则返回null
		return null;
	}
}
