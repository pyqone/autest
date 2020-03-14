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
	public static final String BUTTON_NAME = "按钮名称";
	/**
	 * 用于标记需要添加的信息
	 */
	public static final String ADD_INFORMATION = "信息";
	/**
	 * 用于标记添加成功预期的前文
	 */
	public static final String SUCCESS_EXCEPT_TEXT_START = "成功预期前文";
	/**
	 * 用于标记添加成功预期的后文
	 */
	public static final String SUCCESS_EXCEPT_TEXT_END = "成功预期后文";
	/**
	 * 用于标记添加失败预期前文
	 */
	public static final String FAIL_EXCEPT_TEXT_START = "失败预期前文";
	/**
	 * 用于标记添加失败预期后文
	 */
	public static final String FAIL_EXCEPT_TEXT_END = "失败预期后文";
	
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
	 * 该方法用于生成正确填写所有信息的用例
	 * 
	 * @return 类本身
	 */
	public Case addBasicTextboxCase(String name, boolean isMust, boolean isRepeat, InputRuleType... inputRuleTypes) {
		//清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addTextboxCase";
		//添加控件名称
		wordMap.put("控件名称", name);
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
			wordMap.put("输入限制", inputRuleText.substring(0, inputRuleText.length() - 1));
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
}
