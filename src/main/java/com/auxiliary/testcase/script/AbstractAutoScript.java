package com.auxiliary.testcase.script;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auxiliary.testcase.file.IncorrectContentException;
import com.auxiliary.testcase.templet.GetAutoScript;

public class AbstractAutoScript {
	/**
	 * 用于存储用例Json
	 */
	JSONObject caseJson;
	
	public AbstractAutoScript() {
		// 初始化json
		caseJson = new JSONObject();
		caseJson.put(GetAutoScript.KEY_ELEMENT, new JSONArray());
		caseJson.put(GetAutoScript.KEY_CASE, new JSONArray());
	}
	
	/**
	 * 用于添加模板中返回的自动化脚本
	 * @param caseTemplet 模板类对象
	 */
	public void addCase(GetAutoScript caseTemplet) {
		addCase(caseTemplet.getAutoScriptJson());
	}
	
	/**
	 * 用于根据指定样式的用例json文本，存储相应的测试用例
	 * @param caseJsonText 测试用例文本
	 */
	public void addCase(String caseJsonText) {
		// 获取并解析相关的脚本
		JSONObject caseTempletJson = analysisJson(caseJsonText);
		
		// 存储元素信息
		
		JSONArray elementListJson = caseJson.getJSONArray(GetAutoScript.KEY_ELEMENT);
		JSONArray templetElementListJson = caseTempletJson.getJSONArray(GetAutoScript.KEY_ELEMENT);
		for (int i = 0; i < templetElementListJson.size(); i++) {
			// 判断元素是否存在于当前用例中，不存在，则进行存储
			if (!elementListJson.contains(templetElementListJson.getString(i))) {
				elementListJson.add(templetElementListJson.getString(i));
			}
		}
		
		// 存储用例信息
		JSONArray caseListJson = caseJson.getJSONArray(GetAutoScript.KEY_CASE);
		JSONArray templetCaseListJson = caseTempletJson.getJSONArray(GetAutoScript.KEY_CASE);
		for (int i = 0; i < templetCaseListJson.size(); i++) {
			caseListJson.add(templetCaseListJson.getString(i));
		}
	}
	
	/**
	 * 用于判断json文本是否包含指定的字段
	 * <p>
	 * 若json通过判断，则返回相应用例的json对象；若未通过，则抛出异常
	 * </p>
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
}
