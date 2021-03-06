package pres.auxiliary.work.selenium.element;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

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
public class Element {
	/**
	 * 存储元素
	 */
	private WebElement element = null;
	/**
	 * 存储查找元素的By对象
	 */
	private By by;
	/**
	 * 存储获取需要获取的元素下标
	 */
	private int index;
	
	/**
	 * 存储WebDriver对象，以查找相应元素
	 */
	private WebDriver driver;
	
	/**
	 * 用于存储元素的名称
	 */
	private String name;
	
	/**
	 * 标记元素的类型，以用于重新获取时
	 */
	private ElementType elementType;
	
	/**
	 * 初始化信息，并添加需要获取元素的下标，用于需要获取列表时使用
	 * @param name 元素名称或定位内容
	 * @param byType 元素定位
	 */
	public Element(WebDriver driver, ElementType elementType, By by, String name, int index) {
		super();
		this.by = by;
		this.index = index;
		this.driver = driver;
		this.elementType = elementType;
		this.name = name;
	}
	
	/**
	 * 初始化信息，指定获取第一个元素，用于只获取单个元素时使用
	 * @param name 元素名称或定位内容
	 * @param byType 元素定位
	 */
	public Element(WebDriver driver, ElementType elementType, By by, String name) {
		this(driver, elementType, by, name, 0);
	}
	
	/**
	 * 用于返回元素对应的WebElement对象
	 * @return 返回元素对应的WebElement对象
	 */
	public WebElement getWebElement() {
		//若元素未进行查找，则查找一次元素
		if(element == null) {
			//对元素进行一次查找
			findElement();
		}
		
		return element;
	}
	
	/**
	 * 根据存储的WebDriver对象对元素进行更新
	 * @throws Exception 
	 */
	public void findElement() {
		switch (elementType) {
		case COMMON_ELEMENT:
		case DATA_LIST_ELEMENT:
		case SELECT_DATAS_ELEMENT:
			element = driver.findElements(by).get(index);
			break;
		case SELECT_OPTION_ELEMENT:
			element = new Select(driver.findElement(by)).getOptions().get(index);
			break;
		default:
			throw new IllegalArgumentException("Unexpected value: " + elementType);
		}
	}
	
	/**
	 * 用于返回元素 的名称
	 * @return 元素名称
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 用于返回元素信息，输出格式“元素名称 + “元素” + 列表选项”
	 * @return 元素信息
	 */
	public String getLog() {
		switch (elementType) {
		case COMMON_ELEMENT:
			return name + "元素";
		case DATA_LIST_ELEMENT:
		case SELECT_DATAS_ELEMENT:
		case SELECT_OPTION_ELEMENT:
			return name + "元素第" + index + "个选项";
		default:
			throw new IllegalArgumentException("Unexpected value: " + elementType);
		}
	}
}
