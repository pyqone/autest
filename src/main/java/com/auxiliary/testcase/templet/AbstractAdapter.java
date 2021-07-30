package com.auxiliary.testcase.templet;

import com.alibaba.fastjson.JSONObject;

public abstract class AbstractAdapter {
	/**
	 * @param elementName
	 * @param condition
	 * @param conditionValue
	 */
	public void addCondition(String elementName, String condition, String conditionValue) { 
		
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
