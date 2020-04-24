package pres.auxiliary.work.selenium.element;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.rules.ExpectedException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pres.auxiliary.selenium.event.NoSuchWindownException;
import pres.auxiliary.selenium.event.UnrecognizableLocationModeException;
import pres.auxiliary.selenium.xml.ElementLocationType;
import pres.auxiliary.selenium.xml.ReadXml;

public class Element {
	/**
	 * 用于存储浏览器的WebDriver对象，设为静态，保证所有的子类只使用一个WebDriver对象，以避免造成WebDriver不正确导致的Bug
	 */
	private WebDriver driver;
	/**
	 * 用于指向存储控件定位方式的xml文件
	 */
	private ReadXml xml = new ReadXml();
	/**
	 * 全局控件等待时间
	 */
	private long waitTime = 3;
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
	private ArrayList<String> iframeNameList = new ArrayList<>();
	
	/**
	 * 控制是否自动切换窗体，由于通过Event类调用时会构造另一个事件类，但每个类都应共享一个开关，故需要加上static
	 */
	boolean switchIframe = true;
	
	/**
	 * 构造对象并存储浏览器的WebDriver对象
	 * 
	 * @param driver 浏览器的WebDriver对象
	 */
	public Element(WebDriver driver) {
		this.driver = driver;
		browserHandles = this.driver.getWindowHandle();
	}
	
	/**
	 * 用于设置等待时间
	 * 
	 * @param waitTime 等待时间
	 */
	public void setWaitTime(long waitTime) {
//		wait = new WebDriverWait(driver, waitTime, 200);
	}
	
	/**
	 * 设置是否自动切换窗体
	 * @param switchIframe 是否自动切换窗体
	 */
	public void setSwitchIframe(boolean switchIframe) {
		this.switchIframe = switchIframe;
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
	 * 用于设置指向存储元素定位方式的xml文件对象，并根据传参，判断窗体是否需要回到顶层
	 * @param xmlFile 存储元素定位方式的xml文件对象
	 * @param isBreakRootFrame 是否需要将窗体切回到顶层
	 */
	public void setXmlFile(File xmlFile, boolean isBreakRootFrame) {
		xml.setXmlPath(xmlFile);
		
		if (isBreakRootFrame) {
			switchRootFrame();
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
		//切换窗口至顶层
		driver.switchTo().defaultContent();
		//清空iframeNameList中的内容
		iframeNameList.clear();
	}
	
	/**
	 * 该方法用于将窗体切换到上一层（父层）。若当前层只有一层，则调用方法后切回顶层；
	 * 若当前层为最顶层时，则该方法将使用无效
	 */
	public void switchParentFrame() {
		//若iframeNameList大于1层，则向上切换窗体
		if (iframeNameList.size() > 1) {
			driver.switchTo().parentFrame();
			iframeNameList.remove(iframeNameList.size() - 1);
		} else if (iframeNameList.size() == 1) {
			//若iframeNameList等于1层，则调用切换至顶层的方法
			switchRootFrame();
		} else {
			//若iframeNameList小于1层，则不做操作
			return;
		}
	}

	/**
	 * 通过传入在xml文件中的控件名称，到类中指向的xml文件中查找控件
	 * 名称对应的定位方式，或直接传入xpath与css定位方式，
	 * 根据定位方式对相应的窗体进行定位。当传入的窗体为当前窗体的前层（父层）窗体时，
	 * 通过该方法将调用切换父层的方法，将窗体切换到父层上，例如：<br>
	 * 当前存在f1, f2, f3, f4四层窗体，则调用方法：<br>
	 * switchFrame("f2")<br>
	 * 此时窗体将回到f2层，无需再从顶层开始向下切换。<br>
	 * 注意，窗体的切换按照从前向后的顺序进行切换，切换顺序不能相反
	 * 
	 * @param names 窗体的名称或xpath与css定位方式
	 */
	public void switchFrame(String...names) {
		switchFrame(Arrays.asList(names));
	}
	
	/**
	 * 通过传入在xml文件中的控件名称，到类中指向的xml文件中查找控件
	 * 名称对应的定位方式，或直接传入xpath与css定位方式，
	 * 根据定位方式对相应的窗体进行定位。当传入的窗体为当前窗体的前层（父层）窗体时，
	 * 通过该方法将调用切换父层的方法，将窗体切换到父层上，例如：<br>
	 * 当前存在f1, f2, f3, f4四层窗体，则调用方法：<br>
	 * List<String> nameList = new ArrayList<String>();<br>
	 * nameList.add("f2");<br>
	 * switchFrame(nameList)<br>
	 * 此时窗体将回到f2层，无需再从顶层开始向下切换。<br>
	 * 注意，窗体的切换按照从前向后的顺序进行切换，切换顺序不能相反
	 * 
	 * @param nameList 存储窗体的名称或xpath与css定位方式的List集合
	 */
	public void switchFrame(List<String> nameList) {
		nameList.forEach(name -> {
			//判断name指向的窗体是否在iframeNameList中，若存在，则向上切换父层，直到切换到name指向的窗体；若不存在，则直接切换，并添加窗体名称
			if (iframeNameList.contains(name)) {
				//获取name窗体在iframeNameList中的位置
				int index = iframeNameList.indexOf(name);
				//获取需要向上切换窗体的次数，公式为推断出来
				int count = iframeNameList.size() - index - 1;
				for (int i = 0; i < count; i++) {
					switchParentFrame();
				}
			} else {
				//切换窗体
				driver.switchTo().frame(recognitionElements(name).get(0));
				iframeNameList.add(name);
			}
		});
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
				recognitionElements(controlName).get(0);
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
	List<WebElement> recognitionElements(String name) {
		//判断元素是否存在于xml文件中，根据返回的状态，来判断调用的方法
		if (xml.isElement(name)) {
			return findXmlElement(name);
		} else {
			return findCommonElement(name, judgeElementLocationMode(name));
		}
	}

	/**
	 * 根据传入的元素定位方式，返回在页面上查找到的WebElement对象
	 * @param name 元素内容
	 * @return 
	 */
	private List<WebElement> findCommonElement(String name, By by) {
		if (isFindWebElement(controlWaitTime.containsKey(name) ? controlWaitTime.get(name) : waitTime, by)) {
			return driver.findElements(by);
		}
		
		// 若循环结束后仍未能找到该元素，则返回一个空串
		throw new TimeoutException("元素“" + name + "”无法查找，请核对定位方式：" + by.toString());
	}

	/**
	 * 根据传入的元素名称，到指定的xml文件中查找相应的有效的定位方式，并在页面中查找，返回查找到的元素对象
	 * @param name 需要查找的元素名称
	 * @return 页面中查找到的元素
	 * @throws TimeoutException 当元素不能找到时抛出的异常
	 */
	private List<WebElement> findXmlElement(String name) {
		// 循环，逐个在页面上配对有效的标签对应的定位方式
		for (ElementLocationType mode : xml.getElementMode(name)) {
			if (isFindWebElement(controlWaitTime.containsKey(name) ? controlWaitTime.get(name) : waitTime, xml.getBy(name, mode))) {
				driver.findElements(xml.getBy(name, mode));
			}
		}

		// 若循环结束后仍未能找到该元素，则返回一个空串
		throw new TimeoutException("元素“" + name + "”无法查找，请核对xml文件：" + xml.getXmlFile().getName() + "\n文件路径：" + xml.getXmlFile().getAbsolutePath());
	}
	
	/**
	 * 用于判断传入参数的定位方式，只识别xpath与css两种定位方式。需要注意的是，该方法主要用于
	 * 识别xptah，若元素的定位方式不是xpath定位，则根据css方式来返回
	 * @param text 元素定位方式
	 * @return 相应的By对象
	 */
	private By judgeElementLocationMode(String text) {
		// 定义判断参数为xpath的字符
		String judgeXpath = "/";
		
		// 如果抛出元素名称查找不到的的异常，则对应匹配xpath和css两种定位方式
		// 匹配xpath定位，判定方法，判断text的第一个字符是否是“/”
		if (text.indexOf(judgeXpath) == 0) {
			// 查找该定位方式在有限的时间内是否内被查到
			return By.xpath(text);
		} else {
			return By.cssSelector(text);
		}
	}
	
	/**
	 * 用于获取元素在xml文件中所有的父窗体，并以集合的形式返回，存储的顺序为父窗体在前，子窗体在后，若当前元素没有窗体，
	 * 则集合的长度为0
	 * @param name 元素在xml文件中的名称
	 * @return 元素在xml文件中所有的父窗体集合
	 */
	private List<String> getParentFrameName(String name) {
		//存储获取到的父层窗体名称
		List<String> nameList = new ArrayList<String>();
		
		//获取元素所在窗体的名称
		String iframeName = xml.getIframeName(name);
		//循环，判断窗体是否存在（方法返回不为空），若存在，则再将父窗体进行存储
		while(!iframeName.isEmpty()) {
			//存储窗体
			nameList.add(iframeName);
			//再以当前窗体的名称再次获取该窗体的父窗体
			iframeName = xml.getIframeName(iframeName);
		}
		
		//将nameList的内容倒序，保证父窗体在子窗体之前
		Collections.reverse(nameList);
		
		return nameList;
	}

	/**
	 * 用于将页面控件元素高亮显示
	 * @param newElement 当前指向的元素
	 */
	void elementHight(WebElement newElement) {
		//获取当前指向的元素的style属性
		String newElementStyle = newElement.getAttribute("style");
		// 控件高亮
		((JavascriptExecutor) getDriver()).executeScript("arguments[0].setAttribute('style',arguments[1])", newElement,
				newElementStyle + "background:yellow;solid:red;");
		
		//解除高亮
		((JavascriptExecutor) getDriver()).executeScript("arguments[0].setAttribute('style',arguments[1])",
				newElement, newElementStyle + "background:yellow;solid:red;");
	}
	
	/**
	 * 根据页面的等待时间和元素定位方式，在页面上查找相应的元素，返回是否能查到元素
	 * @param time 控件等待时间
	 * @param by 元素定位方式
	 * @return 是否能查找到的元素
	 */
	boolean isFindWebElement(long time, By by) {
//		new WebDriverWait(driver, time, 200).until(ExpectedConditions.elementToBeClickable(by));
		//当查找到元素时，则返回true，若查不到元素，则会抛出异常，故返回false
		try {
			new WebDriverWait(driver, time, 200).until(ExpectedConditions.presenceOfElementLocated(by));
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
