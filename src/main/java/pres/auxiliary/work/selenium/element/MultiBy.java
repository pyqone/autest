package pres.auxiliary.work.selenium.element;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.TimeoutException;

import pres.auxiliary.work.selenium.brower.AbstractBrower;

/**
 * <p>
 * <b>文件名：</b>ListBy.java
 * </p>
 * <p>
 * <b>用途：</b> 提供对多元素获取与返回的基本方法
 * </p>
 * <p>
 * <b>编码时间：</b>2020年10月14日下午6:54:46
 * </p>
 * <p>
 * <b>修改时间：</b>2020年10月14日下午6:54:46
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 *
 */
public abstract class MultiBy<T extends MultiBy<T>> extends AbstractBy {
	/**
	 * 用于记录当前元素集合中第一个元素是否为不可选择的元素，用于对元素的随机返回
	 */
	protected boolean firstEmpty = false;

	/**
	 * 构造方法，初始化浏览器对象
	 * 
	 * @param brower {@link AbstractBrower}类对象
	 */
	public MultiBy(AbstractBrower brower) {
		super(brower);
	}

	/**
	 * 用于设置当前元素集合中首元素是否为允许随机返回的元素<br>
	 * 例如：获取列表元素时，首元素为标题元素；获取下拉选项元素集合时，
	 * 第一个元素为空选项或者为类似“请选择”等选项
	 * 
	 * @param firstEmpty 首元素是否为允许随机返回的元素
	 */
	public void setFirstIsEmpty(boolean firstEmpty) {
		this.firstEmpty = firstEmpty;
	}

	/**
	 * 用于指定查找的元素集合名称，二次查找元素时，将覆盖上一次查找的元素。调用该方法在
	 * 无法查到页面元素时，其不会抛出{@link TimeoutException}异常
	 * 
	 * @param elementName 元素名称
	 * @param linkKeys    外链词语
	 */
	@SuppressWarnings("unchecked")
	public T find(String elementName, String... linkKeys) {
		//根据元素名称，获取元素信息数据
		elementData = new ElementData(elementName, read);
		elementData.addLinkWord(linkKeys);
		
		//获取元素数据在页面上对应的一组元素，若无法查到元素，则记录elementList为null
		try {
			elementList = recognitionElement(elementData);
		} catch (TimeoutException e) {
			elementList = new ArrayList<>();
		}
		
		return (T)this;
	}

	/**
	 * 用于返回当前存储的元素集合长度，若未对元素进行获取，则返回0
	 * 
	 * @return 元素集合长度
	 */
	public int size() {
		return elementList == null ? 0 : elementList.size();
	}

	/**
	 * 用于移除元素集合中的指定下标的元素。其下标所传入的数字即为元素所在的真实下标，可参见
	 * {@link #getElement(int)}方法的参数说明。若未调用{@link #find(String, String...)}
	 * 方法对元素进行查找，或者查无元素时，调用该方法将不进行任何操作
	 * 
	 * @param index 元素下标
	 * @return 是否删除成功
	 */
	public boolean removeElement(int index) {
		//当且仅当元素集合存在，并且下标传入正确时，将元素删除，并返回成功
		if (elementList != null && elementList.size() != 0) {
			int newIndex = toElementIndex(elementList.size(), index, firstEmpty);
			if (newIndex != -1) {
				elementList.remove(toElementIndex(elementList.size(), index, firstEmpty));
				return true;
			}
		}
		
		return false;
	}

	/**
	 * <p>
	 * 用于获取元素集合中指定下标的元素，该下标允许反向遍历与随机返回，其下标所传入的数字即为元素所在的真实下标。
	 * </p>
	 * <p>
	 * 例如：
	 * <ul>
	 * <li>传入1时，表示获取元素集合中的第1个元素</li>
	 * <li>传入0时，表示获取元素集合中在长度范围内随机一个元素</li>
	 * <li>传入-1时，表示获取元素集合中的倒数第1个元素</li>
	 * </ul>
	 * </p>
	 * <p>
	 * <b>注意：</b>调用该方法前，若未调用{@link #find(String, String...)}方法对元素进行查找，
	 * 或者查无元素时，调用该方法不会抛出异常，但当调用{@link Element#getWebElement()}方法时，
	 * 由于未查到元素则会抛出异常
	 * </p>
	 * 
	 * @param index 元素下标
	 * @return {@link Element}类对象
	 */
	public Element getElement(int index) {
		return new Element(toElementIndex(elementList.size(), index, firstEmpty), elementData, this);
	}

	/**
	 * 用于返回当前元素集合中所有的元素集合。若未调用{@link #find(String, String...)}方法对元素进行查找，
	 * 或者查无元素时，则调用该方法时会抛出超时异常
	 * 
	 * @return {@link Element}类对象{@link List}集合
	 * @throws TimeoutException 元素在页面不存在时抛出的异常
	 */
	public List<Element> getAllElement() {
		//定义返回元素的集合
		List<Element> elementList = new ArrayList<>();
		
		//判断当前列表是否为空，若为空，则向集合中添加一条假数据，以便于后续调用时抛出异常
		if (this.elementList == null || this.elementList.size() == 0) {
			throw new TimeoutException("页面上无相应定位方式的元素，元素名称：" + elementData.getName());
		} 

		//循环，遍历类中的elementList，根据其长度，构造Elemenet对象，并进行添加
		for (int index = 0; index < this.elementList.size(); index++) {
			elementList.add(new Element(index, elementData, this));
		}
		
		return elementList;
	}
}
