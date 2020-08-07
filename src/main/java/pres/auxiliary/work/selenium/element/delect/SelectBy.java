package pres.auxiliary.work.selenium.element.delect;

import java.util.List;

import org.openqa.selenium.NoSuchElementException;

import pres.auxiliary.work.selenium.brower.AbstractBrower;

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
public class SelectBy extends ListBy {
	/**
	 * 用于指向存储的元素名称
	 */
	private String name;
	/**
	 * 用于指向存储的外链词语
	 */
	private List<String> linkKey;
	
	/**
	 * 通过浏览器对象{@link AbstractBrower}进行构造
	 * 
	 * @param brower {@link AbstractBrower}对象
	 */
	public SelectBy(AbstractBrower brower) {
		super(brower);
	}
	
	@Override
	void add(ElementInformation elementInformation) {
		//存储元素信息
		name = elementInformation.name;
		linkKey = elementInformation.linkKeyList;
		
		//清空父类中的链表，重新插入数据
		clear();
		
		super.add(elementInformation);
	}

	/**
	 * 该方法用于根据列名称，查找到相应的列，并返回与传入下标对应的元素。下标支持从后向前获取，传入的下标
	 * 与元素实际所在位置一致，当传入0时，则表示随机获取一个元素，如：<br>
	 * {@code getWebElement(1)}表示获取第1个选项<br>
	 * {@code getWebElement(0)}表示获取随机一个选项<br>
	 * {@code getWebElement(-1)}表示获取倒数第1个选项<br>
	 * 
	 * @param index 元素下标（即列表中对应的某一个元素）
	 * @return 对应的选项
	 * @throws NoSuchElementException 当未对name列进行获取数据或index的绝对值大于列表最大值时抛出的异常
	 */
	public Element getElement(int index) {
		Element element = findElement();
		element.setElementIndex(index);
		return element;
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
		Element element = findElement();
		element.setElementIndex(keys);
		return element;
	}
	
	/**
	 * 用于查找相应的元素
	 * @return 需要查找的元素
	 */
	private Element findElement() {
		//获取元素信息，并判断元素是否存在，不存在则抛出异常
		ElementInformation elementInfo = nameToElementInformation(name, linkKey);
		if (elementInfo == null) {
			throw new NoSuchElementException("不存在的定位方式：" + name);
		}
		
		return elementMap.get(elementInfo);
	}
	
	@Override
	ElementType setElementType() {
		return ElementType.SELECT_ELEMENT;
	}
}
