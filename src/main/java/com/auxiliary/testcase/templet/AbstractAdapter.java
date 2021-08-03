package com.auxiliary.testcase.templet;

import org.dom4j.Element;

import com.alibaba.fastjson.JSONObject;

public abstract class AbstractAdapter {
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
