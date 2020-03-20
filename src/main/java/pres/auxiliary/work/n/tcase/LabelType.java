package pres.auxiliary.work.n.tcase;

/**
 * <p><b>文件名：</b>LabelType.java</p>
 * <p><b>用途：</b>定义测试用例xml文件中标签的名称枚举，用于定位获取的标签内容</p>
 * <p><b>编码时间：</b>2020年3月12日下午6:49:22</p>
 * <p><b>修改时间：</b>2020年3月12日下午6:49:22</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public enum LabelType {
	/**
	 * 用例标签
	 */
	CASE("case"), 
	/**
	 * 步骤
	 */
	STEP("step"), 
	/**
	 * 预期
	 */
	EXCEPT("except"), 
	/**
	 * 标题
	 */
	TITLE("title"), 
	/**
	 * 前置条件
	 */
	PRECONDITION("precondition"), 
	/**
	 * 优先级
	 */
	RANK("rank"), 
	/**
	 * 关键词
	 */
	KEY("key");
	
	/**
	 * 记录标签的名称
	 */
	private String name;

	/**
	 * 初始化每个枚举的名称
	 * @param name 枚举名称
	 */
	private LabelType(String name) {
		this.name = name;
	}

	/**
	 * 用于返回枚举对应的标签名
	 * @return 标签名
	 */
	public String getName() {
		return name;
	}
}
