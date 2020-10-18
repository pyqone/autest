package pres.auxiliary.work.selenium.event;

/**
 * <p><b>文件名：</b>EventAction.java</p>
 * <p><b>用途：</b>
 * 用于执行事件增强的方法，方法中可通过{@link EventInformation}类对象，回调事件的操作的步骤
 * 以及传入到事件中的{@link Element_Old}类对象
 * </p>
 * <p><b>编码时间：</b>2020年7月10日下午3:40:13</p>
 * <p><b>修改时间：</b>2020年7月10日下午3:40:13</p>
 * @author 彭宇琦
 * @version Ver1.0
 *
 */
public interface EventAction {
	/**
	 * 事件增强的方法，可通过形参回调事件的操作步骤以及传入到事件中的{@link Element_Old}类对象
	 * @param name {@link EventInformation}类对象
	 */
	void action(EventInformation elemenetInformation);
}
