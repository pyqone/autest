package com.auxiliary.testcase.templet;

import org.dom4j.Element;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auxiliary.testcase.script.GetAutoScript;

public abstract class AbstractAdapter {
	/**
	 * 用于存储用例演化的操作Json
	 */
	JSONObject caseJson = new JSONObject();
	
	/**
	 * 初始化操作json
	 */
	public AbstractAdapter() {
		// 初始化元素集合
		caseJson.put(GetAutoScript.KEY_ELEMENT, new JSONArray());
		// 初始化脚本集合
		caseJson.put(GetAutoScript.KEY_CASE, new JSONArray());
	}
	
	/**
	 * 
	 * @param elementName
	 * @param operateElement
	 */
	public void addCondition(String elementName, Element operateElement) { 
		
	}
	
	public void addElement(String elementName) {
		
	}
	
	public String getScriptJson() {
		return "";
	}
	
	protected abstract JSONObject analysisCondition(JSONObject conditionJson);
	
	protected abstract JSONObject createBeforeOperate();
	
	protected abstract JSONObject createAfterOperate();
}
