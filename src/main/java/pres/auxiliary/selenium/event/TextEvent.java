package pres.auxiliary.selenium.event;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.openqa.selenium.Rectangle;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import pres.auxiliary.selenium.event.inter.ClearInter;
import pres.auxiliary.selenium.event.inter.GetAttributeValueInter;
import pres.auxiliary.selenium.event.inter.GetTextInter;
import pres.auxiliary.selenium.event.inter.InputInter;
import pres.auxiliary.selenium.event.inter.TextEventInter;
import pres.auxiliary.selenium.tool.RecognitionImage;
import pres.auxiliary.selenium.tool.RecordTool;
import pres.auxiliary.selenium.tool.Screenshot;
import pres.auxiliary.tool.randomstring.RandomString;
import pres.auxiliary.tool.randomstring.StringMode;

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
public class TextEvent extends CommenElementEvent implements TextEventInter {
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

	@Override
	public Event clear(String name) {
		//自动记录异常
		try {
			// 为避免出现抛出StaleElementReferenceException（页面过期异常），则通过循环的方式，
			// 当抛出异常时，则重新获取并操作，若未抛出，则结束循环
			while (true) {
				try {
					//获取元素
					element = judgeElementMode(name);
					
					//修饰元素
					elementHight(element);
					
					//操作元素
					//调用ClearInter接口的静态方法，将从页面上搜索到的控件元素对象传入其中
					ClearInter.clear(element);
					break;
				} catch (StaleElementReferenceException e) {
					continue;
				}
			}
		} catch (Exception exception) {
			//捕捉到异常后将异常信息记录在工具中，并将异常抛出
			RecordTool.recordException(exception);
			throw exception;
		} finally {
			RecordTool.recordStep("清空“" + name + "”对应控件中的内容");
		}
		
		//返回Event类
		return Event.newInstance(getDriver());
	}

	@Override
	public Event getAttributeValue(String name, String attributeName) {
		//自动记录异常
		try {
			// 为避免出现抛出StaleElementReferenceException（页面过期异常），则通过循环的方式，
			// 当抛出异常时，则重新获取并操作，若未抛出，则结束循环
			while (true) {
				try {
					//获取元素
					element = judgeElementMode(name);
					
					//修饰元素
					elementHight(element);
					
					//操作元素
					//调用GetAttributeValueInter接口的静态方法，将从页面上搜索到的控件元素对象传入其中
					GetAttributeValueInter.getAttributeValue(element, attributeName);
					break;
				} catch (StaleElementReferenceException e) {
					continue;
				}
			}
		} catch (Exception exception) {
			//捕捉到异常后将异常信息记录在工具中，并将异常抛出
			RecordTool.recordException(exception);
			throw exception;
		} finally {
			RecordTool.recordStep("获取“" + name + "”对应控件" + attributeName + "属性的内容");
		}
				
		//返回Event类
		return Event.newInstance(getDriver());
	}

	@Override
	public Event getText(String name) {
		//自动记录异常
		try {
			// 为避免出现抛出StaleElementReferenceException（页面过期异常），则通过循环的方式，
			// 当抛出异常时，则重新获取并操作，若未抛出，则结束循环
			while (true) {
				try {
					//获取元素
					element = judgeElementMode(name);
					
					//修饰元素
					elementHight(element);
					
					//操作元素
					//调用GetTextInter接口的静态方法，将从页面上搜索到的控件元素对象传入其中
					GetTextInter.getText(element);
					break;
				} catch (StaleElementReferenceException e) {
					continue;
				}
			}
		} catch (Exception exception) {
			//捕捉到异常后将异常信息记录在工具中，并将异常抛出
			RecordTool.recordException(exception);
			throw exception;
		} finally {
			RecordTool.recordStep("获取“" + name + "”对应控件的内容");
		}
				
		//返回Event类
		return Event.newInstance(getDriver());
	}

	@Override
	public Event input(String name, String text) {
		//自动记录异常
		try {
			// 为避免出现抛出StaleElementReferenceException（页面过期异常），则通过循环的方式，
			// 当抛出异常时，则重新获取并操作，若未抛出，则结束循环
			while (true) {
				try {
					//获取元素
					element = judgeElementMode(name);
					
					//修饰元素
					elementHight(element);
					
					//操作元素
					//调用InputInter接口的静态方法，将从页面上搜索到的控件元素对象传入其中
					InputInter.input(element, text);
					break;
				} catch (StaleElementReferenceException e) {
					continue;
				}
			}
		} catch (Exception exception) {
			//捕捉到异常后将异常信息记录在工具中，并将异常抛出
			RecordTool.recordException(exception);
			throw exception;
		} finally {
			RecordTool.recordStep("向“" + name + "”对应控件中输入“" + text + "”");
		}
				
		//返回Event类
		return Event.newInstance(getDriver());
	}

	@Override
	public Event codeInput(String textName, String codeName) {
		// 判断验证码信息是否加载，加载后，获取其Rectang对象
		Rectangle r = judgeElementMode(codeName).getRect();
		// 构造截图对象，并创建截图
		Screenshot sc = new Screenshot(getDriver(), "Temp");
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

		return input(textName, text);
	}

	@Override
	public Event avgIntergeInput(int num, String... names) {
		//定义存储控件数量及需要随机的数量
		int contrlNum = names.length;
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
			input(names[i], inputNum[i]);
			inputNumText += (Event.getStringValve() + ",");
		}
		
		Event.setValue(EventResultEnum.STRING.setValue(inputNumText.substring(0, inputNumText.length() - 1)));
		return Event.newInstance(getDriver());
	}

	@Override
	public Event randomInput(String name, int minLength, int maxLength, StringMode... modes) {
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
			text = new RandomString(modes).toString(maxLength);
		} else {
			text = new RandomString(modes).toString(minLength, maxLength);
		}
		
		//调用input方法进行返回
		return input(name, text);
	}

	@Override
	public Event randomInput(String name, int minLength, int maxLength, String mode) {
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
			text = new RandomString(mode).toString(maxLength);
		} else {
			text = new RandomString(mode).toString(minLength, maxLength);
		}
		
		//调用input方法进行返回
		return input(name, text);
	}

	@Override
	public Event updataFile(String name, File updataFile) {
		return input(name, updataFile.getAbsolutePath());
	}

}
