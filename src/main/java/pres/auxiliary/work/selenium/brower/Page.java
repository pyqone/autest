package pres.auxiliary.work.selenium.brower;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;

import com.alibaba.fastjson.JSONObject;

/**
 * <p><b>文件名：</b>Page.java</p>
 * <p><b>用途：</b>用于存储对浏览器加载的页面信息以及页面操作</p>
 * <p><b>编码时间：</b>2020年4月10日上午8:02:45</p>
 * <p><b>修改时间：</b>2020年4月10日上午8:02:45</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public class Page {
	/**
	 * 用于接收用户需要进入的网站
	 */
	private String url = "";
	/**
	 * 存储页面加载等待时间
	 */
	private int loadTime = 120;
	/**
	 * 用于存储目标站点的title
	 */
	private String assertTitle = "";
	/**
	 * 用于存储页面的名称
	 */
	private String pageName = "NewPage";
	/**
	 * 存储页面在窗口上的handle
	 */
	private String handle = "";
	/**
	 * 用于存储页面自动刷新的次数，默认为0次
	 */
	int rafreshCount = 0;
	
	/**
	 * 用于存储浏览器启动时的信息
	 */
	private JSONObject pageInformationJson = new JSONObject();
	
	/**
	 * 初始化需要打开页面url以及站点的名称
	 * @param url 待测站点url
	 * @param pageName 站点名称
	 */
	public Page(String url, String pageName) {
		this.url = url;
		this.pageName = pageName;
		
		//存储页面信息
		pageInformationJson.put("页面名称", pageName);
		pageInformationJson.put("页面站点", url);
		pageInformationJson.put("页面加载时间", loadTime);
	}
	
	/**
	 * 用于返回存储的页面名称
	 * @return 页面名称
	 */
	public String getPageName() {
		return pageName;
	}
	
	/**
	 * 返回待测站点的url
	 * @return 待测站点的url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 用于返回页面所在窗口的handle
	 * @return 页面名称
	 */
	public String getHandle() {
		return handle;
	}
	
	/**
	 * 用于设置页面所在窗口的handle
	 * @param handle handle值
	 */
	public void setHandle(String handle) {
		this.handle = handle;
		//存储页面所在窗口的handle信息
		pageInformationJson.put("页面所在窗口", handle);
	}

	/**
	 * 用于设置页面加载时间
	 * @param loadTime 页面加载时间
	 */
	public void setLoadTime(int loadTime) {
		this.loadTime = loadTime;
		//存储加载时间信息
		pageInformationJson.put("页面加载时间", loadTime);
	}
	
	/**
	 * 用于设置对页面的标题断言
	 * @param assertTitle 标题断言
	 */
	public void setAssertTitle(String assertTitle) {
		this.assertTitle = assertTitle;
		
		pageInformationJson.put("页面断言", assertTitle);
	}

	/**
	 * 用于设置对页面自动加载次数的限制
	 * @param rafreshCount 加载次数限制
	 */
	public void setRafreshCount(int rafreshCount) {
		this.rafreshCount = rafreshCount;
		
		pageInformationJson.put("页面重载次数", rafreshCount);
	}

	/**
	 * 以Json形式返回当前页面的信息
	 * @return 当前页面的信息
	 */
	public JSONObject getPageInformation() {
		return pageInformationJson;
	}
	
	/**
	 * 用于通过浏览器加载页面，并根据页面断言，返回页面是否加载成功。若未设置断言，则无论
	 * 页面是否成功加载，均返回true
	 * @param driver WebDriver对象，通过可通过{@link Brower}类及其子类来生成
	 * @return 根据页面断言返回页面是否加载成功
	 */
	public boolean loadPage(WebDriver driver) {
		// 将设置的自动刷新次数存储在临时变量中，并加上1
		// 加1的目的是自动刷新判断是用do...while循环实现，若不事先加上1在会导致循环少1次
		int rafresh = rafreshCount + 1;
				
		// 进入站点
		driver.get(url);
		// 读取并设置浏览器等待时间
		driver.manage().timeouts().pageLoadTimeout(loadTime, TimeUnit.SECONDS);
		
		// 循环执行页面加载判断，判断其是否加载出目标站点，若加载出来页面，则结束循环，若用户设置的自动刷新次数循环完还没加载出页面，则
		// 抛出PageNotFoundException异常。页面加载判断必须读取一次，所以使用do...while循环
		do {
			// 若页面加载成功，则判断加载的页面是否与用户设置的一致，若用户未设置目标站点的title，则跳过判断
			if (!judgeAssert(driver)) {
				// 将刷新次数减1
				rafresh--;
				// 刷新网页
				driver.navigate().refresh();
				// 不执行后续代码，继续循环
				continue;
			}
			
			//若通过判断，则返回相应的WebDriver对象
			return true;

		// 判断的条件刷新次数rafresh为0时则结束循环
		} while (rafresh != 0);
		
		return false;
	}
	
	/**
	 * 用于对加载的页面信息与断言对比 ，返回对比结果
	 * @param driver WebDriver对象
	 * @return
	 */
	private boolean judgeAssert(WebDriver driver) {
		//若页面断言为空，则直接返回true
		if (assertTitle.isEmpty()) {
			return true;
		} else {
			//若页面断言不为空且页面信息与断言不相同，则返回false
			if (!driver.getTitle().equals(assertTitle)) {
				return false;
			} else {
				return true;
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((handle == null) ? 0 : handle.hashCode());
		result = prime * result + ((pageName == null) ? 0 : pageName.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Page other = (Page) obj;
		if (handle == null) {
			if (other.handle != null)
				return false;
		} else if (!handle.equals(other.handle))
			return false;
		if (pageName == null) {
			if (other.pageName != null)
				return false;
		} else if (!pageName.equals(other.pageName))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}
}
