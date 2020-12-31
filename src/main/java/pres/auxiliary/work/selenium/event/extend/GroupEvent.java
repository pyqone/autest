package pres.auxiliary.work.selenium.event.extend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

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
public class GroupEvent {
	/**
	 * 用于存储事件的组合
	 */
	HashMap<String, EvenetAction> eventMap = new HashMap<>();

	/**
	 * 用于控制在连续执行事件时是否因为执行失败而中断执行
	 */
	boolean isExceptionBreak = true;

	/**
	 * 设置在连续执行事件时，是否由于某一方法执行失败而终止运行
	 * 
	 * @param isExceptionBreak 失败是否继续执行
	 */
	public void setExceptionBreak(boolean isExceptionBreak) {
		this.isExceptionBreak = isExceptionBreak;
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
	 * 用于添加有参数的事件组合。
	 * <p>
	 * 其中，执行方法中的参数为Object的封装类型，其可获取后强转为所需要的类型进行使用，而形参arg为
	 * 需要传入到执行方法中的参数，无需使用{@link Optional}类进行封装。例如，假设类的对象为test，则调用方法 时可以写为：
	 * <code><pre>
	 * test.addEvent("测试", 1, 
	 * 	text -> Optional.ofNullable("当前参数：" + text.map(t -> (Integer) t).orElse(-1)));
	 * System.out.println(test.actionEvent("测试").orElse("错误"));//输出：当前参数：1
	 * <pre></code>
	 * </p>
	 * 
	 * @param eventName 事件组合名称
	 * @param arg       需要传入到事件组中的传参
	 * @param event     相应的一组操作
	 * @throws ParamException 事件组名称为null或为空串时或事件组执行方法为null时抛出的异常
	 */
	public void addEvent(String eventName, Object arg, HasParamGroupEventFunction event) {
		eventMap.put(
				Optional.ofNullable(eventName).filter(name -> !name.isEmpty())
						.orElseThrow(() -> new ParamException("未定义事件组名称")),
				new EvenetAction(Optional.ofNullable(event).orElseThrow(() -> new ParamException("未定义事件组执行方法")),
						Optional.ofNullable(arg)));
	}

	/**
	 * 用于根据组合名称执行事件组合
	 * 
	 * @param eventName 事件名称
	 * @return 方法执行后的出参封装类
	 * @throws ParamException 事件组名称不存在时抛出的异常
	 */
	public Optional<Object> action(String eventName) {
		return eventMap.get(judgeParam(eventName)).run();
	}

	/**
	 * 用于执行指定的事件组，并根据执行是否成功（是否报错），再执行其相应事件组，并将两者的返回结果进行存储<sup>1</sup>，以集合形式返回
	 * <p>
	 * <b>注意：</b>
	 * <ol>
	 * <li>结果返回值将返回原方法的执行结果与之后执行的结果的组合，以{@link List}的形式存储，
	 * 其第一个元素为原方法返回值，第二个元素为成功或失败方法返回值</li>
	 * <li>若未指定失败或成功方法组名称时，将不执行方法，且不存储执行结果，故结果集中最少存在1个结果，最多存在两个结果</li>
	 * </ol>
	 * </p>
	 * 
	 * @param mustActionEventName    原执行的事件组名称
	 * @param successActionEventName 原事件组执行成功后执行的事件组名称
	 * @param failActionEventName    原事件组执行失败后执行的事件组名称
	 * @return 事件组执行结果集
	 * @throws ParamException 事件组名称不存在时抛出的异常
	 */
	public List<Optional<Object>> ifSuccessOrElseAction(String mustActionEventName, String successActionEventName,
			String failActionEventName) {
		List<Optional<Object>> result = new ArrayList<>();
		// 先进行事件组名称判断，避免因该原因而执行失败事件组
		judgeParam(mustActionEventName);

		// 执行指定的组事件，若事件抛出异常，则执行失败组方法
		try {
			result.add(action(mustActionEventName));
		} catch (Exception e) {
			// 由于事件执行失败，其存储也未生效，故存储一个空封装类
			result.add(Optional.empty());
			// 若当前存在需要失败执行的方法时，则存储失败执行方法的执行结果，否则存储空封装类
			Optional.ofNullable(failActionEventName).filter(name -> !name.isEmpty()).map(this::action)
					.ifPresent(result::add);
			return result;
		}

		// 若事件执行成功，则执行成功的事件
		// 注意：该方法不能放入try中，避免因该方法异常失效而执行catch中失败才执行的方法
		Optional.ofNullable(successActionEventName).filter(name -> !name.isEmpty()).map(this::action)
				.ifPresent(result::add);

		return result;
	}

	/**
	 * 用于执行指定的事件组成功后，再执行另一个指定的事件组，若另一事件组名称为空 或为null时，则不执行方法也不存储结果
	 * 
	 * @param mustActionEventName  原执行的事件组名称
	 * @param aftreActionEventName 原事件组执行成功后执行的事件组名称
	 * @return 事件组执行结果集
	 * @throws ParamException 原事件组名称未指定或事件组名称不存在时抛出的异常
	 */
	public List<Optional<Object>> successAction(String mustActionEventName, String aftreActionEventName) {
		return ifSuccessOrElseAction(mustActionEventName, aftreActionEventName, null);
	}

	/**
	 * 用于执行指定的事件组失败后，再执行另一个指定的事件组，若另一事件组名称为空 或为null时，则不执行方法也不存储结果
	 * 
	 * @param mustActionEventName  原执行的事件组名称
	 * @param aftreActionEventName 原事件组执行失败后执行的事件组名称
	 * @return 事件组执行结果集
	 * @throws ParamException 原事件组名称未指定或事件组名称不存在时抛出的异常
	 */
	public List<Optional<Object>> failAction(String mustActionEventName, String aftreActionEventName) {
		return ifSuccessOrElseAction(mustActionEventName, null, aftreActionEventName);
	}

	/**
	 * 用于按照顺序执行事件。
	 * <p>
	 * <b>注意：</b>
	 * <ol>
	 * <li>若在{@link #setExceptionBreak(boolean)}方法中设置false时，则当事件运行失败后，其相应的执行结果存储为{@link Optional#empty()}，并继续运行下一个事件组</li>
	 * <li>设置的是否中断执行，只控制执行方法中产生的异常，若因方法名称不存在而抛出异常时，则同样会中断运行</li>
	 * </ol>
	 * </p>
	 * 
	 * @param eventNames 执行方法名称组
	 * @return 方法执行结果集合
	 * @throws ParamException 原事件组名称未指定或事件组名称不存在时抛出的异常
	 */
	public List<Optional<Object>> orderlyAction(String... eventNames) {
		List<Optional<Object>> resultList = new ArrayList<>();
		Arrays.stream(Optional.ofNullable(eventNames).filter(names -> names.length != 0)
				.orElseThrow(() -> new ParamException("未指定事件名称组"))).map(this::getContinueResult)
				.forEach(resultList::add);

		return resultList;
	}

	/**
	 * 用于按照顺序执行事件，并将每条事件的结果进行判断，若判断不通过，则终止运行。
	 * 
	 * @param predicate  继续执行条件，其参数为上一个事件组执行的结果
	 * @param eventNames 执行方法名称组
	 * @return 方法执行结果集合
	 * @throws ParamException 原事件组名称未指定或事件组名称不存在时抛出的异常
	 * @see #orderlyAction(String...)
	 */
	public List<Optional<Object>> orderlyAction(Predicate<Optional<Object>> predicate, String... eventNames) {
		List<Optional<Object>> resultList = new ArrayList<>();
		for (String name : eventNames) {
			// 获取相应的执行结果
			Optional<Object> result = getContinueResult(name);
			// 若结果不符合筛选条件，则结束循环
			if (!predicate.test(result)) {
				break;
			}

			resultList.add(result);
		}

		return resultList;
	}
	
	/**
	 * 用于循环执行一组事件组，并将每个结果进行判断，若判断不通过，则终止循环，并返回最后一次事件组的执行结果
	 * <p>
	 * <b>注意：</b>无论条件如何设置，其事件组都会执行一次
	 * </p>
	 * @param predicate 继续执行条件，其参数为上一次事件组执行的一组结果
	 * @param eventNames 执行方法名称组
	 * @return 最后一次循环方法执行结果集合
	 * @throws ParamException 原事件组名称未指定或事件组名称不存在时抛出的异常
	 * @see #orderlyAction(String...)
	 */
	public List<Optional<Object>> whileAction(Predicate<List<Optional<Object>>> predicate, String... eventNames) {
		List<Optional<Object>> resultList = new ArrayList<>();
		do {
			resultList = orderlyAction(eventNames);
		} while (predicate.test(resultList));
		
		return resultList;
	}

	/**
	 * 用于判断事件组名称是否存在
	 * 
	 * @param name 事件组名称
	 * @return 事件组名称
	 * @throws ParamException 原事件组名称未指定或事件组名称不存在时抛出的异常
	 */
	private String judgeParam(String name) {
		return Optional.ofNullable(name).filter(eventMap::containsKey)
				.orElseThrow(() -> new ParamException("未指定执行名称或名称不存在：" + name));
	}

	/**
	 * 集成在持续运行中相应事件组的执行结果
	 * 
	 * @param name 事件组名称
	 * @return 相应事件组的执行结果
	 */
	private Optional<Object> getContinueResult(String name) {
		// 执行名称判断方法，若方法名称不存在，则抛出异常
		judgeParam(name);
		// 执行方法，若方法抛出异常，则根据是否中断运行，来给出相应的结果
		try {
			return action(name);
		} catch (Exception e) {
			if (isExceptionBreak) {
				throw e;
			} else {
				return Optional.empty();
			}
		}
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
		 * 
		 * @return 方法执行出参
		 */
		public Optional<Object> run() {
			if (arg.isPresent()) {
				return Optional.ofNullable(eventFunction.action(arg));
			} else {
				eventFunction.action();
				return Optional.empty();
			}
		}
	}

	/**
	 * <p>
	 * <b>文件名：</b>GroupEvent.java
	 * </p>
	 * <p>
	 * <b>用途：</b> 为方便存储，故定义一个父层接口，并定义事件组中带参与不带参的执行方法，由相应的子接口默认实现其中一个，
	 * 使子接口变为函数式接口，方便添加事件时调用
	 * </p>
	 * <p>
	 * <b>编码时间：</b>2020年12月30日上午7:59:58
	 * </p>
	 * <p>
	 * <b>修改时间：</b>2020年12月30日上午7:59:58
	 * </p>
	 * 
	 * @author 彭宇琦
	 * @version Ver1.0
	 * @since JDK 1.8
	 *
	 */
	public interface GroupEventFunction {
		void action();

		Object action(Optional<Object> arg);
	}

	/**
	 * <p>
	 * <b>文件名：</b>GroupEvent.java
	 * </p>
	 * <p>
	 * <b>用途：</b> 继承{@link GroupEventFunction}接口，默认实现带参执行方法，保留不带参数的执行方法，使其变为函数式接口
	 * </p>
	 * <p>
	 * <b>编码时间：</b>2020年12月30日上午8:01:37
	 * </p>
	 * <p>
	 * <b>修改时间：</b>2020年12月30日上午8:01:37
	 * </p>
	 * 
	 * @author 彭宇琦
	 * @version Ver1.0
	 * @since JDK 1.8
	 *
	 */
	public interface NoParamGroupEventFunction extends GroupEventFunction {
		default Object action(Optional<Object> arg) {
			return null;
		}
	}

	/**
	 * <p>
	 * <b>文件名：</b>GroupEvent.java
	 * </p>
	 * <p>
	 * <b>用途：</b> 继承{@link GroupEventFunction}接口，默认实现不带参执行方法，保留带参数的执行方法，使其变为函数式接口
	 * </p>
	 * <p>
	 * <b>编码时间：</b>2020年12月30日上午8:03:07
	 * </p>
	 * <p>
	 * <b>修改时间：</b>2020年12月30日上午8:03:07
	 * </p>
	 * 
	 * @author 彭宇琦
	 * @version Ver1.0
	 * @since JDK 1.8
	 *
	 */
	public interface HasParamGroupEventFunction extends GroupEventFunction {
		default void action() {
			return;
		}
	}

	/**
	 * <p>
	 * <b>文件名：</b>GroupEvent.java
	 * </p>
	 * <p>
	 * <b>用途：</b> 定义参数错误异常
	 * </p>
	 * <p>
	 * <b>编码时间：</b>2020年12月30日下午12:36:37
	 * </p>
	 * <p>
	 * <b>修改时间：</b>2020年12月30日下午12:36:37
	 * </p>
	 * 
	 * @author 彭宇琦
	 * @version Ver1.0
	 * @since JDK 1.8
	 *
	 */
	public class ParamException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public ParamException(String message) {
			super(message);
		}
	}
}
