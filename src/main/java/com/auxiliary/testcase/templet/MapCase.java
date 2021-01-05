package com.auxiliary.testcase.templet;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * <p><b>文件名：</b>MapCase.java</p>
 * <p><b>用途：</b>
 * 提供生成与地图相关测试用例的方法
 * </p>
 * <p><b>编码时间：</b>2020年04月05日下午10:03:54</p>
 * <p><b>修改时间：</b>2020年04月05日下午10:03:54</p>
 * @author 彭宇琦
 * @version Ver1.0
 *
 */
public class MapCase extends Case {
	/**
	 * 通过测试用例模板库的xml配置文件来构造MapCase对象
	 * @param configXmlFile 用例模板库的xml文件对象
	 */
	public MapCase(File configXmlFile) {
		super(configXmlFile);
	}
	
	/**
	 * 用于生成地图测距相关的测试用例
	 * @return 类本身
	 */
	public Case rangeFindingCase() {
		//清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addRangeFindingCase";
		
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
	 * 用于生成地图上单一标记点的测试用例
	 * 
	 * @param pointName 标记点的名称
	 * @return 类本身
	 */
	public Case mapPointCase(String pointName) {
		//清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addMapPointCase";
		
		//添加替换词语
		wordMap.put(WordType.POINT_NAME.getName(), pointName);
		
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
	 * 用于生成对地图定位点搜索相关的测试用例
	 * @param condition 搜索条件
	 * @param information 搜索信息
	 * @return 类本身
	 */
	public Case mapSearchInformationCase(String condition, String information) {
		//清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addMapSearchInformationCase";
		
		//添加替换词语
		wordMap.put(WordType.SEARCH_CONDITION.getName(), condition);
		wordMap.put(WordType.INFORMATION.getName(), information);
		
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
	 * 用于添加与车辆轨迹回放相关的测试用例
	 * @return 类本身
	 */
	public Case carLocusPlaybackCase() {
		//清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addCarLocusPlaybackCase";
		
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
	 * 用于生成在地图绘制图案相关的测试用例
	 * @param signName 标记名称
	 * @param isMultiple 是否可以绘制多个
	 * @param graphTypes 图形类型
	 * @return 类本身
	 */
	public Case mapGraphSignCase(String signName, boolean isMultiple, GraphType...graphTypes) {
		//清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "mapGraphSignCase";
		
		//转换输入限制为集合类型
		List<GraphType> graphRules = Arrays.asList(graphTypes);
		if (graphRules.size() != 0) {
			String graphRulesText = "";
			for (GraphType graphRule : graphRules) {
				graphRulesText += (graphRule.getName() + "、");
			}
			wordMap.put(WordType.GRAPH.getName(), graphRulesText.substring(0, graphRulesText.length() - 1));
		}
		
		//添加替换词语
		wordMap.put(WordType.SIGN_NAME.getName(), signName);
		
		//存储标题信息
		addFieldText(LabelType.TITLE, getLabelText(caseName, LabelType.TITLE, "1"));
		
		//----------------------------------------------------------
		//添加步骤与预期
		//点击*{图形类型}*图形进行绘制
		relevanceAddData(caseName, "1", "1");
		//多边形相关用例
		if (graphRules.contains(GraphType.POLYGON)) {
			relevanceAddData(caseName, "2", "2");
			relevanceAddData(caseName, "3", "3");
		}
		//绘制多个
		relevanceAddData(caseName, "4", isMultiple ? "4" : "5");
		//画大片区域
		relevanceAddData(caseName, "5", "6");
		//绘制后关闭
		relevanceAddData(caseName, "6", "7");
		//绘制后清除绘制
		relevanceAddData(caseName, "7", "8");
		//----------------------------------------------------------
		//存储前置条件信息
		addFieldText(LabelType.PRECONDITION, getAllLabelText(caseName, LabelType.PRECONDITION));
		//存储关键词信息
		addFieldText(LabelType.KEY, getLabelText(caseName, LabelType.KEY, "1"));
		//存储优先级信息
		addFieldText(LabelType.RANK, getLabelText(caseName, LabelType.RANK, "1"));
		
		return this;
	}
	
	/**
	 * <p><b>文件名：</b>MapCase.java</p>
	 * <p><b>用途：</b>枚举在预设测试用例中需要被替换的词语</p>
	 * <p><b>编码时间：</b>2020年3月27日上午7:40:48</p>
	 * <p><b>修改时间：</b>2020年3月27日上午7:40:48</p>
	 * @author 彭宇琦
	 * @version Ver1.0
	 * @since JDK 12
	 *
	 */
	private enum WordType {
		/**
		 * 标记点名称
		 */
		POINT_NAME("标记点名称"), 
		/**
		 * 搜索条件
		 */
		SEARCH_CONDITION("搜索条件"), 
		/**
		 * 信息
		 */
		INFORMATION("信息"), 
		/**
		 * 标记名称
		 */
		SIGN_NAME("标记名称"), 
		/**
		 * 图形类型
		 */
		GRAPH("图形类型"), 
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
	
	/**
	 * <p><b>文件名：</b>MapCase.java</p>
	 * <p><b>用途：</b>用于枚举地图相应的图形标记</p>
	 * <p><b>编码时间：</b>2020年3月28日下午6:54:53</p>
	 * <p><b>修改时间：</b>2020年3月28日下午6:54:53</p>
	 * @author 彭宇琦
	 * @version Ver1.0
	 * @since JDK 12
	 *
	 */
	public enum GraphType {
		/**
		 * 圆形
		 */
		CIRCLE("圆形"), 
		/**
		 * 矩形
		 */
		RECTANGLE("矩形"), 
		/**
		 * 多边形
		 */
		POLYGON("多边形"), 
		;
		/**
		 * 存储需要替换的单词名称
		 */
		private String name;

		/**
		 * 初始化枚举值
		 * @param name 枚举的名称
		 */
		private GraphType(String name) {
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
