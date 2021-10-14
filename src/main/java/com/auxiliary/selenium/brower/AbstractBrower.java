package com.auxiliary.selenium.brower;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.openqa.selenium.WebDriver;

import com.alibaba.fastjson.JSONObject;

/**
 * <p>
 * <b>文件名：</b>AbstractBrower.java
 * </p>
 * <p>
 * <b>用途：</b> 定义浏览器的基本方法，以便于操作类获取到相应的WebDriver对象
 * </p>
 * <p>
 * <b>编码时间：</b>2021年4月2日上午8:19:27
 * </p>
 * <p>
 * <b>修改时间：</b>2021年4月2日上午8:19:27
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public abstract class AbstractBrower {
	/**
	 * 用于记录日志
	 */
	private ActionLogRecord log = new ActionLogRecord();
	
	/**
	 * 用于存储获取到的浏览器对象
	 */
	protected WebDriver driver = null;

	/**
	 * 用于存储浏览器启动时的信息
	 */
	protected JSONObject informationJson = new JSONObject();

	/**
	 * 用于启动浏览器，并返回WebDriver对象
	 * 
	 * @return 指向浏览器的WebDriver对象
	 */
	public abstract WebDriver getDriver();

	/**
	 * 用于以json字符串的形式返回浏览器的信息
	 * 
	 * @return 浏览器的信息
	 */
	public abstract String getAllInformation();

	/**
	 * 用于关闭浏览器
	 */
	public void closeBrower() {
		if (driver != null) {
			// 关闭浏览器
			driver.quit();
			// 将driver指定为null
			driver = null;
		}
	}

	/**
	 * 用于启动浏览器
	 */
	protected abstract void openBrower();
	
	/**
	 * 用于返回日志记录类对象
	 * @return 日志记录类对象
	 */
	public ActionLogRecord getLogRecord() {
		return log;
	}
	
	/**
	 * <p><b>文件名：</b>AbstractBrower.java</p>
	 * <p><b>用途：</b>用于记录自动化执行过程中产生的日志信息</p>
	 * <p><b>编码时间：</b>2021年10月14日 上午10:37:34</p>
	 * <p><b>修改时间：</b>2021年10月14日 上午10:37:34</p>
	 * @author 彭宇琦
	 * @version Ver1.0
	 * @since JDK 1.8
	 */
	public class ActionLogRecord {
		/**
		 * 默认的符号
		 */
		public final static String DEFAULT_SIGN = "* ";

		/**
		 * 存储记录的日志信息
		 */
		private List<String> logList = new ArrayList<String>();
		
		/**
		 * 用于记录日志
		 * 
		 * @param logText 日志信息
		 */
		public void recordLog(String logText) {
			Optional.ofNullable(logText).filter(t -> !t.isEmpty()).ifPresent(logList::add);
		}

		/**
		 * 用于返回无符号的日志集合
		 * 
		 * @return 无符号的日志集合
		 */
		public List<String> getNoSignLogList() {
			return new ArrayList<String>(logList);
		}

		/**
		 * 用于返回无符号的日志数组
		 * 
		 * @return 无符号的日志数组
		 */
		public String[] getNoSignLogArray() {
			return listToArray(logList);
		}

		/**
		 * 用于返回包含数字编号的日志集合
		 * <p>
		 * 数字编号的格式为：数字.&nbsp;日志。例如：1.&nbsp;点击XX按钮
		 * </p>
		 * 
		 * @return 包含数字编号的日志集合
		 */
		public List<String> getSerialNumberLogList() {
			// 判空
			if (logList.isEmpty()) {
				return new ArrayList<String>();
			}

			// 若日志集合不为空，则在日志前面添加数字编号
			return IntStream.range(1, logList.size() + 1)
					.mapToObj(index -> String.format("%d. %s", index, logList.get(index - 1))).collect(Collectors.toList());
		}

		/**
		 * 用于返回包含数字编号的日志数组
		 * <p>
		 * 数字编号的格式为：数字.&nbsp;日志。例如：1.&nbsp;点击XX按钮
		 * </p>
		 * 
		 * @return 包含数字编号的日志数组
		 */
		public String[] getSerialNumberLogArray() {
			return listToArray(getSerialNumberLogList());
		}

		/**
		 * 用于返回包含自定义符号的日志集合，自定义的符号在日志前面添加
		 * <p>
		 * 若自定义的符号为null或者为空，则采用默认的符号“* ”
		 * </p>
		 * 
		 * @param sign 自定义的符号
		 * @return 包含自定义符号的日志集合
		 */
		public List<String> getSignLogList(String sign) {
			// 判空
			if (logList.isEmpty()) {
				return new ArrayList<String>();
			}

			// 若sign为null或者为空，则采用默认的符号
			return logList.stream().map(t -> Optional.ofNullable(sign).filter(s -> !s.isEmpty()).orElse(DEFAULT_SIGN) + t)
					.collect(Collectors.toList());
		}

		/**
		 * 用于返回包含自定义符号的日志数组，自定义的符号在日志前面添加
		 * 
		 * @param sign 自定义的符号
		 * @return 包含自定义符号的日志数组
		 */
		public String[] getSignLogArray(String sign) {
			return listToArray(getSignLogList(sign));
		}

		/**
		 * 用于将集合的内容，转换为数组进行返回
		 * 
		 * @param logList 日志集合
		 * @return 日志数组
		 */
		private String[] listToArray(List<String> logList) {
			// 则向数组中添加数据
			String[] logs = new String[logList.size()];
			for (int index = 0; index < logList.size(); index++) {
				logs[index] = logList.get(index);
			}

			return logs;
		}
	}
}
