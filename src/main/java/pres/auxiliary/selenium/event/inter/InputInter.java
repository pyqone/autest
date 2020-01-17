package pres.auxiliary.selenium.event.inter;

import org.openqa.selenium.WebElement;

import pres.auxiliary.selenium.event.Event;
import pres.auxiliary.selenium.event.EventResultEnum;

/**
 * <p><b>文件名：</b>InputInter.java</p>
 * <p><b>用途：</b>该接口定义键盘输入事件的实现方法，可直接使用</p>
 * <p><b>编码时间：</b>2019年9月4日上午8:19:53</p>
 * <p><b>修改时间：</b>2019年9月4日上午8:19:53</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public interface InputInter {
	/**
	 * 根据定位得到的控件元素对象，在其控件中进行输入指定的内容
	 * @param element 通过查找页面得到的控件元素对象
	 * @param text 待输入的内容
	 */
	static void input(WebElement element, String text) {
		//控件中输入内容
		element.sendKeys(text);
		//将输入的内容存储至枚举中
		Event.setValue(EventResultEnum.STRING.setValue(text));
	}
}
