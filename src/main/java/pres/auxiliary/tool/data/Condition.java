package pres.auxiliary.tool.data;

/**
 * <p><b>文件名：</b>Condition.java</p>
 * <p><b>用途：</b>定义了条件类应包含的方法</p>
 * <p><b>编码时间：</b>2020年1月7日上午8:32:28</p>
 * <p><b>修改时间：</b>2020年1月7日上午8:32:28</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 * @param <T> 继承Condition类的条件类
 */
public abstract class Condition<T extends Condition<?>> {
	/**
	 * 用于判断传入的数据是否满足相应的判断条件
	 * @param <T> 相应的条件类对象
	 * @param data 数据
	 * @param condition 条件类对象 
	 * @return 是否满足相应的判断条件
	 */
	 public abstract boolean judgeData(String data, T condition);
}
