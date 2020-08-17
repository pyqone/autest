package pres.auxiliary.tool.regex;

/**
 * <p><b>文件名：</b>RegexType.java</p>
 * <p><b>用途：</b>
 * 枚举常用的正则表达式
 * </p>
 * <p><b>编码时间：</b>2020年8月17日下午5:47:33</p>
 * <p><b>修改时间：</b>2020年8月17日下午5:47:33</p>
 * @author 彭宇琦
 * @version Ver1.0
 *
 */
public enum RegexType {
	URL("(?:(?:(?:[a-z]+:)?\\/\\/)|www\\.)(?:\\S+(?::\\S*)?@)?(?:localhost|(?:(?:25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]\\d|\\d)(?:\\.(?:25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]\\d|\\d)){3})|(?:(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)(?:\\.(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)*(?:\\.(?:[a-z\\u00a1-\\uffff]{2,}))\\.?)(?::\\d{2,5})?(?:[/?#][^\\s\"]*)?"), 
	EMAIL("(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))"),
	FOLDER_PATH("([a-zA-Z]:(\\/|\\\\))?([^/:*?<>\\\"\"|\\\\]+(\\/|\\\\)?)+"),
	;
	
	/**
	 * 存储枚举的正则
	 */
	private String regex;

	/**
	 * 初始化枚举值
	 * @param regex 枚举值
	 */
	private RegexType(String regex) {
		this.regex = regex;
	}

	/**
	 * 返回枚举对应的正则表达式
	 * @return 正则表达式
	 */
	public String getRegex() {
		return regex;
	}
	
	/**
	 * 用于判断字符串是否符合当前枚举的正则表达式
	 * @param content 待判断的字符串
	 * @return 是否符合当前枚举的正则表达式
	 */
	public boolean judgeString(String content) {
		return content.matches(regex);
	}
}	
