package pres.auxiliary.work.testcase.file;

import java.io.File;

import org.dom4j.DocumentException;

import pres.auxiliary.work.testcase.templet.LabelType;

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
public class JiraTestCaseWrite extends CommonTestCaseWrite<JiraTestCaseWrite> {
	/**
	 * 通过测试文件模板xml配置文件和测试用例文件来构造JiraTestCaseWrite类。当配置文件中
	 * 只存在一个sheet标签时，则直接获取其对应sheet下所有column标签的id属性；若存在
	 * 多个sheet标签时，则读取第一个sheet标签，如需切换sheet标签，则可调用{@link #switchSheet(String)} 方法。
	 * 
	 * @param configFile 测试文件模板xml配置文件类对象
	 * @param caseFile   测试用例文件类对象
	 * @throws DocumentException 
	 * @throws IncorrectFileException 文件格式或路径不正确时抛出的异常
	 */
	public JiraTestCaseWrite(File configFile, File caseFile) throws DocumentException {
		super(configFile, caseFile);
		
		//TODO 添加与测试用例模板的关联，若测试用例模板字段有所改变，则在此改变关联字段
		relevanceCase(JiraFieldIdType.STEP.getName(), LabelType.STEP.getName());
		relevanceCase(JiraFieldIdType.EXCEPT.getName(), LabelType.EXCEPT.getName());
		relevanceCase(JiraFieldIdType.PRECONDITION.getName(), LabelType.PRECONDITION.getName());
		relevanceCase(JiraFieldIdType.PRIORITY.getName(), LabelType.RANK.getName());
		relevanceCase(JiraFieldIdType.TITLE.getName(), LabelType.TITLE.getName());
	}
	
	@Override
	String getStepName() {
		return JiraFieldIdType.STEP.getName();
	}

	@Override
	String getExceptName() {
		return JiraFieldIdType.EXCEPT.getName();
	}

	@Override
	String getModuleName() {
		return JiraFieldIdType.FOLDER.getName();
	}
	
	@Override
	String getTitleName() {
		return JiraFieldIdType.TITLE.getName();
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
	public enum JiraFieldIdType {
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
