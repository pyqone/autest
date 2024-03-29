package com.auxiliary.selenium.element;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.auxiliary.appium.brower.AbstractCellphoneBrower;
import com.auxiliary.selenium.brower.AbstractBrower;
import com.auxiliary.selenium.brower.AbstractWebBrower;
import com.auxiliary.selenium.location.AbstractLocation;
import com.auxiliary.selenium.location.ByType;
import com.auxiliary.selenium.location.ElementLocationInfo;

import io.appium.java_client.AppiumDriver;

/**
 * <p>
 * <b>文件名：</b>AbstractElement.java
 * </p>
 * <p>
 * <b>用途：</b>
 * </p>
 * <p>
 * 对辅助化测试工具selenium的获取元素代码进行的二次封装，通过类中提供的方法以及配合相应存储元素的
 * xml文件，以更简便的方式对页面元素进行获取，减少编程时的代码量。
 * </p>
 * <p>
 * <b>编码时间：</b>2020年4月25日 下午4:18:37
 * </p>
 * <p>
 * <b>修改时间：</b>2021年4月10日下午2:53:33
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.1
 * @since JDK 1.8
 */
public abstract class FindElement {
	/**
	 * 存储通过信息获取到的页面元素对象
	 */
	protected List<WebElement> elementList;

	/**
	 * 用于存储元素信息
	 */
	protected ElementData elementData;
	/**
	 * 存储元素的定位方式读取类对象，由于多个子类之间需要互通，故此处标记为static
	 */
	protected static AbstractLocation read;

	/**
	 * 用于存储浏览器对象
	 */
	protected AbstractBrower brower;
	/**
	 * 存储当前定位的窗体层级，由于多个子类之间需要互通，故此处标记为static
	 */
	private static ArrayList<ElementData> nowIframeList = new ArrayList<>();
	/**
	 * 存储当前app上下文是否切换到原生层
	 */
	private static boolean isNative = true;

	/**
	 * 用于当元素获取失败时执行的方法
	 */
	protected ExceptionAction action;

	/**
	 * 用于存储元素通用的等待时间，默认5秒
	 */
	private long globalWaitTime = 5;

	/**
	 * 控制是否自动切换窗体，由于通过Event类调用时会构造另一个事件类
	 */
	protected boolean isAutoSwitchIframe = true;

	/**
	 * 通过元素信息类对象（{@link ElementData}）构造对象
	 * 
	 * @param brower {@link AbstractBrower}类对象
	 */
	public FindElement(AbstractBrower brower) {
		this.brower = brower;
	}

	/**
	 * 设置元素查找失败时需要执行的方法
	 * 
	 * @param action 执行方法
	 */
	public void setAction(ExceptionAction action) {
		this.action = action;
	}

	/**
	 * 用于清除元素查找失败时执行的方法
	 */
	public void clearAction() {
		this.action = null;
	}

	/**
	 * 用于设置元素的默认等待时间
	 * 
	 * @param waitTime 元素的默认等待时间
	 */
	public void setWaitTime(long waitTime) {
		this.globalWaitTime = waitTime;
	}

	/**
	 * 设置是否自动切换窗体
	 * 
	 * @param isAutoSwitchIframe 是否自动切换窗体
	 */
	public void setAutoSwitchIframe(boolean isAutoSwitchIframe) {
		this.isAutoSwitchIframe = isAutoSwitchIframe;
	}

	/**
	 * 用于设置元素定位方式文件读取类对象
	 * <p>
	 * isBreakRootFrame参数表示是否将所有的元素切回到顶层，对于web元素而言，则是将iframe切回到顶层；对于app元素
	 * 而言，则是将上下文切换至原生层
	 * </p>
	 * 
	 * @param read             元素定位方式文件读取类对象
	 * @param isBreakRootFrame 是否需要将窗体切回到顶层
	 */
	public void setReadMode(AbstractLocation read, boolean isBreakRootFrame) {
		FindElement.read = read;

		if (isBreakRootFrame) {
			switchRootFrame();

			if (brower instanceof AbstractCellphoneBrower) {
				switchAppNativeContext();
			}
		}
	}

	/**
	 * 返回当前进行搜索的元素信息类（{@link ElementData}）对象
	 * 
	 * @return 元素信息类对象
	 */
	public ElementData getElementData() {
		return elementData;
	}

	/**
	 * 该方法用于将窗体切回顶层，当本身是在最顶层时，则该方法将使用无效
	 */
	public void switchRootFrame() {
		if (elementData == null) {
			return;
		}

		// 判断元素是否是app原生元素，若是该元素，则不进行切换
		if (brower instanceof AbstractCellphoneBrower && elementData.isNativeElement()) {
			return;
		}

		// 切换窗口至顶层
		brower.getDriver().switchTo().defaultContent();
		// 清空nowIframeNameList中的内容
		nowIframeList.clear();
	}

	/**
	 * 该方法用于将窗体切换到上一层（父层）。若当前层只有一层，则调用方法后切回顶层； 若当前层为最顶层时，则该方法将使用无效
	 * 
	 * @param count 需要切换父层的次数
	 */
	public void switchParentFrame(int count) {
		// 判断元素是否是app原生元素，若是该元素，则不进行切换
		if (brower instanceof AbstractCellphoneBrower && elementData.isNativeElement()) {
			return;
		}

		// 判断count是否小于等于0，若小于等于0，则不进行切换
		if (count <= 0) {
			return;
		}

		// 判断count是否大于iframeElementList中所有元素的个数，若大于，则切回至顶层
		if (count > nowIframeList.size()) {
			switchRootFrame();
			return;
		}

		// 循环，对窗体进行切换，并移除nowIframeNameList中最后一个元素
		for (int index = 0; index < count; index++) {
			brower.getDriver().switchTo().parentFrame();
			nowIframeList.remove(nowIframeList.size() - 1);
		}
	}

	/**
	 * 该方法用于根据iframe_id来切换窗体
	 * 
	 * @param iframeElementData 窗体的信息
	 */
	public void switchFrame(ElementData iframeElementData) {
		// 判断元素是否是app原生元素，若是该元素，则不进行切换
		if (brower instanceof AbstractCellphoneBrower && elementData.isNativeElement()) {
			return;
		}

		brower.getDriver().switchTo().frame(recognitionElement(iframeElementData).get(0));
		nowIframeList.add(iframeElementData);
	}

	/**
	 * 用于切换到app的原生上下文中
	 * <p>
	 * <b>注意：</b>该方法对web端元素无效
	 * </p>
	 */
	public void switchAppNativeContext() {
		// 判断元素是否是app原生元素，若是该元素，则不进行切换
		if (brower instanceof AbstractWebBrower) {
			return;
		}

		// 切换上下文
		((AppiumDriver<?>) brower.getDriver()).context(((AbstractCellphoneBrower) brower).getNativeName());
		isNative = true;
	}

	/**
     * 用于根据写入到元素信息中的WebView上下文，对WebView上下文进行切换
     * <p>
     * <b>注意：</b>该方法对web端元素无效，若元素信息中元素上下文属性为空，则按照默认的名称进行切换
     * </p>
     * 
     * @param context 上下文内容
     */
	public void switchWebViewContext(String context) {
		// 若未指定上下文，则不进行任何操作
		if (!Optional.ofNullable(context).filter(t -> !t.isEmpty()).isPresent()) {
			return;
		}

		// 判断元素是否是app原生元素，若是该元素，则不进行切换
		if (brower instanceof AbstractWebBrower) {
			return;
		}

		// 判断元素上下文是否与原生上下文一致，若一致，则切换到顶层
		if (((AbstractCellphoneBrower) brower).getNativeName().equals(context)) {
			switchAppNativeContext();
		}

		AppiumDriver<?> appiumDriver = (AppiumDriver<?>) brower.getDriver();
		// 设置等待，直到获取到上下文为止，若获取不到上下文，则抛出异常，若成功获取，则切换上下文
		appiumDriver.context(new WebDriverWait(appiumDriver, globalWaitTime, 200).withMessage("指定上下文不存在：" + context)
				.until(driver -> {
					if (appiumDriver.getContextHandles().contains(context)) {
						return context;
					} else {
						return null;
					}
				}));
		isNative = false;
	}

    /**
     * 该方法用于自动切换app元素的上下文
     * 
     * @param elementData {@link ElementData}类对象
     */
	protected void autoSwitchContext(ElementData elementData) {
		// 获取当前是否为原生层元素
		boolean nowNativeContext = elementData.isNativeElement();

		// 判断当前元素所在上下文是否与之前元素所在的上下文一致，不一致则根据当前层进行切换
		if (isNative != nowNativeContext) {
			if (nowNativeContext) {
				switchAppNativeContext();
			} else {
				String context = elementData.getWebViewContext();
				// 若未指定webview的上下文，则按照默认上下文进行拼接
				if (context.isEmpty()) {
					context = "WEBVIEW_" + ((AbstractCellphoneBrower) brower).getAppPackage().getAppPackage();
				}

				switchWebViewContext(context);
			}
		}
	}

	/**
	 * 用于根据元素的窗体层级关系列表，对元素的窗体进行切换，并存储当前切换的窗体
	 * 
	 * @param iframeNameList 元素所有窗体名称集合
	 */
	protected void autoSwitchFrame(List<String> iframeNameList) {
		// 若传参为空，则切回到顶层
		if (iframeNameList.size() == 0) {
			// 若当前窗体也为空，则直接返回
			if (nowIframeList.size() == 0) {
				return;
			}

			// 当前窗体不为空时，则切换到顶层
			switchRootFrame();
			return;
		}

		// 判断nowIframeNameList是否为空，或iframeNameList的第一个元素是否存在于nowIframeNameList中，
		// 若不存在，则切回到顶层，并根据iframeList切换窗体
		ElementData firstIframeElementData = new ElementData(iframeNameList.get(0), read);
		if (nowIframeList.size() == 0 || nowIframeList.contains(firstIframeElementData)) {
			switchRootFrame();
			for (String iframeName : iframeNameList) {
				switchFrame(new ElementData(iframeName, read));
			}
			return;
		}

		// 判断iframeElementList中的最后一个元素是否与iframeList中的最后一个元素一致，若一致，则无需切换
		ElementData lastIframeElementData = new ElementData(iframeNameList.get(iframeNameList.size() - 1), read);
		if (nowIframeList.get(nowIframeList.size() - 1).equals(lastIframeElementData)) {
			return;
		}

		// 遍历iframeList，查看iframeList中的窗体元素是否存在于iframeElementList中
		int index = 0;
		for (; index < iframeNameList.size(); index++) {
			if (!nowIframeList.contains(new ElementData(iframeNameList.get(index), read))) {
				break;
			}
		}

		// 计算需要向上切换窗体的个数
		// 当窗体不存在于iframeElementList中时，其index以比存在的窗体下标大1位，故
		// index可直接用于计算需要切换窗体的个数
		switchParentFrame(nowIframeList.size() - index);

		// 切换到相应的父层后，再根据iframeList剩余的元素进行切换
		for (; index < iframeNameList.size(); index++) {
			switchFrame(new ElementData(iframeNameList.get(index), read));
		}
	}

	/**
	 * 根据元素的参数，返回元素的By对象
	 * 
	 * @return 元素的By对象
	 */
	protected By getBy(String value, ByType byType) {
		// 根据元素的定位方式，对定位内容进行选择，返回相应的By对象
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
	 * 用于根据元素信息，在页面上对数据进行查找
	 * 
	 * @param elementData {@link ElementData}类对象
	 * @return {@link WebElement}类对象{@link List}集合
	 */
	protected List<WebElement> recognitionElement(ElementData elementData) {
		// 判断是否需要自动切换窗体，若需要，则对元素窗体进行切换
		if (isAutoSwitchIframe) {
			if (brower instanceof AbstractCellphoneBrower) {
				autoSwitchContext(elementData);
			}

			autoSwitchFrame(elementData.getIframeNameList());
		}

		// 获取元素的定位类型及定位内容
		ArrayList<ElementLocationInfo> locationList = elementData.getLocationList();

		List<WebElement> elementList = findElement(locationList,
				elementData.getWaitTime() == -1 ? globalWaitTime : elementData.getWaitTime());

		// 若获取到的元素列表为空，则继续循环，调用下一个定位方式
		if (elementList.size() != 0) {
			return elementList;
		}

		throw new TimeoutException("页面上无相应定位方式的元素，元素名称：" + elementData.getName());
	}

	/**
	 * 根据传入的定位方式枚举，以及定位内容，在页面查找 元素，返回查到的元素列表，若查不到元素，则返回空列表
	 * 
	 * @param locationList 元素定位信息类{@link ElementLocationInfo}对象
	 * @param waitTime     元素查找超时时间
	 * @return 页面查找到的{@link WebElement}类对象{@link List}集合
	 */
	protected List<WebElement> findElement(List<ElementLocationInfo> locationList, long waitTime) {
		try {
			return new WebDriverWait(brower.getDriver(), waitTime, 200).until(driver -> {
				// 遍历所有的定位方式与定位内容
				for (int i = 0; i < locationList.size(); i++) {
					try {
						// 页面查找元素
						ElementLocationInfo einfo = locationList.get(i);
						List<WebElement> webElementList = driver
								.findElements(getBy(einfo.getLocationText(), einfo.getByType()));
						// 若元素组为空或者获取的元素为空，则重新循环；反之，则返回找到元素
						if (webElementList != null && webElementList.size() != 0) {
							return webElementList;
						} else {
							continue;
						}
					} catch (Exception e) {
						// 若抛出异常，则重新循环
						continue;
					}
				}

				// 若循环完毕仍未找到元素，则返回null
				return null;
			});
		} catch (TimeoutException e) {
			return new ArrayList<>();
		}
	}

	/**
	 * 由于方法允许传入负数和特殊数字0为下标，并且下标的序号由1开始，故可通过该方法对下标的含义进行转义，得到java能识别的下标
	 * 
	 * @param length 元素的个数
	 * @param index  传入的下标
	 * @return 可识别的下标
	 * @throws NoSuchElementException 当元素无法查找到时抛出的异常
	 */
	protected int toElementIndex(int length, int index, boolean firstEmpty) {
		// 判断元素下标是否超出范围，由于可以传入负数，故需要使用绝对值
		if (Math.abs(index) > length) {
			return -1;
		}

		// 判断index的值，若大于0，则从前向后遍历，若小于0，则从后往前遍历，若等于0，则随机输入
		if (index > 0) {
			// 选择元素，正数的选项值从1开始，故需要减小1
			return index - 1;
		} else if (index < 0) {
			// 选择元素，由于index为负数，则长度加上选项值即可得到需要选择的选项
			return length + index;
		} else {
			Random ran = new Random();
			return firstEmpty ? (ran.nextInt(length - 1) + 1) : ran.nextInt(length);
		}
	}
}
