package com.auxiliary.selenium.element;

/**
 * <p><b>文件名：</b>EelementType.java</p>
 * <p><b>用途：</b>
 * 枚举元素的类型
 * </p>
 * <p><b>编码时间：</b>2020年5月22日上午7:57:32</p>
 * <p><b>修改时间：</b>2020年5月22日上午7:57:32</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 *
 */
public enum ElementType {
	/**
	 * 指向普通类型元素
	 */
	COMMON_ELEMENT((short) 0), 
	/**
	 * 指向数据列表类型元素
	 */
	DATA_LIST_ELEMENT((short) 1), 
	/**
	 * 指向标准下拉框选择类型元素
	 */
	SELECT_OPTION_ELEMENT((short) 2), 
	/**
	 * 指向列表型下拉框选择类型元素
	 */
	SELECT_DATAS_ELEMENT((short) 3), 
	/**
	 * 指向窗体型元素
	 */
	IFRAME_ELEMENT((short) 4)
	;
	
	/**
	 * 枚举编码
	 */
	short code;

	private ElementType(short code) {
		this.code = code;
	}

	/**
	 * 返回枚举的编码
	 * @return 枚举的编码
	 */
	public short getValue() {
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
	public static ElementType getElementType(short code) {
		for (ElementType type : values()) {
			if (type.code == code) {
				return type;
			}
		}

		return null;
	}
}
