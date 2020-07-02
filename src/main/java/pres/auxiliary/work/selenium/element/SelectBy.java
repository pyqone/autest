package pres.auxiliary.work.selenium.element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import pres.auxiliary.work.selenium.brower.AbstractBrower;
import pres.auxiliary.work.selenium.xml.ByType;

/**
 * <p><b>文件名：</b>SelectBy.java</p>
 * <p><b>用途：</b>
 * 提供获取下拉框中选项元素的方法，支持标准型下拉框选项（由select与option标签组成的下拉框）以及
 * 非标准型下拉框选项（由普通的div、li等元素组成的选项），并支持根据关键词查找选项。需要注意的是，
 * 标准下拉选项和非标准下拉选项需要传入的参数不同，例如，<br>
 * 标准下拉框：<br>
 * &lt;select&nbsp;id='test'&gt;<br>
 * &emsp;&lt;option&gt;男&lt;/option&gt;<br>
 * &emsp;&lt;option&gt;女&lt;/option&gt;<br>
 * &emsp;&lt;option&gt;其他&lt;/option&gt;<br>
 * &lt;/select&gt;<br>
 * 对于该标准的下拉框选项，只需要定位到//select[@id='test']，得到其WebElement对象即可，但对于非标准的下拉框其下拉框是由input和button标签构成：<br>
 * &lt;div&gt;<br>
 * &emsp;&lt;span&gt;&lt;input/&gt;&lt;/span&gt;<br>
 * &emsp;&lt;span&gt;&lt;button/&gt;&lt;/span&gt;<br>
 * &lt;/div&gt;<br>
 * 点击button对应的按钮后，其下也能弹出选项，但其选项是由div标签写入text构成：<br>
 * &lt;div&nbsp;id='test'&gt;<br>
 * &emsp;&lt;div&gt;男&lt;/div&gt;<br>
 * &emsp;&lt;div&gt;女&lt;/div&gt;<br>
 * &emsp;&lt;div&gt;其他&lt;/div&gt;<br>
 * &lt;/div&gt;<br>
 * 对于这种非标准的下拉框选项，需要传入选项所在的所有div标签对应的WebElement元素，在上例，则需要定位到//div[@id='test']/div，
 * 注意，末尾的div不指定数字，则可以代表整个选项。<br>
 * </p>
 * <p><b>编码时间：</b>2020年5月24日下午3:30:00</p>
 * <p><b>修改时间：</b>2020年5月24日下午3:30:00</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
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
	
	/**
	 * 控制元素首行是否为
	 */
	private boolean fristIsEmpty = false;
	
	/**
	 * 通过浏览器对象{@link AbstractBrower}进行构造，并指定第一个选项是否为空选项或者为“请选择”等文本
	 * 的选项，若为该选项时，则在随机选择时不会选择到该选项
	 * 
	 * @param brower {@link AbstractBrower}对象
	 * @param fristIsEmpty 指定第一个元素是否为空串或为“请选择”等文本
	 */
	public SelectBy(AbstractBrower brower) {
		super(brower);
	}

	/**
	 * 构造对象并存储浏览器的{@link WebDriver}对象，并指定第一个选项是否为空选项或者为“请选择”等文本
	 * 的选项，若为该选项时，则在随机选择时不会选择到该选项
	 * 
	 * @param driver 浏览器的{@link WebDriver}对象
	 * @param fristIsEmpty 指定第一个元素是否为空串或为“请选择”等文本
	 */
	public SelectBy(WebDriver driver) {
		super(driver);
	}
	
	@Override
	public void add(String name, ByType byType) {
		elementInfo = new ElementInformation(name, byType);
		add(elementInfo);
	}
	
	/**
	 * 用于添加选项并指明首个选项是否为不可选择的选项或者文本为空的选项，其他效果与{@link #add(String)}一致
	 * @param name 元素在xml文件或者元素的定位内容
	 * @param fristIsEmpty 首个选项是否为不可选择的选项或者文本为空的选项
	 * @see #add(String)
	 */
	public void add(String name, boolean fristIsEmpty) {
		this.fristIsEmpty = fristIsEmpty;
		add(name);
	}
	
	/**
	 * 用于添加选项并指明首个选项是否为不可选择的选项或者文本为空的选项，其他效果与{@link #add(String, ByType)}一致
	 * @param name 元素在xml文件或者元素的定位内容
	 * @param byType 元素定位方式枚举对象（{@link ByType}枚举）
	 * @param fristIsEmpty 首个选项是否为不可选择的选项或者文本为空的选项
	 * @see #add(String, ByType)
	 */
	public void add(String name, ByType byType, boolean fristIsEmpty) {
		this.fristIsEmpty = fristIsEmpty;
		add(name, byType);
	}
	
	@Override
	public void add(String name, ByType byType, String... links) {
		elementInfo = new ElementInformation(name, byType, ElementType.DATA_LIST_ELEMENT, links);
		add(elementInfo);
		
	}

	@Override
	public void add(String name, String... links) {
		elementInfo = new ElementInformation(name, null, ElementType.DATA_LIST_ELEMENT, links);
		add(elementInfo);
	}
	
	/**
	 * 可用于指明首行是否为空选项，其他说明可参考{@link #add(String, ByType, String...)}
	 * @param name 元素在xml文件或者元素的定位内容
	 * @param byType 元素定位方式枚举对象（{@link ByType}枚举）
	 * @param fristIsEmpty 首个选项是否为不可选择的选项或者文本为空的选项
	 * @param links 替换词语
	 */
	public void add(String name, ByType byType, boolean fristIsEmpty, String... links) {
		this.fristIsEmpty = fristIsEmpty;
		add(name, byType, links);
		
	}

	/**
	 * 可用于指明首行是否为空选项，其他说明可参考{@link #add(String, String...)}
	 * @param name 元素在xml文件或者元素的定位内容
	 * @param fristIsEmpty 首个选项是否为不可选择的选项或者文本为空的选项
	 * @param links 替换词语
	 */
	public void add(String name, boolean fristIsEmpty, String... links) {
		this.fristIsEmpty = fristIsEmpty;
		add(name, null, links);
		
	}
	
	@Override
	void add(ElementInformation elementInformation) {
		//判断传入的元素是否在xml文件中，若存在再判断是否自动切换窗体，若需要，则获取元素的所有父窗体并进行切换
		if (xml != null && xml.isElement(elementInformation.name) && isAutoSwitchIframe) {
			switchFrame(getParentFrameName(elementInformation.name));
		}
				
		//清除原存储的内容
		clear();
		
		//获取元素的By对象
		By by = recognitionElement(elementInformation);
		//根据By获取元素
		List<WebElement> elementList = driver.findElements(by);
		//获取元素个数
		int size = elementList.size();
		
		//根据第一个元素的tagname来判断是否为标准下拉元素
		if ("select".equalsIgnoreCase(elementList.get(0).getTagName())) {
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
	 * 根据关键词组查找选项，并返回选项元素，当传入的元素名称不存在时，则抛出NoSuchElementException异常；
	 * 当查询到有多个包含关键词的选项时，则选择第一个选项<br>
	 * 注意，当传入多个关键词时其选项需要全部满足才会返回相应的选项。
	 * 
	 * @param keys 查询选项的关键词组
	 * @return 相应的选项元素
	 * @throws NoSuchElementException 查找的选项不存在时抛出的异常
	 */
	public Element getElement(String...keys) {
		//查找完全符合关键词的元素
		String elementName = optionText.stream().filter(text -> {
			//遍历关键词，若元素不符合条件，则返回false
			for (String key : keys) {
				if (text.indexOf(key) < 0) {
					return false;
				}
			}
			
			//若条件均符合，则返回true
			return true;
		}).findFirst().orElseThrow(() -> {
			//若不存在符合条件的选项，则抛出NoSuchElementException异常，并返回相应的消息
			StringBuilder keyText = new StringBuilder("[");
			//拼接查询条件
			Arrays.stream(keys).forEach(key -> {
				keyText.append(key + ", ");
			});
			
			return new NoSuchElementException("不存在符合条件的选项：" + keyText.substring(0, keyText.length() - ", ".length()) + "]");
		});
		
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
