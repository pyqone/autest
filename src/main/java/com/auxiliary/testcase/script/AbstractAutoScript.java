package com.auxiliary.testcase.script;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;
import java.util.StringJoiner;

import com.alibaba.fastjson.JSONObject;
import com.auxiliary.selenium.location.ByType;
import com.auxiliary.testcase.file.IncorrectContentException;
import com.auxiliary.testcase.templet.GetAutoScript;

/**
 * <p>
 * <b>文件名：</b>AbstractAutoScript.java
 * </p>
 * <p>
 * <b>用途：</b>定义自动化脚本生成类所必须实现的方法
 * </p>
 * <p>
 * <b>编码时间：</b>2021年7月16日 上午10:36:49
 * </p>
 * <p>
 * <b>修改时间：</b>2021年7月26日 上午8:09:49
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public abstract class AbstractAutoScript {
	/**
	 * 用于存储用例Json
	 */
	JSONObject scriptJson;

	/**
	 * 根据{@link GetAutoScript}类对象返回的内容，初始化脚本生成方法
	 * 
	 * @param caseTemplet 用例模板类对象
	 */
	public AbstractAutoScript(GetAutoScript caseTemplet) {
		// 获取并解析相关的脚本
		scriptJson = analysisJson(Optional.ofNullable(caseTemplet).map(GetAutoScript::getAutoScriptJson).orElse(""));
	}

	/**
	 * 用于在指定的文件路径下生成脚本
	 * 
	 * @param saveFile 文件存储路径
	 */
	public abstract void writeScriptFile(File saveFile);

	/**
	 * 用于在指定的文件路径下生成录制元素定位方式文件
	 * 
	 * @param saveFile 文件存储路径
	 * @param byTypes  启动的定位方式标签
	 */
	public abstract void writeElementFile(File saveFile, ByType... byTypes);

	/**
	 * 用于判断json文本是否包含指定的字段
	 * <p>
	 * 若json通过判断，则返回相应用例的json对象；若未通过，则抛出异常
	 * </p>
	 * 
	 * @param caseJsonText json文本
	 * @return 解析后的用例json
	 */
	protected JSONObject analysisJson(String caseJsonText) {
		JSONObject caseJson;

		// 解析json
		try {
			caseJson = JSONObject.parseObject(caseJsonText);
		} catch (RuntimeException e) {
			throw new IncorrectContentException("无法解析的文本：" + caseJsonText);
		}

		// 判断Json是否包含指定的文本内容，在判断指定内容是否指定正确
		if (!caseJson.containsKey(GetAutoScript.KEY_ELEMENT)) {
			throw new IncorrectContentException("json缺少元素关键词：" + GetAutoScript.KEY_ELEMENT);
		} else {
			try {
				caseJson.getJSONArray(GetAutoScript.KEY_ELEMENT);
			} catch (Exception e) {
				throw new IncorrectContentException("元素关键词非Array类型：" + GetAutoScript.KEY_ELEMENT);
			}
		}

		if (!caseJson.containsKey(GetAutoScript.KEY_CASE)) {
			throw new IncorrectContentException("json缺少用例关键词：" + GetAutoScript.KEY_CASE);
		} else {
			try {
				caseJson.getJSONArray(GetAutoScript.KEY_CASE);
			} catch (Exception e) {
				throw new IncorrectContentException("用例关键词非Array类型：" + GetAutoScript.KEY_CASE);
			}
		}

		return caseJson;
	}

	/**
	 * 用于读取模板文件内容，并将内容进行返回
	 * <p>
	 * <b>注意：</b>拼接字符的写法类似于{@link StringJoiner#StringJoiner(CharSequence, CharSequence, CharSequence)}的写法：
	 * <ul>
	 * <li>line对应StringJoiner中的delimiter形参</li>
	 * <li>before对应StringJoiner中的prefix形参</li>
	 * <li>after对应StringJoiner中的suffix形参</li>
	 * </ul>
	 * </p>
	 * 
	 * @param scriptTempleatFile 模板文件对象
	 * @param line               在读取到的每行元素后添加内容
	 * @param before             在读取到的内容前拼接内容
	 * @param after              在读取到的内容后拼接内容
	 * @return 文件内容
	 */
	protected String readScriptTempleatFile(File scriptTempleatFile, String line, String before, String after) {
		// 读取并存储用例框架模板中的内容
		StringJoiner caseTempletScript = new StringJoiner(line, before, after);
		try (BufferedReader br = new BufferedReader(new FileReader(scriptTempleatFile))) {
			String text = "";
			while ((text = br.readLine()) != null) {
				caseTempletScript.add(text);
			}
		} catch (IOException e) {
			throw new IncorrectContentException("模板文件读取异常：" + scriptTempleatFile.getAbsolutePath());
		}

		return caseTempletScript.toString();
	}
}
