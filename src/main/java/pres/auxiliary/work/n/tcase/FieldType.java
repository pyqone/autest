package pres.auxiliary.work.n.tcase;

/**
 * <p><b>文件名：</b>FieidType.java</p>
 * <p><b>用途：</b>枚举测试用例可进行预设的字段，将xml配置文件中单元格的id属性映射到枚举值，
 * 以便于调用预设的方法编写用例。可预设的字段包括：步骤、预期、前置条件、标题、优先级</p>
 * <p><b>编码时间：</b>2020年2月19日下午10:30:10</p>
 * <p><b>修改时间：</b>2020年2月19日下午10:30:10</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public enum FieldType {
	/**
	 * 映射步骤字段
	 */
	STEP(""), 
	/**
	 * 映射预期字段
	 */
	EXPECT(""), 
	/**
	 * 映射前置条件字段
	 */
	PRECONDITION(""), 
	/**
	 * 映射标题字段
	 */
	TITLE(""), 
	/**
	 * 映射优先级字段
	 */
	RANK("");
	
	/**
	 * 控制枚举的字段值
	 */
	private String value;

	/**
	 * 设置枚举的字段值
	 * @param value 枚举字段值
	 */
	private FieldType(String value) {
		this.value = value;
	}

	/**
	 * 用于返回枚举的字段值
	 * @return 枚举的字段值
	 */
	public String getValue() {
		return value;
	}

	/**
	 * 用于设置枚举的字段值
	 * @param value 枚举的字段值
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
}
