package pres.auxiliary.work.selenium.event;

import java.util.Random;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/**
 * <p><b>文件名：</b>SelectEvent.java</p>
 * <p><b>用途：</b>定义了对控件进行选择操作相关的方法，可通过该类，对页面的下拉框控件进行选择操作
 * 注意，该类中的方法仅对标准的下拉选项框（有<select>与<option>标签组成的下拉框）进行操作，对于非标准的
 * 下拉框无法选择
 * </p>
 * <p><b>编码时间：</b>2019年9月29日下午3:55:34</p>
 * <p><b>修改时间：</b>2020年5月11日上午7:55:26</p>
 * @author 彭宇琦
 * @version Ver2.0
 * @since JDK 12
 *
 */
public class SelectEvent extends AbstractEvent {
	/**
	 * 构造SelectEvent类对象
	 * @param driver WebDriver类对象
	 */
	public SelectEvent(WebDriver driver) {
		super(driver);
	}

	/**
	 * 选择下拉框中的最后一个元素
	 * @param element 下拉框控件对象
	 * @return 选择的选项内容
	 */
	public String selectLast(WebElement element) {
		return select(element, -1);
	}
	
	/**
	 * 选择下拉框中的指定下标的元素，下标支持从后向前获取，传入的下标
	 * 与元素实际所在位置一致，当传入0时，则表示随机获取一个元素，如：<br>
	 * {@code select(element, 1)}表示选择下拉框的第1个元素<br>
	 * {@code select(element, 0)}表示选择下拉框元素个数范围内随机一个元素<br>
	 * {@code select(element, -1)}表示选择下拉框的倒数第1个元素<br>
	 * @param element 下拉框控件对象
	 * @return 选择的选项内容
	 */
	public String select(WebElement element, int index) {
		//元素高亮
		elementHight(element);
		//在等待时间内判断元素是否可以点击，若可以点击元素，则进行点击事件
		String text = wait.until(driver -> {
			try {
				//定义Select类对象
				Select select = new Select(element);
				int newIndex = getIndex(select.getAllSelectedOptions().size(), index);
				//选择元素
				select.selectByIndex(newIndex);
				return select.getOptions().get(newIndex).getText();
			} catch (Exception e) {
				return null;
			}
		});
		
		step = "选择“" + ELEMENT_NAME + "”元素的第" + index + "个选项";
		
		return text;
	}

	/**
	 * 选择下拉框中的第一个元素
	 * @param element 下拉框控件对象
	 * @return 选择的选项内容
	 */
	public String selectFirst(WebElement element) {
		return select(element, 1);
	}

	/**
	 * 由于方法允许传入负数和特殊数字0为下标，并且下标的序号由1开始，
	 * 故可通过该方法对下标的含义进行转义，得到java能识别的下标
	 * @param length 元素的总长度
	 * @param index 传入的下标
	 * @return 可识别的下标
	 * @throws NoSuchElementException 当元素无法查找到时抛出的异常
	 */
	private int getIndex(int length, int index) {
		//判断元素下标是否超出范围，由于可以传入负数，故需要使用绝对值
		if (Math.abs(index) >= length) {
			throw new NoSuchElementException("指定的选项值大于选项的最大值。选项总个数：" + length + "，指定项：" + index);
		}
		
		//判断index的值，若大于0，则从前向后遍历，若小于0，则从后往前遍历，若等于0，则随机输入
		if (index > 0) {
			//选择元素，正数的选项值从1开始，故需要减小1
			return index - 1;
		} else if (index < 0) {
			//选择元素，由于index为负数，则长度加上选项值即可得到需要选择的选项
			return length + index;
		} else {
			//为0，则随机进行选择，但不选择第一个选项，避免出现选择到一个空选项
			return new Random().nextInt(length - 1) + 1;
		}
	}
}
