package pres.auxiliary.selenium.event.inter;

/**
 * <p>
 * <b>文件名：</b>DataListEventInter.java
 * </p>
 * <p>
 * <b>用途：</b>该接口定义了列表元素需要使用的基本方法
 * </p>
 * <p>
 * <b>编码时间：</b>2019年10月8日下午6:50:43
 * </p>
 * <p>
 * <b>修改时间：</b>2019年10月8日下午6:50:43
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public interface DataListEventInter extends ListElementClickInter, ListElementDoubleClickInter,
		ListElementRightClickInter, ListElementGetAttributeValueInter, ListElementGetTextInter,
		ListElementJudgeKeyInter, ListElementJudgeTextInter, ListElementClearInter, ListElementInputInter {

}
