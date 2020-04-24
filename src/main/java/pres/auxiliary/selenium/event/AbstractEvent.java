package pres.auxiliary.selenium.event;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.dom4j.InvalidXPathException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pres.auxiliary.selenium.tool.RecordTool;
import pres.auxiliary.selenium.xml.ElementLocationType;
import pres.auxiliary.selenium.xml.ReadXml;
import pres.auxiliary.selenium.xml.UndefinedElementException;

/**
 * <p><b>文件名：</b>AbstractEvent.java</p>
 * <p><b>用途：</b>所有事件类的基类，包含了事件类所用到的基础方法以及弹窗处理方法和窗体、窗口的切换方法</p>
 * <p><b>编码时间：</b>2019年9月24日下午4:24:15</p>
 * <p><b>修改时间：</b>2019年11月29日上午9:53:37</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public abstract class AbstractEvent {
	/**
	 * 用于存储浏览器的WebDriver对象，设为静态，保证所有的子类只使用一个WebDriver对象，以避免造成WebDriver不正确导致的Bug
	 */
	private static WebDriver driver;
	/**
	 * 用于指向存储控件定位方式的xml文件
	 */
	private static ReadXml xml;
	/**
	 * 设置显示等待的超时时间（默认3秒）
	 */
	private long waitTime = 3;
	/**
	 * 用于构造显示等待类对象
	 */
	private WebDriverWait wait;
	/**
	 * 存储单个控件的等待时间
	 */
	private HashMap<String, Long> controlWaitTime = new HashMap<String, Long>();
	/**
	 * 用于存储当前浏览器窗口的Handles值
	 */
	private String browserHandles;
	/**
	 * 存储当前定位的窗体层级
	 */
	private static ArrayList<String> iframeNames = new ArrayList<>();
	
	/**
	 * 控制是否自动切换窗体，由于通过Event类调用时会构造另一个事件类，但每个类都应共享一个开关，故需要加上static
	 */
	private static boolean switchIframe = true;
	
	/**
	 * 用于存储上一个获取到的元素
	 */
	private WebElement oldElement = null;
	/**
	 * 用于存储上一个获取到的元素的style属性
	 */
	private String oldElementStyle = "";

	/**
	 * 返回是否自动切换窗体
	 * @return 是否自动切换窗体
	 */
	public boolean isSwitchIframe() {
		return switchIframe;
	}

	/**
	 * 设置是否自动切换窗体
	 * @param switchIframe 是否自动切换窗体
	 */
	public void setSwitchIframe(boolean switchIframe) {
		this.switchIframe = switchIframe;
	}

	/**
	 * 构造对象并存储浏览器的WebDriver对象
	 * 
	 * @param driver 浏览器的WebDriver对象
	 */
	public AbstractEvent(WebDriver driver) {
		this.driver = driver;
		browserHandles = this.driver.getWindowHandle();
		wait = new WebDriverWait(driver, waitTime, EventConstants.FIND_TIME);
	}

	/**
	 * 用于返回等待时间
	 * 
	 * @return 等待时间
	 */
	public long getWaitTime() {
		return waitTime;
	}

	/**
	 * 用于设置等待时间
	 * 
	 * @param waitTime 等待时间
	 */
	public void setWaitTime(long waitTime) {
		this.waitTime = waitTime;
		wait = new WebDriverWait(driver, this.waitTime, 200);
	}

	/**
	 * 该方法用于返回浏览器的WebDriver对象
	 * 
	 * @return 浏览器的WebDriver对象
	 */
	public WebDriver getDriver() {
		return driver;
	}

	/**
	 * 该方法用于设置浏览器的WebDriver对象
	 * 
	 * @param driver 浏览器的WebDriver对象
	 */
	public void setDriver(WebDriver driver) {
		// 判断传入WebDriver对象是否为空，为空则抛出异常
		if (driver == null) {
			throw new WebDriverException("无效的WebDriver对象");
		}

		this.driver = driver;
		browserHandles = this.driver.getWindowHandle();
	}

	/**
	 * 用于设置指向存储元素定位方式的xml文件对象
	 * 
	 * @param xmlFile 存储元素定位方式的xml文件对象
	 */
	public void setXmlFile(File xmlFile) {
		xml = new ReadXml(xmlFile);
	}
	
	/**
	 * 用于返回ReadXml类对象
	 * @return ReadXml类对象
	 */
	public ReadXml getXml() {
		return xml;
	}
	
	/**
	 * 用于返回写入xml文件的或传入的元素定位方式，以“mode=position”的形式输出
	 * 
	 * @param name 控件的名称或xpath与css定位方式
	 * @return 元素定位方式
	 */
	public String getElementPosition(String name) {
		// 智能匹配方式：
		// 1.先对xml文件进行扫描，若存在该元素对应的标签，则读取xml文件的定位方式，并识别有效的定位方式一一匹配，直到正确为止；
		// 2.若在xml文件中查找不到该元素，则按照xpath和css的规则进行匹配，直到判断出该元素的定位方式位置
		// 3.若仍找不到元素，则抛出UnrecognizableLocationModeException
		try {
			ElementLocationType mode = readXml(name);
			// 判断readXML()方法的返回值是否为空串，若为空串，则抛出TimeoutException
			if (mode == null) {
				// 将错误信息写入到记录工具的备注中
				RecordTool.recordMark("元素查找超时或不存在，请检查xml文件中“" + name + "”对应元素的定位方式");
				throw new TimeoutException("元素查找超时或不存在，请检查xml文件中“" + name + "”对应元素的定位方式");
			} else {
				// 页面查找元素，并存储该元素
				return mode + "=" + xml.getValue(name, mode);
			}
		} catch (UndefinedElementException | NullPointerException | InvalidXPathException e) {
			ElementLocationType mode = readValue(driver, name);
			// 判断readValue()方法的返回值是否为空串，若为空串，则抛出UnrecognizableLocationModeException
			if (mode == null) {
				// 将错误信息写入到记录工具的备注中
				RecordTool.recordMark("元素定位方式类型无法识别：" + name);
				// 若都不匹配，则抛出异常
				throw new UnrecognizableLocationModeException("元素定位方式类型无法识别：" + name);
			} else {
				//若有返回定位方式，则根据定位方式类型，获取其元素
				switch (mode) {
				case XPATH:
					return ElementLocationType.XPATH.getValue() + "=" + name;
				case CSS:
					return ElementLocationType.CSS.getValue() + "=" + name;
				default:
					return "";
				}
			}
		}
	}

	/**
	 * 用于对单个控件设置等待时间
	 * 
	 * @param name     控件名称
	 * @param waitTime 等待时间
	 */
	public void setContorlWaitTime(String name, long waitTime) {
		controlWaitTime.put(name, waitTime);
	}

	/**
	 * 该方法用于将窗体切回顶层，当本身是在最顶层时，则该方法将使用无效
	 */
	public void switchRootFrame() {
		driver.switchTo().defaultContent();
		iframeNames.clear();
	}
	
	/**
	 * 该方法用于将窗体切换到上一层（父层），当本身是在最顶层时，则该方法将使用无效
	 */
	public void switchParentFrame() {
		driver.switchTo().parentFrame();
		iframeNames.remove(iframeNames.size() - 1);
	}

	/**
	 * 该方法用于通过悬浮窗名称，使用xpath的定位方式将页面切换至悬浮窗（iframe标签）上
	 * 
	 * @param name 悬浮窗的名称
	 */
	public void switchFrame(String name) {
		//获取父窗体的窗体名称
		String parentIframeName = xml.getIframeName(name);
		
		//判断窗体是否存在父窗体，若不存在父窗体（返回值为空），则切换，若有，则递归调用该方法，直到找到最顶层窗体为止
		if (parentIframeName.isEmpty()) {
			//清空iframeNames中的内容
			iframeNames.clear();
			//将窗体切换到顶层
			driver.switchTo().defaultContent();
		} else {
			switchFrame(parentIframeName);
		}
		
		//记录当前自动切换的窗体的状态
		boolean isSwitchFrame = isSwitchIframe();
		//切换窗体前设置不自动切换窗体，避免窗体的循环切换
		setSwitchIframe(false);
		//切换窗体
		driver.switchTo().frame(judgeElementMode(name));
		//切换窗体后还原原始的切换窗体状态
		setSwitchIframe(isSwitchFrame);
		//存储窗体切换记录
		iframeNames.add(name);
	}

	/**
	 * 该方法可用于切换弹出的窗口（新标签或新窗口），并返回新窗口的WebDriver对象，若无新窗口，则返回当前的窗口的WebDriver对象。
	 * 注意，该方法只能切换弹出一个窗口的情况，若有多个窗口，通过该方法无法准确定位，可参考方法{@link #switchWindow(String)}。
	 */
	public void switchWindow() {
		Set<String> handles = driver.getWindowHandles();
		// 判断是否只存在一个窗体，若只存在一个，则直接返回当前浏览器的WebDriver对象
		if (handles.size() == 1) {
			return;
		}

		// 获取当前窗口的handle
		String winHandle = driver.getWindowHandle();
		// 循环，获取所有的页面handle
		for (String newWinHandle : handles) {
			// 判断获取到的窗体handle是否为当前窗口的handle，若不是，则将其定位到获取的handle对应的窗体上
			if (!newWinHandle.equals(winHandle)) {
				driver.switchTo().window(newWinHandle);
			}
		}

		throw new NoSuchWindownException("未找到相应的窗体");
	}

	/**
	 * 该方法用于将窗口切换回最原始的窗口上。
	 */
	public void switchOldWindow() {
		driver.switchTo().window(browserHandles);
	}

	/**
	 * 该方法可根据控件名称，之后对比每一个弹窗，若某一个弹窗上存在元素名对应的元素，则返回相应
	 * 窗口的WebDriver对象,若无新窗口，则返回当前的窗口的WebDriver对象。当所有的窗体都
	 * 不包含相应的元素时，则抛出NoSuchWindownException异常
	 * 
	 * @param controlName 控件名称
	 * @throws NoSuchWindownException 窗口未找到时抛出的异常
	 */
	public void switchWindow(String controlName) {
		Set<String> handles = driver.getWindowHandles();
		// 判断是否只存在一个窗体，若只存在一个，则直接返回当前浏览器的WebDriver对象
		if (handles.size() == 1) {
			return;
		}

		// 循环，获取所有的页面handle
		for (String newWinHandle : handles) {
			driver.switchTo().window(newWinHandle);
			// 调用judgeElementMode()方法来判断元素是否存在，如果元素存在，则返回相应页面的WebDriver对象，若抛出异常（元素不存在），则返回当前
			try {
				judgeElementMode(controlName);
			} catch (TimeoutException e) {
				continue;
			}
		}

		throw new NoSuchWindownException("未找到存在元素" + controlName + "所在的窗体");
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
	 * <p>
	 * 用于根据传入的控件名称或定位方式，对控件在页面上定位，返回其WebElement对象。形参可以传入在xml文件中元素的名称，
	 * 亦可以传入页面元素的定位方式，但目前识别只支持xpath和css两种方式。
	 * 该方法获取的是单个元素，但本质是调用了{@link #judgeElementModes(String)}方法，只是将其容器中第一个元素进行返回。
	 * 可用于普通元素事件获取页面元素中使用
	 * </p>
	 * <p>
	 * 元素识别判断方式按照以下步骤进行：<br>
	 *  1.先对xml文件进行扫描，若存在该元素对应的标签，则读取xml文件的定位方式，并识别有效的定位方式一一匹配，直到正确为止；<br>
	 *  2.若在xml文件中查找不到该元素，则按照xpath和css的规则进行匹配，直到判断出该元素的定位方式位置；<br>
	 *  3.若仍找不到元素，则抛出UnrecognizableLocationModeException
	 *  </p>
	 * 
	 * @param text   元素名称或元素的定位方式
	 * @return 返回页面元素WebElement对象
	 * @throws TimeoutException 元素在指定时间内未查找到时，抛出的异常
	 * @throws UnrecognizableLocationModeException 元素无法识别时抛出的异常
	 */
	protected WebElement judgeElementMode(String name) {
		return judgeElementModes(name).get(0);
	}
	
	/**
	 * <p>
	 * 用于根据传入的控件名称或定位方式，对控件在页面上定位，返回其WebElement对象。形参可以传入在xml文件中元素的名称，
	 * 亦可以传入页面元素的定位方式，但目前识别只支持xpath和css两种方式。
	 * 该方法获取的是一组元素，可用于对列表元素事件中。
	 * </p>
	 * <p>
	 * 元素识别判断方式按照以下步骤进行：<br>
	 *  1.先对xml文件进行扫描，若存在该元素对应的标签，则读取xml文件的定位方式，并识别有效的定位方式一一匹配，直到正确为止；<br>
	 *  2.若在xml文件中查找不到该元素，则按照xpath和css的规则进行匹配，直到判断出该元素的定位方式位置；<br>
	 *  3.若仍找不到元素，则抛出UnrecognizableLocationModeException
	 *  </p>
	 * 
	 * @param name   元素名称或元素的定位方式
	 * @return 返回页面一组元素WebElement的对象
	 * @throws TimeoutException 元素在指定时间内未查找到时，抛出的异常
	 * @throws UnrecognizableLocationModeException 元素无法识别时抛出的异常
	 */
	protected List<WebElement> judgeElementModes(String name) {
		// 获取当前的等待时间，若有对单个控件设置等待时间时，此处可以将等待时间设置回原来的时间
		long time = getWaitTime();
		// 判断是否有设置对单个控件增加等待时间，若存在该控件，则将等待时间设置为该控件设置的等待时间
		if (controlWaitTime.containsKey(name)) {
			setWaitTime(controlWaitTime.get(name));
		}
				
		// 存储最终识别得到元素
		List<WebElement> elements = null;

		// 智能匹配方式：
		// 1.先对xml文件进行扫描，若存在该元素对应的标签，则读取xml文件的定位方式，并识别有效的定位方式一一匹配，直到正确为止；
		// 2.若在xml文件中查找不到该元素，则按照xpath和css的规则进行匹配，直到判断出该元素的定位方式位置
		// 3.若仍找不到元素，则抛出UnrecognizableLocationModeException
		try {
			ElementLocationType mode = readXml(name);
			// 判断readXML()方法的返回值是否为空串，若为空串，则抛出TimeoutException
			if (mode == null) {
				// 将错误信息写入到记录工具的备注中
				RecordTool.recordMark("元素查找超时或不存在，请检查xml文件中“" + name + "”对应元素的定位方式");
				throw new TimeoutException("元素查找超时或不存在，请检查xml文件中“" + name + "”对应元素的定位方式");
			} else {
				// 页面查找元素，并存储该元素
				elements = driver.findElements(xml.getBy(name, mode));
			}
		} catch (UndefinedElementException | NullPointerException | InvalidXPathException e) {
			ElementLocationType mode = readValue(driver, name);
			// 判断readValue()方法的返回值是否为空串，若为空串，则抛出UnrecognizableLocationModeException
			if (mode == null) {
				// 将错误信息写入到记录工具的备注中
				RecordTool.recordMark("元素定位方式类型无法识别：" + name);
				// 若都不匹配，则抛出异常
				throw new UnrecognizableLocationModeException("元素定位方式类型无法识别：" + name);
			} else {
				//若有返回定位方式，则根据定位方式类型，获取其元素
				switch (mode) {
				case XPATH:
					elements = driver.findElements(By.xpath(name));
					break;
				case CSS:
					elements = driver.findElements(By.cssSelector(name));
					break;
				default:
					break;
				}
			}
		}
		
		// 设置等待时间为原来的等待时间
		setWaitTime(time);
		// 返回页面元素对象
		return elements;
	}

	/**
	 * 该方法用于通过传入的控件名称，在xml文件中查找其元素相应的定位方式，并在页面上进行查找， 若在相应的时间内查找到该元素，则将其定位方式进行返回
	 * 
	 * @param name   控件名称
	 * @return 查找到的定位方式名称
	 */
	protected ElementLocationType readXml(String name) {
		// 循环，逐个在页面上配对有效的标签对应的定位方式
		for (ElementLocationType mode : xml.getElementMode(name)) {
			// 在页面上查找元素定位方式
			try {
				//自动定位元素所在的窗体
				if (isSwitchIframe()) {
					autoSwitchIframe(xml.getIframeName(name));
				}
				
				//查找元素
				wait.until(ExpectedConditions.presenceOfElementLocated(xml.getBy(name, mode)));
			} catch (TimeoutException exception) {
				// 若查找超时，则重新查找
				continue;
			}

			// 若在页面上找到该元素，则将该元素的定位方式存储
			return mode;
		}

		// 若循环结束后仍未能找到该元素，则返回一个空串
		return null;

	}
	
	/**
	 * 用于自动定位元素所在窗体
	 * @param iframeName 元素所在窗体名称
	 */
	protected void autoSwitchIframe(String iframeName) {
		//判断传入的窗体名称是否为空，若为空，说明元素在主窗体上
		if (iframeName.isEmpty()) {
			//判断当前定位是否在主窗体上（iframeNames无元素时），若不在主窗体上，则切换到主窗体，若在，则直接结束
			if (iframeNames.size() != 0) {
				//将窗体切换到顶层
				switchRootFrame();
				return;
			} else {
				return;
			} 
		}
				
		//判断当前元素所在的窗体是否与已切换的窗体列表中最后一个元素（当前窗体）的内容相同，相同则直接返回（无需操作）
		//获取最后一层窗体的名称，若iframeNames为空，则返回空串
		String nowIframeName = (iframeNames.isEmpty() ? "" : iframeNames.get(iframeNames.size() - 1));
		if (iframeName.equals(nowIframeName)) {
			return; 
		}
				
		//若传入的字符串不为空，则表示当前窗体不在主窗体上，则判断其是否在已切换的窗体列表中，若在，则通过层级返回，
		//无需再次重新定位，避免等待时间太长，若不在，则重新定位窗体
		int index = 0;
		if ((index = iframeNames.indexOf(iframeName)) > -1) {
			//循环，将窗体一层一层返回
			for(int i = 1; i < iframeNames.size() - index; i++) {
				switchParentFrame();
			}
		} else {
			//若当前元素的窗体不在当前窗体的父窗体，则重新定位窗体
			switchFrame(iframeName);
		}
	}

	/**
	 * 该方法用于在xml文件查找不到对应的元素时，则再对传入的参数进行xpath和css的比较，
	 * 以确认是否为直接传值，若不为xpath和css中的一种，则抛出UnrecognizableLocationModeException
	 * 
	 * @param driver WebDriver对象
	 * @param text 定位方式
	 * @return 定位方式的类型
	 */
	protected ElementLocationType readValue(WebDriver driver, String text) {
		// 定义判断参数为xpath的字符
		String judgeXpath = "/";

		// 定义判断参数为css的字符
		String judgeCss = "html";

		try {
			// 如果抛出元素名称查找不到的的异常，则对应匹配xpath和css两种定位方式
			// 匹配xpath定位，判定方法，判断text的第一个字符是否是“/”
			if (text.indexOf(judgeXpath) == 0) {
				// 查找该定位方式在有限的时间内是否内被查到
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(text)));
				return ElementLocationType.XPATH;
				// 将等待时间设置回原来的
			} else if (text.indexOf(judgeCss) == 0) {
				// 匹配css，判断方法：判断text的前四个字符是否是“html”
				wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(text)));
				return ElementLocationType.CSS;
			} else {
				return null;
			}
		} catch (TimeoutException exception) {
			// 将错误信息写入到记录工具的备注中
			RecordTool.recordMark("元素查找超时或不存在，请检查“" + text + "”定位方式");
			throw new TimeoutException("元素查找超时或不存在，请检查“" + text + "”定位方式");
		}
	}
	
	/**
	 * 用于将页面控件元素高亮显示
	 * @param newElement 当前指向的元素
	 */
	protected void elementHight(WebElement newElement) {
		//判断当前指向的元素是否与原来存储的元素一致，若不一致且有之前有存储元素，则解除之前元素的控件高亮
		if (oldElement != newElement && oldElement != null) {
			// 解除控件高亮，若因为页面跳转导致页面过期时，则不处理其异常
			try {
				((JavascriptExecutor) getDriver()).executeScript("arguments[0].setAttribute('style',arguments[1])",
						oldElement, oldElementStyle);
			} catch (StaleElementReferenceException exception) {
			}
		}
		 
		//获取当前指向的元素的style属性
		String newElementStyle = newElement.getAttribute("style");
		// 控件高亮
		((JavascriptExecutor) getDriver()).executeScript("arguments[0].setAttribute('style',arguments[1])", newElement,
				newElementStyle + "background:yellow;solid:red;");
		
		//将当前指向的元素及其属性存储至属性中
		oldElement = newElement;
		oldElementStyle = newElementStyle;
	}
}
