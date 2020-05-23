package pres.auxiliary.work.selenium.element;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import pres.auxiliary.selenium.xml.ByType;
import pres.auxiliary.work.selenium.brower.AbstractBrower;

public class SelectBy extends MultiBy {
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
	 * 设置标记的下拉选项的类型
	 */
	private ElementType elementType;
	
	private boolean fristIsEmpty = false;
	
	/**
	 * 通过浏览器对象{@link AbstractBrower}进行构造，并指定第一个选项是否为空选项或者为“请选择”等文本
	 * 的选项，若为该选项时，则在随机选择时不会选择到该选项
	 * 
	 * @param brower {@link AbstractBrower}对象
	 * @param fristIsEmpty 指定第一个元素是否为空串或为“请选择”等文本
	 */
	public SelectBy(AbstractBrower brower, boolean fristIsEmpty) {
		super(brower);
		this.fristIsEmpty = fristIsEmpty;
	}

	/**
	 * 构造对象并存储浏览器的{@link WebDriver}对象，并指定第一个选项是否为空选项或者为“请选择”等文本
	 * 的选项，若为该选项时，则在随机选择时不会选择到该选项
	 * 
	 * @param driver 浏览器的{@link WebDriver}对象
	 * @param fristIsEmpty 指定第一个元素是否为空串或为“请选择”等文本
	 */
	public SelectBy(WebDriver driver, boolean fristIsEmpty) {
		super(driver);
		this.fristIsEmpty = fristIsEmpty;
	}
	
	@Override
	public void add(String name, ByType byType) {
		elementInfo = new ElementInformation(name, byType);
		add(elementInfo);
	}
	
	@Override
	void add(ElementInformation elementInformation) {
		//清除原存储的内容
		clear();
		
		//获取元素的By对象
		By by = recognitionElement(elementInformation);
		//根据By获取元素
		List<WebElement> elementList = driver.findElements(by);
		//获取元素个数
		int size = elementList.size();
		
		//根据获取到的元素个数，来判断下拉选项的类型
		if (size == 1) {
			elementType = ElementType.SELECT_OPTION_ELEMENT;
			
			//若是标准下拉选项型，则需要改变size的值，方便后续添加数据
			Select select = new Select(driver.findElement(by));
			//获取所有的选项内容，并计算元素个数
			elementList = select.getOptions();
			size = elementList.size();
		} else {
			elementType = ElementType.SELECT_DATAS_ELEMENT;
		}
		
		//构造Element对象
		for (int i = 0; i < size; i++) {
			//获取元素
			option.add(new Element(driver, elementType, by, i));
			//获取元素的文本内容
			optionText.add(elementList.get(i).getText());
		}
	}
	
	/**
	 * 根据选项下标，返回相应的选项元素。下标与选项元素真实下标一致，支持传入负数，表示从后向前遍历元素；
	 * 当传入0时，表示随机选择一个选项。
	 * @param index 选项下标
	 * @return 相应的选项元素
	 */
	public Element getElement(int index) {
		//当首选项为空时，则在随机时不允许产生0
		return option.get(getIndex(option.size(), index, !fristIsEmpty));
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
		clear();
		add(elementInfo);
	}
	
	@Override
	boolean isExistElement(By by, long waitTime) {
		//当查找到元素时，则返回true，若查不到元素，则会抛出异常，故返回false
		return new WebDriverWait(driver, waitTime, 200).
				until((driver) -> {
					List<WebElement> elements = driver.findElements(by);
					//根据是否能查找到元素进行判断
					if (elements.size() > 0) {
						//若获取到的第一个元素的标签名为select（标准下拉），则可以直接返回true
						if ("select".equals(elements.get(0).getTagName())) {
							return true;
						}
						
						//若查到元素，再进一步判断元素内容是否完全加载
						int textSize = elements.stream().filter(element -> {
							return !element.getText().isEmpty();
						}).collect(Collectors.toList()).size();
						
						//若首选项为空时，则加载的内容必须大于或等于总选项个数-1
						//若首选项不为空时，则加载的内容必须与原选项个数一致
						if (fristIsEmpty) {
							return textSize >= elements.size() - 1;
						} else {
							return textSize == elements.size();
						}
					} else {
						return false;
					}
				});
	}
	
	/**
	 * 用于清除原存储的内容
	 */
	void clear() {
		option.clear();
		optionText.clear();
	}
}
