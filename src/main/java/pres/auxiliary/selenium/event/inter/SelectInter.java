package pres.auxiliary.selenium.event.inter;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import pres.auxiliary.selenium.event.Event;
import pres.auxiliary.selenium.event.EventResultEnum;

/**
 * <p><b>文件名：</b>SelectInter.java</p>
 * <p><b>用途：</b>该接口定义下拉框选择事件的实现方法，可直接使用</p>
 * <p><b>编码时间：</b>2019年9月3日上午8:38:34</p>
 * <p><b>修改时间：</b>2019年9月3日上午8:38:34</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public interface SelectInter {
	/**
	 * <p>
	 * 该方法用于对标准的web下拉框（由标签select和标签option构成）进行选择，例如：<br>
	 * &lt;select&nbsp;id='test'&gt;<br>
	 * &emsp;&lt;option&gt;男&lt;/option&gt;<br>
	 * &emsp;&lt;option&gt;女&lt;/option&gt;<br>
	 * &emsp;&lt;option&gt;其他&lt;/option&gt;<br>
	 * &lt;/select&gt;<br>
	 * 对于该标准的下拉框选项，只需要定位到//select[@id='test']，得到其WebElement对象即可。
	 * </p>
	 * <p>
	 * <b><i>注意：</i></b>option序号从1开始，1表示第一个选项，亦可传入负数，-1表示倒数第一个选项，
	 * -2表示倒数第二个选项，以此类推；当传入的值为0时，则表示随机选择一个选项，但随机的值不包括第一个选项，
	 * 以避免第一个元素为空或为“请选择”之类的选项
	 * </p>
	 * @param element 通过查找页面得到的控件元素对象
	 * @param option 选项的序号，正数表示从前向后选，负数表示从后往前选，0表示随机选择
	 * @throws NoSuchElementException 指定的选项值超过选项的最大值时抛出
	 */
	static void select(WebElement element, int option) {
		//定义Select类对象
		Select select = new Select(element);
		//获取选项的长度
		int length = select.getOptions().size();
		
		//判断传入的选项值的绝对值（由于可传入负数，故取绝对值）是否大于选项的总长度，若大于总长度，则抛出NoSuchElementException
		if (Math.abs(option) > length) {
			throw new NoSuchElementException("指定的选项值大于选项的最大值。选项总个数：" + length + "指定项：" + option);
		}
		
		//判断option的值，若大于0，则从前向后遍历，若小于0，则从后往前遍历，若等于0，则随机输入
		if (option > 0) {
			//选择元素，正数的选项值从1开始，故需要减小1才是正确的选项值
			select.selectByIndex(option - 1);
			//将下拉框的选项的值设置为枚举值
			Event.setValue(EventResultEnum.STRING.setValue(select.getOptions().get(option - 1).getText()));
		} else if (option < 0) {
			//选择元素，由于option为负数，则长度加上选项值即可得到需要选择的选项
			select.selectByIndex(length + option);
			//将下拉框的选项的值设置为枚举值
			Event.setValue(EventResultEnum.STRING.setValue(select.getOptions().get(length + option).getText()));
		} else {
			//为0，则在判断选项是否只有一个，若有多个，则随机抽取，若选项只有一个，则直接选择第一个
			//进行随机选择，但不选择第一个元素，避免第一个元素为空或为“请选择”之类的选项
			int index = length > 1 ? new Random().nextInt(length - 1) + 1 : 0;
			select.selectByIndex(index);
			
			//将下拉框的选项的值设置为枚举值
			Event.setValue(EventResultEnum.STRING.setValue(select.getOptions().get(index).getText()));
		}
	}
	
	/**
	 * <p>
	 * 该方法用于对非标准的web标准下拉框（由input、button和大量的div标签组成）进行选择。对于非标准的下拉框，需要传入的是选项列表，例如：<br>
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
	 * </p>
	 * <p>
	 * <b><i>注意：</i></b>option序号从1开始，1表示第一个选项，亦可传入负数，-1表示倒数第一个选项，
	 * -2表示倒数第二个选项，以此类推；当传入的值为0时，则表示随机选择一个选项，但随机的值不包括第一个选项，
	 * 以避免第一个元素为空或为“请选择”之类的选项
	 * </p>
	 * @param elements 通过查找页面得到的选项组对象
	 * @param option 选项的序号，正数表示从前向后选，负数表示从后往前选，0表示随机选择
	 * @throws NoSuchElementException 指定的选项值超过选项的最大值时抛出
	 */
	static void select(List<WebElement> elements, int option) {
		//获取选项的长度
		int length = elements.size();
		
		//判断传入的选项值的绝对值（由于可传入负数，故取绝对值）是否大于选项的总长度，若大于总长度，则抛出NoSuchElementException
		if (Math.abs(option) > length) {
			throw new NoSuchElementException("指定的选项值大于选项的最大值。选项总个数：" + length + "，指定项：" + option);
		}
		
		//判断option的值，若大于0，则从前向后遍历，若小于0，则从后往前遍历，若等于0，则随机输入
		if (option > 0) {
			//将下拉框的选项的值设置为枚举值
			Event.setValue(EventResultEnum.STRING.setValue(elements.get(option - 1).getText()));
			//选择元素，正数的选项值从1开始，故需要减小1才是正确的选项值
			elements.get(option - 1).click();
		} else if (option < 0) {
			//将下拉框的选项的值设置为枚举值
			Event.setValue(EventResultEnum.STRING.setValue(elements.get(length + option).getText()));
			//选择元素，由于option为负数，则长度加上选项值即可得到需要选择的选项
			elements.get(length + option).click();
		} else {
			//为0，则在判断选项是否只有一个，若有多个，则随机抽取，若选项只有一个，则直接选择第一个
			int index = length > 1 ? new Random().nextInt(length - 1) + 1 : 0;
			//将下拉框的选项的值设置为枚举值
			Event.setValue(EventResultEnum.STRING.setValue(elements.get(index).getText()));
			//进行随机选择，但不选择第一个元素，避免第一个元素为空或为“请选择”之类的选项
			elements.get(index).click();
			
		}
	}
	
	/**
	 * <p>
	 * 该方法用于对标准的web下拉框（由标签select和标签option构成）根据选项的内容进行选择，例如：<br>
	 * &lt;select&nbsp;id='test'&gt;<br>
	 * &emsp;&lt;option&gt;男&lt;/option&gt;<br>
	 * &emsp;&lt;option&gt;女&lt;/option&gt;<br>
	 * &emsp;&lt;option&gt;其他&lt;/option&gt;<br>
	 * &lt;/select&gt;<br>
	 * 对于该标准的下拉框选项，只需要定位到//select[@id='test']，得到其WebElement对象即可。
	 * </p>
	 * @param element 通过查找页面得到的控件元素对象
	 * @param optionStr 选项的内容
	 * @throws NoSuchElementException 指定的选项内容不存在时抛出
	 */
	static void select(WebElement element, String optionStr) {
		//定义Select类对象
		Select select = new Select(element);
		
		//获取select的所有选项的选项值，并判断哪个选项与传入的值对应
		int index = select.getOptions().stream().map(e -> e.getText()).collect(Collectors.toList()).indexOf(optionStr);
		
		//判断选项值是否有与传入的选项值对应的选项，若没有，则抛出异常
		if (index == -1) {
			throw new NoSuchElementException("不存在的选项内容：" + optionStr);
		}
		
		select.selectByIndex(index);
		
		//将下拉框的选项的值设置为枚举值
		Event.setValue(EventResultEnum.STRING.setValue(optionStr));
	}
	
	/**
	 * <p>
	 * 该方法用于对非标准的web标准下拉框（由input、button和大量的div标签组成）根据指定的选项内容进行选择。对于非标准的下拉框，需要传入的是选项列表，例如：<br>
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
	 * </p>
	 * 
	 * @param elements 通过查找页面得到的选项组对象
	 * @param optionStr 选项的内容
	 * @throws NoSuchElementException 指定的选项内容不存在时抛出
	 */
	static void select(List<WebElement> elements, String optionStr) {
		//获取select的所有选项的选项值，并判断哪个选项与传入的值对应
		int index = elements.stream().map(e -> e.getText()).collect(Collectors.toList()).indexOf(optionStr);
		
		//判断选项值是否有与传入的选项值对应的选项，若没有，则抛出异常
		if (index == -1) {
			throw new NoSuchElementException("不存在的选项内容：" + optionStr);
		}
		
		elements.get(index).click();
		
		//将下拉框的选项的值设置为枚举值
		Event.setValue(EventResultEnum.STRING.setValue(optionStr));
	}
}
