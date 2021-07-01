package com.auxiliary.testcase.templet;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * <b>文件名：</b>InformationCase.java
 * </p>
 * <p>
 * <b>用途：</b>用于输出与页面新增或编辑信息相关的用例，类中提供部分模板中会使用到
 * 待替换的词语，可通过类名获取相应的文本，传入{@link #setReplaceWord(String, String)}方法 的第一个参数中
 * </p>
 * <p>
 * <b>编码时间：</b>2020年3月5日上午8:30:12
 * </p>
 * <p>
 * <b>修改时间：</b>2020年3月5日上午8:30:12
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 8
 */
public class InformationCase extends Case {
	/**
	 * 用于标记提交按钮名称
	 */
	public static final String BUTTON_NAME = WordType.SAVE_BUTTON_NAME.getName();
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
	 * 整形最小值，用于数字限制型文本框
	 */
	public static final int MIN_NUMBER = Integer.MIN_VALUE;
	/**
	 * 整形最大值，用于数字限制型文本框
	 */
	public static final int MAX_NUMBER = Integer.MAX_VALUE;

	/**
	 * 新增操作类型文本
	 */
	private final String OPERATION_ADD = "新增";
	/**
	 * 编辑操作类型文本
	 */
	private final String OPERATION_EDIT = "编辑";

	/**
	 * 通过测试用例模板库的xml配置文件来构造InformationCase对象
	 * 
	 * @param configXmlFile 用例模板库的xml文件对象
	 */
	public InformationCase(File configXmlFile) {
		super(configXmlFile);
	}

	public InformationCase() {
		super(new File(InformationCase.class.getClassLoader()
				.getResource("ConfigurationFiles/CaseConfigurationFile/CaseTemplet/InformationCase.xml").getFile()));
	}

	@Override
	public void setReplaceWord(String word, String text) {
		String sign = "，";

		// 由于预期前后文是直接在文本上拼接，故为保证语句通顺，则自动为其添加
		// 若传入的参数为成功或失败预期的前文时，则在文本后加上逗号
		if (SUCCESS_EXCEPT_TEXT_START.equals(word) || FAIL_EXCEPT_TEXT_START.equals(word)) {
			// 判断文本最后一个字符是否为逗号，若不是逗号，则将逗号拼接上
			if (text.lastIndexOf(sign) != text.length() - 1) {
				text += sign;
			}
		}

		// 若传入的参数为成功或失败预期的后文时，则在文本前加上逗号
		if (SUCCESS_EXCEPT_TEXT_END.equals(word) || FAIL_EXCEPT_TEXT_END.equals(word)) {
			// 判断文本第一个字符是否为逗号，若不是逗号，则将逗号拼接上
			if (text.indexOf(sign) != 0) {
				text = sign + text;
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
		// 清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addWholeInformationCase";

		// 存储标题信息
		addFieldText(LabelType.TITLE, getLabelText(caseName, LabelType.TITLE, "1"));

		getAllLabelText(caseName, LabelType.STEP).forEach(text -> {
			addFieldText(LabelType.STEP, text);
		});

		// 存储预期信息
		getAllLabelText(caseName, LabelType.EXCEPT).forEach(text -> {
			addFieldText(LabelType.EXCEPT, text);
		});

		// 存储前置条件信息

		getAllLabelText(caseName, LabelType.PRECONDITION).forEach(text -> {
			addFieldText(LabelType.PRECONDITION, text);
		});

		// 存储关键词信息
		addFieldText(LabelType.KEY, getLabelText(caseName, LabelType.KEY, "1"));
		// 存储优先级信息
		addFieldText(LabelType.RANK, getLabelText(caseName, LabelType.RANK, "1"));

		return this;
	}

	/**
	 * 该方法用于生成不完全填写所有信息的用例
	 * 
	 * @return 类本身
	 */
	public Case addUnWholeInformationCase() {
		// 清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addUnWholeInformationCase";

		// 存储标题信息
		addFieldText(LabelType.TITLE, getLabelText(caseName, LabelType.TITLE, "1"));

		// 存储步骤与预期信息
		// 不填写任何信息
		relevanceAddData(caseName, "1", "2");
		// 只填写所有的必填项信息
		relevanceAddData(caseName, "2", "1");
		// 只填写所有的非必填项信息
		relevanceAddData(caseName, "3", "2");

		// 存储前置条件信息
		addFieldText(LabelType.PRECONDITION, getAllLabelText(caseName, LabelType.PRECONDITION));

		// 存储关键词信息
		addFieldText(LabelType.KEY, getLabelText(caseName, LabelType.KEY, "1"));
		// 存储优先级信息
		addFieldText(LabelType.RANK, getLabelText(caseName, LabelType.RANK, "1"));

		return this;
	}

	/**
	 * 用于生成新增信息中对普通文本框测试用例
	 * 
	 * @param name           控件名称
	 * @param isMust         是否必填
	 * @param isRepeat       是否可以与存在的内容重复
	 * @param isClear        是否有按钮可以清空文本框
	 * @param inputRuleTypes 输入限制（{@link InputRuleType}枚举类）
	 * @return 类本身
	 */
	public Case addBasicTextboxCase(String name, boolean isMust, boolean isRepeat, boolean isClear,
			InputRuleType... inputRuleTypes) {
		return basicTextboxCase(OPERATION_ADD, name, isMust, isRepeat, isClear, inputRuleTypes);
	}

	/**
	 * 用于生成编辑信息中对普通文本框测试用例
	 * 
	 * @param name           控件名称
	 * @param isMust         是否必填
	 * @param isRepeat       是否可以与存在的内容重复
	 * @param isClear        是否有按钮可以清空文本框
	 * @param inputRuleTypes 输入限制（{@link InputRuleType}枚举类）
	 * @return 类本身
	 */
	public Case editBasicTextboxCase(String name, boolean isMust, boolean isRepeat, boolean isClear,
			InputRuleType... inputRuleTypes) {
		return basicTextboxCase(OPERATION_EDIT, name, isMust, isRepeat, isClear, inputRuleTypes);
	}

	/**
	 * 用于生成普通文本框测试用例，由于编辑信息与添加信息使用的文本几乎一致，故将相同的部分进行提取
	 * 
	 * @param operation      操作类型名称
	 * @param name           控件名称
	 * @param isMust         是否必填
	 * @param isRepeat       是否可以与存在的内容重复
	 * @param isClear        是否有按钮可以清空文本框
	 * @param inputRuleTypes 输入限制（{@link InputRuleType}枚举类）
	 * @return 类本身
	 */
	private Case basicTextboxCase(String operation, String name, boolean isMust, boolean isRepeat, boolean isClear,
			InputRuleType... inputRuleTypes) {
		// 清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addTextboxCase";

		// 添加操作类型
		wordMap.put(WordType.OPERATION_TYPE.getName(), operation);
		// 添加控件名称
		wordMap.put(WordType.CONTROL_NAME.getName(), name);

		// 存储标题信息
		addFieldText(LabelType.TITLE, getLabelText(caseName, LabelType.TITLE, "1"));

		// ----------------------------------------------------------------
		// 存储步骤与预期信息
		textboxCommonCase(isMust, isRepeat, isClear, inputRuleTypes);

		// ----------------------------------------------------------------

		// 存储前置条件信息
		addFieldText(LabelType.PRECONDITION, getLabelText(caseName, LabelType.PRECONDITION, "1"));
		addFieldText(LabelType.PRECONDITION,
				getLabelText(caseName, LabelType.PRECONDITION, OPERATION_ADD.equals(operation) ? "2" : "3"));
		// 存储关键词信息
		addFieldText(LabelType.KEY, getLabelText(caseName, LabelType.KEY, "1"));
		// 存储优先级信息
		addFieldText(LabelType.RANK, getLabelText(caseName, LabelType.RANK, isMust ? "1" : "2"));

		return this;
	}

	/**
	 * 用于生成新增信息中带长度限制的文本框测试用例，当只有单边限制时，则其中一个参数传入小于等于0的数字即可，例如：<br>
	 * 1.输入长度限制为2~10个字符时：addLengthRuleTextboxCase(..., 2, 10, ...)<br>
	 * 2.输入长度限制为最多输入10个字符时：addLengthRuleTextboxCase(..., 0, 10, ...)<br>
	 * 3.输入长度限制为最少输入2个字符时：addLengthRuleTextboxCase(..., 2, 0, ...)<br>
	 * 4.输入长度限制仅能输入2个字符时：addLengthRuleTextboxCase(..., 2, 2, ...)<br>
	 * 注意：若两个参数都小于等于0时，则抛出异常
	 * 
	 * @param name           控件名称
	 * @param isMust         是否必填
	 * @param isRepeat       是否可以与存在的内容重复
	 * @param isClear        是否有按钮可以清空文本框
	 * @param minLen         最小输入长度限制
	 * @param maxLen         最大输入长度限制
	 * @param inputRuleTypes 输入限制（{@link InputRuleType}枚举类）
	 * @return 类本身
	 * @throws CaseContentException 当限制参数传入有误时抛出
	 */
	public Case addLengthRuleTextboxCase(String name, boolean isMust, boolean isRepeat, boolean isClear, int minLen,
			int maxLen, InputRuleType... inputRuleTypes) {
		return lengthRuleTextboxCase(OPERATION_ADD, name, isMust, isRepeat, isClear, minLen, maxLen, inputRuleTypes);
	}

	/**
	 * 用于生成编辑信息中带长度限制的文本框测试用例，当只有单边限制时，则其中一个参数传入小于等于0的数字即可，例如：<br>
	 * 1.输入长度限制为2~10个字符时：addLengthRuleTextboxCase(..., 2, 10, ...)<br>
	 * 2.输入长度限制为最多输入10个字符时：addLengthRuleTextboxCase(..., 0, 10, ...)<br>
	 * 3.输入长度限制为最少输入2个字符时：addLengthRuleTextboxCase(..., 2, 0, ...)<br>
	 * 4.输入长度限制仅能输入2个字符时：addLengthRuleTextboxCase(..., 2, 2, ...)<br>
	 * 注意：若两个参数都小于等于0时，则抛出异常
	 * 
	 * @param name           控件名称
	 * @param isMust         是否必填
	 * @param isRepeat       是否可以与存在的内容重复
	 * @param isClear        是否有按钮可以清空文本框
	 * @param minLen         最小输入长度限制
	 * @param maxLen         最大输入长度限制
	 * @param inputRuleTypes 输入限制（{@link InputRuleType}枚举类）
	 * @return 类本身
	 * @throws CaseContentException 当限制参数传入有误时抛出
	 */
	public Case editLengthRuleTextboxCase(String name, boolean isMust, boolean isRepeat, boolean isClear, int minLen,
			int maxLen, InputRuleType... inputRuleTypes) {
		return lengthRuleTextboxCase(OPERATION_EDIT, name, isMust, isRepeat, isClear, minLen, maxLen, inputRuleTypes);
	}

	/**
	 * 用于生成带长度限制的文本框测试用例，当只有单边限制时，则其中一个参数传入小于等于0的数字即可，例如：<br>
	 * 1.输入长度限制为2~10个字符时：addLengthRuleTextboxCase(..., 2, 10, ...)<br>
	 * 2.输入长度限制为最多输入10个字符时：addLengthRuleTextboxCase(..., 0, 10, ...)<br>
	 * 3.输入长度限制为最少输入2个字符时：addLengthRuleTextboxCase(..., 2, 0, ...)<br>
	 * 4.输入长度限制仅能输入2个字符时：addLengthRuleTextboxCase(..., 2, 2, ...)<br>
	 * 注意：若两个参数都小于等于0时，则抛出异常
	 * 
	 * @param operation      操作类型名称
	 * @param name           控件名称
	 * @param isMust         是否必填
	 * @param isRepeat       是否可以与存在的内容重复
	 * @param isClear        是否有按钮可以清空文本框
	 * @param minLen         最小输入长度限制
	 * @param maxLen         最大输入长度限制
	 * @param inputRuleTypes 输入限制（{@link InputRuleType}枚举类）
	 * @return 类本身
	 * @throws CaseContentException 当限制参数传入有误时抛出
	 */
	private Case lengthRuleTextboxCase(String operation, String name, boolean isMust, boolean isRepeat, boolean isClear,
			int minLen, int maxLen, InputRuleType... inputRuleTypes) {
		// 清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addTextboxCase";

		// 添加操作类型
		wordMap.put(WordType.OPERATION_TYPE.getName(), operation);
		// 添加控件名称
		wordMap.put(WordType.CONTROL_NAME.getName(), name);

		// 存储标题信息
		addFieldText(LabelType.TITLE, getLabelText(caseName, LabelType.TITLE, "1"));

		// ----------------------------------------------------------------------
		// 存储步骤与预期信息
		// 添加通用用例
		textboxCommonCase(isMust, isRepeat, isClear, inputRuleTypes);

		// 添加输入长度限制相关的测试用例
		// 判断传入的限制参数是否有小于0的参数，参数小于0则直接转换为0
		minLen = minLen < 0 ? 0 : minLen;
		maxLen = maxLen < 0 ? 0 : maxLen;
		// 判断长度是否都为0，若都为0，则抛出异常
		if (minLen == 0 && maxLen == 0) {
			throw new CaseContentException("长度限制不能全为0");
		} else if (minLen != 0 && maxLen == 0) {
			// 存储最小输入限制
			wordMap.put(WordType.INPUT_MIN_LENGTH.getName(), String.valueOf(minLen));

			// 小于最小限制
			relevanceAddData(caseName, "4", "2");
			// 等于最小限制
			relevanceAddData(caseName, "5", "1");
		} else if (minLen == 0 && maxLen != 0) {
			// 存储最大输入限制
			wordMap.put(WordType.INPUT_MAX_LENGTH.getName(), String.valueOf(maxLen));
			// 大于最大限制
			relevanceAddData(caseName, "6", "2");
			// 等于最大限制
			relevanceAddData(caseName, "7", "1");
		} else {
			// 若都不为0，则将相应的内容进行存储，且判断最大长度与最小长度是否传反，若传反，则调换顺序
			wordMap.put(WordType.INPUT_MIN_LENGTH.getName(), String.valueOf(minLen < maxLen ? minLen : maxLen));
			wordMap.put(WordType.INPUT_MAX_LENGTH.getName(), String.valueOf(minLen < maxLen ? maxLen : minLen));

			// 小于最小限制
			relevanceAddData(caseName, "4", "2");
			// 等于最小限制
			relevanceAddData(caseName, "5", "1");
			// 大于最大限制
			relevanceAddData(caseName, "6", "2");
			// 若最大最小限制相等，则不添加该条用例
			if (minLen != maxLen) {
				// 等于最大限制
				relevanceAddData(caseName, "7", "1");
			}
		}
		// ----------------------------------------------------------------------

		// 存储前置条件信息
		addFieldText(LabelType.PRECONDITION, getLabelText(caseName, LabelType.PRECONDITION, "1"));
		addFieldText(LabelType.PRECONDITION,
				getLabelText(caseName, LabelType.PRECONDITION, "新增".equals(operation) ? "2" : "3"));
		// 存储关键词信息
		addFieldText(LabelType.KEY, getLabelText(caseName, LabelType.KEY, "1"));
		// 存储优先级信息
		addFieldText(LabelType.RANK, getLabelText(caseName, LabelType.RANK, isMust ? "1" : "2"));

		return this;
	}

	/**
	 * 用于生成新增信息中带数字限制的文本框测试用例，当只有单边限制时，则可通过类名，调用{@link #MAX_NUMBER}与{@link #MIN_NUMBER}来获取整形最大值与最小值，例如：<br>
	 * 1.输入限制数字最小输入为2时：addNumberRuleTextboxCase(..., 2, InformationCase.MAX_NUMBER,
	 * ...)<br>
	 * 2.输入限制数字最大输入10时：addLengthRuleTextboxCase(..., InformationCase.MIN_NUMBER, 10,
	 * ...)<br>
	 * 3.输入限制数字为2~10之间时：addLengthRuleTextboxCase(..., 2, 10, ...)<br>
	 * 注意：若两个参数传入一样或最小参数为{@link #MIN_NUMBER}， 最大参数为{@link #MAX_NUMBER}，则抛出异常
	 * 
	 * @param name     控件名称
	 * @param isMust   是否必填
	 * @param isRepeat 是否可以与存在的内容重复
	 * @param isClear  是否有按钮可以清空文本框
	 * @param decimals 小数位数
	 * @param minNum   最小数字限制
	 * @param maxNum   最大数字限制
	 * @return 类本身
	 * @throws CaseContentException 当限制参数传入有误时抛出
	 */
	public Case addNumberRuleTextboxCase(String name, boolean isMust, boolean isRepeat, boolean isClear, int decimals,
			int minNum, int maxNum) {
		return numberRuleTextboxCase(OPERATION_ADD, name, isMust, isRepeat, isClear, decimals, minNum, maxNum);
	}

	/**
	 * 用于生成编辑信息中带数字限制的文本框测试用例，当只有单边限制时，则可通过类名，调用{@link #MAX_NUMBER}与{@link #MIN_NUMBER}来获取整形最大值与最小值，例如：<br>
	 * 1.输入限制数字最小输入为2时：addNumberRuleTextboxCase(..., 2, InformationCase.MAX_NUMBER,
	 * ...)<br>
	 * 2.输入限制数字最大输入10时：addLengthRuleTextboxCase(..., InformationCase.MIN_NUMBER, 10,
	 * ...)<br>
	 * 3.输入限制数字为2~10之间时：addLengthRuleTextboxCase(..., 2, 10, ...)<br>
	 * 注意：若两个参数传入一样或最小参数为{@link #MIN_NUMBER}， 最大参数为{@link #MAX_NUMBER}，则抛出异常
	 * 
	 * @param name     控件名称
	 * @param isMust   是否必填
	 * @param isRepeat 是否可以与存在的内容重复
	 * @param isClear  是否有按钮可以清空文本框
	 * @param decimals 小数位数
	 * @param minNum   最小数字限制
	 * @param maxNum   最大数字限制
	 * @return 类本身
	 * @throws CaseContentException 当限制参数传入有误时抛出
	 */
	public Case editNumberRuleTextboxCase(String name, boolean isMust, boolean isRepeat, boolean isClear, int decimals,
			int minNum, int maxNum) {
		return numberRuleTextboxCase(OPERATION_EDIT, name, isMust, isRepeat, isClear, decimals, minNum, maxNum);
	}

	/**
	 * 用于生成带数字限制的文本框测试用例，当只有单边限制时，则可通过类名，调用{@link #MAX_NUMBER}与{@link #MIN_NUMBER}来获取整形最大值与最小值，例如：<br>
	 * 1.输入限制数字最小输入为2时：addNumberRuleTextboxCase(..., 2, InformationCase.MAX_NUMBER,
	 * ...)<br>
	 * 2.输入限制数字最大输入10时：addLengthRuleTextboxCase(..., InformationCase.MIN_NUMBER, 10,
	 * ...)<br>
	 * 3.输入限制数字为2~10之间时：addLengthRuleTextboxCase(..., 2, 10, ...)<br>
	 * 注意：若两个参数传入一样或最小参数为{@link #MIN_NUMBER}， 最大参数为{@link #MAX_NUMBER}，则抛出异常
	 * 
	 * @param operation 操作类型名称
	 * @param name      控件名称
	 * @param isMust    是否必填
	 * @param isRepeat  是否可以与存在的内容重复
	 * @param isClear   是否有按钮可以清空文本框
	 * @param decimals  小数位数
	 * @param minNum    最小数字限制
	 * @param maxNum    最大数字限制
	 * @return 类本身
	 * @throws CaseContentException 当限制参数传入有误时抛出
	 */
	private Case numberRuleTextboxCase(String operation, String name, boolean isMust, boolean isRepeat, boolean isClear,
			int decimals, int minNum, int maxNum) {
		// 清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addTextboxCase";

		// 添加操作类型
		wordMap.put(WordType.OPERATION_TYPE.getName(), operation);
		// 添加控件名称
		wordMap.put(WordType.CONTROL_NAME.getName(), name);

		// 存储标题信息
		addFieldText(LabelType.TITLE, getLabelText(caseName, LabelType.TITLE, "1"));

		// ----------------------------------------------------------------------
		// 存储步骤与预期信息
		// 添加通用用例
		textboxCommonCase(isMust, isRepeat, isClear, InputRuleType.NUM);

		// 添加输入数字限制相关的测试用例
		// 判断最大最小值是否一致，一致则抛出异常
		if (minNum == maxNum) {
			throw new CaseContentException("大小限制不能相同");
		}
		// 判断最大与最小值是否需要调换，存储正确最大最小值
		int min = minNum < maxNum ? minNum : maxNum;
		int max = minNum < maxNum ? maxNum : minNum;

		// 存储数字限制
		wordMap.put(WordType.INPUT_MIN_NUMBER.getName(), String.valueOf(min));
		wordMap.put(WordType.INPUT_MAX_NUMBER.getName(), String.valueOf(max));

		// 根据两个值的结果添加相应的用例
		if (min == MIN_NUMBER && max == MAX_NUMBER) {
			// 若两个值均为极值，则抛出异常
			throw new CaseContentException("大小限制不能同时为极值");
		} else if (min == MIN_NUMBER && max != MAX_NUMBER) {
			// 大于最大限制
			relevanceAddData(caseName, "10", "2");
			// 等于最大限制
			relevanceAddData(caseName, "11", "1");
			// 小于最小负数限制
			relevanceAddData(caseName, "8", "5");
		} else if (min != MIN_NUMBER && max == MAX_NUMBER) {
			// 小于最小限制
			relevanceAddData(caseName, "8", "2");
			// 等于最小限制
			relevanceAddData(caseName, "9", "1");
			// 大于最大正数限制
			relevanceAddData(caseName, "10", "5");
		} else {
			// 小于最小限制
			relevanceAddData(caseName, "8", "2");
			// 等于最小限制
			relevanceAddData(caseName, "9", "1");
			// 大于最大限制
			relevanceAddData(caseName, "10", "2");
			// 等于最大限制
			relevanceAddData(caseName, "11", "1");
		}

		// 若传入的小数位数大于0，则添加小数位相关的用例
		if (decimals > 0) {
			// 存储小数位数
			wordMap.put(WordType.INPUT_DECIMALS.getName(), String.valueOf(decimals));
			// 等于最小限制
			relevanceAddData(caseName, "14", "6");
			// 大于最大限制
			relevanceAddData(caseName, "15", "1");
			// 等于最大限制
			relevanceAddData(caseName, "16", "1");
		}

		// ----------------------------------------------------------------------

		// 存储前置条件信息
		addFieldText(LabelType.PRECONDITION, getLabelText(caseName, LabelType.PRECONDITION, "1"));
		addFieldText(LabelType.PRECONDITION,
				getLabelText(caseName, LabelType.PRECONDITION, "新增".equals(operation) ? "2" : "3"));

		// 存储关键词信息
		addFieldText(LabelType.KEY, getLabelText(caseName, LabelType.KEY, "1"));
		// 存储优先级信息
		addFieldText(LabelType.RANK, getLabelText(caseName, LabelType.RANK, isMust ? "1" : "2"));

		return this;
	}

	/**
	 * 用于生成新增信息中与号码相关的测试用例，通过{@link PhoneType}来控制传入号码类型
	 * 
	 * @param name       控件名称
	 * @param isMust     是否必填
	 * @param isRepeat   是否可以与存在的内容重复
	 * @param isClear    是否有按钮可以清空文本框
	 * @param phoneTypes 号码类型枚举，可通过类名进行调用
	 * @return 类本身
	 */
	public Case addPhoneCase(String name, boolean isMust, boolean isRepeat, boolean isClear, PhoneType... phoneTypes) {
		return phoneCase(OPERATION_ADD, name, isMust, isRepeat, isClear, phoneTypes);
	}

	/**
	 * 用于生成编辑信息中与号码相关的测试用例，通过{@link PhoneType}来控制传入号码类型
	 * 
	 * @param name       控件名称
	 * @param isMust     是否必填
	 * @param isRepeat   是否可以与存在的内容重复
	 * @param isClear    是否有按钮可以清空文本框
	 * @param phoneTypes 号码类型枚举，可通过类名进行调用
	 * @return 类本身
	 */
	public Case editPhoneCase(String name, boolean isMust, boolean isRepeat, boolean isClear, PhoneType... phoneTypes) {
		return phoneCase(OPERATION_EDIT, name, isMust, isRepeat, isClear, phoneTypes);
	}

	/**
	 * 用于生成与号码相关的测试用例，通过{@link PhoneType}来控制传入号码类型
	 * 
	 * @param operation  操作类型名称
	 * @param name       控件名称
	 * @param isMust     是否必填
	 * @param isRepeat   是否可以与存在的内容重复
	 * @param isClear    是否有按钮可以清空文本框
	 * @param phoneTypes 号码类型枚举，可通过类名进行调用
	 * @return 类本身
	 */
	private Case phoneCase(String operation, String name, boolean isMust, boolean isRepeat, boolean isClear,
			PhoneType... phoneTypes) {
		// 清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addPhoneCase";

		// 添加操作类型
		wordMap.put(WordType.OPERATION_TYPE.getName(), operation);
		// 添加控件名称
		wordMap.put(WordType.CONTROL_NAME.getName(), name);
		// 转换输入限制为集合类型
		List<PhoneType> phoneRules = Arrays.asList(phoneTypes);

		// 存储标题信息
		addFieldText(LabelType.TITLE, getLabelText(caseName, LabelType.TITLE, "1"));

		// ----------------------------------------------------------------------
		// 存储步骤与预期信息
		// 添加通用用例
		textboxCommonCase(isMust, isRepeat, isClear, InputRuleType.NUM);

		// 添加手机相关的测试用例
		if (phoneRules.contains(PhoneType.MOBLE)) {
			// 大于11位
			relevanceAddData(caseName, "3", "2");
			// 小于11位
			relevanceAddData(caseName, "4", "2");
		}

		// 添加座机相关的测试用例
		if (phoneRules.contains(PhoneType.FIXED)) {
			// 大于7位
			relevanceAddData(caseName, "5", "2");
			// 小于7位
			relevanceAddData(caseName, "6", "2");
			// 区位号
			relevanceAddData(caseName, "1", "1");
		}

		// 长度符合但不和规则的用例
		relevanceAddData(caseName, "7", "2");
		// ----------------------------------------------------------------------

		// 存储前置条件信息
		addFieldText(LabelType.PRECONDITION, getLabelText(caseName, LabelType.PRECONDITION, "1"));
		addFieldText(LabelType.PRECONDITION,
				getLabelText(caseName, LabelType.PRECONDITION, "新增".equals(operation) ? "2" : "3"));

		// 存储关键词信息
		addFieldText(LabelType.KEY, getLabelText(caseName, LabelType.KEY, "1"));
		// 存储优先级信息
		addFieldText(LabelType.RANK, getLabelText(caseName, LabelType.RANK, isMust ? "1" : "2"));

		return this;
	}

	/**
	 * 用于新增信息中生成身份证相关的测试用例
	 * 
	 * @param name     控件名称
	 * @param isMust   是否必填
	 * @param isRepeat 是否可以与存在的内容重复
	 * @param isClear  是否有按钮可以清空文本框
	 * @return 类本身
	 */
	public Case addIdCardCase(String name, boolean isMust, boolean isRepeat, boolean isClear) {
		return idCardCase(OPERATION_ADD, name, isMust, isRepeat, isClear);
	}

	/**
	 * 用于编辑信息中生成身份证相关的测试用例
	 * 
	 * @param name     控件名称
	 * @param isMust   是否必填
	 * @param isRepeat 是否可以与存在的内容重复
	 * @param isClear  是否有按钮可以清空文本框
	 * @return 类本身
	 */
	public Case editIdCardCase(String name, boolean isMust, boolean isRepeat, boolean isClear) {
		return idCardCase(OPERATION_EDIT, name, isMust, isRepeat, isClear);
	}

	/**
	 * 用于生成身份证相关的测试用例
	 * 
	 * @param operation 操作类型名称
	 * @param name      控件名称
	 * @param isMust    是否必填
	 * @param isRepeat  是否可以与存在的内容重复
	 * @param isClear   是否有按钮可以清空文本框
	 * @return 类本身
	 */
	private Case idCardCase(String operation, String name, boolean isMust, boolean isRepeat, boolean isClear) {
		// 清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addIDCardCase";

		// 添加控件名称
		wordMap.put(WordType.CONTROL_NAME.getName(), name);
		// 添加操作类型
		wordMap.put(WordType.OPERATION_TYPE.getName(), operation);

		// 存储标题信息
		addFieldText(LabelType.TITLE, getLabelText(caseName, LabelType.TITLE, "1"));

		// ----------------------------------------------------------------------
		// 存储步骤与预期信息
		// 添加通用用例
		textboxCommonCase(isMust, isRepeat, isClear, InputRuleType.NUM);

		// 输入15位
		relevanceAddData(caseName, "3", "1");
		// 输入18位
		relevanceAddData(caseName, "4", "1");
		// 输入末尾带“X”或“x”
		relevanceAddData(caseName, "5", "1");

		// 输入大于18位
		relevanceAddData(caseName, "6", "2");
		// 输入小于18位大于15位
		relevanceAddData(caseName, "7", "2");
		// 输入小于15位
		relevanceAddData(caseName, "8", "2");

		// 输入不符合规则
		relevanceAddData(caseName, "9", "2");
		// ----------------------------------------------------------------------

		// 存储前置条件信息
		addFieldText(LabelType.PRECONDITION, getLabelText(caseName, LabelType.PRECONDITION, "1"));
		addFieldText(LabelType.PRECONDITION,
				getLabelText(caseName, LabelType.PRECONDITION, "新增".equals(operation) ? "2" : "3"));

		// 存储关键词信息
		addFieldText(LabelType.KEY, getLabelText(caseName, LabelType.KEY, "1"));
		// 存储优先级信息
		addFieldText(LabelType.RANK, getLabelText(caseName, LabelType.RANK, isMust ? "1" : "2"));

		return this;
	}

	/**
	 * 用于生成新增信息中下拉框的测试用例
	 * 
	 * @param name     控件名称
	 * @param isMust   是否必填
	 * @param hasEmpty 是否存在空选项
	 * @param isClear  是否有按钮可以清空文本框
	 * @return 类本身
	 */
	public Case addSelectboxCase(String name, boolean isMust, boolean hasEmpty, boolean isClear) {
		return selectboxCase(OPERATION_ADD, name, isMust, hasEmpty, isClear);
	}

	/**
	 * 用于生成编辑信息中下拉框的测试用例
	 * 
	 * @param name     控件名称
	 * @param isMust   是否必填
	 * @param hasEmpty 是否存在空选项
	 * @param isClear  是否有按钮可以清空文本框
	 * @return 类本身
	 */
	public Case editSelectboxCase(String name, boolean isMust, boolean hasEmpty, boolean isClear) {
		return selectboxCase(OPERATION_EDIT, name, isMust, hasEmpty, isClear);
	}

	/**
	 * 该方法用于生成下拉框的测试用例
	 * 
	 * @param operation 操作类型名称
	 * @param name      控件名称
	 * @param isMust    是否必填
	 * @param hasEmpty  是否存在空选项
	 * @param isClear   是否有按钮可以清空文本框
	 * @return 类本身
	 */
	private Case selectboxCase(String operation, String name, boolean isMust, boolean hasEmpty, boolean isClear) {
		// 清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addSelectboxCase";

		// 添加控件名称
		wordMap.put(WordType.CONTROL_NAME.getName(), name);
		// 添加操作类型
		wordMap.put(WordType.OPERATION_TYPE.getName(), operation);

		// 存储标题信息
		addFieldText(LabelType.TITLE, getLabelText(caseName, LabelType.TITLE, "1"));

		// ----------------------------------------------------------------------
		// 存储步骤与预期信息
		// 选择选项后清空
		relevanceAddData(caseName, "4", hasEmpty ? "4" : "3");

		// 添加空选项用例
		if (hasEmpty) {
			relevanceAddData(caseName, "1", isMust ? "1" : "2");
		}

		// 添加选择第一个选项
		relevanceAddData(caseName, "2", "1");

		// 添加选择最后一个选项
		relevanceAddData(caseName, "3", "1");
		// ----------------------------------------------------------------------

		// 存储前置条件信息
		addFieldText(LabelType.PRECONDITION, getLabelText(caseName, LabelType.PRECONDITION, "1"));
		addFieldText(LabelType.PRECONDITION,
				getLabelText(caseName, LabelType.PRECONDITION, "新增".equals(operation) ? "2" : "3"));

		// 存储关键词信息
		addFieldText(LabelType.KEY, getLabelText(caseName, LabelType.KEY, "1"));
		// 存储优先级信息
		addFieldText(LabelType.RANK, getLabelText(caseName, LabelType.RANK, isMust ? "1" : "2"));

		return this;
	}

	/**
	 * 用于生成新增信息中单选按钮的测试用例
	 * 
	 * @param name 控件名称
	 * @return 类本身
	 */
	public Case addRadioButtonCase(String name) {
		return radioButtonCase(OPERATION_ADD, name);
	}

	/**
	 * 用于生成编辑信息中单选按钮的测试用例
	 * 
	 * @param name 控件名称
	 * @return 类本身
	 */
	public Case editRadioButtonCase(String name) {
		return radioButtonCase(OPERATION_EDIT, name);
	}

	/**
	 * 用于生成单选按钮的测试用例
	 * 
	 * @param operation 操作类型名称
	 * @param name      控件名称
	 * @return 类本身
	 */
	private Case radioButtonCase(String operation, String name) {
		// 清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addRadioButtonCase";

		// 添加控件名称
		wordMap.put(WordType.CONTROL_NAME.getName(), name);
		// 添加操作类型
		wordMap.put(WordType.OPERATION_TYPE.getName(), operation);

		// 存储标题信息
		addFieldText(LabelType.TITLE, getLabelText(caseName, LabelType.TITLE, "1"));

		// ----------------------------------------------------------------------
		// 存储步骤与预期信息
		// 选择选项后清空
		relevanceAddData(caseName, "5", "4");

		// 依次选择选项
		relevanceAddData(caseName, "1", "3");

		// 不改变默认选项
		relevanceAddData(caseName, "2", "1");

		// 选第一个
		relevanceAddData(caseName, "3", "1");

		// 选最后一个
		relevanceAddData(caseName, "4", "1");

		// ----------------------------------------------------------------------

		// 存储前置条件信息
		addFieldText(LabelType.PRECONDITION, getLabelText(caseName, LabelType.PRECONDITION, "1"));
		addFieldText(LabelType.PRECONDITION,
				getLabelText(caseName, LabelType.PRECONDITION, "新增".equals(operation) ? "2" : "3"));

		// 存储关键词信息
		addFieldText(LabelType.KEY, getLabelText(caseName, LabelType.KEY, "1"));
		// 存储优先级信息
		addFieldText(LabelType.RANK, getLabelText(caseName, LabelType.RANK, "1"));

		return this;
	}

	/**
	 * 用于生成新增信息中多选按钮的测试用例
	 * 
	 * @param name   控件名称
	 * @param isMust 是否必填
	 * @return 类本身
	 */
	public Case addCheckboxCase(String name, boolean isMust) {
		return checkboxCase(OPERATION_ADD, name, isMust);
	}

	/**
	 * 用于生成编辑信息中多选按钮的测试用例
	 * 
	 * @param name   控件名称
	 * @param isMust 是否必填
	 * @return 类本身
	 */
	public Case editCheckboxCase(String name, boolean isMust) {
		return checkboxCase(OPERATION_EDIT, name, isMust);
	}

	/**
	 * 用于生成多选按钮的测试用例
	 * 
	 * @param operation 操作类型名称
	 * @param name      控件名称
	 * @param isMust    是否必填
	 * @return 类本身
	 */
	private Case checkboxCase(String operation, String name, boolean isMust) {
		// 清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addCheckboxCase";

		// 添加控件名称
		wordMap.put(WordType.CONTROL_NAME.getName(), name);
		// 添加操作类型
		wordMap.put(WordType.OPERATION_TYPE.getName(), operation);

		// 存储标题信息
		addFieldText(LabelType.TITLE, getLabelText(caseName, LabelType.TITLE, "1"));

		// ----------------------------------------------------------------------
		// 存储步骤与预期信息
		// 依次选择选项
		relevanceAddData(caseName, "1", "3");
		// 再次依次选择选项
		relevanceAddData(caseName, "2", "4");

		// 不进行选择
		relevanceAddData(caseName, "3", isMust ? "2" : "1");

		// 选择第一个
		relevanceAddData(caseName, "4", "1");

		// 选择最后一个
		relevanceAddData(caseName, "5", "1");

		// 选择多个
		relevanceAddData(caseName, "6", "1");

		// ----------------------------------------------------------------------

		// 存储前置条件信息
		addFieldText(LabelType.PRECONDITION, getLabelText(caseName, LabelType.PRECONDITION, "1"));
		addFieldText(LabelType.PRECONDITION,
				getLabelText(caseName, LabelType.PRECONDITION, "新增".equals(operation) ? "2" : "3"));

		// 存储关键词信息
		addFieldText(LabelType.KEY, getLabelText(caseName, LabelType.KEY, "1"));
		// 存储优先级信息
		addFieldText(LabelType.RANK, getLabelText(caseName, LabelType.RANK, "1"));

		return this;
	}

	/**
	 * 用于生成编辑信息中与日期相关的测试用例
	 * 
	 * @param name    控件名称
	 * @param isMust  是否必填
	 * @param isInput 是否可以输入
	 * @param isClear 是否可以清除
	 * @return 类本身
	 */
	public Case editDateCase(String name, boolean isMust, boolean isInput, boolean isClear) {
		return dateCase(OPERATION_EDIT, name, isMust, isInput, isClear);
	}

	/**
	 * 用于生成新增信息中与日期相关的测试用例
	 * 
	 * @param name    控件名称
	 * @param isMust  是否必填
	 * @param isInput 是否可以输入
	 * @param isClear 是否可以清除
	 * @return 类本身
	 */
	public Case addDateCase(String name, boolean isMust, boolean isInput, boolean isClear) {
		return dateCase(OPERATION_ADD, name, isMust, isInput, isClear);
	}

	/**
	 * 用于生成与日期相关的测试用例
	 * 
	 * @param operation 操作类型名称
	 * @param name      控件名称
	 * @param isMust    是否必填
	 * @param isInput   是否可以输入
	 * @param isClear   是否可以清除
	 * @return 类本身
	 */
	private Case dateCase(String operation, String name, boolean isMust, boolean isInput, boolean isClear) {
		// 清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addDateCase";

		// 添加控件名称
		wordMap.put(WordType.CONTROL_NAME.getName(), name);
		// 添加操作类型
		wordMap.put(WordType.OPERATION_TYPE.getName(), operation);

		// 存储标题信息
		addFieldText(LabelType.TITLE, getLabelText(caseName, LabelType.TITLE, "1"));

		// ----------------------------------------------------------------------
		// 存储步骤与预期信息
		// 添加基本用例
		dateboxCommonCase(isMust, isInput, isClear);
		// ----------------------------------------------------------------------

		// 存储前置条件信息
		addFieldText(LabelType.PRECONDITION, getLabelText(caseName, LabelType.PRECONDITION, "1"));
		addFieldText(LabelType.PRECONDITION,
				getLabelText(caseName, LabelType.PRECONDITION, "新增".equals(operation) ? "2" : "3"));

		// 存储关键词信息
		addFieldText(LabelType.KEY, getLabelText(caseName, LabelType.KEY, "1"));
		// 存储优先级信息
		addFieldText(LabelType.RANK, getLabelText(caseName, LabelType.RANK, "1"));

		return this;
	}

	/**
	 * 用于生成新增信息中与日期相关的测试用例
	 * 
	 * @param name    控件名称
	 * @param isMust  是否必填
	 * @param isInput 是否可以输入
	 * @param isClear 是否可以清除
	 * @return 类本身
	 */
	public Case addStartDateCase(String name, String endDateName, boolean isMust, boolean isInput, boolean isClear) {
		return startDateCase(OPERATION_ADD, name, endDateName, isMust, isInput, isClear);
	}

	/**
	 * 用于生成编辑信息中与日期相关的测试用例
	 * 
	 * @param name    控件名称
	 * @param isMust  是否必填
	 * @param isInput 是否可以输入
	 * @param isClear 是否可以清除
	 * @return 类本身
	 */
	public Case editStartDateCase(String name, String endDateName, boolean isMust, boolean isInput, boolean isClear) {
		return startDateCase(OPERATION_EDIT, name, endDateName, isMust, isInput, isClear);
	}

	/**
	 * 用于生成与日期相关的测试用例
	 * 
	 * @param operation 操作类型名称
	 * @param name      控件名称
	 * @param isMust    是否必填
	 * @param isInput   是否可以输入
	 * @param isClear   是否可以清除
	 * @return 类本身
	 */
	private Case startDateCase(String operation, String name, String endDateName, boolean isMust, boolean isInput,
			boolean isClear) {
		// 清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addDateCase";

		// 添加控件名称
		wordMap.put(WordType.CONTROL_NAME.getName(), name);
		// 添加操作类型
		wordMap.put(WordType.OPERATION_TYPE.getName(), operation);

		// 存储标题信息
		addFieldText(LabelType.TITLE, getLabelText(caseName, LabelType.TITLE, "1"));

		// ----------------------------------------------------------------------
		// 存储步骤与预期信息
		// 添加基本用例
		dateboxCommonCase(isMust, isInput, isClear);

		wordMap.put(WordType.END_DATE.getName(), endDateName);
		// 大于结束时间
		relevanceAddData(caseName, "6", "2");

		// 等于结束时间
		relevanceAddData(caseName, "7", "1");
		// ----------------------------------------------------------------------

		// 存储前置条件信息
		addFieldText(LabelType.PRECONDITION, getLabelText(caseName, LabelType.PRECONDITION, "1"));
		addFieldText(LabelType.PRECONDITION,
				getLabelText(caseName, LabelType.PRECONDITION, "新增".equals(operation) ? "2" : "3"));

		// 存储关键词信息
		addFieldText(LabelType.KEY, getLabelText(caseName, LabelType.KEY, "1"));
		// 存储优先级信息
		addFieldText(LabelType.RANK, getLabelText(caseName, LabelType.RANK, "1"));

		return this;
	}

	/**
	 * 用于生成新增信息中与日期相关的测试用例
	 * 
	 * @param name    控件名称
	 * @param isMust  是否必填
	 * @param isInput 是否可以输入
	 * @param isClear 是否可以清除
	 * @return 类本身
	 */
	public Case addEndDateCase(String name, String startDateName, boolean isMust, boolean isInput, boolean isClear) {
		return endDateCase(OPERATION_ADD, name, startDateName, isMust, isInput, isClear);
	}

	/**
	 * 用于生成编辑信息中与日期相关的测试用例
	 * 
	 * @param name    控件名称
	 * @param isMust  是否必填
	 * @param isInput 是否可以输入
	 * @param isClear 是否可以清除
	 * @return 类本身
	 */
	public Case editEndDateCase(String name, String startDateName, boolean isMust, boolean isInput, boolean isClear) {
		return endDateCase(OPERATION_EDIT, name, startDateName, isMust, isInput, isClear);
	}

	/**
	 * 用于生成与日期相关的测试用例
	 * 
	 * @param operation 操作类型名称
	 * @param name      控件名称
	 * @param isMust    是否必填
	 * @param isInput   是否可以输入
	 * @param isClear   是否可以清除
	 * @return 类本身
	 */
	private Case endDateCase(String operation, String name, String startDateName, boolean isMust, boolean isInput,
			boolean isClear) {
		// 清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addDateCase";

		// 添加控件名称
		wordMap.put(WordType.CONTROL_NAME.getName(), name);
		// 添加操作类型
		wordMap.put(WordType.OPERATION_TYPE.getName(), operation);

		// 存储标题信息
		addFieldText(LabelType.TITLE, getLabelText(caseName, LabelType.TITLE, "1"));

		// ----------------------------------------------------------------------
		// 存储步骤与预期信息
		// 添加基本用例
		dateboxCommonCase(isMust, isInput, isClear);

		wordMap.put(WordType.START_DATE.getName(), startDateName);
		// 小于开始时间
		relevanceAddData(caseName, "8", "2");

		// 等于开始时间
		relevanceAddData(caseName, "9", "1");
		// ----------------------------------------------------------------------

		// 存储前置条件信息
		addFieldText(LabelType.PRECONDITION, getLabelText(caseName, LabelType.PRECONDITION, "1"));
		addFieldText(LabelType.PRECONDITION,
				getLabelText(caseName, LabelType.PRECONDITION, "新增".equals(operation) ? "2" : "3"));

		// 存储关键词信息
		addFieldText(LabelType.KEY, getLabelText(caseName, LabelType.KEY, "1"));
		// 存储优先级信息
		addFieldText(LabelType.RANK, getLabelText(caseName, LabelType.RANK, "1"));

		return this;
	}

	/**
	 * 用于生成新增信息中上传文件或图片相关的测试用例，当只有单边限制时，则其中一个参数传入小于等于0的数字即可，例如：<br>
	 * 1.输入文件个数限制为2~10个字符时：addUploadFileCase(..., 2, 10, ...)<br>
	 * 2.输入文件个数限制为最多输入10个字符时：addUploadFileCase(..., 0, 10, ...)<br>
	 * 3.输入文件个数限制为最少输入2个字符时：addUploadFileCase(..., 2, 0, ...)<br>
	 * 4.输入文件个数限制仅能输入2个字符时：addUploadFileCase(..., 2, 2, ...)<br>
	 * 5.若两个参数都小于等于0时，则不对文件数量进行限制
	 * 
	 * @param name           控件名称
	 * @param isMust         是否必填
	 * @param isDelete       是否可以删除
	 * @param isPreview      是否可以预览
	 * @param fileSize       文件大小限制（单位为M），传参大于0时表示有文件大小限制
	 * @param minLen         最小文件个数限制
	 * @param maxLen         最大文件个数限制
	 * @param uploadFileType 上传的文件类型
	 * @param fileRuleTypes  文件格式限制
	 * @return 类本身
	 */
	public Case addUploadFileCase(String name, boolean isMust, boolean isDelete, boolean isPreview, int fileSize,
			int minLen, int maxLen, UploadFileType uploadFileType, FileRuleType... fileRuleTypes) {
		return uploadFileCase(OPERATION_ADD, name, isMust, isDelete, isPreview, fileSize, minLen, maxLen,
				uploadFileType, fileRuleTypes);
	}

	/**
	 * 用于生成编辑信息中上传文件或图片相关的测试用例，当只有单边限制时，则其中一个参数传入小于等于0的数字即可，例如：<br>
	 * 1.输入文件个数限制为2~10个字符时：addUploadFileCase(..., 2, 10, ...)<br>
	 * 2.输入文件个数限制为最多输入10个字符时：addUploadFileCase(..., 0, 10, ...)<br>
	 * 3.输入文件个数限制为最少输入2个字符时：addUploadFileCase(..., 2, 0, ...)<br>
	 * 4.输入文件个数限制仅能输入2个字符时：addUploadFileCase(..., 2, 2, ...)<br>
	 * 5.若两个参数都小于等于0时，则不对文件数量进行限制
	 * 
	 * @param name           控件名称
	 * @param isMust         是否必填
	 * @param isDelete       是否可以删除
	 * @param isPreview      是否可以预览
	 * @param fileSize       文件大小限制（单位为M），传参大于0时表示有文件大小限制
	 * @param minLen         最小文件个数限制
	 * @param maxLen         最大文件个数限制
	 * @param uploadFileType 上传的文件类型
	 * @param fileRuleTypes  文件格式限制
	 * @return 类本身
	 */
	public Case editUploadFileCase(String name, boolean isMust, boolean isDelete, boolean isPreview, int fileSize,
			int minLen, int maxLen, UploadFileType uploadFileType, FileRuleType... fileRuleTypes) {
		return uploadFileCase(OPERATION_EDIT, name, isMust, isDelete, isPreview, fileSize, minLen, maxLen,
				uploadFileType, fileRuleTypes);
	}

	/**
	 * 用于生成上传文件或图片相关的测试用例，当只有单边限制时，则其中一个参数传入小于等于0的数字即可，例如：<br>
	 * 1.输入文件个数限制为2~10个字符时：addUploadFileCase(..., 2, 10, ...)<br>
	 * 2.输入文件个数限制为最多输入10个字符时：addUploadFileCase(..., 0, 10, ...)<br>
	 * 3.输入文件个数限制为最少输入2个字符时：addUploadFileCase(..., 2, 0, ...)<br>
	 * 4.输入文件个数限制仅能输入2个字符时：addUploadFileCase(..., 2, 2, ...)<br>
	 * 5.若两个参数都小于等于0时，则不对文件数量进行限制
	 * 
	 * @param operation      操作类型名称
	 * @param name           控件名称
	 * @param isMust         是否必填
	 * @param isDelete       是否可以删除
	 * @param isPreview      是否可以预览
	 * @param fileSize       文件大小限制（单位为M），传参大于0时表示有文件大小限制
	 * @param minLen         最小文件个数限制
	 * @param maxLen         最大文件个数限制
	 * @param uploadFileType 上传的文件类型
	 * @param fileRuleTypes  文件格式限制
	 * @return 类本身
	 */
	private Case uploadFileCase(String operation, String name, boolean isMust, boolean isDelete, boolean isPreview,
			int fileSize, int minLen, int maxLen, UploadFileType uploadFileType, FileRuleType... fileRuleTypes) {
		// 清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addUploadFileCase";

		// 添加控件名称
		wordMap.put(WordType.CONTROL_NAME.getName(), name);
		// 添加操作类型
		wordMap.put(WordType.OPERATION_TYPE.getName(), operation);

		// 转换输入限制为集合类型
		List<FileRuleType> fileRules = Arrays.asList(fileRuleTypes);

		// 存储标题信息
		addFieldText(LabelType.TITLE, getLabelText(caseName, LabelType.TITLE, "1"));

		// ----------------------------------------------------------------------
		// 存储步骤与预期信息
		// 添加文件类型
		wordMap.put(WordType.FILE_TYPE.getName(), uploadFileType.getName());

		// 预览文件
		if (isPreview) {
			// 根据枚举类型选择相应的预期
			switch (uploadFileType) {
			case FILE:
				relevanceAddData(caseName, "11", "6");
				break;
			case IMAGE:
				relevanceAddData(caseName, "11", "5");
				break;
			default:
				throw new IllegalArgumentException("不支持的类型：" + uploadFileType.getName());
			}
		}

		// 不上传文件
		relevanceAddData(caseName, "1", isMust ? "2" : "1");

		// 删除文件用例
		if (isDelete) {
			// 删除文件点保存
			relevanceAddData(caseName, "3", isMust ? "2" : "1");

			// 删除文件再上传
			relevanceAddData(caseName, "2", uploadFileType.equals(UploadFileType.IMAGE) ? "4" : "3");
		}

		// 上传文件个数限制
		// 判断传入的限制参数是否有小于0的参数，参数小于0则直接转换为0
		minLen = minLen < 0 ? 0 : minLen;
		maxLen = maxLen < 0 ? 0 : maxLen;
		// 判断两个限制参数是否其中一个大于0，若是，则添加上传文件个数限制的用例
		// 判断长度是否都为0，若都为0，则抛出异常
		if (minLen != 0 && maxLen == 0) {
			// 存储最小上传限制
			wordMap.put(WordType.UPLOAD_MIN_LENGTH.getName(), String.valueOf(minLen));

			// 小于最小限制
			relevanceAddData(caseName, "7", "2");

			// 等于最小限制
			relevanceAddData(caseName, "8", "1");
		} else if (minLen == 0 && maxLen != 0) {
			// 存储最大输入限制
			wordMap.put(WordType.UPLOAD_MAX_LENGTH.getName(), String.valueOf(maxLen));
			// 大于最大限制
			relevanceAddData(caseName, "9", "2");
			// 等于最大限制
			relevanceAddData(caseName, "10", "1");
		} else if (minLen != 0 && maxLen != 0) {
			// 若都不为0，则将相应的内容进行存储，且判断最大长度与最小长度是否传反，若传反，则调换顺序
			wordMap.put(WordType.UPLOAD_MIN_LENGTH.getName(), String.valueOf(minLen < maxLen ? minLen : maxLen));
			wordMap.put(WordType.UPLOAD_MAX_LENGTH.getName(), String.valueOf(minLen < maxLen ? maxLen : minLen));

			// 小于最小限制
			relevanceAddData(caseName, "7", "2");
			// 等于最小限制
			relevanceAddData(caseName, "8", "1");
			// 大于最大限制
			relevanceAddData(caseName, "8", "2");
			// 若最大最小限制相等，则不添加等于最大限制
			if (minLen != maxLen) {
				// 等于最大限制
				relevanceAddData(caseName, "10", "1");
			}
		} else {
			// 若均等于0，则不进行处理
		}

		// 上传大于限制的文件
		if (fileSize > 0) {
			// 存储大小限制
			wordMap.put(WordType.FILE_SIZE.getName(), String.valueOf(fileSize));
			// 超过大小限制的用例
			relevanceAddData(caseName, "4", "7");
		} else {
			// 超过大小限制的用例
			relevanceAddData(caseName, "5", "8");
		}

		// 上传非限制格式文件
		if (fileRules.size() != 0) {
			String fileRulesText = "";
			for (FileRuleType fileRule : fileRules) {
				fileRulesText += (fileRule.getName() + "、");
			}
			wordMap.put(WordType.FILE_RULE.getName(), fileRulesText.substring(0, fileRulesText.length() - 1));
			relevanceAddData(caseName, "6", "9");
		}
		// ----------------------------------------------------------------------

		// 存储前置条件信息
		addFieldText(LabelType.PRECONDITION, getLabelText(caseName, LabelType.PRECONDITION, "1"));
		addFieldText(LabelType.PRECONDITION,
				getLabelText(caseName, LabelType.PRECONDITION, "新增".equals(operation) ? "2" : "3"));

		// 存储关键词信息
		addFieldText(LabelType.KEY, getLabelText(caseName, LabelType.KEY, "1"));
		// 存储优先级信息
		addFieldText(LabelType.RANK, getLabelText(caseName, LabelType.RANK, "1"));

		return this;
	}

	/**
	 * 用于生成浏览编辑信息界面的测试用例
	 * 
	 * @return 类本身
	 */
	public Case browseEditPageCase() {
		// 清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "openEditPage";

		// 存储标题信息
		addFieldText(LabelType.TITLE, getLabelText(caseName, LabelType.TITLE, "1"));

		// 添加步骤
		addFieldText(LabelType.STEP, getAllLabelText(caseName, LabelType.STEP));

		// 存储预期信息
		addFieldText(LabelType.EXCEPT, getAllLabelText(caseName, LabelType.EXCEPT));

		// 存储前置条件信息
		addFieldText(LabelType.PRECONDITION, getAllLabelText(caseName, LabelType.PRECONDITION));

		// 存储关键词信息
		addFieldText(LabelType.KEY, getLabelText(caseName, LabelType.KEY, "1"));
		// 存储优先级信息
		addFieldText(LabelType.RANK, getLabelText(caseName, LabelType.RANK, "1"));

		return this;
	}

	/**
	 * 用于生成取消保存新增信息相关的测试用例
	 * 
	 * @param cencelButtonName 取消按钮名称
	 * @return 类本身
	 */
	public Case cencelSaveAddDataCase(String cencelButtonName) {
		// 清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "cancelSaveData";

		// 添加信息
		wordMap.put(WordType.OPERATION_TYPE.getName(), OPERATION_ADD);
		wordMap.put(WordType.CENCEL_BUTTON_NAME.getName(), cencelButtonName);

		// 存储标题信息
		addFieldText(LabelType.TITLE, getLabelText(caseName, LabelType.TITLE, "1"));

		// --------------------------------------
		// 添加步骤
		// 点击取消按钮
		relevanceAddData(caseName, "1", "1");
		// 点击关闭界面按钮
		relevanceAddData(caseName, "2", "1");
		// --------------------------------------

		// 存储前置条件信息
		getAllLabelText(caseName, LabelType.PRECONDITION).forEach(text -> {
			addFieldText(LabelType.PRECONDITION, text);
		});

		// 存储关键词信息
		addFieldText(LabelType.KEY, getLabelText(caseName, LabelType.KEY, "1"));
		// 存储优先级信息
		addFieldText(LabelType.RANK, getLabelText(caseName, LabelType.RANK, "1"));

		return this;
	}

	/**
	 * 用于生成取消保存编辑信息相关的测试用例
	 * 
	 * @param cencelButtonName 取消按钮名称
	 * @return 类本身
	 */
	public Case cencelSaveEditDataCase(String cencelButtonName) {
		// 清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "cancelSaveData";

		// 添加信息
		wordMap.put(WordType.OPERATION_TYPE.getName(), OPERATION_EDIT);
		wordMap.put(WordType.CENCEL_BUTTON_NAME.getName(), cencelButtonName);

		// 存储标题信息
		addFieldText(LabelType.TITLE, getLabelText(caseName, LabelType.TITLE, "1"));

		// --------------------------------------
		// 添加步骤
		// 点击取消按钮
		relevanceAddData(caseName, "3", "2");
		// 点击关闭界面按钮
		relevanceAddData(caseName, "4", "2");
		// --------------------------------------

		// 存储前置条件信息
		getAllLabelText(caseName, LabelType.PRECONDITION).forEach(text -> {
			addFieldText(LabelType.PRECONDITION, text);
		});

		// 存储关键词信息
		addFieldText(LabelType.KEY, getLabelText(caseName, LabelType.KEY, "1"));
		// 存储优先级信息
		addFieldText(LabelType.RANK, getLabelText(caseName, LabelType.RANK, "1"));

		return this;
	}

	/**
	 * 用于生成文本框类型共同的测试用例步骤与预期（即普通文本框测试用例的步骤与预期）
	 * 
	 * @param name     控件名称
	 * @param isMust   是否必填
	 * @param isRepeat 是否可以与存在的内容重复
	 * @param isClear  是否有按钮可以清空文本框
	 */
	private void textboxCommonCase(boolean isMust, boolean isRepeat, boolean isClear, InputRuleType... inputRuleTypes) {
		// 存储case标签的name属性内容
		String caseName = "textboxBasicCase";

		// 转换输入限制为集合类型
		List<InputRuleType> inputRules = Arrays.asList(inputRuleTypes);

		// 不填写或只输入空格
		// 根据是否必填来判断填入成功或失败预期
		relevanceAddData(caseName, "1", isMust ? "2" : "1");

		// 填写特殊字符或HTML代码
		// 根据是否存在输入
		relevanceAddData(caseName, "2", inputRules.size() == 0 || inputRules.contains(InputRuleType.SPE) ? "1" : "2");

		// 输入非限制的字符
		if (inputRules.size() != 0) {
			String inputRuleText = "";
			for (InputRuleType inputRule : inputRules) {
				inputRuleText += (inputRule.getName() + "、");
			}
			wordMap.put(WordType.INPUT_RULE.getName(), inputRuleText.substring(0, inputRuleText.length() - 1));
			relevanceAddData(caseName, "3", "2");
		}

		// 清空输入的内容
		if (isClear) {
			// 不填写或只输入空格
			addFieldText(LabelType.STEP, getLabelText(caseName, LabelType.STEP, "13"));
			// 根据是否必填来判断填入成功或失败预期
			addFieldText(LabelType.EXCEPT, getLabelText(caseName, LabelType.EXCEPT, isMust ? "4" : "3"));
		}
	}

	/**
	 * 添加日期相关的基本用例
	 * 
	 * @param isMust  是否必填
	 * @param isInput 是否可输入
	 * @param isClear 是否可清空
	 */
	private void dateboxCommonCase(boolean isMust, boolean isInput, boolean isClear) {
		// 存储case标签的name属性内容
		String caseName = "addDateCase";

		// 不填写
		// 根据是否必填来判断填入成功或失败预期
		relevanceAddData(caseName, "1", isMust ? "2" : "1");

		// 选择当前时间
		relevanceAddData(caseName, "2", isMust ? "2" : "1");

		// 选择时间后清除时间
		if (isClear) {
			relevanceAddData(caseName, "3", isMust ? "2" : "1");
		}

		// 手动输入时间
		if (isInput) {
			// 输入正确格式
			relevanceAddData(caseName, "4", "1");
			// 输入不正确的格式
			relevanceAddData(caseName, "5", "2");
		}
	}

	/**
	 * <p>
	 * <b>文件名：</b>InformationCase.java
	 * </p>
	 * <p>
	 * <b>用途：</b>枚举在预设测试用例中需要被替换的词语
	 * </p>
	 * <p>
	 * <b>编码时间：</b>2020年3月15日下午5:50:28
	 * </p>
	 * <p>
	 * <b>修改时间：</b>2020年3月15日下午5:50:28
	 * </p>
	 * 
	 * @author 彭宇琦
	 * @version Ver1.0
	 * @since JDK 1.8
	 *
	 */
	private enum WordType {
		/**
		 * 提交按钮名称
		 */
		SAVE_BUTTON_NAME("保存按钮名称"),
		/**
		 * 取消按钮名称
		 */
		CENCEL_BUTTON_NAME("取消按钮名称"),
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
		/**
		 * 小数位数
		 */
		INPUT_DECIMALS("小数位数"),
		/**
		 * 结束日期
		 */
		END_DATE("结束日期"),
		/**
		 * 开始日期
		 */
		START_DATE("开始日期"),
		/**
		 * 文件类型
		 */
		FILE_TYPE("文件类型"),
		/**
		 * 最长文件个数限制
		 */
		UPLOAD_MAX_LENGTH("文件最大个数"),
		/**
		 * 最短文件个数限制
		 */
		UPLOAD_MIN_LENGTH("文件最小个数"),
		/**
		 * 文件大小
		 */
		FILE_SIZE("文件大小"),
		/**
		 * 文件格式
		 */
		FILE_RULE("文件格式"),
		/**
		 * 操作类型
		 */
		OPERATION_TYPE("操作类型"),;
		/**
		 * 存储需要替换的单词名称
		 */
		private String name;

		/**
		 * 初始化枚举值
		 * 
		 * @param name 枚举的名称
		 */
		private WordType(String name) {
			this.name = name;
		}

		/**
		 * 返回需要替换的单词的名称
		 * 
		 * @return 需要替换的单词的名称
		 */
		public String getName() {
			return name;
		}
	}

	/**
	 * <p>
	 * <b>文件名：</b>InputRuleType.java
	 * </p>
	 * <p>
	 * <b>用途：</b>用于枚举控件中可输入字符类型的限制
	 * </p>
	 * <p>
	 * <b>编码时间：</b>2020年3月14日 下午9:14:30
	 * </p>
	 * <p>
	 * <b>修改时间：</b>2020年3月14日 下午9:14:30
	 * </p>
	 * 
	 * @author 彭宇琦
	 * @version Ver1.0
	 * @since JDK 1.8
	 */
	public enum InputRuleType {
		/**
		 * 中文
		 */
		CH("中文"),
		/**
		 * 英文
		 */
		EN("英文"),
		/**
		 * 数字
		 */
		NUM("数字"),
		/**
		 * 特殊字符
		 */
		SPE("特殊字符"),
		/**
		 * 小写字母
		 */
		LOW("小写字母"),
		/**
		 * 大写字母
		 */
		CAP("大写字母");

		/**
		 * 枚举名称
		 */
		private String name;

		/**
		 * 初始化枚举名称
		 * 
		 * @param name 枚举名称
		 */
		private InputRuleType(String name) {
			this.name = name;
		}

		/**
		 * 返回枚举名称
		 * 
		 * @return 枚举名称
		 */
		public String getName() {
			return name;
		}
	}

	/**
	 * <p>
	 * <b>文件名：</b>InputRuleType.java
	 * </p>
	 * <p>
	 * <b>用途：</b>用于枚举号码限制
	 * </p>
	 * <p>
	 * <b>编码时间：</b>2020年3月14日 下午9:14:30
	 * </p>
	 * <p>
	 * <b>修改时间：</b>2020年3月14日 下午9:14:30
	 * </p>
	 * 
	 * @author 彭宇琦
	 * @version Ver1.0
	 * @since JDK 1.8
	 */
	public enum PhoneType {
		/**
		 * 设置类型为移动电话（手机）
		 */
		MOBLE,
		/**
		 * 设置类型为固定电话（座机）
		 */
		FIXED;
	}

	/**
	 * <p>
	 * <b>文件名：</b>InformationCase.java
	 * </p>
	 * <p>
	 * <b>用途：</b>用于枚举上传文件的格式
	 * </p>
	 * <p>
	 * <b>编码时间：</b>2020年3月18日上午9:30:34
	 * </p>
	 * <p>
	 * <b>修改时间：</b>2020年3月18日上午9:30:34
	 * </p>
	 * 
	 * @author 彭宇琦
	 * @version Ver1.0
	 * @since JDK 1.8
	 *
	 */
	public enum FileRuleType {
		/**
		 * jpg格式
		 */
		JPG("jpg"),
		/**
		 * gif格式
		 */
		GIF("gif"),
		/**
		 * png格式
		 */
		PNG("png"),
		/**
		 * bmp格式
		 */
		BMP("bmp"),
		/**
		 * doc格式
		 */
		DOC("doc"),
		/**
		 * docx格式
		 */
		DOCX("docx"),
		/**
		 * txt格式
		 */
		TXT("txt"),
		/**
		 * xls格式
		 */
		XLS("xls"),
		/**
		 * xlsx格式
		 */
		XLSX("xlsx"),
		/**
		 * pdf格式
		 */
		PDF("pdf"),
		/**
		 * csv格式
		 */
		CSV("csv"),;

		/**
		 * 文件格式名称
		 */
		private String name;

		/**
		 * 初始化枚举名称
		 * 
		 * @param name 枚举名称
		 */
		private FileRuleType(String name) {
			this.name = name;
		}

		/**
		 * 返回格式名称
		 */
		public String getName() {
			return name;
		}
	}

	/**
	 * <p>
	 * <b>文件名：</b>InformationCase.java
	 * </p>
	 * <p>
	 * <b>用途：</b>用于枚举上传文件的类型
	 * </p>
	 * <p>
	 * <b>编码时间：</b>2020年3月18日上午9:56:51
	 * </p>
	 * <p>
	 * <b>修改时间：</b>2020年3月18日上午9:56:51
	 * </p>
	 * 
	 * @author 彭宇琦
	 * @version Ver1.0
	 * @since JDK 1.8
	 *
	 */
	public enum UploadFileType {
		/**
		 * 上传文件
		 */
		FILE("文件"),
		/**
		 * 上传图片
		 */
		IMAGE("图片");

		/**
		 * 上传文件类型名称
		 */
		private String name;

		/**
		 * 返回枚举名称
		 * 
		 * @return 枚举名称
		 */
		public String getName() {
			return name;
		}

		/**
		 * 初始化枚举值名称
		 * 
		 * @param name 枚举值名称
		 */
		private UploadFileType(String name) {
			this.name = name;
		}
	}
}
