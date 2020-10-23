package pres.auxiliary.work.selenium.element;

import java.util.ArrayList;

import org.openqa.selenium.TimeoutException;

import pres.auxiliary.work.selenium.brower.AbstractBrower;

/**
 * <p><b>文件名：</b>CommonBy.java</p>
 * <p><b>用途：</b>
 * 提供在辅助化测试中，对页面单一元素获取的方法。类中获取元素的方法兼容传入定位方式对
 * 元素进行查找，建议使用xml对页面元素的定位方式进行存储，以简化编码时的代码量，也便于
 * 对代码的维护
 * </p>
 * <p><b>编码时间：</b>2020年10月13日上午9:44:01</p>
 * <p><b>修改时间：</b>2020年10月13日上午9:44:01</p>
 * @author 彭宇琦
 * @version Ver1.0
 *
 */
public class CommonBy extends AbstractBy {

	/**
	 * 构造方法，初始化浏览器对象
	 * @param brower {@link AbstractBrower}类对象
	 */
	public CommonBy(AbstractBrower brower) {
		super(brower);
	}
	
	/**
	 * 用于根据元素名称与需要获取的元素位置，返回指定的元素。调用该方法在无法查到页面元素时，其不会
	 * 抛出{@link TimeoutException}异常，但当调用{@link Element#getWebElement()}方法时，由于未查到元素
	 * 则会抛出异常
	 * @param elementName 元素名称
	 * @param index 元素所在下标
	 * @param linkKeys 外链词语组
	 * @return {@link Element}类对象
	 */
	public Element getElement(String elementName, String...linkKeys) {
		//根据元素名称，获取元素信息数据
		elementData = new ElementData(elementName, read);
		elementData.addLinkWord(linkKeys);
		
		//判断是否需要自动切换窗体，若需要，则对元素窗体进行切换
		if (isAutoSwitchIframe) {
			autoSwitchFrame(elementData.getIframeNameList());
		}
		
		//获取元素数据在页面上对应的一组元素，若无法查到元素，则记录elementList为null
		try {
			elementList = recognitionElement(elementData);
		} catch (TimeoutException e) {
			elementList = new ArrayList<>();
		}
		
		//返回元素对象
		return new Element(0, elementList, elementData, this);
	}
}
