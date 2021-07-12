package com.auxiliary.selenium.event;

import java.util.Objects;

/**
 * <p>
 * <b>文件名：</b>OperateType.java
 * </p>
 * <p>
 * <b>用途：</b> 枚举元素可进行的操作
 * </p>
 * <p>
 * <b>编码时间：</b>2021年7月12日下午5:53:12
 * </p>
 * <p>
 * <b>修改时间：</b>2021年7月12日下午5:53:12
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public enum OperateType {
	/**
	 * 点击
	 */
	CLICK("click", (short) 0),
	/**
	 * 双击
	 */
	DOUBLE_CLICK("doubleClick", (short) 1),
	/**
	 * 右击
	 */
	RIGHT_CLICK("rightClick", (short) 2),
	/**
	 * 连续点击
	 */
	CONTINUOUS_CLICK("continuousClick", (short) 3),
	/**
	 * 清除文本
	 */
	CLEAR("clear", (short) 4),
	/**
	 * 获取元素指定属性文本
	 */
	GET_ATTRIBUTE_VALUE("getAttributeValue", (short) 5),
	/**
	 * 获取元素文本
	 */
	GET_TEXT("getText", (short) 6),
	/**
	 * 在元素中输入内容
	 */
	INPUT("input", (short) 7),
	/**
	 * 发送按键
	 */
	KEY_TO_SEND("keyToSend", (short) 8),
	/**
	 * 获取图片中的文本
	 */
	GET_IMAGE_TEXT("getImageText", (short) 9),
	/**
	 * 上传文件
	 */
	UPDATA_FILE("updataFile", (short) 10),
	/**
	 * 等待元素消失
	 */
	DISAPPEAR("disappear", (short) 11),
	/**
	 * 等待元素出现
	 */
	APPEAR("appear", (short) 12),
	/**
	 * 等待元素出现指定文本
	 */
	SHOW_TEXT("showText", (short) 13),
	/**
	 * 断言元素文本包含指定内容
	 */
	ASSERT_TEXT_CONTAIN_KEY("assertTextContainKey", (short) 14),
	/**
	 * 断言元素文本不包含指定文本
	 */
	ASSERT_TEXT_NOT_CONTAIN_KEY("assertTextNotContainKey", (short) 15),
	/**
	 * 断言元素属性文本包含指定文本
	 */
	ASSERT_ATTRIBUTE_CONTAIN_KEY("assertAttributeContainKey", (short) 16),
	/**
	 * 断言元素属性文本不包含指定文本
	 */
	ASSERT_ATTRIBUTE_NOT_CONTAIN_KEY("assertAttributeNotContainKey", (short) 17),
	/**
	 * 断言元素文本与指定文本相同
	 */
	ASSERT_EQUALS_TEXT("assertEqualsText", (short) 18),
	/**
	 * 断言元素文本与指定文本不相同
	 */
	ASSERT_NOT_EQUALS_TEXT("assertNotEqualsText", (short) 19),
	/**
	 * 断言元素文本与指定元素文本相同
	 */
	ASSERT_EQUALS_ELEMENT_TEXT("assertEqualsElementText", (short) 20),
	/**
	 * 断言元素文本与指定元素文本不相同
	 */
	ASSERT_NOT_EQUALS_ELEMENT_TEXT("assertNotEqualsElementText", (short) 21),
	/**
	 * 断言元素存在
	 */
	ASSERT_EXIST_ELEMENT("assertExistElement", (short) 22),
	/**
	 * 断言元素不存在
	 */
	ASSERT_NOT_EXIST_ELEMENT("assertNotExistElement", (short) 23),
	/**
	 * 断言两元素的数字文本比较
	 */
	ASSERT_ELEMENTS_NUMBER("assertElementsNumber", (short) 24),
	/**
	 * 断言元素文本与指定数字比较
	 */
	ASSERT_NUMBER("assertNumber", (short) 25);

	/**
	 * 记录事件名称
	 */
	String name;
	/**
	 * 记录事件编号
	 */
	short code;

	/**
	 * 初始化枚举
	 * 
	 * @param name 枚举名称
	 * @param code 枚举编号
	 */
	private OperateType(String name, short code) {
		this.name = name;
		this.code = code;
	}

	/**
	 * @return 返回枚举的名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return 返回枚举编号
	 */
	public short getCode() {
		return code;
	}

	/**
	 * 用于根据枚举编号，识别枚举，并进行返回
	 * <p>
	 * 若无法查到与之匹配的编码，则返回null
	 * </p>
	 * 
	 * @param code 枚举编号
	 * @return 枚举值
	 */
	public static OperateType getOperateType(short code) {
		for (OperateType type : values()) {
			if (type.code == code) {
				return type;
			}
		}

		return null;
	}

	/**
	 * 用于根据枚举名称，识别枚举，并进行返回
	 * <p>
	 * 若无法查到与之匹配的名称，则返回null
	 * </p>
	 * 
	 * @param code 枚举名称
	 * @return 枚举值
	 */
	public static OperateType getMarkColorsType(String name) {
		for (OperateType type : values()) {
			if (Objects.equals(type.name, name)) {
				return type;
			}
		}

		return null;
	}
}
