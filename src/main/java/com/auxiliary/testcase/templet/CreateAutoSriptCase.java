package com.auxiliary.testcase.templet;

import java.io.File;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auxiliary.testcase.file.IncorrectFileException;
import com.auxiliary.testcase.script.GetAutoScript;

/**
 * <p><b>文件名：</b>CreateAutoSriptCase.java</p>
 * <p><b>用途：</b>
 * 指定能生成自动化测试脚本的测试用例模板所必须的方法
 * </p>
 * <p><b>编码时间：</b>2021年7月9日上午8:20:53</p>
 * <p><b>修改时间：</b>2021年7月9日上午8:20:53</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public abstract class CreateAutoSriptCase extends Case implements GetAutoScript {
	/**
	 * 用于存储脚本相关的Json
	 */
	protected JSONObject scriptJson = new JSONObject();
	
	/**
	 * 根据用例xml文件来构造用例模板类
	 * 
	 * @param configXmlFile xml配置文件
	 * @throws IncorrectFileException 文件格式或路径不正确时抛出的异常
	 */
	public CreateAutoSriptCase(File configXmlFile) {
		super(configXmlFile);
		
		// 初始化json
		scriptJson.put(KEY_ELEMENT, new JSONArray());
		scriptJson.put(KEY_CASE, new JSONArray());
	}

	@Override
	public String getAutoScriptJson() {
		return scriptJson.toJSONString();
	}

}
