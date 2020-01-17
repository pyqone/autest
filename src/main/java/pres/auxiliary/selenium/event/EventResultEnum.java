package pres.auxiliary.selenium.event;

import java.util.regex.Pattern;

/**
 * <p><b>文件名：</b>EventResultEnum.java</p>
 * <p><b>用途：</b>用于枚举事件类的返回值类型，并可以通过类中提供的{@link #getValue()}方法对返回值进行返回</p>
 * <p><b>编码时间：</b>2019年8月31日上午14:20:04</p>
 * <p><b>修改时间：</b>2019年9月2日上午8:41:04</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public enum EventResultEnum {
	/**
	 * 返回值为boolean类型false
	 */
	BOOLEAN_FALSE("false"),
	/**
	 * 返回值为boolean类型true
	 */
	BOOLEAN_TRUE("true"),
	/**
	 * 无返回值类型
	 */
	VOID(""),
	/**
	 * 数字返回值类型
	 */
	NUMBER("0"), 
	/**
	 * 字符串返回值类型
	 */
	STRING("");
	
	/**
	 * 多组返回值时的分隔符
	 */
	public final static String TEXT_SEPARATOR = ",";
	
	/**
	 * 存储枚举的值
	 */
	private String value;
	/**
	 * 用于作为判断传值是否为数字的正则表达式
	 */
	private final String NUMBER_REGEX = "-?\\d+(\\.\\d+)?";
	
	/**
	 * 初始化枚举中的值
	 * @param value 枚举值
	 */
	private EventResultEnum(String value) {
		this.value = value;
	}

	/**
	 * 用于返回枚举中的值
	 * @return 枚举中的值
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * 用于设置枚举中的值，请注意，只能修改STRING和符合正则的NUMBER类型的值，其他的类型或不符合正则的数字，修改无效，依然是原始值
	 * @param value 需要修改的值
	 */
	public EventResultEnum setValue(String value) {
		if (this == EventResultEnum.STRING) {
			this.value = value;
		} else if (this == EventResultEnum.NUMBER) {
			if (Pattern.compile(NUMBER_REGEX).matcher(value).matches()) {
				this.value = value;
			}
		}
		
		return this;
	}
	
	
}
