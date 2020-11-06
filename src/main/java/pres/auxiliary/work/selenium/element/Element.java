package pres.auxiliary.work.selenium.element;

import org.openqa.selenium.NoSuchElementException;
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
	 * @param elementData 元素信息
	 * @param abstractBy 元素获取类对象
	 */
	public Element(int index, ElementData elementData, AbstractBy abstractBy) {
		super();
		this.index = index;
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
		if (abstractBy.elementList == null || abstractBy.elementList.size() == 0) {
			throw new NoSuchElementException("页面上无相应定位方式的元素，当前元素名称：" + elementData.getName());
		}
		
		//判断元素下标是否为-1，若为-1，则抛出元素不存在异常
		if (index == -1) {
			throw new NoSuchElementException("指定的元素下标值不存在，当前元素集合个数：" + abstractBy.elementList.size());
		}
		
		//判断当前类中存储的元素数据类对象是否与abstractBy中存储的数据类对象一致，若不一致，则重新获取元素
		if (!this.elementData.getName().equals(abstractBy.elementData.getName())) {
			againFindElement();
		}
		
		return abstractBy.elementList.get(index);
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
//		abstractBy.elementList.clear();
//		try {
//			abstractBy.elementList.addAll(abstractBy.recognitionElement(elementData));
//		}catch (TimeoutException e) {
//		}
		abstractBy.elementList = abstractBy.recognitionElement(elementData);
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
