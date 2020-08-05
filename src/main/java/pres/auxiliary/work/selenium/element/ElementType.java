package pres.auxiliary.work.selenium.element;

/**
 * <p><b>文件名：</b>EelementType.java</p>
 * <p><b>用途：</b>
 * 用于标记当前传入的元素是以何种方式进行获取
 * </p>
 * <p><b>编码时间：</b>2020年5月22日上午7:57:32</p>
 * <p><b>修改时间：</b>2020年5月22日上午7:57:32</p>
 * @author 
 * @version Ver1.0
 * @since JDK 12
 *
 */
public enum ElementType {
	/**
	 * 指向普通类型元素
	 */
	COMMON_ELEMENT, 
	/**
	 * 指向数据列表类型元素
	 */
	DATA_LIST_ELEMENT, 
	/**
	 * 指向下拉框选择类型元素
	 */
	SELECT_ELEMENT, 
}
