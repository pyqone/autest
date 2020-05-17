package pres.auxiliary.work.selenium.element;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

import pres.auxiliary.selenium.event.NoSuchWindownException;
import pres.auxiliary.selenium.xml.ByType;
import pres.auxiliary.selenium.xml.ReadXml;
import pres.auxiliary.work.selenium.brower.AbstractBrower;
import pres.auxiliary.work.selenium.brower.Page;

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
	ReadXml xml;
	
	/**
	 * 用于存储浏览器对象
	 */
	AbstractBrower brower;
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
	 * 用于存储元素通用的等待时间，默认5秒
	 */
	private long waitTime = 5;
	
	/**
	 * 控制是否自动切换窗体，由于通过Event类调用时会构造另一个事件类，但每个类都应共享一个开关，故需要加上static
	 */
	boolean isAutoSwitchIframe = true;
	
	/**
	 * 通过{@link WebDriver}对象进行构造
	 * 
	 * @param driver {@link WebDriver}对象
	 */
	public AbstractElement(WebDriver driver) {
		this.driver = driver;
		browserHandles = this.driver.getWindowHandle();
	}
	
	/**
	 * 通过浏览器对象{@link AbstractBrower}进行构造
	 * @param brower {@link AbstractBrower}对象
	 */
	public AbstractElement(AbstractBrower brower) {
		this.brower = brower;
		this.driver = brower.getDriver();
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
		if (xml == null) {
			xml = new ReadXml(xmlFile);
		} else {
			xml.setXmlPath(xmlFile);
		}
		
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
		switchFrame(new ElementInformation(name, null));
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
		switchFrame(new ElementInformation(name, byType));
	}
	
	/**
	 * 切换窗体的底层方法
	 * @param elementInformation 元素信息类对象
	 */
	private void switchFrame(ElementInformation elementInformation) {
		List<String> nameList = new ArrayList<String>();
		//判断传入的元素名称是否存在于xml文件中，若存在，则将该元素的父层名称存储至nameList
		if (isXmlElement(elementInformation.name) && isAutoSwitchIframe) {
			nameList.addAll(getParentFrameName(elementInformation.name));
		}
		
		//调用切换窗体的方法
		switchFrame(nameList);
		
		driver.switchTo().frame(driver.findElement(recognitionElement(elementInformation)));
		//切换窗体
		iframeNameList.add(elementInformation.name);
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
	void switchFrame(List<String> frameNameList) {
		frameNameList.forEach(frameName -> {
			//判断name指向的窗体是否在iframeNameList中，若存在，则向上切换父层，直到切换到name指向的窗体；若不存在，则直接切换，并添加窗体名称
			if (iframeNameList.contains(frameName)) {
				//获取name窗体在iframeNameList中的位置
				int index = iframeNameList.indexOf(frameName);
				//获取需要向上切换窗体的次数，公式为推断出来
				int count = iframeNameList.size() - index - 1;
				for (int i = 0; i < count; i++) {
					switchParentFrame();
				}
			} else {
				//切换窗体
				driver.switchTo().frame(driver.findElement(recognitionElement(new ElementInformation(frameName, null))));
				iframeNameList.add(frameName);
			}
		});
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
		
		List<Page> pageList = new ArrayList<>();
		//若浏览器对象存在，则将已打开的页面的handle进行存储，优先遍历新打开的标签
		if (brower != null) {
			pageList.addAll(brower.getOpenPage());
		}
		
		//移除已打开的窗口
		handles.removeAll(pageList.stream().map(page -> {
			return page.getHandle();
		}).collect(Collectors.toList()));

		// 循环，获取所有的页面handle
		for (String newWinHandle : handles) {
			//切换窗口，并查找元素是否在窗口上，若存在，则结束切换
			driver.switchTo().window(newWinHandle);
			try {
				//构造信息，因为在构造过程中会判断元素是否存在，
				recognitionElement(new ElementInformation(controlName, null));
				return;
			}catch (Exception e) {
				continue;
			}
		}
		
		//若不在新打开的窗口上，则遍历已打开的窗口
		if (brower != null) {
			for (Page page : pageList) {
				//切换窗口，并查找元素是否在窗口上，若存在，则结束切换
				brower.switchWindow(page);
				try {
					recognitionElement(new ElementInformation(controlName, null));
					return;
				}catch (Exception e) {
					continue;
				}
			}
		}
		
		//若遍历所有窗口后均未能查到元素，则抛出异常
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
	By recognitionElement(ElementInformation elementInformation) {
		By by;
		if (isXmlElement(elementInformation.name)) {
			//若指定了xml文件，且传入的元素名称存在与xml文件中，则判断元素相应的定位方式及定位内容
			by = recognitionXmlElement(elementInformation);
		} else {
			//若未指定xml文件，或者在xml文件中无法查到相应的元素时，则将name的值赋给value，且根据value判断相应定位方式
			by = recognitionCommonElement(elementInformation);
		}
		
//		return driver.findElements(by);
		return by;
	}

	/**
	 * 获取普通元素的By对象
	 * @param elementInformation 元素信息类对象
	 * @return 元素信息指向的By对象
	 */
	private By recognitionCommonElement(ElementInformation elementInformation) {
		//判断传入的ByType对象是否为null
		if (elementInformation.byType == null) {
			return judgeCommonElementBy(elementInformation.name);
		} else {
			By by = getBy(elementInformation.name, elementInformation.byType);
			if (isExistElement(by, getWaitTime(elementInformation.name)))
				return by;
			else 
				throw new TimeoutException("普通元素定位方式类型无法识别：" + by);
		}
	}

	/**
	 * 获取xml文件内元素的By对象
	 * @param elementInformation 元素信息类对象
	 * @return 元素信息指向的By对象
	 */
	private By recognitionXmlElement(ElementInformation elementInformation) {
		//判断传入的ByType对象是否为null
		if (elementInformation.byType == null) {
			return judgeXmlElementBy(elementInformation.name);
		} else {
			By by = xml.getBy(elementInformation.name, elementInformation.byType);
			if (isExistElement(by, getWaitTime(elementInformation.name)))
				return by;
			else 
				throw new TimeoutException("普通元素定位方式类型无法识别：" + by);
		}
	}
	
	/**
	 * 用于返回控件的等待时间，若设置单个控件的等待时间（使用{@link #setContorlWaitTime(String, long)}方法设置），
	 * 则返回设置的控件等待时间；若未设置单个控件的等待时间，则返回设置的通用等待时间（使用{@link #setWaitTime(long)}方法）
	 * ；若未对通用时间进行设置，则返回默认时间（{@link #waitTime}）
	 * @param name 控件名称
	 * @return 相应控件的等待时间
	 * @see #setContorlWaitTime(String, long)
	 * @see #setWaitTime(long)
	 */
	long getWaitTime(String name) {
		return controlWaitTime.containsKey(name) ? controlWaitTime.get(name) : waitTime;
	}
	
	/**
	 * 用于获取元素在xml文件中所有的父窗体，并以集合的形式返回，存储的顺序为父窗体在前，子窗体在后，若当前元素没有窗体，
	 * 则集合的长度为0
	 * @param name 元素在xml文件中的名称
	 * @return 元素在xml文件中所有的父窗体集合
	 */
	List<String> getParentFrameName(String name) {
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
	 * 用于根据传入的参数，识别非xml文件内的元素定位方式。
	 * 该方法能快速识别xpath定位方式以及绝对css定位方式，若不是以上两种定位方式
	 * 则会遍历所有的定位方式，此时会降低运行速度，建议在不是以上两种定位方式的
	 * 情况下，直接指定元素的定位方式，以提高效率
	 */
	By judgeCommonElementBy(String value) {
		// 如果抛出元素名称查找不到的的异常，则对应匹配xpath和绝对css路径两种定位方式
		// 匹配xpath定位，判定方法，判断text的第一个字符是否是“/”
		//由于是识别普通元素，非xml元素其value就是元素的名称name， 故获取等待时间时可直接将value传入
		if (value.indexOf("/") == 0) {
			//在页面中查找元素，若元素能找到，则结束查找
			By by = getBy(value, ByType.XPATH);
			if (isExistElement(by, getWaitTime(value))) {
				return by;
			}
		} else if (value.indexOf("html") == 0) {
			//在页面中查找元素，若元素能找到，则结束查找
			By by = getBy(value, ByType.CSS);
			if (isExistElement(by, getWaitTime(value))) {
				return by;
			}
		} 
		
		//若元素无法识别，则将所有的定位类型（排除xpath类型）与之进行对比，直到在页面上找到元素为止
		for(ByType type : ByType.values()) {
			if (type == ByType.XPATH) {
				continue;
			}
			
			By by = getBy(value, type);
			
			//在页面中查找元素，若元素能找到，则结束查找
			if (isExistElement(by, getWaitTime(value))) {
				return by;
			}
		}
		
		//若所有的定位方式均无法查找到元素，则抛出异常
		throw new TimeoutException("普通元素定位方式类型无法识别：" + value);
	}
	
	/**
	 * 用于设置xml文件内的元素的定位方式及定位内容
	 */
	By judgeXmlElementBy(String name) {
		By by;
		// 循环，逐个在页面上配对有效的标签对应的定位方式
		for (ByType mode : xml.getElementMode(name)) {
			by = getBy(xml.getValue(name, mode), mode);
			
			//若元素能被找到，则返回相应的By对象，若未找到，则再次循环
			if (isExistElement(by, getWaitTime(name))) {
				return by;
			} else {
				continue;
			}
		}
		
		// 若循环结束后仍未能找到该元素，则抛出异常
		throw new TimeoutException("xml文件内元素“" + name + "”无法查找，请核对xml文件：" + xml.getXmlFile().getName() + "\n文件路径：" + xml.getXmlFile().getAbsolutePath());
	}
	
	/**
	 * 根据元素的参数，返回元素的By对象
	 * @return 元素的By对象
	 */
	By getBy(String value, ByType byType) {
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
	 * 根据页面的等待时间和元素定位方式，在页面上查找相应的元素，返回是否能查到元素
	 * @param time 控件等待时间
	 * @param by 元素定位方式
	 * @return 是否能查找到的元素
	 */
	abstract boolean isExistElement(By by, long waitTime);
	
	/**
	 * 用于判断元素是否为xml文件内的元素
	 * @param name 元素名称
	 * @return 是否为xml文件内的元素
	 */
	boolean isXmlElement(String name) {
		return (xml != null && xml.isElement(name));
	}
	
	/**
	 * <p><b>文件名：</b>AbstractElement.java</p>
	 * <p><b>用途：</b>
	 * 存储获取元素时的信息
	 * </p>
	 * <p><b>编码时间：</b>2020年5月9日上午7:57:24</p>
	 * <p><b>修改时间：</b>2020年5月9日上午7:57:24</p>
	 * @author 彭宇琦
	 * @version Ver1.0
	 * @since JDK 12
	 *
	 */
	class ElementInformation {
		/**
		 * 存储元素的名称或定位内容
		 */
		public String name;
		/**
		 * 存储元素的定位方式
		 */
		public ByType byType;
		/**
		 * 初始化信息
		 * @param name 元素名称或定位内容
		 * @param byType 元素定位
		 */
		public ElementInformation(String name, ByType byType) {
			super();
			this.name = name;
			this.byType = byType;
		}
	}
}
