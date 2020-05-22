package pres.auxiliary.work.selenium.element;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import pres.auxiliary.selenium.xml.ByType;
import pres.auxiliary.work.selenium.brower.AbstractBrower;

public class SelectElement extends MultiElement {
	/**
	 * 用于存储获取下拉选项时的信息
	 */
	ElementInformation elementInfo;
	/**
	 * 用于存储下拉选项的元素
	 */
	ArrayList<Element> option = new ArrayList<>();
	/**
	 * 用于存储下拉选项的文本
	 */
	ArrayList<String> optionText = new ArrayList<>();
	
	/**
	 * 设置获取的下拉选项的类型，0表示标准的select-option型；1表示非标准的下拉选项型
	 */
	private ElementType elementType;
	
	/**
	 * 通过浏览器对象{@link AbstractBrower}进行构造
	 * @param brower {@link AbstractBrower}对象
	 */
	public SelectElement(AbstractBrower brower) {
		super(brower);
	}

	/**
	 * 构造对象并存储浏览器的{@link WebDriver}对象
	 * 
	 * @param driver 浏览器的{@link WebDriver}对象
	 */
	public SelectElement(WebDriver driver) {
		super(driver);
	}
	
	@Override
	public void add(String name, ByType byType) {
		add(new ElementInformation(name, byType));
	}
	
	@Override
	void add(ElementInformation elementInformation) {
		//获取元素的By对象
		By by = recognitionElement(elementInformation);
		//根据By获取元素
		List<WebElement> elementList = driver.findElements(by);
		//获取元素个数
		int size = elementList.size();
		
		//根据获取到的元素个数，来判断下拉选项的类型
		if (size == 1) {
			elementInformation.elementType = ElementType.SELECT_OPTION_ELEMENT;
			
			//若是标准下拉选项型，则需要改变size的值，方便后续添加数据
			Select select = new Select(driver.findElement(by));
			//获取所有的选项内容，并计算元素个数
			elementList = select.getOptions();
			size = elementList.size();
		} else {
			elementInformation.elementType = ElementType.SELECT_DATAS_ELEMENT;
		}
		
		//构造Element对象
		for (int i = 0; i < size; i++) {
			//获取元素
			option.add(new Element(driver, elementType, by, i));
			//获取元素的文本内容
			optionText.add(elementList.get(0).getText());
		}
	}
	
	/**
	 * 根据选项下标，返回相应的选项元素。下标与选项元素真实下标一致，支持传入负数，表示从后向前遍历元素；
	 * 当传入0时，表示随机选择一个选项。
	 * @param index 选项下标
	 * @return 相应的选项元素
	 */
	public Element getElement(int index) {
		return option.get(getIndex(option.size(), index));
	}
	
	/**
	 * 根据选项内容，返回相应的选项元素，当传入的元素名称不存在时，则返回null；当存在重复的选项
	 * 名称时，则选择第一个选项
	 * 
	 * @param elementName 选项名称
	 * @return 相应的选项元素
	 */
	public Element getElement(String elementName) {
		//根据名称获取元素下标
		int index = optionText.indexOf(elementName);
		//判断下标是否为-1（元素是否存在），若不存在则返回null
		if (index == -1) {
			return null;
		}
		
		return option.get(optionText.indexOf(elementName));
	}
	
	@Override
	public void againGetElement() {
		option.clear();
		optionText.clear();
		add(elementInfo);
	}
	
	@Override
	boolean isExistElement(By by, long waitTime) {
		//当查找到元素时，则返回true，若查不到元素，则会抛出异常，故返回false
		return new WebDriverWait(driver, waitTime, 200).
				until((driver) -> {
					WebElement element = driver.findElement(by);
					return element != null;
				});
	}
}
