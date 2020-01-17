package pres.auxiliary.selenium.xml;

/**
 * <p>
 * <b>文件名：</b>PosMode.java
 * </p>
 * <p>
 * <b>用途：</b>用于枚举出能被识别的元素定位方式
 * </p>
 * <p>
 * <b>编码时间：</b>2019年10月24日下午5:18:57
 * </p>
 * <p>
 * <b>修改时间：</b>2019年10月24日下午5:18:57
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public enum PosMode {
	/** 通过xpath方式进行定位 */
	XPATH("xpath"),
	/** 通过css方式进行定位 */
	CSS("css"),
	/** 通过className方式进行定位 */
	CLASSNAME("classname"),
	/** 通过id方式进行定位 */
	ID("id"),
	/** 通过linkText方式进行定位 */
	LINKTEXT("linktext"),
	/** 通过name方式进行定位 */
	NAME("name"),
	/** 通过tagName方式进行定位 */
	TAGNAME("tagname");

	/**
	 * 定义枚举值
	 */
	private String value;

	private PosMode(String value) {
		this.value = value;
	}

	/**
	 * 返回元素定位枚举对应的定位方式名称
	 * 
	 * @return 元素定位方式名称
	 */
	public String getValue() {
		return value;
	}
	
	public static PosMode getMode(String modeName) {
		switch (modeName) {
		case "xpath":
			return XPATH;
		case "css":
			return CSS;
		case "classname":
			return CLASSNAME;
		case "id":
			return ID;
		case "linktext":
			return LINKTEXT;
		case "name":
			return NAME;
		case "tagname":
			return TAGNAME;
			
		default:
			throw new IllegalArgumentException("Unexpected value: " + modeName);
		}
	}
}
