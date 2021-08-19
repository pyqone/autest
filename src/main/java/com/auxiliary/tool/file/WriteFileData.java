package com.auxiliary.tool.file;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * <p><b>文件名：</b>WriteFileData.java</p>
 * <p><b>用途：</b>
 * 存储写入文件中的数据
 * </p>
 * <p><b>编码时间：</b>2021年8月19日下午6:49:44</p>
 * <p><b>修改时间：</b>2021年8月19日下午6:49:44</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public class WriteFileData {
	/**
	 * 存储当前使用的模板
	 */
	private FileTemplet templet;
	/**
	 * 存储当前需要写入文件的内容
	 */
	private JSONObject contentJson = new JSONObject();
	/**
	 * 存储当前需要使用的默认内容
	 */
	private JSONObject defaultCaseJson = new JSONObject();
	/**
	 * 存储当前已写入的内容条数
	 */
	private int nowCaseNum = 0;

	/**
	 * 初始化相关的数据
	 * 
	 * @param fileTemplet 模板文件对象
	 */
	public WriteFileData(FileTemplet fileTemplet) {
		this.templet = fileTemplet;
		contentJson.put(WriteTempletFile.KEY_CASE, new JSONArray());
	}

	/**
	 * 根据传入的文件信息初始化相关的数据
	 * 
	 * @param data 文件写入类数据对象
	 */
	public WriteFileData(String fileTempletJson, String contentJson, String defaultJson) {
		this.templet = new FileTemplet(fileTempletJson);
		this.contentJson = JSONObject.parseObject(contentJson);
		this.defaultCaseJson = JSONObject.parseObject(defaultJson);
	}

	/**
	 * 用于移除当前字段的默认内容
	 * 
	 * @param field 字段id
	 */
	public void removeFieldDefault(String field) {
		// 判断字段是否存在，若不存在，则不进行操作
		if (!templet.containsField(field)) {
			return;
		}

		defaultCaseJson.remove(field);
	}

	/**
	 * 用于根据模板json对当前模板进行设置，调用该方法会覆盖当前的模板
	 * @param 模板json
	 */
	public void setTemplet(String templetJsonText) {
		this.templet = new FileTemplet(templetJsonText);
	}

	/**
	 * 用于根据内容json对当前内容进行设置，调用该方法会覆盖当前的内容
	 * @param 内容json
	 */
	public void setContentJson(String contentJsonText) {
		this.contentJson = JSONObject.parseObject(contentJsonText);
	}

	/**
	 * 用于根据字段默认内容json对当前字段默认内容进行设置，调用该方法会覆盖当前的字段默认内容
	 * @param 字段默认内容json
	 */
	public void setDefaultCaseJson(String defaultCaseJsonText) {
		this.defaultCaseJson = JSONObject.parseObject(defaultCaseJsonText);
	}

	/**
	 * 设置当前内容的行数
	 * @param 当前内容的行数
	 */
	public void setNowCaseNum(int nowCaseNum) {
		this.nowCaseNum = nowCaseNum;
	}

	/**
	 * 用于返回当前模板类对象 
	 * @return 模板类对象
	 */
	public FileTemplet getTemplet() {
		return templet;
	}
	
	/**
	 * 用于返回当前模板类对象json内容
	 * @return 模板类对象json内容
	 */
	public String getTempletJsonText() {
		return templet.getTempletJson();
	}

	/**
	 * 用于返回当前内容的json类对象
	 * @return 当前内容的json类对象
	 */
	public JSONObject getContentJson() {
		return contentJson;
	}
	
	/**
	 * 用于返回当前内容的json类内容
	 * @return 当前内容的json类内容
	 */
	public String getContentJsonText() {
		return contentJson.toJSONString();
	}

	/**
	 * 返回当前字段默认内容json类对象
	 * @return 字段默认内容json类对象
	 */
	public JSONObject getDefaultCaseJson() {
		return defaultCaseJson;
	}
	
	/**
	 * 返回当前字段默认内容json类内容
	 * @return 字段默认内容json类内容
	 */
	public String getDefaultCaseJsonText() {
		return defaultCaseJson.toJSONString();
	}

	/**
	 * 返回当前已写入文件的行数
	 * @return 已写入文件的行数
	 */
	public int getNowCaseNum() {
		return nowCaseNum;
	}
}
