package com.auxiliary.testcase.templet;

import java.util.ArrayList;
import java.util.Optional;

import org.dom4j.Element;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auxiliary.testcase.script.AutoScriptUtil;
import com.auxiliary.testcase.script.GetAutoScript;

public abstract class AbstractAdapter {
	/**
	 * 用于控制传入非指定类型的内容（类型只适配于枚举形式的类型）
	 */
	protected final String INPUT_NOT = "^";

	/**
	 * 用于存储用例演化的操作Json
	 */
	protected JSONObject caseJson = new JSONObject();

	/**
	 * 用于存储元素名称
	 */
	protected ArrayList<String> elementNameList = new ArrayList<>();

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
	 * 用于根据传入的操作标签对象，向元素中添加操作
	 * <p>
	 * 若元素名称未被存储，则向元素名称集合中添加元素名称
	 * </p>
	 * 
	 * @param elementName    元素名称
	 * @param operateElement 操作元素对象
	 */
	public void addStep(String elementName, Element operateElement) {
		Optional.ofNullable(operateElement).filter(element -> !element.elements().isEmpty()).ifPresent(element -> {
			// 根据不同适配器，解析相应的操作标签对象，获取其其中的操作信息
			analysisOperateElement(element).stream().filter(oe -> oe != null).forEach(oe -> {
				// 获取解析的内容，并生成操作json
				JSONObject operateJson = new JSONObject();
				operateJson.put(GetAutoScript.KEY_ELEMENT_NAME, oe.getElementName());
				operateJson.put(GetAutoScript.KEY_ELEMENT_TYPE, oe.getElementType());
				operateJson.put(GetAutoScript.KEY_INDEX, oe.getIndex());
				operateJson.put(GetAutoScript.KEY_OPERATE, oe.getOperate());
				operateJson.put(GetAutoScript.KEY_INPUT, oe.getInput());
				
				// TODO 添加步骤操作
			});
		});
	}

	public void addElement(String elementName) {
		Optional.ofNullable(elementName).filter(name -> !name.isEmpty()).filter(name -> !elementNameList.contains(name))
				.ifPresent(elementNameList::add);
	}

	public String getScriptJson() {
		return caseJson.toJSONString();
	}

	protected abstract ArrayList<OperateInfo> analysisOperateElement(Element operateElement);

	protected abstract JSONObject createBeforeOperate();

	protected abstract JSONObject createAfterOperate();

	/**
	 * <p>
	 * <b>文件名：</b>AbstractAdapter.java
	 * </p>
	 * <p>
	 * <b>用途：</b> 存储转换后的元素信息
	 * </p>
	 * <p>
	 * <b>编码时间：</b>2021年8月12日上午7:58:26
	 * </p>
	 * <p>
	 * <b>修改时间：</b>2021年8月12日上午7:58:26
	 * </p>
	 * 
	 * @author 彭宇琦
	 * @version Ver1.0
	 * @since JDK 1.8
	 */
	protected class OperateInfo {
		/**
		 * 元素名称
		 */
		private String elementName;
		/**
		 * 元素下标
		 */
		private String index;
		/**
		 * 元素类型
		 */
		private short elementType;
		/**
		 * 操作名称
		 */
		private String operate;
		/**
		 * 输入内容
		 */
		private String input;

		/**
		 * 初始化操作信息
		 * 
		 * @param elementName 元素名称
		 * @param index       元素下标
		 * @param elementType 元素类型
		 * @param operate     操作名称
		 * @param input       输入内容
		 */
		public OperateInfo(String elementName, String index, short elementType, String operate, String input) {
			this.elementName = elementName;
			this.index = index;
			this.elementType = elementType;
			this.operate = operate;
			this.input = input;
		}

		/**
		 * 用于返回元素名称
		 * 
		 * @return 元素名称
		 */
		public String getElementName() {
			return elementName;
		}

		/**
		 * 用于返回元素下标
		 * 
		 * @return 元素下标
		 */
		public String getIndex() {
			return index;
		}

		/**
		 * 用于返回元素类型
		 * 
		 * @return 元素类型
		 */
		public short getElementType() {
			return elementType;
		}

		/**
		 * 用于返回操作名称
		 * 
		 * @return 操作名称
		 */
		public String getOperate() {
			return operate;
		}

		/**
		 * 用于返回输入内容
		 * 
		 * @return 输入内容
		 */
		public String getInput() {
			return input;
		}
	}
}
