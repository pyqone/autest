package com.auxiliary.testcase.script;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auxiliary.selenium.element.ElementType;
import com.auxiliary.selenium.event.OperateType;
import com.auxiliary.selenium.location.ByType;
import com.auxiliary.testcase.file.IncorrectContentException;
import com.auxiliary.testcase.templet.GetAutoScript;
import com.auxiliary.testcase.templet.InformationCase;
import com.auxiliary.tool.date.Time;

/**
 * <p>
 * <b>文件名：</b>TestNGAutoScript.java
 * </p>
 * <p>
 * <b>用途：</b> 用于根据用例json，生成TestNG框架下的自动化测试脚本
 * </p>
 * <p>
 * <b>编码时间：</b>2021年7月17日下午6:19:24
 * </p>
 * <p>
 * <b>修改时间：</b>2021年7月17日下午6:19:24
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public class TestNGAutoScript extends AbstractAutoScript {
	/**
	 * 生成脚本中点击事件类对象名称
	 */
	public final static String CLASS_CLICK_EVENT = "clickEvent";
	/**
	 * 生成脚本中输入事件类对象名称
	 */
	public final static String CLASS_INPUT_EVENT = "inputEvent";
	/**
	 * 生成脚本中等待事件类对象名称
	 */
	public final static String CLASS_WAIT_EVENT = "waitEvent";
	/**
	 * 生成脚本中断言事件类对象名称
	 */
	public final static String CLASS_ASSERT_EVENT = "assertEvent";

	/**
	 * 生成脚本中普通元素类对象名称
	 */
	public final static String CLASS_COMMON_ELEMENT = "commonElement";
	/**
	 * 生成脚本中列表元素类对象名称
	 */
	public final static String CLASS_DATA_LIST_ELEMENT = "listElement";
	/**
	 * 生成脚本中选择型元素类对象名称
	 */
	public final static String CLASS_SELECT_ELEMENT = "selectElement";

	private final String TEMP_CLASS_INDEX = "脚本类编号";
	private final String TEMP_CASE_TITLE = "用例标题";
	private final String TEMP_CASE_TOTAL_STEP = "用例步骤";
	private final String TEMP_CASE_SIGN_STEP = "单条用例步骤";
	private final String TEMP_CASE_TOTAL_EXCEPT = "用例预期";
	private final String TEMP_CASE_SIGN_EXCEPT = "单条用例预期";
	private final String TEMP_WRITE_TIME = "编码时间";
	private final String TEMP_SCRIPT_BEFORE_CLASS = "BeforeClass";
	private final String TEMP_SCRIPT_AFTER_CLASS = "AfterClass";
	private final String TEMP_SCRIPT_BEFORE_METHOD = "BeforeMethod";
	private final String TEMP_SCRIPT_AFTER_METHOD = "AfterMethod";
	private final String TEMP_SCRIPT_STEP = "用例步骤脚本";
	private final String TEMP_SCRIPT_STEP_INDEX = "步骤数目";
	private final String TEMP_SCRIPT_STEP_OPERATE = "操作脚本";
	private final String TEMP_SCRIPT_OPERATE_INPUT = "输入";
	private final String TEMP_SCRIPT_METHOD = "其他方法";

	/**
	 * 用于拼接待替换的词语
	 */
	private final String REPLACE_WORD = "\\#\\{%s\\}";
	/**
	 * 用于替换类说明中的步骤与预期的格式
	 */
	private final String REPLACE_STEP_WORD = " * <li>%s</li>";
	/**
	 * 用于每个操作步骤脚本后的换行符号
	 */
	private final String REPLACE_OPERATE_LINE_SIGN = "\r\n";

	/**
	 * 指向用例步骤模板文件名称
	 */
	protected final String STEP_TERMPLET_NAME = "CaseStepTemplet.txt";
	/**
	 * 指向用例框架模板文件名称
	 */
	protected final String CASE_FRAME_TERMPLET_NAME = "CaseFrameTemplet.txt";

	/**
	 * 存储模板文件的存放路径
	 */
	File templetFileFolder;

	/**
	 * 根据{@link GetAutoScript}类对象返回的内容，初始化脚本生成方法
	 * 
	 * @param caseTemplet 用例模板类对象
	 */
	public TestNGAutoScript(GetAutoScript caseTemplet) {
		super(caseTemplet);
		templetFileFolder = new File(InformationCase.class.getClassLoader()
				.getResource("ConfigurationFiles/CaseConfigurationFile/CaseAutoScriptTemplet/TestNGTemplet/")
				.getFile());
	}

	@Override
	public void writeScriptFile(File saveFile) {
		// 读取并存储用例框架模板中的内容
		String caseTempletScript = readScriptTempleatFile(new File(templetFileFolder, CASE_FRAME_TERMPLET_NAME), "\r\n",
				"", "");
		// 读取用例json，获取case中的所有内容
		JSONArray caseListJson = scriptJson.getJSONArray(GetAutoScript.KEY_CASE);
		for (int caseIndex = 0; caseIndex < caseListJson.size(); caseIndex++) {
			String script = saveCaseFrameTemplet(caseListJson.getJSONObject(caseIndex), caseTempletScript.toString());
			// 替换脚本类编号
			script = script.replaceAll(String.format(REPLACE_WORD, TEMP_CLASS_INDEX), String.valueOf(caseIndex + 1));
			// TODO 调试使用
			System.out.println(script);
		}
	}

	@Override
	public void writeElementFile(File saveFile, ByType... byTypes) {
		Document dom = DocumentHelper.createDocument();
		Element root = dom.addElement("project");

		// 添加模板标签，并加上示例
		root.addElement("templet").addComment("用于存放元素定位内容模板").addElement("xpath").addAttribute("id", "示例")
				.addText("//*[text()='${name}']");

		// 读取用例json，获取element中的所有内容
		JSONArray caseListJson = scriptJson.getJSONArray(GetAutoScript.KEY_ELEMENT);
		for (int index = 0; index < caseListJson.size(); index++) {
			try {
				String elementName = caseListJson.getString(index);

				// 添加element节点
				Element elementNode = root.addElement("element").addAttribute("name", elementName);
				// 根据启用的by标签，来添加相应的内容
				for (ByType byType : byTypes) {
					elementNode.addElement(byType.getValue()).addAttribute("is_use", "true").addText("");
				}
			} catch (Exception e) {
				throw new IncorrectContentException("元素数组中，第" + index + "个元素无法转换为字符串");
			}
		}

		// TODO 调试使用
		System.out.println(dom.asXML());
	}

	/**
	 * 用于替换并存储测试用例脚本文件
	 * 
	 * @param saveFile 脚本存储路径
	 */
	private String saveCaseFrameTemplet(JSONObject caseJson, String caseTempletScript) {
		// 记录测试用例步骤以及预期
		StringJoiner stepText = new StringJoiner("\r\n");
		StringJoiner exceptText = new StringJoiner("\r\n");
		StringJoiner operateScript = new StringJoiner("\r\n\r\n");

		// 转换文本，由于一个json中包含多条case，故需要为每一个case附带一份模板
		String content = caseTempletScript.toString();

		// 获取操作步骤信息
		JSONArray stepListJson = caseJson.getJSONArray(GetAutoScript.KEY_STEP);
		for (int stepIndex = 0; stepIndex < stepListJson.size(); stepIndex++) {
			// 获取用例json
			JSONObject stepJson = stepListJson.getJSONObject(stepIndex);

			// 获取步骤与预期的文本
			JSONObject recordJson = stepJson.getJSONObject(GetAutoScript.KEY_RECORD);
			stepText.add(String.format(REPLACE_STEP_WORD, recordJson.getString(GetAutoScript.KEY_CASE_STEP_TEXT)));
			exceptText.add(String.format(REPLACE_STEP_WORD, recordJson.getString(GetAutoScript.KEY_CASE_EXCEPT_TEXT)));

			// 存储步骤脚本
			operateScript.add(readStepTemplet(stepIndex + 1, recordJson.getString(GetAutoScript.KEY_CASE_STEP_TEXT),
					recordJson.getString(GetAutoScript.KEY_CASE_EXCEPT_TEXT),
					analysisOperate(stepJson.getJSONArray(GetAutoScript.KEY_OPERATE_STEP))));
		}

		// 替换标题
		content = content.replaceAll(String.format(REPLACE_WORD, TEMP_CASE_TITLE),
				caseJson.getString(GetAutoScript.KEY_CASE_TITLE_TEXT));
		// 替换编码时间
		content = content.replaceAll(String.format(REPLACE_WORD, TEMP_WRITE_TIME), Time.parse().getFormatTime());
		// 替换步骤与预期
		content = content.replaceAll(String.format(REPLACE_WORD, TEMP_CASE_TOTAL_STEP), stepText.toString());
		content = content.replaceAll(String.format(REPLACE_WORD, TEMP_CASE_TOTAL_EXCEPT), exceptText.toString());
		// 替换用例步骤脚本
		content = content.replaceAll(String.format(REPLACE_WORD, TEMP_SCRIPT_STEP), operateScript.toString());

		// 替换beforeClass等脚本
		content = content.replaceAll(String.format(REPLACE_WORD, TEMP_SCRIPT_BEFORE_CLASS),
				Optional.ofNullable(caseJson.getJSONObject(GetAutoScript.KEY_BEFORE_CLASS))
						.map(json -> json.getJSONArray(GetAutoScript.KEY_OPERATE_STEP)).map(this::analysisOperate)
						.orElse(""));
		content = content.replaceAll(String.format(REPLACE_WORD, TEMP_SCRIPT_AFTER_CLASS),
				Optional.ofNullable(caseJson.getJSONObject(GetAutoScript.KEY_AFTER_CLASS))
						.map(json -> json.getJSONArray(GetAutoScript.KEY_OPERATE_STEP)).map(this::analysisOperate)
						.orElse(""));
		content = content.replaceAll(String.format(REPLACE_WORD, TEMP_SCRIPT_BEFORE_METHOD),
				Optional.ofNullable(caseJson.getJSONObject(GetAutoScript.KEY_BEFORE_METHOD))
						.map(json -> json.getJSONArray(GetAutoScript.KEY_OPERATE_STEP)).map(this::analysisOperate)
						.orElse(""));
		content = content.replaceAll(String.format(REPLACE_WORD, TEMP_SCRIPT_AFTER_METHOD),
				Optional.ofNullable(caseJson.getJSONObject(GetAutoScript.KEY_AFTER_METHOD))
						.map(json -> json.getJSONArray(GetAutoScript.KEY_OPERATE_STEP)).map(this::analysisOperate)
						.orElse(""));

		// 替换其他方法的脚本
		content = content.replaceAll(String.format(REPLACE_WORD, TEMP_SCRIPT_METHOD),
				getOtherMethodScript(caseJson.getJSONArray(GetAutoScript.KEY_OTHER_METHOD)));

		return content;
	}

	/**
	 * 拼接并返回自定义方法的脚本
	 * 
	 * @param otherMethodListJson 自定义方法集合json
	 * @return 自定义方法的脚本
	 */
	private String getOtherMethodScript(JSONArray otherMethodListJson) {
		// 存储所有的脚本
		StringBuilder script = new StringBuilder();

		// 循环，遍历方法集合json，拼接与方法相关的脚本
		for (int index = 0; index < otherMethodListJson.size(); index++) {
			JSONObject methodJson = otherMethodListJson.getJSONObject(index);

			// 拼接方法脚本，限定脚本的格式
			StringJoiner methodScript = new StringJoiner("", "\tpublic void %s() {" + REPLACE_OPERATE_LINE_SIGN,
					"\t}\r\n\r\n");
			methodScript.add(analysisOperate(methodJson.getJSONArray(GetAutoScript.KEY_OPERATE_STEP)));

			script.append(String.format(methodScript.toString(), methodJson.getString(GetAutoScript.KEY_NAME)));
		}

		return script.toString();
	}

	/**
	 * 用于读取步骤模板文件，并返回文件内容
	 * 
	 * @return 文件内容
	 */
	private String readStepTemplet(int index, String step, String except, String operateScrtpt) {
		// 读取并存储用例框架模板中的内容
		String stepTempletScript = readScriptTempleatFile(new File(templetFileFolder, STEP_TERMPLET_NAME), "\r\n\t",
				"\t", "");

		String content = stepTempletScript.toString();
		// 替换步骤数
		content = content.replaceAll(String.format(REPLACE_WORD, TEMP_SCRIPT_STEP_INDEX), String.valueOf(index));
		// 替换步骤与预期
		content = content.replaceAll(String.format(REPLACE_WORD, TEMP_CASE_SIGN_STEP), step);
		content = content.replaceAll(String.format(REPLACE_WORD, TEMP_CASE_SIGN_EXCEPT), except);
		// 替换操作脚本
		content = content.replaceAll(String.format(REPLACE_WORD, TEMP_SCRIPT_STEP_OPERATE), operateScrtpt.toString());

		return content;
	}

	/**
	 * 用于分析操作json，将其解析为脚本，并返回
	 * 
	 * @param operateJson 操作json
	 * @return 操作脚本
	 */
	private ArrayList<String> analysisOperate(JSONArray operateListJson) {
		// 解析操作
		ArrayList<String> scriptList = new ArrayList<String>();
		for (int operateIndex = 0; operateIndex < operateListJson.size(); operateIndex++) {
			JSONObject operateJson = operateListJson.getJSONObject(operateIndex);

			// 获取操作枚举，并存储操作相关的脚本
			OperateType operateType = OperateType.getOperateType(operateJson.getString(GetAutoScript.KEY_OPERATE));
			String operateScript = "";
			if (operateType.getClassCode() > -1) {
				operateScript = String.format("%s.%s", getClassObject(operateType.getClassCode()),
						operateType.getName());
				// 处理元素相关的脚本
				operateScript = getElementObject(operateJson.getShortValue(GetAutoScript.KEY_ELEMENT_TYPE),
						operateJson.getString(GetAutoScript.KEY_ELEMENT_NAME),
						operateJson.getString(GetAutoScript.KEY_INDEX), operateScript);
				// 处理输入内容
				operateScript = operateScript.replaceAll(String.format(REPLACE_WORD, TEMP_SCRIPT_OPERATE_INPUT),
						getInputText(operateJson.getString(GetAutoScript.KEY_INPUT)));
			} else {
				operateScript = getOtherContent(operateType.getClassCode(),
						operateJson.getString(GetAutoScript.KEY_INPUT));
			}

			scriptList.add(operateScript);
		}

		return scriptList;
	}

	/**
	 * 用于根据操作枚举所属的事件类编码，返回对应的类对象名称
	 * 
	 * @param classCode 事件类编码
	 * @return 事件类对象名称
	 */
	private String getClassObject(short classCode) {
		switch (classCode) {
		case 0:
			return CLASS_CLICK_EVENT;
		case 1:
			return CLASS_INPUT_EVENT;
		case 2:
			return CLASS_WAIT_EVENT;
		case 3:
			return CLASS_ASSERT_EVENT;
		default:
			throw new IncorrectContentException("不支持的类对象编码：" + classCode);
		}
	}

	/**
	 * 用于生成非类对象形式的内容
	 * 
	 * @param code  类型编码
	 * @param input 输入的内容
	 * @return 生成的代码
	 */
	private String getOtherContent(short code, String input) {
		switch (code) {
		case -1:
			return "// TODO " + input + REPLACE_OPERATE_LINE_SIGN;
		default:
			throw new IncorrectContentException("不支持的类型编码：" + code);
		}
	}

	/**
	 * 用于添加元素相关的脚本
	 * 
	 * @param elementTypeCode 元素类型编码
	 * @param elementName     元素名称
	 * @param indexText       下标文本
	 * @param operateScript   操作脚本
	 * @return 加工后包含元素相关的脚本
	 */
	private String getElementObject(short elementTypeCode, String elementName, String indexText, String operateScript) {
		// 根据元素类型编码，查找指定的枚举，并根据枚举，选择相应的脚本生成方法
		switch (ElementType.getElementType(elementTypeCode)) {
		case COMMON_ELEMENT:
			return getCommonElementScript(elementName, indexText, operateScript);
		case DATA_LIST_ELEMENT:
			return getDataListElementScript(elementName, indexText, operateScript);
		case SELECT_DATAS_ELEMENT:
		case SELECT_OPTION_ELEMENT:
			return getSelectElemntScript(elementName, indexText, operateScript);
		default:
			throw new IncorrectContentException("不支持的元素类型编码：" + elementTypeCode);
		}
	}

	/**
	 * 用于返回单一型元素的脚本
	 * 
	 * @param elementName   元素名称
	 * @param indexText     下标文本
	 * @param operateScript 操作脚本
	 * @return 单一元素获取相关的脚本
	 */
	private String getCommonElementScript(String elementName, String indexText, String operateScript) {
		if (!Optional.ofNullable(indexText).filter(text -> !text.isEmpty()).isPresent()) {
			return String.format("%s(%s.getElement(\"%s\")#{输入});", operateScript, CLASS_COMMON_ELEMENT, elementName);
		} else {
			// 由于单一元素获取方法不支持多下标获取，故判断下标文本是否为单一下标，不是则抛出异常
			if (indexText.matches("-?\\d+")) {
				return String.format("%s(%s.getElement(\"%s\", %s)#{输入});", operateScript, CLASS_COMMON_ELEMENT,
						elementName, indexText);
			} else {
				throw new IncorrectContentException("单元素不支持多值获取：" + indexText);
			}
		}
	}

	/**
	 * 用于返回集合型元素的脚本
	 * 
	 * @param elementName   元素名称
	 * @param indexText     下标文本
	 * @param operateScript 操作脚本
	 * @return 集合型元素相关的脚本
	 */
	private String getDataListElementScript(String elementName, String indexText, String operateScript) {
		if (Optional.ofNullable(indexText).filter(text -> !text.isEmpty()).isPresent()) {
			// 按照相关的规则，分隔下标文本
			String[] indexs = indexText.split(GetAutoScript.ELEMENT_INDEX_SPLIT_SIGN);
			// 判断下标是否只包含一个元素
			if (indexs.length == 1) {
				// 若下标只有一个，且为获取所有元素的标志，则按照获取所有元素进行获取
				if (Objects.equals(GetAutoScript.ELEMENT_INDEX_All_SIGN, indexs[0])) {
					return String.format("%s.find(%s).getAllElement().forEach(element -> %s(element#{输入}));",
							CLASS_DATA_LIST_ELEMENT, elementName, operateScript);
				} else {
					return String.format("%s(%s.getElement(\"%s\", %s)#{输入});", operateScript, CLASS_COMMON_ELEMENT,
							elementName, indexText);
				}
			} else {
				String script = String.format("%s.find(%s);", CLASS_DATA_LIST_ELEMENT, elementName);
				// 获取所有的下标，将下标转换为
				for (String index : indexs) {
					index = index.trim();
					// 判断下标是否为数字
					if (index.matches("-?\\d+")) {
						script += String.format("%s(%s.getElement(%s)#{输入});", operateScript, CLASS_DATA_LIST_ELEMENT,
								index);
					} else {
						throw new IncorrectContentException("集合元素不支持的下标：" + index);
					}
				}

				return script;
			}
		} else {
			throw new IncorrectContentException("集合元素不支持无下标获取元素（下标为空）");
		}
	}

	/**
	 * 用于返回下拉型元素的脚本
	 * 
	 * @param elementName   元素名称
	 * @param indexText     下标文本
	 * @param operateScript 操作脚本
	 * @return 下拉型元素相关的脚本
	 */
	private String getSelectElemntScript(String elementName, String indexText, String operateScript) {
		//TODO 想办法单一一行写完
		if (Optional.ofNullable(indexText).filter(text -> !text.isEmpty()).isPresent()) {
			String annotation = "// TODO 非标准下拉选项需要使用点击事件打开下拉框";

			StringJoiner script = new StringJoiner(REPLACE_OPERATE_LINE_SIGN, "", REPLACE_OPERATE_LINE_SIGN);
			script.add(annotation);
			script.add(String.format("%s.find(%s);", CLASS_SELECT_ELEMENT, elementName));

			// 按照相关的规则，分隔下标文本
			String[] indexs = indexText.split(GetAutoScript.ELEMENT_INDEX_SPLIT_SIGN);
			// 判断下标是否只包含一个元素
			if (indexs.length == 1) {
				// 若下标只有一个，且为获取所有元素的标志，则按照获取所有元素进行获取
				if (Objects.equals(GetAutoScript.ELEMENT_INDEX_All_SIGN, indexs[0])) {
					script.add(String.format("%s.getAllElement().forEach(element -> %s(element#{输入}));",
							CLASS_SELECT_ELEMENT, elementName, operateScript));
				} else {
					script.add(String.format("%s(%s.getElement(%s)#{输入});", operateScript, CLASS_SELECT_ELEMENT,
							indexText));
				}
			} else {
				// 获取所有的下标，将下标转换为
				for (String index : indexs) {
					// 判断下标是否为数字，根据传参不同，其脚本将存在变化
					if (index.matches("-?\\d+")) {
						script.add(annotation);
						script.add(String.format("%s(%s.getElement(%s)#{输入});", operateScript, CLASS_SELECT_ELEMENT,
								index));
					} else {
						script.add(annotation);
						script.add(String.format("%s(%s.getElement(\"%s\")#{输入});", operateScript, CLASS_SELECT_ELEMENT,
								index));
					}
				}
			}

			return script.toString();
		} else {
			throw new IncorrectContentException("下拉型元素不支持无下标获取（下标为空）");
		}
	}

	/**
	 * 用于生成输入相关的脚本内容
	 * 
	 * @param inputText 输入内容
	 * @return 输入相关的脚本内容
	 */
	private String getInputText(String inputText) {
		if (Optional.ofNullable(inputText).filter(text -> !text.isEmpty()).isPresent()) {
			StringJoiner script = new StringJoiner(", ");
			Arrays.stream(inputText.trim().split(GetAutoScript.ELEMENT_INDEX_SPLIT_SIGN))
					.filter(text -> !text.isEmpty()).map(text -> "\"" + text + "\"").forEach(script::add);

			return ", " + script.toString();
		}

		return "";
	}
}
