package pres.auxiliary.work.selenium.event.extend;

import java.util.HashMap;
import java.util.Optional;

import pres.auxiliary.work.selenium.brower.AbstractBrower;
import pres.auxiliary.work.selenium.event.AbstractEvent;

/**
 * <p>
 * <b>文件名：</b>GroupEvent.java
 * </p>
 * <p>
 * <b>用途：</b> 提供对单一事件进行组合，形成一个事件组（可看成对多条事件进行提取，形成一个方法），
 * 并根据特定的条件，对相应的事件组进行回放，可根据返回值来获取回放是否成功。
 * </p>
 * <p>
 * <b>编码时间：</b>2020年12月28日上午8:23:55
 * </p>
 * <p>
 * <b>修改时间：</b>2020年12月28日上午8:23:55
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public class GroupEvent extends AbstractEvent {
	/**
	 * 用于存储事件的组合
	 */
	HashMap<String, EvenetAction> eventMap = new HashMap<>();

	/**
	 * 初始化浏览器对象
	 * 
	 * @param brower 浏览器对象
	 */
	public GroupEvent(AbstractBrower brower) {
		super(brower);
	}

	/**
	 * 用于添加无参数的事件组合
	 * 
	 * @param eventName 事件组合名称
	 * @param event     相应的一组操作
	 */
	public void addEvent(String eventName, NoParamGroupEventFunction event) {
		eventMap.put(eventName, new EvenetAction(event, Optional.empty()));
	}
	
	/**
	 * 用于添加有参数的事件组合
	 * @param eventName 事件组合名称
	 * @param event 相应的一组操作
	 * @param arg 需要传入到事件组中的传参
	 */
	public void addEvent(String eventName, HasParamGroupEventFunction event, Object arg) {
		eventMap.put(eventName, new EvenetAction(event, Optional.ofNullable(arg)));
	}
	
	/**
	 * 用于执行事件组合
	 * 
	 * @param eventName 事件名称
	 * @return
	 */
	public Optional<Object> actionEvent(String eventName) {
		return eventMap.get(eventName).run();
	}
	
	/**
	 * <p>
	 * <b>文件名：</b>GroupEvent.java
	 * </p>
	 * <p>
	 * <b>用途：</b> 封装组合事件执行的条件，以便于在后续调用中能带参执行
	 * </p>
	 * <p>
	 * <b>编码时间：</b>2020年12月29日上午8:25:06
	 * </p>
	 * <p>
	 * <b>修改时间：</b>2020年12月29日上午8:25:06
	 * </p>
	 * 
	 * @author 彭宇琦
	 * @version Ver1.0
	 * @since JDK 1.8
	 *
	 */
	public class EvenetAction {
		/**
		 * 事件组执行方法
		 */
		private GroupEventFunction eventFunction;
		/**
		 * 事件组执行参数
		 */
		private Optional<Object> arg;

		/**
		 * 初始化事件组
		 * 
		 * @param eventFunction 事件组执行方法
		 * @param arg           执行 参数
		 */
		public EvenetAction(GroupEventFunction eventFunction, Optional<Object> arg) {
			super();
			this.eventFunction = eventFunction;
			this.arg = arg;
		}

		/**
		 * 用于执行指定的方法，以Object类的封装类返回执行出参，若事件组无出参，则返回{@link Optional#empty()}。
		 * @return 方法执行出参
		 */
		public Optional<Object> run() {
			if (arg.isPresent()) {
				return eventFunction.action(arg);
			} else {
				eventFunction.action();
				return Optional.empty();
			}
		}
	}
}
