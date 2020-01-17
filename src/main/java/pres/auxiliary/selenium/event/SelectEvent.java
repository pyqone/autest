package pres.auxiliary.selenium.event;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.UnexpectedTagNameException;

import pres.auxiliary.selenium.event.inter.SelectEventInter;
import pres.auxiliary.selenium.event.inter.SelectFirstInter;
import pres.auxiliary.selenium.event.inter.SelectInter;
import pres.auxiliary.selenium.event.inter.SelectLastInter;
import pres.auxiliary.selenium.tool.RecordTool;

/**
 * <p><b>文件名：</b>SelectEvent.java</p>
 * <p><b>用途：</b>定义了对控件进行点击操作相关的方法，可通过该类，对页面的下拉框控件进行选择操作</p>
 * <p><b>编码时间：</b>2019年9月29日下午3:55:34</p>
 * <p><b>修改时间：</b>2019年11月29日上午9:53:37</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public class SelectEvent extends CommenElementEvent implements SelectEventInter {
	/**
	 * 构造SelectEvent类对象
	 * @param driver WebDriver类对象
	 */
	public SelectEvent(WebDriver driver) {
		super(driver);
	}

	@Override
	public Event selectLast(String name) {
		//自动记录异常
		try {
			//调用SelectLastInter接口的静态方法，将对页面下拉框进行选择，若下拉选项不是标准的下拉框，则会抛出UnexpectedTagNameException
			//异常，则此时再选择应对非标准下拉框的方法
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
						SelectLastInter.selectLast(judgeElementMode(name));
						break;
					} catch (StaleElementReferenceException e) {
						continue;
					}
				}
			} catch (UnexpectedTagNameException e) {
				//非标准下拉框无法代码高亮
				// 为避免出现抛出StaleElementReferenceException（页面过期异常），则通过循环的方式，
				// 当抛出异常时，则重新获取并操作，若未抛出，则结束循环
				while (true) {
					try {
						SelectLastInter.selectLast(judgeElementModes(name));
						break;
					} catch (StaleElementReferenceException sere) {
						continue;
					}
				}
			}
		} catch (Exception exception) {
			//捕捉到异常后将异常信息记录在工具中，并将异常抛出
			RecordTool.recordException(exception);
			throw exception;
		} finally {
			RecordTool.recordStep("选择“" + name + "”对应控件中的“" + Event.getStringValve() + "”选项");
		}
		
		//返回Event类
		return Event.newInstance(getDriver());
	}

	@Override
	public Event select(String name, int option) {
		//自动记录异常
		try {
			//调用SelectInter接口的静态方法，将对页面下拉框进行选择，若下拉选项不是标准的下拉框，则会抛出UnexpectedTagNameException
			//异常，则此时再选择应对非标准下拉框的方法
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
						SelectInter.select(judgeElementMode(name), option);
						break;
					} catch (StaleElementReferenceException e) {
						continue;
					}
				}
			} catch (UnexpectedTagNameException e) {
				//非标准下拉框无法代码高亮
				// 为避免出现抛出StaleElementReferenceException（页面过期异常），则通过循环的方式，
				// 当抛出异常时，则重新获取并操作，若未抛出，则结束循环
				while (true) {
					try {
						SelectInter.select(judgeElementModes(name), option);
						break;
					} catch (StaleElementReferenceException sere) {
						continue;
					}
				}
			}
		} catch (Exception exception) {
			//捕捉到异常后将异常信息记录在工具中，并将异常抛出
			RecordTool.recordException(exception);
			throw exception;
		} finally {
			RecordTool.recordStep("选择“" + name + "”对应控件中的“" + Event.getStringValve() + "”选项");
		}
		
		//返回Event类
		return Event.newInstance(getDriver());
	}

	@Override
	public Event selectFirst(String name) {
		//自动记录异常
		try {
			//调用SelectFirstInter接口的静态方法，将对页面下拉框进行选择，若下拉选项不是标准的下拉框，则会抛出UnexpectedTagNameException
			//异常，则此时再选择应对非标准下拉框的方法
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
						SelectFirstInter.selectFirst(element);
						break;
					} catch (StaleElementReferenceException e) {
						continue;
					}
				}
			} catch (UnexpectedTagNameException e) {
				// 为避免出现抛出StaleElementReferenceException（页面过期异常），则通过循环的方式，
				// 当抛出异常时，则重新获取并操作，若未抛出，则结束循环
				while (true) {
					try {
						//非标准下拉框无法代码高亮
						SelectFirstInter.selectFirst(judgeElementModes(name));
						break;
					} catch (StaleElementReferenceException sere) {
						continue;
					}
				}
			}
		} catch (Exception exception) {
			//捕捉到异常后将异常信息记录在工具中，并将异常抛出
			RecordTool.recordException(exception);
			throw exception;
		} finally {
			RecordTool.recordStep("选择“" + name + "”对应控件中的“" + Event.getStringValve() + "”选项");
		}
		
		//返回Event类
		return Event.newInstance(getDriver());
	}

	@Override
	public Event select(String name, String optionStr) {
		//自动记录异常
		try {
			//调用SelectFirstInter接口的静态方法，将对页面下拉框进行选择，若下拉选项不是标准的下拉框，则会抛出UnexpectedTagNameException
			//异常，则此时再选择应对非标准下拉框的方法
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
						SelectInter.select(element, optionStr);
						break;
					} catch (StaleElementReferenceException e) {
						continue;
					}
				}
			} catch (UnexpectedTagNameException e) {
				//非标准下拉框无法代码高亮
				// 为避免出现抛出StaleElementReferenceException（页面过期异常），则通过循环的方式，
				// 当抛出异常时，则重新获取并操作，若未抛出，则结束循环
				while (true) {
					try {
						SelectInter.select(judgeElementModes(name), optionStr);
						break;
					} catch (StaleElementReferenceException sere) {
						continue;
					}
				}
			}
		} catch (Exception exception) {
			//捕捉到异常后将异常信息记录在工具中，并将异常抛出
			RecordTool.recordException(exception);
			throw exception;
		} finally {
			RecordTool.recordStep("选择“" + name + "”对应控件中的“" + Event.getStringValve() + "”选项");
		}
		
		//返回Event类
		return Event.newInstance(getDriver());
	}
}
