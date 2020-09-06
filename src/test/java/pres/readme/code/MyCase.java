package pres.readme.code;

import java.io.File;

import pres.auxiliary.work.testcase.templet.Case;
import pres.auxiliary.work.testcase.templet.LabelType;

public class MyCase extends Case {

	public MyCase(File configXmlFile) {
		super(configXmlFile);
	}
	
	/**
	 * 用于生成app上浏览列表的测试用例
	 * @return 类本身
	 */
	public Case myCase1() {
		//清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "myTest001";
		
		//存储标题信息
		addFieldText(LabelType.TITLE, getLabelText(caseName, LabelType.TITLE, "1"));
		
		//添加步骤与预期
		relevanceAddData(caseName, ALL, ALL);
		
		//存储前置条件信息
		addFieldText(LabelType.PRECONDITION, getAllLabelText(caseName, LabelType.PRECONDITION));
		
		//存储关键词信息
		addFieldText(LabelType.KEY, getLabelText(caseName, LabelType.KEY, "1"));
		//存储优先级信息
		addFieldText(LabelType.RANK, getLabelText(caseName, LabelType.RANK, "1"));
		
		return this;
	}
	
	/**
	 * 用于生成web上列表的测试用例
	 * @return 类本身
	 */
	public Case myCase2(String word) {
		//清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "myTest002";
		
		wordMap.put("词语", word);
		//存储标题信息
		addFieldText(LabelType.TITLE, getLabelText(caseName, LabelType.TITLE, "1"));
		
		//添加步骤与预期
		relevanceAddData(caseName, ALL, ALL);
		
		//存储前置条件信息
		addFieldText(LabelType.PRECONDITION, getAllLabelText(caseName, LabelType.PRECONDITION));
		
		//存储关键词信息
		addFieldText(LabelType.KEY, getLabelText(caseName, LabelType.KEY, "1"));
		//存储优先级信息
		addFieldText(LabelType.RANK, getLabelText(caseName, LabelType.RANK, "1"));
		
		return this;
	}
	
	/**
	 * 用于生成web上列表的测试用例
	 * @return 类本身
	 */
	public Case myCase3() {
		//清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "myTest001";
		
		//添加替换词语
		wordMap.put("条件", "混合");
		//存储标题信息
		addFieldText(LabelType.TITLE, getLabelText(caseName, LabelType.TITLE, "1"));
		
		//添加步骤与预期
		relevanceAddData(caseName, ALL, ALL);
		
		//存储前置条件信息
		addFieldText(LabelType.PRECONDITION, getAllLabelText(caseName, LabelType.PRECONDITION));
		
		//存储关键词信息
		addFieldText(LabelType.KEY, getLabelText(caseName, LabelType.KEY, "1"));
		//存储优先级信息
		addFieldText(LabelType.RANK, getLabelText(caseName, LabelType.RANK, "1"));
		
		return this;
	}
}