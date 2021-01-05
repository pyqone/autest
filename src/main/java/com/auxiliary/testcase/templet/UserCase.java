package com.auxiliary.testcase.templet;

import java.io.File;

/**
 * <p><b>文件名：</b>UserCase.java</p>
 * <p><b>用途：</b>
 * 提供生成与用户登录、注册、忘记密码等相关操作测试用例的方法
 * </p>
 * <p><b>编码时间：</b>2020年11月12日上午7:24:27</p>
 * <p><b>修改时间：</b>2020年11月12日上午7:24:27</p>
 * @author 彭宇琦
 * @version Ver1.0
 *
 */
public class UserCase extends Case {
	/**
	 * 通过测试用例模板库的xml配置文件来构造MapCase对象
	 * @param configXmlFile 用例模板库的xml文件对象
	 */
	public UserCase(File configXmlFile) {
		super(configXmlFile);
	}
	
	/**
	 * 用于生成正常登录相关的测试用例
	 * @return 类本身
	 */
	public Case rightLoginCase() {
		//清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addRightLoginCase";
		
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
	 * 用于生成异常登录相关的测试用例
	 * @return 类本身
	 */
	public Case errorLoginCase() {
		//清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addErrorLogkinCase";
		
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
	 * 用于生成通过输入不同的验证码进行登录相关的测试用例
	 * @return 类本身
	 */
	public Case captchaLoginCase() {
		//清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addCaptchaCase";
		
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
	 * 用于生成通过不同权限的用户名与密码进行登录相关的测试用例
	 * @param isSparateLogin 不同权限是否需要分页登录
	 * @return 类本身
	 */
	public Case loginAuthorityCase(boolean isSparateLogin) {
		//清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addLoginAuthorityCase";
		
		//存储标题信息
		addFieldText(LabelType.TITLE, getLabelText(caseName, LabelType.TITLE, "1"));
		//添加步骤与预期
		if (isSparateLogin) {
			relevanceAddData(caseName, ALL, ALL);
		} else {
			relevanceAddData(caseName, "1", "1");
		}
		
		//存储前置条件信息
		addFieldText(LabelType.PRECONDITION, getAllLabelText(caseName, LabelType.PRECONDITION));
		//存储关键词信息
		addFieldText(LabelType.KEY, getLabelText(caseName, LabelType.KEY, "1"));
		//存储优先级信息
		addFieldText(LabelType.RANK, getLabelText(caseName, LabelType.RANK, "1"));
		
		return this;
	}
	
	/**
	 * 用于生成注册用户名相关的测试用例
	 * @param isPhone 是否为手机号码注册
	 * @return 类本身
	 */
	public Case usernameRegisterCase(boolean isPhone) {
		//清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addUsernameRegisterCase";
		
		//存储标题信息
		addFieldText(LabelType.TITLE, getLabelText(caseName, LabelType.TITLE, "1"));
		
		//添加步骤与预期
		relevanceAddData(caseName, "1", "1");
		relevanceAddData(caseName, "2", "2");
		if (isPhone) {
			relevanceAddData(caseName, "3", "3");
			relevanceAddData(caseName, "4", "4");
			relevanceAddData(caseName, "5", "5");
		} else {
			relevanceAddData(caseName, "6", "6");
			
		}
		relevanceAddData(caseName, "7", "7");
		
		//存储前置条件信息
		addFieldText(LabelType.PRECONDITION, getAllLabelText(caseName, LabelType.PRECONDITION));
		//存储关键词信息
		addFieldText(LabelType.KEY, getLabelText(caseName, LabelType.KEY, "1"));
		//存储优先级信息
		addFieldText(LabelType.RANK, getLabelText(caseName, LabelType.RANK, "1"));
		
		return this;
	}
	
	/**
	 * 用于生成通过用户名进行忘记密码操作相关的测试用例
	 * @return 类本身
	 */
	public Case usernameForgetCase() {
		//清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addUsernameForgetCase";
		
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
	 * 用于生成与密码操作（注册或忘记密码操作）相关的测试用例
	 * @param oprateType 操作类型（{@link OprateType}枚举）
	 * @return 类本身
	 */
	public Case passwordRegisterOrForgetCase(OprateType oprateType) {
		//清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addPasswordRegisterOrForgetCase";
		//添加替换的词语
		wordMap.put(WordType.OPRATE_TYPE.getName(), oprateType.getName());
		
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
	 * 用于生成与手机验证码操作（注册或忘记密码操作）相关的测试用例
	 * @param oprateType 操作类型（{@link OprateType}枚举）
	 * @param isPast 是否存在过期时间
	 * @return 类本身
	 */
	public Case captchaOprateCase(OprateType oprateType, boolean isPast) {
		//清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "captchaOprateCase";
		//添加替换的词语
		wordMap.put(WordType.OPRATE_TYPE.getName(), oprateType.getName());
		
		//存储标题信息
		addFieldText(LabelType.TITLE, getLabelText(caseName, LabelType.TITLE, "1"));
		
		//添加步骤与预期
		relevanceAddData(caseName, "1", "1");
		relevanceAddData(caseName, "2", "2");
		relevanceAddData(caseName, "3", "3");
		relevanceAddData(caseName, "4", "4");
		relevanceAddData(caseName, "5", "5");
		if (isPast) {
			relevanceAddData(caseName, "6", "6");
		}
		
		//存储前置条件信息
		addFieldText(LabelType.PRECONDITION, getAllLabelText(caseName, LabelType.PRECONDITION));
		//存储关键词信息
		addFieldText(LabelType.KEY, getLabelText(caseName, LabelType.KEY, "1"));
		//存储优先级信息
		addFieldText(LabelType.RANK, getLabelText(caseName, LabelType.RANK, "1"));
		
		return this;
	}
	
	/**
	 * 用于生成修改密码相关的测试用例
	 * @return 类本身
	 */
	public Case amendPasswordCase() {
		//清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addAlterPasswordCase";
		
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
	 * <p><b>文件名：</b>UserCase.java</p>
	 * <p><b>用途：</b>枚举在预设测试用例中需要被替换的词语</p>
	 * <p><b>编码时间：</b>2020年3月27日上午7:40:48</p>
	 * <p><b>修改时间：</b>2020年3月27日上午7:40:48</p>
	 * @author 彭宇琦
	 * @version Ver1.0
	 *
	 */
	private enum WordType {
		/**
		 * 标记点名称
		 */
		OPRATE_TYPE("操作类型"), 
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
	 * <p><b>文件名：</b>UserCase.java</p>
	 * <p><b>用途：</b>
	 * 用于枚举UserCase用例类相关的操作
	 * </p>
	 * <p><b>编码时间：</b>2020年11月12日上午7:53:43</p>
	 * <p><b>修改时间：</b>2020年11月12日上午7:53:43</p>
	 * @author 彭宇琦
	 * @version Ver1.0
	 *
	 */
	public enum OprateType {
		/**
		 * 注册
		 */
		REGISTER("注册"), 
		/**
		 * 忘记密码
		 */
		FORGET_PASSWORD("忘记密码"), 
		;
		/**
		 * 操作名称
		 */
		private String name;

		private OprateType(String name) {
			this.name = name;
		}

		/**
		 * 返回操作名称
		 * @return 操作名称
		 */
		public String getName() {
			return name;
		}
		
	}
}
