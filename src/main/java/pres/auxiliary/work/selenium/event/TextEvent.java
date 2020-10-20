package pres.auxiliary.work.selenium.event;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import pres.auxiliary.work.selenium.brower.AbstractBrower;
import pres.auxiliary.work.selenium.element.AbstractBy.Element;
import pres.auxiliary.work.selenium.tool.RecognitionImage;
import pres.auxiliary.work.selenium.tool.Screenshot;

/**
 * <p><b>文件名：</b>TextEvent.java</p>
 * <p><b>用途：</b>定义了对控件文本操作相关的方法，可通过该类，对页面进行对控件输入，文本获取等操作</p>
 * <p><b>编码时间：</b>2019年9月6日上午9:28:59</p>
 * <p><b>修改时间：</b>2020年10月17日下午16:34:37</p>
 * @author 彭宇琦
 * @version Ver2.0
 * @since JDK 8
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
	 * 该方法通过控件名称或定位方式对页面元素进行定位来清空控件中的内容，主要用于清空文本框中已有的数据。该方法将存储被清空的
	 * 文本
	 * @param element {@link Element}对象
	 * @return 被清空的文本内容
	 * @throws TimeoutException 元素无法操作时抛出的异常 
	 * @throws NoSuchElementException 元素不存在或下标不正确时抛出的异常 
	 */
	public String clear(Element element) {
		//由于在until()方法中无法直接抛出元素不存在的异常，故此处直接调用返回元素的方法，让元素不存在的异常抛出
		element.getWebElement();
		
		//由于需要存储步骤，若直接调用getText方法进行返回时，其会更改存储的step，为保证step正确，故存储返回值进行返回
		resultText = getText(element);
		//由于在获取元素时，已对元素进行相应操作等待，故此处将不再重新获取
		element.getWebElement().clear();
		
		logText = "清除“" + element.getElementData().getName() + "”元素中的文本内容";
		return resultText;
	}

	/**
	 * 用于获取元素中的指定属性值的内容
	 * @param element {@link Element}对象
	 * @param attributeName 属性名称
	 * @return 对应属性的值
	 * @throws TimeoutException 元素无法操作时抛出的异常 
	 * @throws NoSuchElementException 元素不存在或下标不正确时抛出的异常 
	 */
	public String getAttributeValue(Element element, String attributeName) {
		//由于在until()方法中无法直接抛出元素不存在的异常，故此处直接调用返回元素的方法，让元素不存在的异常抛出
		element.getWebElement();
		
		//在指定的时间内判断是否能进行操作，若抛出StaleElementReferenceException异常，则重新获取元素
		resultText = wait.until((driver) -> {
			try {
				WebElement we = element.getWebElement();
				//定位到元素上
				locationElement(we);
				return we.getAttribute(attributeName);
			} catch (StaleElementReferenceException e) {
				element.againFindElement();
				throw e;
			}
		});
		
		logText = "获取“" + element.getElementData().getName() + "”元素的" + attributeName + "属性的属性值";
		return resultText;
	}

	/**
	 * 用于获取相应元素中的文本内容
	 * @param element {@link Element}对象
	 * @return 对应元素中的文本内容
	 * @throws TimeoutException 元素无法操作时抛出的异常 
	 * @throws NoSuchElementException 元素不存在或下标不正确时抛出的异常 
	 */
	public String getText(Element element) {
		//由于在until()方法中无法直接抛出元素不存在的异常，故此处直接调用返回元素的方法，让元素不存在的异常抛出
		element.getWebElement();
		
		//在指定的时间内判断是否能进行操作，若抛出StaleElementReferenceException异常，则重新获取元素
		resultText = wait.until((driver) -> {
			try {
				WebElement we = element.getWebElement();
				//定位到元素上
				locationElement(we);
				return "input".equalsIgnoreCase(we.getTagName()) ? we.getAttribute("value") : we.getText();
			} catch (StaleElementReferenceException e) {
				element.againFindElement();
				throw e;
			}
		});
		
		logText = "获取“" + element.getElementData().getName() + "”元素的文本内容";
		return resultText;
	}

	/**
	 * 用于在指定的控件中输入相应的内容
	 * @param element {@link Element}对象
	 * @param text 需要输入到控件中的
	 * @return 在控件中输入的内容
	 * @throws TimeoutException 元素无法操作时抛出的异常 
	 * @throws NoSuchElementException 元素不存在或下标不正确时抛出的异常 
	 */
	public String input(Element element, String text) {
		//由于在until()方法中无法直接抛出元素不存在的异常，故此处直接调用返回元素的方法，让元素不存在的异常抛出
		element.getWebElement();
		
		//在指定的时间内判断是否能进行操作，若抛出StaleElementReferenceException异常，则重新获取元素
		wait.until((driver) -> {
			try {
				WebElement we = element.getWebElement();
				//定位到元素上
				locationElement(we);
				we.sendKeys(text);
				return true;
			} catch (StaleElementReferenceException e) {
				element.againFindElement();
				throw e;
			}
		});
		
		logText = "在“" + element.getElementData().getName() + "”元素中输入" + text;
		resultText = text;
		
		return resultText;
	}
	
	/**
	 * 用于向页面上发送键盘按键，可使用{@link Keys}枚举类控制发送的特殊按键。
	 * 例如，需要在页面上按下“alt + d”，则可以传入<br>
	 * keyToSend(element, {@link Keys}.ALT, "d")
	 * 
	 * @param element {@link Element}对象
	 * @param keys 需要传入的按键，可传入{@link Keys}枚举类或字符串,若传入字符串，则只取字符串中第一个字母
	 * @return 发送按键组合，每个按键间用“ + ”字符串连接
	 * @throws TimeoutException 元素无法操作时抛出的异常 
	 * @throws NoSuchElementException 元素不存在或下标不正确时抛出的异常 
	 */
	public String keyToSend(Element element, CharSequence... keys) {
		//由于在until()方法中无法直接抛出元素不存在的异常，故此处直接调用返回元素的方法，让元素不存在的异常抛出
		element.getWebElement();
		
		//在指定的时间内判断是否能进行操作，若抛出StaleElementReferenceException异常，则重新获取元素
		wait.until((driver) -> {
			try {
				WebElement we = element.getWebElement();
				//定位到元素上
				locationElement(we);
				we.sendKeys(keys);
				return true;
			} catch (StaleElementReferenceException e) {
				element.againFindElement();
				throw e;
			}
		});
		
		StringBuilder textBul = new StringBuilder();
		Arrays.asList(keys).stream().map(key -> {
			//判断key是否为Keys枚举，若是，则调用其name()方法，否则调用toString()方法
			if (key instanceof Keys) {
				return ((Keys) key).name();
			} else {
				return key.toString();
			}
		}).forEach(text -> {
			textBul.append(text);
			textBul.append(" + ");
		});
		
		//删除最后多余的符号
		resultText = textBul.substring(0, textBul.lastIndexOf(" + "));
		logText = "在“" + element.getElementData().getName() + "”元素发送按键“" + resultText + "”";
		
		return resultText;
	}
	
	/**
	 * 用于对“数字+英文”类型的图片验证码进行输入的方法，根据验证码图片元素位置，识别图片中的验证码，
	 * 并将结果填入对应的文本框中。注意，该方法识别验证码成功的概率不高，在“数字+英文”的验证码模式下，
	 * 经常将数字识别为英文。
	 * 
	 * @param textElement 通过查找页面得到的文本框控件{@link Element}对象
	 * @param codeImageElement 通过查找页面得到的验证码图片控件{@link Element}对象
	 * @return 输入的内容
	 * @throws TimeoutException 元素无法操作时抛出的异常 
	 * @throws NoSuchElementException 元素不存在或下标不正确时抛出的异常 
	 */
	public String codeInput(Element textElement, Element codeImageElement) {
		//由于在until()方法中无法直接抛出元素不存在的异常，故此处直接调用返回元素的方法，让元素不存在的异常抛出
		codeImageElement.getWebElement();
		textElement.getWebElement();
		
		// 判断验证码信息是否加载，加载后，获取其Rectang对象
		Rectangle r = codeImageElement.getWebElement().getRect();
		// 构造截图对象，并创建截图
		Screenshot sc = new Screenshot(brower.getDriver(), new File("Temp"));
		File image = null;
		try {
			image = sc.creatImage("code");
		} catch (WebDriverException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 设置图片识别的语言包存放位置
		RecognitionImage.setTessdataPath(new File(TESSDATA));
		// 识别图片
		String text = RecognitionImage.judgeImage(image, r.x, r.y, r.width, r.height);

		// 删除生成的图片及文件夹
		image.delete();
		new File("Temp").delete();

		resultText = input(textElement, text);
		logText = "识别“" + codeImageElement.getElementData().getName() + "”图片中内容，在"
				+ textElement.getElementData().getName() + "元素中输入" + text;
		
		return resultText;
	}

	/**
	 * 该方法用于将一个指定的整数随机填写到传入的控件组中<br>
	 * <b><i>建议在指定值远远大于控件的数量时再使用该方法，否则将会出现不可预期的问题</i></b>
	 * 
	 * @param num 指定的整数
	 * @param textElements 通过查找页面得到的一组控件元素对象
	 * @return 由于涉及到多个文本框，故其返回值有多个，将以“值1,值2,值3...”的形式进行返回
	 * @deprecated 当前方法有些BUG，请勿调用，下个版本修复
	 * @throws TimeoutException 元素无法操作时抛出的异常 
	 * @throws NoSuchElementException 元素不存在或下标不正确时抛出的异常 
	 */
	@Deprecated
	public String avgIntergeInput(int num, Element... elements) {
		//TODO 存在BUG，需要修改
		//定义存储控件数量及需要随机的数量
		int contrlNum = elements.length;
		String inputNumText = "";
		String[] inputNum = new String[contrlNum];
		
		// 向下取整获得平均数
		int avgNum = num / contrlNum;
		// 向上取整获得差值
		int diffNum = (int) Math.ceil(avgNum / 10.0);

		//求取通过随机得出的值之和，用于计算最终其随机值之和与实际值的差值
		int sum = 0;
		//循环，生成控件个数个随机值，其随机值在给定数值的均值之前
		for (int i = 0; i < contrlNum; i++) {
			//注意：2 * diffNum为以下算式的简写：
			//minNum = avgNum - diffNum;
			//maxNum = avgNum + diffNum;
			//int ranNum = new Random().nextInt(maxNum - minNum + 1) + minNum;
			int ranNum = new Random().nextInt(2 * diffNum + 1) + (avgNum - diffNum);
			sum += ranNum;
			inputNum[i] = String.valueOf(ranNum);
		}
		
		//由于数值是随机的，可能会出现随机值相加不为指定值，故需要补上差值，但由于差值通过算法后不会很大，故可随机附加到一个控件值上
		if ( (diffNum = sum - num) != 0 ) {
			inputNum[new Random().nextInt(contrlNum)] = String.valueOf(Integer.valueOf(inputNum[new Random().nextInt(contrlNum)]) - diffNum);
		}
		
		//将随机值填写至控件中
		for (int i = 0; i < contrlNum; i++) {
			inputNumText += (input(elements[i], inputNum[i]) + ",");
		}
		
		return inputNumText.substring(0, inputNumText.length() - 1);
	}


	/**
	 * 用于向控件中上传指定的文件。
	 * 
	 * @param element {@link Element}对象
	 * @param updataFile 需要上传到控件中的文件
	 * @return 上传的文件路径
	 * @throws TimeoutException 元素无法操作时抛出的异常 
	 * @throws NoSuchElementException 元素不存在或下标不正确时抛出的异常 
	 */
	public String updataFile(Element element, File updataFile) {
		resultText = input(element, updataFile.getAbsolutePath());
		logText = "向“" + element.getElementData().getName() + "”元素中上传文件，文件路径为：“" + resultText + "”";
		
		return resultText;
	}
}
