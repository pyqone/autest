package pres.auxiliary.work.old.testcase.writecase;

import java.io.File;
import java.io.IOException;

import pres.auxiliary.work.old.testcase.change.CaseTab;
import pres.auxiliary.work.old.testcase.change.Tab;

/**
 * 该类用于简单的编写一条测试用例
 * 
 * @author 彭宇琦
 * @version Ver1.1
 */
public class MyCase extends Case {
	// 定义测试用例基本的信息
	// 用于存储标题信息
	private StringBuilder title = new StringBuilder("");
	// 用于存储步骤信息
	private StringBuilder step = new StringBuilder("");
	// 用于存储预期信息
	private StringBuilder expectation = new StringBuilder("");
	// 用于存储关键词信息
	private StringBuilder keyword = new StringBuilder("");
	// 用于存储优先级信息
	private int rank = 2;
	// 用于存储前置条件
	private StringBuilder precondition = new StringBuilder("");
	
	/**
	 * 在创建了模版后可调用该构造方法，该构造会检测模版类中存储的模板文件保存路径及文件名，若模板文件保存路径及文件名的其中一项为空，
	 * 则抛出UndefinedDirectoryException异常，此时请使用带参构造
	 */
	public MyCase() {
		super();
	}

	/**
	 * 用于指定模板文件对象
	 * 
	 * @param excel
	 *            模板文件对象
	 * @throws IOException
	 */
	public MyCase(File excel) throws IOException {
		super(excel);
	}

	/**
	 * 该方法可分步写入前置条件，写入多条时可不带序号以及换行符，如：<br/>
	 * setPrecondition("已在添加信息界面", "所有信息均正确填写");<br/>
	 * 则调用getPrecondition()方法后，输出：<br/>
	 * 1.已在添加信息界面<br/>
	 * 2.所有信息均正确填写<br/>
	 * 
	 * @param preconditions
	 *            写入的多条前置条件
	 */
	public MyCase addPrecondition(String... preconditions) {
		// 清除原存储的信息
		this.precondition.delete(0, this.precondition.length());
		// 循环，分解preconditions，并拼接前置条件，存储precondition中
		for (int i = 0; i < preconditions.length; i++) {
			// 添加序号
			precondition.append((i + 1) + ".");
			// 添加传入的条件
			precondition.append(preconditions[i]);
			// 判断是否正在添加最后一个条件，若不是，则添加空行(\r\n)
			if (i != preconditions.length - 1) {
				precondition.append("\r\n");
			}
		}

		return this;
	}

	/**
	 * 该方法可分步写入预期，写入多条时可不带序号以及换行符，如：<br/>
	 * setExpectation("已在添加信息界面", "所有信息均正确填写");<br/>
	 * 则调用getExpectation()方法后，输出：<br/>
	 * 1.已在添加信息界面<br/>
	 * 2.所有信息均正确填写<br/>
	 * 
	 * @param expectations
	 *            写入的多条预期
	 * @return 类本身
	 */
	public MyCase addExpectation(String... expectations) {
		// 清除原存储的信息
		this.expectation.delete(0, this.expectation.length());
		// 循环，分解Expectations，并拼接预期，存储Expectation中
		for (int i = 0; i < expectations.length; i++) {
			// 添加序号
			expectation.append((i + 1) + ".");
			// 添加传入的条件
			expectation.append(expectations[i]);
			// 判断是否正在添加最后一个条件，若不是，则添加空行(\r\n)
			if (i != expectations.length - 1) {
				expectation.append("\r\n");
			}
		}

		return this;
	}

	/**
	 * 该方法可分步写入步骤，写入多条时可不带序号以及换行符，如：<br/>
	 * setStep("已在添加信息界面", "所有信息均正确填写");<br/>
	 * 则调用getStep()方法后，输出：<br/>
	 * 1.已在添加信息界面<br/>
	 * 2.所有信息均正确填写<br/>
	 * 
	 * @param steps
	 *            写入的多条步骤
	 */
	public MyCase addStep(String... steps) {
		// 清除原存储的信息
		this.step.delete(0, this.step.length());
		// 循环，分解steps，并拼接步骤，存储step中
		for (int i = 0; i < steps.length; i++) {
			// 添加序号
			step.append((i + 1) + ".");
			// 添加传入的条件
			step.append(steps[i]);
			// 判断是否正在添加最后一个条件，若不是，则添加空行(\r\n)
			if (i != steps.length - 1) {
				step.append("\r\n");
			}
		}

		return this;
	}

	/**
	 * 该方法用于添加标题
	 * 
	 * @param title
	 *            待添加的标题
	 * @return 类本身
	 */
	public MyCase addTitle(String title) {
		this.title.delete(0, this.title.length());
		this.title.append(title);
		return this;
	}

	/**
	 * 该方法用于添加关键词
	 * 
	 * @param keyword
	 *            待添加的关键词
	 * @return 类本身
	 */
	public MyCase addKeyword(String... keywords) {
		// 清除原存储的信息
		this.keyword.delete(0, this.keyword.length());
		// 循环，分解steps，并拼接步骤，存储step中
		for (int i = 0; i < keywords.length; i++) {
			// 添加传入的条件
			keyword.append(keywords[i]);
			// 判断是否正在添加最后一个条件，若不是，则添加空行(\r\n)
			if (i != keywords.length - 1) {
				keyword.append(",");
			}
		}
		return this;
	}

	/**
	 * 该方法用于添加优先级
	 * 
	 * @param rank
	 *            待添加的优先级
	 * @return 类本身
	 */
	public MyCase addRank(int rank) {
		// 判断传入的优先级是否在1~4之间，若不在，则按默认的数据进行添加
		if (0 < rank && 5 > rank) {
			this.rank = rank;
		}
		return this;
	}

	/**
	 * 该方法用于在编写用例结束时调用的方法，其作用是将编写的测试用例写入到文件中<br/>
	 * <b><i>NOTE：若在测试用例编写结束后不调用该方法，则测试用例无法被写入到文件中</i></b>
	 * 
	 * @return 标记类对象，用于标记该行的测试用例
	 * @throws IOException 
	 */
	public Tab end() throws IOException {
		//调用PresetCase类中的写入测试用例的方法
		after(title.toString(), step, expectation, keyword.toString(), rank, precondition.toString());
		
		//清除存储在类中的数据
		this.title.delete(0, this.title.length());
		this.step.delete(0, this.step.length());
		this.expectation.delete(0, this.expectation.length());
		this.keyword.delete(0, this.keyword.length());
		this.precondition.delete(0, this.precondition.length());
		
		//返回标记类对象
		return CaseTab.newInstence(new File(getSavePath() + getFileName()));
	}
	
	/**
	 * 该方法用于将类中存储的数据转存入xml文件中，以作为模版使用
	 */
	/*
	public void caseToXml() {
		//TODO 编写将用例存储在xml模版的方法
	}
	*/
}
