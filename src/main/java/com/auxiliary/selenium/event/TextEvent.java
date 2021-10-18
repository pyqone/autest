package com.auxiliary.selenium.event;

import java.io.File;
import java.util.Arrays;

import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import com.auxiliary.selenium.brower.AbstractBrower;
import com.auxiliary.selenium.element.Element;
import com.auxiliary.selenium.tool.RecognitionImage;
import com.auxiliary.selenium.tool.Screenshot;

/**
 * <p>
 * <b>文件名：</b>TextEvent.java
 * </p>
 * <p>
 * <b>用途：</b>定义了对控件文本操作相关的方法，可通过该类，对页面进行对控件输入，文本获取等操作
 * </p>
 * <p>
 * <b>编码时间：</b>2019年9月6日上午9:28:59
 * </p>
 * <p>
 * <b>修改时间：</b>2021年10月18日 上午11:37:14
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver2.1
 * @since JDK 1.8
 *
 */
public class TextEvent extends AbstractEvent {
	/**
	 * 定义验证码识别的制定文件夹名称
	 */
	private final String TESSDATA = "tessdata";

	/**
	 * 构造对象
	 * 
	 * @param brower 浏览器{@link AbstractBrower}类对象
	 */
	public TextEvent(AbstractBrower brower) {
		super(brower);
	}

	/**
	 * 该方法通过控件名称或定位方式对页面元素进行定位来清空控件中的内容，主要用于清空文本框中已有的数据。该方法将存储被清空的 文本
	 * 
	 * @param element {@link Element}对象
	 * @return 被清空的文本内容
	 * @throws TimeoutException       元素无法操作时抛出的异常
	 * @throws NoSuchElementException 元素不存在或下标不正确时抛出的异常
	 */
	public String clear(Element element) {
		// 由于需要存储步骤，若直接调用getText方法进行返回时，其会更改存储的step，为保证step正确，故存储返回值进行返回
		String resultText = getText(element);
		// 由于在获取元素时，已对元素进行相应操作等待，故此处将不再重新获取
		webElement.clear();

		// 清除获取文本的日志
		brower.getLogRecord().removeLog(1);
		
		String logText = "清除“%s”元素中的文本内容";
		brower.getLogRecord().recordLog(String.format(logText, element.getElementData().getName()));
		
		return resultText;
	}

	/**
	 * 用于获取元素中的指定属性值的内容
	 * 
	 * @param element       {@link Element}对象
	 * @param attributeName 属性名称
	 * @return 对应属性的值
	 * @throws TimeoutException       元素无法操作时抛出的异常
	 * @throws NoSuchElementException 元素不存在或下标不正确时抛出的异常
	 */
	public String getAttributeValue(Element element, String attributeName) {
		String resultText = actionOperate(element, (e) -> {
			return e.getWebElement().getAttribute(attributeName);
		});

		String logText = "获取“%s”元素的“%s”属性的属性值";
		brower.getLogRecord().recordLog(String.format(logText, element.getElementData().getName(), attributeName));

		return resultText;
	}

	/**
	 * 用于获取相应元素中的文本内容
	 * 
	 * @param element {@link Element}对象
	 * @return 对应元素中的文本内容
	 * @throws TimeoutException       元素无法操作时抛出的异常
	 * @throws NoSuchElementException 元素不存在或下标不正确时抛出的异常
	 */
	public String getText(Element element) {
		String resultText = actionOperate(element, (e) -> {
			WebElement we = e.getWebElement();
			return "input".equalsIgnoreCase(we.getTagName()) ? we.getAttribute("value") : we.getText();
		});

		String logText = "获取“%s”元素的文本内容";
		brower.getLogRecord().recordLog(String.format(logText, element.getElementData().getName()));
		
		return resultText;
	}

	/**
	 * 用于在指定的控件中输入相应的内容
	 * 
	 * @param element {@link Element}对象
	 * @param text    需要输入到控件中的内容
	 * @return 在控件中输入的内容
	 * @throws TimeoutException       元素无法操作时抛出的异常
	 * @throws NoSuchElementException 元素不存在或下标不正确时抛出的异常
	 */
	public String input(Element element, String text) {
		actionOperate(element, (e) -> {
			e.getWebElement().sendKeys(text);
			return text;
		});

		String logText = "在“%s”元素中输入“%s”";
		brower.getLogRecord().recordLog(String.format(logText, element.getElementData().getName(), text));

		return text;
	}

	/**
	 * 用于向页面上发送键盘按键，可使用{@link Keys}枚举类控制发送的特殊按键。 例如，需要在页面上按下“alt + d”，则可以传入<br>
	 * keyToSend(element, {@link Keys}.ALT, "d")
	 * 
	 * @param element {@link Element}对象
	 * @param keys    需要传入的按键，可传入{@link Keys}枚举类或字符串,若传入字符串，则只取字符串中第一个字母
	 * @return 发送按键组合，每个按键间用“ + ”字符串连接
	 * @throws TimeoutException       元素无法操作时抛出的异常
	 * @throws NoSuchElementException 元素不存在或下标不正确时抛出的异常
	 */
	public String keyToSend(Element element, CharSequence... keys) {
		actionOperate(element, (e) -> {
			e.getWebElement().sendKeys(keys);
			return "";
		});

		StringBuilder textBul = new StringBuilder();
		Arrays.asList(keys).stream().map(key -> {
			// 判断key是否为Keys枚举，若是，则调用其name()方法，否则调用toString()方法
			if (key instanceof Keys) {
				return ((Keys) key).name();
			} else {
				return key.toString();
			}
		}).forEach(text -> {
			textBul.append(text);
			textBul.append(" + ");
		});

		// 删除最后多余的符号
		String resultText = textBul.substring(0, textBul.lastIndexOf(" + "));
		String logText = "在“%s”元素发送按键“%s”";
		
		brower.getLogRecord().recordLog(String.format(logText, element.getElementData().getName(), resultText));

		return resultText;
	}

	/**
	 * 用于对“数字+英文”类型的图片内容进行识别的方法，根据图片元素位置，识别图片中的内容。
	 * 注意，该方法识别成功的概率不高，在“数字+英文”的模式下，经常将数字识别为英文。
	 * 
	 * @param element 图片元素
	 * @return 识别图片的结果
	 */
	public String getImageText(Element element) {
		actionOperate(element, null);
		// 判断验证码信息是否加载，加载后，获取其Rectang对象
		Rectangle r = webElement.getRect();
		// 构造截图对象，并创建截图
		Screenshot sc = new Screenshot(brower.getDriver(), new File("Temp"));
		File image = sc.creatImage("code");

		// 设置图片识别的语言包存放位置
		RecognitionImage.setTessdataPath(new File(TESSDATA));
		// 识别图片，并存储至结果中
		String resultText = RecognitionImage.judgeImage(image, r.x, r.y, r.width, r.height);

		String logText = "识别“%s”图片中内容，内容为：%s";
		brower.getLogRecord().recordLog(String.format(logText, element.getElementData().getName(), resultText));

		return resultText;
	}

	/**
	 * 用于向控件中上传指定的文件。
	 * 
	 * @param element    {@link Element}对象
	 * @param updataFile 需要上传到控件中的文件
	 * @return 上传的文件路径
	 * @throws TimeoutException       元素无法操作时抛出的异常
	 * @throws NoSuchElementException 元素不存在或下标不正确时抛出的异常
	 */
	public String updataFile(Element element, File updataFile) {
		String resultText = input(element, updataFile.getAbsolutePath());
		
		// 删除输入事件的日志
		brower.getLogRecord().removeLog(1);
		String logText = "向“%s”元素中上传文件，文件路径为：“" + resultText + "”";
		brower.getLogRecord().recordLog(String.format(logText, element.getElementData().getName(), resultText));

		return resultText;
	}
}
