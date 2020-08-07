package pres.auxiliary.work.selenium.element.delect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import pres.auxiliary.work.selenium.brower.AbstractBrower;

/**
 * <p><b>文件名：</b>Element.java</p>
 * <p><b>用途：</b>
 * 用于返回和查找页面元素，以方便在元素过期时能进行重新获取
 * </p>
 * <p><b>编码时间：</b>2020年5月18日上午8:39:05</p>
 * <p><b>修改时间：</b>2020年5月18日上午8:39:05</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 8
 *
 */
public class Element implements Cloneable {
	/**
	 * 用于对文本进行分隔
	 */
	private final String SPILT_SIGN = ",";
	
	/**
	 * 存储元素
	 */
	private WebElement element = null;
	/**
	 * 存储查找元素的By对象
	 */
	private List<By> byList;
	/**
	 * 用于存储窗体元素
	 */
	private Element iframeElement = null;
	/**
	 * 用于存储当前已指向的窗体
	 */
	private static ArrayList<Element> iframeElementList = new ArrayList<>();
	/**
	 * 存储获取需要获取的元素下标
	 */
	private String elementIndex = "0";
	
	/**
	 * 存储WebDriver对象，以查找相应元素
	 */
	private WebDriver driver;
	/**
	 * 用于存储当前打开的浏览器对象
	 */
//	private AbstractBrower brower;
	/**
	 * 用于存储元素的名称
	 */
	private String name;
	/**
	 * 标记元素的类型，以用于重新获取时
	 */
	private ElementType elementType;
	
	/**
	 * 表示当前通过有效By对象能获取到的元素个数
	 */
	private int size = -1;
	
	/**
	 * 用于存储元素查找等待时间，默认3秒
	 */
	private long waitTime = 3;
	
	/**
	 * 用于指向是否需要重新获取元素
	 */
	private boolean isReplaceFind = false;
	
	/**
	 * 控制是否允许在随机时出现第一个元素
	 */
	private boolean isRandomZero = true;
	
	/**
	 * TODO 确定构造后添加注释
	 */
	public Element(AbstractBrower brower, String name, ElementType elementType) {
		super();
		//存储Driver对象
		this.driver = brower.getDriver();
		this.name = name;
		this.elementType = elementType;
	}
	
	/**
	 * TODO 确定构造后添加注释
	 */
	public Element(WebDriver driver, String name, ElementType elementType) {
		super();
		//存储Driver对象
		this.driver = driver;
		this.name = name;
		this.elementType = elementType;
	}

	/**
	 * 用于根据存储的元素信息，在相应的页面中查找，并返回一个{@link WebElement}对象
	 * @return {@link WebElement}对象
	 */
	public WebElement getWebElement() {
		//切换窗体
		autoSwitchIframe();
		
		//若element为null，则对元素进行一次查找
		if (element == null || isReplaceFind || isPageRefresh()) {
			findElement();
		}
		
		return element;
	}
	
	/**
	 * 返回当前能获取到的元素组的个数，若未获取元素或元素获取失败，则返回-1
	 * @return 获取到的元素组的个数
	 */
	public int getSize() {
		return size;
	}

	/**
	 * 用于设置元素定位的{@link By}对象集合，调用该方法后将清空原存储的内容，重新添加{@link By}对象集合
	 * @param byList {@link By}对象集合
	 */
	public void setByList(List<By> byList) {
		this.byList = byList;
		isReplaceFind = true;
	}
	
	/**
	 * 用于设置元素的父层窗体
	 * @param iframeElement 元素的父层窗体
	 */
	public void setIframeElement(Element iframeElement) {
		this.iframeElement = iframeElement;
		isReplaceFind = true;
	}

	/**
	 * 用于返回元素所在的窗体元素对象
	 * @return 元素所在的窗体元素对象
	 */
	public Element getIframeElement() {
		return iframeElement;
	}
	
	/**
	 * 用于返回当前存储的{@link WebDriver}对象
	 * @return {@link WebDriver}对象
	 */
	public WebDriver getDriver() {
		return driver;
	}

	/**
	 * 用于返回元素名称
	 * @return 元素名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 用于设置元素查询等待时间，该时间为元素每个{@link By}对象在页面查询的时间。注意，若添加
	 * 过多无效的{@link By}对象，会导致查询等待时间过长
	 * @param waitTime 元素查询等待时间
	 */
	public void setWaitTime(long waitTime) {
		this.waitTime = waitTime;
	}

	/**
	 * 用于以数字方式设置元素的下标
	 * @param elementIndex 元素下标
	 */
	public void setElementIndex(int elementIndex) {
		this.elementIndex = String.valueOf(elementIndex);
		isReplaceFind = true;
	}
	
	/**
	 * 用于以文本方式设置元素下标，该下标用于以文本的形式查找元素
	 * @param elementIndex 元素下标
	 */
	public void setElementIndex(String... elementIndexs) {
		//拼接词语
		this.elementIndex = "";
		for (String elementIndex : elementIndexs) {
			this.elementIndex += elementIndex;
		}
		isReplaceFind = true;
	}

	/**
	 * 用于设置是否允许随机出元素组的第一个元素，默认为允许
	 * @param isRandomZero 是否允许随机第一个元素
	 */
	public void setRandomZero(boolean isRandomZero) {
		this.isRandomZero = isRandomZero;
	}

	/**
	 * 用于返回元素所在的所有父窗体
	 * @return 父窗体集合
	 */
	public ArrayList<Element> getAllIframe() {
		//存储当前元素的所在的所有窗体
		ArrayList<Element> iframeList = new ArrayList<>();
		
		//用于指向元素所在的窗体
		Element iframe = iframeElement;
		//若窗体元素不为null，则循环
		while(iframe != null) {
			//存储窗体元素
			iframeList.add(iframe);
			//向下探寻是否存在下一级窗体元素
			iframe = iframe.getIframeElement();
		}
		
		//若iframeList中未存储元素，则不进行倒叙
		if (iframeList.size() != 0) {
			//由于存储顺序为子窗体在前，不符合切换窗体的顺序，故需要对集合进行倒叙操作
			Collections.reverse(iframeList);
		}
		
		return iframeList;
	}
	
	/**
	 * 用于将窗体切换至顶层
	 */
	public void switchRootIframe() {
		//切换窗体至顶层
		driver.switchTo().defaultContent();
		//将iframeElementList中的内容清空
		iframeElementList.clear();
	}
	
	/**
	 * 用于向下切换一层窗体
	 * @param iframeElement 窗体元素类对象
	 */
	public void switchIframe(Element iframeElement) {
		//切换窗体
		driver.switchTo().frame(iframeElement.findIframeElement());
		//存储窗体切换记录
		iframeElementList.add(iframeElement);
	}
	
	/**
	 * 用于向上切换指定数量的窗体，若传入的数量大于当前窗体的数量，则将窗体切换至顶层；若传入的数量小于
	 * 等于0，则不进行切换
	 * @param count 需要向上切换窗体的数量
	 */
	public void switchParentIframe(int count) {
		//判断count是否小于等于0，若小于等于0，则不进行切换
		if (count <= 0) {
			return;
		}
		
		//判断count是否大于iframeElementList中所有元素的个数，若大于，则切回至顶层
		if (count > iframeElementList.size()) {
			switchRootIframe();
			return;
		}
		
		//循环，对窗体进行切换
		for (int i = 0; i < count; i++) {
			//向上切换窗体
			driver.switchTo().parentFrame();
			//移除iframeElementList中最后一个元素
			iframeElementList.remove(iframeElementList.size() - 1);
		}
	}
	
	/**
	 * 用于根据By对象集合，查找有效的By对象，并根据有效的By对象，查找相应的元素集合
	 */
	public void findElement() {
		//遍历By集合，查找有效的By对象，若所有的By对象均无法查到页面元素，则直接抛出超时异常
		for (int i = 0; i < byList.size(); i++) {
			//查找元素，若抛出异常，则移除该元素，保证有效的By对象在第一个元素上
			try {
				if (isExistElement(byList.get(i))) {
					break;
				} else {
					byList.remove(i);
					i--;
				}
			} catch (Exception e) {
				byList.remove(i);
				i--;
			}
		}
		
		//若byList被清空，则抛出查找超时的异常
		if (byList.size() == 0) {
			throw new TimeoutException("页面无法查找到相应的元素，“" + name + "”元素无有效的By对象");
		}
		
		//获取元素
		List<WebElement> elementList = getWebElementList();
		
		//记录元素个数
		size = elementList.size();
		
		//转换下标，若下标能转换成数字，则以数字方式获取下标，若不能则以文本的形式转换下标，根据下标返回元素
		try {
			element = elementList.get(getIndex(elementList, Integer.valueOf(elementIndex)));
		} catch (NumberFormatException e) {
			element = elementList.get(getIndex(elementList, elementIndex));
		}
	}
	
	private List<WebElement> getWebElementList() {
		//根据正确的By对象，通过Webdriver，查找到WebElement对象，存储
		//由于在遍历byList时，无效的By对象被剔除，查找到By对象时会返回，故此时可直接使用第一个元素为有效的By对象
		switch (elementType) {
		case COMMON_ELEMENT:
		case DATA_LIST_ELEMENT:
			return driver.findElements(byList.get(0));
		case SELECT_ELEMENT:
			//判断第一个元素的TagName是否为select，若是select，则按照下拉选项进行获取
			if (driver.findElement(byList.get(0)).getTagName().equals("select")) {
				return new Select(driver.findElement(byList.get(0))).getOptions();
			} else {
				return driver.findElements(byList.get(0));
			}
		default:
			throw new IllegalArgumentException("Unexpected value: " + elementType);
		}
	}

	/**
	 * 用于自动切换元素所在窗体
	 */
	private void autoSwitchIframe() {
		//获取元素所在的所有窗体，并存储
		ArrayList<Element> iframeList = getAllIframe();
		
		//判断iframeList中是否存在元素，若不存在，则切回顶层后结束运行
		if (iframeList.isEmpty()) {
			switchRootIframe();
			return;
		}
		
		//判断iframeElementList是否为空，或iframeList的第一个元素是否存在于iframeElementList中，若不存在，则切回到顶层，并根据iframeList切换窗体
		if (iframeElementList.size() == 0 || !iframeElementList.contains(iframeList.get(0))) {
			switchRootIframe();
			iframeList.forEach(iframeElement -> {
				switchIframe(iframeElement);
			});
			
			return;
		}
		
		//判断iframeElementList中的最后一个元素是否与iframeList中的最后一个元素一致，若一致，则无需切换
		if (iframeElementList.get(iframeElementList.size() - 1).equals(iframeList.get(iframeList.size() - 1))) {
			return;
		}
		
		//遍历iframeList，查看iframeList中的窗体元素是否存在于iframeElementList中
		int index = 0;
		for (; index < iframeList.size(); index++) {
			//若当前遍历的元素不存在于iframeElementList中，则结束循环
			if (!iframeElementList.contains(iframeList.get(index))) {
				break;
			}
		}
		//计算需要向上切换窗体的个数
		//当窗体不存在于iframeElementList中时，其index以比存在的窗体下标大1位，故
		//index可直接用于计算需要切换窗体的个数
		switchParentIframe(iframeElementList.size() - index);
		
		//切换到相应的父层后，再根据iframeList剩余的元素进行切换
		for (; index < iframeList.size(); index++) {
			switchIframe(iframeList.get(index));
		}
	}
	
	/**
	 * 用于返回窗体{@link WebElement}元素
	 * @return 窗体{@link WebElement}元素
	 */
	private WebElement findIframeElement() {
		//若elementList为null或其内无元素，则对元素进行一次查找
		if (element == null) {
			findElement();
		}
		
		//返回相应的内容
		return element;
	}
	
	/**
	 * 判断根据传入的{@link By}对象，判断是否能查找到元素
	 * @param by {@link By}对象
	 * @return 是否查找到元素
	 */
	boolean isExistElement(By by) {
		//当查找到元素时，则返回true，若查不到元素，则会抛出异常，故返回false
		return new WebDriverWait(driver, waitTime, 200).
				until((driver) -> {
					WebElement element = driver.findElement(by);
					return element != null;
				});
	}
	
	/**
	 * 用于判断页面是否过期（刷新等）
	 * @return 页面是否刷新
	 */
	boolean isPageRefresh() {
		//若页面抛出StaleElementReferenceException异常时，则表示页面已被更改
		try {
			element.getTagName();
			return false;
		} catch (StaleElementReferenceException e) {
			return true;
		}
	}
	
	/**
	 * 由于方法允许传入负数和特殊数字0为下标，并且下标的序号由1开始，
	 * 故可通过该方法对下标的含义进行转义，得到java能识别的下标
	 * @param index 传入的下标
	 * @param randomZero 标记是否可以随机出数字0
	 * @return 可识别的下标
	 * @throws NoSuchElementException 当元素无法查找到时抛出的异常
	 */
	int getIndex(List<WebElement> elementList, int index) {
		//存储传入的元素组长度
		int size = elementList.size();
		
		//若当前元素组中只有一个元素，则直接返回0
		if (size == 1) {
			return 0;
		}
		
		//判断元素下标是否超出范围，由于可以传入负数，故需要使用绝对值
		if (Math.abs(index) > size) {
			throw new NoSuchElementException("指定的选项值大于选项的最大值。选项总个数：" + size + "，指定项：" + index);
		}
		
		//判断index的值，若大于0，则从前向后遍历，若小于0，则从后往前遍历，若等于0，则随机输入
		if (index > 0) {
			//选择元素，正数的选项值从1开始，故需要减小1
			return index - 1;
		} else if (index < 0) {
			//选择元素，由于index为负数，则长度加上选项值即可得到需要选择的选项
			return size + index;
		} else {
			//为0，则进行随机选择，但需要判断是否允许随机出0（第一个元素）
			int newindex = 0;
			do {
				newindex = new Random().nextInt(size);
			} while(newindex == 0 && !isRandomZero);
			
			return newindex;
		}
	}
	
	/**
	 * 以文本形式查找元素，返回文本对应元素的下标
	 * @param elementList 元素组
	 * @param text 需要查找的文本
	 * @return 文本对应元素的下标
	 */
	int getIndex(List<WebElement> elementList, String text) {
		//切分文本
		String[] keys = text.split(SPILT_SIGN);
		//遍历元素组，若存在文本为所传文本的元素，则进行返回
		for (int index = 0; index < getSize(); index++) {
			//遍历词语，将词语与元素的文本一一比对
			for(String key : keys) {
				//若存在不包含在元素文本的词语，则结束当前循环，查找下一个
				if (elementList.get(index).getText().indexOf(key) < 0) {
					break;
				}
				
				//若所有的词语均包含在文本中，则直接返回当前下标
				return index;
			}
		}
		
		//若所有词语均不包含文本，则抛出异常
		throw new NoSuchElementException("无法找到指定文本的元素：" + text);
	}
	
	@Override
	public Element clone() {
		try {
			return (Element)super.clone();
		} catch (CloneNotSupportedException e) {
			return this;
		}
	}
}
