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
	CLICK("click", (short) 0, (short) 0),
	/**
	 * 双击
	 */
	DOUBLE_CLICK("doubleClick", (short) 0, (short) 1),
	/**
	 * 右击
	 */
	RIGHT_CLICK("rightClick", (short) 0, (short) 2),
	/**
	 * 连续点击
	 */
	CONTINUOUS_CLICK("continuousClick", (short) 0, (short) 3),
	/**
	 * 清除文本
	 */
	CLEAR("clear", (short) 1, (short) 4),
	/**
	 * 获取元素指定属性文本
	 */
	GET_ATTRIBUTE_VALUE("getAttributeValue", (short) 1, (short) 5),
	/**
	 * 获取元素文本
	 */
	GET_TEXT("getText", (short) 1, (short) 6),
	/**
	 * 在元素中输入内容
	 */
	INPUT("input", (short) 1, (short) 7),
	/**
	 * 发送按键
	 */
	KEY_TO_SEND("keyToSend", (short) 1, (short) 8),
	/**
	 * 获取图片中的文本
	 */
	GET_IMAGE_TEXT("getImageText", (short) 1, (short) 9),
	/**
	 * 上传文件
	 */
	UPDATA_FILE("updataFile", (short) 1, (short) 10),
	/**
	 * 等待元素消失
	 */
	DISAPPEAR("disappear", (short) 2, (short) 11),
	/**
	 * 等待元素出现
	 */
	APPEAR("appear", (short) 2, (short) 12),
	/**
	 * 等待元素出现指定文本
	 */
	SHOW_TEXT("showText", (short) 2, (short) 13),
	/**
	 * 断言元素文本包含指定内容
	 */
	ASSERT_TEXT_CONTAIN_KEY("assertTextContainKey", (short) 3, (short) 14),
	/**
	 * 断言元素文本不包含指定文本
	 */
	ASSERT_TEXT_NOT_CONTAIN_KEY("assertTextNotContainKey", (short) 3, (short) 15),
	/**
	 * 断言元素属性文本包含指定文本
	 */
	ASSERT_ATTRIBUTE_CONTAIN_KEY("assertAttributeContainKey", (short) 3, (short) 16),
	/**
	 * 断言元素属性文本不包含指定文本
	 */
	ASSERT_ATTRIBUTE_NOT_CONTAIN_KEY("assertAttributeNotContainKey", (short) 3, (short) 17),
	/**
	 * 断言元素文本与指定文本相同
	 */
	ASSERT_EQUALS_TEXT("assertEqualsText", (short) 3, (short) 18),
	/**
	 * 断言元素文本与指定文本不相同
	 */
	ASSERT_NOT_EQUALS_TEXT("assertNotEqualsText", (short) 3, (short) 19),
	/**
	 * 断言元素文本与指定元素文本相同
	 */
	ASSERT_EQUALS_ELEMENT_TEXT("assertEqualsElementText", (short) 3, (short) 20),
	/**
	 * 断言元素文本与指定元素文本不相同
	 */
	ASSERT_NOT_EQUALS_ELEMENT_TEXT("assertNotEqualsElementText", (short) 3, (short) 21),
	/**
	 * 断言元素存在
	 */
	ASSERT_EXIST_ELEMENT("assertExistElement", (short) 3, (short) 22),
	/**
	 * 断言元素不存在
	 */
	ASSERT_NOT_EXIST_ELEMENT("assertNotExistElement", (short) 3, (short) 23),
	/**
	 * 断言两元素的数字文本比较
	 */
	ASSERT_ELEMENTS_NUMBER("assertElementsNumber", (short) 3, (short) 24),
	/**
	 * 断言元素文本与指定数字比较
	 */
	ASSERT_NUMBER("assertNumber", (short) 3, (short) 25);

	/**
	 * 记录事件名称
	 */
	String name;
	/**
	 * 记录事件所属类编码
	 */
	short classCode;
	/**
	 * 记录事件编号
	 */
	short code;

	/**
	 * 初始化枚举
	 * 
	 * @param name 枚举名称
	 * @param classCode 所属类编码
	 * @param code 枚举编号
	 */
	private OperateType(String name, short classCode, short code) {
		this.name = name;
		this.classCode = classCode;
		this.code = code;
	}

	/**
	 * 返回枚举的名称
	 * @return 返回枚举的名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 返回枚举编号
	 * @return 返回枚举编号
	 */
	public short getCode() {
		return code;
	}
	
	/**
	 * 返回枚举的所属类的编码
	 * <p>
	 * <ul>
	 * <li>点击事件类（{@link ClickEvent}）编码：0</li>
	 * <li>文本事件类（{@link TextEvent}）编码：1</li>
	 * <li>等待事件类（{@link WaitEvent}）编码：2</li>
	 * <li>断言事件类（{@link AssertEvent}）编码：3</li>
	 * </ul>
	 * </p>
	 * @return 枚举的所属类的编码
	 */
	public short getClassCode() {
		return classCode;
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
	public static OperateType getOperateType(String name) {
		for (OperateType type : values()) {
			if (Objects.equals(type.name, name)) {
				return type;
			}
		}

		return null;
	}
}
