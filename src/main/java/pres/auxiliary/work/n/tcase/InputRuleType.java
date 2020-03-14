package pres.auxiliary.work.n.tcase;

/**
 * <p><b>文件名：</b>InputRuleType.java</p>
 * <p><b>用途：</b>用于枚举控件中可输入字符类型的限制</p>
 * <p><b>编码时间：</b>2020年3月14日 下午9:14:30</p>
 * <p><b>修改时间：</b>2020年3月14日 下午9:14:30</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 */
public enum InputRuleType {
	/**
	 * 中文
	 */
	CH("中文"), 
	/**
	 * 英文
	 */
	EN("英文"), 
	/**
	 * 数字
	 */
	NUM("数字"),
	/**
	 * 特殊字符
	 */
	SPE("特殊字符"), 
	/**
	 * 小写字母
	 */
	LOW("小写字母"), 
	/**
	 * 大写字母
	 */
	CAP("大写字母");
	
	/**
	 * 枚举名称
	 */
	private String name;
	/**
	 * 初始化枚举名称
	 * @param name 枚举名称
	 */
	private InputRuleType(String name) {
		this.name = name;
	}
	/**
	 * 返回枚举名称
	 * @return 枚举名称
	 */
	public String getName() {
		return name;
	}
}
