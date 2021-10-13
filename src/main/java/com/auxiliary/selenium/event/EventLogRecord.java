package com.auxiliary.selenium.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EventLogRecord {
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
