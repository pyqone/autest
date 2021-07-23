package com.auxiliary.testcase.script;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auxiliary.selenium.brower.ChromeBrower;
import com.auxiliary.selenium.element.ElementType;
import com.auxiliary.selenium.element.FindDataListElement;
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

	/**
	 * 用于拼接待替换的词语
	 */
	private final String REPLACE_WORD = "\\#\\{%s\\}";
	/**
	 * 用于替换类说明中的步骤与预期的格式
	 */
	private final String REPLACE_STEP_WORD = " * <li>%s</li>";
	/**
	 * 用于替换的类名
	 */
	private final String REPLACE_CLASS_NAME = "Test_01";

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

	public TestNGAutoScript() {
		super();
		templetFileFolder = new File(InformationCase.class.getClassLoader()
				.getResource("ConfigurationFiles/CaseConfigurationFile/CaseAutoScriptTemplet/TestNGTemplet/")
				.getFile());
	}

	@Override
	public void writeScriptFile(File saveFile) {
		saveCaseFrameTemplet(saveFile);
	}

	@Override
	public void writeElementFile(File saveFile, ByType... byTypes) {
		// TODO Auto-generated method stub
	}

	/**
	 * 用于替换并存储测试用例脚本文件
	 * 
	 * @param saveFile 脚本存储路径
	 */
	private void saveCaseFrameTemplet(File saveFile) {
		// 读取并存储用例框架模板中的内容
		StringJoiner caseTempletScript = new StringJoiner("\r\n");
		try (BufferedReader br = new BufferedReader(
				new FileReader(new File(templetFileFolder, CASE_FRAME_TERMPLET_NAME)))) {
			String text = "";
			while ((text = br.readLine()) != null) {
				caseTempletScript.add(text);
			}
		} catch (IOException e) {
			throw new IncorrectContentException(
					"模板文件读取异常：" + new File(templetFileFolder, CASE_FRAME_TERMPLET_NAME).getAbsolutePath());
		}

		// 读取用例json，获取case中的所有内容
		JSONArray caseListJson = caseJson.getJSONArray(GetAutoScript.KEY_CASE);
		for (int caseIndex = 0; caseIndex < caseListJson.size(); caseIndex++) {
			// 记录测试用例步骤以及预期
			StringJoiner stepText = new StringJoiner("\r\n");
			StringJoiner exceptText = new StringJoiner("\r\n");
			StringJoiner operateScript = new StringJoiner("\r\n\r\n");

			// 转换文本，由于一个json中包含多条case，故需要为每一个case附带一份模板
			String content = caseTempletScript.toString();

			// 获取用例json
			JSONObject caseJson = caseListJson.getJSONObject(caseIndex);

			// 获取操作步骤信息
			JSONArray stepListJson = caseJson.getJSONArray(GetAutoScript.KEY_STEP);
			for (int stepIndex = 0; stepIndex < stepListJson.size(); stepIndex++) {
				// 获取用例json
				JSONObject stepJson = stepListJson.getJSONObject(stepIndex);

				// 获取步骤与预期的文本
				JSONObject recordJson = stepJson.getJSONObject(GetAutoScript.KEY_RECORD);
				stepText.add(String.format(REPLACE_STEP_WORD, recordJson.getString(GetAutoScript.KEY_CASE_STEP_TEXT)));
				exceptText.add(
						String.format(REPLACE_STEP_WORD, recordJson.getString(GetAutoScript.KEY_CASE_EXCEPT_TEXT)));

				// 存储步骤脚本
				operateScript.add(readStepTemplet(stepIndex + 1, recordJson.getString(GetAutoScript.KEY_CASE_STEP_TEXT),
						recordJson.getString(GetAutoScript.KEY_CASE_EXCEPT_TEXT),
						stepJson.getJSONArray(GetAutoScript.KEY_OPERATE_STEP)));
			}

			// 替换标题
			content = content.replaceAll(String.format(REPLACE_WORD, TEMP_CASE_TITLE),
					caseJson.getString(GetAutoScript.KEY_CASE_TITLE_TEXT));
			// 替换编码时间
			content = content.replaceAll(String.format(REPLACE_WORD, TEMP_WRITE_TIME), Time.parse().getFormatTime());
			// 替换步骤与预期
			content = content.replaceAll(String.format(REPLACE_WORD, TEMP_CASE_TOTAL_STEP), stepText.toString());
			content = content.replaceAll(String.format(REPLACE_WORD, TEMP_CASE_TOTAL_EXCEPT), exceptText.toString());
			// 替换脚本类编号
			content = content.replaceAll(String.format(REPLACE_WORD, TEMP_CLASS_INDEX), String.valueOf(caseIndex + 1));
			// 替换用例步骤脚本
			content = content.replaceAll(String.format(REPLACE_WORD, TEMP_SCRIPT_STEP), operateScript.toString());

			System.out.println(content);
		}
	}

	/**
	 * 用于读取步骤模板文件，并返回文件内容
	 * 
	 * @return 文件内容
	 */
	private String readStepTemplet(int index, String step, String except, JSONArray operateListJson) {
		// 读取并存储用例框架模板中的内容
		StringJoiner stepTempletScript = new StringJoiner("\r\n\t", "\t", "");
		try (BufferedReader br = new BufferedReader(new FileReader(new File(templetFileFolder, STEP_TERMPLET_NAME)))) {
			String text = "";
			while ((text = br.readLine()) != null) {
				stepTempletScript.add(text);
			}
		} catch (IOException e) {
			throw new IncorrectContentException(
					"模板文件读取异常：" + new File(templetFileFolder, STEP_TERMPLET_NAME).getAbsolutePath());
		}

		// TODO 解析操作

		String content = stepTempletScript.toString();
		// 替换步骤数
		content = content.replaceAll(String.format(REPLACE_WORD, TEMP_SCRIPT_STEP_INDEX), String.valueOf(index));
		// 替换步骤与预期
		content = content.replaceAll(String.format(REPLACE_WORD, TEMP_CASE_SIGN_STEP), step);
		content = content.replaceAll(String.format(REPLACE_WORD, TEMP_CASE_SIGN_EXCEPT), except);

		return content;
	}

	/**
	 * 用于分析操作json，将其解析为脚本，并返回
	 * 
	 * @param operateJson 操作json
	 * @return 操作脚本
	 */
	private String analysisOperate(JSONObject operateJson) {
		// 获取操作枚举，并存储操作相关的脚本
		OperateType operateType = OperateType.getOperateType(operateJson.getString(GetAutoScript.KEY_OPERATE));
		String operateScript = String.format("%s.%s", getClassObject(operateType.getClassCode()),
				operateType.getName());

		// 获取元素枚举，并存储元素相关的脚本
		ElementType elementType = ElementType.getElementType(operateJson.getShortValue(GetAutoScript.KEY_ELEMENT_TYPE));

		// 判断当前返回值是否需要存储
		Optional.ofNullable(operateJson.getString(GetAutoScript.KEY_OUTPUT)).filter(text -> !text.isEmpty())
				.ifPresent(text -> {
					String script = "String %s = String.value(%s);";
					operateScript.append(String.format(script, text));
				});

		return operateScript.toString();
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

	private String getElementObject(short elementTypeCode, String elementName, String indexText, String operateScript) {
		// 切分元素下标文本，并将文本内容转换为整形类型
		List<Integer> indexList = Arrays.stream(indexText.split(REPLACE_MULTIPLE_VALUE_WORD)).map(Integer::valueOf)
				.collect(Collectors.toList());

		// 判断当前下标个数且第一个元素大于0
		if (indexList.size() == 1 && indexList.get(0) > 1) {

		}

		switch (ElementType.getElementType(elementTypeCode)) {
		case COMMON_ELEMENT:
			script = CLASS_COMMON_ELEMENT + ".getElement(\"" + elementName + "\")";
		case DATA_LIST_ELEMENT:
			script = CLASS_DATA_LIST_ELEMENT + ".find()";

		default:
			throw new IncorrectContentException("不支持的元素类型编码：" + elementTypeCode);
		}

		return script;
	}

	/**
	 * 用于返回单一元素的脚本
	 * @param elementName 元素名称
	 * @param indexText 下标文本
	 * @return 单一元素获取相关的脚本
	 */
	private String getCommonElementScript(String elementName, String indexText, String operateScript) {
		// 由于单一元素获取方法不支持多下标获取，故判断下标文本是否为单一下标，不是则抛出异常
		if (indexText.matches("-?\\d+")) {
			return String.format("%s(%s.getElement(\"%s\", %s)#{输入});\r\n", operateScript, CLASS_COMMON_ELEMENT, elementName, indexText);
		} else {
			throw new IncorrectContentException("单元素不支持多值获取：" + indexText);
		}
	}
	
	private String getListElementScript(String elementName, String indexText, String operateScript) {
		FindDataListElement f = new FindDataListElement(new ChromeBrower(new File("")));
		
		// 按照相关的规则，分隔下标文本
		String[] indexs = indexText.split(GetAutoScript.ELEMENT_INDEX_SPLIT_SIGN);
		// 判断下标是否只包含一个元素
		if (indexs.length == 1) {
			// 若下标只有一个，且为获取所有元素的标志，则按照获取所有元素进行获取
			if (Objects.equals(GetAutoScript.ELEMENT_INDEX_All_SIGN, indexs[0])) {
				return String.format("%s.find(%s).getAllElement().forEach(element -> %s(element#{输入}));\r\n", CLASS_DATA_LIST_ELEMENT, elementName, operateScript);
			} else {
				return String.format("%s(%s.getElement(\"%s\", %s)#{输入});\r\n", operateScript, CLASS_COMMON_ELEMENT, elementName, indexText);
			}
		} else {
			// 获取所有的下标，将下标转换为
			for (String index : indexs) {
				String script = String.format("%s.find(%s);\r\n", CLASS_DATA_LIST_ELEMENT, elementName);
				
			}
		}
	}
}
