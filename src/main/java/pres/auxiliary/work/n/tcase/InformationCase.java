package pres.auxiliary.work.n.tcase;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * <p><b>文件名：</b>InformationCase.java</p>
 * <p><b>用途：</b>用于输出与页面新增或编辑信息相关的用例，类中提供部分模板中会使用到
 * 待替换的词语，可通过类名获取相应的文本，传入{@link #setReplaceWord(String, String)}方法
 * 的第一个参数中</p>
 * <p><b>编码时间：</b>2020年3月5日上午8:30:12</p>
 * <p><b>修改时间：</b>2020年3月5日上午8:30:12</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 */
public class InformationCase extends Case {
	/**
	 * 用于标记提交按钮名称
	 */
	public static final String BUTTON_NAME = WordType.BUTTON_NAME.getName();
	/**
	 * 用于标记需要添加的信息
	 */
	public static final String ADD_INFORMATION = WordType.ADD_INFORMATION.getName();
	/**
	 * 用于标记添加成功预期的前文
	 */
	public static final String SUCCESS_EXCEPT_TEXT_START = WordType.SUCCESS_EXCEPT_TEXT_START.getName();
	/**
	 * 用于标记添加成功预期的后文
	 */
	public static final String SUCCESS_EXCEPT_TEXT_END = WordType.SUCCESS_EXCEPT_TEXT_END.getName();
	/**
	 * 用于标记添加失败预期前文
	 */
	public static final String FAIL_EXCEPT_TEXT_START = WordType.FAIL_EXCEPT_TEXT_START.getName();
	/**
	 * 用于标记添加失败预期后文
	 */
	public static final String FAIL_EXCEPT_TEXT_END = WordType.FAIL_EXCEPT_TEXT_END.getName();
	
	/**
	 * 通过测试用例模板库的xml配置文件来构造InformationCase对象
	 * @param configXmlFile 用例模板库的xml文件对象
	 */
	public InformationCase(File configXmlFile) {
		super(configXmlFile);
	}
	
	@Override
	public void setReplaceWord(String word, String text) {
		//由于预期前后文是直接在文本上拼接，故为保证语句通顺，则自动为其添加
		//若传入的参数为成功或失败预期的前文时，则在文本后加上逗号
		if (SUCCESS_EXCEPT_TEXT_START.equals(word) || FAIL_EXCEPT_TEXT_START.equals(word)) {
			//判断文本最后一个字符是否为逗号，若不是逗号，则将逗号拼接上
			if (text.lastIndexOf("，") != text.length() - 1) {
				text += "，";
			}
		}
		
		//若传入的参数为成功或失败预期的后文时，则在文本前加上逗号
		if (SUCCESS_EXCEPT_TEXT_END.equals(word) || FAIL_EXCEPT_TEXT_END.equals(word)) {
			//判断文本第一个字符是否为逗号，若不是逗号，则将逗号拼接上
			if (text.indexOf("，") != 0) {
				text = "，" + text;
			}
		}
		
		super.setReplaceWord(word, text);
	}
	
	/**
	 * 用于生成正确填写所有信息的用例
	 * 
	 * @return 类本身
	 */
	public Case addWholeInformationCase() {
		//清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addWholeInformationCase";
		
		//存储标题信息
		addFieldText(LabelType.TITLE, getAllLabelText(caseName, LabelType.TITLE));
		//存储步骤信息
		addFieldText(LabelType.STEP, getAllLabelText(caseName, LabelType.STEP));
		//存储预期信息
		addFieldText(LabelType.EXCEPT, getAllLabelText(caseName, LabelType.EXCEPT));
		//存储前置条件信息
		addFieldText(LabelType.PRECONDITION, getAllLabelText(caseName, LabelType.PRECONDITION));
		//存储关键词信息
		addFieldText(LabelType.KEY, getAllLabelText(caseName, LabelType.KEY));
		//存储优先级信息
		addFieldText(LabelType.RANK, getAllLabelText(caseName, LabelType.RANK));
		
		return this;
	}
	
	/**
	 * 该方法用于生成不完全填写所有信息的用例
	 * 
	 * @return 类本身
	 */
	public Case addUnWholeInformationCase() {
		//清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addUnWholeInformationCase";
		
		//存储标题信息
		addFieldText(LabelType.TITLE, getAllLabelText(caseName, LabelType.TITLE));
		
		//存储步骤与预期信息
		//不填写任何信息
		addFieldText(LabelType.STEP, getLabelText(caseName, LabelType.STEP, "1"));
		addFieldText(LabelType.EXCEPT, getLabelText(caseName, LabelType.EXCEPT, "2"));
		//只填写所有的必填项信息
		addFieldText(LabelType.STEP, getLabelText(caseName, LabelType.STEP, "2"));
		addFieldText(LabelType.EXCEPT, getLabelText(caseName, LabelType.EXCEPT, "1"));
		//只填写所有的非必填项信息
		addFieldText(LabelType.STEP, getLabelText(caseName, LabelType.STEP, "3"));
		addFieldText(LabelType.EXCEPT, getLabelText(caseName, LabelType.EXCEPT, "2"));
		
		//存储前置条件信息
		addFieldText(LabelType.PRECONDITION, getAllLabelText(caseName, LabelType.PRECONDITION));
		//存储关键词信息
		addFieldText(LabelType.KEY, getAllLabelText(caseName, LabelType.KEY));
		//存储优先级信息
		addFieldText(LabelType.RANK, getAllLabelText(caseName, LabelType.RANK));
		
		return this;
	}
	
	/**
	 * 用于生成普通文本框测试用例
	 * @param name 文本框名称（控件名称）
	 * @param isMust 是否必填
	 * @param isRepeat 是否可以与存在的内容重复
	 * @param inputRuleTypes 输入限制（{@link InputRuleType}枚举类）
	 * @return 类本身
	 */
	public Case addBasicTextboxCase(String name, boolean isMust, boolean isRepeat, InputRuleType... inputRuleTypes) {
		//清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addTextboxCase";
		//添加控件名称
		wordMap.put(WordType.CONTROL_NAME.getName(), name);
		//转换输入限制为集合类型
		List<InputRuleType> inputRules = Arrays.asList(inputRuleTypes);
		
		//存储标题信息
		addFieldText(LabelType.TITLE, getLabelText(caseName, LabelType.TITLE, "1"));
		
		//存储步骤与预期信息
		//不填写或只输入空格
		addFieldText(LabelType.STEP, getLabelText(caseName, LabelType.STEP, "1"));
		//根据是否必填来判断填入成功或失败预期
		addFieldText(LabelType.EXCEPT, getLabelText(caseName, LabelType.EXCEPT, isMust ? "2" : "1"));
		
		//填写特殊字符或HTML代码
		addFieldText(LabelType.STEP, getLabelText(caseName, LabelType.STEP, "2"));
		//根据是否存在输入
		addFieldText(LabelType.EXCEPT, getLabelText(caseName, LabelType.EXCEPT, inputRules.size() == 0 || inputRules.contains(InputRuleType.SPE) ? "1" : "2"));
		
		//输入非限制的字符
		if (inputRules.size() != 0) {
			String inputRuleText = "";
			for (InputRuleType inputRule : inputRules) {
				inputRuleText += (inputRule.getName() + "、");
			}
			wordMap.put(WordType.INPUT_RULE.getName(), inputRuleText.substring(0, inputRuleText.length() - 1));
			addFieldText(LabelType.STEP, getLabelText(caseName, LabelType.STEP, "3"));
			addFieldText(LabelType.EXCEPT, getLabelText(caseName, LabelType.EXCEPT, "2"));
		}
		
		//填写存在的内容
		addFieldText(LabelType.STEP, getLabelText(caseName, LabelType.STEP, "12"));
		//根据是否必填来判断填入成功或失败预期
		addFieldText(LabelType.EXCEPT, getLabelText(caseName, LabelType.EXCEPT, isRepeat ? "2" : "1"));
				
		//存储前置条件信息
		addFieldText(LabelType.PRECONDITION, getAllLabelText(caseName, LabelType.PRECONDITION));
		//存储关键词信息
		addFieldText(LabelType.KEY, getAllLabelText(caseName, LabelType.KEY));
		//存储优先级信息
		addFieldText(LabelType.RANK, getLabelText(caseName, LabelType.RANK, isMust ? "1" : "2"));
		
		return this;
	}
	
	/**
	 * 用于生成带长度限制的文本框测试用例，当只有单边限制时，则其中一个参数传入小于等于0的数字即可，例如：<br>
	 * 1.输入长度限制为2~10个字符时：addLengthRuleTextboxCase(..., 2, 10, ...)<br>
	 * 2.输入长度限制为最多输入10个字符时：addLengthRuleTextboxCase(..., 0, 10, ...)<br>
	 * 3.输入长度限制为最少输入2个字符时：addLengthRuleTextboxCase(..., 2, 0, ...)<br>
	 * 注意：若两个参数传入一样，且都不为0，则等价于第2中情况；若两个参数都小于等于0时，则抛出异常
	 * @param name 文本框名称（控件名称）
	 * @param isMust 是否必填
	 * @param isRepeat 是否可以与存在的内容重复
	 * @param minLen 最小输入长度限制
	 * @param maxLen 最大输入长度限制
	 * @param inputRuleTypes 输入限制（{@link InputRuleType}枚举类）
	 * @return 类本身
	 * @throws CaseContentException 当限制参数传入有误时抛出
	 */
	public Case addLengthRuleTextboxCase(String name, boolean isMust, boolean isRepeat, int minLen, int maxLen, InputRuleType... inputRuleTypes) {
		//清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addTextboxCase";
		//添加控件名称
		wordMap.put(WordType.CONTROL_NAME.getName(), name);
		//转换输入限制为集合类型
		List<InputRuleType> inputRules = Arrays.asList(inputRuleTypes);
		
		//存储标题信息
		addFieldText(LabelType.TITLE, getLabelText(caseName, LabelType.TITLE, "1"));
		
		//----------------------------------------------------------------------
		//存储步骤与预期信息
		//不填写或只输入空格
		addFieldText(LabelType.STEP, getLabelText(caseName, LabelType.STEP, "1"));
		//根据是否必填来判断填入成功或失败预期
		addFieldText(LabelType.EXCEPT, getLabelText(caseName, LabelType.EXCEPT, isMust ? "2" : "1"));
		
		//填写特殊字符或HTML代码
		addFieldText(LabelType.STEP, getLabelText(caseName, LabelType.STEP, "2"));
		//根据是否存在输入
		addFieldText(LabelType.EXCEPT, getLabelText(caseName, LabelType.EXCEPT, inputRules.size() == 0 || inputRules.contains(InputRuleType.SPE) ? "1" : "2"));
		
		//输入非限制的字符
		if (inputRules.size() != 0) {
			String inputRuleText = "";
			for (InputRuleType inputRule : inputRules) {
				inputRuleText += (inputRule.getName() + "、");
			}
			wordMap.put(WordType.INPUT_RULE.getName(), inputRuleText.substring(0, inputRuleText.length() - 1));
			addFieldText(LabelType.STEP, getLabelText(caseName, LabelType.STEP, "3"));
			addFieldText(LabelType.EXCEPT, getLabelText(caseName, LabelType.EXCEPT, "2"));
		}
		
		//填写存在的内容
		addFieldText(LabelType.STEP, getLabelText(caseName, LabelType.STEP, "12"));
		//根据是否必填来判断填入成功或失败预期
		addFieldText(LabelType.EXCEPT, getLabelText(caseName, LabelType.EXCEPT, isRepeat ? "2" : "1"));
		
		//添加输入长度限制相关的测试用例
		//判断传入的限制参数是否有小于0的参数，参数小于0则直接转换为0
		minLen = minLen < 0 ? 0 : minLen;
		maxLen = maxLen < 0 ? 0 : maxLen;
		//判断长度是否都为0，若都为0，则抛出异常
		if (minLen == 0 && maxLen == 0) {
			throw new CaseContentException("长度限制不能全为0");
		} else if (minLen != 0 && maxLen != 0) {
			//若都不为0，则将相应的内容进行存储，且判断最大长度与最小长度是否传反，若传反，则调换顺序
			wordMap.put(WordType.INPUT_MIN_LENGTH.getName(), String.valueOf(minLen < maxLen ? minLen : maxLen));
			wordMap.put(WordType.INPUT_MAX_LENGTH.getName(), String.valueOf(minLen < maxLen ? maxLen : minLen));
			
			//小于最小限制
			addFieldText(LabelType.STEP, getLabelText(caseName, LabelType.STEP, "4"));
			addFieldText(LabelType.EXCEPT, getLabelText(caseName, LabelType.EXCEPT, "2"));
			//等于最小限制
			addFieldText(LabelType.STEP, getLabelText(caseName, LabelType.STEP, "5"));
			addFieldText(LabelType.EXCEPT, getLabelText(caseName, LabelType.EXCEPT, "1"));
			//大于最大限制
			addFieldText(LabelType.STEP, getLabelText(caseName, LabelType.STEP, "6"));
			addFieldText(LabelType.EXCEPT, getLabelText(caseName, LabelType.EXCEPT, "2"));
			//等于最大限制
			addFieldText(LabelType.STEP, getLabelText(caseName, LabelType.STEP, "7"));
			addFieldText(LabelType.EXCEPT, getLabelText(caseName, LabelType.EXCEPT, "1"));
		} else if (minLen == 0 && maxLen != 0 || minLen == maxLen) {
			//存储最大输入限制
			wordMap.put(WordType.INPUT_MAX_LENGTH.getName(), String.valueOf(maxLen));
			//大于最大限制
			addFieldText(LabelType.STEP, getLabelText(caseName, LabelType.STEP, "6"));
			addFieldText(LabelType.EXCEPT, getLabelText(caseName, LabelType.EXCEPT, "2"));
			//等于最大限制
			addFieldText(LabelType.STEP, getLabelText(caseName, LabelType.STEP, "7"));
			addFieldText(LabelType.EXCEPT, getLabelText(caseName, LabelType.EXCEPT, "1"));
		} else {
			//存储最小输入限制
			wordMap.put(WordType.INPUT_MIN_LENGTH.getName(), String.valueOf(minLen));
			
			//小于最小限制
			addFieldText(LabelType.STEP, getLabelText(caseName, LabelType.STEP, "4"));
			addFieldText(LabelType.EXCEPT, getLabelText(caseName, LabelType.EXCEPT, "2"));
			//等于最小限制
			addFieldText(LabelType.STEP, getLabelText(caseName, LabelType.STEP, "5"));
			addFieldText(LabelType.EXCEPT, getLabelText(caseName, LabelType.EXCEPT, "1"));
		}
		//----------------------------------------------------------------------
		
		//存储前置条件信息
		addFieldText(LabelType.PRECONDITION, getAllLabelText(caseName, LabelType.PRECONDITION));
		//存储关键词信息
		addFieldText(LabelType.KEY, getAllLabelText(caseName, LabelType.KEY));
		//存储优先级信息
		addFieldText(LabelType.RANK, getLabelText(caseName, LabelType.RANK, isMust ? "1" : "2"));
		
		return this;
	}
	
	/**
	 * <p><b>文件名：</b>InformationCase.java</p>
	 * <p><b>用途：</b>枚举在预设测试用例中需要被替换的词语</p>
	 * <p><b>编码时间：</b>2020年3月15日下午5:50:28</p>
	 * <p><b>修改时间：</b>2020年3月15日下午5:50:28</p>
	 * @author 
	 * @version Ver1.0
	 * @since JDK 12
	 *
	 */
	private enum WordType {
		/**
		 * 提交按钮名称
		 */
		BUTTON_NAME("按钮名称"), 
		/**
		 * 需要添加的信息
		 */
		ADD_INFORMATION("信息"), 
		/**
		 * 成功预期的前文
		 */
		SUCCESS_EXCEPT_TEXT_START("成功预期前文"), 
		/**
		 * 成功预期的后文
		 */
		SUCCESS_EXCEPT_TEXT_END("成功预期后文"), 
		/**
		 * 失败预期前文
		 */
		FAIL_EXCEPT_TEXT_START("失败预期前文"), 
		/**
		 * 失败预期后文
		 */
		FAIL_EXCEPT_TEXT_END("失败预期后文"), 
		/**
		 * 控件名称
		 */
		CONTROL_NAME("控件名称"), 
		/**
		 * 输入限制
		 */
		INPUT_RULE("输入限制"), 
		/**
		 * 最长长度输入限制
		 */
		INPUT_MAX_LENGTH("最长长度限制"), 
		/**
		 * 最短长度输入限制
		 */
		INPUT_MIN_LENGTH("最短长度限制"), 
		/**
		 * 最大数字输入限制
		 */
		INPUT_MAX_NUMBER("数字最大限制"), 
		/**
		 * 最小数字输入限制
		 */
		INPUT_MIN_NUMBER("数字最小限制"), 
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
