package com.auxiliary.testcase.templet;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.auxiliary.testcase.templet.InformationCase.PhoneType;

/**
 * <p>
 * <b>文件名：</b>InterfaceCase.java
 * </p>
 * <p>
 * <b>用途：</b> 用于生成接口测试相关的测试用例
 * </p>
 * <p>
 * <b>编码时间：</b>2021年6月29日下午7:55:01
 * </p>
 * <p>
 * <b>修改时间：</b>2021年6月29日下午7:55:01
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 2.4.0
 * @deprecated 用例生成类已重构，可参考{@link AbstractCaseTemplet}及其相应的子类，原始用例生成类将于4.1.0或后续版本中删除
 */
@Deprecated
public class InterfaceCase extends Case {
	private final String FIELD_TYPE = "字段类型";
	private final String INTER_NAME = "接口名称";
	private final String FIELD_NAME = "字段名称";
	private final String LIMIT_MIN_LENGTH = "最小限制";
	private final String LIMIT_MAX_LENGTH = "最大限制";
	private final String END_DATE_NAME = "结束日期";
	private final String START_DATE_NAME = "开始日期";

	/**
	 * 限制最小值，将最小限制赋予该值后，则表示不做最小限制
	 */
	public static final int RULE_MIN = Integer.MIN_VALUE;
	/**
	 * 限制最大值，将最大限制赋予该值后，则表示不做最大限制
	 */
	public static final int RULE_MAX = Integer.MAX_VALUE;

	/**
	 * 存储验签字段集合
	 */
	private ArrayList<String> attestationFieldList = new ArrayList<>();

	public InterfaceCase(File configXmlFile) {
		super(configXmlFile);
	}
	
	/**
	 * 通过默认的用例模板库来构造对象
	 */
	public InterfaceCase() {
		super(new File(InformationCase.class.getClassLoader()
				.getResource("com/auxiliary/testcase/templet/InterfaceCase.xml").getFile()));
	}

	/**
	 * 用于设置接口名称
	 * 
	 * @param interName 接口名称
	 */
	public void setInterfaceName(String interName) {
		// 添加接口名称
		wordMap.put(INTER_NAME, interName);
	}

	/**
	 * 用于添加验签字段的名称，以准确输出用例
	 * 
	 * @param attestationField 验签字段
	 */
	public void addAttestationField(String attestationField) {
		attestationFieldList.add(attestationField);
	}

	/**
	 * 用于生成正确填写所有字段的用例
	 * 
	 * @return 类本身
	 */
	public Case wholeFieldCase() {
		// 清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "整体字段";

		// 存储标题信息
		addFieldText(LabelType.TITLE, getLabelText(caseName, LabelType.TITLE, "1"));

		// 存储步骤信息
		addFieldText(LabelType.STEP, getLabelText(caseName, LabelType.STEP, "1"));

		// 存储预期信息
		addFieldText(LabelType.EXCEPT, getLabelText(caseName, LabelType.EXCEPT, "1"));

		// 存储前置条件信息

		// 存储关键词信息
		addFieldText(LabelType.KEY, getLabelText(caseName, LabelType.KEY, "1"));

		// 存储优先级信息
		addFieldText(LabelType.RANK, getLabelText(caseName, LabelType.RANK, "1"));

		return this;
	}

	/**
	 * 用于生成填写部分字段的用例
	 * 
	 * @param existAttestation 是否存在验签串
	 * @return 类本身
	 */
	public Case unwholeFieldCase(boolean existAttestation) {
		// 清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "整体字段";

		// 存储标题信息
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

		// 存储前置条件信息

		// 存储关键词信息
		addFieldText(LabelType.KEY, getLabelText(caseName, LabelType.KEY, "1"));

		// 存储优先级信息
		addFieldText(LabelType.RANK, getLabelText(caseName, LabelType.RANK, "1"));

		return this;
	}

	/**
	 * 用于生成无限制字段的用例
	 * 
	 * @param fieldName     字段名称
	 * @param isMust        是否必传
	 * @param isRepeat      是否允许值重复
	 * @param jsonFieldType 字段可传入类型
	 * @return 类本身
	 */
	public Case basicCase(String fieldName, boolean isMust, boolean isRepeat, JsonFieldType jsonFieldType) {
		// 清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String basicCaseName = "字段基础用例";
		String constraintCaseName = "字段条件约束";
		
		// 存储字段名称
		wordMap.put(FIELD_NAME, fieldName);

		// 存储标题信息
		addFieldText(LabelType.TITLE, getLabelText(basicCaseName, LabelType.TITLE, "1"));

		// 存储步骤与预期信息
		addFieldText(LabelType.STEP, getLabelText(basicCaseName, LabelType.STEP, "1"));
		if (isMust) {
			if (attestationFieldList.contains(constraintCaseName)) {
				addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "3"));
			} else {
				addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "2"));
			}
		} else {
			addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "1"));
		}

		addFieldText(LabelType.STEP, getLabelText(basicCaseName, LabelType.STEP, "2"));
		if (jsonFieldType == JsonFieldType.STRING) {
			addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "1"));
		} else {
			addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "2"));
		}

		if (!isRepeat) {
			addFieldText(LabelType.STEP, getLabelText(basicCaseName, LabelType.STEP, "3"));
			addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "2"));
		}

		addConstraintCase(isMust, jsonFieldType);

		// 存储前置条件信息
		getAllLabelText(basicCaseName, LabelType.PRECONDITION).forEach(text -> {
			addFieldText(LabelType.PRECONDITION, text);
		});

		// 存储关键词信息
		addFieldText(LabelType.KEY, getLabelText(basicCaseName, LabelType.KEY, "1"));

		// 存储优先级信息
		addFieldText(LabelType.RANK, getLabelText(basicCaseName, LabelType.RANK, "1"));

		return this;
	}

	/**
	 * 用于生成条件约束与异常约束相关的用例
	 * 
	 * @param isMust        是否必填
	 * @param jsonFieldType 字段类型
	 */
	private void addConstraintCase(boolean isMust, JsonFieldType jsonFieldType) {
		String basicCaseName = "字段基础用例";
		String constraintCaseName = "字段条件约束";
		String exceptionCaseName = "字段异常约束";

		// 存储字段名称
		wordMap.put(FIELD_TYPE, jsonFieldType.getTypeName());

		addFieldText(LabelType.STEP, getLabelText(constraintCaseName, LabelType.STEP, "1"));
		addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "2"));

		// 存储步骤与预期信息
		switch (jsonFieldType) {
		case STRING:
			addFieldText(LabelType.STEP, getLabelText(constraintCaseName, LabelType.STEP, "2"));
			if (isMust) {
				if (attestationFieldList.contains(constraintCaseName)) {
					addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "3"));
				} else {
					addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "2"));
				}
			} else {
				addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "1"));
			}

			addFieldText(LabelType.STEP, getLabelText(exceptionCaseName, LabelType.STEP, "1"));
			addFieldText(LabelType.EXCEPT, getLabelText(exceptionCaseName, LabelType.EXCEPT, "1"));

			addFieldText(LabelType.STEP, getLabelText(exceptionCaseName, LabelType.STEP, "2"));
			addFieldText(LabelType.EXCEPT, getLabelText(exceptionCaseName, LabelType.EXCEPT, "1"));
			break;
		case NUMBER:
			addFieldText(LabelType.STEP, getLabelText(constraintCaseName, LabelType.STEP, "5"));
			addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "1"));
			break;
		case BOOLEAN:
			addFieldText(LabelType.STEP, getLabelText(constraintCaseName, LabelType.STEP, "6"));
			addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "1"));
			break;
		case JSON:
			addFieldText(LabelType.STEP, getLabelText(constraintCaseName, LabelType.STEP, "3"));
			if (isMust) {
				if (attestationFieldList.contains(constraintCaseName)) {
					addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "3"));
				} else {
					addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "2"));
				}
			} else {
				addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "1"));
			}
			break;
		case ARRAY:
			addFieldText(LabelType.STEP, getLabelText(constraintCaseName, LabelType.STEP, "4"));
			if (isMust) {
				if (attestationFieldList.contains(constraintCaseName)) {
					addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "3"));
				} else {
					addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "2"));
				}
			} else {
				addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "1"));
			}

			addFieldText(LabelType.STEP, getLabelText(exceptionCaseName, LabelType.STEP, "3"));
			addFieldText(LabelType.EXCEPT, getLabelText(exceptionCaseName, LabelType.EXCEPT, "1"));
			break;
		default:
			break;
		}
	}

	/**
	 * 用于生成文本长度限制相关的用例
	 * <p>
	 * 当最小限制赋予{@link #LIMIT_MIN_LENGTH}时，则表示无最小限制，同理，当最大限制赋予{@link #LIMIT_MAX_LENGTH}时，则表示无最大限制
	 * </p>
	 * 
	 * @param fieldName 字段名称
	 * @param isMust    是否必传
	 * @param isRepeat  是否允许值重复
	 * @param minLen    最小长度
	 * @param maxLen    最大长度
	 * @return 类本身
	 */
	public Case stringLengthRuleCase(String fieldName, boolean isMust, boolean isRepeat, int minLen, int maxLen) {
		// 存储case标签的name属性内容
		basicCase(fieldName, isMust, isRepeat, JsonFieldType.STRING);
		addLengthRuleCase(minLen, maxLen, JsonFieldType.STRING);
		return this;
	}

	/**
	 * 用于生成整形数字大小限制相关的用例
	 * <p>
	 * 当最小限制赋予{@link #LIMIT_MIN_LENGTH}时，则表示无最小限制，同理，当最大限制赋予{@link #LIMIT_MAX_LENGTH}时，则表示无最大限制
	 * </p>
	 * 
	 * @param fieldName 字段名称
	 * @param isMust    是否必传
	 * @param isRepeat  是否允许值重复
	 * @param minNum    最小长度
	 * @param maxNum    最大长度
	 * @return 类本身
	 */
	public Case intNumberRuleCase(String fieldName, boolean isMust, boolean isRepeat, int minNum, int maxNum) {
		// 存储case标签的name属性内容
		basicCase(fieldName, isMust, isRepeat, JsonFieldType.NUMBER);
		addLengthRuleCase(minNum, maxNum, JsonFieldType.NUMBER);

		String businessCaseName = "字段业务约束";
		String basicCaseName = "字段基础用例";
		addFieldText(LabelType.STEP, getLabelText(businessCaseName, LabelType.STEP, "12"));
		addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "1"));

		return this;
	}

	/**
	 * 用于生成小数数字大小限制相关的用例
	 * <p>
	 * 当最小限制赋予{@link #LIMIT_MIN_LENGTH}时，则表示无最小限制，同理，当最大限制赋予{@link #LIMIT_MAX_LENGTH}时，则表示无最大限制
	 * </p>
	 * 
	 * @param fieldName   字段名称
	 * @param isMust      是否必传
	 * @param isRepeat    是否允许值重复
	 * @param decimalsNum 小数位长度限制
	 * @param minNum      最小长度
	 * @param maxNum      最大长度
	 * @return 类本身
	 */
	public Case doubleNumberRuleCase(String fieldName, boolean isMust, boolean isRepeat, int decimalsNum, int minNum,
			int maxNum) {
		// 存储case标签的name属性内容
		basicCase(fieldName, isMust, isRepeat, JsonFieldType.NUMBER);
		addLengthRuleCase(minNum, maxNum, JsonFieldType.NUMBER);

		String businessCaseName = "字段业务约束";
		String basicCaseName = "字段基础用例";
		if (decimalsNum > 0) {
			addFieldText(LabelType.STEP, getLabelText(businessCaseName, LabelType.STEP, "9"));
			addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "1"));

			addFieldText(LabelType.STEP, getLabelText(businessCaseName, LabelType.STEP, "10"));
			addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "1"));

			addFieldText(LabelType.STEP, getLabelText(businessCaseName, LabelType.STEP, "11"));
			addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "1"));
		}

		return this;
	}

	/**
	 * 用于生成json数组长度限制相关的用例
	 * <p>
	 * 当最小限制赋予{@link #LIMIT_MIN_LENGTH}时，则表示无最小限制，同理，当最大限制赋予{@link #LIMIT_MAX_LENGTH}时，则表示无最大限制
	 * </p>
	 * 
	 * @param fieldName 字段名称
	 * @param isMust    是否必传
	 * @param isRepeat  是否允许值重复
	 * @param minLen    最小长度
	 * @param maxLen    最大长度
	 * @return 类本身
	 */
	public Case arrayLengthRuleCase(String fieldName, boolean isMust, boolean isRepeat, int minLen, int maxLen) {
		// 存储case标签的name属性内容
		basicCase(fieldName, isMust, isRepeat, JsonFieldType.ARRAY);
		addLengthRuleCase(minLen, maxLen, JsonFieldType.ARRAY);

		return this;
	}

	/**
	 * 用于根据字段类型添加业务约束中长度或数字相关的约束
	 * 
	 * @param minLen        最小约束
	 * @param maxLen        最大约束
	 * @param jsonFieldType 字段类型
	 */
	private void addLengthRuleCase(int minLen, int maxLen, JsonFieldType jsonFieldType) {
		// 存储case标签的name属性内容
		String businessCaseName = "字段业务约束";
		String basicCaseName = "字段基础用例";

		// 存储字段名称
		int ruleMin = Math.min(minLen, maxLen);
		int ruleMax = Math.max(minLen, maxLen);
		wordMap.put(LIMIT_MAX_LENGTH, String.valueOf(ruleMax));
		wordMap.put(LIMIT_MIN_LENGTH, String.valueOf(ruleMin));

		if (ruleMin != RULE_MIN && ruleMax != RULE_MAX) {
			// 若两边均不为无限制
			if (ruleMin == ruleMax) {
				// 若限制两边相等
				addFieldText(LabelType.STEP, getLabelText(businessCaseName, LabelType.STEP,
						String.format("%s_%s_%s", jsonFieldType.getTypeName(), "小于", "最小限制")));
				addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "2"));

				addFieldText(LabelType.STEP, getLabelText(businessCaseName, LabelType.STEP,
						String.format("%s_%s_%s", jsonFieldType.getTypeName(), "等于", "最小限制")));
				addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "1"));

				addFieldText(LabelType.STEP, getLabelText(businessCaseName, LabelType.STEP,
						String.format("%s_%s_%s", jsonFieldType.getTypeName(), "大于", "最大限制")));
				addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "2"));
			} else {
				// 若限制两边不等
				addFieldText(LabelType.STEP, getLabelText(businessCaseName, LabelType.STEP,
						String.format("%s_%s_%s", jsonFieldType.getTypeName(), "小于", "最小限制")));
				addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "2"));

				addFieldText(LabelType.STEP, getLabelText(businessCaseName, LabelType.STEP,
						String.format("%s_%s_%s", jsonFieldType.getTypeName(), "等于", "最小限制")));
				addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "1"));

				addFieldText(LabelType.STEP, getLabelText(businessCaseName, LabelType.STEP,
						String.format("%s_%s_%s", jsonFieldType.getTypeName(), "大于", "最大限制")));
				addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "2"));

				addFieldText(LabelType.STEP, getLabelText(businessCaseName, LabelType.STEP,
						String.format("%s_%s_%s", jsonFieldType.getTypeName(), "等于", "最大限制")));
				addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "1"));
			}
		} else if (ruleMin == RULE_MIN && ruleMax != RULE_MAX) {
			// 若只有最大限制
			addFieldText(LabelType.STEP, getLabelText(businessCaseName, LabelType.STEP,
					String.format("%s_%s_%s", jsonFieldType.getTypeName(), "大于", "最大限制")));
			addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "2"));

			addFieldText(LabelType.STEP, getLabelText(businessCaseName, LabelType.STEP,
					String.format("%s_%s_%s", jsonFieldType.getTypeName(), "等于", "最大限制")));
			addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "1"));
		} else if (ruleMin != RULE_MIN && ruleMax == RULE_MAX) {
			// 若只有最小限制
			addFieldText(LabelType.STEP, getLabelText(businessCaseName, LabelType.STEP,
					String.format("%s_%s_%s", jsonFieldType.getTypeName(), "小于", "最小限制")));
			addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "2"));

			addFieldText(LabelType.STEP, getLabelText(businessCaseName, LabelType.STEP,
					String.format("%s_%s_%s", jsonFieldType.getTypeName(), "等于", "最小限制")));
			addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "1"));
		} else {
			// 若两边均为无限制，则不做处理
		}
	}

	/**
	 * 用于生成电话号码相关的测试用例
	 * 
	 * @param fieldName  字段名称
	 * @param isMust     是否必填
	 * @param isRepeat   是否可重复
	 * @param phoneTypes 号码类型枚举
	 * @return 类本身
	 */
	public Case phoneTypeCase(String fieldName, boolean isMust, boolean isRepeat, PhoneType... phoneTypes) {
		// 存储case标签的name属性内容
		basicCase(fieldName, isMust, isRepeat, JsonFieldType.STRING);

		String phoneCaseName = "号码类型用例";
		String basicCaseName = "字段基础用例";

		// 转换输入限制为集合类型
		List<PhoneType> phoneRules = Arrays.asList(phoneTypes);
		// 添加手机相关的测试用例
		if (phoneRules.contains(PhoneType.MOBLE)) {
			addFieldText(LabelType.STEP, getLabelText(phoneCaseName, LabelType.STEP, "1"));
			addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "2"));

			addFieldText(LabelType.STEP, getLabelText(phoneCaseName, LabelType.STEP, "2"));
			addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "2"));
		}

		// 添加座机相关的测试用例
		if (phoneRules.contains(PhoneType.FIXED)) {
			addFieldText(LabelType.STEP, getLabelText(phoneCaseName, LabelType.STEP, "3"));
			addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "2"));

			addFieldText(LabelType.STEP, getLabelText(phoneCaseName, LabelType.STEP, "4"));
			addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "2"));

			addFieldText(LabelType.STEP, getLabelText(phoneCaseName, LabelType.STEP, "6"));
			addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "1"));
		}

		addFieldText(LabelType.STEP, getLabelText(phoneCaseName, LabelType.STEP, "5"));
		addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "2"));

		return this;
	}

	/**
	 * 用于生成身份证相关的测试用例
	 * 
	 * @param fieldName 字段名称
	 * @param isMust    是否必填
	 * @param isRepeat  是否可重复
	 * @return 类本身
	 */
	public Case idCardTypeCase(String fieldName, boolean isMust, boolean isRepeat) {
		// 存储case标签的name属性内容
		basicCase(fieldName, isMust, isRepeat, JsonFieldType.STRING);

		String idCardCaseName = "身份证类型用例";
		String basicCaseName = "字段基础用例";

		addFieldText(LabelType.STEP, getLabelText(idCardCaseName, LabelType.STEP, "1"));
		addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "1"));

		addFieldText(LabelType.STEP, getLabelText(idCardCaseName, LabelType.STEP, "2"));
		addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "1"));

		addFieldText(LabelType.STEP, getLabelText(idCardCaseName, LabelType.STEP, "3"));
		addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "1"));

		addFieldText(LabelType.STEP, getLabelText(idCardCaseName, LabelType.STEP, "4"));
		addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "2"));

		addFieldText(LabelType.STEP, getLabelText(idCardCaseName, LabelType.STEP, "5"));
		addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "2"));

		addFieldText(LabelType.STEP, getLabelText(idCardCaseName, LabelType.STEP, "6"));
		addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "2"));

		addFieldText(LabelType.STEP, getLabelText(idCardCaseName, LabelType.STEP, "7"));
		addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "2"));

		return this;
	}

	/**
	 * 用于普通日期相关的测试用例
	 * 
	 * @param fieldName 字段名称
	 * @param isMust    是否必填
	 * @param isFormat  是否为格式化的时间（非毫秒值）
	 * @return 类本身
	 */
	public Case normalDateTypeCase(String fieldName, boolean isMust, boolean isFormat) {
		// 存储case标签的name属性内容
		if (isFormat) {
			basicCase(fieldName, isMust, true, JsonFieldType.STRING);
		} else {
			basicCase(fieldName, isMust, true, JsonFieldType.NUMBER);
		}

		String dateCaseName = "日期类型用例";
		String basicCaseName = "字段基础用例";

		addFieldText(LabelType.STEP, getLabelText(dateCaseName, LabelType.STEP, "1"));
		addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "1"));

		if (isFormat) {
			addFieldText(LabelType.STEP, getLabelText(dateCaseName, LabelType.STEP, "2"));
			addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "2"));

			addFieldText(LabelType.STEP, getLabelText(dateCaseName, LabelType.STEP, "4"));
			addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "2"));
		} else {
			addFieldText(LabelType.STEP, getLabelText(dateCaseName, LabelType.STEP, "3"));
			addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "2"));
		}

		return this;
	}

	/**
	 * 用于生成开始日期相关的测试用例
	 * 
	 * @param fieldName        字段名称
	 * @param endDateFieldName 结束日期字段名称
	 * @param isMust           是否必填
	 * @param isFormat         是否为格式化的时间（非毫秒值）
	 * @return 类本身
	 */
	public Case startDateTypeCase(String fieldName, String endDateFieldName, boolean isMust, boolean isFormat) {
		normalDateTypeCase(fieldName, isMust, isFormat);

		String dateCaseName = "日期类型用例";
		String basicCaseName = "字段基础用例";

		wordMap.put(END_DATE_NAME, endDateFieldName);

		addFieldText(LabelType.STEP, getLabelText(dateCaseName, LabelType.STEP, "5"));
		addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "2"));

		addFieldText(LabelType.STEP, getLabelText(dateCaseName, LabelType.STEP, "6"));
		addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "1"));

		return this;
	}

	/**
	 * 用于生成结束日期相关的测试用例
	 * 
	 * @param fieldName          字段名称
	 * @param startDateFieldName 开始日期字段名称
	 * @param isMust             是否必填
	 * @param isFormat           是否为格式化的时间（非毫秒值）
	 * @return 类本身
	 */
	public Case endDateTypeCase(String fieldName, String startDateFieldName, boolean isMust, boolean isFormat) {
		normalDateTypeCase(fieldName, isMust, isFormat);

		String dateCaseName = "日期类型用例";
		String basicCaseName = "字段基础用例";

		wordMap.put(START_DATE_NAME, startDateFieldName);

		addFieldText(LabelType.STEP, getLabelText(dateCaseName, LabelType.STEP, "7"));
		addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "2"));

		addFieldText(LabelType.STEP, getLabelText(dateCaseName, LabelType.STEP, "8"));
		addFieldText(LabelType.EXCEPT, getLabelText(basicCaseName, LabelType.EXCEPT, "1"));

		return this;
	}

	/**
	 * <p>
	 * <b>文件名：</b>InterfaceCase.java
	 * </p>
	 * <p>
	 * <b>用途：</b> 指定json串的字段类型
	 * </p>
	 * <p>
	 * <b>编码时间：</b>2021年6月26日下午7:08:13
	 * </p>
	 * <p>
	 * <b>修改时间：</b>2021年6月26日下午7:08:13
	 * </p>
	 * 
	 * @author 彭宇琦
	 * @version Ver1.0
	 * @since JDK 1.8
	 */
	public enum JsonFieldType {
		/**
		 * 数字类型
		 */
		NUMBER("数字"),
		/**
		 * 布尔类型
		 */
		BOOLEAN("布尔"),
		/**
		 * 字符串类型
		 */
		STRING("字符串"),
		/**
		 * json类型
		 */
		JSON("json"),
		/**
		 * 数组类型
		 */
		ARRAY("json数组");

		String typeName;

		private JsonFieldType(String typeName) {
			this.typeName = typeName;
		}

		/**
		 * 用于返回枚举名称
		 * 
		 * @return 枚举名称
		 */
		public String getTypeName() {
			return typeName;
		}
	}
}
