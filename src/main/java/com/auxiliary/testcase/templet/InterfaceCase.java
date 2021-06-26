package com.auxiliary.testcase.templet;

import java.io.File;

public class InterfaceCase extends Case {
	protected String FIELD_TYPE = "字段类型";
	protected String INTER_NAME = "接口名称";

	public InterfaceCase(File configXmlFile) {
		super(configXmlFile);
	}
	
	/**
	 * 用于设置接口名称
	 * @param interName 接口名称
	 */
	public void setInterfaceName(String interName) {
		//添加控件名称
		wordMap.put(INTER_NAME, interName);
	}
	
	/**
	 * 用于生成正确填写所有字段的用例
	 * 
	 * @return 类本身
	 */
	public Case wholeFieldCase() {
		//清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "整体字段";
		
		//存储标题信息
		addFieldText(LabelType.TITLE, getLabelText(caseName, LabelType.TITLE, "1"));
		
		// 存储步骤信息
		addFieldText(LabelType.STEP, getLabelText(caseName, LabelType.STEP, "1"));
		
		//存储预期信息
		addFieldText(LabelType.EXCEPT, getLabelText(caseName, LabelType.EXCEPT, "1"));
		
		//存储前置条件信息
		
		//存储关键词信息
		addFieldText(LabelType.KEY, getLabelText(caseName, LabelType.KEY, "1"));
		
		//存储优先级信息
		addFieldText(LabelType.RANK, getLabelText(caseName, LabelType.RANK, "1"));
		
		return this;
	}
	
	/**
	 * 用于生成填写部分字段的用例
	 * @param existAttestation 是否存在验签串
	 * @return 类本身
	 */
	public Case unwholeFieldCase(boolean existAttestation) {
		//清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "整体字段";
		
		//存储标题信息
		addFieldText(LabelType.TITLE, getLabelText(caseName, LabelType.TITLE, "2"));
		
		// 存储步骤与预期信息
		if (existAttestation) {
			addFieldText(LabelType.STEP, getLabelText(caseName, LabelType.STEP, "2"));
			addFieldText(LabelType.EXCEPT, getLabelText(caseName, LabelType.EXCEPT, "2"));
			
			addFieldText(LabelType.STEP, getLabelText(caseName, LabelType.STEP, "8"));
			addFieldText(LabelType.EXCEPT, getLabelText(caseName, LabelType.EXCEPT, "3"));
			
			addFieldText(LabelType.STEP, getLabelText(caseName, LabelType.STEP, "5"));
			addFieldText(LabelType.EXCEPT, getLabelText(caseName, LabelType.EXCEPT, "1"));
			
			addFieldText(LabelType.STEP, getLabelText(caseName, LabelType.STEP, "6"));
			addFieldText(LabelType.EXCEPT, getLabelText(caseName, LabelType.EXCEPT, "2"));
		} else {
			addFieldText(LabelType.STEP, getLabelText(caseName, LabelType.STEP, "3"));
			addFieldText(LabelType.EXCEPT, getLabelText(caseName, LabelType.EXCEPT, "1"));
			
			addFieldText(LabelType.STEP, getLabelText(caseName, LabelType.STEP, "4"));
			addFieldText(LabelType.EXCEPT, getLabelText(caseName, LabelType.EXCEPT, "2"));
		}
		
		
		//存储前置条件信息
		
		//存储关键词信息
		addFieldText(LabelType.KEY, getLabelText(caseName, LabelType.KEY, "1"));
		
		//存储优先级信息
		addFieldText(LabelType.RANK, getLabelText(caseName, LabelType.RANK, "1"));
		
		return this;
	}
	
	/**
	 * 用于生成无限制字段的用例
	 * @param jsonFieldType 字段可传入类型
	 * @return 
	 */
	public Case basicCase(String fieldName, boolean isMust, JsonFieldType jsonFieldType) {
		//清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String basicCaseName = "字段基础用例";
		String constraintCaseName = "字段基础用例";
		
		// 存储字段名称
		wordMap.put(FIELD_TYPE, fieldName);
		
		//存储标题信息
		addFieldText(LabelType.TITLE, getLabelText(basicCaseName, LabelType.TITLE, "1"));
		
		// TODO 未完成
		addFieldText(LabelType.STEP, getLabelText(basicCaseName, LabelType.STEP, "1"));
		addFieldText(LabelType.STEP, getLabelText(basicCaseName, LabelType.STEP, "1"));
		
		addFieldText(LabelType.STEP, getLabelText(basicCaseName, LabelType.STEP, "2"));
		// 存储步骤与预期信息
		switch (jsonFieldType) {
		case STRING:
			addFieldText(LabelType.STEP, getLabelText(constraintCaseName, LabelType.STEP, "1"));
			break;
		default:
			break;
		}
		
		
		//存储前置条件信息
		getAllLabelText(basicCaseName, LabelType.PRECONDITION).forEach(text -> {
			addFieldText(LabelType.PRECONDITION, text);
		});
		
		//存储关键词信息
		addFieldText(LabelType.KEY, getLabelText(basicCaseName, LabelType.KEY, "1"));
		
		//存储优先级信息
		addFieldText(LabelType.RANK, getLabelText(basicCaseName, LabelType.RANK, "1"));
		
		return this;
	}
	
	/**
	 * <p><b>文件名：</b>InterfaceCase.java</p>
	 * <p><b>用途：</b>
	 * 指定json串的字段类型
	 * </p>
	 * <p><b>编码时间：</b>2021年6月26日下午7:08:13</p>
	 * <p><b>修改时间：</b>2021年6月26日下午7:08:13</p>
	 * @author 彭宇琦
	 * @version Ver1.0
	 * @since JDK 1.8
	 */
	public enum JsonFieldType {
		/**
		 * 数字类型
		 */
		NUMBER, 
		/**
		 * 布尔类型
		 */
		BOOLEAN, 
		/**
		 * 字符串类型
		 */
		STRING, 
		/**
		 * json类型
		 */
		JSON, 
		/**
		 * 数组类型
		 */
		ARRAY;
	}
}
