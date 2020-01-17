package pres.auxiliary.selenium.event;

import pres.auxiliary.tool.randomstring.RandomString;
import pres.auxiliary.tool.randomstring.StringMode;

/**
 * 该类提供根据固定文本格式来选择相应的事件，其相应的文本为：
 * <p>
 * 1.输入事件：<br>
 * 1.1 在 “XXX” 中 输入 “XXX”<br>
 * 1.2 在 “XXX” 中 随机 输入 6位 “XXX” 字符<br>
 * 1.3 在 “XXX” 中 随机 输入 6位 “XXX、XXX” 字符<br>
 * 1.4 在 “XXX” 中 随机 输入 6-10位 “XXX” 字符<br>
 * 1.5 在 “XXX” 中 随机 输入 6-10位 “XXX、XXX” 字符<br>
 * </p>
 * <p>
 * 2.清除事件：<br>
 * 2.1 清空 “XXX” 中的内容
 * </p>
 * <p>
 * 3.获取事件：<br>
 * 3.1 获取 “XXX” 中的内容
 * </p>
 * <p>
 * 4.选择事件：<br>
 * 4.1 选择 “XXX” 第X个 选项<br>
 * 4.2 选择 “XXX” 最后 一个选项
 * </p>
 * <p>
 * 5.点击事件：<br>
 * 5.1 点击 “XXX”<br>
 * 5.2 右击 “XXX”<br>
 * 5.3 双击 “XXX”
 * </p>
 * <p>
 * 6.定位窗体事件：<br>
 * 6.1 定位 “XXX” 窗体<br>
 * 6.2 定位“顶层窗体”
 * </p>
 * <p>
 * 7.判断事件：<br>
 * 7.1 判断 “XXX” 中是否 包含 “XXX”<br>
 * 7.2 判断 “XXX” 中是否 为 “XXX”<br>
 * 7.3 判断 “XXX” 是否存在
 * </p>
 *
 * @author 彭宇琦
 */
public class TextToOperation {
	// 定义事件类
	private Event event;

	public TextToOperation(Event event) {
		this.event = event;
	}

	/**
	 * 添加中文步骤
	 * 
	 * @param text
	 *            步骤内容
	 * @return 执行返回值
	 */
	public String step(String text) {
		return judgeEventType(text);
	}

	/**
	 * 用于判根据传入的信息，判断其信息对应的是哪一种事件
	 */
	private String judgeEventType(String text) {
		String name = null;
		try {
			// 处理控件名
			name = text.substring(text.indexOf("“") + 1, text.indexOf("”"));
		} catch (IndexOutOfBoundsException e) {
			// 若抛出下标越界异常，则抛出语法错误异常
			throw new IncorrectGrammarException("控件名缺失");
		}

		// 对传入的text进行判断
		if (text.indexOf("在") == 0) {
			return inputEvent(text.substring(text.indexOf("”") + 1), name);
		} else if (text.indexOf("清空") == 0) {
			return clearEvent(name);
		} else if (text.indexOf("获取") == 0) {
			return getEvent(name);
		} else if (text.indexOf("选择") == 0) {
			return String.valueOf(selectEvent(text.substring(text.indexOf("”") + 1), name));
		} else if (text.indexOf("击") == 1) {
			return clickEvent(text.substring(0, text.indexOf("“")), name);
		} else if (text.indexOf("定位") == 0) {
			return frameEvent(name);
		} else if (text.indexOf("判断") == 0) {
			//return judgeEvent(text.substring(text.indexOf("”") + 1), name);
			return "";
		} else {
			throw new IncorrectGrammarException("类型判断词无法识别");
		}
	}

	/**
	 * 处理定位事件
	 */
	private String frameEvent(String name) {
		String judeg_Text = "顶层窗体";
		// 判断是否是定位到顶层窗体
		if (judeg_Text.equals(name)) {
			event.switchRootFrame();
		} else {
			event.switchFrame(name);
		}

		return "";
	}

	/**
	 * 处理判断事件
	 */
	/*
	private String judgeEvent(String text, String name) {
		// 处理方式，先将text按照双引号进行切分，若无内容，则说明可能是判断元素是否存在，若有内容，则说明可能是文本判断
		// 按照双引号的前部分对text的内容进行切分，得到控件名和内容两部分，之后再分别处理
		String content = null;
		try {
			// 处理判断内容
			content = text.substring(text.indexOf("“") + 1, text.indexOf("”"));
			// 若未抛出下标越界异常，则说明可能是文本判断
			// 删除text判断内容的文本，以避免误判断
			text = new StringBuilder(text).delete(text.indexOf("“"), text.indexOf("”") + 1).toString();

			// 用于判断文本是包含包含还是需要一致
			boolean f = false;
			// 判断是文本包含还是元素包含（直接在if中加上对f变量的赋值，若text包含“为”，则赋为true，并可直接通过判断）
			if ((f = text.indexOf("为") > -1 ? true : false) || text.indexOf("包含") > -1) {
				// 添加事件
				return String.valueOf(event.getJudgeEvent().judgeText(name, content, f));
			} else {
				// 若抛出下标越界异常，则抛出语法错误异常
				throw new IncorrectGrammarException("无效的判断词");
			}
		} catch (StringIndexOutOfBoundsException e) {
			// 若抛出下标越界异常，则说明可能是元素判断
			if (text.indexOf("存在") > -1) {
				// 添加事件
				return String.valueOf(event.getJudgeEvent().judgeControl(name));
			} else {
				// 若未包含，则说明其表述有误
				throw new IncorrectGrammarException("无效的判断词");
			}
		}
	}
	*/

	/**
	 * 处理点击事件
	 */
	private String clickEvent(String text, String name) {
		// 判断需要使用哪一种点击事件
		if (text.indexOf("点击") == 0) {
			event.getClickEvent().click(name);
		} else if (text.indexOf("双击") == 0) {
			event.getClickEvent().doubleClick(name);
		} else if (text.indexOf("右击") == 0) {
			event.getClickEvent().rightClick(name);
		} else {
			throw new IncorrectGrammarException("选择下拉框选项语法缺失");
		}

		return "";
	}

	/**
	 * 处理选择事件
	 */
	private String selectEvent(String text, String name) {
		// 判断text中包含“第”或者“最后”，若头不包含，则抛出语法错误异常
		if (text.indexOf("第") > -1) {
			// 根据语法获取需要的选项
			int option = Integer.valueOf(text.substring(text.indexOf("第") + 1, text.indexOf("个")));
			// 添加事件
			return event.getSelectEvent().select(name, option).getStringValve();
		} else if (text.indexOf("最后") > -1) {
			return event.getSelectEvent().selectLast(name).getStringValve();
		} else {
			throw new IncorrectGrammarException("选择下拉框选项语法缺失");
		}
	}

	/**
	 * 处理获取事件
	 */
	private String getEvent(String name) {
		// 添加事件
		return event.getTextEvent().getText(name).getStringValve();
	}

	/**
	 * 处理清空事件
	 */
	private String clearEvent(String name) {
		// 添加事件
		return event.getTextEvent().clear(name).getStringValve();
	}

	/**
	 * 处理输入事件
	 */
	private String inputEvent(String text, String name) {
		// 按照双引号的前部分对text的内容进行切分，得到控件名和内容两部分，之后再分别处理
		String content = null;
		try {
			// 处理输入内容
			content = text.substring(text.indexOf("“") + 1, text.indexOf("”"));
		} catch (ArrayIndexOutOfBoundsException e) {
			// 若抛出下标越界异常，则抛出语法错误异常
			throw new IncorrectGrammarException("输入内容缺失");
		}

		// 判断是否包含“随机”二字，有，则按照随机的方式进行处理
		if (text.indexOf("随机") > -1) {
			// 定义随机字符串类，用于生成相应的随机字符串
			RandomString rs = new RandomString();
			// 循环，读取所有的模型
			for (String mode : content.split("、")) {
				// 判断需要加入到随机字符串中的模型
				if (mode.equalsIgnoreCase("中文")) {
					rs.addMode(StringMode.CH);
				} else if (mode.equalsIgnoreCase("小写字母")) {
					rs.addMode(StringMode.LOW);
				} else if (mode.equalsIgnoreCase("大写字母")) {
					rs.addMode(StringMode.CAP);
				} else if (mode.equalsIgnoreCase("数字")) {
					rs.addMode(StringMode.NUM);
				}
			}

			// 判断模型是否未加载
			if ("".equals(rs.getStringSeed())) {
				throw new IncorrectGrammarException("输入的随机模型信息有误");
			}

			// 在判断是否有长度分隔符，根据其结果来对事件进行处理
			if (text.indexOf("-") > -1) {
				// 获取间隔长度的最小值
				int min = Integer.valueOf(text.substring(text.indexOf("输入") + 2, text.indexOf("-")));
				// 获取间隔长度的最大值
				int max = Integer.valueOf(text.substring(text.indexOf("-") + 1, text.indexOf("位")));

				// 添加事件
				return event.getTextEvent().randomInput(name, min, max, rs.getStringSeed()).getStringValve();
			} else {
				// 获取需要输入的长度
				int len = Integer.valueOf(text.substring(text.indexOf("输入") + 2, text.indexOf("位")));

				// 添加事件
				return event.getTextEvent().randomInput(name, len, len, rs.getStringSeed()).getStringValve();
			}
		} else {
			// 若不是随机输入，则按照正常的输入方式添加事件
			return event.getTextEvent().input(name, content).getStringValve();
		}
	}
}
