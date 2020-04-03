package pres.auxiliary.work.n.testcase.file;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import pres.auxiliary.work.n.testcase.templet.LabelType;

/**
 * <p><b>文件名：</b>JiraTestCaseWrite.java</p>
 * <p><b>用途：</b>用于对上传jira测试用例模板，通过该类构造的用例文件，在使用测试用例
 * 模板类写入用例时可以不用指定相应的字段关系。该类中包含部分个性的方法，以方便编写
 * 测试用例</p>
 * <p><b>编码时间：</b>2020年4月3日下午4:23:05</p>
 * <p><b>修改时间：</b>2020年4月3日下午4:23:05</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public class JiraTestCaseWrite extends TestCaseWrite {
	/**
	 * 通过测试文件模板xml配置文件和测试用例文件来构造WriteTestCase类。当配置文件中
	 * 只存在一个sheet标签时，则直接获取其对应sheet下所有column标签的id属性；若存在
	 * 多个sheet标签时，则读取第一个sheet标签，如需切换sheet标签，则可调用{@link #switchSheet(String)} 方法。
	 * 
	 * @param configFile 测试文件模板xml配置文件类对象
	 * @param caseFile   测试用例文件类对象
	 * @throws IncorrectFileException 文件格式或路径不正确时抛出的异常
	 */
	public JiraTestCaseWrite(File configFile, File caseFile) {
		super(configFile, caseFile);
		
		//TODO 添加与测试用例模板的关联，若测试用例模板字段有所改变，则在此改变关联字段
		relevanceCase(JiraFieldIdType.STEP.getName(), LabelType.STEP.getName());
		relevanceCase(JiraFieldIdType.EXCEPT.getName(), LabelType.EXCEPT.getName());
		relevanceCase(JiraFieldIdType.PRECONDITION.getName(), LabelType.PRECONDITION.getName());
		relevanceCase(JiraFieldIdType.PRIORITY.getName(), LabelType.RANK.getName());
		relevanceCase(JiraFieldIdType.TITLE.getName(), LabelType.TITLE.getName());
	}
	
	/**
	 * 用于写入标题信息，由于标题唯一且不换行，故重复调用该方法时将覆盖原写入的内容。
	 * 写入的内容可以使用替换符，具体规则可以参见{@link #addContent(String, String...)}
	 * @param title 标题
	 * @return 类本身
	 */
	public JiraTestCaseWrite addTitle(String title) {
		//清除原有的内容
		clearContent(JiraFieldIdType.TITLE.getName());
		//重新将标题数据写入到用例中
		addContent(JiraFieldIdType.TITLE.getName(), title);
		
		return this;
	}
	
	/**
	 * 用于写入步骤信息
	 * @param stpes 步骤
	 * @return 类本身
	 */
	public JiraTestCaseWrite addStep(String... steps) {
		//写入步骤信息
		addContent(JiraFieldIdType.STEP.getName(), steps);
		
		return this;
	}
	
	/**
	 * 用于写入预期信息
	 * @param stpes 预期
	 * @return 类本身
	 */
	public JiraTestCaseWrite addExcept(String... excepts) {
		//写入预期信息
		addContent(JiraFieldIdType.EXCEPT.getName(), excepts);
		
		return this;
	}
	
	/**
	 * 由于步骤与预期是对应的，故可使用该方法写入一条步骤与预期信息
	 * @param step 步骤
	 * @param except 预期
	 * @return 类本身
	 */
	public JiraTestCaseWrite addStepAndExcept(String step, String except) {
		//写入步骤信息
		addContent(JiraFieldIdType.STEP.getName(), step);
		//写入预期信息
		addContent(JiraFieldIdType.EXCEPT.getName(), except);
		
		return this;
	}
	
	/**
	 * 用于根据数据有效性顺序选择相应的模块信息，重复调用该方法时将覆盖原写入的内容。
	 * @param index 模块对应的数据有效性选项
	 * @return 类本身
	 */
	public JiraTestCaseWrite addFolder(int index) {
		//清除原有的内容
		clearContent(JiraFieldIdType.FOLDER.getName());
		//由于传入的本身为数字，故可直接将数字转换成字符串后传入到addContent中
		addContent(JiraFieldIdType.FOLDER.getName(), String.valueOf(index));
		
		return this;
	}
	
	/**
	 * 根据关键词，匹配相应的模块信息，若未传入信息，则不写入信息，若能匹配信息，则会有以下三种情况：
	 * <ol>
	 * <li>匹配一个结果，则直接存入结果</li>
	 * <li>匹配多个结果，则存入第一个命中的结果</li>
	 * <li>无匹配结果，则以“key1/key2/key3/.../keyN/”的形式拼接字符串</li>
	 * </ol>
	 * 重复调用该方法时将覆盖原写入的内容。
	 * @param keys 关键词组
	 * @return 类本身
	 */
	public JiraTestCaseWrite addFolder(String... keys) {
		//清除原有的内容
		clearContent(JiraFieldIdType.FOLDER.getName());
				
		//若未传入关键词，则不填写信息
		if (keys == null) {
			return this;
		}
		
		//获取数据有效性
		ArrayList<String> dataList = fieldMap.get(JiraFieldIdType.FOLDER.getName()).matchDataValidation(keys);
		//存储最终得到的模块信息
		StringBuilder dataText = new StringBuilder();
		
		//匹配模块信息，分为三种情况：
		//1.命中一个结果，则直接存入结果
		//2.命中多个结果，则存入第一个命中的结果
		//3.未命中结果，则以“key1/key2/key3/.../keyN/”的形式拼接字符串
		
		if (dataList.size() >= 1) {
			dataText.append(dataList.get(0));
		} else {
			Arrays.stream(keys).forEach(text -> {
				//拼接关键词
				dataText.append("/" + text);
			});
		}
		
		//写入得到的关键词
		addContent(JiraFieldIdType.FOLDER.getName(), String.valueOf(dataText.toString()));
		return this;
	}
	
	/**
	 * <p><b>文件名：</b>JiraTestCaseWrite.java</p>
	 * <p><b>用途：</b>用于枚举出jira用例文件模板xml文件中所有字段</p>
	 * <p><b>编码时间：</b>2020年4月3日下午4:04:30</p>
	 * <p><b>修改时间：</b>2020年4月3日下午4:04:30</p>
	 * @author 彭宇琦
	 * @version Ver1.0
	 * @since JDK 12
	 *
	 */
	enum JiraFieldIdType {
		/**
		 * 标题（Name）
		 */
		TITLE("标题"),
		/**
		 * 目的（Objective）
		 */
		OBJECTIVE("目的"),
		/**
		 * 前置条件（Precondition）
		 */
		PRECONDITION("前置条件"),
		/**
		 * 步骤（Test Script (Step-by-Step) - Step）
		 */
		STEP("步骤"),
		/**
		 * 预期（Test Script (Step-by-Step) - Expected Result）
		 */
		EXCEPT("预期"),
		/**
		 * 模块（Folder）
		 */
		FOLDER("模块"),
		/**
		 * 状态（Status）
		 */
		STATUS("状态"),
		/**
		 * 优先级（Priority）
		 */
		PRIORITY("优先级"),
		/**
		 * 项目（Component）
		 */
		COMPONENT("项目"),
		/**
		 * 设计者（Owner）
		 */
		OWNER("设计者"),
		/**
		 * 关联需求（Coverage (Issues)）
		 */
		ISSUES("关联需求"),
		/**
		 * 关键用例
		 */
		CASE_KEY("关键用例"),
		;
		//用于存储枚举的名称
		private String name = "";
		
		/**
		 * 初始化枚举值
		 * @param value 枚举值
		 */
		private JiraFieldIdType(String name) {
			this.name = name;
		}

		/**
		 * 用于返回枚举中存储的内容，即jira用例文件模板中的字段ID
		 * @return 枚举值
		 */
		public String getName() {
			return name;
		}
	}
}
