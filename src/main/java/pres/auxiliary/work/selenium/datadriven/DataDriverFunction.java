package pres.auxiliary.work.selenium.datadriven;

/**
 * <p><b>文件名：</b>DataDriverFunction.java</p>
 * <p><b>用途：</b>
 * 用于存储数据驱动中处理数据的公式
 * </p>
 * <p><b>编码时间：</b>2020年6月12日上午8:27:04</p>
 * <p><b>修改时间：</b>2020年6月12日上午8:27:04</p>
 * @author 彭宇琦
 * @version Ver1.0
 *
 */
public class DataDriverFunction {
	/**
	 * 用于存储正则表达式
	 */
	private String regex;
	
	/**
	 * 用于存储处理数据的
	 */
	private DataFunction function;
	
	/**
	 * 初始化数据
	 * @param regex 正则表达式
	 * @param function 数据对应的处理方法
	 */
	public DataDriverFunction(String regex, DataFunction function) {
		this.regex = regex;
		this.function = function;
	}

	/**
	 * 返回正则表达式
	 * @return 正则表达式
	 */
	public String getRegex() {
		return regex;
	}

	/**
	 * 返回数据处理的方法
	 * @return 数据处理的方法
	 */
	public DataFunction getFunction() {
		return function;
	}
}
