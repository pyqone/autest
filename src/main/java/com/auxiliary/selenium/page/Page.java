package com.auxiliary.selenium.page;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import com.alibaba.fastjson.JSONObject;
import com.auxiliary.selenium.brower.AbstractBrower;
import com.auxiliary.selenium.brower.IncorrectPageException;
import com.auxiliary.selenium.element.ElementData;
import com.auxiliary.selenium.location.ByType;
import com.auxiliary.selenium.location.NoFileLocation;

/**
 * <p><b>文件名：</b>Page.java</p>
 * <p><b>用途：</b>用于存储对浏览器加载的页面信息以及页面操作</p>
 * <p><b>编码时间：</b>2020年4月10日上午8:02:45</p>
 * <p><b>修改时间：</b>2021年2月18日下午6:12:14</p>
 * @author 彭宇琦
 * @version Ver1.1
 * @since JDK 1.8
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
	 * 用于存储需要断言的内容
	 */
	private HashMap<PageAssertType, List<Object>> assertMap;
	/**
	 * 标记当前是否启用断言
	 */
	private boolean isAssert = false;
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
		
		assertMap = new HashMap<>(16);
		//初始化断言的内容
		Arrays.stream(PageAssertType.values()).forEach(type -> assertMap.put(type, new ArrayList<>()));
		
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
	 * <p>
	 * <b>注意：</b>
	 * <ol>
	 * <li>页面断言为一次性断言，故添加的断言内容只累积，即多次添加断言内容，在进行断言时将全部用于判断</li>
	 * <li>设置的断言内容为正则表达式，若设置的断言内容存在正则表达式中的特殊字符，则请自行转译，
	 * 否则可能会得到不符合预期的结果</li>
	 * </ol>
	 * </p>
	 * @param titles 标题断言
	 */
	public void setAssertTitle(String...titles) {
		Optional.ofNullable(titles).filter(ts -> ts.length != 0).ifPresent(ts -> {
			List<Object> list = assertMap.get(PageAssertType.TITLE);
			Arrays.stream(ts).forEach(list::add);
			assertMap.put(PageAssertType.TITLE, list);
			
			pageInformationJson.put("页面标题断言", list);
			
			//记录启用了断言
			isAssert = true;
		});
	}
	
	/**
	 * 用于设置对页面的html内容断言
	 * <p>
	 * <b>注意：</b>
	 * <ol>
	 * <li>页面断言为一次性断言，故添加的断言内容只累积，即多次添加断言内容，在进行断言时将全部用于判断</li>
	 * <li>设置的断言内容为正则表达式，若设置的断言内容存在正则表达式中的特殊字符，则请自行转译，
	 * 否则可能会得到不符合预期的结果</li>
	 * </ol>
	 * </p>
	 * @param htmls html断言数组
	 */
	public void setAssertHtml(String...htmls) {
		Optional.ofNullable(htmls).filter(ts -> ts.length != 0).ifPresent(ts -> {
			List<Object> list = assertMap.get(PageAssertType.HTML);
			Arrays.stream(ts).forEach(list::add);
			assertMap.put(PageAssertType.HTML, list);
			
			pageInformationJson.put("页面html断言", list);
			
			//记录启用了断言
			isAssert = true;
		});
	}
	
	/**
	 * 用于设置对页面最终跳转的url
	 * <p>
	 * <b>注意：</b>
	 * <ol>
	 * <li>页面断言为一次性断言，故添加的断言内容只累积，即多次添加断言内容，在进行断言时将全部用于判断</li>
	 * <li>设置的断言内容为正则表达式，若设置的断言内容存在正则表达式中的特殊字符，则请自行转译，
	 * 否则可能会得到不符合预期的结果</li>
	 * </ol>
	 * </p>
	 * @param urls url断言数组
	 */
	public void setAssertUrl(String...urls) {
		Optional.ofNullable(urls).filter(ts -> ts.length != 0).ifPresent(ts -> {
			List<Object> list = assertMap.get(PageAssertType.URL);
			Arrays.stream(ts).forEach(list::add);
			assertMap.put(PageAssertType.URL, list);
			
			pageInformationJson.put("页面url断言", list);
			
			//记录启用了断言
			isAssert = true;
		});
	}
	
	/**
	 * 用于设置对页面的文本内容断言
	 * <p>
	 * <b>注意：</b>
	 * <ol>
	 * <li>页面断言为一次性断言，故添加的断言内容只累积，即多次添加断言内容，在进行断言时将全部用于判断</li>
	 * <li>页面文本断言不支持正则表达式，需要指定确定的关键词，否则可能会得到不符合预期的结果</li>
	 * </ol>
	 * </p>
	 * @param texts 页面文本断言数组
	 */
	public void setAssertText(String...texts) {
		Optional.ofNullable(texts).filter(ts -> ts.length != 0).ifPresent(ts -> {
			List<Object> list = assertMap.get(PageAssertType.TEXT);
			//将文本转换为ElementData类，以元素的形式进行断言
			Arrays.stream(texts).map(text -> {
				NoFileLocation nf = new NoFileLocation();
				nf.putElementLocation("文本元素", ByType.XPATH, "//*[contains(text(), '" + text + "')]");
				
				ElementData el = new ElementData("文本元素", nf);
				return el;
			}).forEach(list::add);
			assertMap.put(PageAssertType.TEXT, list);
			
			pageInformationJson.put("页面文本断言", list);
			
			//记录启用了断言
			isAssert = true;
		});
	}
	
	/**
	 * 用于设置对页面的元素断言
	 * <p>
	 * <b>注意：</b>
	 * <ol>
	 * <li>页面断言为一次性断言，故添加的断言内容只累积，即多次添加断言内容，在进行断言时将全部用于判断</li>
	 * <li>设置的断言内容为正则表达式，若设置的断言内容存在正则表达式中的特殊字符，则请自行转译，
	 * 否则可能会得到不符合预期的结果</li>
	 * </ol>
	 * </p>
	 * @param elements 元素断言数组
	 */
	public void setAssertElement(ElementData...elements) {
		Optional.ofNullable(elements).filter(ts -> ts.length != 0).ifPresent(ts -> {
			List<Object> list = assertMap.get(PageAssertType.ELEMENT);
			Arrays.stream(elements).filter(e -> e != null).forEach(list::add);
			assertMap.put(PageAssertType.ELEMENT, list);
			
			pageInformationJson.put("页面元素断言", list);
			
			//记录启用了断言
			isAssert = true;
		});
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
	 * @param driver WebDriver对象，通过可通过{@link AbstractBrower}类及其子类来生成
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
	 * @return 断言是否成功
	 */
	private boolean judgeAssert(WebDriver driver) {
		if (driver == null) {
			throw new IncorrectPageException("未指定WebDriver对象");
		}
		
		//判断当前是否启用了断言
		if (!isAssert) {
			return true;
		}
		
		//遍历断言内容的key值
		for (PageAssertType key : assertMap.keySet()) {
			//若当前未设置断言，则跳过该判断
			if (assertMap.get(key).size() == 0) {
				continue;
			}
			
			//根据关键词，对页面进行断言
			switch (key) {
			case TITLE:
				//获取页面标题，判断标题是否符合所有的正则，若存在不符合的，则返回false
				if (!assertTextContent(driver.getTitle(), key)) {
					return false;
				}
				break;
			case HTML:
				//获取页面html，判断标题是否符合所有的正则，若存在不符合的，则返回false
				if (!assertTextContent(driver.getPageSource(), key)) {
					return false;
				}
				break;
			case URL:
				//获取页面url，判断标题是否符合所有的正则，若存在不符合的，则返回false
				if (!assertTextContent(driver.getCurrentUrl(), key)) {
					return false;
				}
				break;
			//文本与元素判断的机制一致
			case TEXT:
			case ELEMENT:
				//读取需要判断的元素，若存在不符合的，则返回false
				if (!assertElementContent(driver, key)) {
					return false;
				}
				break;
			default:
				break;
			}
		}
		
		return true;
	}
	
	/**
	 * 用于对页面文本相关的内容进行断言的方法
	 * @param content 文本内容
	 * @param key 断言类型
	 * @return 断言是否成功
	 */
	private boolean assertTextContent(String content, PageAssertType key) {
		for (Object assertObj : assertMap.get(key)) {
			//设置当前值为正则，传入的内容为需要判断的字符串
			Matcher match = Pattern.compile((String) assertObj).matcher(content);
			//若通过该正则，在内容中无法找到，则返回false
			if (!match.find()) {
				return false;
			}
		}
		
		//若通过所有判断，则返回true
		return true;
	}
	
	/**
	 * 用于对页面元素相关的内容进行断言的方法
	 * @param driver 页面WebDriver对象
	 * @param key 断言类型
	 * @return 断言是否成功
	 */
	private boolean assertElementContent(WebDriver driver, PageAssertType key) {
		for (Object assertObj : assertMap.get(key)) {
			//将Object类转换为ElementData类
			ElementData e = (ElementData) assertObj;
			//获取元素的定位方式
			List<ByType> byTypeList = e.getByTypeList();
			List<String> valueList = e.getValueList();
			
			//遍历元素的定位方式，拼接定位方式为By对象
			boolean isSuccess = true;
			for (int i = 0; i < e.getLocationSize(); i++) {
				try {
					driver.findElement(byTypeList.get(i).getBy(valueList.get(i)));
					isSuccess = true;
				} catch (NoSuchElementException ex) {
					isSuccess = false;
				}
			}
			
			//判断当前查找完所有元素定位方式后是否能在页面找到元素，
			//若不能找到，则直接返回false；否则，进行下一个判断
			if (!isSuccess) {
				return false;
			} else {
				continue;
			}
		}
		
		//若通过所有判断，则返回true
		return true;
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
	
	/**
	 * <p><b>文件名：</b>Page.java</p>
	 * <p><b>用途：</b>
	 * 指定页面断言的条件枚举
	 * </p>
	 * <p><b>编码时间：</b>2021年2月18日下午5:25:34</p>
	 * <p><b>修改时间：</b>2021年2月18日下午5:25:34</p>
	 * @author 彭宇琦
	 * @version Ver1.0
	 * @since JDK 1.8
	 *
	 */
	public enum PageAssertType {
		/**
		 * 断言页面标题
		 */
		TITLE, 
		/**
		 * 断言页面html代码
		 */
		HTML, 
		/**
		 * 断言页面最终跳转的url
		 */
		URL, 
		/**
		 * 断言页面文本
		 */
		TEXT, 
		/**
		 * 断言页面元素
		 */
		ELEMENT
		;
	}
}
