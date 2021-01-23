package com.auxiliary.selenium.element;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.support.ui.Select;

import com.auxiliary.selenium.brower.AbstractBrower;

/**
 * <p><b>文件名：</b>SelectBy.java</p>
 * <p><b>用途：</b>
 * 提供在辅助化测试中，通过一个元素定位方式，获取下拉选项型元素（控件）的方法。通过元素的定位方式，自动
 * 识别下拉选项元素的类型（标准下拉选项<sup>1</sup>与非标准下拉选项<sup>2</sup>），并根据其类型获取到下拉
 * 选项的元素。
 * </p>
 * <p>
 * 注解：
 * <ol>
 * 	<li>标准下拉选项：由&lt;select&gt;与&lt;option&gt;标签组成的下拉选项，例如：
 * 	<pre>
 * 		&lt;select&nbsp;name='select'&gt;
 * 			&lt;option&gt;男&lt;/option&gt;
 * 			&lt;option&gt;女&lt;/option&gt;
 * 		&lt;/select&gt;
 * 	</pre>
 * 	</li>
 * 	<li>非标准下拉选项：由其他标签组成的一组元素，例如：
 * 	<pre>
 * 		&lt;div&nbsp;name='select'&gt;
 * 			&lt;div&nbsp;name='option'&gt;男&lt;/div&gt;
 * 			&lt;div&nbsp;name='option'&gt;女&lt;/div&gt;
 * 		&lt;/div&gt;
 * 	</pre>
 * 	</li>
 * </ol>
 * </p>
 * <p>
 * <b>注意：</b>若下拉选项为标准下拉选项，其元素定位方式只需要定位到select标签即可，若为非标准下拉选项，则需要
 * 将元素定位到能获取到所有下拉选项的元素上，以上述html代码为例，使用xpath对以上两个控件定位，则分别为：<br>
 * <ul>
 * 	<li>获取标准下拉选项：//select[@name='select']，通过该xpath在页面查找元素时，只能找到一个元素</li>
 * 	<li>获取非标准下拉选项：//div[@name='select']/div[@name='option']，通过该xpath在页面查找元素时，能找到两个元素</li>
 * </ul>
 * </p>
 * <p><b>编码时间：</b>2020年11月11日上午8:12:58</p>
 * <p><b>修改时间：</b>2020年11月11日上午8:12:58</p>
 * @author 彭宇琦
 * @version Ver1.0
 *
 */
public class FindSelectElement extends FindMultiElement<FindSelectElement> {
	/**
	 * 定义标准下拉选项的tagname
	 */
	public static final String SELECT_TAGNAME = "select";
	
	/**
	 * 用于存储当前元素列表中所有选项的文本内容
	 */
	private List<String> selectTextList = new ArrayList<>();
	/**
	 * 用于存储按照文本返回下拉选项时其名称读取的属性名
	 */
	private String attributeName = "text";
	
	/**
	 * 构造方法，初始化浏览器对象
	 * @param brower {@link AbstractBrower}类对象
	 */
	public FindSelectElement(AbstractBrower brower) {
		super(brower);
	}
	
	/**
	 * <p>
	 * 用于设置获取文本时读取的属性名称，即在调用{@link #getElement(String)}方法时，其元素内容的来源。
	 * </p>
	 * <p>
	 * 该方法存在设置一个特殊的属性值“text”（默认），表示获取元素的文本节点内容，其他的值均为
	 * 设置读取的属性值，建议设置的属性值的内容不存在重复，否则获取到的元素可能与预期不符。
	 * </p>
	 * <p>
	 * <b>注意：</b>调用该方法时将自动获取一次元素内容，若未调用{@link #find(String, String...)}方法查找
	 * 元素或查找的元素不存在时，调用该方法仅设置属性值。设置的属性名称不区分大小写
	 * </p>
	 * 
	 * @param attributeName 属性名称
	 */
	public void setReadArributeName(String attributeName) {
		this.attributeName = attributeName;
		
		//判断当前类中是否存在元素，若不存在，则结束运行
		if (elementList == null || elementList.size() == 0) {
			return;
		}
		
		//存在元素，则对元素内容，按照设置的查找方式进行查找
		findElementContent();
	}
	
	@Override
	public FindSelectElement find(String elementName, String... linkKeys) {
		super.find(elementName, linkKeys);
		//判断当前存储的元素集合中是否为空，若为空，则直接返回
		if (elementList == null || elementList.size() == 0) {
			return this;
		}
		
		//判断当前元素集合中，第一个元素的tagName是否为select，若为select，则表示其为标准下拉
		//选项元素，则调用标准下拉选项方式获取选项元素并存储；若不是，则不进行处理
		if (SELECT_TAGNAME.equals(elementList.get(0).getTagName())) {
			elementList = new Select(elementList.get(0)).getOptions();
			if (elementList == null || elementList.size() == 0) {
				return this;
			}
		}
		
		//按照设置的查找方式，查找元素内容
		findElementContent();
		return this;
	}
	
	/**
	 * 用于获取元素集合中指定内容的元素。其元素内容可以调用{@link #setReadArributeName(String)}方法进行设置。
	 * 若元素所传的元素内容不在当前元素内容集合中时，该方法将不会抛出异常，但当调用
	 * {@link Element#getWebElement()}方法时，则会抛出元素不存在异常
	 * 
	 * @param selectText 元素的内容
	 * @return 相应内容的元素
	 */
	public Element getElement(String selectText) {
		//判断当前内容是否存在于selectTextList中，存在，则获取下标，并返回相应的元素；不存在，则构造元素下标为-1
		int index = textToIndex(selectText);
		if (index > -1) {
			return getElement(index + 1);
		} else {
			return new Element(-1, elementData, this);
		}
	}
	
	@Override
	public boolean removeElement(int index) {
		if (super.removeElement(index + 1)) {
			selectTextList.remove(index);
			return true;
		}
		
		return false;
	}
	
	/**
	 * 用于根据选项内容移除元素
	 * @param selectText 下拉选项内容
	 * @return 是否移除成功
	 */
	public boolean removeElement(String selectText) {
		//判断当前内容是否存在于selectTextList中，存在，则获取下标，并返回相应的元素；不存在，则构造元素下标为-1
		int index = textToIndex(selectText);
		if (index > -1) {
			return removeElement(index);
		} else {
			return false;
		}
	}
	
	/**
	 * 用于根据设置，查找元素内容，并进行存储
	 */
	private void findElementContent() {
		selectTextList = elementList.stream().map(element -> {
			if ("text".equalsIgnoreCase(attributeName)) {
				return element.getText();
			} else {
				return element.getAttribute(attributeName);
			}
		}).collect(Collectors.toList());
	}
	
	/**
	 * 用于将下拉的文本转换为下标
	 * @param text 文本内容
	 * @return 文本所在集合中的下标
	 */
	private int textToIndex(String text) {
		int index = 0;
		//遍历selectTextList，查找第一个包含text的内容，返回相应的下标
		for (; index < selectTextList.size(); index++) {
			if (selectTextList.get(index).contains(text)) {
				return index;
			}
		}
		
		//若循环结束仍未找到相应的下标，则返回-1
		return -1;
	}
}