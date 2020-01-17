package pres.auxiliary.selenium.event;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

import pres.auxiliary.selenium.event.inter.JudgeEventInter;
import pres.auxiliary.selenium.event.inter.JudgeKeyInter;
import pres.auxiliary.selenium.event.inter.JudgeTextInter;
import pres.auxiliary.selenium.tool.RecordTool;

/**
 * <p><b>文件名：</b>JudgeEvent.java</p>
 * <p><b>用途：</b>定义了对控件进行判断操作相关的方法，可通过该类，对页面数据进行基判断</p>
 * <p><b>编码时间：</b>2019年9月2日下午7:09:13</p>
 * <p><b>修改时间：</b>2019年11月29日上午9:53:37</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public class JudgeEvent extends CommenElementEvent implements JudgeEventInter {

	/**
	 * 构造JudgeEvent类对象
	 * @param driver WebDriver类对象
	 */
	public JudgeEvent(WebDriver driver) {
		super(driver);
	}

	@Override
	public Event judgeKey(String name, boolean keyFull, String... keys) {
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
					//调用JudgeKeyInter接口的静态方法，将从页面上搜索到的控件元素对象传入其中
					JudgeKeyInter.judgeKey(element, keyFull, keys);
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
			//记录步骤
			// 拼接传入的关键词
			String text = "";
			for (String key : keys) {
				text += (key + "、");
			}
			text = text.substring(0, text.length() - 1);
			
			//根据keyFull的不同记录的文本将有改变
			if (keyFull) {
				RecordTool.recordStep("判断“" + name + "”对应的控件中的内容是否包含所有关键词（关键词为：" + text + "）");
			} else {
				RecordTool.recordStep("判断“" + name + "”对应的控件中的内容是否包含部分关键词（关键词为：" + text + "）");
			}
		}
		
		//返回Event类
		return Event.newInstance(getDriver());
	}

	@Override
	public Event judgeText(String name, boolean keyFull, String key) {
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
					//调用JudgeTextInter接口的静态方法，将从页面上搜索到的控件元素对象传入其中
					JudgeTextInter.judgeText(element, keyFull, key);
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
			//根据keyFull的不同记录的文本将有改变
			if (keyFull) {
				RecordTool.recordStep("判断“" + name + "”对应的控件中的内容是否与所有关键词“" + key + "”一致");
			} else {
				RecordTool.recordStep("判断“" + name + "”对应的控件中的内容是否包含关键词“" + key + "”");
			}
		}
		
		//返回Event类
		return Event.newInstance(getDriver());
	}

	@Override
	public Event judgeControl(String name) {
		//自动记录异常
		try {
			// 判断页面上是否存在该元素,若抛出TimeoutException或UnrecognizableLocationModeException异常则说明元素不存在
			try {
				judgeElementMode(name);
				Event.setValue(EventResultEnum.BOOLEAN_TRUE);
			} catch (TimeoutException | UnrecognizableLocationModeException e) {
				Event.setValue(EventResultEnum.BOOLEAN_FALSE);
			}
		} catch (Exception exception) {
			//捕捉到异常后将异常信息记录在工具中，并将异常抛出
			RecordTool.recordException(exception);
			throw exception;
		} finally {
			RecordTool.recordStep("判断页面是否包含“" + name + "”控件");
		}
		
		//返回Event类
		return Event.newInstance(getDriver());
	}

}
