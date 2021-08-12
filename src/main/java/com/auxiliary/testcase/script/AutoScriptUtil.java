package com.auxiliary.testcase.script;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * <p>
 * <b>文件名：</b>TestNGAutoScriptUtil.java
 * </p>
 * <p>
 * <b>用途：</b> 用于生成TestNG脚本json相关的工具
 * </p>
 * <p>
 * <b>编码时间：</b>2021年8月6日下午5:57:35
 * </p>
 * <p>
 * <b>修改时间：</b>2021年8月6日下午5:57:35
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public class AutoScriptUtil {
	/**
	 * 用于生成特殊方法的初始化json内容
	 * 
	 * @param methodType 方法枚举
	 * @return 特殊方法的初始化json内容
	 */
	public static JSONObject getMethodInitJson() {
		JSONObject json = new JSONObject();

		// 遍历所有的枚举，初始化所有的方法json
		for (MethodType methodType : MethodType.values()) {
			JSONObject methodJson = new JSONObject();
			methodJson.put(GetAutoScript.KEY_OPERATE_STEP, new JSONArray());

			json.put(methodType.getName(), methodJson);
		}

		return json;
	}

	/**
	 * 用于生成其他方法的初始化json内容
	 * 
	 * @param methodNames 方法名称
	 * @return 其他方法的初始化json内容
	 */
	public static JSONObject getOtherInitJson(String... methodNames) {
		// 解析所有的方法名称，初始化相应的json
		JSONArray methodListJson = new JSONArray();
		Arrays.stream(Optional.ofNullable(methodNames).filter(arr -> arr.length != 0).orElse(new String[] {}))
				.forEach(name -> {
					JSONObject methodJson = new JSONObject();
					methodJson.put(GetAutoScript.KEY_NAME, name);
					methodJson.put(GetAutoScript.KEY_OPERATE_STEP, new JSONArray());
				});

		JSONObject json = new JSONObject();
		json.put(GetAutoScript.KEY_OTHER_METHOD, methodListJson);

		return json;
	}

	/**
	 * 用于根据传入的需要添加操作的方法名称和操作json，向传入的脚本json中对应的方法添加操作，并返回添加操作后的脚本json
	 * <p>
	 * <b>注意：</b>传入的方法名可以特殊方法（{@link MethodType}枚举的方法），也可以是普通方法，传入方法明后
	 * 由程序判断。若无法找到指定的方法名称，则不添加操作json
	 * </p>
	 * @param methodName 方法名称
	 * @param scriptJsonText 脚本json文本
	 * @param operateJsonText 操作json文本
	 * @return 添加操作后的脚本json类对象
	 */
	public static JSONObject addOperateJson(String methodName, String scriptJsonText, String operateJsonText) {
		// 转译脚本json
		JSONObject scriptJson = JSONObject.parseObject(scriptJsonText);

		// 通过方法名称，将其转换为MethodType
		MethodType methodType = MethodType.getType(methodName);
		// 若能转换，则表示在特定的方法中添加操作；若无法添加，则表示在普通方法中进行操作
		if (methodType != null) {
			scriptJson.getJSONObject(methodType.getName()).getJSONArray(GetAutoScript.KEY_OPERATE_STEP)
					.add(JSONObject.parse(operateJsonText));
		} else {
			// 获取所有的其他方法
			JSONArray methodListJson = scriptJson.getJSONArray(GetAutoScript.KEY_OTHER_METHOD);
			// 遍历，查询到指定名称的方法。若无法找到指定的方法，则不添加操作json
			for (int index = 0; index < methodListJson.size(); index++) {
				JSONObject methodJson = methodListJson.getJSONObject(index);
				if (Objects.equals(methodJson.getString(GetAutoScript.KEY_NAME), methodName)) {
					methodJson.getJSONArray(GetAutoScript.KEY_OPERATE_STEP).add(JSONObject.parse(operateJsonText));
				}
			}
		}
		
		return scriptJson;
	}

	/**
	 * <p>
	 * <b>文件名：</b>TestNGAutoScriptUtil.java
	 * </p>
	 * <p>
	 * <b>用途：</b> 指定脚本中能生成的特定方法类型
	 * </p>
	 * <p>
	 * <b>编码时间：</b>2021年8月6日下午5:04:37
	 * </p>
	 * <p>
	 * <b>修改时间：</b>2021年8月6日下午5:04:37
	 * </p>
	 * 
	 * @author 彭宇琦
	 * @version Ver1.0
	 * @since JDK 1.8
	 */
	public static enum MethodType {
		/**
		 * 方法前置操作
		 */
		BEFORE_METHOD("beforeMethod"),
		/**
		 * 方法后置操作
		 */
		AFTER_METHOD("afterMethod"),
		/**
		 * 类前置操作
		 */
		BEFORE_CLASS("beforeClass"),
		/**
		 * 类后置操作
		 */
		AFTER_CLASS("afterClass");

		/**
		 * 方法名称
		 */
		private String name;

		private MethodType(String name) {
			this.name = name;
		}

		/**
		 * 用于返回方法名称
		 * 
		 * @return 方法名称
		 */
		public String getName() {
			return name;
		}

		/**
		 * 用于返回特定方法枚举
		 * 
		 * @param typeName 枚举名称
		 * @return 相应的枚举对象
		 */
		public static MethodType getType(String typeName) {
			for (MethodType type : values()) {
				if (Objects.equals(typeName, type.name)) {
					return type;
				}
			}

			return null;
		}
	}
}
