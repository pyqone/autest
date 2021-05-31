package com.auxiliary.tool.file;

import java.io.File;
import java.util.Optional;
import java.util.Set;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

/**
 * <p>
 * <b>文件名：</b>FileTemplet.java
 * </p>
 * <p>
 * <b>用途：</b> 指定创建的模板文件时文件拥有的属性。根据不同的创建文件方式，模板的属性可能有所不同， 需要根据相关的文件写入类来决定使用的属性
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
	public static final String KEY_SAVE = "save";
	public static final String KEY_FIELD = "field";
	public static final String KEY_INDEX = "index";

	/**
	 * 存储模板json串
	 */
	protected JSONObject templetJson = new JSONObject();

	/**
	 * 初始化模板文件的保存路径
	 * 
	 * @param saveFile 模板文件保存路径
	 */
	public FileTemplet(File saveFile) {
		templetJson.put(KEY_SAVE, saveFile.getAbsolutePath());
		templetJson.put(KEY_FIELD, new JSONObject());
	}
	
	public FileTemplet(String templetJsonText) {
		// 将传入的json字符串转换成JSONObject类，并判断其是否包含必要字段，若不存在，则抛出异常
		this.templetJson = Optional.ofNullable(templetJsonText).filter(t -> !t.isEmpty()).map(t -> {
			try {
				return JSONObject.parseObject(t);
			} catch (Exception e) {
				return new JSONObject();
			}
		}).filter(json -> json.containsKey(KEY_SAVE) && json.containsKey(KEY_FIELD)).filter(json -> {
			// 判断传入的json中，其field字段是否为json类形式，若非该形式，则也被过滤
			try {
				json.getJSONObject(KEY_FIELD);
				return true;
			} catch (JSONException e) {
				return false;
			}
		}).orElseThrow(() -> new WriteFileException("缺少模板必须字段，无法将文本转换为模板类"));
	}

	/**
	 * 用于向模板中添加字段ID信息
	 * <p>
	 * 创建写入文件内容的字段定位信息，在写入文件内容时，将根据该ID内容，查找到相应的字段，进而将相应的内容写入到文件的正确位置中
	 * </p>
	 * 
	 * @param field 字段ID
	 * @return 类本身
	 */
	public void addField(String field) {
		// 判断文本内容是否为空
		if (!isEmpty(field)) {
			JSONObject fieldJson = templetJson.getJSONObject(KEY_FIELD);
			fieldJson.put(field, new JSONObject());
			addFieldAttribute(field, KEY_INDEX, fieldJson.keySet().size() - 1);
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
	 * 
	 * @return 类本身
	 */
	public void addFieldAttribute(String field, String attName, Object attValue) {
		// 判断字段内容是否为空，任何一个内容为空时，则不进行存储
		if (isEmpty(field) || isEmpty(attName)) {
			return;
		}

		JSONObject fieldJson = templetJson.getJSONObject(KEY_FIELD);
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
	 * 
	 * @return 类本身
	 */
	public void addTempletAttribute(String attName, Object attValue) {
		// 判断字段内容是否为空，任何一个内容为空时，则不进行存储
		if (isEmpty(attName) || attValue == null) {
			return;
		}

		// 判断关键词是否与指定关键词重复
		if (KEY_FIELD.equals(attName) || KEY_SAVE.equals(attName)) {
			throw new WriteFileException(String.format("不能使用%s、%s作为属性名称", KEY_FIELD, KEY_SAVE));
		}

		templetJson.put(attName, attValue);
	}
	
	/**
	 * 用于返回模板的属性
	 * @param attName 
	 * @return
	 */
	public Object getTempletAttribute(String attName) {
		return templetJson.get(attName);
	}
	
	/**
	 * 用于返回创建的模板json串
	 * 
	 * @return 模板json串
	 */
	public String getTempletJson() {
		return templetJson.toJSONString();
	}

	/**
	 * 判断模板中是否存在指定的字段
	 * 
	 * @param field 字段
	 * @return 模板中是否存在指定的字段
	 */
	public boolean containsField(String field) {
		return templetJson.getJSONObject(KEY_FIELD).containsKey(field);
	}
	
	/**
	 * 判断模板中是否存在指定的属性
	 * @param attribute 属性
	 * @return 模板中是否存在指定的属性
	 */
	public boolean containsAttribute(String attribute) {
		return templetJson.containsKey(attribute);
	}

	/**
	 * 用于返回模板中的字段集合
	 * 
	 * @return 字段Set集合
	 */
	public Set<String> getFieldList() {
		return templetJson.getJSONObject(KEY_FIELD).keySet();
	}

	/**
	 * 用于返回模板中的属性集合
	 * 
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
	 * 
	 * @param text 文本内容
	 * @return 文本内容是否为空
	 */
	private boolean isEmpty(String text) {
		return !Optional.ofNullable(text).filter(n -> !n.isEmpty()).isPresent();
	}
}
