package pres.auxiliary.work.selenium.element;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

/**
 * <p><b>文件名：</b>AbstractBy.java</p>
 * <p><b>用途：</b>
 * 用于存储通过元素信息查找到的元素内容
 * </p>
 * <p><b>编码时间：</b>2020年10月13日上午8:02:38</p>
 * <p><b>修改时间：</b>2020年10月13日上午8:02:38</p>
 * @author 彭宇琦
 * @version Ver1.0
 *
 */
public class Element {
	/**
	 * 存储元素的获取下标
	 */
	private int index;
	/**
	 * 存储获取的元素类对象
	 */
	private List<WebElement> elementList;
	/**
	 * 存储元素的信息
	 */
	private ElementData elementData;
	/**
	 * 存储AbstractBy类对象
	 */
	private AbstractBy abstractBy;
	
	/**
	 * 构造对象。其传入的下标允许为-1，表示当前元素集合中不存在该元素
	 * @param index 元素下标
	 * @param elementList 元素类对象数据
	 * @param elementData 元素信息
	 * @param abstractBy 元素获取类对象
	 */
	public Element(int index, List<WebElement> elementList, ElementData elementData, AbstractBy abstractBy) {
		super();
		this.index = index;
		this.elementList = elementList;
		this.elementData = elementData;
		this.abstractBy = abstractBy;
	}



	/**
	 * 用于返回当前存储的{@link WebElement}对象，若该对象为空，则抛出元素查找超时异常
	 * @return {@link WebElement}对象
	 * @throws NoSuchElementException 元素在页面不存在时或元素集合不存在指定的下标时抛出的异常
	 */
	public WebElement getWebElement() {
		//判断元素集合是否为空，若为空，则抛出查找超时异常
		if (elementList == null || elementList.size() == 0) {
			throw new NoSuchElementException("页面上无相应定位方式的元素，当前元素名称：" + elementData.getName());
		}
		
		//判断元素下标是否为-1，若为-1，则抛出元素不存在异常
		if (index == -1) {
			throw new NoSuchElementException("指定的元素下标值不存在，当前元素集合个数：" + elementList.size());
		}
		
		return elementList.get(index);
	}
	
	/**
	 * 用于返回当前元素的{@link ElementData}类对象
	 * @return 当前元素的{@link ElementData}类对象
	 */
	public ElementData getElementData() {
		return elementData;
	}
	
	/**
	 * 重新根据元素信息，在页面查找元素
	 */
	public void againFindElement() {
		//重新构造elementList
		elementList = new ArrayList<WebElement>();
		try {
			elementList.addAll(abstractBy.recognitionElement(elementData));
		}catch (TimeoutException e) {
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((elementData == null) ? 0 : elementData.hashCode());
		result = prime * result + index;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Element other = (Element) obj;
		if (elementData == null) {
			if (other.elementData != null)
				return false;
		} else if (!elementData.equals(other.elementData))
			return false;
		if (index != other.index)
			return false;
		return true;
	}
}
