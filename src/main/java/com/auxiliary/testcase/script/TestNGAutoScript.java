package com.auxiliary.testcase.script;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringJoiner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
	protected final String REPLACE_WORD = "\\#\\{%s\\}";
	/**
	 * 用于替换类说明中的步骤与预期的格式
	 */
	protected final String REPLACE_STEP_WORD = " * <li>%s</li>";
	/**
	 * 用于替换的类名
	 */
	protected final String REPLACE_CLASS_NAME = "Test_01";

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

	protected void saveCaseFrameTemplet(File saveFile) {
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
			ArrayList<String> operateScriptList = new ArrayList<>();

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
			content = content.replaceAll(String.format(REPLACE_WORD, TEMP_CLASS_INDEX), String.valueOf(caseIndex + 1 ));
			
			System.out.println(content);
		}
	}

	/**
	 * 用于读取步骤模板文件，并返回文件内容
	 * 
	 * @return 文件内容
	 */
	private String readStepTemplet() {
		// 读取并存储用例框架模板中的内容
		StringJoiner stepTempletScript = new StringJoiner("\r\n");
		try (BufferedReader br = new BufferedReader(new FileReader(new File(templetFileFolder, STEP_TERMPLET_NAME)))) {
			String text = "";
			while ((text = br.readLine()) != null) {
				stepTempletScript.add(text);
			}
		} catch (IOException e) {
			throw new IncorrectContentException(
					"模板文件读取异常：" + new File(templetFileFolder, STEP_TERMPLET_NAME).getAbsolutePath());
		}

		return stepTempletScript.toString();
	}
}
