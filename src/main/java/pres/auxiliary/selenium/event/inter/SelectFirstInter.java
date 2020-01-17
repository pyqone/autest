package pres.auxiliary.selenium.event.inter;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import pres.auxiliary.selenium.event.Event;
import pres.auxiliary.selenium.event.EventResultEnum;

/**
 * <p><b>文件名：</b>SelectFirstInter.java</p>
 * <p><b>用途：</b>该接口定义下拉框选择事件选择第一个选项的实现方法，可直接使用</p>
 * <p><b>编码时间：</b>2019年9月3日下午2:33:11</p>
 * <p><b>修改时间：</b>2019年9月29日下午7:33:11</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public interface SelectFirstInter {
	/**
	 * 该方法用于选择标准的web下拉框（由标签select和标签option构成）的第一个选项，例如：<br>
	 * &lt;select&nbsp;id='test'&gt;<br>
	 * &emsp;&lt;option&gt;男&lt;/option&gt;<br>
	 * &emsp;&lt;option&gt;女&lt;/option&gt;<br>
	 * &emsp;&lt;option&gt;其他&lt;/option&gt;<br>
	 * &lt;/select&gt;<br>
	 * 对于该标准的下拉框选项，只需要定位到//select[@id='test']，得到其WebElement对象即可。
	 * 
	 * @param element 通过查找页面得到的控件元素对象
	 */
	static void selectFirst(WebElement element) {
		//定义Select类对象
		Select select = new Select(element);
		//选择元素
		select.selectByIndex(0);
		//将下拉框的选项的值设置为枚举值
		Event.setValue(EventResultEnum.STRING.setValue(select.getOptions().get(0).getText()));
	}
	
	/**
	 * 该方法用于选择非标准的web标准下拉框（由input、button和大量的div标签组成）的第一个选项。对于非标准的下拉框，需要传入的是选项列表，例如：<br>
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
	 * 注意，末尾的div不指定数字，则可以代表整个选项。
	 * 
	 * @param elements 通过查找页面得到的选项组对象
	 */
	static void selectFirst(List<WebElement> elements) {
		elements.get(0).click();
		//将下拉框的选项的值设置为枚举值
		Event.setValue(EventResultEnum.STRING.setValue(elements.get(0).getText()));
	}
}
