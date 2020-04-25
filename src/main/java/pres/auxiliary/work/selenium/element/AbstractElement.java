package pres.auxiliary.work.selenium.element;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pres.auxiliary.selenium.event.NoSuchWindownException;
import pres.auxiliary.selenium.xml.ByType;
import pres.auxiliary.selenium.xml.ReadXml;

/**
 * <p><b>文件名：</b>AbstractElement.java</p>
 * <p><b>用途：</b></p>
 * <p><pre>
 *   对辅助化测试工具selenium的获取元素代码进行的二次封装，通过类中提供的方法以及配合相应存储元素的
 * xml文件，以更简便的方式对页面元素进行获取，减少编程时的代码量。
 * </pre></p>
 * <p><b>编码时间：</b>2020年4月25日 下午4:18:37</p>
 * <p><b>修改时间：</b>2020年4月25日 下午4:18:37</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 */
public abstract class AbstractElement {
	/**
	 * 用于存储浏览器的WebDriver对象，设为静态，保证所有的子类只使用一个WebDriver对象，以避免造成WebDriver不正确导致的Bug
	 */
	WebDriver driver;
	/**
	 * 用于指向存储控件定位方式的xml文件
	 */
	ReadXml xml = new ReadXml();
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
	 * 用于存储元素通用的等待时间
	 */
	private long waitTime = 5;
	
	/**
	 * 控制是否自动切换窗体，由于通过Event类调用时会构造另一个事件类，但每个类都应共享一个开关，故需要加上static
	 */
	boolean isAutoSwitchIframe = true;
	
	/**
	 * 构造对象并存储浏览器的WebDriver对象
	 * 
	 * @param driver 浏览器的WebDriver对象
	 */
	public AbstractElement(WebDriver driver) {
		this.driver = driver;
		browserHandles = this.driver.getWindowHandle();
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
	 * 用于设置控件的通用等待时间
	 * @param waitTime 控件通用的等待时间
	 */
	public void setWaitTime(long waitTime) {
		this.waitTime = waitTime;
	}
	
	/**
	 * 设置是否自动切换窗体
	 * @param switchIframe 是否自动切换窗体
	 */
	public void setAutoSwitchIframe(boolean isAutoSwitchIframe) {
		this.isAutoSwitchIframe = isAutoSwitchIframe;
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
	 * <p>
	 * 通过传入在xml文件中的控件名称，到类中指向的xml文件中查找控件
	 * 名称对应的定位方式，或直接传入xpath与css定位方式，
	 * 根据定位方式对相应的窗体进行定位。当传入的窗体为当前窗体的前层（父层）窗体时，
	 * 通过该方法将调用切换父层的方法，将窗体切换到父层上，例如：<br>
	 * 当前存在f1, f2, f3, f4四层窗体，则调用方法：<br>{@code
	 * switchFrame("f2");
	 * }<br>
	 * 此时窗体将回到f2层，无需再从顶层开始向下切换。
	 * </p>
	 * <p>
	 * 若传入该方法的名称存在于xml文件中，且该元素存在父窗体时，调用
	 * 该方法会从xml文件中获取相应所有父窗体，并对相应的父窗体进行切换，
	 * 从而达到无须切换父窗体的目的，例如，存在以下xml文件片段：<pre>{@code
	 * ...
	 * <iframe name='f1'>
	 * 	<xpath>...</xpath>
	 * 	<iframe name='f2'>
	 * 		<xpath>...</xpath>
	 * 		<iframe name='f3'>
	 * 			<xpath>...</xpath>
	 * 		</iframe>
	 * 	</iframe>
	 * </iframe>
	 * ...
	 * }</pre>
	 * 当调用该方法：<br>{@code
	 * switchFrame("f3");
	 * }<br>
	 * 时，则会先将窗体从f1开始切换，至窗体f2，最后再切换为窗体f3
	 * </p>
	 * 
	 * @param name 窗体的名称或xpath与css定位方式
	 */
	public void switchFrame(String name) {
		switchFrame(name, null);
	}
	
	/**
	 * 通过传入在xml文件中的控件名称，到类中指向的xml文件中查找控件
	 * 名称对应的定位方式，或直接传入xpath与css定位方式，
	 * 根据定位方式对相应的窗体进行定位。通过该方法可以指定使用的定位类型，一般用于
	 * 传入非xml文件的元素，也可用于指定xml文件元素的定位方式，其他说明参见{@link #switchFrame(String)}
	 * @param name 窗体的名称或xpath与css定位方式
	 * @param byType 元素的定位方式
	 * @see #switchFrame(String)
	 */
	public void switchFrame(String name, ByType byType) {
		List<ElementInformation> nameList = new ArrayList<ElementInformation>();
		//判断传入的元素名称是否存在于xml文件中，若存在，则将该元素的父层名称存储至nameList
		if (xml != null && xml.isElement(name) && isAutoSwitchIframe) {
			nameList.addAll(getParentFrameName(name));
		}
		
		//将相应的元素存入nameList中
		nameList.add(new ElementInformation(name, byType, controlWaitTime.containsKey(name) ? controlWaitTime.get(name) : waitTime));
		//调用
		switchFrame(nameList);
	}
	
	/**
	 * 通过传入在xml文件中的控件名称，到类中指向的xml文件中查找控件
	 * 名称对应的定位方式，或直接传入xpath与css定位方式，
	 * 根据定位方式对相应的窗体进行定位。当传入的窗体为当前窗体的前层（父层）窗体时，
	 * 通过该方法将调用切换父层的方法，将窗体切换到父层上，例如：<br>
	 * 当前存在f1, f2, f3, f4四层窗体，则调用方法：<pre>{@code
	 * List<String> nameList = new ArrayList<String>();
	 * nameList.add("f2");
	 * switchFrame(nameList);
	 * }</pre>
	 * 此时窗体将回到f2层，无需再从顶层开始向下切换。<br>
	 * <b>注意：</b>
	 * <ol>
	 * <li>窗体的切换按照从前向后的顺序进行切换，切换顺序不能相反</li>
	 * <li>传入的参数若在xml文件中且存在父窗体，调用该方法也不会对窗体进行切换</li>
	 * </ol>
	 * 
	 * @param elementInformationList 存储窗体的名称或xpath与css定位方式的List集合
	 */
	private void switchFrame(List<ElementInformation> elementInformationList) {
		elementInformationList.forEach(elementInformation -> {
			//判断name指向的窗体是否在iframeNameList中，若存在，则向上切换父层，直到切换到name指向的窗体；若不存在，则直接切换，并添加窗体名称
			if (iframeNameList.contains(elementInformation.name)) {
				//获取name窗体在iframeNameList中的位置
				int index = iframeNameList.indexOf(elementInformation.name);
				//获取需要向上切换窗体的次数，公式为推断出来
				int count = iframeNameList.size() - index - 1;
				for (int i = 0; i < count; i++) {
					switchParentFrame();
				}
			} else {
				//切换窗体
				driver.switchTo().frame(recognitionElements(elementInformation).get(0));
				iframeNameList.add(elementInformation.name);
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
	List<WebElement> recognitionElements(ElementInformation element) {
		//判断元素是否存在于xml文件中，根据返回的状态，来判断调用的方法
		if (xml.isElement(element.name)) {
			return findXmlElement(element);
		} else {
			return findCommonElement(element);
		}
	}

	/**
	 * 根据传入的元素定位方式，返回在页面上查找到的WebElement对象
	 * @param name 元素内容
	 * @return 
	 */
	private List<WebElement> findCommonElement(ElementInformation element) {
		//若未对元素的定位方式进行存储，则调用方法，对元素的定位方式进行识别
		if (element.byType == null) {
			element.byType = judgeElementLocationMode(element.value);
		}
		
		if (isExistElement(element)) {
			return driver.findElements(getBy(element.byType, element.value));
		}
		
		// 若循环结束后仍未能找到该元素，则返回一个空串
		throw new TimeoutException("元素“" + element.name + "”无法查找，请核对定位方式：" + element.byType.getValue() + "=" + element.value);
	}

	/**
	 * 根据传入的元素名称，到指定的xml文件中查找相应的有效的定位方式，并在页面中查找，返回查找到的元素对象
	 * @param name 需要查找的元素名称
	 * @return 页面中查找到的元素
	 * @throws TimeoutException 当元素不能找到时抛出的异常
	 */
	private List<WebElement> findXmlElement(ElementInformation element) {
		//若元素的定位方式未被存储，则调用方法对元素的定位方式进行获取
		if (element.byType == null || element.value.isEmpty()) {
			// 循环，逐个在页面上配对有效的标签对应的定位方式
			for (ByType mode : xml.getElementMode(element.name)) {
				//存储当前查询到的元素信息
				element.byType = mode;
				element.value = xml.getValue(element.name, mode);
				//若元素能被找到，则结束循环
				if (isExistElement(element)) {
					break;
				}
			}
	 
			// 若循环结束后仍未能找到该元素，则抛出异常
			throw new TimeoutException("元素“" + element.name + "”无法查找，请核对xml文件：" + xml.getXmlFile().getName() + "\n文件路径：" + xml.getXmlFile().getAbsolutePath());
		}
		
		return driver.findElements(getBy(element.byType, element.value));
	}
	
	/**
	 * 用于判断传入参数的定位方式，只识别xpath路径与绝对的css路径两种定位方式。
	 * 
	 * @param text 元素定位方式
	 * @return {@link ByType}枚举
	 */
	private ByType judgeElementLocationMode(String text) {
		// 如果抛出元素名称查找不到的的异常，则对应匹配xpath和绝对css路径两种定位方式
		// 匹配xpath定位，判定方法，判断text的第一个字符是否是“/”
		if (text.indexOf("/") == 0) {
			// 查找该定位方式在有限的时间内是否内被查到
			return ByType.XPATH;
		} else if (text.indexOf("html") == 0) {
			return ByType.CSS;
		} else {
			throw new UnrecognizableLocationModeException("元素定位方式类型无法识别：" + text);
		}
	}
	
	/**
	 * 根据页面的等待时间和元素定位方式，在页面上查找相应的元素，返回是否能查到元素
	 * @param time 控件等待时间
	 * @param by 元素定位方式
	 * @return 是否能查找到的元素
	 */
	boolean isExistElement(ElementInformation element) {
//		new WebDriverWait(driver, time, 200).until(ExpectedConditions.elementToBeClickable(by));
		By by = getBy(element.byType, element.value);
		
		//当查找到元素时，则返回true，若查不到元素，则会抛出异常，故返回false
		try {
			new WebDriverWait(driver, element.waitTime, 200).until(ExpectedConditions.presenceOfElementLocated(by));
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	By getBy(ByType byType, String value) {
		//根据元素的定位方式，对定位内容进行选择，返回相应的By对象
		switch (byType) {
		case XPATH: 
			return By.xpath(value);
		case CLASSNAME:
			return By.className(value);
		case CSS:
			return By.cssSelector(value);
		case ID:
			return By.id(value);
		case LINKTEXT:
			return By.linkText(value);
		case NAME:
			return By.name(value);
		case TAGNAME:
			return By.tagName(value);
		default:
			throw new UnrecognizableLocationModeException("无法识别的定位类型：" + byType);
		}
	}
	
	/**
	 * 用于获取元素在xml文件中所有的父窗体，并以集合的形式返回，存储的顺序为父窗体在前，子窗体在后，若当前元素没有窗体，
	 * 则集合的长度为0
	 * @param name 元素在xml文件中的名称
	 * @return 元素在xml文件中所有的父窗体集合
	 */
	List<ElementInformation> getParentFrameName(String name) {
		//存储获取到的父层窗体名称
		List<ElementInformation> nameList = new ArrayList<ElementInformation>();
		
		//获取元素所在窗体的名称
		String iframeName = xml.getIframeName(name);
		//循环，判断窗体是否存在（方法返回不为空），若存在，则再将父窗体进行存储
		while(!iframeName.isEmpty()) {
			//存储窗体
			nameList.add(new ElementInformation(iframeName, controlWaitTime.containsKey(iframeName) ? controlWaitTime.get(iframeName) : waitTime));
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
	 * <p><b>文件名：</b>AbstractElement.java</p>
	 * <p><b>用途：</b>用于</p>
	 * <p><b>编码时间：</b>2020年4月25日 下午5:43:59</p>
	 * <p><b>修改时间：</b>2020年4月25日 下午5:43:59</p>
	 * @author 彭宇琦
	 * @version Ver1.0
	 * @since JDK 12
	 */
	class ElementInformation {
		/**
		 * 用于存储元素的名称
		 */
		private String name;
		
		/**
		 * 用于存储元素的定位内容
		 */
		private String value = "";
		
		/**
		 * 用于存储元素的定位方式
		 */
		private ByType byType;
		
		/**
		 * 用于存储在页面上查找的等待时间
		 */
		private long waitTime;
		
		/**
		 * 用于初始化元素的信息
		 * @param name 元素在xml文件的名称，或xpath路径，或绝对的css路径
		 * @param waitTime 元素控件的等待时间
		 */
		public ElementInformation(String name, long waitTime) {
			this.name = name;
			this.waitTime = waitTime;
			
			//若未指定xml文件或xml文件中不存在name对应的元素，则将name的值赋给value，表示
			if (xml == null || !xml.isElement(name)) {
				value = name;
			}
		}
		
		/**
		 * 用于初始化元素的信息
		 * @param name 元素在xml文件的名称，或xpath路径，或绝对的css路径
		 * @param waitTime 元素控件的等待时间
		 */
		public ElementInformation(String name, ByType byType, long waitTime) {
			this.name = name;
			this.byType = byType;
			this.waitTime = waitTime;
			
			//若未指定xml文件，则将name的值赋给value
			if (xml == null) {
				value = name;
			}
			//xml文件中不存在name对应的元素，则将name的值赋给value；反之，则在xml文件中查找对应的定位内容
			if (!xml.isElement(name)) {
				value = name;
			} else {
				value = xml.getValue(name, byType);
			}
		}

		/**
		 * 用于返回元素的名称，或定位方式
		 * @return 元素的名称
		 */
		public String getName() {
			return name;
		}

		/**
		 * 用于返回元素的等待时间
		 * @return
		 */
		public long getWaitTime() {
			return waitTime;
		}
		
		/**
		 * 用于返回定位元素的内容，当未对元素进行查找时，返回空串
		 * @return 定位元素的内容
		 */
		public String getValue() {
			return value;
		}
	}
}