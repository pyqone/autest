package pres.auxiliary.selenium.event.inter;

import pres.auxiliary.selenium.event.Event;
import pres.auxiliary.selenium.event.EventResultEnum;

/**
 * <p><b>文件名：</b>CommenElementSelectFirstInter.java</p>
 * <p><b>用途：</b>该接口定义了普通元素选择下拉框第一个选线事件的实现标准</p>
 * <p><b>编码时间：</b>2019年9月3日下午4:27:40</p>
 * <p><b>修改时间：</b>2019年9月3日下午4:27:40</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
@FunctionalInterface
public interface CommenElementSelectFirstInter extends SelectFirstInter {
	/**
	 * 通过传入在xml文件中的控件名称，到类中指向的xml文件中查找控件名称对应的定位方式，或直接传入xpath与css定位方式，
	 * 根据定位方式在页面查找元素并对标准的web下拉框（由标签select和标签option构成）选择其下拉框第一个选项，
	 * 亦可以对非标准的下拉框（由大量的div等标签组成）进行操作，但对于非标准的下拉框，需要传入的是选项列表，例如：<br>
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
	 * <p>
	 * 本操作返回的枚举值是{@link EventResultEnum#STRING}，其枚举的value值为当前选择选项的内容，可参见{@link EventResultEnum}类。
	 * </p>
	 * 
	 * @param name 控件的名称或xpath与css定位方式
	 * @return {@link Event}类对象，可通过{@link Event#getStringValve()}方法获取操作的返回值
	 */
	Event selectFirst(String name);
}
