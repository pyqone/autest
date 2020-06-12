package pres.auxiliary.work.selenium.event;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.openqa.selenium.Rectangle;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pres.auxiliary.tool.randomstring.RandomString;
import pres.auxiliary.tool.randomstring.StringMode;
import pres.auxiliary.work.selenium.element.Element;
import pres.auxiliary.work.selenium.tool.RecognitionImage;
import pres.auxiliary.work.selenium.tool.Screenshot;

/**
 * <p><b>文件名：</b>TextEvent.java</p>
 * <p><b>用途：</b>定义了对控件文本操作相关的方法，可通过该类，对页面进行对控件输入，文本获取等操作</p>
 * <p><b>编码时间：</b>2019年9月6日上午9:28:59</p>
 * <p><b>修改时间：</b>2019年11月29日上午9:53:37</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public class TextEvent extends AbstractEvent {
	/**
	 * 定义验证码识别的制定文件夹名称
	 */
	private final String TESSDATA = "tessdata";

	/**
	 * 构造TextEvent类对象
	 * @param driver WebDriver类对象
	 */
	public TextEvent(WebDriver driver) {
		super(driver);
	}

	/**
	 * 该方法通过控件名称或定位方式对页面元素进行定位来清空控件中的内容，主要用于清空文本框中已有的数据。该方法将存储被清空的
	 * 文本
	 * @param element 通过查找页面得到的控件元素对象
	 * @return 被清空的文本内容
	 */
	public String clear(Element element) {
		//由于需要存储步骤，若直接调用getText方法进行返回时，其会更改存储的step，为保证step正确，故存储返回值进行返回
		String text = getText(element);
				
		//对元素进行操作，若元素过期，则重新获取
		wait.until(driver -> {
			try {
				element.getWebElement().clear();
				return true;
			} catch (StaleElementReferenceException e) {
				element.findElement();
				return false;
			}
		});
		
		//记录操作
		step = "清空“" + ELEMENT_NAME + "”元素内的文本";
		
		return text;
	}

	/**
	 * 用于获取元素中的指定属性值的内容
	 * @param element 通过查找页面得到的控件元素对象
	 * @param attributeName 属性名称
	 * @return 对应属性的值
	 */
	public String getAttributeValue(Element element, String attributeName) {
		//等待元素中attributeName指向的属性内容出现
		wait.until(ExpectedConditions.attributeToBeNotEmpty(element.getWebElement(), attributeName));
		
		
		//记录操作
		step = "获取“" + ELEMENT_NAME + "”元素的" + attributeName +"属性的内容";
		
		//对元素进行操作，若元素过期，则重新获取
		return wait.until(driver -> {
			try {
				return element.getWebElement().getAttribute(attributeName);
			} catch (StaleElementReferenceException e) {
				element.findElement();
				return null;
			}
		});
	}

	/**
	 * 用于获取相应元素中的文本内容
	 * @param element 通过查找页面得到的控件元素对象
	 * @return 对应元素中的文本内容
	 */
	public String getText(Element element) {
		//记录操作
		step = "获取“" + ELEMENT_NAME + "”元素内的文本";
		
		//对元素进行操作，若元素过期，则重新获取
		return wait.until(driver -> {
				try {
					WebElement webElement = element.getWebElement();
					return "input".equalsIgnoreCase(webElement.getTagName()) ? webElement.getAttribute("value") : webElement.getText();
				} catch (StaleElementReferenceException e) {
					element.findElement();
					return null;
				}
			});
	}

	/**
	 * 用于在指定的控件中输入相应的内容
	 * @param element 通过查找页面得到的控件元素对象
	 * @param text 需要输入到控件中的
	 * @return 在控件中输入的内容
	 */
	public String input(Element element, String text) {
		//等待事件可操作后对事件进行操作
		wait.until(driver -> {
			try {
				element.getWebElement().sendKeys(text);
				return true;
			} catch (Exception e) {
				return false;
			}
		});
		
		//记录操作
		step = "获取“" + ELEMENT_NAME + "”元素内的文本";
		
		return text;
	}

	/**
	 * 用于对“数字+英文”类型的图片验证码进行输入的方法，根据验证码图片元素位置，识别图片中的验证码，
	 * 并将结果填入对应的文本框中。注意，该方法识别验证码成功的概率不高，在“数字+英文”的验证码模式下，
	 * 经常将数字识别为英文。
	 * 
	 * @param textElement 通过查找页面得到的文本框控件元素对象
	 * @param codeImageElement 通过查找页面得到的验证码图片控件元素对象
	 * @return 输入的内容
	 */
	public String codeInput(Element textElement, Element codeImageElement) {
		// 判断验证码信息是否加载，加载后，获取其Rectang对象
		Rectangle r = codeImageElement.getWebElement().getRect();
		// 构造截图对象，并创建截图
		Screenshot sc = new Screenshot(driver, "Temp");
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

		return input(textElement, text);
	}

	/**
	 * 该方法用于将一个指定的整数随机填写到传入的控件组中<br>
	 * <b><i>建议在指定值远远大于控件的数量时再使用该方法，否则将会出现不可预期的问题</i></b>
	 * 
	 * @param num 指定的整数
	 * @param textElements 通过查找页面得到的一组控件元素对象
	 * @return 由于涉及到多个文本框，故其返回值有多个，将以“值1,值2,值3...”的形式进行返回
	 */
	public String avgIntergeInput(int num, Element... elements) {
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
	 * 通过指定的随机字符串长度与指定的随机字符串模型枚举（{@link StringMode}枚举类），向控件中
	 * 随机输入字符串。
	 * @param element 通过查找页面得到的控件元素对象
	 * @param minLength 字符串最小长度，设为小于等于0的数值时则默认为1
	 * @param maxLength 字符串最大长度，设为小于等于0的数值时则默认为1，若需要指定字符串的输入长度，可设置minLength数值与maxLength一致
	 * @param modes {@link StringMode}枚举，指定字符串输入的类型，可传入多种模型，参见{@link RandomString#RandomString(StringMode...)}
	 * @return 在控件中输入的内容
	 */
	public String randomInput(Element element, int minLength, int maxLength, StringMode... modes) {
		return randomInput(element, minLength, maxLength, new RandomString(modes));
	}

	/**
	 * 通过指定的随机字符串长度与指定的随机字符串模型，向控件中随机输入字符串操作。
	 * @param element 通过查找页面得到的控件元素对象
	 * @param minLength 字符串最小长度，设为小于等于0的数值时则默认为1
	 * @param maxLength 字符串最大长度，设为小于等于0的数值时则默认为1，若需要指定字符串的输入长度，可设置minLength数值与maxLength一致
	 * @param mode 可用的随机字符串抽取范围，参见{@link RandomString#RandomString(String)}
	 * @return 在控件中输入的内容
	 */
	public String randomInput(Element element, int minLength, int maxLength, String mode) {
		return randomInput(element, minLength, maxLength, new RandomString(mode));
	}

	/**
	 * 用于向控件中上传指定的文件。<br>
	 * @param element 通过查找页面得到的控件元素对象
	 * @param updataFile 需要上传到控件中的文件
	 * @return 上传的文件路径
	 */
	public String updataFile(Element element, File updataFile) {
		//记录操作
		step = "向“" + ELEMENT_NAME + "”元素中的上传文件";
		return input(element, updataFile.getAbsolutePath());
	}
	
	/**
	 * 向控件随机输入信息的底层方法
	 * @param element 通过查找页面得到的控件元素对象
	 * @param minLength 字符串最小长度，设为小于等于0的数值时则默认为1
	 * @param maxLength 字符串最大长度，设为小于等于0的数值时则默认为1，若需要指定字符串的输入长度，可设置minLength数值与maxLength一致
	 * @param rs 随机字符类对象
	 * @return 在控件中输入的内容
	 */
	private String randomInput(Element element, int minLength, int maxLength, RandomString rs) {
		// 判断传入的参数是否小于0，小于0则将其都设置为1
		if (minLength < 0 || maxLength < 0) {
			minLength = 1;
			maxLength = 1;
		}

		// 判断传入的随机字符串最小生成长度是否大于最大生成长度，若大于，则调换两数字的位置
		if (minLength > maxLength) {
			int tem = minLength;
			minLength = maxLength;
			maxLength = tem;
		}
		
		//根据参数，生成随机字符串
		String text = "";
		if (minLength == maxLength) {
			text = rs.toString(maxLength);
		} else {
			text = rs.toString(minLength, maxLength);
		}
		
		//调用input方法进行返回
		return input(element, text);
	}
}
