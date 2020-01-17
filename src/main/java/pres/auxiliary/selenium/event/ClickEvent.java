package pres.auxiliary.selenium.event;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

import pres.auxiliary.selenium.event.inter.ClickEventInter;
import pres.auxiliary.selenium.event.inter.ClickInter;
import pres.auxiliary.selenium.event.inter.DoubleClickInter;
import pres.auxiliary.selenium.event.inter.RightClickInter;
import pres.auxiliary.selenium.tool.RecordTool;

/**
 * <p>
 * <b>文件名：</b>ClickEvent.java
 * </p>
 * <p>
 * <b>用途：</b>定义了对控件进行点击操作相关的方法，可通过该类，对页面进行基本的点击操作
 * </p>
 * <p>
 * <b>编码时间：</b>2019年8月29日下午3:24:34
 * </p>
 * <p>
 * <b>修改时间：</b>2019年11月29日上午9:53:37
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public class ClickEvent extends CommenElementEvent implements ClickEventInter {
	/**
	 * 构造ClickEvent类对象
	 * 
	 * @param driver WebDriver类对象
	 */
	public ClickEvent(WebDriver driver) {
		super(driver);
	}

	@Override
	public Event click(String name) {
		try {
			// 为避免出现抛出StaleElementReferenceException（页面过期异常），则通过循环的方式，
			// 当抛出异常时，则重新获取并操作，若未抛出，则结束循环
			while (true) {
				try {
					//获取元素
					element = judgeElementMode(name);
					/*
					// 获取原style属性
					style = element.getAttribute("style");
					// 控件高亮
					((JavascriptExecutor) getDriver()).executeScript("arguments[0].setAttribute('style',arguments[1])", element,
							style + "background:yellow;solid:red;");
					*/
					//修饰元素
					elementHight(element);
					
					//操作元素
					ClickInter.click(element);
					break;
				} catch (StaleElementReferenceException e) {
					continue;
				}
			}
			
			/*
			// 解除控件高亮，若因为页面跳转导致页面过期时，则不处理其异常
			try {
				((JavascriptExecutor) getDriver()).executeScript("arguments[0].setAttribute('style',arguments[1])",
						element, style);
			} catch (StaleElementReferenceException exception) {
			}
			*/
		} catch (TimeoutException timeException) {
			RecordTool.recordException(new TimeoutException("元素查找超时或不存在，请检查“" + name + "”对应元素的定位方式"));
			throw new TimeoutException("元素查找超时或不存在，请检查中“" + name + "”对应元素的定位方式");
		} catch (Exception exception) {
			// 捕捉到异常后将异常信息记录在工具中，并将异常抛出
			RecordTool.recordException(exception);
			throw exception;
		} finally {
			RecordTool.recordStep("点击“" + name + "”对应控件");
		}

		// 返回Event类
		return Event.newInstance(getDriver());
	}

	@Override
	public Event doubleClick(String name) {
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
					DoubleClickInter.doubleClick(getDriver(), element);
					break;
				} catch (StaleElementReferenceException e) {
					continue;
				}
			}
		} catch (TimeoutException timeException) {
			RecordTool.recordException(new TimeoutException("元素查找超时或不存在，请检查“" + name + "”对应元素的定位方式"));
			throw new TimeoutException("元素查找超时或不存在，请检查中“" + name + "”对应元素的定位方式");
		} catch (Exception exception) {
			// 捕捉到异常后将异常信息记录在工具中，并将异常抛出
			RecordTool.recordException(exception);
			throw exception;
		} finally {
			RecordTool.recordStep("双击“" + name + "”对应控件");
		}

		// 返回Event类
		return Event.newInstance(getDriver());
	}

	@Override
	public Event rightClick(String name) {
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
					RightClickInter.rightClick(getDriver(), element);
					break;
				} catch (StaleElementReferenceException e) {
					continue;
				}
			}
		} catch (TimeoutException timeException) {
			RecordTool.recordException(new TimeoutException("元素查找超时或不存在，请检查“" + name + "”对应元素的定位方式"));
			throw new TimeoutException("元素查找超时或不存在，请检查中“" + name + "”对应元素的定位方式");
		} catch (Exception exception) {
			// 捕捉到异常后将异常信息记录在工具中，并将异常抛出
			RecordTool.recordException(exception);
			throw exception;
		} finally {
			RecordTool.recordStep("右击“" + name + "”对应控件");
		}

		// 返回Event类
		return Event.newInstance(getDriver());
	}

}
