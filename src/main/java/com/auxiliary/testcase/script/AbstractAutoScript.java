package com.auxiliary.testcase.script;

import com.alibaba.fastjson.JSONObject;
import com.auxiliary.testcase.file.IncorrectContentException;
import com.auxiliary.testcase.templet.GetAutoScript;

public class AbstractAutoScript {
	/**
	 * 用于存储用例Json
	 */
	JSONObject caseJson;
	
	/**
	 * 根据用例json构造类对象
	 * @param caseJsonText 用例json文本
	 */
	public AbstractAutoScript(String caseJsonText) {
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
	}

}
