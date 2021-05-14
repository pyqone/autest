package com.auxiliary.tool.file;

import java.io.File;
import java.util.Optional;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;

/**
 * <p>
 * <b>文件名：</b>FileTemplet.java
 * </p>
 * <p>
 * <b>用途：</b> 指定创建的模板文件时文件拥有的属性。根据不同的创建文件方式，模板的属性可能有所不同，
 * 需要根据相关的文件写入类来决定使用的属性
 * </p>
 * <p>
 * <b>编码时间：</b>2021年5月10日上午8:26:10
 * </p>
 * <p>
 * <b>修改时间：</b>2021年5月10日上午8:26:10
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public class FileTemplet {
	/**
	 * 存储模板json串
	 */
	JSONObject templetJson = new JSONObject();

	/**
	 * 初始化模板文件的保存路径
	 * 
	 * @param saveFile 模板文件保存路径
	 */
	public FileTemplet(File saveFile) {
		addTempletAttribute("save", saveFile.getAbsolutePath());
		addTempletAttribute("field", new JSONObject());
	}

	/**
	 * 用于向模板中添加字段ID信息
	 * <p>
	 * 创建写入文件内容的字段定位信息，在写入文件内容时，将根据该ID内容，查找到相应的字段，进而将相应的内容写入到文件的正确位置中
	 * </p>
	 * 
	 * @param field 字段ID
	 */
	public void addField(String field) {
		//判断文本内容是否为空
		if (!isEmpty(field)) {
			templetJson.getJSONObject("field").put(field, new JSONObject());
		}
	}

	/**
	 * 用于设置模板的属性
	 * <p>
	 * 该属性为模板文件中字段所拥有的属性，用于在文件中的特殊处理。字段可拥有多个属性，需根据实际的文件写入类来确定生效的属性。当未添加字段ID时，则不会写入信息
	 * </p>
	 * 
	 * @param field    字段ID
	 * @param attName  属性名称
	 * @param attValue 属性值
	 */
	public void addFieldAttribute(String field, String attName, String attValue) {
		//判断字段内容是否为空，任何一个内容为空时，则不进行存储
		if (isEmpty(field) || isEmpty(attName) || isEmpty(attValue)) {
			return;
		}
		
		JSONObject fieldJson = templetJson.getJSONObject("field");
		// 判断字段是否已被添加
		if (fieldJson.containsKey(field)) {
			fieldJson.getJSONObject(field).put(attName, attValue);
		}
	}

	/**
	 * 用于添加模板属性
	 * <p>
	 * 该属性为模板中的属性，对于不同的模板文件而言，可能存在不同的处理方式，故可通过添加该属性来执行处理的方式。
	 * </p>
	 * 
	 * @param attName  属性名称
	 * @param attValue 属性值
	 */
	public void addTempletAttribute(String attName, Object attValue) {
		//判断字段内容是否为空，任何一个内容为空时，则不进行存储
		if (isEmpty(attName) || attValue == null) {
			return;
		}
				
		templetJson.put(attName, attValue);
	}
	
	/**
	 * 用于返回创建的模板json串
	 * @return 模板json串
	 */
	public String getTempletJson() {
		return templetJson.toJSONString();
	}
	
	/**
	 * 判断当前字段是否存在
	 * @param field 字段
	 * @return 模板中是否存在该字段
	 */
	public boolean contains(String field) {
		return templetJson.getJSONObject("field").containsKey(field);
	}
	
	/**
	 * 用于返回模板中的字段集合
	 * @return 字段Set集合
	 */
	public Set<String> getFieldList() {
		return templetJson.getJSONObject("field").keySet();
	}
	
	/**
	 * 用于返回模板中的属性集合
	 * @return
	 */
	public Set<String> getTempletAttributeList() {
		return templetJson.keySet();
	}
	
	@Override
	public String toString() {
		return getTempletJson();
	}
	
	/**
	 * 判断文本内容是否为空
	 * @param text 文本内容
	 * @return 文本内容是否为空
	 */
	private boolean isEmpty(String text) {
		return !Optional.ofNullable(text).filter(n -> !n.isEmpty()).isPresent();
	}
}
