package pres.auxiliary.selenium.brower;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import pres.auxiliary.selenium.page.Page;

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
	HashMap<String, Page> pageMap = new HashMap<String, Page>(16);
	
	/**
	 * 存储浏览器打开的窗口handle值
	 */
	HashSet<String> windowHandleSet = new HashSet<>();

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
	 * @param driverFile 驱动文件对象
	 */
	public AbstractBrower(File driverFile) {
		this.driverFile = driverFile;

		// 存储页面信息
		informationJson.put("驱动所在路径", driverFile.getAbsolutePath());
	}

	/**
	 * 指定驱动文件路径并添加一个待测站点
	 * 
	 * @param driverFile 驱动文件对象
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
		if (pageMap.size() == 0) {
			// 存储页面对象
			pageMap.put(newPage.getPageName(), newPage);
			// 将当前页面对象指向newPage
			nowPage = newPage;
			// 在当前标签上打开页面
			overridePage();
		} else {
			if (openNewLabel) {
				// 存储页面对象
				pageMap.put(newPage.getPageName(), newPage);
				// 将当前页面对象指向newPage
				nowPage = newPage;
				// 在标签页上打开页面
				openNewLabelPage();
			} else {
				// 先在pageList移除nowPage
				pageMap.remove(nowPage.getPageName());
				pageMap.put(newPage.getPageName(), newPage);
				// 将nowPage指向newPage
				nowPage = newPage;
				// 在当前标签上打开页面
				overridePage();
			}
		}
		
		//存储当前打开窗口的handle
		windowHandleSet.add(nowPage.getHandle());
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
			// 添加驱动的存储位置
			System.setProperty(getBrowerDriverSetName(), driverFile.getAbsolutePath());
			// 打开浏览器
			openBrower();
			
			// 添加操作信息
			informationJson.put("浏览器名称", ((RemoteWebDriver) driver).getCapabilities().getBrowserName());
			informationJson.put("浏览器版本", ((RemoteWebDriver) driver).getCapabilities().getVersion());
			informationJson.put("操作系统版本", System.getProperties().getProperty("os.name"));
			
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
	/**
	 * 用于返回浏览器驱动设置的名称，由于每个浏览器不同，导致启动驱动名称也不同
	 * @return 驱动设置名称
	 */
	abstract String getBrowerDriverSetName();

	public String getAllInformation() {
		// 遍历所有标签页，存储标签页信息
		JSONArray labelInformation = new JSONArray();
		pageMap.forEach((name, page) -> {
			labelInformation.add(page.getPageInformation());
		});
		// 存储标签页信息
		informationJson.put("标签信息", labelInformation);
					
		return informationJson.toJSONString();
	}
	
	/**
	 * 用于返回当前在浏览器中被打开的页面
	 * @return 在浏览器中被打开的{@link Page}对象
	 */
	public List<Page> getOpenPage() {
		List<Page> pageList = new ArrayList<>();
		
		//遍历pageMap，存储所有存在handle的Page对象
		pageMap.forEach((k, v) -> {
			if (!v.getHandle().isEmpty()) {
				pageList.add(v);
			}
		});
		
		return pageList;
	}
	
	/**
	 * 用于返回当前指向的窗体打开的页面{@link Page}对象
	 * @return {@link Page}对象
	 */
	public Page getNowPage() {
		return nowPage;
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
			pageMap.clear();
			nowPage = null;
		} else {
			// 根据当前窗口handle查找相应的page对象
			Page page = findPage(driver.getWindowHandle());
			// 若窗口指向的页面存在，则将该页面从类中移除
			if (page != null) {
				pageMap.remove(page.getPageName());
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
		pageMap.clear();
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
			//切换当前指向的页面
			nowPage = pageMap.get(pageName);
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
		if (pageMap.containsKey(page.getPageName())) {
			try {
				driver.switchTo().window(page.getHandle());
				//切换当前指向的页面
				nowPage = page;
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
	 * 用于切换到弹窗上，当浏览器存在多个弹窗时，该方法无法保证能切换至理想的窗口，
	 * 但调用该方法后，其弹窗会作为{@link Page}类对象进行存储，其页面名称为当前窗口的
	 * handle值，若切换的窗口非理想的窗口，则可以多次调用该方法，直至切换至理想的窗口
	 * 为止。若当前不存在弹窗，则返回false，若切换弹窗成功，则返回true
	 * 
	 * @return 是否成功切换弹窗
	 */
	public boolean switchPopuWindow() {
		//判断当前是否存在弹窗，若不存在，则直接失败
		if (!hasPopuWindow()) {
			return false;
		}
		
		//若存在弹窗，则获取获取当前所有窗口的handle，获取其中一个弹窗的handle值进行存储
		String popuHandle = getPopuHandle(driver.getWindowHandles());
		//切换弹窗
		driver.switchTo().window(popuHandle);
		//将弹窗转换为Page类对象，存储至pageMap中，以避免存在多个弹窗时其他弹窗无法被切换的情况
		Page popuPage = new Page(driver.getCurrentUrl(), popuHandle);
		popuPage.setHandle(popuHandle);
		popuPage.setAssertTitle(driver.getTitle());
		
		//存储弹窗
		pageMap.put(popuHandle, popuPage);
		//将当前页面指向到弹窗页面上
		nowPage = popuPage;
		windowHandleSet.add(popuHandle);
		
		//返回切换弹窗成功
		return true;
	}
	
	/**
	 * 用于判断当前浏览器中是否存在弹窗（未被存储的浏览器标签）。注意，若在
	 * 浏览器上自行打开的标签，其也会被计算为弹窗
	 * @return 是否存在弹窗
	 */
	public boolean hasPopuWindow() {
		//获取当前浏览器上的窗口handle
		Set<String> nowWindowHandleSet = driver.getWindowHandles();
		//移除被手动关闭的标签
		removeClosePage(nowWindowHandleSet);
		//若nowWindowHandleSet中的数量与windowHandleSet不一致，则可认为存在弹窗
		return nowWindowHandleSet.size() != windowHandleSet.size();
	}
	
	/**
	 * 定位到弹框上并且点击确定按钮，并返回弹框上的文本
	 * 
	 * @return 弹框上的文本
	 */
	public String alertAccept() {
		String text = alertGetText();
		driver.switchTo().alert().accept();

		return text;

	}

	/**
	 * 定位到弹框上并且点击取消按钮，并返回弹框上的文本
	 * 
	 * @return 弹框上的文本
	 */
	public String alertDimiss() {
		String text = alertGetText();
		driver.switchTo().alert().dismiss();

		return text;
	}

	/**
	 * 定位到弹框上并且在其文本框中输入信息
	 * 
	 * @param content 需要输入的信息
	 * @return 弹框上的文本
	 */
	public String alertInput(String content) {
		String text = alertGetText();
		driver.switchTo().alert().sendKeys("");

		return text;
	}

	/**
	 * 获取弹框上的文本
	 * 
	 * @return 弹框上的文本
	 */
	public String alertGetText() {
		return driver.switchTo().alert().getText();
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
		if (pageMap.containsKey(pageName)) {
			return pageMap.get(pageName).getHandle();
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
		for (String pageName : pageMap.keySet()) {
			// 若存储的handle与传入的handle相同，则对其进行
			if (handle.equals(pageMap.get(pageName).getHandle())) {
				return pageMap.get(pageName);
			}
		}
		
		// 若类中未存储相应page，则返回null
		return null;
	}
	
	/**
	 * 用于在pageMap中移除已被关闭的浏览器标签，该方法主要用于清除手动关闭的标签
	 * @param handleList 当前浏览器存储的标签值
	 */
	private void removeClosePage(Set<String> handleList) {
		ArrayList<String> removeKeyList = new ArrayList<>();
		//遍历pageMap，若其handle值不存在于handleList中，则记录需要移除的key值
		pageMap.forEach((key, value) -> {
			if (!handleList.contains(value.getHandle())) {
				removeKeyList.add(key);
			}
		});
		
		//遍历removeKeyList，在pageMap中移除相应的key
		removeKeyList.forEach(key -> {
			pageMap.remove(key);
		});
	}
	
	/**
	 * 用于返回当前浏览器中其中一个弹窗的handle值，当存在多个弹窗时
	 * 该方法不保证返回的handle为理想的handle值
	 * @param handleList 当前浏览器中所有的窗口handle集合
	 * @return 其中一个弹窗的handle值
	 */
	private String getPopuHandle(Set<String> handleSet) {
		return handleSet.stream().filter(handle -> findPage(handle) == null).findAny().get();
	}
}
