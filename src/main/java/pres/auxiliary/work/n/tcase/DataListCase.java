package pres.auxiliary.work.n.tcase;

import java.io.File;

/**
 * <p><b>文件名：</b>DataListCase.java</p>
 * <p><b>用途：</b>用于生成数据列表相关的测试用例</p>
 * <p><b>编码时间：</b>2020年3月3日下午8:25:33</p>
 * <p><b>修改时间：</b>2020年3月3日下午8:25:33</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public class DataListCase extends Case {
	/**
	 * 用于列表名称
	 */
	public static final String DATA_NAME = WordType.INFORMATION.getName();
	
	/**
	 * 通过测试用例模板库的xml配置文件来构造DataListCase对象
	 * @param configXmlFile 用例模板库的xml文件对象
	 */
	public DataListCase(File configXmlFile) {
		super(configXmlFile);
	}
	
	/**
	 * 用于生成app上浏览列表的测试用例
	 * @return 类本身
	 */
	public Case appBrowseListCase() {
		//清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addAppBrowseListCase";
		
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
	public Case webBrowseListCase() {
		//清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addWebBrowseListCase";
		
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
	 * 用于添加输入条件对列表进行搜索的测试用例
	 * @param condition 搜索条件（控件）名称
	 * @return 类本身
	 */
	public Case textboxSearchCase(String condition) {
		//清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addInputSearchCase";
		
		//添加替换词语
		wordMap.put(WordType.SEARCH_CONDITION.getName(), condition);
		
		//存储标题信息
		addFieldText(LabelType.TITLE, getLabelText(caseName, LabelType.TITLE, "1"));
		
		//添加步骤与预期
		relevanceAddData(caseName, ALL, ALL);
		
		//存储预期信息
		addFieldText(LabelType.EXCEPT, getAllLabelText(caseName, LabelType.EXCEPT));
		
		//存储前置条件信息
		addFieldText(LabelType.PRECONDITION, getAllLabelText(caseName, LabelType.PRECONDITION));
		
		//存储关键词信息
		addFieldText(LabelType.KEY, getLabelText(caseName, LabelType.KEY, "1"));
		//存储优先级信息
		addFieldText(LabelType.RANK, getLabelText(caseName, LabelType.RANK, "1"));
		
		return this;
	}
	
	/**
	 * 用于添加选择条件对列表进行搜索的测试用例
	 * @param condition 搜索条件（控件）名称
	 * @return 类本身
	 */
	public Case selectSearchCase(String condition) {
		//清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addInputSearchCase";
		
		//添加替换词语
		wordMap.put(WordType.SEARCH_CONDITION.getName(), condition);
		
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
	 * 用于添加通过选择时间的方式对列表进行搜索的测试用例
	 * @param condition 搜索条件（控件）名称
	 * @param isTimeSlot 是否为时间段
	 * @return 类本身
	 */
	public Case dateSearchCase(String condition) {
		//清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addDateSearchCase";
		
		//添加替换词语
		wordMap.put(WordType.SEARCH_CONDITION.getName(), condition);
		
		//存储标题信息
		addFieldText(LabelType.TITLE, getLabelText(caseName, LabelType.TITLE, "1"));
		
		//----------------------------------------
		//输入存在数据的时间
		relevanceAddData(caseName, "1", "1");
		//输入不存在数据的时间
		relevanceAddData(caseName, "2", "2");
		//不选择时间
		relevanceAddData(caseName, "3", "3");
		//----------------------------------------
		
		//存储前置条件信息
		addFieldText(LabelType.PRECONDITION, getAllLabelText(caseName, LabelType.PRECONDITION));
		
		//存储关键词信息
		addFieldText(LabelType.KEY, getLabelText(caseName, LabelType.KEY, "1"));
		//存储优先级信息
		addFieldText(LabelType.RANK, getLabelText(caseName, LabelType.RANK, "1"));
		
		return this;
	}
	
	/**
	 * 用于添加通过选择时间段的方式对列表进行搜索的测试用例
	 * @param condition 搜索条件（控件）名称
	 * @param isTimeSlot 是否为时间段
	 * @return 类本身
	 */
	public Case timeQuantumSearchCase(String startTimeName, String endTimeName) {
		//清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addDateSearchCase";
		
		//添加替换词语
		wordMap.put(WordType.START_TIME.getName(), startTimeName);
		wordMap.put(WordType.END_TIME.getName(), endTimeName);
		
		//存储标题信息
		addFieldText(LabelType.TITLE, getLabelText(caseName, LabelType.TITLE, "1"));
		
		//----------------------------------------
		//输入存在数据的时间段
		relevanceAddData(caseName, "7", "7");
		//输入不存在数据的时间段
		relevanceAddData(caseName, "8", "2");
		//不选择时间段
		relevanceAddData(caseName, "9", "3");
		//不选择开始时间
		relevanceAddData(caseName, "5", "5");
		//不选择结束时间
		relevanceAddData(caseName, "6", "6");
		//选择开始时间大于结束时间
		relevanceAddData(caseName, "4", "4");
		//----------------------------------------
		
		//存储前置条件信息
		addFieldText(LabelType.PRECONDITION, getAllLabelText(caseName, LabelType.PRECONDITION));
		
		//存储关键词信息
		addFieldText(LabelType.KEY, getLabelText(caseName, LabelType.KEY, "1"));
		//存储优先级信息
		addFieldText(LabelType.RANK, getLabelText(caseName, LabelType.RANK, "1"));
		
		return this;
	}
	
	/**
	 * 该方法用于生成对列表排序的测试用例
	 * @param condition 排序条件
	 * @return 类本身
	 */
	public Case listSortCase(String condition) {
		//清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addListSortCase";
		
		//存储标题信息
		addFieldText(LabelType.TITLE, getLabelText(caseName, LabelType.TITLE, "1"));
		
		//----------------------------------------
		//添加步骤与预期
		relevanceAddData(caseName, ALL, ALL);
		//----------------------------------------
		
		//存储前置条件信息
		addFieldText(LabelType.PRECONDITION, getAllLabelText(caseName, LabelType.PRECONDITION));
		
		//存储关键词信息
		addFieldText(LabelType.KEY, getLabelText(caseName, LabelType.KEY, "1"));
		//存储优先级信息
		addFieldText(LabelType.RANK, getLabelText(caseName, LabelType.RANK, "1"));
		
		return this;
	}
	
	/**
	 * 用于添加导出信息的测试用例
	 * @param exportButton 导出按钮名称
	 * @param isCheck 是否可以勾选导出的内容
	 * @return 类本身
	 */
	public Case exportListCase(String exportButton, boolean isCheck) {
		//清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addListSortCase";
		
		//存储标题信息
		addFieldText(LabelType.TITLE, getLabelText(caseName, LabelType.TITLE, "1"));
		
		//----------------------------------------
		//添加步骤与预期
		//存在数据时点击导出
		relevanceAddData(caseName, "1", "1");
		relevanceAddData(caseName, "5", "5");
		relevanceAddData(caseName, "6", "1");
		if (isCheck) {
			relevanceAddData(caseName, "2", "2");
			relevanceAddData(caseName, "3", "2");
			relevanceAddData(caseName, "4", "4");
		}
		
		//----------------------------------------
		
		//存储前置条件信息
		addFieldText(LabelType.PRECONDITION, getAllLabelText(caseName, LabelType.PRECONDITION));
		
		//存储关键词信息
		addFieldText(LabelType.KEY, getLabelText(caseName, LabelType.KEY, "1"));
		//存储优先级信息
		addFieldText(LabelType.RANK, getLabelText(caseName, LabelType.RANK, "1"));
		
		return this;
	}
	
	/**
	 * <p><b>文件名：</b>DataListCase.java</p>
	 * <p><b>用途：</b>枚举在预设测试用例中需要被替换的词语</p>
	 * <p><b>编码时间：</b>2020年3月15日下午5:50:28</p>
	 * <p><b>修改时间：</b>2020年3月15日下午5:50:28</p>
	 * @author 彭宇琦
	 * @version Ver1.0
	 * @since JDK 12
	 *
	 */
	private enum WordType {
		/**
		 * 提交按钮名称
		 */
		INFORMATION("信息"), 
		/**
		 * 取消按钮名称
		 */
		SEARCH_CONDITION("搜索条件"), 
		/**
		 * 开始时间
		 */
		START_TIME("开始时间"), 
		/**
		 * 结束时间
		 */
		END_TIME("结束时间"), 
		/**
		 * 导出按钮
		 */
		EXPORT_BUTTON_NAME("导出按钮"), 
		;
		/**
		 * 存储需要替换的单词名称
		 */
		private String name;

		/**
		 * 初始化枚举值
		 * @param name 枚举的名称
		 */
		private WordType(String name) {
			this.name = name;
		}

		/**
		 * 返回需要替换的单词的名称
		 * @return 需要替换的单词的名称
		 */
		public String getName() {
			return name;
		}
	}
}
